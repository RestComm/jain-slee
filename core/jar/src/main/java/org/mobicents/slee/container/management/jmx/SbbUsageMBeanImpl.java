/*
 * SbbUsageMBeanImpl.java
 * 
 * Created on Jan 12, 2005
 * 
 * Created by: M. Ranganathan
 *
 * The Mobicents Open SLEE project
 * 
 * A SLEE for the people!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, 
 * AND DATA ACCURACY.  We do not warrant or make any representations 
 * regarding the use of the software or the  results thereof, including 
 * but not limited to the correctness, accuracy, reliability or 
 * usefulness of the software.
 */

package org.mobicents.slee.container.management.jmx;

import java.util.Iterator;
import java.util.List;

import javax.management.ListenerNotFoundException;
import javax.management.MBeanNotificationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ManagementException;
import javax.slee.usage.SbbUsageMBean;
import javax.slee.usage.UsageNotification;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.InstalledUsageParameterSet;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArrayList;

/**
 * SbbUsageMBeanImpl -- implementation of the SbbUsageMBean
 * 
 * @author M. Ranganathan
 * 
 */
public class SbbUsageMBeanImpl extends StandardMBean implements SbbUsageMBean,
		NotificationBroadcaster {

	private SbbID sbbId;

	private String name;

	private List listeners;

	private InstalledUsageParameterSet usagePramSet;

	private ServiceID serviceId;

	/*
	 * static { logger = Logger.getLogger(SbbUsageMBeanImpl.class); }
	 */
	public SbbUsageMBeanImpl(String interfaceName)
			throws NotCompliantMBeanException, ClassNotFoundException {
		super(SleeContainerUtils.getCurrentThreadClassLoader().loadClass(
				interfaceName));
		listeners = new CopyOnWriteArrayList();

	}

	class ListenerFilterHandbackTriplet {
		NotificationListener notificationListener;

		NotificationFilter notificationFilter;

		Object handbackObject;

		public ListenerFilterHandbackTriplet(NotificationListener listener,
				NotificationFilter notificationFilter, Object handbackObject) {
			this.notificationListener = listener;
			this.notificationFilter = notificationFilter;
			this.handbackObject = handbackObject;
		}
	}

	public SbbUsageMBeanImpl(ServiceID serviceID, SbbID sbbId, String name,
			String interfaceName) throws NotCompliantMBeanException,
			ClassNotFoundException {
		this(interfaceName);
		this.serviceId = serviceID;
		this.sbbId = sbbId;
		this.name = name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.usage.SbbUsageMBean#getService()
	 */
	public ServiceID getService() throws ManagementException {

		return this.serviceId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.usage.SbbUsageMBean#getSbb()
	 */
	public SbbID getSbb() throws ManagementException {
		return this.sbbId;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.usage.SbbUsageMBean#getUsageParameterSet()
	 */
	public String getUsageParameterSet() throws ManagementException {
		return name;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.usage.SbbUsageMBean#close()
	 */
	public void close() throws InvalidStateException, ManagementException {
		if (listeners.size() != 0) {
			throw new InvalidStateException(
					"Could not close Usage MBean listeners still attached!");
		}
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.usage.SbbUsageMBean#resetAllUsageParameters()
	 */
	public void resetAllUsageParameters() throws ManagementException {
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		SleeTransactionManager txmgr = sleeContainer.getTransactionManager();
		boolean rb = true;
		try {
			txmgr.begin();
			ServiceComponent service = sleeContainer.getServiceManagement()
					.getServiceComponent(serviceId);

			try {
				String[] paramNames = service
						.getNamedUsageParameterSets(this.sbbId);

				for (int i = 0; paramNames != null && i < paramNames.length; i++) {
					InstalledUsageParameterSet ups = Service
							.getNamedUsageParameter(this.serviceId, sbbId, name);
					if (ups != null)
						ups.reset();
				}
			} catch (InvalidArgumentException ex) {
				// This is ok because the service may not have
				// any named usage parameters and getNamedUsageParameterSets
				// will
				// throw exception at this point.

			}
			InstalledUsageParameterSet ups = Service
					.getDefaultUsageParameterSet(this.serviceId, this.sbbId);
			if (ups != null)
				ups.reset();
			rb = false;

		} catch (Exception ex) {
			throw new ManagementException("error resetting usage parametes", ex);
		} finally {
			try {
				if (rb)
					txmgr.setRollbackOnly();
				txmgr.commit();
			} catch (SystemException e) {
				throw new RuntimeException("Txmgr failed", e);
			}

		}

	}

	/*
	 * Add a notification listener Created from equivalent method in
	 * TraceMBeanImpl.
	 * 
	 * @see javax.management.NotificationBroadcaster#addNotificationListener(javax.management.NotificationListener,
	 *      javax.management.NotificationFilter, java.lang.Object)
	 */
	public void addNotificationListener(NotificationListener listener,
			NotificationFilter filter, Object handback)
			throws IllegalArgumentException {
		// logger.debug("addNotificationListener");
		this.listeners.add(new ListenerFilterHandbackTriplet(listener, filter,
				handback));
	}

	/*
	 * Remove the listener from the list. Created from equivalent method in
	 * TraceMBeanImpl.
	 * 
	 * @see javax.management.NotificationBroadcaster#removeNotificationListener(javax.management.NotificationListener)
	 */
	public void removeNotificationListener(NotificationListener listener)
			throws ListenerNotFoundException {
		// logger.debug("removeNotificationListener");
		boolean found = false;
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet) iter
					.next();
			if (triplet.notificationListener == listener) {
				found = true;
				break;
			}
		}
		if (found)
			listeners.remove(listener);
		else
			throw new ListenerNotFoundException();
	}

	/*
	 * @see javax.management.NotificationBroadcaster#getNotificationInfo()
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
		// logger.debug("getNotificationInfo");
		String[] notificationTypes = new String[] { USAGE_NOTIFICATION_TYPE };
		MBeanNotificationInfo[] mbeanNotificationInfo = new MBeanNotificationInfo[] { new MBeanNotificationInfo(
				notificationTypes, "UsageNotifications",
				"Notification of SBB usage") };

		return mbeanNotificationInfo;
	}

	/**
	 * Send the notification.
	 * 
	 * @param value
	 * @param seqno
	 * @param usageParameterSetName
	 * @param usageParameterName
	 * @param isCounter
	 */
	public void sendUsageNotification(long value, long seqno,
			String usageParameterSetName, String usageParameterName,
			boolean isCounter) {
		UsageNotification notification = new UsageNotification(this,
				this.serviceId, this.sbbId, usageParameterSetName,
				usageParameterName, isCounter, value, seqno, System
						.currentTimeMillis());
		// logger.debug("sendNotification nListeners = " + listeners.size() );
		Iterator iter = listeners.iterator();
		while (iter.hasNext()) {
			ListenerFilterHandbackTriplet triplet = (ListenerFilterHandbackTriplet) iter
					.next();
			if (triplet.notificationFilter == null
					|| triplet.notificationFilter
							.isNotificationEnabled(notification)) {
				triplet.notificationListener.handleNotification(notification,
						triplet.handbackObject);
			}
		}

	}

	/**
	 * @param usageParam
	 */
	public void setUsageParameter(InstalledUsageParameterSet usageParam) {
		this.usagePramSet = usageParam;

	}

}
