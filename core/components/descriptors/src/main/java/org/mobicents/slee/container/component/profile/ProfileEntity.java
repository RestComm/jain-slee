package org.mobicents.slee.container.component.profile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.slee.SLEEException;
import javax.slee.profile.ReadOnlyProfileException;

import org.apache.log4j.Logger;
import org.jboss.serial.io.JBossObjectInputStream;
import org.jboss.serial.io.JBossObjectOutputStream;

/**
 * The container for for a profile cmp fields
 * @author martins
 *
 */
public abstract class ProfileEntity {

	protected static final Logger logger = Logger.getLogger(ProfileEntity.class);
	
	/**
	 * the profile name
	 */
	protected String profileName;
	
	/**
	 * the profile table name
	 */
	protected String tableName;
	
	/**
	 * indicates if the pojo should be removed in the end of transaction
	 */
	private boolean remove = false;
	
	/**
	 * indicates if the pojo should be created in the end of transaction, or its cmp fields just updated
	 */
	private boolean create = false;
	
	/**
	 * indicates if the pojo cmp fields values have changed since being load from persistence
	 */
	private boolean dirty = false;
	
	/**
	 * indicates if the pojo cmp fields are read only
	 */
	private boolean readOnly = false;
	
	/**
	 * Only for usage by JPA, use getProfileName() instead
	 * @return
	 */
    public String getProfileName() {
        return profileName;
    }

    /**
     * Only for usage by JPA, use setProfileName(String) instead
     * @param s
     */
    public void setProfileName(String s) {
        profileName = s;
    }

    /**
     * 
     * @return
     */
	public String getTableName() {
        return tableName;
    }

	/**
	 * Sets the profile table name
	 * @param s
	 */
    public void setTableName(String s) {
        tableName = s;
    }
    
    /**
     * 
     * @return
     */
    public boolean isCreate() {
		return create;
	}
    
    /**
     * 
     * @return
     */
    public boolean isDirty() {
		return dirty;
	}
    
    /**
     * 
     * @return
     */
    public boolean isReadOnly() {
		return readOnly;
	}
    
    /**
     * 
     * @return
     */
    public boolean isRemove() {
		return remove;
	}
    
    /**
     * 
     */
    public void create() {
    	create = true;
    }
    
    /**
     * 
     */
    public void setDirty(boolean dirty) {
    	this.dirty = dirty;
    }
    
    /**
     * 
     */
    public void setReadOnly(boolean readOnly) {
    	this.readOnly = readOnly;
    }
    
    /**
     * 
     */
    public void remove() throws ReadOnlyProfileException {
    	if (isReadOnly()) {				
			throw new ReadOnlyProfileException("Profile: " + getProfileName() + ", table:" + getTableName() + " , is not writeable.");
		}
    	remove = true;
    }
    
    public String toString() {
		return " ProfileEntity( profileName = "+getProfileName()+" , tableName = "+tableName+" , create = "+create+" , dirty = "+dirty+ " , readOnly = "+readOnly+" , remove = "+remove+" )";
	}    
        
    public static <T> T makeDeepCopy(final T orig) {
    	
     if(System.getSecurityManager()!=null)
     {
    	 try {
			return (T) AccessController.doPrivileged(new PrivilegedExceptionAction(){

				public Object run() throws Exception {
					
					return _makeDeepCopy(orig);
				}});
		} catch (PrivilegedActionException e) {
			if(e.getCause() instanceof RuntimeException)
			{
				throw (RuntimeException) e.getCause();
			}else
			{
				throw new SLEEException("Failed to create copy of CMP object.", e);
			}
		}
     }else
     {
    	 return _makeDeepCopy(orig);
     }
    }    
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
       		  throw new SLEEException("Failed to create copy of CMP object.", e);
       	  }
         }
         return copy;
    }

}
