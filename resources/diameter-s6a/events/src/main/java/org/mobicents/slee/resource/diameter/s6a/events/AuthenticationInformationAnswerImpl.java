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

import net.java.slee.resource.diameter.s6a.events.avp.AuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.ErrorDiagnostic;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.*;

import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.base.events.avp.ExperimentalResultAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.AuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link AuthenticationInformationAnswer}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AuthenticationInformationAnswerImpl extends DiameterMessageImpl implements AuthenticationInformationAnswer {

  /**
   * @param message
   */
  public AuthenticationInformationAnswerImpl(Message message) {
    super(message);
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Authentication-Information-Answer";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "AIA";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#hasErrorDiagnostic()
   */
  public boolean hasErrorDiagnostic() {
    return hasAvp(ERROR_DIAGNOSTIC, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#getErrorDiagnostic()
   */
  public ErrorDiagnostic getErrorDiagnostic() {
    return (ErrorDiagnostic) getAvpAsEnumerated(ERROR_DIAGNOSTIC, S6A_VENDOR_ID, ErrorDiagnostic.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setErrorDiagnostic(net.java.slee.resource.diameter.s6a.events.avp.ErrorDiagnostic)
   */
  public void setErrorDiagnostic(ErrorDiagnostic errorDiagnostic) {
    addAvp(ERROR_DIAGNOSTIC, S6A_VENDOR_ID, errorDiagnostic.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#getExperimentalResult()
   */
  public ExperimentalResultAvp getExperimentalResult() {
    return (ExperimentalResultAvp) getAvpAsCustom(DiameterAvpCodes.EXPERIMENTAL_RESULT, ExperimentalResultAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#hasExperimentalResult()
   */
  public boolean hasExperimentalResult() {
    return hasAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setExperimentalResult(net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp)
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult) {
    addAvp(DiameterAvpCodes.EXPERIMENTAL_RESULT, experimentalResult.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for(SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#setAuthenticationInfo(net.java.slee.resource.diameter.s6a.events.avp.AuthenticationInfoAvp)
   */
  public void setAuthenticationInfo(AuthenticationInfoAvp authenticationInfo) {
    addAvp(AUTHENTICATION_INFO, S6A_VENDOR_ID, authenticationInfo.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#getAuthenticationInfo()
   */
  public AuthenticationInfoAvp getAuthenticationInfo() {
    return (AuthenticationInfoAvp) getAvpAsCustom(AUTHENTICATION_INFO, S6A_VENDOR_ID, AuthenticationInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationAnswer#hasAuthenticationInfo()
   */
  public boolean hasAuthenticationInfo() {
    return hasAvp(AUTHENTICATION_INFO, S6A_VENDOR_ID);
  }

}
