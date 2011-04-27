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
 * Defines an interface representing the Time-Stamps grouped AVP type.<br>
 * <br>
 * From the Diameter Ro Reference Point Protocol Details (3GPP TS 32.299 V7.1.0) specification:
 * <pre>
 * 7.2.111 Time-Stamps AVP 
 * The Time-Stamps AVP (AVP code 833) is of type Grouped and holds the time of the initial SIP request and the time of
 * the response to the initial SIP Request. 
 * 
 * It has the following ABNF grammar: 
 *  Time-Stamps ::= AVP Header: 833 
 *      [ SIP-Request-Timestamp ] 
 *      [ SIP-Response-Timestamp ]
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface TimeStamps extends GroupedAvp {

  /**
   * Returns the value of the SIP-Request-Timestamp AVP, of type Time. A return value of null implies that the AVP has not been set.
   */
  abstract java.util.Date getSipRequestTimestamp();

  /**
   * Returns the value of the SIP-Response-Timestamp AVP, of type Time. A return value of null implies that the AVP has not been set.
   */
  abstract java.util.Date getSipResponseTimestamp();

  /**
   * Returns true if the SIP-Request-Timestamp AVP is present in the message.
   */
  abstract boolean hasSipRequestTimestamp();

  /**
   * Returns true if the SIP-Response-Timestamp AVP is present in the message.
   */
  abstract boolean hasSipResponseTimestamp();

  /**
   * Sets the value of the SIP-Request-Timestamp AVP, of type Time.
   */
  abstract void setSipRequestTimestamp(java.util.Date sipRequestTimestamp);

  /**
   * Sets the value of the SIP-Response-Timestamp AVP, of type Time.
   */
  abstract void setSipResponseTimestamp(java.util.Date sipResponseTimestamp);

}
