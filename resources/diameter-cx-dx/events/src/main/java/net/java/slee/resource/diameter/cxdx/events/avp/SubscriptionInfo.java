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

package net.java.slee.resource.diameter.cxdx.events.avp;

import net.java.slee.resource.diameter.base.events.avp.GroupedAvp;

/**
 * <pre>
 * <b>6.3.49  Subscription-Info AVP</b>
 * The Subscription-Info AVP is of type Grouped and it contains the UE�s subscription information.
 * The Contact AVP contains the Contact Address and Parameters in the Contact header of the
 * subscription request.
 * 
 * AVP format
 * Subscription-Info ::= < AVP Header: 642, 10415>
 *                   { Call-ID-SIP-Header }
 *                   { From-SIP-Header }
 *                   { To-SIP-Header }
 *                   { Record-Route }
 *                   { Contact }
 *                  *[ AVP ] 
 *
 * </pre>
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface SubscriptionInfo extends GroupedAvp {

  /**
   * Returns true if the Call-ID-SIP-Header AVP is present in the message.
   */
  boolean hasCallIDSIPHeader();

  /**
   * Returns the value of the Call-ID-SIP-Header AVP, of type OctetString.
   * @return the value of the Call-ID-SIP-Header AVP or null if it has not been set on this message
   */
  byte[] getCallIDSIPHeader();

  /**
   * Sets the value of the Call-ID-SIP-Header AVP, of type OctetString.
   * @throws IllegalStateException if setCallIDSIPHeader has already been called
   */
  void setCallIDSIPHeader(byte[] callIDSIPHeader);

  /**
   * Returns true if the From-SIP-Header AVP is present in the message.
   */
  boolean hasFromSIPHeader();

  /**
   * Returns the value of the From-SIP-Header AVP, of type OctetString.
   * @return the value of the From-SIP-Header AVP or null if it has not been set on this message
   */
  byte[] getFromSIPHeader();

  /**
   * Sets the value of the From-SIP-Header AVP, of type OctetString.
   * @throws IllegalStateException if setFromSIPHeader has already been called
   */
  void setFromSIPHeader(byte[] fromSIPHeader);

  /**
   * Returns true if the To-SIP-Header AVP is present in the message.
   */
  boolean hasToSIPHeader();

  /**
   * Returns the value of the To-SIP-Header AVP, of type OctetString.
   * @return the value of the To-SIP-Header AVP or null if it has not been set on this message
   */
  byte[] getToSIPHeader();

  /**
   * Sets the value of the To-SIP-Header AVP, of type OctetString.
   * @throws IllegalStateException if setToSIPHeader has already been called
   */
  void setToSIPHeader(byte[] toSIPHeader);

  /**
   * Returns true if the Record-Route AVP is present in the message.
   */
  boolean hasRecordRoute();

  /**
   * Returns the value of the Record-Route AVP, of type OctetString.
   * @return the value of the Record-Route AVP or null if it has not been set on this message
   */
  byte[] getRecordRoute();

  /**
   * Sets the value of the Record-Route AVP, of type OctetString.
   * @throws IllegalStateException if setRecordRoute has already been called
   */
  void setRecordRoute(byte[] recordRoute);

  /**
   * Returns true if the Contact AVP is present in the message.
   */
  boolean hasContact();

  /**
   * Returns the value of the Contact AVP, of type OctetString.
   * @return the value of the Contact AVP or null if it has not been set on this message
   */
  byte[] getContact();

  /**
   * Sets the value of the Contact AVP, of type OctetString.
   * @throws IllegalStateException if setContact has already been called
   */
  void setContact(byte[] contact);

}
