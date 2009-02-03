package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventDefinition;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MEventJar;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.event.MLibraryRef;
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
  private MEventDefinition eventDefinition;
  private String description;
  private String eventClassName;
  private List<MLibraryRef> libraryRefs;
  
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
    this.eventDefinition = this.eventJar.getEventDefinition().get(index);
    this.description = this.eventDefinition.getDescription();
    
    this.eventClassName = this.eventDefinition.getEventClassName();
    
    // FIXME: alexandre: is this supposed to be like that? do we use it in here?
    this.libraryRefs = this.eventJar.getLibraryRef();
  }

  @Override
  public Object getJAXBDescriptor()
  {
    return this.eventJar;
  }
  
  public MEventDefinition getEventDefinition()
  {
    return eventDefinition;
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

}
