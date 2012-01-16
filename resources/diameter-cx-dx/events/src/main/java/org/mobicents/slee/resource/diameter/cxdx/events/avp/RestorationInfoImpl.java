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

import net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * RestorationInfoImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RestorationInfoImpl extends GroupedAvpImpl implements RestorationInfo {

  public RestorationInfoImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public RestorationInfoImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#getContact()
   */
  public byte[] getContact() {
    return getAvpAsOctetString(CONTACT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#getPath()
   */
  public byte[] getPath() {
    return getAvpAsOctetString(PATH, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#getSubscriptionInfo()
   */
  public SubscriptionInfo getSubscriptionInfo() {
    return (SubscriptionInfo) getAvpAsCustom(SUBSCRIPTION_INFO, CXDX_VENDOR_ID, SubscriptionInfoImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#hasContact()
   */
  public boolean hasContact() {
    return hasAvp(CONTACT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#hasPath()
   */
  public boolean hasPath() {
    return hasAvp(PATH, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#hasSubscriptionInfo()
   */
  public boolean hasSubscriptionInfo() {
    return hasAvp(SUBSCRIPTION_INFO, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#setContact(java.lang.byte[])
   */
  public void setContact(byte[] contact) {
    addAvp(CONTACT, CXDX_VENDOR_ID, contact);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#setPath(java.lang.byte[])
   */
  public void setPath(byte[] path) {
    addAvp(PATH, CXDX_VENDOR_ID, path);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.RestorationInfo#setSubscriptionInfo(net.java.slee.resource.diameter.cxdx.events.avp.SubscriptionInfo)
   */
  public void setSubscriptionInfo(SubscriptionInfo subscriptionInfo) {
    addAvp(SUBSCRIPTION_INFO, CXDX_VENDOR_ID, subscriptionInfo.byteArrayValue());
  }

}
