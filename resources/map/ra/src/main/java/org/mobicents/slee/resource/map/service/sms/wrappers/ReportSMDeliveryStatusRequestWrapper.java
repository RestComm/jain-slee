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

	@Override
	public Integer getAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAbsentSubscriberDiagnosticSM();
	}

	@Override
	public Integer getAdditionalAbsentSubscriberDiagnosticSM() {
		return this.wrappedEvent.getAdditionalAbsentSubscriberDiagnosticSM();
	}

	@Override
	public SMDeliveryOutcome getAdditionalSMDeliveryOutcome() {
		return this.wrappedEvent.getAdditionalSMDeliveryOutcome();
	}

	@Override
	public boolean getDeliveryOutcomeIndicator() {
		return this.wrappedEvent.getDeliveryOutcomeIndicator();
	}

	@Override
	public MAPExtensionContainer getExtensionContainer() {
		return this.wrappedEvent.getExtensionContainer();
	}

	@Override
	public boolean getGprsSupportIndicator() {
		return this.wrappedEvent.getGprsSupportIndicator();
	}

	@Override
	public ISDNAddressString getMsisdn() {
		return this.wrappedEvent.getMsisdn();
	}

	@Override
	public SMDeliveryOutcome getSMDeliveryOutcome() {
		return this.wrappedEvent.getSMDeliveryOutcome();
	}

	@Override
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
		return "ReportSMDeliveryStatusRequest [wrapped=" + this.wrappedEvent + "]";
	}

}
