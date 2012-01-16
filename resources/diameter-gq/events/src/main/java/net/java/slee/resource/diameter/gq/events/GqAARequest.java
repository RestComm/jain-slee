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

package net.java.slee.resource.diameter.gq.events;


import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.gq.events.avp.BindingInformation;
import net.java.slee.resource.diameter.gq.events.avp.FlowGrouping;
import net.java.slee.resource.diameter.gq.events.avp.GloballyUniqueAddress;
import net.java.slee.resource.diameter.gq.events.avp.LatchingIndication;
import net.java.slee.resource.diameter.gq.events.avp.MediaComponentDescription;
import net.java.slee.resource.diameter.gq.events.avp.OverbookingIndicator;
import net.java.slee.resource.diameter.gq.events.avp.ReservationPriority;
import net.java.slee.resource.diameter.gq.events.avp.SIPForkingIndication;
import net.java.slee.resource.diameter.gq.events.avp.SpecificAction;

/**
 * <pre>
 * <b>7.1.1 AA-Request(AAR) command</b>
 * The AAR command, indicated by the Command-Code field set to 265 and the "R" bit set in the Command Flags field,
 * is sent by an AF to the SPDF in order to request the authorization for the bearer usage for the AF session.
 * Message Format:
 *   &lt;AA-Request&gt; ::= < Diameter Header: 265, REQ, PXY >
 *                    < Session-Id >
 *                    { Auth-Application-Id }
 *                    { Origin-Host }
 *                    { Origin-Realm }
 *                    { Destination-Realm }
 *                   *[ Media-Component-Description ]
 *                   *[ Flow-Grouping ]
 *                    [ AF-Charging-Identifier ]
 *                    [ SIP-Forking-Indication ]
 *                   *[ Specific-Action ]
 *                    [ User-Name ]
 *                    [ Binding-Information ]
 *                    [ Latching-Indication ]
 *                    [ Reservation-Priority ]
 *                    [ Globally-Unique-Address ]
 *                    [ Service-Class ]
 *                    [ Authorization-Lifetime ]
 *                   *[ Proxy-Info ]
 *                   *[ Route-Record ]
 *                    [ Overbooking-Indicator ]
 *                   *[ Authorization-Package-Id ]
 *                   *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */

public interface GqAARequest extends DiameterMessage {

  public static final int COMMAND_CODE = 265;

  /**
   * Returns the value of the Auth-Application-Id AVP, of type Unsigned32.
   * 
   * @return
   */
  long getAuthApplicationId();

  /**
   * Sets the value of the Auth-Application-Id AVP, of type Unsigned32.
   * 
   * @param authApplicationId
   * @throws IllegalStateException
   */
  void setAuthApplicationId(long authApplicationId) throws IllegalStateException;

  /**
   * Returns true if the Auth-Application-Id AVP is present in the message.
   * 
   * @return
   */
  boolean hasAuthApplicationId();

  /**
   * Returns true if the User-Name AVP is present in the message.
   */
  boolean hasUserName();

  /**
   * Returns the value of the User-Name AVP, of type UTF8String.
   * 
   * @return the value of the User-Name AVP or null if it has not been set on this message
   */
  String getUserName();

  /**
   * Sets the value of the User-Name AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *           if setUserName has already been called
   */
  void setUserName(String userName);

  /**
   * Returns the set of Proxy-Info AVPs.
   * 
   * @return
   */
  ProxyInfoAvp[] getProxyInfos();

  /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * 
   * @param proxyInfo
   * @throws IllegalStateException
   */
  void setProxyInfo(ProxyInfoAvp proxyInfo) throws IllegalStateException;

  /**
   * Sets the set of Proxy-Info AVPs, with all the values in the given array.
   * 
   * @param proxyInfos
   * @throws IllegalStateException
   */
  void setProxyInfos(ProxyInfoAvp[] proxyInfos) throws IllegalStateException;

  /**
   * Returns the set of Route-Record AVPs.
   * 
   * @return
   */
  DiameterIdentity[] getRouteRecords();

