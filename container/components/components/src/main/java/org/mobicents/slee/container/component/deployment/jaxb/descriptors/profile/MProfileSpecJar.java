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
