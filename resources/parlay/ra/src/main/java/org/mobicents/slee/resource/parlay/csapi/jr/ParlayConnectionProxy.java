package org.mobicents.slee.resource.parlay.csapi.jr;

import java.util.Properties;

import javax.slee.resource.ResourceException;

import org.mobicents.csapi.jr.slee.IpServiceConnection;
import org.mobicents.csapi.jr.slee.TpServiceIdentifier;

/**
 * This interface will be implemented by the underlying RA objects which proxy
 * client requests to the Parlay Gateway.
 * 
 * The operation set is identical to that on the ParlayConnection with the
 * exception of cci defined operations.
 */
public interface ParlayConnectionProxy {

	/**
	 * Gets an ID representing an instance of a Parlay service <br>
	 * 
	 * @exception ResourceException
	 *                if the Connection cannot be created
	 * @param serviceTypeName
	 *            the parlay defined service type name
	 * @param serviceProperties
	 *            the set of service properties relating to this service
	 * @return an identifier for the service 
	 * 
	 * TODO service properties not
	 *         correctly typed
	 */
	TpServiceIdentifier getService(String serviceTypeName,
			Properties serviceProperties) throws ResourceException;

	/**
	 * Get a IpServiceConnection. This can be downcasted to a service specific
	 * connection e.g. IpMultiPartyCallControlManagerConnection.
	 * 
	 * @param serviceIdentifier
	 *            an identifier for the service
	 * @return a IpServiceConnection object
	 * @throws ResourceException
	 *             if the Connection cannot be created
	 */
	IpServiceConnection getIpServiceConnection(
			TpServiceIdentifier serviceIdentifier)
			throws ResourceException;
	
	/**
	 * Called by an association if it is closed due to an external operation
	 * e.g. client calls close().
	 * 
	 * @param association
	 */
	void associationClosed(ParlayConnectionProxyAssociation association);
}