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
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.LinkedList;

import javax.slee.TransactionRolledbackLocalException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.SbbDescriptorImpl;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionData;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete;

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

    public Object invokeAndReturnObject(final Object proxy, String methodName,
            final Object[] args, Class[] types) throws Exception {
        if (this.setRollbackOnly) {
            throw new TransactionRolledbackLocalException(
                    "Previous invocation caused rollback");
        }

        final ClassLoader oldClassLoader = SleeContainerUtils
                .getCurrentThreadClassLoader();
        
        SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
        
        if (System.getSecurityManager()!=null)
            AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    Thread.currentThread().setContextClassLoader(myClassLoader);
                    return null;
                }
            });
        else
            Thread.currentThread().setContextClassLoader(myClassLoader);

        SbbConcrete sbbConcrete = (SbbConcrete) proxy;
        String sbbEntityId = sbbConcrete.getSbbEntity().getSbbEntityId();
        LinkedList<String> invokedsbbEntities = EventRoutingTransactionData.getFromTransactionContext().getInvokedSbbEntities();
        try {
            final Method meth = proxy.getClass().getMethod(methodName, types);
            invokedsbbEntities.add(sbbEntityId);
            SbbDescriptorImpl descriptor = sbbConcrete.getSbbEntity().getSbbComponent().getDescriptor();
           boolean isolateSecurityPermissions = descriptor.getSbbLocalInterface()==null?false:descriptor.getSbbLocalInterface().isIsolateSecurityPermissions();
           if(!isolateSecurityPermissions)
           {
        	   return meth.invoke(proxy, args);
           }else
           {
        	   //This try catch is akward inside, but its simpler than copying parts of this code
        	   try {
       			
       			//This is required. Since domain chain may indicate RA for instance, or SLEE deployer. If we dont do that test: tests/runtime/security/Test1112012Test.xml and second one, w
       			//will fail because domain of SLEE tck ra is too restrictive (or we have bad desgin taht allows this to happen?)
        		   Object result =AccessController.doPrivileged(new PrivilegedExceptionAction(){

       				public Object run() throws IllegalAccessException, InvocationTargetException{
       					return meth.invoke(proxy, args);
       				}});
       			
       			
       			return result;
       		} catch(PrivilegedActionException pae)
       		{
       			Throwable cause = pae.getException();
       			if(cause instanceof  IllegalAccessException)
       			 {
       				throw new RuntimeException(cause);
       			} else if(cause instanceof InvocationTargetException ) {
       				// Remember the actual exception is hidden inside the
       				// InvocationTarget exception when you use reflection!
       				Throwable realException = cause.getCause();
       				if (realException instanceof RuntimeException) {
       					RuntimeException re = (RuntimeException) realException;
       					throw re;
       				} else if (realException instanceof Error) {
       					Error re = (Error) realException;
       					throw re;
       				} else if (realException instanceof Exception) {
       					Exception re = (Exception) realException;
       					throw re;
       				}
       			}else
       			{
       				throw pae.getException();
       			}
       			//FIXME: why this is required?
       			return null;
       		}
           }
        } catch (InvocationTargetException ex) {           
            if (logger.isDebugEnabled())
            	logger.debug("Invocation resulted in exception: " + ex.getMessage());
            if(ex.getCause() instanceof RuntimeException){
            	sleeContainer.getTransactionManager().setRollbackOnly();
                this.setRollbackOnly = true;
                throw new TransactionRolledbackLocalException(
                        "Invocation resulted in exception ! ", ex.getCause());
            } else {
            	if(ex.getCause() instanceof Exception)
            	{
            		throw (Exception) ex.getCause();
            	}else
            	{
            		throw new Exception(ex.getCause());
            	}
            }
        } catch (Exception ex) {
            if (logger.isDebugEnabled())
                logger.error(ex.getMessage(), ex);
            throw ex;
        } finally {
        	// FIXME remove the equals test once this model is bug proof
        	if (!invokedsbbEntities.removeLast().equals(sbbEntityId)) {
        		logger.error("last sbb entity id removed from event routing tx data was not the expected");
        	}
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

