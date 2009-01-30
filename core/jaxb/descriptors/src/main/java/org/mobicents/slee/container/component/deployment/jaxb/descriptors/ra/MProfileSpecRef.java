package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

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
  
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ProfileSpecRef profileSpecRef11;
  
  protected String description;
  protected String profileSpecName;
  protected String profileSpecVendor;
  protected String profileSpecVersion;

  
  public MProfileSpecRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.ProfileSpecRef profileSpecRef11)
  {
    this.profileSpecRef11 = profileSpecRef11;
    
    this.description = profileSpecRef11.getDescription() == null ? null : profileSpecRef11.getDescription().getvalue();
    
    this.profileSpecName = profileSpecRef11.getProfileSpecName().getvalue();
    this.profileSpecVendor = profileSpecRef11.getProfileSpecVendor().getvalue();
    this.profileSpecVersion = profileSpecRef11.getProfileSpecVersion().getvalue();
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

}
