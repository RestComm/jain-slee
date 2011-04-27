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
 * Java class to represent the RoleOfNode enumerated type.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RoleOfNode implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _B2BUA_ROLE = 3;

  public static final int _ORIGINATING_ROLE = 0;

  public static final int _PROXY_ROLE = 2;

  public static final int _TERMINATING_ROLE = 1;

  /**
   * The AS is applying a B2BUA role.
   */
  public static final RoleOfNode B2BUA_ROLE = new RoleOfNode(_B2BUA_ROLE);

  /**
   * The AS/CSCF is applying an originating role, serving the calling subscriber.
   */
  public static final RoleOfNode ORIGINATING_ROLE = new RoleOfNode(_ORIGINATING_ROLE);

  /**
   * The AS is applying a proxy role.
   */
  public static final RoleOfNode PROXY_ROLE = new RoleOfNode(_PROXY_ROLE);

  /**
   * The AS/CSCF is applying a terminating role, serving the called subscriber.
   */
  public static final RoleOfNode TERMINATING_ROLE = new RoleOfNode(_TERMINATING_ROLE);

  private RoleOfNode(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static RoleOfNode  fromInt(int type) {
    switch(type) {
    case _B2BUA_ROLE: return B2BUA_ROLE;

    case _ORIGINATING_ROLE: return ORIGINATING_ROLE;

    case _PROXY_ROLE: return PROXY_ROLE;

    case _TERMINATING_ROLE: return TERMINATING_ROLE;
    default: throw new IllegalArgumentException("Invalid RoleOfNode value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _B2BUA_ROLE: return "B2BUA_ROLE";

    case _ORIGINATING_ROLE: return "ORIGINATING_ROLE";

    case _PROXY_ROLE: return "PROXY_ROLE";

    case _TERMINATING_ROLE: return "TERMINATING_ROLE";
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
