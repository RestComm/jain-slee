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

package net.java.slee.resource.diameter.s6a.events.avp;

import java.io.Serializable;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class representing the Network-Access-Mode enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.21 Network-Access-Mode
 * 
 * The Network-Access-Mode AVP is of type Enumerated. The following values are defined:
 *  PACKET_AND_CIRCUIT (0)
 *  Reserved (1)
 *  ONLY_PACKET (2)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class NetworkAccessMode implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _PACKET_AND_CIRCUIT = 0;
  public static final int _Reserved = 1;
  public static final int _ONLY_PACKET = 2;

  public static final NetworkAccessMode PACKET_AND_CIRCUIT = new NetworkAccessMode(_PACKET_AND_CIRCUIT);
  public static final NetworkAccessMode Reserved = new NetworkAccessMode(_Reserved);
  public static final NetworkAccessMode ONLY_PACKET = new NetworkAccessMode(_ONLY_PACKET);

  private int value = -1;

  private NetworkAccessMode(int value) {
    this.value = value;
  }

  public static NetworkAccessMode fromInt(int type) {
    switch (type) {
      case _PACKET_AND_CIRCUIT:
        return PACKET_AND_CIRCUIT;
      case _Reserved:
        return Reserved;
      case _ONLY_PACKET:
        return ONLY_PACKET;
      default:
        throw new IllegalArgumentException("Invalid value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case _PACKET_AND_CIRCUIT:
        return "PACKET_AND_CIRCUIT";
      case _Reserved:
        return "Reserved";
      case _ONLY_PACKET:
        return "ONLY_PACKET";
      default:
        return "<Invalid Value>";
    }
  }
}