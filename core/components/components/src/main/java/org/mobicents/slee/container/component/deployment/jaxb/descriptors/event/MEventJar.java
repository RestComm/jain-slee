package org.mobicents.slee.container.component.deployment.jaxb.descriptors.event;

import java.util.ArrayList;
import java.util.List;

import javax.slee.management.LibraryID;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.event.LibraryRef;

/**
 * 
 * MEventJar.java
 *
 * <br>Project:  mobicents
 * <br>11:25:40 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MEventJar {

  private String description;
  private List<LibraryID> libraryRefs = new ArrayList<LibraryID>();
  private List<MEventDefinition> eventDefinition = new ArrayList<MEventDefinition>();
  
  public MEventJar(org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventJar eventJar10)
  {    
    this.description = eventJar10.getDescription() == null ? null : eventJar10.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventDefinition eventDefinition10 : eventJar10.getEventDefinition())
    {
      this.eventDefinition.add( new MEventDefinition(eventDefinition10) );
    }
  }
  
  public MEventJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventJar eventJar11)
  {    
    this.description = eventJar11.getDescription() == null ? null : eventJar11.getDescription().getvalue();
    
    for (LibraryRef libraryRef : eventJar11.getLibraryRef()) {
		this.libraryRefs.add(new LibraryID(libraryRef.getLibraryName()
				.getvalue(), libraryRef.getLibraryVendor().getvalue(),
				libraryRef.getLibraryVersion().getvalue()));
	}

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventDefinition eventDefinition11 : eventJar11.getEventDefinition())
    {
      this.eventDefinition.add( new MEventDefinition(eventDefinition11) );
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<LibraryID> getLibraryRef()
  {
    return libraryRefs;
  }
  
  public List<MEventDefinition> getEventDefinition()
  {
    return eventDefinition;
  }
  
}
