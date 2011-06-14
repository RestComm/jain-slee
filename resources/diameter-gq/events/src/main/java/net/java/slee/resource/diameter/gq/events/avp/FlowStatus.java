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

package net.java.slee.resource.diameter.gq.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the Flow Status enumerated type.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class FlowStatus implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _ENABLED_UPLINK = 0;
  public static final int _ENABLED_DOWNLINK = 1;
  public static final int _ENABLED = 2;
  public static final int _DISABLED = 3;
  public static final int _REMOVED = 4;

  public static final FlowStatus ENABLED_UPLINK = new FlowStatus(_ENABLED_UPLINK);
  public static final FlowStatus ENABLED_DOWNLINK = new FlowStatus(_ENABLED_DOWNLINK);
  public static final FlowStatus ENABLED = new FlowStatus(_ENABLED);
  public static final FlowStatus DISABLED = new FlowStatus(_DISABLED);
  public static final FlowStatus REMOVED = new FlowStatus(_REMOVED);

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
