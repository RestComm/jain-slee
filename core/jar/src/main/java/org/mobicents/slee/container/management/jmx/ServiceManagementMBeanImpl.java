/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.management.jmx;

import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.SleeContainer;

/**
 * Implementation of the ServiceManagementMBean
 * 
 * @author M. Ranganathan
 * @author Eduardo Martins
 */
public class ServiceManagementMBeanImpl extends StandardMBean implements
		ServiceManagementMBean {

	public ServiceManagementMBeanImpl() throws NotCompliantMBeanException {
		super(ServiceManagementMBean.class);

	}

	public void activate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement().activate(
				serviceID);
	}

	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement().activate(
				serviceIDs);
	}

	public void deactivate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement().deactivate(
				serviceID);
	}

	public void deactivate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement().deactivate(
				serviceIDs);
	}

	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement()
				.deactivateAndActivate(arg0, arg1);
	}

	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {

		SleeContainer.lookupFromJndi().getServiceManagement()
				.deactivateAndActivate(arg0, arg1);
	}

	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		return SleeContainer.lookupFromJndi().getServiceManagement()
				.getServiceUsageMBean(serviceID);
	}

	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException {

		return SleeContainer.lookupFromJndi().getServiceManagement()
				.getServices(serviceState);
	}

	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		return SleeContainer.lookupFromJndi().getServiceManagement().getState(
				serviceID);
	}

}
