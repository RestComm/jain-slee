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

package org.mobicents.slee.resource.diameter.rf;

import net.java.slee.resource.diameter.base.DiameterAvpFactory;
import net.java.slee.resource.diameter.base.events.avp.AvpUtilities;
import net.java.slee.resource.diameter.rf.RfAvpFactory;
import net.java.slee.resource.diameter.rf.events.avp.AdditionalContentInformation;
import net.java.slee.resource.diameter.rf.events.avp.AddressDomain;
import net.java.slee.resource.diameter.rf.events.avp.ApplicationServerInformation;
import net.java.slee.resource.diameter.rf.events.avp.EventType;
import net.java.slee.resource.diameter.rf.events.avp.ImsInformation;
import net.java.slee.resource.diameter.rf.events.avp.InterOperatorIdentifier;
import net.java.slee.resource.diameter.rf.events.avp.LcsClientId;
import net.java.slee.resource.diameter.rf.events.avp.LcsClientName;
import net.java.slee.resource.diameter.rf.events.avp.LcsInformation;
import net.java.slee.resource.diameter.rf.events.avp.LcsRequestorId;
import net.java.slee.resource.diameter.rf.events.avp.LocationType;
import net.java.slee.resource.diameter.rf.events.avp.MbmsInformation;
import net.java.slee.resource.diameter.rf.events.avp.MbmsServiceType;
import net.java.slee.resource.diameter.rf.events.avp.MbmsUserServiceType;
import net.java.slee.resource.diameter.rf.events.avp.MessageBody;
import net.java.slee.resource.diameter.rf.events.avp.MessageClass;
import net.java.slee.resource.diameter.rf.events.avp.MmContentType;
import net.java.slee.resource.diameter.rf.events.avp.MmsInformation;
import net.java.slee.resource.diameter.rf.events.avp.NodeFunctionality;
import net.java.slee.resource.diameter.rf.events.avp.OriginatorAddress;
import net.java.slee.resource.diameter.rf.events.avp.PocInformation;
import net.java.slee.resource.diameter.rf.events.avp.PsFurnishChargingInformation;
import net.java.slee.resource.diameter.rf.events.avp.PsInformation;
import net.java.slee.resource.diameter.rf.events.avp.RecipientAddress;
import net.java.slee.resource.diameter.rf.events.avp.SdpMediaComponent;
import net.java.slee.resource.diameter.rf.events.avp.ServiceInformation;
import net.java.slee.resource.diameter.rf.events.avp.TalkBurstExchange;
import net.java.slee.resource.diameter.rf.events.avp.TimeStamps;
import net.java.slee.resource.diameter.rf.events.avp.TrunkGroupId;
import net.java.slee.resource.diameter.rf.events.avp.WlanInformation;
import net.java.slee.resource.diameter.rf.events.avp.WlanRadioContainer;

import org.mobicents.slee.resource.diameter.base.DiameterAvpFactoryImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.AdditionalContentInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.AddressDomainImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.ApplicationServerInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.DiameterRfAvpCodes;
import org.mobicents.slee.resource.diameter.rf.events.avp.EventTypeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.ImsInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.InterOperatorIdentifierImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.LcsClientIdImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.LcsClientNameImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.LcsInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.LcsRequestorIdImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.LocationTypeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.MbmsInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.MessageBodyImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.MessageClassImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.MmContentTypeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.MmsInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.OriginatorAddressImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.PocInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.PsFurnishChargingInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.PsInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.RecipientAddressImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.SdpMediaComponentImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.ServiceInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.TalkBurstExchangeImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.TimeStampsImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.TrunkGroupIdImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.WlanInformationImpl;
import org.mobicents.slee.resource.diameter.rf.events.avp.WlanRadioContainerImpl;

