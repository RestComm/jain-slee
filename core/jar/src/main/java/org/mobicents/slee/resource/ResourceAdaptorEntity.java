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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.resource.ResourceException;
import javax.slee.InvalidStateException;
import javax.slee.management.ResourceAdaptorEntityState;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptor;

import org.apache.commons.jxpath.util.TypeUtils;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;

/**
 *
 * Resource Adaptor Entity Class. A run-time wrapper for a resource adaptor that is mapped to JNDI.
 * A resource adaptor entity encapsulates a resource adaptor instance.
 *
 * @author F.Moggia
 * @author M. Ranganathan ( hacks )
 * @author Ivelin Ivanov
 */
public class ResourceAdaptorEntity {
    
	/**
	 * the ra entity name
	 */
	private final String name;
	
	/**
	 * the ra component related to this entity
	 */
	private final ResourceAdaptorComponent component;
	
	/**
	 * the ra entity state
	 */
	private ResourceAdaptorEntityState state;
	
	/**
	 * the ra object
	 */
	private final ResourceAdaptorObject object;
	
	/**
	 * the slee container
	 */
	private SleeContainer sleeContainer;

    private static final Logger log = Logger.getLogger(ResourceAdaptorEntity.class);
    
    public ResourceAdaptorEntity(String name, ResourceAdaptorComponent component, ConfigProperties properties, SleeContainer sleeContainer ) {
    	this.name = name;
    	this.component = component;    	
    	this.sleeContainer = sleeContainer;
    	// create ra object
    	Constructor cons = this.component.getResourceAdaptorClass().getConstructor(null);
    	ResourceAdaptor ra = (ResourceAdaptor) cons.newInstance(null);            
        object = new ResourceAdaptorObject(ra);
        // set ra context and configure it
        object.setResourceAdaptorContext(new ResourceAdaptorContextImpl(this,sleeContainer));
        object.raConfigure(properties);
        // process to inactive state
        this.state = ResourceAdaptorEntityState.INACTIVE;
    }
    
    public ResourceAdaptorComponent getComponent() {
		return component;
	}
    
    public ResourceAdaptorObject getObject() {
		return object;
	}
    
    public void sleeIsRunning() {
    	// if entity is active then activate the ra object
    	if (this.state == ResourceAdaptorEntityState.ACTIVE) {
    		object.raActive();
    	}
    }
    
    public void sleeIsStopping() {    	
    	if (this.state == ResourceAdaptorEntityState.ACTIVE) {
    		object.raStopping();
    	}
    }
    
    public void activate() throws InvalidStateException {
        if(this.state != ResourceAdaptorEntityState.INACTIVE){
            throw new InvalidStateException("entity "+name+" is in state: " + this.state);
        }
        this.state = ResourceAdaptorEntityState.ACTIVE;
    	// if slee is running then activate ra object
        if (sleeContainer.getSleeState().isRunning()) {
        	object.raActive();
        }
    }
    
    public void deactivate() throws InvalidStateException {
        if(this.state != ResourceAdaptorEntityState.ACTIVE){
        	throw new InvalidStateException("entity "+name+" is in state: " + this.state);
        }
        object.raStopping();
        this.state = ResourceAdaptorEntityState.STOPPING;
    }
    
    public void deactivated() throws InvalidStateException {
        if(this.state != ResourceAdaptorEntityState.STOPPING){
        	throw new InvalidStateException("entity "+name+" is in state: " + this.state);
        }
        object.raInactive();
        this.state = ResourceAdaptorEntityState.INACTIVE;
    }
    
    public void remove() throws InvalidStateException {
        if(this.state != ResourceAdaptorEntityState.INACTIVE){
        	throw new InvalidStateException("entity "+name+" is in state: " + this.state);
        }
        object.raUnconfigure();
        object.unsetResourceAdaptorContext();
        state = null;
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
            signature = new Class[] {Thread.currentThread().getContextClassLoader().loadClass(propertyDescriptor.getType())};
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
        return this.sleeContainer;
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
