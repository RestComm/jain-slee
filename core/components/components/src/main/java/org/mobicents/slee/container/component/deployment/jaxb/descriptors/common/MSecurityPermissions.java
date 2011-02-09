package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSecurityPermissions {

  private String description;
  private String securityPermissionSpec;

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public String getSecurityPermissionSpec()
  {
    return securityPermissionSpec;
  }

  public String getDescription()
  {
    return description;
  }

}
