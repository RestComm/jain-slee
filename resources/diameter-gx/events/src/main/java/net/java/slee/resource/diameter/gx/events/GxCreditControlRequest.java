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

package net.java.slee.resource.diameter.gx.events;


import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.cca.events.avp.UserEquipmentInfoAvp;
import net.java.slee.resource.diameter.gx.events.avp.PDPSessionOperation;
import net.java.slee.resource.diameter.gx.events.avp.TFTPacketFilterInformation;

/**
 * Interface defining GxCreditControlAnswer message as defined in 3GPP TS 29.210 V6.7.0 (2006-12). It has following structure:
 *
 * <pre>
 *      <CC-Request> ::= < Diameter Header: 272, REQ, PXY >
 *                        < Session-Id >
 *                        { Auth-Application-Id }
 *                        { Origin-Host }
 *                        { Origin-Realm }
 *                        { Destination-Realm }
 *                        { CC-Request-Type }
 *                        { CC-Request-Number }
 *                        [ Destination-Host ]
 *                        [ Origin-State-Id ]
 *                       *[ Subscription-Id ]
 *                        [ Framed-IP-Address ]
 *                        [ Framed-IPv6-Prefix ]
 *                        [ 3GPP-RAT-Type ]
 *                        [ Termination-Cause ]
 *                        [ User-Equipment-Info ]
 *                        [ 3GPP-GPRS-Negotiated-QoS-Profile ]
 *                        [ 3GPP-SGSN-MCC-MNC ]
 *                        [ 3GPP-SGSN-Address ]
 *                        [ 3GPP-SGSN-IPv6-Address ]
 *                        [ Called-Station-ID ]
 *                        [ Bearer-Usage ]
 *                        [ PDP-Session-Operation ]
 *                       *[ TFT-Packet-Filter-Information ]
 *                       *[ Proxy-Info ]
 *                       *[ Route-Record ]
 *                       *[ AVP ]
 *
 * </pre>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:karthikeyan_s@spanservices.com"> Karthikeyan Shanmugam (EmblaCom)</a>
 * @author <a href="mailto:carl-magnus.bjorkell@emblacom.com"> Carl-Magnus Björkell </a>
 */
public interface GxCreditControlRequest extends GxCreditControlMessage {

    static final int commandCode = 272;

    /**
     * Returns the value of the Service-Context-Id AVP, of type UTF8String.
     *
     * @return String
     */
    String getServiceContextId();

    /**
     * Sets the value of the Service-Context-Id AVP, of type UTF8String.
     *
     * @param serviceContextId
     * @throws IllegalStateException
     */
    void setServiceContextId(String serviceContextId) throws IllegalStateException;

    /**
     * Returns true if the Service-Context-Id AVP is present in the message.
     *
     * @return boolean
     */
    boolean hasServiceContextId();
   
    /**
     * Returns the value of the Origin-State-Id AVP, of type Unsigned32.
     *
     * @return long 
     */
    long getOriginStateId();

    /**
     * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
     *
     * @param originStateId long
     * @throws IllegalStateException
     */
    void setOriginStateId(long originStateId) throws IllegalStateException;

    /**
     * Returns true if the Origin-State-Id AVP is present in the message.
     *
     * @return
     */
    boolean hasOriginStateId();

    /**
     * Returns the set of Subscription-Id AVPs.
     *
     * @return
     */
    SubscriptionIdAvp[] getSubscriptionIds();

    /**
     * Sets a single Subscription-Id AVP in the message, of type Grouped.
     *
     * @param subscriptionId
     * @throws IllegalStateException
     */
    void setSubscriptionId(SubscriptionIdAvp subscriptionId) throws IllegalStateException;

    /**
     * Sets the set of Subscription-Id AVPs, with all the values in the given
     * array.
     *
     * @param subscriptionIds
     * @throws IllegalStateException
     */
    void setSubscriptionIds(SubscriptionIdAvp[] subscriptionIds) throws IllegalStateException;

    /**
     * Returns the value of the Termination-Cause AVP, of type Enumerated.
     *
     * @return
     */
    TerminationCauseType getTerminationCause();

    /**
     * Sets the value of the Termination-Cause AVP, of type Enumerated.
     *
     * @param terminationCause
     * @throws IllegalStateException
     */
    void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException;

    /**
     * Returns true if the Termination-Cause AVP is present in the message.
     *
     * @return
     */
    boolean hasTerminationCause();

    /**
     * Returns the value of Framed-Ip-Address AVP as OctetString. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @return String
     */
    byte[] getFramedIPAddress();

    /**
     * Returns true if Framed-Ip-Address AVP is present in the request. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @return
     */
    boolean hasFramedIPAddress();

    /**
     * Sets the value of the Framed-Ip-Address AVP, of type OctetString. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @param framedIpAddress String
     */
    void setFramedIPAddress(byte[] framedIpAddress);

    /**
     * Returns the value of Framed-IPv6-Prefix AVP as octet string. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4006.txt
     * @return String
     */
    byte[] getFramedIPV6Prefix();

    /**
     * Returns true if Framed-IPv6-Prefix AVP is present in the request. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @return boolean
     */
    boolean hasFramedIPV6Prefix();

    /**
     * Sets the value of the Framed-IPv6-Prefix AVP, of type OctetString. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @param framedIPV6Prefix
     */
    void setFramedIPV6Prefix(byte[] framedIPV6Prefix);

