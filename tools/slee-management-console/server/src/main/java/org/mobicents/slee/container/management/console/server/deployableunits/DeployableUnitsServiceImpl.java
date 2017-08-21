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

import java.util.ArrayList;

import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitInfo;
import org.mobicents.slee.container.management.console.client.deployableunits.DeployableUnitsService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.DeploymentMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class DeployableUnitsServiceImpl extends RemoteServiceServlet implements DeployableUnitsService {

  private static final long serialVersionUID = 407304756120559074L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private DeploymentMBeanUtils deploymentMBeanUtils = sleeConnection.getSleeManagementMBeanUtils().getDeploymentMBeanUtils();

  public DeployableUnitInfo[] getDeployableUnits() throws ManagementConsoleException {
    DeployableUnitInfo[] deployableUnitInfos = DeployableUnitInfoUtils.toDeployableUnitInfos(deploymentMBeanUtils.getDeployableUnitDescriptors());

    return deployableUnitInfos;
  }

  public DeployableUnitInfo[] searchDeployableUnits(String text) throws ManagementConsoleException {
    if (text == null || text.trim().equals(""))
      return null;

    String lowerCaseText = text.toLowerCase();
    DeployableUnitDescriptor[] allDeployableUnitDescriptor = deploymentMBeanUtils.getDeployableUnitDescriptors();
    ArrayList<DeployableUnitDescriptor> resultDeployableUnitDescriptorArrayList = new ArrayList<DeployableUnitDescriptor>();
    DeployableUnitDescriptor[] resultDeployableUnitDescriptors;
    DeployableUnitInfo[] resultDeployableUnitInfos;

    for (int i = 0; i < allDeployableUnitDescriptor.length; i++) {
      DeployableUnitDescriptor deployableUnitDescriptor = allDeployableUnitDescriptor[i];
      if (deployableUnitDescriptor.getURL() != null && deployableUnitDescriptor.getURL().toLowerCase().indexOf(lowerCaseText) > -1)
        resultDeployableUnitDescriptorArrayList.add(deployableUnitDescriptor);
    }

    if (resultDeployableUnitDescriptorArrayList.size() == 0)
      return null;

    resultDeployableUnitDescriptors = new DeployableUnitDescriptor[resultDeployableUnitDescriptorArrayList.size()];
    resultDeployableUnitDescriptors = resultDeployableUnitDescriptorArrayList.toArray(resultDeployableUnitDescriptors);

    resultDeployableUnitInfos = DeployableUnitInfoUtils.toDeployableUnitInfos(resultDeployableUnitDescriptors);

    return resultDeployableUnitInfos;
  }

  public void uninstall(String id) throws ManagementConsoleException {
    DeployableUnitID deployableUnitID = managementConsole.getDeployableUnitIDMap().get(id);
    deploymentMBeanUtils.uninstall(deployableUnitID);
  }

  public String getDeployableUnitName(String id) throws ManagementConsoleException {
    DeployableUnitID deployableUnitID = managementConsole.getDeployableUnitIDMap().get(id);
    DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBeanUtils.getDescriptor(deployableUnitID);
    return DeployableUnitInfoUtils.toDeployableUnitInfo(deployableUnitDescriptor).getName();
  }
}
