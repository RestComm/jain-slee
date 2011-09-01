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
 * The Media-Type AVP (AVP code 520) is of type Enumerated, and it determines
 * the media type of a session component. The media types indicate the type of
 * media in the same way as the SDP media types with the same names defined in
 * RFC 4566 [13]. The following values are defined:
 * 
 * <pre>
 *   AUDIO(0)
 *   VIDEO(1)
 *   DATA(2)
 *   APPLICATION(3)
 *   CONTROL(4)
 *   TEXT(5)
 *   MESSAGE(6)
 *   OTHER(0xFFFFFFFF)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public enum MediaType implements Enumerated {

  AUDIO(0), VIDEO(1), DATA(2), APPLICATION(3), CONTROL(4), TEXT(5), MESSAGE(6), OTHER(0xFFFFFFFF);

  public static final int _AUDIO = 0;
  public static final int _VIDEO = 1;
  public static final int _DATA = 2;
  public static final int _APPLICATION = 3;
  public static final int _CONTROL = 4;
  public static final int _TEXT = 5;
  public static final int _MESSAGE = 6;
  public static final int _OTHER = 0xFFFFFFFF;

  private int value = 0;

  private MediaType(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    return value;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static MediaType fromInt(int type) {
    switch (type) {
      case _AUDIO:
        return AUDIO;
      case _VIDEO:
        return VIDEO;
      case _DATA:
        return DATA;
      case _APPLICATION:
        return APPLICATION;
      case _CONTROL:
        return CONTROL;
      case _TEXT:
        return TEXT;
      case _MESSAGE:
        return MESSAGE;
      case _OTHER:
        return OTHER;
      default:
        throw new IllegalArgumentException("Invalid Media Type value: " + type);
    }
  }

  public String toString() {
    switch (value) {
      case _AUDIO:
        return "AUDIO";
      case _VIDEO:
        return "VIDEO";
      case _DATA:
        return "DATA";
      case _APPLICATION:
        return "APPLICATION";
      case _CONTROL:
        return "CONTROL";
      case _TEXT:
        return "TEXT";
      case _MESSAGE:
        return "MESSAGE";
      case _OTHER:
        return "OTHER";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid Media Type found: " + value);
    }
  }

}
