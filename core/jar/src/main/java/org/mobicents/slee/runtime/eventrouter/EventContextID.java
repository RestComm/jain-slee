package org.mobicents.slee.runtime.eventrouter;

import java.io.Serializable;

import org.mobicents.slee.runtime.activity.ActivityContextHandle;

/**
 * 
 * @author martins
 *
 */
public class EventContextID implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the handle of the activity context related with the event context
	 */
	private final ActivityContextHandle ach;
	
	/**
	 * the event object related with the event context
	 */
	private final Object eventObject;
	
	public EventContextID(ActivityContextHandle ach, Object eventObject) {
		this.ach = ach;
		this.eventObject = eventObject;
	}
	
	public ActivityContextHandle getActivityContextHandle() {
		return ach;
	}
	
	public Object getEventObject() {
		return eventObject;
	}
	
	@Override
	public int hashCode() {
		return ach.hashCode()*31+ eventObject.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			EventContextID other = (EventContextID) obj;
			return ((this.eventObject.equals(other.eventObject)) && (this.ach.equals(other.ach)));
		}
		else {
			return false;
		}
	}
}
