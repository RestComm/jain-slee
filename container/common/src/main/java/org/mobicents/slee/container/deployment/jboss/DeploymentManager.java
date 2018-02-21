/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.deployment.jboss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.slee.ComponentID;
import javax.slee.InvalidStateException;
import javax.slee.management.DependencyException;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.UnrecognizedLinkNameException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.deployment.jboss.action.ActivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.CreateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateServiceAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.deployment.jboss.action.RemoveResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.UnbindLinkNameAction;
import org.mobicents.slee.container.management.ResourceManagement;

/**
 * This class represents the Manager responsible for executing deployment actions
 * and for controlling dependencies and monitoring new deployments using the deployer.
 * 
 * @author Alexandre Mendonça
 * @author martins
 * @version 1.0
 */
public class DeploymentManager {

  // The Logger.
  private static Logger logger = Logger.getLogger(DeploymentManager.class);

  // The DUs waiting to be installed.
  private Collection<DeployableUnit> waitingForInstallDUs = new ConcurrentLinkedQueue<DeployableUnit>();

  // The DUs waiting for being uninstalled.
  private Collection<DeployableUnit> waitingForUninstallDUs = new ConcurrentLinkedQueue<DeployableUnit>();

  // The DUs waiting for being uninstalled.
  private LinkedBlockingDeque<DeployableUnit> deployedDUs = new LinkedBlockingDeque<DeployableUnit>();

  // The components already deployed to SLEE
  private Collection<String> deployedComponents = new ConcurrentLinkedQueue<String>();

  private ConcurrentHashMap<DeployableUnit, Collection<Class<? extends ManagementAction>>> actionsToAvoidByDU = new ConcurrentHashMap<DeployableUnit, Collection<Class<? extends ManagementAction>>>();

  public long waitTimeBetweenOperations = 250;

  private final SleeContainerDeployerImpl sleeContainerDeployer;
  
  public DeploymentManager(SleeContainerDeployerImpl sleeContainerDeployer) {
	this.sleeContainerDeployer = sleeContainerDeployer;
  }

