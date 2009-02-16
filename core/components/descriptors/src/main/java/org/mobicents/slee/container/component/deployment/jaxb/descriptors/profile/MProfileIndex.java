package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;

/**
 * Represents indexed attribute from slee 1.0 specs.
 * Start time:23:37:29 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileIndex {

  private String name;

  private boolean unique;

  public MProfileIndex(ProfileIndex profileIndex10)
  {
    this.name = profileIndex10.getvalue();
    this.unique = Boolean.parseBoolean(profileIndex10.getUnique());
  }

  public String getName()
  {
    return name;
  }

  public boolean getUnique()
  {
    return unique;
  }
}
