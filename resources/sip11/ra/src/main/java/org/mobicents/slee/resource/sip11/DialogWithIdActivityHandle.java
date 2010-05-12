package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import net.java.slee.resource.sip.DialogActivity;

/**
 * The {@link SipActivityHandle} for {@link DialogActivity}
 * 
 * @author martins
 * 
 */
public class DialogWithIdActivityHandle extends MarshableSipActivityHandle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final Class<? extends SipActivityHandle> TYPE = DialogWithIdActivityHandle.class;
	
	/**
	 * the dialog's id
	 */
	private String dialogId;

	/**
	 * 
	 * @param dialogId
	 */
	public DialogWithIdActivityHandle(String dialogId) {
		if (dialogId == null) {
			throw new NullPointerException("null dialogId");
		}		
		this.dialogId = dialogId;		
	}

	/**
	 * Retrieves the dialog's id.
	 * 
	 * @return
	 */
	public String getDialogId() {
		return dialogId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.MarshableSipActivityHandle#getEstimatedHandleSize()
	 */
	@Override
	public int getEstimatedHandleSize() {
		return dialogId.length() + 3;
	}
	
	@Override
	public int hashCode() {
		return dialogId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DialogWithIdActivityHandle other = (DialogWithIdActivityHandle) obj;
		if (!dialogId.equals(other.dialogId)) {
			return false;
		}		
		return true;
	}

	@Override
	public String toString() {
		return dialogId;
	}
		
}
