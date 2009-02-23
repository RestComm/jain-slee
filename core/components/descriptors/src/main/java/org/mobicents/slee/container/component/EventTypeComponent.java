/**
 * Start time:00:44:47 2009-02-04<br>
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
import javax.slee.EventTypeID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DependencyException;
import javax.slee.management.DeploymentException;
import javax.slee.management.EventTypeDescriptor;
import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.EventTypeDescriptorImpl;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;

/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EventTypeComponent extends SleeComponent {

	/**
	 * the event type descriptor
	 */
	private final EventTypeDescriptorImpl descriptor;

	/**
	 * the event type class
	 */
	private Class eventTypeClass = null;

	/**
	 * the JAIN SLEE specs event type descriptor
	 */
	private EventTypeDescriptor specsDescriptor = null;
	
	/**
	 * 
	 * @param descriptor
	 */
	public EventTypeComponent(EventTypeDescriptorImpl descriptor) {
		this.descriptor = descriptor;
	}
	
	/**
	 * Retrieves the event type class
	 * @return
	 */
	public Class getEventTypeClass() {
		return eventTypeClass;
	}

	/**
	 * Sets the event type class
	 * @param eventTypeClass
	 */
	public void setEventTypeClass(Class eventTypeClass) {
		this.eventTypeClass = eventTypeClass;
	}

	/**
	 * Retrieves the event type id
	 * @return
	 */
	public EventTypeID getEventTypeID() {
		return descriptor.getEventTypeID();
	}

	/**
	 * Retrieves the event type descriptor
	 * @return
	 */
	public EventTypeDescriptorImpl getDescriptor() {
		return descriptor;
	}

	@Override
	void addToDeployableUnit() {
		getDeployableUnit().getEventTypeComponents().put(getEventTypeID(), this);
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
		return getEventTypeID();
	}
	
	@Override
	public boolean validate() throws DependencyException, DeploymentException {
		// nothing to validate
		return true;
	}
	
	/**
	 * Retrieves the JAIN SLEE specs event type descriptor
	 * @return
	 */
	public EventTypeDescriptor getSpecsDescriptor() {
		if (specsDescriptor == null) {
			Set<LibraryID> libraryIDSet = new HashSet<LibraryID>();
			for (MLibraryRef mLibraryRef : getDescriptor().getLibraryRefs()) {
				libraryIDSet.add(mLibraryRef.getComponentID());
			}
			LibraryID[] libraryIDs = libraryIDSet.toArray(new LibraryID[libraryIDSet.size()]);
			specsDescriptor = new EventTypeDescriptor(getEventTypeID(),getDeployableUnit().getDeployableUnitID(),getDeploymentUnitSource(),libraryIDs,getDescriptor().getEventClassName());
		}
		return specsDescriptor;
	}
	
	@Override
	public ComponentDescriptor getComponentDescriptor() {
		return getSpecsDescriptor(); 
	}
}
