/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.test.suite;

import org.mobicents.slee.container.management.jmx.SleeProviderImpl;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.slee.ComponentID;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.facilities.Level;
import javax.slee.management.ComponentDescriptor;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.SbbDescriptor;
import javax.slee.management.ServiceDescriptor;

import org.jboss.jmx.adaptor.rmi.RMIAdaptor;

import com.opencloud.logging.LogLevel;
import com.opencloud.logging.Logable;
import com.opencloud.logging.PrintWriterLog;

import com.opencloud.sleetck.lib.EnvironmentKeys;
import com.opencloud.sleetck.lib.SleeTCKTestUtils;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.infra.SleeTCKTestUtilsImpl;
import com.opencloud.sleetck.lib.infra.eventbridge.BulkEventHandler;
import com.opencloud.sleetck.lib.infra.eventbridge.TCKEventBroadcaster;
import com.opencloud.sleetck.lib.infra.eventbridge.TCKEventListenerRegistry;
import com.opencloud.sleetck.lib.infra.eventbridge.TCKEventListenerRegistryImpl;
import com.opencloud.sleetck.lib.infra.jmx.MBeanFacadeAgentRemote;
import com.opencloud.sleetck.lib.infra.jmx.MBeanFacadeAgentRemoteImpl;
import com.opencloud.sleetck.lib.infra.jmx.MBeanFacadeImpl;
import com.opencloud.sleetck.lib.resource.impl.TCKResourceTestInterfaceProxy;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;
import com.opencloud.sleetck.lib.testutils.jmx.DeploymentMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.MBeanFacade;
import com.opencloud.sleetck.lib.testutils.jmx.MBeanProxyFactory;
import com.opencloud.sleetck.lib.testutils.jmx.ProfileMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.ProfileProvisioningMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.ServiceManagementMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.SleeManagementMBeanProxy;
import com.opencloud.sleetck.lib.testutils.jmx.TraceMBeanProxy;
import org.mobicents.slee.test.suite.MBeanProxyFactoryImpl;

/**
 * 
 * RMI Adaptor to invoke JMX functionality in the SLEE
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 */
public class JUnitSleeTestUtils implements SleeTCKTestUtils {
    private Hashtable env;
    private Logable log;
    private RMIAdaptor rmiAdaptor;
    // private RemoteServer rmiServer;
    
    //private Properties testParams;
    private Properties envParams;
    private Set activatedServiceIDs;
    
    private ObjectName sleeMBean;
    private ObjectName deploymentMBean;
    private ObjectName serviceManagementMBean;
    private ObjectName traceMBean;
    private ObjectName mgmtMBeanName;
   
    
    private HashSet deployableUnitIds;
    
    private TCKResourceTestInterface resourceInterface;
    private Properties testParameters;
    private SleeManagementMBeanProxy sleeManagementMBeanProxy;
    private DeploymentMBeanProxy deploymentMBeanProxy;
    private ServiceManagementMBeanProxy serviceManagementMBeanProxy;
    private TraceMBeanProxy traceMBeanProxy;
	private ProfileProvisioningMBeanProxy profileProvisioningMBeanProxy;
	private MBeanFacadeImpl mbeanFacade;
	
    private MBeanProxyFactoryImpl mBeanProxyFactory;
  
    private MBeanServerConnection server;
  
