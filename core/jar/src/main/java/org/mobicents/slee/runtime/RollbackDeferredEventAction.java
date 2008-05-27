package org.mobicents.slee.runtime;

import java.io.Serializable;

import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
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
	
	private DeferredEvent de;
	private ActivityContext ac;
	
	public RollbackDeferredEventAction(DeferredEvent de, ActivityContext ac) {
		this.de = de;
		this.ac = ac;
	}
	
	public void execute() {
		// create  a task to be executed by event router on the activity, this will ensure no concurrency problems
		Runnable r = new Runnable() {
			public void run() {
				boolean rollback = true;
				try {
					SleeContainer.getTransactionManager().begin();
					ac.removeOutstandingEvent(de);
					rollback = false;
				} catch (Exception e) {
					logger.error("error on rollback of deferred event", e);
				}

				finally {
					try {
						if (rollback) {
							SleeContainer.getTransactionManager().rollback();
						} else {
							SleeContainer.getTransactionManager().commit();
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
		SleeContainer.lookupFromJndi().getEventRouter().serializeTaskForActivity(r,ac.getActivity());

	}
	
}