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
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link AuthenticationInformationRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public class AuthenticationInformationRequestImpl extends DiameterMessageImpl implements AuthenticationInformationRequest {

  /**
   * @param message
   */
  public AuthenticationInformationRequestImpl(Message message) {
    super(message);
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  @Override
  public String getLongName() {
    return "Authentication-Information-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  @Override
  public String getShortName() {
    return "AIR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#hasRequestedEUTRANAuthenticationInfo()
   */
  public boolean hasRequestedEUTRANAuthenticationInfo() {
    return hasAvp(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#getRequestedEUTRANAuthenticationInfo()
   */
  public RequestedEUTRANAuthenticationInfoAvp getRequestedEUTRANAuthenticationInfo() {
    return (RequestedEUTRANAuthenticationInfoAvp) getAvpAsCustom(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, RequestedEUTRANAuthenticationInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setRequestedEUTRANAuthenticationInfo(net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp)
   */
  public void setRequestedEUTRANAuthenticationInfo(RequestedEUTRANAuthenticationInfoAvp requestedEUTRANAuthenticationInfo) {
    addAvp(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, requestedEUTRANAuthenticationInfo.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#hasRequestedUTRANGERANAuthenticationInfo()
   */
  public boolean hasRequestedUTRANGERANAuthenticationInfo() {
    return hasAvp(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#getRequestedUTRANGERANAuthenticationInfo()
   */
  public RequestedUTRANGERANAuthenticationInfoAvp getRequestedUTRANGERANAuthenticationInfo() {
    return (RequestedUTRANGERANAuthenticationInfoAvp) getAvpAsCustom(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, RequestedUTRANGERANAuthenticationInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setRequestedUTRANGERANAuthenticationInfo(net.java.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvp)
   */
  public void setRequestedUTRANGERANAuthenticationInfo(RequestedUTRANGERANAuthenticationInfoAvp requestedUTRANGERANAuthenticationInfoAvp) {
    addAvp(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, requestedUTRANGERANAuthenticationInfoAvp.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#hasVisitedPLMNId()
   */
  public boolean hasVisitedPLMNId() {
    return hasAvp(VISITED_PLMN_ID, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#getVisitedPLMNId()
   */
  public byte[] getVisitedPLMNId() {
    return getAvpAsOctetString(VISITED_PLMN_ID, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.AuthenticationInformationRequest#setVisitedPLMNId(byte[])
   */
  public void setVisitedPLMNId(byte[] visitedPLMNId) {
    addAvp(VISITED_PLMN_ID, S6A_VENDOR_ID, visitedPLMNId);
  }

}
