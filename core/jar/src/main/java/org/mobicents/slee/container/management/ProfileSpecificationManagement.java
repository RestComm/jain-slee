package org.mobicents.slee.container.management;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.ComponentID;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.profile.ProfileDeployer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manages the profile specs installed on the container.
 * 
 * @author martins
 * 
 */
public class ProfileSpecificationManagement {

	private static final Logger logger = Logger
			.getLogger(ProfileSpecificationManagement.class.getName());

	private final SleeContainer sleeContainer;

	private final ConcurrentHashMap<ComponentID, ProfileSpecificationDescriptorImpl> descriptors;

	public ProfileSpecificationManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.descriptors = new ConcurrentHashMap<ComponentID, ProfileSpecificationDescriptorImpl>();
	}

	/**
	 * 
	 * Deploys a Profile specification. This generates the code to generate
	 * concrete classes and registers the component in the component table
	 * 
	 * @param profileSpecificationDescriptorImpl
	 *            the descriptor of the profile to install
	 * @throws DeploymentException
	 * @throws SystemException
	 * @throws Exception
	 */
	public void installProfileSpecification(
			final ProfileSpecificationDescriptorImpl profileSpecificationDescriptorImpl)
			throws DeploymentException {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing Profile "
					+ profileSpecificationDescriptorImpl.getID());
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		File duPath = ((DeployableUnitIDImpl) profileSpecificationDescriptorImpl
				.getDeployableUnit()).getDUDeployer()
				.getTempClassDeploymentDir();

		new ProfileDeployer(duPath)
				.deployProfile(profileSpecificationDescriptorImpl);

		descriptors.put(profileSpecificationDescriptorImpl.getID(),
				profileSpecificationDescriptorImpl);
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				descriptors.remove(profileSpecificationDescriptorImpl.getID());
				logger.info("Removed Profile "
						+ profileSpecificationDescriptorImpl.getID()
						+ " due to transaction rollback");
			}
		};
		sleeTransactionManager.addAfterRollbackAction(action);

		logger.info("Installed Profile "
				+ profileSpecificationDescriptorImpl.getID());
	}

	public ProfileSpecificationDescriptorImpl getProfileSpecificationDescriptor(
			ComponentID componentID) {
		return descriptors.get(componentID);
	}

	/**
	 * Get a list of profiles known to me
	 * 
	 * @return A list of profiles that are registered with me.
	 */
	public ProfileSpecificationID[] getProfileSpecificationIDs() {
		return descriptors.keySet().toArray(
				new ProfileSpecificationIDImpl[descriptors.size()]);
	}

	/**
	 * Uninstalls all profile specs in the specified deployable unit.
	 * @param deployableUnitID
	 */
	public void uninstallProfileSpecification(
			DeployableUnitIDImpl deployableUnitID) {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling profiles for du " + deployableUnitID);
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		for (final ProfileSpecificationDescriptorImpl descriptor : descriptors
				.values()) {

			if (descriptor.getDeployableUnit().equals(deployableUnitID)) {

				if (logger.isDebugEnabled())
					logger.debug("Uninstalling Profile " + descriptor.getID());

				descriptors.remove(descriptor.getID());
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						descriptors.put(descriptor.getID(), descriptor);
						logger.info("Reinstalled Profile " + descriptor.getID()
								+ " due to transaction rollback");
					}
				};
				sleeTransactionManager.addAfterRollbackAction(action);

				logger.info("Uninstalled Profile " + descriptor.getID());
			}
		}
	}

	@Override
	public String toString() {
		return "Profile Specification Management: " + "\n+-- Descriptors: "
				+ descriptors.keySet();
	}
}