    /**
     * Returns the value of 3GPP-RAT-Type AVP as octet string. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return String
     */
    byte[] getTgppRatType();

    /**
     * Returns true if 3GPP-RAT-Type AVP is present in the request. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return boolean
     */
    boolean hasTgppRatType();

    /**
     * Sets the value of the 3GPP-RAT-Type AVP, of type OctetString. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @param tgppRatType String
     */
    void setTgppRatType(byte[] tgppRatType);

    /**
     * Returns the value of 3GPP-GPRS-Negotiated-QoS-Profile AVP as Octet string. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return String
     */
    byte[] getTgppGPRSNegotiatedQosProfile();

    /**
     * Returns true if 3GPP-GPRS-Negotiated-QoS-Profile AVP is present in the request. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return
     */
    boolean hasTgppGPRSNegotiatedQosProfile();

    /**
     * Sets the value of the 3GPP-GPRS-Negotiated-QoS-Profile AVP, of type OctetString. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @param tgppGPRSNegotiatedQosProfile
     */
    void setTgppGPRSNegotiatedQosProfile(byte[] tgppGPRSNegotiatedQosProfile);

    /**
     * Returns the value of 3GPP-SGSN-MCC_MNC AVP as Octet string. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return String
     */
    byte[] getTgppSgsnMccMnc();

    /**
     * Returns true if 3GPP-SGSN-MCC_MNC AVP is present in the request. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return boolean
     */
    boolean hasTgppSgsnMccMnc();

    /**
     * Sets the value of the 3GPP-SGSN-MCC_MNC AVP, of type OctetString. Defined in 3GPP TS 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @param tgppSgsnMccMnc String
     */
    void setTgppSgsnMccMnc(byte[] tgppSgsnMccMnc);

    /**
     * Returns the value of 3GPP-SGSN-Address AVP as Octet string. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return String
     */
    byte[] getTgppSgsnAddress();

    /**
     * Returns true if 3GPP-SGSN-Address AVP is present in the request. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return boolean
     */
    boolean hasTgppSgsnAddress();

    /**
     * Sets the value of the 3GPP-SGSN-Address, of type OctetString. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @param TgppSgsnAddress
     */
    void setTgppSgsnAddress(byte[] TgppSgsnAddress);

    /**
     * Returns the value of 3GPP-SGSN-IPv6-Address AVP as Octet string. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return String
     */
    byte[] getTgppSgsnIPV6Address();

    /**
     * Returns true if 3GPP-SGSN-IPv6-Address AVP is present in the request. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @return boolean
     */
    boolean hasTgppSgsnIPV6Address();

    /**
     * Sets the value of the 3GPP-SGSN-IPv6-Address, of type OctetString. Defined in 3GPP S 29.061
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329061v6f0
     * @param tgppSgsnIPV6Address
     */
    void setTgppSgsnIPV6Address(byte[] tgppSgsnIPV6Address);

    /**
     * Returns the value of Called-Station AVP as UTF8String. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @return
     */
    String getCalledStationId();

    /**
     * Returns true if Called-Station AVP is present in the request. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @return boolean
     */
    boolean hasCalledStationId();

    /**
     * Sets the value of the Called-Station, of type UTF8String. Defined in IEFT RFC 4005
     * http://www.ietf.org/rfc/rfc4005.txt
     * @param calledStationId
     */
    void setCalledStationId(String calledStationId);

    /**
     * Returns the value of PDP-Session-Operation AVP as Enumerated. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @return PDPSessionOperation
     */
    PDPSessionOperation getPdpSessionOperation();

    /**
     * Returns true if PDP-Session-Operation AVP is present in the request. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @return boolean
     */
    boolean hasPdpSessionOperation();

    /**
     * Sets the value of the PDP-Session-Operation, of type Enumerated. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @param pdpSessionOperation
     */
    void setPdpSessionOperation(PDPSessionOperation pdpSessionOperation);

    /**
     * Returns the value of TFT-Packet-Filter-Information AVP as Grouped. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @return TFTPacketFilterInformation
     */
    TFTPacketFilterInformation getTFTPacketFilterInformation();

    /**
     * Returns true if TFT-Packet-Filter-Information AVP is present in the request. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @return boolean
     */
    boolean hasTFTPacketFilterInformation();

    /**
     * Sets the value of the TFT-Packet-Filter-Information, of type Enumerated. Defined in 3GPP TS 29.210
     * http://pda.etsi.org/pda/home.asp?wkr=RTS/TSGC-0329210v670
     * @param tFTPacketFilterInformation
     */
    void setTFTPacketFilterInformation(TFTPacketFilterInformation tFTPacketFilterInformation);

    /**
     * Returns the value of the User-Equipment-Info AVP, of type Grouped.
     *
     * @return
     */
    UserEquipmentInfoAvp getUserEquipmentInfo();

    /**
     * Sets the value of the User-Equipment-Info AVP, of type Grouped.
     *
     * @param userEquipmentInfo
     * @throws IllegalStateException
     */
    void setUserEquipmentInfo(UserEquipmentInfoAvp userEquipmentInfo) throws IllegalStateException;

    /**
     * Returns true if the User-Equipment-Info AVP is present in the message.
     *
     * @return
     */
    boolean hasUserEquipmentInfo();
}
