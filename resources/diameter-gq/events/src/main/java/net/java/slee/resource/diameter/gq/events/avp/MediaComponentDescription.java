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
 * Defines an interface representing the service information for a single media component with an AF session grouped AVP type.<br>
 * <br>
 * From the Diameter Gq' Reference Point Protocol Details (ETSI TS 183.017 V2.3.1) specification:
 * 
 * <pre>
 * 7.3.26 Media-Component-Description AVP
 * The Binding-input-list AVP (AVP Code 517) is of type Grouped AVP and contains service information
 * for a single media component within an AF session 
 * 
 * It has the following ABNF grammar: 
 *  Media-Component-Description ::= AVP Header: 517 13019
 *        [ Media-Component-Number ]
 *      * [ Media-Sub-Component ]
 *        [ AF-Application-Identifier ]
 *        [ Media-Type ]
 *        [ Max-Request-Bandwidth-UL ]      
 *        [ Max-Request-Bandwidth-DL ]
 *        [ Flow-Status ]
 *        [ RS-Bandwidth ]
 *        [ RR-Bandwidth ]
 *        [ Reservation-Class ]
 *        [ Reservation-Priority ]
 *        [ Transport-Class ]
 *      * [ Codec-Data ]
 *      * [ Media-Authorization-Context-Id ]
 * </pre>
 * 
 * @author <a href="mailto:webdev@web-ukraine.info"> Yulian Oifa </a>
 */
public interface MediaComponentDescription extends GroupedAvp {

  /**
   * Returns the value of the Media-Component-Number AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getMediaComponentNumber();

  /**
   * Returns the value of the Media-Sub-Component AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MediaSubComponent[] getMediaSubComponents();

  /**
   * Returns the value of the AF-Application-Identifier AVP, of type OctetString. A return value of null implies that the AVP has not been
   * set.
   */
  abstract byte[] getAFApplicationIdentifier();

  /**
   * Returns the value of the Media-Type AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract MediaType getMediaType();

  /**
   * Returns the value of the Max-Requested-Bandwidth-UL AVP, of type Unsigned32. A return value of null implies that the AVP has not been
   * set.
   */
  abstract long getMaxRequestedBandwidthUL();

  /**
   * Returns the value of the Max-Requested-Bandwidth-DL AVP, of type Unsigned32. A return value of null implies that the AVP has not been
   * set.
   */
  abstract long getMaxRequestedBandwidthDL();

  /**
   * Returns the value of the Flow-Status AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract FlowStatus getFlowStatus();

  /**
   * Returns the value of the RR-Bandwidth AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getRRBandwidth();

  /**
   * Returns the value of the RS-Bandwidth AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getRSBandwidth();

  /**
   * Returns the value of the Reservation-Class AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getReservationClass();

  /**
   * Returns the value of the Reservation-Priority AVP, of type Grouped. A return value of null implies that the AVP has not been set.
   */
  abstract ReservationPriority getReservationPriority();

  /**
   * Returns the value of the Transport-Class AVP, of type Unsigned32. A return value of null implies that the AVP has not been set.
   */
  abstract long getTransportClass();

  /**
   * Returns the value of the Codec-Data AVP, of type OctetString. A return value of null implies that the AVP has not been set.
   */
  abstract byte[][] getCodecData();

  /**
   * Returns the value of the Media-Authorization-Context-Id AVP, of type UTF8String. A return value of null implies that the AVP has not
   * been set.
   */
  abstract String[] getMediaAuthorizationContextId();

  /**
   * Returns true if the Media-Component-Number AVP is present in the message.
   */
  abstract boolean hasMediaComponentNumber();

  /**
   * Returns true if the AF-Application-Identifier AVP is present in the message.
   */
  abstract boolean hasAFApplicationIdentifier();

  /**
   * Returns true if the Media-Type AVP is present in the message.
   */
  abstract boolean hasMediaType();

