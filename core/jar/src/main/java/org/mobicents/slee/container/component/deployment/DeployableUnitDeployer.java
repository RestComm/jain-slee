/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component.deployment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPath;
import javassist.ClassPool;

import javax.slee.management.AlreadyDeployedException;
import javax.slee.management.DeploymentException;

import org.jboss.logging.Logger;
import org.jboss.mx.loading.RepositoryClassLoader;
import org.jboss.mx.loading.UnifiedClassLoader;
import org.jboss.mx.loading.UnifiedLoaderRepository3;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.management.xml.XMLConstants;
import org.mobicents.slee.container.management.xml.XMLException;
import org.mobicents.slee.container.management.xml.XMLUtils;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An instance of this class is created for every deployable unit that is
 * installed in the slee. Use of this class goes through the static
 * <code>deploy</code>. It then creates a UnitDeployer instance that
 * corresponds the jar file passed to <code>deploy</code>.
 * 
 * @author Emil Ivov (original)
 * @author M. Ranganathan (hacks)
 * @author Ivelin Ivanov
 */
public class DeployableUnitDeployer {
    private JarFile unitJarFile = null;

    private File tempDUJarsDeploymentDirectory = null;

    private SleeContainer componentContainer = null;

    private DeployableUnitIDImpl deployableUnitID;

    private static int deployableUnitCounter;

    private File tempClassDeploymentDir;

    private URL sourceUrl;

    private static Logger logger;
    
    private ClassPath classPath = null;

    /**
     * The unique class loader associated with the given DU This should not be
     * static else you will run into trouble when deploying multiple units.
     */
    private RepositoryClassLoader classLoader;

    /**
     * shared Javassist pool for all DUs
     */
    private ClassPool classPool = ConcreteClassGeneratorUtils.createClassPool();

	private Collection simpleComponentsJarFiles;

	private Collection complexComponentsJarFiles;

	private HashSet extractedJars = new HashSet();

    static {
        logger = Logger.getLogger(DeployableUnitDeployer.class);
    }

    DeployableUnitDeployer(JarFile unitJarFile, File tempDUJarsDeploymentDirectory, URL sourceUrl) {
        this.unitJarFile = unitJarFile;
    	this.componentContainer = SleeContainer.lookupFromJndi();
        this.tempDUJarsDeploymentDirectory = tempDUJarsDeploymentDirectory;
        // create dir to unpack du jars
        this.tempClassDeploymentDir = new File(tempDUJarsDeploymentDirectory.getAbsolutePath()+"-unpackaged");
        if (!tempClassDeploymentDir.exists())
        	tempClassDeploymentDir.mkdirs();
        else
        	logger.warn("deploying du in a dir that already exists");      
        this.sourceUrl = sourceUrl;
    }

    /**
     * Set the descriptor.
     */

    private void setDescriptor(DeployableUnitDescriptorImpl descriptor) {
        this.deployableUnitID = new DeployableUnitIDImpl(
                deployableUnitCounter++);
        this.deployableUnitID.setDescriptor(descriptor);
        descriptor.setDeployableUnit(deployableUnitID);
        descriptor.setTmpDUJarsDirectory(this.tempDUJarsDeploymentDirectory);
        descriptor.setTmpDeploymentDirectory(this.tempClassDeploymentDir);

    }

