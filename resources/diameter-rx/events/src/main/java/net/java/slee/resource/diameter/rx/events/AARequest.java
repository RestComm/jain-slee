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

package net.java.slee.resource.diameter.rx.events;

import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;
import net.java.slee.resource.diameter.rx.events.avp.ReservationPriority;
import net.java.slee.resource.diameter.rx.events.avp.SIPForkingIndication;
import net.java.slee.resource.diameter.rx.events.avp.ServiceInfoStatus;
import net.java.slee.resource.diameter.rx.events.avp.SpecificAction;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;

/**
 * The AAR command, indicated by the Command-Code field set to 265 and the 'R'
 * bit set in the Command Flags field, is sent by an AF to the PCRF in order
 * to provide it with the Session Information.
 * 
 * <pre>
 * < AA-Request> ::= < Diameter Header: 265, REQ, PXY >
 *                   < Session-Id >
 *                   { Auth-Application-Id }
 *                   { Origin-Host }
 *                   { Origin-Realm }
 *                   { Destination-Realm }
 *                   [ Destination-Host ]
 *                   [ AF-Application-Identifier ]
 *                  *[ Media-Component-Description ]
 *                   [ Service-Info-Status ]
 *                   [ AF-Charging-Identifier ]
 *                   [ SIP-Forking-Indication ]
 *                  *[ Specific-Action ]
 *                  *[ Subscription-ID ]
 *                  *[ Supported-Features ]
 *                   [ Reservation-Priority ]
 *                   [ Framed-IP-Address ]
 *                   [ Framed-IPv6-Prefix ]
 *                   [ Called-Station-ID ]
 *                   [ Service-URN ]
 *                   [ Sponsored-Connectivity-Data ]
 *                   [ MPS-Identifier ]
 *                   [ Rx-Request-Type ] 
 *                   [ Origin-State-Id ]
 *                  *[ Proxy-Info ]
 *                  *[ Route-Record ]
 *                  *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface AARequest extends AAMessage {

  static final int commandCode = 265;

  /**
   * The AF-Application-identifier AVP (AVP code 504) is of type OctetString,
   * and it contains information that identifies the particular service that
   * the AF service session belongs to. This information may be used by the
   * PCRF to differentiate QoS for different application services. For example
   * the AF-Application-Identifier may be used as additional information
   * together with the Media-Type AVP when the QoS class for the bearer
   * authorization at the Gx interface is selected. The
   * AF-Application-Identifier may be used also to complete the QoS
   * authorization with application specific default settings in the PCRF if
   * the AF does not provide full Session-Component-Description information.
   * 
   * @param afAppId
   */
  public void setAFApplicationIdentifier(byte[] afAppId);

  /**
   * Check if AF-Application-identifier AVP (AVP code 504) is present in
   * message. Returns true in case it is.
   * 
   * @return
   */
  public boolean hasAFApplicationIdentifier();

  /**
   * Retrieves possible value of AF-Application-identifier AVP (AVP code 504)
   * from message
   * 
   * @return
   */
  public byte[] getAFApplicationIdentifier();

  /**
   * Check if Media-Component-Description AVP (AVP code 517) is present in
   * message. Returns true in case it is.
   * 
   * @return
   */
  public boolean hasMediaComponentDescription();

  /**
   * The Media-Component-Description AVP (AVP code 517) is of type Grouped,
   * and it contains service information for a single media component within
   * an AF session or the AF signalling information. The service information
   * may be based on the SDI exchanged between the AF and the AF session
   * client in the UE. The information may be used by the PCRF to determine
   * authorized QoS and IP flow classifiers for bearer authorization and PCC
   * rule selection.
   * 
   * @param mcd
   */
  public void setMediaComponentDescription(MediaComponentDescriptionAvp mcd);

  public void setMediaComponentDescriptions(MediaComponentDescriptionAvp[] mcds);

  public MediaComponentDescriptionAvp[] getMediaComponentDescriptions();

  /**
   * The Service-Info-Status AVP (AVP code 527) is of type Enumerated, and
   * indicates the status of the service information that the AF is providing
   * to the PCRF.
   * 
   * @return
   */
  public ServiceInfoStatus getServiceInfoStatus();

  /**
   * Sets value of Service-Info-Status AVP (AVP code 527)
   * 
   * @param serviceInfoStatus
   */
  public void setServiceInfoStatus(ServiceInfoStatus serviceInfoStatus);

  /**
   * Check if Service-Info-Status AVP (AVP code 527) is present in message.
   * Returns true in case it is.
   * 
   * @return
   */
  public boolean hasServiceInfoStatus();

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
   * Checks if the SIP-Forking-Indication AVP (AVP code 523) is present in
   * message, returns true in case it is.
   * 
   * @return
   */
  public boolean hasSIPForkingIndication();

  /**
   * Sets value of the SIP-Forking-Indication AVP (AVP code 523),of type
   * Enumerated, and describes if several SIP dialogues are related to one
   * Diameter session
   * 
   * @param sipForkingIndication
   */
  public void setSIPForkingIndication(SIPForkingIndication sipForkingIndication);

  /**
   * Retrieves value of the SIP-Forking-Indication AVP (AVP code 523).
   * 
   * @return
   */
  public SIPForkingIndication getSIPForkingIndication();

  /**
   * Checks if the Specific-Action AVP (AVP code 513) is present in the
   * message.
   * 
   * @return
   */
  public boolean hasSpecificAction();

  /**
   * Sets value of the Specific-Action AVP (AVP code 513),of type Enumerated.
   * Within a PCRF initiated Re-Authorization Request, the Specific-Action AVP
   * determines the type of the action. Within an initial AA request the AF
   * may use the Specific-Action AVP to request specific actions from the
   * server at the bearer events and to limit the contact to such bearer
   * events where specific action is required. If the Specific-Action AVP is
   * omitted within the initial AA request, no notification of any of the
   * events defined below is requested.
   * 
   * @param specificAction
   */
  public void setSpecificAction(SpecificAction specificAction);

  public void setSpecificActions(SpecificAction[] specificAction);

  /**
   * Retrieves value of the Specific-Action AVP (AVP code 513).
   * 
   * @return
   */
  public SpecificAction[] getSpecificActions();

  /**
   * Checks if the Subscription-Id AVP (AVP Code 443) is present in the
   * message. In case it is, it returns true;
   * 
   * @return
   */
  public boolean hasSubscriptionId();

  public void setSubscriptionId(SubscriptionIdAvp subscriptionId);

  /**
   * Sets value of the Subscription-Id AVP (AVP Code 443). It is used to
   * identify the end user's subscription and is of type Grouped.
   * 
   * @param subscriptionIds
   */
  public void setSubscriptionIds(SubscriptionIdAvp[] subscriptionIds);

  /**
   * Retrieves value of the Subscription-Id AVP (AVP Code 443).
   * 
   * @return
   */
  public SubscriptionIdAvp[] getSubscriptionIds();

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
   * Fetches the Framed-IP-Address AVP (AVP Code 8) [RADIUS], of type
   * OctetString
   * 
   * @return
   */
  public byte[] getFramedIPAddress();

  /**
   * Checks if the Framed-IP-Address AVP (AVP Code 8) is present in message.
   * In case it is, this method returns true.
   * 
   * @return
   */
  public boolean hasFramedIPAddress();

  /**
   * Sets value of the Framed-IP-Address AVP (AVP Code 8).
   * 
   * @param framedIpAddress
   */
  public void setFramedIPAddress(byte[] framedIpAddress);

  /**
   * Fetches the Framed-IP-V6Prefix AVP (AVP Code 97) [RADIUS], of type
   * OctetString
   * 
   * @return
   */
  public byte[] getFramedIPV6Prefix();

  /**
   * Checks if the Framed-IP-V6Prefix AVP (AVP Code 97) is present in message.
   * In case it is, this method returns true.
   * 
   * @return
   */
  public boolean hasFramedIPV6Prefix();

  /**
   * Sets value of the Framed-IP-V6Prefix AVP (AVP Code 97).
   * 
   * @param framedIpV6Prefix
   */
  public void setFramedIPV6Prefix(byte[] framedIpV6Prefix);

  /**
   * Checks if the Called-Station-Id AVP (AVP Code 30) is present in message.
   * In case it is, method returns true.
   * 
   * @return
   */
  public boolean hasCalledStationId();

  /**
   * Fetches value of the Called-Station-Id AVP (AVP Code 30), of type
   * UTF8String. It allows the NAS to send the ASCII string describing the
   * layer 2 address the user contacted in the request. For dialup access,
   * this can be a phone number obtained by using Dialed Number Identification
   * (DNIS) or a similar technology. Note that this may be different from the
   * phone number the call comes in on. For use with IEEE 802 access, the
   * Called-Station-Id MAY contain a MAC address formatted as described in
   * [RAD802.1X]. It SHOULD only be present in authentication and/or
   * authorization requests.
   * 
   * If the Auth-Request-Type AVP is set to authorization-only and the
   * User-Name AVP is absent, the Diameter Server MAY perform authorization
   * based on this field. This can be used by a NAS to request whether a call
   * should be answered based on the DNIS.
   * 
   * The codification of this field's allowed usage range is outside the scope
   * of this specification.
   * 
   * @return
   */
  public String getCalledStationId();

  /**
   * Sets value of the Called-Station-Id AVP (AVP Code 30).
   * 
   * @param calledStationId
   */
  public void setCalledStationId(String calledStationId);

  /**
   * Checks if the Service-URN AVP (AVP code 525) is present in message. In
   * case it is, method returns true.
   * 
   * @return
   */
  public boolean hasServiceURN();

  /**
   * Sets value of the Service-URN AVP (AVP code 525), of type OctetString. It
   * indicates that an AF session is used for emergency traffic.
   * 
   * @param serviceURN
   */
  public void setServiceURN(byte[] serviceURN);

  /**
   * Fetches value of the Service-URN AVP (AVP code 525).
   * 
   * @return
   */
  public byte[] getServiceURN();

  /**
   * Checks if the Sponsored-Connectivity-Data AVP (AVP code 530) is present
   * in message. In case it is, method returns true.
   * 
   * @return
   */
  public boolean hasSponsoredConnectivityData();

  /**
   * Sets value of the Sponsored-Connectivity-Data AVP (AVP code 530), of type
   * Grouped
   * 
   * @param scd
   */
  public void setSponsoredConnectivityData(SponsoredConnectivityDataAvp scd);

  /**
   * Fetches value of the Sponsored-Connectivity-Data AVP (AVP code 530), of
   * type Grouped
   * 
   * @return
   */
  public SponsoredConnectivityDataAvp getSponsoredConnectivityData();

  /**
   * Checks if the MPS-Identifier AVP (AVP code 528) is present in message,
   * in case it is, method returns true.
   * 
   * @return
   */
  public boolean hasMPSIdentifier();

  /**
   * Sets value of the MPS-Identifier AVP (AVP code 528),of type OctetString.
   * It indicates that an AF session relates to an MPS session. It contains
   * the national variant for MPS service name (e.g., NGN GETS).
   * 
   * @param mpsIdentifier
   */
  public void setMPSIdentifier(byte[] mpsIdentifier);

  /**
   * Fetches value of the MPS-Identifier AVP (AVP code 528).
   * 
   * @return
   */
  public byte[] getMPSIdentifier();

  /**
   * Returns true if the Origin-State-Id AVP is present in the message.
   */
  public boolean hasOriginStateId();

  /**
   * Returns the value of the Origin-State-Id AVP, of type Unsigned32. Use
   * {@link #hasOriginStateId()} to check the existence of this AVP.
   * 
   * @return the value of the Origin-State-Id AVP
   * @throws IllegalStateException
   *             if the Origin-State-Id AVP has not been set on this message
   */
  public long getOriginStateId();

  /**
   * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *             if setOriginStateId has already been called
   */
  public void setOriginStateId(long originStateId);

  /**
   * Returns true if the Proxy-Info AVP is present in the message.
   */
  public boolean hasProxyInfo();

  /**
   * Returns true if the Route-Record AVP is present in the message.
   */
  public boolean hasRouteRecord();

}
