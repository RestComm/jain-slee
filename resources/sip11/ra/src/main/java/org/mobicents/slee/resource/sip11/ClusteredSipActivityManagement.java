package org.mobicents.slee.resource.sip11;

import javax.sip.Dialog;

import org.mobicents.ha.javax.sip.ClusteredSipStack;
import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * The clustered implementation of {@link SipActivityManagement}.
 * It replicates handles for established dialogs.
 * 
 * @author martins
 *
 */
public class ClusteredSipActivityManagement implements SipActivityManagement {
	
	/**
	 * the jsip clustered stack
	 */
	private final ClusteredSipStack sipStack;
	
	/**
	 * currently there is no support for replicated transactions or early dialogs in jain sip ha, so we manage them with this
	 */
	private final LocalSipActivityManagement nonReplicatedActivityManagement;
	
	/**
	 * 
	 * @param sipStack
	 */
	public ClusteredSipActivityManagement(ClusteredSipStack sipStack) {
		this.sipStack = sipStack;
		this.nonReplicatedActivityManagement = new LocalSipActivityManagement();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#get(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper get(SipActivityHandle handle) {
		if (!(handle instanceof DialogWithIdActivityHandle)) {
			return nonReplicatedActivityManagement.get(handle);
		}
		else {
			Dialog d = sipStack.getDialog(((DialogWithIdActivityHandle)handle).getDialogId());
			if (d != null) {
				return (Wrapper) d.getApplicationData();
			}
			else {
				// maybe it is still in the handle
				return handle.getActivity();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#put(org.mobicents.slee.resource.sip11.SipActivityHandle, org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface)
	 */
	public void put(SipActivityHandle handle, Wrapper activity) {
		if (!(handle instanceof DialogWithIdActivityHandle)) {
			nonReplicatedActivityManagement.put(handle,activity);
		}
		// else nothing to do, replication is done by stack
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#remove(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper remove(SipActivityHandle handle) {
		if (!(handle instanceof DialogWithIdActivityHandle)) {
			return nonReplicatedActivityManagement.remove(handle);
		}
		else {
			// nothing to return, at this point the dialog does not exists in jain sip
			return null;
		}
	}
	
}
