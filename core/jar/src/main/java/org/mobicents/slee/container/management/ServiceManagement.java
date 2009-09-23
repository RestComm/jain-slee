/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

/*
 * Created on Jul 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.container.management;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.InvalidLinkNameBindingStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.SbbNotification;
import javax.slee.management.ServiceState;
import javax.slee.management.SleeState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentRepositoryImpl;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceFactory;
import org.mobicents.slee.runtime.facilities.MNotificationSource;
import org.mobicents.slee.runtime.sbb.SbbObjectPoolManagement;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * @author root
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ServiceManagement {

	private static final Logger logger = Logger
			.getLogger(ServiceManagement.class);

	private final SleeContainer sleeContainer;
	private final SleeTransactionManager transactionManager;
	private final ComponentRepositoryImpl componentRepositoryImpl;

	public ServiceManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.transactionManager = sleeContainer.getTransactionManager();
		this.componentRepositoryImpl = sleeContainer.getComponentRepositoryImpl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID)
	 */
	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Service.getState " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("Null service ID!");
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			final Service service = getService(serviceID);
			if (logger.isDebugEnabled()) {
				logger.debug("returning state " + service.getState());
			}
			return service.getState();

		} catch (Exception ex) {

			try {
				transactionManager.setRollbackOnly();
			} catch (SystemException e) {
				logger.error("Failed getState for serviceID " + serviceID);
			}
			throw new ManagementException(
					"Unexpected system exception while getting state of service: "
							+ serviceID, ex);
		} finally {
			if (b)
				try {
					transactionManager.commit();
				} catch (Exception e) {
					logger.error("Failed getState for serviceID " + serviceID);
					throw new ManagementException(
							"Unexpected system exception while committing transaction after getState for serviceID: "
									+ serviceID, e);
				}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.management.ServiceState)
	 */
	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException {

		if (serviceState == null)
			throw new NullPointerException("Passed a null state");

		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			ArrayList<ServiceID> retval = new ArrayList<ServiceID>();
			for (ServiceID serviceID : componentRepositoryImpl.getServiceIDs()) {
				Service service = getServiceFromServiceComponent(componentRepositoryImpl.getComponentByID(serviceID));
				if (service.getState().equals(serviceState)) {
					retval.add(serviceID);
				}
			}
			return retval.toArray(new ServiceID[retval.size()]);
		} catch (Exception e) {
			throw new ManagementException("Error getting services by state!", e);
		} finally {
			try {
				if (b)
					transactionManager.commit();
			} catch (Exception ex) {
				String s = "Transaction manager failed to commit while getting services by state";
				logger.error(s, ex);
				throw new ManagementException(s, ex);
			}
		}
	}

	/**
	 * Retrieves the set of ra entity link names referenced by the service componen, which do not exist
	 * @param serviceComponent
	 */
	public Set<String> getReferencedRAEntityLinksWhichNotExists(ServiceComponent serviceComponent) {
		Set<String> result = new HashSet<String>();
		Set<String> raLinkNames = sleeContainer.getResourceManagement().getLinkNamesSet();
		for (String raLink : serviceComponent.getResourceAdaptorEntityLinks(componentRepositoryImpl)) {
			if (!raLinkNames.contains(raLink)) {
				result.add(raLink);
			}
		}
		return result;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID serviceID) throws NullPointerException,
	UnrecognizedServiceException, InvalidStateException,
	InvalidLinkNameBindingStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Activating " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("null service id");

		synchronized (sleeContainer.getManagementMonitor()) {

			boolean b = transactionManager.requireTransaction();
			boolean rb = true;

			try {

				final ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);
				if (serviceComponent == null) {
					throw new UnrecognizedServiceException(
							"Unrecognized service " + serviceID);
				}

				final boolean doFullActivation = sleeContainer.getCluster().isHeadMember();
					
				final Service service = getServiceFromServiceComponent(serviceComponent);
				
				if (service.getState() == ServiceState.ACTIVE && doFullActivation) {
					throw new InvalidStateException("Service " + serviceID
							+ " already active");
				}

				// if we are in a cluster we don't need to change state or fire service started event
				if (doFullActivation) {
					// check sbb ra entity links
					Set<String> referencedRAEntityLinksWhichNotExists = getReferencedRAEntityLinksWhichNotExists(serviceComponent);
					if (!referencedRAEntityLinksWhichNotExists.isEmpty()) {
						throw new InvalidLinkNameBindingStateException(referencedRAEntityLinksWhichNotExists.iterator().next());
					}

					// If there was a deactivate before we have sbb entities
					// pending,
					// remove those first
					RootSbbEntitiesRemovalTask task = RootSbbEntitiesRemovalTask
					.getTask(serviceID);
					if (task != null) {
						task.run();
						if (logger.isDebugEnabled()) {
							logger
							.debug("Found timer task running to remove remaining sbb entities. Executing now...");
						}
					}

					// change service state
					service.setState(ServiceState.ACTIVE);

					// only create activity if slee is already running, otherwise
					// slee will do it by itself when state changes
					if (sleeContainer.getSleeState() == SleeState.RUNNING) {
						service.startActivity();
					}
				}
				
				// lets cache some info in the event components this service refer
				for (MEventEntry mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
					if (mEventEntry.isInitialEvent()) {
						EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference().getComponentID());
						eventTypeComponent.activatedServiceWhichDefineEventAsInitial(serviceComponent);
					}
				}
								
				// add rollback tx action to removed state added
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						// remove references created
						for (MEventEntry mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
							if (mEventEntry.isInitialEvent()) {
								EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference().getComponentID());
								eventTypeComponent.deactivatedServiceWhichDefineEventAsInitial(serviceComponent);
							}
						}					
					}
				};
				transactionManager.addAfterRollbackAction(action);
				
				rb = false;
				logger.info("Activated " + serviceID);
								
			} catch (SystemException e) {
				throw new SLEEException(e.getMessage(),e);
			} finally {

				try {
					if (rb)
						transactionManager.setRollbackOnly();
					if (b)
						transactionManager.commit();

				} catch (Exception e) {
					logger.error("Failed: transaction commit", e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		if (serviceIDs.length == 0) {
			throw new InvalidArgumentException("InvalidArgumentException");
		}

		for (int i = 0; i < serviceIDs.length; i++) {
			if (serviceIDs[i] == null) {
				throw new InvalidArgumentException("InvalidArgumentException");
			}
		}
		for (int i = 0; i < serviceIDs.length - 1; i++)
			for (int j = i + 1; j < serviceIDs.length; j++)
				if (serviceIDs[i] == (serviceIDs[j])) {
					throw new InvalidArgumentException(
							"InvalidArgumentException");
				}
		
		for (int i = 0; i < serviceIDs.length; i++) {
			activate(serviceIDs[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
	 */
	public void deactivate(final ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Deactivating " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("NullPointerException");

		synchronized (sleeContainer.getManagementMonitor()) {

			boolean rb = true;
			boolean newTx = transactionManager.requireTransaction();
			try {

				final ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);

				if (serviceComponent == null)
					throw new UnrecognizedServiceException(
							"Service not found for " + serviceID);

				// get the transactionally isolated copy of the service.
				final Service service = getServiceFromServiceComponent(serviceComponent);

				if (logger.isDebugEnabled())
					logger.debug(serviceID.toString() + " state = "
							+ service.getState());

				final boolean clustered = sleeContainer.getCluster().getClusterMembers().size() > 1; 
				// only really deactivate the service if we are not in a cluster
				if (!clustered) {
					if (service.getState() == ServiceState.STOPPING)
						throw new InvalidStateException("Service is STOPPING");

					if (service.getState() == ServiceState.INACTIVE) {
						throw new InvalidStateException(
						"Service already deactivated");
					}

					service.setState(ServiceState.STOPPING);

					// only end activity if slee is running, otherwise
					// slee already did it
					if (sleeContainer.getSleeState() == SleeState.RUNNING) {
						service.endActivity();
					}
					else {
						service.setState(ServiceState.INACTIVE);					
					}
				}
				// remove runtime cache related wih this service
				for (MEventEntry mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
					if (mEventEntry.isInitialEvent()) {
						EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference().getComponentID());
						eventTypeComponent.deactivatedServiceWhichDefineEventAsInitial(serviceComponent);
					}
				}
				// add rollback tx action to add state removed
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						// re-add references created
						for (MEventEntry mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
							if (mEventEntry.isInitialEvent()) {
								EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference().getComponentID());
								eventTypeComponent.activatedServiceWhichDefineEventAsInitial(serviceComponent);
							}
						}					
					}
				};
				transactionManager.addAfterRollbackAction(action);
				
				rb = false;

			} catch (InvalidStateException e) {
				logger.error("Service " + serviceID + " is not active", e);
				throw e;
			} catch (UnrecognizedServiceException e) {
				logger.error("Service " + serviceID + " not found ", e);
				throw e;
			} catch (Exception e) {
				logger.error(e);
				throw new SLEEException("Failed to deactivate service "
						+ serviceID, e);
			} finally {
				try {
					if (newTx) {
						if (rb) {
							transactionManager.rollback();
						} else {
							transactionManager.commit();
						}
					} else {
						if (rb)
							transactionManager.setRollbackOnly();
					}
				} catch (Exception e2) {
					logger.error(e2);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID[])
	 */
	public void deactivate(ServiceID[] arg0) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {

		if (arg0.length == 0) {
			throw new InvalidArgumentException("InvalidArgumentException");
		}

		for (int i = 0; i < arg0.length; i++) {
			if (arg0[i] == null) {
				throw new InvalidArgumentException("InvalidArgumentException");
			}
		}
		for (int i = 0; i < arg0.length - 1; i++)
			for (int j = i + 1; j < arg0.length; j++)
				if (arg0[i] == (arg0[j])) {
					throw new InvalidArgumentException(
							"InvalidArgumentException");
				}
		try {
			for (int i = 0; i < arg0.length; i++) {
				deactivate(arg0[i]);
			}
		} catch (InvalidStateException ise) {
			throw ise;
		} catch (Exception ex) {
			throw new ManagementException("system exception starting service",
					ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID,
	 *      javax.slee.ServiceID)
	 */
	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		if (logger.isInfoEnabled())
			logger.debug("deactivateAndActivate (" + arg0 + " , " + arg1);
		if (arg0 == arg1)
			throw new InvalidArgumentException(
					"Activating and deactivating the same service!");
		if ((arg0 == null) || (arg1 == null))
			throw new InvalidArgumentException("The service(s) are null!");
		try {
			deactivate(arg0);
			activate(arg1);
		} catch (InvalidStateException ise) {
			throw ise;
		} catch (Exception ex) {
			throw new ManagementException(
					"exception in deactivating/activating service ! ");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID[],
	 *      javax.slee.ServiceID[])
	 */
	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		if (arg0.length == 0 || arg1.length == 0)
			throw new InvalidArgumentException("The service array is empty!");
		for (int i = 0; i < arg0.length; i++)
			if (arg0[i] == null)
				throw new InvalidArgumentException("InvalidArgumentException");

		for (int i = 0; i < arg1.length; i++)
			if (arg1[i] == null)
				throw new InvalidArgumentException("InvalidArgumentException");
		for (int i = 0; i < arg0.length - 1; i++)
			for (int j = i + 1; j < arg0.length; j++)
				if (arg0[i] == (arg0[j]))
					throw new InvalidArgumentException(
							"InvalidArgumentException");
		for (int i = 0; i < arg1.length - 1; i++)
			for (int j = i + 1; j < arg1.length; j++)
				if (arg1[i] == (arg1[j]))
					throw new InvalidArgumentException(
							"InvalidArgumentException");

		for (int i = 0; i < arg0.length; i++)
			for (int j = 0; j < arg1.length; j++)
				if (arg0[i] == (arg1[j]))
					throw new InvalidArgumentException(
							"InvalidArgumentException");
		try {
			for (int i = 0; i < arg0.length; i++) {
				deactivate(arg0[i]);
			}
			for (int i = 0; i < arg1.length; i++) {
				activate(arg1[i]);
			}
		} catch (InvalidStateException ise) {
			throw ise;
		} catch (ManagementException me) {
			throw me;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
	 */
	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("getServiceUsageMBean " + serviceID);
		}
		
		if (componentRepositoryImpl.getComponentByID(serviceID) != null) {
			return ServiceUsageMBeanImpl.getObjectName(serviceID);
		} else {
			throw new UnrecognizedServiceException(serviceID.toString());
		}

	}

	// --- non JMX

	/**
	 * 
	 * Retrieves {@link Service} with the specified {@link ServiceID}.
	 * 
	 * @param serviceID
	 * @return
	 * @throws UnrecognizedServiceException
	 */
	public Service getService(ServiceID serviceID)
			throws UnrecognizedServiceException {

		final ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);
		if (serviceComponent == null)
			throw new UnrecognizedServiceException("Service not found for "
					+ serviceID);
		return getServiceFromServiceComponent(serviceComponent);
	}

	private Service getServiceFromServiceComponent(
			ServiceComponent serviceComponent) {
		return ServiceFactory.getService(serviceComponent);
	}

	/**
	 * Install a service into SLEE
	 * 
	 * @param serviceComponent
	 * @throws Exception
	 */
	public void installService(final ServiceComponent serviceComponent)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing Service " + serviceComponent);
		}

		final boolean doFullInstall = sleeContainer.getCluster().isHeadMember();

		if (doFullInstall) {
			ServiceFactory.createService(serviceComponent);
		}
		
		// FIXME what about clustering and usage mbean ???
		
		// creates and registers the service usage mbean
		final ServiceUsageMBeanImpl serviceUsageMBean = new ServiceUsageMBeanImpl(serviceComponent);
		// add rollback action to remove state created
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				try {
					serviceUsageMBean.remove();				
				}
				catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}
		};
		sleeContainer.getTransactionManager().addAfterRollbackAction(action);

		
			// register notification sources for all sbbs
			//
			final TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceMBean();
			for (final SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {
				
				SbbComponent sbbComponent = componentRepositoryImpl.getComponentByID(sbbID);
				if(sbbComponent.isSlee11())
				{
					traceMBeanImpl.registerNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
					
					// add rollback action to remove state created
					action = new TransactionalAction() {
						public void execute() {
							// remove notification sources for all sbbs
							traceMBeanImpl.deregisterNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));						
						}
					};
					sleeContainer.getTransactionManager().addAfterRollbackAction(action);
				}
				
				//this might be used not only by 1.1 sbbs...
				MNotificationSource sbbMNotificationSource = new MNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
				serviceComponent.getAlarmNotificationSources().putIfAbsent(sbbID, sbbMNotificationSource);
			}
			
			
		
		// create object pools
		SbbObjectPoolManagement sbbObjectPoolManagement = sleeContainer.getSbbPoolManagement();
		for (SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {
			// create the pool for the given SbbID
			sbbObjectPoolManagement.createObjectPool(serviceComponent.getServiceID(), componentRepositoryImpl.getComponentByID(sbbID),
					sleeContainer.getTransactionManager());
		}
		
	}

	/**
	 * uninstall a service.
	 * 
	 * @throws SystemException
	 * @throws UnrecognizedServiceException
	 * @throws MBeanRegistrationException
	 * @throws InstanceNotFoundException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws NullPointerException
	 * @throws InvalidStateException 
	 * 
	 */
	public void uninstallService(final ServiceComponent serviceComponent)
			throws SystemException, UnrecognizedServiceException,
			InstanceNotFoundException, MBeanRegistrationException,
			NullPointerException, UnrecognizedResourceAdaptorEntityException,ManagementException, InvalidStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling service with id "
					+ serviceComponent.getServiceID());
		}

		// get service
		final Service service = this
				.getService(serviceComponent.getServiceID());
		
		final boolean clustered = sleeContainer.getCluster().getClusterMembers().size() > 1; 

		if (!clustered) { 
			if (!service.getState().isInactive()) {
				throw new InvalidStateException(serviceComponent.toString()+" is not inactive");
			}
			// Remove and probably run task which will remove sbb entities
			// if it hadnt done it already
			RootSbbEntitiesRemovalTask task = RootSbbEntitiesRemovalTask
			.getTask(serviceComponent.getServiceID());
			if (task != null) {
				task.run();
				if (logger.isDebugEnabled()) {
					logger
					.debug("Found timer task running to remove remaining sbb entities. Executing now...");
				}
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("NO TASK TO RUN ON SERVICE UNINSTALL FOR "
							+ serviceComponent.getServiceID());
				}
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Usage MBean of service "
					+ serviceComponent.getServiceID());
		}
		ServiceUsageMBeanImpl serviceUsageMBean = (ServiceUsageMBeanImpl) serviceComponent.getServiceUsageMBean();
		if (serviceUsageMBean != null) {
			serviceUsageMBean.remove();
			// add rollback action to re-create the mbean
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						new ServiceUsageMBeanImpl(serviceComponent);									
					}
					catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		}
		
		
			// register notification sources for all sbbs
			final TraceMBeanImpl traceMBeanImpl = sleeContainer.getTraceMBean();
			for (final SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {
				
				
				SbbComponent sbbComponent = componentRepositoryImpl.getComponentByID(sbbID);
				if(sbbComponent.isSlee11())
				{
					traceMBeanImpl.deregisterNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
			
					// add rollback action to re-add state removed
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
					// remove notification sources for all sbbs
						traceMBeanImpl.registerNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
						}
					
					};
					sleeContainer.getTransactionManager().addAfterRollbackAction(action);
				}
			}
		
		// remove sbb object pools
		SbbObjectPoolManagement sbbObjectPoolManagement = sleeContainer.getSbbPoolManagement();
		for (SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {
			// remove the pool for the given SbbID
			sbbObjectPoolManagement.removeObjectPool(serviceComponent.getServiceID(), componentRepositoryImpl.getComponentByID(sbbID),
					sleeContainer.getTransactionManager());
		}
		
		if(!clustered){
			if (logger.isDebugEnabled()) {
				logger.debug("Removing Service " + serviceComponent.getServiceID()
						+ " from cache and active services set");
			}
			service.removeFromCache();
		}
	}

	public void endActiveServicesActivities() throws NullPointerException, ManagementException, UnrecognizedServiceException {
		for (ServiceID serviceID : getServices(ServiceState.ACTIVE)) {
			Service service = getService(serviceID);
			service.endActivity();
		}
	}
	
	public void startActiveServicesActivities() throws NullPointerException, ManagementException, UnrecognizedServiceException, SystemException {
		for (ServiceID serviceID : getServices(ServiceState.ACTIVE)) {
			Service service = getService(serviceID);
			service.startActivity();
		}
	}
	
	/**
	 * Verifies if the specified ra entity link name is referenced by a non inactive service.
	 * 
	 * @param raLinkName
	 * @return
	 */
	public boolean isRAEntityLinkNameReferenced(String raLinkName) {
		if (raLinkName == null) {
			throw new NullPointerException("null ra link name");
		}
		
		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			for (ServiceID serviceID : componentRepositoryImpl.getServiceIDs()) {
				ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);
				Service service = getServiceFromServiceComponent(serviceComponent);
				if (service.getState() != ServiceState.INACTIVE && serviceComponent.getResourceAdaptorEntityLinks(componentRepositoryImpl).contains(raLinkName)) {
					return true;
				}				
			}
			return false;
		} finally {
			try {
				transactionManager.requireTransactionEnd(b, false);
			} catch (Throwable ex) {
				throw new SLEEException(ex.getMessage(),ex);
			}
		}
	}
}
