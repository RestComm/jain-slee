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
