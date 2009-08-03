package org.mobicents.slee.container.management.jmx;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.management.SleeStateChangeNotification;
import javax.slee.management.UnrecognizedSubsystemException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.resource.ResourceAdaptorObjectState;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.activity.ActivityType;
import org.mobicents.slee.runtime.cache.MobicentsCache;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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

	private MBeanServer mbeanServer;

	private ObjectName alarmMBean;

	private ObjectName activityManagementMBean;

	private ObjectName sbbEntitiesMBean;

	private ObjectName traceMBean;

	private static Logger logger;

	private ObjectName deploymentMBean;

	private SleeContainer sleeContainer;

	private ObjectName profileProvisioningMBean;

	private ObjectName serviceManagementMBean;

	private ObjectName rmiServerInterfaceMBean;

	private ObjectName resourceManagementMBean;

	private SleeTransactionManager sleeTransactionManager;

	private MobicentsCache mobicentsCache;

	private NotificationBroadcasterSupport notificationBroadcaster = new NotificationBroadcasterSupport();

	/** counter for the number of slee state change notifications sent out */
	private long sleeStateChangeSequenceNumber = 1;

	/**
	 * The array of MBean notifications that this MBean broadcasts
	 * 
	 */
	private static final MBeanNotificationInfo[] MBEAN_NOTIFICATIONS;

	/**
	 * Holds the startup time of the Mobicents SAR. Assumes that this MBean is
	 * instantiated first and started last within the scope of Mobicents.sar
	 */
	private long startupTime;

	private boolean isFullSleeStop = true;

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
	public SleeManagementMBeanImpl() throws Exception {
		super(SleeManagementMBeanImplMBean.class);
		startupTime = System.currentTimeMillis();
		logger.info(generateMessageWithLogo("starting"));
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
		if (this.sleeContainer.getSleeState() == SleeState.STARTING
				|| this.sleeContainer.getSleeState() == SleeState.RUNNING
				|| this.sleeContainer.getSleeState() == SleeState.STOPPING)
			throw new InvalidStateException(
					"SLEE is already in an active state");
		try {
			startSleeContainer();
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	/**
	 * Gracefully stop the SLEE. Should do it in a non-blocking manner.
	 * 
	 * @see javax.slee.management.SleeManagementMBean#stop()
	 */
	public void stop() throws InvalidStateException, ManagementException {
		try {
			if (this.sleeContainer.getSleeState() == SleeState.STOPPING
					|| this.sleeContainer.getSleeState() == SleeState.STOPPED)
				throw new InvalidStateException(
						"SLEE is already stopping or stopped.");
			stopSleeContainer();
		} catch (Exception ex) {
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
		if (this.sleeContainer.getSleeState() != SleeState.STOPPED)
			throw new InvalidStateException("SLEE is not in STOPPED state.");
		try {
			// NOP. Because I am not convinced we need to shut JBoss down.
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
		return this.traceMBean;
	}

	/**
	 * set the ObjectName of the TraceMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setTraceMBean()
	 */
	public void setTraceMBean(ObjectName newTM) {
		this.traceMBean = newTM;
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
		return this.alarmMBean;
	}

	/**
	 * set the ObjectName of the AlarmMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setAlarmMBean()
	 */
	public void setAlarmMBean(ObjectName newAM) {
		this.alarmMBean = newAM;
	}

	public ObjectName preRegister(MBeanServer mbs, ObjectName oname)
			throws Exception {
		this.mbeanServer = mbs;
		this.objectName = oname;
		this.sleeContainer = new SleeContainer(mbeanServer,
				sleeTransactionManager, mobicentsCache);

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
	 * Start the SleeContainer and initialize the necessary resources
	 * 
	 */
	protected void startSleeContainer() throws Exception {

		changeSleeState(SleeState.STARTING);
		boolean created = sleeContainer.getTransactionManager()
				.requireTransaction();
		boolean rollback = true;
		try {
			// (Ivelin) the following check is symmetric to the one is stop().
			// see the comments in stop() for more detail.
			if (isFullSleeStop) {
				sleeContainer.init(this, rmiServerInterfaceMBean);
				isFullSleeStop = false;
			} else {
				// re config event router
				sleeContainer.getEventRouter().config(
						MobicentsManagement.eventRouterExecutors,
						MobicentsManagement.monitoringUncommittedAcAttachs);
			}
			changeSleeState(SleeState.RUNNING);
			startResourceAdaptors();
			startServiceActivities();
			startProfileTableActivities();
			rollback = false;
		} catch (Exception ex) {
			logger.error("Error starting SLEE container", ex);
			throw ex;
		} finally {
			boolean started = false;
			try {
				if (created) {
					if (rollback) {
						sleeContainer.getTransactionManager().rollback();
					} else {
						sleeContainer.getTransactionManager().commit();
						started = true;
					}
				} else {
					started = true;
				}

			} catch (Exception e) {
				logger
						.error(
								"Error finishing transaction on SLEE container startup",
								e);
			}
			// if startup did not succeed, try to clean up resources
			if (sleeContainer.getSleeState() != SleeState.RUNNING || !started)
				stopSleeContainer();
		}
	}

	private void startResourceAdaptors() {
		// inform all ra entities that we are starting the container
		final ResourceManagement resourceManagement = sleeContainer
				.getResourceManagement();
		for (String entityName : resourceManagement
				.getResourceAdaptorEntities()) {
			try {
				resourceManagement.getResourceAdaptorEntity(entityName)
						.sleeRunning();
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}

	}

	private void startServiceActivities() throws NullPointerException,
			UnrecognizedServiceException, ManagementException, SystemException {
		sleeContainer.getServiceManagement().startActiveServicesActivities();
	}

	/**
	 * Stop the service container and clean up the resources it used.
	 * 
	 */
	protected void stopSleeContainer() throws Exception {

		changeSleeState(SleeState.STOPPING);

		logger.info(generateMessageWithLogo("stopping"));

		// (Ivelin)
		// If the stop() is invoked externally, skip further effort to stop all
		// services
		// only if the stop() is invoked as a result of undeployment of
		// mobicents.sar, then stop everything
		// the reason we need to do this is because MBean service dependecy is
		// not taken into account
		// with external stop(),start() invocation and as a result the start()
		// after stop() is not clean.
		//
		// The value of isFullSleeStop is controlled by SleeLifecycleMonitor

		if (isFullSleeStop) {
			sleeContainer.close();
			// we do not want STOPPED state, because that is not desired
			// in a cluster situation and redeployment.
			// Only when the server is really crashing on all nodes, STOPPED
			// state
			// makes sense, but at that point it is not as relevant.
			// changeSleeState(SleeState.STOPPED);

			// Still, we want an indication that the container service concluded
			// stopping cycle
			logger.info(generateMessageWithLogo("stopped (HA)"));
		} else {
			scheduleStopped();
		}
	}

	private void endAllServiceAndProfileTableActivities() {
		
		logger.info("Ending all service and profile table activities...");
		
		boolean rb = true;
		try {
			
			sleeTransactionManager.begin();
			
			// end all service and profile table activities
			for (ActivityContextHandle handle : sleeContainer
					.getActivityContextFactory()
					.getAllActivityContextsHandles()) {
				if (handle.getActivityType() == ActivityType.SERVICE
						|| handle.getActivityType() == ActivityType.PTABLE) {
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("Ending activity " + handle);
						}
						ActivityContext ac = sleeContainer
								.getActivityContextFactory()
								.getActivityContext(handle);
						if (ac != null) {
							ac.endActivity();
						}
					} catch (Exception e) {
						if (logger.isDebugEnabled()) {
							logger.debug("Failed to end activity "
									+ handle, e);
						}
					}
				}
			}
								
			rb = false;
		} catch (Exception e) {
			logger
					.error(
							"Exception while ending all service and profile table activities",
							e);

		} finally {
			try {
				if (rb) {
					sleeTransactionManager.rollback();
				} else {
					sleeTransactionManager.commit();
				}
			} catch (Exception e) {
				logger
						.error(
								"Error in tx management while ending all service and profile table activities",
								e);
			}
		}
		
		// wait all activites end
		
		boolean loop;
		do {
			loop = false;
			
			try {
				sleeTransactionManager.begin();
				for (ActivityContextHandle handle : sleeContainer
						.getActivityContextFactory()
						.getAllActivityContextsHandles()) {
					if (handle.getActivityType() == ActivityType.SERVICE
							|| handle.getActivityType() == ActivityType.PTABLE) {
						logger.info("Waiting for activity "+handle+" to end...");
						loop = true;
						break;
					}
				}
			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
			finally {
				try {
					sleeTransactionManager.rollback();
				} catch (Exception e) {
					logger.error("Error in tx management while stopping SLEE",e);
				}
			}
			if (loop) {
				try {
					// wait a sec
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		} while (loop);
		
	}
	
	private void stopResourceAdaptors() {
		
		logger.info("Stopping all active resource adaptors ...");
		
		final ResourceManagement resourceManagement = sleeContainer
		.getResourceManagement();
		
		boolean rb = true;
		try {
			
			sleeTransactionManager.begin();
			
			// inform all ra entities that we are stopping the container
			for (String entityName : resourceManagement
					.getResourceAdaptorEntities()) {
				try {
					resourceManagement.getResourceAdaptorEntity(entityName)
							.sleeStopping();
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}
								
			rb = false;
		} catch (Exception e) {
			logger
					.error(
							"Exception while stopping resource adaptors",
							e);

		} finally {
			try {
				if (rb) {
					sleeTransactionManager.rollback();
				} else {
					sleeTransactionManager.commit();
				}
			} catch (Exception e) {
				logger
						.error(
								"Error in tx management while stopping resource adaptors",
								e);
			}
		}

		// wait till all ra entity objects are stopped
		boolean loop;
		do {
			loop = false;
			for (String entityName : resourceManagement
					.getResourceAdaptorEntities()) {
				try {
					if (resourceManagement.getResourceAdaptorEntity(
							entityName).getResourceAdaptorObject()
							.getState() == ResourceAdaptorObjectState.STOPPING) {
						logger.info("Waiting for ra entity "+entityName+" to stop...");
						loop = true;
					}
				} catch (Exception e) {
					if (logger.isDebugEnabled()) {
						logger.debug(e.getMessage(), e);
					}
				}
			}
			if (loop) {
				try {
					// wait a sec
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
			}
		} while (loop);
	}
	
	/**
	 * Setup a polling process to wait until all ActivityContexts ended. Then
	 * set the SLEE container in STOPPED state.
	 * 
	 */
	protected void scheduleStopped() {

		if (logger.isDebugEnabled()) {
			logger.debug("schedule stopped");
		}
		
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Runnable acStateChecker = new Runnable() {
			public void run() {
				endAllServiceAndProfileTableActivities();
				stopResourceAdaptors();
				changeSleeState(SleeState.STOPPED);
			}
		};

		try {
			// if jboss as is shutting down then we wait till servers stops
			Boolean inShutdown = (Boolean) mbeanServer.getAttribute(
					new ObjectName("jboss.system:type=Server"), "InShutdown");
			Future future = exec.submit(acStateChecker);
			if (inShutdown) {
				future.get();
			}
		} catch (Exception e) {
			logger
					.error(
							"Failed scheduling polling task for STOPPED state. The SLEE Container may remain in STOPPING state.",
							e);
		}

	}

	private void startProfileTableActivities()
			throws ActivityAlreadyExistsException {
		if (logger.isDebugEnabled()) {
			logger.debug("starting all profile table activities");
		}
		sleeContainer.getSleeProfileTableManager()
				.startAllProfileTableActivities();
	}

	/**
	 * Changes the SLEE container state and emits JMX notifications
	 * 
	 * @param newState
	 */
	protected void changeSleeState(SleeState newState) {
		SleeState oldState = sleeContainer.getSleeState();
		sleeContainer.setSleeState(newState);
		notificationBroadcaster
				.sendNotification(new SleeStateChangeNotification(this,
						newState, oldState, sleeStateChangeSequenceNumber++));

		if (newState == SleeState.RUNNING) {
			String timerSt = "";
			if (isFullSleeStop) {
				startupTime = System.currentTimeMillis() - startupTime;
				long startupSec = startupTime / 1000;
				long startupMillis = startupTime % 1000;
				timerSt = "in " + startupSec + "s:" + startupMillis + "ms ";
			}
			logger.info(generateMessageWithLogo("started " + timerSt));

		} else if (newState == SleeState.STOPPED) {
			logger.info(generateMessageWithLogo("stopped"));
		}
	}

	public void setFullSleeStop(boolean b) {
		isFullSleeStop = b;
	}

	/**
	 * Indicates whether Mobicents SLEE is simply marked as STOPPED or all its
	 * services actually stopped. The distinction is important due to the way
	 * MBean service dependencies are handled on mobicents.sar undeploy vs.
	 * externally calling SleeManagementMBean.stop()
	 */
	public boolean isFullSleeStop() {
		return isFullSleeStop;
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

	public SleeTransactionManager getSleeTransactionManager() {
		return sleeTransactionManager;
	}

	public void setSleeTransactionManager(
			SleeTransactionManager sleeTransactionManager) {
		this.sleeTransactionManager = sleeTransactionManager;
	}

	public MobicentsCache getMobicentsCache() {
		return mobicentsCache;
	}

	public void setMobicentsCache(MobicentsCache mobicentsCache) {
		this.mobicentsCache = mobicentsCache;
	}

	// ah ah

	private static final String rLogo = " ><><><><><><><><>< ";
	private static final String lLogo = rLogo;

	private String generateMessageWithLogo(String message) {
		return lLogo + getSleeName() + ' ' + getSleeVersion() + ' ' + message
				+ rLogo;
	}

	public String getSleeName() {
		String name = Version.instance.getProperty("name");
		if (name != null) {
			return name;
		} else {
			return "Mobicents JAIN SLEE Server";
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

}
