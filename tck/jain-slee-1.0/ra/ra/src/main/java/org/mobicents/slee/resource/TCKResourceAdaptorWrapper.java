/*
 * Created on Nov 19, 2004
 * 
 * The Open SLEE Project
 * 
 * A SLEE for the People
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.resource;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.Address;
import javax.slee.InvalidStateException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.FailureReason;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.tck.TCKActivityHandle;
import org.mobicents.slee.resource.tck.TCKMarshaller;
import org.mobicents.slee.util.JndiRegistrationManager;

import com.opencloud.logging.LogLevel;
import com.opencloud.logging.PrintWriterLog;
import com.opencloud.sleetck.lib.TCKTestErrorException;
import com.opencloud.sleetck.lib.resource.TCKActivityID;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceAdaptorInterface;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceFactory;
import com.opencloud.sleetck.lib.resource.adaptor.TCKResourceSetupInterface;
import com.opencloud.sleetck.lib.resource.events.TCKResourceEvent;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivity;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKActivityContextInterfaceFactory;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceAdaptorSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TCKResourceSbbInterface;
import com.opencloud.sleetck.lib.resource.sbbapi.TransactionIDAccess;
import com.opencloud.sleetck.lib.resource.testapi.TCKResourceTestInterface;

/**
 * Resource adaptor for the TCK.
 * 
 * @author F.Moggia
 * @author M. Ranganathan ( hacks )
 */
