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

package org.mobicents.slee.container.management.console.client.activity;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Vladimir Ralev
 * 
 */
public interface ActivityService extends RemoteService {

  public ActivityContextInfo[] listActivityContexts() throws ManagementConsoleException;

  public void endActivity(String id) throws ManagementConsoleException;

  public ActivityContextInfo retrieveActivityContextDetails(String id) throws ManagementConsoleException;

  public ActivityContextInfo[] retrieveActivityContextIDByResourceAdaptorEntityName(String name) throws ManagementConsoleException;

  public ActivityContextInfo[] retrieveActivityContextIDByActivityType(String type) throws ManagementConsoleException;

  public ActivityContextInfo[] retrieveActivityContextIDBySbbEntityID(String id) throws ManagementConsoleException;

  public ActivityContextInfo[] retrieveActivityContextIDBySbbID(String id) throws ManagementConsoleException;

  public void queryActivityContextLiveness() throws ManagementConsoleException;

}
