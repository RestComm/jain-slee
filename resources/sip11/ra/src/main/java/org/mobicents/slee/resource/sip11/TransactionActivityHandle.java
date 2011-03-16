package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import javax.sip.Transaction;

/**
 * The {@link SipActivityHandle} for {@link Transaction} activity.
 * 
 * @author martins
 * 
 */
public abstract class TransactionActivityHandle extends SipActivityHandle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the transaction's id
	 */
	private final String txId;
	
	/**
	 * 
	 * @param branchId
	 * @param method
	 */
	public TransactionActivityHandle(String txId) {
		if (txId == null) {
			throw new NullPointerException("null tx id");
		}
		this.txId = txId;		
	}

	public String getTxId() {
		return txId;
	}

	@Override
	public int hashCode() {
		return txId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TransactionActivityHandle other = (TransactionActivityHandle) obj;
		if (!txId.equals(other.txId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return txId;
	}

}