    /**
     * Analyses the specified jar file, extracts and deploys the components that
     * it contains.
     * 
     * @param deployableUnitJar
     *            The Deployable Unit jar file that we want to deploy.
     * @param tempDUJarsDeploymentDirectory
     *            The location where the SLEE stores deployed files.
     * @param container
     *            the container where we should install component contents
     * @param classpathDirectory
     *            the directory where we should unzip component jars.
     * @throws DeploymentException
     *             if deployment fails.
     */
    public static synchronized DeployableUnitIDImpl deploy(URL sourceUrl,File deployableUnitJarFile, File deploymentDirectory,SleeContainer container) throws DeploymentException {
        if(logger.isDebugEnabled()){
            logger.debug("jarFile = " + deployableUnitJarFile);
        }
        JarFile deployableUnitJar;
        try {
            deployableUnitJar = new JarFile(deployableUnitJarFile);
        } catch (IOException e) {
            throw new DeploymentException(
                    "Failed to open DU file as JAR file: "
                            + deployableUnitJarFile, e);
        }
        
        if (sourceUrl == null || deployableUnitJar == null
                || deploymentDirectory == null || container == null)
            throw new NullPointerException("null arg!");

        //init the deployer.
        DeployableUnitDeployer deployer = new DeployableUnitDeployer(deployableUnitJar,deploymentDirectory,sourceUrl);
       
        //      Get te DU deployment Descriptor;
        deployer.setDescriptor(new DeployableUnitDescriptorImpl(sourceUrl
                .toString(), new Date()));

        // set UnitID properties to uniquely identify this DU in SLEE
        deployer.deployableUnitID.setSourceURL(sourceUrl);
        deployer.deployableUnitID.setSourceURI(deployableUnitJarFile.toURI());
        deployer.deployableUnitID.setDUDeployer(deployer);

        deployer.deployUnitContent();

        container.getDeployableUnitManagement().addDeployableUnit(deployer.deployableUnitID.getDescriptor());
        return deployer.deployableUnitID;

    }

    /**
     * @return the source Url where the DU was read originally
     */
    public URL getSourceURL() {
        return sourceUrl;
    }

    /**
     * Returns the jar file that this unit deployer has been created for.
     * 
     * @return the JarFile that contains the deployable unit that this instance
     *         is to take care of.
     */
    JarFile getUnitJarFile() {
        return unitJarFile;
    }

    /**
     * Returns the SLEE directory where deployable units are stored.
     * 
     * @return the directory where DU jar files are stored.
     */
    public File getTempDUJarsDeploymentDirectory() {
        return tempDUJarsDeploymentDirectory;
    }

    /**
     * Returns a reference to the Container where services should be installed.
     * 
     * @return the container where we shall install services
     */
    public SleeContainer getComponentContainer() {
        return componentContainer;
    }

    /**
     * Returns the directory where component contents should be stored. The
     * directory should be part of the classpath.
     * 
     * @return A File instance,
     */
    public File getClasspathDirectory() {
        return tempClassDeploymentDir/*
                                      * FIXME: this method should be merged with
                                      * getTempDeploymentDir() - used to return
                                      * classpathDirectory ->
                                      * server/all/deploy/mobicents.sar
                                      */;
    }

    /**
     * Extract and deploy all components contained by the unit jar file.
     * 
     * @return the deployable unit id for the deployed component
     * @throws Exception
     *             if deployment fails for a reason.
     */
    private void deployUnitContent() throws DeploymentException {
        boolean b = false;
        SleeTransactionManager transactionManager = SleeContainer.getTransactionManager(); 
        try {
            b = transactionManager.requireTransaction();

        	exploreDULib();

            parseDUDescriptor();
            
            prepareDUJars();

            // unpack all jar files to allow class visibility across dependent
            // DU components
            prepareDeploy(simpleComponentsJarFiles);
            prepareDeploy(complexComponentsJarFiles);
                        
            deferDeployForUnresolvedDependancies();
            
            loadDeployment(simpleComponentsJarFiles, complexComponentsJarFiles, extractedJars);

            retryDeployForResolvedDependancies();
            
        } catch (DeploymentException ex) {
            try {
                transactionManager.setRollbackOnly();
            } catch (Exception e) {
                throw new RuntimeException("Pb with Slee Transaction Manager",
                        e);
            }
            throw ex;

        } finally {
            try {
                if (b) transactionManager.commit();
            } catch (Exception e) {
                throw new RuntimeException("Pb with Slee Transaction Manager",
                        e);
            }
        }


    }
    
