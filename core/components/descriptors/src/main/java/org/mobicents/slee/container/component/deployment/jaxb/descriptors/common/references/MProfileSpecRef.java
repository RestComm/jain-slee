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
}
