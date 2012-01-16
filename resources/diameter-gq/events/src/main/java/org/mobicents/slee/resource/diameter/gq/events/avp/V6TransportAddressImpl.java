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

package org.mobicents.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.gq.events.avp.V6TransportAddress;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * Implementation for {@link V6TransportAddress}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class V6TransportAddressImpl extends GroupedAvpImpl implements V6TransportAddress {

  public V6TransportAddressImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public V6TransportAddressImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#getFramedIPV6Prefix()
   */
  public byte[] getFramedIPV6Prefix() {
    return getAvpAsOctetString(DiameterGqAvpCodes.FRAMED_IPV6_PREFIX);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#getPortNumber()
   */
  public long getPortNumber() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#hasFramedIPV6Prefix()
   */
  public boolean hasFramedIPV6Prefix() {
    return hasAvp(DiameterGqAvpCodes.FRAMED_IPV6_PREFIX);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#hasPortNumber()
   */
  public boolean hasPortNumber() {
    return hasAvp(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#setFramedIPV6Prefix()
   */
  public void setFramedIPV6Prefix(byte[] framedIPV6Prefix) {
    addAvp(DiameterGqAvpCodes.FRAMED_IPV6_PREFIX, framedIPV6Prefix);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V6TransportAddress#setPortNumber()
   */
  public void setPortNumber(long portNumber) {
    addAvp(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID, portNumber);
  }

}
