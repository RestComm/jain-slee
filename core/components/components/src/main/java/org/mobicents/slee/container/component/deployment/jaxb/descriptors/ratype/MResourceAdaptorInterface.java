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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ratype;

/**
 * 
 * MResourceAdaptorInterface.java
 *
 * <br>Project:  mobicents
 * <br>5:33:32 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorInterface {
  
  private String description;
  private String resourceAdaptorInterfaceName;

  public MResourceAdaptorInterface(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorInterface resourceAdaptorInterface10)
  {
    super();
        
    this.description = resourceAdaptorInterface10.getDescription() == null ? null : resourceAdaptorInterface10.getDescription().getvalue();
    this.resourceAdaptorInterfaceName = resourceAdaptorInterface10.getResourceAdaptorInterfaceName().getvalue(); 
  }
  
  public MResourceAdaptorInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorInterface resourceAdaptorInterface11)
  {
    super();
    
    this.description = resourceAdaptorInterface11.getDescription() == null ? null : resourceAdaptorInterface11.getDescription().getvalue();
    this.resourceAdaptorInterfaceName = resourceAdaptorInterface11.getResourceAdaptorInterfaceName().getvalue(); 
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getResourceAdaptorInterfaceName()
  {
    return resourceAdaptorInterfaceName;
  }

}
