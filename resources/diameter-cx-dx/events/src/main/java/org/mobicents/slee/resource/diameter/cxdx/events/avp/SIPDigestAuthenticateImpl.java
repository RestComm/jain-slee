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

import net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes; 

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPDigestAuthenticateImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SIPDigestAuthenticateImpl extends GroupedAvpImpl implements SIPDigestAuthenticate {

  public SIPDigestAuthenticateImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SIPDigestAuthenticateImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#getDigestAlgorithm()
   */
  public String getDigestAlgorithm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.TGPP_DIGEST_ALGORITHM); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#getDigestHA1()
   */
  public byte[] getDigestHA1() {
    return getAvpAsOctetString(DiameterCxDxAvpCodes.TGPP_DIGEST_HA1); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#getDigestQoP()
   */
  public String getDigestQoP() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.TGPP_DIGEST_QOP); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#getDigestRealm()
   */
  public String getDigestRealm() {
    return getAvpAsUTF8String(DiameterCxDxAvpCodes.TGPP_DIGEST_REALM); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#hasDigestAlgorithm()
   */
  public boolean hasDigestAlgorithm() {
    return hasAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_ALGORITHM); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#hasDigestHA1()
   */
  public boolean hasDigestHA1() {
    return hasAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_HA1); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#hasDigestQoP()
   */
  public boolean hasDigestQoP() {
    return hasAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_QOP); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#hasDigestRealm()
   */
  public boolean hasDigestRealm() {
    return hasAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_REALM); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#setDigestAlgorithm(java.lang.String)
   */
  public void setDigestAlgorithm(String digestAlgorithm) {
    addAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_ALGORITHM, digestAlgorithm); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#setDigestHA1(java.lang.String)
   */
  public void setDigestHA1(byte[] digestHA1) {
    addAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_HA1, digestHA1); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#setDigestQoP(java.lang.String)
   */
  public void setDigestQoP(String digestQoP) {
    addAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_QOP, digestQoP); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate#setDigestRealm(java.lang.String)
   */
  public void setDigestRealm(String digestRealm) {
    addAvp(DiameterCxDxAvpCodes.TGPP_DIGEST_REALM, digestRealm); 
  }

}