  /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   * 
   * @param routeRecord
   * @throws IllegalStateException
   */
  void setRouteRecord(DiameterIdentity routeRecord) throws IllegalStateException;

  /**
   * Sets the set of Route-Record AVPs, with all the values in the given array.
   * 
   * @param routeRecords
   * @throws IllegalStateException
   */
  void setRouteRecords(DiameterIdentity[] routeRecords) throws IllegalStateException;

  /**
   * Returns the value of the Authorization-Lifetime AVP, of type Unsigned32.
   * 
   * @return
   */
  long getAuthorizationLifetime();

  /**
   * Sets the value of the Authorization-Lifetime AVP, of type Unsigned32.
   * 
   * @param authApplicationId
   * @throws IllegalStateException
   */
  void setAuthorizationLifetime(long authorizationLifetime) throws IllegalStateException;

  /**
   * Returns true if the Authorization-Lifetime AVP is present in the message.
   * 
   * @return
   */
  boolean hasAuthorizationLifetime();

  /**
   * Returns the set of Media-Component-Description AVPs.
   * 
   * @return
   */
  MediaComponentDescription[] getMediaComponentDescriptions();

  /**
   * Sets a single Media-Component-Description AVP in the message, of type MediaComponentDescription.
   * 
   * @param mediaComponentDescription
   * @throws IllegalStateException
   */
  void setMediaComponentDescription(MediaComponentDescription mediaComponentDescription) throws IllegalStateException;

  /**
   * Sets the set of Media-Component-Description AVPs, with all the values in the given array.
   * 
   * @param mediaComponentDescriptions
   * @throws IllegalStateException
   */
  void setMediaComponentDescriptions(MediaComponentDescription[] mediaComponentDescriptions) throws IllegalStateException;

  /**
   * Returns the set of Flow-Grouping AVPs.
   * 
   * @return
   */
  FlowGrouping[] getFlowGroupings();

  /**
   * Sets a single Media-Component-Description AVP in the message, of type FlowGrouping.
   * 
   * @param flowGrouping
   * @throws IllegalStateException
   */
  void setFlowGrouping(FlowGrouping flowGrouping) throws IllegalStateException;

  /**
   * Sets the set of Media-Component-Description AVPs, with all the values in the given array.
   * 
   * @param flowGroupings
   * @throws IllegalStateException
   */
  void setFlowGroupings(FlowGrouping[] flowGroupings) throws IllegalStateException;

  /**
   * Returns the value of the AF-Charging-Identifier AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  byte[] getAFChargingIdentifier();

  /**
   * Sets the value of the AF-Charging-Identifier AVP, of type OctetString.
   */
  void setAFChargingIdentifier(byte[] AFChargingIdentifier);

  /**
   * Returns true if the AF-Charging-Identifier AVP is present in the message.
   * 
   * @return
   */
  boolean hasAFChargingIdentifier();

  /**
   * Returns the value of the SIP-Forking-Indication AVP, of type SIPForkingIndication. A return value of null implies that the AVP has not
   * been set.
   */
  SIPForkingIndication getSIPForkingIndication();

  /**
   * Sets the value of the SIP-Forking-Indication AVP, of type SIPForkingIndication.
   */
  void setSIPForkingIndication(SIPForkingIndication SIPForkingIndication);

  /**
   * Returns true if the Sip-Forking-Indication AVP is present in the message.
   * 
   * @return
   */
  boolean hasSIPForkingIndication();

  /**
   * Returns the set of Specific-Action AVPs.
   * 
   * @return
   */
  SpecificAction[] getSpecificActions();

  /**
   * Sets a single Specific-Action AVP in the message, of type SpecificAction.
   * 
   * @param specificAction
   * @throws IllegalStateException
   */
  void setSpecificAction(SpecificAction specificAction) throws IllegalStateException;

  /**
   * Sets the set of Specific-Action AVPs, with all the values in the given array.
   * 
   * @param specificActions
   * @throws IllegalStateException
   */
  void setSpecificActions(SpecificAction[] specificActions) throws IllegalStateException;

