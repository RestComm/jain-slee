package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;

import java.util.UUID;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

public class MgcpConnectionActivityImpl implements MgcpConnectionActivity {

	private final String id;
	
	private String connectionIdentifier;
	private final Integer transactionHandle;
	private MgcpResourceAdaptor ra;
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(ConnectionIdentifier connectionIdentifier, MgcpResourceAdaptor ra) {
		this(connectionIdentifier.toString(),null,ra);
	}
	
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(int transactionHandle, MgcpResourceAdaptor ra) {
		this(null,Integer.valueOf(transactionHandle),ra);
	}
	
	private MgcpConnectionActivityImpl(String connectionIdentifier,Integer transactionHandle, MgcpResourceAdaptor ra) {
		this.id = UUID.randomUUID().toString();
		this.transactionHandle = transactionHandle;
		this.connectionIdentifier = connectionIdentifier;
		this.ra = ra;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getId() {
		return id;
	}
	
	public Integer getTransactionHandle() {
		return transactionHandle;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getConnectionIdentifier() {
		return connectionIdentifier;
	}
	
	/**
	 * TODO
	 * @param connectionIdentifier
	 */
	public void setConnectionIdentifier(ConnectionIdentifier connectionIdentifier) {
		this.connectionIdentifier = connectionIdentifier.toString();
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpConnectionActivityImpl)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}

	public void release() {
		ra.endActivity(new MgcpConnectionActivityHandle(id));		
	}

}
