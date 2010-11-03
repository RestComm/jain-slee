/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package net.java.slee.resource.diameter.rf;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

/**
 * An RfClientSessionActivity represents an offline charging session for
 * accounting clients.
 * 
 * All requests for the session must be sent via the same
 * RfClientSessionActivity.
 * 
 * All responses related to the session will be received as events fired on the
 * same RfClientSessionActivity.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfClientSessionActivity extends RfSessionActivity {

  /**
   * Send an Accounting Request.
   * 
   * @param accountingRequest
   *            request message to send
   * @throws IOException
   *             if the message could not be sent
   * @throws IllegalArgumentException
   *             if accountingRequest is missing any required AVPs
   */
  public void sendRfAccountingRequest(RfAccountingRequest accountingRequest) throws IOException, IllegalArgumentException;

  /**
   * Create an Accounting-Request message populated with the following AVPs:
   * 
   * * Accounting-Record-Type: as per accountingRecordType parameter *
   * Acct-Application-Id: the value 3 as specified by RFC3588
   * 
   * @param accountingRecordType
   *            value for the Accounting-Record-Type AVP
   * @return a new AccountingRequest
   */
  RfAccountingRequest createRfAccountingRequest(AccountingRecordType accountingRecordType);

}
