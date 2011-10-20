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

package org.mobicents.slee.resource.diameter.s6a;

import static net.java.slee.resource.diameter.s6a.events.avp.DiameterS6aAvpCodes.*;
import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.s6a.S6aAVPFactory;
import net.java.slee.resource.diameter.s6a.events.avp.AMBRAvp;
import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.APNConfigurationProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.ActiveAPNAvp;
import net.java.slee.resource.diameter.s6a.events.avp.AllocationRetentionPriorityAvp;
import net.java.slee.resource.diameter.s6a.events.avp.AuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSSubscribedQoSProfileAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EPSUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.EUTRANVectorAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MIPHomeAgentHostAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MMELocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.MMEUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SGSNLocationInformationAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SGSNUserStateAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp;

import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.AMBRAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.APNConfigurationAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.APNConfigurationProfileAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.ActiveAPNAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.AllocationRetentionPriorityAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.AuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EPSLocationInformationAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EPSSubscribedQoSProfileAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EPSUserStateAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.EUTRANVectorAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.MIPHomeAgentHostAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.MMELocationInformationAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.MMEUserStateAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SGSNLocationInformationAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SGSNUserStateAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SpecificAPNInfoAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvpImpl;
import org.mobicents.slee.resource.diameter.s6a.events.avp.TerminalInformationAvpImpl;

