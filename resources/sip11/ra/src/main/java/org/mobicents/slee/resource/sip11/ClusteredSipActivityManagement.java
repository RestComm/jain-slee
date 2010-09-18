package org.mobicents.slee.resource.sip11;

import javax.sip.Dialog;
import javax.slee.transaction.SleeTransactionManager;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.ha.javax.sip.ClusteredSipStack;
import org.mobicents.slee.resource.cluster.ReplicatedData;
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
	 * Replicated data source
	 */
	private final ReplicatedData<SipActivityHandle, String> replicatedData;
	
	/**
	 * SLEE's tx manager
	 */
	private final SleeTransactionManager sleeTransactionManager;
	
	/**
	 * 
	 */
	private final SipResourceAdaptor ra;
	
	/**
	 * 
	 * @param sipStack
	 */
	public ClusteredSipActivityManagement(ClusteredSipStack sipStack,ReplicatedData<SipActivityHandle, String> replicatedData, SleeTransactionManager sleeTransactionManager, SipResourceAdaptor ra) {
		this.sipStack = sipStack;
		this.nonReplicatedActivityManagement = new LocalSipActivityManagement();
		this.replicatedData = replicatedData;
		this.sleeTransactionManager = sleeTransactionManager;
		this.ra = ra;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#get(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper get(SipActivityHandle handle) {
		final Class<?> handleType = handle.getClass();
		if (handleType == ServerTransactionActivityHandle.TYPE || handleType == ClientTransactionActivityHandle.TYPE) {
			return nonReplicatedActivityManagement.get(handle);
		}
		else if (handleType == DialogWithoutIdActivityHandle.TYPE) {
			Wrapper activity = nonReplicatedActivityManagement.get(handle);
			if (activity == null) {
				final DialogWithoutIdActivityHandle dialogWithoutIdActivityHandle = (DialogWithoutIdActivityHandle) handle;
				// the handle doesn't have a remote tag but dialog may actually be confirmed and 
				// exist in stack's replicated data, lets look for the tag in
				// RA's replicated data
				final String remoteTag = replicatedData.get(handle);
				if (remoteTag != null) {
					final String dialogId = 
						new StringBuilder(dialogWithoutIdActivityHandle.getCallId())
					.append(DialogWithoutIdActivityHandle.DIALOG_ID_SEPARATOR).append(dialogWithoutIdActivityHandle.getLocalTag())
					.append(DialogWithoutIdActivityHandle.DIALOG_ID_SEPARATOR).append(remoteTag)
					.toString()
					.toLowerCase();
					Dialog d = sipStack.getDialog(dialogId);
					if (d != null) {
						// cool, found it in jsip ha stack, lets relink wrapper and store it in
						// non replicated data
						activity = ra.getDialogWrapper(d);
						nonReplicatedActivityManagement.put(handle,
								activity);
					}
				}				
			}
			return activity;
		}
		else if (handleType == DialogWithIdActivityHandle.TYPE) { 
			Dialog d = sipStack.getDialog(((DialogWithIdActivityHandle)handle).getDialogId());
			if (d != null) {
				return ra.getDialogWrapper(d);				
			}
			else {
				// maybe it is still in the handle
				return handle.getActivity();
			}
		}
		else {
			throw new IllegalArgumentException("unknown type of sip activity handle -> "+handle.getClass());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#put(org.mobicents.slee.resource.sip11.SipActivityHandle, org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface)
	 */
	public void put(SipActivityHandle handle, Wrapper activity) {
		if (handle.getClass() != DialogWithIdActivityHandle.TYPE) {
			nonReplicatedActivityManagement.put(handle,activity);
		}
		// else nothing to do, replication is done by stack
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#remove(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper remove(SipActivityHandle handle) {
		final Class<?> handleType = handle.getClass();
		if (handleType == ServerTransactionActivityHandle.TYPE || handleType == ClientTransactionActivityHandle.TYPE) {
			return nonReplicatedActivityManagement.remove(handle);
		}
		else if (handleType == DialogWithoutIdActivityHandle.TYPE) {
			Wrapper activity = nonReplicatedActivityManagement.remove(handle);
			// remove remote tag from Ra's replicated data
			Transaction tx = null;
			try {
				tx = sleeTransactionManager.getTransaction();
				if (tx != null) {
					sleeTransactionManager.suspend();
				}
			} catch (SystemException e) {
				// ignore
			}
			replicatedData.remove(handle);
			if (tx != null) {
				try {
					sleeTransactionManager.resume(tx);
				} catch (Throwable e) {
					// ignore
				}
			}
			return activity;
		}
		else if (handleType == DialogWithIdActivityHandle.TYPE) { 
			// not stored here, so if it is not in handle it is not recoverable
			return handle.getActivity();
		}
		else {
			throw new IllegalArgumentException("unknown type of sip activity handle -> "+handle.getClass());
		}
	}
	
	public void replicateRemoteTag(SipActivityHandle handle, String tag) {
		replicatedData.put(handle,tag);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClusteredSipActivityManagement[ non replicated activities = "+nonReplicatedActivityManagement.handleSet()+" ]";
	}
	
}
