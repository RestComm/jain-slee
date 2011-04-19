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

/**
 * 
 * MEventDefinition.java
 *
 * <br>Project:  mobicents
 * <br>11:19:48 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MEventDefinition {
  
  private String description;
  private String eventTypeName;
  private String eventTypeVendor;
  private String eventTypeVersion;
  private String eventClassName;
  
  public MEventDefinition(org.mobicents.slee.container.component.deployment.jaxb.slee.event.EventDefinition eventDefinition10)
  {    
    this.description = eventDefinition10.getDescription() == null ? null : eventDefinition10.getDescription().getvalue();
    
    this.eventTypeName = eventDefinition10.getEventTypeName().getvalue();
    this.eventTypeVendor = eventDefinition10.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventDefinition10.getEventTypeVersion().getvalue();
    
    this.eventClassName = eventDefinition10.getEventClassName().getvalue();
  }

  public MEventDefinition(org.mobicents.slee.container.component.deployment.jaxb.slee11.event.EventDefinition eventDefinition11)
  {    
    this.description = eventDefinition11.getDescription() == null ? null : eventDefinition11.getDescription().getvalue();
    
    this.eventTypeName = eventDefinition11.getEventTypeName().getvalue();
    this.eventTypeVendor = eventDefinition11.getEventTypeVendor().getvalue();
    this.eventTypeVersion = eventDefinition11.getEventTypeVersion().getvalue();
    
    this.eventClassName = eventDefinition11.getEventClassName().getvalue();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getEventTypeName()
  {
    return eventTypeName;
  }
  
  public String getEventTypeVendor()
  {
    return eventTypeVendor;
  }
  
  public String getEventTypeVersion()
  {
    return eventTypeVersion;
  }
  
  public String getEventClassName()
  {
    return eventClassName;
  }

}
