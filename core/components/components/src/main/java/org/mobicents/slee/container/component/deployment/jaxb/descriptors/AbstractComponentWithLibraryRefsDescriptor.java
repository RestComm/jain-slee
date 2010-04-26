/**
 * 
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;

/**
 * @author martins
 * 
 */
public abstract class AbstractComponentWithLibraryRefsDescriptor extends
		AbstractComponentDescriptor implements
		ComponentWithLibraryRefsDescriptor {

	/**
	 * @param slee11
	 */
	public AbstractComponentWithLibraryRefsDescriptor(boolean slee11) {
		super(slee11);
	}

	private Set<LibraryID> libraryRefs = new HashSet<LibraryID>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.core.component.ComponentWithLibraryRefsDescriptor#
	 * getLibraryRefs()
	 */
	public Set<LibraryID> getLibraryRefs() {
		return libraryRefs;
	}

	protected void setLibraryRefs(List<MLibraryRef> list) {
		for (MLibraryRef mLibraryRef : list) {
			libraryRefs.add(mLibraryRef.getComponentID());
		}
		this.dependenciesSet.addAll(libraryRefs);
	}
}
