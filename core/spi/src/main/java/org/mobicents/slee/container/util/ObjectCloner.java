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
 * 
 */
package org.mobicents.slee.container.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.slee.SLEEException;

import org.apache.log4j.Logger;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;

/**
 * @author martins
 *
 */
public class ObjectCloner {

	protected static final Logger logger = Logger.getLogger(ObjectCloner.class);

	public static <T> T makeDeepCopy(final T orig) {
    	
	     if(System.getSecurityManager()!=null)
	     {
	    	 try {
				return AccessController.doPrivileged(new PrivilegedExceptionAction<T>(){

					public T run() throws Exception {
						
						return _makeDeepCopy(orig);
					}});
			} catch (PrivilegedActionException e) {
				if(e.getCause() instanceof RuntimeException)
				{
					throw (RuntimeException) e.getCause();
				}else
				{
					throw new SLEEException("Failed to create object copy.", e);
				}
			}
	     }else
	     {
	    	 return _makeDeepCopy(orig);
	     }
	    }    
	    @SuppressWarnings("unchecked")
		private static <T> T _makeDeepCopy(T orig)
	    {
	    	 T copy = null;
	         if (orig != null) {
	       	  ByteArrayOutputStream baos = null;
	       	  JBossObjectOutputStream out = null;
	       	  JBossObjectInputStream in = null;
	       	  try {
	       		  baos = new ByteArrayOutputStream();
	       		  out = new JBossObjectOutputStream(baos);
	       		  out.writeObject(orig);
	       		  out.close();
	       		  in = new JBossObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
	       		  copy = (T) in.readObject();
	       		  in.close();
	       	  }
	       	  catch(Throwable e) {
	       		  if (out != null)  {
	       			  try {
	       				  out.close();
	       			  } catch (IOException e1) {
	       				  logger.error(e.getMessage(),e);
	       			  }  
	       		  }
	       		  if (in != null)  {
	       			  try {
	       				  in.close();
	       			  } catch (IOException e1) {
	       				  logger.error(e.getMessage(),e);
	       			  }    
	       		  }
	       		  throw new SLEEException("Failed to create object copy.", e);
	       	  }
	         }
	         return copy;
	    }

}
