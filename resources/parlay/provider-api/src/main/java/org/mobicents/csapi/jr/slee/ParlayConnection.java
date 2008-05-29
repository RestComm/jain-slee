
package org.mobicents.csapi.jr.slee;

// Resource adapter imports

import javax.slee.resource.ResourceException;
import javax.resource.cci.Connection;
import java.util.Properties;

import org.mobicents.csapi.jr.slee.cc.gccs.IpCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.cc.mpccs.IpMultiPartyCallControlManagerConnection;
import org.mobicents.csapi.jr.slee.ui.IpUIManagerConnection;



/**
 * This is handle is the Provider interface that can be used for accessing Parlay 
 * service instance. 
 */
public interface ParlayConnection extends Connection {

 /*
  * Gets an Id representing an instance of a Parlay service
  * @exception ResourceException if the Connection cannot be create 
  * @ param serviceTypeName the parlay defined service type name 
  * @param serviceProperties the set of service properties relating to this service
  * @ return an identifier for the service
  * 
  * TODO service properties not 
  *         correctly typed 
  */
TpServiceIdentifier getService(String serviceTypeName, Properties serviceProperties) throws ResourceException;


 /*
  * Gets a IpServiceConnection . This can be downcasted to a service specific
  * connection e.g. IpMultiPartyCallControlManagerConnection.
  * 
  * @param serviceIdentifier an identifier for the service
  * @return a IpServiceConnection object             
  * @throws ResourceException if the Connection cannot be creared  
  * 
  * 
  *         
  */
IpServiceConnection getIpServiceConnection (TpServiceIdentifier serviceIdentifier) throws ResourceException;


}
 




