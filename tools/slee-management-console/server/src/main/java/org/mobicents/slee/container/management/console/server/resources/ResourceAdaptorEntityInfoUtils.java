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

package org.mobicents.slee.container.management.console.server.resources;

import javax.slee.management.ResourceAdaptorEntityState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.resources.ResourceAdaptorEntityInfo;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ResourceAdaptorEntityInfoUtils {

  static public ResourceAdaptorEntityInfo toResourceAdaptorEntityInfo(String name, ResourceAdaptorEntityState state) throws ManagementConsoleException {
    String strState = "";
    if (state.isActive())
      strState = ResourceAdaptorEntityInfo.ACTIVE;
    else if (state.isInactive())
      strState = ResourceAdaptorEntityInfo.INACTIVE;
    else if (state.isStopping())
      strState = ResourceAdaptorEntityInfo.STOPPING;
    return new ResourceAdaptorEntityInfo(name, strState);
  }

  static public ResourceAdaptorEntityInfo[] toResourceAdaptorEntityInfos(String[] names, ResourceAdaptorEntityState[] states) throws ManagementConsoleException {
    if (names.length != states.length)
      throw new ManagementConsoleException("different array sizes");

    ResourceAdaptorEntityInfo[] resourceAdaptorEntityInfos = new ResourceAdaptorEntityInfo[names.length];

    for (int i = 0; i < resourceAdaptorEntityInfos.length; i++) {
      resourceAdaptorEntityInfos[i] = toResourceAdaptorEntityInfo(names[i], states[i]);
    }

    return resourceAdaptorEntityInfos;
  }

}
