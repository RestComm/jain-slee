package net.java.slee.resource.mgcp.event;

import java.io.Serializable;
import java.util.UUID;

/**
 * Activity timeout event.
 * @author eduardomartins
 *
 */
public final class TransactionTimeout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6178141966339265518L;
	
	private final String id;
	
	public TransactionTimeout() {
		id = UUID.randomUUID().toString();
	}
	
	protected String getId() {
		return id;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((TransactionTimeout)obj).getId() == getId();
		}
		else {
			return false;
		}
	}
	
}
