package org.mobicents.slee.resource.diameter.base.events;

import java.util.Date;

import org.jdiameter.api.Avp;
import org.jdiameter.api.Message;

import net.java.slee.resource.diameter.base.events.AccountingAnswer;
import net.java.slee.resource.diameter.base.events.DiameterCommand;
import net.java.slee.resource.diameter.base.events.DiameterHeader;
import net.java.slee.resource.diameter.base.events.avp.AccountingRealtimeRequiredType;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;

import net.java.slee.resource.diameter.base.events.avp.AvpNotAllowedException;
import net.java.slee.resource.diameter.base.events.avp.DiameterIdentityAvp;
import net.java.slee.resource.diameter.base.events.avp.ProxyInfoAvp;
import net.java.slee.resource.diameter.base.events.avp.VendorSpecificApplicationIdAvp;

public class AccountingAnswerImpl extends ExtensionDiameterMessageImpl implements AccountingAnswer
{

	
	public AccountingAnswerImpl(Message message) {
        super(message);
    }

    public boolean hasAccountingRecordType() {
        return message.getAvps().getAvp(Avp.ACC_RECORD_TYPE) != null;
    }

    public AccountingRecordType getAccountingRecordType() {
        return AccountingRecordType.fromInt(getAvpAsInt32(Avp.ACC_RECORD_TYPE));
    }

    public void setAccountingRecordType(AccountingRecordType accountingRecordType) {
        setAvpAsInt32(Avp.ACC_RECORD_TYPE, accountingRecordType.getValue(), true);
    }

    public boolean hasAccountingRecordNumber() {
        return message.getAvps().getAvp(Avp.ACC_RECORD_NUMBER) != null;
    }

    public long getAccountingRecordNumber() {
        return getAvpAsUInt32(Avp.ACC_RECORD_NUMBER);
    }

    public void setAccountingRecordNumber(long accountingRecordNumber) {
        setAvpAsUInt32(Avp.ACC_RECORD_NUMBER, accountingRecordNumber, true);
    }

    public boolean hasAccountingSubSessionId() {
        return message.getAvps().getAvp(Avp.ACC_SUB_SESSION_ID) != null;
    }

    public long getAccountingSubSessionId() {
        return getAvpAsUInt32(Avp.ACC_SUB_SESSION_ID);
    }

    public void setAccountingSubSessionId(long accountingSubSessionId) {
        setAvpAsUInt32(Avp.ACC_SUB_SESSION_ID, accountingSubSessionId, true);
    }

    public boolean hasAccountingSessionId() {
        return false;  // todo unknown
    }

    public byte[] getAccountingSessionId() {
        return new byte[0];  // todo unknown
    }

    public void setAccountingSessionId(byte[] accountingSessionId) {
        // todo unknown
    }

    public boolean hasAcctMultiSessionId() {
        return message.getAvps().getAvp(Avp.ACC_MULTI_SESSION_ID) != null;
    }

    public String getAcctMultiSessionId() {
        return getAvpAsUtf8(Avp.ACC_MULTI_SESSION_ID);
    }

    public void setAcctMultiSessionId(String acctMultiSessionId) {
        setAvpAsUtf8(Avp.ACC_MULTI_SESSION_ID, acctMultiSessionId, true);
    }

    public boolean hasAcctInterimInterval() {
        return message.getAvps().getAvp(Avp.ACCT_INTERIM_INTERVAL) != null;
    }

    public long getAcctInterimInterval() {
        return getAvpAsUInt32(Avp.ACCT_INTERIM_INTERVAL);
    }

    public void setAcctInterimInterval(long acctInterimInterval) {
        setAvpAsUInt32(Avp.ACCT_INTERIM_INTERVAL, acctInterimInterval, true);
    }

    public boolean hasAccountingRealtimeRequired() {
        return message.getAvps().getAvp(Avp.ACCOUNTING_REALTIME_REQUIRED) != null;
    }

    public AccountingRealtimeRequiredType getAccountingRealtimeRequired() {
        return AccountingRealtimeRequiredType.fromInt(getAvpAsInt32(Avp.ACCOUNTING_REALTIME_REQUIRED));
    }

    public void setAccountingRealtimeRequired(AccountingRealtimeRequiredType accountingRealtimeRequired) {
        setAvpAsInt32(Avp.ACCOUNTING_REALTIME_REQUIRED, accountingRealtimeRequired.getValue(), true);
    }
	
	@Override
	public String getLongName() {
		
		return "Accounting-Answer";
	}

	@Override
	public String getShortName() {

		return "ACA";
	}

	

}
