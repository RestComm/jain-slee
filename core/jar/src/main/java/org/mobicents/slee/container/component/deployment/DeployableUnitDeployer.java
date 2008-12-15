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
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.LoaderClassPath;

import javax.slee.management.DeploymentException;

import org.apache.log4j.Logger;
import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.ParentPolicy;
import org.jboss.classloading.spi.metadata.ExportAll;
import org.jboss.classloading.spi.vfs.policy.VFSClassLoaderPolicy;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.management.xml.XMLConstants;
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
 * @author Eduardo Martins
 */
public class DeployableUnitDeployer {
    
	private static final Logger logger = Logger.getLogger(DeployableUnitDeployer.class);
	
	/**
	 * deployable unit id generator
	 */
	private static AtomicInteger deployableUnitCounter = new AtomicInteger(0);
	
	/**
     * Javassist pool to generate components
     */
    private ClassPool classPool;
	    
    /**
     * the deployable unit jar file
     */
	private JarFile unitJarFile;

	private SleeContainer sleeContainer;

    /**
     * the DU id
     */
    private DeployableUnitIDImpl deployableUnitID;  

    /**
     * the dir where all classes for this du will be extracted
     */
    private File tempClassDeploymentDir;

    /**
	 * a temp dir where du jars will be extracted
	 */
    private File tempDUJarsDeploymentDirectory;

    /**
     * The unique class loader associated with the given DU This should not be
     * static else you will run into trouble when deploying multiple units.
     */
    private ClassLoader classLoader;

    /**
     * the class loader policy that created the classloader for this du 
     */
    private VFSClassLoaderPolicy classLoaderPolicy;

    /**
     * private constructor
     * 
     * @param unitJarFile
     * @param tempDUJarsDeploymentDirectory
     * @param sleeContainer
     */
	private DeployableUnitDeployer(JarFile unitJarFile, File tempDUJarsDeploymentDirectory, SleeContainer sleeContainer) {
        this.unitJarFile = unitJarFile;
    	this.sleeContainer = sleeContainer;
        this.tempDUJarsDeploymentDirectory = tempDUJarsDeploymentDirectory;
        // create dir to unpack du jars
        this.tempClassDeploymentDir = new File(tempDUJarsDeploymentDirectory.getAbsolutePath()+"-unpackaged");
        if (!tempClassDeploymentDir.exists())
        	tempClassDeploymentDir.mkdirs();
        else
        	logger.warn("deploying du in a dir that already exists");      
    }

    /**
     * Set the descriptor.
     */

    private void setDescriptor(DeployableUnitDescriptorImpl descriptor) {
        this.deployableUnitID = new DeployableUnitIDImpl(
                deployableUnitCounter.getAndIncrement());
        this.deployableUnitID.setDescriptor(descriptor);
        descriptor.setDeployableUnit(deployableUnitID);
        descriptor.setTmpDUJarsDirectory(this.tempDUJarsDeploymentDirectory);
        descriptor.setTmpDeploymentDirectory(this.tempClassDeploymentDir);

    }

