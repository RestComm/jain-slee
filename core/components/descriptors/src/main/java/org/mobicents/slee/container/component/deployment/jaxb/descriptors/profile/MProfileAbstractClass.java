package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:09:19 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileAbstractClass {

  // This is tricky.. it's the same thing but with different names, and SLEE 1.0 
  // doesn't have any attributes or elements. Choosed SLEE 1.1 name for class.
  private org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementAbstractClassName profileManagementAbstractClassName10;
  private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileAbstractClass profileAbstractClass11;

  private String description;
  private String profileAbstractClassName;

  private boolean reentrant = false;

  public MProfileAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileManagementAbstractClassName profileManagementAbstractClassName10)
  {
    this.profileManagementAbstractClassName10 = profileManagementAbstractClassName10;

    this.profileAbstractClassName = profileManagementAbstractClassName10.getvalue();
  }

  public MProfileAbstractClass(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileAbstractClass profileAbstractClass11)
  {
    this.profileAbstractClass11 = profileAbstractClass11;

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
