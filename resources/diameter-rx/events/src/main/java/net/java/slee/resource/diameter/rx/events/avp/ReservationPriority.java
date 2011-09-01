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
 * The Reservation-Priority AVP (AVP code 458) is of type Enumerated. The following values are specified:
 * 
 * <pre>
 * DEFAULT (0): 
 *   This is the lowest level of priority. If no Reservation-Priority AVP is specified in the AA-Request, this is the priority associated with the reservation.
 * PRIORITY-ONE (1)
 * PRIORITY-TWO (2)
 * PRIORITY-THREE (3)
 * PRIORITY-FOUR (4)
 * PRIORITY-FIVE (5)
 * PRIORITY-SIX (6)
 * PRIORITY-SEVEN (7)
 * PRIORITY-EIGHT (8)
 * PRIORITY-NINE (9)
 * PRIORITY-TEN(10)
 * PRIORITY-ELEVEN (11)
 * PRIORITY-TWELVE (12)
 * PRIORITY-THIRTEEN (13)
 * PRIORITY-FOURTEEN (14)
 * PRIORITY-FIFTEEN (15)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum ReservationPriority implements Enumerated {

  DEFAULT(0), PRIORITY_ONE(1), PRIORITY_TWO(2), PRIORITY_THREE(3), PRIORITY_FOUR(4), PRIORITY_FIVE(5), PRIORITY_SIX(6),
  PRIORITY_SEVEN(7), PRIORITY_EIGHT(8), PRIORITY_NINE(9), PRIORITY_TEN(10), PRIORITY_ELEVEN(11), PRIORITY_TWELVE(12),
  PRIORITY_THIRTEEN(13), PRIORITY_FOURTEEN(14), PRIORITY_FIFTEEN(15);

  public static final int _DEFAULT = 0;
  public static final int _PRIORITY_ONE = 1;
  public static final int _PRIORITY_TWO = 2;
  public static final int _PRIORITY_THREE = 3;
  public static final int _PRIORITY_FOUR = 4;
  public static final int _PRIORITY_FIVE = 5;
  public static final int _PRIORITY_SIX = 6;
  public static final int _PRIORITY_SEVEN = 7;
  public static final int _PRIORITY_EIGHT = 8;
  public static final int _PRIORITY_NINE = 9;
  public static final int _PRIORITY_TEN = 10;
  public static final int _PRIORITY_ELEVEN = 11;
  public static final int _PRIORITY_TWELVE = 12;
  public static final int _PRIORITY_THIRTEEN = 13;
  public static final int _PRIORITY_FOURTEEN = 14;
  public static final int _PRIORITY_FIFTEEN = 15;

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
      case _PRIORITY_ONE:
        return PRIORITY_ONE;
      case _PRIORITY_TWO:
        return PRIORITY_TWO;
      case _PRIORITY_THREE:
        return PRIORITY_THREE;
      case _PRIORITY_FOUR:
        return PRIORITY_FOUR;
      case _PRIORITY_FIVE:
        return PRIORITY_FIVE;
      case _PRIORITY_SIX:
        return PRIORITY_SIX;
      case _PRIORITY_SEVEN:
        return PRIORITY_SEVEN;
      case _PRIORITY_EIGHT:
        return PRIORITY_EIGHT;
      case _PRIORITY_NINE:
        return PRIORITY_NINE;
      case _PRIORITY_TEN:
        return PRIORITY_TEN;
      case _PRIORITY_ELEVEN:
        return PRIORITY_ELEVEN;
      case _PRIORITY_TWELVE:
        return PRIORITY_TWELVE;
      case _PRIORITY_THIRTEEN:
        return PRIORITY_THIRTEEN;
      case _PRIORITY_FOURTEEN:
        return PRIORITY_FOURTEEN;
      case _PRIORITY_FIFTEEN:
        return PRIORITY_FIFTEEN;
      default:
        throw new IllegalArgumentException("Invalid Reservatin Priority value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _DEFAULT:
        return "DEFAULT";
      case _PRIORITY_ONE:
        return "PRIORITY-ONE";
      case _PRIORITY_TWO:
        return "PRIORITY-TWO";
      case _PRIORITY_THREE:
        return "PRIORITY-THREE";
      case _PRIORITY_FOUR:
        return "PRIORITY-FOUR";
      case _PRIORITY_FIVE:
        return "PRIORITY-FIVE";
      case _PRIORITY_SIX:
        return "PRIORITY-SIX";
      case _PRIORITY_SEVEN:
        return "PRIORITY-SEVEN";
      case _PRIORITY_EIGHT:
        return "PRIORITY-EIGHT";
      case _PRIORITY_NINE:
        return "PRIORITY-NINE";
      case _PRIORITY_TEN:
        return "PRIORITY-TEN";
      case _PRIORITY_ELEVEN:
        return "PRIORITY-ELEVEN";
      case _PRIORITY_TWELVE:
        return "PRIORITY-TWELVE";
      case _PRIORITY_THIRTEEN:
        return "PRIORITY-THIRTEEN";
      case _PRIORITY_FOURTEEN:
        return "PRIORITY-FOURTEEN";
      case _PRIORITY_FIFTEEN:
        return "PRIORITY-FIFTEEN";
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
