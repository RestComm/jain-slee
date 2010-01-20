package org.mobicents.slee.resource.sip11.wrappers;

import java.io.Serializable;

import org.mobicents.slee.resource.sip11.SipActivityHandle;

public class ClientTransactionAssociation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final SipActivityHandle dialogActivityHandle;
	private final SipActivityHandle associatedServerTransaction;
	
	public ClientTransactionAssociation(SipActivityHandle dialogActivityHandle,
			SipActivityHandle associatedServerTransaction) {
		this.dialogActivityHandle = dialogActivityHandle;
		this.associatedServerTransaction = associatedServerTransaction;
	}

	public SipActivityHandle getDialogActivityHandle() {
		return dialogActivityHandle;
	}

	public SipActivityHandle getAssociatedServerTransaction() {
		return associatedServerTransaction;
	}
	
}
