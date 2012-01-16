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

package org.mobicents.slee.resource.diameter.gx.events;

import net.java.slee.resource.diameter.gx.events.avp.PDPSessionOperation;
import net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation;
import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.cca.events.avp.SubscriptionIdAvpImpl;
import org.mobicents.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvpImpl;

import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import net.java.slee.resource.diameter.cca.events.avp.CreditControlAVPCodes;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp;
import net.java.slee.resource.diameter.gx.events.GxCreditControlRequest;
import org.mobicents.slee.resource.diameter.gx.events.avp.DiameterGxAvpCodes;
import org.mobicents.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformationImpl;

/**
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 */
public class GxCreditControlRequestImpl extends GxCreditControlMessageImpl implements GxCreditControlRequest {

    /**
     * @param message
     */
    public GxCreditControlRequestImpl(Message message) {
        super(message);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getServiceContextId()
     */
    @Override
    public String getServiceContextId() {
        return getAvpAsUTF8String(CreditControlAVPCodes.Service_Context_Id);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setServiceContextId(java.lang.String)
     */
    @Override
    public void setServiceContextId(String serviceContextId) throws IllegalStateException {
        addAvp(CreditControlAVPCodes.Service_Context_Id, serviceContextId);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasServiceContextId()
     */
    @Override
    public boolean hasServiceContextId() {
        return hasAvp(CreditControlAVPCodes.Service_Context_Id);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getSubscriptionIds()
     */
    @Override
    public SubscriptionIdAvp[] getSubscriptionIds() {
        return (SubscriptionIdAvp[]) getAvpsAsCustom(CreditControlAVPCodes.Subscription_Id, SubscriptionIdAvpImpl.class);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setSubscriptionId(net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp)
     */
    @Override
    public void setSubscriptionId(SubscriptionIdAvp subscriptionId) throws IllegalStateException {
        addAvp(CreditControlAVPCodes.Subscription_Id, subscriptionId.byteArrayValue());
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setSubscriptionIds(net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp[])
     */
    @Override
    public void setSubscriptionIds(SubscriptionIdAvp[] subscriptionIds) throws IllegalStateException {
        for (SubscriptionIdAvp subscriptionId : subscriptionIds) {
            setSubscriptionId(subscriptionId);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTerminationCause()
     */
    @Override
    public TerminationCauseType getTerminationCause() {
        return (TerminationCauseType) getAvpAsEnumerated(DiameterAvpCodes.TERMINATION_CAUSE, TerminationCauseType.class);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTerminationCause(net.java.slee.resource.diameter.base.events.avp.TerminationCauseType)
     */
    @Override
    public void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException {
        addAvp(DiameterAvpCodes.TERMINATION_CAUSE, terminationCause.getValue());
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTerminationCause()
     */
    @Override
    public boolean hasTerminationCause() {
        return hasAvp(DiameterAvpCodes.TERMINATION_CAUSE);
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getUserEquipmentInfo()
     */
    @Override
    public UserEquipmentInfoAvp getUserEquipmentInfo() {
        return (UserEquipmentInfoAvp) getAvpAsCustom(CreditControlAVPCodes.User_Equipment_Info, UserEquipmentInfoAvpImpl.class);
    }

    /* (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setUserEquipmentInfo(net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp)
     */
    @Override
    public void setUserEquipmentInfo(UserEquipmentInfoAvp userEquipmentInfo) throws IllegalStateException {
        addAvp(CreditControlAVPCodes.User_Equipment_Info, userEquipmentInfo.byteArrayValue());
    }

    /*
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasUserEquipmentInfo()
     */
    @Override
    public boolean hasUserEquipmentInfo() {
        return hasAvp(CreditControlAVPCodes.User_Equipment_Info);
    }

    @Override
    public String getLongName() {
        return "Credit-Control-Request";
    }

    @Override
    public String getShortName() {
        return "CCR";
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getFramedIPAddress()
     */
    public byte[] getFramedIPAddress() {
        return getAvpAsOctetString(DiameterGxAvpCodes.FRAMED_IP_ADDRESS);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasFramedIPAddress()
     */
    public boolean hasFramedIPAddress() {
        return hasAvp(DiameterGxAvpCodes.FRAMED_IP_ADDRESS);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setFramedIPAddress()
     */
    public void setFramedIPAddress(byte[] framedIpAddress) {
        addAvp(DiameterGxAvpCodes.FRAMED_IP_ADDRESS, framedIpAddress);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getFramedIPV6Prefix()
     */
    public byte[] getFramedIPV6Prefix() {
        return getAvpAsOctetString(DiameterGxAvpCodes.FRAMED_IPV6_PREFIX);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasFramedIPV6Prefix()
     */
    public boolean hasFramedIPV6Prefix() {
        return hasAvp(DiameterGxAvpCodes.FRAMED_IPV6_PREFIX);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setFramedIPV6Prefix()
     */
    public void setFramedIPV6Prefix(byte[] framedIPV6Prefix) {
        addAvp(DiameterGxAvpCodes.FRAMED_IPV6_PREFIX, framedIPV6Prefix);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTgppRatType()
     */
    public byte[] getTgppRatType() {
        return getAvpAsOctetString(DiameterGxAvpCodes.TGPP_RAT_TYPE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTgppRatType()
     */
    public boolean hasTgppRatType() {
        return hasAvp(DiameterGxAvpCodes.TGPP_RAT_TYPE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTgppRatType()
     */
    public void setTgppRatType(byte[] tgppRatType) {
        addAvp(DiameterGxAvpCodes.TGPP_RAT_TYPE, DiameterGxAvpCodes.TGPP_VENDOR_ID, tgppRatType);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTgppGPRSNegotiatedQosProfile()
     */
    public byte[] getTgppGPRSNegotiatedQosProfile() {
        return getAvpAsOctetString(DiameterGxAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTgppGPRSNegotiatedQosProfile()
     */
    public boolean hasTgppGPRSNegotiatedQosProfile() {
        return hasAvp(DiameterGxAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTgppGPRSNegotiatedQosProfile()
     */
    public void setTgppGPRSNegotiatedQosProfile(byte[] tgppGPRSNegotiatedQosProfile) {
        addAvp(DiameterGxAvpCodes.TGPP_GPRS_NEGOTIATED_QOS_PROFILE, DiameterGxAvpCodes.TGPP_VENDOR_ID, tgppGPRSNegotiatedQosProfile);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTgppSgsnMccMnc()
     */
    public byte[] getTgppSgsnMccMnc() {
        return getAvpAsOctetString(DiameterGxAvpCodes.TGPP_SGSN_MCC_MNC, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTgppSgsnMccMnc()
     */
    public boolean hasTgppSgsnMccMnc() {
        return hasAvp(DiameterGxAvpCodes.TGPP_SGSN_MCC_MNC, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTgppSgsnMccMnc()
     */
    public void setTgppSgsnMccMnc(byte[] tgppSgsnMccMnc) {
        addAvp(DiameterGxAvpCodes.TGPP_SGSN_MCC_MNC, DiameterGxAvpCodes.TGPP_VENDOR_ID, tgppSgsnMccMnc);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTgppSgsnAddress()
     */
    public byte[] getTgppSgsnAddress() {
        return getAvpAsOctetString(DiameterGxAvpCodes.TGPP_SGSN_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTgppSgsnAddress()
     */
    public boolean hasTgppSgsnAddress() {
        return hasAvp(DiameterGxAvpCodes.TGPP_SGSN_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTgppSgsnAddress()
     */
    public void setTgppSgsnAddress(byte[] TgppSgsnAddress) {
        addAvp(DiameterGxAvpCodes.TGPP_SGSN_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID, TgppSgsnAddress);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTgppSgsnIPV6Address()
     */
    public byte[] getTgppSgsnIPV6Address() {
        return getAvpAsOctetString(DiameterGxAvpCodes.TGPP_SGSN_IPv6_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTgppSgsnIPV6Address()
     */
    public boolean hasTgppSgsnIPV6Address() {
        return hasAvp(DiameterGxAvpCodes.TGPP_SGSN_IPv6_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTgppSgsnIPV6Address()
     */
    public void setTgppSgsnIPV6Address(byte[] tgppSgsnIPV6Address) {
        addAvp(DiameterGxAvpCodes.TGPP_SGSN_IPv6_ADDRESS, DiameterGxAvpCodes.TGPP_VENDOR_ID, tgppSgsnIPV6Address);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getCalledStationId()
     */
    public String getCalledStationId() {
        return getAvpAsUTF8String(DiameterGxAvpCodes.CALLED_STATION_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasCalledStationId()
     */
    public boolean hasCalledStationId() {
        return hasAvp(DiameterGxAvpCodes.CALLED_STATION_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setCalledStationId()
     */
    public void setCalledStationId(String calledStationId) {
      addAvp(DiameterGxAvpCodes.CALLED_STATION_ID, calledStationId);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getPdpdSessionOperation()
     */
    public PDPSessionOperation getPdpSessionOperation() {
        return (PDPSessionOperation) getAvpAsEnumerated(DiameterGxAvpCodes.PDP_SESSION_OPERATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, PDPSessionOperation.class);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasPdpSessionOperation()
     */
    public boolean hasPdpSessionOperation() {
        return hasAvp(DiameterGxAvpCodes.PDP_SESSION_OPERATION, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setPdpSessionOperation(net.java.slee.resource.diameter.gx.events.avp.PDPSessionOperation)
     */
    public void setPdpSessionOperation(PDPSessionOperation pdpSessionOperation) {
        addAvp(DiameterGxAvpCodes.PDP_SESSION_OPERATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, pdpSessionOperation.getValue());
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#getTFTPacketFilterInformation()
     */
    public TFTPacketFilterInformation getTFTPacketFilterInformation() {
        return (TFTPacketFilterInformation) getAvpAsCustom(DiameterGxAvpCodes.TFT_PACKET_FILTER_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, TFTPacketFilterInformationImpl.class);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#hasTFTPacketFilterInformation()
     */
    public boolean hasTFTPacketFilterInformation() {
        return hasAvp(DiameterGxAvpCodes.TFT_PACKET_FILTER_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID);
    }

    /**
     * (non-Javadoc)
     * @see net.java.slee.resource.diameter.gx.events.GxCreditControlRequest#setTFTPacketFilterInformation(net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation)
     */
    public void setTFTPacketFilterInformation(TFTPacketFilterInformation tFTPacketFilterInformation) {
        addAvp(DiameterGxAvpCodes.TFT_PACKET_FILTER_INFORMATION, DiameterGxAvpCodes.TGPP_VENDOR_ID, tFTPacketFilterInformation.byteArrayValue());
    }
}
