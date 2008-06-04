package net.java.slee.resource.mgcp;

import jain.protocol.ip.mgcp.message.CreateConnection;
import jain.protocol.ip.mgcp.message.NotificationRequest;
import jain.protocol.ip.mgcp.message.parms.CallIdentifier;
import jain.protocol.ip.mgcp.message.parms.ConnectionIdentifier;
import jain.protocol.ip.mgcp.message.parms.EndpointIdentifier;
import jain.protocol.ip.mgcp.message.parms.RequestIdentifier;

import java.util.List;

/**
 * Sbb interface to interact with the Mgcp RA.
 * 
 * @author eduardomartins
 * 
 */
public interface JainMgcpProvider extends jain.protocol.ip.mgcp.JainMgcpProvider {
	
	/**
	 * Retrieves a connection activity for the specified {@link ConnectionIdentifier}. The activity is created if does not exists.
	 * @param connectionIdentifier
	 * @return
	 */
	public MgcpConnectionActivity getConnectionActivity(ConnectionIdentifier connectionIdentifier, EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves a connection activity for an unknown {@link ConnectionIdentifier}, to be used when sending {@link CreateConnection} events and receive further related messages from a Mgcp Server. The activity is created if does not exists.
	 * 
	 * @param transactionHandle the event to be send by server, which the Resource Adaptor will use to learn the returned {@link ConnectionIdentifier}
	 * @return
	 */
	public MgcpConnectionActivity getConnectionActivity(int transactionHandle, EndpointIdentifier endpointIdentifier);
	
	public List<MgcpConnectionActivity> getConnectionActivities(EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves an endpoint activity for the specified {@link EndpointIdentifier}. The activity is created if does not exists.
	 * 
	 * @return
	 */
	public MgcpEndpointActivity getEndpointActivity(EndpointIdentifier endpointIdentifier);
	
	/**
	 * Retrieves an unique transaction handler to be used on mgcp messages
	 * @return
	 */
	public int getUniqueTransactionHandler();
	
	/**
	 * Retrieves an unique valid {@link CallIdentifier} to be used on mgcp commands.
	 * @return
	 */
	public CallIdentifier getUniqueCallIdentifier();
	
	/**
	 * Retrieves an unique valid {@link RequestIdentifier} to be used on {@link NotificationRequest} commands.
	 * @return
	 */
	public RequestIdentifier getUniqueRequestIdentifier();
	
}
