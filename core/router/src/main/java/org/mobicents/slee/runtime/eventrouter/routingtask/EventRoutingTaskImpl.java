/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.resource.FailureReason;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.eventrouter.SbbInvocationState;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class EventRoutingTaskImpl implements EventRoutingTask {

	private final static Logger logger = Logger.getLogger(EventRoutingTaskImpl.class);
	
	private final SleeContainer container;
	
	/**
	 * 
	 * @author martins
	 *
	 */
	private static enum RoutingPhase { DELIVERING , DELIVERED };
	
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
	 * processing logic to retrieve next sbb entity to handle an event on an activity
	 */
	private static final NextSbbEntityFinder nextSbbEntityFinder = new NextSbbEntityFinder();
	
	/**
	 * 
	 */
	private final EventContext eventContext;
	
	/**
	 * indicates which phase we are in routing of event
	 */
	private RoutingPhase routingPhase = RoutingPhase.DELIVERING;
	
	/**
	 * 
	 * @param eventContext
	 */
	public EventRoutingTaskImpl(EventContext eventContext, SleeContainer sleeContainer) {
		this.eventContext = eventContext;
		this.container = sleeContainer;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.container.eventrouter.EventRoutingTask#getEventContext()
	 */
	public EventContext getEventContext() {
		return eventContext;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {	
		if (System.getSecurityManager() != null) {
			AccessController.doPrivileged(
				new PrivilegedAction<Object>() {
					public Object run()	{
						routeQueuedEvent();
						return null;
					}
				}
			);
		}
		else {
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
		
		final SleeTransactionManager txMgr = container.getTransactionManager();
		
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();

		try {
			final LocalActivityContext lac = eventContext.getLocalActivityContext();
			final EventRoutingTask activityCurrentEventRoutingTask = lac.getCurrentEventRoutingTask();
			EventContext activityCurrentEventContext = activityCurrentEventRoutingTask == null ? null : activityCurrentEventRoutingTask.getEventContext();
			if (activityCurrentEventContext == null) {				
				if (debugLogging)
					logger.debug("\n\n\nStarting routing for "+eventContext);			
				
				activityCurrentEventContext = eventContext;
				// activity has no event routing task set 
				lac.setCurrentEventRoutingTask(this);
				
				EventTypeComponent eventTypeComponent = container.getComponentRepository().getComponentByID(eventContext.getEventTypeId());
				if (eventTypeComponent == null) {
					logger.error("Unable to route event, the related component is not installed");
					eventContext.eventProcessingFailed(FailureReason.OTHER_REASON);
					return;
				}
				
				// setup initial event processing
				if (eventContext.getService() != null) {
					ServiceComponent serviceComponent = container.getComponentRepository().getComponentByID(eventContext.getService());
					if (eventTypeComponent.getActiveServicesWhichDefineEventAsInitial().contains(serviceComponent)) {
						activityCurrentEventContext.getActiveServicesToProcessEventAsInitial().add(serviceComponent);
					}
				}
				else {
					Set<ServiceComponent> services = eventTypeComponent.getActiveServicesWhichDefineEventAsInitial();
					if (services != null) {
						activityCurrentEventContext.getActiveServicesToProcessEventAsInitial().addAll(services);
					}
				}
			}
			else {
				if (activityCurrentEventContext.isSuspendedNotTransacted()) {
					if (debugLogging)
						logger.debug("\n\n\nFreezing (due to suspended context) the routing for "+eventContext);						
					activityCurrentEventContext.barrierEvent(eventContext);
					return;
				}
				else {
					if (debugLogging)
						logger.debug("\n\n\nResuming the routing for"+eventContext);
					// needed to ensure tests/events/eventcontext/Test1108039Test.xml passes
					// the test must be fixed
					Thread.sleep(10);
				}
			}
			
			LinkedList<ServiceComponent> serviceComponents = activityCurrentEventContext.getActiveServicesToProcessEventAsInitial();
			if (debugLogging)
				logger.debug("Active services which define "+eventContext.getEventTypeId()+" as initial: "
						+ serviceComponents);
			
			boolean finished;
			SbbEntityID rootSbbEntityId;
			ClassLoader invokerClassLoader;
			SbbEntity sbbEntity;
			SbbObject sbbObject;
			ServiceComponent serviceComponent;
			boolean keepSbbEntityIfTxRollbacks;
			NextSbbEntityFinder.Result nextSbbEntityFinderResult;
			ActivityContext ac = null;
			Exception caught = null;
			Set<SbbEntityID> sbbEntitiesThatHandledCurrentEvent;
			boolean deliverEvent; 
			boolean rollbackTx;
			boolean rollbackOnlySet;
			
			boolean sbbHandledEvent = false;
			
			do {
				
				// For each SBB that is attached to this activity context and active service to process event as initial

				rootSbbEntityId = null;
				invokerClassLoader = null;
				sbbEntity = null;
				sbbObject = null;
				serviceComponent = null;
				keepSbbEntityIfTxRollbacks = false;
				nextSbbEntityFinderResult = null;
				finished = false;
				ac = null;
				caught = null;
				deliverEvent = true;
				rollbackTx = true;
				rollbackOnlySet = false;
				
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

					sbbEntitiesThatHandledCurrentEvent = activityCurrentEventContext.getSbbEntitiesThatHandledEvent();

					try {

						// load ac
						ac = container.getActivityContextFactory().getActivityContext(eventContext.getActivityContextHandle(),true);
						if (ac == null) {
							logger.error("Unable to route event "+eventContext+". The activity context is gone");
							try {
								eventContext.eventProcessingFailed(FailureReason.OTHER_REASON);
								txMgr.commit();
							}
							catch (Throwable e) {
								logger.error(e.getMessage(),e);
							}
							return;
						}
						
						if (routingPhase == RoutingPhase.DELIVERING) {
							
							// calculate highest priority attached sbb entity that needs to handle the event
							try {
								nextSbbEntityFinderResult = nextSbbEntityFinder.next(ac, eventContext, sbbEntitiesThatHandledCurrentEvent,container);								
							} catch (Exception e) {
								logger.warn("Failed to find next sbb entity to deliver the event "+eventContext+" in "+ac.getActivityContextHandle(), e);
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
									sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, eventContext, container, ac);	
									// if service returned no sbb entity and there are no more service components we are done
									if (sbbEntity == null && serviceComponents.isEmpty()) {										
										finished = true;
									}
								}
								else {
									// nothing else to route
									finished = true;
									if (debugLogging)
										logger.debug("No sbb entities attached, which didn't already route the event, and no services left to process the event as initial");		
									
								}
							} else {
								if (serviceComponent != null && serviceComponent.getDescriptor().getDefaultPriority() >= nextSbbEntityFinderResult.sbbEntity.getPriority()) {
									if (debugLogging)
										logger.debug("Found an sbb entity attached, which didn't already route the event, but "+serviceComponent+" defines the event type as initial and has the same or higher priority, starting initial event processing");
									// the service has higher or equal priority as the sbb entity, let the service process the eventas initial
									serviceComponents.removeFirst();
									sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, eventContext, container, ac);												
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
							
							if (debugLogging) {
								logger
										.debug("Highest priority SBB entity, which is attached to the ac "+eventContext.getActivityContextHandle()+" , to deliver the event: "
												+ sbbEntity.getSbbEntityId());
							}

							// CHANGE CLASS LOADER
							invokerClassLoader = sbbEntity.getSbbComponent().getClassLoader();
							Thread.currentThread().setContextClassLoader(invokerClassLoader);

							if (!sbbEntity.getSbbEntityId().isRootSbbEntity()) {
								rootSbbEntityId = sbbEntity.getSbbEntityId().getRootSBBEntityID();
							}

							SleeThreadLocals.setInvokingService(sbbEntity.getSbbEntityId().getServiceID());

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
								
								// GET AND CHECK EVENT MASK FOR THIS SBB ENTITY
								Set<EventTypeID> eventMask = sbbEntity.getMaskedEventTypes(eventContext.getActivityContextHandle());
								if (eventMask == null || !eventMask.contains(eventContext.getEventTypeId())) {

									// TIME TO INVOKE THE EVENT HANDLER METHOD
									sbbObject.setSbbInvocationState(SbbInvocationState.INVOKING_EVENT_HANDLER);
									ac.beforeDeliveringEvent(eventContext);
									
									if (debugLogging) {
										logger
										.debug("---> Invoking event handler: ac="+eventContext.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
									}

									sbbEntity.invokeEventHandler(eventContext,ac,activityCurrentEventContext);

									sbbHandledEvent = true;
									
									if (debugLogging) {
										logger
										.debug("<--- Invoked event handler: ac="+eventContext.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId()+" , sbbObject="+sbbObject);
									}

									// check to see if the transaction is marked for
									// rollback if it is then we need to get out of
									// here soon as we can.
									rollbackOnlySet = txMgr.getRollbackOnly();
									if (!rollbackOnlySet) {
										// TODO understand why the invoking state is not changed if rollback is set
										sbbObject.setSbbInvocationState(SbbInvocationState.NOT_INVOKING);
									}

								} else {
									if (debugLogging) {
										logger
										.debug("Not invoking event handler since event is masked");
									}
								}
							}
							
							if (!rollbackOnlySet) {
								// IF IT'S AN ACTIVITY END EVENT DETACH SBB ENTITY HERE
								if (eventContext.isActivityEndEvent() && activityCurrentEventContext.getSbbEntitiesThatHandledEvent().contains(sbbEntity.getSbbEntityId())) {
									if (debugLogging) {
										logger
										.debug("The event is an activity end event, detaching ac="+eventContext.getActivityContextHandle()+" , sbbEntity="+sbbEntity.getSbbEntityId());
									}
									ac.detachSbbEntity(sbbEntity.getSbbEntityId());
									sbbEntity.afterACDetach(eventContext.getActivityContextHandle());
								}

								// CHECK IF WE CAN CLAIM THE ROOT SBB ENTITY
								if (rootSbbEntityId != null) {
									SbbEntity rootSbbEntity = container.getSbbEntityFactory().getSbbEntity(rootSbbEntityId,false);
									if (rootSbbEntity == null || rootSbbEntity.getAttachmentCount() != 0) {
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
										container.getSbbEntityFactory().removeSbbEntity(sbbEntity,true);
									}
								}
							}
						}
						
					} catch (Exception e) {
						logger.error("Caught exception while routing "+eventContext, e);
						if (sbbEntity != null) {
							sbbObject = sbbEntity.getSbbObject();
						}
						caught = e;
					} catch (Throwable e) {
						// not an exception, wrap it in exception so sbb learns about it
						logger.error("Caught throwable while routing "+eventContext, e);
						if (sbbEntity != null) {
							sbbObject = sbbEntity.getSbbObject();
						}
						caught = new SLEEException("Caught throwable!",e);
					} 
										
					// do a final check to see if there is another SBB to
					// deliver.
					// We don't want to waste another loop. Note that
					// rollback
					// will not has any impact on this because the
					// ac.DeliveredSet
					// is not in the cache.
					if (!finished) {
						if (serviceComponents.isEmpty()) {
							// no more services to process event as initial
							try {
								if (nextSbbEntityFinder.next(ac, eventContext,sbbEntitiesThatHandledCurrentEvent,container) == null) {
								//if (nextSbbEntityFinder.next(ac, de.getEventTypeId(),de.getService(),sbbEntitiesThatHandledCurrentEvent) == null && sbbEntitiesThatHandledCurrentEvent.contains(sbbEntity.getSbbEntityID())) {
									// no more attached sbb entities to route the event
									finished = true;
								}
							} catch (Throwable e) {
								if (debugLogging) {
									logger.debug("failed to get next attached sbb entity to handle event",e);
								}
							}
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
							rootSbbEntity = container.getSbbEntityFactory().getSbbEntity(rootSbbEntityId,false);
							if (rootSbbEntity != null) {
								container.getSbbEntityFactory().removeSbbEntity(rootSbbEntity,false);
							}
						} catch (Exception e) {
							logger.error("Failure while routing event; third phase. Event Posting ["+ eventContext + "]", e);
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
						handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, container,false);
					} else if (sbbEntity != null && !txMgr.getRollbackOnly()
							&& sbbEntity.getSbbObject() != null) {

						sbbObject.sbbStore();
						sbbEntity.passivateAndReleaseSbbObject();

					}

					if (txMgr.getRollbackOnly()) {
						if (debugLogging) {
							logger
									.trace("Rolling back SLEE Originated Invocation Sequence");
						}
						txMgr.rollback();
					} else {
						
						if (finished) {
							switch (routingPhase) {
							case DELIVERING:
								// last tx for this event delivering and it is going to commit (hopefully)
								// if it has a unrefrenced callback which is tx aware try to take advantage of that 
								// and do post processing in this tx, in the worst (and unexpected) scenario
								// the tx will rollback and we will do another spin, due to setting gotSbb as true
								if (eventContext.unreferencedCallbackRequiresTransaction()) {
									finished = false;
									routingPhase = RoutingPhase.DELIVERED;
									eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
								}	
								break;
							case DELIVERED:
								if (eventContext.unreferencedCallbackRequiresTransaction()) {
									// we had bad luck and last tx rollbacked, repeat action
									finished = false;
									eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
								}
								break;								
							default:
								logger.error("Unknown routing phase!!!");
								break;
							}
						}
						
						if (debugLogging) {
							logger
									.trace("Committing SLEE Originated Invocation Sequence");
						}
						txMgr.commit();
						
						// if we are not in delivering mode anymore and tx commits then we allow the loop to exit
						if (routingPhase != RoutingPhase.DELIVERING) {
							finished = true;
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
									.trace("Invoking sbbRolledBack for Op Only or Op and Remove");

						}
						//FIXME: baranowb: de is passed for test: tests/sbb/abstractclass/SbbRolledBackNewTransaction.xml
						handleSbbRollback.handleSbbRolledBack(sbbEntity, null, eventContext, ac, invokerClassLoader, false, container, keepSbbEntityIfTxRollbacks);
					}
					if (invokeSbbRolledBackRemove) {
						// Now for the "Remove Only" if appropriate
						handleSbbRollback.handleSbbRolledBack(rootSbbEntity, null, null, null, rootInvokerClassLoader, true, container,keepSbbEntityIfTxRollbacks);						
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
						// FIXME this should not be possible, check if this is ever called by tck!!!
						final Transaction forgottenTx = txMgr.getTransaction();
						if (forgottenTx != null) {
							logger
							.error("HOUSTON WE HAVE A PROBLEM! Transaction "+forgottenTx+" left open in event routing.");
							if (rollbackTx) {
								txMgr.rollback();
							} else {
								txMgr.commit();
							}
						}
					} catch (SystemException se) {
						logger.error(se.getMessage(), se);
					}
					if (sbbEntity != null) {
						if (debugLogging) {
							logger
									.debug("Finished routing for "
											+ eventContext
											+ "\n\n\n");
						}
					}
				}
				
				SleeThreadLocals.setInvokingService(null);
				
				if (activityCurrentEventContext.isSuspendedNotTransacted()) {
					if (debugLogging)
						logger.debug("Suspended routing for "
							+ eventContext+"\n\n\n");
					return;
				}
				
			} while (!finished);

			/*
			 * End of SLEE Originated Invocation Sequence
			 * ==========================================
			 * 
			 */		

			eventContext.eventProcessingSucceed(sbbHandledEvent);
			
			// we got to the end of the event routing, remove from local ac
			lac.setCurrentEventRoutingTask(null);
			
			// manage event references
			if (!eventContext.unreferencedCallbackRequiresTransaction()) {
				eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
			}			
						
		} catch (Exception e) {
			logger.error("Unhandled Exception in event router top try", e);
		}
		
		Thread.currentThread().setContextClassLoader(
				oldClassLoader);
	}

}
