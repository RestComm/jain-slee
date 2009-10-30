package org.mobicents.slee.resource.sip11;

import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * The manager of the {@link SipResourceAdaptor} activities.
 * @author martins
 *
 */
public interface SipActivityManagement {

	/**
	 * 
	 * @param handle
	 * @param activity
	 */
	public void put(SipActivityHandle handle, Wrapper activity);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public Wrapper remove(SipActivityHandle handle);
	
	/**
	 * 
	 * @param handle
	 * @return
	 */
	public Wrapper get(SipActivityHandle handle);
	
}
