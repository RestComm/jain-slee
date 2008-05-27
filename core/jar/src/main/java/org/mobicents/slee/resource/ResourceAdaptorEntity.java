/*
 * Created on Oct 26, 2004
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

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;
import javax.slee.CreateException;
import javax.slee.InvalidStateException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceException;

import org.apache.commons.jxpath.util.TypeUtils;
import org.jboss.logging.Logger;
import org.jboss.util.naming.NonSerializableFactory;
import org.jboss.util.naming.Util;
import org.mobicents.slee.container.SleeContainer;

/**
 *
 * Resource Adaptor Entity Class. A run-time wrapper for a resource adaptor that is mapped to JNDI.
 * A resource adaptor entity encapsulates a resource adaptor instance.
 *
 * @author F.Moggia
 * @author M. Ranganathan ( hacks )
 * @author Ivelin Ivanov
 */
public class ResourceAdaptorEntity  implements Serializable {
    
    private static final long serialVersionUID = 6314789586609488087L;
    
    private InstalledResourceAdaptor installedResourceAdaptor;
    // This does not need to be serialized.
    private transient BootstrapContext raContext;
    private String raInterfaceJNDIName;
    private String raFactoryInterfaceJNDIName;
    // The resource adaptor object from which this entity was created.
    private transient ResourceAdaptor resourceAdaptor;
    private ResourceAdaptorEntityState state;
    private String name;
    private SleeContainer serviceContainer;

    private static Logger log = Logger.getLogger(ResourceAdaptorEntity.class);
    
    public ResourceAdaptorEntity(String name, InstalledResourceAdaptor ra, BootstrapContext context,
            SleeContainer serviceContainer )
            throws CreateException {
    	if(name != null) {
    		this.name = name;
    		this.installedResourceAdaptor = ra;
    		this.raContext = context;
    		state = ResourceAdaptorEntityState.INACTIVE;
    		this.serviceContainer = serviceContainer;

    		this.create();
		}
		else {
			throw new IllegalArgumentException("name is null");
		}
        
    }
    
    public ResourceAdaptor getResourceAdaptor() {
        return this.resourceAdaptor;
    }
    
    public InstalledResourceAdaptor getInstalledResourceAdaptor() {
        return this.installedResourceAdaptor;
    }
    
    public String getInterfaceJNDIName(){
        return this.raInterfaceJNDIName;
    }
    
    public String getFactoryInterfaceJNDIName(){
        return this.raFactoryInterfaceJNDIName;
    }
    
    private void setupNamingContext() {
        try {
            String prefix = "slee/resources/" +
                    installedResourceAdaptor.getDescriptor().getName() + "/" +
                    installedResourceAdaptor.getDescriptor().getVendor() + "/" +
                    installedResourceAdaptor.getDescriptor().getVersion() ;
            
            
            Object object = resourceAdaptor.getSBBResourceAdaptorInterface(null);
            if ( log.isDebugEnabled())
                log.debug("Resource Adaptor Object: " + object);
            String GLOBAL_ENV = "java:";
            Context ctx = new InitialContext();
            ctx = (Context) ctx.lookup(GLOBAL_ENV + prefix);
            
            try {
                ctx=ctx.createSubcontext(this.name);
            } catch (NameAlreadyBoundException e) {
                log.warn("Context, " + this.name + " is already bounded");
                log.warn(e);
                ctx = (Context) ctx.lookup(this.name);
            }
            prefix += "/"+this.name;
            
            String factoryProvider = "factoryprovider";
            NonSerializableFactory.rebind(GLOBAL_ENV + prefix + "/" + factoryProvider,
                    object);
            
            StringRefAddr addr = new StringRefAddr("nns", GLOBAL_ENV
                    + prefix + "/" + factoryProvider);
            log.debug("=============== object:"+object+" addr:"+addr+" ===================");
            Reference ref = new Reference(object.getClass().getName(), addr,
                    NonSerializableFactory.class.getName(), null);
            ctx.rebind(factoryProvider, ref);
            ctx.close();
            raInterfaceJNDIName = "java:slee/resources/" +
                    installedResourceAdaptor.getDescriptor().getName() + "/" +
                    installedResourceAdaptor.getDescriptor().getVendor() + "/" +
                    installedResourceAdaptor.getDescriptor().getVersion() + "/" +
                    this.name;
            
            raFactoryInterfaceJNDIName = raInterfaceJNDIName + "/" + factoryProvider;
            
        } catch (Exception e) {
            log.warn("Failed setting up Naming Context", e);
        }
        
        
    }
    
    private void cleanNamingContext() throws NamingException {
        Context ctx = new InitialContext();
        Util.unbind(ctx, getFactoryInterfaceJNDIName());
        Util.unbind(ctx, getInterfaceJNDIName());
        
        if (log.isDebugEnabled()) {
            log.debug("Context " + getFactoryInterfaceJNDIName() + " unbound from Naming Service");
        }
        
    }
    
    public void create() throws CreateException {
        try {

            Constructor cons = this.installedResourceAdaptor.getResourceAdaptorClass().getConstructor(null);
 
            this.resourceAdaptor = (ResourceAdaptor) cons.newInstance(null);
            resourceAdaptor.entityCreated((BootstrapContext) raContext);

        } catch (Exception ex ) {
        	ex.printStackTrace();
            throw new CreateException("Could not create ", ex);
        }catch(Error er)
        {
        	er.printStackTrace();
        	throw er;
        }
    }
    
    public BootstrapContext getBootstrapContext() {
        return raContext;
    }
    
