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

	/**
	 * THis is internaly called, by 1.1 tracers
	 * 
	 * @param src
	 */
	void createTrace(NotificationSource src, javax.slee.facilities.TraceLevel lvl, String traceType, String tracerName, String msg, Throwable cause, int seq, long timeStamp) {

		TraceNotification traceNotification = new TraceNotification(traceType, this.traceMBeanImpl, src, tracerName, lvl, msg, cause, seq, timeStamp);
		this.traceMBeanImpl.sendNotification(traceNotification);
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
		if (level.isHigherLevel(lev)) {
			TraceNotification traceNotification = new TraceNotification(traceMBeanImpl, messageType, componentId, level, message, cause, seqno, timeStamp);
			this.traceMBeanImpl.sendNotification(traceNotification);
		}

	}

	public String[] getNotificationTypes() {
		String ntypes[] = new String[this.notificationTypes.size()];
		ntypes = this.notificationTypes.toArray(ntypes);
		return ntypes;
	}

	// 1.1 TracerImpl are. We define internal private class to hide details :)

	private Map<NotificationSource, TracerStorage> tracerStorage = new HashMap<NotificationSource, TracerStorage>();

	/**
	 * This checks if tracer name is ok. It must not be null;
	 * 
	 * @param split
	 * @throws IllegalArgumentException
	 */
	public static void checkTracerName(String tracerName, NotificationSource notificationSource) throws IllegalArgumentException {

		if (tracerName.compareTo("") == 0) {
			// This is root
			return;
		}

		// String[] splitName = tracerName.split("\\.");
		StringTokenizer stringTokenizer = new StringTokenizer(tracerName, ".", true);

		int fqdnPartIndex = 0;

		// if(splitName.length==0)
		// {
		// throw new IllegalArgumentException("Passed tracer:" + tracerName +
		// ", name for source: " + notificationSource + ", is illegal");
		// }

		String lastToken = null;

		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (lastToken == null) {
				// this is start
				lastToken = token;
			}

			if (lastToken.compareTo(token) == 0 && token.compareTo(".") == 0) {
				throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
			}

			if (token.compareTo(".") != 0) {
				for (int charIndex = 0; charIndex < token.length(); charIndex++) {
					Character c = token.charAt(charIndex);
					if (c.isLetter(c) || c.isDigit(c)) {
						// Its ok?
					} else {
						throw new IllegalArgumentException("Passed tracer:" + tracerName + " Token[" + token + "], name for source: " + notificationSource
								+ ", is illegal, contains illegal character: " + charIndex);
					}

				}

				fqdnPartIndex++;
			}
			lastToken = token;

		}

		if (lastToken.compareTo(".") == 0) {
			throw new IllegalArgumentException("Passed tracer:" + tracerName + ", name for source: " + notificationSource + ", is illegal");
		}

		// for (String part : splitName) {
		// if (part == null || part.compareTo("") == 0) {
		// throw new IllegalArgumentException("Passed tracer:" + tracerName +
		// ", name for source: " + notificationSource +
		// ", is illegal, it can not be empty. Name part: " + part +
		// ", at index: "
		// + fqdnPartIndex);
		// }
		//
		// for (int charIndex = 0; charIndex < part.length(); charIndex++) {
		// Character c = part.charAt(charIndex);
		// if (c.isLetter(c) || c.isDigit(c)) {
		// // Its ok?
		// } else {
		// throw new IllegalArgumentException("Passed tracer:" + tracerName +
		// ", name for source: " + notificationSource +
		// ", is illegal, contains illegal character: " + charIndex);
		// }
		//
		// }
		//
		// fqdnPartIndex++;
		// }

	}

	public void registerNotificationSource(final NotificationSource src) {
		if (this.tracerStorage.containsKey(src)) {

		} else {
			TracerStorage ts = new TracerStorage(src, this);
			this.tracerStorage.put(src, ts);
			
		}
		
		
		try {
			if(SleeContainer.lookupFromJndi().getTransactionManager().getTransaction()!=null)
			{
				TransactionalAction action = new TransactionalAction() {
					NotificationSource notiSrc = src;
					public void execute() {
						tracerStorage.remove(notiSrc);
					}
				};
				SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
			}
		} catch (SystemException e) {
			
			e.printStackTrace();
		}
		
		
		
	}

	/**
	 * This method shoudl be called on on removal of notification source from
	 * slee
	 * 
	 * @param src
	 */
	public void deregisterNotificationSource(final NotificationSource src) {
		
		final TracerStorage st=this.tracerStorage.remove(src);
		
		try {
			if(SleeContainer.lookupFromJndi().getTransactionManager().getTransaction()!=null)
			{
				TransactionalAction action = new TransactionalAction() {
					NotificationSource notiSrc = src;
					TracerStorage storage = st;
					public void execute() {
						tracerStorage.put(src,storage);
					}
				};
				SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
			}
		} catch (SystemException e) {
			
			e.printStackTrace();
		}
	}

	public boolean isNotificationSourceDefined(NotificationSource src) {
		return this.tracerStorage.containsKey(src);
	}

	public boolean isTracerDefined(NotificationSource src, String tracerName) throws ManagementException {
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}

		return ts.isTracerDefined(tracerName);
	}

	/**
	 * This method creates tracer for specified source, with specified name.
	 * boolean flag indicates that tracer has been requested by src, else, its
	 * just management operation. This method can me invoked multiple times.
	 * Only difference is boolean flag.
	 * 
	 * @param src
	 *            - notification source
	 * @param tracerName
	 *            - tracer name
	 * @param createdBySource
	 *            - flag indicating that src requested this tracer. In case
	 *            tracer already exists (created by mgmt operation) and this
	 *            method is invoked from NotificationSource this flag is set to
	 *            true. This alters state of tracer. It can not be changed back
	 * @return Tracer object. Either newly created or one that previously
	 *         existed.
	 * 
	 */
	public Tracer createTracer(NotificationSource src, String tracerName, boolean createdBySource) throws ManagementException {

		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}

		return ts.createTracer(tracerName, createdBySource);

	}

	public String[] getTracersSet(NotificationSource src) throws ManagementException {
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		return ts.getDefinedTracerNames();
	}

	public String[] getTracersUsed(NotificationSource src) throws ManagementException {
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		return ts.getRequestedTracerNames();
	}

	public void setTraceLevel(NotificationSource src, final String tracerName, TraceLevel lvl) throws ManagementException {
		final TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			
			
			try {
				if(SleeContainer.lookupFromJndi().getTransactionManager().getTransaction()!=null)
				{
					final TraceLevel _oldLevel=ts.getTracerLevel(tracerName);
					TransactionalAction action = new TransactionalAction() {
						TraceLevel oldLevel = _oldLevel;
				
						String name = tracerName;
						public void execute() {
							try {
								ts.setTracerLevel(oldLevel, tracerName);
							} catch (InvalidArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
				}
			} catch (SystemException e) {
				
				e.printStackTrace();
			}
			
			ts.setTracerLevel(lvl, tracerName);
			
			
		} catch (Exception e) {
			throw new ManagementException("Failed to set trace level due to: ", e);
		}

	}

	public void unsetTraceLevel(NotificationSource src, final String tracerName) throws ManagementException {
		final TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			
			
			
			try {
				if(SleeContainer.lookupFromJndi().getTransactionManager().getTransaction()!=null)
				{
					final TraceLevel _oldLevel=ts.getTracerLevel(tracerName);
					TransactionalAction action = new TransactionalAction() {
						TraceLevel oldLevel = _oldLevel;
				
						String name = tracerName;
						public void execute() {
							try {
								ts.setTracerLevel(oldLevel, tracerName);
							} catch (InvalidArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					};
					SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
				}
			} catch (SystemException e) {
				
				e.printStackTrace();
			}
			
			
			
			
			
			ts.unsetTracerLevel(tracerName);
		} catch (Exception e) {
			throw new ManagementException("Failed to unset trace level due to: ", e);
		}

	}

	public TraceLevel getTraceLevel(NotificationSource src, String tracerName) throws ManagementException {
		TracerStorage ts = this.tracerStorage.get(src);
		if (ts == null) {
			throw new ManagementException("NotificationSource has been uninstalled from SLEE. Can not create tracer.");
		}
		try {
			return ts.getTracerLevel(tracerName);
		} catch (Exception e) {
			throw new ManagementException("Failed to unset trace level due to: ", e);
		}
	}

}
