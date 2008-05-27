/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component.deployment;

import javax.slee.management.*;
import java.util.jar.*;
import java.util.*;
import java.io.*;
import org.jboss.logging.*;
import org.mobicents.slee.container.component.ComponentContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.DeployedComponent;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;

/**
 * Instances of this class represent DU components or in other words - JAR file
 * entries contained in a deployable unit jar such as SBBs, Profiles, RA Types
 * and etc.
 * @author Emil Ivov
 * @author Ivelin Ivanov
 */
abstract class AbstractComponentDeployer {
    private Logger logger;
    private JarFile componentJar = null;
    protected List componentDescriptors = null;
    protected ComponentContainer componentContainer = null;
    protected File classpathDirectory = null;
    protected DeployableUnitDeployer duDeployer;
    private boolean preparedToDeploy;
  
  
    AbstractComponentDeployer() {
        logger = Logger.getLogger(AbstractComponentDeployer.class);
    }

    JarFile getJar() {
        return componentJar;
    }

    /**
     * Inits and parses
     * @param jarEntry JarFile
     * @param componentContainer the container where the component should be
     * deployed.
     * @param a directory, part of the classpath, where component contents
     * should be unjarred.
     * @throws DeploymentException
     */
    protected void initDeployer(JarFile componentJar,
                                ComponentContainer container,
                                File classpathDirectory,
                                DeployableUnitDeployer duDeployer)
        throws DeploymentException {
        if ( logger.isDebugEnabled())
        logger.debug("componentJar " + componentJar + " container = " + container + " classpathDir " + classpathDirectory);
        if ( componentJar == null || container == null || classpathDirectory == null) 
            throw new NullPointerException ("null arg!");
        this.componentJar = componentJar;
        this.componentContainer = container;
        this.classpathDirectory = classpathDirectory;
        this.duDeployer = duDeployer;
     
        try {
            this.componentDescriptors = parseComponentDescriptors();
        }
        catch (Exception ex) {
            throw new DeploymentException("Failed to parse deployment descriptor of "
                                          + componentJar.getName(), ex);
        }
    }

    protected abstract List parseComponentDescriptors()
        throws Exception;
    
    
    /**
     * Installs all the component descriptors in the ComponentContainer specified
     * to the ComponentDeployer in the initDeployer method..
     * @param  deployableUnitID -- the deployable unit id to assign.
     * @throws Exception if an Exception occurs during deployment.
     */
    protected void deployAndInstall(DeployableUnitIDImpl deployableUnitID)
        throws Exception
    {
        if (preparedToDeploy == false) throw new IllegalStateException("Failed, because the component has not been prepared to deploy.");
        
        if(componentDescriptors == null || componentDescriptors.size() == 0)
            return;
        if(logger.isDebugEnabled()) {
        	logger.debug("Deploying and installing " + componentJar.getName()+". ClassPathDirectory " + classpathDirectory);
        }
      
        Iterator descriptors = componentDescriptors.iterator();
        while (descriptors.hasNext()) {
           
            ComponentDescriptor cd = (ComponentDescriptor) descriptors.next();
         
            DeployedComponent deployedComponent = (DeployedComponent) cd;
            deployedComponent.setDeployableUnit(deployableUnitID);
            DeployableUnitDescriptorImpl duD = deployableUnitID.getDescriptor();
          
           if ( cd instanceof SbbDescriptor) {
                MobicentsSbbDescriptor sbbD = ( MobicentsSbbDescriptor) cd;
                ClassLoader cl = this.duDeployer.createDUClassLoader();
                sbbD.setClassLoader(cl);
            }
            deployedComponent.checkDeployment();
            // This is where the classes are generated.
            componentContainer.install(cd,duD);
        
            duD.addComponent(cd.getID());
        }
    }
    
    /**
     * 
     * Unpackages the component files. Should be always invoked before deployAndInstall.
     * More specifically this method should be invoked on all components in the same DU.
     * After that the deployAndInstall method should be invoked on all components.
     * This is required to ensure class visibility across components at deployment time.  
     *
     */
    protected void prepareDeploy(DeployableUnitIDImpl deployableUnitID) throws Exception
    {
        preparedToDeploy = false;
        //Extract jar contents to a classpath location
        
         JarInputStream jarIs = new JarInputStream(new BufferedInputStream(new FileInputStream( componentJar.getName())));
        
         
         for ( JarEntry entry = jarIs.getNextJarEntry();  jarIs.available()>0 && entry != null; entry = jarIs.getNextJarEntry()){
            
             logger.debug("zipEntry = " + entry.getName());
            
             if(entry.getName().indexOf("META-INF") != -1){
                 continue;
             }

             if( entry.isDirectory() )
             {
                 //Create jar directories.
                 File dir = new File(classpathDirectory, entry.getName());
                 if (!dir.exists() ) 
                 {
                     if ( !dir.mkdirs()) {
                         logger.debug("Failed to create directory " + dir.getAbsolutePath());
                         throw new DeploymentException("Failed to create directory " +
                                                   dir.getAbsolutePath());
                     }
                 }
                 else
                     logger.debug("Created directory" + dir.getAbsolutePath());
             }
             else // unzip files
             {
             	File dir = new File(classpathDirectory, entry.getName()).getParentFile();

                 if (!dir.exists() ) 
                 {
                     if ( !dir.mkdirs()) {
                         logger.debug("Failed to create directory " + dir.getAbsolutePath());
                         throw new DeploymentException("Failed to create directory " +
                                                   dir.getAbsolutePath());
                     } else
                     	logger.debug("Created directory" + dir.getAbsolutePath());
                 }
  
                 
                 deployableUnitID.addEntry(entry.getName());
                
                 DeploymentManager.pipeStream(componentJar.getInputStream(entry),
                                              new FileOutputStream(new File(
                     classpathDirectory, entry.getName()))
                                              );
             }
         }
         // Done with the jar file.
         jarIs.close();
         componentJar.close();
         preparedToDeploy = true;
    }
    
    
    protected static void undeploy(DeployableUnitIDImpl deployableUnitID)
    {
      // TODO - populate this with the symmetric code of deployAndInstall()
      // currently the undeploy code is sprinkled. 
      // SleeContainer.doRemove() should take advantage of this method.
    }
}

