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
public class SipActivityHandle implements ActivityHandle, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the activity related with the handle
	 */
	private transient Wrapper activity;

	/**
	 * the ra object that owns the handle
	 */
	private transient SipResourceAdaptor ra;
	
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
	
	/**
	 * Retrieves the ra object that owns the handle. 
	 * @return
	 */
	public SipResourceAdaptor getResourceAdaptor() {
		return ra;
	}
	
	/**
	 * Sets the ra object that owns the handle.
	 * @param ra
	 */
	public void setResourceAdaptor(SipResourceAdaptor ra) {
		this.ra = ra;
	}
}
