package org.mobicents.slee.container.deployment.jboss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.slee.ComponentID;
import javax.slee.InvalidStateException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedLinkNameException;

import org.jboss.deployment.DeploymentException;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;

/**
 * This class represents the Manager responsible for executing deployment actions
 * and for controlling dependencies and monitoring new deployments using the deployer.
 * 
 * @author Alexandre Mendonça
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class DeploymentManager {

  // Let's make sure it's a singleton
  public final static DeploymentManager INSTANCE = new DeploymentManager();

  // The Logger.
  private static Logger logger = Logger.getLogger(DeploymentManager.class);

  // The DUs waiting to be installed.
  private Collection<DeployableUnit> waitingForInstallDUs = new ConcurrentLinkedQueue<DeployableUnit>();

  // The DUs waiting for being uninstalled.
  private Collection<DeployableUnit> waitingForUninstallDUs = new ConcurrentLinkedQueue<DeployableUnit>();

  // The DUs waiting for being uninstalled.
  private Collection<DeployableUnit> deployedDUs = new ConcurrentLinkedQueue<DeployableUnit>();

  // The components already deployed to SLEE
  private Collection<String> deployedComponents = new ConcurrentLinkedQueue<String>();

  // Actions using DeploymentMBean
  private Collection<String> deploymentActions = Arrays.asList(new String[] { "install", "uninstall" });

  // Actions using ResourceManagementMBean
  private Collection<String> resourceAdaptorActions = Arrays.asList(new String[] { 
      "bindLinkName", "unbindLinkName",
      "createResourceAdaptorEntity", "removeResourceAdaptorEntity",
      "activateResourceAdaptorEntity", "deactivateResourceAdaptorEntity" });

  // Actions using ServiceManagementMBean
  private Collection<String> serviceActions = Arrays.asList(new String[] { "activate", "deactivate" });

  private Collection<String> deployActions = Arrays.asList(new String[] { "activate", 
      "bindLinkName", "createResourceAdaptorEntity", "activateResourceAdaptorEntity" });

  private ConcurrentHashMap<DeployableUnit, Collection<String>> actionsToAvoidByDU = new ConcurrentHashMap<DeployableUnit, Collection<String>>();

  public long waitTimeBetweenOperations = 250;

  /**
   * Constructor.
   */
  private DeploymentManager() {
  }

  /**
   * Updates the list of components already deployed to SLEE.
   */
  public void updateDeployedComponents() {
    try {
      // Get the SLEE Container from JNDI
      SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

      // First we'll put the components in a temp Collection
      ConcurrentLinkedQueue<String> newDeployedComponents = new ConcurrentLinkedQueue<String>();

      // Get the deployed Profile Specifications
      for (ComponentID componentID: sleeContainer.getComponentRepository().getProfileSpecificationIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Event Types
      for (ComponentID componentID: sleeContainer.getComponentRepository().getEventComponentIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Resource Adaptor Types
      for (ComponentID componentID: sleeContainer.getComponentRepository().getResourceAdaptorTypeIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Resource Adaptors
      for (ComponentID componentID: sleeContainer.getComponentRepository().getResourceAdaptorIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Service Building Blocks (SBBs)
      for (ComponentID componentID: sleeContainer.getComponentRepository().getSbbIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Services
      for (ComponentID componentID: sleeContainer.getComponentRepository().getServiceIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Libraries
      for (ComponentID componentID: sleeContainer.getComponentRepository().getLibraryIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the existing Resource Adaptor Entity links
      String[] entityNames = sleeContainer.getResourceManagement().getResourceAdaptorEntities();

      for (String entityName : entityNames) {
        newDeployedComponents.addAll(Arrays.asList(sleeContainer.getResourceManagement().getLinkNames(entityName)));
      }

      // All good.. Make the temp the good one.
      deployedComponents = newDeployedComponents;
    }
    catch (Exception e) {
      logger.warn("Failure while updating deployed components.", e);
    }
  }

  /**
   * Method for installing a Deployable Unit into SLEE.
   * @param du the Deployable Unit to install.
   * @throws Exception
   */
  public void installDeployableUnit(DeployableUnit du) throws Exception {

    // Update the deployed components from SLEE
    updateDeployedComponents();

    // Check if the DU is ready to be installed
    if (du.isReadyToInstall(true)) {
      // Get and Run the actions needed for installing this DU
      sciAction(du.getInstallActions(), du);

      // Set the DU as installed
      du.setInstalled(true);

      // Add the DU to the installed list
      deployedDUs.add(du);

      // Update the deployed components from SLEE
      updateDeployedComponents();

      // Go through the remaining DUs waiting for installation
      Iterator<DeployableUnit> duIt = waitingForInstallDUs.iterator();

      while (duIt.hasNext()) {
        DeployableUnit waitingDU = duIt.next();

        // If it is ready for installation, follow the same procedure
        if (waitingDU.isReadyToInstall(false)) {
          // Get and Run the actions needed for installing this DU
          sciAction(waitingDU.getInstallActions(), waitingDU);

          // Set the DU as installed
          waitingDU.setInstalled(true);

          // Add the DU to the installed list
          deployedDUs.add(waitingDU);
          
          // Update the deployed components from SLEE
          updateDeployedComponents();

          // Remove the DU from the waiting list.
          waitingForInstallDUs.remove(waitingDU);

          // Let's start all over.. :)
          duIt = waitingForInstallDUs.iterator();
        }
      }
    }
    else {
      logger.warn("Unable to INSTALL " + du.getDeploymentInfoShortName() + " right now. Waiting for dependencies to be resolved.");

      // The DU can't be installed now, let's wait...
      waitingForInstallDUs.add(du);
    }
  }

  /**
   * Method for uninstalling a Deployable Unit into SLEE.
   * @param du the Deployable Unit to install.
   * @throws Exception
   */
  public void uninstallDeployableUnit(DeployableUnit du) throws Exception {

    // Update the deployed components from SLEE
    updateDeployedComponents();

    // It isn't installed?
    if (!du.isInstalled()) {
      // Then it should be in the waiting list... remove and we're done.
      if (waitingForInstallDUs.remove(du)) {
        logger.info(du.getDeploymentInfoShortName() + " wasn't deployed. Removing from waiting list.");
      }
    }
    // Check if DU components are still present 
    else if (!du.areComponentsStillPresent()) {
      logger.info(du.getDeploymentInfoShortName() + " components already removed. Removing DU info.");
      
      // Process internals of undeployment...
      processInternalUndeploy(du);
    }
    // Check if the DU is ready to be uninstalled
    else if (du.isReadyToUninstall()) {
      // Get and Run the actions needed for uninstalling this DU
      sciAction(du.getUninstallActions(), du);

      // Process internals of undeployment...
      processInternalUndeploy(du);
    }
    else {
      // Have we been her already? If so, don't flood user with log messages...
      if (!waitingForUninstallDUs.contains(du)) {
        // Add it to the waiting list.
        waitingForUninstallDUs.add(du);

        logger.warn("Unable to UNINSTALL " + du.getDeploymentInfoShortName() + " right now. Waiting for dependents to be removed.");
      }

      // But we have to throw this so task knows that it needs to retry
      throw new DeploymentException("Unable to UNINSTALL " + du.getDeploymentInfoShortName() + " right now. Waiting for dependents to be removed.");
    }
  }

  /**
   * Sets the DU as not installed and remove it from waiting list if present there.
   * Also, tries to undeploy DU's waiting for dependencies to be removed.
   * 
   * @param du the DeployableUnit that was just removed
   * @throws Exception
   */
  private void processInternalUndeploy(DeployableUnit du) throws Exception {
    // Set the DU as not installed
    du.setInstalled(false);

    // Remove if it was present in waiting list
    waitingForUninstallDUs.remove(du);

    // Update the deployed components from SLEE
    updateDeployedComponents();

    // Go through the remaining DUs waiting for uninstallation
    Iterator<DeployableUnit> duIt = waitingForUninstallDUs.iterator();

    while (duIt.hasNext()) {
      DeployableUnit waitingDU = duIt.next();

      // If it is ready for being uninstalled, follow the same procedure
      if (waitingDU.isReadyToUninstall()) {
        // Schedule removal
        SLEESubDeployer.INSTANCE.stop(waitingDU.getURL());

        // Remove the DU from the waiting list. If it fails, will go back.
        waitingForUninstallDUs.remove(waitingDU);

        // Let's start all over.. :)
        duIt = waitingForUninstallDUs.iterator();
      }
    }
  }
  
  /**
   * Method for performing the actions needed for (un)deployment.
   * @param actions the array of strings containing the actions to perform.
   * @param du the DeployableUnit from where the actions are being performed.
   * @throws Exception
   */
  private void sciAction(Collection<Object[]> actions, DeployableUnit du)
  throws Exception {
    // Get the MBeanServer
    MBeanServer ms = SleeContainer.lookupFromJndi().getMBeanServer();

    // For each action, get the params..
    for (Object[] params : actions) {
      // The ObjectName for the MBean invocations
      ObjectName objectName = null;

      // The arguments and their signature
      Object[] arguments = new Object[params.length - 1];

      String[] signature = new String[params.length - 1];

      // Get the action to perform
      String action = (String) params[0];

      // Shall we skip this action?
      if (actionsToAvoidByDU.get(du) != null && actionsToAvoidByDU.get(du).remove(action)) {
        // Clean if it was the last one.
        if (actionsToAvoidByDU.get(du).size() == 0) {
          actionsToAvoidByDU.remove(du);
        }

        continue;
      }

      // Get the parameters and the signature
      for (int i = 1; i < params.length; i++) {
        arguments[i - 1] = params[i];
        signature[i - 1] = params[i].getClass().getName();
      }

      // Get the correct object name for the action
      if (deploymentActions.contains(action)) {
        // Set the corresponding ObjectName
        objectName = new ObjectName(DeploymentMBean.OBJECT_NAME);

        if (action.equals("uninstall")) {
          // Need to convert from file to DeployableUnitID
          DeployableUnitID duID = (DeployableUnitID) ms.invoke(objectName, "getDeployableUnit",
              new Object[] { params[1] }, new String[] { params[1].getClass().getName() });

          // Update arguments and signature
          arguments[0] = duID;
          signature[0] = "javax.slee.management.DeployableUnitID";
        }
      }
      else if (resourceAdaptorActions.contains(action)) {
        // Set the corresponding ObjectName
        objectName = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

        // Special case as arg is ResourceAdaptorIDImpl..
        if (action.equals("createResourceAdaptorEntity")) {
          signature[0] = "javax.slee.resource.ResourceAdaptorID";
        }
      }
      else if (serviceActions.contains(action)) {
        // Set the corresponding ObjectName
        objectName = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

        // Special case as arg is ServiceIDImpl..
        signature[0] = "javax.slee.ServiceID";
      }

      if (logger.isTraceEnabled()) {
        logger.trace("Invoking " + action + "(" + Arrays.toString(arguments) + ") on " + objectName);
      }

      // We are isolating each action, so it won't affect the whole proccess
      try {
        // Invoke it.
        ms.invoke(objectName, action, arguments, signature);

        // We need to wait for service/entity to deactivate...
        if(action.equals("deactivate")) {
          waitForServiceDeactivation(objectName, new Object[]{params[1]}, signature);
        }
        else if (action.equals("deactivateResourceAdaptorEntity")) {
          waitForResourceAdaptorEntityDeactivation(objectName, new Object[]{params[1]}, signature);
        }
      }
      catch (Exception e) {
        // We might expect some exceptions...
        if (e.getCause() instanceof ResourceAdaptorEntityAlreadyExistsException || (e.getCause() instanceof InvalidStateException 
            && action.equals("activateResourceAdaptorEntity"))) {
          String actionToAvoid = "";

          // If the activate/create failed then we don't want to deactivate/remove
          if (action.equals("activateResourceAdaptorEntity")) {
            actionToAvoid = "deactivateResourceAdaptorEntity";
          }
          else if (action.equals("createResourceAdaptorEntity")) {
            actionToAvoid = "removeResourceAdaptorEntity";
          }

          Collection<String> actionsToAvoid;
          if ((actionsToAvoid = actionsToAvoidByDU.get(du)) == null) {
            actionsToAvoid = new ArrayList<String>();

            // Add it to the list of actions to skip on undeploy
            actionsToAvoid.add(actionToAvoid);

            // And put it to the map
            actionsToAvoidByDU.put(du, actionsToAvoid);
          }
          else {
            // Add it to the list of actions to skip on undeploy
            actionsToAvoid.add(actionToAvoid);
          }

          logger.warn(e.getCause().getMessage());
        }
        else if (e.getCause() instanceof InvalidStateException && action.equals("deactivate")) {
          logger.info("Delaying uninstall due to service deactivation not complete.");
        }
        else if (e.getCause() instanceof InvalidStateException && action.equals("deactivateResourceAdaptorEntity")) {
          // ignore this... someone has already deactivated the link.
        }
        else if (e.getCause() instanceof UnrecognizedLinkNameException && action.equals("unbindLinkName")) {
          // ignore this... someone has already removed the link.
        }
        else if (deployActions.contains(action)) {
          logger.error("Failure invoking '" + action + "(" + Arrays.toString(arguments) + ") on " + objectName, e);
        }
        else {
          throw (e);
        }
      }

      // Wait a little while just to make sure it finishes
      Thread.sleep(waitTimeBetweenOperations);
    }
  }

  /**
   * Getter for the Deployed Components collection.
   * @return a Collection of Strings with the deployed components IDs.
   */
  public Collection<String> getDeployedComponents() {
    return deployedComponents;
  }

  /**
   * Method for showing current status of the Deployment Manager.
   * @return a HTML string with the status.
   */
  public String showStatus() {
    // Update the currently deployed components.
    updateDeployedComponents();

    String output = "";

    output += "<p>Deployable Units Waiting For Install:</p>";
    for (DeployableUnit waitingDU : waitingForInstallDUs) {
      output += "+-- " + waitingDU.getDeploymentInfoShortName() + "<br>";
      for (String dependency : waitingDU.getExternalDependencies()) {
        if (!deployedComponents.contains(dependency))
          dependency += " <strong>MISSING!</strong>";

        output += "  +-- depends on " + dependency + "<br>";
      }
    }

    output += "<p>Deployable Units Waiting For Uninstall:</p>";
    for (DeployableUnit waitingDU : waitingForUninstallDUs) {
      output += "+-- " + waitingDU.getDeploymentInfoShortName() + "<br>";
    }

    return output;
  }

  /**
   * Callback for {@link SleeStateJMXMonitor}, to learn when SLEE is stopping.
   */
  public void sleeIsStopping() {
    if (logger.isTraceEnabled()) {
		logger.trace("Got notified that SLEE is now stopping");
    }

    if(SLEESubDeployer.INSTANCE.isServerShuttingDown()) {
      // process all DUs that are on hold
      Set<DeployableUnit> duSet = new HashSet<DeployableUnit>(deployedDUs);
      deployedDUs.clear();
      for (DeployableUnit du : duSet) {
        try {
          uninstallDeployableUnit(du);            
        }
        catch (DeploymentException de) {
          // ignore, this has been dealt before
        }
        catch (Exception e) {
          logger.error("Failed to uninstall DU on hold, while SLEE is stopping",e);
        }
      }
    }
  }

  private boolean waitForServiceDeactivation(ObjectName objectName, Object params[], String signature[]) throws InterruptedException, InstanceNotFoundException, ReflectionException, MBeanException, MalformedObjectNameException, NullPointerException {
    // Get the MBeanServer
    MBeanServer ms = SleeContainer.lookupFromJndi().getMBeanServer();

    long waited = 0;

    ServiceState ss = (ServiceState) ms.invoke(new ObjectName(ServiceManagementMBean.OBJECT_NAME), "getState", params, signature);

    if(logger.isTraceEnabled()) {
		logger.trace(params[0] + " Initial State [" + ss.toString() + "].");
    }

    double maxWaitTime = 60 * 1000 * MobicentsManagement.entitiesRemovalDelay;

    while(!ss.isInactive() && waited <= maxWaitTime) {
      ss = (ServiceState) ms.invoke(objectName, "getState", params, signature);

      if(logger.isTraceEnabled()) {
    	  logger.trace(params[0] + " State [" + ss.toString() + "]. Waiting more " + waitTimeBetweenOperations + "ms. Waited a total of " + waited + "ms." );
      }

      Thread.sleep(waitTimeBetweenOperations);
      waited += waitTimeBetweenOperations;
    }

    return ss.isInactive();
  }

  private boolean waitForResourceAdaptorEntityDeactivation(ObjectName objectName, Object params[], String signature[]) throws InterruptedException, InstanceNotFoundException, ReflectionException, MBeanException, MalformedObjectNameException, NullPointerException {
    // Get the MBeanServer
    MBeanServer ms = SleeContainer.lookupFromJndi().getMBeanServer();

    long waited = 0;

    ResourceAdaptorEntityState raes = (ResourceAdaptorEntityState) ms.invoke(new ObjectName(ResourceManagementMBean.OBJECT_NAME), "getState", params, signature);

    if(logger.isTraceEnabled()) {
		logger.trace(params[0] + " Initial State [" + raes.toString() + "].");
    }

    double maxWaitTime = 60 * 1000 * MobicentsManagement.entitiesRemovalDelay;

    while(!raes.isInactive() && waited <= maxWaitTime) {

      raes = (ResourceAdaptorEntityState) ms.invoke(objectName, "getState", params, signature);

      if(logger.isTraceEnabled()) {
		logger.trace(params[0] + " State [" + raes.toString() + "]. Waiting more " + waitTimeBetweenOperations + "ms. Waited a total of " + waited + "ms." );
      }

      Thread.sleep(waitTimeBetweenOperations);
      waited += waitTimeBetweenOperations;
    }

    return raes.isInactive();
  }

}