    /**
     * Depending on the deployment oder, sometimes deployment units that have dependencies 
     * are provided for deployment before the dependencies have been deployed. For example when
     * an SBB component is attempted to be deployed before the Event Types that it depends have been deployed.
     * In this case the SLEE spec requires that dependency exceptions are thrown.
     * 
     * Mobicents can be configured to defer deployment until a later point when the dependencies are resolved
     * instead of throwing exceptions. This behaviour is desirable for auto-deployment as well as deployment
     * of more complex environments where multiple sources contribute components to a completely operational system.
     * 
     * @TODO - How is the dep tracking switch turned on/off?
     *
     */
	private void deferDeployForUnresolvedDependancies() {
		
	}

    /**
     * After a new DU is deployed and deployment dependency tracking is enabled,
     * Mobicents will revisit the list of waiting components to see if their dependencies
     * have been resolved. When that is the case the waiting DUs will be deployed.
     *
     */
    private void retryDeployForResolvedDependancies() {
		// see if there are waiting components with an empty set of unresolved dependencies.
    	// If there are any, try to deploy them
	}

	/**
     * Given the list of JARS listed in a DU, iterate and prepare each for loading into runtime.
     * @throws DeploymentException 
     *
     */
	private void prepareDUJars() throws DeploymentException {
        // Parse the event-jar entries first and the other ones later.
        ArrayList jarNames = new ArrayList();//used for assserting unique
        // names
        Iterator iter = getDescriptor().getJarNodes().iterator();
        complexComponentsJarFiles = new LinkedList();
        simpleComponentsJarFiles = new LinkedList();
        while (iter.hasNext()) {
            try {
                String jarName = XMLUtils
                        .getElementTextValue((Element) iter.next());
                if (jarName.length() == 0
                        || !jarName.toLowerCase().endsWith("jar")) {
                    throw new DeploymentException(jarName
                            + " is not a valid jar file name.");
                }
                if (jarNames.contains(jarName)) {
                    throw new DeploymentException("Encountered " + jarName
                            + " more than once.");
                }

                //extract the jar entry

                File extractedJarFile = DeploymentManager.extractFile(
                        jarName, unitJarFile,
                        getTempDUJarsDeploymentDirectory());
                if(extractedJarFile == null) {
                    throw new DeploymentException("Error Extracting jar file:  " + jarName + " " + unitJarFile);
                   
                }
                JarFile extractedJar = new JarFile(extractedJarFile);
                extractedJars.add(extractedJarFile);
                getDescriptor().addJar(jarName);
                jarNames.add(jarName);
                //take care of the deployment of the componentitself
                AbstractComponentDeployer deployer = createComponentDeployer(extractedJar);
                deployer.initDeployer(extractedJar,
                        getComponentContainer(),
                        getTempClassDeploymentDir(), this
                /* TODO was: getClasspathDirectory() */);

                if (deployer instanceof EventTypeComponentDeployer ||
                    deployer instanceof ProfileSpecComponentDeployer ) {
                    simpleComponentsJarFiles.add(deployer);
                } else {
                    complexComponentsJarFiles.add(deployer);
                }

            } catch (AlreadyDeployedException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new DeploymentException(ex.getMessage(),ex);
            }
        }
        //Create DeployableUnitJarEntries for all sbb.jars in the
        // deployable
        // unit
	}

