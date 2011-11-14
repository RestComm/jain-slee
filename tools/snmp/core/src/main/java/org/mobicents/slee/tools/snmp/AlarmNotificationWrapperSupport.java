package org.mobicents.slee.tools.snmp;

import javax.management.Notification;
import javax.slee.management.AlarmNotification;

import org.jboss.jmx.adaptor.snmp.agent.NotificationWrapperSupport;
/**
 * Simple implementation of wrapper support. It fetches all data from Alarm notification and encodes it as
 * @author baranowb
 *
 */
public class AlarmNotificationWrapperSupport extends NotificationWrapperSupport {

	// Agent properties
	public static final String NOTIFICATION_SRC_TAG = "n:source";
	public static final String ALARM_LEVEL_TAG = "n:level";
	public static final String INSTANCE_ID_TAG = "n:instanceId";
	public static final String ALARM_ID_TAG = "n:id";
	public static final String ALARM_TYPE_TAG = "n:type";
	
	  
	
	public AlarmNotificationWrapperSupport() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prime(Notification n) {
		if( !(n instanceof AlarmNotification) )
		{
			throw new IllegalArgumentException("Wrong type of notification!");
		}
		//trigger super, so it puts all the data...
		super.prime(n);
		//now put our specific data
		AlarmNotification an = (AlarmNotification) n;
		//remember, we may get v1 alarms... which dont have certain fields!
		//simply divide... this will bring less chaos!
		
		//current version of adaptor does not clean payload...
		super.payload.remove(NOTIFICATION_SRC_TAG);
		super.payload.remove(ALARM_LEVEL_TAG);
		super.payload.remove(INSTANCE_ID_TAG);
		super.payload.remove(ALARM_ID_TAG);
		super.payload.remove(ALARM_TYPE_TAG);
		
		if(an.getNotificationSource()==null)
		{
			//1.0 - it does not set NS, however 1.1 alarm sets alarmSource to NS!
			super.payload.put(NOTIFICATION_SRC_TAG, an.getAlarmSource().toString());
			super.payload.put(ALARM_LEVEL_TAG, an.getLevel().toString());
		}else
		{
			//1.1
			super.payload.put(NOTIFICATION_SRC_TAG, an.getNotificationSource().toString());
			super.payload.put(ALARM_LEVEL_TAG, an.getAlarmLevel().toString());
			super.payload.put(INSTANCE_ID_TAG, an.getInstanceID());
			super.payload.put(ALARM_ID_TAG, an.getAlarmID());
		}
		
		//common part.
		super.payload.put(ALARM_TYPE_TAG, an.getAlarmType());
		if(an.getCause()!=null)
		{
			//TODO: this possibly may be tooooo large? leave it for now.
		}

	}
}
