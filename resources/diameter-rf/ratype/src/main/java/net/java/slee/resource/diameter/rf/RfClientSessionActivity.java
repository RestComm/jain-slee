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
