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

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 * Java class to represent the MBMS-2G-3G-Indicator enumerated type.
 * <pre>
 * 17.7.10  MBMS-2G-3G-Indicator AVP
 * The MBMS-2G-3G-Indicator AVP  (AVP code 907) is of type Enumerated. It indicates  whether the MBMS bearer service will be delivered  in 2G- only, 3G- only of both  coverage areas. The following values are supported:
 * 2G (0)
 *     The MBMS bearer service shall only be delivered in 2G only coverage areas.
 * 3G (1)
 *     The MBMS bearer service shall only be delivered in 3G only coverage areas.
 * 2G-AND-3G (2)
 *    The MBMS bearer service shall be delivered both in 2G and 3G coverage areas.
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public enum Mbms2g3gIndicator implements Enumerated {

  _2G(0), 
  _3G(1), 
  _2G_AND_3G(2);

  private int value = -1;

  private Mbms2g3gIndicator(int value) {
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

  public static Mbms2g3gIndicator fromInt(int type) throws IllegalArgumentException
  {
    switch (type) {
    case 0:
      return _2G;
    case 1:
      return _3G;
    case 2:
      return _2G_AND_3G;

    default:
      throw new IllegalArgumentException();
    }
  }

}
