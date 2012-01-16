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
import net.java.slee.resource.diameter.cxdx.events.PushProfileRequest;
import net.java.slee.resource.diameter.cxdx.events.avp.ChargingInformation;
import net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem;
import net.java.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.ChargingInformationImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItemImpl;
import org.mobicents.slee.resource.diameter.cxdx.events.avp.SupportedFeaturesAvpImpl;


/**
 *
 * PushProfileRequestImpl.java
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PushProfileRequestImpl extends DiameterMessageImpl implements PushProfileRequest {

  /**
   * @param message
   */
  public PushProfileRequestImpl(Message message) {
    super(message);
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  @Override
  public String getLongName() {
    return "Push-Profile-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  @Override
  public String getShortName() {
    return "PPR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#getChargingInformation()
   */
  public ChargingInformation getChargingInformation() {
    return (ChargingInformation) getAvpAsCustom(CHARGING_INFORMATION, CXDX_VENDOR_ID, ChargingInformationImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#getSIPAuthDataItem()
   */
  public SIPAuthDataItem getSIPAuthDataItem() {
    return (SIPAuthDataItem) getAvpAsCustom(SIP_AUTH_DATA_ITEM, CXDX_VENDOR_ID, SIPAuthDataItemImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, CXDX_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#getUserData()
   */
  public byte[] getUserData() {
    return getAvpAsOctetString(USER_DATA, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#hasChargingInformation()
   */
  public boolean hasChargingInformation() {
    return hasAvp(CHARGING_INFORMATION, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#hasSIPAuthDataItem()
   */
  public boolean hasSIPAuthDataItem() {
    return hasAvp(SIP_AUTH_DATA_ITEM, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#hasUserData()
   */
  public boolean hasUserData() {
    return hasAvp(USER_DATA, CXDX_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setChargingInformation(net.java.slee.resource.diameter.cxdx.events.avp.ChargingInformation)
   */
  public void setChargingInformation(ChargingInformation chargingInformation) {
    addAvp(CHARGING_INFORMATION, CXDX_VENDOR_ID, chargingInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setSIPAuthDataItem(net.java.slee.resource.diameter.cxdx.events.avp.SIPAuthDataItem)
   */
  public void setSIPAuthDataItem(SIPAuthDataItem sipAuthDataItem) {
    addAvp(SIP_AUTH_DATA_ITEM, CXDX_VENDOR_ID, sipAuthDataItem.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setSupportedFeatures(net.java.slee.resource.diameter.sh.client.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, CXDX_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setSupportedFeatureses(net.java.slee.resource.diameter.sh.client.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for(SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.cxdx.events.PushProfileRequest#setUserData(java.lang.String)
   */
  public void setUserData(byte[] userData) {
    addAvp(USER_DATA, CXDX_VENDOR_ID, userData);
  }

}
