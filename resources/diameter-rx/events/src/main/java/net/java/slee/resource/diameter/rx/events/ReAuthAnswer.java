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
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.DiameterURI;
import net.java.slee.resource.diameter.base.events.avp.ExperimentalResultAvp;
import net.java.slee.resource.diameter.base.events.avp.FailedAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.RedirectHostUsageType;
import net.java.slee.resource.diameter.rx.events.avp.MediaComponentDescriptionAvp;

/**
 * 
 * The RAA command, indicated by the Command-Code field set to 258 and the 'R'
 * bit cleared in the Command Flags field, is sent by the AF to the PCRF in
 * response to the RAR command. Message Format:
 * 
 * <pre>
 *     <RA-Answer> ::=  < Diameter Header: 258, PXY >
 *                      < Session-Id >
 *                      { Origin-Host }
 *                      { Origin-Realm }
 *                      [ Result-Code ]
 *                      [ Experimental-Result ]
 *                     *[ Media-Component-Description ]
 *                      [ Service-URN ]
 *                      [ Origin-State-Id ]
 *                     *[ Class ]
 *                      [ Error-Message ]
 *                      [ Error-Reporting-Host ] 
 *                     *[ Redirect-Host ]
 *                      [ Redirect-Host-Usage ]
 *                      [ Redirect-Max-Cache-Time ]
 *                     *[ Failed-AVP ]
 *                     *[ Proxy-Info ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:richard.good@smilecoms.com"> Richard Good </a>
 */
public interface ReAuthAnswer extends DiameterMessage {

  static final int commandCode = 258;

  /**
   * Returns true if the Experimental-Result AVP is present in the message.
   */
  boolean hasExperimentalResult();

  /**
   * Returns the value of the Experimental-Result AVP, of type
   * ExperimentalResultAvp.
   * 
   * @return the value of the Experimental-Result AVP or null if it has not
   *         been set on this message
   */
  ExperimentalResultAvp getExperimentalResult();

  /**
   * Sets the value of the Experimental-Result AVP, of type
   * ExperimentalResultAvp.
   * 
   * @throws IllegalStateException
   *             if setErrorReportingHost has already been called
   */
  void setExperimentalResult(ExperimentalResultAvp experimentalResult) throws IllegalStateException;

  /**
   * Check if the we have a Result-Code AVP in this message.
   * 
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
   * 
   * @param resultCode
   *            the result code to set.
   */
  void setResultCode(long resultCode);

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
   * Returns true if the Error-Message AVP is present in the message.
   */
  boolean hasErrorMessage();

  /**
   * Returns the value of the Error-Message AVP, of type UTF8String.
   * 
   * @return the value of the Error-Message AVP or null if it has not been set
   *         on this message
   */
  String getErrorMessage();

  /**
   * Sets the value of the Error-Message AVP, of type UTF8String.
   * 
   * @throws IllegalStateException
   *             if setErrorMessage has already been called
   */
  void setErrorMessage(String errorMessage) throws IllegalStateException;

  /**
   * Returns true if the Error-Reporting-Host AVP is present in the message.
   */
  boolean hasErrorReportingHost();

  /**
   * Returns the value of the Error-Reporting-Host AVP, of type
   * DiameterIdentity.
   * 
   * @return the value of the Error-Reporting-Host AVP or null if it has not
   *         been set on this message
   */
  DiameterIdentity getErrorReportingHost();

  /**
   * Sets the value of the Error-Reporting-Host AVP, of type DiameterIdentity.
   * 
   * @throws IllegalStateException
   *             if setErrorReportingHost has already been called
   */
  void setErrorReportingHost(DiameterIdentity errorReportingHost) throws IllegalStateException;

  /**
   * Returns the set of Failed-AVP AVPs. The returned array contains the AVPs
   * in the order they appear in the message. A return value of null implies
   * that no Failed-AVP AVPs have been set. The elements in the given array
   * are FailedAvp objects.
   */
  FailedAvp[] getFailedAvps();

