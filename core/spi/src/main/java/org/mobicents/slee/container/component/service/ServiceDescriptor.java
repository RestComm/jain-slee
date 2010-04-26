/**
 * 
 */
package org.mobicents.slee.container.component.service;

import javax.slee.SbbID;
import javax.slee.ServiceID;

import org.mobicents.slee.container.component.ComponentDescriptor;

/**
 * @author martins
 *
 */
public interface ServiceDescriptor extends ComponentDescriptor {

	/**
	 * @return
	 */
	public String getAddressProfileTable();
	
	/**
	 * Retrieves the service's default priority.
	 * @return
	 */
	public byte getDefaultPriority();
	
	/**
	 * 
	 * @return
	 */
	public String getResourceInfoProfileTable();
	
	/**
	 * 
	 * @return
	 */
	public SbbID getRootSbbID();
	
	/**
	 * 
	 * @return
	 */
	public ServiceID getServiceID(); 
	
}
