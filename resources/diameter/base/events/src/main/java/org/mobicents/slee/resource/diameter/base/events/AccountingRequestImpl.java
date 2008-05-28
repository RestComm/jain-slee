package org.mobicents.slee.resource.diameter.base.events;

import net.java.slee.resource.diameter.base.events.AccountingRequest;
import net.java.slee.resource.diameter.base.events.avp.AccountingRealtimeRequiredType;
import net.java.slee.resource.diameter.base.events.avp.AccountingRecordType;

import org.jdiameter.api.Message;

public class AccountingRequestImpl extends AccountingAnswerImpl implements AccountingRequest
{

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

	
 
}
