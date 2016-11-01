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

import javax.slee.ComponentID;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.EventTypeDescriptor;
import javax.slee.management.LibraryDescriptor;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.ServiceDescriptor;
import javax.slee.profile.ProfileSpecificationDescriptor;
import javax.slee.resource.ResourceAdaptorDescriptor;
import javax.slee.resource.ResourceAdaptorTypeDescriptor;

import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;
import org.mobicents.slee.container.management.console.client.components.info.EventTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.LibraryInfo;
import org.mobicents.slee.container.management.console.client.components.info.ProfileSpecificationInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorInfo;
import org.mobicents.slee.container.management.console.client.components.info.ResourceAdaptorTypeInfo;
import org.mobicents.slee.container.management.console.client.components.info.SbbInfo;
import org.mobicents.slee.container.management.console.client.components.info.ServiceInfo;
import org.mobicents.slee.container.management.console.server.ManagementConsole;
import org.mobicents.slee.container.management.console.server.deployableunits.DeployableUnitInfoUtils;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ComponentInfoUtils {

  static private String toString(ComponentID componentID) {
    if (componentID == null)
      return null;

    ManagementConsole.getInstance().getComponentIDMap().put(componentID);
    return componentID.toString();
  }

  static public String[] toStringArray(ComponentID[] componentIDs) {
    if (componentIDs == null)
      return null;

    String[] stringArray = new String[componentIDs.length];

    for (int i = 0; i < componentIDs.length; i++)
      stringArray[i] = componentIDs[i].toString();

    ManagementConsole.getInstance().getComponentIDMap().put(componentIDs);
    return stringArray;
  }

  static public ComponentInfo toComponentInfo(ComponentDescriptor componentDescriptor) {

    if (componentDescriptor instanceof ServiceDescriptor) {
      ServiceDescriptor serviceDescriptor = (ServiceDescriptor) componentDescriptor;

      return new ServiceInfo(serviceDescriptor.getName(), serviceDescriptor.getSource(), serviceDescriptor.getVendor(), serviceDescriptor.getVersion(),
          toString(serviceDescriptor.getID()), DeployableUnitInfoUtils.toString(serviceDescriptor.getDeployableUnit()),
          serviceDescriptor.getAddressProfileTable(), serviceDescriptor.getResourceInfoProfileTable(), toString(serviceDescriptor.getRootSbb()),
          toStringArray(serviceDescriptor.getLibraries()));
    }
    else if (componentDescriptor instanceof SbbDescriptor) {
      SbbDescriptor sbbDescriptor = (SbbDescriptor) componentDescriptor;

      return new SbbInfo(sbbDescriptor.getName(), sbbDescriptor.getSource(), sbbDescriptor.getVendor(), sbbDescriptor.getVersion(),
          toString(sbbDescriptor.getID()), DeployableUnitInfoUtils.toString(sbbDescriptor.getDeployableUnit()),
          toString(sbbDescriptor.getAddressProfileSpecification()), toStringArray(sbbDescriptor.getEventTypes()),
          toStringArray(sbbDescriptor.getProfileSpecifications()), sbbDescriptor.getResourceAdaptorEntityLinks(),
          toStringArray(sbbDescriptor.getResourceAdaptorTypes()), toStringArray(sbbDescriptor.getSbbs()), toStringArray(sbbDescriptor.getLibraries()));

    }
    else if (componentDescriptor instanceof ResourceAdaptorTypeDescriptor) {
      ResourceAdaptorTypeDescriptor resourceAdaptorTypeDescriptor = (ResourceAdaptorTypeDescriptor) componentDescriptor;

      return new ResourceAdaptorTypeInfo(resourceAdaptorTypeDescriptor.getName(), resourceAdaptorTypeDescriptor.getSource(),
          resourceAdaptorTypeDescriptor.getVendor(), resourceAdaptorTypeDescriptor.getVersion(), toString(resourceAdaptorTypeDescriptor.getID()),
          DeployableUnitInfoUtils.toString(resourceAdaptorTypeDescriptor.getDeployableUnit()), toStringArray(resourceAdaptorTypeDescriptor.getEventTypes()),
          toStringArray(resourceAdaptorTypeDescriptor.getLibraries()));

    }
    else if (componentDescriptor instanceof EventTypeDescriptor) {
      EventTypeDescriptor eventTypeDescriptor = (EventTypeDescriptor) componentDescriptor;

      return new EventTypeInfo(eventTypeDescriptor.getName(), eventTypeDescriptor.getSource(), eventTypeDescriptor.getVendor(),
          eventTypeDescriptor.getVersion(), toString(eventTypeDescriptor.getID()), DeployableUnitInfoUtils.toString(eventTypeDescriptor.getDeployableUnit()),
          eventTypeDescriptor.getEventClassName(), toStringArray(eventTypeDescriptor.getLibraries()));

    }
    else if (componentDescriptor instanceof ProfileSpecificationDescriptor) {
      ProfileSpecificationDescriptor profileSpecificationDescriptor = (ProfileSpecificationDescriptor) componentDescriptor;

      return new ProfileSpecificationInfo(profileSpecificationDescriptor.getName(), profileSpecificationDescriptor.getSource(),
          profileSpecificationDescriptor.getVendor(), profileSpecificationDescriptor.getVersion(), toString(profileSpecificationDescriptor.getID()),
          DeployableUnitInfoUtils.toString(profileSpecificationDescriptor.getDeployableUnit()), profileSpecificationDescriptor.getCMPInterfaceName(),
          toStringArray(profileSpecificationDescriptor.getLibraries()));

    }
    else if (componentDescriptor instanceof ResourceAdaptorDescriptor) {
      ResourceAdaptorDescriptor resourceAdaptorDescriptor = (ResourceAdaptorDescriptor) componentDescriptor;

      return new ResourceAdaptorInfo(resourceAdaptorDescriptor.getName(), resourceAdaptorDescriptor.getSource(), resourceAdaptorDescriptor.getVendor(),
          resourceAdaptorDescriptor.getVersion(), toString(resourceAdaptorDescriptor.getID()), DeployableUnitInfoUtils.toString(resourceAdaptorDescriptor
              .getDeployableUnit()), toString(resourceAdaptorDescriptor.getResourceAdaptorType()), resourceAdaptorDescriptor.supportsActiveReconfiguration(), 
              toStringArray(resourceAdaptorDescriptor.getLibraries()));
    }
    else if (componentDescriptor instanceof LibraryDescriptor) {
      LibraryDescriptor libraryDescriptor = (LibraryDescriptor) componentDescriptor;

      return new LibraryInfo(libraryDescriptor.getName(), libraryDescriptor.getSource(), libraryDescriptor.getVendor(), libraryDescriptor.getVersion(),
          toString(libraryDescriptor.getID()), DeployableUnitInfoUtils.toString(libraryDescriptor.getDeployableUnit()), libraryDescriptor.getLibraryJars(),
          toStringArray(libraryDescriptor.getLibraries()));
    }
    return null;
  }

  static public ComponentInfo[] toComponentInfos(ComponentDescriptor[] componentDescriptors) {
    ComponentInfo[] componentInfos = new ComponentInfo[componentDescriptors.length];
    for (int i = 0; i < componentDescriptors.length; i++)
      componentInfos[i] = toComponentInfo(componentDescriptors[i]);
    return componentInfos;
  }

  static public SbbInfo[] toSbbInfos(SbbDescriptor[] sbbDescriptors) {
    SbbInfo[] sbbInfos = new SbbInfo[sbbDescriptors.length];
    for (int i = 0; i < sbbDescriptors.length; i++)
      sbbInfos[i] = (SbbInfo) toComponentInfo(sbbDescriptors[i]);
    return sbbInfos;
  }
}
