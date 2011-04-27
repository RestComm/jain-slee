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

package net.java.slee.resource.diameter.ro.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the MediaInitiatorFlag enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class MediaInitiatorFlag implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _CALLED_PARTY = 0;

  public static final int _CALLING_PARTY = 1;

  public static final int _UNKNOWN = 2;

  public static final MediaInitiatorFlag CALLED_PARTY = new MediaInitiatorFlag(_CALLED_PARTY);

  public static final MediaInitiatorFlag CALLING_PARTY = new MediaInitiatorFlag(_CALLING_PARTY);

  public static final MediaInitiatorFlag UNKNOWN = new MediaInitiatorFlag(_UNKNOWN);

  private MediaInitiatorFlag(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static MediaInitiatorFlag fromInt(int type) {
    switch(type) {
    case _CALLED_PARTY:
      return CALLED_PARTY;
    case _CALLING_PARTY:
      return CALLING_PARTY;
    case _UNKNOWN:
      return UNKNOWN;

    default:
      throw new IllegalArgumentException("Invalid MediaInitiatorFlag value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _CALLED_PARTY:
      return "CALLED_PARTY";
    case _CALLING_PARTY:
      return "CALLING_PARTY";
    case _UNKNOWN:
      return "UNKNOWN";

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

  private int value = 0;

}
