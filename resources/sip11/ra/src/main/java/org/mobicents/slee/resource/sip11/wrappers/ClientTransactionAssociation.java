package org.mobicents.slee.resource.sip11.wrappers;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ClientTransactionAssociation {

	private SipActivityHandle associationHandle;
	private String associatedTransactionBranchId;
	
	public ClientTransactionAssociation(SipActivityHandle associationHandle,
			String associatedTransactionBranchId) {
		this.associationHandle = associationHandle;
		this.associatedTransactionBranchId = associatedTransactionBranchId;
	}

	public SipActivityHandle getAssociationHandle() {
		return associationHandle;
	}

	public String getAssociatedTransactionBranchId() {
		return associatedTransactionBranchId;
	}
	
}
