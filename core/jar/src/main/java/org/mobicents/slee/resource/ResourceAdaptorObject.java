package org.mobicents.slee.resource;

import javax.slee.InvalidStateException;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;

public class ResourceAdaptorObject {

	/**
	 * the ra object
	 */
	private final ResourceAdaptor object;
    
	/**
	 * the state of the ra object
	 */
	private ResourceAdaptorObjectState state = null;

	public ResourceAdaptorObject(ResourceAdaptor object) {
		this.object = object;		
	}
	
	public ResourceAdaptor getObject() {
		return object;
	}
	
	public ResourceAdaptorObjectState getState() {
		return state;
	}
	
	// OPERATIONS ON RA OBJECT
	
	public void setResourceAdaptorContext(ResourceAdaptorContext context) throws InvalidStateException {
		if (state == null) {
			object.setResourceAdaptorContext(context);
			state = ResourceAdaptorObjectState.UNCONFIGURED;
		}
		else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    public void raConfigure(ConfigProperties properties) throws InvalidConfigurationException {
    	object.raVerifyConfiguration(properties);
        object.raConfigure(properties);
        if (state == ResourceAdaptorObjectState.UNCONFIGURED) {
        	state = ResourceAdaptorObjectState.INACTIVE;
        }
    }
	
    public void raActive() throws InvalidStateException {
    	if (state == ResourceAdaptorObjectState.INACTIVE) {
    		object.raActive();
    		state = ResourceAdaptorObjectState.ACTIVE;
    	}
    	else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    public void raStopping() throws InvalidStateException {
    	if (state == ResourceAdaptorObjectState.ACTIVE) {
    		object.raStopping();
    		state = ResourceAdaptorObjectState.STOPPING;
    	}
    	else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    public void raInactive() throws InvalidStateException {
    	if (state == ResourceAdaptorObjectState.STOPPING) {
    		object.raInactive();
    		state = ResourceAdaptorObjectState.INACTIVE;
    	}
    	else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    public void raUnconfigure() throws InvalidStateException {
    	if (state == ResourceAdaptorObjectState.INACTIVE) {
    		object.raUnconfigure();
    		state = ResourceAdaptorObjectState.UNCONFIGURED;
    	}
    	else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    public void unsetResourceAdaptorContext() throws InvalidStateException {
		if (state == ResourceAdaptorObjectState.UNCONFIGURED) {
			object.unsetResourceAdaptorContext();
			state = null;
		}
		else {
    		throw new InvalidStateException("ra object is in state "+state);
    	}
    }
    
    // TODO config update, etc ?
}
