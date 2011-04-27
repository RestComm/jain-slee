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
 * AVP representation of Subs-Req AVP. Defined in 3GPP TS 29.329 section 6.3.7.<br>
 * The Subs-Req-Type AVP is of type Enumerated, and indicates the type of the
 * subscription-to-notifications request. The following values are defined:
 * <ul>
 * <li>Subscribe (0) - This value is used by an AS to subscribe to notifications
 * of changes in data.</li>
 * <li>Unsubscribe (1) - This value is used by an AS to unsubscribe to
 * notifications of changes in data.</li>
 * </ul>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SubsReqType implements Serializable, Enumerated {

  private static final long serialVersionUID = -7748012167965997571L;

  /**
   * Int value equal in diameter message - it indicates subscription request - subscirbe to updates - see TS29.328 for description
   */
  public static final int _SUBSCRIBE = 0;

  /**
   * Int value equal in diameter message - it indicates subscription request - unsubscirbe to updates - see TS29.328 for description
   */
  public static final int _UNSUBSCRIBE = 1;

  /**
   * Singleton representation of {@link _SUBSCRIBE}
   */
  public static final SubsReqType SUBSCRIBE = new SubsReqType(_SUBSCRIBE);

  /**
   * Singleton representation of {@link _UNSUBSCRIBE}
   */
  public static final SubsReqType UNSUBSCRIBE = new SubsReqType(_UNSUBSCRIBE);

  private SubsReqType(int value) {
    this.value = value;
  }

  public static SubsReqType fromInt(int type) {
    switch (type) {
    case _SUBSCRIBE:
      return SUBSCRIBE;
    case _UNSUBSCRIBE:
      return UNSUBSCRIBE;
    default:
      throw new IllegalArgumentException("Invalid SubsReqType value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch (value) {
    case _SUBSCRIBE:
      return "SUBSCRIBE";
    case _UNSUBSCRIBE:
      return "UNSUBSCRIBE";
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
