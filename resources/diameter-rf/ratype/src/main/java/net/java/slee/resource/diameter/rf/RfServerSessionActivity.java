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

import net.java.slee.resource.diameter.rf.events.RfAccountingAnswer;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

/**
 * An RfServerSessionActivity represents an offline charging session for accounting servers.
 * 
 * A single RfServerSessionActivity will be created for the Diameter session. All requests received
 * for the session will be fired as events on the same RfServerSessionActivity.
 *
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfServerSessionActivity extends RfSessionActivity {

  /**
   * Create an Accounting-Answer with the Acct-Application-Id set to 3.
   *
   * @return an Accounting-Answer
   */
  public RfAccountingAnswer createRfAccountingAnswer();

  /**
   * Create an Accounting-Answer with some AVPs populated from the provided Accounting-Request.
   * 
   * The ACA will contain the AVPs specified in createRfAccountingAnswer() and the following AVPs from the Accounting-Request:
   * <ul>Accounting-Record-Type</ul>
   * <ul>Accounting-Record-Number</ul>
   * 
   * @param acr Accounting-Request to copy AVPs from
   * @return an Accounting-Answer
   */
  public RfAccountingAnswer createRfAccountingAnswer(RfAccountingRequest acr);

  /**
   * Send an Accounting Answer.
   * 
   * @param accountingAnswer answer message to send
   * @throws IOException if the message could not be sent 
   * @throws IllegalArgumentException if accountingAnswer is missing any required AVPs
   */
  public void sendRfAccountingAnswer(RfAccountingAnswer accountingAnswer) throws IOException, IllegalArgumentException;

}
