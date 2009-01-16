package org.mobicents.slee.runtime.cache.tests;

import javax.transaction.xa.Xid;

public class XidMockup implements Xid {

	public byte[] getBranchQualifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFormatId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getGlobalTransactionId() {
		// TODO Auto-generated method stub
		return null;
	}

}
