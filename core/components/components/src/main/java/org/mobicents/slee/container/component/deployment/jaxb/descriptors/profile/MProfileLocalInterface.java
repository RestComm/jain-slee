package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileLocalInterface {

  private boolean isolateSecurityPermissions = false;

  private String description;

  private String profileLocalInterfaceName;

  public MProfileLocalInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileLocalInterface profileLocalInterface11)
  {

    this.description = profileLocalInterface11.getDescription() == null ? null : profileLocalInterface11.getDescription().getvalue();

    this.profileLocalInterfaceName = profileLocalInterface11.getProfileLocalInterfaceName().getvalue();

    this.isolateSecurityPermissions = Boolean.parseBoolean(profileLocalInterface11.getIsolateSecurityPermissions());
  }

  public boolean isIsolateSecurityPermissions() {
    return isolateSecurityPermissions;
  }

  public String getDescription() {
    return description;
  }

  public String getProfileLocalInterfaceName() {
    return profileLocalInterfaceName;
  }

}
