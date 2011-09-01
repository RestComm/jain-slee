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

import net.java.slee.resource.diameter.base.events.avp.Address;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.RedirectHostUsageType;
import net.java.slee.resource.diameter.rx.events.avp.AcceptableServiceInfoAvp;
import net.java.slee.resource.diameter.rx.events.avp.AccessNetworkChargingIdentifierAvp;
import net.java.slee.resource.diameter.rx.events.avp.IPCANType;
import net.java.slee.resource.diameter.rx.events.avp.RATType;


/**
 * The AAA command, indicated by the Command-Code field set to 265 and the 'R' 
 * bit cleared in the Command Flags field, is sent by the PCRF to the AF in 
 * response to the AAR command.
 * 
 * <pre>
 * 
 * < AA-Answer> ::= < Diameter Header: 265, PXY >
 *                  < Session-Id >
 *                  { Auth-Application-Id }
 *                  { Origin-Host }
 *                  { Origin-Realm }
 *                  [ Result-Code ]
 *                  [ Experimental-Result ]
 *                 *[ Access-Network-Charging-Identifier ]
 *                  [ Access-Network-Charging-Address ]
 *                  [ Acceptable-Service-Info ]
 *                  [ IP-CAN-Type ]
 *                  [ RAT-Type ]
 *                 *[ Supported-Features ]
 *                 *[ Class ]
 *                  [ Error-Message ]
 *                  [ Error-Reporting-Host ]
 *                 *[ Failed-AVP ]
 *                  [ Origin-State-Id ] 
 *                 *[ Redirect-Host ]
 *                  [ Redirect-Host-Usage ]
 *                  [ Redirect-Max-Cache-Time ]
 *                 *[ Proxy-Info ]
 *                 *[ AVP ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface AAAnswer extends AAMessage {

  static final int commandCode = 265;

  /**
   * Check if the we have a Result-Code AVP in this message.
   * @return true if the Result-Code AVP is present in the message.
   */
  boolean hasResultCode();

  /**
   * Returns the value of the Result-Code AVP, of type Unsigned32. Use
   * {@link #hasResultCode()} to check the existence of this AVP.
   *
   * @return the value of the Result-Code AVP
   */
  long getResultCode();

  /**
   * Sets the value of the Result-Code AVP, of type Unsigned32.
   * @param resultCode the result code to set.
   */
  void setResultCode(long resultCode);

  /**
   * Returns true if the Error-Message AVP is present in the message.
   */
  boolean hasErrorMessage();

  /**
   * Returns the value of the Error-Message AVP, of type UTF8String.
   * @return the value of the Error-Message AVP or null if it has not been set on this message
   */
  String getErrorMessage();

  /**
   * Sets the value of the Error-Message AVP, of type UTF8String.
   * @throws IllegalStateException if setErrorMessage has already been called
   */
  void setErrorMessage(String errorMessage) throws IllegalStateException;

  /**
   * Returns true if the Error-Reporting-Host AVP is present in the message.
   */
  boolean hasErrorReportingHost();

  /**
   * Returns the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
   * @return the value of the Error-Reporting-Host AVP or null if it has not been set on this message
   */
  DiameterIdentity getErrorReportingHost();

  /**
   * Sets the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
   * @throws IllegalStateException if setErrorReportingHost has already been called
   */
  void setErrorReportingHost(DiameterIdentity errorReportingHost) throws IllegalStateException;

  /**
   * Returns true if the Experimental-Result AVP is present in the message.
   */
  boolean hasExperimentalResult();

  /**
   * Returns the value of the Experimental-Result AVP, of type ExperimentalResultAvp.
   * @return the value of the Experimental-Result AVP or null if it has not been set on this message
   */
  ExperimentalResultAvp getExperimentalResult();

  /**
   * Sets the value of the Experimental-Result AVP, of type ExperimentalResultAvp.
   * @throws IllegalStateException if setErrorReportingHost has already been called
   */
  void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException;

  /**
   * Returns true if the Origin-State-Id AVP is present in the message.
   */
  boolean hasOriginStateId();

  /**
   * Returns the value of the Origin-State-Id AVP, of type Unsigned32.
   * Use {@link #hasOriginStateId()} to check the existence of this AVP.  
   * @return the value of the Origin-State-Id AVP
   * @throws IllegalStateException if the Origin-State-Id AVP has not been set on this message
   */
  long getOriginStateId();

  /**
   * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
   * @throws IllegalStateException if setOriginStateId has already been called
   */
  void setOriginStateId(long originStateId);

  /**
   * Returns true if the Failed-AVP AVP is present in the message.
   */
  boolean hasFailedAvp();

  /**
   * Returns the set of Failed-AVP AVPs. The returned array contains
   * the AVPs in the order they appear in the message.
   * A return value of null implies that no Failed-AVP AVPs have been set.
   * The elements in the given array are FailedAvp objects.
   */
  FailedAvp[] getFailedAvps();

  /**
   * Sets a single Failed-AVP AVP in the message, of type Grouped.
   * @throws IllegalStateException if setFailedAvp or setFailedAvps
   *  has already been called
   */
  void setFailedAvp(FailedAvp failedAvp);

  /**
   * Sets the set of Failed-AVP AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and
   * getFailedAvps() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   *
   * @throws IllegalStateException if setFailedAvp or setFailedAvps
   *  has already been called
   */
  void setFailedAvps(FailedAvp[] failedAvps);

  /**
   * Returns true if the Redirect-Hosts AVP is present in the message.
   */
  boolean hasRedirectHosts();

  /**
   * Returns the set of Redirect-Host AVPs. The returned array contains
   * the AVPs in the order they appear in the message.
   * A return value of null implies that no Redirect-Host AVPs have been set.
   * The elements in the given array are DiameterURI objects.
   */
  DiameterURI[] getRedirectHosts();

  /**
   * Sets a single Redirect-Host AVP in the message, of type DiameterURI.
   * @throws IllegalStateException if setRedirectHost or setRedirectHosts
   *  has already been called
   */
  void setRedirectHost(DiameterURI redirectHost);

  /**
   * Sets the set of Redirect-Host AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and
   * getRedirectHosts() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   *
   * @throws IllegalStateException if setRedirectHost or setRedirectHosts
   *  has already been called
   */
  void setRedirectHosts(DiameterURI[] redirectHosts);

  /**
   * Returns true if the Redirect-Host-Usage AVP is present in the message.
   */
  boolean hasRedirectHostUsage();

  /**
   * Returns the value of the Redirect-Host-Usage AVP, of type Enumerated.
   * @return the value of the Redirect-Host-Usage AVP or null if it has not been set on this message
   */
  RedirectHostUsageType getRedirectHostUsage();

  /**
   * Sets the value of the Redirect-Host-Usage AVP, of type Enumerated.
   * @throws IllegalStateException if setRedirectHostUsage has already been called
   */
  void setRedirectHostUsage(RedirectHostUsageType redirectHostUsage);

  /**
   * Returns true if the Redirect-Max-Cache-Time AVP is present in the message.
   */
  boolean hasRedirectMaxCacheTime();

  /**
   * Returns the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
   * Use {@link #hasRedirectMaxCacheTime()} to check the existence of this AVP.  
   * @return the value of the Redirect-Max-Cache-Time AVP
   * @throws IllegalStateException if the Redirect-Max-Cache-Time AVP has not been set on this message
   */
  long getRedirectMaxCacheTime();

  /**
   * Sets the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
   * @throws IllegalStateException if setRedirectMaxCacheTime has already been called
   */
  void setRedirectMaxCacheTime(long redirectMaxCacheTime);

  /**
   * Returns true if the Proxy-Info AVP is present in the message.
   */
  boolean hasProxyInfo();

  /**
   * Returns the set of Proxy-Info AVPs. The returned array contains
   * the AVPs in the order they appear in the message.
   * A return value of null implies that no Proxy-Info AVPs have been set.
   * The elements in the given array are ProxyInfo objects.
   */
  ProxyInfoAvp[] getProxyInfos();

  /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * @throws IllegalStateException if setProxyInfo or setProxyInfos
   *  has already been called
   */
  void setProxyInfo(ProxyInfoAvp proxyInfo);

  /**
   * Sets the set of Proxy-Info AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in the array.
   *
   * Note: the array must not be altered by the caller following this call, and
   * getProxyInfos() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   *
   * @throws IllegalStateException if setProxyInfo or setProxyInfos
   *  has already been called
   */
  void setProxyInfos(ProxyInfoAvp[] proxyInfos);

  /**
   * Checks if the Access-Network-Charging-Identifier AVP (AVP code 502) is present in message. In case it is, method returns true;
   * @return
   */
  public boolean hasAccessNetworkChargingIdentifier();
  /**
   * Fetches values of the Access-Network-Charging-Identifier AVP (AVP code 502) from the message.
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
   * @param accessNetowrkChargingId
   */
  public void setAccessNetworkChargingIdentifier(AccessNetworkChargingIdentifierAvp accessNetowrkChargingId);

  public void setAccessNetworkChargingIdentifiers(AccessNetworkChargingIdentifierAvp[] accessNetowrkChargingIds);

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
   * @param address
   */
  public void setAccessNetworkChargingAddress(Address address);

  /**
   * Checks if the Acceptable-Service-Info AVP (AVP code 526) is present in message. In case it is, method returns true;
   * @return
   */
  public boolean hasAcceptableServiceInfo();

  /**
   * Sets value of the Acceptable-Service-Info AVP (AVP code 526),of type Grouped;
   * @param asi
   */
  public void setAcceptableServiceInfo(AcceptableServiceInfoAvp asi);

  /**
   * Fetches value of the Acceptable-Service-Info AVP (AVP code 526);
   * @return
   */
  public AcceptableServiceInfoAvp getAcceptableServiceInfo();

  /**
   * Checks if the IP-CAN-Type AVP (AVP code 1027) is present in message. In case it is, method returns true;
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
   * @param ipCanType
   */
  public void setIPCANType(IPCANType ipCanType);

  /**
   * Fetches value of  the IP-CAN-Type AVP (AVP code 1027);
   * @return
   */
  public IPCANType getIPCANType();

  /**
   * Checks if the RAT-Type AVP (AVP code 1032) is present in message. In case it is, method returns true;
   * @return
   */
  public boolean hasRATType();

  /**
   * Sets the value of the RAT-Type AVP (AVP code 1032), of type enumerated. It is used to identify the radio access technology that is serving the UE.
   * @param ratType
   */
  public void setRATType(RATType ratType);

  /**
   * Fetches value of the RAT-Type AVP (AVP code 1032);
   * @return
   */
  public RATType getRATType();

}
