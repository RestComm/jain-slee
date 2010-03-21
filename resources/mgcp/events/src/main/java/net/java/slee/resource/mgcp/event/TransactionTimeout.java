package net.java.slee.resource.mgcp.event;

import jain.protocol.ip.mgcp.JainMgcpCommandEvent;

import java.io.Serializable;
import java.util.UUID;

/**
 * Activity timeout event.
 * 
 * @author eduardomartins
 * @author amit bhayani
 * 
 */
public final class TransactionTimeout implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6178141966339265518L;

	private final String id;
	private JainMgcpCommandEvent event;

	public TransactionTimeout(JainMgcpCommandEvent event) {
		id = UUID.randomUUID().toString();
		this.event = event;
	}

	protected String getId() {
		return id;
	}

	public int hashCode() {
		return getId().hashCode();
	}

	public JainMgcpCommandEvent getJainMgcpCommandEvent() {
		return this.event;
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((TransactionTimeout) obj).getId() == getId();
		} else {
			return false;
		}
	}

}
