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

import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPDigestAuthenticateImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class EtsiSIPAuthorizationImpl extends GroupedAvpImpl implements EtsiSIPAuthorization {

  public EtsiSIPAuthorizationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public EtsiSIPAuthorizationImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestUsername()
   */
  public String getDigestUsername() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_USERNAME, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestRealm()
   */
  public String getDigestRealm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestNonce()
   */
  public String getDigestNonce() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestURI()
   */
  public String getDigestURI() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_URI, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestResponse()
   */
  public String getDigestResponse() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestAlgorithm()
   */
  public String getDigestAlgorithm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestCNonce()
   */
  public String getDigestCNonce() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestOpaque()
   */
  public String getDigestOpaque() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }    

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestQoP()
   */
  public String getDigestQoP() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.CXDX_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestNonceCount()
   */
  public String getDigestNonceCount() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestMethod()
   */
  public String getDigestMethod() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_METHOD, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestEntityBodyHash()
   */
  public String getDigestEntityBodyHash() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.ETSI_DIGEST_ENTITY_BODY_HASH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#getDigestAuthParams()
   */
  public byte[][] getDigestAuthParams() {
	  return getAvpsAsOctetString(DiameterCxDxAvpCodes.ETSI_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestUsername()
   */
  public boolean hasDigestUsername() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_USERNAME, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestRealm()
   */
  public boolean hasDigestRealm() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestNonce()
   */
  public boolean hasDigestNonce() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestURI()
   */
  public boolean hasDigestURI() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_URI, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestResponse()
   */
  public boolean hasDigestResponse() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestAlgorithm()
   */
  public boolean hasDigestAlgorithm() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestCNonce()
   */
  public boolean hasDigestCNonce() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  } 
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestOpaque()
   */
  public boolean hasDigestOpaque() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestQoP()
   */
  public boolean hasDigestQoP() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestNonceCount()
   */
  public boolean hasDigestNonceCount() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestMethod()
   */
  public boolean hasDigestMethod() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_METHOD, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  } 
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#hasDigestEntityBodyHash()
   */
  public boolean hasDigestEntityBodyHash() {
    return hasAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ENTITY_BODY_HASH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID);
  } 

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestUsername(java.lang.String)
   */
  public void setDigestUsername(String digestUsername) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_USERNAME, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestUsername);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestRealm(java.lang.String)
   */
  public void setDigestRealm(String digestRealm) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_REALM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestRealm);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestNonce(java.lang.String)
   */
  public void setDigestNonce(String digestNonce) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestNonce);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestURI(java.lang.String)
   */
  public void setDigestURI(String digestURI) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_URI, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestURI);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestResponse(java.lang.String)
   */
  public void setDigestResponse(String digestResponse) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_RESPONSE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestResponse);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestAlgorithm(java.lang.String)
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestAlgorithm);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestCNonce(java.lang.String)
   */
  public void setDigestCNonce(String digestCNonce) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_CNONCE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestCNonce);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestOpaque(java.lang.String)
   */
  public void setDigestOpaque(String digestOpaque) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_OPAQUE, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestOpaque);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestQoP(java.lang.String)
   */
  public void setDigestQoP(String digestQoP) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_QOP, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestQoP);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestHA1(java.lang.String)
   */
  public void setDigestNonceCount(String digestNonceCount) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_NONCE_COUNT, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestNonceCount);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestMethod(java.lang.String)
   */
  public void setDigestMethod(String digestMethod) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_METHOD, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestMethod);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestEntityBodyHash(java.lang.String)
   */
  public void setDigestEntityBodyHash(String digestEntityBodyHash) {
    addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_ENTITY_BODY_HASH, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestEntityBodyHash);
  }  
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestAuthParam(java.lang.String)
   */
  public void setDigestAuthParam(byte[] digestAuthParam)
  {
	  addAvp(DiameterCxDxAvpCodes.ETSI_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.ETSI_VENDOR_ID, digestAuthParam);  
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization#setDigestAuthParams(java.lang.String[])
   */
  public void setDigestAuthParams(byte[][] digestAuthParams)
  {
	  for(byte[] digestAuthParam : digestAuthParams) {
		  setDigestAuthParam(digestAuthParam);
	    }
  }
}
