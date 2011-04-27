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

package net.java.slee.resource.diameter.cca.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 *<pre> <b>8.32. CC-Unit-Type AVP</b>
 *
 *
 *   The CC-Unit-Type AVP (AVP Code 454) is of type Enumerated and
 *   specifies the type of units considered to be pooled into a credit
 *   pool.
 *
 *   The following values are defined for the CC-Unit-Type AVP:
 *
 *      <b>TIME                         0</b>
 *      <b>MONEY                        1</b>
 *      <b>TOTAL-OCTETS                 2</b>
 *      <b>INPUT-OCTETS                 3</b>
 *      <b>OUTPUT-OCTETS                4</b>
 *      <b>SERVICE-SPECIFIC-UNITS       5</b>
 *      <pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum CcUnitType implements Enumerated {
  
  TIME(0), MONEY(1), TOTAL_OCTETS(2), INPUT_OCTETS(3), OUTPUT_OCTETS(4), SERVICE_SPECIFIC_UNITS(5);

  private int value = -1;

  private CcUnitType(int value) {
    this.value = value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static CcUnitType fromInt(int presumableValue) throws IllegalArgumentException {

    switch (presumableValue) {
    case 0:
      return TIME;
    case 1:
      return MONEY;
    case 2:
      return TOTAL_OCTETS;
    case 3:
      return INPUT_OCTETS;
    case 4:
      return OUTPUT_OCTETS;
    case 5:
      return SERVICE_SPECIFIC_UNITS;
      
    default:
      throw new IllegalArgumentException();
    }
  }

  public String toString() {
    return super.toString().replace("_", "-");
  }

  public int getValue() {
    return this.value;
  }

}
