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
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceActivityContextHandle;
import org.mobicents.slee.container.service.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityFactory;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.container.service.ServiceActivityHandleImpl;
import org.mobicents.slee.container.service.ServiceImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.container.util.JndiRegistrationManager;
import org.mobicents.slee.runtime.facilities.NotificationSourceWrapperImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;

/**
 * 
 * @author martins
 *
 */
public class ServiceManagementImpl extends AbstractSleeContainerModule implements ServiceManagement {

	private static final Logger logger = Logger
			.getLogger(ServiceManagementImpl.class);

	private SleeTransactionManager transactionManager;
	private ComponentRepository componentRepositoryImpl;
	
	private ServiceActivityFactory serviceActivityFactory;
	private ServiceActivityContextInterfaceFactoryImpl serviceActivityContextInterfaceFactory;
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ServiceManagement#getServiceActivityFactory()
	 */
	public ServiceActivityFactory getServiceActivityFactory() {
		return serviceActivityFactory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ServiceManagement#getServiceActivityContextInterfaceFactory()
	 */
	public ServiceActivityContextInterfaceFactoryImpl getServiceActivityContextInterfaceFactory() {
		return serviceActivityContextInterfaceFactory;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.AbstractSleeContainerModule#sleeInit()
	 */
	@Override
	public void sleeStarting() {
		transactionManager = sleeContainer.getTransactionManager();
		componentRepositoryImpl = sleeContainer.getComponentManagement().getComponentRepository();
		serviceActivityFactory = new ServiceActivityFactoryImpl(this);
		serviceActivityContextInterfaceFactory = new ServiceActivityContextInterfaceFactoryImpl(sleeContainer);
		JndiRegistrationManager.registerWithJndi("slee/serviceactivity/",
				ServiceActivityFactoryImpl.JNDI_NAME, serviceActivityFactory);
		JndiRegistrationManager.registerWithJndi("slee/serviceactivity/",
				ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
				serviceActivityContextInterfaceFactory);
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
			final ServiceImpl service = getService(serviceID);
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
				ServiceImpl service = getService(componentRepositoryImpl.getComponentByID(serviceID));
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
	public void activate(final ServiceID serviceID) throws NullPointerException,
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
					
				final ServiceImpl service = getService(serviceComponent);
				
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
				
				// notifying the resource adaptors about service state change if the tx commits
				final ResourceManagement resourceManagement = sleeContainer
						.getResourceManagement();
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						for (String raEntityName : resourceManagement
								.getResourceAdaptorEntities()) {
							resourceManagement.getResourceAdaptorEntity(raEntityName).serviceActive(serviceID);											
						}
					}
				};
				transactionManager.getTransactionContext().getAfterCommitActions().add(action);
				
				// lets cache some info in the event components this service refer
				for (EventEntryDescriptor mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
					if (mEventEntry.isInitialEvent()) {
						EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference());
						eventTypeComponent.activatedServiceWhichDefineEventAsInitial(serviceComponent);
					}
				}
								