  /**
   * Sets a single Failed-AVP AVP in the message, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setFailedAvp or setFailedAvps has already been called
   */
  void setFailedAvp(FailedAvp failedAvp);

  /**
   * Sets the set of Failed-AVP AVPs, with all the values in the given array.
   * The AVPs will be added to message in the order in which they appear in
   * the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getFailedAvps() is not guaranteed to return the same array instance,
   * e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setFailedAvp or setFailedAvps has already been called
   */
  void setFailedAvps(FailedAvp[] failedAvps);

  /**
   * Returns the set of Redirect-Host AVPs. The returned array contains the
   * AVPs in the order they appear in the message. A return value of null
   * implies that no Redirect-Host AVPs have been set. The elements in the
   * given array are DiameterURI objects.
   */
  DiameterURI[] getRedirectHosts();

  /**
   * Sets a single Redirect-Host AVP in the message, of type DiameterURI.
   * 
   * @throws IllegalStateException
   *             if setRedirectHost or setRedirectHosts has already been
   *             called
   */
  void setRedirectHost(DiameterURI redirectHost);

  /**
   * Sets the set of Redirect-Host AVPs, with all the values in the given
   * array. The AVPs will be added to message in the order in which they
   * appear in the array.
   * 
   * Note: the array must not be altered by the caller following this call,
   * and getRedirectHosts() is not guaranteed to return the same array
   * instance, e.g. an "==" check would fail.
   * 
   * @throws IllegalStateException
   *             if setRedirectHost or setRedirectHosts has already been
   *             called
   */
  void setRedirectHosts(DiameterURI[] redirectHosts);

  /**
   * Returns true if the Redirect-Host-Usage AVP is present in the message.
   */
  boolean hasRedirectHostUsage();

  /**
   * Returns the value of the Redirect-Host-Usage AVP, of type Enumerated.
   * 
   * @return the value of the Redirect-Host-Usage AVP or null if it has not
   *         been set on this message
   */
  RedirectHostUsageType getRedirectHostUsage();

  /**
   * Sets the value of the Redirect-Host-Usage AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setRedirectHostUsage has already been called
   */
  void setRedirectHostUsage(RedirectHostUsageType redirectHostUsage);

  /**
   * Returns true if the Redirect-Max-Cache-Time AVP is present in the
   * message.
   */
  boolean hasRedirectMaxCacheTime();

  /**
   * Returns the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
   * Use {@link #hasRedirectMaxCacheTime()} to check the existence of this
   * AVP.
   * 
   * @return the value of the Redirect-Max-Cache-Time AVP
   * @throws IllegalStateException
   *             if the Redirect-Max-Cache-Time AVP has not been set on this
   *             message
   */
  long getRedirectMaxCacheTime();

  /**
   * Sets the value of the Redirect-Max-Cache-Time AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *             if setRedirectMaxCacheTime has already been called
   */
  void setRedirectMaxCacheTime(long redirectMaxCacheTime);

  /**
   * Returns the set of Proxy-Info AVPs. The returned array contains the AVPs
   * in the order they appear in the message. A return value of null implies
   * that no Proxy-Info AVPs have been set. The elements in the given array
   * are ProxyInfo objects.
   */
  ProxyInfoAvp[] getProxyInfos();

  /**
   * Sets a single Proxy-Info AVP in the message, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setProxyInfo or setProxyInfos has already been called
   */
  void setProxyInfo(ProxyInfoAvp proxyInfo);

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
  void setProxyInfos(ProxyInfoAvp[] proxyInfos);

  /**
   * Returns true if the Proxy-Info AVP is present in the message.
   */
  boolean hasProxyInfo();

  /**
   * Returns true if the Redirect-Hosts AVP is present in the message.
   */
  boolean hasRedirectHosts();

  /**
   * Returns true if the Failed-AVP AVP is present in the message.
   */
  boolean hasFailedAvp();
}
