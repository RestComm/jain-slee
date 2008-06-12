package org.mobicents.mgcp.stack;

import java.io.Serializable;


/**
 * The ID of a received MGCP transaction.
 * @author eduardomartins
 *
 */
public class ReceivedTransactionID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2376823218467778072L;
	
	protected final int transactionHandle;
	protected final String remoteAddress;
	protected final int remotePort;
	
	protected ReceivedTransactionID(int transactionHandle, String remoteAddress,int remotePort) {
		this.transactionHandle = transactionHandle;
		this.remoteAddress = remoteAddress;
		this.remotePort = remotePort;
	} 
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ReceivedTransactionID other = (ReceivedTransactionID) obj; 
			return this.transactionHandle == other.transactionHandle && this.remoteAddress.equals(other.remoteAddress) && this.remotePort == other.remotePort;
		}
		return false;
	}
	
	public int hashCode() {
		return (transactionHandle * 31 + remoteAddress.hashCode()) * 31 + remotePort;
	}
}
