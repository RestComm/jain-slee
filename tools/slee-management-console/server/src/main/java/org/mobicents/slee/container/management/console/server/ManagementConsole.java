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

package org.mobicents.slee.container.management.console.server;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.server.components.ComponentIDMap;
import org.mobicents.slee.container.management.console.server.deployableunits.DeployableUnitIDMap;
import org.mobicents.slee.container.management.console.server.mbeans.SleeMBeanConnection;

/**
 * @author Stefano Zappaterra
 * 
 */
public class ManagementConsole {

  private static ManagementConsole singleton;

  private SleeMBeanConnection sleeConnection;

  private ComponentIDMap componentIDMap;
  private DeployableUnitIDMap deployableUnitIDMap;

  private ManagementConsole() throws ManagementConsoleException {
    sleeConnection = new SleeMBeanConnection();
    componentIDMap = new ComponentIDMap();
    deployableUnitIDMap = new DeployableUnitIDMap();
  }

  static {
    try {
      singleton = new ManagementConsole();
    }
    catch (ManagementConsoleException e) {
      e.printStackTrace();
    }
  }

  static public ManagementConsole getInstance() {

    return singleton;
  }

  public SleeMBeanConnection getSleeConnection() {
    return sleeConnection;
  }

  public ComponentIDMap getComponentIDMap() {
    return componentIDMap;
  }

  public DeployableUnitIDMap getDeployableUnitIDMap() {
    return deployableUnitIDMap;
  }
}
