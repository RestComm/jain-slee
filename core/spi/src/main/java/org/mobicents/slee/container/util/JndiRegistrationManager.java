package org.mobicents.slee.container.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import org.apache.log4j.Logger;
import org.jboss.util.naming.NonSerializableFactory;
import org.jboss.util.naming.Util;

public class JndiRegistrationManager {

	private static final Logger logger = Logger.getLogger(JndiRegistrationManager.class);
	
	public static final String JVM_ENV = "java:";
	
	/**
	 * Register a internal slee component with jndi.
	 */
	public static void registerWithJndi(String prefix, String name,
			Object object) {
		String fullName = JVM_ENV + prefix + "/" + name;
		try {
			Context ctx = new InitialContext();
			try {
				Util.createSubcontext(ctx, fullName);
			} catch (NamingException e) {
				logger.warn("Context, " + fullName + " might have been bound.");
				logger.warn(e);
			}
			ctx = (Context) ctx.lookup(JVM_ENV + prefix);
			// ctx.createSubcontext(name);
			// ctx = (Context) ctx.lookup(name);
			// Util.rebind(JVM_ENV + prefix + "/" + name, object);
			NonSerializableFactory.rebind(fullName, object);
			StringRefAddr addr = new StringRefAddr("nns", fullName);
			Reference ref = new Reference(object.getClass().getName(), addr,
					NonSerializableFactory.class.getName(), null);
			Util.rebind(ctx, name, ref);
			logger.debug("registered with jndi " + fullName);
		} catch (Exception ex) {
			logger.warn("registerWithJndi failed for " + fullName, ex);
		}
	}
	
	/**
	 * Unregister an internal slee component with jndi.
	 * 
	 * @param Name -
	 *            the full path to the resource except the "java:" prefix.
	 */
	public static void unregisterWithJndi(String name) {
		Context ctx;
		String path = JVM_ENV + name;
		try {
			ctx = new InitialContext();
			Util.unbind(ctx, path);
		} catch (NamingException ex) {
			logger.warn("unregisterWithJndi failed for " + path);
		}
	}
	
	/**
	 * lookup a name reference from jndi.
	 * 
	 * @param resourceName --
	 *            name to lookup.
	 * @throws NamingException
	 */
	public static Object getFromJndi(String resourceName)
			throws NamingException {

		Context initialContext = new InitialContext();
		Context compEnv = (Context) initialContext.lookup(JVM_ENV);
		return compEnv.lookup(resourceName);
	}
}
