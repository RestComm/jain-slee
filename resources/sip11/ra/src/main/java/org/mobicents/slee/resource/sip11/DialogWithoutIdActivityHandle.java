package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import javax.sip.Dialog;

import net.java.slee.resource.sip.DialogActivity;

/**
 * The {@link SipActivityHandle} for {@link DialogActivity}
 * related with a {@link Dialog} that does not exist yet.
 * 
 * @author martins
 * 
 */
public class DialogWithoutIdActivityHandle extends MarshableSipActivityHandle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the dialog's call id
	 */
	private String callId;

	/**
	 * the dialog's local tag
	 */
	private String localTag;
	
	/**
	 * 
	 * @param callID
	 * @param localTag
	 * @param remoteTag
	 */
	public DialogWithoutIdActivityHandle(String callId, String localTag) {
		if (callId == null) {
			throw new NullPointerException("null call id");
		}
		if (localTag == null) {
			throw new NullPointerException("null local tag");
		}
		this.callId = callId;
		this.localTag = localTag;
	}

	/**
	 * Retrieves the dialog's call id.
	 * 
	 * @return
	 */
	public String getCallId() {
		return callId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.MarshableSipActivityHandle#getEstimatedHandleSize()
	 */
	@Override
	public int getEstimatedHandleSize() {
		return callId.length() + localTag.length() + 7;
	}
	
	@Override
	public boolean isReplicated() {
		return true;
	}
	
	/**
	 * Retrieves the dialog's local tag
	 * 
	 * @return
	 */
	public String getLocalTag() {
		return localTag;
	}

	@Override
	public int hashCode() {
		return callId.hashCode()*31 + localTag.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DialogWithoutIdActivityHandle other = (DialogWithoutIdActivityHandle) obj;
		if (!callId.equals(other.callId)) {
			return false;
		}
		if (!localTag.equals(other.localTag)) {
			return false;
		}
		return true;
	}

	public static final char DIALOG_ID_SEPARATOR = ':';
	private static final String REMOTE_TAG = ":null";

	public static final Class<? extends SipActivityHandle> TYPE = DialogWithoutIdActivityHandle.class;
	
	@Override
	public String toString() {
		return new StringBuilder(callId).append(DIALOG_ID_SEPARATOR).append(localTag).append(REMOTE_TAG).toString();
	}
	
}
