package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import javax.sip.Transaction;

/**
 * The {@link SipActivityHandle} for {@link Transaction} activity.
 * 
 * @author martins
 * 
 */
public class TransactionActivityHandle extends SipActivityHandle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the transaction's branch id
	 */
	private String branchId;
	
	/**
	 * 
	 * @param branchId
	 * @param method
	 */
	public TransactionActivityHandle(String branchId) {
		if (branchId == null) {
			throw new NullPointerException("null branch id");
		}
		this.branchId = branchId;
	}

	/**
	 * Retrieves the transaction's branch id
	 * 
	 * @return
	 */
	public String getBranchId() {
		return branchId;
	}

	@Override
	public int hashCode() {
		return branchId.hashCode();
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
		if (!branchId.equals(other.branchId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return branchId;
	}

}
