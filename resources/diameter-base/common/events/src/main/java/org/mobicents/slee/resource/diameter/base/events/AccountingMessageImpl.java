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

import net.java.slee.resource.diameter.base.events.AccountingMessage;
import net.java.slee.resource.diameter.base.events.avp.AccountingRealtimeRequiredType;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

/**
 * Super class for ACX messages Super class implementing methods for ACR and
 * ACA. it implements methods from {@link AccountingMessage}
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @see DiameterMessageImpl
 */
public abstract class AccountingMessageImpl extends DiameterMessageImpl implements AccountingMessage {

  /**
   * 
   * @param message
   */
  public AccountingMessageImpl(Message message) {
    super(message);
  }

  public boolean hasAccountingRecordType() {
    return hasAvp(Avp.ACC_RECORD_TYPE);
  }

  public AccountingRecordType getAccountingRecordType() {
    return (AccountingRecordType) getAvpAsEnumerated(Avp.ACC_RECORD_TYPE, AccountingRecordType.class);
  }

  public void setAccountingRecordType(AccountingRecordType accountingRecordType) {
    addAvp(Avp.ACC_RECORD_TYPE, accountingRecordType.getValue());
  }

  public boolean hasAccountingRecordNumber() {
    return hasAvp(Avp.ACC_RECORD_NUMBER);
  }

  public long getAccountingRecordNumber() {
    return getAvpAsUnsigned32(Avp.ACC_RECORD_NUMBER);
  }

  public void setAccountingRecordNumber(long accountingRecordNumber) {
    addAvp(Avp.ACC_RECORD_NUMBER, accountingRecordNumber);
  }

  public boolean hasAccountingSubSessionId() {
    return hasAvp(Avp.ACC_SUB_SESSION_ID);
  }

  public long getAccountingSubSessionId() {
    return getAvpAsUnsigned64(Avp.ACC_SUB_SESSION_ID);
  }

  public void setAccountingSubSessionId(long accountingSubSessionId) {
    addAvp(Avp.ACC_SUB_SESSION_ID, accountingSubSessionId);
  }

  public boolean hasAccountingSessionId() {
    return super.hasAvp(Avp.ACC_SESSION_ID);
  }

  public byte[] getAccountingSessionId() {
    return getAvpAsOctetString(Avp.ACC_SESSION_ID);
  }

  public void setAccountingSessionId(byte[] accountingSessionId) {
    addAvp(Avp.ACC_SESSION_ID, accountingSessionId);
  }

  public boolean hasAcctMultiSessionId() {
    return hasAvp(Avp.ACC_MULTI_SESSION_ID);
  }

  public String getAcctMultiSessionId() {
    return getAvpAsUTF8String(Avp.ACC_MULTI_SESSION_ID);
  }

  public void setAcctMultiSessionId(String acctMultiSessionId) {
    addAvp(Avp.ACC_MULTI_SESSION_ID, acctMultiSessionId);
  }

  public boolean hasAcctInterimInterval() {
    return hasAvp(Avp.ACCT_INTERIM_INTERVAL);
  }

  public long getAcctInterimInterval() {
    return getAvpAsUnsigned32(Avp.ACCT_INTERIM_INTERVAL);
  }

  public void setAcctInterimInterval(long acctInterimInterval) {
    addAvp(Avp.ACCT_INTERIM_INTERVAL, acctInterimInterval);
  }

  public boolean hasAccountingRealtimeRequired() {
    return hasAvp(Avp.ACCOUNTING_REALTIME_REQUIRED);
  }

  public AccountingRealtimeRequiredType getAccountingRealtimeRequired() {
    return (AccountingRealtimeRequiredType) getAvpAsEnumerated(Avp.ACCOUNTING_REALTIME_REQUIRED, AccountingRealtimeRequiredType.class);
  }

  public void setAccountingRealtimeRequired(AccountingRealtimeRequiredType accountingRealtimeRequired) {
    addAvp(Avp.ACCOUNTING_REALTIME_REQUIRED, accountingRealtimeRequired.getValue());
  }

}
