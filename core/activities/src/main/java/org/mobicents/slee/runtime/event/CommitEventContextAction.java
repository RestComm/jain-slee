package org.mobicents.slee.runtime.event;

import java.io.Serializable;

import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * Tx action to commit a slee event.
 * 
 * @author eduardomartins
 *
 */

public class CommitEventContextAction implements TransactionalAction, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private final EventContext event;
	private final ActivityEventQueueManager acheqm;
	
	public CommitEventContextAction(EventContext event,ActivityEventQueueManager acheqm) {
		this.event = event;
		this.acheqm = acheqm;
	}

	public void execute() {
		acheqm.commit(event);
	}
}
