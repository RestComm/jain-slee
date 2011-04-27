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
 *<pre> <b>8.13. Credit-Control AVP</b>
 *
 *
 *   The Credit-Control AVP (AVP Code 426) is of type Enumerated and MUST
 *   be included in AA requests when the service element has credit-
 *   control capabilities.
 *
 *   <b>CREDIT_AUTHORIZATION            0</b>
 *      If the home Diameter AAA server determines that the user has
 *      prepaid subscription, this value indicates that the credit-control
 *      server MUST be contacted to perform the first interrogation.  The
 *      value of the Credit-Control AVP MUST always be set to 0 in an AA
 *      request sent to perform the first interrogation and to initiate a
 *      new credit-control session.
 *
 *   <b>RE_AUTHORIZATION                1</b>
 *      This value indicates to the Diameter AAA server that a credit-
 *      control session is ongoing for the subscriber and that the
 *      credit-control server MUST not be contacted.  The Credit-Control
 *      AVP set to the value of 1 is to be used only when the first
 *      interrogation has been successfully performed and the credit-
 *      control session is ongoing (i.e., re-authorization triggered by
 *      Authorization-Lifetime).  This value MUST NOT be used in an AA
 *      request sent to perform the first interrogation.
 *
 *	<pre>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum CreditControlType implements Enumerated {
  CREDIT_AUTHORIZATION(0), RE_AUTHORIZATION(1);

  private int value = -1;

  private CreditControlType(int value) {
    this.value = value;
  }

  public int getValue() {
    return this.value;
  }

  private Object readResolve() throws StreamCorruptedException {
    try {
      return fromInt(value);
    }
    catch (IllegalArgumentException iae) {
      throw new StreamCorruptedException("Invalid internal state found: " + value);
    }
  }

  public CreditControlType fromInt(int type) throws IllegalArgumentException {

    switch (type) {
    case 0:
      return CREDIT_AUTHORIZATION;
    case 1:
      return RE_AUTHORIZATION;

    default:
      throw new IllegalArgumentException();

    }
  }
}
