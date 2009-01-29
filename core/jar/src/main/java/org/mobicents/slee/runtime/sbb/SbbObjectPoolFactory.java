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

import org.apache.commons.pool.PoolableObjectFactory;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;

/**
 * Implements the methods invoked by the object pool to create the SbbEntity
 * Object
 * 
 * @author F.Moggia
 * @author Eduardo Martins
 */
public class SbbObjectPoolFactory implements PoolableObjectFactory {

	private static Logger logger = Logger.getLogger(SbbObjectPoolFactory.class);
	   
	private final MobicentsSbbDescriptor sbbDescriptor; 

    /** Creates a new instance of SbbObjectPoolFactory */
    public SbbObjectPoolFactory(MobicentsSbbDescriptor sbbDescriptor) {
        this.sbbDescriptor = sbbDescriptor;
    }

    public void activateObject(Object obj) throws java.lang.Exception {
        /*
         * NOTE. We must not call sbbActivate in here This is because this
         * method would get called when borrowing an sbbObject in order to do a
         * sbbCreate - in which case it is illegal to call sbbActivat before
         * sbbCreate - Tim
         */
    	
    	if (logger.isDebugEnabled()) {
        	logger.debug("activateObject() for "+sbbDescriptor.getID());
        }
    	
    }

    public void destroyObject(Object sbb) throws java.lang.Exception {
    	
    	if (logger.isDebugEnabled()) {
        	logger.debug("destroyObject() for "+sbbDescriptor.getID());
        }
        
        SbbObject sbbObject = (SbbObject) sbb;
        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();

        try {
        	//unsetSbbContext must be called with the context classloader
            //of the entities sbbDescriptor as with other sbb invocatiions.
            Thread.currentThread().setContextClassLoader(sbbDescriptor.getClassLoader());
            if (sbbObject.getState() != SbbObjectState.DOES_NOT_EXIST) {
                sbbObject.unsetSbbContext();
            }
        } finally {
            if (SleeContainer.isSecurityEnabled())
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
            logger.debug("makeObject() for "+sbbDescriptor.getID());
        }

        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();

        try {
            final ClassLoader cl = sbbDescriptor.getClassLoader();
            if (SleeContainer.isSecurityEnabled())
                AccessController.doPrivileged(new PrivilegedAction() {
                    public Object run() {
                        Thread.currentThread().setContextClassLoader(cl);
                        return null;

                    }
                });
            else
                Thread.currentThread().setContextClassLoader(cl);
            
            retval = new SbbObject(sbbDescriptor);
        
        } finally {
            if (SleeContainer.isSecurityEnabled())
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
    		logger.debug("passivateObject() for "+sbbDescriptor.getID());
    	}
    	
        SbbObject sbbObj = (SbbObject) sbb;
        sbbObj.setState(SbbObjectState.POOLED);         
    }

    public boolean validateObject(Object sbbo) {
       
    	boolean retval = ((SbbObject) sbbo).getState() == SbbObjectState.POOLED;
        
    	if (logger.isDebugEnabled()) {
        	logger.debug("validateObject() for "+sbbDescriptor.getID()+" returning " + retval);
        }
        
    	return retval;
    }

}