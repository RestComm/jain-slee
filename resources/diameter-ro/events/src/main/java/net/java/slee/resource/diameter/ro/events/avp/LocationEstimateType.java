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

package net.java.slee.resource.diameter.ro.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the LocationEstimateType enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LocationEstimateType implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _CURRENT_LOCATION = 0;

  public static final int _CURRENT_LAST_KNOWN_LOCATION = 1;

  public static final int _INITIAL_LOCATION = 2;

  public static final int _ACTIVATE_DEFERRED_LOCATION = 3;

  public static final int _CANCEL_DEFERRED_LOCATION = 4;

  public static final LocationEstimateType CURRENT_LOCATION = new LocationEstimateType(_CURRENT_LOCATION);

  public static final LocationEstimateType CURRENT_LAST_KNOWN_LOCATION = new LocationEstimateType(_CURRENT_LAST_KNOWN_LOCATION);

  public static final LocationEstimateType INITIAL_LOCATION = new LocationEstimateType(_INITIAL_LOCATION);

  public static final LocationEstimateType ACTIVATE_DEFERRED_LOCATION = new LocationEstimateType(_ACTIVATE_DEFERRED_LOCATION);

  public static final LocationEstimateType CANCEL_DEFERRED_LOCATION = new LocationEstimateType(_CANCEL_DEFERRED_LOCATION);

  private LocationEstimateType(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static LocationEstimateType fromInt(int type) {
    switch(type) {
    case _ACTIVATE_DEFERRED_LOCATION:
      return ACTIVATE_DEFERRED_LOCATION;
    case _CANCEL_DEFERRED_LOCATION:
      return CANCEL_DEFERRED_LOCATION;
    case _CURRENT_LAST_KNOWN_LOCATION:
      return CURRENT_LAST_KNOWN_LOCATION;
    case _CURRENT_LOCATION:
      return CURRENT_LOCATION;
    case _INITIAL_LOCATION:
      return INITIAL_LOCATION;

    default:
      throw new IllegalArgumentException("Invalid LocationEstimateType value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _ACTIVATE_DEFERRED_LOCATION:
      return "ACTIVATE_DEFERRED_LOCATION";
    case _CANCEL_DEFERRED_LOCATION:
      return "CANCEL_DEFERRED_LOCATION";
    case _CURRENT_LAST_KNOWN_LOCATION:
      return "CURRENT_LAST_KNOWN_LOCATION";
    case _CURRENT_LOCATION:
      return "CURRENT_LOCATION";
    case _INITIAL_LOCATION:
      return "INITIAL_LOCATION";

    default:
      return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  private int value = 0;

}
