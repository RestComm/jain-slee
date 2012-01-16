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

package net.java.slee.resource.diameter.gq.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the V4 transport address grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.5 V4-Transport-address AVP
 * The V4-Transport-address AVP (AVP Code 454) is of type Grouped AVP and holds information about a single IPv4 address and a single
 * port number 
 * 
 * It has the following ABNF grammar: 
 *  V4-Transport-Address ::= AVP Header: 454 13019
 *      [ Framed-IP-Address ] AVP Code 8
 *      [ Port-Number ] AVP Code 455
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface V4TransportAddress extends GroupedAvp {

  /**
   * Returns the value of the Framed-IP-Address AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getFramedIPAddress();

  /**
   * Returns the value of the Port-Number AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getPortNumber();

  /**
   * Returns true if the Framed-IP-Address AVP is present in the message.
   */
  abstract boolean hasFramedIPAddress();

  /**
   * Returns true if the Port-Number AVP is present in the message.
   */
  abstract boolean hasPortNumber();

  /**
   * Sets the value of the Framed-IP-Address AVP, of type OctetString.
   */
  abstract void setFramedIPAddress(byte[] framedIPAddress);

  /**
   * Sets the value of the Port-Number AVP, of type Unsigned32.
   */
  abstract void setPortNumber(long portNumber);
}
