/**
 * 
 */
package org.mobicents.slee.container.component.library;

import java.util.List;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;

/**
 * @author martins
 *
 */
public interface LibraryDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<JarDescriptor> getJars();
	
	/**
	 * 
	 * @return
	 */
	public LibraryID getLibraryID();
	
	/**
	 * 
	 * @return
	 */
	public String getSecurityPermissions();
}
