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

import java.util.Arrays;

/**
 * 
 * Java class to represent the Address AVP type.
 * <p/>
 * The Address format is derived from the OctetString AVP Base
 * Format.  It is a discriminated union, representing, for example a
 * 32-bit (IPv4) [IPV4] or 128-bit (IPv6) [IPV6] address, most
 * significant octet first.  The first two octets of the Address
 * AVP represents the AddressType, which contains an Address Family
 * defined in [IANAADFAM].  The AddressType is used to discriminate
 * the content and format of the remaining octets.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class Address {

  private final AddressType addressType;
  private final byte[] address;

  public Address(AddressType addressType, byte[] address) {
    this.addressType = addressType;
    this.address = address;
  }

  public AddressType getAddressType() {
    return addressType;
  }

  public byte[] getAddress() {
    return address;
  }

  public static Address decode(byte[] encodedAddress) {
    // "The first two octets of the Address AVP represents the AddressType"
    // And in 8bits at a time
    int addressTypeInt = (encodedAddress[0] << 8);
    addressTypeInt |= (encodedAddress[1]) & 0x000000ff;

    // decode the address bytes
    byte[] addressBytes = new byte[encodedAddress.length - 2];
    System.arraycopy(encodedAddress, 2, addressBytes, 0, addressBytes.length);

    return new Address(AddressType.fromInt(addressTypeInt), addressBytes);
  }

  public byte[] encode() {
    // "The first two octets of the Address AVP represents the AddressType"
    int addressTypeInt = addressType.getValue();
    byte[] encodedAddress = new byte[address.length + 2];
    encodedAddress[0] = (byte) (addressTypeInt >> 8); // get bits 15-8
    encodedAddress[1] = (byte) addressTypeInt; // get bits 7-0

    // encode the address bytes
    System.arraycopy(address, 0, encodedAddress, 2, address.length);
    return encodedAddress;
  }

  @Override
  public String toString() {
     return addressType + "=" + new String(address);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(address) + addressType.getValue();
  }

  @Override
  public boolean equals(Object obj) {
    if(obj == null) {
      return false;
    }

    if(obj instanceof Address) {
      Address that = (Address) obj;
      return this.addressType == that.addressType && Arrays.equals(this.address, that.address); 
    }

    return false;
  }

}
