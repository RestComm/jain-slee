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
 *<pre> <b>8.38. Redirect-Address-Type AVP</b>
 *
 *
 *   The Redirect-Address-Type AVP (AVP Code 433) is of type Enumerated
 *   and defines the address type of the address given in the Redirect-
 *   Server-Address AVP.
 *
 *   The address type can be one of the following:
 *
 *   <b>IPv4 Address                    0</b>
 *      The address type is in the form of "dotted-decimal" IPv4 address,
 *      as defined in [IPv4].
 *
 *   <b>IPv6 Address                    1</b>
 *      The address type is in the form of IPv6 address, as defined in
 *      [IPv6Addr].  The address is a text representation of the address
 *      in either the preferred or alternate text form [IPv6Addr].
 *      Conformant implementations MUST support the preferred form and
 *      SHOULD support the alternate text form for IPv6 addresses.
 *
 *   <b>URL                             2</b>
 *      The address type is in the form of Uniform Resource Locator, as
 *      defined in [URL].
 *
 *   <b>SIP URI                         3</b>
 *      The address type is in the form of SIP Uniform Resource
 *      Identifier, as defined in [SIP].
 *      </pre>
 *  
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum RedirectAddressType implements Enumerated {
  IPv4_Address(0),IPv6_Address(1),URL(2),SIP_URI(3);

  private int value = -1;

  private RedirectAddressType(int value) {
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

  public static RedirectAddressType fromInt(int type) throws IllegalArgumentException
  {
    switch (type) {
    case 0:
      return IPv4_Address;
    case 1:
      return IPv4_Address;
    case 2:
      return URL;
    case 3:
      return SIP_URI;

    default:
      throw new IllegalArgumentException();
    }
  }

  public String toString()
  {
    return super.toString().replace("_", " ");
  }

  public int getValue() {
    return this.value;
  }

}