  /**
   * Returns the Binding-Information AVP.
   * 
   * @return
   */
  BindingInformation getBindingInformation();

  /**
   * Sets a Binding-Information AVP in the message, of type BindingInformation.
   * 
   * @param bindingInformation
   * @throws IllegalStateException
   */
  void setBindingInformation(BindingInformation bindingInformation) throws IllegalStateException;

  /**
   * Returns true if the Binding-Information AVP is present in the message.
   * 
   * @return
   */
  boolean hasBindingInformation();

  /**
   * Returns the Latching-Information AVP.
   * 
   * @return
   */
  LatchingIndication getLatchingIndication();

  /**
   * Sets a Latching-Indication AVP in the message, of type BindingInformation.
   * 
   * @param latchingIndication
   * @throws IllegalStateException
   */
  void setLatchingIndication(LatchingIndication latchingIndication) throws IllegalStateException;

  /**
   * Returns true if the Latching-Indication AVP is present in the message.
   * 
   * @return
   */
  boolean hasLatchingIndication();

  /**
   * Returns the Reservation-Priority AVP.
   * 
   * @return
   */
  ReservationPriority getReservationPriority();

  /**
   * Sets a Reservation-Priority AVP in the message, of type ReservationPriority.
   * 
   * @param reservationPriority
   * @throws IllegalStateException
   */
  void setReservationPriority(ReservationPriority reservationPriority) throws IllegalStateException;

  /**
   * Returns true if the Reservation-Priority AVP is present in the message.
   * 
   * @return
   */
  boolean hasReservationPriority();

  /**
   * Returns the Globally-Unique-Address AVP.
   * 
   * @return
   */
  GloballyUniqueAddress getGloballyUniqueAddress();

  /**
   * Sets a Globally-Unique-Address AVP in the message, of type GloballyUniqueAddress.
   * 
   * @param globallyUniqueAddress
   * @throws IllegalStateException
   */
  void setGloballyUniqueAddress(GloballyUniqueAddress globallyUniqueAddress) throws IllegalStateException;

  /**
   * Returns true if the Globally-Unique-Address AVP is present in the message.
   * 
   * @return
   */
  boolean hasGloballyUniqueAddress();

  /**
   * Returns true if the Service-Class AVP is present in the message.
   */
  boolean hasServiceClass();

  /**
   * Returns the value of the Service-Class AVP, of type UTF8String.
   * 
   * @return the value of the Service-Class AVP or null if it has not been set on this message
   */
  String getServiceClass();

  /**
   * Sets the value of the Service-Class AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *           if setServiceClass has already been called
   */
  void setServiceClass(String serviceClass);

  /**
   * Returns the Overbooking-Indicator AVP.
   * 
   * @return
   */
  OverbookingIndicator getOverbookingIndicator();

  /**
   * Sets a Overbooking-Indicator AVP in the message, of type OverbookingIndicator.
   * 
   * @param overbookingIndicator
   * @throws IllegalStateException
   */
  void setOverbookingIndicator(OverbookingIndicator overbookingIndicator) throws IllegalStateException;

  /**
   * Returns true if the Overbooking-Indicator AVP is present in the message.
   * 
   * @return
   */
  boolean hasOverbookingIndicator();

  /**
   * Returns the set of Authorization-Package-Id AVPs.
   * 
   * @return
   */
  String[] getAuthorizationPackageIds();

  /**
   * Sets a single Authorization-Package-Id AVP in the message, of type UTF8String.
   * 
   * @param specificAction
   * @throws IllegalStateException
   */
  void setAuthorizationPackageId(String authorizationPackageId) throws IllegalStateException;

  /**
   * Sets the set of Authorization-Package-Id AVPs, with all the values in the given array.
   * 
   * @param specificActions
   * @throws IllegalStateException
   */
  void setAuthorizationPackageIds(String[] authorizationPackageIds) throws IllegalStateException;
}
