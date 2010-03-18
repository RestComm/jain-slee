package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:17:30:02 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQueryParameter {

  private String name;
  private String type;

  public MQueryParameter(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryParameter queryParameter11)
  {
    this.name = queryParameter11.getName();
    this.type = queryParameter11.getType();
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

}
