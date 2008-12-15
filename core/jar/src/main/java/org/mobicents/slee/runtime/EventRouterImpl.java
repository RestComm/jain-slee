/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.slee.management.SleeState;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.resource.FailureReason;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.serviceactivity.ServiceActivity;
import javax.transaction.SystemException;

import org.apache.commons.pool.ObjectPool;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.facilities.NullActivityContext;
import org.mobicents.slee.runtime.facilities.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.NullActivityImpl;
import org.mobicents.slee.runtime.facilities.TimerEventImpl;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Implements the router algorithm. See appendix B of the JAIN-SLEE
 * Specification.
 * 
 * @author M. Ranganathan
 * @author F.Moggia
 * @author Tim Fox
 * @author Ralf Siedow
 * @author Ivelin Ivanov
 * @author eduardomartins
 */

public class EventRouterImpl implements EventRouter {

	private SleeContainer container;

	private SleeTransactionManager txMgr;

	private EventTypeID activityEndEventId;

	private EventTypeID getActivityEndEventID() {
		if (activityEndEventId == null) {
			activityEndEventId = container.getEventManagement().getEventType(
					new ComponentKey("javax.slee.ActivityEndEvent",
							"javax.slee", "1.0"));
		}
		return activityEndEventId;
	}

	private EventTypeID timerEventId;

	private EventTypeID getTimerEventID() {
		if (timerEventId == null) {
			timerEventId = container.getEventManagement().getEventType(
					new ComponentKey("javax.slee.facilities.TimerEvent",
							"javax.slee", "1.0"));
		}
		return timerEventId;
	}

	/**
	 * Flag that turns on or off the monitoring of uncommitted modifications of
	 * AC attaches. When this flag is true, which means monitoring is on, if
	 * exist uncommitted attaches/detaches on the activity where one event is
	 * about to be routed, then that event (and others in queue) waits until no
	 * uncommitted modifications exist. If your apps don't suffer any
	 * concurrency issues on attach/detaches, and don't miss response events on
	 * activities after their creation,then turn this off to get more
	 * performance turn it off.
	 */
	public final static boolean MONITOR_UNCOMMITTED_AC_ATTACHS = false;

	// Executor Pool related fields
	// TODO: the executor pool size should be configurable
	public static int EXECUTOR_POOL_SIZE = 313;
	ExecutorService[] execs;

	private static Logger logger = Logger.getLogger(EventRouterImpl.class);

	// Each executor is indexed by activity.
	private ConcurrentHashMap<Object, ExecutorService> executors;

	public class EventExecutor implements Runnable {

		private DeferredEvent de;

		EventExecutor(DeferredEvent de) {
			this.de = de;
		}

		public void run() {
			// wait if there are txs running that have uncommitted modifications
			// to
			// the attachment set of sbb entities, to avoid concurrency issues.
			if (MONITOR_UNCOMMITTED_AC_ATTACHS) {
				try {
					while (TemporaryActivityContextAttachmentModifications
							.SINGLETON().hasTxModifyingAttachs(
									de.getActivityContextId())) {
						Thread.sleep(30);
					}
				} catch (InterruptedException e) {

					logger
							.warn("Routing event: "
									+ de.getEventTypeId()
									+ " activity "
									+ de.getActivity()
									+ " address "
									+ de.getAddress()
									+ " failed to ensure no temp attachs exist for the activity, re-routing...");

					// restart invocation
					run();
					return;
				}
			}
			if (routeQueuedEvent(de)) {
				processSucessfulEventRouting(de);
			}
		}
	}

	/**
	 * Pickup the next executor to handle an activity
	 * 
	 * @return
	 */
	private ExecutorService pickupExecutor() {
		return this.execs[executors.size() % EXECUTOR_POOL_SIZE];
	}

	/**
	 * Retreives the executor assigned to the specified activity, if there is no
	 * executor for specified activity this method assigns one
	 * 
	 * @param activity
	 * @return
	 */
	private ExecutorService getExecutor(Object activity) {
		ExecutorService executor = this.executors.get(activity);
		if (executor == null) {
			executor = pickupExecutor();
			ExecutorService otherExecutor = executors.putIfAbsent(activity,
					executor);
			if (otherExecutor != null) {
				return otherExecutor;
			}
		}
		return executor;
	}

	public void routeEvent(DeferredEvent de) {
		if (logger.isDebugEnabled()) {
			logger.debug("Routing event: " + de.getEventTypeId() + " activity "
					+ de.getActivity() + " address " + de.getAddress());
		}

		if (container.getSleeState().equals(SleeState.STOPPED)) {
			throw new SLEEException(
					"Mobicents SLEE container is in STOPPED state. Cannot route events.");
		}

		// execute routing of event
		this.getExecutor(de.getActivity()).execute(new EventExecutor(de));

		if (logger.isDebugEnabled())
			logger.debug("FINISHED routeEvent " + de.getEventTypeId());
	}

	/** Creates a new instance of EventRouterImpl */
	public EventRouterImpl(SleeContainer container) {

		// this.currentEvent = new HashMap();
		/*
		 * this.queue = new PooledExecutor(new LinkedQueue());
		 * this.queue.setKeepAliveTime(-1); this.queue.createThreads(5);
		 */

		this.executors = new ConcurrentHashMap<Object, ExecutorService>();
		this.container = container;
		this.txMgr = SleeContainer.getTransactionManager();
		this.executors = new ConcurrentHashMap<Object, ExecutorService>();
		// create executor service array, each one is a single thread excutor
		this.execs = new ExecutorService[EXECUTOR_POOL_SIZE];
		for (int i = 0; i < EXECUTOR_POOL_SIZE; i++) {
			this.execs[i] = Executors.newSingleThreadExecutor();
		}

	}

