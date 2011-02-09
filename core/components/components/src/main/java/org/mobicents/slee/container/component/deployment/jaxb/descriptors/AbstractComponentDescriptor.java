/**
 * 
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;

import org.mobicents.slee.container.component.ComponentDescriptor;

/**
 * @author martins
 *
 */
public abstract class AbstractComponentDescriptor implements ComponentDescriptor {

	protected Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();
	
	private final boolean slee11;
	
	/**
	 * 
	 */
	public AbstractComponentDescriptor(boolean slee11) {
		this.slee11 = slee11;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentDescriptor#getDependenciesSet()
	 */
	public Set<ComponentID> getDependenciesSet() {
		return dependenciesSet;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.ComponentDescriptor#isSlee11()
	 */
	public boolean isSlee11() {
		return slee11;
	}
}
