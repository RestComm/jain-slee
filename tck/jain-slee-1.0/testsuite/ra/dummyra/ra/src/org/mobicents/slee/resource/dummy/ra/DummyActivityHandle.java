/*
 * DummyActivityHandle.java
 *
 * Created on 14 Декабрь 2006 г., 10:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.mobicents.slee.resource.dummy.ra;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;
import org.mobicents.slee.resource.dummy.ratype.DummyActivity;

/**
 *
 * @author Oleg Kulikov
 */
public class DummyActivityHandle implements ActivityHandle, Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String handle = null;
    private DummyActivity activity;
    
    public DummyActivityHandle(String id) {
        this.handle = id;
    }

    public DummyActivityHandle(DummyActivity activity) {
        this.handle = activity.getId();
        this.activity = activity;
    }
    
    public DummyActivity getActivity() {
        return activity;
    }
    
    public boolean equals(Object o) {
    	if (o != null && o.getClass() == this.getClass()) {
			return ((DummyActivityHandle)o).handle.equals(this.handle);
		}
		else {
			return false;
		}
    }
       
    public int hashCode() {
        return handle.hashCode();
    }       
    
}
