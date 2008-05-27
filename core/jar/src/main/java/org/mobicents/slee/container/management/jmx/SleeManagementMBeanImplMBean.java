/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on Feb 9, 2005                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.MBeanRegistration;
import javax.management.NotificationBroadcaster;
import javax.management.ObjectName;
import javax.slee.InvalidStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;

/**
 * @author Ivelin Ivanov
 * 
 */
public interface SleeManagementMBeanImplMBean extends SleeManagementMBean,
		MBeanRegistration, NotificationBroadcaster {
	/**
	 * @return the current state of the SLEE Container
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getState()
	 */
	public abstract SleeState getState() throws ManagementException;

	/**
	 * Start the SLEE container
	 * 
	 * @see javax.slee.management.SleeManagementMBean#start()
	 */
	public abstract void start() throws InvalidStateException,
			ManagementException;

	/**
	 * Gracefully stop the SLEE. Should do it in a non-blocking manner.
	 * 
	 * @throws Exception
	 * 
	 * @see javax.slee.management.SleeManagementMBean#stop()
	 */
	public abstract void stop() throws InvalidStateException,
			ManagementException;

	/**
	 * Shutdown the SLEE processes. The spec requires that System.exit() be
	 * called before this methods returns. We are not convinced this is
	 * necessary yet. A trivial implementation would be to make a call to the
	 * JBoss server shutdown()
	 * 
	 * @see javax.slee.management.SleeManagementMBean#shutdown()
	 */
	public abstract void shutdown() throws InvalidStateException,
			ManagementException;

	/**
	 * return the ObjectName of the DeploymentMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getDeploymentMBean()
	 */
	public abstract ObjectName getDeploymentMBean();

	/**
	 * set the ObjectName of the DeploymentMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getDeploymentMBean()
	 */
	public abstract void setDeploymentMBean(ObjectName newDM);

	/**
	 * return the ObjectName of the ServiceManagementMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getServiceManagementMBean()
	 */
	public abstract ObjectName getServiceManagementMBean();

	/**
	 * set the ObjectName of the ServiceManagementMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setServiceManagementMBean()
	 */
	public abstract void setServiceManagementMBean(ObjectName newSMM);

	/*
	 * return the ObjectName of the ProfileProvisioningMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getProfileProvisioningMBean()
	 */public abstract ObjectName getProfileProvisioningMBean();

	/**
	 * set the ObjectName of the ProfileProvisioningMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setProfileProvisioningMBean()
	 */
	public abstract void setProfileProvisioningMBean(ObjectName newPPM);

	/*
	 * return the ObjectName of the TraceMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getTraceMBean()
	 */
	public abstract ObjectName getTraceMBean();

	/**
	 * set the ObjectName of the TraceMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setTraceMBean()
	 */
	public abstract void setTraceMBean(ObjectName newTM);

	/**
	 * return the ObjectName of the AlarmMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#getAlarmMBean()
	 */
	public abstract ObjectName getAlarmMBean();

	/**
	 * set the ObjectName of the AlarmMBean
	 * 
	 * @see javax.slee.management.SleeManagementMBean#setAlarmMBean()
	 */
	public abstract void setAlarmMBean(ObjectName newAM);

	public void setFullSleeStop(boolean b);

	/**
	 * Indicates whether Mobicents SLEE is simply marked as STOPPED or all its
	 * services actually stopped. The distinction is important due to the way
	 * MBean service dependencies are handled on mobicents.sar undeploy vs.
	 * externally calling SleeManagementMBean.stop()
	 */
	public boolean isFullSleeStop();

	// /**
	// * @param resourceAdaptorMBean The resourceAdaptorMBean to set.
	// * @deprecated Use setResourceManagementMBean
	// */
	// public void setResourceAdaptorMBean(ObjectName resourceAdaptorMBean);

	// /**
	// * @return Returns the resourceAdaptorMBean.
	// * @deprecated Use getResourceManagementMBean
	// */
	// public ObjectName getResourceAdaptorMBean();

	/**
	 * @param resourceManagementMBean
	 *            The resourceManagementMBean to set.
	 */
	public void setResourceManagementMBean(ObjectName resourceManagementMBean);

	/**
	 * @return Returns the resourceManagementMBean.
	 */
	public ObjectName getResourceManagementMBean();

	/**
	 * @return activityManagementMBean()
	 */
	public ObjectName getActivityManagementMBean();

	/**
	 * @return sbbEntitiesMBean
	 */
	public ObjectName getSbbEntitiesMBean();

	public void setSbbEntitiesMBean(ObjectName sbbEntitiesMBean);

	public void setActivityManagementMBean(ObjectName activityManagementMBean);

	/**
	 * 
	 * @return string representation of container's version.
	 */
	public String getVersion();

	public ObjectName getRmiServerInterfaceMBean();

	public void setRmiServerInterfaceMBean(ObjectName rmiServerInterfaceMBean);
}