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
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SubscriptionDataAvp;
import net.java.slee.resource.diameter.s6a.events.avp.SupportedFeaturesAvp;

/**
 * Defines an interface representing the Update-Location-Answer message.
 * From the Diameter S6a Reference Point Protocol Details (3GPP TS 29.272 V9.6.0) specification:
 * 
 * <pre>
 * 7.2.4  Update-Location-Answer (ULA) Command
 * 
 * The Update-Location-Answer (ULA) command, indicated by the Command-Code field set to 316 and the
 * 'R' bit cleared in the Command Flags field, is sent from HSS to MME or SGSN.
 * 
 * < Update-Location-Answer> ::= < Diameter Header: 316, PXY, 16777251 >
 *                               < Session-Id >
 *                               [ Vendor-Specific-Application-Id ]
 *                               [ Result-Code ]
 *                               [ Experimental-Result ] 
 *                               [ Error-Diagnostic ] 
 *                               { Auth-Session-State }
 *                               { Origin-Host }
 *                               { Origin-Realm }
 *                              *[ Supported-Features ]
 *                               [ ULA-Flags ]
 *                               [ Subscription-Data ]
 *                              *[ AVP ]
 *                              *[ Failed-AVP ]
 *                              *[ Proxy-Info ]
 *                              *[ Route-Record ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 * @author <a href="mailto:paul.carter-brown@smilecoms.com"> Paul Carter-Brown </a>
 */
public interface UpdateLocationAnswer extends DiameterMessage {

  public static final int COMMAND_CODE = 316;

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
   * Returns true if the Result-Code AVP is present in the message.
   * 
   * @return
   */
  boolean hasResultCode();

  /**
   * Returns the value of the Result-Code AVP, of type Unsigned32.
   * Use {@link #hasResultCode()} to check the existence of this AVP.
   *   
   * @return the value of the Result-Code AVP
   */
  long getResultCode();

  /**
   * Sets the value of the Result-Code AVP, of type Unsigned32.
   * 
   * @param resultCode
   */
  void setResultCode(long resultCode);

  /**
   * Returns true if the Experimental-Result AVP is present in the message.
   * 
   * @return
   */
  public boolean hasExperimentalResult();

  /**
   * Returns the value of the Experimental-Result AVP, of type Grouped.
   * Use {@link #hasExperimentalResult()} to check the existence of this AVP.
   *   
   * @return the value of the Experimental-Result AVP
   */
  public ExperimentalResultAvp getExperimentalResult();

  /**
   * Sets the value of the Experimental-Result AVP, of type Grouped.
   * 
   * @param experimentalResult
   */
  public void setExperimentalResult(ExperimentalResultAvp experimentalResult);

  // FIXME: [ Error-Diagnostic ]

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
   * Returns true if the ULA-Flags AVP is present in the message.
   * 
   * @return
   */
  public boolean hasULAFlags();

  /**
   * Returns the value of the ULA-Flags AVP, of type Unsigned32.
   * 
   * @return
   */
  public long getULAFlags();

  /**
   * Sets the value of the ULA-Flags AVP, of type Unsigned32.
   * 
   * @param ulaFlags
   */
  public void setULAFlags(long ulaFlags);

  /**
   * Returns true if the Subscription-Data AVP is present in the message.
   * 
   * @return
   */
  public boolean hasSubscriptionData();

  /**
   * Returns the value of the Subscription-Data AVP, of type Grouped.
   * 
   * @return
   */
  public SubscriptionDataAvp getSubscriptionData();

  /**
   * Sets the value of the Subscription-Data AVP, of type Grouped.
   * 
   * @param subscriptionData
   */
  public void setSubscriptionData(SubscriptionDataAvp subscriptionData);

  /**
   * Returns the set of Failed-AVP AVPs. The returned array contains the AVPs in the order they
   * appear in the message.
   * A return value of null implies that no Failed-AVP AVPs have been set.
   * The elements in the given array are FailedAvp objects.
   */
  public FailedAvp[] getFailedAvps();

  /**
   * Sets a single Failed-AVP AVP in the message, of type Grouped.
   * 
   * @param failedAvp
   */
  public void setFailedAvp(FailedAvp failedAvp);

  /**
   * Sets the set of Failed-AVP AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and getFailedAvps()
   * is not guaranteed to return the same array instance, e.g. an "==" check would fail.
   * 
   * @param failedAvps
   */
  public void setFailedAvps(FailedAvp[] failedAvps);

  /**
   * Returns the set of Proxy-Info AVPs. The returned array contains the AVPs in the order they
   * appear in the message.
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
