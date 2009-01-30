package org.mobicents.slee.container.component.deployment.jaxb.descriptors.library;

/**
 * 
 * MSecurityPermissions.java
 *
 * <br>Project:  mobicents
 * <br>3:38:15 AM Jan 30, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a> 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MSecurityPermissions {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.library.SecurityPermissions securityPermissions11;
  
  private String description;
  private String securityPermissionSpec;

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.SecurityPermissions securityPermissions11)
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
