/**
 * 
 */
package org.mobicents.slee.container.service;

import java.util.Set;

import javax.slee.management.ServiceState;

import org.mobicents.slee.container.sbbentity.SbbEntity;

/**
 * @author martins
 *
 */
public interface Service {

	/**
	 * Get the SBB entity values for the service.  Note operation is rather
	 * expensive as reading all the SBB entities from the cache.  Avoid using
	 * it whenever possible
	 * 
	 *  
	 */
	public Set<String> getChildObj();

	/**
	 * @return
	 */
	public ServiceState getState();

	/**
	 * @param state
	 */
	public void setState(ServiceState state);

	/**
	 * @param serviceConvergenceName
	 */
	public void removeConvergenceName(String serviceConvergenceName);

	/**
	 * Add a child for a given convergence name. This actually creates an Sbb
	 * Entity for the given convergence name and returns it.
	 * 
	 * @param convergenceName
	 */
	public SbbEntity addChild(String convergenceName);
	
	/**
	 * Check if this service maps the specified convergence name.
	 * 
	 * @param convergenceName
	 * @return
	 */
	public boolean containsConvergenceName(String convergenceName);
	
	/**
	 * 
	 * @param convergenceName
	 * @return
	 */
	public String getRootSbbEntityId(String convergenceName);
}
