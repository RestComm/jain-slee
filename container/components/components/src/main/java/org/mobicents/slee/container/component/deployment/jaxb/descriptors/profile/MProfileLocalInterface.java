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

package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.profile.ProfileLocalInterfaceDescriptor;

/**
 * Start time:16:56:24 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileLocalInterface implements ProfileLocalInterfaceDescriptor {

  private boolean isolateSecurityPermissions = false;

  private String description;

  private String profileLocalInterfaceName;

  public MProfileLocalInterface(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.ProfileLocalInterface profileLocalInterface11)
  {

    this.description = profileLocalInterface11.getDescription() == null ? null : profileLocalInterface11.getDescription().getvalue();

    this.profileLocalInterfaceName = profileLocalInterface11.getProfileLocalInterfaceName().getvalue();

    this.isolateSecurityPermissions = Boolean.parseBoolean(profileLocalInterface11.getIsolateSecurityPermissions());
  }

  public boolean isIsolateSecurityPermissions() {
    return isolateSecurityPermissions;
  }

  public String getDescription() {
    return description;
  }

  public String getProfileLocalInterfaceName() {
    return profileLocalInterfaceName;
  }

}