  /**
   * Updates the list of components already deployed to SLEE.
   */
  public void updateDeployedComponents() {
    try {
      // Get the SLEE Component Repo
      ComponentRepository componentRepository = sleeContainerDeployer.getSleeContainer().getComponentRepository();

      // First we'll put the components in a temp Collection
      ConcurrentLinkedQueue<String> newDeployedComponents = new ConcurrentLinkedQueue<String>();

      // Get the deployed Profile Specifications
      for (ComponentID componentID: componentRepository.getProfileSpecificationIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Event Types
      for (ComponentID componentID: componentRepository.getEventComponentIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Resource Adaptor Types
      for (ComponentID componentID: componentRepository.getResourceAdaptorTypeIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Resource Adaptors
      for (ComponentID componentID: componentRepository.getResourceAdaptorIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Service Building Blocks (SBBs)
      for (ComponentID componentID: componentRepository.getSbbIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Services
      for (ComponentID componentID: componentRepository.getServiceIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      // Get the deployed Libraries
      for (ComponentID componentID: componentRepository.getLibraryIDs()) {
        newDeployedComponents.add(componentID.toString());
      }

      ResourceManagement resourceManagement = sleeContainerDeployer.getSleeContainer().getResourceManagement();
      
      // Get the existing Resource Adaptor Entity links
      String[] entityNames = resourceManagement.getResourceAdaptorEntities();

      for (String entityName : entityNames) {
        newDeployedComponents.addAll(Arrays.asList(resourceManagement.getLinkNames(entityName)));
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
      throw new DependencyException("Unable to undeploy "+du.getDeploymentInfoShortName());
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
        sleeContainerDeployer.getSleeSubDeployer().stop(waitingDU.getURL(), waitingDU.getDeploymentInfoShortName());

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
  private void sciAction(Collection<ManagementAction> actions, DeployableUnit du)
  throws Exception {
   
    // For each action, get the params..
    for (ManagementAction action : actions) {
     
      // Shall we skip this action?
      if (actionsToAvoidByDU.get(du) != null && actionsToAvoidByDU.get(du).remove(action)) {
        // Clean if it was the last one.
        if (actionsToAvoidByDU.get(du).size() == 0) {
          actionsToAvoidByDU.remove(du);
        }
        continue;
      }

      if (logger.isTraceEnabled()) {
        logger.trace("Invoking " + action);
      }

      // We are isolating each action, so it won't affect the whole proccess
      try {
        // Invoke it.
        action.invoke();
        // We need to wait for service/entity to deactivate...
        /*if(action instanceof DeactivateServiceAction) {
          waitForServiceDeactivation((DeactivateServiceAction) action);
        }
        else if (action instanceof DeactivateResourceAdaptorEntityAction) {
          waitForResourceAdaptorEntityDeactivation((DeactivateResourceAdaptorEntityAction) action);
        }*/
      }
      catch (Exception e) {
        // We might expect some exceptions...
        if (e.getCause() instanceof ResourceAdaptorEntityAlreadyExistsException || (e.getCause() instanceof InvalidStateException 
            && action instanceof ActivateResourceAdaptorEntityAction)) {
          Class<? extends ManagementAction> actionToAvoid = null;

          // If the activate/create failed then we don't want to deactivate/remove
          if (action instanceof ActivateResourceAdaptorEntityAction) {
            actionToAvoid = DeactivateResourceAdaptorEntityAction.class;
          }
          else if (action instanceof CreateResourceAdaptorEntityAction) {
            actionToAvoid = RemoveResourceAdaptorEntityAction.class;
          }

          Collection<Class<? extends ManagementAction>> actionsToAvoid;
          if ((actionsToAvoid = actionsToAvoidByDU.get(du)) == null) {
            actionsToAvoid = new ArrayList<Class<? extends ManagementAction>>();

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
        else if (e.getCause() instanceof InvalidStateException && action instanceof DeactivateServiceAction) {
          logger.info("Delaying uninstall due to service deactivation not complete.");
        }
        else if (e.getCause() instanceof InvalidStateException && action instanceof DeactivateResourceAdaptorEntityAction) {
          // ignore this... someone has already deactivated the link.
        }
        else if (e.getCause() instanceof UnrecognizedLinkNameException && action instanceof UnbindLinkNameAction) {
          // ignore this... someone has already removed the link.
        }
        else if (action.getType() == ManagementAction.Type.DEPLOY_MANAGEMENT) {
          logger.error("Failure invoking '" + action, e);
        }
        else {
          throw e;
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

  public Collection<DeployableUnit> getWaitingForInstallDUs() {
	return waitingForInstallDUs;
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
  public void sleeShutdown() {
	  logger.info("Undeploying all Deployable Units due to SLEE shutdown");    
	  // undeploy each DU in reverse order 
	  while(!deployedDUs.isEmpty()) {
		  DeployableUnit du = deployedDUs.removeLast();
		  try {
			  uninstallDeployableUnit(du);            
		  }
		  catch (Exception e) {
			  logger.error("Failed to uninstall DU, in SLEE shutdown",e);
		  }
	  }
  }

  /*
  private boolean waitForServiceDeactivation(DeactivateServiceAction action) throws InterruptedException, NullPointerException, UnrecognizedServiceException, ManagementException {
   
    long waited = 0;
    
    final ServiceManagement serviceManagement = sleeContainerDeployer.getSleeContainer().getServiceManagement();
    
    ServiceState ss = serviceManagement.getState(action.getServiceID());

    if(logger.isTraceEnabled()) {
		logger.trace(action.getServiceID() + " Initial State [" + ss.toString() + "].");
    }

    double maxWaitTime = 60 * 1000 * MobicentsManagement.entitiesRemovalDelay;

    while(!ss.isInactive() && waited <= maxWaitTime) {
      ss = serviceManagement.getState(action.getServiceID());

      if(logger.isTraceEnabled()) {
    	  logger.trace(action.getServiceID() + " State [" + ss.toString() + "]. Waiting more " + waitTimeBetweenOperations + "ms. Waited a total of " + waited + "ms." );
      }

      Thread.sleep(waitTimeBetweenOperations);
      waited += waitTimeBetweenOperations;
    }

    return ss.isInactive();
  }

  private boolean waitForResourceAdaptorEntityDeactivation(DeactivateResourceAdaptorEntityAction action) throws InterruptedException, NullPointerException, UnrecognizedResourceAdaptorEntityException {
    
    long waited = 0;

    final ResourceManagement resourceManagement = sleeContainerDeployer.getSleeContainer().getResourceManagement();
    
    ResourceAdaptorEntityState raes = resourceManagement.getState(action.getRaEntity());

    if(logger.isTraceEnabled()) {
		logger.trace(action.getRaEntity() + " RA Entity Initial State [" + raes.toString() + "].");
    }

    double maxWaitTime = 60 * 1000 * MobicentsManagement.entitiesRemovalDelay;

    while(!raes.isInactive() && waited <= maxWaitTime) {

      raes = resourceManagement.getState(action.getRaEntity());

      if(logger.isTraceEnabled()) {
		logger.trace(action.getRaEntity() + " RA Entity State [" + raes.toString() + "]. Waiting more " + waitTimeBetweenOperations + "ms. Waited a total of " + waited + "ms." );
      }

      Thread.sleep(waitTimeBetweenOperations);
      waited += waitTimeBetweenOperations;
    }

    return raes.isInactive();
  }
 */
  
}