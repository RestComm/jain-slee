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

package org.mobicents.slee.runtime;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Set;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;

import javax.slee.Sbb;
import javax.slee.SbbID;
import javax.slee.TransactionRolledbackLocalException;

import org.mobicents.slee.container.component.MobicentsSbbDescriptor;

import org.apache.commons.pool.PoolableObjectFactory;
import org.jboss.logging.Logger;

/**
 * Implements the methods invoked by the object pool to create the SbbEntity
 * Object
 * 
 * @author F.Moggia
 */
public class SbbObjectPoolFactory implements PoolableObjectFactory {
    private SbbID sbbId;

    private static Logger logger;

    private SleeContainer serviceContainer;

    static {
        logger = Logger
                .getLogger("org.mobicents.slee.runtime.SbbObjectPoolFactory");
    }

    /** Creates a new instance of SbbObjectPoolFactory */
    public SbbObjectPoolFactory(SbbID id, SleeContainer serviceContainer) {
        sbbId = id;
        this.serviceContainer = serviceContainer;
    }

    public void activateObject(Object obj) throws java.lang.Exception {
        /*
         * NOTE. We must not call sbbActivate in here This is because this
         * method would get called when borrowing an sbbObject in order to do a
         * sbbCreate - in which case it is illegal to call sbbActivat before
         * sbbCreate - Tim
         */
    }

    public void destroyObject(Object sbb) throws java.lang.Exception {
        SbbObject sbbObject = (SbbObject) sbb;
        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();

        try {
            //Thread.currentThread().setContextClassLoader(sbbDescriptor.getClassLoader());

            //FIXME - unsetSbbContext must be called with the context
            // classloader
            //of the entities sbbDescriptor as with other sbb invocatiions.
            //Unfortunately by this point we don't know what the sbbe entity is
            //we need to somehow store the classloader on the sbb object
            if (logger.isDebugEnabled()) {
                logger.debug("Calling unsetSbbContext");
            }
            if (!sbbObject.getState().equals(SbbObjectState.DOES_NOT_EXIST)) {
                sbbObject.unsetSbbContext();
                if (logger.isDebugEnabled()) {
                    logger.debug("Called unsetSbbContext");
                }
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
        MobicentsSbbDescriptor sbbDescriptor = (MobicentsSbbDescriptor) serviceContainer
                .getSbbComponent(sbbId);
        if (sbbDescriptor == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("null descriptor retrieved for " + sbbId);
            }
            throw new NullPointerException("null descriptor!");
        }
        SbbObject retval;
        if (logger.isDebugEnabled()) {
            logger.debug(sbbDescriptor.getConcreteSbbClass());
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
            if (logger.isDebugEnabled()) {
                logger.debug("Calling setSbbContext");
            }
            retval = new SbbObject(this.serviceContainer, sbbDescriptor);
            retval.setSbbContext(new SbbContextImpl(retval,
                    this.serviceContainer));
            if (logger.isDebugEnabled()) {
                logger.debug("Called setSbbContext");
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

        retval.setState(SbbObjectState.POOLED);
        if (logger.isDebugEnabled()) {
            logger.debug("making object: " + retval);
        }
        return retval;
    }

    public void passivateObject(Object sbb) throws java.lang.Exception {
        SbbObject sbbObj = (SbbObject) sbb;
        sbbObj.setState(SbbObjectState.POOLED);
        if (logger.isDebugEnabled())
        sbbObj.printInvocationSeq();
        
        
    }

    public boolean validateObject(Object sbbo) {
        boolean retval = ((SbbObject) sbbo).getState() == SbbObjectState.POOLED;
        logger.debug("vaidateObject returning " + retval);
        return retval;
    }

}