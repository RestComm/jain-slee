/**
 * 
 */
package org.mobicents.slee.container.profile;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandleImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;

/**
 * @author martins
 *
 */
public class ProfileTableActivityContextHandle implements ActivityContextHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final ProfileTableActivityHandleImpl activityHandle;
	
	private transient ProfileTableActivityImpl activityObject;
	
	/**
	 * 
	 */
	public ProfileTableActivityContextHandle(ProfileTableActivityHandleImpl activityHandle) {
		this.activityHandle = activityHandle;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivity()
	 */
	public ProfileTableActivityImpl getActivityObject() {
		if (activityObject == null) {
			activityObject = new ProfileTableActivityImpl(activityHandle);
		}
		return activityObject;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle()
	 */
	public ProfileTableActivityHandleImpl getActivityHandle() {
		return activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType()
	 */
	public ActivityType getActivityType() {
		return ActivityType.PTABLE;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		
		if (obj == null) {
			return false;
		}
		
		if (obj.getClass() == this.getClass()) {
			final ProfileTableActivityContextHandle other = (ProfileTableActivityContextHandle) obj;
			return other.activityHandle.equals(this.activityHandle);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return activityHandle.hashCode();
	}
	
	@Override
	public String toString() {
		return new StringBuilder ("ACH=").append(getActivityType()).append('>').append(activityHandle).toString(); 		
	}
}
