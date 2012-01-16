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

package org.mobicents.slee.resource.diameter.s6a.events;

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.*;
import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.s6a.events.UpdateLocationRequest;
import net.java.slee.resource.diameter.s6a.events.avp.ActiveAPNAvp;
import net.java.slee.resource.diameter.s6a.events.avp.HomogeneousSupportOfIMSVoiceOverPSSessions;
import net.java.slee.resource.diameter.s6a.events.avp.RATType;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.ActiveAPNAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.TerminalInformationAvpImpl;

/**
 * Implementation for {@link UpdateLocationRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class UpdateLocationRequestImpl extends DiameterMessageImpl implements UpdateLocationRequest {

  /**
   * @param message
   */
  public UpdateLocationRequestImpl(Message message) {
    super(message);
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  @Override
  public String getLongName() {
    return "Update-Location-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  @Override
  public String getShortName() {
    return "ULR";
  }

  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  public boolean hasVisitedPLMNId() {
    return hasAvp(VISITED_PLMN_ID, S6A_VENDOR_ID);
  }

  public byte[] getVisitedPLMNId() {
    return getAvpAsOctetString(VISITED_PLMN_ID, S6A_VENDOR_ID);
  }

  public void setVisitedPLMNId(byte[] visitedPLMNId) {
    addAvp(VISITED_PLMN_ID, S6A_VENDOR_ID, visitedPLMNId);
  }

  public boolean hasSGSNNumber() {
    return hasAvp(SGSN_NUMBER, S6A_VENDOR_ID);
  }

  public byte[] getSGSNNumber() {
    return getAvpAsOctetString(SGSN_NUMBER, S6A_VENDOR_ID);
  }

  public void setSGSNNumber(byte[] sgsnNumber) {
    addAvp(SGSN_NUMBER, S6A_VENDOR_ID, sgsnNumber);
  }

  public boolean hasTerminalInformation() {
    return hasAvp(TERMINAL_INFORMATION, S6A_VENDOR_ID);
  }

  public TerminalInformationAvp getTerminalInformation() {
    return (TerminalInformationAvp) getAvpAsCustom(TERMINAL_INFORMATION, S6A_VENDOR_ID, TerminalInformationAvpImpl.class);
  }

  public void setTerminalInformation(TerminalInformationAvp terminalInformation) {
    addAvp(TERMINAL_INFORMATION, S6A_VENDOR_ID, terminalInformation.byteArrayValue());
  }

  public boolean hasRATType() {
    return hasAvp(RAT_TYPE, S6A_VENDOR_ID);
  }

  public RATType getRATType() {
    return (RATType) getAvpAsEnumerated(RAT_TYPE, S6A_VENDOR_ID, RATType.class);
  }

  public void setRATType(RATType ratType) {
    addAvp(RAT_TYPE, S6A_VENDOR_ID, ratType.getValue());
  }

  public boolean hasULRFlags() {
    return hasAvp(ULR_FLAGS, S6A_VENDOR_ID);
  }

  public long getULRFlags() {
    return getAvpAsUnsigned32(ULR_FLAGS, S6A_VENDOR_ID);
  }

  public void setULRFlags(long ulrFlags) {
    addAvp(ULR_FLAGS, S6A_VENDOR_ID, ulrFlags);
  }

  public boolean hasHomogeneousSupportOfIMSVoiceOverPSSessions() {
    return hasAvp(HOMOGENEOUS_SUPPORT_OF_IMS_VOICE_OVER_PS_SESSIONS, S6A_VENDOR_ID);
  }

  public HomogeneousSupportOfIMSVoiceOverPSSessions getHomogeneousSupportOfIMSVoiceOverPSSessions() {
    return (HomogeneousSupportOfIMSVoiceOverPSSessions) getAvpAsEnumerated(HOMOGENEOUS_SUPPORT_OF_IMS_VOICE_OVER_PS_SESSIONS, S6A_VENDOR_ID, HomogeneousSupportOfIMSVoiceOverPSSessions.class);
  }

  public void setHomogeneousSupportOfIMSVoiceOverPSSessions(HomogeneousSupportOfIMSVoiceOverPSSessions homogeneousSupportOfIMSVoiceOverPSSessions) {
    addAvp(HOMOGENEOUS_SUPPORT_OF_IMS_VOICE_OVER_PS_SESSIONS, S6A_VENDOR_ID, homogeneousSupportOfIMSVoiceOverPSSessions.getValue());
  }

  public boolean hasGMLCAddress() {
    return hasAvp(GMLC_ADDRESS, S6A_VENDOR_ID);
  }

  public Address getGMLCAddress() {
    return getAvpAsAddress(GMLC_ADDRESS, S6A_VENDOR_ID);
  }

  public void setGMLCAddress(Address gmlcAddress) {
    addAvp(GMLC_ADDRESS, S6A_VENDOR_ID, gmlcAddress);
  }

  public boolean hasActiveAPN() {
    return hasAvp(ACTIVE_APN, S6A_VENDOR_ID);
  }

  public ActiveAPNAvp getActiveAPN() {
    return (ActiveAPNAvp) getAvpAsCustom(ACTIVE_APN, S6A_VENDOR_ID, ActiveAPNAvpImpl.class);
  }

  public void setActiveAPN(ActiveAPNAvp activeAPN) {
    addAvp(ACTIVE_APN, S6A_VENDOR_ID, activeAPN.byteArrayValue());
  }

}
