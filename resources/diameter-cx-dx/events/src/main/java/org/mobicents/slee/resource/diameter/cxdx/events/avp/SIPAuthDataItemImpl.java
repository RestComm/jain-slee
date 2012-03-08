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

import net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate;
import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo;
import net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization;
import net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem;
import net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 *
 * SIPAuthDataItemImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class SIPAuthDataItemImpl extends GroupedAvpImpl implements SIPAuthDataItem {

  public SIPAuthDataItemImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public SIPAuthDataItemImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getConfidentialityKey()
   */
  public byte[] getConfidentialityKey() {
    return getAvpAsOctetString(CONFIDENTIALITY_KEY, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getFramedIPAddress()
   */
  public byte[] getFramedIPAddress() {
    // 6.11.1. Framed-IP-Address AVP
    // The Framed-IP-Address AVP (AVP Code 8) [RADIUS] is of type OctetString
    return getAvpAsOctetString(FRAMED_IP_ADDRESS); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getFramedIPv6Prefix()
   */
  public byte[] getFramedIPv6Prefix() {
    // 6.11.6. Framed-IPv6-Prefix AVP
    // The Framed-IPv6-Prefix AVP (AVP Code 97) is of type OctetString
    return getAvpAsOctetString(FRAMED_IPV6_PREFIX); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getFramedInterfaceId()
   */
  public long getFramedInterfaceId() {
    // 6.11.5. Framed-Interface-Id AVP
    // The Framed-Interface-Id AVP (AVP Code 96) is of type Unsigned64
    return getAvpAsUnsigned64(FRAMED_INTERFACE); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getIntegrityKey()
   */
  public byte[] getIntegrityKey() {
    return getAvpAsOctetString(INTEGRITY_KEY, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getLineIdentifiers()
   */
  public byte[][] getLineIdentifiers() { 
    // 6.3.42  Line-Identifier AVP
    // The Line-Identifier AVP is of type OctetString. This AVP has Vendor Id ETSI (13019) and AVP code 500.
    return getAvpsAsOctetString(LINE_IDENTIFIER, ETSI_VENDOR_ID); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPAuthenticate()
   */
  public byte[] getSIPAuthenticate() {
    return getAvpAsOctetString(SIP_AUTHENTICATE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPAuthenticationContext()
   */
  public byte[] getSIPAuthenticationContext() {
    return getAvpAsOctetString(SIP_AUTHENTICATION_CONTEXT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPAuthenticationScheme()
   */
  public String getSIPAuthenticationScheme() {
    return getAvpAsUTF8String(SIP_AUTHENTICATION_SCHEME, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPAuthorization()
   */
  public byte[] getSIPAuthorization() {
    return getAvpAsOctetString(SIP_AUTHORIZATION, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPDigestAuthenticate()
   */
  public SIPDigestAuthenticate getSIPDigestAuthenticate() {
    return (SIPDigestAuthenticate) getAvpAsCustom(SIP_DIGEST_AUTHENTICATE, CXDX_VENDOR_ID, SIPDigestAuthenticateImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getSIPItemNumber()
   */
  public long getSIPItemNumber() {
    return getAvpAsUnsigned32(SIP_ITEM_NUMBER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getCableLabsSipDigestAuthenticate()
   */
  public CableLabsSIPDigestAuthenticate getCableLabsSipDigestAuthenticate() {
    return (CableLabsSIPDigestAuthenticate) getAvpAsCustom(CABLELABS_SIP_DIGEST_AUTHENTICATE, CABLELABS_VENDOR_ID, CableLabsSIPDigestAuthenticateImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getEtsiSIPAuthorization()
   */
  public EtsiSIPAuthorization getEtsiSIPAuthorization() {
    return (EtsiSIPAuthorization) getAvpAsCustom(ETSI_SIP_AUTHORIZATION, ETSI_VENDOR_ID, EtsiSIPAuthorizationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getEtsiSIPAuthenticate()
   */
  public EtsiSIPAuthenticate getEtsiSIPAuthenticate() {
    return (EtsiSIPAuthenticate) getAvpAsCustom(ETSI_SIP_AUTHENTICATE, ETSI_VENDOR_ID, EtsiSIPAuthenticateImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#getEtsiSIPAuthenticationInfo()
   */
  public EtsiSIPAuthenticationInfo getEtsiSIPAuthenticationInfo() {
    return (EtsiSIPAuthenticationInfo) getAvpAsCustom(ETSI_SIP_AUTHENTICATION_INFO, ETSI_VENDOR_ID, EtsiSIPAuthenticationInfoImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasConfidentialityKey()
   */
  public boolean hasConfidentialityKey() {
    return hasAvp(CONFIDENTIALITY_KEY, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasFramedIPAddress()
   */
  public boolean hasFramedIPAddress() {
    return hasAvp(FRAMED_IP_ADDRESS); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasFramedIPv6Prefix()
   */
  public boolean hasFramedIPv6Prefix() {
    return hasAvp(FRAMED_IPV6_PREFIX); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasFramedInterfaceId()
   */
  public boolean hasFramedInterfaceId() {
    return hasAvp(FRAMED_INTERFACE); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasIntegrityKey()
   */
  public boolean hasIntegrityKey() {
    return hasAvp(INTEGRITY_KEY, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPAuthenticate()
   */
  public boolean hasSIPAuthenticate() {
    return hasAvp(SIP_AUTHENTICATE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPAuthenticationContext()
   */
  public boolean hasSIPAuthenticationContext() {
    return hasAvp(SIP_AUTHENTICATION_CONTEXT, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPAuthenticationScheme()
   */
  public boolean hasSIPAuthenticationScheme() {
    return hasAvp(SIP_AUTHENTICATION_SCHEME, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPAuthorization()
   */
  public boolean hasSIPAuthorization() {
    return hasAvp(SIP_AUTHORIZATION, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPDigestAuthenticate()
   */
  public boolean hasSIPDigestAuthenticate() {
    return hasAvp(SIP_DIGEST_AUTHENTICATE, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasSIPItemNumber()
   */
  public boolean hasSIPItemNumber() {
    return hasAvp(SIP_ITEM_NUMBER, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasCableLabsSipDigestAuthenticate()
   */
  public boolean hasCableLabsSipDigestAuthenticate() {
    return hasAvp(CABLELABS_SIP_DIGEST_AUTHENTICATE, CABLELABS_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasEtsiSIPAuthenticate()
   */
  public boolean hasEtsiSIPAuthenticate() {
    return hasAvp(ETSI_SIP_AUTHENTICATE, ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasEtsiSIPAuthorization()
   */
  public boolean hasEtsiSIPAuthorization() {
    return hasAvp(ETSI_SIP_AUTHORIZATION, ETSI_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#hasEtsiSIPAuthenticationInfo()
   */
  public boolean hasEtsiSIPAuthenticationInfo() {
    return hasAvp(ETSI_SIP_AUTHENTICATION_INFO, ETSI_VENDOR_ID);
  }  

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setConfidentialityKey(java.lang.byte[])
   */
  public void setConfidentialityKey(byte[] confidentialityKey) {
    addAvp(CONFIDENTIALITY_KEY, CXDX_VENDOR_ID, confidentialityKey);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setFramedIPAddress(java.lang.byte[])
   */
  public void setFramedIPAddress(byte[] framedIPAddress) {
    addAvp(FRAMED_IP_ADDRESS, framedIPAddress); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setFramedIPv6Prefix(java.lang.byte[])
   */
  public void setFramedIPv6Prefix(byte[] framedIPv6Prefix) {
    addAvp(FRAMED_IPV6_PREFIX, framedIPv6Prefix); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setFramedInterfaceId(long)
   */
  public void setFramedInterfaceId(long framedInterfaceId) {
    addAvp(FRAMED_INTERFACE, framedInterfaceId); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setIntegrityKey(java.lang.byte[])
   */
  public void setIntegrityKey(byte[] integrityKey) {
    addAvp(INTEGRITY_KEY, CXDX_VENDOR_ID, integrityKey);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setLineIdentifier(java.lang.byte[])
   */
  public void setLineIdentifier(byte[] lineIdentifier) {
    addAvp(LINE_IDENTIFIER, ETSI_VENDOR_ID, lineIdentifier); 
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setLineIdentifiers(java.lang.byte[][])
   */
  public void setLineIdentifiers(byte[][] lineIdentifiers) {
    for(byte[] lineIdentifier : lineIdentifiers) {
      setLineIdentifier(lineIdentifier);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPAuthenticate(java.lang.byte[])
   */
  public void setSIPAuthenticate(byte[] sipAuthenticate) {
    addAvp(SIP_AUTHENTICATE, CXDX_VENDOR_ID, sipAuthenticate);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPAuthenticationContext(java.lang.byte[])
   */
  public void setSIPAuthenticationContext(byte[] sipAuthenticationContext) {
    addAvp(SIP_AUTHENTICATION_CONTEXT, CXDX_VENDOR_ID, sipAuthenticationContext);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPAuthenticationScheme(java.lang.String)
   */
  public void setSIPAuthenticationScheme(String sipAuthenticationScheme) {
    addAvp(SIP_AUTHENTICATION_SCHEME, CXDX_VENDOR_ID, sipAuthenticationScheme);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPAuthorization(java.lang.byte[])
   */
  public void setSIPAuthorization(byte[] sipAuthorization) {
    addAvp(SIP_AUTHORIZATION, CXDX_VENDOR_ID, sipAuthorization);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPDigestAuthenticate(net.java.slee.resource.diameter.cxdx.events.avp.SIPDigestAuthenticate)
   */
  public void setSIPDigestAuthenticate(SIPDigestAuthenticate sipDigestAuthenticate) {
    addAvp(SIP_DIGEST_AUTHENTICATE, CXDX_VENDOR_ID, sipDigestAuthenticate.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setSIPItemNumber(long)
   */
  public void setSIPItemNumber(long sipItemNumber) {
    addAvp(SIP_ITEM_NUMBER, CXDX_VENDOR_ID, sipItemNumber);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setCableLabsSipDigestAuthenticate(net.java.slee.resource.diameter.cxdx.events.avp.CableLabsSIPDigestAuthenticate)
   */
  public void setCableLabsSipDigestAuthenticate(CableLabsSIPDigestAuthenticate cableLabsSipDigestAuthenticate) {
    addAvp(CABLELABS_SIP_DIGEST_AUTHENTICATE, CABLELABS_VENDOR_ID, cableLabsSipDigestAuthenticate.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setEtsiSIPAuthentication(net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticate)
   */
  public void setEtsiSIPAuthentication(EtsiSIPAuthenticate etsiSIPAuthenticate) {
    addAvp(ETSI_SIP_AUTHENTICATE, ETSI_VENDOR_ID, etsiSIPAuthenticate.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setEtsiSIPAuthorization(net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthorization)
   */
  public void setEtsiSIPAuthorization(EtsiSIPAuthorization etsiSipAuthorization) {
    addAvp(ETSI_SIP_AUTHORIZATION, ETSI_VENDOR_ID, etsiSipAuthorization.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem#setEtsiSIPAuthenticationInfo(net.java.slee.resource.diameter.cxdx.events.avp.EtsiSIPAuthenticationInfo)
   */
  public void setEtsiSIPAuthenticationInfo(EtsiSIPAuthenticationInfo etsiSIPAuthenticationInfo) {
    addAvp(ETSI_SIP_AUTHENTICATION_INFO, ETSI_VENDOR_ID, etsiSIPAuthenticationInfo.byteArrayValue());
  }

}
