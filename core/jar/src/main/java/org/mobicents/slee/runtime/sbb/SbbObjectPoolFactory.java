/*
 * SbbObjectPoolFactory.java
 *
 * Created on June 28, 2004, 2:45 PM
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */

package org.mobicents.slee.runtime.sbb;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.ServiceID;

import org.apache.commons.pool.PoolableObjectFactory;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.SbbComponent;

/**
 * Implements the methods invoked by the object pool to create the SbbEntity
 * Object
 * 
 * @author F.Moggia
 * @author Eduardo Martins
 */
public class SbbObjectPoolFactory implements PoolableObjectFactory {

	private static Logger logger = Logger.getLogger(SbbObjectPoolFactory.class);
	   
	private final SbbComponent sbbComponent; 
	private final ServiceID serviceID;
	
    /** Creates a new instance of SbbObjectPoolFactory */
    public SbbObjectPoolFactory(ServiceID serviceID, SbbComponent sbbComponent) {
    	this.serviceID = serviceID;
        this.sbbComponent = sbbComponent;
    }

    public void activateObject(Object obj) throws java.lang.Exception {
        /*
         * NOTE. We must not call sbbActivate in here This is because this
         * method would get called when borrowing an sbbObject in order to do a
         * sbbCreate - in which case it is illegal to call sbbActivat before
         * sbbCreate - Tim
         */
    	
    	if (logger.isDebugEnabled()) {
        	logger.debug("activateObject() for "+sbbComponent);
        }
    	
    }

    public void destroyObject(Object sbb) throws java.lang.Exception {
    	
    	if (logger.isDebugEnabled()) {
        	logger.debug("destroyObject() for "+sbbComponent);
        }
        
        SbbObject sbbObject = (SbbObject) sbb;
        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();

        try {
        	//unsetSbbContext must be called with the context classloader
            //of the entities sbbDescriptor as with other sbb invocatiions.
            Thread.currentThread().setContextClassLoader(sbbComponent.getClassLoader());
            if (sbbObject.getState() != SbbObjectState.DOES_NOT_EXIST) {
                sbbObject.unsetSbbContext();
            }
        } finally {
            if (System.getSecurityManager()!=null)
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        Thread.currentThread().setContextClassLoader(
                                oldClassLoader);
                        return null;
                    }
                });
            else
                Thread.currentThread().setContextClassLoader(oldClassLoader);

        }

        sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);

    }

    /**
     * Create a new instance of this object and set the SbbContext This places
     * it into the object pool.
     */
    public Object makeObject() {
        
        SbbObject retval;
        if (logger.isDebugEnabled()) {
            logger.debug("makeObject() for "+serviceID+" and "+sbbComponent);
        }

        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();

        try {
            final ClassLoader cl = sbbComponent.getClassLoader();
            if (System.getSecurityManager()!=null)
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        Thread.currentThread().setContextClassLoader(cl);
                        return null;

                    }
                });
            else
                Thread.currentThread().setContextClassLoader(cl);
            
            retval = new SbbObject(serviceID,sbbComponent);
        
        } finally {
            if (System.getSecurityManager()!=null)
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        Thread.currentThread().setContextClassLoader(
                                oldClassLoader);
                        return null;
                    }
                });
            else
                Thread.currentThread().setContextClassLoader(oldClassLoader);
        }

        retval.setState(SbbObjectState.POOLED);
        
        return retval;
    }

    public void passivateObject(Object sbb) throws java.lang.Exception {
    	
    	if (logger.isDebugEnabled()) {
    		logger.debug("passivateObject() for "+sbbComponent);
    	}
    	
        SbbObject sbbObj = (SbbObject) sbb;
        sbbObj.setState(SbbObjectState.POOLED);         
    }

    public boolean validateObject(Object sbbo) {
       
    	boolean retval = ((SbbObject) sbbo).getState() == SbbObjectState.POOLED;
        
    	if (logger.isDebugEnabled()) {
        	logger.debug("validateObject() for "+sbbComponent+" returning " + retval);
        }
        
    	return retval;
    }

}