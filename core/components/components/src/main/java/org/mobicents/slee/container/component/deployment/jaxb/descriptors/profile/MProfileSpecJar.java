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

import java.util.ArrayList;
import java.util.List;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;

/**
 * 
 * MProfileSpecJar.java
 *
 * <br>Project:  mobicents
 * <br>12:17:40 PM Feb 16, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MProfileSpecJar {

  private String description;
  private List<MProfileSpec> profileSpec = new ArrayList<MProfileSpec>();
  private MSecurityPermissions securityPermissions;
  
  public MProfileSpecJar(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpecJar profileSpecJar10)
  {
    this.description = profileSpecJar10.getDescription() == null ? null : profileSpecJar10.getDescription().getvalue();
    
    for(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileSpec profileSpec10 : profileSpecJar10.getProfileSpec())
    {
      this.profileSpec.add( new MProfileSpec(profileSpec10) );
    }
    
  }

  public MProfileSpecJar(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecJar profileSpecJar11)
  {
    this.description = profileSpecJar11.getDescription() == null ? null : profileSpecJar11.getDescription().getvalue();

    for(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpec profileSpec11 : profileSpecJar11.getProfileSpec())
    {
      this.profileSpec.add( new MProfileSpec(profileSpec11) );
    }

    if(profileSpecJar11.getSecurityPermissions()!=null)
    	this.securityPermissions = new MSecurityPermissions(profileSpecJar11.getSecurityPermissions());
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public List<MProfileSpec> getProfileSpec()
  {
    return profileSpec;
  }
  
  public MSecurityPermissions getSecurityPermissions()
  {
    return securityPermissions;
  }
}
