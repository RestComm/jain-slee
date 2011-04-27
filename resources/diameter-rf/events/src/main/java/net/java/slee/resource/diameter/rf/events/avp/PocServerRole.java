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

/**
 * Java class to represent the PocServerRole enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PocServerRole implements net.java.slee.resource.diameter.base.events.avp.Enumerated, java.io.Serializable{

  private static final long serialVersionUID = 1L;

  public static final int _CONTROLLING_POC_SERVER = 1;

  public static final int _PARTICIPATING_POC_SERVER = 0;

  public static final PocServerRole CONTROLLING_POC_SERVER = new PocServerRole(_CONTROLLING_POC_SERVER);

  public static final PocServerRole PARTICIPATING_POC_SERVER = new PocServerRole(_PARTICIPATING_POC_SERVER);

  private PocServerRole(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static PocServerRole fromInt(int type) {
    switch(type) {
    case _CONTROLLING_POC_SERVER: return CONTROLLING_POC_SERVER;
    case _PARTICIPATING_POC_SERVER: return PARTICIPATING_POC_SERVER;
    default: throw new IllegalArgumentException("Invalid PocServerRole value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _CONTROLLING_POC_SERVER: return "CONTROLLING_POC_SERVER";
    case _PARTICIPATING_POC_SERVER: return "PARTICIPATING_POC_SERVER";
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
