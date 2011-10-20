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

package net.java.slee.resource.diameter.s6a;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
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

/**
 * Factory to support the creation of Grouped AVP instances for S6a.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface S6aAVPFactory extends DiameterAvpFactory {

  public ActiveAPNAvp createActiveAPN();

  public AllocationRetentionPriorityAvp createAllocationRetentionPriority();

  public AMBRAvp createAMBR();

  public APNConfigurationAvp createAPNConfiguration();

  public APNConfigurationProfileAvp createAPNConfigurationProfile();

  public AuthenticationInfoAvp createAuthenticationInfo();

  public EPSLocationInformationAvp createEPSLocationInformation();

  public EPSSubscribedQoSProfileAvp createEPSSubscribedQoSProfile();

  public EPSUserStateAvp createEPSUserState();

  public EUTRANVectorAvp createEUTRANVector();

  public MIP6AgentInfoAvp createMIP6AgentInfo();

  public MIPHomeAgentHostAvp createMIPHomeAgentHost();

  public MMELocationInformationAvp createMMELocationInformation();

  public MMEUserStateAvp createMMEUserState();

  public RequestedEUTRANAuthenticationInfoAvp createRequestedEUTRANAuthenticationInfo();

  public RequestedUTRANGERANAuthenticationInfoAvp createRequestedUTRANGERANAuthenticationInfo();

  public SGSNLocationInformationAvp createSGSNLocationInformation();

  public SGSNUserStateAvp createSGSNUserState();

  public SpecificAPNInfoAvp createSpecificAPNInfo();

  public SubscriptionDataAvp createSubscriptionData();

  public SupportedFeaturesAvp createSupportedFeatures();

  public TerminalInformationAvp createTerminalInformation();

}
