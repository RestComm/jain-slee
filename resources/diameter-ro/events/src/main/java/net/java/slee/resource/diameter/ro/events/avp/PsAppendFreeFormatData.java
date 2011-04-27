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

import java.io.StreamCorruptedException;

/**
 * Java class to represent the PsAppendFreeFormatData enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PsAppendFreeFormatData implements net.java.slee.resource.diameter.base.events.avp.Enumerated, java.io.Serializable{

  private static final long serialVersionUID = 1L;

  public static final int _APPEND = 0;

  public static final int _OVERWRITE = 1;

  /**
   * If this AVP is present and indicates ???Append???, the GGSN shall append the received PS free format data to the PS free format data stored for the online charging session.
   */
  public static final PsAppendFreeFormatData APPEND = new PsAppendFreeFormatData(_APPEND);

  /**
   * If this AVP is absent or in value ???Overwrite???, the GGSN shall overwrite all PS free format data already stored for the online charging session.
   */
  public static final PsAppendFreeFormatData OVERWRITE = new PsAppendFreeFormatData(_OVERWRITE);

  private PsAppendFreeFormatData(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static PsAppendFreeFormatData fromInt(int type) {
    switch(type) {
    case _APPEND: return APPEND;
    case _OVERWRITE: return OVERWRITE;

    default: throw new IllegalArgumentException("Invalid DisconnectCause value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _APPEND: return "APPEND";
    case _OVERWRITE: return "OVERWRITE";
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
