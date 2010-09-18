package org.mobicents.slee.resource.sip11;

import java.io.Serializable;

import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * Base class for SIP RA activity handles, which provides a link to the related activity object.
 * 
 * @author martins
 *
 */
public abstract class SipActivityHandle implements ActivityHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @return
	 */
	public abstract boolean isReplicated();
	
	/**
	 * the activity related with the handle
	 */
	private transient Wrapper activity;

	/**
	 * Retrieves the activity related with the handle. 
	 * @return
	 */
	public Wrapper getActivity() {
		return activity;
	}

	/**
	 * Sets the activity related with the handle.
	 * @param activity
	 */
	public void setActivity(Wrapper activity) {
		this.activity = activity;
	}
	
}
