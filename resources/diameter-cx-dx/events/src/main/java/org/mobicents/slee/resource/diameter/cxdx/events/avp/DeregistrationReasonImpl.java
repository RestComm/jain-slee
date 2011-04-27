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

import net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason;
import net.java.slee.resource.diameter.cxdx.events.avp.ReasonCode;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * DeregistrationReasonImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class DeregistrationReasonImpl extends GroupedAvpImpl implements DeregistrationReason {

  public DeregistrationReasonImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public DeregistrationReasonImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#getReasonCode()
   */
  public ReasonCode getReasonCode() {
    return (ReasonCode) getAvpAsEnumerated(REASON_CODE, CXDX_VENDOR_ID, ReasonCode.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#getReasonInfo()
   */
  public String getReasonInfo() {
    return getAvpAsUTF8String(REASON_INFO, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#hasReasonCode()
   */
  public boolean hasReasonCode() {
    return hasAvp(REASON_CODE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#hasReasonInfo()
   */
  public boolean hasReasonInfo() {
    return hasAvp(REASON_INFO, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#setReasonCode(net.java.slee.resource.diameter.cxdx.events.avp.ReasonCode)
   */
  public void setReasonCode(ReasonCode reasonCode) {
    addAvp(REASON_CODE, CXDX_VENDOR_ID, reasonCode.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.DeregistrationReason#setReasonInfo(java.lang.String)
   */
  public void setReasonInfo(String reasonInfo) {
    addAvp(REASON_INFO, CXDX_VENDOR_ID, reasonInfo);
  }

}
