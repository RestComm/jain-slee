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

/**
 * Defines an interface representing the Accounting-Answer command.
 * 
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * 
 * <pre>
 * 9.7.2.  Accounting-Answer
 * 
 *    The Accounting-Answer (ACA) command, indicated by the Command-Code
 *    field set to 271 and the Command Flags' 'R' bit cleared, is used to
 *    acknowledge an Accounting-Request command.  The Accounting-Answer
 *    command contains the same Session-Id and includes the usage AVPs only
 *    if CMS is in use when sending this command.  Note that the inclusion
 *    of the usage AVPs when CMS is not being used leads to unnecessarily
 *    large answer messages, and can not be used as a server's proof of the
 *    receipt of these AVPs in an end-to-end fashion.  If the Accounting-
 *    Request was protected by end-to-end security, then the corresponding
 *    ACA message MUST be protected by end-to-end security.
 * 
 *    Only the target Diameter Server, known as the home Diameter Server,
 *    SHOULD respond with the Accounting-Answer command.
 * 
 *    One of Acct-Application-Id and Vendor-Specific-Application-Id AVPs
 *    MUST be present.  If the Vendor-Specific-Application-Id grouped AVP
 *    is present, it must have an Acct-Application-Id inside.
 * 
 *    The AVP listed below SHOULD include service specific accounting AVPs,
 *    as described in Section 9.3.
 * 
 *    Message Format
 * 
 *       &lt;Accounting-Answer&gt; ::= &lt; Diameter Header: 271, PXY &gt;
 *                 &lt; Session-Id &gt;
 *                 { Result-Code }
 *                 { Origin-Host }
 *                 { Origin-Realm }
 *                 { Accounting-Record-Type }
 *                 { Accounting-Record-Number }
 *                 [ Acct-Application-Id ]
 *                 [ Vendor-Specific-Application-Id ]
 *                 [ User-Name ]
 *                 [ Accounting-Sub-Session-Id ]
 *                 [ Accounting-Session-Id ]
 *                 [ Acct-Multi-Session-Id ]
 *                 [ Error-Reporting-Host ]
 *                 [ Acct-Interim-Interval ]
 *                 [ Accounting-Realtime-Required ]
 *                 [ Origin-State-Id ]
 *                 [ Event-Timestamp ]
 *               * [ Proxy-Info ]
 *               * [ AVP ]
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountingAnswer extends DiameterMessage, AccountingMessage {

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

}
