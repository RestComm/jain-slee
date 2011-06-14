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
import net.java.slee.resource.diameter.gq.events.avp.AbortCause;

/**
 * <pre>
 * <b>7.1.7 Abort-Session-Request(ASR) command</b>
 * The ASR command, indicated by the Command-Code field set to 274 and the "R" bit set in the Command Flags field, is
 * sent by the SPDF to inform the AF that all bearer resources for the authorized session have become unavailable.
 * Message Format:
 * &lt;Abort-Session-Request&gt; ::= < Diameter Header: 274, REQ, PXY >
 *                             < Session-Id >
 *                             { Origin-Host }
 *                             { Origin-Realm }
 *                             { Destination-Realm }
 *                             { Destination-Host }
 *                             { Auth-Application-Id }
 *                             { Abort-Cause }
 *                             [ Origin-State-Id ]
 *                            *[ Proxy-Info ]
 *                            *[ Route-Record ]
 *                             [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GqAbortSessionRequest extends DiameterMessage {

  public static final int COMMAND_CODE = 274;

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
   * Returns true if the Abort-Cause AVP is present in the message.
   */
  boolean hasAbortCause();

  /**
   * Returns the value of the Abort-Cause AVP, of type AbortCause.
   * 
   * @return the value of the Abort-Cause AVP or null if it has not been set on this message
   */
  AbortCause getAbortCause();

  /**
   * Sets the value of the Abort-Cause AVP, of type AbortCause.
   * 
   * @throws IllegalStateException
   *           if setAbortCause has already been called
   */
  void setAbortCause(AbortCause abortCause) throws IllegalStateException;
}
