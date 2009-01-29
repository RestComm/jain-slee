package org.mobicents.slee.container.management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeploymentException;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentIDImpl;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorDescriptorImpl;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class ComponentManagement {

	private final static Logger logger = Logger
			.getLogger(ComponentManagement.class);

	private static final ComponentID[] noComponentIDs = new ComponentID[0];

	private ConcurrentHashMap<ComponentID, ConcurrentHashMap<ComponentID, ComponentID>> referringComponents;

	private final SleeContainer sleeContainer;

	public ComponentManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.referringComponents = new ConcurrentHashMap<ComponentID, ConcurrentHashMap<ComponentID, ComponentID>>();
	}

	/**
	 * Add a referring component to a given component ID. This is only a table
	 * of direct references. A component cannot be uninstalled if there are any
	 * components that refer to it.
	 * 
	 * @param toComponent
	 * @param fromComponent
	 * @throws DeploymentException
	 */
	public void addComponentDependency(final ComponentID toComponent,
			final ComponentID fromComponent) throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Creating dependency from component " + fromComponent
					+ " to component " + toComponent);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		ConcurrentHashMap<ComponentID, ComponentID> map = referringComponents
				.get(toComponent);
		if (map != null) {
			if (map.putIfAbsent(fromComponent, fromComponent) == null) {
				logger.info("Created dependency from component "
						+ fromComponent + " to component " + toComponent);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						ConcurrentHashMap<ComponentID, ComponentID> map = referringComponents
								.get(toComponent);
						if (map != null && map.remove(fromComponent) != null) {
							logger.info("Removed dependency from component "
									+ fromComponent + " to component "
									+ toComponent
									+ " due to transaction rollback");
						}
					}
				};
				sleeTransactionManager.addAfterRollbackAction(action);
			}
		} else {
			throw new DeploymentException("Component " + fromComponent
					+ " trying to refer a component " + toComponent
					+ " that is not installed");
		}
	}

	/**
	 * This is called by our management interface.
	 * 
	 * @param referredComponent
	 * @return
	 */
	public ComponentID[] getReferringComponents(ComponentID referredComponent) {

		ConcurrentHashMap<ComponentID, ComponentID> map = referringComponents
				.get(referredComponent);
		if (map != null) {
			Set<ComponentID> set = map.keySet();
			return set.toArray(new ComponentID[set.size()]);
		} else {
			return noComponentIDs;
		}
	}

	/**
	 * This is done pre-removal. Go through the component table and remove all
	 * places where the components refer to other components, and remove it's
	 * map of dependencies.
	 * 
	 */
	public void removeComponentDependencies(final ComponentID componentID) {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing dependencies from component " + componentID
					+ " to all components");
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		// dependencies to others
		for (final ComponentID other : referringComponents.keySet()) {
			ConcurrentHashMap<ComponentID, ComponentID> map = referringComponents
					.get(other);
			if (map != null && map.remove(componentID) != null) {
				logger.info("Removed dependency from component " + componentID
						+ " to component " + other);
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						ConcurrentHashMap<ComponentID, ComponentID> map = referringComponents
								.get(other);
						if (map != null
								&& map.putIfAbsent(componentID, componentID) == null) {
							logger.info("Recreated dependency from component "
									+ componentID + " to component " + other
									+ " due to transaction rollback");
						}
					}
				};
				sleeTransactionManager.addAfterRollbackAction(action);
			}
		}

		// dependencies from others, should be empty
		if (referringComponents.remove(componentID) != null) {
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					referringComponents.putIfAbsent(componentID,
							new ConcurrentHashMap<ComponentID, ComponentID>());
				}
			};
			sleeTransactionManager.addAfterRollbackAction(action);		
		}
	}

	/**
	 * Initiates all resources need to track dependencies from other components
	 * to the one with the specified component ID.
	 * 
	 * @param componentID
	 */
	public void initComponentReferencesMap(final ComponentID componentID) {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();

		boolean b = sleeTransactionManager.requireTransaction();

		if (referringComponents.putIfAbsent(componentID,
				new ConcurrentHashMap<ComponentID, ComponentID>()) == null) {
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					referringComponents.remove(componentID);
				}
			};
			sleeTransactionManager.addAfterRollbackAction(action);
		}
	}

	// management logic

	/**
	 * Installs a component in the container
	 * 
	 * @param descriptor
	 *            a descriptor of the component to deploy.
	 */
	public void install(ComponentDescriptor descriptor,
			DeployableUnitDescriptor duDescriptor) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing " + descriptor);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		boolean b = sleeTransactionManager.requireTransaction();
		try {
			boolean initComponentReferencesMap = true;
			if (descriptor instanceof MobicentsSbbDescriptor) {
				sleeContainer.getSbbManagement().installSbb(
						(MobicentsSbbDescriptor) descriptor);
				// already initiated
				initComponentReferencesMap = false;
			} else if (descriptor instanceof ServiceDescriptorImpl) {
				sleeContainer.getServiceManagement().installService(
						(ServiceDescriptorImpl) descriptor);
				// there are no refs to services
				initComponentReferencesMap = false;
			} else if (descriptor instanceof MobicentsEventTypeDescriptor) {
				sleeContainer.getEventManagement().installEventType(
						(MobicentsEventTypeDescriptor) descriptor);
			} else if (descriptor instanceof ResourceAdaptorTypeDescriptor) {
				sleeContainer.getResourceManagement()
						.installResourceAdaptorType(
								(ResourceAdaptorTypeDescriptorImpl) descriptor);
			} else if (descriptor instanceof ProfileSpecificationDescriptorImpl) {
				sleeContainer
						.getSleeProfileManager()
						.getProfileSpecificationManagement()
						.installProfileSpecification(
								(ProfileSpecificationDescriptorImpl) descriptor);
			} else if (descriptor instanceof ResourceAdaptorDescriptorImpl) {
				sleeContainer.getResourceManagement().installResourceAdaptor(
						(ResourceAdaptorDescriptorImpl) descriptor);
			} else {
				throw new DeploymentException("unknown component type!");
			}
			// init references map for this component if needed
			if (initComponentReferencesMap) {
				initComponentReferencesMap(descriptor.getID());
			}
		} catch (Exception ex) {
			logger.error("Exception caught while installing component", ex);
			throw ex;
		} finally {
			if (b)
				sleeTransactionManager.commit();
		}
	}

	/**
	 * Get the descriptor of a component given its component ID
	 * 
	 * @param componentId --
	 *            component id for which we want the descriptor
	 * 
	 * @return the descriptor corresponding to the component id.
	 */

	public ComponentDescriptor getComponentDescriptor(ComponentID componentId)
			throws IllegalArgumentException {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		boolean b = sleeTransactionManager.requireTransaction();
		try {
			ComponentIDImpl cidImpl = (ComponentIDImpl) componentId;
			if (cidImpl.isSbbID()) {
				return sleeContainer.getSbbManagement().getSbbComponent(
						componentId);
			} else if (cidImpl.isServiceID()) {
				return sleeContainer.getServiceManagement()
						.getServiceComponent((ServiceID) componentId)
						.getServiceDescriptor();
			} else if (cidImpl.isProfileSpecificationID()) {
				return sleeContainer.getSleeProfileManager()
						.getProfileSpecificationManagement()
						.getProfileSpecificationDescriptor(componentId);
			} else if (cidImpl.isEventTypeID()) {
				return sleeContainer.getEventManagement().getEventDescriptor(
						(EventTypeID) componentId);
			} else if (cidImpl.isResourceAdaptorTypeID()) {
				return sleeContainer.getResourceManagement()
						.getResourceAdaptorType(
								(ResourceAdaptorTypeID) componentId)
						.getRaTypeDescr();
			} else if (cidImpl instanceof ResourceAdaptorIDImpl) {
				return sleeContainer.getResourceManagement()
						.getInstalledResourceAdaptor(
								(ResourceAdaptorID) componentId)
						.getDescriptor();
			} else
				throw new IllegalArgumentException("unknown component id");
		} catch (Exception ex) {
			throw new RuntimeException("Failed to get component " + componentId
					+ " descriptor", ex);
		} finally {
			try {
				if (b)
					sleeTransactionManager.commit();
			} catch (SystemException ex) {
				throw new RuntimeException(
						"Tx manager failed when getting component descriptor!",
						ex);
			}
		}
	}

	/**
	 * Checks if the component is installed.
	 * 
	 * @param componentId
	 * @return
	 */
	public boolean isInstalled(ComponentID componentId) {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		boolean b = sleeTransactionManager.requireTransaction();
		try {

			ComponentIDImpl cidImpl = (ComponentIDImpl) componentId;
			if (cidImpl.isSbbID()) {
				return sleeContainer.getSbbManagement().getSbbComponent(
						componentId) != null;
			} else if (cidImpl.isServiceID()) {
				return sleeContainer.getServiceManagement()
						.getServiceComponent((ServiceID) componentId) != null;
			} else if (cidImpl.isProfileSpecificationID()) {
				return sleeContainer.getSleeProfileManager()
						.getProfileSpecificationManagement()
						.getProfileSpecificationDescriptor(componentId) != null;
			} else if (cidImpl.isEventTypeID()) {
				return sleeContainer.getEventManagement().isInstalled(
						(EventTypeID) componentId);
			} else if (cidImpl.isResourceAdaptorTypeID()) {
				return sleeContainer.getResourceManagement()
						.getResourceAdaptorType(
								(ResourceAdaptorTypeID) componentId) != null;
			} else if (cidImpl instanceof ResourceAdaptorIDImpl) {
				return sleeContainer.getResourceManagement()
						.getInstalledResourceAdaptor(
								(ResourceAdaptorID) componentId) != null;
			} else
				return false;
		} catch (Exception ex) {
			throw new RuntimeException("Failed to check component "
					+ componentId + " is installed", ex);
		} finally {
			try {
				if (b)
					sleeTransactionManager.commit();
			} catch (SystemException ex) {
				throw new RuntimeException(
						"Tx Manager failed when checking component "
								+ componentId + " is installed", ex);
			}
		}
	}

	/**
	 * Return a list of component descriptors for the given list of component
	 * ids.
	 * 
	 * @param componentIds
	 * 
	 * @return an array of component descriptors.
	 * 
	 */
	public ComponentDescriptor[] getDescriptors(ComponentID[] componentIds) {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		boolean b = sleeTransactionManager.requireTransaction();

		try {
			ArrayList<ComponentDescriptor> knownComps = new ArrayList<ComponentDescriptor>();
			for (int i = 0; i < componentIds.length; i++) {
				if (isInstalled(componentIds[i])) {
					// may be too strict (spec says 'recognized', 14.7.8, p214)

					ComponentDescriptor descr = this
							.getComponentDescriptor(componentIds[i]);
					knownComps.add(descr);

				}
			}
			return knownComps
					.toArray(new ComponentDescriptor[knownComps.size()]);
		} finally {
			try {
				if (b)
					sleeTransactionManager.commit();
			} catch (SystemException ex) {
				throw new RuntimeException(
						"tx manager failed when getting descriptors for components: "
								+ Arrays.asList(componentIds), ex);
			}
		}
	}

	@Override
	public String toString() {
		return "Component Management: " + "\n+-- Referring components: "
				+ referringComponents.keySet();
	}
}
