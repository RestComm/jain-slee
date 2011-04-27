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

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the NodeFunctionality enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class NodeFunctionality implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _S_CSCF = 0;

  public static final int _P_CSCF = 1;

  public static final int _I_CSCF = 2;

  public static final int _MRFC = 3;

  public static final int _MGCF = 4;

  public static final int _BGCF = 5;

  public static final int _AS = 6;

  public static final NodeFunctionality AS = new NodeFunctionality(_AS);

  public static final NodeFunctionality BGCF = new NodeFunctionality(_BGCF);

  public static final NodeFunctionality I_CSCF = new NodeFunctionality(_I_CSCF);

  public static final NodeFunctionality MGCF = new NodeFunctionality(_MGCF);

  public static final NodeFunctionality MRFC = new NodeFunctionality(_MRFC);

  public static final NodeFunctionality P_CSCF = new NodeFunctionality(_P_CSCF);

  public static final NodeFunctionality S_CSCF = new NodeFunctionality(_S_CSCF);

  private NodeFunctionality(int v) {
    value = v;
  }
  /**
   * Return the value of this instance of this enumerated type.
   */
  public static NodeFunctionality fromInt(int type) {
    switch(type) {
    case _AS: return AS;

    case _BGCF: return BGCF;

    case _I_CSCF: return I_CSCF;

    case _MGCF: return MGCF;

    case _MRFC: return MRFC;

    case _P_CSCF: return P_CSCF;

    case _S_CSCF: return S_CSCF;
    default: throw new IllegalArgumentException("Invalid NodeFunctionality value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _AS: return "AS";

    case _BGCF: return "BGCF";

    case _I_CSCF: return "I_CSCF";

    case _MGCF: return "MGCF";

    case _MRFC: return "MRFC";

    case _P_CSCF: return "P_CSCF";

    case _S_CSCF: return "S_CSCF";
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
