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
 * Defines an interface representing the address information grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.10 Globally-unique-address AVP
 * The Binding-information AVP (AVP Code 300) is of type Grouped AVP and is representing address information 
 * 
 * It has the following ABNF grammar: 
 *  Globally-unique-address ::= AVP Header: 300 13019
 *      [ Framed-IP-Address ]
 *      [ Framed-IPv6-Prefix ]
 *      [ Address-Realm ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface GloballyUniqueAddress extends GroupedAvp {

  /**
   * Returns the value of the Framed-IP-Address AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getFramedIPAddress();

  /**
   * Returns the value of the Framed-IPv6-Prefix AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getFramedIPV6Prefix();

  /**
   * Returns the value of the Address-Realm AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[] getAddressRealm();

  /**
   * Returns true if the Framed-IP-Address AVP is present in the message.
   */
  abstract boolean hasFramedIPAddress();

  /**
   * Returns true if the Framed-IPv6-Prefix AVP is present in the message.
   */
  abstract boolean hasFramedIPV6Prefix();

  /**
   * Returns true if the Address-Realm AVP is present in the message.
   */
  abstract boolean hasAddressRealm();

  /**
   * Sets the value of the Framed-IP-Address AVP, of type OctetString.
   */
  abstract void setFramedIPAddress(byte[] framedIPAddress);

  /**
   * Sets the value of the Framed-IPv6-Prefix AVP, of type OctetString.
   */
  abstract void setFramedIPV6Prefix(byte[] framedIPV6Prefix);

  /**
   * Sets the value of the Address-Realm AVP, of type OctetString.
   */
  abstract void setAddressRealm(byte[] addressRealm);
}
