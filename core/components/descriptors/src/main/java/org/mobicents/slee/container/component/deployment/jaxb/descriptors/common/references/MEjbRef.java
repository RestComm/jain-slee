package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

/**
 * Start time:15:15:53 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEjbRef {

  private String description;

  private String ejbRefName;
  private String ejbRefType;

  private String home;
  private String remote;

  private String ejbLink;

  public MEjbRef(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EjbRef ejbRef10)
  {
    this.description = ejbRef10.getDescription() == null ? null : ejbRef10.getDescription().getvalue();

    this.ejbRefName = ejbRef10.getEjbRefName().getvalue();
    this.ejbRefType = ejbRef10.getEjbRefType().getvalue();

    this.home = ejbRef10.getHome().getvalue();
    this.remote = ejbRef10.getRemote().getvalue();

    //Optional, removed in 1.1
    this.ejbLink = ejbRef10.getEjbLink() == null ? null : ejbRef10.getEjbLink().getvalue();
  }

  public MEjbRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EjbRef ejbRef11)
  {
    this.description = ejbRef11.getDescription() == null ? null : ejbRef11.getDescription().getvalue();

    this.ejbRefName = ejbRef11.getEjbRefName().getvalue();
    this.ejbRefType = ejbRef11.getEjbRefType().getvalue();

    this.home = ejbRef11.getHome().getvalue();
    this.remote = ejbRef11.getRemote().getvalue();

  }

  public String getDescription()
  {
    return description;
  }

  public String getEjbRefName()
  {
    return ejbRefName;
  }

  public String getEjbRefType()
  {
    return ejbRefType;
  }

  public String getHome()
  {
    return home;
  }

  public String getRemote()
  {
    return remote;
  }

  public String getEjbLink()
  {
    return ejbLink;
  }

}
