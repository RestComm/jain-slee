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

package org.mobicents.slee.container.management.console.server.services;

import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.services.ServiceStateInfo;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceStateInfoUtils {

  static public ServiceStateInfo toServiceStateInfo(ServiceState serviceState) throws ManagementConsoleException {
    switch (serviceState.toInt()) {
      case ServiceState.SERVICE_ACTIVE:
        return new ServiceStateInfo(ServiceStateInfo.ACTIVE);
      case ServiceState.SERVICE_INACTIVE:
        return new ServiceStateInfo(ServiceStateInfo.INACTIVE);
      case ServiceState.SERVICE_STOPPING:
        return new ServiceStateInfo(ServiceStateInfo.STOPPING);
    }
    throw new ManagementConsoleException("Unrecognized service state: " + serviceState.toString());
  }
}
