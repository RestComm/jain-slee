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
