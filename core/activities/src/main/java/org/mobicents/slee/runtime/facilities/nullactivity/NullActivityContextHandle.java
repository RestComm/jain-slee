/**
 * 
 */
package org.mobicents.slee.runtime.facilities.nullactivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
	
	private transient NullActivityHandle activityHandle;
	
	private transient NullActivityImpl activityObject;
	
	/**
	 * 
	 */
	public NullActivityContextHandle(NullActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityHandle()
	 */
	public NullActivityHandle getActivityHandle() {
		return activityHandle;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityObject()
	 */
	public Object getActivityObject() {
		if (activityObject == null) {
			activityObject = new NullActivityImpl(activityHandle);
		}
		return activityObject;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.activity.ActivityContextHandle#getActivityType()
	 */
	public ActivityType getActivityType() {
		return ActivityType.NULL;
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
			final NullActivityContextHandle other = (NullActivityContextHandle) obj;
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
		return new StringBuilder("ACH=").append(getActivityType()).append('>').append(activityHandle).toString(); 		
	}
	
	// serialization

	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeUTF(((NullActivityHandleImpl)activityHandle).getId());
	}

	private void readObject(ObjectInputStream stream) throws IOException,
				ClassNotFoundException {
		stream.defaultReadObject();
		this.activityHandle = new NullActivityHandleImpl(stream.readUTF());
	} 
}
