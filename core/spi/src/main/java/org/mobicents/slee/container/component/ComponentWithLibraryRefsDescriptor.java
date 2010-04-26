/**
 * 
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.management.LibraryID;

/**
 * @author martins
 *
 */
public interface ComponentWithLibraryRefsDescriptor extends ComponentDescriptor {

	/**
	 * 
	 * @return
	 */
	public Set<LibraryID> getLibraryRefs();
	
}
