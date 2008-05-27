/*
 * Created on Mar 7, 2005
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

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.slee.resource.ActivityHandle;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ResourceAdaptor;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;

/**
 * 
 * TODO Class Description
 * 
 * @author F.Moggia
 */
public class SleeActivityHandle {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(SleeActivityHandle.class);
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		SleeActivityHandle other = (SleeActivityHandle)obj;
    		return other.handle.equals(this.handle) && other.raEntityName.equals(this.raEntityName);
    	}
    	else {
    		return false;
    	}       
    }
    
    private ActivityHandle handle;
    private String raEntityName;
    private SleeContainer container;
    
    public SleeActivityHandle(String raEntityName, ActivityHandle handle, SleeContainer container){
        if (handle != null) {
        	this.handle = handle;
        	this.raEntityName = raEntityName;
        	this.container = container;
        }
        else {
        	throw new IllegalArgumentException("handle is null");
        }
    }
    
    /**
     * @return Returns the handle.
     */
    public ActivityHandle getHandle() {
        return handle;
    }
    
    /**
     * @return Returns the raEntityName.
     */
    public String getRaEntityName() {
        return raEntityName;
    }

    public javax.slee.resource.ResourceAdaptor getResourceAdaptor() {
        if ( raEntityName == null ) return null;
        ResourceAdaptorEntity raEntity =
            container.getResourceAdaptorEnitity(this.raEntityName);
        ResourceAdaptor ra  = raEntity.getResourceAdaptor();
        return ra;
    }
   
   
    public ByteBuffer marshalledVersion(){
        ResourceAdaptorEntity raEntity = container.getResourceAdaptorEnitity(this.raEntityName);
        ResourceAdaptor ra  = raEntity.getResourceAdaptor();
        Marshaler marshaller =  ra.getMarshaler();
        try {
            return marshaller.marshalHandle(this.handle);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    

   
    public Object getActivity() {
        if ( logger.isDebugEnabled()) {
            logger.debug ("getActivity():  handle = " + handle );
        }
        ResourceAdaptorEntity raEntity =  container.getResourceAdaptorEnitity(this.raEntityName);
        ResourceAdaptor ra  = raEntity.getResourceAdaptor();
        return ra.getActivity(this.handle);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        // TODO Auto-generated method stub
        return this.handle.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        // TODO Auto-generated method stub
        return this.handle.hashCode();
    }
}
