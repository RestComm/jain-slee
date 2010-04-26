package org.mobicents.slee.runtime.facilities.nullactivity;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityHandle;

/**
 * Handle for a null activity.
 * 
 * @author martins
 *
 */
public class NullActivityHandleImpl implements NullActivityHandle {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final String id;

    public NullActivityHandleImpl(String id) {
		this.id = id;
	}
    
    /* (non-Javadoc)
     * @see org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle#getActivityContextHandle()
     */
    public ActivityContextHandle getActivityContextHandle() {
    	return new NullActivityContextHandle(this);
    }
    
	public boolean equals(Object obj) {
		if ((obj != null) && (obj.getClass() == this.getClass())) {
			return this.id.equals(((NullActivityHandleImpl) obj).id);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}
    
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {		
		return id;
	}
}
