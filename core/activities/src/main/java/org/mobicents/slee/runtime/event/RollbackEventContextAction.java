package org.mobicents.slee.runtime.event;

import java.io.Serializable;

import org.mobicents.slee.container.activity.ActivityEventQueueManager;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * Tx action to rollback slee event.
 * 
 * @author eduardomartins
 *
 */
public class RollbackEventContextAction implements TransactionalAction, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private final EventContext e;
	private final ActivityEventQueueManager aeqm; 
	
	public RollbackEventContextAction(EventContext e, ActivityEventQueueManager aeqm) {
		this.e = e;
		this.aeqm = aeqm;
	}
	
	public void execute() {				
		aeqm.rollback(e);						
	}
	
}