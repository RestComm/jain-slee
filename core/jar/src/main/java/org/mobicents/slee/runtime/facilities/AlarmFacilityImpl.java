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
import java.util.concurrent.atomic.AtomicInteger;

import javax.slee.ComponentID;
import javax.slee.UnrecognizedAlarmException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.AlarmLevel;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Level;
import javax.slee.management.AlarmNotification;
import javax.slee.management.NotificationSource;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.AlarmMBeanImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

/**
 * Implementation of the SLEE Alarm facility. This is statefull part of
 * TraceFacility framework. Its has assigned source. All no statefull alarm are
 * delegated to MBean impl. Since those are common for ALL components. MBean is
 * responsible for maintainng life cycle of alarms.
 * 
 * @author baranowb
 * @see javax.slee.facilities.AlarmFacility
 * 
 */
public class AlarmFacilityImpl implements AlarmFacility {

	private AlarmMBeanImpl mBean;

	private MNotificationSource notificationSource = null;

	private AtomicInteger sequenceNumber = new AtomicInteger();
	private Logger log = Logger.getLogger(AlarmFacilityImpl.class);

	public AlarmFacilityImpl(AlarmMBeanImpl aMBean, NotificationSource notificationSource) {

		if(aMBean == null || notificationSource == null)
		{
			throw new NullPointerException("Parameters must not be null.");
		}
		this.mBean = aMBean;
		this.notificationSource = new MNotificationSource(notificationSource);

	}

	public boolean clearAlarm(String alarmID) throws NullPointerException, FacilityException {
		if (alarmID == null) {
			throw new NullPointerException("AllarmID must not be null.");
		}

		if (this.mBean.isSourceOwnerOfAlarm(this.notificationSource, alarmID)) {
			//FIXME: not specified
			throw new FacilityException("Source: "+this.notificationSource+", is not owner of alarm with id: "+alarmID);
		}
		
		try {
			

			return this.mBean.clearAlarm(alarmID);

		} catch (Exception e) {
			throw new FacilityException("Failed to clear alarm: " + alarmID + ", for source: " + this.notificationSource, e);
		}
		
	}

	public int clearAlarms() throws FacilityException {
		try {
			return this.mBean.clearAlarms(this.notificationSource.getNotificationSource());
		} catch (Exception e) {
			throw new FacilityException("Failed to clear alarms for source: " + this.notificationSource, e);
		}
	}

	public int clearAlarms(String alarmType) throws NullPointerException, FacilityException {
		if (alarmType == null) {
			throw new NullPointerException("AlarmType must not be null.");
		}
		try {
			return this.mBean.clearAlarms(this.notificationSource.getNotificationSource(), alarmType);
		} catch (Exception e) {
			throw new FacilityException("Failed to clear alarms for source: " + this.notificationSource, e);
		}
	}

	public String raiseAlarm(java.lang.String alarmType, java.lang.String instanceID, AlarmLevel level, String message) throws NullPointerException, IllegalArgumentException, FacilityException {

		return this.raiseAlarm(alarmType, instanceID, level, message, null);
	}

	public String raiseAlarm(java.lang.String alarmType, java.lang.String instanceID, AlarmLevel level, String message, Throwable cause) throws NullPointerException, IllegalArgumentException,
			FacilityException {
		if (alarmType == null) {
			throw new NullPointerException("AlarmType must not be null");
		}

		if (instanceID == null) {
			throw new NullPointerException("InstanceID must not be null");
		}

		if (level == null) {
			throw new NullPointerException("AlarmLevel must not be null");
		}
		if (message == null) {
			throw new NullPointerException("Message must not be null");
		}

		if (level.isClear()) {
			throw new IllegalArgumentException("Raised alarm must not have level equal to AlarmLevel.CLEAR");
		}

		try {
			if (this.mBean.isAlarmAlive(this.notificationSource, alarmType, instanceID)) {
				return this.mBean.getAlarmId(this.notificationSource, alarmType, instanceID);
			} else {
				return this.mBean.raiseAlarm(this.notificationSource, alarmType, instanceID, level, message, cause);
			}
		} catch (Exception e) {
			throw new FacilityException("Failed to raise alarm for source: " + this.notificationSource, e);
		}
	}

	public void createAlarm(ComponentID alarmSource, Level alarmLevel, java.lang.String alarmType, java.lang.String message, long timestamp) throws NullPointerException, IllegalArgumentException,
			UnrecognizedComponentException, FacilityException {
		this.createAlarm(alarmSource, alarmLevel, alarmType, message, null, timestamp,true);

	}

	public void createAlarm(ComponentID alarmSource, Level alarmLevel, java.lang.String alarmType, java.lang.String message, java.lang.Throwable cause, long timestamp) throws NullPointerException,
			IllegalArgumentException, UnrecognizedComponentException, FacilityException {
		this.createAlarm(alarmSource, alarmLevel, alarmType, message, null, timestamp,false);
	}

	public void createAlarm(ComponentID alarmSource, Level alarmLevel, java.lang.String alarmType, java.lang.String message, java.lang.Throwable cause, long timestamp, boolean allowCauseNull) throws NullPointerException,
			IllegalArgumentException, UnrecognizedComponentException, FacilityException {
		if(alarmSource == null)
		{
			throw new NullPointerException("AlarmSource must not be null");
		}
		
		if(alarmLevel == null)
		{
			throw new NullPointerException("AlarmLevel must not be null");
		}
		
		if(alarmType == null)
		{
			throw new NullPointerException("AlarmType must not be null");
		}
		
		if(message == null)
		{
			throw new NullPointerException("Message must not be null");
		}
		
		if(!allowCauseNull && cause == null)
		{
			throw new NullPointerException("Cause must nto be null");
		}
		
		if(!this.mBean.isRegisteredAlarmComponent(alarmSource))
		{
			throw new UnrecognizedComponentException("Declared alarm source is not valid compoenent. Either it is nto able to create alarms or has been uninstalled");
		}
		
		try{
			this.mBean.createAlarm( alarmSource,  alarmLevel, alarmType,  message, cause,  timestamp);
		}catch(Exception e)
		{
			
		}
		
	}

}
