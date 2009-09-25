package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.slee.Address;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceFactory;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectPool;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class InitialEventProcessor {

	private static final Logger logger = Logger.getLogger(InitialEventProcessor.class);

	private final SleeContainer sleeContainer;

	private static final String NULL_STRING = "null";

	/**
	 * @param sleeContainer
	 */
	public InitialEventProcessor(SleeContainer sleeContainer) {
		this.sleeContainer = sleeContainer;
	}

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
			
			Object[] args = new Object[] { selector };

			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						sbbComponent.getClassLoader());
				final Method m = sbbComponent.getInitialEventSelectorMethods().get(selector.getSelectMethodName());
				selector = (InitialEventSelectorImpl) m.invoke(concreteSbb,
						args);
				if (selector == null) {
					return null;
				}
				if (!selector.isInitialEvent()) {
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
			
			if (selector.getAddress() == null) {
				buff.append(NULL_STRING);
			} else {
				ProfileSpecificationID addressProfileId = sbbComponent.getDescriptor().getAddressProfileSpecRef();
				ProfileSpecificationComponent profileSpecificationComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(addressProfileId);
			
				String addressProfileTable = serviceComponent.getDescriptor().getMService().getAddressProfileTable();
				// Cannot find an address profile table spec. 
				if (addressProfileTable == null) {
					throw new SLEEException("null address profile table in service !");
				}
				ProfileTableImpl profileTable = sleeContainer.getSleeProfileTableManager().getProfileTable(addressProfileTable);
				Collection<ProfileID> profileIDs = profileTable.getProfilesByAttribute(profileSpecificationComponent.isSlee11() ? "address" : "addresses", selector.getAddress(), profileSpecificationComponent.isSlee11());
				if (profileIDs.isEmpty())
					// no profiles located
					return null;
				else {
					buff.append(profileIDs.iterator().next());
				}				
			}

		} else
			buff.append(NULL_STRING);

		String customName = selector.getCustomName();

		buff.append(customName);

		return buff.toString();
	}

	public SbbEntity processInitialEvent(ServiceComponent serviceComponent,
			DeferredEvent deferredEvent, SleeTransactionManager txMgr,
			ActivityContext ac) {

		SbbEntity sbbEntity = null;
		
		if (logger.isDebugEnabled()) {
			logger.debug("Initial event processing for " + serviceComponent+" and "+deferredEvent);
		}

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
			}
			catch (Exception e) {
				logger.error("Failed to compute convergance name: "+e.getMessage(),e);
			}
			
			if (name != null) {

				if (!service.containsConvergenceName(name)) {

					if (logger.isDebugEnabled()) {
						logger.debug("Computed convergence name for "+serviceComponent+" and "+deferredEvent+" is "
								+ name + ", creating sbb entity and attaching to activity context.");
					}

					// Create a new root sbb entity
					sbbEntity = service.addChild(name);

					// attach sbb entity on AC
					if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
						// do the reverse on the sbb entity
						sbbEntity.afterACAttach(deferredEvent.getActivityContextHandle());
					}

				} else {

					if (logger.isDebugEnabled()) {
						logger.debug("Computed convergence name for "+service+" and "+deferredEvent+" is "
								+ name + ", sbb entity already exists, attaching to activity context (if not attached yet)");
					}
					// get sbb entity id for this convergence name
					sbbEntity = SbbEntityFactory.getSbbEntity(service.getRootSbbEntityId(name));
					// attach sbb entity on AC
					if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
						// do the reverse on the sbb entity
						sbbEntity.afterACAttach(deferredEvent.getActivityContextHandle());
					}
				}

			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("Computed convergence name for "+service+" and "+deferredEvent+" is null, either the root sbb is not interested in the event or an error occurred.");
				}
			}
		}
		
		return sbbEntity;

	}
	
	
}
