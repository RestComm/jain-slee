package org.mobicents.slee.resource.map.service.sms.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.sms.LocationInfoWithLMSI;
import org.mobicents.protocols.ss7.map.api.service.sms.MAPDialogSms;
import org.mobicents.protocols.ss7.map.api.service.sms.MWStatus;
import org.mobicents.protocols.ss7.map.api.service.sms.SMDeliveryOutcome;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_DA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_MTI;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_OA;
import org.mobicents.protocols.ss7.map.api.service.sms.SM_RP_SMEA;
import org.mobicents.protocols.ss7.map.api.service.sms.SmsSignalInfo;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogSmsWrapper extends MAPDialogWrapper<MAPDialogSms> implements MAPDialogSms {

	public MAPDialogSmsWrapper(MAPDialogSms wrappedDialog, MAPDialogActivityHandle activityHandle, MAPResourceAdaptor ra) {
		super(wrappedDialog, activityHandle, ra);
	}

	public Long addAlertServiceCentreRequest(ISDNAddressString arg0, AddressString arg1) throws MAPException {
		return this.wrappedDialog.addAlertServiceCentreRequest(arg0, arg1);
	}

	public Long addAlertServiceCentreRequest(int arg0, ISDNAddressString arg1, AddressString arg2) throws MAPException {
		return this.wrappedDialog.addAlertServiceCentreRequest(arg0, arg1, arg2);
	}

	public void addAlertServiceCentreResponse(long arg0) throws MAPException {
		this.wrappedDialog.addAlertServiceCentreResponse(arg0);
	}

	public Long addForwardShortMessageRequest(SM_RP_DA arg0, SM_RP_OA arg1, SmsSignalInfo arg2, boolean arg3)
			throws MAPException {
		return this.wrappedDialog.addForwardShortMessageRequest(arg0, arg1, arg2, arg3);
	}

	public Long addForwardShortMessageRequest(int arg0, SM_RP_DA arg1, SM_RP_OA arg2, SmsSignalInfo arg3, boolean arg4)
			throws MAPException {
		return this.wrappedDialog.addForwardShortMessageRequest(arg0, arg1, arg2, arg3, arg4);
	}

	public void addForwardShortMessageResponse(long arg0) throws MAPException {
		this.wrappedDialog.addForwardShortMessageResponse(arg0);
	}

	public Long addInformServiceCentreRequest(ISDNAddressString arg0, MWStatus arg1, MAPExtensionContainer arg2,
			Integer arg3, Integer arg4) throws MAPException {
		return this.wrappedDialog.addInformServiceCentreRequest(arg0, arg1, arg2, arg3, arg4);
	}

	public Long addInformServiceCentreRequest(int arg0, ISDNAddressString arg1, MWStatus arg2,
			MAPExtensionContainer arg3, Integer arg4, Integer arg5) throws MAPException {
		return this.wrappedDialog.addInformServiceCentreRequest(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public Long addMoForwardShortMessageRequest(SM_RP_DA arg0, SM_RP_OA arg1, SmsSignalInfo arg2,
			MAPExtensionContainer arg3, IMSI arg4) throws MAPException {
		return this.wrappedDialog.addMoForwardShortMessageRequest(arg0, arg1, arg2, arg3, arg4);
	}

	public Long addMoForwardShortMessageRequest(int arg0, SM_RP_DA arg1, SM_RP_OA arg2, SmsSignalInfo arg3,
			MAPExtensionContainer arg4, IMSI arg5) throws MAPException {
		return this.wrappedDialog.addMoForwardShortMessageRequest(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void addMoForwardShortMessageResponse(long arg0, SmsSignalInfo arg1, MAPExtensionContainer arg2)
			throws MAPException {
		this.wrappedDialog.addMoForwardShortMessageResponse(arg0, arg1, arg2);
	}

	public Long addMtForwardShortMessageRequest(SM_RP_DA arg0, SM_RP_OA arg1, SmsSignalInfo arg2, boolean arg3,
			MAPExtensionContainer arg4) throws MAPException {
		return this.wrappedDialog.addMtForwardShortMessageRequest(arg0, arg1, arg2, arg3, arg4);
	}

	public Long addMtForwardShortMessageRequest(int arg0, SM_RP_DA arg1, SM_RP_OA arg2, SmsSignalInfo arg3,
			boolean arg4, MAPExtensionContainer arg5) throws MAPException {
		return this.wrappedDialog.addMtForwardShortMessageRequest(arg0, arg1, arg2, arg3, arg4, arg5);
	}

	public void addMtForwardShortMessageResponse(long arg0, SmsSignalInfo arg1, MAPExtensionContainer arg2)
			throws MAPException {
		this.wrappedDialog.addMtForwardShortMessageResponse(arg0, arg1, arg2);
	}

	public Long addReportSMDeliveryStatusRequest(ISDNAddressString arg0, AddressString arg1, SMDeliveryOutcome arg2,
			Integer arg3, MAPExtensionContainer arg4, boolean arg5, boolean arg6, SMDeliveryOutcome arg7, Integer arg8)
			throws MAPException {
		return this.wrappedDialog
				.addReportSMDeliveryStatusRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	}

	public Long addReportSMDeliveryStatusRequest(int arg0, ISDNAddressString arg1, AddressString arg2,
			SMDeliveryOutcome arg3, Integer arg4, MAPExtensionContainer arg5, boolean arg6, boolean arg7,
			SMDeliveryOutcome arg8, Integer arg9) throws MAPException {
		return this.wrappedDialog.addReportSMDeliveryStatusRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9);
	}

	public void addReportSMDeliveryStatusResponse(long arg0, ISDNAddressString arg1, MAPExtensionContainer arg2)
			throws MAPException {
		this.wrappedDialog.addReportSMDeliveryStatusResponse(arg0, arg1, arg2);
	}

	public Long addSendRoutingInfoForSMRequest(ISDNAddressString arg0, boolean arg1, AddressString arg2,
			MAPExtensionContainer arg3, boolean arg4, SM_RP_MTI arg5, SM_RP_SMEA arg6) throws MAPException {
		return this.wrappedDialog.addSendRoutingInfoForSMRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	}

	public Long addSendRoutingInfoForSMRequest(int arg0, ISDNAddressString arg1, boolean arg2, AddressString arg3,
			MAPExtensionContainer arg4, boolean arg5, SM_RP_MTI arg6, SM_RP_SMEA arg7) throws MAPException {
		return this.wrappedDialog.addSendRoutingInfoForSMRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	public void addSendRoutingInfoForSMResponse(long arg0, IMSI arg1, LocationInfoWithLMSI arg2,
			MAPExtensionContainer arg3) throws MAPException {
		this.wrappedDialog.addSendRoutingInfoForSMResponse(arg0, arg1, arg2, arg3);
	}

	@Override
	public MAPDialogSms getWrappedDialog() {
		return this.wrappedDialog;
	}

	@Override
	public String toString() {
		return "MAPDialogSmsWrapper [wrappedDialog=" + wrappedDialog + "]";
	}

}
