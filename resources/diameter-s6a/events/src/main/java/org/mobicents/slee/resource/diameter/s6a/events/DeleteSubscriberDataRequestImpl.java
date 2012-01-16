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
import net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;

/**
 * Implementation for {@link DeleteSubscriberDataRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class DeleteSubscriberDataRequestImpl extends DiameterMessageImpl implements DeleteSubscriberDataRequest {

  /**
   * @param message
   */
  public DeleteSubscriberDataRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Delete-Subscriber-Data-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "DSR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#hasDSRFlags()
   */
  public boolean hasDSRFlags() {
    return hasAvp(DiameterS6aAvpCodes.DSR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getDSRFlags()
   */
  public long getDSRFlags() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.DSR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setDSRFlags(long)
   */
  public void setDSRFlags(long dsrFlags) {
    addAvp(DiameterS6aAvpCodes.DSR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID, dsrFlags);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getContextIdentifiers()
   */
  public long[] getContextIdentifiers() {
    return getAvpsAsUnsigned32(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setContextIdentifier(long)
   */
  public void setContextIdentifier(long contextIdentifier) {
    addAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, contextIdentifier);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setContextIdentifiers(long[])
   */
  public void setContextIdentifiers(long[] contextIdentifiers) {
    for(long contextIdentifier : contextIdentifiers) {
      setContextIdentifier(contextIdentifier);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#hasTraceReference()
   */
  public boolean hasTraceReference() {
    return hasAvp(DiameterS6aAvpCodes.TRACE_REFERENCE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getTraceReference()
   */
  public byte[] getTraceReference() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.TRACE_REFERENCE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setTraceReference(byte[])
   */
  public void setTraceReference(byte[] traceReference) {
    addAvp(DiameterS6aAvpCodes.TRACE_REFERENCE, DiameterS6aAvpCodes.S6A_VENDOR_ID, traceReference);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getTSCodes()
   */
  public byte[][] getTSCodes() {
    return getAvpsAsOctetString(DiameterS6aAvpCodes.TS_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setTSCode(byte[])
   */
  public void setTSCode(byte[] tsCode) {
    addAvp(DiameterS6aAvpCodes.TS_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID, tsCode);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setTSCodes(byte[][])
   */
  public void setTSCodes(byte[][] tsCodes) {
    for(byte[] tsCode : tsCodes) {
      setTSCode(tsCode);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#getSSCodes()
   */
  public byte[][] getSSCodes() {
    return getAvpsAsRaw(DiameterS6aAvpCodes.SS_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setSSCode(byte[])
   */
  public void setSSCode(byte[] ssCode) {
    addAvp(DiameterS6aAvpCodes.SS_CODE, DiameterS6aAvpCodes.S6A_VENDOR_ID, ssCode);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.DeleteSubscriberDataRequest#setSSCodes(byte[][])
   */
  public void setSSCodes(byte[][] ssCodes) {
    for(byte[] ssCode : ssCodes) {
      setSSCode(ssCode);
    }
  }

}
