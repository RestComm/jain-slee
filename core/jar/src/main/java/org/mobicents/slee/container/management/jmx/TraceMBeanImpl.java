/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.slee.ComponentID;
import javax.slee.InvalidArgumentException;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceLevel;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.TraceMBean;
import javax.slee.management.UnrecognizedNotificationSourceException;

import org.jboss.logging.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.TraceFacilityImpl;

/**
 * Implementation of the Trace MBean.
 * 
 * @author M. Ranganathan
 * @author baranowb
 */
public class TraceMBeanImpl extends ServiceMBeanSupport implements TraceMBeanImplMBean {

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
	 * @see
	 * javax.slee.management.TraceMBean#setTraceLevel(javax.slee.ComponentID,
	 * javax.slee.facilities.Level)
	 */
	public void setTraceLevel(ComponentID componentId, Level level) throws NullPointerException, UnrecognizedComponentException, ManagementException {
		if (componentId == null)
			throw new NullPointerException("null component Id");
		// this.traceFacility.checkComponentID(componentId);
		this.traceFacility.setTraceLevel(componentId, level);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.management.TraceMBean#getTraceLevel(javax.slee.ComponentID)
	 */
	public Level getTraceLevel(ComponentID componentId) throws NullPointerException, UnrecognizedComponentException, ManagementException {
		if (componentId == null)
			throw new NullPointerException(" null component Id ");
		Level level = this.traceFacility.getTraceLevel(componentId);
		return level;
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
		MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(notificationTypes, TraceMBean.TRACE_NOTIFICATION_TYPE,
				"SLEE Spec 1.0, #13.4. SBBs use the Trace Facility to generate trace messages intended for "
						+ "consumption by external management clients, such as a network management console or a management policy engine.") };
		return mbeanNotificationInfo;
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

	public TraceLevel getTraceLevel(NotificationSource src, String tracerName) throws NullPointerException, InvalidArgumentException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		
		if(tracerName == null)
		{
			throw new NullPointerException("TracerName must not be null!");
		}
		if(!this.traceFacility.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TraceFacilityImpl.checkTracerName(tracerName, src);
		
		if(!this.traceFacility.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.traceFacility.createTracer(src,tracerName,false);
		}
		
		
		
		return this.traceFacility.getTraceLevel(src,tracerName);
	}

	public String[] getTracersSet(NotificationSource src) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		if(!this.traceFacility.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}

			
		return this.traceFacility.getTracersSet(src);
	}

	public String[] getTracersUsed(NotificationSource src) throws NullPointerException, UnrecognizedNotificationSourceException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		if(!this.traceFacility.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		return this.traceFacility.getTracersUsed(src);
	}

	public void setTraceLevel(NotificationSource src, String tracerName, TraceLevel lvl) throws NullPointerException, UnrecognizedNotificationSourceException, InvalidArgumentException,
			ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		
		if(tracerName == null)
		{
			throw new NullPointerException("TracerName must not be null!");
		}
		
		if(lvl == null)
		{
			throw new NullPointerException("TraceLevel must not be null!");
		}
		if
		(!this.traceFacility.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TraceFacilityImpl.checkTracerName(tracerName, src);
		
		if(!this.traceFacility.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.traceFacility.createTracer(src,tracerName,false);
		}
		
		this.traceFacility.setTraceLevel(src, tracerName, lvl);
		
	}

	public void unsetTraceLevel(NotificationSource src, String tracerName) throws NullPointerException, UnrecognizedNotificationSourceException, InvalidArgumentException, ManagementException {
		if(src == null)
		{
			throw new NullPointerException("NotificationSource must nto be null!");
		}
		
		if(tracerName == null)
		{
			throw new NullPointerException("TracerName must not be null!");
		}
		if(!this.traceFacility.isNotificationSourceDefined(src))
		{
			throw new UnrecognizedNotificationSourceException("Notification source not recognized: "+src);
		}
		TraceFacilityImpl.checkTracerName(tracerName, src);
		
		if(!this.traceFacility.isTracerDefined(src,tracerName))
		{
			//FIXME: what is valid tracer name? JDOC contradicts that not existing tracer name is invalid
			this.traceFacility.createTracer(src,tracerName,false);
		}
		
		this.traceFacility.unsetTraceLevel(src, tracerName);
		
	}

	
}
