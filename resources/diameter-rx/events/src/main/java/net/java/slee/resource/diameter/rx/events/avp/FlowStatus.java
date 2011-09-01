/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package net.java.slee.resource.diameter.rx.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * The Flow-Status AVP (AVP code 511) is of type Enumerated, and describes
 * whether the IP flow(s) are enabled or disabled. The following values are
 * defined:
 * 
 * <pre>
 * ENABLED-UPLINK (0)
 *   This value shall be used to enable associated uplink IP flow(s) and to disable associated downlink IP flow(s).
 * ENABLED-DOWNLINK (1)
 *   This value shall be used to enable associated downlink IP flow(s) and to disable associated uplink IP flow(s).
 * ENABLED (2)
 *   This value shall be used to enable all associated IP flow(s) in both directions.
 * DISABLED (3)
 *   This value shall be used to disable all associated IP flow(s) in both directions.
 * REMOVED (4)
 *   This value shall be used to remove all associated IP flow(s). The IP Filters for the associated IP flow(s) shall be removed. The associated IP flows shall not be taken into account when deriving the authorized QoS.
 * 
 * NOTE: The interpretation of values for the RTCP flows in the Rx interface is described within the procedures in clause 4.4.3.
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum FlowStatus implements Enumerated {

  ENABLED_UPLINK(0), ENABLED_DOWNLINK(1), ENABLED(2), DISABLED(3), REMOVED(4);

  public static final int _ENABLED_UPLINK = 0;
  public static final int _ENABLED_DOWNLINK = 1;
  public static final int _ENABLED = 2;
  public static final int _DISABLED = 3;
  public static final int _REMOVED = 4;

  private int value = 0;

  private FlowStatus(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static FlowStatus fromInt(int type) {
    switch (type) {
      case _ENABLED_UPLINK:
        return ENABLED_UPLINK;
      case _ENABLED_DOWNLINK:
        return ENABLED_DOWNLINK;
      case _ENABLED:
        return ENABLED;
      case _DISABLED:
        return DISABLED;
      case _REMOVED:
        return REMOVED;
      default:
        throw new IllegalArgumentException("Invalid Flow Status value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _ENABLED_UPLINK:
        return "ENABLED_UPLINK";
      case _ENABLED_DOWNLINK:
        return "ENABLED_DOWNLINK";
      case _ENABLED:
        return "ENABLED";
      case _DISABLED:
        return "DISABLED";
      case _REMOVED:
        return "REMOVED";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid flow status found: " + value);
    }
  }
}
