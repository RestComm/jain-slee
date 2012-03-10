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

package net.java.slee.resource.diameter.sh.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * 
 * AVP representation of Session-Priority AVP. Defined in 3GPP TS 29.329
 * section 6.3.56.<br>
 * 
 * 6.3.56 Session-Priority AVP
 * The Session-Priority AVP is of type Enumerated and indicates to the HSS 
 * the session's priority. The following values are defined:
 * <ul>
 * <li>PRIORITY-0 (0)</li>
 * <li>PRIORITY-1 (1)</li>
 * <li>PRIORITY-2 (2)</li>
 * <li>PRIORITY-3 (3)</li>
 * <li>PRIORITY-4 (4)</li>
 * </ul>
 * PRIORITY-0 is the highest priority.
 * 
 * The value of the AVP when sent to the HSS is mapped from the value received
 * by the CSCF as described in 3GPP TS 24.229 table A.162. 
 * The mapping is operator specific.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SessionPriorityType implements Serializable, Enumerated {

  private static final long serialVersionUID = 6175897437829561030L;

  /**
   * Value indicating Priority 0
   */
  public static final int _PRIORITY_0 = 0;

  /**
   * Value indicating Priority 1
   */
  public static final int _PRIORITY_1 = 1;

  /**
   * Value indicating Priority 2
   */
  public static final int _PRIORITY_2 = 2;

  /**
   * Value indicating Priority 3
   */
  public static final int _PRIORITY_3 = 3;

  /**
   * Value indicating Priority 4
   */
  public static final int _PRIORITY_4 = 4;

  /**
   * Static class representing 
   */
  public static final SessionPriorityType PRIORITY_0 = new SessionPriorityType(_PRIORITY_0);

  public static final SessionPriorityType PRIORITY_1 = new SessionPriorityType(_PRIORITY_1);

  public static final SessionPriorityType PRIORITY_2 = new SessionPriorityType(_PRIORITY_2);

  public static final SessionPriorityType PRIORITY_3 = new SessionPriorityType(_PRIORITY_3);

  public static final SessionPriorityType PRIORITY_4 = new SessionPriorityType(_PRIORITY_4);


  private SessionPriorityType(int value) {
    this.value = value;
  }

  public static SessionPriorityType fromInt(int type) {
    switch (type) {
      case _PRIORITY_0:
        return PRIORITY_0;
      case _PRIORITY_1:
        return PRIORITY_1;
      case _PRIORITY_2:
        return PRIORITY_2;
      case _PRIORITY_3:
        return PRIORITY_3;
      case _PRIORITY_4:
        return PRIORITY_4;
      default:
        throw new IllegalArgumentException("Invalid SessionPriority value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
      case _PRIORITY_0:
        return "PRIORITY-0";
      case _PRIORITY_1:
        return "PRIORITY-1";
      case _PRIORITY_2:
        return "PRIORITY-2";
      case _PRIORITY_3:
        return "PRIORITY-3";
      case _PRIORITY_4:
        return "PRIORITY-4";
      default:
        return "<Invalid Value>";
    }
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    } catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  private int value;
}
