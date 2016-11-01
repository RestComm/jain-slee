/*
 * JBoss, Home of Professional Open Source
 * Copyright 2003-2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.slee.container.management.console.server.deployableunits;

import java.util.Date;

import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitDescriptorEx extends DeployableUnitDescriptor {

  private static final long serialVersionUID = -8136616077143508585L;

  DeployableUnitDescriptor deployableUnitDescriptor;

  DeployableUnitID deployableUnitID;

  public DeployableUnitDescriptorEx(DeployableUnitDescriptor deployableUnitDescriptor, DeployableUnitID deployableUnitID) {
    super(deployableUnitID, new Date() /* FIXME */, deployableUnitDescriptor.getComponents());
    this.deployableUnitDescriptor = deployableUnitDescriptor;
    this.deployableUnitID = deployableUnitID;
  }

  public ComponentID[] getComponents() {
    return deployableUnitDescriptor.getComponents();
  }

  public Date getDeploymentDate() {
    return deployableUnitDescriptor.getDeploymentDate();
  }

  public String getURL() {
    return deployableUnitDescriptor.getURL();
  }

  public DeployableUnitID getDeployableUnitID() {
    return deployableUnitID;
  }

}
