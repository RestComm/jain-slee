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
 * <pre><b>8.6. Check-Balance-Result AVP</b>
 *   The Check Balance Result AVP (AVP Code 422) is of type Enumerated and
 *   contains the result of the balance check.  This AVP is applicable
 *   only when the Requested-Action AVP indicates CHECK_BALANCE in the
 *   Credit-Control-Request command.
 *
 *   The following values are defined for the Check-Balance-Result AVP.
 *
 *   ENOUGH_CREDIT                   0
 *      There is enough credit in the account to cover the requested
 *      service.
 *
 *   NO_CREDIT                       1
 *      There isn't enough credit in the account to cover the requested
 *      service.</pre>
 *      
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public enum CheckBalanceResultType implements Enumerated {

  ENOUGH_CREDIT(0),NO_CREDIT(1);

  private int value = -1;

  private CheckBalanceResultType(int val) {
    this.value = val;
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

  public static CheckBalanceResultType fromInt(int type) throws IllegalArgumentException
  {
    switch (type) {
    case 0:
      return ENOUGH_CREDIT;
    case 1:
      return NO_CREDIT;

    default:
      throw new IllegalArgumentException();
    }
  }

}
