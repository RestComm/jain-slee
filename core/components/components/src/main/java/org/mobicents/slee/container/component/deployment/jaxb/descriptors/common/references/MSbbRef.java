package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references;

import javax.slee.SbbID;

/**
 * Start time:10:25:19 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSbbRef {

  private String description;

  private String sbbName;
  private String sbbVendor;
  private String sbbVersion;

  private String sbbAlias;

  private SbbID sbbID;

  public MSbbRef(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SbbRef sbbRef11)
  {
    this.description = sbbRef11.getDescription() == null ? null : sbbRef11.getDescription().getvalue();

    this.sbbName = sbbRef11.getSbbName().getvalue();
    this.sbbVendor = sbbRef11.getSbbVendor().getvalue();
    this.sbbVersion = sbbRef11.getSbbVersion().getvalue();

    this.sbbAlias = sbbRef11.getSbbAlias().getvalue();

    this.sbbID = new SbbID(this.sbbName, this.sbbVendor, this.sbbVersion);
  }

  public MSbbRef(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.SbbRef sbbRef10)
  {
    this.description = sbbRef10.getDescription() == null ? null : sbbRef10.getDescription().getvalue();

    this.sbbName = sbbRef10.getSbbName().getvalue();
    this.sbbVendor = sbbRef10.getSbbVendor().getvalue();
    this.sbbVersion = sbbRef10.getSbbVersion().getvalue();

    this.sbbAlias = sbbRef10.getSbbAlias().getvalue();

    this.sbbID = new SbbID(this.sbbName, this.sbbVendor, this.sbbVersion);
  }

  public String getDescription()
  {
    return description;
  }

  public String getSbbName()
  {
    return sbbName;
  }

  public String getSbbVendor()
  {
    return sbbVendor;
  }

  public String getSbbVersion()
  {
    return sbbVersion;
  }

  public String getSbbAlias()
  {
    return sbbAlias;
  }

  public SbbID getComponentID()
  {
    return this.sbbID;
  }

}
