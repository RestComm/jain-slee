/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

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
