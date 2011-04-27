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
 * Java class to represent the Priority enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class Priority implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _HIGH = 2;

  public static final int _LOW = 0;

  public static final int _NORMAL = 1;

  public static final Priority HIGH = new Priority(_HIGH);

  public static final Priority LOW = new Priority(_LOW);

  public static final Priority NORMAL = new Priority(_NORMAL);

  private Priority(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static Priority fromInt(int type) {
    switch(type) {
    case _HIGH: return HIGH;
    case _LOW: return LOW;
    case _NORMAL: return NORMAL;
    default: throw new IllegalArgumentException("Invalid Priority value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _HIGH: return "HIGH";
    case _LOW: return "LOW";
    case _NORMAL: return "NORMAL";
    default: return "<Invalid Value>";
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
