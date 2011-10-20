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
import net.java.slee.resource.diameter.s6a.events.avp.RequestedEUTRANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.RequestedUTRANGERANAuthenticationInfoAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

/**
 * Defines an interface representing the Authentication-Information-Request message.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.2.5  Authentication-Information-Request (AIR) Command
 * 
 * Defines an interface representing the Authentication-Information-Request message.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * < Authentication-Information-Request> ::= < Diameter Header: 318, REQ, PXY, 16777251 >
 *                                           < Session-Id >
 *                                           [ Vendor-Specific-Application-Id ]
 *                                           { Auth-Session-State }
 *                                           { Origin-Host }
 *                                           { Origin-Realm }
 *                                           [ Destination-Host ]
 *                                           { Destination-Realm }
 *                                           { User-Name }
 *                                          *[ Supported-Features ]
 *                                           [ Requested-EUTRAN-Authentication-Info ]
 *                                           [ Requested-UTRAN-GERAN-Authentication-Info ]
 *                                           { Visited-PLMN-Id }
 *                                          *[ AVP ]
 *                                          *[ Proxy-Info ]
 *                                          *[ Route-Record ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface AuthenticationInformationRequest extends DiameterMessage {

  public static final int COMMAND_CODE = 318;

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
   * Returns the value of the Supported-Features AVP, of type Grouped.
   * 
   * @return
   */
  public SupportedFeaturesAvp[] getSupportedFeatureses();

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
   * Returns true if the Requested-UTRAN- GERAN-Authentication-Info AVP is present in the message.
   * 
   * @return
   */
  public boolean hasRequestedEUTRANAuthenticationInfo();

  /**
   * Returns the value of the Requested-EUTRAN-Authentication-Info AVP, of type Grouped.
   * 
   * @return
   */
  public RequestedEUTRANAuthenticationInfoAvp getRequestedEUTRANAuthenticationInfo();

  /**
   * Sets the value of the Requested-EUTRAN-Authentication-Info AVP, of type Grouped.
   * 
   * @param requestedEUTRANAuthenticationInfo
   */
  public void setRequestedEUTRANAuthenticationInfo(RequestedEUTRANAuthenticationInfoAvp requestedEUTRANAuthenticationInfo);

  /**
   * Returns true if the Requested-UTRAN-GERAN-Authentication-Info AVP is present in the message.
   * 
   * @return
   */
  public boolean hasRequestedUTRANGERANAuthenticationInfo();

  /**
   * Returns the value of the Requested-UTRAN-GERAN-Authentication-Info AVP, of type Grouped.
   * 
   * @return
   */
  public RequestedUTRANGERANAuthenticationInfoAvp getRequestedUTRANGERANAuthenticationInfo();

  /**
   * Sets the value of the Requested-UTRAN-GERAN-Authentication-Info AVP, of type Grouped.
   * 
   * @param requestedUTRANGERANAuthenticationInfo
   */
  public void setRequestedUTRANGERANAuthenticationInfo(RequestedUTRANGERANAuthenticationInfoAvp requestedUTRANGERANAuthenticationInfo);

  /**
   * Returns true if the Visited-PLMN-Id AVP is present in the message.
   * 
   * @return
   */
  public boolean hasVisitedPLMNId();

  /**
   * Returns the value of the Visited-PLMN-Id AVP, of type OctetString.
   * 
   * @return the value of the Visited-PLMN-Id AVP, of type OctetString.
   */
  public byte[] getVisitedPLMNId();

  /**
   * Sets the value of the Visited-PLMN-Id AVP, of type OctetString.
   * 
   * @param visitedPLMNId
   */
  public void setVisitedPLMNId(byte[] visitedPLMNId);

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
