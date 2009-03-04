/***************************************************
 *                                                 *
 *  Mobicents: The Open Source JSLEE Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
/*
 * CoonectorClient.java
 * 
 * Created on Nov 26, 2004
 *
 */
package org.mobicents.slee.test;

/**
 * @author DERUELLE Jean <a href="mailto:jean.deruelle@gmail.com">jean.deruelle@gmail.com</a>
 *
 */

import javax.naming.*;
import javax.slee.profile.ProfileSpecificationID;
import javax.management.*;
import java.util.*;
import org.jboss.jmx.adaptor.rmi.RMIAdaptor;
import org.apache.log4j.Category;


/** Helper Class that connects to the RMA Adaptor on any JBoss node
 *  to provide some services like start/stop JBoss services registered
 *  in the MBean server.
 *
 * @author Anil.Saldhana@jboss.org
 * @version $Revision: 1.2 $
 */

public class ConnectorClient
{
    protected RMIAdaptor rmiserver = null;
    protected Category log;

    /**
     * Constructor
     */
    public ConnectorClient()
    {
        //log = Category.getInstance(this.getClass().getName());
    }

    /**
     * Constructor that takes a JNDI url
     * @param jndiurl    JNDI Url (jnp://localhost:1099)
     */
    public ConnectorClient( String jndiurl ){
        this();
        try {
                //Set Some JNDI Properties
                Hashtable env = new Hashtable();
                env.put( Context.PROVIDER_URL, jndiurl );
                env.put( Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
                env.put( Context.URL_PKG_PREFIXES, "org.jnp.interfaces");

	            InitialContext ctx = new InitialContext(env);
                rmiserver = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");
                if( rmiserver == null ) System.out.println( "RMIAdaptor is null");
        }catch( Exception e){
                 e.printStackTrace();
        }
    }

    /**
     * Get the Metadata for the MBean
     * @param oname   ObjectName of the MBean
     * @return  MBeanInfo about the MBean
     */
    public MBeanInfo getMBeanInfo( ObjectName oname ){
        /* Example:
           //Get the MBeanInfo for the Tomcat MBean
           ObjectName name = new ObjectName( "jboss.web:service=WebServer" );
        */
        MBeanInfo info = null;

        try{
             info = rmiserver.getMBeanInfo( oname );
        } catch( Exception e){
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Invoke an Operation on the MBean
     * @param oname      ObjectName of the MBean
     * @param methodname Name of the operation on the MBean
     * @param pParams    Arguments to the operation
     * @param pSignature Signature for the operation.
     * @return   result from the MBean operation
     * @throws Exception
     */
    public Object invokeOperation( ObjectName oname,
                                   String methodname,Object[] pParams,
                                   String[] pSignature )
    throws Exception {
        Object result = null;
        try{
            /* Example:
            //Stop the Tomcat Instance
            Object result = server.invoke(name, "stop",null,null);
            */
             result = rmiserver.invoke(oname, methodname ,pParams,pSignature);
        } catch( Exception e){
            e.printStackTrace();
        }

        return  result;
    }

	public static void main(String args[]){
		try{
			ConnectorClient connector=new ConnectorClient("jnp://localhost:2000");			
			ObjectName name = new ObjectName( "slee:name=ProfileProvisoning" );
	        ProfileSpecificationID profileSpecificationID=new ProfileSpecificationID("jean","nist-sip","1.0");
			Object[] params=new Object[]{profileSpecificationID,"jean"};
			String[] sig=new String[]{"javax.slee.profile.ProfileSpecificationID","java.lang.String"};
			Object result = connector.invokeOperation(name, "createProfileTable",params,sig);
			result = connector.invokeOperation(name, "getProfileTables",null,null);
			System.out.println("Result after createProfleTable : "+result);
			params=new Object[]{"jean"};
			sig=new String[]{"java.lang.String"};
			result = connector.invokeOperation(name, "removeProfileTable",params,sig);			
			result = connector.invokeOperation(name, "getProfileTables",null,null);
			System.out.println("Result after removeProfleTable "+ result);						
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}