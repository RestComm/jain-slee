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

package net.java.slee.resource.diameter.rf.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the MBMS-2G-3G-Indicator enumerated type.
 * <pre>
 * 17.7.18 MBMS-User-Data-Mode-Indication AVP
 * The MBMS-User-Data-Mode-Indication AVP (AVP code 915) is of type Enumerated. The meaning of the message containing this AVP depends on the sending entity. The absence of this AVP indicates unicast mode of operation.
 * 
 * The following values are supported:
 * Unicast (0)
 *   When BM-SC sends this value, that indicates to GGSN that BM-SC supports only unicast mode (IP multicast packets encapsulated by IP unicast header).
 *   When GGSN sends this value, that indicates to BM-SC that BM-SC shall send user plane data with unicast mode (IP multicast packets encapsulated by IP unicast header).
 * Multicast and Unicast (1)
 *   When BM-SC sends this value, that indicates to GGSN that BM-SC supports both modes of operation.
 *   When GGSN sends this value, that indicates to BM-SC that BM-SC shall send user plane data with multicast mode.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum MbmsUserDataModeIndication implements Enumerated {

  UNICAST(0), 
  MULTICAST_AND_UNICAST(1);

  private int value = -1;

  private MbmsUserDataModeIndication(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static MbmsUserDataModeIndication fromInt(int type) throws IllegalArgumentException {
    switch (type) {
    case 0:
      return UNICAST;
    case 1:
      return MULTICAST_AND_UNICAST;

    default:
      throw new IllegalArgumentException();
    }
  }

}
