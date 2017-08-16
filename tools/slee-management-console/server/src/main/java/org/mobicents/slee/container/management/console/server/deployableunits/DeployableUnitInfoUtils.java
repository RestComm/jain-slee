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

import java.io.File;

import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.components.ComponentInfoUtils;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitInfoUtils {

  static private ManagementConsole managementConsole = ManagementConsole.getInstance();

  static public String toString(DeployableUnitID deployableUnitID) {
    if (deployableUnitID == null)
      return null;

    managementConsole.getDeployableUnitIDMap().put(deployableUnitID);
    return deployableUnitID.toString();
  }

  static public DeployableUnitInfo toDeployableUnitInfo(DeployableUnitDescriptor deployableUnitDescriptor) {

    String id = toString(deployableUnitDescriptor.getID()); /*
                                                             * ammendonca ; if (deployableUnitDescriptor instanceof
                                                             * DeployableUnitDescriptorEx) id = toString(((DeployableUnitDescriptorEx)
                                                             * deployableUnitDescriptor) .getDeployableUnitID()); else id = null;
                                                             */

    return new DeployableUnitInfo(ComponentInfoUtils.toStringArray(deployableUnitDescriptor.getComponents()), deployableUnitDescriptor.getDeploymentDate(),
        deployableUnitDescriptor.getURL(), getDeployableUnitName(deployableUnitDescriptor), id);
  }

  static public DeployableUnitInfo[] toDeployableUnitInfos(DeployableUnitDescriptor[] deployableUnitDescriptors) {
    DeployableUnitInfo[] deployableUnitInfos = new DeployableUnitInfo[deployableUnitDescriptors.length];

    for (int i = 0; i < deployableUnitDescriptors.length; i++) {
      deployableUnitInfos[i] = toDeployableUnitInfo(deployableUnitDescriptors[i]);
    }
    return deployableUnitInfos;
  }

  static private String getDeployableUnitName(DeployableUnitDescriptor deployableUnitDescriptor) {
    File file = new File(deployableUnitDescriptor.getURL());
    String name = file.getName();

    if (name.endsWith(".jar"))
      name = name.substring(0, name.length() - ".jar".length());

    return name;
  }
}