	private void parseDUDescriptor() throws DeploymentException {
    	// Note -- once you extact a file from a jar archive, it is
        // not in the jar archive any longer!! This is totally unexpected.
        JarEntry duXmlEntry = unitJarFile
                .getJarEntry("META-INF/deployable-unit.xml");
        if (duXmlEntry == null) {
            throw new DeploymentException(
                    "No DeployableUniDeploymentDescriptor descriptor "
                            + "(META-INF/deployable-unit.xml) was found in deployable unit"
                            + unitJarFile.getName());
        }

        //Parse the descriptor
        Document doc = null;

        try {
            //FIXME -- turn off validation for now.
            doc = XMLUtils.parseDocument(unitJarFile
                    .getInputStream(duXmlEntry), false);

        } catch (IOException ex) {
            throw new DeploymentException("Failed to extract the DU depl "
                    + "descriptor from " + unitJarFile.getName());
        }

        Element duNode = doc.getDocumentElement();
        DeployableUnitDescriptorImpl deployableUnitDescriptor = 
        	getDescriptor();

        try {
            String description = XMLUtils.getElementTextValue(duNode,
                    XMLConstants.DESCRIPTION_ND);
            if (description != null)
                deployableUnitDescriptor.setDescription(description);
        } catch (XMLException ex) {
            throw new DeploymentException(ex.getMessage());
        }

        //Get a list of the jars and services in the deployable unit.
        List jarNodes = XMLUtils.getAllChildElements(duNode,
                XMLConstants.JAR_ND);
        deployableUnitDescriptor.setJarNodes(jarNodes);
        List serviceNodes = XMLUtils.getAllChildElements(duNode,
                XMLConstants.SERVICE_XML_ND);
        deployableUnitDescriptor.setServiceNodes(serviceNodes);
        if (jarNodes.size() == 0 && serviceNodes.size() == 0) {
            throw new DeploymentException("The " + unitJarFile.getName()
                    + " deployable unit contains no jars or services");
        }
	}

	private DeployableUnitDescriptorImpl getDescriptor() {
		return this.deployableUnitID.getDescriptor();
	}

	private void exploreDULib() throws DeploymentException {
		// explore the library directory
		// TODO: The proper way to explore the library directories is to follow the JSLEE 1.1
		// spec for adding the proper library descriptor
      
		Enumeration entries = unitJarFile.entries();
     
		try {
      
			for (JarEntry entry = (JarEntry) entries.nextElement(); 
				entries.hasMoreElements(); ) 
			{
				entry = (JarEntry) entries.nextElement();	

				if (entry.getName().indexOf("library")!=-1 &&
						entry.getName().indexOf("jar")!=-1 && 
						!entry.isDirectory()) 
				{
					File f = DeploymentManager.extractFile(entry.getName(),unitJarFile,getClasspathDirectory());
					DeploymentManager.extractJar(new JarFile(f), getClasspathDirectory());

				}
			}
		} catch (IOException ex) {
			throw new DeploymentException(ex.getMessage(), ex);
		}
	}

