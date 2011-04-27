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

package net.java.slee.resource.diameter.cxdx.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * <pre>
 * <b>6.3.26  User-Data-Already-Available AVP</b>
 * The User-Data-Already-Available AVP is of type Enumerated, and indicates to the HSS whether or
 * not the S-CSCF already has the part of the user profile that it needs to serve the user. The 
 * following values are defined:
 * 
 * USER_DATA_NOT_AVAILABLE (0)
 *   The S-CSCF does not have the data that it needs to serve the user.
 * USER_DATA_ALREADY_AVAILABLE (1)
 *   The S-CSCF already has the data that it needs to serve the user.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class UserDataAlreadyAvailable implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _USER_DATA_NOT_AVAILABLE = 0;

  public static final int _USER_DATA_ALREADY_AVAILABLE = 1;

  public static final UserDataAlreadyAvailable USER_DATA_NOT_AVAILABLE = new UserDataAlreadyAvailable(_USER_DATA_NOT_AVAILABLE);

  public static final UserDataAlreadyAvailable USER_DATA_ALREADY_AVAILABLE = new UserDataAlreadyAvailable(_USER_DATA_ALREADY_AVAILABLE);

  private int value = -1;

  private UserDataAlreadyAvailable(int value) {
    this.value = value;
  }

  public static UserDataAlreadyAvailable fromInt(int type) {
    switch(type) {
    case _USER_DATA_NOT_AVAILABLE: 
      return USER_DATA_NOT_AVAILABLE;
    case _USER_DATA_ALREADY_AVAILABLE: 
      return USER_DATA_ALREADY_AVAILABLE;
    default: 
      throw new IllegalArgumentException("Invalid User-Data-Already-Available value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _USER_DATA_NOT_AVAILABLE: 
      return "USER_DATA_NOT_AVAILABLE";
    case _USER_DATA_ALREADY_AVAILABLE: 
      return "USER_DATA_ALREADY_AVAILABLE";
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

}
