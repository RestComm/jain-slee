package org.mobicents.slee.container.profile.entity;

import javax.slee.profile.ReadOnlyProfileException;

/**
 * The container for for a profile cmp fields
 * @author martins
 *
 */
public abstract class ProfileEntity {

	
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
	 * 
	 * @return
	 */
    public String getProfileName() {
        return profileName;
    }

    /**
     * 
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
		return "ProfileEntity( profileName = "+getProfileName()+" , tableName = "+tableName+" , create = "+create+" , dirty = "+dirty+ " , readOnly = "+readOnly+" , remove = "+remove+" )";
	}    
        
    
}
