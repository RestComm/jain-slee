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
 * Java class to represent the Media Type enumerated type.
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class MediaType implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _AUDIO = 0;
  public static final int _VIDEO = 1;
  public static final int _DATA = 2;
  public static final int _APPLICATION = 3;
  public static final int _CONTROL = 4;
  public static final int _TEXT = 5;
  public static final int _MESSAGE = 6;
  public static final int _OTHER = 0xFFFFFFFF;

  public static final MediaType AUDIO = new MediaType(_AUDIO);
  public static final MediaType VIDEO = new MediaType(_VIDEO);
  public static final MediaType DATA = new MediaType(_DATA);
  public static final MediaType APPLICATION = new MediaType(_APPLICATION);
  public static final MediaType CONTROL = new MediaType(_CONTROL);
  public static final MediaType TEXT = new MediaType(_TEXT);
  public static final MediaType MESSAGE = new MediaType(_MESSAGE);
  public static final MediaType OTHER = new MediaType(_OTHER);

  private int value = 0;

  private MediaType(int v) {
    value = v;
  }

  @Override
  public int getValue() {
    // TODO Auto-generated method stub
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
