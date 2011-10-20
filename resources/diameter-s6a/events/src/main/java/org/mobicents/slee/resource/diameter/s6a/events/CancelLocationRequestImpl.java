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

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.CANCELLATION_TYPE;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.S6A_VENDOR_ID;
import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.SUPPORTED_FEATURES;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.s6a.events.CancelLocationRequest;
import net.java.slee.resource.diameter.s6a.events.avp.CancellationType;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link CancelLocationRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class CancelLocationRequestImpl extends DiameterMessageImpl implements CancelLocationRequest {

  /**
   * @param message
   */
  public CancelLocationRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Cancel-Location-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "CLR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#hasCancellationType()
   */
  public boolean hasCancellationType() {
    return hasAvp(CANCELLATION_TYPE, S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#getCancellationType()
   */
  public CancellationType getCancellationType() {
    return (CancellationType) getAvpAsEnumerated(CANCELLATION_TYPE, S6A_VENDOR_ID, CancellationType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.CancelLocationRequest#setCancellationType(net.java.slee.resource.diameter.s6a.events.avp.CancellationType)
   */
  public void setCancellationType(CancellationType cancellationType) {
    addAvp(CANCELLATION_TYPE, S6A_VENDOR_ID, cancellationType.getValue());
  }

}
