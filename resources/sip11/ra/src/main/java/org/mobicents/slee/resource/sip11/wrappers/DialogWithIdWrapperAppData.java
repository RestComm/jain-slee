package org.mobicents.slee.resource.sip11.wrappers;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javax.sip.Dialog;

import org.mobicents.slee.resource.sip11.DialogWithIdActivityHandle;
import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

public class DialogWithIdWrapperAppData implements DialogWrapperAppData {

	private DialogWrapper dialogWrapper;

	public DialogWithIdWrapperAppData(DialogWrapper dialogWrapper) {
		this.dialogWrapper = dialogWrapper;
	}

	public DialogWithIdWrapperAppData() {

	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// nothing
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// nothing
	}

	@Override
	public DialogWrapper getDialogWrapper(Dialog wrappedDialog, SipResourceAdaptor ra) {
		if (dialogWrapper == null) {
			dialogWrapper = new DialogWrapper(new DialogWithIdActivityHandle(wrappedDialog.getDialogId()), ra);
			dialogWrapper.setWrappedDialog(wrappedDialog);
		}
		return dialogWrapper;
	}

}
