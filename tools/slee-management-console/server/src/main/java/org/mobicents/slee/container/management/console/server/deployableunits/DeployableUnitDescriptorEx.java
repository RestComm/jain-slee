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
