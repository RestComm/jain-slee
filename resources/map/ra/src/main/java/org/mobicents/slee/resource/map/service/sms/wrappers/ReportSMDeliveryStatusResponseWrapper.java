package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusResponse;

/**
 * 
 * @author amit bhayani
 *
 */
public class ReportSMDeliveryStatusResponseWrapper extends SmsMessageWrapper<ReportSMDeliveryStatusResponse> implements
		ReportSMDeliveryStatusResponse {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_RESPONSE";

	public ReportSMDeliveryStatusResponseWrapper(MAPDialogSmsWrapper mAPDialog, ReportSMDeliveryStatusResponse req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public ISDNAddressString getStoredMSISDN() {
		return this.wrappedEvent.getStoredMSISDN();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportSMDeliveryStatusResponseWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
