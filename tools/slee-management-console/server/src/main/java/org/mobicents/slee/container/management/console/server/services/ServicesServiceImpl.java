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

package org.mobicents.slee.container.management.console.server.services;

import java.util.ArrayList;

import javax.slee.SbbID;
import javax.slee.ComponentID;
import javax.slee.ServiceID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.ServiceDescriptor;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.services.ServiceInfoHeader;
import org.mobicents.slee.container.management.console.client.services.ServicesService;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.components.ComponentInfoUtils;
import org.mobicents.slee.container.management.console.server.mbeans.DeploymentMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.ServiceManagementMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServicesServiceImpl extends RemoteServiceServlet implements ServicesService {

  private static final long serialVersionUID = 5980141473412768257L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private DeploymentMBeanUtils deploymentMBeanUtils = sleeConnection.getSleeManagementMBeanUtils().getDeploymentMBeanUtils();

  private ServiceManagementMBeanUtils serviceManagementMBeanUtils = sleeConnection.getSleeManagementMBeanUtils().getServiceManagementMBeanUtils();

  public ServiceInfoHeader[] getServiceInfoHeaders() throws ManagementConsoleException {

    ServiceID[] serviceIDs = deploymentMBeanUtils.getServices();
    ArrayList<ServiceInfoHeader> serviceInfoHeadersArrayList = new ArrayList<ServiceInfoHeader>(serviceIDs.length);
    ServiceInfoHeader[] serviceInfoHeaders = new ServiceInfoHeader[serviceIDs.length];

    for (int i = 0; i < serviceIDs.length; i++) {
      ServiceDescriptor serviceDescriptor = (ServiceDescriptor) deploymentMBeanUtils.getDescriptor(serviceIDs[i]);

      ServiceState serviceState = serviceManagementMBeanUtils.getState(serviceIDs[i]);

      ServiceInfoHeader serviceInfoHeader = ServiceInfoUtils.toServiceInfoHeader(serviceDescriptor, serviceState);

      serviceInfoHeadersArrayList.add(serviceInfoHeader);
    }

    serviceInfoHeaders = serviceInfoHeadersArrayList.toArray(serviceInfoHeaders);

    return serviceInfoHeaders;
  }

  public void activate(String id) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(id);
    serviceManagementMBeanUtils.activate(serviceID);
  }

  public void deactivate(String id) throws ManagementConsoleException {
    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(id);
    serviceManagementMBeanUtils.deactivate(serviceID);
  }

  private SbbID[] getUsedSbbs(SbbID sbbID) throws ManagementConsoleException {
    SbbDescriptor sbbDescriptor = (SbbDescriptor) deploymentMBeanUtils.getDescriptor(sbbID);

    return sbbDescriptor.getSbbs();
  }

  private void addUsedSbbs(ArrayList<SbbID> resultSbbIDs, SbbID[] sbbIDs) throws ManagementConsoleException {
    if (sbbIDs == null)
      return;

    for (int i = 0; i < sbbIDs.length; i++) {
      resultSbbIDs.add(sbbIDs[i]);
      addUsedSbbs(resultSbbIDs, getUsedSbbs(sbbIDs[i]));
    }
  }

  public ComponentInfo[] getSbbsWithinService(String strServiceID) throws ManagementConsoleException {

    ServiceID serviceID = (ServiceID) managementConsole.getComponentIDMap().get(strServiceID);

    ServiceDescriptor serviceDescriptor = (ServiceDescriptor) deploymentMBeanUtils.getDescriptor(serviceID);

    ArrayList<SbbID> resultSbbIDArrayList = new ArrayList<SbbID>();
    addUsedSbbs(resultSbbIDArrayList, new SbbID[] { serviceDescriptor.getRootSbb() });

    if (resultSbbIDArrayList.size() == 0)
      return null;

    SbbID[] referringSbbIDs = new SbbID[resultSbbIDArrayList.size()];
    referringSbbIDs = resultSbbIDArrayList.toArray(referringSbbIDs);

    ComponentDescriptor[] referringSbbComponentDescriptors = deploymentMBeanUtils.getDescriptors(referringSbbIDs);
    ComponentInfo[] componentInfos = new ComponentInfo[referringSbbComponentDescriptors.length];
	for (int i = 0; i < referringSbbComponentDescriptors.length; i++){
		componentInfos[i] = ComponentInfoUtils.toComponentInfo(referringSbbComponentDescriptors[i]);
		if(referringSbbComponentDescriptors[i] instanceof SbbDescriptor){
			String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)referringSbbComponentDescriptors[i].getID());
			((SbbInfo)componentInfos[i]).setEnvEntries(envEntries);
		}
	}
    return componentInfos;
  }
}
