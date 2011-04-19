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

/**
 * Start time:16:39:44 2009-06-12<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.security;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.Iterator;

import javax.management.ObjectName;
import javax.slee.management.ManagementException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.ProfileObject;


/**
 * Start time:16:39:44 2009-06-12<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Dev class, to test logic, put all security related methods here to delegate to org.mobicents domain.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class Utility {

	private Utility() {

	}

	private static final Logger logger = Logger.getLogger(Utility.class);


	
	public static void registerSafelyMBean(final SleeContainer sleeContainer, final ObjectName on, final Object bean) throws ManagementException {
		if (System.getSecurityManager() != null) {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction() {

					public Object run() throws Exception {
						_registerSafelyMBean(sleeContainer, on, bean);
						return null;
					}
				});
			} catch (PrivilegedActionException e) {
				Throwable t = e.getCause();
				throw new ManagementException(t.getMessage(), t);
			}
		} else {
			_registerSafelyMBean(sleeContainer, on, bean);
		}
	}
	private static void _registerSafelyMBean(final SleeContainer sleeContainer, final ObjectName on, final Object bean) throws ManagementException {
		try {
			sleeContainer.getMBeanServer().registerMBean(bean, on);
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}
	public static void unregisterSafelyMBean(final SleeContainer sleeContainer, final ObjectName on) throws ManagementException {
		if (System.getSecurityManager() != null) {
			try {
				AccessController.doPrivileged(new PrivilegedExceptionAction() {

					public Object run() throws Exception {
						_unregisterSafelyMBeanfinal(sleeContainer, on);
						return null;
					}
				});
			} catch (PrivilegedActionException e) {
				Throwable t = e.getCause();
				throw new ManagementException(t.getMessage(), t);
			}
		} else {
			_unregisterSafelyMBeanfinal(sleeContainer, on);
		}
	}

	private static void _unregisterSafelyMBeanfinal( SleeContainer sleeContainer, final ObjectName on) throws ManagementException {
		try {
			sleeContainer.getMBeanServer().unregisterMBean(on);
		} catch (Throwable e) {
			throw new ManagementException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * This method depending if SecurityManger is present switches class loader using priviledged action, 
	 * this is requried as some action may be initiated by unsecure domains.
	 * @param cl
	 * @param po
	 * @return
	 */
	public static ClassLoader switchSafelyClassLoader(final ClassLoader cl,final ProfileObject po)
	{
		ClassLoader _cl = null;
		if(System.getSecurityManager()!=null)
		{
			_cl =	(ClassLoader) AccessController.doPrivileged(new PrivilegedAction(){

				public Object run() {
					
					return _switchSafelyClassLoader(cl,po);
				}});
		}else
		{
			_cl = _switchSafelyClassLoader(cl, po);
		}
		
		return _cl;
		
	}
	
	
	private static ClassLoader _switchSafelyClassLoader(ClassLoader cl,ProfileObject po)
	{


		Thread t = Thread.currentThread();
		if(cl == null)
		{
			ClassLoader currentCL = t.getContextClassLoader();
			t.setContextClassLoader(po.getProfileTable().getProfileSpecificationComponent().getClassLoader());
			return currentCL;
			
		}else
		{
			t.setContextClassLoader(cl);
			return null;
		}
		
		
		
	}
	/**
	 * Its used to embed calls in AccessController in case of insturmented code, cause javassist does not support anonmous inner class.
	 * @param proxy
	 * @param methodToCallname
	 * @param signature
	 * @param values
	 * @return
	 */
	public static Object makeSafeProxyCall(final Object proxy,final String methodToCallname,final  Class[] signature,final  Object[] values) throws PrivilegedActionException
	{
		//Here we execute in sbb/profile or any other slee component domain
		// so no security calls can be made
		try {
			
			//AccessControlContext acc = new AccessControlContext(new ProtectionDomain[]{proxy.getClass().getProtectionDomain()});
			
			return AccessController.doPrivileged(new PrivilegedExceptionAction(){

				public Object run() throws Exception {
					final Method m = proxy.getClass().getMethod(methodToCallname, signature);
					//Here we cross to org.mobicents domain, with all perms, once m.invoke is called, we go into proxy object domain, effective rightsd are cross section of All + proxy object domain permissions
					//This is used when isolate security permissions is set to true;
					
					return m.invoke(proxy, values);
				//}},acc);
				}});
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(PrivilegedActionException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static Class getReturnType(Object o,String methodName)
	{
		try {
			Method m = o.getClass().getMethod(methodName, null);
			return m.getReturnType();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	public static boolean evaluateNext(final Iterator i)
	{
		return ((Boolean)AccessController.doPrivileged(new PrivilegedAction(){

			public Object run() {
				return i.hasNext();
			}})).booleanValue();
	}

}
