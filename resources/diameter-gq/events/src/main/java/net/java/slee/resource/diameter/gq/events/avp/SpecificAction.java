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
 * Java class to represent the Specific Action enumerated type.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class SpecificAction implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _INDICATION_OF_LOSS_OF_BEARER = 2;
  public static final int _INDICATION_OF_RECOVERY_BEARER = 3;
  public static final int _INDICATION_OF_RELEASE_BEARER = 4;
  public static final int _INDICATION_OF_SUBSCRIBER_DETACHMENT = 6;
  public static final int _INDICATION_OF_RESERVATION_EXPIRATION = 7;

  public static final SpecificAction INDICATION_OF_LOSS_OF_BEARER = new SpecificAction(_INDICATION_OF_LOSS_OF_BEARER);
  public static final SpecificAction INDICATION_OF_RECOVERY_BEARER = new SpecificAction(_INDICATION_OF_RECOVERY_BEARER);
  public static final SpecificAction INDICATION_OF_RELEASE_BEARER = new SpecificAction(_INDICATION_OF_RELEASE_BEARER);
  public static final SpecificAction INDICATION_OF_SUBSCRIBER_DETACHMENT = new SpecificAction(_INDICATION_OF_SUBSCRIBER_DETACHMENT);
  public static final SpecificAction INDICATION_OF_RESERVATION_EXPIRATION = new SpecificAction(_INDICATION_OF_RESERVATION_EXPIRATION);

  private int value = 0;

  private SpecificAction(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static SpecificAction fromInt(int type) {
    switch (type) {
      case _INDICATION_OF_LOSS_OF_BEARER:
        return INDICATION_OF_LOSS_OF_BEARER;
      case _INDICATION_OF_RECOVERY_BEARER:
        return INDICATION_OF_RECOVERY_BEARER;
      case _INDICATION_OF_RELEASE_BEARER:
        return INDICATION_OF_RELEASE_BEARER;
      case _INDICATION_OF_SUBSCRIBER_DETACHMENT:
        return INDICATION_OF_SUBSCRIBER_DETACHMENT;
      case _INDICATION_OF_RESERVATION_EXPIRATION:
        return INDICATION_OF_RESERVATION_EXPIRATION;
      default:
        throw new IllegalArgumentException("Invalid Specific Action value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _INDICATION_OF_LOSS_OF_BEARER:
        return "INDICATION OF LOSS OF BEARER";
      case _INDICATION_OF_RECOVERY_BEARER:
        return "INDICATION OF RECOVERY BEARER";
      case _INDICATION_OF_RELEASE_BEARER:
        return "INDICATION OF RELEASE BEARER";
      case _INDICATION_OF_SUBSCRIBER_DETACHMENT:
        return "INDICATION OF SUBSCRIBER DETACHMENT";
      case _INDICATION_OF_RESERVATION_EXPIRATION:
        return "INDICATION OF RESERVATION EXPIRATION";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid specific action found: " + value);
    }
  }
}
