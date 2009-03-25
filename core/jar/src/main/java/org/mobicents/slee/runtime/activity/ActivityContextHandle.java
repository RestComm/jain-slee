package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;

/**
 * The handle for an {@link ActivityContext}. Useful to understand what is the
 * source or the type of the related activity, or even get that activity object.
 * 
 * @author martins
 * 
 */
public class ActivityContextHandle {

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	private static ResourceManagement getResourceManagement() {
		return sleeContainer.getResourceManagement();
	}

	private ActivityHandle activityHandle;
	private String activitySource;
	private ActivityType activityType;

	protected ActivityContextHandle(ActivityType activityType,
			String activitySource, ActivityHandle activityHandle) {
		this.activityHandle = activityHandle;
		this.activitySource = activitySource;
		this.activityType = activityType;
	}

	public ActivityHandle getActivityHandle() {
		return activityHandle;
	}

	public String getActivitySource() {
		return activitySource;
	}

	public ActivityType getActivityType() {
		return activityType;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			ActivityContextHandle other = (ActivityContextHandle) obj;
			if (other.activityType == this.activityType
					&& other.activityHandle.equals(this.activityHandle)) {
				// only compare the source if the activity type is external
				if (this.activityType == ActivityType.externalActivity) {
					return other.activitySource.equals(this.activitySource);
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = activityHandle.hashCode();
		result = prime * result + activitySource.hashCode();
		result = prime * result + activityType.hashCode();
		return result;
	}

	public Object getActivity() {

		Object activity = null;

		switch (activityType) {
		case externalActivity:
			try {
				activity = getResourceManagement().getResourceAdaptorEntity(
						activitySource).getResourceAdaptorObject().getActivity(
						getActivityHandle());
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(), e);
			}
			break;
		case nullActivity:
			activity = new NullActivityImpl((NullActivityHandle) activityHandle);
			break;
		case profileTableActivity:
			activity = new ProfileTableActivityImpl(
					(ProfileTableActivityHandle) getActivityHandle());
			break;
		case serviceActivity:
			activity = ServiceActivityFactoryImpl
					.getActivity(((ServiceActivityHandle) activityHandle)
							.getServiceID());
			break;
		default:
			throw new SLEEException("Unknown activity type " + activityType);
		}

		return activity;
	}

	@Override
	public String toString() {
		return "ac handle : type=" + activityType + ", source="
				+ activitySource + ", handle=" + activityHandle;
	}

}