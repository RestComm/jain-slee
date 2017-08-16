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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * MResourceAdaptorTypeJar.java
 *
 * <br>Project:  mobicents
 * <br>12:23:09 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeJar {

  private String description;
  private List<MResourceAdaptorType> resourceAdaptorType = new ArrayList<MResourceAdaptorType>();

  public MResourceAdaptorTypeJar(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar10)
  {    
    this.description = resourceAdaptorTypeJar10.getDescription() == null ? null : resourceAdaptorTypeJar10.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorType resourceAdaptorType10 : resourceAdaptorTypeJar10.getResourceAdaptorType())
    {
      this.resourceAdaptorType.add( new MResourceAdaptorType(resourceAdaptorType10) );
    }
  }
  
  public MResourceAdaptorTypeJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeJar resourceAdaptorTypeJar11)
  {    
    this.description = resourceAdaptorTypeJar11.getDescription() == null ? null : resourceAdaptorTypeJar11.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorType resourceAdaptorType11 : resourceAdaptorTypeJar11.getResourceAdaptorType())
    {
      this.resourceAdaptorType.add( new MResourceAdaptorType(resourceAdaptorType11) );
    }
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<MResourceAdaptorType> getResourceAdaptorType()
  {
    return resourceAdaptorType;
  }
  
}
