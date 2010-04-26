package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import org.mobicents.slee.container.component.UsageParameterDescriptor;

/**
 * 
 * MUsageParameter.java
 * 
 * <br>
 * Project: mobicents <br>
 * 5:47:05 PM Jan 22, 2009 <br>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MUsageParameter implements UsageParameterDescriptor {

  private String name;
  private boolean notificationsEnabled = false;

  public MUsageParameter(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.UsageParameter usageParameter11)
  {
    this.name = usageParameter11.getName();
    this.notificationsEnabled = Boolean.parseBoolean(usageParameter11.getNotificationsEnabled());
  }

  public MUsageParameter(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.UsageParameter usageParameter11)
  {
    this.name = usageParameter11.getName();
    this.notificationsEnabled = Boolean.parseBoolean(usageParameter11.getNotificationsEnabled());
  }

  public MUsageParameter(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.UsageParameter usageParameter11)
  {
    this.name = usageParameter11.getName();
    this.notificationsEnabled = Boolean.parseBoolean(usageParameter11.getNotificationsEnabled());
  }

  public String getName() {
    return name;
  }

  public boolean getNotificationsEnabled() {
    return notificationsEnabled;
  }

}