/**
 * Diameter S6a Reference Point AVP Factory. Implementation for {@link S6aAVPFactory}
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class S6aAVPFactoryImpl extends DiameterAvpFactoryImpl implements S6aAVPFactory {

  // TODO: Add helper create methods for the composite AVPs
  protected DiameterAvpFactory baseAvpFactory;

  public S6aAVPFactoryImpl(final DiameterAvpFactory baseAvpFactory) {
    this.baseAvpFactory = baseAvpFactory;
  }

  public DiameterAvpFactory getBaseFactory() {
    return this.baseAvpFactory;
  }

  public ActiveAPNAvp createActiveAPN() {
    return (ActiveAPNAvp) AvpUtilities.createAvp(ACTIVE_APN, S6A_VENDOR_ID, null, ActiveAPNAvpImpl.class);
  }

  public AllocationRetentionPriorityAvp createAllocationRetentionPriority() {
    return (AllocationRetentionPriorityAvp) AvpUtilities.createAvp(ALLOCATION_RETENTION_POLICY, S6A_VENDOR_ID, null, AllocationRetentionPriorityAvpImpl.class);
  }

  public AMBRAvp createAMBR() {
    return (AMBRAvp) AvpUtilities.createAvp(AMBR, S6A_VENDOR_ID, null, AMBRAvpImpl.class);
  }

  public APNConfigurationAvp createAPNConfiguration() {
    return (APNConfigurationAvp) AvpUtilities.createAvp(APN_CONFIGURATION, S6A_VENDOR_ID, null, APNConfigurationAvpImpl.class);
  }

  public APNConfigurationProfileAvp createAPNConfigurationProfile() {
    return (APNConfigurationProfileAvp) AvpUtilities.createAvp(APN_CONFIGURATION_PROFILE, S6A_VENDOR_ID, null, APNConfigurationProfileAvpImpl.class);
  }

  public AuthenticationInfoAvp createAuthenticationInfo() {
    return (AuthenticationInfoAvp) AvpUtilities.createAvp(AUTHENTICATION_INFO, S6A_VENDOR_ID, null, AuthenticationInfoAvpImpl.class);
  }

  public EPSLocationInformationAvp createEPSLocationInformation() {
    return (EPSLocationInformationAvp) AvpUtilities.createAvp(EPS_LOCATION_INFORMATION, S6A_VENDOR_ID, null, EPSLocationInformationAvpImpl.class);
  }

  public EPSSubscribedQoSProfileAvp createEPSSubscribedQoSProfile() {
    return (EPSSubscribedQoSProfileAvp) AvpUtilities.createAvp(EPS_SUBSCRIBED_QOS_PROFILE, S6A_VENDOR_ID, null, EPSSubscribedQoSProfileAvpImpl.class);
  }

  public EPSUserStateAvp createEPSUserState() {
    return (EPSUserStateAvp) AvpUtilities.createAvp(EPS_USER_STATE, S6A_VENDOR_ID, null, EPSUserStateAvpImpl.class);
  }

  public EUTRANVectorAvp createEUTRANVector() {
    return (EUTRANVectorAvp) AvpUtilities.createAvp(EUTRAN_VECTOR, S6A_VENDOR_ID, null, EUTRANVectorAvpImpl.class);
  }

  public MIP6AgentInfoAvp createMIP6AgentInfo() {
    return (MIP6AgentInfoAvp) AvpUtilities.createAvp(MIP6_AGENT_INFO, null, MIP6AgentInfoAvpImpl.class);
  }

  public MIPHomeAgentHostAvp createMIPHomeAgentHost() {
    return (MIPHomeAgentHostAvp) AvpUtilities.createAvp(MIP_HOME_AGENT_HOST, null, MIPHomeAgentHostAvpImpl.class);
  }

  public MMELocationInformationAvp createMMELocationInformation() {
    return (MMELocationInformationAvp) AvpUtilities.createAvp(MME_LOCATION_INFORMATION, S6A_VENDOR_ID, null, MMELocationInformationAvpImpl.class);
  }

  public MMEUserStateAvp createMMEUserState() {
    return (MMEUserStateAvp) AvpUtilities.createAvp(MME_USER_STATE, S6A_VENDOR_ID, null, MMEUserStateAvpImpl.class);
  }

  public RequestedEUTRANAuthenticationInfoAvp createRequestedEUTRANAuthenticationInfo() {
    return (RequestedEUTRANAuthenticationInfoAvp) AvpUtilities.createAvp(REQUESTED_EUTRAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, null, RequestedEUTRANAuthenticationInfoAvpImpl.class);
  }

  public RequestedUTRANGERANAuthenticationInfoAvp createRequestedUTRANGERANAuthenticationInfo() {
    return (RequestedUTRANGERANAuthenticationInfoAvp) AvpUtilities.createAvp(REQUESTED_UTRAN_GERAN_AUTHENTICATION_INFO, S6A_VENDOR_ID, null, RequestedUTRANGERANAuthenticationInfoAvpImpl.class);
  }

  public SGSNLocationInformationAvp createSGSNLocationInformation() {
    return (SGSNLocationInformationAvp) AvpUtilities.createAvp(SGSN_LOCATION_INFORMATION, S6A_VENDOR_ID, null, SGSNLocationInformationAvpImpl.class);
  }

  public SGSNUserStateAvp createSGSNUserState() {
    return (SGSNUserStateAvp) AvpUtilities.createAvp(SGSN_USER_STATE, S6A_VENDOR_ID, null, SGSNUserStateAvpImpl.class);
  }

  public SpecificAPNInfoAvp createSpecificAPNInfo() {
    return (SpecificAPNInfoAvp) AvpUtilities.createAvp(SPECIFIC_APN_INFO, S6A_VENDOR_ID, null, SpecificAPNInfoAvpImpl.class);
  }

  public SubscriptionDataAvp createSubscriptionData() {
    return (SubscriptionDataAvp) AvpUtilities.createAvp(SUBSCRIPTION_DATA, S6A_VENDOR_ID, null, SubscriptionDataAvpImpl.class);
  }

  public SupportedFeaturesAvp createSupportedFeatures() {
    return (SupportedFeaturesAvp) AvpUtilities.createAvp(SUPPORTED_FEATURES, S6A_VENDOR_ID, null, SupportedFeaturesAvpImpl.class);
  }

  public TerminalInformationAvp createTerminalInformation() {
    return (TerminalInformationAvp) AvpUtilities.createAvp(TERMINAL_INFORMATION, S6A_VENDOR_ID, null, TerminalInformationAvpImpl.class);
  }

}
