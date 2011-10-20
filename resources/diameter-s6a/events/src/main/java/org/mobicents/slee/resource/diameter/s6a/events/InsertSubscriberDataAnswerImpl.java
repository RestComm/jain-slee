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

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.EPS_LOCATION_INFORMATION;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.EPS_USER_STATE;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.LAST_UE_ACTIVITY_TIME;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.RAT_TYPE;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.S6A_VENDOR_ID;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.SUPPORTED_FEATURES;

import java.util.Date;

import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.IMSVoiceOverPSSessionsSupported;
import net.java.slee.resource.diameter.s6a.events.avp.RATType;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EPSUserStateAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link InsertSubscriberDataAnswer}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class InsertSubscriberDataAnswerImpl extends DiameterMessageImpl implements InsertSubscriberDataAnswer {

  /**
   * @param message
   */
  public InsertSubscriberDataAnswerImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Insert-Subscriber-Data-Answer";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "IDA";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasIDAFlags()
   */
  public boolean hasIDAFlags() {
    return hasAvp(DiameterS6aAvpCodes.IDA_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getIDAFlags()
   */
  public long getIDAFlags() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.IDA_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setIDAFlags(long)
   */
  public void setIDAFlags(long idaFlags) {
    addAvp(DiameterS6aAvpCodes.IDA_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID, idaFlags);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasExperimentalResult()
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getExperimentalResult()
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setExperimentalResult(net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp)
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasIMSVoiceOverPSSessionsSupported()
   */
  public boolean hasIMSVoiceOverPSSessionsSupported() {
    return hasAvp(IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getIMSVoiceOverPSSessionsSupported()
   */
  public IMSVoiceOverPSSessionsSupported getIMSVoiceOverPSSessionsSupported() {
    return (IMSVoiceOverPSSessionsSupported) getAvpAsEnumerated(IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED, S6A_VENDOR_ID, IMSVoiceOverPSSessionsSupported.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setIMSVoiceOverPSSessionsSupported(net.java.slee.resource.diameter.s6a.events.avp.IMSVoiceOverPSSessionsSupported)
   */
  public void setIMSVoiceOverPSSessionsSupported(IMSVoiceOverPSSessionsSupported imsVoiceOverPSSessionsSupported) {
    addAvp(IMS_VOICE_OVER_PS_SESSIONS_SUPPORTED, S6A_VENDOR_ID, imsVoiceOverPSSessionsSupported.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasLastUEActivityTime()
   */
  public boolean hasLastUEActivityTime() {
    return hasAvp(DiameterS6aAvpCodes.LAST_UE_ACTIVITY_TIME, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getLastUEActivityTime()
   */
  public Date getLastUEActivityTime() {
    return getAvpAsTime(LAST_UE_ACTIVITY_TIME, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setLastUEActivityTime(java.util.Date)
   */
  public void setLastUEActivityTime(Date lastUEActivityTime) {
    addAvp(LAST_UE_ACTIVITY_TIME, S6A_VENDOR_ID, lastUEActivityTime);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasRATType()
   */
  public boolean hasRATType() {
    return hasAvp(DiameterS6aAvpCodes.RAT_TYPE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getRATType()
   */
  public RATType getRATType() {
    return (RATType) getAvpAsEnumerated(RAT_TYPE, S6A_VENDOR_ID, RATType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setRATType(net.java.slee.resource.diameter.s6a.events.avp.RATType)
   */
  public void setRATType(RATType ratType) {
    addAvp(RAT_TYPE, S6A_VENDOR_ID, ratType.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasEPSUserState()
   */
  public boolean hasEPSUserState() {
    return hasAvp(DiameterS6aAvpCodes.EPS_USER_STATE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getEPSUserState()
   */
  public EPSUserStateAvp getEPSUserState() {
    return (EPSUserStateAvp) getAvpAsCustom(EPS_USER_STATE, S6A_VENDOR_ID, EPSUserStateAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setEPSUserState(net.java.slee.resource.diameter.s6a.events.avp.EPSUserStateAvp)
   */
  public void setEPSUserState(EPSUserStateAvp epsUserState) {
    addAvp(EPS_USER_STATE, S6A_VENDOR_ID, epsUserState.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#hasEPSLocationInformation()
   */
  public boolean hasEPSLocationInformation() {
    return hasAvp(DiameterS6aAvpCodes.EPS_LOCATION_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#getEPSLocationInformation()
   */
  public EPSLocationInformationAvp getEPSLocationInformation() {
    return (EPSLocationInformationAvp) getAvpAsCustom(EPS_LOCATION_INFORMATION, S6A_VENDOR_ID, EPSLocationInformationAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataAnswer#setEPSLocationInformation(net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp)
   */
  public void setEPSLocationInformation(EPSLocationInformationAvp epsLocationInformation) {
    addAvp(EPS_LOCATION_INFORMATION, S6A_VENDOR_ID, epsLocationInformation.byteArrayValue());
  }

}
