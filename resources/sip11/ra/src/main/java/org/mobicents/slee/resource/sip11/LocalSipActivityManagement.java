package org.mobicents.slee.resource.sip11;

import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * Implementation of {@link SipActivityManagement} for usage in a non clustered environment.
 * 
 * In a non clustered environment the activity handle has a direct reference to the activity.
 * @author martins
 *
 */
public class LocalSipActivityManagement implements SipActivityManagement {

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#get(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper get(SipActivityHandle handle) {
		return handle.getActivity();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#put(org.mobicents.slee.resource.sip11.SipActivityHandle, org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface)
	 */
	public void put(SipActivityHandle handle, Wrapper activity) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#remove(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper remove(SipActivityHandle handle) {
		final Wrapper activity = handle.getActivity();
		handle.setActivity(null);
		return activity;
	}

}
