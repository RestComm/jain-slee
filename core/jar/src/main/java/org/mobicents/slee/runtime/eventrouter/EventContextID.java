package org.mobicents.slee.runtime.eventrouter;

import java.io.Serializable;

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
	 * the id of the activity context related with the event context
	 */
	private final String activityContextID;
	
	/**
	 * the event object related with the event context
	 */
	private final Object eventObject;
	
	public EventContextID(String activityContextID, Object eventObject) {
		this.activityContextID = activityContextID;
		this.eventObject = eventObject;
	}
	
	public String getActivityContextID() {
		return activityContextID;
	}
	
	public Object getEventObject() {
		return eventObject;
	}
	
	@Override
	public int hashCode() {
		return activityContextID.hashCode()*31+ eventObject.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			EventContextID other = (EventContextID) obj;
			return ((this.eventObject.equals(other.eventObject)) && (this.activityContextID.equals(other.activityContextID)));
		}
		else {
			return false;
		}
	}
}
