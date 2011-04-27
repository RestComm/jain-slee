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

package net.java.slee.resource.diameter.ro.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * Defines an interface representing the Event-Type grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification: 
 * <pre>
 * 7.2.32 Event-Type AVP 
 * 
 * The Event-Type AVP (AVP code 823) is of type Grouped and contains information about the type of chargeable
 * telecommunication service/event for which the accounting-request message is generated.
 * 
 * It has the following ABNF grammar: 
 *  Event-Type ::= AVP Header: 823 
 *      [ SIP-Method ] 
 *      [ Event ] 
 *      [ Expires ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface EventType extends GroupedAvp {

  /**
   * Returns the value of the Event AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getEvent();

  /**
   * Returns the value of the Expires AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getExpires();

  /**
   * Returns the value of the SIP-Method AVP, of type UTF8String. A return value of null implies that the AVP has not been set.
   */
  abstract String getSipMethod();

  /**
   * Returns true if the Event AVP is present in the message.
   */
  abstract boolean hasEvent();

  /**
   * Returns true if the Expires AVP is present in the message.
   */
  abstract boolean hasExpires();

  /**
   * Returns true if the SIP-Method AVP is present in the message.
   */
  abstract boolean hasSipMethod();

  /**
   * Sets the value of the Event AVP, of type UTF8String.
   */
  abstract void setEvent(String event);

  /**
   * Sets the value of the Expires AVP, of type Unsigned32.
   */
  abstract void setExpires(long expires);

  /**
   * Sets the value of the SIP-Method AVP, of type UTF8String.
   */
  abstract void setSipMethod(String sipMethod);

}
