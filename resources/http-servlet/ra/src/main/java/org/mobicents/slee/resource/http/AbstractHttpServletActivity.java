package org.mobicents.slee.resource.http;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

/**
 * Base class for an activity (which is also the handle) of the {@link HttpServletResourceAdaptor}.
 * 
 * @author martins
 *
 */
public abstract class AbstractHttpServletActivity implements ActivityHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final String id;
	
	public AbstractHttpServletActivity(String id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((AbstractHttpServletActivity)obj).id.equals(this.id);
		}
		else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return id;
	}
	
}
