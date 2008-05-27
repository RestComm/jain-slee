package org.mobicents.slee.runtime;

import java.io.Serializable;

import javax.slee.InvalidStateException;

import org.mobicents.slee.container.SleeContainer;
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
	
	private DeferredEvent de;

	public CommitDeferredEventAction(DeferredEvent de) {
		this.de = de;
	}

	public void execute() {
		try {
			SleeContainer.lookupFromJndi().getSleeEndpoint()
								.enqueueEvent(de);
		} catch (InvalidStateException e) {
			throw new IllegalStateException();
		}
	}
}
