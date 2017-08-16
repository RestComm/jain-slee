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

/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MSecurityPermissions {

  private String description;
  private String securityPermissionSpec;

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.ra.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public MSecurityPermissions(org.mobicents.slee.container.component.deployment.jaxb.slee11.library.SecurityPermissions securityPermissions11)
  {
    this.description = securityPermissions11.getDescription() == null ? null : securityPermissions11.getDescription().getvalue();

    this.securityPermissionSpec = securityPermissions11.getSecurityPermissionSpec().getvalue();
  }

  public String getSecurityPermissionSpec()
  {
    return securityPermissionSpec;
  }

  public String getDescription()
  {
    return description;
  }

}