	/**
	 * Process the initial events of a service. This method possibly creates new
	 * Sbbs. The container keeps a factory that creates new Sbbs keyed on the
	 * convergence name of the service.
	 */
	private void processInitialEvents(ServiceComponent svc,
			MobicentsSbbDescriptor rootSbbDescriptor, SleeEvent eventObject)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Initial event processing for " + svc.getServiceID());
		}

		try {
			Exception caught = null;
			SbbEntity sbbEntity = null;
			SbbObject sbbObject = null;
			ClassLoader invokerClassLoader = null;
			ClassLoader oldClassLoader = Thread.currentThread()
					.getContextClassLoader();

			txMgr.begin();

			try {

				/*
				 * Start of SLEE originated invocation sequence
				 * ============================================ We run a SLEE
				 * originated invocation sequence here for every service that
				 * this might be an intial event for
				 */

				/*
				 * Use the initial event select to compute the convergence names
				 * for the service. This is a method that is provided by the
				 * service deployment. The names set is composed by only one
				 * convergence name the error is due an error in the pseudocode
				 */
				final Service service = Service.getService(svc
						.getServiceDescriptor());
				if (service.getState().isActive()) {

					String name = rootSbbDescriptor.computeConvergenceName(
							eventObject, svc);

					if (logger.isDebugEnabled()) {
						logger.debug("Convergence name computed for "
								+ svc.getServiceID() + " is " + name);
					}

					if (name != null) {

						if (!service.containsConvergenceName(name)) {

							if (logger.isDebugEnabled()) {
								logger.debug("not found the convergence name "
										+ name + ", creating new sbb entity");
							}
							// Create a new root sbb entity
							sbbEntity = service.addChild(name);
							// change class loader
							invokerClassLoader = rootSbbDescriptor
									.getClassLoader();
							Thread.currentThread().setContextClassLoader(
									invokerClassLoader);
							// invoke sbb lifecycle methods on sbb entity
							// creation
							try {
								sbbEntity.assignAndCreateSbbObject();
								sbbObject = sbbEntity.getSbbObject();
							} catch (Exception ex) {
								sbbObject = sbbEntity.getSbbObject();
								if (sbbObject != null) {
									try {
										sbbEntity.removeAndReleaseSbbObject();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								throw ex;
							}
							// attach sbb entity on AC
							container
									.getActivityContextFactory()
									.getActivityContextById(
											eventObject.getActivityContextID())
									.attachSbbEntity(sbbEntity.getSbbEntityId());
							// do the reverse on the sbb entity
							sbbEntity.afterACAttach(eventObject
									.getActivityContextID());

						} else {

							if (logger.isDebugEnabled()) {
								logger
										.debug("found the convergence name "
												+ name
												+ ", attaching entity to AC (if not attached yet)");
							}
							// get sbb entity id for this convergence name
							String rootSbbEntityId = service
									.getRootSbbEntityId(name);
							// attach sbb entity on AC
							container.getActivityContextFactory()
									.getActivityContextById(
											eventObject.getActivityContextID())
									.attachSbbEntity(rootSbbEntityId);
							// do the reverse on the sbb entity
							SbbEntityFactory.getSbbEntity(rootSbbEntityId)
									.afterACAttach(
											eventObject.getActivityContextID());
						}

					} else {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Service with id:"
											+ svc.getServiceID()
											+ " returns a null convergence name. Either the service does not exist or it is not interested in the event.");
						}
					}
				}
			} catch (Exception e) {
				logger.error("Caught an error! ", e);
				caught = e;
			} finally {
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}

			boolean invokeSbbRolledBack = handleRollback(sbbObject, null,
					caught, invokerClassLoader);
			// If there is no entity associated then the invokeSbbRolledBack is
			// handle in
			// the same tx, otherwise in a new tx (6.10.1)

			if (sbbEntity == null && invokeSbbRolledBack) {
				handleSbbRolledBack(sbbEntity, sbbObject, eventObject,
						invokerClassLoader, false);
			}
			// commit or rollback the tx. if the setRollbackOnly flag is set
			// then this will
			// trigger rollback action.
			if (logger.isDebugEnabled()) {
				logger.debug("Committing SLEE Originated Invocation Sequence");
			}
			txMgr.commit();
			// We may need to run sbbRolledBack for invocation sequence 1 in
			// another
			// tx
			if (sbbEntity != null && invokeSbbRolledBack) {
				handleSbbRolledBack(sbbEntity, sbbObject, eventObject,
						invokerClassLoader, false);
			}
			/*
			 * End of SLEE Originated Invocation sequence
			 * ==========================================
			 */
		} catch (Exception e) {
			logger.error("Failed to process initial event for "
					+ svc.getServiceID(), e);
		}
	}

	/**
	 * 
	 * @param sbbObject
	 *            The sbb object that was being invoked when the exception was
	 *            caught - can be null if SLEE wasn't calling an sbb object when
	 *            the exception was thrown
	 * @param sleeEvent -
	 *            the slee event - optional - only specified if it was an event
	 *            handler that threw the exception
	 * @param e -
	 *            the exception caught
	 * @param contextClassLoader
	 * 
	 * @return
	 */
	private boolean handleRollback(SbbObject sbbObject, SleeEvent sleeEvent,
			Exception e, ClassLoader contextClassLoader) {
		txMgr.assertIsInTx();

		boolean invokeSbbRolledBack = false;

		if (e != null && e instanceof RuntimeException) {

			// See spec. 9.12.2 for full details of what we do here
			if (logger.isInfoEnabled())
				logger
						.info(
								"Caught RuntimeException in invoking SLEE originated invocation",
								e);

			// We only invoke sbbExceptionThrown if there is an sbb Object *and*
			// an
			// sbb object method was being invoked when the exception was thrown
			if (sbbObject != null
					&& !sbbObject.getInvocationState().equals(
							SbbInvocationState.NOT_INVOKING)) {
				if (logger.isDebugEnabled()) {
					logger.debug("sbbObject is not null");
				}
				// Invoke sbbExceptionThrown method but only if it was a sbb
				// method that threw the
				// RuntimeException

				ClassLoader oldClassLoader = Thread.currentThread()
						.getContextClassLoader();
				try {
					Thread.currentThread().setContextClassLoader(
							contextClassLoader);

					try {
						txMgr.setRollbackOnly();
					} catch (SystemException ex) {
						throw new RuntimeException("Unexpected exception ! ",
								ex);
					}
					// Spec. 6.9. event and activity are null if exception was
					// not thrown at
					// event handler
					ActivityContextInterface aci = null;
					Object eventObject = null;
					if (sleeEvent != null) {
						aci = sleeEvent.getActivityContextInterface();

						eventObject = sleeEvent.getEventObject();
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Calling sbbExceptionThrown");
					}
					try {
						sbbObject.sbbExceptionThrown(e, eventObject, aci);
					} catch (Exception ex) {

						// If method throws an exception , just log it.
						if (logger.isDebugEnabled()) {
							logger
									.debug(
											"Threw an exception while invoking sbbExceptionThrown ",
											ex);
						}
					}

					// Spec section 6.10.1
					// The sbbRolledBack method is only invoked on SBB objects
					// in the Ready state.
					invokeSbbRolledBack = sbbObject.getState().equals(
							SbbObjectState.READY);

					// now we move the object to the does not exist state
					// (6.9.3)
					// sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);

					sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);
					// Mark tx for rollback
					if (logger.isDebugEnabled()) {
						logger.debug("handleRollback done");
					}
				} finally {
					Thread.currentThread()
							.setContextClassLoader(oldClassLoader);
				}
			}

		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Runtime exception was not thrown");
			}
			// See 9.12.2

			// We do this block if either the invocation sequence completed
			// successfully
			// OR only a checked exception was thrown

			if (sbbObject != null
					&& sbbObject.getSbbContext().getRollbackOnly()) {
				if (logger.isDebugEnabled()) {
					logger.debug("object is set rollbackonly=true");
					// The SBB signaled that a rollback is needed.
					// run the rollback method.
					logger.debug("sbb rolled back context "
							+ sbbObject.getSbbContext());
				}
				// Spec section 6.10.1
				// The sbbRolledBack method is only invoked on SBB objects in
				// the Ready state.
				invokeSbbRolledBack = sbbObject.getState().equals(
						SbbObjectState.READY);

			}
		}

		if (sbbObject == null && e != null) {
			invokeSbbRolledBack = true;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("InvokeSbbRolledBack?:" + invokeSbbRolledBack);
		}
		return invokeSbbRolledBack;
	}

	/**
	 * 
	 * @param sbbEntity
	 *            The sbb entity - optional - if the invocation sequence does
	 *            not have a target sbb entity this is null
	 * @param sbbObj
	 *            The sbb object - optional this is only specified if the
	 *            invocation sequence does not have a target sbb entity
	 * @param sleeEvent
	 *            The slee event - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param contextClassLoader
	 * @param removeRolledBack
	 * 
	 * 
	 */
	public void handleSbbRolledBack(SbbEntity sbbEntity, SbbObject sbbObj,
			SleeEvent sleeEvent, ClassLoader contextClassLoader,
			boolean removeRolledBack) {
		// Sanity checks
		if ((sbbEntity == null && sbbObj == null)
				|| (sbbEntity != null && sbbObj != null)) {
			logger
					.error("Illegal State! Only one of sbbEntity or SbbObject can be specified");

			return;
		}

		/*
		 * Depending on whether exceptions were thrown during the invocation
		 * sequence we may need to invoke the sbbRolledBack method This must be
		 * done on a different sbb object and a new transaction See Spec. Sec.
		 * 9.12.2 for details
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking sbbRolledBack");
		}

		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		try {

			// Only start new tx if there's a target sbb entity (6.10.1)
			if (sbbEntity != null) {
				String sbbId = sbbEntity.getSbbEntityId();
				txMgr.begin();
				// we have to refresh the sbb entity by reading it frmo the
				// cache
				sbbEntity = SbbEntityFactory.getSbbEntity(sbbId);
			}

			RolledBackContext rollbackContext = new RolledBackContextImpl(
					sleeEvent == null ? null : sleeEvent.getEventObject(),
					sleeEvent == null ? null
							: new ActivityContextInterfaceImpl(sleeEvent
									.getActivityContextID()), removeRolledBack);

			Thread.currentThread().setContextClassLoader(contextClassLoader);

			if (sbbEntity != null) {
				// We invoke the callback method a *different* sbb object 9.12.2
				// and 6.10.1
				if (logger.isDebugEnabled()) {
					logger
							.debug("Invoking sbbRolledBack on different sbb object");
				}
				ObjectPool pool = sbbEntity.getObjectPool();

				// Get rid of old object (if any) first
				if (sbbEntity.getSbbObject() != null) {
					// This was set to DOES_NOT_EXIST here because
					// unsetSbbContext
					// should not be called in this case.
					sbbEntity.getSbbObject().setState(
							SbbObjectState.DOES_NOT_EXIST);
					pool.invalidateObject(sbbEntity.getSbbObject());
				}

				sbbEntity.assignAndActivateSbbObject();
				sbbObj = sbbEntity.getSbbObject();
				sbbObj.sbbLoad();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking sbbRolledBack");
			}
			// We only invoke this on objects in the ready state 6.10.1
			// E.g. if an exception was thrown from a sbbCreate then there will
			// be no sbb entity
			// and the the sbbobject won't be in the ready state so we invoke it
			if (sbbObj.getState().equals(SbbObjectState.READY))
				sbbObj.sbbRolledBack(rollbackContext);

			if (sbbEntity != null) {
				sbbObj.sbbStore();
			}

			if (sbbEntity != null) {
				try {
					txMgr.commit();
				} catch (SystemException ex) {
					ex.printStackTrace();
					throw new RuntimeException("tx manager System Failure ", ex);
				}
			}
		} catch (Exception e) {
			// If an exception is thrown here we just log it and don't retry
			if (sbbObj != null && sbbEntity != null) {
				sbbObj = sbbEntity.getSbbObject();
				sbbObj.setState(SbbObjectState.DOES_NOT_EXIST);
			}
			logger
					.error(
							"Exception thrown in attempting to invoke sbbRolledBack",
							e);
			sbbObj.sbbExceptionThrown(e, sleeEvent.getEventObject(), sleeEvent
					.getActivityContextInterface());
		} finally {
			try {
				if (txMgr.isInTx())
					txMgr.commit();
			} catch (Exception e2) {
				logger.error("Failed to commit transaction", e2);
				throw new RuntimeException("Failed to commit tx ", e2);
			}

			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	public void serializeTaskForActivity(Runnable r, Object activity) {
		if (logger.isDebugEnabled()) {
			logger.debug("serializeTaskForActivity() for activity " + activity);
		}
		// get executor directly from map, if does not exist one for the
		// activity then just pickup up one
		// to execute the task
		ExecutorService executor = executors.get(activity);
		if (executor != null) {
			executor.execute(r);
		} else {
			pickupExecutor().execute(r);
		}
	}

	/**
	 * Delivers to SBBs an event off the top of the queue for an activity
	 * context
	 * 
	 * @param de
	 */
	private boolean routeQueuedEvent(DeferredEvent de) {
		if (logger.isDebugEnabled())
			logger.debug("\n\n\nrouteTheEvent : [[[ eventId "
					+ de.getEventTypeId());

		SleeEventImpl eventObject = null;
		String activityContextId = null;
		EventTypeID eventTypeID = null;
		ActivityContext ac = null;

		// these flags will signal that the event being routed has been removed
		// from the AC's outstanding events queue
		boolean removedOutstandingEventFromAC = false;
		boolean removalOutstandingEventFromACCommitted = false;

		boolean rb = true;
		try {

			ServiceID[] serviceIDs = null;
			HashMap<ServiceID, ServiceComponent> services = new HashMap<ServiceID, ServiceComponent>();
			HashMap<ServiceID, MobicentsSbbDescriptor> rootSbbComponents = new HashMap<ServiceID, MobicentsSbbDescriptor>();

			txMgr.begin();
			try {

				// get ac & it's id
				ac = container.getActivityContextFactory().getActivityContext(
						de.getActivity());
				activityContextId = ac.getActivityContextId();

				// the removal of the outstanding events can't always be done
				// here, because if the ac is a null ac and no other refs exist
				// then on commit the activity end will be scheduled
				int outStandingEvents = ac.getOutstandingEvents();
				if (outStandingEvents > 1) {
					// we can safely remove the outstanding event here
					ac.removeOutstandingEvent(de);
					removedOutstandingEventFromAC = true;
					if (de.getEventTypeId().equals(getActivityEndEventID())) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("can't deliver activity end now, delaying till outstanding events are empty");
						}
						// can't deliver activity end now, freeze activity end
						// event
						ac.setFrozenActivityEndEvent(de);
						return false;
					}
				} else {
					if (ac instanceof NullActivityContext) {
						if (de.getEventTypeId().equals(getActivityEndEventID())) {
							// activity is ending, also no problem removing the
							// outstanding event here
							ac.removeOutstandingEvent(de);
							removedOutstandingEventFromAC = true;
						}
					} else {
						// well, even if there are no more outstanding events,
						// this
						// is no null ac, no problem removing outstanding event
						// here
						// we can safely remove the outstanding event here
						ac.removeOutstandingEvent(de);
						removedOutstandingEventFromAC = true;
					}
				}
				eventObject = new SleeEventImpl(de.getEventTypeId(), de
						.getEvent(), activityContextId, de.getActivity(), de
						.getAddress());

				eventTypeID = eventObject.getEventTypeID();

				if (logger.isDebugEnabled()) {
					logger.debug("Retrieveing active services...");
				}

				serviceIDs = container.getServiceManagement().getServices(
						ServiceState.ACTIVE);

				// Iterate through each service that has the event type as an
				// initial
				// event type.
				for (int j = 0; j < serviceIDs.length; j++) {
					ServiceComponent serviceComponent = this.container
							.getServiceManagement().getServiceComponent(
									serviceIDs[j]);
					if (serviceComponent != null) {
						MobicentsSbbDescriptor rootSbbDescriptor = serviceComponent
								.getRootSbbComponent();
						if (rootSbbDescriptor != null) {
							services.put(serviceIDs[j], serviceComponent);
							rootSbbComponents.put(serviceIDs[j],
									rootSbbDescriptor);
						}
					}
				}

			} catch (Throwable e) {
				logger.error(
						"Failure while routing event; first phase. DefferedEvent ["
								+ de.getEventTypeId() + "]", e);
				removedOutstandingEventFromAC = false;
			} finally {
				txMgr.commit();
			}

			if (removedOutstandingEventFromAC) {
				// don't bother again with the outstanding event, it's removal
				// was committed
				removalOutstandingEventFromACCommitted = true;
			}

			// INITIAL EVENT PROCESSING
			for (Iterator<ServiceID> i = services.keySet().iterator(); i
					.hasNext();) {
				ServiceID serviceID = i.next();
				if (rootSbbComponents.get(serviceID).getInitialEventTypes()
						.contains(eventTypeID)) {
					processInitialEvents(services.get(serviceID),
							rootSbbComponents.get(serviceID), eventObject);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Event is not initial for " + serviceID);
					}
				}
			}

			boolean gotSbb = false;
			boolean toSleep = false;

			/*
			 * TODO one tx per service which declares the event type as initial
			 * and already attached services[] begin() find
			 * highestPrioritySbbEntity find highestPriorityService if
			 * (highestPriorityService.getPriority() >
			 * highestPrioritySbbEntity.getPriority()) { sbbEntity =
			 * processInitialEvent(highestPriorityService);
			 * services.remove(highestPriorityService); if (sbbEntity == null) {
			 * sbbEntity = highestPrioritySbbEntity; } }
			 * addToDeliveredSet(sbbEntity) invokeEventHandler(sbbEntity)
			 */

			// For each SBB that is attached to this activity context.
			do {
				String rootSbbEntityId = null;
				ClassLoader invokerClassLoader = null;
				SbbEntity sbbEntity = null;
				SbbObject sbbObject = null;

				try {

					/*
					 * Start of SLEE Originated Invocation Sequence
					 * ============================================== This
					 * sequence consists of either: 1) One "Op Only" SLEE
					 * Originated Invocation - in the case that it's a
					 * straightforward event routing for the sbb entity. 2) One
					 * "Op and Remove" SLEE Originated Invocation - in the case
					 * it's an event routing to a root sbb entity that ends up
					 * in a remove to the same entity since the attachment count
					 * goes to zero after the event invocation 3) One "Op Only"
					 * followed by one "Remove Only" SLEE Originated Invocation -
					 * in the case it's an event routing to a non-root sbb
					 * entity that ends up in a remove to the corresponding root
					 * entity since the root attachment count goes to zero after
					 * the event invocation Each Invocation Sequence is handled
					 * in it's own transaction. All exception handling for each
					 * invocation sequence is handled here. Any exceptions that
					 * propagate up aren't necessary to be caught. -Tim
					 */

					// If this fails then we propagate up since there's nothing
					// to
					// roll-back anyway
					if (toSleep) {
						Thread.sleep(100);
						toSleep = false;
					}
					txMgr.begin();
					if (logger.isDebugEnabled()) {
						logger
								.debug("Delivering event to sbb entities attached to AC...");
					}

					Exception caught = null;
					SbbEntity highestPrioritySbbEntity = null;
					ClassLoader oldClassLoader = Thread.currentThread()
							.getContextClassLoader();
					try {

						// LOAD THE AC FOR THIS TRANSACTION
						ac = (ActivityContext) container
								.getActivityContextFactory()
								.getActivityContextById(activityContextId);

						try {
							highestPrioritySbbEntity = findSbbEntityForDelivering(
									ac, eventTypeID);
						} catch (Exception e) {
							logger.warn(
									"Exception in findSbbEntityForDelivering( ac["
											+ ac + "],\n" + "  eventTypeID["
											+ eventTypeID + "]). \n"
											+ "  Reason: " + e.getMessage(), e);
							highestPrioritySbbEntity = null;
						}

						if (highestPrioritySbbEntity == null) {
							if (logger.isDebugEnabled()) {
								logger
										.debug("No more sbbs to deliver the event");
							}
							ac.clearDeliveredSet();
							// this is only false if and only if iter.hasNext()
							// == false
							gotSbb = false;

							// do we still need to remove the outstanding event
							// at this phase?
							if (!removalOutstandingEventFromACCommitted) {
								// yes so let's try to use this tx to remove
								// outstanding event
								ac.removeOutstandingEvent(de);
								removedOutstandingEventFromAC = true;
							}

						} else {
							gotSbb = true;

							// TODO: Warning that if anything above the else
							// part failed;
							// then we can go into the Exception and then back
							// to the
							// infinity while loop.
							ac.addToDeliveredSet(highestPrioritySbbEntity
									.getSbbEntityId());

						}

						if (gotSbb) {

							if (logger.isDebugEnabled()) {
								logger
										.debug("Highest priority SBB entity to deliver the event: "
												+ highestPrioritySbbEntity);
							}

							// CHANGE CLASS LOADER
							invokerClassLoader = highestPrioritySbbEntity
									.getSbbDescriptor().getClassLoader();
							Thread.currentThread().setContextClassLoader(
									invokerClassLoader);

							sbbEntity = highestPrioritySbbEntity;
							rootSbbEntityId = sbbEntity.getRootSbbId();

							sbbEntity.setCurrentEvent(eventObject);

							// Assign an sbb from the pool if was not assigned
							// in initial event processing
							if (sbbEntity.getSbbObject() == null) {
								sbbEntity.assignAndActivateSbbObject();
							}

							sbbObject = sbbEntity.getSbbObject();
							sbbObject.sbbLoad();

							// GET AND CHECK EVENT MASK FOR THIS SBB ENTITY
							Set eventMask = sbbEntity.getMaskedEventTypes(ac
									.getActivityContextId());
							if (!eventMask.contains(eventObject
									.getEventTypeID())) {

								// TIME TO INVOKE THE EVENT HANDLER METHOD
								sbbObject
										.setSbbInvocationState(SbbInvocationState.INVOKING_EVENT_HANDLER);

								sbbEntity.invokeEventHandler(eventObject);

								// check to see if the transaction is marked for
								// rollback
								// if it is then we need to get out of here soon
								// as we can.
								// JBOSS cache does not allow a transaction to
								// go back to
								// read the cache that it currently the owner
								// which can
								// cause lock timeout problem

								if (txMgr.getRollbackOnly()) {
									throw new Exception(
											"The transaction is marked for rollback");
								}

								sbbObject
										.setSbbInvocationState(SbbInvocationState.NOT_INVOKING);

							} else {
								if (logger.isDebugEnabled()) {
									logger
											.debug("Not invoking event handler since event is masked");
								}
							}

							// IF IT'S AN ACTIVITY END EVENT DETACH SBB ENTITY
							// HERE
							if (eventObject.getEventTypeID().equals(
									getActivityEndEventID())) {
								highestPrioritySbbEntity.afterACDetach(ac
										.getActivityContextId());
							}

							// CHECK IF WE CAN CLAIM THE ROOT SBB ENTITY
							if (rootSbbEntityId != null) {
								if (SbbEntityFactory.getSbbEntity(
										rootSbbEntityId).getAttachmentCount() != 0) {
									// the root sbb entity is not be claimed
									rootSbbEntityId = null;
								}
							} else {
								// it's a root sbb
								if (!sbbEntity.isRemoved()
										&& sbbEntity.getAttachmentCount() == 0) {
									if (logger.isDebugEnabled()) {
										logger
												.debug("Attachment count for sbb entity "
														+ sbbEntity
																.getSbbEntityId()
														+ " is 0, removing it...");
									}
									// If it's the same entity then this is an
									// "Op and
									// Remove Invocation Sequence"
									// so we do the remove in the same
									// invocation
									// sequence as the Op
									SbbEntityFactory.removeSbbEntity(sbbEntity,
											true);
								}
							}
						}
					} catch (Exception e) {
						logger.error(
								"Failure while routing event; second phase. DeferredEvent ["
										+ de.getEventTypeId() + "]", e);
						if (highestPrioritySbbEntity != null)
							sbbObject = highestPrioritySbbEntity.getSbbObject();
						caught = e;
					} finally {

						// do a final check to see if there is another SBB to
						// deliver.
						// We don't want to waste another loop. Note that
						// rollback
						// will not has any impact on this because the
						// ac.DeliveredSet
						// is not in the cache.
						if (gotSbb) {
							boolean skipAnotherLoop = false;
							try {
								if (findSbbEntityForDelivering(ac, eventTypeID) == null) {
									skipAnotherLoop = true;
								}
							} catch (Exception e) {
								skipAnotherLoop = true;
							} finally {
								if (skipAnotherLoop) {
									gotSbb = false;
									ac.clearDeliveredSet();
									// do we still need to remove the
									// outstanding event at this phase?
									if (!removalOutstandingEventFromACCommitted) {
										// yes so let's try to use this tx to
										// remove outstanding event
										ac.removeOutstandingEvent(de);
										removedOutstandingEventFromAC = true;
									}
								}
							}
						}

						Thread.currentThread().setContextClassLoader(
								oldClassLoader);
					}

					boolean invokeSbbRolledBack = handleRollback(sbbObject,
							eventObject, caught, invokerClassLoader);

					boolean invokeSbbRolledBackRemove = false;
					ClassLoader rootInvokerClassLoader = null;
					SbbEntity rootSbbEntity = null;

					if (!invokeSbbRolledBack && rootSbbEntityId != null) {
						/*
						 * If we get here this means that we need to do a
						 * cascading remove of the root sbb entity - since the
						 * original invocation was done on a non-root sbb entity
						 * then this is done in a different SLEE originated
						 * invocation, but inside the same SLEE originated
						 * invocation sequence. Confused yet? This is case 3) in
						 * my previous comment - the SLEE originated invocation
						 * sequence contains two SLEE originated invocations:
						 * One "Op Only" and One "Remove Only" This is the
						 * "Remove Only" part. We don't bother doing this if we
						 * already need to rollback
						 */

						caught = null;

						oldClassLoader = Thread.currentThread()
								.getContextClassLoader();

						try {
							rootSbbEntity = SbbEntityFactory
									.getSbbEntity(rootSbbEntityId);

							rootInvokerClassLoader = rootSbbEntity
									.getSbbDescriptor().getClassLoader();
							Thread.currentThread().setContextClassLoader(
									rootInvokerClassLoader);

							SbbEntityFactory.removeSbbEntity(rootSbbEntity,
									true);

						} catch (Exception e) {
							logger.error(
									"Failure while routing event; third phase. Event Posting ["
											+ de + "]", e);
							caught = e;
						} finally {

							Thread.currentThread().setContextClassLoader(
									oldClassLoader);
						}

						// We have no target sbb object in a Remove Only SLEE
						// originated invocation
						invokeSbbRolledBackRemove = handleRollback(null,
								eventObject, caught, rootInvokerClassLoader);
					}

					/*
					 * We are now coming to the end of the SLEE originated
					 * invocation sequence We may need to run sbbRolledBack This
					 * is done in the same tx if there is no target sbb entity
					 * in any of the SLEE originated invocations making up this
					 * SLEE originated invocation sequence Otherwise we do it in
					 * a separate tx for each SLEE originated invocation that
					 * has a target sbb entity. In other words we might have a
					 * maximum of 2 sbbrolledback callbacks invoked in separate
					 * tx in the case this SLEE Originated Invocation Sequence
					 * contained an Op Only and a Remove Only (since these have
					 * different target sbb entities) Pretty obvious really! ;)
					 */
					if (invokeSbbRolledBack && sbbEntity == null) {
						// We do it in this tx
						handleSbbRolledBack(null, sbbObject, null,
								invokerClassLoader, false);
					} else if (sbbEntity != null && !txMgr.getRollbackOnly()
							&& sbbEntity.getSbbObject() != null) {

						sbbObject.sbbStore();
						sbbEntity.passivateAndReleaseSbbObject();

					}

					if (txMgr.getRollbackOnly()) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Rolling back SLEE Originated Invocation Sequence");
						}
						txMgr.rollback();
					} else {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Committing SLEE Originated Invocation Sequence");
						}
						txMgr.commit();
						if (!removalOutstandingEventFromACCommitted
								&& removedOutstandingEventFromAC) {
							removalOutstandingEventFromACCommitted = true;
						}
					}

					/*
					 * Now we invoke sbbRolledBack for each SLEE originated
					 * invocation that had a target sbb entity in a new tx - the
					 * new tx creating is handled inside the handleSbbRolledBack
					 * method
					 */
					if (invokeSbbRolledBack && sbbEntity != null) {
						// Firstly for the "Op only" or "Op and Remove" part
						sbbEntity.getSbbEntityId();
						if (logger.isDebugEnabled()) {
							logger
									.debug("Invoking sbbRolledBack for Op Only or Op and Remove");

						}
						handleSbbRolledBack(sbbEntity, null, eventObject,
								invokerClassLoader, false);
					}
					if (invokeSbbRolledBackRemove) {
						// Now for the "Remove Only" if appropriate
						handleSbbRolledBack(rootSbbEntity, null, null,
								rootInvokerClassLoader, true);
					}

					/*
					 * A note on exception handling here- Any exceptions thrown
					 * further further up that need to be caught in order to
					 * handle rollback or otherwise maintain consistency of the
					 * SLEE state are all handled further up I.e. We *do not*
					 * need to call rollback here. So any exceptions that get
					 * here do not result in the SLEE being in an inconsistent
					 * state, therefore we just log them and carry on.
					 */

					rb = false;
				} catch (RuntimeException e) {
					logger.error(
							"Unhandled RuntimeException in event router: ", e);
				} catch (Exception e) {
					logger.error("Unhandled Exception in event router: ", e);
				} catch (Error e) {
					logger.error("Unhandled Error in event router: ", e);
					throw e; // Always rethrow errors
				} catch (Throwable t) {
					logger.error("Unhandled Throwable in event router: ", t);
				} finally {
					try {
						if (txMgr.isInTx()) {
							if (rb) {
								logger
										.error("Rolling back tx in routeTheEvent.");
								txMgr.rollback();
							} else {
								logger
										.error("Transaction left open in routeTheEvent! It has to be pinned down and fixed! Debug information follows.");
								logger.error(txMgr
										.displayOngoingSleeTransactions());
								txMgr.commit();
							}
						}
					} catch (SystemException se) {

						logger.error("Failure in TX operation", se);
					}
					if (sbbEntity != null) {
						if (logger.isDebugEnabled()) {
							logger
									.debug("Finished delivering the event "
											+ de.getEventTypeId()
											+ " to the sbbEntity = "
											+ sbbEntity.getSbbEntityId()
											+ "]]] \n\n\n");
						}
					}
				}
				// need to ensure gotSbb = false if and only if iter.hasNext()
				// == false
			} while (gotSbb);

			/*
			 * End of SLEE Originated Invocation Sequence
			 * ==========================================
			 * 
			 */

			if (de.getEventTypeId().equals(getActivityEndEventID())) {
				handleActivityEndEvent(eventObject);
			} else if (de.getEventTypeId().equals(getTimerEventID())) {
				// get timer task from event, and check if timer should be
				// cancelled
				TimerEventImpl timerEventImpl = (TimerEventImpl) de.getEvent();
				if (timerEventImpl.isLastTimerEvent()) {
					container.getTimerFacility().cancelTimer(
							timerEventImpl.getTimerID());
				}
			}

		} catch (Exception e) {
			logger.error("Unhandled Exception in event router top try", e);
		}

		if (!removalOutstandingEventFromACCommitted) {
			// seems that after all we need a new tx to remove the
			// outstanding event which may end the null ac
			try {
				txMgr.begin();
				ac.removeOutstandingEvent(de);
				txMgr.commit();
			} catch (Exception e) {
				logger
						.error(
								"failed to end tx to remove outstanding event on null ac, possible null ac leak",
								e);
				try {
					txMgr.rollback();
					// possible leak, even if there is just a remote
					// chance this will happen, better to use activity
					// mbean to ensure it's fixed
				} catch (SystemException e1) {
					// ignore
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * Execute cascading remove on any root sbb entities whose attachment count
	 * has gone to zero after activity end event has detached the sbbs. If the
	 * activity end event is a received event for the sbb then this would be
	 * handled in the previous slee originated invocation sequence. It's done
	 * here in the case that the sbb is attached to the ac and so needs to be
	 * detached but activity end event is not one of it's received events. So
	 * this is a SLEE Originated Invocation Sequence containing at most one SLEE
	 * Originated Invocation of type "Remove Only"
	 * 
	 * @param eventObject
	 * @param ac
	 * @return
	 * @throws SystemException
	 */
	private void handleActivityEndEvent(SleeEventImpl eventObject)
			throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("Handling an activity end event on AC "
					+ eventObject.getActivityContextID());
		}

		/*
		 * Start of SLEE Originated Invocation Sequence
		 * ============================================ We start a new SLEE
		 * originated invocation sequence to execute the cascading removes.
		 */

		boolean hasMore = false;
		Iterator iter = null;

		do {

			boolean invokeSbbRolledBack = false;
			Exception caught = null;
			SbbEntity rootSbbEntity = null;
			SbbEntity sbbEntity = null;
			ActivityContext ac = null;
			String sbbEntityId = null;

			try {
				txMgr.begin();

				ac = (ActivityContext) container.getActivityContextFactory()
						.getActivityContextById(
								eventObject.getActivityContextID());

				if (iter == null) {
					iter = ac.getSbbAttachmentSet().values().iterator();
				}

				hasMore = iter.hasNext();

				if (hasMore) {

					sbbEntityId = (String) iter.next();

					if (logger.isDebugEnabled()) {
						logger.debug("Dettaching sbb entity " + sbbEntityId
								+ " on handle activity end event for ac "
								+ eventObject.getActivityContextID());
					}

					// get sbb entity
					try {
						sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
					} catch (Exception e) {
						// ignore
					}

					if (sbbEntity != null) {
						// detach from ac
						sbbEntity.afterACDetach(eventObject
								.getActivityContextID());
						// get it's root sbb entity
						if (sbbEntity.isRootSbbEntity()) {
							rootSbbEntity = sbbEntity;
						} else {
							try {
								rootSbbEntity = SbbEntityFactory
										.getSbbEntity(sbbEntity.getRootSbbId());
							} catch (Exception e) {
								// ignore
							}
						}
						// check root sbb entity attach count
						if (rootSbbEntity != null
								&& rootSbbEntity.getAttachmentCount() == 0) {
							// attach count is 0 so we claim the root sbb entity
							if (logger.isDebugEnabled()) {
								logger
										.debug("Removing root sbb entity "
												+ sbbEntity.getRootSbbId()
												+ " because AC attachement count is now 0.");
							}
							// change classloader since we will invoke sbb
							// lifecylce methods on sbb entity removal
							ClassLoader oldClassLoader = Thread.currentThread()
									.getContextClassLoader();
							try {
								Thread.currentThread().setContextClassLoader(
										rootSbbEntity.getSbbDescriptor()
												.getClassLoader());
								if (!container.getServiceManagement()
										.getService(sbbEntity.getServiceId())
										.getState().isActive()) {
									// service is inactive, which means it has
									// been deactivated,
									// we don't need to remove the root sbb
									// entity from the service
									SbbEntityFactory.removeSbbEntity(
											rootSbbEntity, false);
								} else {
									// service is active, remove the root sbb
									// entity, including it's entry from the
									// service
									SbbEntityFactory.removeSbbEntity(
											rootSbbEntity, true);
								}
							} finally {
								// restore old class loader
								Thread.currentThread().setContextClassLoader(
										oldClassLoader);
							}
						}
					}

				}

				// see if there is any more sbb entity to detach the AC
				hasMore = iter.hasNext();
				if (!hasMore) {
					// no more sbb entities, lets try to take advantage of this
					// tx to remove the AC
					// check activity type
					if (ac.getActivity() instanceof SleeActivityHandle) {
						// external activity, notify RA that the activity has
						// ended
						SleeActivityHandle sah = (SleeActivityHandle) ac
								.getActivity();
						if (sah != null) {
							ResourceAdaptor ra = sah.getResourceAdaptor();
							if (ra != null) {
								ra.activityEnded(sah.getHandle());
							}
						}
					} else if (ac.getActivity() instanceof NullActivityImpl) {
						// null activity, remove from factory
						NullActivityFactoryImpl nullActivityFactory = container
								.getNullActivityFactory();
						nullActivityFactory.removeNullActivity(ac
								.getActivityContextId());
					} else if (ac.getActivity() instanceof ProfileTableActivity) {
						// profile table activity, clean up
						container.getSleeProfileManager()
								.removeProfileAfterTableActivityEnd(
										((ProfileTableActivity) ac
												.getActivity())
												.getProfileTableName());
					} else if (ac.getActivity() instanceof ServiceActivity) {
						// service activity ending
						ServiceActivityImpl serviceActivity = (ServiceActivityImpl) ac
								.getActivity();
						// change service state to inactive
						container.getServiceManagement().getService(
								serviceActivity.getService()).setState(
								ServiceState.INACTIVE);
						// schedule task to remove outstanding root sbb entities
						// of the service
						new RootSbbEntitiesRemovalTask(serviceActivity
								.getService());
						logger.info("Deactivated "
								+ serviceActivity.getService());
					}
					// remove references to this AC in timer and ac naming
					// facility
					ac.removeNamingBindings();
					ac.removeFromTimers(); // Spec 7.3.4.1 Step 10
					if (logger.isDebugEnabled()) {
						logger
								.debug("Removed naming and timers references to AC "
										+ ac.getActivityContextId());
					}
					// changeac state
					ac.setState(ActivityContextState.INVALID);
					// finally remove ac
					this.container.getActivityContextFactory()
							.removeActivityContext(ac.getActivityContextId());
					// End of SLEE Originated Invocation Sequence
				}

			} catch (Exception e) {
				logger.error("Failure while handling ActivityEndEvent. Event ["
						+ eventObject + "]", e);
				caught = e;
			} finally {
				boolean txCommitted = false;
				if (rootSbbEntity != null)
					invokeSbbRolledBack = handleRollback(null, null, caught,
							rootSbbEntity.getSbbDescriptor().getClassLoader());

				try {
					txMgr.commit();
					txCommitted = true;
				} catch (Exception ex) {
					logger.error("Problem committing transaction!", ex);
					// reset flag to make sure we do one more round, since the
					// last tx,
					// where the ac is removed, must commit
					hasMore = true;
				}
				// We may need to run sbbRolledBack
				if (txCommitted && invokeSbbRolledBack) {
					try {
						if (rootSbbEntity != null)
							handleSbbRolledBack(rootSbbEntity, null, null,
									rootSbbEntity.getSbbDescriptor()
											.getClassLoader(), true);
					} catch (Exception ex) {
						logger.error(
								"problem in handleSbbRolledBack processing! ",
								ex);
					}
				}
			}
		} while (hasMore);

		// remove executor reference to activity
		removeExecutor(eventObject.getActivity());
		// stop management of temp attachs for the ac
		if (MONITOR_UNCOMMITTED_AC_ATTACHS) {
			TemporaryActivityContextAttachmentModifications.SINGLETON()
					.activityContextEnded(eventObject.getActivityContextID());
		}

	}

	/**
	 * @param activity
	 */
	private void removeExecutor(Object activity) {
		this.executors.remove(activity);
	}

	private SbbEntity findSbbEntityForDelivering(ActivityContext ac,
			EventTypeID eventTypeID) {

		String sbbEntityId = null;
		SbbEntity sbbEntity = null;

		// get the highest priority sbb from sbb entities attached to AC
		for (Iterator iter = ac.getSortedCopyOfSbbAttachmentSet().iterator(); iter
				.hasNext();) {
			sbbEntityId = (String) iter.next();
			// check sbb entity is not on the delivery set
			if (ac.deliveredSetContains(sbbEntityId)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Already delivered event to sbbEntityId "
							+ sbbEntityId + ", skipping...");
				}
				continue;
			}
			try {
				sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
				// check event is allowed to be handled by the sbb
				if (sbbEntity.getSbbDescriptor().getReceivedEvents().contains(
						eventTypeID)) {
					return sbbEntity;
				} else {
					if (logger.isDebugEnabled()) {
						logger
								.debug("Event is not received by sbb descriptor of entity "
										+ sbbEntityId + ", skipping...");
					}
					continue;
				}
			} catch (IllegalStateException e) {
				// ignore, sbb entity has been removed
				continue;
			}
		}

		return null;

	}

	protected void processSucessfulEventRouting(DeferredEvent de) {
		if (!container.getSleeState().equals(SleeState.STOPPED)) {
			if (de.getActivity() instanceof SleeActivityHandle) {
				SleeActivityHandle sleeActivityHandle = (SleeActivityHandle) de
						.getActivity();
				// Call the RA back that we successfully processed
				// event.
				sleeActivityHandle.getResourceAdaptor()
						.eventProcessingSuccessful(
								sleeActivityHandle.getHandle(),
								de.getEvent(),
								((EventTypeIDImpl) de.getEventTypeId())
										.getEventID(), de.getAddress(), 0);
			}
		}
	}

	protected void processUnsucessfulEventRouting(FailureReason failureReason,
			DeferredEvent de) {
		if (!container.getSleeState().equals(SleeState.STOPPED)) {
			if (de.getActivity() instanceof SleeActivityHandle) {
				SleeActivityHandle sleeActivityHandle = (SleeActivityHandle) de
						.getActivity();
				// Call the RA back that a failure existed while processing the
				// event.
				sleeActivityHandle.getResourceAdaptor().eventProcessingFailed(
						sleeActivityHandle.getHandle(), de.getEvent(),
						((EventTypeIDImpl) de.getEventTypeId()).getEventID(),
						de.getAddress(), 0, failureReason);

			}
		}
	}

	public String toString() {
		return "EventRouterImpl[executors.size() =" + executors.size() + "]";
	}

}