    static public String TCK_TEST_JARS ;
    private ObjectName profileMBean;
    private Properties properties;
    private BulkEventHandlerImpl bulkEventHandler;
   
    
    static {
        try {
            TCK_TEST_JARS = new File("TckTestJars").toURL().toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
	
    public JUnitSleeTestUtils()  throws Exception {
        this.testTimeout = 7000;
    	this.activatedServiceIDs = new HashSet();
        this.deployableUnitIds = new HashSet();
        this.testParameters = new Properties();
        this.envParams = new Properties();
        this.envParams.setProperty(EnvironmentKeys.SBB_TRACE_LEVEL, (new Integer(Level.LEVEL_FINE)).toString());
        this.log = new PrintWriterLog(new PrintWriter(System.out),LogLevel.FINEST,true,true);
       
        Registry rmiRegistry = LocateRegistry.getRegistry(4099);
        TCKResourceTestInterface remoteHandle = (TCKResourceTestInterface) rmiRegistry.lookup("TCKResourceTestInterface");
      	resourceInterface = new TCKResourceTestInterfaceProxy(remoteHandle,log); 
      	
      	env = new Hashtable();
        env.put( Context.PROVIDER_URL, "jnp://localhost:1099");
        env.put( Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put( Context.URL_PKG_PREFIXES, "org.jnp.interfaces");
        InitialContext ctx = new InitialContext(env);
    	rmiAdaptor = (RMIAdaptor) ctx.lookup("jmx/rmi/RMIAdaptor");
        if( rmiAdaptor == null ) System.out.println( "RMIAdaptor is null");
        sleeMBean = new ObjectName( SleeProviderImpl.SLEE_MANAGEMENT_OBJECT_NAME_STRING);
       // mbeanFacadeObjectName = new ObjectName( SleeProviderImpl.FACADE_OBJECT_NAME);
        
        this.deploymentMBean  = (ObjectName) this.invokeOperation(sleeMBean, "getDeploymentMBean",null,null);
        this.serviceManagementMBean = (ObjectName) this.invokeOperation(sleeMBean, "getServiceManagementMBean",null,null);
        this.traceMBean = (ObjectName) this.invokeOperation(sleeMBean, "getTraceMBean",null,null);
        this.profileMBean = (ObjectName) this.invokeOperation(sleeMBean, "getProfileProvisioningMBean",null,null);
        
        mBeanProxyFactory = new org.mobicents.slee.test.suite.MBeanProxyFactoryImpl ( this);
        
        this.deploymentMBeanProxy = mBeanProxyFactory.createDeploymentMBeanProxy(deploymentMBean);
        this.serviceManagementMBeanProxy = mBeanProxyFactory.createServiceManagementMBeanProxy(serviceManagementMBean);
        this.sleeManagementMBeanProxy = mBeanProxyFactory.createSleeManagementMBeanProxy(this.sleeMBean);
        this.traceMBeanProxy = mBeanProxyFactory.createTraceMBeanProxy(this.traceMBean);
        this.profileProvisioningMBeanProxy = mBeanProxyFactory.createProfileProvisioningMBeanProxy(this.profileMBean);
        // JEAN -- you dont need your own MBeanFacade -- o.c. provides one.
        MBeanServer mbeanServer = new MBeanServerImpl ( this);
        MBeanFacadeAgentRemoteImpl mbAgentRemote = new MBeanFacadeAgentRemoteImpl ( );
        mbAgentRemote.setMBeanServer( mbeanServer);
        TCKEventBroadcaster eventBroadcaster = new TCKEventBroadcaster ();
        bulkEventHandler = new BulkEventHandlerImpl();
        eventBroadcaster.setBulkEventHandler( bulkEventHandler );
        mbAgentRemote.setEventBroadcaster(eventBroadcaster);
        this.mbeanFacade = new MBeanFacadeImpl (log );
        ((MBeanFacadeImpl)this.mbeanFacade).setMBeanFacadeAgentRemote(mbAgentRemote);
        TCKEventListenerRegistry eventListenerRegistry = new TCKEventListenerRegistryImpl ( );
        ((MBeanFacadeImpl) this.mbeanFacade).setListenerRegistry(eventListenerRegistry);
        bulkEventHandler.setMBeanFacade(mbeanFacade);
        
    } 
    
    public SleeTCKTestUtilsImpl createSleeTCKTestUtilsImpl(){
        return new SleeTCKTestUtilsImpl(
                testParameters,
                envParams,
                mbeanFacade,
                mBeanProxyFactory,
                resourceInterface,
                log,
                sleeMBean,
                TCK_TEST_JARS,
                10000,
                10000);
    }
    
    
    public BulkEventHandlerImpl getBulkEventHandler()  {
        return this.bulkEventHandler;
    }
    
    public RMIAdaptor getRMIAdapter() {
        return this.rmiAdaptor;
    }
    
    public JUnitSleeTestUtils(Properties properties)  throws Exception {
        this();
        this.testParameters = properties;
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
            
             result = rmiAdaptor.invoke(oname, methodname ,pParams,pSignature);
        } catch( Exception e){
            e.printStackTrace();
            throw e;
        }

        return  result;
    }

    /**
     * Invoke the getAttribute method on the MBean server 
     * @param oname      ObjectName of the MBean
     * @param attributenName Name of the attribute on the MBean
     * @return   result from the MBean operation
     * @throws Exception
     */
    public Object getAttribute( ObjectName oname,
                                String attributeName)
    throws Exception {
        Object result = null;
        try{
            
             result = rmiAdaptor.getAttribute(oname,attributeName);
        } catch( Exception e){
            e.printStackTrace();
            throw e;
        }

        return  result;
    }

    /**
     * Invoke the getAttribute method on the MBean server 
     * @param oname      ObjectName of the MBean
     * @param attribute attribute to set on the MBean
     * @return   result from the MBean operation
     * @throws Exception
     */
    public void setAttribute( ObjectName oname,
                                Attribute attribute)
    throws Exception {        
        try{            
             rmiAdaptor.setAttribute(oname,attribute);
        } catch( Exception e){
            e.printStackTrace();
            throw e;
        }        
    }

    
    public Properties getTestParams() {
      
        return testParameters;
    }

    public Properties getEnvParams() {
     
        return null;
    }

    public int getDefaultTimeout() {
      
        return 5000;
    }

    private int testTimeout;
    
    public void setTestTimeout(int timeout) {
        testTimeout = timeout;
    }
    
    public int getTestTimeout() {
       
        return testTimeout;
    }

    public Logable getLog() {
       
        return log;
    }

    public MBeanFacade getMBeanFacade() {
      
        return this.mbeanFacade;
    }

    public MBeanProxyFactory getMBeanProxyFactory() {
       
        return mBeanProxyFactory;
    }

    public ObjectName getSleeManagementMBeanName() {
       
        return this.mgmtMBeanName;
    }

    public SleeManagementMBeanProxy getSleeManagementMBeanProxy() throws TCKTestErrorException {
     
        return sleeManagementMBeanProxy;
    }

    public String getDeploymentUnitURL(String deployableUnitName) {
      try {
        return new URL(deployableUnitName).toString();
      } catch ( Exception ex ) {
          return null;
      }
    }

    public DeploymentMBeanProxy getDeploymentMBeanProxy() throws TCKTestErrorException {
        return deploymentMBeanProxy;
    }
    
    public ServiceManagementMBeanProxy getServiceManagementMBeanProxy() {
        return serviceManagementMBeanProxy;
    }
    
    public ObjectName getDeploymentMBeanName() {
        return this.deploymentMBean;
    }

    public DeployableUnitID install(String url) throws TCKTestErrorException {
        
        
        
        try {
        	log.debug("URL is " + url);
            DeployableUnitID deployableUnitId = this.deploymentMBeanProxy.install(url);       
            deployableUnitIds.add(deployableUnitId);
            return deployableUnitId;
        } catch ( Exception e ) {
            e.printStackTrace();
            throw new TCKTestErrorException ( "error installing ", (Exception)e.getCause());
        }
    }

    public boolean uninstallAll() throws TCKTestErrorException {
        Iterator i = this.deployableUnitIds.iterator();
        while (i.hasNext()){
            DeployableUnitID duid = (DeployableUnitID)i.next();
 
            try {
                this.deploymentMBeanProxy.uninstall(duid);
               
            } catch (Exception e){
                throw new TCKTestErrorException("Could not uninstall DeployableUnit " + duid, e);
            }
            
        }
        return true;
        
    }
    
    /**
     * Returns the ServiceIDs of all services contained in the given deployable unit.
     */
    private ServiceID[] getServices(DeployableUnitID duID) throws TCKTestErrorException {
        try {
 
        	DeployableUnitDescriptor duDes = this.deploymentMBeanProxy.getDescriptor(duID);
        	ComponentID[] componentIDs = duDes.getComponents();
            ServiceID[] serviceIDs = new ServiceID[componentIDs.length];
            int serviceCount = 0;
            for (int i = 0; i < componentIDs.length; i++) {
                if(componentIDs[i] instanceof ServiceID) serviceIDs[serviceCount++] = (ServiceID)componentIDs[i];
            }
            if(serviceCount < serviceIDs.length) {
                ServiceID[] copyTo = new ServiceID[serviceCount];
                System.arraycopy(serviceIDs,0,copyTo,0,serviceCount);
                serviceIDs = copyTo;
            }
            return serviceIDs;
        } catch(Exception e) {
            throw new TCKTestErrorException("Caught Exception while trying to get ServiceIDs from DeployableUnitID "+duID,e);
        }
    }

    public ServiceID[] activateServices(DeployableUnitID duID, boolean setTraceLevel)
            throws TCKTestErrorException {
        try {
            // set the trace level to set only if setTraceLevel is true
            // and the sbb trace level is set to a non-null value other than Level.LEVEL_OFF
            Level traceLevel = null;
            if(setTraceLevel) {
                String traceLevelString = envParams.getProperty(EnvironmentKeys.SBB_TRACE_LEVEL);
                if(traceLevelString != null) {
                    int traveLevelInt = Integer.parseInt(traceLevelString);
                    if(traveLevelInt != Level.LEVEL_OFF) traceLevel = Level.fromInt(traveLevelInt);
                }
            }
            // activate the services
            log.fine("Activating services for deployable unit "+duID);
            ServiceID[] serviceIDs = getServices(duID);
      
            
            synchronized (activatedServiceIDs) {
                for (int i = 0; i < serviceIDs.length; i++) {
                    log.finer("Activating service "+serviceIDs[i]);
                  
                    this.serviceManagementMBeanProxy.activate(serviceIDs[i]);
                   
                	activatedServiceIDs.add(serviceIDs[i]);
                    // set the trace level if specified
                    if(traceLevel != null) {
                        ComponentDescriptor serviceDescriptor = this.deploymentMBeanProxy.getDescriptor (serviceIDs[i]);
                        SbbID rootSbbID = ((ServiceDescriptor)serviceDescriptor).getRootSbb();
                        setTraceLevelForSbbTree(rootSbbID,traceLevel);
                    }
                }
            }
            return serviceIDs;
        } catch(Exception e) {
            if(e instanceof TCKTestErrorException) throw (TCKTestErrorException)e;
            throw new TCKTestErrorException("Caught Exception while trying to activate service",e);
        }
    }
    
    /**
     * Sets the trace level for the given SBB and all its descendants recursively.
     */
    private void setTraceLevelForSbbTree(SbbID rootSbbID, Level traceLevel) throws TCKTestErrorException {
        HashSet s = new HashSet();
        s.add(rootSbbID);
        setTraceLevelForSbbTree(rootSbbID, traceLevel, s);
    }

    private void setTraceLevelForSbbTree(SbbID rootSbbID, Level traceLevel, HashSet visited) throws TCKTestErrorException {
        try {
         
            SbbDescriptor sbbDescriptor = (SbbDescriptor) this.deploymentMBeanProxy.getDescriptor(rootSbbID);
            // set the trace level for the given SBB
            this.traceMBeanProxy.setTraceLevel(rootSbbID,traceLevel);
            // recursively set the trace level for all the SBB's descendants
            SbbID[] sbbIDs = sbbDescriptor.getSbbs();
            if(sbbIDs != null) {
                for (int i = 0; i < sbbIDs.length; i++) {
                    // only visit sbbs if not already visited
                    if (visited.add(sbbIDs[i])) setTraceLevelForSbbTree(sbbIDs[i],traceLevel,visited);
                }
            }
        } catch (Exception e) {
            if(e instanceof TCKTestErrorException) throw (TCKTestErrorException)e;
            throw new TCKTestErrorException("Caught Exception while trying to set trace level for SBB. sbbID="+rootSbbID,e);
        }
    }

    public boolean deactivateAllServices() throws TCKTestErrorException {
        try {
            for ( Iterator it  = this.activatedServiceIDs.iterator(); it.hasNext(); ) {
                ServiceID id = (ServiceID)it.next();
                this.serviceManagementMBeanProxy.deactivate(id);
            }
        } catch ( Exception ex) {
            throw new TCKTestErrorException ("Could not deactivate " , (Exception) ex.getCause());  
        }
        return true;
    }


    public TraceMBeanProxy getTraceMBeanProxy() throws TCKTestErrorException {
 
        return this.traceMBeanProxy;
    }

    public ProfileProvisioningMBeanProxy getProfileProvisioningProxy() throws TCKTestErrorException {    
         return this.profileProvisioningMBeanProxy;
    }
    
    public ProfileMBeanProxy getProfileMBeanProxy(ObjectName objectName) throws TCKTestErrorException {    
        try {
            return this.mBeanProxyFactory.createProfileMBeanProxy(objectName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new TCKTestErrorException("error getting the profileMBean proxy");
        }
   }
    
    public TCKResourceTestInterface getResourceInterface() {
        return this.resourceInterface;
    }
    
  
    
}
