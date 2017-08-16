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
