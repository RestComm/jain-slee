/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-8-1                            *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.deployment.jboss;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ManagementException;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.resource.ResourceAdaptorDescriptor;
import javax.slee.resource.ResourceAdaptorID;

import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.DeploymentInfo;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.mx.util.MBeanProxyExt;
import org.jboss.system.ServiceControllerMBean;
import org.mobicents.slee.container.management.jmx.SleeManagementMBeanImplMBean;

/**
 * A SleeDeployer is used to deploy SLEE applications. It can be given a
 * URL to a SLEE DU jar or SLEE-JAR XML file, which will be used to instantiate
 * SLEE components and make them available for invocation.
 *
 * @version <tt>$Revision: 1.6 $</tt>
 * @author Ivelin Ivanov
 */
public class SleeDeployer 
   extends SubDeployerSupport
   implements SleeDeployerMBean
{
   private ServiceControllerMBean serviceController;
   
   /** reference to the standard SLEE deployment MBean */
   private DeploymentMBean sleeDeploymentMBean;

   /** reference to the SLEE ResourceManagementMBean */
   private ResourceManagementMBean  resourceManagementMBean;

   /** A flag indicating if deployment descriptors should be validated */
   private boolean validateDTDs;

   /** A map of current deployments. */
   private HashMap deployments = new HashMap();

   private ObjectName sleeManagementMBeanName;
   
   /** reference to the Mobicents SLEE Management MBean */
   private SleeManagementMBeanImplMBean sleeManagementMBean;

private ServiceManagementMBean serviceManagementMBean;

   /**
    * Returns the deployed SLEE jars.
    *
    */
   public Iterator getDeployedApplications()
   {
      return deployments.values().iterator();
   }

   protected ObjectName getObjectName(MBeanServer server, ObjectName name)
      throws MalformedObjectNameException
   {
      return name == null ? new ObjectName("slee:service=SleeDeployer,platform=jboss") : name;
   }

   /**
    * Get a reference to the ServiceController
    */
   protected void startService() throws Exception
   {
       
      serviceController = (ServiceControllerMBean)
         MBeanProxyExt.create(ServiceControllerMBean.class,
                              ServiceControllerMBean.OBJECT_NAME, server);
      
      sleeManagementMBean = (SleeManagementMBeanImplMBean)
      	MBeanProxyExt.create(SleeManagementMBeanImplMBean.class,
   	         	sleeManagementMBeanName, server);

      sleeDeploymentMBean = (DeploymentMBean)
      	 MBeanProxyExt.create(DeploymentMBean.class,
      	         	sleeManagementMBean.getDeploymentMBean(), server);
  
      resourceManagementMBean  = (ResourceManagementMBean)
         MBeanProxyExt.create(ResourceManagementMBean.class,
                 sleeManagementMBean.getResourceManagementMBean(), server);
      
      serviceManagementMBean = (ServiceManagementMBean)
         MBeanProxyExt.create(ServiceManagementMBean.class,
                 sleeManagementMBean.getServiceManagementMBean(), server);

      // register with MainDeployer
      super.startService();      
   }

   /**
    * This method stops all SLEE deployments
    */
   protected void stopService() throws Exception
   {

      for( Iterator modules = deployments.values().iterator();
         modules.hasNext(); )
      {
         DeploymentInfo di = (DeploymentInfo) modules.next();
         stop(di);
      }

      // avoid concurrent modification exception
      for( Iterator modules = new ArrayList(deployments.values()).iterator();
         modules.hasNext(); )
      {
         DeploymentInfo di = (DeploymentInfo) modules.next();
         destroy(di);
      }
      deployments.clear();

      // deregister with MainDeployer
      super.stopService();

      serviceController = null;
   }

   /**
    * Tests whether a given target URL is a SLEE deployment unit
    */
   public boolean accepts(DeploymentInfo di)
   {
      // To be accepted the deployment's root name must end in jar
      String urlStr = di.url.getFile();
      if( !urlStr.endsWith("jar") && !urlStr.endsWith("jar/") )
      {
         return false;
      }

      // However the jar must also contain at least one deployable-unit.xml
      boolean accepts = false;
      try
      {
         URL dd = di.localCl.findResource("META-INF/deployable-unit.xml");
         if (dd == null)
         {
            return false;
         }

         // If the DD url is not a subset of the urlStr then this is coming
         // from a jar referenced by the deployment jar manifest and the
         // this deployment jar it should not be treated as an ejb-jar
         if( di.localUrl != null )
         {
            urlStr = di.localUrl.toString();
         }

         String ddStr = dd.toString();
         if ( ddStr.indexOf(urlStr) >= 0 )
         {
            accepts = true;
         }
      }
      catch( Exception ignore )
      {
      }

      return accepts;
   }

   public void init(DeploymentInfo di)
      throws DeploymentException
   {
      log.debug("init, "+di.shortName);
      try
      {
         if( di.url.getProtocol().equalsIgnoreCase("file") )
         {
            File file = new File(di.url.getFile());

            if( !file.isDirectory() )
            {
               // If not directory we watch the package
               di.watch = di.url;
            }
            else
            {
               // If directory we watch the xml files
               di.watch = new URL(di.url, "META-INF/deployable-unit.xml");
            }
         }
         else
         {
            // We watch the top only, no directory support
            di.watch = di.url;
         }

      }
      catch (Exception e)
      {
         if (e instanceof DeploymentException)
            throw (DeploymentException)e;
         throw new DeploymentException( "failed to initialize", e );
      }

      // invoke super-class initialization
      super.init(di);
   }

   /** 
    * 
    * We do not want to allow SLEE jars to have arbitrary sub deployments.
    * @param di
    * @throws DeploymentException
    */ 
   protected void processNestedDeployments(DeploymentInfo di)
      throws DeploymentException
   {
       return;
   }

   public synchronized void start(DeploymentInfo di)
      throws DeploymentException
   {
      try
      {
         // Start application
         log.debug( "start application, deploymentInfo: " + di +
                    ", short name: " + di.shortName +
                    ", parent short name: " +
                    (di.parent == null ? "null" : di.parent.shortName) );
         
         
         DeployableUnitID did = sleeDeploymentMBean.install(di.localUrl.toExternalForm()); 
         di.metaData = did;
         
         DeployableUnitDescriptor dud = sleeDeploymentMBean.getDescriptor(did);
         
         activateComponents( dud.getComponents() );
         
         
         // Register deployment. Use the application name in the hashtable
         // FIXME: this is obsolete!! (really?!)
         deployments.put(di.url, di);

         log.info( "Deployed: " + di.url );
      }
      catch (Exception e)
      {
         stop(di);
         destroy(di);

         throw new DeploymentException( "Could not deploy " + di.url, e );
      }
      super.start(di);
   }

 /**
  * 
  * Activate the SLEE components once they are installed
  * 
  * @param components to activate
 * @throws ManagementException
 * @throws UnrecognizedComponentException
 * @throws NullPointerException
  */
private void activateComponents(ComponentID[] components) throws Exception {
    for (int i = 0; i < components.length; i++) {
        ComponentID cid = components[i];
        if (cid instanceof ResourceAdaptorID) {
            activateResourceAdaptor((ResourceAdaptorID)cid);
        } else if (cid instanceof ServiceID) {
            activateService((ServiceID)cid);
        }
    }
}

/**
 * @param serviceID
 * @throws ManagementException
 * @throws InvalidStateException
 * @throws UnrecognizedServiceException
 * @throws NullPointerException
 */
private void activateService(ServiceID serviceID) throws Exception {
    serviceManagementMBean.activate(serviceID);
}

private void deactivateService(ServiceID serviceID) throws Exception {
    serviceManagementMBean.deactivate(serviceID);
}

/**
 * 
 * Deactivate SLEE components before they are uninstalled in the reverse order that they were activated.
 * 
 * @param components to deactivate
 * @throws ManagementException
 * @throws UnrecognizedComponentException
 * @throws NullPointerException
 */
private void deactivateComponents(ComponentID[] components) throws Exception {
   for (int i = components.length-1; i >= 0; i--) {
       ComponentID cid = components[i];
       if (cid instanceof ResourceAdaptorID) {
           deactivateResourceAdaptor((ResourceAdaptorID)cid);
	   } else if (cid instanceof ServiceID) {
	       deactivateService((ServiceID)cid);
	   }
   }
}


private String getRADefaultEntityName(ResourceAdaptorID raid) throws Exception {
    ResourceAdaptorDescriptor rad = (ResourceAdaptorDescriptor)sleeDeploymentMBean.getDescriptor(raid);
    String raEntityName = rad.getName() + "-ra";
    return raEntityName;
}

/**
 * Activate an RA after it has been installed.
 * 
 * @param raid
 * @throws ManagementException
 * @throws UnrecognizedComponentException
 * @throws NullPointerException
 */
private void activateResourceAdaptor(ResourceAdaptorID raid) throws Exception {
    String raEntityName = getRADefaultEntityName(raid);
    resourceManagementMBean.createResourceAdaptorEntity(raid, raEntityName, new Properties());
    resourceManagementMBean.activateResourceAdaptorEntity(raEntityName);
    resourceManagementMBean.bindLinkName(raEntityName, raEntityName);
}

/**
 * Deactivate an RA before it is uninstalled.
 * 
 * @param raid
 * @throws ManagementException
 * @throws UnrecognizedComponentException
 * @throws NullPointerException
 */
private void deactivateResourceAdaptor(ResourceAdaptorID raid) throws Exception {
    String raEntityName = getRADefaultEntityName(raid);
    resourceManagementMBean.unbindLinkName(raEntityName);
    resourceManagementMBean.deactivateResourceAdaptorEntity(raEntityName);
    resourceManagementMBean.removeResourceAdaptorEntity(raEntityName);
}


public void stop(DeploymentInfo di)
      throws DeploymentException
   {
      log.info( "Undeploying: " + di.url );
      try
      {
          DeployableUnitID did = (DeployableUnitID)di.metaData;
          
          // if the start failed did would be null
          if (did == null) return;
          
          DeployableUnitDescriptor dud = sleeDeploymentMBean.getDescriptor(did);
          
          deactivateComponents( dud.getComponents() );

          sleeDeploymentMBean.uninstall(did);
      }
      catch (Exception e)
      {
         throw new DeploymentException( "problem stopping SLEE module: " +
            di.url, e );
      }
      super.stop(di);
   }

   /**
    * Get the flag indicating that ejb-jar.dtd, jboss.dtd &amp;
    * jboss-web.dtd conforming documents should be validated
    * against the DTD.
    *
    */
   public boolean getValidateDTDs()
   {
      return validateDTDs;
   }

   /**
    * Set the flag indicating that ejb-jar.dtd, jboss.dtd &amp;
    * jboss-web.dtd conforming documents should be validated
    * against the DTD.
    *
    */
   public void setValidateDTDs(boolean validate)
   {
      this.validateDTDs = validate;
   }


   /**
    * return the ObjectName of the DeploymentMBean
    * 
    */
   public ObjectName getSleeManagementMBean() {
       return this.sleeManagementMBeanName;
   }

   /**
    * set the ObjectName of the SleeManagementMBean
    * 
    */
   public void setSleeManagementMBean(ObjectName newSM) {
       this.sleeManagementMBeanName = newSM;
   }

   public void destroy(DeploymentInfo di)
   throws DeploymentException
{
   // FIXME: If the put() is obsolete above, this is obsolete, too
   deployments.remove(di.url);

   super.destroy(di);
}

}
