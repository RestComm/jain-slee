/**
 * 
 */
package org.mobicents.slee.container.management;

import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.InvalidLinkNameBindingStateException;
import javax.slee.management.ManagementException;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.transaction.SystemException;

import org.mobicents.slee.container.SleeContainerModule;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceActivityFactory;
import org.mobicents.slee.container.service.ServiceActivityHandle;

/**
 * @author martins
 *
 */
public interface ServiceManagement extends SleeContainerModule {

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getState(javax.slee.ServiceID)
	 */
	public ServiceState getState(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException; 

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServices(javax.slee.management.ServiceState)
	 */
	public ServiceID[] getServices(ServiceState serviceState)
			throws NullPointerException, ManagementException;

	/**
	 * Retrieves the set of ra entity link names referenced by the service componen, which do not exist
	 * @param serviceComponent
	 */
	public Set<String> getReferencedRAEntityLinksWhichNotExists(ServiceComponent serviceComponent);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID)
	 */
	public void activate(final ServiceID serviceID) throws NullPointerException,
	UnrecognizedServiceException, InvalidStateException,
	InvalidLinkNameBindingStateException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#activate(javax.slee.ServiceID[])
	 */
	public void activate(ServiceID[] serviceIDs) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID)
	 */
	public void deactivate(final ServiceID serviceID) throws NullPointerException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivate(javax.slee.ServiceID[])
	 */
	public void deactivate(ServiceID[] arg0) throws NullPointerException,
			InvalidArgumentException, UnrecognizedServiceException,
			InvalidStateException, ManagementException;
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID,
	 *      javax.slee.ServiceID)
	 */
	public void deactivateAndActivate(ServiceID arg0, ServiceID arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#deactivateAndActivate(javax.slee.ServiceID[],
	 *      javax.slee.ServiceID[])
	 */
	public void deactivateAndActivate(ServiceID[] arg0, ServiceID[] arg1)
			throws NullPointerException, InvalidArgumentException,
			UnrecognizedServiceException, InvalidStateException,
			ManagementException;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ServiceManagementMBean#getServiceUsageMBean(javax.slee.ServiceID)
	 */
	public ObjectName getServiceUsageMBean(ServiceID serviceID)
			throws NullPointerException, UnrecognizedServiceException,
			ManagementException;

	// --- non JMX

	/**
	 * 
	 * Retrieves {@link ServiceImpl} with the specified {@link ServiceID}.
	 * 
	 * @param serviceID
	 * @return
	 * @throws UnrecognizedServiceException
	 */
	public Service getService(ServiceID serviceID)
			throws UnrecognizedServiceException;

	/**
	 * 
	 * @return
	 */
	public ServiceActivityFactory getServiceActivityFactory();
	
	/**
	 * 
	 * @return
	 */
	public ServiceActivityContextInterfaceFactory getServiceActivityContextInterfaceFactory();
	
	/**
	 * Install a service into SLEE
	 * 
	 * @param serviceComponent
	 * @throws Exception
	 */
	public void installService(final ServiceComponent serviceComponent)
			throws Exception;

	/**
	 * uninstall a service.
	 * 
	 * @throws SystemException
	 * @throws UnrecognizedServiceException
	 * @throws MBeanRegistrationException
	 * @throws InstanceNotFoundException
	 * @throws UnrecognizedResourceAdaptorEntityException
	 * @throws NullPointerException
	 * @throws InvalidStateException 
	 * 
	 */
	public void uninstallService(final ServiceComponent serviceComponent)
			throws SystemException, UnrecognizedServiceException,
			InstanceNotFoundException, MBeanRegistrationException,
			NullPointerException, UnrecognizedResourceAdaptorEntityException,ManagementException, InvalidStateException;
		
	/**
	 * Verifies if the specified ra entity link name is referenced by a non inactive service.
	 * 
	 * @param raLinkName
	 * @return
	 */
	public boolean isRAEntityLinkNameReferenced(String raLinkName) ;

	/**
	 * @param serviceComponent
	 * @return
	 */
	public Service getService(ServiceComponent serviceComponent);

	/**
	 * @param activityHandle
	 */
	public void activityEnded(ServiceActivityHandle activityHandle);
}
