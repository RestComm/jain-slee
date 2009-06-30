package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:09:19 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileAbstractClass {

  private String description;
  private String profileAbstractClassName;

  private boolean reentrant = false;

  public MProfileAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementAbstractClassName profileManagementAbstractClassName10)
  {
    this.profileAbstractClassName = profileManagementAbstractClassName10.getvalue();
  }

  public MProfileAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileAbstractClass profileAbstractClass11)
  {
    this.description = profileAbstractClass11.getDescription() == null ? null : profileAbstractClass11.getDescription().getvalue();

    this.reentrant = Boolean.parseBoolean(profileAbstractClass11.getReentrant());

    this.profileAbstractClassName = profileAbstractClass11.getProfileAbstractClassName().getvalue();
  }

  public String getProfileAbstractClassName() {
    return profileAbstractClassName;
  }

  public String getDescription() {
    return description;
  }

  public boolean getReentrant() {
    return reentrant;
  }

}
