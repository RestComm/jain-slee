/*
 * ***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Dec 6, 2004 RemoteSleeEndpoint.java
 */
package org.mobicents.slee.connector.local;

import javax.resource.ResourceException;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.management.SleeState;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * This interface duplicates methods from {@link javax.slee.connection.SleeConnection}.
 * @author baranowb
 * 
 */
public class SleeConnectionServiceImpl extends AbstractSleeContainerModule implements SleeConnectionService{

	private static final Logger logger = Logger.getLogger(SleeConnectionServiceImpl.class);
	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#createActivityHandle()
	 */
	public ExternalActivityHandle createActivityHandle() throws ResourceException {
		if(sleeContainer == null)
		{
			throw new ResourceException("Connection is in closed state");
		}
		if(sleeContainer.getSleeState()!=SleeState.RUNNING)
		{
			throw new ResourceException("Container is not in running state.");
		}
		
		return sleeContainer.getNullActivityFactory().createNullActivityHandle();
	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#fireEvent(java.lang.Object, javax.slee.EventTypeID, javax.slee.connection.ExternalActivityHandle, javax.slee.Address)
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address) throws NullPointerException,
			UnrecognizedActivityException, UnrecognizedEventException, ResourceException {
		if(sleeContainer == null)
		{
			throw new ResourceException("Connection is in closed state");
		}
		if(sleeContainer.getSleeState()!=SleeState.RUNNING)
		{
			throw new ResourceException("Container is not in running state.");
		}
		if (logger.isDebugEnabled()) {
			logger.debug("fireEvent(event=" + event + ",eventType=" + eventType
					+ ",activityHandle=" + activityHandle + ",address="
					+ address + ")");
		}

		if (event == null) {
			throw new NullPointerException("event is null");
		}
		if (eventType == null) {
			throw new NullPointerException("event type is null");
		}
		if (activityHandle == null) {
			throw new NullPointerException("activity handle is null");
		}

		EventTypeComponent eventTypeComponent = sleeContainer
				.getComponentRepository().getComponentByID(eventType);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException("event type not installed");
		}
			if (!eventTypeComponent.getEventTypeClass().isAssignableFrom(event.getClass())) {
			throw new UnrecognizedEventException(
					"the class of the event object fired is not assignable to the event class of the event type.\n EventClass: "+event.getClass()+", component class: "+eventTypeComponent.getEventTypeClass());
		}

		if (!(activityHandle instanceof NullActivityHandle)) {
			throw new UnrecognizedActivityException(activityHandle);
		}

		// check container state is running
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		if (sleeContainer.getSleeState() != SleeState.RUNNING) {
			throw new IllegalStateException("Container is not running");
		}

		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		boolean newTx = txMgr.requireTransaction();
		boolean rollback = true;

		try {
			final NullActivityHandle nullActivityHandle = (NullActivityHandle) activityHandle;
			final ActivityContextHandle ach = nullActivityHandle.getActivityContextHandle();
			ActivityContext ac = sleeContainer.getActivityContextFactory()
					.getActivityContext(ach);
			if (ac == null) {
				sleeContainer.getNullActivityFactory().createNullActivity(
						nullActivityHandle, false);
				ac = sleeContainer.getActivityContextFactory()
						.getActivityContext(ach);
				if (ac == null) {
					throw new SLEEException(
							"unable to create null ac for external activity handle "
									+ activityHandle);
				}
			}
			ac.fireEvent(eventType, event, address, null, null,null,null);
			rollback = false;
		} catch (Throwable ex) {
			logger.error("Exception in fireEvent!", ex);
		} finally {
			if (newTx) {
				if (rollback) {
					try {
						txMgr.rollback();
					} catch (Throwable e) {
						logger.error("failed to rollback implicit tx", e);
					}
				} else {
					try {
						txMgr.commit();
					} catch (Throwable e) {
						logger.error("failed to commit implicit tx", e);
					}
				}
			}
			// else ignore, specs say there is no need to rollback a tx if event
			// queuing failed
		}
	}

	/* (non-Javadoc)
	 * @see javax.slee.connection.SleeConnection#getEventTypeID(java.lang.String, java.lang.String, java.lang.String)
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version) throws UnrecognizedEventException, ResourceException {
		if(sleeContainer == null)
		{
			throw new ResourceException("Connection is in closed state");
		}
		if(sleeContainer.getSleeState()!=SleeState.RUNNING)
		{
			throw new ResourceException("Container is not in running state.");
		}
		EventTypeID eventTypeID = new EventTypeID(name, vendor, version);
		EventTypeComponent eventTypeComponent = sleeContainer
				.getComponentRepository().getComponentByID(eventTypeID);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException("event type not installed");
		} else {
			return eventTypeID;
		}
	}

}