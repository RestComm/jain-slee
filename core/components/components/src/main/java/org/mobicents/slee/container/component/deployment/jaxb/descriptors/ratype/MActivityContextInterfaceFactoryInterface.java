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
 * MActivityContextInterfaceFactoryInterface.java
 *
 * <br>Project:  mobicents
 * <br>5:32:07 PM Jan 21, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a> 
 */
public class MActivityContextInterfaceFactoryInterface
{
  
  private String description;
  private String activityContextInterfaceFactoryInterfaceName;

  public MActivityContextInterfaceFactoryInterface(org.mobicents.slee.container.component.deployment.jaxb.slee.ratype.ActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface10)
  { 
    this.description = activityContextInterfaceFactoryInterface10.getDescription() == null ? null : activityContextInterfaceFactoryInterface10.getDescription().getvalue();
    this.activityContextInterfaceFactoryInterfaceName = activityContextInterfaceFactoryInterface10.getActivityContextInterfaceFactoryInterfaceName().getvalue(); 
  }

  public MActivityContextInterfaceFactoryInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.ratype.ActivityContextInterfaceFactoryInterface activityContextInterfaceFactoryInterface11)
  {    
    this.description = activityContextInterfaceFactoryInterface11.getDescription() == null ? null : activityContextInterfaceFactoryInterface11.getDescription().getvalue();
    this.activityContextInterfaceFactoryInterfaceName = activityContextInterfaceFactoryInterface11.getActivityContextInterfaceFactoryInterfaceName().getvalue(); 
  }

  public String getDescription()
  {
    return description;
  }
  
  public String getActivityContextInterfaceFactoryInterfaceName()
  {
    return activityContextInterfaceFactoryInterfaceName;
  }
}
