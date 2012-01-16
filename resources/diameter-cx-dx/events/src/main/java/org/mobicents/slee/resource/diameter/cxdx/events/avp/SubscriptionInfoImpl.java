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

package org.mobicents.slee.resource.diameter.cxdx.events.avp;

import static net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes.*;

import net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SubscriptionInfoImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SubscriptionInfoImpl extends GroupedAvpImpl implements SubscriptionInfo {

  public SubscriptionInfoImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SubscriptionInfoImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#getCallIDSIPHeader()
   */
  public byte[] getCallIDSIPHeader() {
    return getAvpAsOctetString(CALL_ID_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#getContact()
   */
  public byte[] getContact() {
    return getAvpAsOctetString(CONTACT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#getFromSIPHeader()
   */
  public byte[] getFromSIPHeader() {
    return getAvpAsOctetString(FROM_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#getRecordRoute()
   */
  public byte[] getRecordRoute() {
    return getAvpAsOctetString(RECORD_ROUTE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#getToSIPHeader()
   */
  public byte[] getToSIPHeader() {
    return getAvpAsOctetString(TO_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#hasCallIDSIPHeader()
   */
  public boolean hasCallIDSIPHeader() {
    return hasAvp(CALL_ID_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#hasContact()
   */
  public boolean hasContact() {
    return hasAvp(CONTACT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#hasFromSIPHeader()
   */
  public boolean hasFromSIPHeader() {
    return hasAvp(FROM_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#hasRecordRoute()
   */
  public boolean hasRecordRoute() {
    return hasAvp(RECORD_ROUTE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#hasToSIPHeader()
   */
  public boolean hasToSIPHeader() {
    return hasAvp(TO_SIP_HEADER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#setCallIDSIPHeader(java.lang.byte[])
   */
  public void setCallIDSIPHeader(byte[] callIDSIPHeader) {
    addAvp(CALL_ID_SIP_HEADER, CXDX_VENDOR_ID, callIDSIPHeader);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#setContact(java.lang.byte[])
   */
  public void setContact(byte[] contact) {
    addAvp(CONTACT, CXDX_VENDOR_ID, contact);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#setFromSIPHeader(java.lang.byte[])
   */
  public void setFromSIPHeader(byte[] fromSIPHeader) {
    addAvp(FROM_SIP_HEADER, CXDX_VENDOR_ID, fromSIPHeader);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#setRecordRoute(java.lang.byte[])
   */
  public void setRecordRoute(byte[] recordRoute) {
    addAvp(RECORD_ROUTE, CXDX_VENDOR_ID, recordRoute);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo#setToSIPHeader(java.lang.byte[])
   */
  public void setToSIPHeader(byte[] toSIPHeader) {
    addAvp(TO_SIP_HEADER, CXDX_VENDOR_ID, toSIPHeader);
  }

}
