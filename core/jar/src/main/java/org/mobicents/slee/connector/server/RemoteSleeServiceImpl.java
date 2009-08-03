package org.mobicents.slee.connector.server;

import java.rmi.RemoteException;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.management.SleeState;
import javax.slee.resource.EventFlags;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * Implementation of the RemoteSleeService.
 * 
 * An instance of this class receives invocations via HA-RMI from the outside
 * world.
 * 
 * @author Tim
 * @author eduardomartins
 */
public class RemoteSleeServiceImpl implements RemoteSleeService {

	private SleeContainer sleeContainer;

	private static final Logger log = Logger
			.getLogger(RemoteSleeServiceImpl.class);

	public RemoteSleeServiceImpl() {
		if (log.isDebugEnabled())
			log.debug("Creating RemoteSleeServiceImpl");
		this.sleeContainer = SleeContainer.lookupFromJndi();
	}

	/**
	 * @see RemoteSleeService#createActivityHandle()
	 */
	public ExternalActivityHandle createActivityHandle() {
		if (log.isDebugEnabled()) {
			log.debug("Creating external activity handle");
		}
		// creates a new instance of activity handle it with a safe unique id
		// for a it's null activity (and related activity context) if this
		// handle is used to fire events
		return sleeContainer.getNullActivityFactory()
				.createNullActivityHandle();
	}

	/**
	 * @see RemoteSleeService#fireEvent(Object, EventTypeID,
	 *      ExternalActivityHandle, Address)
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle activityHandle, Address address)
			throws NullPointerException, UnrecognizedEventException,
			RemoteException {

		if (log.isDebugEnabled()) {
			log.debug("fireEvent(event=" + event + ",eventType=" + eventType
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
				.getComponentRepositoryImpl().getComponentByID(eventType);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException("event type not installed");
		}
		if (!event.getClass().isAssignableFrom(
				eventTypeComponent.getEventTypeClass())) {
			throw new UnrecognizedEventException(
					"the class of the event object fired is not assignable to the event class of the event type");
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
			NullActivityHandle nullActivityHandle = (NullActivityHandle) activityHandle;
			ActivityContextHandle ach = ActivityContextHandlerFactory
					.createNullActivityContextHandle(nullActivityHandle);
			ActivityContext ac = sleeContainer.getActivityContextFactory()
					.getActivityContext(ach);
			if (ac == null) {
				sleeContainer.getNullActivityFactory().createNullActivityImpl(
						nullActivityHandle, false);
				ac = sleeContainer.getActivityContextFactory()
						.getActivityContext(ach);
				if (ac == null) {
					throw new SLEEException(
							"unable to create null ac for external activity handle "
									+ activityHandle);
				}
			}
			ac.fireEvent(eventType, event, address, null, EventFlags.NO_FLAGS);
			rollback = false;
		} catch (Throwable ex) {
			log.error("Exception in fireEvent!", ex);
		} finally {
			if (newTx) {
				if (rollback) {
					try {
						txMgr.rollback();
					} catch (Throwable e) {
						log.error("failed to rollback implicit tx", e);
					}
				} else {
					try {
						txMgr.commit();
					} catch (Throwable e) {
						log.error("failed to commit implicit tx", e);
					}
				}
			}
			// else ignore, specs say there is no need to rollback a tx if event
			// queuing failed
		}
	}

	/**
	 * @see RemoteSleeService#getEventTypeID(String, String, String)
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version)
			throws UnrecognizedEventException {

		EventTypeID eventTypeID = new EventTypeID(name, vendor, version);
		EventTypeComponent eventTypeComponent = sleeContainer
				.getComponentRepositoryImpl().getComponentByID(eventTypeID);
		if (eventTypeComponent == null) {
			throw new UnrecognizedEventException("event type not installed");
		} else {
			return eventTypeID;
		}
	}
}