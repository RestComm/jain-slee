package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.Set;

import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.eventrouter.ActivityEndEventImpl;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.DeferredEventReferencesManagement;
import org.mobicents.slee.runtime.eventrouter.EventContextImpl;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
import org.mobicents.slee.runtime.eventrouter.PendingAttachementsMonitor;
import org.mobicents.slee.runtime.eventrouter.SbbInvocationState;
import org.mobicents.slee.runtime.facilities.TimerEventImpl;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class EventRoutingTask implements Runnable {

	private final static Logger logger = Logger.getLogger(EventRoutingTask.class);
	private static SleeContainer container;
	private static SleeTransactionManager txMgr;
	
	static {
		container = SleeContainer.lookupFromJndi();
		txMgr = container.getTransactionManager();
	}
	
	/**
	 * 
	 * @author Miguel
	 *
	 */
	private static enum RoutingPhase { DELIVERING , END_ACTIVITY, END_TIMER };
	
	/**
	 * processing logic to handle a tx rollback
	 */
	private static final HandleRollback handleRollback = new HandleRollback();
	
	/**
	 * processing logic to handle a sbb tx rollback
	 */
	private static final HandleSbbRollback handleSbbRollback = new HandleSbbRollback();
	
	/**
	 * processing logic to handle an event as initial
	 */
	private static final InitialEventProcessor initialEventProcessor = new InitialEventProcessor(container);
	
	/**
	 * processing logic after a timer event has been routed
	 */
	private static final TimerEventPostProcessor timerEventPostProcessor = new TimerEventPostProcessor();
	
	/**
	 * processing logic to retrieve next sbb entity to handle an event on an activity
	 */
	private static final NextSbbEntityFinder nextSbbEntityFinder = new NextSbbEntityFinder();
	
	/**
	 * 
	 */
	private final DeferredEvent de;
	
	/**
	 * indicates which phase we are in routing of event
	 */
	private RoutingPhase routingPhase = RoutingPhase.DELIVERING;
	
	/**
	 * 
	 * @param de
	 */
	public EventRoutingTask(DeferredEvent de) {
		this.de = de;
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		PendingAttachementsMonitor pendingAttachementsMonitor = de.getEventRouterActivity().getPendingAttachementsMonitor();
		if (pendingAttachementsMonitor != null) {
			pendingAttachementsMonitor.waitTillNoTxModifyingAttachs();
		}
		
		if(System.getSecurityManager()!=null)
		{
			AccessController.doPrivileged(new PrivilegedAction(){

				public Object run(){
					routeQueuedEvent();
					return null;
				}});
		}else
		{
			routeQueuedEvent();
		}
		
	}

	/**
	 * Delivers to SBBs an event off the top of the queue for an activity
	 * context
	 * 
	 * @param de
	 * @return true if the event processing suceeds
	 */
	private void routeQueuedEvent() {

		boolean debugLogging = logger.isDebugEnabled();
		
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {

			EventContextImpl eventContextImpl = de.getEventRouterActivity().getCurrentEventContext();
			if (eventContextImpl == null) {				
				if (debugLogging)
					logger.debug("\n\n\nDelivering event : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextHandle()+"\n");
				
				// event context not set, create it 
				eventContextImpl = new EventContextImpl(de,container);
				de.getEventRouterActivity().setCurrentEventContext(eventContextImpl);
				// setup initial event processing
				EventTypeComponent eventTypeComponent = container.getComponentRepositoryImpl().getComponentByID(de.getEventTypeId());
				if (de.getService() != null) {
					ServiceComponent serviceComponent = container.getComponentRepositoryImpl().getComponentByID(de.getService());
					if (eventTypeComponent.getActiveServicesWhichDefineEventAsInitial().contains(serviceComponent)) {
						eventContextImpl.getActiveServicesToProcessEventAsInitial().add(serviceComponent);
					}
				}
				else {
					eventContextImpl.getActiveServicesToProcessEventAsInitial().addAll(eventTypeComponent.getActiveServicesWhichDefineEventAsInitial());
				}
			}
			else {
				if (eventContextImpl.isSuspendedNotTransacted()) {
					if (debugLogging)
						logger.debug("\n\n\nFreezing (due to suspended context) event delivery : [[[ eventId "
								+ de.getEventTypeId() + " on ac "+de.getActivityContextHandle()+"\n");
					eventContextImpl.barrierEvent(de);
					return;
				}
				else {
					if (debugLogging)
						logger.debug("\n\n\nResuming event delivery : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextHandle()+"\n");
					// needed to ensure tests/events/eventcontext/Test1108039Test.xml passes
					// the test must be fixed
					Thread.sleep(10);
				}
			}
			
			LinkedList<ServiceComponent> serviceComponents = eventContextImpl.getActiveServicesToProcessEventAsInitial();
			if (debugLogging)
				logger.debug("Active services which define "+de.getEventTypeId()+" as initial: "
					+ serviceComponents);
			
			boolean notFinished;
			String rootSbbEntityId;
			ClassLoader invokerClassLoader;
			SbbEntity sbbEntity;
			SbbObject sbbObject;
			ServiceComponent serviceComponent;
			boolean keepSbbEntityIfTxRollbacks;
			NextSbbEntityFinder.Result nextSbbEntityFinderResult;
			ActivityContext ac = null;
			Exception caught = null;
			Set<String> sbbEntitiesThatHandledCurrentEvent;
			boolean deliverEvent; 
			boolean rollbackTx;
			
			do {
				
				// For each SBB that is attached to this activity context and active service to process event as initial

				rootSbbEntityId = null;
				invokerClassLoader = null;
				sbbEntity = null;
				sbbObject = null;
				serviceComponent = null;
				keepSbbEntityIfTxRollbacks = false;
				nextSbbEntityFinderResult = null;
				notFinished = false;
				ac = null;
				caught = null;
				deliverEvent = true;
				rollbackTx = true;
				
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

					// If this fails then we propagate up since there's nothing to roll-back anyway

					txMgr.begin();			

					sbbEntitiesThatHandledCurrentEvent = eventContextImpl.getSbbEntitiesThatHandledEvent();

					try {

						// load ac
						ac = container.getActivityContextFactory().getActivityContext(de.getActivityContextHandle());

						if (routingPhase == RoutingPhase.DELIVERING) {
							
							// calculate highest priority attached sbb entity that needs to handle the event
							try {
								nextSbbEntityFinderResult = nextSbbEntityFinder.next(ac, de.getEventTypeId(), de.getService(), sbbEntitiesThatHandledCurrentEvent);								
							} catch (Exception e) {
								logger.warn("Failed to find next sbb entity to deliver the event "+de+" in "+ac.getActivityContextHandle(), e);
							}

							// calculate highest priority service to process event as initial
							if (!serviceComponents.isEmpty()) {
								serviceComponent = serviceComponents.getFirst();
							}

							// compare highest priority sbb entity already attached with highest priority service to process event as initial
							if (nextSbbEntityFinderResult == null) {
								if (serviceComponent != null) {
									if (debugLogging)
										logger.debug("No sbb entities attached, which didn't already route the event, but "+serviceComponent+" defines the event type as initial, starting initial event processing");
									// let the service process event as initial
									serviceComponents.removeFirst();
									sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, de, txMgr, ac);									
								}
								else {
									// nothing else to route
									if (debugLogging)
										logger.debug("No sbb entities attached, which didn't already route the event, and no services left to process the event as initial");									
								}
							} else {
								if (serviceComponent != null && serviceComponent.getDescriptor().getMService().getDefaultPriority() >= nextSbbEntityFinderResult.sbbEntity.getPriority()) {
									if (debugLogging)
										logger.debug("Found an sbb entity attached, which didn't already route the event, but "+serviceComponent+" defines the event type as initial and has the same or higher priority, starting initial event processing");
									// the service has higher or equal priority as the sbb entity, let the service process the eventas initial
									serviceComponents.removeFirst();
									sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, de, txMgr, ac);									
								}
								else {
									if (debugLogging)
										logger.debug("Found an sbb entity attached, which didn't already route the event, and either there are no more services, which defines the event type as initial, or their priorities is lower than the attached sbb entity found");
									sbbEntity = nextSbbEntityFinderResult.sbbEntity;
									// in this case it needs to find out if the sbb entity should receive the event or not
									// it may be an activity end event and the attached sbb entity does not receives such event
									deliverEvent = nextSbbEntityFinderResult.deliverEvent;
								}
							}
						}
						
						if (sbbEntity != null) {

							sbbEntitiesThatHandledCurrentEvent.add(sbbEntity.getSbbEntityId());
							notFinished = true;
							
							if (debugLogging) {
								logger
										.debug("Highest priority SBB entity, which is attached to the ac "+de.getActivityContextHandle()+" , to deliver the event: "
												+ sbbEntity.getSbbEntityId());
							}

							// CHANGE CLASS LOADER
							invokerClassLoader = sbbEntity.getSbbComponent().getClassLoader();
							Thread.currentThread().setContextClassLoader(invokerClassLoader);

							rootSbbEntityId = sbbEntity.getRootSbbId();

							EventRouterThreadLocals.setInvokingService(sbbEntity.getServiceId());

							// we deliver the event in case it is an initial
							// event processing or if the highest priority
							// attached sbb entity should do it (in activity
							// end event it may not if the service id was
							// set in the event firing, or if the sbb entity
							// does not declares the event in sbb descriptor

							if (deliverEvent) {

								sbbEntity.assignSbbObject();
								sbbObject = sbbEntity.getSbbObject();
								if (sbbEntity.isCreated() && !txMgr.getRollbackOnly()) {
									keepSbbEntityIfTxRollbacks = true;
								}

								sbbObject.sbbLoad();

								// GET AND CHECK EVENT MASK FOR THIS SBB ENTITY
								Set<?> eventMask = sbbEntity.getMaskedEventTypes(de.getActivityContextHandle());
								if (eventMask == null || !eventMask.contains(de.getEventTypeId())) {

									// TIME TO INVOKE THE EVENT HANDLER METHOD
									sbbObject.setSbbInvocationState(SbbInvocationState.INVOKING_EVENT_HANDLER);

									if (debugLogging) {
										logger
										.debug("---> Invoking event handler: ac="+de.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
									}

									sbbEntity.invokeEventHandler(de,ac,eventContextImpl);

									if (debugLogging) {
										logger
										.debug("<--- Invoked event handler: ac="+de.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
									}

									// check to see if the transaction is marked for
									// rollback if it is then we need to get out of
									// here soon as we can.
									if (txMgr.getRollbackOnly()) {
										throw new Exception("The transaction is marked for rollback");
									}

									sbbObject.setSbbInvocationState(SbbInvocationState.NOT_INVOKING);

								} else {
									if (debugLogging) {
										logger
										.debug("Not invoking event handler since event is masked");
									}
								}
							}
							
							// IF IT'S AN ACTIVITY END EVENT DETACH SBB ENTITY HERE
							if (de.getEventTypeId() == ActivityEndEventImpl.EVENT_TYPE_ID && eventContextImpl.getSbbEntitiesThatHandledEvent().contains(sbbEntity.getSbbEntityId())) {
								if (debugLogging) {
									logger
											.debug("The event is an activity end event, detaching ac="+de.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId());
								}
								ac.detachSbbEntity(sbbEntity.getSbbEntityId());
								sbbEntity.afterACDetach(de.getActivityContextHandle());
							}

							// CHECK IF WE CAN CLAIM THE ROOT SBB ENTITY
							if (rootSbbEntityId != null) {
								if (SbbEntityFactory.getSbbEntity(rootSbbEntityId).getAttachmentCount() != 0) {
									if (debugLogging) {
										logger
												.debug("Not removing sbb entity "+sbbEntity.getSbbEntityId()+" , the attachment count is not 0");
									}
									// the root sbb entity is not be claimed
									rootSbbEntityId = null;
								}
							} else {
								// it's a root sbb
								if (!sbbEntity.isRemoved() && sbbEntity.getAttachmentCount() == 0) {
									if (debugLogging) {
										logger
												.debug("Removing sbb entity "+sbbEntity.getSbbEntityId()+" , the attachment count is not 0");
									}
									// If it's the same entity then this is an
									// "Op and
									// Remove Invocation Sequence"
									// so we do the remove in the same
									// invocation
									// sequence as the Op
									SbbEntityFactory.removeSbbEntityWithCurrentClassLoader(sbbEntity,true);
								}
							}
						}
						
					} catch (Exception e) {
						logger.error("Failure while routing "+de, e);
						if (sbbEntity != null) {
							sbbObject = sbbEntity.getSbbObject();
						}
						caught = e;
					} 
										
					// do a final check to see if there is another SBB to
					// deliver.
					// We don't want to waste another loop. Note that
					// rollback
					// will not has any impact on this because the
					// ac.DeliveredSet
					// is not in the cache.
					if (notFinished) {
						if (serviceComponents.isEmpty()) {
							// no more services to process event as initial
							try {
								if (nextSbbEntityFinder.next(ac, de.getEventTypeId(),de.getService(),sbbEntitiesThatHandledCurrentEvent) == null) {
								//if (nextSbbEntityFinder.next(ac, de.getEventTypeId(),de.getService(),sbbEntitiesThatHandledCurrentEvent) == null && sbbEntitiesThatHandledCurrentEvent.contains(sbbEntity.getSbbEntityId())) {
									// no more attached sbb entities to route the event
									notFinished = false;
								}
							} catch (Throwable e) {
								if (debugLogging) {
									logger.debug("failed to get next attached sbb entity to handle event",e);
								}
							}
						}
					}
					else {
						if (nextSbbEntityFinderResult != null && serviceComponent != null) {
							// edge case when there is an attached sbb entity with less or same priority as
							// a service to process event as initial, but the service doesn't accept the event
							notFinished = true;
						}
					}
					
					boolean invokeSbbRolledBack = handleRollback.handleRollback(sbbObject, caught, invokerClassLoader,txMgr);

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

						try {
							rootSbbEntity = SbbEntityFactory.getSbbEntity(rootSbbEntityId);
							SbbEntityFactory.removeSbbEntity(rootSbbEntity,true);
						} catch (Exception e) {
							logger.error("Failure while routing event; third phase. Event Posting ["+ de + "]", e);
							caught = e;
						}

						// We have no target sbb object in a Remove Only SLEE
						// originated invocation
						// FIXME emmartins review
						invokeSbbRolledBackRemove = handleRollback.handleRollback(null, caught, rootSbbEntity.getSbbComponent().getClassLoader(),txMgr);
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
						handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, txMgr,false);
					} else if (sbbEntity != null && !txMgr.getRollbackOnly()
							&& sbbEntity.getSbbObject() != null) {

						sbbObject.sbbStore();
						sbbEntity.passivateAndReleaseSbbObject();

					}

					if (txMgr.getRollbackOnly()) {
						if (debugLogging) {
							logger
									.debug("Rolling back SLEE Originated Invocation Sequence");
						}
						txMgr.rollback();
					} else {
						
						if (!notFinished) {
							switch (routingPhase) {
							case DELIVERING:
								// last tx for this event delivering and it is going to commit (hopefully)
								// if it is an activity end or timer event lets try to take advantage
								// and do post processing in this tx, in the worst (and unexpected) scenario
								// the tx will rollback and we will do another spin, due to setting gotSbb as true
								// NOTE: in JVM only one instance exist for 
								if (de.getEventTypeId() == ActivityEndEventImpl.EVENT_TYPE_ID) {
									notFinished = true;
									routingPhase = RoutingPhase.END_ACTIVITY;
									container.getActivityContextFactory().removeActivityContext(ac);
								} else if (de.getEventTypeId() == TimerEventImpl.EVENT_TYPE_ID) {
									notFinished = true;
									routingPhase = RoutingPhase.END_TIMER;
									timerEventPostProcessor.process(de,container.getTimerFacility());
								}	
								break;
							case END_ACTIVITY:
								// we had bad luck and last tx rollbacked, repeat action
								notFinished = true;
								container.getActivityContextFactory().removeActivityContext(ac);
								break;
							case END_TIMER:
								// we had bad luck and last tx rollbacked, repeat action
								notFinished = true;
								timerEventPostProcessor.process(de,container.getTimerFacility());
								break;	
							default:
								logger.error("Unknown routing phase!!!");
								break;
							}
						}
						
						if (debugLogging) {
							logger
									.debug("Committing SLEE Originated Invocation Sequence");
						}
						txMgr.commit();
						
						// if we are not in delivering mode anymore and tx commits then we allow the loop to exit
						if (routingPhase != RoutingPhase.DELIVERING) {
							notFinished = false;
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
						if (debugLogging) {
							logger
									.debug("Invoking sbbRolledBack for Op Only or Op and Remove");

						}
						//FIXME: baranowb: de is passed for test: tests/sbb/abstractclass/SbbRolledBackNewTransaction.xml
						handleSbbRollback.handleSbbRolledBack(sbbEntity, null, de, new ActivityContextInterfaceImpl(ac), invokerClassLoader, false, txMgr, keepSbbEntityIfTxRollbacks);
					}
					if (invokeSbbRolledBackRemove) {
						// Now for the "Remove Only" if appropriate
						handleSbbRollback.handleSbbRolledBack(rootSbbEntity, null, null, null, rootInvokerClassLoader, true, txMgr,keepSbbEntityIfTxRollbacks);						
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

					rollbackTx = false;
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
						if (txMgr.getTransaction() != null) {
							if (rollbackTx) {
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
						if (debugLogging) {
							logger
									.debug("Finished delivering the event "
											+ de.getEventTypeId()
											+ " to the sbbEntity = "
											+ sbbEntity.getSbbEntityId()
											+ "]]] \n\n\n");
						}
					}
				}
				
				EventRouterThreadLocals.setInvokingService(null);
				
				if (eventContextImpl.isSuspendedNotTransacted()) {
					if (debugLogging)
						logger.debug("\n\n\nSuspended event delivery : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextHandle()+"\n");
					return;
				}
				
			} while (notFinished);

			/*
			 * End of SLEE Originated Invocation Sequence
			 * ==========================================
			 * 
			 */		

			de.eventProcessingSucceed();
			
			// we got to the end of the event routing, remove the event context
			de.getEventRouterActivity().setCurrentEventContext(null);
			
			// manage event references
			DeferredEventReferencesManagement eventReferencesManagement = container.getEventRouter().getDeferredEventReferencesManagement();
			eventReferencesManagement.eventUnreferencedByActivity(de.getEvent(), de.getActivityContextHandle());
			
		} catch (Exception e) {
			logger.error("Unhandled Exception in event router top try", e);
		}
		
		Thread.currentThread().setContextClassLoader(
				oldClassLoader);
	}

}