/**
 * Implementation of {@link RfAvpFactory}.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class RfAvpFactoryImpl extends DiameterAvpFactoryImpl implements RfAvpFactory {

  public RfAvpFactoryImpl() {
    super();

  }

  public AdditionalContentInformation createAdditionalContentInformation() {
    return (AdditionalContentInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.ADDITIONAL_CONTENT_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null,
        AdditionalContentInformationImpl.class);
  }

  public AddressDomain createAddressDomain() {
    return (AddressDomain) AvpUtilities.createAvp(DiameterRfAvpCodes.ADDRESS_DOMAIN, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, AddressDomainImpl.class);
  }

  public ApplicationServerInformation createApplicationServerInformation() {
    return (ApplicationServerInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.APPLICATION_SERVER_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null,
        ApplicationServerInformationImpl.class);
  }

  public EventType createEventType() {
    return (EventType) AvpUtilities.createAvp(DiameterRfAvpCodes.EVENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, EventTypeImpl.class);
  }

  public ImsInformation createImsInformation() {
    return (ImsInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.IMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, ImsInformationImpl.class);
  }

  public ImsInformation createImsInformation(NodeFunctionality nodeFunctionality) {
    // Create the empty AVP
    ImsInformation imsInformationAvp = createImsInformation();

    // Set the provided AVP values
    imsInformationAvp.setNodeFunctionality(nodeFunctionality);

    return imsInformationAvp;
  }

  public InterOperatorIdentifier createInterOperatorIdentifier() {
    return (InterOperatorIdentifier) AvpUtilities.createAvp(DiameterRfAvpCodes.INTER_OPERATOR_IDENTIFIER, DiameterRfAvpCodes.TGPP_VENDOR_ID, null,
        InterOperatorIdentifierImpl.class);
  }

  public LcsClientId createLcsClientId() {
    return (LcsClientId) AvpUtilities.createAvp(DiameterRfAvpCodes.LCS_CLIENT_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, LcsClientIdImpl.class);
  }

  public LcsClientName createLcsClientName() {
    return (LcsClientName) AvpUtilities.createAvp(DiameterRfAvpCodes.LCS_CLIENT_NAME, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, LcsClientNameImpl.class);
  }

  public LcsInformation createLcsInformation() {
    return (LcsInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.LCS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, LcsInformationImpl.class);
  }

  public LcsRequestorId createLcsRequestorId() {
    return (LcsRequestorId) AvpUtilities.createAvp(DiameterRfAvpCodes.LCS_REQUESTOR_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, LcsRequestorIdImpl.class);
  }

  public LocationType createLocationType() {
    return (LocationType) AvpUtilities.createAvp(DiameterRfAvpCodes.LOCATION_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, LocationTypeImpl.class);
  }

  public MbmsInformation createMbmsInformation() {
    return (MbmsInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.MBMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, MbmsInformationImpl.class);
  }

  public MbmsInformation createMbmsInformation(byte[] tmgi, MbmsServiceType mbmsServiceType, MbmsUserServiceType mbmsUserServiceType) {
    // Create the empty AVP
    MbmsInformation mbmsInformationAvp = createMbmsInformation();

    // Set the provided AVP values
    mbmsInformationAvp.setTmgi(tmgi);
    mbmsInformationAvp.setMbmsServiceType(mbmsServiceType);
    mbmsInformationAvp.setMbmsUserServiceType(mbmsUserServiceType);

    return mbmsInformationAvp;
  }

  public MessageBody createMessageBody() {
    return (MessageBody) AvpUtilities.createAvp(DiameterRfAvpCodes.MESSAGE_BODY, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, MessageBodyImpl.class);
  }

  public MessageClass createMessageClass() {
    return (MessageClass) AvpUtilities.createAvp(DiameterRfAvpCodes.MESSAGE_CLASS, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, MessageClassImpl.class);
  }

  public MmContentType createMmContentType() {
    return (MmContentType) AvpUtilities.createAvp(DiameterRfAvpCodes.MM_CONTENT_TYPE, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, MmContentTypeImpl.class);
  }

  public MmsInformation createMmsInformation() {
    return (MmsInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.MMS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, MmsInformationImpl.class);
  }

  public OriginatorAddress createOriginatorAddress() {
    return (OriginatorAddress) AvpUtilities.createAvp(DiameterRfAvpCodes.ORIGINATOR_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, OriginatorAddressImpl.class);
  }

  public PocInformation createPocInformation() {
    return (PocInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.POC_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, PocInformationImpl.class);
  }

  public PsFurnishChargingInformation createPsFurnishChargingInformation() {
    return (PsFurnishChargingInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.PS_FURNISH_CHARGING_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null,
        PsFurnishChargingInformationImpl.class);
  }

  public PsFurnishChargingInformation createPsFurnishChargingInformation(byte[] tgppChargingId, byte[] psFreeFormatData) {
    // Create the empty AVP
    PsFurnishChargingInformation psFurnishChargingInformationAvp = createPsFurnishChargingInformation();

    // Set the provided AVP values
    psFurnishChargingInformationAvp.setTgppChargingId(tgppChargingId);
    psFurnishChargingInformationAvp.setPsFreeFormatData(psFreeFormatData);

    return psFurnishChargingInformationAvp;
  }

  public PsInformation createPsInformation() {
    return (PsInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.PS_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, PsInformationImpl.class);
  }

  public RecipientAddress createRecipientAddress() {
    return (RecipientAddress) AvpUtilities.createAvp(DiameterRfAvpCodes.RECIPIENT_ADDRESS, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, RecipientAddressImpl.class);
  }

  public SdpMediaComponent createSdpMediaComponent() {
    return (SdpMediaComponent) AvpUtilities.createAvp(DiameterRfAvpCodes.SDP_MEDIA_COMPONENT, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, SdpMediaComponentImpl.class);
  }

  public ServiceInformation createServiceInformation() {
    return (ServiceInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.SERVICE_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, ServiceInformationImpl.class);
  }

  public TalkBurstExchange createTalkBurstExchange() {
    return (TalkBurstExchange) AvpUtilities.createAvp(DiameterRfAvpCodes.TALK_BURST_EXCHANGE, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, TalkBurstExchangeImpl.class);
  }

  public TimeStamps createTimeStamps() {
    return (TimeStamps) AvpUtilities.createAvp(DiameterRfAvpCodes.TIME_STAMPS, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, TimeStampsImpl.class);
  }

  public TrunkGroupId createTrunkGroupId() {
    return (TrunkGroupId) AvpUtilities.createAvp(DiameterRfAvpCodes.TRUNK_GROUP_ID, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, TrunkGroupIdImpl.class);
  }

  public WlanInformation createWlanInformation() {
    return (WlanInformation) AvpUtilities.createAvp(DiameterRfAvpCodes.WLAN_INFORMATION, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, WlanInformationImpl.class);
  }

  public WlanRadioContainer createWlanRadioContainer() {
    return (WlanRadioContainer) AvpUtilities.createAvp(DiameterRfAvpCodes.WLAN_RADIO_CONTAINER, DiameterRfAvpCodes.TGPP_VENDOR_ID, null, WlanRadioContainerImpl.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see net.java.slee.resource.diameter.rf.RfAVPFactory#getBaseFactory()
   */
  @Override
  public DiameterAvpFactory getBaseFactory() {
    return this;
  }

}
