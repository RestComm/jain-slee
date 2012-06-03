package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.ReportSMDeliveryStatusRequest;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;

/**
 * 
 * @author amit bhayani
 *
 */
public class ReportSMDeliveryStatusRequestWrapper extends SmsMessageWrapper<ReportSMDeliveryStatusRequest> implements
		ReportSMDeliveryStatusRequest {

	private static final String EVENT_TYPE_NAME = "ss7.map.service.sms.REPORT_SM_DELIVERY_STATUS_REQUEST";

	public ReportSMDeliveryStatusRequestWrapper(MAPDialogSmsWrapper mAPDialog, ReportSMDeliveryStatusRequest req) {
		super(mAPDialog, EVENT_TYPE_NAME, req);
	}

	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAbsentSubscriberDiagnosticSM();
	}

	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAdditionalAbsentSubscriberDiagnosticSM();
	}

	public SMDeliveryOutcome getAdditionalSMDeliveryOutcome() {
		return this.wrappedEvent.getAdditionalSMDeliveryOutcome();
	}

	public boolean getDeliveryOutcomeIndicator() {
		return this.wrappedEvent.getDeliveryOutcomeIndicator();
	}

	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	public boolean getGprsSupportIndicator() {
		return this.wrappedEvent.getGprsSupportIndicator();
	}

	public ISDNAddressString getMsisdn() {
		return this.wrappedEvent.getMsisdn();
	}

	public SMDeliveryOutcome getSMDeliveryOutcome() {
		return this.wrappedEvent.getSMDeliveryOutcome();
	}

	public AddressString getServiceCentreAddress() {
		return this.wrappedEvent.getServiceCentreAddress();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportSMDeliveryStatusRequestWrapper [wrapped=" + this.wrappedEvent + "]";
	}

}
