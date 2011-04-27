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

/**
 * Defines an interface representing the Capabilities-Exchange-Request command.
 *
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * <pre>
 * 5.3.1.  Capabilities-Exchange-Request
 * 
 *    The Capabilities-Exchange-Request (CER), indicated by the Command-
 *    Code set to 257 and the Command Flags' 'R' bit set, is sent to
 *    exchange local capabilities.  Upon detection of a transport failure,
 *    this message MUST NOT be sent to an alternate peer.
 * 
 *    When Diameter is run over SCTP [SCTP], which allows for connections
 *    to span multiple interfaces and multiple IP addresses, the
 *    Capabilities-Exchange-Request message MUST contain one Host-IP-
 *    Address AVP for each potential IP address that MAY be locally used
 *    when transmitting Diameter messages.
 * 
 *    Message Format
 * 
 *       &lt;Capabilities-Exchange-Request&gt; ::= &lt; Diameter Header: 257, REQ &gt;
 *                 { Origin-Host }
 *                 { Origin-Realm }
 *              1* { Host-IP-Address }
 *                 { Vendor-Id }
 *                 { Product-Name }
 *                 [ Origin-State-Id ]
 *               * [ Supported-Vendor-Id ]
 *               * [ Auth-Application-Id ]
 *               * [ Inband-Security-Id ]
 *               * [ Acct-Application-Id ]
 *               * [ Vendor-Specific-Application-Id ]
 *                 [ Firmware-Revision ]
 *               * [ AVP ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface CapabilitiesExchangeRequest extends CapabilitiesExchangeMessage {

}
