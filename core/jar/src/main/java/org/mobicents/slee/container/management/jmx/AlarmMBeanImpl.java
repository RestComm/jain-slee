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
 * AlarmMBeanImpl.java
 * 
 */
package org.mobicents.slee.container.management.jmx;

import java.util.Iterator;
import java.util.List;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.slee.AlarmID;
import javax.slee.UnrecognizedAlarmException;
import javax.slee.management.Alarm;
import javax.slee.management.AlarmMBean;
import javax.slee.management.AlarmNotification;
import javax.slee.management.ManagementException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.AlarmFacilityImpl;

import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tim
 *
 * Implementation of the Alarm MBean: The implementation of the JMX interface to the
 * SLEE alarm facility
 * 
 */
public class AlarmMBeanImpl extends ServiceMBeanSupport implements AlarmMBeanImplMBean {
	
	private List listeners;	
	private AlarmFacilityImpl alarmFacility;
	public static String JNDI_NAME = "alarm";
	
	private static Logger log = Logger.getLogger(AlarmMBeanImpl.class);
	  
	/*
	 * Lifted from TraceMBeanImpl
	 */
	class ListenerFilterHandbackTriplet {
        NotificationListener notificationListener;
        NotificationFilter  notificationFilter;
        Object handbackObject;
        public ListenerFilterHandbackTriplet (NotificationListener listener, 
                NotificationFilter notificationFilter,
                Object handbackObject) {
            this.notificationListener = listener;
            this.notificationFilter = notificationFilter;
            this.handbackObject = handbackObject;            
        }
    }
	
	public AlarmMBeanImpl() throws NotCompliantMBeanException {
        super(AlarmMBeanImplMBean.class);
        
        /* We use a CopyOnWriteArrayList to ensure thread safety without having
         * to synchronize all operations
         */
        listeners = new CopyOnWriteArrayList();
        this.alarmFacility = new AlarmFacilityImpl(this);
    }
	
	public AlarmMBeanImpl(AlarmFacilityImpl alarmFacility) throws NotCompliantMBeanException {
        this();
        this.alarmFacility = alarmFacility;
    }
 
       
	/* Add a notification listener
	 * Created from equivalent method in TraceMBeanImpl.
	 * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener, javax.management.NotificationFilter, java.lang.Object)
	 */
	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
	    log.debug("addNotificationListener");
		this.listeners.add(new ListenerFilterHandbackTriplet(listener, filter, handback));
	}

	/* Remove the listener from the list.
	 * Created from equivalent method in TraceMBeanImpl.
	 * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
	 */
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
	    log.debug("removeNotificationListener");
	    boolean found = false;
	    Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet)iter.next();            
            if (triplet.notificationListener == listener) {
                found = true;
               
                break;
            }
        }
        // iter.remove is not supported 
        if ( found )listeners.remove(listener);
        else throw new ListenerNotFoundException();  
	}

	/* Created from equivalent method in TraceMBeanImpl
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
	    log.debug("getNotificationInfo");
	    if (this.alarmFacility == null) return null;
	    String[] notificationTypes = this.alarmFacility.getNotificationTypes();
        MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[ ] { 
                new MBeanNotificationInfo(notificationTypes, AlarmMBean.ALARM_NOTIFICATION_TYPE, 
                    "User Notifications")};
       return mbeanNotificationInfo;
	}
	
	/**
	 * Send a notification to all listeners taking into account any filter.
	 */
	public void sendNotification(AlarmNotification notification)
	{
	    log.debug("sendNotification");
	    Iterator iter = listeners.iterator();
	    while (iter.hasNext()){
	        ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet)iter.next();
	        if (triplet.notificationFilter == null || triplet.notificationFilter.isNotificationEnabled(notification)){
	            triplet.notificationListener.handleNotification(notification, triplet.handbackObject);
	        }	           
	    }
	}

	protected void startService() throws Exception {
        SleeContainer.registerFacilityWithJndi(JNDI_NAME,this.alarmFacility);
	}

	protected void stopService() throws Exception {
        SleeContainer.unregisterFacilityWithJndi(JNDI_NAME);
	}

	public void clearAlarm(AlarmID arg0) throws NullPointerException, ManagementException {
		// TODO Auto-generated method stub
		
	}

	public void clearAlarms(String arg0) throws NullPointerException, ManagementException {
		// TODO Auto-generated method stub
		
	}

	public Alarm getAlarm(AlarmID arg0) throws NullPointerException, UnrecognizedAlarmException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public AlarmID[] getAlarms() throws ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	public Alarm[] getAlarms(AlarmID[] arg0) throws NullPointerException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}
}
