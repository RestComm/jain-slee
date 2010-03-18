/**
 * Start time:00:44:47 2009-02-04<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
	private Class<?> eventTypeClass = null;

	/**
	 * the JAIN SLEE specs event type descriptor
	 */
	private EventTypeDescriptor specsDescriptor = null;
	
	/**
	 * the ordered set of active {@link ServiceComponent} which define this event as initial 
	 */
	private SortedSet<ServiceComponent> activeServicesWhichDefineEventAsInitial = new TreeSet<ServiceComponent>(new ActiveServicesWhichDefineEventAsInitialComparator());
	
	private class ActiveServicesWhichDefineEventAsInitialComparator implements Comparator<ServiceComponent> {
		public int compare(ServiceComponent o1, ServiceComponent o2) {
			if (o1 == o2) {
				return 0;
			}
			int result = o2.getDescriptor().getMService().getDefaultPriority() - o1.getDescriptor().getMService().getDefaultPriority();
			if (result == 0) {
				// older wins
				result = (int) (o1.getCreationTime() - o2.getCreationTime());
				if (result == 0) {
					// id string comparation, cause a 0 result means one entity is not added to set
					result = o1.getServiceID().compareTo(o2.getServiceID());
				}
			}
			return result;
		}
	}
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
		synchronized (activeServicesWhichDefineEventAsInitial) {
			activeServicesWhichDefineEventAsInitial.add(serviceComponent);
		}
	}
	
	/**
	 * Signals that the specified {@link ServiceComponent} which define this event as initial was deactivated
	 * @param serviceComponent
	 */
	public void deactivatedServiceWhichDefineEventAsInitial(ServiceComponent serviceComponent) {
		synchronized (activeServicesWhichDefineEventAsInitial) {
			activeServicesWhichDefineEventAsInitial.remove(serviceComponent);
		}
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
