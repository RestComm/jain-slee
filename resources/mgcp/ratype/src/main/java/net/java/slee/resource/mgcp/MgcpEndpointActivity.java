package net.java.slee.resource.mgcp;

import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;

/**
 * This activity for Mgcp Resource Adaptor type can be used as the context of
 * Mgcp commands related with a specific Mgcp endpoint.
 * 
 * @author eduardomartins
 * 
 */
public interface MgcpEndpointActivity {

	/**
	 * Retrieves the mgcp {@link EndpointIdentifier} associated with this
	 * activity type.
	 * 
	 */
	public EndpointIdentifier getEndpointIdentifier();

	/**
	 * Forces the end of this activity.
	 */
	public void release();
	
}
