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
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.InvalidLinkNameBindingStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ServiceManagement;

/**
 * Implementation of the ServiceManagementMBean
 * 
 * @author M. Ranganathan
 * @author Eduardo Martins
 */
public class ServiceManagementMBeanImpl extends MobicentsServiceMBeanSupport implements
		ServiceManagementMBeanImplMBean {

	private final ServiceManagement serviceManagement;
	
	public ServiceManagementMBeanImpl(SleeContainer sleeContainer) throws NotCompliantMBeanException {
		super(sleeContainer,ServiceManagementMBeanImplMBean.class);
		this.serviceManagement = sleeContainer.getServiceManagement();
	}
	
	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			InvalidLinkNameBindingStateException, ManagementException {	
		try {
			serviceManagement.activate(serviceID);
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (UnrecognizedServiceException e) {
			throw e;
		}
		catch (InvalidLinkNameBindingStateException e) {
			throw e;
		}
		catch (InvalidStateException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		}
	}

	
	/* (non-Javadoc)
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
	InvalidArgumentException, UnrecognizedServiceException,
	InvalidStateException, InvalidLinkNameBindingStateException,
	ManagementException {
		try {
			serviceManagement.activate(serviceIDs);
		}
		catch (NullPointerException e) {
			throw e;
		}
		catch (InvalidArgumentException e) {
			throw e;
		}
		catch (UnrecognizedServiceException e) {
			throw e;
		}
		catch (InvalidLinkNameBindingStateException e) {
			throw e;
		}
		catch (InvalidStateException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new ManagementException(e.getMessage(),e);
		}		
	}

	public void deactivate(ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivate(serviceID);
	}

	public void deactivate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException {
		serviceManagement.deactivate(serviceIDs);
	}

	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivateAndActivate(arg0, arg1);
	}

	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException {
		serviceManagement.deactivateAndActivate(arg0, arg1);
	}

	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {
		return serviceManagement.getServiceUsageMBean(serviceID);
	}

	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException {

		return serviceManagement.getServices(serviceState);
	}

	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException {

		return serviceManagement.getState(serviceID);
	}

}
