/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
