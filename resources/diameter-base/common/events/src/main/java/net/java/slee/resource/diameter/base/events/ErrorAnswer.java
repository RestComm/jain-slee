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

package net.java.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.avp.DiameterIdentity;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;

/**
 * Defines an interface representing the Error-Answer command.
 * 
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * 
 * <pre>
 * 7.2.  Error Bit
 * 
 *    The 'E' (Error Bit) in the Diameter header is set when the request
 *    caused a protocol-related error (see Section 7.1.3).  A message with
 *    the 'E' bit MUST NOT be sent as a response to an answer message.
 *    Note that a message with the 'E' bit set is still subjected to the
 *    processing rules defined in Section 6.2.  When set, the answer
 *    message will not conform to the ABNF specification for the command,
 *    and will instead conform to the following ABNF:
 * 
 *    Message Format
 * 
 *    &lt;Error-Answer&gt; ::= &lt; Diameter Header: 0, ERR [PXY] &gt;
 *                      0*1&lt; Session-Id &gt;
 *                         { Origin-Host }
 *                         { Origin-Realm }
 *                         { Result-Code }
 *                         [ Origin-State-Id ]
 *                         [ Error-Reporting-Host ]
 *                         [ Proxy-Info ]
 *                       * [ AVP ]
 *    Note that the code used in the header is the same than the one found
 *    in the request message, but with the 'R' bit cleared and the 'E' bit
 *    set.  The 'P' bit in the header is set to the same value as the one
 *    found in the request message.
 * 
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface ErrorAnswer extends DiameterMessage {

  int commandCode = 0;

  /**
   * Returns true if the Result-Code AVP is present in the message.
   */
  boolean hasResultCode();

  /**
   * Returns the value of the Result-Code AVP, of type Unsigned32. Use
   * {@link #hasResultCode()} to check the existence of this AVP.
   * 
   * @return the value of the Result-Code AVP
   * @throws IllegalStateException
   *             if the Result-Code AVP has not been set on this message
   */
  long getResultCode();

  /**
   * Sets the value of the Result-Code AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *             if setResultCode has already been called
   */
  void setResultCode(long resultCode);

  /**
   * Returns true if the Origin-State-Id AVP is present in the message.
   */
  boolean hasOriginStateId();

  /**
   * Returns the value of the Origin-State-Id AVP, of type Unsigned32. Use
   * {@link #hasOriginStateId()} to check the existence of this AVP.
   * 
   * @return the value of the Origin-State-Id AVP
   * @throws IllegalStateException
   *             if the Origin-State-Id AVP has not been set on this message
   */
  long getOriginStateId();

  /**
   * Sets the value of the Origin-State-Id AVP, of type Unsigned32.
   * 
   * @throws IllegalStateException
   *             if setOriginStateId has already been called
   */
  void setOriginStateId(long originStateId);

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
  void setErrorReportingHost(DiameterIdentity errorReportingHost);

  /**
   * Returns true if the Proxy-Info AVP is present in the message.
   */
  boolean hasProxyInfo();

  /**
   * Returns the value of the Proxy-Info AVP, of type Grouped.
   * 
   * @return the value of the Proxy-Info AVP or null if it has not been set on
   *         this message
   */
  ProxyInfoAvp getProxyInfo();

  /**
   * Sets the value of the Proxy-Info AVP, of type Grouped.
   * 
   * @throws IllegalStateException
   *             if setProxyInfo has already been called
   */
  void setProxyInfo(ProxyInfoAvp proxyInfo);

}
