package org.mobicents.slee.resource.mgcp.ra;

import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

import net.java.slee.resource.mgcp.MgcpEndpointActivity;

public class MgcpEndpointActivityImpl implements MgcpEndpointActivity {

	private final EndpointIdentifier endpointIdentifier;
	
	private final MgcpResourceAdaptor ra;
	
	/**
	 * TODO
	 */
	public MgcpEndpointActivityImpl(MgcpResourceAdaptor ra, EndpointIdentifier endpointIdentifier) {
		this.ra = ra;
		this.endpointIdentifier = endpointIdentifier;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public EndpointIdentifier getEndpointIdentifier() {
		return endpointIdentifier;
	}
	
	
	
	@Override
	public int hashCode() {
		return endpointIdentifier.toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((MgcpEndpointActivityImpl)obj).endpointIdentifier.toString().equals(this.endpointIdentifier.toString());
		}
		else {
			return false;
		}
	}
	
	public void release() {
		ra.endActivity(new MgcpEndpointActivityHandle(endpointIdentifier.toString()));
	}
}
