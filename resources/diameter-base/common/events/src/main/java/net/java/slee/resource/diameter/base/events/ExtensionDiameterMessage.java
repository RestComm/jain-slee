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
 * Defines an interface representing the Extension-Diameter-Message command.
 * 
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * 
 * <pre>
 * 9.7.0.  Extension-Diameter-Message
 * 
 *     An implementation of DiameterMessage for extension messages--those not defined by the
 *     Diameter RA being used.
 * 
 *     It follows the same pattern as the standard message types, but with the DiameterCommand supplied
 *     by the user.
 * 
 *     The AVPs are exposed as the set of 'extension AVP's', the same way as exposed for messages
 *     which define a &quot;* [ AVP ]&quot; line in the BNF definition of the message.
 * 
 *     Message Format
 * 
 *       &lt;Extension-Diameter-Message&gt; ::= &lt; Diameter Header: 0, PXY &gt;
 *                  &lt; Session-Id &gt;
 *                  { Origin-Host }
 *                  { Origin-Realm }
 *                  { Destination-Host }
 *                  { Destination-Realm }
 *                * [ AVP ]
 * </pre>
 */
public interface ExtensionDiameterMessage extends DiameterMessage {

  int commandCode = 0;

}
