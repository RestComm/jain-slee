package org.mobicents.slee.runtime.eventrouter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.resource.EventFlags;

import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.util.concurrent.ConcurrentHashSet;

/**
 * Manages references to events fired by RAs with {@link EventFlags#REQUEST_EVENT_UNREFERENCED_CALLBACK}
 * 
 * @author martins
 *
 */
public class DeferredEventReferencesManagement {

	private final ConcurrentHashMap<Object, DeferredEventReferences> eventsBeingManaged = new ConcurrentHashMap<Object, DeferredEventReferences>();
	
	public void manageReferencesForEvent(DeferredEvent de) {		 
		eventsBeingManaged.put(de.getEvent(),new DeferredEventReferences(de));
	}
	
	public void unmanageReferencesForEvent(DeferredEvent de) {		 
		eventsBeingManaged.remove(de.getEvent());
	}
	
	public void eventReferencedByActivity(Object event, ActivityContextHandle ach) {
		DeferredEventReferences der = eventsBeingManaged.get(event);
		if (der != null) {
			der.getActivitiesReferencing().add(ach);
		}
	}
	
	public void eventUnreferencedByActivity(Object event, ActivityContextHandle ach) {
		DeferredEventReferences der = eventsBeingManaged.get(event);
		if (der != null) {
			der.getActivitiesReferencing().remove(ach);
			if (der.getActivitiesReferencing().isEmpty()) {
				eventsBeingManaged.remove(event);
				der.getDeferredEvent().eventUnreferenced();
			}
		}
	}
		
	private class DeferredEventReferences {
		
		private final DeferredEvent de;
		
		private final Set<ActivityContextHandle> activitiesReferencing = new ConcurrentHashSet<ActivityContextHandle>();
		
		public DeferredEventReferences(DeferredEvent de) {
			this.de = de;
			activitiesReferencing.add(de.getActivityContextHandle());
		}
		
		public DeferredEvent getDeferredEvent() {
			return de;
		}
		
		public Set<ActivityContextHandle> getActivitiesReferencing() {
			return activitiesReferencing;
		}
		
		@Override
		public int hashCode() {
			return de.getEvent().hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj != null && obj.getClass() == this.getClass()) {
				return ((DeferredEventReferences)obj).de.getEvent().equals(this.de.getEvent());
			}
			else {
				return false;
			}
		}
	}
}
