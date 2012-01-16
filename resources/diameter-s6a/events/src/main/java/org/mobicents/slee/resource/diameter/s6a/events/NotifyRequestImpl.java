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
import net.java.slee.resource.diameter.s6a.events.NotifyRequest;
import net.java.slee.resource.diameter.s6a.events.avp.AlertReason;
import net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes;
import net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.TerminalInformationAvpImpl;

/**
 * Implementation for {@link NotifyRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class NotifyRequestImpl extends DiameterMessageImpl implements NotifyRequest {

  /**
   * @param message
   */
  public NotifyRequestImpl(Message message) {
    super(message);
  }

  /*
   * (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getLongName()
   */
  public String getLongName() {
    return "Notify-Request";
  }

  /* (non-Javadoc)
   * @see org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl#getShortName()
   */
  public String getShortName() {
    return "NOR";
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasAuthSessionState()
   */
  public boolean hasAuthSessionState() {
    return hasAvp(DiameterAvpCodes.AUTH_SESSION_STATE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getAuthSessionState()
   */
  public AuthSessionStateType getAuthSessionState() {
    return (AuthSessionStateType) getAvpAsEnumerated(DiameterAvpCodes.AUTH_SESSION_STATE, AuthSessionStateType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setAuthSessionState(net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType)
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState) {
    addAvp(DiameterAvpCodes.AUTH_SESSION_STATE, authSessionState.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setSupportedFeatures(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp)
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures) {
    addAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, supportedFeatures.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setSupportedFeatureses(net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp[])
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses) {
    for (SupportedFeaturesAvp supportedFeatures : supportedFeatureses) {
      setSupportedFeatures(supportedFeatures);
    }
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getSupportedFeatureses()
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses() {
    return (SupportedFeaturesAvp[]) getAvpsAsCustom(SUPPORTED_FEATURES, S6A_VENDOR_ID, SupportedFeaturesAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasTerminalInformation()
   */
  public boolean hasTerminalInformation() {
    return hasAvp(DiameterS6aAvpCodes.TERMINAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getTerminalInformation()
   */
  public TerminalInformationAvp getTerminalInformation() {
    return (TerminalInformationAvp) getAvpAsCustom(DiameterS6aAvpCodes.TERMINAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, TerminalInformationAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setTerminalInformation(net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp)
   */
  public void setTerminalInformation(TerminalInformationAvp terminalInformation) {
    addAvp(DiameterS6aAvpCodes.TERMINAL_INFORMATION, DiameterS6aAvpCodes.S6A_VENDOR_ID, terminalInformation.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasMIP6AgentInfo()
   */
  public boolean hasMIP6AgentInfo() {
    return hasAvp(DiameterS6aAvpCodes.MIP6_AGENT_INFO);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getMIP6AgentInfo()
   */
  public MIP6AgentInfoAvp getMIP6AgentInfo() {
    return (MIP6AgentInfoAvp) getAvpAsCustom(DiameterS6aAvpCodes.MIP6_AGENT_INFO, MIP6AgentInfoAvpImpl.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setMIP6AgentInfo(net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp)
   */
  public void setMIP6AgentInfo(MIP6AgentInfoAvp mip) {
    addAvp(DiameterS6aAvpCodes.MIP6_AGENT_INFO, mip.byteArrayValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasVisitedNetworkIdentifier()
   */
  public boolean hasVisitedNetworkIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getVisitedNetworkIdentifier()
   */
  public byte[] getVisitedNetworkIdentifier() {
    return getAvpAsOctetString(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setVisitedNetworkIdentifier(byte[])
   */
  public void setVisitedNetworkIdentifier(byte[] visitedNetworkIdentifier) {
    addAvp(DiameterS6aAvpCodes.VISITED_NETWORK_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, visitedNetworkIdentifier);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasContextIdentifier()
   */
  public boolean hasContextIdentifier() {
    return hasAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getContextIdentifier()
   */
  public long getContextIdentifier() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setContextIdentifier(long)
   */
  public void setContextIdentifier(long contextIdentifier) {
    addAvp(DiameterS6aAvpCodes.CONTEXT_IDENTIFIER, DiameterS6aAvpCodes.S6A_VENDOR_ID, contextIdentifier);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasServiceSelection()
   */
  public boolean hasServiceSelection() {
    return hasAvp(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getServiceSelection()
   */
  public String getServiceSelection() {
    return getAvpAsUTF8String(DiameterS6aAvpCodes.SERVICE_SELECTION);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setServiceSelection(java.lang.String)
   */
  public void setServiceSelection(String serviceSelection) {
    addAvp(DiameterS6aAvpCodes.SERVICE_SELECTION, serviceSelection);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasAlertReason()
   */
  public boolean hasAlertReason() {
    return hasAvp(DiameterS6aAvpCodes.ALERT_REASON, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getAlertReason()
   */
  public AlertReason getAlertReason() {
    return (AlertReason) getAvpAsEnumerated(DiameterS6aAvpCodes.ALERT_REASON, DiameterS6aAvpCodes.S6A_VENDOR_ID, AlertReason.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setAlertReason(net.java.slee.resource.diameter.s6a.events.avp.AlertReason)
   */
  public void setAlertReason(AlertReason alertReason) {
    addAvp(DiameterS6aAvpCodes.ALERT_REASON, DiameterS6aAvpCodes.S6A_VENDOR_ID, alertReason.getValue());
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#hasNORFlags()
   */
  public boolean hasNORFlags() {
    return hasAvp(DiameterS6aAvpCodes.NOR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#getNORFlags()
   */
  public long getNORFlags() {
    return getAvpAsUnsigned32(DiameterS6aAvpCodes.NOR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.s6a.events.NotifyRequest#setNORFlags(long)
   */
  public void setNORFlags(long norFlags) {
    addAvp(DiameterS6aAvpCodes.NOR_FLAGS, DiameterS6aAvpCodes.S6A_VENDOR_ID, norFlags);
  }

}
