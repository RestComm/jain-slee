/**
 * 
 */
package org.mobicents.slee.container.service;

import javax.slee.management.ServiceState;

/**
 * @author martins
 *
 */
public interface Service {

	/**
	 * @return
	 */
	public ServiceState getState();

	/**
	 * @param state
	 */
	public void setState(ServiceState state);

}
