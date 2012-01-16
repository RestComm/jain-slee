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
import net.java.slee.resource.diameter.base.events.avp.TerminationCauseType;


/**
 * <pre>
 * <b>7.1.5 Session-Termination-Request(STR) command</b>
 * The STR command, indicated by the Command-Code field set to 275 and the "R" bit set in the Command Flags field, is
 * sent by the AF to inform the SPDF that an authorized session shall be terminated.
 * Message Format:
 * &lt;Session-Termination-Request&gt; ::= < Diameter Header: 275, REQ, PXY >
 *                                   < Session-Id >
 *                                   { Origin-Host }
 *                                   { Origin-Realm }
 *                                   { Destination-Realm }
 *                                   { Termination-Cause }
 *                                   { Auth-Application-Id }
 *                                   [ Destination-Host ]
 *                                  *[ Class ]
 *                                   [ Origin-State-Id ]
 *                                  *[ Proxy-Info ]
 *                                  *[ Route-Record ]
 *                                  *[ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqSessionTerminationRequest extends DiameterMessage {

  public static final int COMMAND_CODE = 275;

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
   * Returns true if the Origin-State-Id AVP is present in the message.
   */
  boolean hasOriginStateId();

  /**
   * Returns the value of the Origin-State-Id AVP, of type Unsigned32. Use {@link #hasOriginStateId()} to check the existence of this AVP.
   * 
   * @return the value of the Origin-State-Idd AVP
   * @throws IllegalStateException
   *           if the Origin-State-Id AVP has not been set on this message
   */
  long getOriginStateId();

  /**
   * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *           if setOriginStateId has already been called
   */
  void setOriginStateId(long originStateId);

  /**
   * Returns true if the Termination-Cause AVP is present in the message.
   */
  boolean hasTerminationCause();

  /**
   * Returns the value of the Termination-Cause AVP, of type TerminationCauseType.
   * 
   * @return the value of the Termination-Cause AVP or null if it has not been set on this message
   */
  TerminationCauseType getTerminationCause();

  /**
   * Sets the value of the Termination-Cause AVP, of type TerminationCauseType.
   * 
   * @throws IllegalStateException
   *           if setTerminationCause has already been called
   */
  void setTerminationCause(TerminationCauseType terminationCause) throws IllegalStateException;

  /**
   * Returns the set of Class AVPs.
   * 
   * @return
   */
  byte[][] getClasses();

  /**
   * Sets a single Class AVP in the message, of type OctetString.
   * 
   * @param classValue
   * @throws IllegalStateException
   */
  void setClass(byte[] classValue) throws IllegalStateException;

  /**
   * Sets the set of Class AVPs, with all the values in the given array.
   * 
   * @param classes
   * @throws IllegalStateException
   */
  void setClasses(byte[][] classes) throws IllegalStateException;
}
