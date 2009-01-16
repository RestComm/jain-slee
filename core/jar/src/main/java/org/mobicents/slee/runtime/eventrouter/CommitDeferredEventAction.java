package org.mobicents.slee.runtime.eventrouter;

import java.io.Serializable;

import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Tx action to commit a deferred event.
 * 
 * @author eduardomartins
 *
 */

public class CommitDeferredEventAction implements TransactionalAction, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private final DeferredEvent de;
	private final ActivityEventQueueManager acheqm;
	
	public CommitDeferredEventAction(DeferredEvent de,ActivityEventQueueManager acheqm) {
		this.de = de;
		this.acheqm = acheqm;
	}

	public void execute() {
		acheqm.commit(de);
	}
}
