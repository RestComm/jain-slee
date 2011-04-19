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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

import org.mobicents.slee.container.component.ra.ConfigPropertyDescriptor;

/**
 * 
 * MConfigProperty.java
 *
 * <br>Project:  mobicents
 * <br>12:42:23 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MConfigProperty implements ConfigPropertyDescriptor {
  
  private String description;
  private String configPropertyName;
  private String configPropertyType;
  private String configPropertyValue;
  
  public MConfigProperty(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ConfigProperty configProperty11)
  {    
    this.description = configProperty11.getDescription() == null ? null : configProperty11.getDescription().getvalue();
    
    this.configPropertyName = configProperty11.getConfigPropertyName().getvalue();
    this.configPropertyType = configProperty11.getConfigPropertyType().getvalue();
    this.configPropertyValue = configProperty11.getConfigPropertyValue() == null ? null : configProperty11.getConfigPropertyValue().getvalue();
  }

  public String getDescription()
  {
    return description;
  }
  
  public String getConfigPropertyName()
  {
    return configPropertyName;
  }
  
  public String getConfigPropertyType()
  {
    return configPropertyType;
  }
  
  public String getConfigPropertyValue()
  {
    return configPropertyValue;
  }
  
}
