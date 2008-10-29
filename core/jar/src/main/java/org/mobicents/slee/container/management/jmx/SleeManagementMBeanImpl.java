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
import java.util.HashMap;
import java.util.Iterator;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.management.ServiceState;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;
import javax.slee.management.SleeStateChangeNotification;
import javax.slee.resource.ResourceException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.SleeEndpointImpl;
import org.mobicents.slee.runtime.facilities.ProfileTableActivityImpl;

import EDU.oswego.cs.dl.util.concurrent.ThreadedExecutor;

/**
 * Implementation of the Slee Management MBean See SLEE 1.0 spec, section 14.6.2
 * SleeManagementMBean inteface
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
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

	private ObjectName objectName;

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

	/**
	 * Holds the startup time of the Mobicents SAR. Assumes that this MBean is
	 * instantiated first and started last within the scope of Mobicents.sar
	 */
	private long startupTime;

	private boolean isFullSleeStop = true;

	private final String mobicentsVersion = Version.instance.toString();
	/**
	 * List of services, which were active immediately before stop. They need to
	 * be remembered (TODO: persisted!) and resumed on start.
	 */
	private ServiceID[] activeServicesBeforeStop;

	/**
	 * list of active RAEntities - before stop, these need to be activated after
	 * start. List must be persited somewhere - TODO
	 */
	private ArrayList<String> activeRAEntities = new ArrayList<String>();

	static {
		MBEAN_NOTIFICATIONS = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				new String[] { SleeStateChangeNotification.class.getName() },
				SleeManagementMBean.SLEE_STATE_CHANGE_NOTIFICATION_TYPE,
				"SLEE 1.0 Spec, #14.6, Each time the operational state of the SLEE changes, "
						+ "the SleeManagementMBean object generates a SLEE state change notification.") };
		try {
			logger = Logger.getLogger(SleeManagementMBeanImpl.class);
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
		logger.info("[[[[[[[[[ " + mobicentsVersion + " starting... ]]]]]]]]]");
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
			throw new ManagementException(ex.getMessage());
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
	 * @see javax.slee.management.SleeManagementMBean#getProfileProvisioningMBean()
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
		this.sleeContainer = new SleeContainer(mbeanServer);

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
	 * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener,
	 *      javax.management.NotificationFilter, java.lang.Object)
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
	 * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
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
		try {
			// (Ivelin) the following check is symmetric to the one is stop().
			// see the comments in stop() for more detail.
			if (isFullSleeStop) {
				sleeContainer.init(this, rmiServerInterfaceMBean);
				isFullSleeStop = false;
			}
			;

			changeSleeState(SleeState.RUNNING);
			reactivateResourceAdaptors();
			resumeServicesActiveBeforeStop();
		} catch (Exception ex) {
			logger.error("Error starting SLEE container", ex);
			throw ex;
		} finally {
			// if startup did not succeed, try to clean up resources
			if (sleeContainer.getSleeState() != SleeState.RUNNING)
				stopSleeContainer();
		}
	}

	private void reactivateResourceAdaptors() {

		final ResourceManagement resourceManagement = sleeContainer.getResourceManagement();
		ArrayList<String> activated = new ArrayList<String>();
		for (String entityName : this.activeRAEntities) {
			ResourceAdaptorEntity entity = null;
			try {
				entity = resourceManagement.getResourceAdaptorEntity(entityName);
			}
			catch (Exception e) {
				logger.error("failed to get ra entity "+entityName,e);
			}
			if (entity != null) {
				try {
					entity.activate();
				} catch (ResourceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// this.activeRAEntities.remove(entityName);
			activated.add(entityName);
		}

		this.activeRAEntities.removeAll(activated);
	}

	/**
	 * Activate services, which were active before SLEE stopped
	 * 
	 * @throws InvalidStateException
	 * @throws UnrecognizedServiceException
	 * 
	 */
	private void resumeServicesActiveBeforeStop()
			throws UnrecognizedServiceException, InvalidStateException {
		if (activeServicesBeforeStop != null) {
			for (int i = 0; i < activeServicesBeforeStop.length; i++) {
				try {

					sleeContainer
							.reviveAndStartService(activeServicesBeforeStop[i]);
				} catch (UnrecognizedServiceException e) {
					logger.info("Service was removed while SLEE was inactive: "
							+ activeServicesBeforeStop[i]);
				} catch (Exception e) {
					logger.error("Failed starting service "
							+ activeServicesBeforeStop[i], e);
				}
			}
		}
	}

	/**
	 * Stop the service container and clean up the resources it used.
	 * 
	 */
	protected void stopSleeContainer() throws Exception {
		changeSleeState(SleeState.STOPPING);

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

		rememberActiveServicesBeforeStop();

		rememberActiveResourceAdaptorsBeforeStop();

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
			logger.info("[[[[[[[[[[ MOBICENTS " + mobicentsVersion
					+ " Stopped (HA). ]]]]]]]]]");
		} else {
			scheduleStopped();
		}
	}

	/**
	 * Setup a polling process to wait until all ActivityContexts ended. Then
	 * set the SLEE container in STOPPED state.
	 * 
	 */
	protected void scheduleStopped() {

		ThreadedExecutor exec = new ThreadedExecutor();
		Runnable acStateChecker = new Runnable() {

			public void run() {

				boolean rb = true;
				try {

					SleeContainer.getTransactionManager().begin();

					try {
						// wait a little because test 1479
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}

					stopAllServices();
					stopAllResourceAdaptors();
					stopAllProfileTableActivities();

					// are there any other possible activities that have not
					// been terminated?
					// if so, force termination
					SleeEndpointImpl.allActivitiesEnding();

					// ...and wait until they all end

					// force some delay, because the TCK relies on time to test
					// behaviour in STOPPING state
					// see test
					// tests/management/sleestate/SleeStateMachineTest.xml
					// 
					/*
					 * try { try { Thread.sleep(2000); } catch
					 * (InterruptedException e1) { // TODO Auto-generated catch
					 * block e1.printStackTrace(); }
					 */
					rb = false;
				} catch (Exception e) {
					logger.error("Exception while stopping SLEE", e);

				} finally {
					try {
						if (rb) {
							SleeContainer.getTransactionManager().rollback();
						} else {
							SleeContainer.getTransactionManager().commit();
							// FIXME we should wait for all activities to end,
							// do this once all leaks are fixed
							while (!areAllServicesInvalid()) {
								logger
										.info("Waiting on all Activities and Services  to end before setting the SLEE container in STOPPED state...");
								try {
									// wait a little before retry
									Thread.sleep(1000);
								} catch (InterruptedException e) {
								}
							}
							changeSleeState(SleeState.STOPPED);
						}
					} catch (SystemException e) {
						logger
								.error(
										"Error in tx management while stopping SLEE",
										e);
					}
				}
			}

		};

		try {
			exec.execute(acStateChecker);
		} catch (InterruptedException e) {
			logger
					.error("Failed scheduling polling task for STOPPED state. The SLEE Container may remain in STOPPING state.");
		}
	}

	/**
	 * 
	 */
	private void stopAllProfileTableActivities() {
		SleeProfileManager sleeProfileManager = SleeProfileManager
				.getInstance();
		HashMap profileTableActivities = sleeProfileManager
				.getProfileTableActivities();
		logger.debug("Stopping profile table activities !");
		Iterator it = profileTableActivities.keySet().iterator();
		while (it.hasNext()) {
			String profileTableName = (String) it.next();
			logger.debug("Stopping following profile table activity : "
					+ profileTableName);
			SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
			int eventID = sleeContainer.getEventLookupFacility().getEventID(
					new ComponentKey("javax.slee.ActivityEndEvent",
							"javax.slee", "1.0"));

			ProfileTableActivityImpl profileTableActivity = (ProfileTableActivityImpl) profileTableActivities
					.get(profileTableName);
			sleeContainer.getSleeEndpoint().scheduleActivityEndedEvent(
					profileTableActivity);
		}
	}

	/**
	 * 
	 * Deactivate all active services in preparation for moving to the STOPPED
	 * state. See SLEE #14.6.1
	 * 
	 */
	private void stopAllServices() {
		// stop all services
		ServiceID[] services = null;
		try {
			services = sleeContainer.getServicesByState(ServiceState.ACTIVE);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		for (int i = 0; i < services.length; i++) {
			try {
				sleeContainer.stopService(services[i]);
			} catch (Exception e1) {
				logger.debug("Failed to schedule SLEE end", e1);
			}
		}
	}

	/**
	 * 
	 * Deactivate all active resource adaptors in preparation for moving to the
	 * STOPPED state. See SLEE #14.6.1
	 * 
	 */
	private void stopAllResourceAdaptors() {

		final ResourceManagement resourceManagement = sleeContainer.getResourceManagement();
		for (String entityName : resourceManagement.getResourceAdaptorEntities()) {
			ResourceAdaptorEntity entity = null;
			
			try {
				entity = resourceManagement.getResourceAdaptorEntity(entityName);
			} catch (Exception e) {
				logger.error("failed to get RA entity "+entityName,e);
			}
			
			if (entity != null && !entity.getName().equals("tck")
					&& entity.getState().equals(
							ResourceAdaptorEntityState.ACTIVE))
				try {

					entity.deactivate();
				} catch (InvalidStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * @param activeServices
	 * @return
	 */
	private boolean areAllServicesInvalid() {
		boolean b = SleeContainer.getTransactionManager().requireTransaction();
		boolean rb = true;
		try {
			int invalidServices = sleeContainer
					.getServicesByState(ServiceState.INACTIVE).length;
			int allServices = sleeContainer.getDeploymentManager()
					.getServiceComponents().size();
			rb = false;
			return (invalidServices == allServices);
		} catch (Exception e) {
			logger.info("Failed areAllServicesInvalid() " + e.getMessage());
		} finally {
			try {
				if (rb)
					SleeContainer.getTransactionManager().setRollbackOnly();
				if (b)
					SleeContainer.getTransactionManager().commit();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				logger.info("Failed areAllServicesInvalid() ", e1);
			}
		}
		return false;
	}

	/**
	 * 
	 * Remembers in memory active services so that they can be reactivated on
	 * start. (NOTE: the list should be persisted info according to SLEE spec
	 * #2.2.17)
	 * 
	 */
	private void rememberActiveServicesBeforeStop() throws Exception {
		activeServicesBeforeStop = sleeContainer
				.getServicesByState(ServiceState.ACTIVE);
	}

	/**
	 * 
	 * Remembers in memory active Resource Adaptors so that they can be
	 * reactivated on start. (NOTE: the list should be persisted info according
	 * to SLEE spec #14.6.1, #2.2.17)
	 * 
	 */
	private void rememberActiveResourceAdaptorsBeforeStop() {
		final ResourceManagement resourceManagement = sleeContainer.getResourceManagement();
		for (String entityName : resourceManagement.getResourceAdaptorEntities()) {
			ResourceAdaptorEntity entity = null;
			try {
				entity = resourceManagement
						.getResourceAdaptorEntity(entityName);
			} catch (Exception e) {
				logger.error("failed to get Ra entity " + entityName, e);
			}
			if (entity != null && !entityName.equals("tck")
					&& entity.getState().equals(
							ResourceAdaptorEntityState.ACTIVE))
				this.activeRAEntities.add(entityName);

		}

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

		if (newState.equals(SleeState.RUNNING)) {
			String timerSt = "";
			if (isFullSleeStop) {
				startupTime = System.currentTimeMillis() - startupTime;
				long startupSec = startupTime / 1000;
				long startupMillis = startupTime % 1000;
				timerSt = "in " + startupSec + "s:" + startupMillis + "ms ";
			}
			logger.info("[[[[[[[[[ " + mobicentsVersion + " Started "
					+ timerSt + "]]]]]]]]]");

		} else if (newState.equals(SleeState.STOPPED)) {
			logger.info("[[[[[[[[[[ " + mobicentsVersion
					+ " Stopped ]]]]]]]]]");
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

}