				// add rollback tx action to removed state added
				TransactionalAction action1 = new TransactionalAction() {
					public void execute() {
						// remove references created
						for (EventEntryDescriptor mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
							if (mEventEntry.isInitialEvent()) {
								EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference());
								eventTypeComponent.deactivatedServiceWhichDefineEventAsInitial(serviceComponent);
							}
						}					
					}
				};
				transactionManager.getTransactionContext().getAfterRollbackActions().add(action1);
				
				rb = false;
				logger.info("Activated " + serviceID);
								
			} catch (IllegalStateException e) {
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
				final ServiceImpl service = getService(serviceComponent);

				if (logger.isDebugEnabled())
					logger.debug(serviceID.toString() + " state = "
							+ service.getState());

				final boolean sleeRunning = sleeContainer.getSleeState() == SleeState.RUNNING;
				
				final boolean clustered = !sleeContainer.getCluster().isSingleMember(); 
				// only really deactivate the service if we are not in a cluster
				if (!clustered) {
					if (service.getState() == ServiceState.STOPPING)
						throw new InvalidStateException("Service is STOPPING");

					if (service.getState() == ServiceState.INACTIVE) {
						throw new InvalidStateException(
						"Service already deactivated");
					}

					service.setState(ServiceState.STOPPING);
					
					// warn ra entities about state change
					final ResourceManagement resourceManagement = sleeContainer
							.getResourceManagement();
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							for (String raEntityName : resourceManagement
									.getResourceAdaptorEntities()) {
								resourceManagement.getResourceAdaptorEntity(raEntityName).serviceStopping(serviceID);											
							}
						}
					};
					transactionManager.getTransactionContext().getAfterCommitActions().add(action);
					
					// only end activity if slee is running, otherwise
					// slee already did it
					if (sleeRunning) {
						service.endActivity();
					}
					else {
						service.setState(ServiceState.INACTIVE);
						// warn ra entities about state change
						TransactionalAction action1 = new TransactionalAction() {
							public void execute() {
								for (String raEntityName : resourceManagement
										.getResourceAdaptorEntities()) {
									resourceManagement.getResourceAdaptorEntity(raEntityName).serviceInactive(serviceID);											
								}
							}
						};
						transactionManager.getTransactionContext().getAfterCommitActions().add(action1);
					}
				}
				else {
					// just warn ra entities
					final ResourceManagement resourceManagement = sleeContainer
							.getResourceManagement();
					TransactionalAction action = new TransactionalAction() {
						public void execute() {
							for (String raEntityName : resourceManagement
									.getResourceAdaptorEntities()) {
								ResourceAdaptorEntity raEntity = resourceManagement.getResourceAdaptorEntity(raEntityName);
								raEntity.serviceStopping(serviceID);
								raEntity.serviceInactive(serviceID);
							}
						}
					};
					transactionManager.getTransactionContext().getAfterCommitActions().add(action);
				}
								
				// remove runtime cache related wih this service
				for (EventEntryDescriptor mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
					if (mEventEntry.isInitialEvent()) {
						EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference());
						eventTypeComponent.deactivatedServiceWhichDefineEventAsInitial(serviceComponent);
					}
				}
				// add rollback tx action to add state removed
				TransactionalAction action2 = new TransactionalAction() {
					public void execute() {
						// re-add references created
						for (EventEntryDescriptor mEventEntry : serviceComponent.getRootSbbComponent().getDescriptor().getEventEntries().values()) {
							if (mEventEntry.isInitialEvent()) {
								EventTypeComponent eventTypeComponent = componentRepositoryImpl.getComponentByID(mEventEntry.getEventReference());
								eventTypeComponent.activatedServiceWhichDefineEventAsInitial(serviceComponent);
							}
						}					
					}
				};
				transactionManager.getTransactionContext().getAfterRollbackActions().add(action2);
				
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
		
		final ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);
		if (serviceComponent != null) {			
			return serviceComponent.getServiceUsageMBean().getObjectName();
		} else {
			throw new UnrecognizedServiceException(serviceID.toString());
		}
	}

	
	
	// --- non JMX

	/**
	 * 
	 * Retrieves {@link ServiceImpl} with the specified {@link ServiceID}.
	 * 
	 * @param serviceID
	 * @return
	 * @throws UnrecognizedServiceException
	 */
	public ServiceImpl getService(ServiceID serviceID)
			throws UnrecognizedServiceException {

		final ServiceComponent serviceComponent = componentRepositoryImpl.getComponentByID(serviceID);
		if (serviceComponent == null)
			throw new UnrecognizedServiceException("Service not found for "
					+ serviceID);
		return getService(serviceComponent);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ServiceManagement#getService(org.mobicents.slee.core.component.service.ServiceComponent)
	 */
	public ServiceImpl getService(
			ServiceComponent serviceComponent) {
		return new ServiceImpl(serviceComponent);
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
			new ServiceImpl(serviceComponent);
		}
		
		// FIXME what about clustering and usage mbean ???
		
		// creates and registers the service usage mbean
		final ServiceUsageMBean serviceUsageMBean = sleeContainer.getUsageParametersManagement().newServiceUsageMBean(serviceComponent);
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
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		txContext.getAfterRollbackActions().add(action);

		
			// register notification sources for all sbbs
			//
			final TraceManagement traceMBeanImpl = sleeContainer.getTraceManagement();
			for (final SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {
				
			  // Tracer must be available for both 1.1 and 1.0 sbb components
				//SbbComponent sbbComponent = componentRepositoryImpl.getComponentByID(sbbID);
				//if(sbbComponent.isSlee11())
				{
					traceMBeanImpl.registerNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
					
					// add rollback action to remove state created
					action = new TransactionalAction() {
						public void execute() {
							// remove notification sources for all sbbs
							traceMBeanImpl.deregisterNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));						
						}
					};
					txContext.getAfterRollbackActions().add(action);
				}
				
				//this might be used not only by 1.1 sbbs...
				NotificationSourceWrapperImpl sbbMNotificationSource = new NotificationSourceWrapperImpl(new SbbNotification(serviceComponent.getServiceID(),sbbID));
				serviceComponent.getAlarmNotificationSources().putIfAbsent(sbbID, sbbMNotificationSource);
			}
			
			
		
		sleeContainer.getSbbManagement().serviceInstall(serviceComponent);
		
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
		final ServiceImpl service = this
				.getService(serviceComponent.getServiceID());
		
		final boolean clustered = !sleeContainer.getCluster().isSingleMember(); 

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
		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Closing Usage MBean of service "
					+ serviceComponent.getServiceID());
		}

		ServiceUsageMBean serviceUsageMBean = (ServiceUsageMBean) serviceComponent.getServiceUsageMBean();
		if (serviceUsageMBean != null) {
			serviceUsageMBean.remove();
			// add rollback action to re-create the mbean
			//FIXME this doesn't make sense, this restore looses all old data, it shoudl only remove on 
			// commit but as it is right now, the needed sbb components are already removed
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						sleeContainer.getUsageParametersManagement().newServiceUsageMBean(serviceComponent);									
					}
					catch (Throwable e) {
						logger.error(e.getMessage(),e);
					}
				}
			};
			txContext.getAfterRollbackActions().add(action);			
		}
		
		
		// register notification sources for all sbbs
		final TraceManagement traceMBeanImpl = sleeContainer.getTraceManagement();
		for (final SbbID sbbID : serviceComponent.getSbbIDs(componentRepositoryImpl)) {

			// Tracer must be available for both 1.1 and 1.0 sbb components
			//SbbComponent sbbComponent = componentRepositoryImpl.getComponentByID(sbbID);
			//if(sbbComponent.isSlee11())
			{
				traceMBeanImpl.deregisterNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));

				// add rollback action to re-add state removed
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						// remove notification sources for all sbbs
						traceMBeanImpl.registerNotificationSource(new SbbNotification(serviceComponent.getServiceID(),sbbID));
					}

				};
				txContext.getAfterRollbackActions().add(action);					
			}
		}
		
		// warn sbb management that the service is being uninstalled, giving it the option to clear any related resources 
		sleeContainer.getSbbManagement().serviceUninstall(serviceComponent);
		
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
			ServiceImpl service = getService(serviceID);
			service.endActivity();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.AbstractSleeContainerModule#afterSleeRunning()
	 */
	@Override
	public void afterSleeRunning() {
		super.afterSleeRunning();
		if (sleeContainer.getCluster().isHeadMember()) {
			try {
				startActiveServicesActivities();
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(),e);
			};
		}
	}
	
	public void startActiveServicesActivities() throws NullPointerException, ManagementException, UnrecognizedServiceException, SystemException {
		ActivityContextFactory acf = sleeContainer.getActivityContextFactory();
		for (ServiceID serviceID : getServices(ServiceState.ACTIVE)) {
			ServiceImpl service = getService(serviceID);
			ActivityContext ac = acf.getActivityContext(new ServiceActivityContextHandle(new ServiceActivityHandleImpl(serviceID)));
			if (ac == null) {
				service.startActivity();
			}
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
				ServiceImpl service = getService(serviceComponent);
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
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ServiceManagement#activityEnded(org.mobicents.slee.container.service.ServiceActivityHandle)
	 */
	public void activityEnded(ServiceActivityHandle activityHandle) {
		final ServiceID serviceID = activityHandle.getServiceID();
		try {
			// change service state to inactive if it is stopping
			Service service = getService(serviceID);
			if (service.getState().isStopping()) {
				service.setState(ServiceState.INACTIVE);
				// notifying the resource adaptors about service state change if the tx commits
				final ResourceManagement resourceManagement = sleeContainer
						.getResourceManagement();
				TransactionalAction action1 = new TransactionalAction() {
					public void execute() {
						for (String raEntityName : resourceManagement
								.getResourceAdaptorEntities()) {
							resourceManagement.getResourceAdaptorEntity(raEntityName).serviceInactive(serviceID);											
						}
					}
				};
				sleeContainer.getTransactionManager().getTransactionContext().getAfterCommitActions().add(action1);
				// schedule task to remove outstanding root sbb entities of the service
				new RootSbbEntitiesRemovalTask(serviceID);
				Logger.getLogger(ServiceManagement.class).info("Deactivated "+ serviceID);
			}
		} catch (UnrecognizedServiceException e) {
			logger.error("Unable to find "+serviceID+" to deactivate",e);
		}
	}
}
