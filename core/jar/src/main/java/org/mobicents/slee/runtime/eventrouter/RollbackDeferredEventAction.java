package org.mobicents.slee.runtime.eventrouter;

import java.io.Serializable;

import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Tx action to rollback deferred event on the ActivityContext.
 * 
 * @author eduardomartins
 *
 */
public class RollbackDeferredEventAction implements TransactionalAction, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8427380358783989321L;
	
	private static Logger logger = Logger
			.getLogger(RollbackDeferredEventAction.class);
	
	private final DeferredEvent de;
	private final ActivityContextHandle ach; 
	
	public RollbackDeferredEventAction(DeferredEvent de, ActivityContextHandle ach) {
		this.de = de;
		this.ach = ach;
	}
	
	public void execute() {
		// create  a task to be executed by event router on the activity, this will ensure no concurrency problems
		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		Runnable r = new Runnable() {
			public void run() {
				boolean rollback = true;
				try {
					sleeContainer.getEventRouter().getEventRouterActivity(ach).getEventQueueManager().rollback(de);
					rollback = false;
				} catch (Exception e) {
					logger.error("error on rollback of deferred event", e);
				}
				finally {
					try {
						if (rollback) {
							sleeContainer.getTransactionManager().rollback();
						} else {
							sleeContainer.getTransactionManager().commit();
						}
					} catch (SystemException e1) {
						logger
								.error(
										"failed to commit tx while executing action to rollback deferred event",
										e1);
					}
				}
			}
		};
		sleeContainer.getEventRouter().getEventRouterActivity(ach).getExecutorService().submit(r);

	}
	
}