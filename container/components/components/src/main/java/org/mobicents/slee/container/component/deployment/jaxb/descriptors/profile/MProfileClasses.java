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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MUsageParametersInterface;

/**
 * 
 * MProfileClasses.java
 *
 * <br>Project:  mobicents
 * <br>2:28:35 AM Feb 14, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MProfileClasses {

  private String description;
  private MProfileCMPInterface profileCMPInterface;
  private MProfileManagementInterface profileManagementInterface;
  private MProfileAbstractClass profileAbstractClass;

  // JAIN SLEE 1.1 Only
  private MProfileLocalInterface profileLocalInterface;
  private MProfileTableInterface profileTableInterface;
  private MUsageParametersInterface profileUsageParameterInterface;

  public MProfileClasses(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileClasses profileClasses10)
  {
    this.description = profileClasses10.getDescription() == null ? null : profileClasses10.getDescription().getvalue();

    this.profileCMPInterface = new MProfileCMPInterface(profileClasses10.getProfileCmpInterfaceName());
    
    if(profileClasses10.getProfileManagementInterfaceName() != null)
      this.profileManagementInterface = new MProfileManagementInterface(profileClasses10.getProfileManagementInterfaceName());
    
    if(profileClasses10.getProfileManagementAbstractClassName() != null)
      this.profileAbstractClass = new MProfileAbstractClass(profileClasses10.getProfileManagementAbstractClassName()); 
  }

  public MProfileClasses(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileClasses profileClasses11)
  {
    this.description = profileClasses11.getDescription() == null ? null : profileClasses11.getDescription().getvalue();

    this.profileCMPInterface = new MProfileCMPInterface(profileClasses11.getProfileCmpInterface());
    if(profileClasses11.getProfileManagementInterface()!=null)
    	this.profileManagementInterface = new MProfileManagementInterface(profileClasses11.getProfileManagementInterface());
    if(profileClasses11.getProfileAbstractClass()!=null)
    	this.profileAbstractClass = new MProfileAbstractClass(profileClasses11.getProfileAbstractClass());

    if(profileClasses11.getProfileLocalInterface()!=null)
    	this.profileLocalInterface = new MProfileLocalInterface(profileClasses11.getProfileLocalInterface());
    if(profileClasses11.getProfileTableInterface()!=null)
    	this.profileTableInterface = new MProfileTableInterface(profileClasses11.getProfileTableInterface());
    if(profileClasses11.getProfileUsageParametersInterface()!=null)
    	this.profileUsageParameterInterface = new MUsageParametersInterface(profileClasses11.getProfileUsageParametersInterface());
  }

  public String getDescription()
  {
    return description;
  }

  public MProfileCMPInterface getProfileCMPInterface()
  {
    return profileCMPInterface;
  }

  public MProfileManagementInterface getProfileManagementInterface()
  {
    return profileManagementInterface;
  }

  public MProfileAbstractClass getProfileAbstractClass()
  {
    return profileAbstractClass;
  }

  public MProfileLocalInterface getProfileLocalInterface()
  {
    return profileLocalInterface;
  }

  public MProfileTableInterface getProfileTableInterface()
  {
    return profileTableInterface;
  }

  public MUsageParametersInterface getProfileUsageParameterInterface()
  {
    return profileUsageParameterInterface;
  }
}
