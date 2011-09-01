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

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.cca.events.avp.SubscriptionIdAvp;
import net.java.slee.resource.diameter.rx.events.avp.AbortCause;
import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.FlowsAvp;
import net.java.slee.resource.diameter.rx.events.avp.IPCANType;
import net.java.slee.resource.diameter.rx.events.avp.RATType;
import net.java.slee.resource.diameter.rx.events.avp.SpecificAction;
import net.java.slee.resource.diameter.rx.events.avp.SponsoredConnectivityDataAvp;

/**
 * 
 * The RAR command, indicated by the Command-Code field set to 258 and the 'R'
 * bit set in the Command Flags field, is sent by the PCRF to the AF in order to
 * indicate an Rx specific action. Message Format:
 * 
 * <pre>
 *     <RA-Request> ::= < Diameter Header: 258, REQ, PXY >
 *          < Session-Id >
 *          { Origin-Host }
 *          { Origin-Realm }
 *          { Destination-Realm }
 *          { Destination-Host }
 *          { Auth-Application-Id }
 *          { Specific-Action }
 *         *[ Access-Network-Charging-Identifier ]
 *          [ Access-Network-Charging-Address ]
 *         *[ Flows ]
 *         *[ Subscription-ID ]
 *          [ Abort-Cause ]
 *          [ IP-CAN-Type ]
 *          [ RAT-Type ]
 *          [ Sponsored-Connectivity-Data ]
 *          [ Origin-State-Id ]
 *         *[ Class ]
 *         *[ Proxy-Info ]
 *         *[ Route-Record ]
 *         *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface ReAuthRequest extends DiameterMessage {

  static final int commandCode = 258;

  /**
   * Returns true if the Auth-Application-Id AVP is present in the message.
   */
  boolean hasAuthApplicationId();

  /**
   * Returns the value of the Auth-Application-Id AVP, of type Unsigned32. Use
   * {@link #hasAuthApplicationId()} to check the existence of this AVP.
   * 
   * @return the value of the Auth-Application-Id AVP
   * @throws IllegalStateException
   *             if the Auth-Application-Id AVP has not been set on this
   *             message
   */
  long getAuthApplicationId();

  /**
   * @throws IllegalStateException
   *             if setAuthApplicationId has already been called
   */
  void setAuthApplicationId(long authApplicationId);

  /**
   * Returns true if the Specific-Action AVP (AVP code 513) is present in the
   * message.
   */
  public boolean hasSpecificAction();

  /**
   * @throws IllegalStateException
   *             if the Specific-Action AVP (AVP code 513) has not been set on
   *             this message
   */
  public SpecificAction getSpecificAction();

  /**
   * Sets value of the Specific-Action AVP (AVP code 513)
   * 
   * @throws IllegalStateException
   *             if setSpecificAction has already been called
   */
  public void setSpecificAction(SpecificAction authApplicationId);

  /**
   * Checks if the Access-Network-Charging-Identifier AVP (AVP code 502) is
   * present in message. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasAccessNetworkChargingIdentifier();

  /**
   * Fetches values of the Access-Network-Charging-Identifier AVP (AVP code
   * 502) from the message.
   * 
   * @return
   */
  public AccessNetworkChargingIdentifierAvp[] getAccessNetworkChargingIdentifiers();

  /**
   * Sets the value of the Access-Network-Charging-Identifier AVP (AVP code
   * 502), of type Grouped. It contains a charging identifier (e.g. GCID)
   * within the Access-Network-Charging-Identifier-Value AVP along with
   * information about the flows transported within the corresponding bearer
   * within the Flows AVP. If no Flows AVP is provided, the Access Network
   * Charging-Identifier-Value applies for all flows within the AF session.
   * The Access-Network-Charging-Identifier AVP can be sent from the PCRF to
   * the AF. The AF may use this information for charging correlation with
   * session layer.
   * 
   * @param accessNetowrkChardingId
   */
  public void setAccessNetworkChargingIdentifier(AccessNetworkChargingIdentifierAvp accessNetowrkChardingId);

  public void setAccessNetworkChargingIdentifiers(AccessNetworkChargingIdentifierAvp[] accessNetowrkChardingIds);

  /**
   * Checks if the Access-Network-Charging-Address AVP (AVP code 501) is
   * present in message. In case it is, method returns true;
   * 
   * @return
   */
  public boolean hasAccessNetworkChargingAddress();

  /**
   * Fetches value of the Access-Network-Charging-Address AVP (AVP code 501)
   * ,of type Address. It indicates the IP Address of the network entity
   * within the access network performing charging (e.g. the GGSN IP address).
   * The Access Network Charging-Address AVP should not be forwarded over an
   * inter-operator interface.
   * 
   * @return
   */
  public Address getAccessNetworkChargingAddress();

  /**
   * Sets value of the Access-Network-Charging-Address AVP (AVP code 501).
   * 
   * @param a
   */
  public void setAccessNetworkChargingAddress(Address a);

  /**
   * Checks if Flows-AVP (AVP code 513) is present in GroupedAVP.
   * 
   * @return
   */
  public boolean hasFlows();

  /**
   * Fetches values of Flows-AVP (AVP code 513) AVPs.
   * 
   * @return
   */
  public FlowsAvp[] getFlows();

  /**
   * Sets value of Flows-AVP (AVP code 513).
   * 
   * @param flows
   */
  public void setFlows(FlowsAvp flows);

  public void setFlows(FlowsAvp[] flows);

  /**
   * Checks if the Subscription-Id AVP (AVP Code 443) is present in the
   * message. In case it is, it returns true;
   * 
   * @return
   */
  public boolean hasSubscriptionId();

  public void setSubscriptionId(SubscriptionIdAvp sid);

  /**
   * Sets value of the Subscription-Id AVP (AVP Code 443). It is used to
   * identify the end user's subscription and is of type Grouped.
   * 
   * @param sids
   */
  public void setSubscriptionIds(SubscriptionIdAvp[] sids);

  /**
   * Retrieves value of the Subscription-Id AVP (AVP Code 443).
   * 
   * @return
   */
  public SubscriptionIdAvp[] getSubscriptionIds();

  /**
   * Returns true if the Abort-Cause AVP is present in the message.
   */
  public boolean hasAbortCause();

  /**
   * Returns the value of the Abort-Cause AVP, of type AbortCause.
   * 
   * @return the value of the Abort-Cause AVP or null if it has not been set
   *         on this message
   */
  public AbortCause getAbortCause();

  /**
   * Sets the value of the Abort-Cause AVP, of type AbortCause.
   * 
   * @throws IllegalStateException
   *             if setAbortCause has already been called
   */
  public void setAbortCause(AbortCause abortCause) throws IllegalStateException;

  /**
   * Checks if the IP-CAN-Type AVP (AVP code 1027) is present in message. In
   * case it is, method returns true;
   * 
   * @return
   */
  public boolean hasIPCANType();

  /**
   * Sets value of the IP-CAN-Type AVP (AVP code 1027), of type Enumerated. It
   * indicates the type of Connectivity Access Network in which the user is
   * connected. The IP-CAN-Type AVP shall always be present during the IP-CAN
   * session establishment. During an IP-CAN session modification, this AVP
   * shall be present when there has been a change in the IP-CAN type and the
   * PCRF requested to be informed of this event. The Event-Trigger AVP with
   * value IP-CAN-CHANGE shall be provided together with the IP-CAN-Type AVP.
   * 
   * @param t
   */
  public void setIPCANType(IPCANType t);

  /**
   * Fetches value of the IP-CAN-Type AVP (AVP code 1027);
   * 
   * @return
   */
  public IPCANType getIPCANType();

  /**
   * Checks if the RAT-Type AVP (AVP code 1032) is present in message. In case
   * it is, method returns true;
   * 
   * @return
   */
  public boolean hasRATType();

  /**
   * Sets the value of the RAT-Type AVP (AVP code 1032), of type enumerated.
   * It is used to identify the radio access technology that is serving the
   * UE.
   * 
   * @param t
   */
  public void setRATType(RATType t);

  /**
   * Fetches value of the RAT-Type AVP (AVP code 1032);
   * 
   * @return
   */
  public RATType getRATType();

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
   * Returns true if the Destination-Realm AVP is present in the message.
   */
  public boolean hasDestinationRealm();

  /**
   * Returns the set of Proxy-Info AVPs. The returned array contains the AVPs
   * in the order they appear in the message. A return value of null implies
   * that no Proxy-Info AVPs have been set. The elements in the given array
   * are ProxyInfo objects.
   */
  public ProxyInfoAvp[] getProxyInfos();

  /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setProxyInfo or setProxyInfos has already been called
   */
  public void setProxyInfo(ProxyInfoAvp proxyInfo);

  /**
   * Sets the set of Proxy-Info AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in
   * the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getProxyInfos() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setProxyInfo or setProxyInfos has already been called
   */
  public void setProxyInfos(ProxyInfoAvp[] proxyInfos);

  /**
   * Returns the set of Route-Record AVPs. The returned array contains the
   * AVPs in the order they appear in the message. A return value of null
   * implies that no Route-Record AVPs have been set. The elements in the
   * given array are DiameterIdentity objects.
   */
  public DiameterIdentity[] getRouteRecords();

  /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   * 
   * @throws IllegalStateException
   *             if setRouteRecord or setRouteRecords has already been called
   */
  public void setRouteRecord(DiameterIdentity routeRecord);

  /**
   * Sets the set of Route-Record AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getRouteRecords() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setRouteRecord or setRouteRecords has already been called
   */
  public void setRouteRecords(DiameterIdentity[] routeRecords);

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
  boolean hasProxyInfo();

  /**
   * Returns true if the Route-Record AVP is present in the message.
   */
  boolean hasRouteRecord();

}
