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

package org.mobicents.slee.container.management.console.client.usage;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Stefano Zappaterra
 * 
 */
public interface UsageService extends RemoteService {

  public String[] getParameterSets(String serviceID, String sbbID) throws ManagementConsoleException;

  public void createUsageParameterSet(String serviceID, String sbbID, String name) throws ManagementConsoleException;

  public void removeUsageParameterSet(String serviceID, String sbbID, String parameterSet) throws ManagementConsoleException;

  public void resetAllUsageParameters(String serviceID, String sbbID) throws ManagementConsoleException;

  public UsageParameterInfo[] getSBBUsageParameters(String serviceID, String sbbID, String parameterSet) throws ManagementConsoleException;

  public void resetAllUsageParameters(String serviceID, String sbbID, String parameterSet) throws ManagementConsoleException;

  public void resetUsageParameter(String serviceID, String sbbID, String parameterSet, String parameterName, boolean isCounterType)
      throws ManagementConsoleException;

}
