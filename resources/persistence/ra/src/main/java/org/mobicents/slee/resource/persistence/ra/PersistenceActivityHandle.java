package org.mobicents.slee.resource.persistence.ra;

import javax.slee.resource.ActivityHandle;


import org.mobicents.slee.resource.persistence.ratype.SbbEntityManager;


public class PersistenceActivityHandle implements ActivityHandle {

	
	private String id=null;
	
	
	public PersistenceActivityHandle(SbbEntityManager em)
	{
		id=em.getID();
	}
	
	/* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((PersistenceActivityHandle)o).id.equals(this.id);
		}
		else {
			return false;
		}
    }
    
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {

        return id.hashCode();
    }
    
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {

        return this.id.trim();
    }
	
	
	
	
	
}