public class TCKResourceAdaptorWrapper implements javax.slee.resource.ResourceAdaptor, 
TCKResourceAdaptorSbbInterface, Serializable {
    private static TCKResourceSetupInterface tckResourceSetup;
    private transient TCKResourceEventHandlerImpl eventHandler;  
    private TCKResourceAdaptorInterface tckResourceAdaptor;
    private TCKResourceAdaptorSbbInterface factoryInterface;
    private TCKResourceSbbInterface sbbInterface;
    private TCKActivityContextInterfaceFactory tckACIFactory;
    //private transient ResourceAdaptorEntity resourceAdaptorEntity;
    private static Logger log;
    private Marshaler marshaller;
    private TransactionIDAccess transactionIDAccess;
	private BootstrapContext context;
    static {
        log = Logger.getLogger(TCKResourceAdaptorWrapper.class);
        
    }
    
    // we store activities ending because the tck removes them when indicatiing
	// that to SLEE, which makes it possible to have a NPE in activity end event
	// handlers
    protected ConcurrentHashMap<TCKActivityID, TCKActivity> activitiesEnding = new ConcurrentHashMap<TCKActivityID, TCKActivity>();
    
    public TCKResourceAdaptorWrapper() {
        //new Exception().printStackTrace();
        marshaller = new TCKMarshaller();
        try {
            tckResourceSetup = TCKResourceFactory.createResource();
            tckResourceAdaptor = tckResourceSetup.getResourceAdaptorInterface();
            try {
                tckResourceSetup.setLog(new PrintWriterLog(new PrintWriter(new FileWriter(SleeContainer.getDeployPath() + "/TCKRA.log")),LogLevel.FINEST,true,true));
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            //factoryInterface = this;
            sbbInterface = this.tckResourceAdaptor.getSbbInterface();
            
        } catch (TCKTestErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

    public void init(BootstrapContext context) throws ResourceException, NullPointerException {
        
        this.context = context;
        eventHandler = new TCKResourceEventHandlerImpl(context, this, this.tckResourceAdaptor);
        try {
            this.tckResourceAdaptor = tckResourceSetup.getResourceAdaptorInterface();
            
        } catch (TCKTestErrorException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        
    }

    public void configure(Properties properties) throws InvalidStateException {
        
        
    }

    public void start() throws ResourceException {
    	
    	SleeContainer slee = SleeContainer.lookupFromJndi();
    	
    	try {
            TCKResourceTestInterface testInterface = tckResourceSetup.getTestInterface();
            transactionIDAccess = new TransactionIDAccessImpl(slee.getTransactionManager());
            int port = slee.getRmiRegistryPort();
            Registry rmiRegistry = LocateRegistry.getRegistry(port);
            Class tcktest = Thread.currentThread().getContextClassLoader().loadClass("com.opencloud.sleetck.lib.resource.impl.TCKResourceTestInterfaceImpl");
            rmiRegistry.rebind("TCKResourceTestInterface", testInterface);
            log.debug("bound TCKResourceTestInterface");
            
    	 } catch (Exception e) {
             throw new ResourceException("Error binding test interface", e);
         }

    	 try {
			this.tckACIFactory = new TCKActivityContextInterfaceFactoryImpl(
					slee, this.context.getEntityName());
			final ResourceAdaptorEntity raEntity = slee.getResourceManagement()
					.getResourceAdaptorEntity(this.context.getEntityName());
			slee
					.getResourceManagement()
					.getActivityContextInterfaceFactories()
					.put(
							raEntity.getInstalledResourceAdaptor().getRaType()
									.getResourceAdaptorTypeID(),
							(ResourceAdaptorActivityContextInterfaceFactory) this.tckACIFactory);

			if (this.tckACIFactory != null) {
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.tckACIFactory)
						.getJndiName();
				int begind = jndiName.indexOf(':');
				int toind = jndiName.lastIndexOf('/');
				String prefix = jndiName.substring(begind + 1, toind);
				String name = jndiName.substring(toind + 1);
				if (log.isDebugEnabled()) {
					log.debug("jndiName prefix =" + prefix + "; jndiName = "
							+ name);
				}
				JndiRegistrationManager
						.registerWithJndi(prefix, name, this.tckACIFactory);
			}

		} catch (Exception e) {
			throw new ResourceException(
					"Error creating and binding aci factory", e);
		}
        
        try {
            this.tckResourceAdaptor.addEventHandler(eventHandler);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    

    public void stop() {
        
    }

    public void stopping() {
        // TODO Auto-generated method stub
        
    }

    public Object getInterface() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getFactoryInterface() {
        // TODO Auto-generated method stub
        return this;
    }

    public TCKResourceSbbInterface getResource() {
        return sbbInterface;
    }

    public TransactionIDAccess getTransactionIDAccess() {
       
        return transactionIDAccess;
    }

    public Object getActivityContextInterfaceFactory() {
        return this.tckACIFactory;
    }
    
    public void setActivityContextInterfaceFactory(TCKActivityContextInterfaceFactory acif) {
        this.tckACIFactory = acif;
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.ResourceAdaptor#setResourceAdaptorEntity(gov.nist.slee.resource.ResourceAdaptorEntity)
     */
    /*public void setResourceAdaptorEntity(ResourceAdaptorEntity resourceAdaptorEntity) {
        this.resourceAdaptorEntity = resourceAdaptorEntity; 
    }*/

    public void entityCreated(BootstrapContext ctx) throws ResourceException {
        init(ctx);
        
    }

    public void entityRemoved() {
        // TODO Auto-generated method stub
        
    }

    public void entityActivated() throws ResourceException {
        this.start();
        
    }

    public void entityDeactivating() {
        // TODO Auto-generated method stub
        
    }

    public void entityDeactivated() {
    	if (this.tckACIFactory != null) {
			try {
				String jndiName = ((ResourceAdaptorActivityContextInterfaceFactory) this.tckACIFactory)
				.getJndiName();
				// remove "java:" prefix
				int begind = jndiName.indexOf(':');
				String javaJNDIName = jndiName.substring(begind + 1);
				JndiRegistrationManager.unregisterWithJndi(javaJNDIName);
			}
			catch (Exception e) {
				log.error("failed to unbind aci factory", e);
			}
		}
        
    }

    public void eventProcessingSuccessful(ActivityHandle activityHandle, 
            Object event, int eventId, Address address, int arg4) {
        try {
            if ( event instanceof TCKResourceEvent) {
                this.tckResourceAdaptor.onEventProcessingSuccessful(((TCKResourceEvent) event).getEventObjectID());
                if ( log.isDebugEnabled())
                    log.debug("eventProcessingSuccessfull called for " + event);
            } else {
                if (log.isDebugEnabled())
                    log.debug("eventProcessingSucessful for " + event);
            }
        } catch ( Exception ex) {
            throw new RuntimeException("Unexpected exception ", ex);
        }
        
        
    }

    public void eventProcessingFailed(ActivityHandle arg0, Object arg1, int arg2, Address arg3, int arg4, FailureReason arg5) {
        // TODO Auto-generated method stub
        
    }

    public void activityEnded(ActivityHandle ah) {
        try {
            TCKActivityHandle tckh = (TCKActivityHandle)ah;
            this.tckResourceAdaptor.onActivityContextInvalid(tckh.getActivityID());
            activitiesEnding.remove(tckh.getActivityID());
        } catch (RemoteException e) {
            log.error("Failed activityEnded", e);
        }
    }

    public void activityUnreferenced(ActivityHandle arg0) {
        // TODO Auto-generated method stub
        
    }

    public void queryLiveness(ActivityHandle arg0) {
        // TODO Auto-generated method stub
        
    }

    public Object getActivity(ActivityHandle handle) {
       
        TCKActivityHandle tckHandle = (TCKActivityHandle) handle;
        try {
            Object retval = this.tckResourceAdaptor.getActivity(tckHandle.getActivityID());
            if (retval == null) {            	
            	retval = activitiesEnding.get(tckHandle.getActivityID());
            }
            if ( log.isDebugEnabled()) {
                log.debug("getActivity():  returning " + retval);
            }
            return retval;
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve Activty!", e);
        }
    }

    public ActivityHandle getActivityHandle(Object arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getSBBResourceAdaptorInterface(String arg0) {
        // TODO Auto-generated method stub
        return this;
    }

    public Marshaler getMarshaler() {
        // TODO Auto-generated method stub
        return null;
    }

    public void serviceInstalled(String arg0, int[] arg1, String[] arg2) {
        // TODO Auto-generated method stub
        
    }

    public void serviceUninstalled(String arg0) {
        // TODO Auto-generated method stub
        
    }

    public void serviceActivated(String arg0) {
        // TODO Auto-generated method stub
        
    }

    public void serviceDeactivated(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
    

}
