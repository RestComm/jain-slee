package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.sip.Dialog;

import org.mobicents.slee.resource.sip11.DialogWithoutIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public class DialogWithoutIdWrapperData implements DialogWrapperAppData {

	private boolean forkingWinner;
	private boolean stoppedForking;
	
	private ClientDialogWrapper dialogWrapper;

	public DialogWithoutIdWrapperData(ClientDialogWrapper dialogWrapper) {
		this.dialogWrapper = dialogWrapper;
	}

	public DialogWithoutIdWrapperData() {

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		if (dialogWrapper == null) {
			out.writeBoolean(stoppedForking);
			out.writeBoolean(forkingWinner);
		}
		else {
			out.writeBoolean(!dialogWrapper.isForkingPossible());
			out.writeBoolean(dialogWrapper.isForkingWinner());
		}
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {		
		stoppedForking = in.readBoolean();
		forkingWinner = in.readBoolean();		
	}

	@Override
	public DialogWrapper getDialogWrapper(Dialog wrappedDialog, SipResourceAdaptor ra) {
		if (dialogWrapper == null) {
			dialogWrapper = new ClientDialogWrapper(new DialogWithoutIdActivityHandle(wrappedDialog.getCallId().getCallId(),wrappedDialog.getLocalTag()), ra);
			dialogWrapper.setWrappedDialog(wrappedDialog);
			if (stoppedForking) {
				dialogWrapper.stopForking(forkingWinner);
			}
		}
		return dialogWrapper;
	}

}
