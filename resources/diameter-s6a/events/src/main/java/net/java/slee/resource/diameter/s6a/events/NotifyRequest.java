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

package net.java.slee.resource.diameter.s6a.events;

import net.java.slee.resource.diameter.base.events.DiameterMessage;
import net.java.slee.resource.diameter.base.events.avp.AuthSessionStateType;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.s6a.events.avp.AlertReason;
import net.java.slee.resource.diameter.s6a.events.avp.MIP6AgentInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;
import net.java.slee.resource.diameter.s6a.events.avp.TerminalInformationAvp;

/**
 * Defines an interface representing the Purge-UE-Request message.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.2.17 Notify-Request (NOR) Command
 * 
 * The Notify-Request (NOR) command, indicated by the Command-Code field set to 323 and the 'R' bit set in the Command Flags field, is sent from MME or SGSN to HSS. 
 * 
 * Message Format
 * < Notify-Request> ::= < Diameter Header: 323, REQ, PXY, 16777251 >
 *                       < Session-Id >
 *                       [ Vendor-Specific-Application-Id ]
 *                       { Auth-Session-State }
 *                       { Origin-Host }
 *                       { Origin-Realm }
 *                       [ Destination-Host ]
 *                       { Destination-Realm }
 *                       { User-Name }
 *                     * [ Supported-Features ]
 *                       [ Terminal-Information ]
 *                       [ MIP6-Agent-Info ] 
 *                       [ Visited-Network-Identifier ]
 *                       [ Context-Identifier ]
 *                       [Service-Selection]
 *                       [ Alert-Reason ]
 *                       [ NOR-Flags ]
 *                      *[ AVP ]
 *                      *[ Proxy-Info ]
 *                      *[ Route-Record ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface NotifyRequest extends DiameterMessage {

  public static final int COMMAND_CODE = 323;

  /**
   * Returns true if the Vendor-Specific-Application-Id AVP is present in the message.
   * 
   * @return true if the Vendor-Specific-Application-Id AVP is present in the message, false otherwise
   */
  public boolean hasVendorSpecificApplicationId();

  /**
   * Returns the value of the Vendor-Specific-Application-Id AVP, of type Grouped.
   * 
   * @return the value of the Vendor-Specific-Application-Id AVP or null if it has not been set on this message
   */
  public VendorSpecificApplicationIdAvp getVendorSpecificApplicationId();

  /**
   * Sets the value of the Vendor-Specific-Application-Id AVP, of type Grouped.
   * 
   * @param vendorSpecificApplicationId the new value for the Vendor-Specific-Application-Id AVP
   */
  public void setVendorSpecificApplicationId(VendorSpecificApplicationIdAvp vendorSpecificApplicationId);

  /**
   * Returns true if the Auth-Session-State AVP is present in the message.
   * 
   * @return true if the Auth-Session-State AVP is present in the message, false otherwise
   */
  public boolean hasAuthSessionState();

  /**
   * Returns the value of the Auth-Session-State AVP, of type Enumerated.
   * 
   * @return the value of the Auth-Session-State AVP, of type Enumerated
   */
  public AuthSessionStateType getAuthSessionState();

  /**
   * Sets the value of the Auth-Session-State AVP, of type Enumerated.
   * 
   * @param authSessionState
   */
  public void setAuthSessionState(AuthSessionStateType authSessionState);

  /**
   * Returns true if the User-Name AVP is present in the message.
   * 
   * @return
   */
  public boolean hasUserName();

  /**
   * Returns the value of the User-Name AVP, of type UTF8String.
   * 
   * @return
   */
  public String getUserName();

  /**
   * Sets the value of the User-Name AVP, of type UTF8String.
   * 
   * @param userName
   */
  public void setUserName(String userName);

  /**
   * Set a single instance value of the Supported-Features AVP, of type Grouped.
   * 
   * @param supportedFeatures
   */
  public void setSupportedFeatures(SupportedFeaturesAvp supportedFeatures);

  /**
   * Set multiple instance value of the Supported-Features AVP, of type Grouped.
   * 
   * @param supportedFeatureses
   */
  public void setSupportedFeatureses(SupportedFeaturesAvp[] supportedFeatureses);

  /**
   * Returns the value of the Supported-Features AVP, of type Grouped.
   * 
   * @return
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses();

  /**
   * Returns true if the Terminal-Information AVP is present in the message.
   * 
   * @return true if the Terminal-Information AVP is present in the message, false otherwise
   */
  public boolean hasTerminalInformation();

  /**
   * Returns the value of the Terminal-Information AVP, of type Grouped.
   * 
   * @return the value of the Terminal-Information AVP or null if it has not been set on this message
   */
  public TerminalInformationAvp getTerminalInformation();

  /**
   * Sets the value of the Terminal-Information AVP, of type Grouped.
   * 
   * @param terminalInformation the new value for the Terminal-Information AVP
   */
  public void setTerminalInformation(TerminalInformationAvp terminalInformation);

  /**
   * Returns true if the MIP6-Agent-Info AVP is present in the message.
   * 
   * @return true if the MIP6-Agent-Info AVP is present in the message, false otherwise
   */
  public boolean hasMIP6AgentInfo();

  /**
   * Returns the value of the MIP6-Agent-Info AVP, of type Grouped.
   * 
   * @return the value of the MIP6-Agent-Info AVP or null if it has not been set on this message
   */
  public MIP6AgentInfoAvp getMIP6AgentInfo();

  /**
   * Sets the value of the MIP6-Agent-Info AVP, of type Grouped.
   * 
   * @param mip the new value for the MIP6-Agent-Info AVP
   */
  public void setMIP6AgentInfo(MIP6AgentInfoAvp mip);

  /**
   * Returns true if the Visited-Network-Identifier AVP is present in the message.
   * 
   * @return true if the Visited-Network-Identifier AVP is present in the message, false otherwise
   */
  public boolean hasVisitedNetworkIdentifier();

  /**
   * Returns the value of the Visited-Network-Identifier AVP, of type OctetString.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return the value of the Visited-Network-Identifier AVP or null if it has not been set on this message
   */
  public byte[] getVisitedNetworkIdentifier();

  /**
   * Sets the value of the Visited-Network-Identifier AVP, of type OctetString.
   * 
   * @param visitedNetworkIdentifier the new value for the Visited-Network-Identifier AVP
   */
  public void setVisitedNetworkIdentifier(byte[] visitedNetworkIdentifier);

  /**
   * Returns true if the Context-Identifier AVP is present in the message.
   * 
   * @return true if the Context-Identifier AVP is present in the message, false otherwise
   */
  public boolean hasContextIdentifier();

  /**
   * Returns the value of the Context-Identifier AVP, of type Unsigned32.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return the value of the Context-Identifier AVP or null if it has not been set on this message
   */
  public long getContextIdentifier();

  /**
   * Sets the value of the Context-Identifier AVP, of type Unsigned32.
   * 
   * @param contextIdentifier the new value for the Context-Identifier AVP
   */
  public void setContextIdentifier(long contextIdentifier);

  /**
   * Returns true if the Service-Selection AVP is present in the message.
   * 
   * @return true if the Service-Selection AVP is present in the message, false otherwise
   */
  public boolean hasServiceSelection();

  /**
   * Returns the value of the Service-Selection AVP, of type UTF8String.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return the value of the Service-Selection AVP or null if it has not been set on this message
   */
  public String getServiceSelection();

  /**
   * Sets the value of the Service-Selection AVP, of type UTF8String.
   * 
   * @param serviceSelection the new value for the Service-Selection AVP
   */
  public void setServiceSelection(String serviceSelection);

  /**
   * Returns true if the Alert-Reason AVP is present in the message.
   * 
   * @return true if the Alert-Reason AVP is present in the message, false otherwise
   */
  public boolean hasAlertReason();

  /**
   * Returns the value of the Alert-Reason AVP, of type Enumerated.
   * 
   * @return the value of the Alert-Reason AVP, of type Enumerated
   */
  public AlertReason getAlertReason();

  /**
   * Sets the value of the Alert-Reason AVP, of type Enumerated.
   * 
   * @param alertReason
   */
  public void setAlertReason(AlertReason alertReason);

  /**
   * Returns true if the NOR-Flags AVP is present in the message.
   * 
   * @return true if the NOR-Flags AVP is present in the message, false otherwise
   */
  public boolean hasNORFlags();

  /**
   * Returns the value of the NOR-Flags AVP, of type Unsigned32.
   * A return value of null implies that the AVP has not been set.
   * 
   * @return the value of the NOR-Flags AVP or null if it has not been set on this message
   */
  public long getNORFlags();

  /**
   * Sets the value of the NOR-Flags AVP, of type Unsigned32.
   * 
   * @param norFlags the new value for the NOR-Flags AVP
   */
  public void setNORFlags(long norFlags);

  /**
   * Returns the set of Proxy-Info AVPs. The returned array contains the AVPs in the order they appear in the message.
   * A return value of null implies that no Proxy-Info AVPs have been set.
   * The elements in the given array are ProxyInfo objects.
   * 
   * @return
   */
  public ProxyInfoAvp[] getProxyInfos();

  /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * 
   * @param proxyInfo
   */
  public void setProxyInfo(ProxyInfoAvp proxyInfo);

  /**
   * Sets the set of Proxy-Info AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getProxyInfos() is
   * not guaranteed to return the same array instance, e.g. an "==" check would fail.
   *
   * @param proxyInfos
   */
  public void setProxyInfos(ProxyInfoAvp[] proxyInfos);

  /**
   * Returns the set of Route-Record AVPs. The returned array contains the AVPs in the order they appear in the message.
   * A return value of null implies that no Route-Record AVPs have been set.
   * The elements in the given array are DiameterIdentity objects.
   * 
   * @return
   */
  public DiameterIdentity[] getRouteRecords();

  /**
   * Sets a single Route-Record AVP in the message, of type DiameterIdentity.
   * 
   * @param routeRecord
   */
  public void setRouteRecord(DiameterIdentity routeRecord);

  /**
   * Sets the set of Route-Record AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getRouteRecords() is
   * not guaranteed to return the same array instance, e.g. an "==" check would fail.
   *
   * @param routeRecords
   */
  public void setRouteRecords(DiameterIdentity[] routeRecords);

}
