package org.mobicents.slee.container.rmi;

import java.rmi.Remote;
import java.util.Hashtable;

import org.jboss.invocation.MarshalledInvocation;

/**
 * 
 * @author amit.bhayani
 *
 */
public interface RMIServer extends Remote {
	
	public static Hashtable rmiServers = new Hashtable();
	
	public RMIResponse invoke (MarshalledInvocation mi) throws Exception;
	
	   /**
	    * Get local stub for this service.
	    */   
	   public Object getLocal() throws Exception;	
}
