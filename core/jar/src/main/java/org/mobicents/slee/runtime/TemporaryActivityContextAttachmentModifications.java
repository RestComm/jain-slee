package org.mobicents.slee.runtime;

import java.io.ObjectStreamException;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Manager of temporary attachements related with a {@link ActivityContext}, by {@link Transaction}s being executed.
 * @author eduardomartins
 *
 */
public class TemporaryActivityContextAttachmentModifications {

	/**
	 * static single instance
	 */
	private static final TemporaryActivityContextAttachmentModifications singleton = new TemporaryActivityContextAttachmentModifications();
	
	/**
	 * retreives the singleton
	 * @return
	 */
	public static TemporaryActivityContextAttachmentModifications SINGLETON() {
		return singleton;
	}

	/**
	 * solves serialization of singletons
	 * @return
	 * @throws ObjectStreamException
	 */
	private Object readResolve() throws ObjectStreamException {
        return singleton;
    }

    /**
     * solves cloning of singletons
     */ 
    protected Object clone() throws CloneNotSupportedException {
            throw new CloneNotSupportedException();
    }
    
    /**
     * single private constructor
     */
    private TemporaryActivityContextAttachmentModifications() {
		
	}

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
	
	private ConcurrentHashMap<String, ConcurrentHashMap<String, TxModifyingAttachsState>> txsModifyingAttachsPerActivity = new ConcurrentHashMap<String, ConcurrentHashMap<String, TxModifyingAttachsState>>();
	
	/**
	 * Stops the management of txs attaching for the {@link ActivityContext} with the specified id.
	 * @param activityContextId
	 */
	public void activityContextEnded(String activityContextId) {		
		txsModifyingAttachsPerActivity.remove(activityContextId);
	}
	
	private void txModifyingAttachs(ActivityContext ac, boolean attach) throws TransactionRequiredLocalException {
		
		// get set of txs modifying attachs for the ac
		ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(ac.getActivityContextId());
		if (txsModifyingAttachs == null) {
			// create map because it doesn't exists
			txsModifyingAttachs = new ConcurrentHashMap<String,TxModifyingAttachsState>();
			ConcurrentHashMap<String, TxModifyingAttachsState> other = txsModifyingAttachsPerActivity.putIfAbsent(ac.getActivityContextId(),txsModifyingAttachs);
			if(other != null) {
				txsModifyingAttachs = other;
			}
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
			WhenTransactionEndsAction txAction = new WhenTransactionEndsAction(ac.getActivityContextId(),txId);
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
	public void txAttaching(ActivityContext ac) throws TransactionRequiredLocalException {
		txModifyingAttachs(ac,true);	
	}
	
	/**
	 * Adds current tx to the set of txs detaching from the specified {@link ActivityContext}.
	 * @param ac
	 * @throws SystemException
	 */
	public void txDetaching(ActivityContext ac) throws TransactionRequiredLocalException {
		txModifyingAttachs(ac,false);
	}
	
	/**
	 * Checks if the specified {@link ActivityContext} has tx changing it's attachement set of sbb entities
	 * @param activityContextId
	 * @return
	 */
	public boolean hasTxModifyingAttachs(String activityContextId) {
		ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(activityContextId);
		if (txsModifyingAttachs != null) {
			return !txsModifyingAttachs.isEmpty();
		}
		else {
			return false;
		}
	}
	
	/**
	 * {@link TransactionalAction} implementation that removes one tx from the map of txs changing attachements for an {@link ActivityContext}.
	 * @author eduardomartins
	 *
	 */
	private class WhenTransactionEndsAction implements TransactionalAction {
		
		private final String activityContextId;
		private final String transactionId;
		
		public WhenTransactionEndsAction(String activityContextId,String transactionId) {
			this.activityContextId = activityContextId; 
			this.transactionId = transactionId;
		}
		
		public void execute() {
			// get map of txs modifing attachs for the ac
			ConcurrentHashMap<String, TxModifyingAttachsState> txsModifyingAttachs = txsModifyingAttachsPerActivity.get(activityContextId);
			if(txsModifyingAttachs != null) {
				// if map exists then remove the tx
				txsModifyingAttachs.remove(transactionId);
				// signal the event router that it can proceed with routing of events for the ac
				// TODO if performance by pooling in the event router is not good
			}
		}
	}
}
