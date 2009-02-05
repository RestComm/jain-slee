/**
 * Start time:00:46:57 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import javax.slee.management.DeployableUnitID;
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
public class LibraryComponent {

	protected LibraryDescriptorImpl descriptor = null;
	protected LibraryID libraryID = null;
	protected DeployableUnitID deployableUnitID = null;

	public LibraryDescriptorImpl getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(LibraryDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}

	public LibraryID getLibraryID() {
		return libraryID;
	}

	public void setLibraryID(LibraryID libraryID) {
		this.libraryID = libraryID;
	}

	public DeployableUnitID getDeployableUnitID() {
		return deployableUnitID;
	}

	public void setDeployableUnitID(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = deployableUnitID;
	}

}
