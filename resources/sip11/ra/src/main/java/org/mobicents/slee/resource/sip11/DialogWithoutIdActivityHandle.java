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
public class DialogWithoutIdActivityHandle extends SipActivityHandle implements Serializable {

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
	 * the dialog's remote tag
	 */
	private String remoteTag;

	/**
	 * 
	 * @param callID
	 * @param localTag
	 * @param remoteTag
	 */
	public DialogWithoutIdActivityHandle(String callId, String localTag, String remoteTag) {
		if (callId == null) {
			throw new NullPointerException("null call id");
		}
		if (localTag == null) {
			throw new NullPointerException("null local tag");
		}
		this.callId = callId;
		this.localTag = localTag;
		this.remoteTag = remoteTag;
	}

	/**
	 * Retrieves the dialog's call id.
	 * 
	 * @return
	 */
	public String getCallId() {
		return callId;
	}

	/**
	 * Retrieves the dialog's local tag
	 * 
	 * @return
	 */
	public String getLocalTag() {
		return localTag;
	}

	/**
	 * Retrieves the dialog's remote tag
	 * 
	 * @return
	 */
	public String getRemoteTag() {
		return remoteTag;
	}

	@Override
	public int hashCode() {
		int result = callId.hashCode();
		result = 31 * result + localTag.hashCode();
		result = 31 * result + ((remoteTag == null) ? 0 : remoteTag.hashCode());
		return result;
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
		if (remoteTag == null) {
			if (other.remoteTag != null)
				return false;
		} else if (!remoteTag.equals(other.remoteTag))
			return false;
		return true;
	}

	private static final char SEPARATOR = ':';

	public static final Class<? extends SipActivityHandle> TYPE = DialogWithoutIdActivityHandle.class;
	
	@Override
	public String toString() {
		return new StringBuilder(callId).append(SEPARATOR).append(localTag).append(SEPARATOR).append(remoteTag).toString();
	}
	
}
