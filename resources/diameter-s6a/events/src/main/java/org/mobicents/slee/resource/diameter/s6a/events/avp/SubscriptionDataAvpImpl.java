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

package org.mobicents.slee.resource.diameter.s6a.events.avp;

import net.java.slee.resource.diameter.s6a.events.avp.AMBRAvp;
import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.ICSIndicator;
import net.java.slee.resource.diameter.s6a.events.avp.NetworkAccessMode;
import net.java.slee.resource.diameter.s6a.events.avp.RoamingRestrictedDueToUnsupportedFeature;
import net.java.slee.resource.diameter.s6a.events.avp.SubscriberStatus;
import net.java.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvp;
import org.mobicents.slee.resource.diameter.base.events.avp.GroupedAvpImpl;

/**
 * Implementation for {@link SubscriptionDataAvp}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class SubscriptionDataAvpImpl extends GroupedAvpImpl implements SubscriptionDataAvp {

  public SubscriptionDataAvpImpl() {
    super();
  }

  public SubscriptionDataAvpImpl(int code, long vendorId, int mnd, int prt, byte[] value) {
    super(code, vendorId, mnd, prt, value);
  }

  public boolean hasSubscriberStatus() {
    return hasAvp(DiameterS6aAvpCodes.SUBSCRIBER_STATUS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setSubscriberStatus(SubscriberStatus ss) {
    addAvp(DiameterS6aAvpCodes.SUBSCRIBER_STATUS, DiameterS6aAvpCodes.S6A_VENDOR_ID, ss.getValue());
  }

  public SubscriberStatus getSubscriberStatus() {
    return (SubscriberStatus) getAvpAsEnumerated(DiameterS6aAvpCodes.SUBSCRIBER_STATUS, DiameterS6aAvpCodes.S6A_VENDOR_ID, SubscriberStatus.class);
  }

  public boolean hasMSISDN() {
    return hasAvp(DiameterS6aAvpCodes.MSISDN, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public byte[] getMSISDN() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.MSISDN, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setMSISDN(byte[] msisdn) {
    addAvp(DiameterS6aAvpCodes.MSISDN, DiameterS6aAvpCodes.S6A_VENDOR_ID, msisdn);
  }

  public boolean hasSTNSR() {
    return hasAvp(DiameterS6aAvpCodes.STN_SR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public byte[] getSTNSR() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.STN_SR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setSTNSR(byte[] stnsr) {
    addAvp(DiameterS6aAvpCodes.STN_SR, DiameterS6aAvpCodes.S6A_VENDOR_ID, stnsr);
  }

  public boolean hasICSIndicator() {
    return hasAvp(DiameterS6aAvpCodes.ICS_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public ICSIndicator getICSIndicator() {
    return (ICSIndicator) getAvpAsEnumerated(DiameterS6aAvpCodes.ICS_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID, ICSIndicator.class);
  }

  public void setICSIndicator(ICSIndicator icsIndicator) {
    addAvp(DiameterS6aAvpCodes.ICS_INDICATOR, DiameterS6aAvpCodes.S6A_VENDOR_ID, icsIndicator.getValue());
  }

  public boolean hasNetworkAccessMode() {
    return hasAvp(DiameterS6aAvpCodes.NETWORK_ACCESS_MODE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setNetworkAccessMode(NetworkAccessMode nam) {
    addAvp(DiameterS6aAvpCodes.NETWORK_ACCESS_MODE, DiameterS6aAvpCodes.S6A_VENDOR_ID, nam.getValue());
  }

  public NetworkAccessMode getNetworkAccessMode() {
    return (NetworkAccessMode) getAvpAsEnumerated(DiameterS6aAvpCodes.NETWORK_ACCESS_MODE, DiameterS6aAvpCodes.S6A_VENDOR_ID, NetworkAccessMode.class);
  }

  public boolean has3GPPChargingCharacteristics() {
    return hasAvp(DiameterS6aAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public String get3GPPChargingCharacteristics() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void set3GPPChargingCharacteristics(String threeGPPChargingCharacteristics) {
    addAvp(DiameterS6aAvpCodes.TGPP_CHARGING_CHARACTERISTICS, DiameterS6aAvpCodes.S6A_VENDOR_ID, threeGPPChargingCharacteristics);
  }

  public boolean hasAMBR() {
    return hasAvp(DiameterS6aAvpCodes.AMBR, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public AMBRAvp getAMBR() {
    return (AMBRAvp) getAvpAsCustom(DiameterS6aAvpCodes.AMBR, DiameterS6aAvpCodes.S6A_VENDOR_ID, AMBRAvpImpl.class);
  }

  public void setAMBR(AMBRAvp ambr) {
    addAvp(DiameterS6aAvpCodes.AMBR, DiameterS6aAvpCodes.S6A_VENDOR_ID, ambr.byteArrayValue());
  }

  public boolean hasAPNConfigurationProfile() {
    return hasAvp(DiameterS6aAvpCodes.APN_CONFIGURATION_PROFILE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public APNConfigurationProfileAvp getAPNConfigurationProfile() {
    return (APNConfigurationProfileAvp) getAvpAsCustom(DiameterS6aAvpCodes.APN_CONFIGURATION_PROFILE, DiameterS6aAvpCodes.S6A_VENDOR_ID, APNConfigurationProfileAvpImpl.class);
  }

  public void setAPNConfigurationProfile(APNConfigurationProfileAvp apnconfig) {
    addAvp(DiameterS6aAvpCodes.APN_CONFIGURATION_PROFILE, DiameterS6aAvpCodes.S6A_VENDOR_ID, apnconfig.byteArrayValue());
  }

  public boolean hasOperatorDeterminedBarring() {
    return hasAvp(DiameterS6aAvpCodes.OPERATOR_DETERMINED_BARRING, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getOperatorDeterminedBarring() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.OPERATOR_DETERMINED_BARRING, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setOperatorDeterminedBarring(long operatorDeterminedBarring) {
    addAvp(DiameterS6aAvpCodes.OPERATOR_DETERMINED_BARRING, DiameterS6aAvpCodes.S6A_VENDOR_ID, operatorDeterminedBarring);
  }

  public boolean hasHPLMNODB() {
    return hasAvp(DiameterS6aAvpCodes.HPLMN_ODB, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getHPLMNODB() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.HPLMN_ODB, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setHPLMNODB(long hplmnOdb) {
    addAvp(DiameterS6aAvpCodes.HPLMN_ODB, DiameterS6aAvpCodes.S6A_VENDOR_ID, hplmnOdb);
  }

  public byte[][] getRegionalSubscriptionZoneCodes() {
    return getAvpsAsOctetString(DiameterS6aAvpCodes.REGIONAL_SUBSCRIPTION_ZONE_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setRegionalSubscriptionZoneCode(byte[] rszc) {
    addAvp(DiameterS6aAvpCodes.REGIONAL_SUBSCRIPTION_ZONE_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID, rszc);
  }

  public void setRegionalSubscriptionZoneCodes(byte[][] rszcs) {
    for(byte[] rszc : rszcs) {
      setRegionalSubscriptionZoneCode(rszc);
    }
  }

  public boolean hasAccessRestrictionData() {
    return hasAvp(DiameterS6aAvpCodes.ACCESS_RESTRICTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getAccessRestrictionData() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.ACCESS_RESTRICTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setAccessRestrictionData(long ard) {
    addAvp(DiameterS6aAvpCodes.ACCESS_RESTRICTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID, ard);
  }

  public boolean hasAPNOIReplacement() {
    return hasAvp(DiameterS6aAvpCodes.APN_OI_REPLACEMENT, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public String getAPNOIReplacement() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.APN_OI_REPLACEMENT, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setAPNOIReplacement(String apnOiReplacement) {
    addAvp(DiameterS6aAvpCodes.APN_OI_REPLACEMENT, DiameterS6aAvpCodes.S6A_VENDOR_ID, apnOiReplacement);
  }

  public boolean hasRATFrequencySelectionPriorityID() {
    return hasAvp(DiameterS6aAvpCodes.RAT_FREQUENCY_SELECTION_PRIORITY_ID, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public long getRATFrequencySelectionPriorityID() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.RAT_FREQUENCY_SELECTION_PRIORITY_ID, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setRATFrequencySelectionPriorityID(long rfspid) {
    addAvp(DiameterS6aAvpCodes.RAT_FREQUENCY_SELECTION_PRIORITY_ID, DiameterS6aAvpCodes.S6A_VENDOR_ID, rfspid);
  }

  public boolean hasRoamingRestrictedDueToUnsupportedFeature() {
    return hasAvp(DiameterS6aAvpCodes.ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  public void setRoamingRestrictedDueToUnsupportedFeature(RoamingRestrictedDueToUnsupportedFeature rrdtuf) {
    addAvp(DiameterS6aAvpCodes.ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE, DiameterS6aAvpCodes.S6A_VENDOR_ID, rrdtuf.getValue());
  }

  public RoamingRestrictedDueToUnsupportedFeature getRoamingRestrictedDueToUnsupportedFeature() {
    return (RoamingRestrictedDueToUnsupportedFeature) getAvpAsEnumerated(DiameterS6aAvpCodes.ROAMING_RESTRICTED_DUE_TO_UNSUPPORTED_FEATURE, DiameterS6aAvpCodes.S6A_VENDOR_ID, RoamingRestrictedDueToUnsupportedFeature.class);
  }

}
