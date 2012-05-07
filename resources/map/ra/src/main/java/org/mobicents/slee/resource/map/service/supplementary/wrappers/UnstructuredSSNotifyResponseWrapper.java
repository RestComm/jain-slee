package org.mobicents.slee.resource.map.service.supplementary.wrappers;

import org.mobicents.protocols.ss7.map.api.service.supplementary.UnstructuredSSNotifyResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class UnstructuredSSNotifyResponseWrapper extends SupplementaryMessageWrapper<UnstructuredSSNotifyResponse>
		implements UnstructuredSSNotifyResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.suplementary.UNSTRUCTURED_SS_NOTIFY_RESPONSE";

	/**
	 * @param mAPDialog
	 */
	public UnstructuredSSNotifyResponseWrapper(MAPDialogSupplementaryWrapper mAPDialog,
			UnstructuredSSNotifyResponse wrapped) {
		super(mAPDialog, EVENT_TYPE_NAME, wrapped);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UnstructuredSSNotifyResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
