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
 * <b>6.3.45    Loose-Route-Indication AVP</b>
 * The Loose-Route-Indication AVP is of type Enumerated and indicates to the S-CSCF whether or not
 * the loose route mechanism is required to serve the registered Public User Identities. The 
 * following values are defined:
 * 
 * LOOSE_ROUTE_NOT_REQUIRED (0)
 * 
 * LOOSE_ROUTE_REQUIRED (1)
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LooseRouteIndication implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _LOOSE_ROUTE_NOT_REQUIRED = 0;

  public static final int _LOOSE_ROUTE_REQUIRED = 1;

  public static final LooseRouteIndication LOOSE_ROUTE_NOT_REQUIRED = new LooseRouteIndication(_LOOSE_ROUTE_NOT_REQUIRED);

  public static final LooseRouteIndication LOOSE_ROUTE_REQUIRED = new LooseRouteIndication(_LOOSE_ROUTE_REQUIRED);

  private int value = -1;

  private LooseRouteIndication(int value) {
    this.value = value;
  }

  public static LooseRouteIndication fromInt(int type) {
    switch(type) {
    case _LOOSE_ROUTE_NOT_REQUIRED: 
      return LOOSE_ROUTE_NOT_REQUIRED;
    case _LOOSE_ROUTE_REQUIRED: 
      return LOOSE_ROUTE_REQUIRED;
    default: 
      throw new IllegalArgumentException("Invalid Loose-Route-Indication value: " + type);
    }
  }

  public int getValue() {
    return value;
  }

  public String toString() {
    switch(value) {
    case _LOOSE_ROUTE_NOT_REQUIRED: 
      return "LOOSE_ROUTE_NOT_REQUIRED";
    case _LOOSE_ROUTE_REQUIRED: 
      return "LOOSE_ROUTE_REQUIRED";
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
