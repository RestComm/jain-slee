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
