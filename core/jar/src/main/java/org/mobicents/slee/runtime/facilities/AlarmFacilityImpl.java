/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************
 *
 * Created on Nov 18, 2004
 *
 * AlarmFacilityImpl.java
 * 
 */
package org.mobicents.slee.runtime.facilities;

import java.util.Iterator;
import java.util.Map;

import javax.slee.AlarmID;
import javax.slee.ComponentID;
import javax.slee.UnrecognizedAlarmException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Level;
import javax.slee.management.AlarmNotification;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;


/**
 * Implementation of the SLEE Alarm facility
 * 
 * @author Tim
 *
 * @see javax.slee.facilities.AlarmFacility
 *
 */
public class AlarmFacilityImpl implements AlarmFacility {
	
	private AlarmMBeanImpl mBean;
		
	private Map registeredComps;
	
	private Map notificationTypes;
	
	private Logger log = Logger.getLogger(AlarmFacilityImpl.class);
	
	public AlarmFacilityImpl(AlarmMBeanImpl aMBean) {
	    
	    mBean = aMBean;
	    /* We use a ConcurrentReaderHashMap to ensure thread safety but to avoid
	     * synchronizong all methods. Reads outnumber writes.
	     */
	    registeredComps = new ConcurrentReaderHashMap();
	    
	    /* We also use a ConcurrentReaderHashMap on the notification types.
	     * The concurrency library does not have ConcurrentReaderHashSet so we
	     * can just use the map instead and insert same reference in key and value.
	     */
	    notificationTypes = new ConcurrentReaderHashMap();
	  }    
    
	/*
	 * Represents a component registered with the alarm facility.
	 * Basically just stores notification sequence number
	 * @author Tim
	 */
	class RegisteredComp {
	    public long seqNo;
	    public long getSeqNo() { return seqNo++; }
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.AlarmFacility#createAlarm(javax.slee.ComponentID, javax.slee.facilities.Level, java.lang.String, java.lang.String, long)
	 */
	public void createAlarm(ComponentID alarmSource, Level alarmLevel,
			String alarmType, String message, long timeStamp)
			throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {	    
	    if (log.isDebugEnabled()) {
	        log.debug("createAlarm1");
	    }
	    createAlarmInternal(alarmSource, alarmLevel, alarmType, message, null, timeStamp);			        		
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.facilities.AlarmFacility#createAlarm(javax.slee.ComponentID, javax.slee.facilities.Level, java.lang.String, java.lang.String, java.lang.Throwable, long)
	 */
	public void createAlarm(ComponentID alarmSource, Level alarmLevel,
			String alarmType, String message, Throwable cause, long timeStamp)
			throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {
	    if (log.isDebugEnabled()) {
	        log.debug("createAlarm2");
	    }
	    if (cause == null) throw new NullPointerException("Null parameter");
	    createAlarmInternal(alarmSource, alarmLevel, alarmType, message, cause, timeStamp);			        		
	}
	
	private void createAlarmInternal(ComponentID alarmSource, Level alarmLevel,
			String alarmType, String message, Throwable cause, long timeStamp)
			throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {
	    if (log.isDebugEnabled()) {
	        log.debug("alarmSource:" + alarmSource + " alarmLevel:" + alarmLevel +
	            " alarmType:" + alarmType + " message:" + message + " cause:" + cause +
	            " timeStamp:" + timeStamp);
	    }
		if (alarmSource == null || alarmLevel == null || alarmType == null || message == null)
			throw new NullPointerException("Null parameter");
		if (alarmLevel.isOff()) throw new IllegalArgumentException("Invalid alarm level");
		RegisteredComp comp = (RegisteredComp)registeredComps.get(alarmSource);
		if (comp == null) throw new UnrecognizedComponentException("Component not registered");
		
		//TODO I'm not sure if we should log the alarm too 
		
		//Add the notication type if not already in set. See note in declaration about why we are using a map, not a set
		if (!this.notificationTypes.containsKey(alarmType)) this.notificationTypes.put(alarmType, alarmType);

		//Create the alarm notification and propagate to the Alarm MBean
		AlarmNotification notification =
			new AlarmNotification(mBean, alarmType, alarmSource,
			        			  alarmLevel, message, cause, comp.getSeqNo(), timeStamp);
		
		mBean.sendNotification(notification);		
	}
	
	/**
	 * Register a component with the alarm facility. Called by the SleeContainer
	 * when the component is deployed.
	 * @param sbbid
	 */
	public void registerComponent(final ComponentID sbbid) throws SystemException {
	    if (log.isDebugEnabled()) {
	        log.debug("Registering component with alarm facility: " + sbbid);
	    }
	    
	    registeredComps.put(sbbid, new RegisteredComp());
	    
	    TransactionalAction action = new TransactionalAction() {
	    	public void execute() {
	    		registeredComps.remove(sbbid);    
	    	}
	    };
	    SleeContainer.getTransactionManager().addAfterRollbackAction(action);
	}
	
	/**
	 * Unregister a component.
	 * 
	 * @param sbbId
	 */
	public void unRegisterComponent(final ComponentID sbbId) throws SystemException {
	    final RegisteredComp registeredComp = (RegisteredComp) this.registeredComps.remove(sbbId);
	    if (registeredComp != null) {
	    	TransactionalAction action = new TransactionalAction() {
		    	public void execute() {
		    		registeredComps.put(sbbId,registeredComp);    
		    	}
		    };
		    SleeContainer.getTransactionManager().addAfterRollbackAction(action);
	    }
	}
	
	/* 
	 * Return a string array of the alarm notification types
	*/
	public String[] getNotificationTypes() {
	    if (log.isDebugEnabled()) {
	        log.debug("Getting notification types");
	    }
	    String[] types = new String[this.notificationTypes.size()];
	    Iterator iter = this.notificationTypes.values().iterator();
	    for (int i = 0; i < types.length; i++) while (iter.hasNext()) types[i] = (String)iter.next();
	    return types;
	}

	public void clearAlarm(AlarmID arg0) throws NullPointerException, FacilityException {
		// TODO Auto-generated method stub
		
	}

	public void clearAlarms(String arg0) throws NullPointerException, FacilityException {
		// TODO Auto-generated method stub
		
	}

	public AlarmLevel getAlarmLevel(AlarmID arg0) throws NullPointerException, UnrecognizedAlarmException, FacilityException {
		// TODO Auto-generated method stub
		return null;
	}

	public AlarmID raiseAlarm(String arg0, AlarmLevel arg1, String arg2) throws NullPointerException, IllegalArgumentException, FacilityException {
		// TODO Auto-generated method stub
		return null;
	}

	public AlarmID raiseAlarm(String arg0, AlarmLevel arg1, String arg2, Throwable arg3) throws NullPointerException, IllegalArgumentException, FacilityException {
		// TODO Auto-generated method stub
		return null;
	}

	public void updateAlarm(AlarmID arg0, AlarmLevel arg1, String arg2) throws NullPointerException, UnrecognizedAlarmException, IllegalArgumentException, FacilityException {
		// TODO Auto-generated method stub
		
	}

	public void updateAlarm(AlarmID arg0, AlarmLevel arg1, String arg2, Throwable arg3) throws NullPointerException, UnrecognizedAlarmException, IllegalArgumentException, FacilityException {
		// TODO Auto-generated method stub
		
	}

}
