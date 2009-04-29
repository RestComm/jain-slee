package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
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
	
	/**
	 * processing logic after an activity end event has been routed 
	 */
	private static final ActivityEndEventPostProcessor activityEndEventPostProcessor = new ActivityEndEventPostProcessor();
	
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
	private static final InitialEventProcessor initialEventProcessor = new InitialEventProcessor();
	
	/**
	 * processing logic to find the next sbb entity to route an event
	 */
	private static final NextSbbEntityFinder nextSbbEntityFinder = new NextSbbEntityFinder();
	
	/**
	 * processing logic after a timer event has been routed
	 */
	private static final TimerEventPostProcessor timerEventPostProcessor = new TimerEventPostProcessor();
	
	private final SleeContainer container;
	private final DeferredEvent de;
	
	public EventRoutingTask(SleeContainer container, DeferredEvent de) {
		super(); 
		this.de = de;
		this.container = container;
	}
	
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
		
		final SleeTransactionManager txMgr = this.container.getTransactionManager();
				
		boolean rollbackTx = true;
		
		try {

			EventContextImpl eventContextImpl = de.getEventRouterActivity().getCurrentEventContext();
			if (eventContextImpl == null) {				
				if (logger.isDebugEnabled())
					logger.debug("\n\n\nDelivering event : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextId()+"\n");
				
				// event context not set, create it 
				eventContextImpl = new EventContextImpl(de,container);
				de.getEventRouterActivity().setCurrentEventContext(eventContextImpl);
				// do initial event processing
				EventTypeComponent eventTypeComponent = container.getComponentRepositoryImpl().getComponentByID(de.getEventTypeId());
				if (logger.isDebugEnabled()) {
					logger.debug("Active services for event "+de.getEventTypeId()+": "+eventTypeComponent.getActiveServicesWhichDefineEventAsInitial());
				}
				for (final ServiceComponent serviceComponent : eventTypeComponent.getActiveServicesWhichDefineEventAsInitial()) {
					if (de.getService() == null || de.getService().equals(serviceComponent.getServiceID())) {
						initialEventProcessor.processInitialEvents(serviceComponent, de, txMgr, this.container.getActivityContextFactory());
					}
				}
			}
			else {
				if (eventContextImpl.isSuspendedNotTransacted()) {
					if (logger.isDebugEnabled())
						logger.debug("\n\n\nFreezing (due to suspended context) event delivery : [[[ eventId "
								+ de.getEventTypeId() + " on ac "+de.getActivityContextId()+"\n");
					eventContextImpl.barrierEvent(de);
					return;
				}
				else {
					if (logger.isDebugEnabled())
						logger.debug("\n\n\nResuming event delivery : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextId()+"\n");
				}
			}
			
			// For each SBB that is attached to this activity context.
			boolean gotSbb = false;
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

					// If this fails then we propagate up since there's nothing to roll-back anyway
					
					txMgr.begin();
					
					ActivityContext ac = null;
					Exception caught = null;
					SbbEntity highestPrioritySbbEntity = null;
					ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
					
					Set<String> sbbEntitiesThatHandledCurrentEvent = eventContextImpl.getSbbEntitiesThatHandledEvent();
					
					try {
					
						ac = container.getActivityContextFactory().getActivityContext(de.getActivityContextId(),true);

						try {
							highestPrioritySbbEntity = nextSbbEntityFinder.next(ac, de.getEventTypeId(), de.getService(), sbbEntitiesThatHandledCurrentEvent);
						} catch (Exception e) {
							logger.warn("Failed to find next sbb entity to deliver the event "+de+" in "+ac.getActivityContextId(), e);
							highestPrioritySbbEntity = null;
						}

						if (highestPrioritySbbEntity == null) {
							gotSbb = false;
							sbbEntitiesThatHandledCurrentEvent.clear();						
						} else {
							gotSbb = true;
							sbbEntitiesThatHandledCurrentEvent.add(highestPrioritySbbEntity.getSbbEntityId());	
						}

						if (gotSbb) {

							if (logger.isDebugEnabled()) {
								logger
										.debug("Highest priority SBB entity, which is attached to the ac "+de.getActivityContextId()+" , to deliver the event: "
												+ highestPrioritySbbEntity.getSbbEntityId());
							}

							// CHANGE CLASS LOADER
							invokerClassLoader = highestPrioritySbbEntity.getSbbComponent().getClassLoader();
							Thread.currentThread().setContextClassLoader(invokerClassLoader);

							sbbEntity = highestPrioritySbbEntity;
							rootSbbEntityId = sbbEntity.getRootSbbId();

							EventRouterThreadLocals.setInvokingService(sbbEntity.getServiceId());
							
							// Assign an sbb from the pool
							sbbEntity.assignAndActivateSbbObject();
							sbbObject = sbbEntity.getSbbObject();
							sbbObject.sbbLoad();

							// GET AND CHECK EVENT MASK FOR THIS SBB ENTITY
							Set eventMask = sbbEntity.getMaskedEventTypes(de.getActivityContextId());
							if (!eventMask.contains(de.getEventTypeId())) {

								// TIME TO INVOKE THE EVENT HANDLER METHOD
								sbbObject.setSbbInvocationState(SbbInvocationState.INVOKING_EVENT_HANDLER);
								
								if (logger.isDebugEnabled()) {
									logger
											.debug("---> Invoking event handler: ac="+de.getActivityContextId()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
								}
								
								sbbEntity.invokeEventHandler(de,ac,eventContextImpl);

								if (logger.isDebugEnabled()) {
									logger
											.debug("<--- Invoked event handler: ac="+de.getActivityContextId()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
								}
								
								// check to see if the transaction is marked for
								// rollback if it is then we need to get out of
								// here soon as we can.
								if (txMgr.getRollbackOnly()) {
									throw new Exception("The transaction is marked for rollback");
								}

								sbbObject.setSbbInvocationState(SbbInvocationState.NOT_INVOKING);
								
							} else {
								if (logger.isDebugEnabled()) {
									logger
											.debug("Not invoking event handler since event is masked");
								}
							}

							// IF IT'S AN ACTIVITY END EVENT DETACH SBB ENTITY HERE
							if (de.getEventTypeId().equals(ActivityEndEventImpl.EVENT_TYPE_ID)) {
								if (logger.isDebugEnabled()) {
									logger
											.debug("The event is an activity end event, detaching ac="+de.getActivityContextId()+" , sbbEntity="+sbbEntity.getSbbEntityId());
								}
								highestPrioritySbbEntity.afterACDetach(de.getActivityContextId());
							}

							// CHECK IF WE CAN CLAIM THE ROOT SBB ENTITY
							if (rootSbbEntityId != null) {
								if (SbbEntityFactory.getSbbEntity(rootSbbEntityId).getAttachmentCount() != 0) {
									if (logger.isDebugEnabled()) {
										logger
												.debug("Not removing sbb entity "+sbbEntity.getSbbEntityId()+" , the attachment count is not 0");
									}
									// the root sbb entity is not be claimed
									rootSbbEntityId = null;
								}
							} else {
								// it's a root sbb
								if (!sbbEntity.isRemoved()	&& sbbEntity.getAttachmentCount() == 0) {
									if (logger.isDebugEnabled()) {
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
						logger.error("Failure while routing event; second phase. DeferredEvent ["+ de.getEventTypeId() + "]", e);
						if (highestPrioritySbbEntity != null) {
							sbbObject = highestPrioritySbbEntity.getSbbObject();
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
					if (gotSbb) {
						try {
							if (nextSbbEntityFinder.next(ac, de.getEventTypeId(),de.getService(),sbbEntitiesThatHandledCurrentEvent) == null) {
								gotSbb = false;
							}
						} catch (Throwable e) {
							gotSbb = false;
						} 
					}					
					
					Thread.currentThread().setContextClassLoader(
							oldClassLoader);

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

						oldClassLoader = Thread.currentThread().getContextClassLoader();

						try {
							rootSbbEntity = SbbEntityFactory.getSbbEntity(rootSbbEntityId);

							rootInvokerClassLoader = rootSbbEntity.getSbbComponent().getClassLoader();
							Thread.currentThread().setContextClassLoader(rootInvokerClassLoader);

							SbbEntityFactory.removeSbbEntity(rootSbbEntity,true);

						} catch (Exception e) {
							logger.error("Failure while routing event; third phase. Event Posting ["+ de + "]", e);
							caught = e;
						} finally {

							Thread.currentThread().setContextClassLoader(oldClassLoader);
						}

						// We have no target sbb object in a Remove Only SLEE
						// originated invocation
						// FIXME emmartins review
						invokeSbbRolledBackRemove = handleRollback.handleRollback(null, caught, rootInvokerClassLoader,txMgr);
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
						handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, txMgr);
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
						//FIXME: baranowb: de is passed for test: tests/sbb/abstractclass/SbbRolledBackNewTransaction.xml
						handleSbbRollback.handleSbbRolledBack(sbbEntity, null, de, new ActivityContextInterfaceImpl(ac), invokerClassLoader, false, txMgr);
					}
					if (invokeSbbRolledBackRemove) {
						// Now for the "Remove Only" if appropriate
						handleSbbRollback.handleSbbRolledBack(rootSbbEntity, null, null, null, rootInvokerClassLoader, true, txMgr);						
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
				
				EventRouterThreadLocals.setInvokingService(null);
				
				if (eventContextImpl.isSuspendedNotTransacted()) {
					if (logger.isDebugEnabled())
						logger.debug("\n\n\nSuspended event delivery : [[[ eventId "
							+ de.getEventTypeId() + " on ac "+de.getActivityContextId()+"\n");
					return;
				}
				
				// need to ensure gotSbb = false if and only if iter.hasNext()
				// == false
			} while (gotSbb);

			de.eventProcessingSucceed();
			
			/*
			 * End of SLEE Originated Invocation Sequence
			 * ==========================================
			 * 
			 */

			if (de.getEventTypeId().equals(ActivityEndEventImpl.EVENT_TYPE_ID)) {
				activityEndEventPostProcessor.process(de.getActivityContextId(), txMgr, this.container.getActivityContextFactory());
			} else if (de.getEventTypeId().equals(TimerEventImpl.EVENT_TYPE_ID)) {
				timerEventPostProcessor.process(de,this.container.getTimerFacility());
			}			

			// we got to the end of the event routing, remove the event context
			de.getEventRouterActivity().setCurrentEventContext(null);
			// manage event references
			DeferredEventReferencesManagement eventReferencesManagement = container.getEventRouter().getDeferredEventReferencesManagement();
			eventReferencesManagement.eventUnreferencedByActivity(de.getEvent(), de.getActivityContextId());
			
		} catch (Exception e) {
			logger.error("Unhandled Exception in event router top try", e);
		}
	}

}
