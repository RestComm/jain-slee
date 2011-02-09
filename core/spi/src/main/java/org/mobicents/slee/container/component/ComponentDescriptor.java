/**
 * 
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.ComponentID;

/**
 * @author martins
 *
 */
public interface ComponentDescriptor {

	/**
	 * 
	 * @return
	 */
	public Set<ComponentID> getDependenciesSet();
	
	/**
	 * 
	 * @return
	 */
	public boolean isSlee11();
}
