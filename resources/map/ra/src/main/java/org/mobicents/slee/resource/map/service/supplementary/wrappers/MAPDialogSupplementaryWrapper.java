package org.mobicents.slee.resource.map.service.supplementary.wrappers;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.primitives.AlertingPattern;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.USSDString;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPDialogSupplementary;
import org.mobicents.slee.resource.map.MAPDialogActivityHandle;
import org.mobicents.slee.resource.map.MAPResourceAdaptor;
import org.mobicents.slee.resource.map.wrappers.MAPDialogWrapper;

/**
 * 
 * @author amit bhayani
 *
 */
public class MAPDialogSupplementaryWrapper extends MAPDialogWrapper<MAPDialogSupplementary> implements
		MAPDialogSupplementary {

	public MAPDialogSupplementaryWrapper(MAPDialogSupplementary wrappedDialog, MAPDialogActivityHandle activityHandle,
			MAPResourceAdaptor ra) {
		super(wrappedDialog, activityHandle, ra);
	}

	@Override
	public Long addProcessUnstructuredSSRequest(byte arg0, USSDString arg1, AlertingPattern arg2, ISDNAddressString arg3)
			throws MAPException {
		return this.wrappedDialog.addProcessUnstructuredSSRequest(arg0, arg1, arg2, arg3);
	}

	@Override
	public Long addProcessUnstructuredSSRequest(int arg0, byte arg1, USSDString arg2, AlertingPattern arg3,
			ISDNAddressString arg4) throws MAPException {
		return this.wrappedDialog.addProcessUnstructuredSSRequest(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void addProcessUnstructuredSSResponse(long arg0, byte arg1, USSDString arg2) throws MAPException {
		this.wrappedDialog.addProcessUnstructuredSSResponse(arg0, arg1, arg2);
	}

	@Override
	public Long addUnstructuredSSNotifyRequest(byte arg0, USSDString arg1, AlertingPattern arg2, ISDNAddressString arg3)
			throws MAPException {
		return this.wrappedDialog.addUnstructuredSSNotifyRequest(arg0, arg1, arg2, arg3);
	}

	@Override
	public Long addUnstructuredSSNotifyRequest(int arg0, byte arg1, USSDString arg2, AlertingPattern arg3,
			ISDNAddressString arg4) throws MAPException {
		return this.wrappedDialog.addUnstructuredSSNotifyRequest(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public Long addUnstructuredSSRequest(byte arg0, USSDString arg1, AlertingPattern arg2, ISDNAddressString arg3)
			throws MAPException {
		return this.wrappedDialog.addUnstructuredSSRequest(arg0, arg1, arg2, arg3);
	}

	@Override
	public Long addUnstructuredSSRequest(int arg0, byte arg1, USSDString arg2, AlertingPattern arg3,
			ISDNAddressString arg4) throws MAPException {
		return this.wrappedDialog.addUnstructuredSSRequest(arg0, arg1, arg2, arg3, arg4);
	}

	@Override
	public void addUnstructuredSSResponse(long arg0, byte arg1, USSDString arg2) throws MAPException {
		this.wrappedDialog.addUnstructuredSSResponse(arg0, arg1, arg2);
	}

	@Override
	public MAPDialogSupplementary getWrappedDialog() {
		return this.wrappedDialog;
	}

	@Override
	public String toString() {
		return "MAPDialogSupplementaryWrapper [wrappedDialog=" + wrappedDialog + "]";
	}

}