    /**
     * 
     * Provided that all DU classes are extracted in a local dir,
     * proceed with loading them up and registering with SLEE runtime
     * @throws DeploymentException
     * @throws Exception
     * 
     */
    private void loadDeployment(Collection simpleCompJarFiles, Collection complexCompJarFiles,
            HashSet extractedJars) throws DeploymentException  {
        // Deploy the unit with a unique designated classloader.
        // There's one classloader per DU and one per SBB thats derived from
        // this
        // classloader.
        ClassLoader oldClassLoader = Thread.currentThread()
                .getContextClassLoader();

        this.classLoader = createDUClassLoader();
        //this.classPool = ConcreteClassGeneratorUtils.createClassPool();
        try {
        	
            classPath = classPool.appendClassPath(this.getTempClassDeploymentDir().getAbsolutePath());
            Thread.currentThread().setContextClassLoader(this.classLoader);

            // events are deployed first
            Iterator iter = simpleCompJarFiles.iterator();
            while (iter.hasNext()) {
                //take care of the deployment of the componentitself
                AbstractComponentDeployer deployer = (AbstractComponentDeployer) iter
                        .next();
                deployer.deployAndInstall(deployableUnitID);
            }
                
            loadDeployedComponents(complexCompJarFiles, extractedJars);
        
        } catch (AlreadyDeployedException ex) {
            throw ex;
        } catch (DeploymentException ex) {
            throw ex;
        } catch (Exception ex) {
            classPool.removeClassPath(classPath);
            throw new DeploymentException(ex.getMessage(), ex);
        } finally {
            Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
    }

    /**
     * Iterate over deployed components and register them with SLEE runtime
     * @throws DeploymentException
     */
    private void loadDeployedComponents(Collection jarFiles, HashSet extractedJars) throws DeploymentException {
        Iterator iter = jarFiles.iterator();
        while (iter.hasNext()) {
            try {
                //take care of the deployment of the componentitself
                AbstractComponentDeployer deployer = (AbstractComponentDeployer) iter
                        .next();

                deployer.deployAndInstall(deployableUnitID);
                /** @todo keep a reference to the newly created entry */
            } catch (AlreadyDeployedException ex) {
                ex.printStackTrace();
                try {
                	/**** Just undeploy, don't doRemove
                    this.componentContainer
                            .doRemove(deployableUnitID.getDescriptor()); 
                	****/
					this.undeploy();
                } catch (Exception ex1) {
                    ex1.printStackTrace();
                }
                throw ex;
            } catch (DeploymentException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new DeploymentException(ex.getMessage(), ex);
            }
        }

        //Create DeployableUnitServiceEntries for all services in the
        // DU.
        ArrayList serviceNames = new ArrayList();//used for asserting
        // unique
        // names
        iter = getDescriptor().getServiceNodes().iterator();
        while (iter.hasNext()) {
            try {
                String serviceXmlName = XMLUtils
                        .getElementTextValue((Element) iter.next());
                if (serviceXmlName.length() == 0
                        || !serviceXmlName.toLowerCase().endsWith("xml")) {
                    throw new DeploymentException(serviceXmlName
                            + " is not a valid service-xml file name.");
                }
                if (serviceNames.contains(serviceXmlName)) {
                    throw new DeploymentException("Encountered "
                            + serviceXmlName + " more than once.");
                }
                /** @todo keep a reference to the newly created entry */

                //extract the xml entry
                File serviceXmlFile = DeploymentManager.extractFile(
                        serviceXmlName, unitJarFile,
                        getTempDUJarsDeploymentDirectory());

                ServiceDeployer deployer = new ServiceDeployer();
                deployer.initDeployer(serviceXmlFile,
                        getComponentContainer());
                deployer.deployAndInstall(deployableUnitID);

                serviceXmlFile.delete();

            } catch (Exception ex) {
                throw new DeploymentException(ex.getMessage(), ex);
            }
        }

        Iterator it = extractedJars.iterator();
        while (it.hasNext()) {
            File fname = (File) it.next();
            logger.debug("trying to delete file " + fname.getName());

            fname.delete();
        }
    }

    /**
     * 
     * Unjars a list of jar files in the DU's deployment dir
     * 
     * @param compJarFiles
     */
    private void prepareDeploy(Collection compJarFiles)
            throws DeploymentException {
        DeployableUnitDescriptorImpl deployableUnitDescriptor = deployableUnitID
                .getDescriptor();

        Iterator iter = compJarFiles.iterator();
        while (iter.hasNext()) {
            try {
                //take care of the deployment of the componentitself
                AbstractComponentDeployer deployer = (AbstractComponentDeployer) iter
                        .next();

                deployer.prepareDeploy(deployableUnitID);
                /**
                 * (Ivelin) what is this about? Either refer to
                 * forum discussion or delete. --> TODO: keep a reference
                 * to the newly created entry
                 */
            } catch (Exception ex) {
                throw new DeploymentException(ex.getMessage(), ex);
            }
        }
    }

    /**
     * @return the ClassLoader that will be associated with the DU. It will load
     *         the classes in the DU and will be also used for JNDI objects
     *         under ENC - java:comp/env
     * @throws DeploymentException
     */
    protected RepositoryClassLoader createDUClassLoader()
            throws DeploymentException {
        try {
            UnifiedClassLoader ucl = (UnifiedClassLoader) Thread
                    .currentThread().getContextClassLoader();
            UnifiedLoaderRepository3 lr = (UnifiedLoaderRepository3) ucl
                    .getLoaderRepository();
            RepositoryClassLoader cl = lr.newClassLoader(
                    getTempClassDeploymentDir().toURL(), new File(
                            getUnitJarFile().getName()).toURL(), true);
            // make sure to call ucl.unregister() on undeploy()
            return cl;
        } catch (MalformedURLException e1) {
            throw new DeploymentException(
                    "Bad ULR for tempClassDeploymentDir: "
                            + getTempClassDeploymentDir());
        } catch (Exception e) {
            throw new DeploymentException(
                    "Failed Creating ClassLoader for tempClassDeploymentDir: "
                            + getTempClassDeploymentDir());
        }
    }

    /**
     * Extracts the file with name <code>fileName</code> out of the
     * <code>containingJar</code> archive and stores it in <code>dstDir</code>.
     * 
     * @param fileName
     *            the name of the file to extract.
     * @param containingJar
     *            the archive where to extract it from.
     * @param dstDir
     *            the location where the extracted file should be stored.
     * @throws IOException
     *             if reading the archive or storing the extracted file fails
     * @return a <code>java.io.File</code> reference to the extracted file.
     */
    private static JarFile extractJar(String fileName, JarFile containingJar,
            File dstDir) throws IOException {
        return new JarFile(DeploymentManager.extractFile(fileName,
                containingJar, dstDir));

    }

    /**
     * Verifies what kind of a deployment descriptor does the component jar file
     * contain, constructs the corresponding ComponentDeployer accordingly,
     * initilizes it and returns it ready for deployment or throws an exception
     * if problems occur.
     * 
     * @param componentJarFile
     *            the jar file that contains the component.
     * @throws DeploymentException
     *             if an exception occurs while deploying the component
     * @return a ComponentDeployer ready fo <code>deploy</code> the contents
     *         of the specified jar file.
     */
    private AbstractComponentDeployer createComponentDeployer(
            JarFile componentJarFile) throws DeploymentException {

        java.util.jar.JarEntry descriptorXML = null;
        AbstractComponentDeployer deployer = null;

        //Determine whether the type of this instance is an sbb, event, RA type
        // etc.
        if ((descriptorXML = componentJarFile
                .getJarEntry("META-INF/sbb-jar.xml")) != null) {

            deployer = new SbbComponentDeployer(deployableUnitID, descriptorXML);

        } else if ((descriptorXML = componentJarFile
                .getJarEntry("META-INF/profile-spec-jar.xml")) != null) {

            deployer = new ProfileSpecComponentDeployer(deployableUnitID,
                    descriptorXML);

        } else if ((descriptorXML = componentJarFile
                .getJarEntry("META-INF/event-jar.xml")) != null) {

            deployer = new EventTypeComponentDeployer(deployableUnitID,
                    descriptorXML);

        } else if ((descriptorXML = componentJarFile
                .getJarEntry("META-INF/resource-adaptor-type-jar.xml")) != null) {

            deployer = new ResourceAdaptorTypeComponentDeployer(
                    deployableUnitID, descriptorXML);

        } else if ((descriptorXML = componentJarFile
                .getJarEntry("META-INF/resource-adaptor-jar.xml")) != null) {

            deployer = new ResourceAdaptorComponentDeployer(deployableUnitID,
                    descriptorXML);

        } else {
            throw new DeploymentException(
                    "No Deployment Descriptor found in the "
                            + componentJarFile.getName()
                            + " entry of a deployable unit.");
        }

        return deployer;
    }

    /**
     * 
     * @return Returns the tempClassDeploymentDir.
     */
    public File getTempClassDeploymentDir() {
        return tempClassDeploymentDir;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public ClassPool getClassPool() {
        return classPool;
    }

    /**
     * Undeploy sequence for the DU that this deployer is responsible for
     */
    public void undeploy() {
        // remove association with loader repository
        
        if(logger.isDebugEnabled()) {
        	logger.debug("undeploy() " + this);
        }
        
        if ( classLoader != null )
            classLoader.unregister();
        
        // javassist cleanup
        // Guard condition needed to prevent bombing on partial
        // deploy undeployment
        if ( classPool != null && classPath != null )
              classPool.removeClassPath(classPath);

        tempClassDeploymentDir.delete();
        tempDUJarsDeploymentDirectory.delete();
    }

}