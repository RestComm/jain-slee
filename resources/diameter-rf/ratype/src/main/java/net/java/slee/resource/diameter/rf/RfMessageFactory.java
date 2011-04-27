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

import net.java.slee.resource.diameter.base.DiameterMessageFactory;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.rf.events.RfAccountingRequest;

/**
 * Used by applications to create Diameter Rf request messages.
 * 
 * Rf answer messages can be created using the
 * RfServerSessionActivity.createRfAccountingAnswer() methods.
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public interface RfMessageFactory {

  public static final long _RF_TGPP_VENDOR_ID = 10415L;
  public static final long _RF_ACC_APP_ID = 3;

  /**
   * Get a factory to create AVPs and messages defined by Diameter Base.
   * 
   * @return
   */
  public DiameterMessageFactory getBaseMessageFactory();

  /**
   * Creates an Accounting Request message with the Accounting-Record-Type AVP set.
   * 
   * @param accountingRecordType
   * @return
   */
  public RfAccountingRequest createRfAccountingRequest(AccountingRecordType accountingRecordType);

  /**
   * Creates an Accounting Request message with the Accounting-Record-Type AVP set.
   * 
   * @param accountingRecordType
   * @param sessionId
   * @return
   */
  public RfAccountingRequest createRfAccountingRequest(String sessionId, AccountingRecordType accountingRecordType);
}
