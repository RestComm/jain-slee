/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
