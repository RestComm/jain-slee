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

package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.AccountingRequest;

import org.jdiameter.api.Message;

/**
 * Implementation of {@link AccountingRequest}
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author Erick Svenson
 * @see AccountingMessageImpl
 */
public class AccountingRequestImpl extends AccountingMessageImpl implements AccountingRequest {

  public AccountingRequestImpl(Message message) {
    super(message);
  }

  @Override
  public String getLongName() {
    return "Accounting-Request";
  }

  @Override
  public String getShortName() {
    return "ACR";
  }

  public boolean isValid() {
    // One of Acct-Application-Id and Vendor-Specific-Application-Id AVPs
    // MUST be present. If the Vendor-Specific-Application-Id grouped AVP
    // is present, it must have an Acct-Application-Id inside.

    if (!this.message.isRequest()) {
      return false;
    }
    else if (!this.hasAccountingRealtimeRequired()) {
      if (!this.hasVendorSpecificApplicationId()) {
        return false;
      }
      else {
        if (this.getVendorSpecificApplicationId().getAcctApplicationId() == -1) {
          return false;
        }
      }
    }

    return true;
  }

}
