package org.mobicents.slee.resource.sip11.wrappers;

import javax.sip.Transaction;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public abstract class SuperTransactionWrapper implements WrapperSuperInterface{

	protected SipActivityHandle sipActivityHandle;
	protected Transaction wrappedTransaction;
	protected boolean isActivity=false;
	public Object getApplicationData() {
		throw new SecurityException();
	}

	public void setApplicationData(Object arg0) {
		throw new SecurityException();
	}
	
	public SipActivityHandle getActivityHandle() {
		return this.sipActivityHandle;
	}

	public Transaction getWrappedTransaction() {
		return wrappedTransaction;
	}

	public boolean isActivity() {
		
		return this.isActivity;
	}

	public void setActivityFlag() {
		this.isActivity=true;
		
	}
	
	
}
