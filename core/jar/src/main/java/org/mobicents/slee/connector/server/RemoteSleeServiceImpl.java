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
 * Created on Dec 6, 2004 RemoteSleeServiceImpl.java
 */
package org.mobicents.slee.connector.server;

import java.util.ArrayList;
import java.util.Iterator;

import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.management.SleeState;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.EventLookup;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContext;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;

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
	
	private NullActivityFactoryImpl naf;

	private ActivityContextFactoryImpl acf;

	private EventLookup eventLookup;

	private static final Logger log = Logger.getLogger(RemoteSleeServiceImpl.class);

	class DeferredFireEventAction implements
			Runnable {
		ArrayList eventList;

		public DeferredFireEventAction(ArrayList alist) {
			this.eventList = new ArrayList(alist);
		}

		public void run() {
			log.debug("afterCompletion called -- fireEventQueue Again!");
			Iterator iter = eventList.iterator();
			while (iter.hasNext()) {
				EventInvocation ei = (EventInvocation) iter.next();
				fireEvent(ei.event, ei.eventTypeId, ei.externalActivityHandle,
						ei.address);
			}
		}
	}

	public RemoteSleeServiceImpl(NullActivityFactoryImpl naf, EventLookup eventLookup,
			ActivityContextFactoryImpl acf) {
		if (log.isDebugEnabled())
			log.debug("Creating RemoteSleeServiceImpl");
		this.naf = naf;
		this.eventLookup = eventLookup;
		this.acf = acf;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.connector.server.RemoteSleeService#createActivityHandle()
	 */
	public ExternalActivityHandle createActivityHandle() {
		return createExternalActivityHandleImpl();
	}
	
	private ExternalActivityHandle createExternalActivityHandleImpl() {
		if (log.isDebugEnabled()) {
			log.debug("Creating external activity handle");
		}
		// creates a new instance of activity handle it with a safe unique id
		// for a it's null activity (and related activity context) if this
		// handle is used to fire events
		return naf.createNullActivityHandle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.connector.server.RemoteSleeService#fireEvent(java.lang.Object,
	 *      javax.slee.EventTypeID,
	 *      org.mobicents.slee.connector.server.ActivityHandleImpl,
	 *      javax.slee.Address)
	 */
	public void fireEvent(Object event, EventTypeID eventType,
			ExternalActivityHandle externalActivityHandle, Address address) {
		
		if (log.isDebugEnabled()) {
			log.debug("fireEvent(event="+event+",eventType="+eventType+",externalActivityHandle="+externalActivityHandle+",address="+address+")");
		}
		
		if (event == null) {
			throw new NullPointerException("event is null");
		}
		if (eventType == null) {
			throw new NullPointerException("event type is null");
		}
		
		// check container state is running
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		if (sleeContainer.getSleeState() != SleeState.RUNNING) {
			throw new IllegalStateException("Container is not running");
		}
		
		// get the null activity
		NullActivityImpl activity = null;
		
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		boolean newTx = txMgr.requireTransaction();
		boolean rollback = true;
		
		try {
			
			if (externalActivityHandle == null) {
				externalActivityHandle = createExternalActivityHandleImpl();
			}
			NullActivityHandle nah = (NullActivityHandle) externalActivityHandle;
			ActivityContextHandle ach = ActivityContextHandlerFactory.createNullActivityContextHandle(nah);
			ActivityContext ac = acf.getActivityContext(ach,false);
			if(ac == null) {
				// ac has ended or does not exists, (re)create it
				activity = naf.createNullActivityImpl(nah,true);
				ac = acf.getActivityContext(ach,false);
			}
			ac.fireEvent(new DeferredEvent(eventType,event,ac,address));
			rollback = false;
		} catch (Exception ex) {
			log.error("Exception in fireEvent!", ex);
		}
		finally {
			if(newTx) {
				if(rollback) {
					try {
						txMgr.rollback();
					} catch (SystemException e) {
						log.error("failed to rollback implicit tx",e);
					}
				}
				else {
					try {
						txMgr.commit();
					} catch (Exception e) {
						log.error("failed to commit implicit tx", e);
					}
				}
			}
			// else ignore, specs say there is no need to rollback a tx if event
			// queuing failed
		}
	}
	
	public void fireEventQueue(ArrayList queue) {
		if (queue == null)
			throw new NullPointerException("queue is null");
		try {
			if (log.isDebugEnabled()) {
				log.debug("fireEventQueue() called");
			}
			DeferredFireEventAction daf = new DeferredFireEventAction(queue);
			// The following needs to run in its own tx because 
			// it has to start transactions.
			new ThreadedExecutor().execute(daf);
			
		} catch (Exception ex) {
			throw new RuntimeException("Unexpected error", ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.connector.server.RemoteSleeService#getEventTypeID(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public EventTypeID getEventTypeID(String name, String vendor, String version) {
		if (name == null)
			throw new NullPointerException("name is null");
		if (vendor == null)
			throw new NullPointerException("vendor is null");
		if (version == null)
			throw new NullPointerException("version is null");

		if (log.isDebugEnabled())
			log.debug("getEventTypeID() called");
		int eventId = eventLookup.getEventID(name, vendor, version);
		if (log.isDebugEnabled())
			log.debug("eventId is:" + eventId);
		if (eventId == -1) {
			/*
			 * The SLEE spec. does not define what to return if the event type
			 * is not found but we're going to return null
			 */
			return null;
		}
		EventTypeID eventTypeID = eventLookup.getEventTypeID(eventId);
		if (log.isDebugEnabled())
			log.debug("Event type id is:" + eventTypeID);
		return eventTypeID;
	}
}