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

package org.mobicents.slee.container.management.console.server.mbeans;

import java.util.ArrayList;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.server.Logger;

/**
 * 
 * @author Stefano Zappaterra
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeploymentMBeanUtils {

  private MBeanServerConnection mbeanServer;

  private ObjectName deploymentMBean;

  public DeploymentMBeanUtils(MBeanServerConnection mbeanServer, ObjectName sleeManagementMBean) throws ManagementConsoleException {
    this.mbeanServer = mbeanServer;
    try {
      deploymentMBean = (ObjectName) mbeanServer.getAttribute(sleeManagementMBean, "DeploymentMBean");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  // This code does not work! Not all components are found.
  // public ComponentDescriptor[] getDescriptors()
  // throws ManagementConsoleException {
  // try {
  // ArrayList<ComponentID> arrayComponentIDs = new ArrayList<ComponentID>();
  //
  // DeployableUnitID[] deployableUnitIDs = (DeployableUnitID[]) mbeanServer
  // .getAttribute(deploymentMBean, "DeployableUnits");
  //
  // DeployableUnitDescriptor[] deployableUnitDescriptors =
  // (DeployableUnitDescriptor[]) mbeanServer
  // .invoke(deploymentMBean, "getDescriptors",
  // new Object[] { deployableUnitIDs },
  // new String[] { DeployableUnitID[].class.getName() });
  //
  // for (int i = 0; i < deployableUnitDescriptors.length; i++) {
  // // Logger.info(deployableUnitDescriptors[i].getURL());
  // DeployableUnitDescriptor deployableUnitDescriptor =
  // deployableUnitDescriptors[i];
  // ComponentID[] partialComponentIDs = deployableUnitDescriptor
  // .getComponents();
  //
  // for (int j = 0; j < partialComponentIDs.length; j++) {
  // // Logger.info(" " + partialComponentIDs[j].toString());
  // arrayComponentIDs.add(partialComponentIDs[j]);
  // }
  // }
  //
  // ComponentID[] componentIDs = new ComponentID[arrayComponentIDs
  // .size()];
  // componentIDs = (ComponentID[]) arrayComponentIDs
  // .toArray(componentIDs);
  // ManagementConsole.getInstance().getComponentIDMap().put(componentIDs);
  //
  // // This code does not work!
  // //ComponentDescriptor[] componentDescriptors =
  // //(ComponentDescriptor[]) mbeanServer .invoke(deploymentMBean,
  // //"getDescriptors", new Object[] { componentIDs }, new String[] {
  // //ComponentID[].class.getName() });
  //
  //
  // ComponentDescriptor[] componentDescriptors = new
  // ComponentDescriptor[componentIDs.length];
  // for (int i = 0; i < componentIDs.length; i++) {
  // componentDescriptors[i] = (ComponentDescriptor) mbeanServer
  // .invoke(deploymentMBean, "getDescriptor",
  // new Object[] { componentIDs[i] },
  // new String[] { ComponentID.class.getName() });
  // }
  // return componentDescriptors;
  // } catch (Exception e) {
  // e.printStackTrace();
  // throw new ManagementConsoleException("Cannot obtain Components");
  // }
  // }

  public EventTypeID[] getEventTypes() throws ManagementConsoleException {
    try {
      EventTypeID[] IDs = (EventTypeID[]) mbeanServer.getAttribute(deploymentMBean, "EventTypes");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ProfileSpecificationID[] getProfileSpecifications() throws ManagementConsoleException {
    try {
      ProfileSpecificationID[] IDs = (ProfileSpecificationID[]) mbeanServer.getAttribute(deploymentMBean, "ProfileSpecifications");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ResourceAdaptorTypeID[] getResourceAdaptorTypes() throws ManagementConsoleException {
    try {
      ResourceAdaptorTypeID[] IDs = (ResourceAdaptorTypeID[]) mbeanServer.getAttribute(deploymentMBean, "ResourceAdaptorTypes");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ResourceAdaptorID[] getResourceAdaptors() throws ManagementConsoleException {
    try {
      ResourceAdaptorID[] IDs = (ResourceAdaptorID[]) mbeanServer.getAttribute(deploymentMBean, "ResourceAdaptors");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public SbbID[] getSbbs() throws ManagementConsoleException {
    try {
      SbbID[] IDs = (SbbID[]) mbeanServer.getAttribute(deploymentMBean, "Sbbs");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ServiceID[] getServices() throws ManagementConsoleException {
    try {
      ServiceID[] IDs = (ServiceID[]) mbeanServer.getAttribute(deploymentMBean, "Services");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public LibraryID[] getLibraries() throws ManagementConsoleException {
    try {
      LibraryID[] IDs = (LibraryID[]) mbeanServer.getAttribute(deploymentMBean, "Libraries");
      // ManagementConsole.getInstance().getComponentIDMap().put(IDs);
      return IDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ComponentDescriptor getDescriptor(ComponentID id) throws ManagementConsoleException {
    try {
      return (ComponentDescriptor) mbeanServer.invoke(deploymentMBean, "getDescriptor", new Object[] { id }, new String[] { ComponentID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ComponentDescriptor[] getDescriptors(ComponentID[] ids) throws ManagementConsoleException {
    ComponentDescriptor[] componentDescriptors = new ComponentDescriptor[ids.length];

    for (int i = 0; i < ids.length; i++)
      componentDescriptors[i] = getDescriptor(ids[i]);

    return componentDescriptors;
  }

  public String[] getEnvEntries(ComponentID id) throws ManagementConsoleException {
     try {
       return (String[]) mbeanServer.invoke(deploymentMBean, "getEnvEntries", new Object[] { id }, new String[] { ComponentID.class.getName() });
     }
     catch (Exception e) {
       e.printStackTrace();
       throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
     }
   }
  private void addComponentIDToArrayList(ArrayList<ComponentID> componentIDArrayList, ComponentID[] componentIDs) {
    for (int i = 0; i < componentIDs.length; i++)
      componentIDArrayList.add(componentIDs[i]);
  }

  public ComponentDescriptor[] getComponentDescriptors() throws ManagementConsoleException {
    ArrayList<ComponentID> componentIDArrayList = new ArrayList<ComponentID>();

    addComponentIDToArrayList(componentIDArrayList, getEventTypes());
    addComponentIDToArrayList(componentIDArrayList, getProfileSpecifications());
    addComponentIDToArrayList(componentIDArrayList, getSbbs());
    addComponentIDToArrayList(componentIDArrayList, getResourceAdaptors());
    addComponentIDToArrayList(componentIDArrayList, getResourceAdaptorTypes());
    addComponentIDToArrayList(componentIDArrayList, getServices());
    addComponentIDToArrayList(componentIDArrayList, getLibraries());

    ComponentID[] componentIDs = new ComponentID[componentIDArrayList.size()];
    componentIDs = componentIDArrayList.toArray(componentIDs);

    return getDescriptors(componentIDs);
  }

  public DeployableUnitID[] getDeployableUnits() throws ManagementConsoleException {
    try {
      return (DeployableUnitID[]) mbeanServer.getAttribute(deploymentMBean, "DeployableUnits");
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public DeployableUnitDescriptor getDescriptor(DeployableUnitID id) throws ManagementConsoleException {
    try {
      DeployableUnitDescriptor deployableUnitDescriptor = (DeployableUnitDescriptor) mbeanServer.invoke(deploymentMBean, "getDescriptor", new Object[] { id },
          new String[] { DeployableUnitID.class.getName() });
      // ammendonca DeployableUnitDescriptorEx deployableUnitDescriptorEx = new DeployableUnitDescriptorEx(deployableUnitDescriptor, id);
      return deployableUnitDescriptor;
    }
    catch (Exception e) {
      // e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  // The returned array is not granted to have the same size of the input one
  // This beacause some DeployableUnitIDs may not have a correspondent
  // DeployableUnitDescriptor
  public DeployableUnitDescriptor[] getDescriptors(DeployableUnitID[] ids) throws ManagementConsoleException {
    ArrayList<DeployableUnitDescriptor> deployabelUnitDescriptorArray = new ArrayList<DeployableUnitDescriptor>(ids.length);

    for (int i = 0; i < ids.length; i++) {
      try {
        deployabelUnitDescriptorArray.add(getDescriptor(ids[i]));
      }
      catch (Exception e) {
        Logger.error(e.getMessage());
      }
    }

    DeployableUnitDescriptor[] deployableUnitDescriptors = new DeployableUnitDescriptor[deployabelUnitDescriptorArray.size()];
    deployableUnitDescriptors = deployabelUnitDescriptorArray.toArray(deployableUnitDescriptors);

    return deployableUnitDescriptors;
  }

  public DeployableUnitDescriptor[] getDeployableUnitDescriptors() throws ManagementConsoleException {
    DeployableUnitID[] deployableUnitIDs = getDeployableUnits();
    return getDescriptors(deployableUnitIDs);
  }

  public DeployableUnitID install(String url) throws ManagementConsoleException {
    try {
      return (DeployableUnitID) mbeanServer.invoke(deploymentMBean, "install", new Object[] { url }, new String[] { String.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public void uninstall(DeployableUnitID id) throws ManagementConsoleException {
    try {
      mbeanServer.invoke(deploymentMBean, "uninstall", new Object[] { id }, new String[] { DeployableUnitID.class.getName() });
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }
  }

  public ComponentID[] getReferringComponents(ComponentID id) throws ManagementConsoleException {
    try {
      ComponentID[] componentIDs = (ComponentID[]) mbeanServer.invoke(deploymentMBean, "getReferringComponents", new Object[] { id },
          new String[] { ComponentID.class.getName() });
      // ManagementConsole.getInstance().getComponentIDMap().put(componentIDs);
      return componentIDs;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new ManagementConsoleException(SleeManagementMBeanUtils.doMessage(e));
    }

  }

}
