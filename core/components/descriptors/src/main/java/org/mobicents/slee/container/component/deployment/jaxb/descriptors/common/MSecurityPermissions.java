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

  public MSecurityPermissions(String description, String securityPermissionSpec)
  {
    this.description = description;
    this.securityPermissionSpec = securityPermissionSpec;
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
