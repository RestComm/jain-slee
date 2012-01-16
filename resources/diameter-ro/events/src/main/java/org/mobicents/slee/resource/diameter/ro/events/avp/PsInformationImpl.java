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

package org.mobicents.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation;
import net.java.slee.resource.diameter.ro.events.avp.PsInformation;

import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * PsInformationImpl.java
 *
 * <br>Project:  mobicents
 * <br>1:18:52 PM Apr 13, 2009 
 * <br>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class PsInformationImpl extends GroupedAvpImpl implements PsInformation {

  public PsInformationImpl() {
    super();
  }

  /**
   * @param code
   * @param vendorId
   * @param mnd
   * @param prt
   * @param value
   */
  public PsInformationImpl( int code, long vendorId, int mnd, int prt, byte[] value ) {
    super( code, vendorId, mnd, prt, value );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getCgAddress()
   */
  public Address getCgAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.CG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getChargingRuleBaseName()
   */
  public String getChargingRuleBaseName() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getGgsnAddress()
   */
  public Address getGgsnAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.GGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getPdpAddress()
   */
  public Address getPdpAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.PDP_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getPsFurnishChargingInformation()
   */
  public PsFurnishChargingInformation getPsFurnishChargingInformation() {
    return (PsFurnishChargingInformation) getAvpAsCustom(DiameterRoAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID, PsFurnishChargingInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getSgsnAddress()
   */
  public Address getSgsnAddress() {
    return getAvpAsAddress(DiameterRoAvpCodes.SGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppCamelChargingInfo()
   */
  public byte[] getTgppCamelChargingInfo() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppChargingCharacteristics()
   */
  public String getTgppChargingCharacteristics() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppChargingId()
   */
  public byte[] getTgppChargingId() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppGgsnMccMnc()
   */
  public String getTgppGgsnMccMnc() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppGprsNegotiatedQosProfile()
   */
  public String getTgppGprsNegotiatedQosProfile() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppImsiMccMnc()
   */
  public String getTgppImsiMccMnc() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppMsTimezone()
   */
  public byte[] getTgppMsTimezone() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_MS_TIMEZONE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppNsapi()
   */
  public byte[] getTgppNsapi() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_NSAPI, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppPdpType()
   */
  public byte[] getTgppPdpType() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_PDP_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppRatType()
   */
  public byte[] getTgppRatType() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_RAT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppSelectionMode()
   */
  public String getTgppSelectionMode() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_SELECTION_MODE, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppSessionStopIndicator()
   */
  public byte[] getTgppSessionStopIndicator() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppSgsnMccMnc()
   */
  public String getTgppSgsnMccMnc() {
    return getAvpAsUTF8String(DiameterRoAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#getTgppUserLocationInfo()
   */
  public byte[] getTgppUserLocationInfo() {
    return getAvpAsOctetString(DiameterRoAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasCgAddress()
   */
  public boolean hasCgAddress() {
    return hasAvp( DiameterRoAvpCodes.CG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasChargingRuleBaseName()
   */
  public boolean hasChargingRuleBaseName() {
    return hasAvp( DiameterRoAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasGgsnAddress()
   */
  public boolean hasGgsnAddress() {
    return hasAvp( DiameterRoAvpCodes.GGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasPdpAddress()
   */
  public boolean hasPdpAddress() {
    return hasAvp( DiameterRoAvpCodes.PDP_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasPsFurnishChargingInformation()
   */
  public boolean hasPsFurnishChargingInformation() {
    return hasAvp( DiameterRoAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasSgsnAddress()
   */
  public boolean hasSgsnAddress() {
    return hasAvp( DiameterRoAvpCodes.SGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppCamelChargingInfo()
   */
  public boolean hasTgppCamelChargingInfo() {
    return hasAvp( DiameterRoAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppChargingCharacteristics()
   */
  public boolean hasTgppChargingCharacteristics() {
    return hasAvp( DiameterRoAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp( DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppGgsnMccMnc()
   */
  public boolean hasTgppGgsnMccMnc() {
    return hasAvp( DiameterRoAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppGprsNegotiatedQosProfile()
   */
  public boolean hasTgppGprsNegotiatedQosProfile() {
    return hasAvp( DiameterRoAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppImsiMccMnc()
   */
  public boolean hasTgppImsiMccMnc() {
    return hasAvp( DiameterRoAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppMsTimezone()
   */
  public boolean hasTgppMsTimezone() {
    return hasAvp( DiameterRoAvpCodes.TGPP_MS_TIMEZONE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppNsapi()
   */
  public boolean hasTgppNsapi() {
    return hasAvp( DiameterRoAvpCodes.TGPP_NSAPI, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppPdpType()
   */
  public boolean hasTgppPdpType() {
    return hasAvp( DiameterRoAvpCodes.TGPP_PDP_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppRatType()
   */
  public boolean hasTgppRatType() {
    return hasAvp( DiameterRoAvpCodes.TGPP_RAT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppSelectionMode()
   */
  public boolean hasTgppSelectionMode() {
    return hasAvp( DiameterRoAvpCodes.TGPP_SELECTION_MODE, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppSessionStopIndicator()
   */
  public boolean hasTgppSessionStopIndicator() {
    return hasAvp( DiameterRoAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppSgsnMccMnc()
   */
  public boolean hasTgppSgsnMccMnc() {
    return hasAvp( DiameterRoAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#hasTgppUserLocationInfo()
   */
  public boolean hasTgppUserLocationInfo() {
    return hasAvp( DiameterRoAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setCgAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setCgAddress( Address cgAddress ) {
    addAvp(DiameterRoAvpCodes.CG_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, cgAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setChargingRuleBaseName(String)
   */
  public void setChargingRuleBaseName( String chargingRuleBaseName ) {
    addAvp(DiameterRoAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRoAvpCodes.TGPP_VENDOR_ID, chargingRuleBaseName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setGgsnAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setGgsnAddress( Address ggsnAddress ) {
    addAvp(DiameterRoAvpCodes.GGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, ggsnAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setPdpAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setPdpAddress( Address pdpAddress ) {
    addAvp(DiameterRoAvpCodes.PDP_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, pdpAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setPsFurnishChargingInformation(net.java.slee.resource.diameter.ro.events.avp.PsFurnishChargingInformation)
   */
  public void setPsFurnishChargingInformation( PsFurnishChargingInformation psFurnishChargingInformation ) {
    addAvp(DiameterRoAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRoAvpCodes.TGPP_VENDOR_ID, psFurnishChargingInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setSgsnAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setSgsnAddress( Address sgsnAddress ) {
    addAvp(DiameterRoAvpCodes.SGSN_ADDRESS, DiameterRoAvpCodes.TGPP_VENDOR_ID, sgsnAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppCamelChargingInfo(String)
   */
  public void setTgppCamelChargingInfo( byte[] tgppCamelChargingInfo ) {
    addAvp(DiameterRoAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppCamelChargingInfo);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppChargingCharacteristics(String)
   */
  public void setTgppChargingCharacteristics( String tgppChargingCharacteristics ) {
    addAvp(DiameterRoAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppChargingCharacteristics);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppChargingId(String)
   */
  public void setTgppChargingId( byte[] tgppChargingId ) {
    addAvp(DiameterRoAvpCodes.TGPP_CHARGING_ID, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppGgsnMccMnc(String)
   */
  public void setTgppGgsnMccMnc( String tgppGgsnMccMnc ) {
    addAvp(DiameterRoAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppGgsnMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppGprsNegotiatedQosProfile(String)
   */
  public void setTgppGprsNegotiatedQosProfile( String tgppGprsNegotiatedQosProfile ) {
    addAvp(DiameterRoAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppGprsNegotiatedQosProfile);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppImsiMccMnc(String)
   */
  public void setTgppImsiMccMnc( String tgppImsiMccMnc ) {
    addAvp(DiameterRoAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppImsiMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppMsTimezone(String)
   */
  public void setTgppMsTimezone( byte[] tgppMsTimezone ) {
    addAvp(DiameterRoAvpCodes.TGPP_MS_TIMEZONE, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppMsTimezone);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppNsapi(String)
   */
  public void setTgppNsapi( byte[] tgppNsapi ) {
    addAvp(DiameterRoAvpCodes.TGPP_NSAPI, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppNsapi);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppPdpType(String)
   */
  public void setTgppPdpType( byte[] tgppPdpType ) {
    addAvp(DiameterRoAvpCodes.TGPP_PDP_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppPdpType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppRatType(String)
   */
  public void setTgppRatType( byte[] tgppPdpType ) {
    addAvp(DiameterRoAvpCodes.TGPP_RAT_TYPE, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppPdpType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppSelectionMode(String)
   */
  public void setTgppSelectionMode( String tgppSelectionMode ) {
    addAvp(DiameterRoAvpCodes.TGPP_SELECTION_MODE, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppSelectionMode);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppSessionStopIndicator(String)
   */
  public void setTgppSessionStopIndicator( byte[] tgppSessionStopIndicator ) {
    addAvp(DiameterRoAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppSessionStopIndicator);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppSgsnMccMnc(String)
   */
  public void setTgppSgsnMccMnc( String tgppSgsnMccMnc ) {
    addAvp(DiameterRoAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppSgsnMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.ro.events.avp.PsInformation#setTgppUserLocationInfo(String)
   */
  public void setTgppUserLocationInfo( byte[] tgppUserLocationInfo ) {
    addAvp(DiameterRoAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRoAvpCodes.TGPP_VENDOR_ID, tgppUserLocationInfo);
  }

}