    public void activate() throws ResourceException, InvalidStateException {
        if(this.state != ResourceAdaptorEntityState.INACTIVE){
            throw new InvalidStateException("Resource Adaptor Entity wrong state: " + this.state);
        }
        this.resourceAdaptor.entityActivated();
        this.setupNamingContext();
        this.state = ResourceAdaptorEntityState.ACTIVE;
    }
    
    public void deactivate() throws InvalidStateException{
        if(this.state != ResourceAdaptorEntityState.ACTIVE){
            throw new InvalidStateException("Resource Adaptor Entity wrong state: " + this.state);
        }
        this.resourceAdaptor.entityDeactivating();
        this.resourceAdaptor.entityDeactivated();
        
        try {
            cleanNamingContext();
        } catch (NamingException e) {
            log.debug(e);
        }
        
        this.state = ResourceAdaptorEntityState.INACTIVE;
    }
    
    public void serviceInstalled(String serviceID, int[] eventIDs, String[] resourceOptions) {
        this.resourceAdaptor.serviceInstalled(serviceID, eventIDs, resourceOptions);
    }
    
    public void serviceUninstalled(String serviceID) {
        this.resourceAdaptor.serviceUninstalled(serviceID);
    }
    
    public void serviceActivated(String serviceID) {
        this.resourceAdaptor.serviceActivated(serviceID);
    }
    
    public void serviceDeactivated(String serviceID) {
        this.resourceAdaptor.serviceDeactivated(serviceID);
    }
    public void remove() throws InvalidStateException{
        if(this.state != ResourceAdaptorEntityState.INACTIVE){
            throw new InvalidStateException("Resource Adaptor Entity wrong state: " + this.state);
        }
        this.resourceAdaptor.entityRemoved();
    }
    
    /**
     * Builds method for property accessors using JavaBeans property accessor's rules.
     *
     * @param prefix get/set
     * @param name the name of the property.
     * @param the name of the accessor method.
     */
    public String accessorName(String prefix, String name) {
        return prefix + name.substring(0,1).toUpperCase() + name.substring(1);
    }
    
    /**
     * Modify the value of the configuration property.
     *
     * @param propertyDescriptor the object which describes property.
     * @param the new value of the property.
     */
    public void setConfigProperty(ConfigPropertyDescriptor propertyDescriptor, Object value)
    throws InvalidStateException, ResourceException {
        if(this.state != ResourceAdaptorEntityState.INACTIVE){
            throw new InvalidStateException("Resource Adaptor Entity wrong state: " + this.state);
        }
        
        String methodName = accessorName("set", propertyDescriptor.getName());
        
        Class[] signature = null;
        try {
            signature = new Class[] {Class.forName(propertyDescriptor.getType())};
        } catch (ClassNotFoundException ex) {
            //should never happen here because successfully parsed
        }
        
        Method method;
        try {
            method = resourceAdaptor.getClass().getMethod(methodName, signature);
        } catch (SecurityException ex) {
            throw new ResourceException(ex.getMessage());
        } catch (NoSuchMethodException ex) {
            throw new ResourceException(resourceAdaptor.getClass() +
                    " should implement property accessor method " +
                    methodName + "(" + propertyDescriptor.getType() + ")");
        }
        
        Object[] args = new Object[]{value};
        try {
            method.invoke(resourceAdaptor, args);
        } catch (Exception ex) {
            throw new ResourceException("Failed to set " + value +
                    " to " + name + ". Caused by " + ex.getMessage());
        }
    }
    
    /**
     * Gets the value of the specified configuration property.
     *
     * @param propertyDescriptor the description of the property.
     * @return the value of the property specified by descriptor.
     */
    public Object getConfigProperty(ConfigPropertyDescriptor propertyDescriptor)
    throws ResourceException {
        String methodName = accessorName("get", propertyDescriptor.getName());
        Class[] signature  = new Class[]{};
        
        Method method;
        try {
            method = resourceAdaptor.getClass().getMethod(methodName, signature);
        } catch (SecurityException ex) {
            throw new ResourceException(ex.getMessage());
        } catch (NoSuchMethodException ex) {
            throw new ResourceException(resourceAdaptor.getClass() +
                    " should implement property accessor method "
                    + propertyDescriptor.getType() + " " +  methodName + "()");
        }
        
        try {
            return method.invoke(resourceAdaptor, new Object[]{});
        } catch (Exception ex) {
            throw new ResourceException("Failed to get value of " + name +
                    ". Caused by " + ex.getMessage());
        }
    }
    
    
    public void configure(Properties properties) throws InvalidStateException, ResourceException{
        ResourceAdaptorDescriptorImpl raDescriptor = this.installedResourceAdaptor.getDescriptor();
        Collection configDescriptors = raDescriptor.getConfigPropertyDescriptors();
        
        Iterator list = configDescriptors.iterator();
        while (list.hasNext()) {
            ConfigPropertyDescriptor property = (ConfigPropertyDescriptor) list.next();
            Object value = null;
            if (properties != null && properties.containsKey(property.getName())) {
                String lexicalValue = properties.getProperty(property.getName());
                try {
                    Class clazz = Class.forName(property.getType());
                    value = TypeUtils.convert(lexicalValue, clazz);
                } catch (ClassNotFoundException e) {
                    log.warn("Failed reading property[" + property.getName() + "] while configuring RA Entity [" + name + "]. Because of: ", e);
                }
            } else {
                value = property.getValue();
            }
            setConfigProperty(property, value);
        }
    }
    
    public SleeContainer getServiceContainer() {
        return this.serviceContainer;
    }
    
    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		return ((ResourceAdaptorEntity)obj).name.equals(this.name);
    	}
    	else {
    		return false;
    	}       
    }
    
    public int hashCode() {
        return name.hashCode();
    }

	public ResourceAdaptorEntityState getState() {
		return state;
	}

	public String getName() {
		return name;
	}
}
