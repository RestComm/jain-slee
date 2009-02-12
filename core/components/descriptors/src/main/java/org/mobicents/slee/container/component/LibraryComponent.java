/**
 * Start time:00:46:57 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;

/**
 * Start time:00:46:57 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class LibraryComponent extends SleeComponent {

	/**
	 * the library descriptor
	 */
	private final LibraryDescriptorImpl descriptor;
	
	/**
	 * 
	 * @param descriptor
	 */
	public LibraryComponent(LibraryDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * Retrieves the library descriptor
	 * @return
	 */
	public LibraryDescriptorImpl getDescriptor() {
		return descriptor;
	}

	/**
	 * Retrieves the library id
	 * @return
	 */
	public LibraryID getLibraryID() {
		return descriptor.getLibraryID();
	}

	@Override
	public Set<ComponentID> getDependenciesSet() {
		return descriptor.getDependenciesSet();
	}
	
	@Override
	public boolean isSlee11() {
		return descriptor.isSlee11();
	}
	
	@Override
	public ComponentID getComponentID() {
		return getLibraryID();
	}
}
