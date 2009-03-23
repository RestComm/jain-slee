package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:00:48 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileManagementInterface {

  private String description;
  private String profileManagementInterfaceName;

  public MProfileManagementInterface(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementInterfaceName profileManagementInterface10)
  {
    this.profileManagementInterfaceName = profileManagementInterface10.getvalue();
  }

  public MProfileManagementInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileManagementInterface profileManagementInterface11)
  {
    this.description = profileManagementInterface11.getDescription() == null ? null : profileManagementInterface11.getDescription().getvalue();

    this.profileManagementInterfaceName = profileManagementInterface11.getProfileManagementInterfaceName().getvalue();
  }

  public String getDescription()
  {
    return description;
  }

  public String getProfileManagementInterfaceName()
  {
    return profileManagementInterfaceName;
  }

}
