/**
 * 
 */
package org.mobicents.slee.resources.ss7.isup.events;

import java.io.Serializable;

import org.mobicents.protocols.ss7.isup.ISUPTransaction;

/**
 * @author baranowb
 *
 */
public class TransactionEnded implements Serializable {

	private ISUPTransaction transaction;
	private boolean timedOut;
	public TransactionEnded(ISUPTransaction transaction, boolean timedOut) {
		super();
		this.transaction = transaction;
		this.timedOut = timedOut;
	}
	/**
	 * @return the transaction
	 */
	public ISUPTransaction getTransaction() {
		return transaction;
	}
	/**
	 * @return the timedOut
	 */
	public boolean isTimedOut() {
		return timedOut;
	}
	
	
	
	
}
