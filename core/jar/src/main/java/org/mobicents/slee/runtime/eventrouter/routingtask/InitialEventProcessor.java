package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.slee.Address;
import javax.slee.InitialEventSelector;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceFactory;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactory;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectPool;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class InitialEventProcessor {

	private static final Logger logger = Logger.getLogger(InitialEventProcessor.class);
	
	private static final HandleRollback handleRollback = new HandleRollback();
	private static final HandleSbbRollback handleSbbRollback = new HandleSbbRollback();
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	/**
	 * Process the initial events of a service. This method possibly creates new
	 * Sbbs. The container keeps a factory that creates new Sbbs keyed on the
	 * convergence name of the service.
	 */
	public void processInitialEvents(ServiceComponent serviceComponent, DeferredEvent deferredEvent, SleeTransactionManager txMgr, ActivityContextFactory activityContextFactory)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Initial event processing for " + serviceComponent+" and "+deferredEvent);
		}
		
		Exception caught = null;
		SbbEntity sbbEntity = null;
		SbbObject sbbObject = null;
		ClassLoader invokerClassLoader = null;
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		
		EventRouterThreadLocals.setInvokingService(serviceComponent.getServiceID());
		
		try {
		
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
				final Service service = ServiceFactory.getService(serviceComponent);			
				if (service.getState().isActive()) {

					String name = null;
					try {
						name = computeConvergenceName(deferredEvent,serviceComponent);
					} catch (Throwable e) {
						logger.error("Failed to compute convergance name for "+serviceComponent+" and "+deferredEvent,e);
					}

					if (name != null) {

						if (!service.containsConvergenceName(name)) {

							if (logger.isDebugEnabled()) {
								logger.debug("Computed convergence name for "+serviceComponent+" and "+deferredEvent+" is "
										+ name + ", creating sbb entity and attaching to activity context.");
							}
							
							// Create a new root sbb entity
							sbbEntity = service.addChild(name);
							// change class loader
							invokerClassLoader = serviceComponent.getRootSbbComponent().getClassLoader();
							Thread.currentThread().setContextClassLoader(
									invokerClassLoader);
							// invoke sbb lifecycle methods on sbb entity creation
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
							ActivityContext ac = activityContextFactory.getActivityContext(deferredEvent.getActivityContextHandle());
							if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
								// do the reverse on the sbb entity
								sbbEntity.afterACAttach(deferredEvent.getActivityContextHandle());
							}
							// passivate sbb object
							try {
								sbbEntity.passivateAndReleaseSbbObject();
								sbbObject = sbbEntity.getSbbObject();
							} catch (Exception ex) {
								sbbObject = sbbEntity.getSbbObject();
								if (sbbObject != null) {
									try {
										sbbEntity.removeAndReleaseSbbObject();										
									} catch (Exception e) {
										logger.error(e.getMessage(),e);
									}
								}								
								throw ex;
							}
						} else {
							
							if (logger.isDebugEnabled()) {
								logger.debug("Computed convergence name for "+service+" and "+deferredEvent+" is "
										+ name + ", sbb entity already exists, attaching to activity context (if not attached yet)");
							}
							// get sbb entity id for this convergence name
							String rootSbbEntityId = service.getRootSbbEntityId(name);
							// attach sbb entity on AC
							if (activityContextFactory.getActivityContext(deferredEvent.getActivityContextHandle()).attachSbbEntity(rootSbbEntityId)) {
								// do the reverse on the sbb entity
								SbbEntityFactory.getSbbEntity(rootSbbEntityId)
								.afterACAttach(deferredEvent.getActivityContextHandle());
							}
						}

					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Computed convergence name for "+service+" and "+deferredEvent+" is null, either the root sbb is not interested in the event or an error occurred.");
						}
					}
				}
				
			} catch (Exception e) {
				logger.error("Caught an error! ", e);
				caught = e;
			}

			boolean invokeSbbRolledBack = handleRollback.handleRollback(sbbObject, caught, invokerClassLoader, txMgr);
			
			if (sbbEntity == null && invokeSbbRolledBack) {
				// If there is no entity associated then the invokeSbbRolledBack is
				// handle in
				// the same tx, otherwise in a new tx (6.10.1)
				handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, txMgr);
				/* original code was, confirm in specs that at this time we should not send event object and aci
				 * 
					handleSbbRolledBack(sbbEntity, sbbObject, deferredEvent,
							invokerClassLoader, false);
				 */
			}
						
			// commit or rollback the tx. if the setRollbackOnly flag is set then this will trigger rollback action.			
			try {
				txMgr.commit();
			} catch (Exception e) {
				logger.error("failed to commit transaction, invoking sbbRolledBack",e);
				invokeSbbRolledBack = true;
			}
			
			// We may need to run sbbRolledBack for invocation sequence 1 in another tx
			if (sbbEntity != null && invokeSbbRolledBack) {
				handleSbbRollback.handleSbbRolledBack(sbbEntity, sbbObject, null, null, invokerClassLoader, false, txMgr);
				/* original code was, confirm in specs that at this time we should not send event object and aci
				 * 
				handleSbbRolledBack(sbbEntity, sbbObject, deferredEvent,
						invokerClassLoader, false);
						*/
			}
						
		} catch (Exception e) {
			logger.error("Failed to process initial event for "
					+ serviceComponent + " and "+deferredEvent, e);
		}
		
		EventRouterThreadLocals.setInvokingService(null);
		
		if (invokerClassLoader != null) {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	private static final String NULL_STRING = "null";
	
	/**
	 * Compute a convergence name for the Sbb for the given Slee event.
	 * Convergence names are used to instantiate the Sbb. I really ought to move
	 * this to SleeContainer.java
	 * 
	 * @param sleeEvent -
	 *            slee event for the convergence name computation
	 * @return the convergence name or null if this is not an initial event for
	 *         this service
	 */
	private String computeConvergenceName(DeferredEvent sleeEvent,
			ServiceComponent serviceComponent) throws Exception {
		
		final SbbComponent sbbComponent = serviceComponent.getRootSbbComponent();
		MEventEntry mEventEntry = sbbComponent.getDescriptor().getEventEntries().get(sleeEvent.getEventTypeId());
		InitialEventSelectorImpl selector = new InitialEventSelectorImpl(
				sleeEvent.getEventTypeId(), sleeEvent.getEvent(), sleeEvent.getActivityContextHandle(), mEventEntry.getInitialEventSelects(), mEventEntry.getInitialEventSelectorMethod(), sleeEvent
						.getAddress());

		/*
		 * An initial-event-selector-method-name element. This element is
		 * optional and is meaningful only if initial-event is true. It
		 * identifies an in itial event selector method. The SLEE invokes this
		 * optional method to d etermine if an event of the specified event type
		 * is an initial event if the SBB is a root SBB of a Service (see
		 * Section 8.5.4). Note that this method is not static. You can either
		 * used a pooled instance of the object or create a new instance of the
		 * object to run the specified method.
		 */
		if (selector.isSelectMethod()) {
			// According to Section 8.5.4, page 115, some fields should be set
			// before calling the selector method
			// selector.setEvent(sleeEvent.getEventObject());
			// selector.setEventName(); //TODO: not sure what value to put here
			// selector.setActivity(sleeEvent.getActivityContext().getActivity());//TODO:
			// see how to get the activity now
			selector.setAddress(sleeEvent.getAddress());
			selector.setCustomName(null);
			selector.setInitialEvent(true);

			SbbObjectPool pool = sleeContainer.getSbbPoolManagement().getObjectPool(serviceComponent.getServiceID(),
							sbbComponent.getSbbID());
			SbbObject sbbObject = (SbbObject) pool.borrowObject();
			SbbConcrete concreteSbb = (SbbConcrete) sbbObject.getSbbConcrete();

			Class[] argtypes = new Class[] { InitialEventSelector.class };
			Method m = sbbComponent.getConcreteSbbClass().getMethod(
					selector.getSelectMethodName(), argtypes);
			Object[] args = new Object[] { selector };

			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						sbbComponent.getClassLoader());
				selector = (InitialEventSelectorImpl) m.invoke(concreteSbb,
						args);
				if (selector == null) {
					logger
							.debug("Sbb returned null. So its not interested in this event");
					return null;
				}
				if (!selector.isInitialEvent()) {
					logger
							.debug("Sbb has determined it will not attend to this event");
					return null;
				}

			} finally {
				Thread.currentThread().setContextClassLoader(oldCl);
				pool.returnObject(sbbObject);
			}
		}

		StringBuilder buff = new StringBuilder();

		if (selector.isActivityContextSelected()) {
			buff.append(sleeEvent.getActivityContextHandle());
		} else
			buff.append(NULL_STRING);

		// TODO the ProfileTle select varile for now is null

		buff.append(NULL_STRING);

		if (selector.isAddressSelected()) {
			Address address = selector.getAddress();

			if (address == null)
				buff.append(NULL_STRING);
			else
				buff.append(address.toString());
		} else
			buff.append(NULL_STRING);

		// If event type is selected append it to te convergence name.
		if (selector.isEventTypeSelected()) {
			buff.append(selector.getEventTypeID());
		} else
			buff.append(NULL_STRING);

		/*
		 * Event. The value of this variable (if selected) is unique for each
		 * event fired, e.g. each invocation of an SBB fire event method or each
		 * firing of an event by a resource adaptor (using SLEE vendor specific
		 * fire event methods). It is unique regardless of whether the same pair
		 * of event object and Activity Context object (in the case of SBB fired
		 * event, or Activity object in case of resource adaptor fired event)
		 * are passed to the fire event method. There are two unique events
		 * fired in the following scenarios:
		 * 
		 * o An SBB invoking the same fire event method twice. From the SLEEs
		 * perspective, the two fire method invocations fire two unique events
		 * even if the Activity Context object and event object passed to the
		 * fire event method are the same.
		 * 
		 * o An SBB firing an event in its event handler method. The event fired
		 * through the fire event method is a different event even if the same
		 * Activity Context object and event object passed to the event handler
		 * method is passed to the fire event method.
		 * 
		 * o A resource adaptor entity invoking one or more SLEE provided
		 * methods for firing events multiple times. From the SLEE???s
		 * perspective, these invocations fire multiple unique events even if
		 * the Activity object and event object passed are the same.
		 */

		if (selector.isEventSelected()) {
			buff.append(sleeEvent.hashCode()); // TODO: use a more unique value
			// than the hash code
		} else
			buff.append(NULL_STRING);
		/*
		 * The address attribute of the InitialEventSelector object provides the
		 * default address. The value of this attribute may be null if there is
		 * no default address. The value of this attribute determines the value
		 * of the address variable in the convergence name if the address
		 * variable is selected and is also used to look up Address Profiles in
		 * the Address Profile Table of the Service if the Address Profile
		 * variable is selected.
		 * 
		 * o If the address attribute is null when the initial event selector
		 * method returns, then the address convergence name variable is not
		 * selected, i.e. same as setting AddressSelected attribute to false.
		 * 
		 * o If the AddressProfile variable is set to true and the address is
		 * not null but does not locate any Address Profile in the Address
		 * Profile Table of the Service, then no convergence name is created,
		 * i.e. same as setting PossibleInitialEvent to false.
		 */
		if (selector.isAddressProfileSelected()) {
			ProfileSpecificationID addressProfileId = sbbComponent.getDescriptor().getAddressProfileSpecRef();

			if (selector.getAddress() == null) {
				buff.append(NULL_STRING);
			} else {
				ProfileSpecificationComponent profileSpecificationComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(addressProfileId);
				if (profileSpecificationComponent == null) {
					// FIXME this can be checked in deploy
					throw new Exception("Could not find address profile ! "
							+ addressProfileId);
				}
				SleeProfileTableManager sleeProfileManager = sleeContainer.getSleeProfileTableManager();
				String addressProfileTable = serviceComponent.getDescriptor().getMService().getAddressProfileTable();
				// Cannot find an address profile table spec. ( is this the same
				// as
				// the second condtion above?
				if (logger.isDebugEnabled()) {
					logger
							.debug("addressProfileTable = "
									+ addressProfileTable);
				}
				if (addressProfileTable == null) {
					// FIXME this can be checked in deploy
					throw new Exception(
							"null address profile table in service !");
				}
				ProfileTableImpl profileTable = sleeProfileManager.getProfileTable(addressProfileTable);
				Collection<ProfileID> profileIDs = profileTable.getProfilesByAttribute(profileSpecificationComponent.isSlee11() ? "address" : "addresses", selector.getAddress(), profileSpecificationComponent.isSlee11());
				if (profileIDs.isEmpty())
					throw new Exception("Could not find the specified profile");
				else {
					buff.append(profileIDs.iterator().next().toString());
				}				
			}

		} else
			buff.append(NULL_STRING);

		String customName = selector.getCustomName();

		buff.append(customName);

		if (logger.isDebugEnabled()) {
			logger.debug("computed convergence name = " + buff.toString());
			logger.debug("selector = " + selector);
		}
		return buff.toString();
	}
	
}
