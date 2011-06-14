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
 * Defines an interface representing the transport addresses list grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V1.4.0) specification:
 * 
 * <pre>
 * 7.3.2 Binding-input-list AVP
 * The Binding-input-list AVP (AVP Code 451) is of type Grouped AVP and holds a list transport addresses for which binding is requested
 * 
 * 
 * It has the following ABNF grammar: 
 *  Binding-input-list ::= AVP Header: 451 13019
 *      [ V6-Transport-Address ]
 *      [ V4-Transport-Address ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface BindingInputList extends GroupedAvp {

  /**
   * Returns the value of the V4-Transport-Address AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract V4TransportAddress[] getV4TransportAddress();

  /**
   * Returns the value of the V6-Transport-Address AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract V6TransportAddress[] getV6TransportAddress();

  /**
   * Sets the value of the V4-Transport-Address AVP, of type Grouped.
   */
  abstract void setV4TransportAddress(V4TransportAddress v4TransportAddress);

  /**
   * Sets the value of the V4-Transport-Address AVP, of type Grouped.
   */
  abstract void setV4TransportAddresses(V4TransportAddress[] v4TransportAddress);

  /**
   * Sets the value of the V6-Transport-Address AVP, of type Grouped.
   */
  abstract void setV6TransportAddress(V6TransportAddress v6TransportAddress);

  /**
   * Sets the value of the V6-Transport-Address AVP, of type Grouped.
   */
  abstract void setV6TransportAddresses(V6TransportAddress[] v6TransportAddress);
}
