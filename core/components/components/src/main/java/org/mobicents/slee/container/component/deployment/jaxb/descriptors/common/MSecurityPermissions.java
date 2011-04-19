/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

/**
 * Start time:23:53:54 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
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
