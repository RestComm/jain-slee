package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:11:51:45 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MHasPrefix {

  private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.HasPrefix hasPrefix11;

  private String attributeName;
  private String value;
  private String parameter;
  private String collatorRef;

  public MHasPrefix(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.HasPrefix hasPrefix11)
  {
    this.hasPrefix11 = hasPrefix11;
    
    this.attributeName = hasPrefix11.getAttributeName();
    this.value = hasPrefix11.getValue();
    this.parameter = hasPrefix11.getParameter();
    this.collatorRef = hasPrefix11.getCollatorRef();
  }

  public String getAttributeName()
  {
    return attributeName;
  }

  public String getValue()
  {
    return value;
  }

  public String getParameter()
  {
    return parameter;
  }

  public String getCollatorRef()
  {
    return collatorRef;
  }
}
