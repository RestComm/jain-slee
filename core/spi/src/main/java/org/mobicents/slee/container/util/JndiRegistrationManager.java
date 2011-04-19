/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