    /**
     * Deploys a JAIN SLEE DU.
     * 
     * @param sourceUrl the original URL of the deployable unit jar
     * @param deployableUnitJarFile the deployable unit jar file
     * @param deploymentDirectory the directory where the DU should be deployed
     * @param container the container where the dU will be installed
     * @return
     * @throws DeploymentException
     */
    public static DeployableUnitIDImpl deploy(URL sourceUrl,File deployableUnitJarFile, File deploymentDirectory,SleeContainer container) throws DeploymentException {
        
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
        
        if (deployableUnitJar == null
                || deploymentDirectory == null || container == null)
            throw new NullPointerException("null arg!");

        //init the deployer.
        DeployableUnitDeployer deployer = new DeployableUnitDeployer(deployableUnitJar,deploymentDirectory,container);
       
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
     * Returns the jar file that this unit deployer has been created for.
     * 
     * @return the JarFile that contains the deployable unit that this instance
     *         is to take care of.
     */
    JarFile getUnitJarFile() {
        return unitJarFile;
    }

   /**
     * Extract and deploy all components contained by the unit jar file.
     * 
     * @return the deployable unit id for the deployed component
     * @throws DeploymentException
     *             if deployment fails for a reason.
     */
    private void deployUnitContent() throws DeploymentException {
        
    	ClassLoader oldClassLoader = Thread.currentThread()
        .getContextClassLoader();
        
    	boolean newTx = false;
        boolean rollbackTx = true;
        
        SleeTransactionManager transactionManager = sleeContainer.getTransactionManager();
        
        try {
            
        	newTx = transactionManager.requireTransaction();

            // ----------- extract jars in library dir of the DU jar
        	Enumeration entries = unitJarFile.entries();
        	for (JarEntry entry = (JarEntry) entries.nextElement(); entries.hasMoreElements(); ) {
        		entry = (JarEntry) entries.nextElement();	
        		if (entry.getName().indexOf("library")!=-1 &&
        				entry.getName().indexOf("jar")!=-1 && 
        				!entry.isDirectory()) {
        			// get the lib jar
        			File f = DeploymentManager.extractFile(entry.getName(),unitJarFile,getTempClassDeploymentDir());
        			// extract the lib jar to the temp folder
        			deployableUnitID.getDeployedFiles().addAll(DeploymentManager.extractJar(new JarFile(f), getTempClassDeploymentDir()));					
        		}
        	}

           // ------------ parse du descriptor
        	
            JarEntry duXmlEntry = unitJarFile.getJarEntry("META-INF/deployable-unit.xml");
            if (duXmlEntry == null) {
            	throw new DeploymentException("META-INF/deployable-unit.xml was not found in " + unitJarFile.getName());
            }
            
            Document doc = null;
            try {
            	//FIXME -- turn off validation for now.
            	doc = XMLUtils.parseDocument(unitJarFile
            			.getInputStream(duXmlEntry), false);
            } catch (IOException ex) {
            	throw new DeploymentException("Failed to parse META-INF/deployable-unit.xml from " + unitJarFile.getName());
            }

            Element duNode = doc.getDocumentElement();
            DeployableUnitDescriptorImpl deployableUnitDescriptor = this.deployableUnitID.getDescriptor();

            String description = XMLUtils.getElementTextValue(duNode,
            		XMLConstants.DESCRIPTION_ND);
            if (description != null)
            	deployableUnitDescriptor.setDescription(description);

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
            
            // ------------- unpack all jars
            
            LinkedList<AbstractComponentDeployer> complexComponentsJarFiles = new LinkedList<AbstractComponentDeployer>();
            LinkedList<AbstractComponentDeployer> simpleComponentsJarFiles = new LinkedList<AbstractComponentDeployer>();
            HashSet<File> extractedJars = new HashSet<File>();
        	ArrayList<String> jarNames = new ArrayList<String>();//used for assserting unique
            // names
            for (Iterator it = jarNodes.iterator();it.hasNext();) {
            	String jarName = XMLUtils
            	.getElementTextValue((Element) it.next());
            	if (jarName.length() == 0
            			|| !jarName.toLowerCase().endsWith("jar")) {
            		throw new DeploymentException(jarName
            				+ " is not a valid jar file name.");
            	}
            	if (jarNames.contains(jarName)) {
            		throw new DeploymentException("Encountered " + jarName
            				+ " more than once.");
            	}
            	//extract the jar from the DU to a temp dir
            	File extractedJarFile = DeploymentManager.extractFile(
            			jarName, unitJarFile,
            			tempDUJarsDeploymentDirectory);
            	if(extractedJarFile == null) {
            		throw new DeploymentException("Error Extracting jar file:  " + jarName + " " + unitJarFile);

            	}
            	JarFile extractedJar = new JarFile(extractedJarFile);
            	extractedJars.add(extractedJarFile);
            	deployableUnitDescriptor.addJar(jarName);
            	jarNames.add(jarName);
            	// create deployer for this jar
            	AbstractComponentDeployer deployer = createComponentDeployer(extractedJar);
            	deployer.initDeployer(extractedJar,
            			sleeContainer,
            			getTempClassDeploymentDir(), this);
            	// simple order of components
            	if (deployer instanceof EventTypeComponentDeployer ||
            			deployer instanceof ProfileSpecComponentDeployer ) {
            		simpleComponentsJarFiles.add(deployer);
            	} else {
            		complexComponentsJarFiles.add(deployer);
            	}
            }
            
            // ----------- prepare deploy of components
            for (Iterator<AbstractComponentDeployer> it = simpleComponentsJarFiles.iterator(); it.hasNext();) {               	
            	it.next().prepareDeploy(deployableUnitID);
            }               
            for (Iterator<AbstractComponentDeployer> it = complexComponentsJarFiles.iterator(); it.hasNext();) {
            	it.next().prepareDeploy(deployableUnitID);
            }  
                        
            // ------------ create (and change) classloader and create javassist classpool for generated classes
            
            try {
                // jbossas class loader system
            	ClassLoaderSystem classLoaderSystem = ClassLoaderSystem.getInstance();
            	// create policy pointing to the temp du dir and return its class loader
            	VirtualFile tempClassDeploymentDirVF = VFS.getRoot(getTempClassDeploymentDir().toURL());
            	this.classLoaderPolicy = VFSClassLoaderPolicy.createVFSClassLoaderPolicy(tempClassDeploymentDirVF);
            	classLoaderPolicy.setImportAll(true); // if you want to see other classes in the domain
            	classLoaderPolicy.setBlackListable(false);
            	classLoaderPolicy.setExportAll(ExportAll.NON_EMPTY); // if you want others to see your classes
            	classLoaderPolicy.setCacheable(true);            	
            	if (logger.isDebugEnabled()) {
            		logger.debug("DU ClassLoader exported packages: "+Arrays.asList(classLoaderPolicy.getExportedPackages()));
            	}
            	// create class loader that first looks for classes localy and only after looks in parent
            	this.classLoader = classLoaderSystem.registerClassLoaderPolicy(classLoaderSystem.DEFAULT_DOMAIN_NAME,ParentPolicy.AFTER,classLoaderPolicy);
            	// create class pool
            	classPool = new ClassPool();
            	classPool.appendClassPath(new LoaderClassPath(this.classLoader));            	
            } catch (MalformedURLException e1) {
                throw new DeploymentException(
                        "Bad ULR for tempClassDeploymentDir: "
                                + getTempClassDeploymentDir(),e1);
            } catch (Exception e) {
                throw new DeploymentException(
                        "Failed Creating ClassLoader for tempClassDeploymentDir: "
                                + getTempClassDeploymentDir(),e);
            }
            Thread.currentThread().setContextClassLoader(this.classLoader);      
            
            // ---------- deploy component jars
            
            for (Iterator<AbstractComponentDeployer> it = simpleComponentsJarFiles.iterator(); it.hasNext();) {
            	it.next().deployAndInstall(deployableUnitID);
            }               
            for (Iterator<AbstractComponentDeployer> it = complexComponentsJarFiles.iterator(); it.hasNext();) {
               	it.next().deployAndInstall(deployableUnitID);
            }   
            
            // ----------- deploy services
            
            ArrayList<String> serviceNames = new ArrayList<String>();//used for asserting
            // unique names
            for (Iterator it = deployableUnitDescriptor.getServiceNodes().iterator(); it.hasNext();) {
            	String serviceXmlName = XMLUtils
            	.getElementTextValue((Element) it.next());
            	if (serviceXmlName.length() == 0
            			|| !serviceXmlName.toLowerCase().endsWith("xml")) {
            		throw new DeploymentException(serviceXmlName
            				+ " is not a valid service-xml file name.");
            	}
            	if (serviceNames.contains(serviceXmlName)) {
            		throw new DeploymentException("Encountered "
            				+ serviceXmlName + " more than once.");
            	}
            	//extract the xml file in the DU jar
            	File serviceXmlFile = DeploymentManager.extractFile(
            			serviceXmlName, unitJarFile,
            			tempDUJarsDeploymentDirectory);
            	// create deployer and deploy service
            	ServiceDeployer deployer = new ServiceDeployer();
            	deployer.initDeployer(serviceXmlFile,
            			sleeContainer);
            	deployer.deployAndInstall(deployableUnitID);
            	// clean up
            	serviceXmlFile.delete();                
            }

            // delete extracted jars
            for (Iterator<File> it = extractedJars.iterator(); it.hasNext();) {
                File file = it.next();
                if (file.delete()) {
                	 if (logger.isDebugEnabled())
                		 logger.debug("deleted file " + file.getName());
                }
                else {
                	if (logger.isDebugEnabled())
                		logger.debug("failed to delete file " + file.getName());
                }
            }
            
            // lets load all classes extracted in the DU
            /*for (String fileName : deployableUnitID.getDeployedFiles()) {
            	loadClass(fileName);
            }*/
            
            rollbackTx = false;
            
        } catch (Exception ex) {
            
        	// force undeploy to clean up
        	this.undeploy();
        	// re-throw ex
            if (ex instanceof DeploymentException) {
            	throw (DeploymentException)ex;
            }
            else {
            	throw new DeploymentException(ex.getMessage(),ex);
            }
            
        } finally {
        	
        	Thread.currentThread().setContextClassLoader(oldClassLoader);
            
        	try {
            	if (newTx) {
            		if (rollbackTx) {
            			transactionManager.rollback();
            		}
            		else {
            			transactionManager.commit();
            		}
            	}
            	else {
            		if (rollbackTx) {
            			transactionManager.setRollbackOnly();
            		}
            	}
            } catch (Exception e) {
                throw new RuntimeException("Pb with Slee Transaction Manager",
                        e);
            }
        }
    }

    /*
	private void loadClass(String fileName) {
		// if it's a class load it
        if (fileName.endsWith(".class")) {
        	String className = fileName.replace('/', '.').substring(0,(fileName.length()-".class".length()));
        	try {
        		logger.info("Loading class: "+className+ " to loader" + Thread.currentThread().getContextClassLoader());
        		Thread.currentThread().getContextClassLoader().loadClass(className);
        		
        	}
        	catch (Exception e) {
        		logger.error("Failed to load class extracted: "+className, e);
        	}
        }
	}
     */
    
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

    /**
     * Retrieves the class loader for the DU related with this deployer 
     * @return
     */
    public ClassLoader getClassLoader() {    	
        return classLoader;
    }

	/**
	 * This deployer's javassist class pool, useful for class generation
	 * 
	 * @return
	 */
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
        
       classLoader = null;
        
        if (classPool != null) {
        	classPool.clean();
        	classPool = null;
        }

        // unregister class loader policy
        if ( classLoaderPolicy != null ) {
        	ClassLoaderSystem.getInstance().unregisterClassLoaderPolicy(classLoaderPolicy);
        	classLoaderPolicy = null;
        }
        else {
        	if (logger.isDebugEnabled()) {
        		logger.debug("Class loading policy for DU "+deployableUnitID+ " not found, thus not unregistred.");
        	}
        }
        
        deletePath(tempClassDeploymentDir);
        deletePath(tempDUJarsDeploymentDirectory);
        
    }

    /**
     * deletes the whole path, going through directories
     * @param path
     */
    private void deletePath(File path) {
    	if (path.isDirectory()) {
    		for(File file : path.listFiles()) {
    			deletePath(file);
    		}
    	}
    	path.delete();
    }
}