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

package net.java.slee.resource.diameter.s6a.events.avp;

import java.io.Serializable;
import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class representing the User-State enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.114  User-State
 * 
 * The User-State AVP is of type Enumerated and indicates the user state in EPS. The following values are defined:
 * 
 *   DETACHED (0)
 *   ATTACHED_NOT_REACHABLE_FOR_PAGING (1)
 *   ATTACHED_REACHABLE_FOR_PAGING (2)
 *   CONNECTED_NOT_REACHABLE_FOR_PAGING (3)
 *   CONNECTED_REACHABLE_FOR_PAGING (4)
 *   NETWORK_DETERMINED_NOT_REACHABLE (5)
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class UserState implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _DETACHED                           = 0;
  public static final int _ATTACHED_NOT_REACHABLE_FOR_PAGING  = 1;
  public static final int _ATTACHED_REACHABLE_FOR_PAGING      = 2;
  public static final int _CONNECTED_NOT_REACHABLE_FOR_PAGING = 3;
  public static final int _CONNECTED_REACHABLE_FOR_PAGING     = 4;
  public static final int _NETWORK_DETERMINED_NOT_REACHABLE   = 5;

  public static final UserState DETACHED = new UserState(_DETACHED);
  public static final UserState ATTACHED_NOT_REACHABLE_FOR_PAGING = new UserState(_ATTACHED_NOT_REACHABLE_FOR_PAGING);
  public static final UserState ATTACHED_REACHABLE_FOR_PAGING = new UserState(_ATTACHED_REACHABLE_FOR_PAGING);
  public static final UserState CONNECTED_NOT_REACHABLE_FOR_PAGING = new UserState(_CONNECTED_NOT_REACHABLE_FOR_PAGING);
  public static final UserState CONNECTED_REACHABLE_FOR_PAGING = new UserState(_CONNECTED_REACHABLE_FOR_PAGING);
  public static final UserState NETWORK_DETERMINED_NOT_REACHABLE = new UserState(_NETWORK_DETERMINED_NOT_REACHABLE);

  private int value = -1;

  private UserState(int value) {
    this.value = value;
  }

  public static UserState fromInt(int type) {
    switch (type) {
      case _DETACHED:
        return DETACHED;
      case _ATTACHED_NOT_REACHABLE_FOR_PAGING:
        return ATTACHED_NOT_REACHABLE_FOR_PAGING;
      case _ATTACHED_REACHABLE_FOR_PAGING:
        return ATTACHED_REACHABLE_FOR_PAGING;
      case _CONNECTED_NOT_REACHABLE_FOR_PAGING:
        return CONNECTED_NOT_REACHABLE_FOR_PAGING;
      case _CONNECTED_REACHABLE_FOR_PAGING:
        return CONNECTED_REACHABLE_FOR_PAGING;
      case _NETWORK_DETERMINED_NOT_REACHABLE:
        return NETWORK_DETERMINED_NOT_REACHABLE;
      default:
        throw new IllegalArgumentException("Invalid value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  @Override
  public String toString() {
    switch (value) {
      case _DETACHED:
        return "DETACHED";
      case _ATTACHED_NOT_REACHABLE_FOR_PAGING:
        return "ATTACHED_NOT_REACHABLE_FOR_PAGING";
      case _ATTACHED_REACHABLE_FOR_PAGING:
        return "ATTACHED_REACHABLE_FOR_PAGING";
      case _CONNECTED_NOT_REACHABLE_FOR_PAGING:
        return "CONNECTED_NOT_REACHABLE_FOR_PAGING";
      case _CONNECTED_REACHABLE_FOR_PAGING:
        return "CONNECTED_REACHABLE_FOR_PAGING";
      case _NETWORK_DETERMINED_NOT_REACHABLE:
        return "NETWORK_DETERMINED_NOT_REACHABLE";
      default:
        return "<Invalid Value>";
    }
  }

}