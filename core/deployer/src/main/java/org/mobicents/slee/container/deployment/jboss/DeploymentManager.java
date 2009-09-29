package org.mobicents.slee.container.deployment.jboss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.InvalidStateException;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;
import javax.slee.management.ResourceAdaptorEntityAlreadyExistsException;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.management.UnrecognizedLinkNameException;

import org.jboss.deployment.DeploymentException;
import org.jboss.logging.Logger;
import org.jboss.mx.loading.RepositoryClassLoader;
import org.mobicents.slee.container.SleeContainer;

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

	private ConcurrentHashMap<DeployableUnit, RepositoryClassLoader> replacedUCLs = new ConcurrentHashMap<DeployableUnit, RepositoryClassLoader>();

	// The components already deployed to SLEE
	private Collection<String> deployedComponents = new ConcurrentLinkedQueue<String>();

	// Actions using DeploymentMBean
	private Collection<String> deploymentActions = Arrays.asList(new String[] {
			"install", "uninstall" });

	// Actions using ResourceManagementMBean
	private Collection<String> resourceAdaptorActions = Arrays
			.asList(new String[] { "bindLinkName", "unbindLinkName",
					"createResourceAdaptorEntity",
					"removeResourceAdaptorEntity",
					"activateResourceAdaptorEntity",
					"deactivateResourceAdaptorEntity" });

	// Actions using ServiceManagementMBean
	private Collection<String> serviceActions = Arrays.asList(new String[] {
			"activate", "deactivate" });

	private Collection<String> deployActions = Arrays.asList(new String[] {
			"install", "activate", "bindLinkName",
			"createResourceAdaptorEntity", "activateResourceAdaptorEntity" });

	private ConcurrentHashMap<DeployableUnit, Collection<String>> actionsToAvoidByDU = new ConcurrentHashMap<DeployableUnit, Collection<String>>();

	public long waitTimeBetweenOperations = 250;
	
	private final SleeStateJMXMonitor sleeStateJMXMonitor;
	
	/**
	 * Constructor.
	 */
	private DeploymentManager() {
		sleeStateJMXMonitor = new SleeStateJMXMonitor(this);
	}

	/**
	 * Method for adding a new deployable unit to the manager.
	 * @param du the DeployableUnit object to add.
	 * @throws Exception
	 */
	public void addDeployableUnit(DeployableUnit du) throws Exception {
		// Get a fresh copy of the deployed components. 
		updateDeployedComponents();

		// Check if there aren't already deployed components in this DU...
		if (!deployedComponents.containsAll(du.getComponents())) {
			// Add it to the deploy list.
			waitingForInstallDUs.add(du);
		} else {
			logger.warn("Trying to deploy a duplicate DU ("
					+ du.getDeploymentInfoShortName() + ").");
		}
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
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getProfileSpecificationIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Event Types
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getEventComponentIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Resource Adaptor Types
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getResourceAdaptorTypeIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Resource Adaptors
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getResourceAdaptorIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Service Building Blocks (SBBs)
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getSbbIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Services
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getServiceIDs()) {
				newDeployedComponents.add(componentID.toString());
			}

			// Get the deployed Libraries
			for (ComponentID componentID: sleeContainer.getComponentRepositoryImpl().getLibraryIDs()) {
				newDeployedComponents.add(componentID.toString());
			}
			
			// Get the existing Resource Adaptor Entity links
			String[] entityNames = sleeContainer.getResourceManagement()
					.getResourceAdaptorEntities();

			for (String entityName : entityNames) {
				newDeployedComponents.addAll(Arrays.asList(sleeContainer.getResourceManagement().getLinkNames(entityName)));
			}
			
			// All good.. Make the temp the good one.
			deployedComponents = newDeployedComponents;
		} catch (Exception e) {
			logger.warn("Failure while updating deployed components.", e);
		}
	}
	
	/**
	 * Method for installing a Deployable Unit into SLEE.
	 * @param du the Deployable Unit to install.
	 * @throws Exception
	 */
	public void installDeployableUnit(DeployableUnit du) throws Exception {
		
	  SleeState state = sleeStateJMXMonitor.getSleeState();
		if (state != SleeState.RUNNING) {
			
			logger.warn("Unable to INSTALL " + du.getDeploymentInfoShortName()
					+ " right now. Waiting for SLEE to be in RUNNING state (" + state + ").");
			waitingForInstallDUs.add(du);
			return;
		}
		
		// Update the deployed components from SLEE
		updateDeployedComponents();

		// Check if the DU is ready to be installed
		if (du.isReadyToInstall(true)) {
			// Get and Run the actions needed for installing this DU
			sciAction(du.getInstallActions(), du);

			// Set the DU as installed
			du.setInstalled(true);

			// Update the deployed components from SLEE
			updateDeployedComponents();

			// Go through the remaining DUs waiting for installation
			Iterator<DeployableUnit> duIt = waitingForInstallDUs.iterator();

			while (duIt.hasNext()) {
				DeployableUnit waitingDU = duIt.next();

				// If it is ready for installation, follow the same procedure
				if (waitingDU.isReadyToInstall(false)) {
					// Get and Run the actions needed for installing this DU
					sciAction(waitingDU.getInstallActions(), du);

					// Set the DU as installed
					waitingDU.setInstalled(true);

					// Update the deployed components from SLEE
					updateDeployedComponents();

					// Remove the DU from the waiting list.
					waitingForInstallDUs.remove(waitingDU);

					// Let's start all over.. :)
					duIt = waitingForInstallDUs.iterator();
				}
			}
		} else {
			logger.warn("Unable to INSTALL " + du.getDeploymentInfoShortName()
					+ " right now. Waiting for dependencies to be resolved.");

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
		
	  SleeState state = sleeStateJMXMonitor.getSleeState();
		if (state != SleeState.RUNNING && state != SleeState.STOPPING) {
			logger.warn("Unable to UNINSTALL " + du.getDeploymentInfoShortName()
					+ " right now. Waiting for SLEE to be in RUNNING/STOPPING state (" + state + ").");
			waitingForUninstallDUs.add(du);
			return;
		}
		
		// Update the deployed components from SLEE
		updateDeployedComponents();

		// It isn't installed?
		if (!du.isInstalled()) {
			// Then it should be in the waiting list... remove and we're done.
			if (waitingForInstallDUs.remove(du)) {
				logger.info(du.getDeploymentInfoShortName()
						+ " wasn't deployed. Removing from waiting list.");
			}
		}
		// Check if the DU is ready to be uninstalled
		else if (du.isReadyToUninstall()) {
			// Get and Run the actions needed for uninstalling this DU
			sciAction(du.getUninstallActions(), du);

			// Set the DU as not installed
			du.setInstalled(false);

			// Remove if it was present in waiting list
			waitingForUninstallDUs.remove(du);
			
			// Update the deployed components from SLEE
			updateDeployedComponents();

			// Unregister the replaced UCL, if any
			RepositoryClassLoader repositoryClassLoader = replacedUCLs
					.remove(du);
			if (repositoryClassLoader != null) {
				repositoryClassLoader.unregister();
				if (logger.isDebugEnabled()) {
					logger.debug("replacedUCLs size is " + replacedUCLs.size()
							+ " after removing DU "
							+ du.getDeploymentInfoShortName());
				}
			}

			// Go through the remaining DUs waiting for uninstallation
			Iterator<DeployableUnit> duIt = waitingForUninstallDUs.iterator();

			while (duIt.hasNext()) {
				DeployableUnit waitingDU = duIt.next();

				// If it is ready for being uninstalled, follow the same procedure
				if (waitingDU.isReadyToUninstall()) {
					// Get and Run the actions needed for uninstalling this DU
					//sciAction(waitingDU.getUninstallActions(), du);

				  // Schedule removal
          SLEESubDeployer.INSTANCE.stop(new DeployableUnitWrapper(waitingDU.getURL()));

					// Set the DU as not installed
					// waitingDU.setInstalled(false);

					// Update the deployed components from SLEE
					// updateDeployedComponents();

					// Remove the DU from the waiting list.
					waitingForUninstallDUs.remove(waitingDU);

					// Unregister the replaced UCL, if any
					repositoryClassLoader = replacedUCLs.remove(waitingDU);
					if (repositoryClassLoader != null) {
						repositoryClassLoader.unregister();
						if (logger.isDebugEnabled()) {
							logger.debug("replacedUCLs size is "
									+ replacedUCLs.size()
									+ " after removing DU "
									+ du.getDeploymentInfoShortName());
						}
					}

					// Let's start all over.. :)
					duIt = waitingForUninstallDUs.iterator();
				}
			}
		} else {
			// Have we been her already? If so, don't flood user with log messages...
			if (!waitingForUninstallDUs.contains(du)) {
				// Add it to the waiting list.
				waitingForUninstallDUs.add(du);

				logger.warn("Unable to UNINSTALL "
						+ du.getDeploymentInfoShortName()
						+ " right now. Waiting for dependents to be removed.");
			}

			// But we have to throw this so task knows that it needs to retry
			throw new DeploymentException("Unable to UNINSTALL "
					+ du.getDeploymentInfoShortName()
					+ " right now. Waiting for dependents to be removed.");
		}
	}

	public void addReplacedUCL(DeployableUnit du, RepositoryClassLoader ucl) {
		if (ucl != null)
			this.replacedUCLs.put(du, ucl);
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
			;
			String[] signature = new String[params.length - 1];

			// Get the action to perform
			String action = (String) params[0];

			// Shall we skip this action?
			if (actionsToAvoidByDU.get(du) != null
					&& actionsToAvoidByDU.get(du).remove(action)) {
				// Clean if it was the last one.
				if (actionsToAvoidByDU.get(du).size() == 0)
					actionsToAvoidByDU.remove(du);

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
					DeployableUnitID duID = (DeployableUnitID) ms.invoke(
							objectName, "getDeployableUnit",
							new Object[] { params[1] },
							new String[] { params[1].getClass().getName() });

					// Update arguments and signature
					arguments[0] = duID;
					signature[0] = "javax.slee.management.DeployableUnitID";
				}
			} else if (resourceAdaptorActions.contains(action)) {
				// Set the corresponding ObjectName
				objectName = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

				// Special case as arg is ResourceAdaptorIDImpl..
				if (action.equals("createResourceAdaptorEntity"))
					signature[0] = "javax.slee.resource.ResourceAdaptorID";
			} else if (serviceActions.contains(action)) {
				// Set the corresponding ObjectName
				objectName = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

				// Special case as arg is ServiceIDImpl..
				signature[0] = "javax.slee.ServiceID";
			}

			if (logger.isDebugEnabled())
				logger.debug("Invoking " + action + "("
						+ Arrays.toString(signature) + ") on " + objectName);

			// We are isolating each action, so it won't affect the whole proccess
			try {
				// Invoke it.
				ms.invoke(objectName, action, arguments, signature);

				// We need to wait for service to deactivate...
				//        if(action.equals( "deactivate" ))
				//        {
				//          long waited = 0;
				//          
				//          ServiceState ss = null;
				//          
				//          double maxWaitTime = 60 * 1000 * MobicentsManagement.entitiesRemovalDelay;
				//          
				//          while(true)
				//          {
				//            ss = (ServiceState) ms.invoke( objectName, "getState", arguments, signature );
				//            
				//            if(logger.isDebugEnabled())
				//              logger.debug( arguments[0] + " State [" + ss.toString() + "]." );
				//
				//            if(!ss.isInactive() && waited <= maxWaitTime)
				//            {
				//              if(logger.isDebugEnabled())
				//                logger.debug( "Waiting more " + waitTimeBetweenOperations + "ms. Waited a total of " + waited + "ms."  );
				//              
				//              Thread.sleep( waitTimeBetweenOperations );
				//              waited += waitTimeBetweenOperations;
				//            }
				//            else
				//            {
				//              if(logger.isDebugEnabled())
				//                logger.debug( "Service already deactivated or maximum wait time reached. Moving on!" );
				//              
				//              break;
				//            }
				//          }
				//        }
			} catch (Exception e) {
				// We might expect some exceptions...
				if (e.getCause() instanceof ResourceAdaptorEntityAlreadyExistsException
						|| (e.getCause() instanceof InvalidStateException && action
								.equals("activateResourceAdaptorEntity"))) {
					String actionToAvoid = "";

					// If the activate/create failed then we don't want to deactivate/remove
					if (action.equals("activateResourceAdaptorEntity"))
						actionToAvoid = "deactivateResourceAdaptorEntity";
					else if (action.equals("createResourceAdaptorEntity"))
						actionToAvoid = "removeResourceAdaptorEntity";

					Collection actionsToAvoid;
					if ((actionsToAvoid = actionsToAvoidByDU.get(du)) == null) {
						actionsToAvoid = new ArrayList();

						// Add it to the list of actions to skip on undeploy
						actionsToAvoid.add(actionToAvoid);

						// And put it to the map
						actionsToAvoidByDU.put(du, actionsToAvoid);
					} else {
						// Add it to the list of actions to skip on undeploy
						actionsToAvoid.add(actionToAvoid);
					}

					logger.warn(e.getCause().getMessage());
				} else if (e.getCause() instanceof InvalidStateException
						&& action.equals("deactivate")) {
					logger
							.info("Delaying uninstall due to service deactivation not complete.");
				} else if (e.getCause() instanceof InvalidStateException
						&& action.equals("deactivateResourceAdaptorEntity")) {
					// ignore this... someone has already deactivated the link.
				} else if (e.getCause() instanceof UnrecognizedLinkNameException
						&& action.equals("unbindLinkName")) {
					// ignore this... someone has already removed the link.
				} else if (deployActions.contains(action)) {
					logger.error(
							"Failure invoking '" + action + "("
									+ Arrays.toString(arguments) + ") on "
									+ objectName, e);
				} else {
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
	 * Callback for {@link SleeStateJMXMonitor}, to learn when SLEE is running.
	 */
	public void sleeIsRunning() {
		if (logger.isDebugEnabled()) {
			logger.debug("Got notified that SLEE is now in running state");
		}

		// process all DUs that are on hold
		Set<DeployableUnit> duSet = new HashSet<DeployableUnit>(waitingForUninstallDUs);
		waitingForUninstallDUs.clear();
		for (DeployableUnit du : duSet) {
			try {
				uninstallDeployableUnit(du);						
			} catch (Exception e) {
				logger.error("Failed to uninstall DU on hold, after SLEE is running",e);
			}
		}
		duSet = new HashSet<DeployableUnit>(waitingForInstallDUs);
		waitingForInstallDUs.clear();
		for (DeployableUnit du : duSet) {
			try {
				installDeployableUnit(du);
			} catch (Exception e) {
				logger.error("Failed to install DU on hold, after SLEE is running",e);
			}
		}
	}				
	
	
	
}