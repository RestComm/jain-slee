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
 * The Abort-Cause AVP (AVP code 500) is of type Enumerated, and determines the
 * cause of an abort session request (ASR) or of a RAR indicating a bearer
 * release. The following values are defined:
 * 
 * <pre>
 * BEARER_RELEASED (0)
 *   This value is used when the bearer has been deactivated as a result from normal signalling handling. For GPRS the bearer refers to the PDP Context.
 * INSUFFICIENT_SERVER_RESOURCES (1)
 *   This value is used to indicate that the server is overloaded and needs to abort the session.
 * INSUFFICIENT_BEARER_RESOURCES (2)
 *   This value is used when the bearer has been deactivated due to insufficient bearer resources at a transport gateway (e.g. GGSN for GPRS).
 * PS_TO_CS_HANDOVER (3)
 *   This value is used when the bearer has been deactivated due to PS to CS handover.
 * SPONSORED_DATA_CONNECTIVITY_ DISALLOWED (4)
 *   This value is used in the ASR when the PCRF needs to initiates the AF session termination due to the operator policy (e.g. disallowing the UE accessing the sponsored data connectivity in the roaming case).
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum AbortCause implements Enumerated {

  BEARED_RELEASED(0), INSUFFICIENT_SERVER_RESOURCES(1), INSUFFICIENT_BEARER_RESOURCES(2), PS_TO_CS_HANDOVER(3), SPONSORED_DATA_CONNECTIVITY_DISALLOWED(4);

  public static final int _BEARED_RELEASED = 0;
  public static final int _INSUFFICIENT_SERVER_RESOURCES = 1;
  public static final int _INSUFFICIENT_BEARER_RESOURCES = 2;
  public static final int _PS_TO_CS_HANDOVER = 3;
  public static final int _SPONSORED_DATA_CONNECTIVITY_DISALLOWED = 4;

  private int value = 0;

  private AbortCause(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static AbortCause fromInt(int type) {
    switch (type) {
      case _BEARED_RELEASED:
        return BEARED_RELEASED;
      case _INSUFFICIENT_SERVER_RESOURCES:
        return INSUFFICIENT_SERVER_RESOURCES;
      case _INSUFFICIENT_BEARER_RESOURCES:
        return INSUFFICIENT_BEARER_RESOURCES;
      case _PS_TO_CS_HANDOVER:
        return PS_TO_CS_HANDOVER;
      case _SPONSORED_DATA_CONNECTIVITY_DISALLOWED:
        return SPONSORED_DATA_CONNECTIVITY_DISALLOWED;
      default:
        throw new IllegalArgumentException("Invalid Abourt Priority value: " + type);
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid abort cause found: " + value);
    }
  }
}
