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

import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPDigestAuthenticateImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EtsiSIPAuthenticationInfoImpl extends GroupedAvpImpl implements EtsiSIPAuthenticationInfo {

  public EtsiSIPAuthenticationInfoImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public EtsiSIPAuthenticationInfoImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#getDigestNextnonce()
   */
  public String getDigestNextnonce() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_NEXTNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#getDigestQoP()
   */
  public String getDigestQoP() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#getDigestResponseAuth()
   */
  public String getDigestResponseAuth() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE_AUTH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#getDigestCNonce()
   */
  public String getDigestCNonce() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#getDigestNonceCount()
   */
  public String getDigestNonceCount() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#hasDigestNextnonce()
   */
  public boolean hasDigestNextnonce() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NEXTNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#hasDigestQoP()
   */
  public boolean hasDigestQoP() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#DigestResponseAuth()
   */
  public boolean hasDigestResponseAuth() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE_AUTH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#hasDigestCNonce()
   */
  public boolean hasDigestCNonce() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#hasDigestNonceCount()
   */
  public boolean hasDigestNonceCount() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#setDigestNextnonce(java.lang.String)
   */
  public void setDigestNextnonce(String digestNextnonce) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NEXTNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestNextnonce);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#setDigestQoP(java.lang.String)
   */
  public void setDigestQoP(String digestQoP) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestQoP);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#setDigestResponseAuth(java.lang.String)
   */
  public void setDigestResponseAuth(String digestResponseAuth) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE_AUTH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestResponseAuth);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#setDigestCNonce(java.lang.String)
   */
  public void setDigestCNonce(String digestCNonce) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestCNonce);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo#setDigestNonceCount(java.lang.String)
   */
  public void setDigestNonceCount(String digestNonceCount) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestNonceCount);
  }
}
