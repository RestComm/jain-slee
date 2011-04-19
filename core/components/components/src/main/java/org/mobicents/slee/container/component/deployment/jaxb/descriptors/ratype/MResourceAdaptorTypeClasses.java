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

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * MResourceAdaptorTypeClasses.java
 *
 * <br>Project:  mobicents
 * <br>6:15:52 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MResourceAdaptorTypeClasses
{
  
  private String description;
  private List<String> activityType;
  private MActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface;
  private MResourceAdaptorInterface resourceAdaptorInterface;

  public MResourceAdaptorTypeClasses(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses10)
  {
    
    this.description = resourceAdaptorTypeClasses10.getDescription() == null ? null : resourceAdaptorTypeClasses10.getDescription().getvalue();
    this.activityType = new ArrayList<String>();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityType aT : resourceAdaptorTypeClasses10.getActivityType())
    {
      this.activityType.add( new MActivityType(aT).getActivityTypeName() );
    }
    
    this.resourceAdaptorInterface = resourceAdaptorTypeClasses10.getResourceAdaptorInterface() == null ? null : new MResourceAdaptorInterface(resourceAdaptorTypeClasses10.getResourceAdaptorInterface());
    this.activityContextInterfaceFactoryInterface = resourceAdaptorTypeClasses10.getActivityContextInterfaceFactoryInterface() == null ? null : new MActivityContextInterfaceFactoryInterface(resourceAdaptorTypeClasses10.getActivityContextInterfaceFactoryInterface());
  }

  public MResourceAdaptorTypeClasses(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ResourceAdaptorTypeClasses resourceAdaptorTypeClasses11)
  {
    
    this.description = resourceAdaptorTypeClasses11.getDescription() == null ? null : resourceAdaptorTypeClasses11.getDescription().getvalue();
    this.activityType = new ArrayList<String>();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityType aT : resourceAdaptorTypeClasses11.getActivityType())
    {
      this.activityType.add( new MActivityType(aT).getActivityTypeName() );
    }
    
    this.resourceAdaptorInterface = resourceAdaptorTypeClasses11.getResourceAdaptorInterface() == null ? null : new MResourceAdaptorInterface(resourceAdaptorTypeClasses11.getResourceAdaptorInterface());
    this.activityContextInterfaceFactoryInterface = resourceAdaptorTypeClasses11.getActivityContextInterfaceFactoryInterface() == null ? null : new MActivityContextInterfaceFactoryInterface(resourceAdaptorTypeClasses11.getActivityContextInterfaceFactoryInterface());
   }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<String> getActivityType()
  {
    return activityType;
  }
  
  public MActivityContextInterfaceFactoryInterface getActivityContextInterfaceFactoryInterface()
  {
    return activityContextInterfaceFactoryInterface;
  }
  
  public MResourceAdaptorInterface getResourceAdaptorInterface()
  {
    return resourceAdaptorInterface;
  }
}
