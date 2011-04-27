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

package net.java.slee.resource.diameter.base.events.avp;

import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Java class to represent the ReAuthRequestType enumerated type.
 *<P/>
 * Documentation from the relevant specification:
 *<P/>
 * The Re-Auth-Request-Type AVP (AVP Code 285) is of type Enumerated and is included in 
 * application-specific auth answers to inform the client of the action expected upon expiration
 * of the Authorization-Lifetime. If the answer message contains an Authorization-Lifetime AVP
 * with a positive value, the Re-Auth-Request-Type AVP MUST be present in an answer message. 
 *
 * @author Open Cloud
 */
public class ReAuthRequestType implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _AUTHORIZE_ONLY = 0;
  public static final int _AUTHORIZE_AUTHENTICATE = 1;

  /**
   * An authorization only re-auth is expected upon expiration of the 
   * Authorization-Lifetime. This is the default value if the AVP is not 
   * present in answer messages that include the Authorization- Lifetime. 
   */
  public static final ReAuthRequestType AUTHORIZE_ONLY = new ReAuthRequestType(_AUTHORIZE_ONLY);

  /**
   * An authentication and authorization re-auth is expected upon expiration 
   * of the Authorization-Lifetime. 
   */
  public static final ReAuthRequestType AUTHORIZE_AUTHENTICATE = new ReAuthRequestType(_AUTHORIZE_AUTHENTICATE);

  private ReAuthRequestType(int value) {
    this.value = value;
  }

  public static ReAuthRequestType fromInt(int type) {
    switch(type) {
    case _AUTHORIZE_ONLY: 
      return AUTHORIZE_ONLY;
    case _AUTHORIZE_AUTHENTICATE: 
      return AUTHORIZE_AUTHENTICATE;
    default: 
      throw new IllegalArgumentException("Invalid ReAuthRequestType value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _AUTHORIZE_ONLY:
      return "AUTHORIZE_ONLY";
    case _AUTHORIZE_AUTHENTICATE:
      return "AUTHORIZE_AUTHENTICATE";
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

  private int value;
}
