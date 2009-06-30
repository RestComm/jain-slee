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
import org.mobicents.slee.util.concurrent.ConcurrentHashSet;

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
	private Class<?> eventTypeClass = null;

	/**
	 * the JAIN SLEE specs event type descriptor
	 */
	private EventTypeDescriptor specsDescriptor = null;
	
	/**
	 * the set of active {@link ServiceComponent} which define this event as initial 
	 */
	private ConcurrentHashSet<ServiceComponent> activeServicesWhichDefineEventAsInitial = new ConcurrentHashSet<ServiceComponent>();
		
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
	public Class<?> getEventTypeClass() {
		return eventTypeClass;
	}

	/**
	 * Sets the event type class
	 * @param eventTypeClass
	 */
	public void setEventTypeClass(Class<?> eventTypeClass) {
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
	boolean addToDeployableUnit() {
		return getDeployableUnit().getEventTypeComponents().put(getEventTypeID(), this) == null;
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
	
	/**
	 * Retrieves the set of active {@link ServiceComponent} which define this event as initial
	 * @return
	 */
	public Set<ServiceComponent> getActiveServicesWhichDefineEventAsInitial() {
		return activeServicesWhichDefineEventAsInitial;
	}
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was activated
	 * @param serviceComponent
	 */
	public void activatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent) {
		activeServicesWhichDefineEventAsInitial.add(serviceComponent);
	}
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was deactivated
	 * @param serviceComponent
	 */
	public void deactivatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent) {
		activeServicesWhichDefineEventAsInitial.remove(serviceComponent);
	}
	
	@Override
	public void processSecurityPermissions() throws DeploymentException {
		//Do nothing
		
	}
	
	@Override
	public void undeployed() {		
		super.undeployed();
		eventTypeClass = null;
		if (activeServicesWhichDefineEventAsInitial != null) {
			activeServicesWhichDefineEventAsInitial.clear();
			activeServicesWhichDefineEventAsInitial = null;
		}
	}
}
