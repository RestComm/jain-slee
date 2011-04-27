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

package net.java.slee.resource.diameter.cca.events.avp;

import java.io.StreamCorruptedException;

import net.java.slee.resource.diameter.base.events.avp.Enumerated;

/**
 *<pre> <b>8.41. Requested-Action AVP</b>
 *
 *
 *   The Requested-Action AVP (AVP Code 436) is of type Enumerated and
 *   contains the requested action being sent by Credit-Control-Request
 *   command where the CC-Request-Type is set to EVENT_REQUEST.  The
 *   following values are defined for the Requested-Action AVP:
 *
 *   <b>DIRECT_DEBITING                 0</b>
 *      This indicates a request to decrease the end user's account
 *      according to information specified in the Requested-Service-Unit
 *      AVP and/or Service-Identifier AVP (additional rating information
 *      may be included in service-specific AVPs or in the Service-
 *      Parameter-Info AVP).  The Granted-Service-Unit AVP in the Credit-
 *      Control-Answer command contains the debited units.
 *
 *   <b>REFUND_ACCOUNT                  1</b>
 *      This indicates a request to increase the end user's account
 *      according to information specified in the Requested-Service-Unit
 *      AVP and/or Service-Identifier AVP (additional rating information
 *      may be included in service-specific AVPs or in the Service-
 *      Parameter-Info AVP).  The Granted-Service-Unit AVP in the Credit-
 *      Control-Answer command contains the refunded units.
 *
 *   <b>CHECK_BALANCE                   2</b>
 *      This indicates a balance check request.  In this case, the
 *      checking of the account balance is done without any credit
 *      reservation from the account.  The Check-Balance-Result AVP in the
 *      Credit-Control-Answer command contains the result of the balance
 *      check.
 *
 *   <b>PRICE_ENQUIRY                   3</b>
 *      This indicates a price enquiry request.  In this case, neither
 *      checking of the account balance nor reservation from the account
 *      will be done; only the price of the service will be returned in
 *      the Cost-Information AVP in the Credit-Control-Answer Command.
 *      </pre>
 *  
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum RequestedActionType implements Enumerated {

  DIRECT_DEBITING(0), REFUND_ACCOUNT(1), CHECK_BALANCE(2), PRICE_ENQUIRY(3);

  private int value = -1;

  private RequestedActionType(int value) {
    this.value = value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public static RequestedActionType fromInt(int type) throws IllegalArgumentException
  {
    switch (type) {
    case 0:
      return DIRECT_DEBITING;
    case 1:
      return REFUND_ACCOUNT;
    case 2:
      return CHECK_BALANCE;
    case 3:
      return PRICE_ENQUIRY;

    default:
      throw new IllegalArgumentException();
    }
  }

  public int getValue() {
    return this.value;
  }

}
