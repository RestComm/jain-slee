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
package org.mobicents.slee.resource.diameter.rf.events;

import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;
import net.java.slee.resource.diameter.base.events.avp.DiameterAvpCodes;
import net.java.slee.resource.diameter.rf.events.RfAccountingMessage;

import org.jdiameter.api.Message;
import org.mobicents.slee.resource.diameter.base.events.DiameterMessageImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class RfAccountingMessageImpl extends DiameterMessageImpl implements RfAccountingMessage {

  /**
   * @param message
   */
  public RfAccountingMessageImpl(Message message) {
    super(message);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#hasAccountingRecordType()
   */
  @Override
  public boolean hasAccountingRecordType() {
    return super.hasAvp(DiameterAvpCodes.ACCOUNTING_RECORD_TYPE);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#getAccountingRecordType()
   */
  @Override
  public AccountingRecordType getAccountingRecordType() {
    return (AccountingRecordType)super.getAvpAsEnumerated(DiameterAvpCodes.ACCOUNTING_RECORD_TYPE, AccountingRecordType.class);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#setAccountingRecordType(net.java.slee.resource.diameter.base.events.avp.AccountingRecordType)
   */
  @Override
  public void setAccountingRecordType(AccountingRecordType accountingRecordType) {
    super.addAvp(DiameterAvpCodes.ACCOUNTING_RECORD_TYPE,accountingRecordType.getValue());

  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#hasAccountingRecordNumber()
   */
  @Override
  public boolean hasAccountingRecordNumber() {
    return super.hasAvp(DiameterAvpCodes.ACCOUNTING_RECORD_NUMBER);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#getAccountingRecordNumber()
   */
  @Override
  public long getAccountingRecordNumber() {
    return super.getAvpAsUnsigned32(DiameterAvpCodes.ACCOUNTING_RECORD_NUMBER);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#setAccountingRecordNumber(long)
   */
  @Override
  public void setAccountingRecordNumber(long accountingRecordNumber) {
    super.addAvp(DiameterAvpCodes.ACCOUNTING_RECORD_NUMBER,accountingRecordNumber);

  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#hasAcctInterimInterval()
   */
  @Override
  public boolean hasAcctInterimInterval() {
    return super.hasAvp(DiameterAvpCodes.ACCT_INTERIM_INTERVAL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#getAcctInterimInterval()
   */
  @Override
  public long getAcctInterimInterval() {
    return super.getAvpAsUnsigned32(DiameterAvpCodes.ACCT_INTERIM_INTERVAL);
  }

  /* (non-Javadoc)
   * @see net.java.slee.resource.diameter.rf.events.RfMessage#setAcctInterimInterval(long)
   */
  @Override
  public void setAcctInterimInterval(long acctInterimInterval) {
    super.addAvp(DiameterAvpCodes.ACCT_INTERIM_INTERVAL,acctInterimInterval);
  }

}
