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

import javax.slee.management.ServiceDescriptor;
import javax.slee.management.ServiceState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.services.ServiceInfoHeader;
import org.mobicents.slee.container.management.console.server.ManagementConsole;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ServiceInfoUtils {

  static public ServiceInfoHeader toServiceInfoHeader(ServiceDescriptor serviceDescriptor, ServiceState serviceState) throws ManagementConsoleException {

    ManagementConsole.getInstance().getComponentIDMap().put(serviceDescriptor.getID());
    return new ServiceInfoHeader(serviceDescriptor.getID().toString(), serviceDescriptor.getName(), ServiceStateInfoUtils.toServiceStateInfo(serviceState));
  }
}
