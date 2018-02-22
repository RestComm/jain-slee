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

package org.mobicents.slee.container.management.console.server.components;

import java.util.ArrayList;

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.SbbDescriptor;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.components.ComponentsService;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.ComponentSearchParams;
import org.mobicents.slee.container.management.console.client.components.info.ComponentTypeInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.mbeans.DeploymentMBeanUtils;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ComponentsServiceImpl extends RemoteServiceServlet implements ComponentsService {

  private static final long serialVersionUID = -8625898690734642466L;

  private ManagementConsole managementConsole = ManagementConsole.getInstance();

  private SleeMBeanConnection sleeConnection = managementConsole.getSleeConnection();

  private DeploymentMBeanUtils deploymentMBeanUtils = sleeConnection.getSleeManagementMBeanUtils().getDeploymentMBeanUtils();

  public ComponentInfo[] getComponents() throws ManagementConsoleException {
    ComponentDescriptor[] componentDescriptors= deploymentMBeanUtils.getComponentDescriptors();
    ComponentInfo[] componentInfos = new ComponentInfo[componentDescriptors.length];
	for (int i = 0; i < componentDescriptors.length; i++){
		componentInfos[i] = ComponentInfoUtils.toComponentInfo(componentDescriptors[i]);
		if(componentDescriptors[i] instanceof SbbDescriptor){
			String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)componentDescriptors[i].getID());
			((SbbInfo)componentInfos[i]).setEnvEntries(envEntries);
		}
	}
    return componentInfos;
  }

  public ComponentTypeInfo[] getComponentTypeInfos() throws ManagementConsoleException {

    ComponentTypeInfo[] componentTypeInfos = new ComponentTypeInfo[7];

    componentTypeInfos[0] = new ComponentTypeInfo(ComponentTypeInfo.EVENT_TYPE, deploymentMBeanUtils.getEventTypes().length);

    componentTypeInfos[1] = new ComponentTypeInfo(ComponentTypeInfo.PROFILE_SPECIFICATION, deploymentMBeanUtils.getProfileSpecifications().length);

    componentTypeInfos[2] = new ComponentTypeInfo(ComponentTypeInfo.SBB, deploymentMBeanUtils.getSbbs().length);

    componentTypeInfos[3] = new ComponentTypeInfo(ComponentTypeInfo.RESOURCE_ADAPTOR_TYPE, deploymentMBeanUtils.getResourceAdaptorTypes().length);

    componentTypeInfos[4] = new ComponentTypeInfo(ComponentTypeInfo.RESOURCE_ADAPTOR, deploymentMBeanUtils.getResourceAdaptors().length);

    componentTypeInfos[5] = new ComponentTypeInfo(ComponentTypeInfo.SERVICE, deploymentMBeanUtils.getServices().length);

    componentTypeInfos[6] = new ComponentTypeInfo(ComponentTypeInfo.LIBRARY, deploymentMBeanUtils.getLibraries().length);

    return componentTypeInfos;
  }

  public ComponentInfo[] getComponentInfos(ComponentTypeInfo componentTypeInfo) throws ManagementConsoleException {

    ComponentID[] componentIDs;
    ComponentDescriptor[] componentDescriptors;
    ComponentInfo[] componentInfos;

    if (componentTypeInfo.getType().equals(ComponentTypeInfo.EVENT_TYPE)) {
      componentIDs = deploymentMBeanUtils.getEventTypes();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.PROFILE_SPECIFICATION)) {
      componentIDs = deploymentMBeanUtils.getProfileSpecifications();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.RESOURCE_ADAPTOR)) {
      componentIDs = deploymentMBeanUtils.getResourceAdaptors();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.RESOURCE_ADAPTOR_TYPE)) {
      componentIDs = deploymentMBeanUtils.getResourceAdaptorTypes();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.SBB)) {
      componentIDs = deploymentMBeanUtils.getSbbs();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.SERVICE)) {
      componentIDs = deploymentMBeanUtils.getServices();
    }
    else if (componentTypeInfo.getType().equals(ComponentTypeInfo.LIBRARY)) {
      componentIDs = deploymentMBeanUtils.getLibraries();
    }
    else {
      throw new ManagementConsoleException("Unknown component type " + componentTypeInfo.getType());
    }
    componentDescriptors = deploymentMBeanUtils.getDescriptors(componentIDs);
    componentInfos = new ComponentInfo[componentDescriptors.length];
	for (int i = 0; i < componentDescriptors.length; i++){
		componentInfos[i] = ComponentInfoUtils.toComponentInfo(componentDescriptors[i]);
		if(componentDescriptors[i] instanceof SbbDescriptor){
			String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)componentDescriptors[i].getID());
			((SbbInfo)componentInfos[i]).setEnvEntries(envEntries);
		}
	}
    return componentInfos;
  }

  public ComponentInfo getComponentInfo(String id) throws ManagementConsoleException {
    ComponentID componentID = managementConsole.getComponentIDMap().get(id);
    ComponentDescriptor componentDescriptor = deploymentMBeanUtils.getDescriptor(componentID);
    ComponentInfo componentInfo = null;
    componentInfo = ComponentInfoUtils.toComponentInfo(componentDescriptor);
	if(componentDescriptor instanceof SbbDescriptor){
		String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)componentDescriptor.getID());
		((SbbInfo)componentInfo).setEnvEntries(envEntries);
	}
    return componentInfo;
  }

  public ComponentInfo[] searchComponents(String text) throws ManagementConsoleException {
    if (text == null || text.trim().equals(""))
      return null;

    String lowerCaseText = text.toLowerCase();

    ComponentDescriptor[] allComponentDescriptors = deploymentMBeanUtils.getComponentDescriptors();
    ArrayList<ComponentDescriptor> resultComponentDescriptorArrayList = new ArrayList<ComponentDescriptor>();
    ComponentDescriptor[] resultComponentDescriptors;
    ComponentInfo[] resultComponentInfos;

    for (int i = 0; i < allComponentDescriptors.length; i++) {
      ComponentDescriptor componentDescriptor = allComponentDescriptors[i];
      if (componentDescriptor.getName() != null && componentDescriptor.getName().toLowerCase().indexOf(lowerCaseText) > -1)
        resultComponentDescriptorArrayList.add(componentDescriptor);
      else if (componentDescriptor.getVendor() != null && componentDescriptor.getVendor().toLowerCase().indexOf(lowerCaseText) > -1)
        resultComponentDescriptorArrayList.add(componentDescriptor);
    }

    if (resultComponentDescriptorArrayList.size() == 0)
      return null;

    resultComponentDescriptors = new ComponentDescriptor[resultComponentDescriptorArrayList.size()];
    resultComponentDescriptors = resultComponentDescriptorArrayList.toArray(resultComponentDescriptors);

    resultComponentInfos = new ComponentInfo[resultComponentDescriptors.length];
	for (int i = 0; i < resultComponentDescriptors.length; i++){
		resultComponentInfos[i] = ComponentInfoUtils.toComponentInfo(resultComponentDescriptors[i]);
		if(resultComponentDescriptors[i] instanceof SbbDescriptor){
			String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)resultComponentDescriptors[i].getID());
			((SbbInfo)resultComponentInfos[i]).setEnvEntries(envEntries);
		}
	}
    return resultComponentInfos;
  }

  public ComponentInfo[] searchComponents(ComponentSearchParams params) throws ManagementConsoleException {

    ComponentDescriptor[] allComponentDescriptors = deploymentMBeanUtils.getComponentDescriptors();
    ArrayList<ComponentDescriptor> resultComponentDescriptorArrayList = new ArrayList<ComponentDescriptor>();
    ComponentDescriptor[] resultComponentDescriptors;
    ComponentInfo[] resultComponentInfos;

    for (int i = 0; i < allComponentDescriptors.length; i++) {
      ComponentDescriptor componentDescriptor = allComponentDescriptors[i];

      if (params.matches(componentDescriptor.getName(), componentDescriptor.getID().toString(), componentDescriptor.getVendor(),
          componentDescriptor.getVersion()))
        resultComponentDescriptorArrayList.add(componentDescriptor);
    }

    if (resultComponentDescriptorArrayList.size() == 0)
      return null;

    resultComponentDescriptors = new ComponentDescriptor[resultComponentDescriptorArrayList.size()];
    resultComponentDescriptors = resultComponentDescriptorArrayList.toArray(resultComponentDescriptors);

    resultComponentInfos = new ComponentInfo[resultComponentDescriptors.length];
	for (int i = 0; i < resultComponentDescriptors.length; i++){
		resultComponentInfos[i] = ComponentInfoUtils.toComponentInfo(resultComponentDescriptors[i]);
		if(resultComponentDescriptors[i] instanceof SbbDescriptor){
			String[] envEntries = deploymentMBeanUtils.getEnvEntries((ComponentID)resultComponentDescriptors[i].getID());
			((SbbInfo)resultComponentInfos[i]).setEnvEntries(envEntries);
		}
	}
    return resultComponentInfos;
  }

  public String[] getReferringComponents(String id) throws ManagementConsoleException {
    ComponentID componentID = managementConsole.getComponentIDMap().get(id);
    ComponentID[] componentIDs = deploymentMBeanUtils.getReferringComponents(componentID);
    return ComponentInfoUtils.toStringArray(componentIDs);
  }

  public String getComponentName(String id) throws ManagementConsoleException {
    ComponentID componentID = managementConsole.getComponentIDMap().get(id);
    ComponentDescriptor componentDescriptor = deploymentMBeanUtils.getDescriptor(componentID);
    return componentDescriptor.getID().toString();
  }

}
