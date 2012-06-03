package org.mobicents.slee.resource.map.service.lsm.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.CellGlobalIdOrServiceAreaIdOrLAI;
import org.mobicents.protocols.ss7.map.api.primitives.IMEI;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.SubscriberIdentity;
import org.mobicents.protocols.ss7.map.api.service.lsm.AccuracyFulfilmentIndicator;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaEventInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.DeferredmtlrData;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientID;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSCodeword;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSLocationInfo;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSPrivacyCheck;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSQoS;
import org.mobicents.protocols.ss7.map.api.service.lsm.LocationType;
import org.mobicents.protocols.ss7.map.api.service.lsm.MAPDialogLsm;
import org.mobicents.protocols.ss7.map.api.service.lsm.SLRArgExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.lsm.SupportedGADShapes;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogLsmWrapper extends MAPDialogWrapper<MAPDialogLsm> implements MAPDialogLsm {

	public MAPDialogLsmWrapper(MAPDialogLsm wrappedDialog, MAPDialogActivityHandle activityHandle, MAPResourceAdaptor ra) {
		super(wrappedDialog, activityHandle, ra);
	}

	public Long addProvideSubscriberLocationRequest(LocationType arg0, ISDNAddressString arg1, LCSClientID arg2,
			Boolean arg3, IMSI arg4, ISDNAddressString arg5, LMSI arg6, IMEI arg7, Integer arg8, LCSQoS arg9,
			MAPExtensionContainer arg10, SupportedGADShapes arg11, Byte arg12, Integer arg13, LCSCodeword arg14,
			LCSPrivacyCheck arg15, AreaEventInfo arg16, byte[] arg17) throws MAPException {
		// TODO Auto-generated method stub
		return this.wrappedDialog.addProvideSubscriberLocationRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17);
	}

	public Long addProvideSubscriberLocationRequest(int arg0, LocationType arg1, ISDNAddressString arg2,
			LCSClientID arg3, Boolean arg4, IMSI arg5, ISDNAddressString arg6, LMSI arg7, IMEI arg8, Integer arg9,
			LCSQoS arg10, MAPExtensionContainer arg11, SupportedGADShapes arg12, Byte arg13, Integer arg14,
			LCSCodeword arg15, LCSPrivacyCheck arg16, AreaEventInfo arg17, byte[] arg18) throws MAPException {
		return this.wrappedDialog.addProvideSubscriberLocationRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18);
	}

	public void addProvideSubscriberLocationResponse(long arg0, byte[] arg1, byte[] arg2, byte[] arg3, Integer arg4,
			byte[] arg5, MAPExtensionContainer arg6, Boolean arg7, CellGlobalIdOrServiceAreaIdOrLAI arg8, Boolean arg9,
			AccuracyFulfilmentIndicator arg10) throws MAPException {
		this.wrappedDialog.addProvideSubscriberLocationResponse(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8,
				arg9, arg10);
	}

	public Long addSendRoutingInfoForLCSRequest(ISDNAddressString arg0, SubscriberIdentity arg1,
			MAPExtensionContainer arg2) throws MAPException {
		return this.wrappedDialog.addSendRoutingInfoForLCSRequest(arg0, arg1, arg2);
	}

	public Long addSendRoutingInfoForLCSRequest(int arg0, ISDNAddressString arg1, SubscriberIdentity arg2,
			MAPExtensionContainer arg3) throws MAPException {
		return this.wrappedDialog.addSendRoutingInfoForLCSRequest(arg0, arg1, arg2, arg3);
	}

	public void addSendRoutingInfoForLCSResponse(long arg0, SubscriberIdentity arg1, LCSLocationInfo arg2,
			MAPExtensionContainer arg3, byte[] arg4, byte[] arg5, byte[] arg6, byte[] arg7) throws MAPException {
		this.wrappedDialog.addSendRoutingInfoForLCSResponse(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	}

	public Long addSubscriberLocationReportRequest(LCSEvent arg0, LCSClientID arg1, LCSLocationInfo arg2,
			ISDNAddressString arg3, IMSI arg4, IMEI arg5, ISDNAddressString arg6, ISDNAddressString arg7, byte[] arg8,
			Integer arg9, SLRArgExtensionContainer arg10, byte[] arg11, DeferredmtlrData arg12, Byte arg13,
			byte[] arg14, byte[] arg15, CellGlobalIdOrServiceAreaIdOrLAI arg16, byte[] arg17, Integer arg18,
			Boolean arg19, Boolean arg20, AccuracyFulfilmentIndicator arg21) throws MAPException {
		return this.wrappedDialog.addSubscriberLocationReportRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21);
	}

	public Long addSubscriberLocationReportRequest(int arg0, LCSEvent arg1, LCSClientID arg2, LCSLocationInfo arg3,
			ISDNAddressString arg4, IMSI arg5, IMEI arg6, ISDNAddressString arg7, ISDNAddressString arg8, byte[] arg9,
			Integer arg10, SLRArgExtensionContainer arg11, byte[] arg12, DeferredmtlrData arg13, Byte arg14,
			byte[] arg15, byte[] arg16, CellGlobalIdOrServiceAreaIdOrLAI arg17, byte[] arg18, Integer arg19,
			Boolean arg20, Boolean arg21, AccuracyFulfilmentIndicator arg22) throws MAPException {
		return this.wrappedDialog.addSubscriberLocationReportRequest(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7,
				arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16, arg17, arg18, arg19, arg20, arg21, arg22);
	}

	public void addSubscriberLocationReportResponse(long arg0, ISDNAddressString arg1, ISDNAddressString arg2,
			MAPExtensionContainer arg3) throws MAPException {
		this.wrappedDialog.addSubscriberLocationReportResponse(arg0, arg1, arg2, arg3);
	}

	@Override
	public MAPDialogLsm getWrappedDialog() {
		return this.wrappedDialog;
	}

	@Override
	public String toString() {
		return "MAPDialogLsmWrapper [wrappedDialog=" + wrappedDialog + "]";
	}

}
