/**
 * Start time:00:46:57 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.HashSet;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryDescriptor;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.LibraryDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.library.MJar;

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
	 * the JAIN SLEE specs descriptor
	 */
	private LibraryDescriptor specsDescriptor;
	
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
	boolean addToDeployableUnit() {
		return getDeployableUnit().getLibraryComponents().put(getLibraryID(), this) == null;
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
	
	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		// nothing to validate ?
		return true;
	}
	
	/**
	 *  Retrieves the JAIN SLEE specs descriptor
	 * @return
	 */
	public LibraryDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			Set<LibraryID> libraryIDSet = new HashSet<LibraryID>();
			for (MLibraryRef mLibraryRef : getDescriptor().getLibraryRefs()) {
				libraryIDSet.add(mLibraryRef.getComponentID());
			}
			LibraryID[] libraryIDs = libraryIDSet.toArray(new LibraryID[libraryIDSet.size()]);
			Set<String> jarsSet = new HashSet<String>();
			for (MJar mJar : getDescriptor().getJars()) {
				jarsSet.add(mJar.getJarName());
			}
			String[] jars = jarsSet.toArray(new String[jarsSet.size()]);
			specsDescriptor = new LibraryDescriptor(getLibraryID(),getDeployableUnit().getDeployableUnitID(),getDeploymentUnitSource(),libraryIDs,jars);
		}
		return specsDescriptor;
	}
	
	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor();
	}
	
}
