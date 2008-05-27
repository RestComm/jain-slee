/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

/*
 * Created on Jul 30, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.mobicents.slee.container.management.jmx;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;

/**
 * @author root
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ServiceManagement implements ServiceManagementMBean {

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID)
	 */
	public ServiceState getState(ServiceID arg0) throws NullPointerException,
			UnrecognizedServiceException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.management.ServiceState)
	 */
	public ServiceID[] getServices(ServiceState arg0)
			throws NullPointerException, ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID arg0) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] arg0) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
	 */
	public void deactivate(ServiceID arg0) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID[])
	 */
	public void deactivate(ServiceID[] arg0) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID, javax.slee.ServiceID)
	 */
	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID[], javax.slee.ServiceID[])
	 */
	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
	 */
	public ObjectName getServiceUsageMBean(ServiceID arg0)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {
		// TODO Auto-generated method stub
		return null;
	}

}
