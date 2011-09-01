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
 * The AF-Signalling-Protocol AVP (AVP code 529) is of type Enumerated, and
 * indicates the protocol used for signalling between the UE and the AF. If the
 * AF-Signalling-Protocol AVP is not provided in the AA-Request, the value
 * NO_INFORMATION shall be assumed.
 * 
 * <pre>
 * NO_INFORMATION (0)
 *   This value is used to indicate that no information about the AF signalling protocol is being provided.
 * SIP (1)
 *   This value is used to indicate that the signalling protocol is Session Initiation Protocol.
 * 
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum AFSignalingProtocol implements Enumerated {

  NO_INFORMATION(0),
  SIP(1);

  public static final int _NO_INFORMATION = NO_INFORMATION.getValue();
  public static final int _SIP = SIP.getValue();

  private int value = -1;

  private AFSignalingProtocol(int v) {
    this.value = v;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static AFSignalingProtocol fromInt(int type) throws IllegalArgumentException {
    switch (type) {
      case 0:
        return NO_INFORMATION;
      case 1:
        return SIP;

      default:
        throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
