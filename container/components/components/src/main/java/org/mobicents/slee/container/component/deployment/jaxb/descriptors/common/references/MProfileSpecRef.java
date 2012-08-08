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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;

/**
 * 
 * MProfileSpecRef.java
 *
 * <br>Project:  mobicents
 * <br>6:55:46 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MProfileSpecRef implements ProfileSpecRefDescriptor {

  private String profileSpecAlias;

  private ProfileSpecificationID profileSpecificationID;
  
  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ProfileSpecRef profileSpecRef11) {
    
    String profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
    String profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
    String profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();

    this.profileSpecificationID = new ProfileSpecificationID(profileSpecName, profileSpecVendor, profileSpecVersion);
  }
  
  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ProfileSpecRef profileSpecRef10)
  {
    
	  String profileSpecName = profileSpecRef10.getProfileSpecName().getvalue();
	  String profileSpecVendor = profileSpecRef10.getProfileSpecVendor().getvalue();
	  String profileSpecVersion = profileSpecRef10.getProfileSpecVersion().getvalue();
    
    // Mandatory in JAIN SLEE 1.0
    this.profileSpecAlias = profileSpecRef10.getProfileSpecAlias().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(profileSpecName, profileSpecVendor, profileSpecVersion);
  }

  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ProfileSpecRef profileSpecRef11)
  {
    
	  String profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
	  String profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
	  String profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();
    
    // Optional (deprecated) in JAIN SLEE 1.1
    this.profileSpecAlias = profileSpecRef11.getProfileSpecAlias() == null ? null : profileSpecRef11.getProfileSpecAlias().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(profileSpecName, profileSpecVendor, profileSpecVersion);
  }

  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecRef profileSpecRef11)
  {
    
	  String profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
	  String profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
	  String profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(profileSpecName, profileSpecVendor, profileSpecVersion);
  }

  public String getProfileSpecAlias()
  {
    return profileSpecAlias;
  }

  public ProfileSpecificationID getComponentID()
  {
    return this.profileSpecificationID;
  }

@Override
public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
			+ ((profileSpecAlias == null) ? 0 : profileSpecAlias.hashCode());
	result = prime
			* result
			+ ((profileSpecificationID == null) ? 0 : profileSpecificationID
					.hashCode());
	return result;
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	MProfileSpecRef other = (MProfileSpecRef) obj;
	if (profileSpecAlias == null) {
		if (other.profileSpecAlias != null)
			return false;
	} else if (!profileSpecAlias.equals(other.profileSpecAlias))
		return false;	
	if (profileSpecificationID == null) {
		if (other.profileSpecificationID != null)
			return false;
	} else if (!profileSpecificationID.equals(other.profileSpecificationID))
		return false;
	return true;
}  
  
}
