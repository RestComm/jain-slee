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

package net.java.slee.resource.diameter.rx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;
import net.java.slee.resource.diameter.rx.events.avp.ReservationPriority;

/**
 * 
 * The Media-Component-Description AVP (AVP code 517) is of type Grouped, and it
 * contains service information for a single media component within an AF
 * session or the AF signalling information. The service information may be
 * based on the SDI exchanged between the AF and the AF session client in the
 * UE. The information may be used by the PCRF to determine authorized QoS and
 * IP flow classifiers for bearer authorization and PCC rule selection. Within
 * one Diameter message, a single IP flow shall not be described by more than
 * one Media-Component-Description AVP. Bandwidth information and Flow-Status
 * information provided within the Media-Component-Description AVP applies to
 * all those IP flows within the media component, for which no corresponding
 * information is being provided within Media-Sub-Component AVP(s). If a
 * Media-Component-Description AVP is not supplied by the AF, or if optional
 * AVP(s) within a Media-Component-Description AVP are omitted, but
 * corresponding information has been provided in previous Diameter messages,
 * the previous information for the corresponding IP flow(s) remains valid. All
 * IP flows within a Media-Component-Description AVP are permanently disabled by
 * supplying a Flow Status AVP with value "REMOVED". The server may delete
 * corresponding filters and state information. Reservation-Priority provided
 * within the Media-Component-Description AVP in the request from the AF applies
 * to all those IP flows within the media component and describes the relative
 * importance of the IP flow as compared to other IP flows. The PCRF may use
 * this value to implement priority based admission. If the Reservation-Priority
 * AVP is not specified the IP flow priority is DEFAULT (0). Each
 * Media-Component-Description AVP shall contain either zero, or one, or two
 * Codec-Data AVPs. In the case of conflicts, information contained in other
 * AVPs either within this Media-Component-Description AVP, or within the
 * corresponding Media-Component-Description AVP in a previous message, shall
 * take precedence over information within the Codec-Data AVP(s). The AF shall
 * provision all the available information in other applicable AVPs in addition
 * to the information in the Codec-Data AVP, if such other AVPs are specified.
 * If the SDP offer-answer procedures of IETF RFC 3264 [18] are applicable for
 * the session negotiation between the two ends taking part in the communication
 * (e.g. for IMS), the following applies: <br>
 * - The AF shall provision information derived from an SDP answer and shall
 * also provision information derived from the corresponding SDP offer. <br>
 * - If the Media-Component-Description AVP contains two Codec-Data AVPs, one of
 * them shall represent an SDP offer and the other one the corresponding SDP
 * answer. <br>
 * - If the Media-Component-Description AVP contains one Codec-Data AVP, and
 * this AVP represents an SDP offer, the AF shall provision the corresponding
 * SDP answer information in a Codec-Data AVP within a subsequent Rx message. <br>
 * NOTE: Some SDP parameters for the same codec in the SDP offer and answer are
 * independent of each other and refer to IP flows in opposite directions, for
 * instance some MIME parameters conveyed within "a=fmtp" SDP lines and the
 * packetization time within the "a=ptime" line. Other parameters within the SDP
 * answer take precedence over corresponding parameters within the SDP offer. If
 * SDP is applied without using the offer-answer procedures, zero or one
 * Codec-Data AVP shall be provisioned. The PCRF may provide the
 * Media-Component-Description AVP(s) within the Acceptable-Service-Info AVP in
 * the AA-Answer command if the service information received from the AF is
 * rejected. For this usage, the Media-Component-Description AVP shall only
 * include the appropriate Media-Component-Number AVP and the
 * Max-Requested-Bandwidth-UL and/or Max-Requested-Bandwidth-DL AVPs indicating
 * the maximum acceptable bandwidth.
 * 
 * <pre>
 * Media-Component-Description ::= < AVP Header: 517 >
 *                                 { Media-Component-Number } ; Ordinal number of the media comp.
 *                               * [ Media-Sub-Component ]    ; Set of flows for one flow identifier
 *                                 [ AF-Application-Identifier ]
 *                                 [ Media-Type ]
 *                                 [ Max-Requested-Bandwidth-UL ]
 *                                 [ Max-Requested-Bandwidth-DL ]
 *                                 [ Flow-Status ]
 *                                 [ Reservation-priority ]
 *                                 [ RS-Bandwidth ]
 *                                 [ RR-Bandwidth ]
 *                               * [ Codec-Data ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface MediaComponentDescriptionAvp extends GroupedAvp {

  /**
   * Checks if the Media-Component-Number AVP (AVP code 518) is present in
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMediaComponentNumber();

  /**
   * Sets value of the Media-Component-Number AVP (AVP code 518), of type
   * Unsigned32. It contains the ordinal number of the media component.
   * 
   * @param mediaComponentNumber
   */
  public void setMediaComponentNumber(long mediaComponentNumber);

  /**
   * Fetches the Media-Component-Number AVP (AVP code 518);
   * 
   * @return
   */
  public long getMediaComponentNumber();

  /**
   * Checks if the Media-Sub-Component AVP (AVP code 519) is present in
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMediaSubComponent();

  public void setMediaSubComponent(MediaSubComponentAvp msc);

  /**
   * Sets values of the Media-Sub-Component AVP (AVP code 519), of Grouped
   * type.
   * 
   * @param mscs
   */
  public void setMediaSubComponents(MediaSubComponentAvp[] mscs);

  /**
   * Fetches values of the Media-Sub-Component AVP (AVP code 519).
   * 
   * @return
   */
  public MediaSubComponentAvp[] getMediaSubComponents();

  /**
   * The AF-Charging-Identifier AVP (AVP code 505) is of type OctetString,
   * contains the AF Charging Identifier that is sent by the AF. This
   * information may be used for charging correlation with bearer layer.
   * 
   * @param afAppId
   */
  public void setAFChargingIdentifier(byte[] afAppId);

  /**
   * Check if AF-Charging-Identifier AVP (AVP code 505) is present in message.
   * Returns true in case it is.
   * 
   * @return
   */
  public boolean hasAFChargingIdentifier();

  /**
   * Retrieves possible value of AF-Charging-Identifier AVP (AVP code 505)
   * from message
   * 
   * @return
   */
  public byte[] getAFChargingIdentifier();

  /**
   * Returns true if the Media-Type AVP (AVP code 520) is present in the
   * message.
   */
  public boolean hasMediaType();

  /**
   * Sets the value of the Media-Type AVP (AVP code 520), of type Enumerated.
   */
  public void setMediaType(MediaType mediaType);

  /**
   * Returns the value of the Media-Type AVP (AVP code 520), of type
   * Enumerated.
   */
  public MediaType getMediaType();

  /**
   * Checks if the Max-Requested-Bandwidth-DL AVP (AVP code 515) is present
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMaxRequestedBandwidthDL();

  /**
   * Sets value of the Max-Requested-Bandwidth-DL AVP (AVP code 515), of type
   * Unsigned32. It indicates the maximum bandwidth in bits per second for a
   * downlink IP flow. The bandwidth contains all the overhead coming from the
   * IP-layer and the layers above, e.g. IP, UDP, RTP and RTP payload. When
   * provided in an AA-Request, it indicates the maximum requested bandwidth.
   * When provided in an AA-Answer, it indicates the maximum bandwidth
   * acceptable by PCRF.
   * 
   * @param mrbdl
   */
  public void setMaxRequestedBandwidthDL(long mrbdl);

  /**
   * Fetches value of the Max-Requested-Bandwidth-DL AVP (AVP code 515).
   * 
   * @return
   */
  public long getMaxRequestedBandwidthDL();

  /**
   * Checks if the Max-Requested-Bandwidth-UL AVP (AVP code 516) is present
   * Grouped AVP. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasMaxRequestedBandwidthUL();

  /**
   * Sets value of Tthe Max -Bandwidth-UL AVP (AVP code 516),of type
   * Unsigned32, and it indicates the maximum requested bandwidth in bits per
   * second for an uplink IP flow. The bandwidth contains all the overhead
   * coming from the IP-layer and the layers above, e.g. IP, UDP, RTP and RTP
   * payload. When provided in an AA-Request, it indicates the maximum
   * requested bandwidth. When provided in an AA-Answer, it indicates the
   * maximum bandwidth acceptable by PCRF.
   * 
   * 
   * @param mrbUL
   */
  public void setMaxRequestedBandwidthUL(long mrbUL);

  /**
   * Fetches value of the Max-Requested-Bandwidth-UL AVP (AVP code 516).
   * 
   * @return
   */
  public long getMaxRequestedBandwidthUL();

  /**
   * Returns the value of the Flow-Status AVP (AVP code 511) , of type
   * Enumerated.
   */
  public FlowStatus getFlowStatus();

  /**
   * Returns true if the Flow-Status AVP (AVP code 511) is present in the
   * message.
   */
  public boolean hasFlowStatus();

  /**
   * Sets the value of the Flow-Status AVP (AVP code 511) , of type
   * Enumerated.
   */
  public void setFlowStatus(FlowStatus flowStatus);

  /**
   * Returns the Reservation-Priority AVP.
   * 
   * @return
   */
  public ReservationPriority getReservationPriority();

  /**
   * Sets a Reservation-Priority AVP in the message, of type
   * ReservationPriority.
   * 
   * @param reservationPriority
   * @throws IllegalStateException
   */
  public void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException;

  /**
   * Returns true if the Reservation-Priority AVP is present in the message.
   * 
   * @return
   */
  public boolean hasReservationPriority();

  /**
   * Checks if the RS-Bandwidth AVP (AVP code 522) is present in Grouped AVP.
   * In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasRSBandwidth();

  /**
   * Sets value of the RS-Bandwidth AVP (AVP code 522), of type Unsigned32. It
   * indicates the maximum required bandwidth in bits per second for RTCP
   * sender reports within the session component, as specified in RFC 3556
   * [11]. The bandwidth contains all the overhead coming from the IP-layer
   * and the layers above, i.e. IP, UDP and RTCP.
   * 
   * @param mrbUL
   */
  public void setRSBandwidth(long mrbUL);

  /**
   * Fetches value of the RS-Bandwidth AVP (AVP code 522).
   * 
   * @return
   */
  public long getRSBandwidth();

  /**
   * Checks if the RR-Bandwidth AVP (AVP code 521) is present in Grouped AVP.
   * In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasRRBandwidth();

  /**
   * Sets value of the RR-Bandwidth AVP (AVP code 521), of type Unsigned32. It
   * it indicates the maximum required bandwidth in bits per second for RTCP
   * receiver reports within the session component, as specified in RFC 3556
   * [11]. The bandwidth contains all the overhead coming from the IP-layer
   * and the layers above, i.e. IP, UDP and RTCP.
   * 
   * @param mrbUL
   */
  public void setRRBandwidth(long mrbUL);

  /**
   * Fetches value of the RR-Bandwidth AVP (AVP code 521).
   * 
   * @return
   */
  public long getRRBandwidth();

  /**
   * Sets the value of the Codec-Data AVP, of type OctetString.
   */
  public void setCodecDatas(byte[][] codecsData);

  public byte[][] getCodecDatas();

}
