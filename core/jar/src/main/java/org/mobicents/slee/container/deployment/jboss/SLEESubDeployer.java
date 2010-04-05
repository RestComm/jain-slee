package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.slee.InvalidStateException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jboss.deployment.DeploymentException;
import org.jboss.deployment.SubDeployerSupport;
import org.jboss.mx.util.MBeanServerLocator;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorFactory;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;

/**
 * This is the Deployer main class where the AS will invoke the lifecycle methods
 * for new deployments.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class SLEESubDeployer extends SubDeployerSupport implements SLEESubDeployerMBean, NotificationListener {

  public final static SLEESubDeployer INSTANCE = new SLEESubDeployer();

  // Constants -----------------------------------------------------

  // The Logger.
  private static Logger logger = Logger.getLogger(SLEESubDeployer.class);

  private static Timer timer = new Timer();

  // Attributes ----------------------------------------------------

  // The DIs to be accepted by this deployer.
  private ConcurrentHashMap<String, DeployableUnitWrapper> toAccept = new ConcurrentHashMap<String, DeployableUnitWrapper>();

  // Deployable Units present.
  private ConcurrentHashMap<String, DeployableUnit> deployableUnits = new ConcurrentHashMap<String, DeployableUnit>();

  private boolean isNotificationEnabled = false;

  private boolean isServerShuttingDown = false;

  // Constructors -------------------------------------------------------------

  /**
   * Default Constructor.
   */
  private SLEESubDeployer() {
    setSuffixes(new String[] { "xml", "jar" });
  }

  /**
   * Method for deciding whether or not to accept the file.
   */
  public boolean accepts(URL deployableUnitURL)
  {
    DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

    URL url = du.getUrl();

    if(logger.isDebugEnabled()) {
      logger.debug("Method accepts called for " + url);
    }

    try {
      String fullPath = url.getFile();
      String fileName = fullPath.substring(fullPath.lastIndexOf('/')+1, fullPath.length()); 

      // Is it in the toAccept list ? Direct accept.
      if(toAccept.containsKey(fileName)) {
        if(logger.isDebugEnabled()) {
          logger.debug("Accepting " + url.toString() + ".");
        }

        return true;
      }
      // If not it the accept list but it's a jar might be a DU jar...
      else if(fileName.endsWith(".jar")) {
        JarFile duJarFile = null; 

        try {
          // Try to obtain the DU descriptor, if we got it, we're accepting it!
          if(du.getEntry("META-INF/deployable-unit.xml") != null) {
            if(logger.isDebugEnabled()) {
              logger.debug("Accepting " + url.toString() + ".");
            }

            return true;
          }
        }
        finally {
          // Clean up!
          if(duJarFile != null) {
            try {
              duJarFile.close();
            }
            catch (IOException ignore) {
            }
            finally {
              duJarFile = null;
            }
          }
        }
      }
    }
    catch (Exception ignore) {
      // Ignore.. will reject.
    }

    // Uh-oh.. looks like it will stay outside.
    return false;
  }

  /**
   * Initializer method for accepted files. Will parse descriptors at this point.
   */
  public void init(URL deployableUnitURL) throws DeploymentException
  {
    URL url = deployableUnitURL;
    DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);


    if(logger.isDebugEnabled()) {
      logger.debug("Method init called for " + deployableUnitURL);
    }

    if(!isNotificationEnabled) {
      try {
        // find mbean server
        final MBeanServer server = MBeanServerLocator.locateJBoss();
        // register for notifications on jboss
        server.addNotificationListener(new ObjectName("jboss.system:type=Server"), this, null, "");
        isNotificationEnabled = true;
      }
      catch (Exception e) {
        logger.error("Failed to register notification listener.", e);
      }
    }


    // Get the full path and filename for this du
    String fullPath = du.getFullPath();
    String fileName = du.getFileName();

    try {
      DeployableUnitWrapper duWrapper = null;

      // If we're able to remove it from toAccept was because it was there!
      if((duWrapper = toAccept.remove(fileName)) != null) {
        // Create a new Deployable Component from this DI.
        DeployableComponent dc = new DeployableComponent(du, url, fileName); 

        // Also get the deployable unit for this (it exists, we've checked!)
        DeployableUnit deployerDU = deployableUnits.get(duWrapper.getFileName());

        for(DeployableComponent subDC : dc.getSubComponents()) {
          // Add the sub-component to the DU object.
          deployerDU.addComponent(subDC);
        }
      }
      // If the DU for this component doesn't exists.. it's a new DU!
      else if(fileName.endsWith(".jar")) {
        JarFile duJarFile = null;

        try {
          // Get a reference to the DU jar file
          duJarFile = new JarFile(fullPath);

          // Try to get the Deployable Unit descriptor
          JarEntry duXmlEntry = duJarFile.getJarEntry("META-INF/deployable-unit.xml");    

          // Got descriptor?
          if(duXmlEntry != null) {
            // Create a new Deployable Unit object.
            DeployableUnit deployerDU = new DeployableUnit(du);

            // Let's parse the descriptor to see what we've got...
            DeployableUnitDescriptorFactory dudf = new DeployableUnitDescriptorFactory();
            DeployableUnitDescriptorImpl duDesc = dudf.parse(duJarFile.getInputStream(duXmlEntry));

            // Add it to the deployable units map.
            deployableUnits.put(fileName, deployerDU);

            // Go through each jar entry in the DU descriptor
            for(String componentJarName : duDesc.getJarEntries()) {
              // Might have path... strip it!
              int beginIndex;

              if((beginIndex = componentJarName.lastIndexOf('/')) == -1)
                beginIndex = componentJarName.lastIndexOf('\\');

              beginIndex++;

              // Got a clean jar name, no paths.
              componentJarName = componentJarName.substring(beginIndex, componentJarName.length());

              // Put it in the accept list.
              toAccept.put(componentJarName, du);
            }

            // Do the same as above... but for services
            for(String serviceXMLName : duDesc.getServiceEntries()) {
              // Might have path... strip it!
              int beginIndex;

              if((beginIndex = serviceXMLName.lastIndexOf('/')) == -1)
                beginIndex = serviceXMLName.lastIndexOf('\\');

              beginIndex++;

              // Got a clean XML filename
              serviceXMLName = serviceXMLName.substring(beginIndex, serviceXMLName.length());

              // Add it to the accept list.
              toAccept.put(serviceXMLName, du);
            }
          }
        }
        finally {
          // Clean up!
          if(duJarFile != null) {
            try {
              duJarFile.close();
            }
            catch (IOException ignore) {
            }
            finally {
              duJarFile = null;
            }
          }
        }
      }
    }
    catch (Exception e) {
      // Something went wrong...
    	if(logger.isEnabledFor(Level.ERROR))
    	{
    		logger.error("Deployment of " + fileName + " failed. ", e);
    	}

      return;
    }
  }

  /**
   * This is where the fun begins. Time to deploy!
   */
  public void start(URL deployableUnitURL) throws DeploymentException {
    DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

    if(logger.isDebugEnabled()) {
      logger.debug("Method start called for " + du.getUrl());
    }

    try {
      // Get the deployable unit object
      DeployableUnit realDU = deployableUnits.get(du.getFileName());

      // If it exists, install it.
      if(realDU != null) {
        DeploymentManager.INSTANCE.installDeployableUnit(realDU);
      }
    }
    catch (Exception e) {
    	if(logger.isEnabledFor(Level.ERROR))
    	{
    		logger.error("", e);
    	}
    }
  }

  /**
   * Fun has ended. Time to undeploy.
   */
  public void stop(URL deployableUnitURL) throws DeploymentException {
    DeployableUnitWrapper du = new DeployableUnitWrapper(deployableUnitURL);

    DeployableUnit realDU = null;

    String fileName = du.getFileName();

    if ((realDU = deployableUnits.get(du.getFileName())) != null) {
      if(logger.isTraceEnabled()) {
        logger.trace("Got DU: " + realDU.getDeploymentInfoShortName());
      }

      if(isServerShuttingDown) {
        doStop(fileName);
      }
      else {
        // Schedule removal in a recurring task
        timer.scheduleAtFixedRate(new UndeploymentTask(fileName), 0, getWaitTimeBetweenOperations());
      }
    }
  }

  private boolean doStop(String filename) {
    if(logger.isDebugEnabled())
      logger.debug("Method stop called for " + filename);

    DeployableUnit du = null;

    try {
      // Get and remove deployable unit object
      if((du = deployableUnits.get(filename)) != null) {
        // Uninstall it
        DeploymentManager.INSTANCE.uninstallDeployableUnit(du);

        // Remove it from list if successful
        deployableUnits.remove(filename);

        // Make it null. clean.
        du = null;
      }
    }
    catch (Exception e) {
      Throwable cause = e.getCause();

      if(cause instanceof InvalidStateException) {
    	  if(logger.isEnabledFor(Level.WARN))
      	{
    		  logger.warn(cause.getLocalizedMessage() + "... WAITING ...");
      	}
      }
      else if (e instanceof DeploymentException){
        throw new IllegalStateException(e.getLocalizedMessage(), e);
      }
      else {
    	  if(logger.isEnabledFor(Level.ERROR))
      	  {
    		  logger.error(e.getMessage(), e);
      	  }
      }
      return false;
    }

    return true;
  }

  // MBean Operations ---------------------------------------------------------

  /**
   * MBean operation for getting Deployer status.
   */
  public String showStatus() throws DeploymentException {
	  StringBuilder output = new StringBuilder();
    

    output.append("<p>Deployable Units List:</p>");

    for(String key : deployableUnits.keySet()) {
    	 output.append("&lt;").append(key).append("&gt; [").append(deployableUnits.get(key)).append("]<br>");

      for(String duComponent : deployableUnits.get(key).getComponents()) {
        output.append("+-- ").append(duComponent).append("<br>");
      }
    }

    output.append("<p>To Accept List:</p>");

    for(String key : toAccept.keySet()) {
    	output.append("&lt;").append( key ).append("&gt; [").append(toAccept.get(key) ).append("]<br>");   
    }

    output.append("<p>Deployment Manager Status</p>");

    output.append(DeploymentManager.INSTANCE.showStatus());   

    return output.toString();
  }

  /**
   * MBean WaitTimeBetweenOperations property getter
   */
  public long getWaitTimeBetweenOperations() {
    return DeploymentManager.INSTANCE.waitTimeBetweenOperations;
  }

  /**
   * MBean WaitTimeBetweenOperations property getter
   */
  public void setWaitTimeBetweenOperations(long waitTime) {
    DeploymentManager.INSTANCE.waitTimeBetweenOperations = waitTime;
  }

  // Aux Functions ------------------------------------------------------------

  private class UndeploymentTask extends TimerTask {
    String filename;
    long startTime;

    public UndeploymentTask(String filename) {
      this.filename = filename;
      this.startTime = System.currentTimeMillis();
    }

    public void run()
    {
      try {
        long elapsedTime = System.currentTimeMillis() - this.startTime;

        if(doStop(filename) || elapsedTime > 60 * 60 * 1000 + MobicentsManagement.entitiesRemovalDelay) {
          this.cancel();
        }
      }
      catch (IllegalStateException ise) {
        // This only happens when there are dependents. Let's cancel, it'll return.
        this.cancel();
      }
      catch (Exception ignore) {
        // do nothing...
      }
    }

  }

  public void handleNotification(Notification notification, Object o) {
    if(notification.getType().equals("org.jboss.system.server.stopped")) {
      isServerShuttingDown = true;
    }
  }

  public boolean isServerShuttingDown() {
    return isServerShuttingDown;
  }
}