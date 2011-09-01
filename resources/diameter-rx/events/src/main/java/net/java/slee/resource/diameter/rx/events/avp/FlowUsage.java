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
 * The Flow-Usage AVP (AVP code 512) is of type Enumerated, and provides
 * information about the usage of IP Flows. The following values are defined:
 * 
 * <pre>
 * NO_INFORMATION (0)
 *   This value is used to indicate that no information about the usage of the IP flow is being provided.
 * RTCP (1)
 *   This value is used to indicate that an IP flow is used to transport RTCP.
 * AF_SIGNALLING (2)
 *   This value is used to indicate that the IP flow is used to transport AF Signalling Protocols (e.g. SIP/SDP).
 * NO_INFORMATION is the default value.
 * NOTE:  An AF may choose not to identify RTCP flows, e.g. in order to avoid that RTCP flows are always enabled by the server.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum FlowUsage implements Enumerated {

  NO_INFORMATION(0), RTCP(1), AF_SIGNALLING(2);

  public static final int _NO_INFORMATION = 0;
  public static final int _RTCP = 1;
  public static final int _AF_SIGNALLING = 2;

  private int value = 0;

  private FlowUsage(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static FlowUsage fromInt(int type) {
    switch (type) {
      case _NO_INFORMATION:
        return NO_INFORMATION;
      case _RTCP:
        return RTCP;
      case _AF_SIGNALLING:
        return AF_SIGNALLING;
      default:
        throw new IllegalArgumentException("Invalid Flow usage value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _NO_INFORMATION:
        return "NO_INFORMATION";
      case _RTCP:
        return "RTCP";
      case _AF_SIGNALLING:
        return "AF_SIGNALLING";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid flow usage found: " + value);
    }
  }
}
