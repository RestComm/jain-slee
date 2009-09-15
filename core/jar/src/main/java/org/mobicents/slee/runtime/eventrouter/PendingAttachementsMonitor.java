package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manager of temporary attachements related with a {@link ActivityContextHandle}, by {@link Transaction}s being executed.
 * @author eduardomartins
 *
 */
public class PendingAttachementsMonitor {

	/**
     * possible states for a tx regarding attachements related with a {@link ActivityContext}
     * @author eduardomartins
     *
     */
	private enum TxModifyingAttachsState {
		nullStateWithTxAction,
		attachingState,
		detachingState
	}
		
	private final ConcurrentHashMap<String,TxModifyingAttachsState> txsModifyingAttachs = new ConcurrentHashMap<String, TxModifyingAttachsState>();

	private final Object monitor = new Object();
	
	private void txModifyingAttachs(boolean attach) throws SystemException {
		
		SleeTransactionManager txManager = SleeContainer.lookupFromJndi().getTransactionManager();
		String txId = txManager.getTransaction().toString();
		
		// get the current value of the attachment
		TxModifyingAttachsState state = txsModifyingAttachs.get(txId);
		if (state == null) {
			// state doesn't exist, set it
			state = (attach ? TxModifyingAttachsState.attachingState : TxModifyingAttachsState.detachingState); 
			txsModifyingAttachs.put(txId, state);
			// set tx action to remove tx after it ends
			WhenTransactionEndsAction txAction = new WhenTransactionEndsAction(txId);
			txManager.addAfterCommitAction(txAction);
			txManager.addAfterRollbackAction(txAction);
		}
		else {
			// state already exists
			if (state == TxModifyingAttachsState.nullStateWithTxAction) {
				// change state but don't set tx action
				state = (attach ? TxModifyingAttachsState.attachingState : TxModifyingAttachsState.detachingState);
			}
			else if ((attach && state == TxModifyingAttachsState.detachingState) || (!attach && state == TxModifyingAttachsState.attachingState)) {
				// state changed
				state = TxModifyingAttachsState.nullStateWithTxAction;
			}
			
		}
	}
	
	/**
	 * Adds current tx to the set of txs attaching to the {@link ActivityContext} related with this object.
	 * @throws SystemException
	 */
	public void txAttaching() throws SystemException {
		txModifyingAttachs(true);	
	}
	
	/**
	 * Adds current tx to the set of txs dettaching to the {@link ActivityContext} related with this object.
	 * @throws SystemException
	 */
	public void txDetaching() throws SystemException {
		txModifyingAttachs(false);
	}
	
	/**
	 * Holds the thread till all attach modifications are committed/rollback
	 */
	public void waitTillNoTxModifyingAttachs() {
		if (!txsModifyingAttachs.isEmpty()) {			
			synchronized (monitor) {
				try {
					monitor.wait(5000);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}
	
	/**
	 * {@link TransactionalAction} implementation that removes one tx from the map of txs changing attachements for an {@link ActivityContext}.
	 * @author eduardomartins
	 *
	 */
	private class WhenTransactionEndsAction implements TransactionalAction {
		
		private final String transactionId;
		
		public WhenTransactionEndsAction(String transactionId) {			
			this.transactionId = transactionId;
		}
		
		public void execute() {
			if (txsModifyingAttachs != null) {
				// if map exists then remove the tx
				txsModifyingAttachs.remove(transactionId);
				// there may be a event router executor thread waiting for this tx to end so we need to signal its monitor			
				synchronized (monitor) {
					monitor.notify();
				}
			}
		}
	}
}
