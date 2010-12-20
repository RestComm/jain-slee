/**
 * 
 */
package org.mobicents.slee.runtime.facilities.nullactivity;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.facilities.nullactivity.NullActivityHandle;

/**
 * @author martins
 * 
 */
public class NullActivityContextHandle implements ActivityContextHandle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private NullActivityHandle activityHandle;
	private NullActivityImpl activityObject;

	/**
	 * not to be used, needed due to externalizable
	 */
	public NullActivityContextHandle() {

	}

	/**
	 * 
	 */
	public NullActivityContextHandle(NullActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle
	 * ()
	 */
	public NullActivityHandle getActivityHandle() {
		return activityHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityObject
	 * ()
	 */
	public Object getActivityObject() {
		if (activityObject == null) {
			activityObject = new NullActivityImpl(activityHandle);
		}
		return activityObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType
	 * ()
	 */
	public ActivityType getActivityType() {
		return ActivityType.NULL;
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
			final NullActivityContextHandle other = (NullActivityContextHandle) obj;
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
		this.activityHandle = new NullActivityHandleImpl(in.readUTF());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.Externalizable#writeExternal(java.io.ObjectOutput)
	 */
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(((NullActivityHandleImpl) activityHandle).getId());
	}

}
