/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import org.mobicents.slee.container.component.UsageParameterDescriptor;

/**
 * 
 * MUsageParameter.java
 * 
 * <br>
 * Project: restcomm <br>
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
