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

package net.java.slee.resource.diameter.base.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Java class to represent the DisconnectCause enumerated type.
 *<P/>
 * Documentation from the relevant specification:
 *<P/>
 * The Disconnect-Cause AVP (AVP Code 273) is of type Enumerated. A Diameter
 * node MUST include this AVP in the Disconnect-Peer-Request message to inform
 * the peer of the reason for its intention to shutdown the transport
 * connection.
 * 
 * @author Open Cloud
 */

public class DisconnectCauseType implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _REBOOTING = 0;
  public static final int _BUSY = 1;
  public static final int _DO_NOT_WANT_TO_TALK_TO_YOU = 2;

  /**
   * A scheduled reboot is imminent.
   */
  public static final DisconnectCauseType REBOOTING = new DisconnectCauseType(_REBOOTING);

  /**
   * The peer's internal resources are constrained, and it has determined that
   * the transport connection needs to be closed.
   */
  public static final DisconnectCauseType BUSY = new DisconnectCauseType(_BUSY);

  /**
   * The peer has determined that it does not see a need for the transport
   * connection to exist, since it does not expect any messages to be
   * exchanged in the near future.
   */
  public static final DisconnectCauseType DO_NOT_WANT_TO_TALK_TO_YOU = new DisconnectCauseType(_DO_NOT_WANT_TO_TALK_TO_YOU);

  private DisconnectCauseType(int value) {
    this.value = value;
  }

  public static DisconnectCauseType fromInt(int type) {
    switch (type) {
    case _REBOOTING:
      return REBOOTING;
    case _BUSY:
      return BUSY;
    case _DO_NOT_WANT_TO_TALK_TO_YOU:
      return DO_NOT_WANT_TO_TALK_TO_YOU;
    default:
      throw new IllegalArgumentException("Invalid DisconnectCause value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
    case _REBOOTING:
      return "REBOOTING";
    case _BUSY:
      return "BUSY";
    case _DO_NOT_WANT_TO_TALK_TO_YOU:
      return "DO_NOT_WANT_TO_TALK_TO_YOU";
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

  private int value;
}
