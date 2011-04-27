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

import net.java.slee.resource.diameter.base.events.avp.DisconnectCauseType;

/**
 * Defines an interface representing the Disconnect-Peer-Request command.
 * 
 * From the Diameter Base Protocol (rfc3588.txt) specification:
 * 
 * <pre>
 * 5.4.1.  Disconnect-Peer-Request
 * 
 *    The Disconnect-Peer-Request (DPR), indicated by the Command-Code set
 *    to 282 and the Command Flags' 'R' bit set, is sent to a peer to
 *    inform its intentions to shutdown the transport connection.  Upon
 *    detection of a transport failure, this message MUST NOT be sent to an
 *    alternate peer.
 * 
 *    Message Format
 * 
 *       &lt;Disconnect-Peer-Request&gt;  ::= &lt; Diameter Header: 282, REQ &gt;
 *                  { Origin-Host }
 *                  { Origin-Realm }
 *                  { Disconnect-Cause }
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface DisconnectPeerRequest extends DiameterMessage {

  static final int commandCode = 282;

  /**
   * Returns true if the Disconnect-Cause AVP is present in the message.
   */
  boolean hasDisconnectCause();

  /**
   * Returns the value of the Disconnect-Cause AVP, of type Enumerated.
   * 
   * @return the value of the Disconnect-Cause AVP or null if it has not been
   *         set on this message
   */
  DisconnectCauseType getDisconnectCause();

  /**
   * Sets the value of the Disconnect-Cause AVP, of type Enumerated.
   * 
   * @throws IllegalStateException
   *             if setDisconnectCause has already been called
   */
  void setDisconnectCause(DisconnectCauseType disconnectCause);

}
