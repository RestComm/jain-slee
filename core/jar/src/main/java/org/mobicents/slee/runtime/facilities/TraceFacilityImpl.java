/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.management.NotCompliantMBeanException;
import javax.slee.ComponentID;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceFacility;
import javax.slee.facilities.TraceLevel;
import javax.slee.facilities.Tracer;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.ProfileTableNotification;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.SbbNotification;
import javax.slee.management.SubsystemNotification;
import javax.slee.management.TraceNotification;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 *Implementation of the trace facility.
 * 
 *@author M. Ranganathan
 *@author baranowb
 * 
 */
public class TraceFacilityImpl implements TraceFacility {

	private Hashtable<ComponentID, MTraceLevel> traceLevelTable;
	private TraceMBeanImpl traceMBeanImpl; // The MBean for this trace facility.
	private HashSet<String> notificationTypes;

	private static Logger log;

	static {
		log = Logger.getLogger(TraceFacilityImpl.class);
	}

	/*
	 * @return Returns the traceMBeanImpl.
	 */
	public TraceMBeanImpl getTraceMBeanImpl() {
		return traceMBeanImpl;
	}

	@Override
	public String toString() {
		return "Trace Facility Impl : " 
			+ "\n+-- Notification Types: "	+ notificationTypes
			+ "\n" + traceMBeanImpl;
	}
	
	class MTraceLevel {
		private Level level;
		private int seqno;

		public MTraceLevel(Level level) {
			this.level = level;
		}

		public int getSeqno() {
			return this.seqno++;
		}

		public Level getLevel() {
			return this.level;
		}
	}

	public TraceFacilityImpl(TraceMBeanImpl traceMB) throws NotCompliantMBeanException {
		this.notificationTypes = new HashSet<String>();
		
		this.traceLevelTable = new Hashtable<ComponentID, MTraceLevel>();
		this.traceMBeanImpl = traceMB;
		
		//FIXME: default:
		this.notificationTypes.add(ResourceAdaptorEntityNotification.TRACE_NOTIFICATION_TYPE);
		this.notificationTypes.add(SbbNotification.TRACE_NOTIFICATION_TYPE);
		this.notificationTypes.add(ProfileTableNotification.TRACE_NOTIFICATION_TYPE);
		//FIXME: ???
		this.notificationTypes.add(SubsystemNotification.TRACE_NOTIFICATION_TYPE);
	}

	public void setTraceLevelOnTransaction(final ComponentID componentId, Level newLevel) throws SystemException {
		MTraceLevel traceLevel = (MTraceLevel) this.traceLevelTable.get(componentId);
		final Level oldLevel = traceLevel == null ? null : traceLevel.level;
		if (traceLevel == null) {
			this.traceLevelTable.put(componentId, new MTraceLevel(newLevel));
		} else {
			traceLevel.level = newLevel;
		}
		// since we are in a tx and this information is not tx aware ad rollback
		// action
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				if (oldLevel == null) {
					traceLevelTable.remove(componentId);
				} else {
					((MTraceLevel) traceLevelTable.get(componentId)).level = oldLevel;
				}
			}
		};
		SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
	}

	public void setTraceLevel(ComponentID componentId, Level newLevel) {
		MTraceLevel traceLevel = (MTraceLevel) this.traceLevelTable.get(componentId);
		if (traceLevel == null) {
			this.traceLevelTable.put(componentId, new MTraceLevel(newLevel));
		} else {
			traceLevel.level = newLevel;
		}
	}

	public void unSetTraceLevel(final ComponentID componentId) throws SystemException {
		final MTraceLevel level = (MTraceLevel) traceLevelTable.remove(componentId);
		if (level != null) {
			// since we are in a tx and this information is not tx aware ad
			// rollback action
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					traceLevelTable.put(componentId, level);
				}
			};
			SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
		}
	}

	public void checkComponentID(ComponentID componentId) throws UnrecognizedComponentException {
		if (this.traceLevelTable.get(componentId) == null)
			throw new UnrecognizedComponentException(componentId.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.facilities.TraceFacility#getTraceLevel(javax.slee.ComponentID)
	 */
	public Level getTraceLevel(ComponentID componentId) throws NullPointerException, UnrecognizedComponentException, FacilityException {
		checkComponentID(componentId);
		return ((MTraceLevel) this.traceLevelTable.get(componentId)).getLevel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.facilities.TraceFacility#createTrace(javax.slee.ComponentID,
	 * javax.slee.facilities.Level, java.lang.String, java.lang.String, long)
	 */
	public void createTrace(ComponentID componentId, Level level, String messageType, String message, long timeStamp) throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {
		if (log.isDebugEnabled()) {
			log.debug("createTrace: " + componentId + " level = " + level + " messageType " + messageType + " message " + message + " timeStamp " + timeStamp);
		}
		checkComponentID(componentId);
		MTraceLevel tl = (MTraceLevel) this.traceLevelTable.get(componentId);
		this.notificationTypes.add(messageType);
		if (tl == null)
			throw new UnrecognizedComponentException("Could not find " + componentId);
		Level lev = tl.getLevel();
		int seqno = tl.getSeqno();
		if (lev.isOff())
			return;
		// Check if we should log this message.
		// if (level.isHigherLevel(lev)) {
		if (!lev.isHigherLevel(level)) {
			TraceNotification traceNotification = new TraceNotification(traceMBeanImpl, messageType, componentId, level, message, null, seqno, timeStamp);
			this.traceMBeanImpl.sendNotification(traceNotification);
		}

	}

	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.facilities.TraceFacility#createTrace(javax.slee.ComponentID,
	 * javax.slee.facilities.Level, java.lang.String, java.lang.String,
	 * java.lang.Throwable, long)
	 */
	public void createTrace(ComponentID componentId, Level level, String messageType, String message, Throwable cause, long timeStamp) throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {
		checkComponentID(componentId);

		MTraceLevel tl = (MTraceLevel) this.traceLevelTable.get(componentId);
		if (log.isDebugEnabled()) {
			log.debug(" createTrace: " + componentId + " level " + level + " messageType " + messageType + " message " + message + " cause " + cause + " time stamp " + timeStamp);
		}
		if (tl == null)
			throw new UnrecognizedComponentException("Could not find " + componentId);
		this.notificationTypes.add(messageType);
		Level lev = tl.getLevel();
		int seqno = tl.getSeqno();
		if (lev.isOff())
			return;
		// Check if we should log this message.
		if (level.isHigherLevel(lev) || level.toInt() == lev.toInt()) {
			TraceNotification traceNotification = new TraceNotification(traceMBeanImpl, messageType, componentId, level, message, cause, seqno, timeStamp);
			this.traceMBeanImpl.sendNotification(traceNotification);
		}

	}

	public String[] getNotificationTypes() {
		String ntypes[] = new String[this.notificationTypes.size()];
		ntypes = this.notificationTypes.toArray(ntypes);
		return ntypes;
	}

	

}
