package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.profile.ProfileSpecificationID;

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
public class MProfileSpecRef {
  
  private String description;
  
  private String profileSpecName;
  private String profileSpecVendor;
  private String profileSpecVersion;

  private String profileSpecAlias;

  private ProfileSpecificationID profileSpecificationID;
  
  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ProfileSpecRef profileSpecRef11)
  {
    this.description = profileSpecRef11.getDescription() == null ? null : profileSpecRef11.getDescription().getvalue();
    
    this.profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
    this.profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
    this.profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();

    this.profileSpecificationID = new ProfileSpecificationID(this.profileSpecName, this.profileSpecVendor, this.profileSpecVersion);
  }
  
  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.ProfileSpecRef profileSpecRef10)
  {
    this.description = profileSpecRef10.getDescription() == null ? null : profileSpecRef10.getDescription().getvalue();
    
    this.profileSpecName = profileSpecRef10.getProfileSpecName().getvalue();
    this.profileSpecVendor = profileSpecRef10.getProfileSpecVendor().getvalue();
    this.profileSpecVersion = profileSpecRef10.getProfileSpecVersion().getvalue();
    
    // Mandatory in JAIN SLEE 1.0
    this.profileSpecAlias = profileSpecRef10.getProfileSpecAlias().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(this.profileSpecName, this.profileSpecVendor, this.profileSpecVersion);
  }

  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.ProfileSpecRef profileSpecRef11)
  {
    this.description = profileSpecRef11.getDescription() == null ? null : profileSpecRef11.getDescription().getvalue();
    
    this.profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
    this.profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
    this.profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();
    
    // Optional (deprecated) in JAIN SLEE 1.1
    this.profileSpecAlias = profileSpecRef11.getProfileSpecAlias() == null ? null : profileSpecRef11.getProfileSpecAlias().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(this.profileSpecName, this.profileSpecVendor, this.profileSpecVersion);
  }

  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileSpecRef profileSpecRef11)
  {
    this.description = profileSpecRef11.getDescription() == null ? null : profileSpecRef11.getDescription().getvalue();
    
    this.profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
    this.profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
    this.profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();
    
    this.profileSpecificationID = new ProfileSpecificationID(this.profileSpecName, this.profileSpecVendor, this.profileSpecVersion);
  }

  public String getDescription()
  {
    return description;
  }
  
  public String getProfileSpecName()
  {
    return profileSpecName;
  }
  
  public String getProfileSpecVendor()
  {
    return profileSpecVendor;
  }
  
  public String getProfileSpecVersion()
  {
    return profileSpecVersion;
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
			+ ((description == null) ? 0 : description.hashCode());
	result = prime * result
			+ ((profileSpecAlias == null) ? 0 : profileSpecAlias.hashCode());
	result = prime * result
			+ ((profileSpecName == null) ? 0 : profileSpecName.hashCode());
	result = prime * result
			+ ((profileSpecVendor == null) ? 0 : profileSpecVendor.hashCode());
	result = prime
			* result
			+ ((profileSpecVersion == null) ? 0 : profileSpecVersion.hashCode());
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
	if (description == null) {
		if (other.description != null)
			return false;
	} else if (!description.equals(other.description))
		return false;
	if (profileSpecAlias == null) {
		if (other.profileSpecAlias != null)
			return false;
	} else if (!profileSpecAlias.equals(other.profileSpecAlias))
		return false;
	if (profileSpecName == null) {
		if (other.profileSpecName != null)
			return false;
	} else if (!profileSpecName.equals(other.profileSpecName))
		return false;
	if (profileSpecVendor == null) {
		if (other.profileSpecVendor != null)
			return false;
	} else if (!profileSpecVendor.equals(other.profileSpecVendor))
		return false;
	if (profileSpecVersion == null) {
		if (other.profileSpecVersion != null)
			return false;
	} else if (!profileSpecVersion.equals(other.profileSpecVersion))
		return false;
	if (profileSpecificationID == null) {
		if (other.profileSpecificationID != null)
			return false;
	} else if (!profileSpecificationID.equals(other.profileSpecificationID))
		return false;
	return true;
}
  
  
  
}
