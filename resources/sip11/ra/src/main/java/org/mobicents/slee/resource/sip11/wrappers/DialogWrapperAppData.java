package org.mobicents.slee.resource.sip11.wrappers;

import java.io.Externalizable;

import javax.sip.Dialog;

import org.mobicents.slee.resource.sip11.SipResourceAdaptor;

/**
 * The app data stored in JAIN SIP dialogs, which is able to provide the
 * proper {@link DialogWrapper}.
 * 
 * @author martins
 * 
 */
public interface DialogWrapperAppData extends Externalizable {

	public DialogWrapper getDialogWrapper(Dialog wrappedDialog, SipResourceAdaptor ra);

}
