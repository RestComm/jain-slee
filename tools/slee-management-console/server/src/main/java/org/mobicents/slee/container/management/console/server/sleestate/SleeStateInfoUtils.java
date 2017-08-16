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

package org.mobicents.slee.container.management.console.server.sleestate;

import javax.slee.management.SleeState;

import org.mobicents.slee.container.management.console.client.ManagementConsoleException;
import org.mobicents.slee.container.management.console.client.sleestate.SleeStateInfo;

/**
 * @author Stefano Zappaterra
 * 
 */
public class SleeStateInfoUtils {
  static public SleeStateInfo toSleeStateInfo(SleeState sleeState) throws ManagementConsoleException {
    switch (sleeState.toInt()) {
      case SleeState.SLEE_STOPPED:
        return new SleeStateInfo(SleeStateInfo.STOPPED);
      case SleeState.SLEE_STARTING:
        return new SleeStateInfo(SleeStateInfo.STARTING);
      case SleeState.SLEE_RUNNING:
        return new SleeStateInfo(SleeStateInfo.RUNNING);
      case SleeState.SLEE_STOPPING:
        return new SleeStateInfo(SleeStateInfo.STOPPING);
    }
    throw new ManagementConsoleException("Unrecognized Slee state: " + sleeState.toString());
  }
}
