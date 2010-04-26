package org.mobicents.slee.container.component.service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.SbbID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.component.ComponentRepository;
import org.mobicents.slee.container.component.SleeComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBean;

/**
 * 
 * @author martins
 *
 */
public interface ServiceComponent extends SleeComponent {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public ServiceDescriptor getDescriptor();
	
	/**
	 * Retrieves the service alarm notification sources, mapped by sbb id.
	 * @return
	 */
	public ConcurrentHashMap<SbbID, Object> getAlarmNotificationSources();

	/**
	 * Retrieves the id of the service
	 * 
	 * @return
	 */
	public ServiceID getServiceID();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.management.ServiceDescriptor getSpecsDescriptor();

	/**
	 * Retrieves the time in milliseconds this component was created
	 * @return
	 */
	public long getCreationTime();
	
	/**
	 * Retrieves the set of sbbs used by this service
	 * 
	 * @param componentRepository
	 * @return
	 */
	public Set<SbbID> getSbbIDs(ComponentRepository componentRepository);

	/**
	 * Retrieves the set of RA entity links referenced by the sbbs related with the service.
	 * @param componentRepository
	 * @return
	 */
	public Set<String> getResourceAdaptorEntityLinks(ComponentRepository componentRepository);
	
	/**
	 * Retrieves the {@link SbbComponent} the service defines as root
	 * 
	 * @return
	 */
	public SbbComponent getRootSbbComponent();

	/**
	 * Sets the {@link SbbComponent} the service defines as root
	 * 
	 * @param rootSbbComponent
	 */
	public void setRootSbbComponent(SbbComponent rootSbbComponent);
	
	/**
	 * Retrieves the usage mbean for this service
	 * 
	 * @return
	 */
	public ServiceUsageMBean getServiceUsageMBean();

	/**
	 * Sets the usage mbean for this service
	 * 
	 * @param serviceUsageMBean
	 */
	public void setServiceUsageMBean(ServiceUsageMBean serviceUsageMBean);
		
}
