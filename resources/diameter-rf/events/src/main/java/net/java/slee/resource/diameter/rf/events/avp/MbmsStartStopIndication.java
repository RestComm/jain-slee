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
 * Java class to represent the MBMS-StartStop-Indication enumerated type.
 * <pre>
 * The MBMS-StartStop-Indication AVP (AVP code 902) is of type Enumerated. The following values are supported:
 * 
 * START (0)
 *   The message containing this AVP is indicating an MBMS session start procedure.
 * STOP  (1) 
 *   The message containing this AVP is indicating an MBMS session stop procedure.
 * UPDATE (2)
 *   The message containing this AVP is indicating an MBMS session update procedure.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum MbmsStartStopIndication implements Enumerated {
  START(0), 
  STOP(1), 
  UPDATE(2);

  private int value = -1;

  private MbmsStartStopIndication(int value) {
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

  public static MbmsStartStopIndication fromInt(int type) throws IllegalArgumentException {
    switch (type) {
    case 0:
      return START;
    case 1:
      return STOP;
    case 2:
      return UPDATE;

    default:
      throw new IllegalArgumentException();
    }
  }

}
