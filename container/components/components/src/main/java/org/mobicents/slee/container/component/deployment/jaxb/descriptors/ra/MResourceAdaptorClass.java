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

/**
 * 
 * MResourceAdaptorClass.java
 *
 * <br>Project:  mobicents
 * <br>4:47:51 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorClass {
  
  protected boolean supportsActiveReconfiguration = false;
  protected String description;
  protected String resourceAdaptorClassName;

  public MResourceAdaptorClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ResourceAdaptorClass resourceAdaptorClass11)
  {    
    if (resourceAdaptorClass11.getSupportsActiveReconfiguration() != null) {
    	this.supportsActiveReconfiguration = Boolean.parseBoolean(resourceAdaptorClass11.getSupportsActiveReconfiguration());
    }
    
    this.resourceAdaptorClassName = resourceAdaptorClass11.getResourceAdaptorClassName().getvalue();
  }
  
  public boolean getSupportsActiveReconfiguration()
  {
    return supportsActiveReconfiguration;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorClassName()
  {
    return resourceAdaptorClassName;
  }
}
