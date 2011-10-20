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
 * Java class representing the Current-Location-Retrieved enumerated type.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.3.125  Current-Location-Retrieved
 * 
 * The Current-Location-Retrieved AVP is of type Enumerated. The following values are defined:
 * 
 *   ACTIVE-LOCATION-RETRIEVAL (0)
 *   
 * This value is used when location information was obtained after a successful paging procedure
 * for Active Location Retrieval.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CurrentLocationRetrieved implements Enumerated, Serializable {

  private static final long serialVersionUID = 1L;

  public static final int _ACTIVE_LOCATION_RETRIEVAL = 0;

  public static final CurrentLocationRetrieved ACTIVE_LOCATION_RETRIEVAL = new CurrentLocationRetrieved(_ACTIVE_LOCATION_RETRIEVAL);

  private int value = -1;

  private CurrentLocationRetrieved(int value) {
    this.value = value;
  }

  public static CurrentLocationRetrieved fromInt(int type) {
    switch (type) {
      case _ACTIVE_LOCATION_RETRIEVAL:
        return ACTIVE_LOCATION_RETRIEVAL;
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
      case _ACTIVE_LOCATION_RETRIEVAL:
        return "ACTIVE-LOCATION-RETRIEVAL";
      default:
        return "<Invalid Value>";
    }
  }
}