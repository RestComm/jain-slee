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

package org.mobicents.slee.resource.diameter.cxdx.events;

import static net.java.slee.resource.diameter.cxdx.events.avp.DiameterCxDxAvpCodes.*;

import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.OriginatingRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.cxdx.events.avp.UserAuthorizationType;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvpImpl;

/**
 * 
 * LocationInfoRequestImpl.java
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class LocationInfoRequestImpl extends DiameterMessageImpl implements LocationInfoRequest {

  /**
   * Constructor.
   * 
   * @param message
   *          the Message object to be wrapped
   */
  public LocationInfoRequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Location-Info-Request";
  }

  @Override
  public String getShortName() {
    return "LIR";
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#getOriginatingRequest()
   */
  public OriginatingRequest getOriginatingRequest() {
    return (OriginatingRequest) getAvpAsEnumerated(ORIGINATING_REQUEST, CXDX_VENDOR_ID, OriginatingRequest.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#getPublicIdentity()
   */
  public String getPublicIdentity() {
    return getAvpAsUTF8String(PUBLIC_IDENTITY, CXDX_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, CXDX_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#getUserAuthorizationType()
   */
  public UserAuthorizationType getUserAuthorizationType() {
    return (UserAuthorizationType) getAvpAsEnumerated(USER_AUTHORIZATION_TYPE, CXDX_VENDOR_ID, UserAuthorizationType.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#hasOriginatingRequest()
   */
  public boolean hasOriginatingRequest() {
    return hasAvp(ORIGINATING_REQUEST, CXDX_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#hasPublicIdentity()
   */
  public boolean hasPublicIdentity() {
    return hasAvp(PUBLIC_IDENTITY, CXDX_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#hasUserAuthorizationType()
   */
  public boolean hasUserAuthorizationType() {
    return hasAvp(USER_AUTHORIZATION_TYPE, CXDX_VENDOR_ID);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setOriginatingRequest(net.java.slee.resource.diameter.cxdx.events.avp.OriginatingRequest)
   */
  public void setOriginatingRequest(OriginatingRequest originatingRequest) {
    addAvp(ORIGINATING_REQUEST, CXDX_VENDOR_ID, originatingRequest.getValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setPublicIdentity(java.lang.String)
   */
  public void setPublicIdentity(String publicIdentity) {
    addAvp(PUBLIC_IDENTITY, CXDX_VENDOR_ID, publicIdentity);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setSupportedFeatures(net.java.slee.resource.diameter.sh.client.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, CXDX_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setSupportedFeatureses(net.java.slee.resource.diameter.sh.client.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for(SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.cxdx.events.LocationInfoRequest#setUserAuthorizationType(net.java.slee.resource.diameter.cxdx.events.avp.UserAuthorizationType)
   */
  public void setUserAuthorizationType(UserAuthorizationType userAuthorizationType) {
    addAvp(USER_AUTHORIZATION_TYPE, CXDX_VENDOR_ID, userAuthorizationType.getValue());
  }

}
