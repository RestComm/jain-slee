package org.mobicents.slee.container.component.deployment.jaxb.descriptors.ra;

/**
 * 
 * MSecurityPermissions.java
 *
 * <br>Project:  mobicents
 * <br>8:03:24 PM Jan 22, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MSecurityPermissions {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.SecurityPermissions securityPermissions11;
  
  private String description;
  private String securityPermissionSpec;

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.SecurityPermissions securityPermissions11)
  {
    this.securityPermissions11 = securityPermissions11;
    
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();
    
    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }
  
  public String getDescription()
  {
    return description;
  }
  
  public String getSecurityPermissionSpec()
  {
    return securityPermissionSpec;
  }
}
