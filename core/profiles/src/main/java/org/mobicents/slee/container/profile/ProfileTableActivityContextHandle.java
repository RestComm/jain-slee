/**
 * 
 */
package org.mobicents.slee.container.profile;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.jgroups.Address;
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

	private ProfileTableActivityHandleImpl activityHandle;

	private ProfileTableActivityImpl activityObject;

	/**
	 * not to be used, needed due to externalizable
	 */
	public ProfileTableActivityContextHandle() {

	}

	/**
	 * 
	 */
	public ProfileTableActivityContextHandle(
			ProfileTableActivityHandleImpl activityHandle) {
		this.activityHandle = activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivity()
	 */
	public ProfileTableActivityImpl getActivityObject() {
		if (activityObject == null) {
			activityObject = new ProfileTableActivityImpl(activityHandle);
		}
		return activityObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle
	 * ()
	 */
	public ProfileTableActivityHandleImpl getActivityHandle() {
		return activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType
	 * ()
	 */
	public ActivityType getActivityType() {
		return ActivityType.PTABLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return activityHandle.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuilder("ACH=").append(getActivityType()).append('>')
				.append(activityHandle).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		this.activityHandle = new ProfileTableActivityHandleImpl(in.readUTF(),
				(Address) in.readObject());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(activityHandle.getProfileTable());
		out.writeObject(activityHandle.getClusterLocalAddress());
	}
}
