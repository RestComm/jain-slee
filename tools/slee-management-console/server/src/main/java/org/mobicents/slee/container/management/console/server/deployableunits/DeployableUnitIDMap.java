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
