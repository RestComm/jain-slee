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
