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
 * Java class to represent the MessageType enumerated type.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class ReservationPriority implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _DEFAULT = 0;
  public static final int _PRIORITYONE = 1;
  public static final int _PRIORITYTWO = 2;
  public static final int _PRIORITYTHREE = 3;
  public static final int _PRIORITYFOUR = 4;
  public static final int _PRIORITYFIVE = 5;
  public static final int _PRIORITYSIX = 6;
  public static final int _PRIORITYSEVEN = 7;

  public static final ReservationPriority DEFAULT = new ReservationPriority(_DEFAULT);
  public static final ReservationPriority PRIORITYONE = new ReservationPriority(_PRIORITYONE);
  public static final ReservationPriority PRIORITYTWO = new ReservationPriority(_PRIORITYTWO);
  public static final ReservationPriority PRIORITYTHREE = new ReservationPriority(_PRIORITYTHREE);
  public static final ReservationPriority PRIORITYFOUR = new ReservationPriority(_PRIORITYFOUR);
  public static final ReservationPriority PRIORITYFIVE = new ReservationPriority(_PRIORITYFIVE);
  public static final ReservationPriority PRIORITYSIX = new ReservationPriority(_PRIORITYSIX);
  public static final ReservationPriority PRIORITYSEVEN = new ReservationPriority(_PRIORITYSEVEN);

  private int value = 0;

  private ReservationPriority(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static ReservationPriority fromInt(int type) {
    switch (type) {
      case _DEFAULT:
        return DEFAULT;
      case _PRIORITYONE:
        return PRIORITYONE;
      case _PRIORITYTWO:
        return PRIORITYTWO;
      case _PRIORITYTHREE:
        return PRIORITYTHREE;
      case _PRIORITYFOUR:
        return PRIORITYFOUR;
      case _PRIORITYFIVE:
        return PRIORITYFIVE;
      case _PRIORITYSIX:
        return PRIORITYSIX;
      case _PRIORITYSEVEN:
        return PRIORITYSEVEN;
      default:
        throw new IllegalArgumentException("Invalid Reservatin Priority value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _DEFAULT:
        return "DEFAULT";
      case _PRIORITYONE:
        return "PRIORITY-ONE";
      case _PRIORITYTWO:
        return "PRIORITY-TWO";
      case _PRIORITYTHREE:
        return "PRIORITY-THREE";
      case _PRIORITYFOUR:
        return "PRIORITY-FOUR";
      case _PRIORITYFIVE:
        return "PRIORITY-FIVE";
      case _PRIORITYSIX:
        return "PRIORITY-SIX";
      case _PRIORITYSEVEN:
        return "PRIORITY-SEVEN";
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
}
