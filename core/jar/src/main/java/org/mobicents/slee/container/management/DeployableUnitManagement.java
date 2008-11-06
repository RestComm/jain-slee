package org.mobicents.slee.container.management;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitDescriptorImpl;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class DeployableUnitManagement {

	private final static Logger logger = Logger
			.getLogger(DeployableUnitManagement.class);

	private ConcurrentHashMap<DeployableUnitID, DeployableUnitDescriptorImpl> deployableUnitDescriptors;
	private final SleeContainer sleeContainer;

	public DeployableUnitManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.deployableUnitDescriptors = new ConcurrentHashMap<DeployableUnitID, DeployableUnitDescriptorImpl>();
	}

	/**
	 * Adds a deployable unit given its descriptor.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public void addDeployableUnit(final DeployableUnitDescriptorImpl descriptor) {

		if (logger.isDebugEnabled()) {
			logger.debug("Adding DU with descriptor:  " + descriptor);
		}

		if (descriptor == null)
			throw new NullPointerException("null descriptor");

		sleeContainer.getTransactionManager().mandateTransaction();

		// store descriptor
		deployableUnitDescriptors.put(descriptor.getDeployableUnit(),
				descriptor);
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				deployableUnitDescriptors
						.remove(descriptor.getDeployableUnit());
			}
		};
		sleeContainer.getTransactionManager().addAfterRollbackAction(action);
	}

	/**
	 * Get an array containing the deployable unit ids known to the container.
	 * 
	 * @return
	 */
	public DeployableUnitID[] getDeployableUnits() {
		Set<DeployableUnitID> deployableUnitIDs = deployableUnitDescriptors
				.keySet();
		return deployableUnitIDs
				.toArray(new DeployableUnitIDImpl[deployableUnitIDs.size()]);
	}

	/**
	 * Get the deployable unit descriptor for a given deployable unit.
	 * 
	 * @param deployableUnitID --
	 *            the deployable unit id
	 * 
	 * @return
	 */
	public DeployableUnitDescriptorImpl getDeployableUnitDescriptor(
			DeployableUnitID deployableUnitID) {
		return deployableUnitDescriptors.get(deployableUnitID);
	}

	/**
	 * Get the deployable unit descriptor for a given file url.
	 * 
	 * @param deployableUnitUrl
	 * @return
	 */
	public DeployableUnitDescriptorImpl getDeployableUnitDescriptor(
			String deployableUnitUrl) {
		for (DeployableUnitDescriptorImpl descriptor : deployableUnitDescriptors
				.values()) {
			if (descriptor.getURL().equals(deployableUnitUrl)) {
				return descriptor;
			}
		}
		return null;
	}

	/**
	 * Adds a deployable unit given its descriptor.
	 * 
	 * @param descriptor -
	 *            descriptor to register
	 * 
	 */
	public void removeDeployableUnit(final DeployableUnitID deployableUnitID) {

		if (logger.isDebugEnabled()) {
			logger.debug("Removing DU with id:  " + deployableUnitID);
		}

		if (deployableUnitID == null)
			throw new NullPointerException("null id");

		sleeContainer.getTransactionManager().mandateTransaction();

		// remove descriptor
		final DeployableUnitDescriptorImpl descriptor = deployableUnitDescriptors
				.remove(deployableUnitID);
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				deployableUnitDescriptors.put(deployableUnitID, descriptor);
			}
		};
		sleeContainer.getTransactionManager().addAfterRollbackAction(action);
	}

	/**
	 * @return
	 */
	public DeployableUnitDescriptor[] getDeployableUnitDescriptors() {
		final Collection<DeployableUnitDescriptorImpl> descriptors = deployableUnitDescriptors
				.values();
		return descriptors.toArray(new DeployableUnitDescriptorImpl[descriptors
				.size()]);
	}

	/**
	 * Method for checking if this DU components are referred by any others.
	 * 
	 * @return true if there are other DUs installed referring this.
	 */
	public boolean hasReferringDU(DeployableUnitDescriptorImpl descriptor) {

		// 1st build a set with the Components IDs from the descriptor
		HashSet<ComponentID> duComponents = new HashSet<ComponentID>();
		for (ComponentID componentID : descriptor.getComponents()) {
			duComponents.add(componentID);
		}
		// now for each component...
		for (ComponentID componentID : descriptor.getComponents()) {
			// process referring set of that component
			for (ComponentID referringComponentID : sleeContainer
					.getComponentManagement().getReferringComponents(
							componentID)) {
				// if component is not from this du there is a referring du
				// somewhere
				if (!duComponents.contains(referringComponentID)) {
					return true;
				}
			}
		}

		// If we got here
		return false;
	}

	@Override
	public String toString() {
		return "Deployable Unit Management: " + "\n+-- Deployable Unit IDs: "
				+ deployableUnitDescriptors.keySet();
	}
}
