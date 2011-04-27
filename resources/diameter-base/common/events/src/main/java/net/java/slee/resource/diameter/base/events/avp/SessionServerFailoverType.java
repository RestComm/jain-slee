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
 * Java class to represent the SessionServerFailover enumerated type.
 * <p/>
 * Documentation from the relevant specification:
 * <p/>
 * The Session-Server-Failover AVP (AVP Code 271) is of type Enumerated, and MAY
 * be present in application-specific authorization answer messages that either
 * do not include the Session-Binding AVP or include the Session-Binding AVP with
 * any of the bits set to a zero value. If present, this AVP MAY inform the
 * Diameter client that if a re-auth or STR message fails due to a delivery 
 * problem, the Diameter client SHOULD issue a subsequent message without 
 * the Destination-Host AVP. When absent, the default value is REFUSE_SERVICE. 
 *
 * @author Open Cloud
 */
public class SessionServerFailoverType implements Serializable, Enumerated {

  private static final long serialVersionUID = 1L;

  public static final int _REFUSE_SERVICE = 0;
  public static final int _TRY_AGAIN = 1;
  public static final int _ALLOW_SERVICE = 2;
  public static final int _TRY_AGAIN_ALLOW_SERVICE = 3;

  /**
   * If either the re-auth or the STR message delivery fails, terminate service with the user, and do not attempt any subsequent attempts. 
   */
  public static final SessionServerFailoverType REFUSE_SERVICE = new SessionServerFailoverType(_REFUSE_SERVICE);

  /**
   * If either the re-auth or the STR message delivery fails, resend the failed message without the Destination-Host AVP present. 
   */
  public static final SessionServerFailoverType TRY_AGAIN = new SessionServerFailoverType(_TRY_AGAIN);

  /**
   * If re-auth message delivery fails, assume that re-authorization succeeded. If STR message delivery fails, terminate the session. 
   */
  public static final SessionServerFailoverType ALLOW_SERVICE = new SessionServerFailoverType(_ALLOW_SERVICE);

  /**
   * If either the re-auth or the STR message delivery fails, resend the failed message without the Destination-Host AVP present. If the second delivery fails for re-auth, assume re-authorization succeeded. If the second delivery fails for STR, terminate the session. 
   */
  public static final SessionServerFailoverType TRY_AGAIN_ALLOW_SERVICE = new SessionServerFailoverType(_TRY_AGAIN_ALLOW_SERVICE);

  private SessionServerFailoverType(int value) {
    this.value = value;
  }

  public static SessionServerFailoverType fromInt(int type) {
    switch(type) {
    case _REFUSE_SERVICE:
      return REFUSE_SERVICE;
    case _TRY_AGAIN:
      return TRY_AGAIN;
    case _ALLOW_SERVICE:
      return ALLOW_SERVICE;
    case _TRY_AGAIN_ALLOW_SERVICE:
      return TRY_AGAIN_ALLOW_SERVICE;
    default:
      throw new IllegalArgumentException("Invalid SessionServerFailover value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _REFUSE_SERVICE:
      return "REFUSE_SERVICE";
    case _TRY_AGAIN:
      return "TRY_AGAIN";
    case _ALLOW_SERVICE:
      return "ALLOW_SERVICE";
    case _TRY_AGAIN_ALLOW_SERVICE:
      return "TRY_AGAIN_ALLOW_SERVICE";
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
