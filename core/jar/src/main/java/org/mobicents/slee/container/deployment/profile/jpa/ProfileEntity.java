package org.mobicents.slee.container.deployment.profile.jpa;

import javax.persistence.Transient;
import javax.slee.SLEEException;

import org.apache.log4j.Logger;

/**
 * The container for for a profile cmp fields
 * @author martins
 *
 */
public abstract class ProfileEntity implements Cloneable {

	protected static final Logger logger = Logger.getLogger(ProfileEntity.class);
	
	/**
	 * the profile name as stored in db
	 */
	protected String safeProfileName;
	
	/**
	 * the profile table name
	 */
	protected String tableName;
	
	/**
	 * indicates if the pojo should be removed in the end of transaction
	 */
	@Transient
	private boolean remove = false;
	
	/**
	 * indicates if the pojo should be created in the end of transaction, or its cmp fields just updated
	 */
	@Transient
	private boolean create = false;
	
	/**
	 * indicates if the pojo cmp fields values have changed since being load from persistence
	 */
	@Transient
	private boolean dirty = false;
	
	/**
	 * indicates if the pojo cmp fields are read only
	 */
	@Transient
	private boolean readOnly = false;
	
	/**
	 * Only for usage by JPA, use getProfileName() instead
	 * @return
	 */
    public String getSafeProfileName() {
        return safeProfileName;
    }

    /**
     * Only for usage by JPA, use setProfileName(String) instead
     * @param s
     */
    public void setSafeProfileName(String s) {
        safeProfileName = s;
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
     * Retrieves a shallow copy of the object
     * @return
     * @throws CloneNotSupportedException
     */
    public ProfileEntity cl0ne() {
    	try {
			return (ProfileEntity) clone();
		} catch (CloneNotSupportedException e) {
			throw new SLEEException(e.getMessage(),e);
		}
    }

    /**
     * Sets profile name
     * @return
     */
    public String getProfileName() {
    	final String s = getSafeProfileName();
    	if(s.equals(""))
    		return null;
    	else
    		return s;
    }

    /**
     * Retrieves the profile name
     * @param s
     */
    public void setProfileName(String s) {
    	if(s == null)
    		setSafeProfileName("");
    	else
    		setSafeProfileName(s);
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
    public void markAsDirty() {
    	this.dirty = true;
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
    public void remove() {
    	remove = true;
    }
    
    public String toString() {
		return " ProfileEntity( profileName = "+getProfileName()+" , tableName = "+tableName+" , create = "+create+" , dirty = "+dirty+ " , readOnly = "+readOnly+" , remove = "+remove+" )";
	}
}
