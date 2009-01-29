package org.mobicents.slee.runtime.eventrouter;

import java.io.Serializable;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Tx action to rollback deferred event on the ActivityContext.
 * 
 * @author eduardomartins
 *
 */
public class RollbackDeferredEventAction implements TransactionalAction, Serializable {

	final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private final DeferredEvent de;
	private final String acId; 
	
	public RollbackDeferredEventAction(DeferredEvent de, String acId) {
		this.de = de;
		this.acId = acId;
	}
	
	public void execute() {				
		sleeContainer.getEventRouter().getEventRouterActivity(acId).getEventQueueManager().rollback(de);						
	}
	
}