/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.StandardMBean;
import javax.slee.ComponentID;
import javax.slee.InvalidArgumentException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.UnrecognizedSbbException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.slee.management.ManagementException;
import javax.slee.management.TraceMBean;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;

/**
 * Implementation of the Trace MBean.
 * 
 * @author M. Ranganathan
 *  
 */
public class TraceMBeanImpl extends ServiceMBeanSupport implements TraceMBeanImplMBean 
	{

    private TraceFacilityImpl traceFacility;

    private static Logger logger;

  
    private NotificationBroadcasterSupport notifSupport = new NotificationBroadcasterSupport();

    static {
        logger = Logger.getLogger(TraceMBeanImpl.class);
    }

    /** 
     * Creates the trace mbean and registers itself in JNDI.
     * 
     * @throws NotCompliantMBeanException
     */
    public TraceMBeanImpl() throws NotCompliantMBeanException {
        super(TraceMBeanImplMBean.class);
        this.traceFacility = new TraceFacilityImpl(this);
    }


    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.TraceMBean#setTraceLevel(javax.slee.ComponentID,
     *      javax.slee.facilities.Level)
     */
    public void setTraceLevel(ComponentID componentId, Level level)
            throws NullPointerException, UnrecognizedComponentException,
            ManagementException {
        if (componentId == null)
            throw new NullPointerException("null component Id");
        //this.traceFacility.checkComponentID(componentId);
        this.traceFacility.setTraceLevel(componentId, level);

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.TraceMBean#getTraceLevel(javax.slee.ComponentID)
     */
    public Level getTraceLevel(ComponentID componentId)
            throws NullPointerException, UnrecognizedComponentException,
            ManagementException {
        if (componentId == null)
            throw new NullPointerException(" null component Id ");
        Level level = this.traceFacility.getTraceLevel(componentId);
        return level;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener,
     *      javax.management.NotificationFilter, java.lang.Object)
     */
    public void addNotificationListener(
            NotificationListener notificationListener,
            NotificationFilter notificationFilter, Object handbackObject)
            throws IllegalArgumentException {
        notifSupport.addNotificationListener(notificationListener,
                notificationFilter, handbackObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
     */
    public void removeNotificationListener(
            NotificationListener notificationListener)
            throws ListenerNotFoundException {
        notifSupport.removeNotificationListener(notificationListener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.management.NotificationBroadcaster#getNotificationInfo()
     */
    public MBeanNotificationInfo[] getNotificationInfo() {

        if (this.traceFacility == null)
            return null;

        String[] notificationTypes = this.traceFacility.getNotificationTypes();
        MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
                notificationTypes, TraceMBean.TRACE_NOTIFICATION_TYPE,
                "SLEE Spec 1.0, #13.4. SBBs use the Trace Facility to generate trace messages intended for " 
                	+ "consumption by external management clients, such as a network management console or a management policy engine.") };
        return mbeanNotificationInfo;
    }

    public void sendNotification(Notification notification) {
        notifSupport.sendNotification(notification);
    }

    /**
     * the JNDI name where this facility is be bound under the facilities
     * context. The name is fixed by the SLEE 1.0 spec, section 13.8
     */
    public static final String JNDI_NAME = "trace";

    protected void startService() throws Exception {
        SleeContainer.registerFacilityWithJndi(JNDI_NAME, this.traceFacility);
        logger.info("TraceMBean started");
    }

    protected void stopService() throws Exception {
        SleeContainer.unregisterFacilityWithJndi(JNDI_NAME);
        logger.info("TraceMBean stopped");
    }


	public TraceLevel getResourceAdaptorEntityTraceLevel(String arg0, String arg1) throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public String[] getResourceAdaptorEntityTracerNames(String arg0) throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public String[] getResourceAdaptorEntityTracersSet(String arg0) throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public TraceLevel getSbbTraceLevel(ServiceID arg0, SbbID arg1, String arg2) throws NullPointerException, UnrecognizedServiceException, UnrecognizedSbbException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public String[] getSbbTracerNames(ServiceID arg0, SbbID arg1) throws NullPointerException, UnrecognizedServiceException, UnrecognizedSbbException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public String[] getSbbTracersSet(ServiceID arg0, SbbID arg1) throws NullPointerException, UnrecognizedServiceException, UnrecognizedSbbException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}


	public void setResourceAdaptorEntityTraceLevel(String arg0, String arg1, TraceLevel arg2) throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException {
		// TODO Auto-generated method stub
		
	}


	public void setSbbTraceLevel(ServiceID arg0, String arg1, TraceLevel arg2) throws NullPointerException, UnrecognizedServiceException, ManagementException {
		// TODO Auto-generated method stub
		
	}


	public void setSbbTraceLevel(ServiceID arg0, SbbID arg1, String arg2, TraceLevel arg3) throws NullPointerException, UnrecognizedServiceException, UnrecognizedSbbException, ManagementException {
		// TODO Auto-generated method stub
		
	}


	public void unsetResourceAdaptorEntityTraceLevel(String arg0, String arg1) throws NullPointerException, UnrecognizedResourceAdaptorEntityException, InvalidArgumentException, ManagementException {
		// TODO Auto-generated method stub
		
	}


	public void unsetSbbTraceLevel(ServiceID arg0, SbbID arg1, String arg2) throws NullPointerException, UnrecognizedServiceException, UnrecognizedSbbException, InvalidArgumentException, ManagementException {
		// TODO Auto-generated method stub
		
	}
}

