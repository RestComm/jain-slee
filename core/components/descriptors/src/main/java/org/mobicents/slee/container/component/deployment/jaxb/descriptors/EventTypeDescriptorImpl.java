package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;

/**
 * 
 * EventDescriptorImpl.java
 *
 * <br>Project:  mobicents
 * <br>7:22:39 PM Jan 29, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EventTypeDescriptorImpl {

  private final EventTypeID eventTypeID;
  private final String description;
  private final String eventClassName;
  private final List<MLibraryRef> libraryRefs;
  private final boolean isSlee11;
  
  private final Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

  public EventTypeDescriptorImpl(MEventDefinition eventDefinition,
		List<MLibraryRef> libraryRefs, boolean isSlee11) throws DeploymentException {
	  try {
		  this.description = eventDefinition.getDescription();
		  this.eventTypeID = new EventTypeID(eventDefinition
				  .getEventTypeName(), eventDefinition.getEventTypeVendor(),
				  eventDefinition.getEventTypeVersion());
		  this.eventClassName = eventDefinition.getEventClassName();
		  this.libraryRefs = libraryRefs;
		  this.isSlee11 = isSlee11;
		  buildDependenciesSet();
	  } catch (Exception e) {
		  throw new DeploymentException(
				  "failed to build event type descriptor", e);
	  }
	}
  
  private void buildDependenciesSet()
  {
    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }
  }
  
  public EventTypeID getEventTypeID() {
	return eventTypeID;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getEventClassName()
  {
    return eventClassName;
  }
  
  public List<MLibraryRef> getLibraryRefs()
  {
    return libraryRefs;
  }
  
  public Set<ComponentID> getDependenciesSet() {
    return this.dependenciesSet;
  }

  public boolean isSlee11() {
	return isSlee11;
  }

}
