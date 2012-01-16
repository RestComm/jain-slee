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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link RequestedEUTRANAuthenticationInfoAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class RequestedEUTRANAuthenticationInfoAvpImpl extends GroupedAvpImpl implements RequestedEUTRANAuthenticationInfoAvp {

  /**
   * 
   */
  public RequestedEUTRANAuthenticationInfoAvpImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public RequestedEUTRANAuthenticationInfoAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#hasNumberOfRequestedVectors()
   */
  public boolean hasNumberOfRequestedVectors() {
    return hasAvp(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#getNumberOfRequestedVectors()
   */
  public long getNumberOfRequestedVectors() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#setNumberOfRequestedVectors(long)
   */
  public void setNumberOfRequestedVectors(long numberOfRequestedVectors) {
    addAvp(DiameterS6aAvpCodes.NUMBER_OF_REQUESTED_VECTORS, DiameterS6aAvpCodes.S6A_VENDOR_ID, numberOfRequestedVectors);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#hasImmediateResponsePreferred()
   */
  public boolean hasImmediateResponsePreferred() {
    return hasAvp(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#getImmediateResponsePreferred()
   */
  public long getImmediateResponsePreferred() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#setImmediateResponsePreferred(long)
   */
  public void setImmediateResponsePreferred(long immediateResponsePreferred) {
    addAvp(DiameterS6aAvpCodes.IMMEDIATE_RESPONSE_PREFERRED, DiameterS6aAvpCodes.S6A_VENDOR_ID, immediateResponsePreferred);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#hasResynchronizationInfo()
   */
  public boolean hasResynchronizationInfo() {
    return hasAvp(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#getResynchronizationInfo()
   */
  public byte[] getResynchronizationInfo() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp#setResynchronizationInfo(java.lang.String)
   */
  public void setResynchronizationInfo(byte[] resynchronizationInfo) {
    addAvp(DiameterS6aAvpCodes.RESYNCHRONIZATION_INFO, DiameterS6aAvpCodes.S6A_VENDOR_ID, resynchronizationInfo);
  }

}
