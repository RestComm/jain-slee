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
 * Java class to represent the PocSessionType enumerated type.
 */
public class PocSessionType implements net.java.slee.resource.diameter.base.events.avp.Enumerated, java.io.Serializable{

  private static final long serialVersionUID = 1L;

  public static final int _ONE_TO_ONE_POC_SESSION = 0;

  public static final int _CHAT_POC_GROUP_SESSION = 1;

  public static final int _PRE_ARRANGED_POC_GROUP_SESSION = 2;

  public static final int _AD_HOC_POC_GROUP_SESSION = 3;

  public static final PocSessionType ONE_TO_ONE_POC_SESSION = new PocSessionType(_ONE_TO_ONE_POC_SESSION);

  public static final PocSessionType CHAT_POC_GROUP_SESSION = new PocSessionType(_CHAT_POC_GROUP_SESSION);

  public static final PocSessionType PRE_ARRANGED_POC_GROUP_SESSION = new PocSessionType(_PRE_ARRANGED_POC_GROUP_SESSION);

  public static final PocSessionType AD_HOC_POC_GROUP_SESSION = new PocSessionType(_AD_HOC_POC_GROUP_SESSION);

  private PocSessionType(int v) {
    value = v;
  }

  /**
   * Return the value of this instance of this enumerated type.
   */
  public static PocSessionType fromInt(int type) {
    switch(type) {
    case _AD_HOC_POC_GROUP_SESSION: return AD_HOC_POC_GROUP_SESSION;

    case _CHAT_POC_GROUP_SESSION: return CHAT_POC_GROUP_SESSION;

    case _ONE_TO_ONE_POC_SESSION: return ONE_TO_ONE_POC_SESSION;

    case _PRE_ARRANGED_POC_GROUP_SESSION: return PRE_ARRANGED_POC_GROUP_SESSION;
    default: throw new IllegalArgumentException("Invalid PocSessionType value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _AD_HOC_POC_GROUP_SESSION: return "AD_HOC_POC_GROUP_SESSION";

    case _CHAT_POC_GROUP_SESSION: return "CHAT_POC_GROUP_SESSION";

    case _ONE_TO_ONE_POC_SESSION: return "ONE_TO_ONE_POC_SESSION";

    case _PRE_ARRANGED_POC_GROUP_SESSION: return "PRE_ARRANGED_POC_GROUP_SESSION";
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
