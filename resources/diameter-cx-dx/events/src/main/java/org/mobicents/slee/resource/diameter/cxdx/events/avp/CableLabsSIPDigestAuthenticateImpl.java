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

import net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPDigestAuthenticateImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class CableLabsSIPDigestAuthenticateImpl extends GroupedAvpImpl implements CableLabsSIPDigestAuthenticate {

  public CableLabsSIPDigestAuthenticateImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public CableLabsSIPDigestAuthenticateImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestAlgorithm()
   */
  public String getDigestAlgorithm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.CABLELABS_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestHA1()
   */
  public byte[] getDigestHA1() {
    return getAvpAsOctetString(DiameterCxDxAvpCodes.CABLELABS_DIGEST_HA1, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestQoP()
   */
  public String getDigestQoP() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.CABLELABS_DIGEST_QOP, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestRealm()
   */
  public String getDigestRealm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.CABLELABS_DIGEST_REALM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestDomain()
   */
  public String getDigestDomain() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.CABLELABS_DIGEST_DOMAIN, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#getDigestAuthParams()
   */
  public byte[][] getDigestAuthParams() {
	  return getAvpsAsOctetString(DiameterCxDxAvpCodes.CABLELABS_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#hasDigestAlgorithm()
   */
  public boolean hasDigestAlgorithm() {
    return hasAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#hasDigestHA1()
   */
  public boolean hasDigestHA1() {
    return hasAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_HA1, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#hasDigestQoP()
   */
  public boolean hasDigestQoP() {
    return hasAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_QOP, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#hasDigestRealm()
   */
  public boolean hasDigestRealm() {
    return hasAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_REALM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#hasDigestDomain()
   */
  public boolean hasDigestDomain() {
    return hasAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_DOMAIN, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestAlgorithm(java.lang.String)
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_ALGORITHM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestAlgorithm);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestHA1(java.lang.String)
   */
  public void setDigestHA1(byte[] digestHA1) {
    addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_HA1, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestHA1);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestQoP(java.lang.String)
   */
  public void setDigestQoP(String digestQoP) {
    addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_QOP, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestQoP);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestRealm(java.lang.String)
   */
  public void setDigestRealm(String digestRealm) {
    addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_REALM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestRealm);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestDomain(java.lang.String)
   */
  public void setDigestDomain(String digestDomain) {
    addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_DOMAIN, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestDomain);
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestAuthParam(java.lang.String)
   */
  public void setDigestAuthParam(byte[] digestAuthParam)
  {
	  addAvp(DiameterCxDxAvpCodes.CABLELABS_DIGEST_AUTH_PARAM, DiameterCxDxAvpCodes.CABLELABS_VENDOR_ID, digestAuthParam);  
  }
  
  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate#setDigestAuthParams(java.lang.String[])
   */
  public void setDigestAuthParams(byte[][] digestAuthParams)
  {
	  for(byte[] digestAuthParam : digestAuthParams) {
		  setDigestAuthParam(digestAuthParam);
	    }
  }
}
