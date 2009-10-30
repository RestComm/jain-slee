package org.mobicents.slee.resource.sip11.wrappers;

import java.io.Serializable;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ClientTransactionAssociation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SipActivityHandle dialogActivityHandle;
	private final String associatedServerTransactionBranchId;
	
	public ClientTransactionAssociation(SipActivityHandle dialogActivityHandle,
			String associatedServerTransactionBranchId) {
		this.dialogActivityHandle = dialogActivityHandle;
		this.associatedServerTransactionBranchId = associatedServerTransactionBranchId;
	}

	public SipActivityHandle getDialogActivityHandle() {
		return dialogActivityHandle;
	}

	public String getAssociatedServerTransactionBranchId() {
		return associatedServerTransactionBranchId;
	}
	
}
