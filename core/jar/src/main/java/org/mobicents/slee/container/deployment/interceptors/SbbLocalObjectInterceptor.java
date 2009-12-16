package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.LinkedList;

import javax.slee.SLEEException;
import javax.slee.TransactionRolledbackLocalException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionData;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

/**
 * An invoker for the sbb local object.
 * 
 * @author M. Ranganathan
 * @author Ivelin Ivanov
 * @author martins
 */
public class SbbLocalObjectInterceptor {

	private static final Logger logger = Logger
			.getLogger(SbbLocalObjectInterceptor.class);

	private final static SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	private ClassLoader myClassLoader;

    private boolean setRollbackOnly;

    public SbbLocalObjectInterceptor(SbbLocalObjectConcrete sbbLocalObject) {
        this.myClassLoader = sbbLocalObject.getContextClassLoader();       
    }
    
	public Object invokeAndReturnObject(final SbbConcrete sbbConcrete,
			String methodName, final Object[] args, Class<?>[] types)
			throws Exception {

		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : sbbConcrete = " + sbbConcrete
					+ " , methodName = " + methodName + " , args = "
					+ Arrays.asList(args) + " , types = "
					+ Arrays.asList(types));
		}

		if (this.setRollbackOnly) {
			throw new TransactionRolledbackLocalException(
					"Previous invocation caused rollback");
		}

		final Method meth = sbbConcrete.getClass().getMethod(methodName, types);
		final SbbEntity sbbEntity = sbbConcrete.getSbbEntity();
		final String sbbEntityId = sbbEntity.getSbbEntityId();
		
		final LinkedList<String> invokedsbbEntities = ((EventRoutingTransactionData) sleeContainer
				.getTransactionManager().getTransactionContext()
				.getEventRoutingTransactionData()).getInvokedSbbEntities();		
		invokedsbbEntities.add(sbbEntityId);
		
		final ClassLoader currentThreadClassLoader = SleeContainerUtils.getCurrentThreadClassLoader();
		SleeContainerUtils.setCurrentThreadClassLoader(myClassLoader);
		
		try {
			if (System.getSecurityManager() == null
					|| !sbbEntity.getSbbComponent()
							.isolateSecurityPermissionsInLocalInterface()) {
				return meth.invoke(sbbConcrete, args);
			} else {
				try {
					// This is required. Since domain chain may indicate RA for
					// instance, or SLEE deployer. If we don't do that test
					// tests/runtime/security/Test1112012Test.xml fails
					return AccessController
							.doPrivileged(new PrivilegedExceptionAction<Object>() {
								public Object run()
										throws IllegalAccessException,
										InvocationTargetException {
									return meth.invoke(sbbConcrete, args);
								}
							});					
				} catch (PrivilegedActionException pae) {
					final Throwable cause = pae.getException();
					if (cause instanceof InvocationTargetException) {
						processInvocationTargetException((InvocationTargetException)cause);
						// the code above always throws exception
						return null;
					} else {
						throw pae.getException();
					}
				}
			}
		} catch (InvocationTargetException ex) {
			processInvocationTargetException(ex);
			// the code above always throws exception
			return null;
		} finally {
			SleeContainerUtils.setCurrentThreadClassLoader(currentThreadClassLoader);
			invokedsbbEntities.removeLast();
		}
	}

	private void processInvocationTargetException(InvocationTargetException exception) throws Exception {
		// Remember the actual exception is hidden inside the
		// InvocationTarget exception when you use reflection!
		final Throwable realException = exception.getCause();
		if (realException instanceof RuntimeException) {							
			sleeContainer.getTransactionManager().setRollbackOnly();
			this.setRollbackOnly = true;
			throw new TransactionRolledbackLocalException(
					"SbbLocalObject Invocation resulted in exception!",realException);
		}
		else if (realException instanceof Exception) {
			throw (Exception) realException;
		}
		else {
			throw new SLEEException("Dude!!",realException);
		}
	}
	
	// Invokers for the simple types
	
	public void invokeAndReturnvoid(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] argTypes) throws Exception {
		invokeAndReturnObject(proxy, methodName, args, argTypes);
	}

	public boolean invokeAndReturnboolean(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] argTypes) throws Exception {
		final Object retval = invokeAndReturnObject(proxy, methodName, args,
				argTypes);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Boolean) retval).booleanValue();
	}

	public int invokeAndReturnint(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		final Object retval = invokeAndReturnObject(proxy, methodName, args,
				types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Integer) retval).intValue();
	}

	public long invokeAndReturnlong(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		Object retval = invokeAndReturnObject(proxy, methodName, args, types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Long) retval).longValue();
	}

	public double invokeAndReturndouble(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		Object retval = invokeAndReturnObject(proxy, methodName, args, types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Double) retval).doubleValue();
	}

	public char invokeAndReturnchar(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		Object retval = invokeAndReturnObject(proxy, methodName, args, types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Character) retval).charValue();
	}

	public float invokeAndReturnfloat(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		Object retval = invokeAndReturnObject(proxy, methodName, args, types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Float) retval).floatValue();
	}

	public float invokeAndReturnshort(SbbConcrete proxy, String methodName,
			Object[] args, Class<?>[] types) throws Exception {
		Object retval = invokeAndReturnObject(proxy, methodName, args, types);
		if (logger.isTraceEnabled()) {
			logger.trace("invokeAndReturnObject : returned = " + retval);
		}
		return ((Short) retval).shortValue();
	}

}
