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

package org.mobicents.slee.container.management.console.server.deployableunits;

import java.util.HashMap;

import javax.slee.management.DeployableUnitID;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;

/**
 * @author Stefano Zappaterra
 * 
 */
public class DeployableUnitIDMap {

  private HashMap<String, DeployableUnitID> map = new HashMap<String, DeployableUnitID>();

  public void put(DeployableUnitID value) {
    map.put(value.toString(), value);
  }

  public void put(DeployableUnitID[] values) {
    for (int i = 0; i < values.length; i++)
      put(values[i]);
  }

  public DeployableUnitID get(String key) throws ManagementConsoleException {
    DeployableUnitID id = map.get(key);
    if (id == null)
      throw new ManagementConsoleException("Deployable Unit " + key + " not found");
    return id;
  }
}
