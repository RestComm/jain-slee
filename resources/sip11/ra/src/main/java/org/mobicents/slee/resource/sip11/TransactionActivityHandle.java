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
	 * the transaction's branch
	 */
	private final String branchId;
	
	private final String method;
	
	/**
	 * 
	 * @param branchId
	 * @param method
	 */
	public TransactionActivityHandle(String branchId,String method) {
		if (branchId == null) {
			throw new NullPointerException("null branch id");
		}
		if (method == null) {
			throw new NullPointerException("null method");
		}
		this.branchId = branchId;
		this.method = method;
	}

	/**
	 * Retrieves the transaction's branch id
	 * 
	 * @return
	 */
	public String getBranchId() {
		return branchId;
	}

	/**
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	@Override
	public int hashCode() {
		return branchId.hashCode()*31+method.hashCode();
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
		if (!method.equals(other.method))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return new StringBuilder(branchId).append(':').append(method).toString();
	}

}
