/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.Version;
import org.mobicents.slee.container.management.SleeStateChangeRequest;

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

/**
 * Implementation of the Slee Management MBean for SLEE 1.1 specs
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author Eduardo Martins
 * @author <a href="mailto:info@pro-ids.com">ProIDS sp. z o.o.</a>
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

	/**
	 * Graceful shutdown feature defaults config.
	 */
	private int defaultActiveSessionsThreshold;
	private long defaultGracefulShutdownWaitTime;

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
	public SleeManagementMBeanImpl(SleeContainer sleeContainer)
			throws NotCompliantMBeanException {
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
			// request to change to STARTING
			final SleeStateChangeRequest startingRequest = new SleeStateChangeRequest() {

				@Override
				public void stateChanged(SleeState oldState) {
					if(logger.isDebugEnabled()) {
						logger.debug(generateMessageWithLogo("starting"));
					}
					notifyStateChange(oldState, getNewState());
				}

				@Override
				public void requestCompleted() {
					// inner request, executed when the parent completes, to change to RUNNING
					final SleeStateChangeRequest runningRequest = new SleeStateChangeRequest() {

						private SleeState oldState;

						@Override
						public void stateChanged(SleeState oldState) {
							logger.info(generateMessageWithLogo("started"));
							this.oldState = oldState;
						}

						@Override
						public void requestCompleted() {
							notifyStateChange(oldState, getNewState());
						}

						@Override
						public boolean isGraceful() {
							return false;
						}

						@Override
						public boolean isBlockingRequest() {
							return true;
						}

						@Override
						public SleeState getNewState() {
							return SleeState.RUNNING;
						}
					};
					try {
						sleeContainer.setSleeState(runningRequest);
					} catch (Throwable e) {
						logger.error(
								"Failed to set container in RUNNING state", e);
						try {
							stop(false, false);
						} catch (Throwable f) {
							logger.error(
									"Failed to set container in STOPPED state, after failure to set in RUNNING state",
									e);
						}
					}
				}

				@Override
				public boolean isGraceful() {
					return false;
				}

				@Override
				public boolean isBlockingRequest() {
					// should be false, but the tck doesn't like it
					return true;
				}

				@Override
				public SleeState getNewState() {
					return SleeState.STARTING;
				}
			};
			sleeContainer.setSleeState(startingRequest);

		} catch (InvalidStateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	private void notifyStateChange(SleeState oldState, SleeState newState) {
		if (logger.isTraceEnabled()) {
			logger.trace("notifyStateChange( old = " + oldState + " , new = "		
				+ newState + " )");
		}
		notificationBroadcaster
				.sendNotification(new SleeStateChangeNotification(this,
						newState, oldState, sleeStateChangeSequenceNumber++));
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		SleeManagementMBeanImpl.logger = logger;
	}

	public int getDefaultActiveSessionsThreshold() {
		return defaultActiveSessionsThreshold;
	}

	public void setDefaultActiveSessionsThreshold(int defaultActiveSessionsThreshold) {
		this.defaultActiveSessionsThreshold = defaultActiveSessionsThreshold;
		if(this.sleeContainer != null) {
			this.sleeContainer.setGracefulStopActivitiesCountThreshold(defaultActiveSessionsThreshold);
		}
	}

	public long getDefaultGracefulShutdownWaitTime() {
		return defaultGracefulShutdownWaitTime;
	}

	public void setDefaultGracefulShutdownWaitTime(long defaultGracefulShutdownWaitTime) {
		this.defaultGracefulShutdownWaitTime = defaultGracefulShutdownWaitTime;
		if(this.sleeContainer != null) {
			this.sleeContainer.setGracefulStopWaitTime(defaultGracefulShutdownWaitTime);
		}
	}

	/**
	 * Gracefully stop the SLEE. Should do it in a non-blocking manner.
	 * Requests SLEE container to enter graceful STOPPING state.
	 * @return Status and the number of active sessions remaining after invocation of Graceful Shutdown command
	 */
	public String gracefulStop(Integer ast, Long time) throws InvalidStateException, ManagementException {
		if(ast != null && ast >= 0){
			sleeContainer.setGracefulStopActivitiesCountThreshold(ast);
		} else {
			logger.info("Using default defaultActiveSessionsThreshold value for graceful shutdown (" + defaultActiveSessionsThreshold + ")" );
			sleeContainer.setGracefulStopActivitiesCountThreshold(defaultActiveSessionsThreshold);
		}
		if(time != null && time > 0) {
			sleeContainer.setGracefulStopWaitTime(time);
		} else {
			logger.info("Using default defaultGracefulShutdownWaitTime value for graceful shutdown (" + defaultGracefulShutdownWaitTime + ")");
			sleeContainer.setGracefulStopWaitTime(defaultGracefulShutdownWaitTime);
		}

		logger.info("gracefulStop called. ast=" + sleeContainer.getGracefulStopActivitiesCountThreshold() + " time=" + sleeContainer.getGracefulStopWaitTime());

		stop(false, true);

		return "Container Graceful Shutdown requested successfully.";
	}

	/**
	 * Stop the SLEE. Should do it in a non-blocking manner.
	 * 
	 * @see javax.slee.management.SleeManagementMBean#stop()
	 */
	public void stop() throws InvalidStateException, ManagementException {
		stop(false, false);
	}

	private void stop(final boolean block, final boolean graceful) throws InvalidStateException,
			ManagementException {
		try {
			SleeStateChangeRequest stoppingRequest = null;
			if(graceful) {
				stoppingRequest = createGracefulStoppingRequest(block);
			} else {
				stoppingRequest = createStoppingRequest(block);

			}
			sleeContainer.setSleeState(stoppingRequest);

		} catch (InvalidStateException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ManagementException(ex.getMessage(), ex);
		}
	}

	private SleeStateChangeRequest createGracefulStoppingRequest(final boolean block) {
		// request to change to STOPPING (gracefully)
		final SleeStateChangeRequest gracefulStoppingRequest = new SleeStateChangeRequest() {

			@Override
			public void stateChanged(SleeState oldState) {
				logger.info(generateMessageWithLogo("gracefully stopping"));
				notifyStateChange(oldState, getNewState());
			}

			@Override
			public void requestCompleted() {
				// inner request, executed when the parent completes, to stop other containers modules
				if(logger.isInfoEnabled()) {
					logger.info("Graceful stopping request completed. Stopping whole container.");
				}

				final SleeStateChangeRequest stoppingRequest = createStoppingRequest(block);
				try {
					sleeContainer.setSleeState(stoppingRequest);
				} catch (Throwable e) {
					logger.error("Failed to set whole container in STOPPING state", e);
				}
			}

			@Override
			public boolean isGraceful() {
				return true;
			}

			@Override
			public boolean isBlockingRequest() {
				return block;
			}

			@Override
			public SleeState getNewState() {
				return SleeState.STOPPING;
			}
		};
		return gracefulStoppingRequest;
	}

	private SleeStateChangeRequest createStoppingRequest(final boolean block) {
			// request to change to STOPPING
			final SleeStateChangeRequest stoppingRequest = new SleeStateChangeRequest() {

				@Override
				public void stateChanged(SleeState oldState) {
					logger.info(generateMessageWithLogo("stopping"));
					notifyStateChange(oldState, getNewState());
				}

				@Override
				public void requestCompleted() {
					// inner request, executed when the parent completes, to change to STOPPED
					final SleeStateChangeRequest stopRequest = createStopRequest();
					try {
						sleeContainer.setSleeState(stopRequest);
					} catch (Throwable e) {
						logger.error("Failed to set container in STOPPED state", e);
					}
				}

				@Override
				public boolean isGraceful() {
					return false;
				}

				@Override
				public boolean isBlockingRequest() {
					return block;
				}

				@Override
				public SleeState getNewState() {
					return SleeState.STOPPING;
				}
			};
		return stoppingRequest;
	}

	private SleeStateChangeRequest createStopRequest() {
		// request to change to STOPPED
		final SleeStateChangeRequest stopRequest = new SleeStateChangeRequest() {

			private SleeState oldState;

			@Override
			public void stateChanged(SleeState oldState) {
				logger.info(generateMessageWithLogo("stopped"));
				this.oldState = oldState;
			}

			@Override
			public void requestCompleted() {
				notifyStateChange(oldState, getNewState());
			}

			@Override
			public boolean isGraceful() {
				return false;
			}

			@Override
			public boolean isBlockingRequest() {
				return true;
			}

			@Override
			public SleeState getNewState() {
				return SleeState.STOPPED;
			}
		};
		return stopRequest;
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
		logger.info(generateMessageWithLogo("shutdown"));
		try {
			sleeContainer.shutdownSlee();
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
	 * @see javax.slee.management.SleeManagementMBean
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
	 * @see javax.slee.management.SleeManagementMBean
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

	private static final String rLogo = " ## ## ## ## ## ## ## ";
	private static final String lLogo = rLogo;

	private String generateMessageWithLogo(String message) {
		return lLogo + getSleeName() + " " + getSleeVersion() + " \""
				+ getSleeCodeName() + "\" " + message + rLogo;
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
		logger.info(generateMessageWithLogo("init"));
		try {
			sleeContainer.initSlee();
			start();
		} catch (Exception e) {
			logger.error("Failure in SLEE startup", e);
		}
	}

	public void stopSlee() {
		try {
			stop(true, false);
			shutdown();
		} catch (Exception e) {
			logger.error("Failed in SLEE stop", e);
		}
	}

}
