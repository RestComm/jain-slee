package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import java.util.UUID;

import net.java.slee.resource.mgcp.MgcpConnectionActivity;

public class MgcpConnectionActivityImpl implements MgcpConnectionActivity {

	private final String id;
	
	private String connectionIdentifier;
	private EndpointIdentifier endpointIdentifier;
	private final Integer transactionHandle;
	private MgcpResourceAdaptor ra;
	
	private MgcpConnectionActivityHandle activityHandle=null;
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this(connectionIdentifier.toString(),null,endpointIdentifier,ra);
	}
	
	/**
	 * TODO
	 */
	public MgcpConnectionActivityImpl(int transactionHandle, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this(null,Integer.valueOf(transactionHandle),endpointIdentifier,ra);
	}
	
	private MgcpConnectionActivityImpl(String connectionIdentifier,Integer transactionHandle, EndpointIdentifier endpointIdentifier, MgcpResourceAdaptor ra) {
		this.id = UUID.randomUUID().toString();
		this.transactionHandle = transactionHandle;
		this.connectionIdentifier = connectionIdentifier;
		this.endpointIdentifier = endpointIdentifier;
		this.ra = ra;
		this.activityHandle=new MgcpConnectionActivityHandle(id);
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
	
	public EndpointIdentifier getEndpointIdentifier() {
		return this.endpointIdentifier;
	}
	
	public void setEndpointIdentifier(EndpointIdentifier endpointIdentifier) {
		this.endpointIdentifier = endpointIdentifier;
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

	public MgcpConnectionActivityHandle getActivityHandle() {
		return activityHandle;
	}

	
	
}
