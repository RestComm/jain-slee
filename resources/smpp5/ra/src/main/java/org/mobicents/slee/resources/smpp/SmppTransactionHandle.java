package org.mobicents.slee.resources.smpp;

import javax.slee.resource.ActivityHandle;

/**
 * 
 * @author amit bhayani
 *
 */
public class SmppTransactionHandle implements ActivityHandle {

	private int hash;
	private long seqNumber;

	public SmppTransactionHandle(long seqNum, int hash) {
		this.hash = hash;
		this.seqNumber = seqNum;
	}

	protected long getSeqNumber() {
		return this.seqNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + hash;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final SmppTransactionHandle other = (SmppTransactionHandle) obj;
		if (hash != other.hash)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SmppTransactionHandle[Hash=" + hash + "]";
	}

}
