package org.mobicents.slee.resource.sip11;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.mobicents.slee.resource.sip11.wrappers.Wrapper;

/**
 * Implementation of {@link SipActivityManagement} for usage in a non clustered environment.
 * 
 * In a non clustered environment the activity handle has a direct reference to the activity.
 * @author martins
 *
 */
public class LocalSipActivityManagement implements SipActivityManagement {

	ConcurrentHashMap<SipActivityHandle, Wrapper> activities = new ConcurrentHashMap<SipActivityHandle, Wrapper>();
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#get(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper get(SipActivityHandle handle) {
		Wrapper wrapper = handle.getActivity();
		if (wrapper == null) {
			wrapper = activities.get(handle);
			handle.setActivity(wrapper);
		}
		return wrapper;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#put(org.mobicents.slee.resource.sip11.SipActivityHandle, org.mobicents.slee.resource.sip11.wrappers.WrapperSuperInterface)
	 */
	public void put(SipActivityHandle handle, Wrapper activity) {
		activities.put(handle, activity);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.resource.sip11.SipActivityManagement#remove(org.mobicents.slee.resource.sip11.SipActivityHandle)
	 */
	public Wrapper remove(SipActivityHandle handle) {
		final Wrapper activity = activities.remove(handle);
		handle.setActivity(null);
		return activity;
	}
	
	/**
	 * Retrieves the set of handles managed. 
	 * @return
	 */
	public Set<SipActivityHandle> handleSet() {
		return Collections.unmodifiableSet(activities.keySet());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LocalSipActivityManagement[ activities = "+handleSet()+" ]";
	}
}
