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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:00:48 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
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
