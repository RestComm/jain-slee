/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */

package org.mobicents.slee.container.management;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
import javax.slee.resource.ActivityFlags;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;
import org.mobicents.slee.container.service.ServiceActivityContextHandle;
import org.mobicents.slee.container.service.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityFactory;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.container.service.ServiceActivityHandleImpl;
import org.mobicents.slee.container.service.ServiceCacheData;
import org.mobicents.slee.container.service.ServiceStartedEventImpl;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.facilities.NotificationSourceWrapperImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.util.concurrent.SleeThreadFactory;

/**
 * 
 * @author martins
 * 
 */
public class ServiceManagementImpl extends AbstractSleeContainerModule
		implements ServiceManagement {

	private static final Logger logger = Logger
			.getLogger(ServiceManagementImpl.class);

	private SleeTransactionManager transactionManager;
	private ComponentRepository componentRepositoryImpl;

	private ServiceActivityFactory serviceActivityFactory;
	private ServiceActivityContextInterfaceFactoryImpl serviceActivityContextInterfaceFactory;

	private final ConcurrentHashMap<ServiceID, ScheduledFuture<?>> activityEndingTasks = new ConcurrentHashMap<ServiceID, ScheduledFuture<?>>();

    private final static SleeThreadFactory SLEE_THREAD_FACTORY = new SleeThreadFactory("SLEE-ServiceManagement");

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.ServiceManagement#
	 * getServiceActivityFactory()
	 */
	public ServiceActivityFactory getServiceActivityFactory() {
		return serviceActivityFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.ServiceManagement#
	 * getServiceActivityContextInterfaceFactory()
	 */
	public ServiceActivityContextInterfaceFactoryImpl getServiceActivityContextInterfaceFactory() {
		return serviceActivityContextInterfaceFactory;
	}

	@Override
	public void sleeInitialization() {
		transactionManager = sleeContainer.getTransactionManager();
		componentRepositoryImpl = sleeContainer.getComponentManagement()
				.getComponentRepository();
		serviceActivityFactory = new ServiceActivityFactoryImpl(this);
		serviceActivityContextInterfaceFactory = new ServiceActivityContextInterfaceFactoryImpl(
				sleeContainer);		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID
	 * )
	 */
	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Service.getState " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("Null service ID!");

		final ServiceComponent serviceComponent = componentRepositoryImpl
				.getComponentByID(serviceID);

		if (serviceComponent == null)
			throw new UnrecognizedServiceException(serviceID.toString());

		return serviceComponent.getServiceState();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.
	 * management.ServiceState)
	 */
	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException {

		if (serviceState == null)
			throw new NullPointerException("Passed a null state");

		try {
			ArrayList<ServiceID> retval = new ArrayList<ServiceID>();
			for (ServiceID serviceID : componentRepositoryImpl.getServiceIDs()) {
				ServiceComponent service = componentRepositoryImpl
						.getComponentByID(serviceID);
				if (service == null) {
					throw new UnrecognizedServiceException(serviceID.toString());
				}
				if (service.getServiceState().equals(serviceState)) {
					retval.add(serviceID);
				}
			}
			return retval.toArray(new ServiceID[retval.size()]);
		} catch (Exception e) {
			throw new ManagementException("Error getting services by state!", e);
		}
	}

	/**
	 * Retrieves the set of ra entity link names referenced by the service
	 * componen, which do not exist
	 * 
	 * @param serviceComponent
	 */
	public Set<String> getReferencedRAEntityLinksWhichNotExists(
			ServiceComponent serviceComponent) {
		Set<String> result = new HashSet<String>();
		Set<String> raLinkNames = sleeContainer.getResourceManagement()
				.getLinkNamesSet();
		for (String raLink : serviceComponent
				.getResourceAdaptorEntityLinks(componentRepositoryImpl)) {
			if (!raLinkNames.contains(raLink)) {
				result.add(raLink);
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.ServiceManagement#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			InvalidLinkNameBindingStateException {
		activate(serviceID,null);		
	}
	
	private void activate(final ServiceID serviceID, final ServiceID oldServiceID)
			throws NullPointerException, UnrecognizedServiceException,
			InvalidStateException, InvalidLinkNameBindingStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Activating " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("null service id");

		synchronized (sleeContainer.getManagementMonitor()) {

			final ServiceComponent serviceComponent = componentRepositoryImpl
					.getComponentByID(serviceID);
			if (serviceComponent == null) {
				throw new UnrecognizedServiceException("Unrecognized service "
						+ serviceID);
			}

			if (serviceComponent.getServiceState() == ServiceState.ACTIVE) {
				throw new InvalidStateException("Service " + serviceID
						+ " already active");
			}

			// check sbb ra entity links
			Set<String> referencedRAEntityLinksWhichNotExists = getReferencedRAEntityLinksWhichNotExists(serviceComponent);
			if (!referencedRAEntityLinksWhichNotExists.isEmpty()) {
				throw new InvalidLinkNameBindingStateException(
						referencedRAEntityLinksWhichNotExists.iterator().next());
			}

			// set old version (used for smooth service version upgrade
			if (oldServiceID != null) {
				serviceComponent.setOldVersion(oldServiceID);
			}
			
			// change service state
			serviceComponent.setServiceState(ServiceState.ACTIVE);

			// lets cache some info in the event components this service refer
			for (EventEntryDescriptor mEventEntry : serviceComponent
					.getRootSbbComponent().getDescriptor().getEventEntries()
					.values()) {
				if (mEventEntry.isInitialEvent()) {
					EventTypeComponent eventTypeComponent = componentRepositoryImpl
							.getComponentByID(mEventEntry.getEventReference());
					eventTypeComponent
							.activatedServiceWhichDefineEventAsInitial(serviceComponent);
				}
			}

			// only create activity if slee is already running and single node
			// in cluster, otherwise
			// slee will do it by itself when state changes
			if (sleeContainer.getSleeState() == SleeState.RUNNING
					&& sleeContainer.getCluster().isHeadMember()) {
				startActivity(serviceComponent);
			}

			// notifying the resource adaptors about service state change
			final ResourceManagement resourceManagement = sleeContainer
					.getResourceManagement();
			for (String raEntityName : resourceManagement
					.getResourceAdaptorEntities()) {
				resourceManagement.getResourceAdaptorEntity(raEntityName)
						.serviceActive(serviceID);
			}

			logger.info("Activated " + serviceID);
		}
	}

	private void startActivity(final ServiceComponent serviceComponent) {

		// create ac for the activity
		ActivityContextHandle ach = new ServiceActivityContextHandle(
				new ServiceActivityHandleImpl(serviceComponent.getServiceID()));
		ActivityContext ac = sleeContainer.getActivityContextFactory()
				.createActivityContext(ach, ActivityFlags.NO_FLAGS);

		if (logger.isDebugEnabled()) {
			logger.debug("Starting " + serviceComponent.getServiceID()
					+ " activity.");
		}

		// ensure the service cache data exists
		new ServiceCacheData(serviceComponent.getServiceID(), sleeContainer
				.getCluster().getMobicentsCache()).create();
		serviceComponent.setActivityEnded(false);
		
		// fire slee 1.0 and 1.1 service started events
		ServiceStartedEventImpl event = new ServiceStartedEventImpl(
				serviceComponent.getServiceID());
		ac.fireEvent(ServiceStartedEventImpl.SLEE_10_EVENT_TYPE_ID, event,
				null, null, null, null, null);
		ac.fireEvent(ServiceStartedEventImpl.SLEE_11_EVENT_TYPE_ID, event,
				null, event.getService(), null, null, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID
	 * [])
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
	 * @see
	 * javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID
	 * )
	 */
	public void deactivate(final ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Deactivating " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("NullPointerException");

		synchronized (sleeContainer.getManagementMonitor()) {

			final ServiceComponent serviceComponent = componentRepositoryImpl
					.getComponentByID(serviceID);

			if (serviceComponent == null)
				throw new UnrecognizedServiceException("Service not found for "
						+ serviceID);				
			
			if (logger.isDebugEnabled())
				logger.debug(serviceID.toString() + " state = "
						+ serviceComponent.getServiceState());

			final SleeState sleeState = sleeContainer.getSleeState();

			if (serviceComponent.getServiceState() == ServiceState.STOPPING)
				throw new InvalidStateException("Service is STOPPING");

			if (serviceComponent.getServiceState() == ServiceState.INACTIVE) {
				throw new InvalidStateException("Service already deactivated");
			}

			serviceComponent.setServiceState(ServiceState.STOPPING);

			// warn ra entities about state change
			final ResourceManagement resourceManagement = sleeContainer
					.getResourceManagement();
			for (String raEntityName : resourceManagement
					.getResourceAdaptorEntities()) {
				resourceManagement.getResourceAdaptorEntity(raEntityName)
						.serviceStopping(serviceID);
			}

			// only end activity if slee was running and is single node in
			// cluster, otherwise not needed (cluster) or
			// slee already did it
			if (sleeContainer.getCluster().isSingleMember() && (sleeState == SleeState.RUNNING || sleeState == SleeState.STOPPING)) {
				if (sleeState == SleeState.RUNNING) {
					endServiceActivity(serviceID);
				}
				else {
					// chance of concurrency with activity end
					synchronized (serviceComponent) {
						if(serviceComponent.isActivityEnded()) {
							// activity already ended but service was not in stopping state
							completeServiceStop(serviceComponent);
						}
					}
				}
			} else {
				serviceComponent.setServiceState(ServiceState.INACTIVE);
				// warn ra entities about state change
				for (String raEntityName : resourceManagement
						.getResourceAdaptorEntities()) {
					resourceManagement.getResourceAdaptorEntity(raEntityName)
							.serviceInactive(serviceID);
				}
				logger.info("Deactivated " + serviceID);
			}

			// remove runtime cache related with this service
			for (EventEntryDescriptor mEventEntry : serviceComponent
					.getRootSbbComponent().getDescriptor().getEventEntries()
					.values()) {
				if (mEventEntry.isInitialEvent()) {
					EventTypeComponent eventTypeComponent = componentRepositoryImpl
							.getComponentByID(mEventEntry.getEventReference());
					eventTypeComponent
							.deactivatedServiceWhichDefineEventAsInitial(serviceComponent);
				}
			}
						
		}
	}

	public void endServiceActivity(final ServiceID serviceID) {

		final ActivityContextHandle ach = new ServiceActivityContextHandle(
				new ServiceActivityHandleImpl(serviceID));
		if (logger.isDebugEnabled()) {
			logger.debug("Ending " + serviceID + " activity.");
		}
		ActivityContext ac = sleeContainer.getActivityContextFactory()
				.getActivityContext(ach);
		if (ac == null) {
			logger.warn("unable to find and end ac " + ach);

			logger.warn("WORKAROUND USAGE: ENDING SERVICE");
			sleeContainer.getActivityContextFactory().WAremove("ACH=SERVICE");

			return;
		}
		if (!activityEndingTasks.contains(serviceID)) {
			// schedule a task to force the ending of the activity after 30 secs
			Runnable r = new Runnable() {
				public void run() {
					try {
						activityEndingTasks.remove(serviceID);
						if (sleeContainer.getSleeState() != null) {
							final ActivityContext ac = sleeContainer
									.getActivityContextFactory()
									.getActivityContext(ach);
							if (ac != null) {
								// if (logger.isDebugEnabled()) {
								logger.info("Forcing the end of " + ach);
								// }
								ac.activityEnded();
							}
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			ScheduledFuture<?> scheduledFuture = sleeContainer
					.getNonClusteredScheduler().schedule(r, 30L,
							TimeUnit.SECONDS);
			activityEndingTasks.put(serviceID, scheduledFuture);
		}
		// end the activity
		if (!ac.isEnding()) {
			ac.endActivity();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID
	 * [])
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
	 * @see
	 * javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax
	 * .slee.ServiceID, javax.slee.ServiceID)
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
			ServiceComponent serviceToDeactivate = componentRepositoryImpl
					.getComponentByID(arg0);
			if (serviceToDeactivate == null) {
				throw new UnrecognizedServiceException();
			} else {
				activate(arg1,arg0);
				deactivate(arg0);
			}
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
	 * @see
	 * javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax
	 * .slee.ServiceID[], javax.slee.ServiceID[])
	 */
	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		if (arg0.length == 0 || arg1.length == 0)
			throw new InvalidArgumentException("The parameter array(s) must not be empty.");
		
		if (arg0.length != arg1.length)
			throw new InvalidArgumentException("The parameter arrays must have same lenght.");
		
		Set<ServiceID> services = new HashSet<ServiceID>();
		
		for (int i = 0; i < arg0.length - 1; i++) {
			if (arg0[i] == null || arg1[i] == null) {
				throw new InvalidArgumentException("Null entry found in parameter array(s).");
			}
			if (!services.add(arg0[i]) || !services.add(arg1[i])) {
				throw new InvalidArgumentException("Repeated entry found in parameter array(s).");
			}
		}
		
		try {
			for (int i = 0; i < arg0.length; i++) {
				deactivateAndActivate(arg0[i],arg1[i]);
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
	 * @see
	 * javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax
	 * .slee.ServiceID)
	 */
	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("getServiceUsageMBean " + serviceID);
		}

		final ServiceComponent serviceComponent = componentRepositoryImpl
				.getComponentByID(serviceID);
		if (serviceComponent != null) {
			return serviceComponent.getServiceUsageMBean().getObjectName();
		} else {
			throw new UnrecognizedServiceException(serviceID.toString());
		}
	}

	// --- non JMX

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

		// creates and registers the service usage mbean
		final ServiceUsageMBean serviceUsageMBean = sleeContainer
				.getUsageParametersManagement().newServiceUsageMBean(
						serviceComponent);
		// add rollback action to remove state created
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				try {
					serviceUsageMBean.remove();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
		};
		final TransactionContext txContext = sleeContainer
				.getTransactionManager().getTransactionContext();
		txContext.getAfterRollbackActions().add(action);

		// register notification sources for all sbbs
		//
		final TraceManagement traceMBeanImpl = sleeContainer
				.getTraceManagement();
		for (final SbbID sbbID : serviceComponent
				.getSbbIDs(componentRepositoryImpl)) {

			// Tracer must be available for both 1.1 and 1.0 sbb components
			// SbbComponent sbbComponent =
			// componentRepositoryImpl.getComponentByID(sbbID);
			// if(sbbComponent.isSlee11())
			{
				traceMBeanImpl.registerNotificationSource(new SbbNotification(
						serviceComponent.getServiceID(), sbbID));

				// add rollback action to remove state created
				action = new TransactionalAction() {
					public void execute() {
						// remove notification sources for all sbbs
						traceMBeanImpl
								.deregisterNotificationSource(new SbbNotification(
										serviceComponent.getServiceID(), sbbID));
					}
				};
				txContext.getAfterRollbackActions().add(action);
			}

			// this might be used not only by 1.1 sbbs...
			NotificationSourceWrapperImpl sbbMNotificationSource = new NotificationSourceWrapperImpl(
					new SbbNotification(serviceComponent.getServiceID(), sbbID));
			serviceComponent.getAlarmNotificationSources().putIfAbsent(sbbID,
					sbbMNotificationSource);
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
			NullPointerException, UnrecognizedResourceAdaptorEntityException,
			ManagementException, InvalidStateException {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling service with id "
					+ serviceComponent.getServiceID());
		}

		if (serviceComponent.getServiceState().isStopping()) {
			// let's be friendly and give it a few secs
			for (int i = 0; i < 15; i++) {
				try {
					Thread.sleep(1000);
					logger.info("Waiting for "
							+ serviceComponent.getServiceID()
							+ " to stop, current state is "
							+ serviceComponent.getServiceState());
					if (serviceComponent.getServiceState().isInactive()) {
						break;
					}
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		if (!serviceComponent.getServiceState().isInactive()) {
			throw new InvalidStateException(serviceComponent.toString()
					+ " is not inactive");
		}

		final TransactionContext txContext = sleeContainer
				.getTransactionManager().getTransactionContext();

		if (logger.isDebugEnabled()) {
			logger.debug("Closing Usage MBean of service "
					+ serviceComponent.getServiceID());
		}

		ServiceUsageMBean serviceUsageMBean = serviceComponent.getServiceUsageMBean();
		if (serviceUsageMBean != null) {
			serviceUsageMBean.remove();
			// add rollback action to re-create the mbean
			// FIXME this doesn't make sense, this restore looses all old data,
			// it shoudl only remove on
			// commit but as it is right now, the needed sbb components are
			// already removed
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						sleeContainer.getUsageParametersManagement()
								.newServiceUsageMBean(serviceComponent);
					} catch (Throwable e) {
						logger.error(e.getMessage(), e);
					}
				}
			};
			txContext.getAfterRollbackActions().add(action);
		}

		// register notification sources for all sbbs
		final TraceManagement traceMBeanImpl = sleeContainer
				.getTraceManagement();
		for (final SbbID sbbID : serviceComponent
				.getSbbIDs(componentRepositoryImpl)) {

			// Tracer must be available for both 1.1 and 1.0 sbb components
			// SbbComponent sbbComponent =
			// componentRepositoryImpl.getComponentByID(sbbID);
			// if(sbbComponent.isSlee11())
			{
				traceMBeanImpl
						.deregisterNotificationSource(new SbbNotification(
								serviceComponent.getServiceID(), sbbID));

				// add rollback action to re-add state removed
				TransactionalAction action = new TransactionalAction() {
					public void execute() {
						// remove notification sources for all sbbs
						traceMBeanImpl
								.registerNotificationSource(new SbbNotification(
										serviceComponent.getServiceID(), sbbID));
					}

				};
				txContext.getAfterRollbackActions().add(action);
			}
		}

		// warn sbb management that the service is being uninstalled, giving it
		// the option to clear any related resources
		sleeContainer.getSbbManagement().serviceUninstall(serviceComponent);

	}

	@Override
	public void sleeRunning() {
		if (sleeContainer.getCluster().isHeadMember()) {
			try {
				startActiveServicesActivities();
			} catch (Throwable e) {
				throw new SLEEException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void sleeStopping() {
		if (sleeContainer.getCluster().isSingleMember()) {
			stopAllServiceActivities();
		}
	}

	private void stopAllServiceActivities() {

		logger.info("Ending all service activities...");

		ActivityContextFactory acf = sleeContainer.getActivityContextFactory();

		try {
			for (ActivityContextHandle ach : acf
					.getAllActivityContextsHandles()) {
				if (ach.getActivityType() == ActivityType.SERVICE) {
					endServiceActivity(((ServiceActivityContextHandle) ach)
							.getActivityHandle().getServiceID());
				}
			}
		} catch (Exception e) {
			logger.error("Exception while ending all service activities", e);
		}

		// give 35 secs for all activities to end
		for (int i = 0; i < 35; i++) {
			// check if there is still any service activity
			boolean noActivities = true;
			try {
				for (ActivityContextHandle ach : acf
						.getAllActivityContextsHandles()) {
					if (ach.getActivityType() == ActivityType.SERVICE) {
						logger.info("Waiting for " + ach.getActivityHandle()
								+ " to stop...");
						noActivities = false;
						break;
					}
				}
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
			if (noActivities) {
				break;
			} else {
				try {
					// wait a sec
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		logger.info("All service activities ended.");
	}

	public void startActiveServicesActivities() throws NullPointerException,
			ManagementException, UnrecognizedServiceException, SystemException {

		// create a set of services to activated, ordered by creation date
		// such order for activation is not mandatory but may overcome not ideal
		// multi service
		// design
		final Comparator<ServiceComponent> comparator = new Comparator<ServiceComponent>() {
			public int compare(ServiceComponent o1, ServiceComponent o2) {
				if (o2.getCreationTime() > o1.getCreationTime()) {
					return 1;
				} else {
					return -1;
				}
			}
		};

		final SortedSet<ServiceComponent> orderedSet = new TreeSet<ServiceComponent>(
				comparator);
		for (ServiceID serviceID : componentRepositoryImpl.getServiceIDs()) {
			ServiceComponent serviceComponent = componentRepositoryImpl
					.getComponentByID(serviceID);
			if (serviceComponent != null
					&& serviceComponent.getServiceState() == ServiceState.ACTIVE) {
				orderedSet.add(serviceComponent);
			}
		}

		ActivityContextFactory acf = sleeContainer.getActivityContextFactory();
		for (ServiceComponent serviceComponent : orderedSet) {
			ActivityContext ac = acf
					.getActivityContext(new ServiceActivityContextHandle(
							new ServiceActivityHandleImpl(serviceComponent
									.getServiceID())));
			if (ac != null) {
				ac.activityEnded();
			}
			startActivity(serviceComponent);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * Verifies if the specified ra entity link name is referenced by a non
	 * inactive service.
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
				ServiceComponent serviceComponent = componentRepositoryImpl
						.getComponentByID(serviceID);
				if (serviceComponent.getServiceState() != ServiceState.INACTIVE
						&& serviceComponent.getResourceAdaptorEntityLinks(
								componentRepositoryImpl).contains(raLinkName)) {
					return true;
				}
			}
			return false;
		} finally {
			try {
				transactionManager.requireTransactionEnd(b, false);
			} catch (Throwable ex) {
				throw new SLEEException(ex.getMessage(), ex);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.container.management.ServiceManagement#activityEnded
	 * (org.mobicents.slee.container.service.ServiceActivityHandle)
	 */
	public void activityEnded(final ServiceActivityHandle activityHandle) {
		// do this only on tx commit and in a new thread, to escape the tx context
		final Runnable r = new Runnable() {			
			public void run() {
				final ServiceID serviceID = activityHandle.getServiceID();
				if (logger.isDebugEnabled()) {
					logger.debug("Activity end for " + serviceID);
				}		
				// remove and cancel the timer task to force activity ending
				ScheduledFuture<?> scheduledFuture = activityEndingTasks.remove(serviceID);
				if (scheduledFuture != null) {
					scheduledFuture.cancel(true);
				}
				// get stopping service
				final ServiceComponent serviceComponent = componentRepositoryImpl
				.getComponentByID(serviceID);
				if (serviceComponent != null) {
					synchronized (serviceComponent) {
						if (logger.isDebugEnabled()) {
							logger.debug("Service is in "
									+ serviceComponent.getServiceState() + " state.");
						}
						if (serviceComponent.getServiceState().isStopping()) {
							completeServiceStop(serviceComponent);
						}
						serviceComponent.setActivityEnded(true);
					}
				}
				else {
					if (logger.isDebugEnabled()) {
						logger.debug(serviceID.toString()+ " activity ended, but component not found, removed concurrently?");
					}
				}				
			}
		};
		final ExecutorService executorService = Executors.newSingleThreadExecutor(SLEE_THREAD_FACTORY);
		TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		if (txContext != null) {
			TransactionalAction txAction = new TransactionalAction() {
				public void execute() {
					try {
						executorService.execute(r);
					}
					catch (Exception e) {
						logger.error("failed to execute task to complete service deactivation",e);
					}
					executorService.shutdown();
				}
			};
			txContext.getAfterCommitActions().add(txAction);
		}
		else {
			try {
				executorService.execute(r);
			}
			catch (Exception e) {
				logger.error("failed to execute task to complete service deactivation",e);
			}
			executorService.shutdown();
		}
	}

	protected void completeServiceStop(ServiceComponent serviceComponent) {
		boolean noRootSbbEntities = false;
		int entitiesRemovalDelay = MobicentsManagement.entitiesRemovalDelay * 60;
		int waitingTime = 0;
		while(true) {
			try {
				if (sleeContainer.getSbbEntityFactory().getRootSbbEntityIDs(serviceComponent.getServiceID()).isEmpty()) {
					noRootSbbEntities = true;
					break;
				}
				Thread.sleep(1000);
				if (logger.isDebugEnabled()) {
					logger.debug("Waiting for service "+serviceComponent+" root sbb entities to end.");
				}

				logger.warn("WORKAROUND USAGE: ENDING ROOT SBB");
				sleeContainer.getSbbEntityFactory().WAremove();
			}
			catch (Exception e) {
				logger.error("failure waiting for the ending of all sbb entities from "+serviceComponent.getServiceID(),e);
			}
			waitingTime += 1;
			if(entitiesRemovalDelay > 0 && entitiesRemovalDelay<= waitingTime) {
				break;
			}
		}
		if (!noRootSbbEntities) {
			// force the removal of all sbb entities
			new RootSbbEntitiesRemovalTask(serviceComponent).run();
		}
		// ensure service cache data is removed
		ServiceCacheData serviceCacheData = new ServiceCacheData(serviceComponent.getServiceID(), sleeContainer
				.getCluster().getMobicentsCache());
		if (serviceCacheData.exists()) {
			serviceCacheData.remove();
		}
		// change state
		serviceComponent.setServiceState(ServiceState.INACTIVE);
		// notifying the resource adaptors about service state change
		final ResourceManagement resourceManagement = sleeContainer
		.getResourceManagement();
		for (String raEntityName : resourceManagement
				.getResourceAdaptorEntities()) {
			resourceManagement.getResourceAdaptorEntity(raEntityName)
			.serviceInactive(serviceComponent.getServiceID());
		}
		logger.info("Deactivated " + serviceComponent.getServiceID());
		
	}
}
