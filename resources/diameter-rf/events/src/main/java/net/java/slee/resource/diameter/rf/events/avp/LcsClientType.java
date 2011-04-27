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

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the LCS-Client-Type enumerated type.
 * 
 * <pre>
 * The LCS-Client-Type AVP (AVP code 1241) is of type Enumerated and contains an estimate 
 * of the location of an MS in universal coordinates and the accuracy of the estimate.
 * 
 * It can be one of the following values:
 *  EMERGENCY_SERVICES          0
 *  VALUE_ADDED_SERVICES        1
 *  PLMN_OPERATOR_SERVICES      2
 *  LAWFUL_INTERCEPT_SERVICES   3
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum LcsClientType implements Enumerated {

  EMERGENCY_SERVICES(0), 
  VALUE_ADDED_SERVICES(1), 
  PLMN_OPERATOR_SERVICES(3), 
  LAWFUL_INTERCEPT_SERVICES(4);

  private int value = -1;

  private LcsClientType(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static LcsClientType fromInt(int type) throws IllegalArgumentException {
    switch (type) {
    case 0:
      return EMERGENCY_SERVICES;
    case 1:
      return VALUE_ADDED_SERVICES;
    case 2:
      return PLMN_OPERATOR_SERVICES;
    case 3:
      return LAWFUL_INTERCEPT_SERVICES;

    default:
      throw new IllegalArgumentException();
    }
  }

}
