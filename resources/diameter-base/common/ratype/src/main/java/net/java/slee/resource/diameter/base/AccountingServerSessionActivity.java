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

package net.java.slee.resource.diameter.base;

import java.io.IOException;

import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.AccountingRequest;

/**
 * 
 * An AccountingServerSessionActivity represents an accounting session for Diameter accounting clients.
 * 
 * A single AccountingServerSessionActivity will be created for the Diameter session. All requests received 
 * for the session will be fired as events on the same AccountingServerSessionActivity. 
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface AccountingServerSessionActivity extends AccountingSessionActivity {

  /**
   * Create an Accounting-Answer with the Acct-Application-Id set to 3.
   * 
   * @return an Accounting-Answer
   */
  AccountingAnswer createAccountingAnswer();

  /**
   * Create an Accounting-Answer with some AVPs populated from the provided Accounting-Request.
   * 
   * The ACA will contain the AVPs specified in createAccountingAnswer() and the following AVPs from the Accounting-Request:
   * 
   * * Accounting-Record-Type
   * * Accounting-Record-Number
   * 
   * @param acr Accounting-Request to copy AVPs from
   * @return an Accounting-Answer
   */
  AccountingAnswer createAccountingAnswer(AccountingRequest acr);

  /**
   * Send an Accounting Answer. 
   * 
   * @param aca answer message to send
   * @throws IOException if the message could not be sent
   * @throws IllegalArgumentException if accountingAnswer is missing any required AVPs
   */
  void sendAccountingAnswer(AccountingAnswer aca) throws IOException, IllegalArgumentException;

}
