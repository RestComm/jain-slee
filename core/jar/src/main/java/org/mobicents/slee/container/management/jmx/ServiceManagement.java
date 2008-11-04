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
package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.slee.EventTypeID;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.DeploymentException;
import javax.slee.management.ManagementException;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.DeploymentCacheManager;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.DeployableUnitIDImpl;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.ServiceDescriptorImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeDescriptorImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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
	private final DeploymentCacheManager deploymentCacheManager;

	public ServiceManagement(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
		this.transactionManager = sleeContainer.getTransactionManager();
		this.deploymentCacheManager = sleeContainer.getDeploymentManager();
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
				} catch (SystemException e) {
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
			if (serviceState == ServiceState.ACTIVE) {
				final Set activeServiceIds = deploymentCacheManager
						.getActiveServiceIDs();
				return (ServiceID[]) activeServiceIds
						.toArray(new ServiceID[activeServiceIds.size()]);
			} else {
				Iterator it = deploymentCacheManager.getServiceComponents()
						.values().iterator();
				ArrayList retval = new ArrayList();
				while (it.hasNext()) {
					ServiceComponent svc = (ServiceComponent) it.next();
					Service service = getServiceFromServiceComponent(svc);
					if (service.getState().equals(serviceState)) {
						retval.add(svc.getServiceID());
					}
				}
				ServiceID[] ret = new ServiceID[retval.size()];
				retval.toArray(ret);
				return ret;
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		if (logger.isDebugEnabled()) {
			logger.debug("Activating " + serviceID);
		}

		if (serviceID == null)
			throw new NullPointerException("null service id");

		synchronized (sleeContainer.getManagementMonitor()) {

			boolean b = transactionManager.requireTransaction();
			boolean rb = true;

			try {

				final ServiceComponent serviceComponent = (ServiceComponent) deploymentCacheManager
						.getServiceComponents().get(serviceID);
				if (serviceComponent == null) {
					throw new UnrecognizedServiceException(
							"Unrecognized service " + serviceID);
				}

				final Service service = getServiceFromServiceComponent(serviceComponent);

				if (service.getState().equals(ServiceState.ACTIVE)) {
					deploymentCacheManager.getActiveServiceIDs().add(serviceID);
					throw new InvalidStateException("Service " + serviceID
							+ " already active");
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

				// notifying the resource adaptors about service activation
				final ResourceManagement resourceManagement = sleeContainer
						.getResourceManagement();
				for (String raEntityName : resourceManagement
						.getResourceAdaptorEntities()) {
					resourceManagement.getResourceAdaptorEntity(raEntityName)
							.serviceActivated(serviceID.toString());
				}

				// Already active just return.
				service.activate();
				deploymentCacheManager.getActiveServiceIDs().add(serviceID);
				serviceComponent.lock();

				rb = false;
				logger.info("Activated " + serviceID);
			} catch (InvalidStateException ise) {
				throw ise;
			} catch (UnrecognizedServiceException use) {
				throw use;
			} catch (Exception ex) {
				throw new ManagementException(
						"system exception starting service", ex);
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
		try {
			for (int i = 0; i < serviceIDs.length; i++) {
				activate(serviceIDs[i]);
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
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
	 */
	public void deactivate(ServiceID serviceID) throws NullPointerException,
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

				final ServiceComponent serviceComponent = (ServiceComponent) deploymentCacheManager
						.getServiceComponents().get(serviceID);
				if (serviceComponent == null)
					throw new UnrecognizedServiceException(
							"Service not found for " + serviceID);

				// get the transactionally isolated copy of the service.
				final Service service = getServiceFromServiceComponent(serviceComponent);

				if (logger.isDebugEnabled())
					logger.debug("Service is " + service + " serviceState = "
							+ service.getState());

				if (service.getState() == ServiceState.STOPPING)
					throw new InvalidStateException("Service is STOPPING");

				if (service.getState() == ServiceState.INACTIVE) {
					deploymentCacheManager.getActiveServiceIDs().remove(
							serviceID);
					throw new InvalidStateException(
							"Service already deactivated");
				}

				// notifying the resource adaptors about service activation
				final ResourceManagement resourceManagement = sleeContainer
						.getResourceManagement();
				for (String raEntityName : resourceManagement
						.getResourceAdaptorEntities()) {
					resourceManagement.getResourceAdaptorEntity(raEntityName)
							.serviceDeactivated(serviceID.toString());
				}

				service.deactivate();
				serviceComponent.unlock();
				deploymentCacheManager.getActiveServiceIDs().remove(serviceID);

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
				} catch (SystemException e2) {
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

		if (serviceID == null)
			throw new NullPointerException("Null service ID ");

		boolean b = false;
		try {
			b = transactionManager.requireTransaction();
			if (deploymentCacheManager.getServiceComponents().containsKey(
					serviceID)) {
				return new ObjectName("slee:ServiceUsageMBean="
						+ serviceID.toString());
			} else {
				throw new UnrecognizedServiceException("bad service id "
						+ serviceID);
			}
		} catch (Exception ex) {
			throw new ManagementException(
					"Exception while getting service usage mbean for service with id "
							+ serviceID, ex);
		} finally {
			try {
				if (b)
					transactionManager.commit();
			} catch (SystemException ex) {
				throw new ManagementException(
						"Exception while getting service usage mbean for service with id "
								+ serviceID, ex);
			}
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

		final ServiceComponent serviceComponent = (ServiceComponent) deploymentCacheManager
				.getServiceComponents().get(serviceID);
		if (serviceComponent == null)
			throw new UnrecognizedServiceException("Service not found for "
					+ serviceID);
		return getServiceFromServiceComponent(serviceComponent);
	}

	private Service getServiceFromServiceComponent(
			ServiceComponent serviceComponent) {
		return Service.getService(serviceComponent.getServiceDescriptor());
	}

	/**
	 * Install a service into SLEE
	 * 
	 * @param serviceDescriptorImpl
	 * @throws Exception
	 */
	public void installService(ServiceDescriptorImpl serviceDescriptorImpl)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Installing Service " + serviceDescriptorImpl.getID());
		}

		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();

		synchronized (sleeContainer.getManagementMonitor()) {

			transactionManager.mandateTransaction();

			final ServiceComponent serviceComponent = new ServiceComponent(
					serviceDescriptorImpl);

			final MobicentsSbbDescriptor sbbDesc = serviceComponent
					.getRootSbbComponent();
			if (sbbDesc == null) {
				throw new DeploymentException(
						"cannot find root SbbID component ! cannot install service ");
			}
			deploymentCacheManager.getServiceComponents().put(
					serviceComponent.getServiceID(), serviceComponent);

			if (logger.isDebugEnabled()) {
				logger.debug("creating reference between root sbb with id "
						+ sbbDesc.getID() + " and service with id "
						+ serviceComponent.getServiceID());
			}
			Set set = (Set) deploymentCacheManager.getReferringComponents()
					.get(sbbDesc.getID());
			if (set == null) {
				set = deploymentCacheManager.newReferringCompSet();
				deploymentCacheManager.getReferringComponents().put(
						sbbDesc.getID(), set);
			}
			set.add(serviceComponent.getServiceID());

			// creates and registers the service usage mbean
			final ServiceUsageMBeanImpl serviceUsageMBean = new ServiceUsageMBeanImpl(
					serviceComponent.getServiceID());
			final ObjectName usageMBeanName = getServiceUsageMBean(serviceComponent
					.getServiceID());
			serviceComponent.setUsageMBeanName(usageMBeanName);
			sleeContainer.getMBeanServer().registerMBean(serviceUsageMBean,
					usageMBeanName);

			// Recursively install all the usage parameters for this and all his
			// children.
			serviceComponent.installDefaultUsageParameters(sbbDesc,
					new HashSet());

			// SBBIDS FOR THIS SERVICE
			HashSet sbbIDs = serviceComponent.getSbbComponents();

			if (logger.isDebugEnabled()) {
				Iterator sbbIdsIterator = sbbIDs.iterator();
				StringBuffer sb = new StringBuffer(300);
				while (sbbIdsIterator.hasNext()) {
					SbbDescriptor sbbdesc = (MobicentsSbbDescriptor) sleeContainer
							.getSbbComponent((SbbID) sbbIdsIterator.next());
					sb.append("NAME=" + sbbdesc.getName() + " COMPONENTID="
							+ sbbdesc.getID() + "\n");
				}
				logger
						.debug("\n==================SERVICE SBBS=======================\n"
								+ sb.toString()
								+ "\n"
								+ "=====================================================");
			}

			// CONTAINS MAPPING RAENTITY TO Events that are of interest of this
			// service, which are storeded in Set( of EventTypeIDs)
			HashMap raEntitiesToEventTypeIDsOfInterest = new HashMap(5);
			// CONTAINS MAPPING OF EVENTID TO resource options FOR THIS EVENT
			// WHICH
			// ARE STORED IN String
			HashMap eventTypeIDsToResourceOptions = new HashMap(30);
			// MAPS RA ENTITY TO EVENTS IT CAN FIRE, EVENTS ARE STORED IN Set(
			// of
			// EventTypeIDs)
			HashMap raEntitiesToEventsFired = new HashMap(5);

			// WE HAVE TO ITERATE THROUGH ALL SBBS IN SERVICE AND BUILD DATA
			// STRUCTURES
			Iterator sbbIdsIterator = sbbIDs.iterator();
			while (sbbIdsIterator.hasNext()) {

				MobicentsSbbDescriptor sbbdesc = (MobicentsSbbDescriptor) sleeContainer
						.getSbbComponent((SbbID) sbbIdsIterator.next());
				if (sbbdesc == null)
					continue;

				// maps EventTypeID to coresponding SbbEventEntry
				// IT CONTAINS MAPPING FOR ALL EVENTS THAT ARE OF INTEREST IF
				// THIS
				// SBB
				HashMap eventTypeIdToEventEntriesMappings = sbbdesc
						.getEventTypesMappings();
				// SIMPLY SET OF EventTypeIDs that are of interest of this SBB
				Set sbbEventsOfInterest = eventTypeIdToEventEntriesMappings
						.keySet();
				if (logger.isDebugEnabled()) {
					logger
							.debug("\n"
									+ "=============SBB==============================\n"
									+ ""
									+ sbbdesc.getName()
									+ ", "
									+ sbbdesc.getVendor()
									+ ", "
									+ sbbdesc.getVersion()
									+ "\n"
									+ "==============================================");
					Iterator eventEntryItarator = eventTypeIdToEventEntriesMappings
							.values().iterator();

					StringBuffer sb = new StringBuffer(300);

					while (eventEntryItarator.hasNext()) {
						SbbEventEntry eventEntry = (SbbEventEntry) eventEntryItarator
								.next();
						sb.append("EVENT :" + eventEntry + "\n");
					}
					logger
							.debug("\n==================EVENTS OF SBB INTEREST=======================\n"
									+ sb.toString()
									+ "\n"
									+ "=================================================================");
					sb = new StringBuffer(300);
				}

				// warn ra entities about service being installed
				for (String raEntityName : resourceManagement
						.getResourceAdaptorEntities()) {

					ResourceAdaptorEntity raEntity = resourceManagement
							.getResourceAdaptorEntity(raEntityName);
					ResourceAdaptorType raType = raEntity
							.getInstalledResourceAdaptor().getRaType();

					// IF WE HAVE PROCESSED THIS RA FOR OTHER SBB IN THIS
					// SERVICE WE
					// SHOULD HAVE A SET OF EventyTypeIDs of EVENTS
					// IT CAN FIRE, OTHERWISE WE HAVE TO CREATE IT.
					if (!raEntitiesToEventsFired.containsKey(raEntity)) {
						// SET OF EVENTS THAT THIS RA CAN FIRE
						HashSet setOfFiredEventIds = new HashSet();
						// ComponentKey
						// eventsK[]=raType.getRaTypeDescr().getEventTypeRefEntries();

						EventTypeID[] events = raType.getRaTypeDescr()
								.getEventTypes();
						if (events == null)
							continue; // IN ORDER TO PASS TCKS... TCK RA
										// RETURNS
						// NULL...
						for (int j = 0; j < events.length; j++)
							setOfFiredEventIds.add(events[j]);
						// LETS STORE EventTypeIDs Set THAT ARE FIRED BY THIS RA
						raEntitiesToEventsFired.put(raEntity,
								setOfFiredEventIds);

						// LETS CREATE FOR THIS RA SET THAT WILL BE FILLED WITH
						// EVENTS THAT ARE OF INTEREST BY THIS SERVICE
						// AND STORE IT, IT WILL BE FILLED LATER
						raEntitiesToEventTypeIDsOfInterest.put(raEntity,
								new HashSet(20));
						// raEntitiesToEventTypeIDsOfInterest.put(raEntity,new
						// TreeSet());
					}

					// EVENTS THAT ARE FIRED BY THIS RA ENTITY AND ARE OF
					// ITNEREST
					// OF THIS SERVICE
					// IT CONTAINS EventyTypeIDs
					Set eventsOfInterest = (Set) raEntitiesToEventTypeIDsOfInterest
							.get(raEntity);
					// SET OF EventTypeIDs FIRED BYT THIS RA ENTITY
					Set eventsFiredByRAEntity = (Set) raEntitiesToEventsFired
							.get(raEntity);
					Iterator eventsOfSbbinterest = sbbEventsOfInterest
							.iterator();

					// LETS FILL IN eventsOfInterest
					while (eventsOfSbbinterest.hasNext()) {
						// IT SHOUDL BE EventTypeID
						Object eventIdOfSbbItenrest = eventsOfSbbinterest
								.next();

						// IF THIS RA FIRES EVENT THAT IS OF ITNEREST OF SBB
						if (eventsFiredByRAEntity
								.contains(eventIdOfSbbItenrest)) {
							// TODO: - CAN WE REMOVE IT? IT SEEMS LIKE GOOD
							// IDEA, IT
							// WILL REDUCE OVERHEAD OF PROCESSING
							// THOSE LOOPS FOR OTHER RAs
							// eventsOfSbbinterest.remove();
							eventsOfInterest.add(eventIdOfSbbItenrest);
							// GET RESOURCE OPTION?
							SbbEventEntry eventEntry = (SbbEventEntry) eventTypeIdToEventEntriesMappings
									.get(eventIdOfSbbItenrest);
							String resourceOption = eventEntry
									.getResourceOption();
							// STORE RESOURCE OPTION FOR THIS EventTypeID
							if (resourceOption != null)
								if (eventTypeIDsToResourceOptions
										.containsKey(eventIdOfSbbItenrest)) {
									String value = (String) eventTypeIDsToResourceOptions
											.remove(eventIdOfSbbItenrest);
									// WE DONT NEED MORE THAN ONE OPTION KIND
									// HERE.
									if (value.indexOf(resourceOption) == -1) {
										value += "," + resourceOption;
										eventTypeIDsToResourceOptions.put(
												eventIdOfSbbItenrest, value);
									}
								} else {
									eventTypeIDsToResourceOptions.put(
											eventIdOfSbbItenrest,
											resourceOption);
								}
						}
					}

					if (logger.isDebugEnabled()) {

						StringBuffer sb = new StringBuffer(300);
						Iterator it = eventsOfInterest.iterator();
						while (it.hasNext()) {
							sb.append(it.next() + "\n");
						}
						logger
								.debug("\n=========================== EVENTIDS OF INTEREST FROM RA===========================\n"
										+ ""
										+ sb
										+ "\n"
										+ "=====================================================================================");
					}
				}

			}

			// NOW WE HAVE TO BUILD ARRAYS OF eventIDs and corresponding
			// resource
			// options for each RA ENTITY

			Iterator raEntities = raEntitiesToEventTypeIDsOfInterest.keySet()
					.iterator();
			Iterator eventTypeIdIterator = null;
			String resourceOption = null;
			int eventID = -1;
			// IF WE HAVE SOME RAs, WE NEED TO LET THEM KNOW THAT SOMEONE IS
			// GOING
			// TO BE INSTALLED
			// AND THAT HE IS INTERESTED IN SOME EVENTS WITH SOME ResoureOptions
			while (raEntities.hasNext()) {
				ResourceAdaptorEntity raEntity = (ResourceAdaptorEntity) raEntities
						.next();
				Set eventsOfServiceInterest = (Set) raEntitiesToEventTypeIDsOfInterest
						.get(raEntity);
				// eventIDs and resourceOptions ARE GOING TO BE PASSED AS ARGS
				// TO RA
				// eventIDs[i]=int# , resourceOptions[i]=optionsFor-int#
				int[] eventIDs = new int[eventsOfServiceInterest.size()];
				String[] resourceOptions = new String[eventsOfServiceInterest
						.size()];

				eventsOfServiceInterest = new TreeSet(eventsOfServiceInterest);

				int i = 0;

				eventTypeIdIterator = eventsOfServiceInterest.iterator();

				while (eventTypeIdIterator.hasNext()) {
					EventTypeIDImpl ETID = (EventTypeIDImpl) eventTypeIdIterator.next();
					eventID = ETID.getEventID();
					resourceOption = (String) eventTypeIDsToResourceOptions
							.get(ETID);
					eventIDs[i] = eventID;
					resourceOptions[i++] = resourceOption;

				}

				if (logger.isDebugEnabled()) {

					StringBuffer sb = new StringBuffer(300);
					sb.append("INDEX[ eventID | resourceOptions ]\n");
					for (int k = 0; k < eventIDs.length; k++) {

						sb.append("#" + k + "[ " + eventIDs[k] + " | "
								+ resourceOptions[k] + " ]\n");
					}
					ResourceAdaptorTypeDescriptorImpl raDesc = raEntity
							.getInstalledResourceAdaptor().getRaType()
							.getRaTypeDescr();
					logger
							.debug("\n============= PASSING INSTALL SERVCICE ARGS TO RA =============\n"
									+ "| RA DESC: "
									+ raDesc.getName()
									+ ", "
									+ raDesc.getVendor()
									+ ", "
									+ raDesc.getVersion()
									+ "\n"
									+ "===============================================================\n"
									+ "| EVENTS : |\n"
									+ "============\n"
									+ ""
									+ sb
									+ "\n"
									+ "===============================================================");
				}

				raEntity.serviceInstalled(serviceComponent.getServiceID()
						.toString(), eventIDs, resourceOptions);
				// ZERO VARS
				resourceOption = null;
				eventID = -1;
			}

			logger.info("Installed Service " + serviceComponent.getServiceID()
					+ ". Root SBB is "
					+ serviceComponent.getRootSbbComponent().getID());

		}
	}

	/**
	 * unistall a service.
	 * 
	 * @throws SystemException
	 * @throws UnrecognizedServiceException
	 * @throws MBeanRegistrationException
	 * @throws InstanceNotFoundException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws NullPointerException
	 * 
	 */
	public void uninstallService(ServiceComponent serviceComponent)
			throws SystemException, UnrecognizedServiceException,
			InstanceNotFoundException, MBeanRegistrationException,
			NullPointerException, UnrecognizedResourceAdaptorEntityException {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling service with id "
					+ serviceComponent.getServiceID());
		}

		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();

		synchronized (sleeContainer.getManagementMonitor()) {

			transactionManager.mandateTransaction();

			// get service
			final Service service = this.getService(serviceComponent
					.getServiceID());
			if (!service.getState().isInactive()) {
				throw new IllegalStateException("Service "
						+ serviceComponent.getServiceID()
						+ " state is not inactive");
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

			if (logger.isDebugEnabled()) {
				logger.debug("Unregistring Usage MBean of service "
						+ serviceComponent.getServiceID());
			}

			// unregister related mbeans
			final MBeanServer mbeanServer = sleeContainer.getMBeanServer();
			mbeanServer.unregisterMBean(serviceComponent.getUsageMBean());
			for (Iterator itr = serviceComponent.getSbbUsageMBeans(); itr
					.hasNext();) {
				ObjectName usageMbeanName = (ObjectName) itr.next();
				mbeanServer.unregisterMBean(usageMbeanName);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Removing all usage parameters of service "
						+ serviceComponent.getServiceID());
			}
			service.removeAllUsageParameters();

			// notifying the resource adaptor entities about service
			for (String raEntityName : resourceManagement
					.getResourceAdaptorEntities()) {
				ResourceAdaptorEntity raEntity = resourceManagement
						.getResourceAdaptorEntity(raEntityName);
				raEntity.serviceUninstalled(serviceComponent.getServiceID()
						.toString());
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Removing Service "
						+ serviceComponent.getServiceID()
						+ " from cache and active services set");
			}
			// remove the service from the list of deployed services
			deploymentCacheManager.getServiceComponents().remove(
					serviceComponent.getServiceID());
			service.removeFromCache();

			logger.info("Uninstalled service "
					+ serviceComponent.getServiceID());

		}
	}

	/**
	 * Get a list of services known to me
	 * 
	 * @return A list of services that are registered with me.
	 */
	public ServiceID[] getServiceIDs() {
		boolean b = SleeContainer.getTransactionManager().requireTransaction();
		try {
			ServiceIDImpl[] retval = new ServiceIDImpl[this.
					deploymentCacheManager.getServiceComponents().size()];
			this.deploymentCacheManager.getServiceComponents().keySet()
					.toArray(retval);
			return retval;
		} catch (Exception ex) {
			throw new RuntimeException("Exception while getting service IDs",ex);
		} finally {
			try {
				if (b) {
					SleeContainer.getTransactionManager().commit();
				}
			} catch (SystemException ex) {
				throw new RuntimeException("Tx manager failed to commit tx while getting service IDs",ex);
			}
		}
	}
	
	/**
	 * uninstall all services in a DU.
	 * 
	 * @param deployableUnitID --
	 *            deployable unit to uninstall
	 */
	public void uninstallServices(
			DeployableUnitIDImpl deployableUnitID) throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalling services on " + deployableUnitID);
		}
		
		for(Object obj : sleeContainer.getDeploymentManager()
			.getServiceComponents().values()) {
			ServiceComponent serviceComponent = (ServiceComponent) obj;
			if (serviceComponent.getDeployableUnit().equals(deployableUnitID)) {
				this.uninstallService((ServiceComponent) obj);
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("Uninstalled services on " + deployableUnitID);
		}
	}
	
	@Override
	public String toString() {
		return 	"Service Management: " +
				"\n+-- Services: " + deploymentCacheManager.getServiceComponents().keySet();
	}
}
