package org.mobicents.slee.runtime.eventrouter;

import java.util.concurrent.ConcurrentHashMap;

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manager of temporary attachements related with a {@link ActivityContextHandle}, by {@link Transaction}s being executed.
 * @author eduardomartins
 *
 */
public class PendingActivityContextAttachmentsManager {

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
	
	private ConcurrentHashMap<ActivityContextHandle, ConcurrentHashMap<String, TxModifyingAttachsState>> txsModifyingAttachsPerActivity = new ConcurrentHashMap<ActivityContextHandle, ConcurrentHashMap<String, TxModifyingAttachsState>>();
	
	/**
	 * we may have to hold event router executor services so activity attach/detach commit/rollback, this map holds monitor objects for sync 
	 */
	private ConcurrentHashMap<ActivityContextHandle, Object> eventRouterExecutorMonitors = new ConcurrentHashMap<ActivityContextHandle, Object>();
	
	/**
	 * Stops the management of txs attaching for the {@link ActivityContext} with the specified id.
	 * @param activityContextId
	 */
	public void activityEnded(ActivityContextHandle ach) {		
		txsModifyingAttachsPerActivity.remove(ach);
		eventRouterExecutorMonitors.remove(ach);
	}
	
	/**
	 * Stops the management of txs attaching for the {@link ActivityContext} with the specified id.
	 * @param activityContextId
	 */
	public void activityStarted(ActivityContextHandle ach) {	
		ConcurrentHashMap<String,TxModifyingAttachsState> txsModifyingAttachs = new ConcurrentHashMap<String,TxModifyingAttachsState>();
		if (txsModifyingAttachsPerActivity.putIfAbsent(ach,txsModifyingAttachs) == null) {
			eventRouterExecutorMonitors.put(ach, new Object());
		}
	}
	
	private void txModifyingAttachs(ActivityContextHandle ach, boolean attach) throws TransactionRequiredLocalException {
		
		// get set of txs modifying attachs for the ac
		ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(ach);
		if (txsModifyingAttachs == null) {
			return;
		}
		
		SleeTransactionManager txManager = SleeContainer.getTransactionManager();
		String txId = null;
		try {
			txId = txManager.getTransaction().toString();
		} catch (SystemException e) {
			throw new TransactionRequiredLocalException("failed to retreive transaction from slee transaction manager");
		}
		
		// get the current value of the attachment
		TxModifyingAttachsState state = txsModifyingAttachs.get(txId);
		if (state == null) {
			// state doesn't exist, set it
			state = (attach ? TxModifyingAttachsState.attachingState : TxModifyingAttachsState.detachingState); 
			txsModifyingAttachs.put(txId, state);
			// set tx action to remove tx after it ends
			WhenTransactionEndsAction txAction = new WhenTransactionEndsAction(ach,txId);
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
	 * Adds current tx to the set of txs attaching to the specified {@link ActivityContext}.
	 * @param ac
	 * @throws SystemException
	 */
	public void txAttaching(ActivityContextHandle ach) throws TransactionRequiredLocalException {
		txModifyingAttachs(ach,true);	
	}
	
	/**
	 * Adds current tx to the set of txs detaching from the specified {@link ActivityContext}.
	 * @param ac
	 * @throws SystemException
	 */
	public void txDetaching(ActivityContextHandle ach) throws TransactionRequiredLocalException {
		txModifyingAttachs(ach,false);
	}
	
	/**
	 * Holds the thread till all attach modifications are committed/rollback
	 * @param activityContextHandle
	 */
	public void waitTillNoTxModifyingAttachs(ActivityContextHandle activityContextHandle) {
		ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(activityContextHandle);
		if (txsModifyingAttachs != null && !txsModifyingAttachs.isEmpty()) {
			Object monitor = eventRouterExecutorMonitors.get(activityContextHandle);
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
		
		private final ActivityContextHandle ach;
		private final String transactionId;
		
		public WhenTransactionEndsAction(ActivityContextHandle ach,String transactionId) {
			this.ach = ach; 
			this.transactionId = transactionId;
		}
		
		public void execute() {
			// get map of txs modifing attachs for the ac
			ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(ach);
			if(txsModifyingAttachs != null) {
				// if map exists then remove the tx
				txsModifyingAttachs.remove(transactionId);
				// there may be a event router executor thread waiting for this tx to end so we need to signal its monitor
				Object monitor = eventRouterExecutorMonitors.get(ach);
				synchronized (monitor) {
					monitor.notify();
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return 	"Temporary AC Attachement Modifications: " +
		"\n+-- Monitors: " + eventRouterExecutorMonitors.size() +
		"\n+-- Activities with pending attachements: " + txsModifyingAttachsPerActivity.size();
	}
}
