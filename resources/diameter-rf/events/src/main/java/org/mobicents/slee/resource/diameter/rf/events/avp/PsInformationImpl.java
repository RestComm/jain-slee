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

package org.mobicents.slee.resource.diameter.rf.events.avp;

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.rf.events.avp.PsFurnishChargingInformation;
import net.java.slee.resource.diameter.rf.events.avp.PsInformation;

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
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getCgAddress()
   */
  public Address getCgAddress() {
    return getAvpAsAddress(DiameterRfAvpCodes.CG_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getChargingRuleBaseName()
   */
  public String getChargingRuleBaseName() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getGgsnAddress()
   */
  public Address getGgsnAddress() {
    return getAvpAsAddress(DiameterRfAvpCodes.GGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getPdpAddress()
   */
  public Address getPdpAddress() {
    return getAvpAsAddress(DiameterRfAvpCodes.PDP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getPsFurnishChargingInformation()
   */
  public PsFurnishChargingInformation getPsFurnishChargingInformation() {
    return (PsFurnishChargingInformation) getAvpAsCustom(DiameterRfAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, PsFurnishChargingInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getSgsnAddress()
   */
  public Address getSgsnAddress() {
    return getAvpAsAddress(DiameterRfAvpCodes.SGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppCamelChargingInfo()
   */
  public byte[] getTgppCamelChargingInfo() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppChargingCharacteristics()
   */
  public String getTgppChargingCharacteristics() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppChargingId()
   */
  public byte[] getTgppChargingId() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppGgsnMccMnc()
   */
  public String getTgppGgsnMccMnc() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppGprsNegotiatedQosProfile()
   */
  public String getTgppGprsNegotiatedQosProfile() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppImsiMccMnc()
   */
  public String getTgppImsiMccMnc() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppMsTimezone()
   */
  public byte[] getTgppMsTimezone() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_MS_TIMEZONE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppNsapi()
   */
  public byte[] getTgppNsapi() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_NSAPI, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppPdpType()
   */
  public byte[] getTgppPdpType() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_PDP_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppRatType()
   */
  public byte[] getTgppRatType() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_RAT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppSelectionMode()
   */
  public String getTgppSelectionMode() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_SELECTION_MODE, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppSessionStopIndicator()
   */
  public byte[] getTgppSessionStopIndicator() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppSgsnMccMnc()
   */
  public String getTgppSgsnMccMnc() {
    return getAvpAsUTF8String(DiameterRfAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#getTgppUserLocationInfo()
   */
  public byte[] getTgppUserLocationInfo() {
    return getAvpAsOctetString(DiameterRfAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasCgAddress()
   */
  public boolean hasCgAddress() {
    return hasAvp( DiameterRfAvpCodes.CG_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasChargingRuleBaseName()
   */
  public boolean hasChargingRuleBaseName() {
    return hasAvp( DiameterRfAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasGgsnAddress()
   */
  public boolean hasGgsnAddress() {
    return hasAvp( DiameterRfAvpCodes.GGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasPdpAddress()
   */
  public boolean hasPdpAddress() {
    return hasAvp( DiameterRfAvpCodes.PDP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasPsFurnishChargingInformation()
   */
  public boolean hasPsFurnishChargingInformation() {
    return hasAvp( DiameterRfAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasSgsnAddress()
   */
  public boolean hasSgsnAddress() {
    return hasAvp( DiameterRfAvpCodes.SGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppCamelChargingInfo()
   */
  public boolean hasTgppCamelChargingInfo() {
    return hasAvp( DiameterRfAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppChargingCharacteristics()
   */
  public boolean hasTgppChargingCharacteristics() {
    return hasAvp( DiameterRfAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppChargingId()
   */
  public boolean hasTgppChargingId() {
    return hasAvp( DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppGgsnMccMnc()
   */
  public boolean hasTgppGgsnMccMnc() {
    return hasAvp( DiameterRfAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppGprsNegotiatedQosProfile()
   */
  public boolean hasTgppGprsNegotiatedQosProfile() {
    return hasAvp( DiameterRfAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppImsiMccMnc()
   */
  public boolean hasTgppImsiMccMnc() {
    return hasAvp( DiameterRfAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppMsTimezone()
   */
  public boolean hasTgppMsTimezone() {
    return hasAvp( DiameterRfAvpCodes.TGPP_MS_TIMEZONE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppNsapi()
   */
  public boolean hasTgppNsapi() {
    return hasAvp( DiameterRfAvpCodes.TGPP_NSAPI, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppPdpType()
   */
  public boolean hasTgppPdpType() {
    return hasAvp( DiameterRfAvpCodes.TGPP_PDP_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppRatType()
   */
  public boolean hasTgppRatType() {
    return hasAvp( DiameterRfAvpCodes.TGPP_RAT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppSelectionMode()
   */
  public boolean hasTgppSelectionMode() {
    return hasAvp( DiameterRfAvpCodes.TGPP_SELECTION_MODE, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppSessionStopIndicator()
   */
  public boolean hasTgppSessionStopIndicator() {
    return hasAvp( DiameterRfAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppSgsnMccMnc()
   */
  public boolean hasTgppSgsnMccMnc() {
    return hasAvp( DiameterRfAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#hasTgppUserLocationInfo()
   */
  public boolean hasTgppUserLocationInfo() {
    return hasAvp( DiameterRfAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID );
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setCgAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setCgAddress( Address cgAddress ) {
    addAvp(DiameterRfAvpCodes.CG_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, cgAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setChargingRuleBaseName(String)
   */
  public void setChargingRuleBaseName( String chargingRuleBaseName ) {
    addAvp(DiameterRfAvpCodes.CHARGING_RULE_BASE_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, chargingRuleBaseName);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setGgsnAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setGgsnAddress( Address ggsnAddress ) {
    addAvp(DiameterRfAvpCodes.GGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, ggsnAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setPdpAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setPdpAddress( Address pdpAddress ) {
    addAvp(DiameterRfAvpCodes.PDP_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, pdpAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setPsFurnishChargingInformation(net.java.slee.resource.diameter.rf.events.avp.PsFurnishChargingInformation)
   */
  public void setPsFurnishChargingInformation( PsFurnishChargingInformation psFurnishChargingInformation ) {
    addAvp(DiameterRfAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, psFurnishChargingInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setSgsnAddress(net.java.slee.resource.diameter.base.events.avp.Address)
   */
  public void setSgsnAddress( Address sgsnAddress ) {
    addAvp(DiameterRfAvpCodes.SGSN_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, sgsnAddress.encode());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppCamelChargingInfo(String)
   */
  public void setTgppCamelChargingInfo( byte[] tgppCamelChargingInfo ) {
    addAvp(DiameterRfAvpCodes.TGPP_CAMEL_CHARGING_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppCamelChargingInfo);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppChargingCharacteristics(String)
   */
  public void setTgppChargingCharacteristics( String tgppChargingCharacteristics ) {
    addAvp(DiameterRfAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppChargingCharacteristics);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppChargingId(String)
   */
  public void setTgppChargingId( byte[] tgppChargingId ) {
    addAvp(DiameterRfAvpCodes.TGPP_CHARGING_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppChargingId);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppGgsnMccMnc(String)
   */
  public void setTgppGgsnMccMnc( String tgppGgsnMccMnc ) {
    addAvp(DiameterRfAvpCodes.TGPP_GGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppGgsnMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppGprsNegotiatedQosProfile(String)
   */
  public void setTgppGprsNegotiatedQosProfile( String tgppGprsNegotiatedQosProfile ) {
    addAvp(DiameterRfAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppGprsNegotiatedQosProfile);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppImsiMccMnc(String)
   */
  public void setTgppImsiMccMnc( String tgppImsiMccMnc ) {
    addAvp(DiameterRfAvpCodes.TGPP_IMSI_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppImsiMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppMsTimezone(String)
   */
  public void setTgppMsTimezone( byte[] tgppMsTimezone ) {
    addAvp(DiameterRfAvpCodes.TGPP_MS_TIMEZONE, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppMsTimezone);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppNsapi(String)
   */
  public void setTgppNsapi( byte[] tgppNsapi ) {
    addAvp(DiameterRfAvpCodes.TGPP_NSAPI, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppNsapi);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppPdpType(String)
   */
  public void setTgppPdpType( byte[] tgppPdpType ) {
    addAvp(DiameterRfAvpCodes.TGPP_PDP_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppPdpType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppRatType(String)
   */
  public void setTgppRatType( byte[] tgppPdpType ) {
    addAvp(DiameterRfAvpCodes.TGPP_RAT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppPdpType);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppSelectionMode(String)
   */
  public void setTgppSelectionMode( String tgppSelectionMode ) {
    addAvp(DiameterRfAvpCodes.TGPP_SELECTION_MODE, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppSelectionMode);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppSessionStopIndicator(String)
   */
  public void setTgppSessionStopIndicator( byte[] tgppSessionStopIndicator ) {
    addAvp(DiameterRfAvpCodes.TGPP_SESSION_STOP_INDICATOR, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppSessionStopIndicator);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppSgsnMccMnc(String)
   */
  public void setTgppSgsnMccMnc( String tgppSgsnMccMnc ) {
    addAvp(DiameterRfAvpCodes.TGPP_SGSN_MCC_MNC, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppSgsnMccMnc);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.avp.PsInformation#setTgppUserLocationInfo(String)
   */
  public void setTgppUserLocationInfo( byte[] tgppUserLocationInfo ) {
    addAvp(DiameterRfAvpCodes.TGPP_USER_LOCATION_INFO, DiameterRfAvpCodes.TGPP_VENDOR_ID, tgppUserLocationInfo);
  }

}
