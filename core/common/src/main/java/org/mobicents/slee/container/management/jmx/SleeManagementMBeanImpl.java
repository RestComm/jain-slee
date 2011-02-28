package org.mobicents.slee.container.management.jmx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.management.SleeStateChangeNotification;
import javax.slee.management.UnrecognizedSubsystemException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;

/**
 * Implementation of the Slee Management MBean for SLEE 1.1 specs
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Eduardo Martins
 * 
 */
public class SleeManagementMBeanImpl extends StandardMBean implements
		SleeManagementMBeanImplMBean {
		
	private ObjectName activityManagementMBean;

	private ObjectName sbbEntitiesMBean;

	private static Logger logger;

	private ObjectName deploymentMBean;

	private SleeContainer sleeContainer;

	private ObjectName profileProvisioningMBean;

	private ObjectName serviceManagementMBean;

	private ObjectName rmiServerInterfaceMBean;

	private ObjectName resourceManagementMBean;

	private NotificationBroadcasterSupport notificationBroadcaster = new NotificationBroadcasterSupport();

	/** counter for the number of slee state change notifications sent out */
	private long sleeStateChangeSequenceNumber = 1;

	/**
	 * The array of MBean notifications that this MBean broadcasts
	 * 
	 */
	private static final MBeanNotificationInfo[] MBEAN_NOTIFICATIONS;

	private ObjectName objectName;

	static {
		MBEAN_NOTIFICATIONS = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				new String[] { SleeStateChangeNotification.class.getName() },
				SleeManagementMBean.SLEE_STATE_CHANGE_NOTIFICATION_TYPE,
				"SLEE 1.0 Spec, #14.6, Each time the operational state of the SLEE changes, "
						+ "the SleeManagementMBean object generates a SLEE state change notification.") };
		try {
			logger = Logger.getLogger(SleeManagementMBean.class);
		} catch (Exception ex) {
			logger.error("error initializing slee management mbean");
		}
	}

	/**
	 * Default constructor
	 * 
	 * @throws Exception
	 */
	public SleeManagementMBeanImpl(SleeContainer sleeContainer) throws NotCompliantMBeanException {
		super(SleeManagementMBeanImplMBean.class);
		this.sleeContainer = sleeContainer;
	}

	/**
	 * 
	 * @return the ObjectName of the SleeManagementMBean
	 */
	public ObjectName getObjectName() {
		return this.objectName;
	}

	/**
	 * @return the current state of the SLEE Container
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getState()
	 */
	public SleeState getState() throws ManagementException {

		return this.sleeContainer.getSleeState();
	}

	/**
	 * Start the SLEE container
	 * 
	 * @see javax.slee.management.SleeManagementMBean#start()
	 */
	public void start() throws InvalidStateException, ManagementException {
		try {
			changeSleeState(SleeState.STARTING);
			changeSleeState(SleeState.RUNNING);
		} catch (InvalidStateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage());
		}
		try {
			
		
		} catch (Throwable ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/**
	 * Gracefully stop the SLEE. Should do it in a non-blocking manner.
	 * 
	 * @see javax.slee.management.SleeManagementMBean#stop()
	 */
	public void stop() throws InvalidStateException, ManagementException {
		stop(false);
	}

	private void stop(boolean blockTillStopped) throws InvalidStateException, ManagementException {

		try {

			changeSleeState(SleeState.STOPPING);

			logger.info(generateMessageWithLogo("stopping"));

			// tck requires the stopping process to run in a diff thread
			final ExecutorService exec = Executors.newSingleThreadExecutor();
			Runnable r = new Runnable() {
				public void run() {
					synchronized (sleeContainer.getManagementMonitor()) {
						try {
							changeSleeState(SleeState.STOPPED);
						} catch (InvalidStateException e) {
							logger.error(e.getMessage(),e);
						}
					}						
					exec.shutdown();
				}
			};
			Future<?> future = exec.submit(r);
			if (blockTillStopped) {
				future.get();					
			}
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			if (ex instanceof InvalidStateException)
				throw (InvalidStateException) ex;
			else
				throw new ManagementException("Failed stopping SLEE container",
						ex);
		}	
	}

	/**
	 * Shutdown the SLEE processes. The spec requires that System.exit() be
	 * called before this methods returns. We are not convinced this is
	 * necessary yet. A trivial implementation would be to make a call to the
	 * JBoss server shutdown()
	 * 
	 * @see javax.slee.management.SleeManagementMBean#shutdown()
	 */
	public void shutdown() throws InvalidStateException, ManagementException {
		try {
			sleeContainer.setSleeState(null);
		} catch (InvalidStateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage());
		}
	}

	/**
	 * return the ObjectName of the DeploymentMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getDeploymentMBean()
	 */
	public ObjectName getDeploymentMBean() {
		return this.deploymentMBean;
	}

	/**
	 * set the ObjectName of the DeploymentMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getDeploymentMBean()
	 */
	public void setDeploymentMBean(ObjectName newDM) {
		this.deploymentMBean = newDM;
	}

	/**
	 * return the ObjectName of the ServiceManagementMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getServiceManagementMBean()
	 */
	public ObjectName getServiceManagementMBean() {
		return this.serviceManagementMBean;
	}

	/**
	 * set the ObjectName of the ServiceManagementMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setServiceManagementMBean()
	 */
	public void setServiceManagementMBean(ObjectName newSMM) {
		this.serviceManagementMBean = newSMM;
	}

	/*
	 * return the ObjectName of the ProfileProvisioningMBean
	 * 
	 * @see
	 * javax.slee.management.SleeManagementMBean#getProfileProvisioningMBean()
	 */
	public ObjectName getProfileProvisioningMBean() {
		return this.profileProvisioningMBean;
	}

	/**
	 * set the ObjectName of the ProfileProvisioningMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setProfileProvisioningMBean()
	 */
	public void setProfileProvisioningMBean(ObjectName newPPM) {
		this.profileProvisioningMBean = newPPM;
	}

	/*
	 * return the ObjectName of the TraceMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getTraceMBean()
	 */
	public ObjectName getTraceMBean() {
		return sleeContainer.getTraceManagement().getTraceMBeanObjectName();
	}

	// /**
	// * @param resourceAdaptorMBean The resourceAdaptorMBean to set.
	// * @deprecated Use setResourceManagementMBean
	// */
	// public void setResourceAdaptorMBean(ObjectName resourceAdaptorMBean) {
	// this.resourceAdaptorMBean = resourceAdaptorMBean;
	// }

	public void setResourceManagementMBean(ObjectName resourceManagementMBean) {
		this.resourceManagementMBean = resourceManagementMBean;
	}

	// /**
	// * @return Returns the resourceAdaptorMBean.
	// * @deprecated Use getResourceManagementMBean
	// */
	// public ObjectName getResourceAdaptorMBean() {
	// return resourceAdaptorMBean;
	// }

	public ObjectName getResourceManagementMBean() {
		return resourceManagementMBean;
	}

	/**
	 * return the ObjectName of the AlarmMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getAlarmMBean()
	 */
	public ObjectName getAlarmMBean() {
		return sleeContainer.getAlarmManagement().getAlarmMBeanObjectName();
	}
	
	public ObjectName preRegister(MBeanServer mbs, ObjectName oname)
			throws Exception {
		this.objectName = oname;
		return oname;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postRegister(java.lang.Boolean)
	 */
	public void postRegister(Boolean arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#preDeregister()
	 */
	public void preDeregister() throws Exception {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.MBeanRegistration#postDeregister()
	 */
	public void postDeregister() {
		// TODO Auto-generated method stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.NotificationBroadcaster#addNotificationListener(javax
	 * .management.NotificationListener, javax.management.NotificationFilter,
	 * java.lang.Object)
	 */
	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
		notificationBroadcaster.addNotificationListener(listener, filter,
				handback);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.management.NotificationBroadcaster#removeNotificationListener(javax
	 * .management.NotificationListener)
	 */
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		notificationBroadcaster.removeNotificationListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
		return MBEAN_NOTIFICATIONS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.system.Service#create()
	 */
	public void create() throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jboss.system.Service#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/**
	 * Changes the SLEE container state and emits JMX notifications
	 * 
	 * @param newState
	 */
	protected void changeSleeState(SleeState newState) throws InvalidStateException {
		SleeState oldState = sleeContainer.getSleeState();
		sleeContainer.setSleeState(newState);
		notificationBroadcaster
				.sendNotification(new SleeStateChangeNotification(this,
						newState, oldState, sleeStateChangeSequenceNumber++));
		if (newState == SleeState.RUNNING) {
			logger.info(generateMessageWithLogo("started"));
		} 
		else {
			if (newState == SleeState.STOPPED) {
				logger.info(generateMessageWithLogo("stopped"));				
			}			
		}		
	}

	public ObjectName getActivityManagementMBean() {
		return activityManagementMBean;
	}

	public void setActivityManagementMBean(ObjectName activityManagementMBean) {
		this.activityManagementMBean = activityManagementMBean;
	}

	public ObjectName getSbbEntitiesMBean() {
		return sbbEntitiesMBean;
	}

	public void setSbbEntitiesMBean(ObjectName sbbEntitiesMBean) {
		this.sbbEntitiesMBean = sbbEntitiesMBean;
	}

	public ObjectName getRmiServerInterfaceMBean() {
		return rmiServerInterfaceMBean;
	}

	public void setRmiServerInterfaceMBean(ObjectName rmiServerInterfaceMBean) {
		this.rmiServerInterfaceMBean = rmiServerInterfaceMBean;
	}
	
	// ah ah

	private static final String rLogo = " >< >< >< >< >< >< >< ";
	private static final String lLogo = rLogo;

	private String generateMessageWithLogo(String message) {
		return lLogo + getSleeName() + " " + getSleeVersion() + " \"" + getSleeCodeName() + "\" " + message
				+ rLogo;
	}

	public String getSleeCodeName() {
		return Version.instance.getProperty("codename");
	}
	
	public String getSleeName() {
		String name = Version.instance.getProperty("name");
		if (name != null) {
			return name;
		} else {
			return "Mobicents JAIN SLEE";
		}
	}

	public String getSleeVendor() {
		String vendor = Version.instance.getProperty("vendor");
		if (vendor != null) {
			return vendor;
		} else {
			return "JBoss, a division of Red Hat";
		}
	}

	public String getSleeVersion() {
		String version = Version.instance.getProperty("version");
		if (version != null) {
			return version;
		} else {
			return "2.0";
		}
	}

	// no subsystems defined

	private static final String DUMMY_SUBSYSTEM = "FooSubsystem";

	public String[] getSubsystems() throws ManagementException {
		return new String[] { DUMMY_SUBSYSTEM };
	}

	public ObjectName getUsageMBean(String arg0) throws NullPointerException,
			UnrecognizedSubsystemException, InvalidArgumentException,
			ManagementException {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		if (arg0.equals(DUMMY_SUBSYSTEM)) {
			throw new InvalidArgumentException();
		} else {
			throw new UnrecognizedSubsystemException();
		}
	}

	public ObjectName getUsageMBean(String arg0, String arg1)
			throws NullPointerException, UnrecognizedSubsystemException,
			InvalidArgumentException,
			UnrecognizedUsageParameterSetNameException, ManagementException {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		if (arg0.equals(DUMMY_SUBSYSTEM)) {
			throw new InvalidArgumentException();
		} else {
			throw new UnrecognizedSubsystemException();
		}
	}

	public ObjectName getUsageNotificationManagerMBean(String arg0)
			throws NullPointerException, UnrecognizedSubsystemException,
			InvalidArgumentException, ManagementException {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		if (arg0.equals(DUMMY_SUBSYSTEM)) {
			throw new InvalidArgumentException();
		} else {
			throw new UnrecognizedSubsystemException();
		}
	}

	public String[] getUsageParameterSets(String arg0)
			throws NullPointerException, UnrecognizedSubsystemException,
			InvalidArgumentException, ManagementException {
		if (arg0 == null) {
			throw new NullPointerException();
		}
		if (arg0.equals(DUMMY_SUBSYSTEM)) {
			throw new InvalidArgumentException();
		} else {
			throw new UnrecognizedSubsystemException();
		}
	}

	public boolean hasUsage(String arg0) throws NullPointerException,
			UnrecognizedSubsystemException, ManagementException {

		if (arg0 == null) {
			throw new NullPointerException();
		}
		if (arg0.equals(DUMMY_SUBSYSTEM)) {
			return false;
		} else {
			throw new UnrecognizedSubsystemException();
		}
	}

	public void startSlee() {
		try {
			logger.info(generateMessageWithLogo("starting"));
			sleeContainer.setSleeState(SleeState.STOPPED);
			start();
		} catch (Exception e) {
			logger.error("Failure in SLEE startup", e);
		}
	}
	
	public void stopSlee() {
		try {
			stop(true);
			shutdown();
		} catch (Exception e) {
			logger.error("Failed to stop slee",e);
		}		
	}
	
}
