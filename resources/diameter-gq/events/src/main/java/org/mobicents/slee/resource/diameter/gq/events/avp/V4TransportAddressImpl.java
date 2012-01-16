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

import net.java.slee.resource.diameter.gq.events.avp.V4TransportAddress;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;


/**
 * Implementation for {@link V4TransportAddress}
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public class V4TransportAddressImpl extends GroupedAvpImpl implements V4TransportAddress {

  public V4TransportAddressImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public V4TransportAddressImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#getFramedIPAddress()
   */
  public byte[] getFramedIPAddress() {
    return getAvpAsOctetString(DiameterGqAvpCodes.FRAMED_IP_ADDRESS);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#getPortNumber()
   */
  public long getPortNumber() {
    return getAvpAsUnsigned32(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#hasFramedIPAddress()
   */
  public boolean hasFramedIPAddress() {
    return hasAvp(DiameterGqAvpCodes.FRAMED_IP_ADDRESS);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#hasPortNumber()
   */
  public boolean hasPortNumber() {
    return hasAvp(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#setFramedIPAddress()
   */
  public void setFramedIPAddress(byte[] framedIPAddress) {
    addAvp(DiameterGqAvpCodes.FRAMED_IP_ADDRESS, framedIPAddress);
  }

  @Override
  /*
   * (non-Javadoc)
   * 
   * @see V4TransportAddress#setPortNumber()
   */
  public void setPortNumber(long portNumber) {
    addAvp(DiameterGqAvpCodes.ETSI_PORT_NUMBER, DiameterGqAvpCodes.ETSI_VENDOR_ID, portNumber);
  }
}
