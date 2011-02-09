/**
 * 
 */
package org.mobicents.slee.container.component.library;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.SleeComponent;

/**
 * 
 * @author martins
 * 
 */
public interface LibraryComponent extends SleeComponent {

	/**
	 * Retrieves the component's descriptor.
	 * @return
	 */
	public LibraryDescriptor getDescriptor();
	
	/**
	 * Retrieves the library id
	 * 
	 * @return
	 */
	public LibraryID getLibraryID();

	/**
	 * Retrieves the JAIN SLEE specs descriptor
	 * 
	 * @return
	 */
	public javax.slee.management.LibraryDescriptor getSpecsDescriptor();

}
