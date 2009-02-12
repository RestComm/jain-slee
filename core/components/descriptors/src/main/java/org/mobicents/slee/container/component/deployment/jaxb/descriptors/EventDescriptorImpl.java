package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventJar;
import org.w3c.dom.Document;

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
public class EventDescriptorImpl extends JAXBBaseUtilityClass {

  private int index;
  private MEventJar eventJar;
  private EventTypeID eventTypeID;
  private String description;
  private String eventClassName;
  private List<MLibraryRef> libraryRefs;
  
  private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

  public EventDescriptorImpl(Document doc)
  {
    super(doc);
  }
  
  public EventDescriptorImpl(Document doc, org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventJar eventJar10, int index)
  {
    super(doc);
    
    this.eventJar = new MEventJar(eventJar10);
    this.index = index;
  }
  
  public EventDescriptorImpl(Document doc, org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventJar eventJar11, int index)
  {
    super(doc);

    this.eventJar = new MEventJar(eventJar11);
    this.index = index;
  }
  
  @Override
  public void buildDescriptionMap()
  {
    MEventDefinition eventDefinition = this.eventJar.getEventDefinition().get(index);
    this.description = eventDefinition.getDescription();
    this.eventTypeID = new EventTypeID(eventDefinition.getEventTypeName(),eventDefinition.getEventTypeVendor(),eventDefinition.getEventTypeVersion());
    this.eventClassName = eventDefinition.getEventClassName();
    
    // FIXME: alexandre: is this supposed to be like that? do we use it in here?
    this.libraryRefs = this.eventJar.getLibraryRef();

    buildDependenciesSet();
  }
  
  private void buildDependenciesSet()
  {
    for(MLibraryRef libraryRef : libraryRefs)
    {
      this.dependenciesSet.add( libraryRef.getComponentID() );
    }
  }

  @Override
  public Object getJAXBDescriptor()
  {
    return this.eventJar;
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

}