  /**
   * Returns true if the Max-Requested-Bandwidth-UL AVP is present in the message.
   */
  abstract boolean hasMaxRequestedBandwidthUL();

  /**
   * Returns true if the Max-Requested-Bandwidth-DL AVP is present in the message.
   */
  abstract boolean hasMaxRequestedBandwidthDL();

  /**
   * Returns true if the Flow-Status AVP is present in the message.
   */
  abstract boolean hasFlowStatus();

  /**
   * Returns true if the RR-Bandwidth AVP is present in the message.
   */
  abstract boolean hasRRBandwidth();

  /**
   * Returns true if the RS-Bandwidth AVP is present in the message.
   */
  abstract boolean hasRSBandwidth();

  /**
   * Returns true if the Reservation-Class AVP is present in the message.
   */
  abstract boolean hasReservationClass();

  /**
   * Returns true if the Reservation-Priority AVP is present in the message.
   */
  abstract boolean hasReservationPriority();

  /**
   * Returns true if the Transport-Class AVP is present in the message.
   */
  abstract boolean hasTransportClass();

  /**
   * Sets the value of the Media-Component-Number AVP, of type Unsigned32.
   */
  abstract void setMediaComponentNumber(long mediaComponentNumber);

  /**
   * Sets the value of the Media-Sub-Component AVP, of type Grouped.
   */
  abstract void setMediaSubComponents(MediaSubComponent[] mediaSubComponent);

  /**
   * Sets the value of the Media-Sub-Component AVP, of type Grouped.
   */
  abstract void setMediaSubComponent(MediaSubComponent mediaSubComponent);

  /**
   * Sets the value of the AF-Application-Identifier AVP, of type OctetString.
   */
  abstract void setAFApplicationIdentifier(byte[] AFApplicationIdentifier);

  /**
   * Sets the value of the Media-Type AVP, of type Unsigned32.
   */
  abstract void setMediaType(MediaType mediaType);

  /**
   * Sets the value of the Max-Requested-Bandwidth-UL AVP, of type Unsigned32.
   */
  abstract void setMaxRequestedBandwidthUL(long maxRequestedBandwidthUL);

  /**
   * Sets the value of the Max-Requested-Bandwidth-DL AVP, of type Unsigned32.
   */
  abstract void setMaxRequestedBandwidthDL(long maxRequestedBandwidthDL);

  /**
   * Sets the value of the Flow-Status AVP, of type Unsigned32.
   */
  abstract void setFlowStatus(FlowStatus flowStatus);

  /**
   * Sets the value of the RS-Bandwidth AVP, of type Unsigned32.
   */
  abstract void setRSBandwidth(long RSBandwidth);

  /**
   * Sets the value of the RR-Bandwidth AVP, of type Unsigned32.
   */
  abstract void setRRBandwidth(long RRBandwidth);

  /**
   * Sets the value of the Reservation-Class AVP, of type Unsigned32.
   */
  abstract void setReservationClass(long reservationClass);

  /**
   * Sets the value of the Reservation-Priority AVP, of type Unsigned32.
   */
  abstract void setReservationPriority(ReservationPriority reservationPriority);

  /**
   * Sets the value of the Transport-Class AVP, of type Unsigned32.
   */
  abstract void setTransportClass(long transportClass);

  /**
   * Sets the value of the Codec-Data AVP, of type OctetString.
   */
  abstract void setCodecData(byte[][] codecsData);

  /**
   * Sets the value of the Codec-Data AVP, of type OctetString.
   */
  abstract void setCodecData(byte[] codecData);

  /**
   * Sets the value of the Media-Authorization-Context-Id AVP, of type UTF8String.
   */
  abstract void setMediaAuthorizationContextId(String[] mediaAuthorizationContextIds);

  /**
   * Sets the value of the Media-Authorization-Context-Id, of type UTF8String.
   */
  abstract void setMediaAuthorizationContextId(String mediaAuthorizationContextId);
}
