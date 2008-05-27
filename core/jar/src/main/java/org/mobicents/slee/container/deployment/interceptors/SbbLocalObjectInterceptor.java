/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.TransactionRolledbackLocalException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.runtime.SbbConcrete;
import org.mobicents.slee.runtime.SbbLocalObjectConcrete;

/**
 * An invoker for the sbb local object.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 */
public class SbbLocalObjectInterceptor {

    private static Logger logger = Logger
            .getLogger(SbbLocalObjectInterceptor.class);

    private ClassLoader myClassLoader;

    private boolean setRollbackOnly;

    public SbbLocalObjectInterceptor(SbbLocalObjectConcrete sbbLocalObject) {

        this.myClassLoader = sbbLocalObject.getContextClassLoader();
        if (logger.isDebugEnabled()) {
            logger.debug("myClassLoader " + myClassLoader);
        }

    }

    public Object invokeAndReturnObject(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        if (this.setRollbackOnly) {
            throw new TransactionRolledbackLocalException(
                    "Previous invocation caused rollback");
        }

        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();
        if (SleeContainer.isSecurityEnabled())
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    Thread.currentThread().setContextClassLoader(myClassLoader);
                    return null;
                }
            });
        else
            Thread.currentThread().setContextClassLoader(myClassLoader);

        SbbConcrete sbbConcrete = (SbbConcrete) proxy;
        try {

            Method meth = proxy.getClass().getMethod(methodName, types);
            return meth.invoke(proxy, args);
        } catch (InvocationTargetException ex) {           
            if (logger.isDebugEnabled())
            	logger.debug("Invocation resulted in exception: " + ex.getMessage());
            if(ex.getCause() instanceof RuntimeException){
            	SleeContainer.getTransactionManager().setRollbackOnly();
                this.setRollbackOnly = true;
                throw new TransactionRolledbackLocalException(
                        "Invocation resulted in exception ! ", ex.getCause());
            } else {
            	throw (Exception) ex.getCause();
            }
        } catch (Exception ex) {
            if (logger.isDebugEnabled())
                ex.printStackTrace();
            throw ex;
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

    }

    // Invokers for the simple types
    public void invokeAndReturnvoid(Object proxy, String methodName,
            Object[] args, Class[] argTypes) throws Exception {
        invokeAndReturnObject(proxy, methodName, args, argTypes);
    }

    public boolean invokeAndReturnboolean(Object proxy, String methodName,
            Object[] args, Class[] argTypes) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, argTypes);
        logger.debug("invokeAndReturnboolean : returned = " + retval);
        return ((Boolean) retval).booleanValue();
    }

    public int invokeAndReturnint(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        if (logger.isDebugEnabled()) {
            logger.debug("invokeAndReturnint : returned = " + retval);
        }
        return ((Integer) retval).intValue();
    }

    public long invokeAndReturnlong(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        logger.debug("invokeAndReturnlong : returned = " + retval);
        return ((Long) retval).longValue();
    }

    public double invokeAndReturndouble(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        if (logger.isDebugEnabled()) {
            logger.debug("invokeAndReturndouble : returned = " + retval);
        }
        return ((Double) retval).doubleValue();
    }

    public char invokeAndReturnchar(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        logger.debug("invokeAndReturnchar : returned = " + retval);
        return ((Character) retval).charValue();
    }

    public float invokeAndReturnfloat(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        if (logger.isDebugEnabled()) {
            logger.debug("invokeAndReturnchar : returned = " + retval);
        }
        return ((Float) retval).floatValue();
    }

    public float invokeAndReturnshort(Object proxy, String methodName,
            Object[] args, Class[] types) throws Exception {
        Object retval = invokeAndReturnObject(proxy, methodName, args, types);
        logger.debug("invokeAndReturnchar : returned = " + retval);
        return ((Short) retval).shortValue();
    }

}

