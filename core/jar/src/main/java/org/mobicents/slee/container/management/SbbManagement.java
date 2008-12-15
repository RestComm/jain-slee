package org.mobicents.slee.container.management;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.NamingException;
import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.facilities.Level;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.deployment.SbbDeployer;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolManagement;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manages sbbs in container
 * 
 * @author martins
 * 
 */
public class SbbManagement {

	private static final Logger logger = Logger.getLogger(SbbManagement.class);

	private final SleeContainer sleeContainer;
	
	// stores sbb descriptors
	private final ConcurrentHashMap<ComponentID, MobicentsSbbDescriptor> sbbDescriptors;

	// object pool management
	private final SbbObjectPoolManagement sbbPoolManagement;

	public SbbManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.sbbDescriptors = new ConcurrentHashMap<ComponentID, MobicentsSbbDescriptor>();
		this.sbbPoolManagement = new SbbObjectPoolManagement(sleeContainer);
		this.sbbPoolManagement.register();
	}

	public SbbObjectPoolManagement getSbbPoolManagement() {
		return sbbPoolManagement;
	}

	public MobicentsSbbDescriptor getSbbComponent(ComponentID componentID) {
		return sbbDescriptors.get(componentID);
	}

	/**
	 * Get the IDs of the sbb descriptors installed
	 */
	public SbbIDImpl[] getSbbIDs() throws Exception {
		return sbbDescriptors.keySet().toArray(
				new SbbIDImpl[sbbDescriptors.size()]);
	}

	/**
	 * Deploys an SBB. This generates the code to convert abstract to concrete
	 * class and registers the component in the component table and creates an
	 * object pool for the sbb id.
	 * 
	 * @param mobicentsSbbDescriptor
	 *            the descriptor of the sbb to install
	 * @throws Exception
	 */
	public void installSbb(final MobicentsSbbDescriptor mobicentsSbbDescriptor)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing SBB " + mobicentsSbbDescriptor.getID());
		}

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		// Iterate over the set of event entries and initialize descriptor
		for (Iterator it = mobicentsSbbDescriptor.getSbbEventEntries()
				.iterator(); it.hasNext();) {
			SbbEventEntry eventEntry = (SbbEventEntry) it.next();
			EventTypeID eventTypeId = sleeContainer.getEventManagement()
					.getEventType(eventEntry.getEventTypeRefKey());
			if (eventTypeId == null)
				throw new DeploymentException("Unknown event type "
						+ eventEntry.getEventTypeRefKey());
			mobicentsSbbDescriptor.addEventEntry(eventTypeId, eventEntry);
		}

		// create deployer
		SbbDeployer sbbDeployer = new SbbDeployer(sleeContainer.getDeployPath());
		// change classloader
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(
					mobicentsSbbDescriptor.getClassLoader());
			// Set up the comp/env naming context for the Sbb.
			mobicentsSbbDescriptor.setupSbbEnvironment();
			// deploy the sbb
			sbbDeployer.deploySbb(mobicentsSbbDescriptor, sleeContainer);
		} catch (Exception ex) {
			throw ex;
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
				
		// create the pool for the given SbbID
		sbbPoolManagement.createObjectPool(mobicentsSbbDescriptor,
				sleeTransactionManager);

		// Set Trace to off
		sleeContainer.getTraceFacility().setTraceLevelOnTransaction(
				mobicentsSbbDescriptor.getID(), Level.OFF);
		sleeContainer.getAlarmFacility().registerComponent(
				mobicentsSbbDescriptor.getID());

		final ComponentManagement componentManagement = sleeContainer
				.getComponentManagement();
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();

		// add tx action to add this sbb descriptor
		if (sbbDescriptors.putIfAbsent(mobicentsSbbDescriptor.getID(),
				mobicentsSbbDescriptor) != null) {
			throw new DeploymentException("Sbb descriptor already installed");
		} else {
			TransactionalAction action2 = new TransactionalAction() {
				public void execute() {
					sbbDescriptors.remove(mobicentsSbbDescriptor.getID());
					logger.info("Removed SBB " + mobicentsSbbDescriptor.getID()
							+ " due to transaction rollback");
				}
			};
			sleeTransactionManager.addAfterRollbackAction(action2);
		}

		// This sbb refers to the following sbbs.
		for (SbbID sbbID : mobicentsSbbDescriptor.getSbbs()) {
			componentManagement.addComponentDependency(sbbID,
					mobicentsSbbDescriptor.getID());
		}

		// This sbb refers to the following profile specifications.
		for (ProfileSpecificationID profileID : mobicentsSbbDescriptor
				.getProfileSpecifications()) {
			componentManagement.addComponentDependency(profileID,
					mobicentsSbbDescriptor.getID());
		}

		// This sbb refers to the following events.
		for (EventTypeID eventTypeID : mobicentsSbbDescriptor.getEventTypes()) {
			componentManagement.addComponentDependency(eventTypeID,
					mobicentsSbbDescriptor.getID());
		}

		// This sbb refers to the following resource adaptor type ids.
		for (ResourceAdaptorTypeID raTypeID : mobicentsSbbDescriptor
				.getResourceAdaptorTypes()) {
			componentManagement.addComponentDependency(raTypeID,
					mobicentsSbbDescriptor.getID());
		}

		// This sbb refers to the following resource adaptor links
		for (String raEntityLink : mobicentsSbbDescriptor
				.getResourceAdaptorEntityLinks()) {
			ResourceAdaptorID raID = resourceManagement
					.getResourceAdaptor(resourceManagement
							.getResourceAdaptorEntityName(raEntityLink));
			componentManagement.addComponentDependency(raID,
					mobicentsSbbDescriptor.getID());
		}

		// This sbb refers to the following address profile.
		ProfileSpecificationID addressProfile = mobicentsSbbDescriptor
				.getAddressProfileSpecification();
		if (addressProfile != null) {
			componentManagement.addComponentDependency(addressProfile,
					mobicentsSbbDescriptor.getID());
		}

		logger.info("Installed SBB " + mobicentsSbbDescriptor.getID());
	}

	public void uninstallSbbs(DeployableUnitIDImpl deployableUnitID)
			throws SystemException, Exception, NamingException {

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		for (final MobicentsSbbDescriptor sbbDescriptor : sbbDescriptors
				.values()) {

			if (sbbDescriptor.getDeployableUnit().equals(deployableUnitID)) {

				if (logger.isDebugEnabled())
					logger.debug("Uninstalling SBB " + sbbDescriptor.getID()
							+ " on DU " + deployableUnitID);

				// removes the sbb object pool
				sbbPoolManagement.removeObjectPool(sbbDescriptor,
						sleeTransactionManager);

				// remove sbb from trace and alarm facilities
				sleeContainer.getTraceFacility().unSetTraceLevel(
						sbbDescriptor.getID());
				sleeContainer.getAlarmFacility().unRegisterComponent(
						sbbDescriptor.getID());
				if (logger.isDebugEnabled()) {
					logger.debug("Removed SBB " + sbbDescriptor.getID()
							+ " from trace and alarm facilities");
				}

				// remove sbb
				sbbDescriptors.remove(sbbDescriptor.getID());
				TransactionalAction action1 = new TransactionalAction() {
					public void execute() {
						sbbDescriptors.putIfAbsent(sbbDescriptor.getID(),
								sbbDescriptor);
						logger.info("Reinstalled SBB " + sbbDescriptor.getID()
								+ " due to transaction rollback");
					}
				};
				
				// remove class loader
				ComponentClassLoadingManagement.INSTANCE.removeClassLoader(sbbDescriptor.getID());
				
				logger.info("Uninstalled SBB " + sbbDescriptor.getID()
						+ " on DU " + sbbDescriptor.getDeployableUnit());
				
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("SBB " + sbbDescriptor.getID()
							+ " belongs to "
							+ sbbDescriptor.getDeployableUnit());
				}
			}
		}
	}

	@Override
	public String toString() {
		return "Sbb Management: " + "\n+-- Sbb Descriptors: "
				+ sbbDescriptors.keySet() + "\n" + ComponentClassLoadingManagement.INSTANCE + "\n" + sbbPoolManagement;
	}
}
