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

import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPDigestAuthenticateImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EtsiSIPAuthenticateImpl extends GroupedAvpImpl implements EtsiSIPAuthenticate {

  public EtsiSIPAuthenticateImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public EtsiSIPAuthenticateImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestRealm()
   */
  public String getDigestRealm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestNonce()
   */
  public String getDigestNonce() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestDomain()
   */
  public String getDigestDomain() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_DOMAIN, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestOpaque()
   */
  public String getDigestOpaque() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestStale()
   */
  public String getDigestStale() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_STALE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestAlgorithm()
   */
  public String getDigestAlgorithm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestQoP()
   */
  public String getDigestQoP() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestHA1()
   */
  public String getDigestHA1() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_HA1, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#getDigestAuthParams()
   */
  public byte[][] getDigestAuthParams() {
	  return getAvpsAsOctetString(DiameterCxDxAvpCodes.ETSI_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestRealm()
   */
  public boolean hasDigestRealm() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestNonce()
   */
  public boolean hasDigestNonce() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestDomain()
   */
  public boolean hasDigestDomain() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_DOMAIN, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestOpaque()
   */
  public boolean hasDigestOpaque() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestStale()
   */
  public boolean hasDigestStale() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_STALE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestAlgorithm()
   */
  public boolean hasDigestAlgorithm() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestQoP()
   */
  public boolean hasDigestQoP() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#hasDigestHA1()
   */
  public boolean hasDigestHA1() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_HA1, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestRealm(java.lang.String)
   */
  public void setDigestRealm(String digestRealm) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestRealm);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestNonce(java.lang.String)
   */
  public void setDigestNonce(String digestNonce) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestNonce);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestDomain(java.lang.String)
   */
  public void setDigestDomain(String digestDomain) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_DOMAIN, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestDomain);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestOpaque(java.lang.String)
   */
  public void setDigestOpaque(String digestOpaque) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestOpaque);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestOpaque(java.lang.String)
   */
  public void setDigestStale(String digestStale) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_STALE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestStale);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestAlgorithm(java.lang.String)
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestAlgorithm);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestQoP(java.lang.String)
   */
  public void setDigestQoP(String digestQoP) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestQoP);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestHA1(java.lang.String)
   */
  public void setDigestHA1(String digestHA1) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_HA1, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestHA1);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestAuthParam(java.lang.String)
   */
  public void setDigestAuthParam(byte[] digestAuthParam)
  {
	  addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestAuthParam);  
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate#setDigestAuthParams(java.lang.String[])
   */
  public void setDigestAuthParams(byte[][] digestAuthParams)
  {
	  for(byte[] digestAuthParam : digestAuthParams) {
		  setDigestAuthParam(digestAuthParam);
	    }
  }
}
