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

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.S6A_VENDOR_ID;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.SUPPORTED_FEATURES;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link InsertSubscriberDataRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class InsertSubscriberDataRequestImpl extends DiameterMessageImpl implements InsertSubscriberDataRequest {

  /**
   * @param message
   */
  public InsertSubscriberDataRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Insert-Subscriber-Data-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "IDR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#hasSubscriptionData()
   */
  public boolean hasSubscriptionData() {
    return hasAvp(DiameterS6aAvpCodes.SUBSCRIPTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#getSubscriptionData()
   */
  public SubscriptionDataAvp getSubscriptionData() {
    return (SubscriptionDataAvp) getAvpAsCustom(DiameterS6aAvpCodes.SUBSCRIPTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID, SubscriptionDataAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#setSubscriptionData(net.java.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvp)
   */
  public void setSubscriptionData(SubscriptionDataAvp subscriptionData) {
    addAvp(DiameterS6aAvpCodes.SUBSCRIPTION_DATA, DiameterS6aAvpCodes.S6A_VENDOR_ID, subscriptionData.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#hasIDRFlags()
   */
  public boolean hasIDRFlags() {
    return hasAvp(DiameterS6aAvpCodes.IDR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#getIDRFlags()
   */
  public long getIDRFlags() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.IDR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.InsertSubscriberDataRequest#setIDRFlags(long)
   */
  public void setIDRFlags(long idrFlags) {
    addAvp(DiameterS6aAvpCodes.IDR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID, idrFlags);
  }

}
