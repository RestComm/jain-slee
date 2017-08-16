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

package org.mobicents.slee.container.management.console.client.services;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.components.info.ComponentInfo;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Stefano Zappaterra
 * 
 */
public interface ServicesService extends RemoteService {

  public ServiceInfoHeader[] getServiceInfoHeaders() throws ManagementConsoleException;

  public void activate(String id) throws ManagementConsoleException;

  public void deactivate(String id) throws ManagementConsoleException;

  public ComponentInfo[] getSbbsWithinService(String serviceID) throws ManagementConsoleException;
}
