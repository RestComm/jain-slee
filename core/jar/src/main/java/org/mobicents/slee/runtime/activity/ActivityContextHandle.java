package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceAdaptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.container.service.ServiceActivityHandle;
import org.mobicents.slee.container.service.ServiceActivityImpl;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;

/**
 * The handle for an {@link ActivityContext}.
 * Useful to understand what is the source or the type of the related activity, or even get that activity object.
 * 
 * @author martins
 *
 */
public class ActivityContextHandle {
	
	private static final Logger logger = Logger.getLogger(ActivityContextHandle.class);
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private static ResourceManagement getResourceManagement() {
		return sleeContainer.getResourceManagement();			
	}
		
	private static ServiceManagement getServiceManagement() {		
		return sleeContainer.getServiceManagement();
	}
	
	private static SleeProfileManager getSleeProfileManager() {		
		return sleeContainer.getSleeProfileManager();
	}
		
	private static NullActivityFactoryImpl getNullActivityFactoryImpl() {		
		return sleeContainer.getNullActivityFactory();
	}
	
	private ActivityHandle activityHandle;
	private String activitySource;
	private ActivityType activityType;
	
	protected ActivityContextHandle(ActivityType activityType, String activitySource, ActivityHandle activityHandle) {
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
			if(other.activityType == this.activityType && other.activityHandle.equals(this.activityHandle)) {
				// only compare the source if the activity type is external
				if (this.activityType == ActivityType.externalActivity) {
					return other.activitySource.equals(this.activitySource);
				}
				else {
					return true;
				}
			}
			else {
				return false;
			}				
		}
		else {
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
				final ResourceAdaptorEntity raEntity = getResourceManagement()
						.getResourceAdaptorEntity(activitySource);
				final ResourceAdaptor ra = raEntity.getResourceAdaptor();
				activity = ra.getActivity(getActivityHandle());
			} catch (Exception e) {
				throw new SLEEException(e.getMessage(), e);
			}
			break;
		case nullActivity:
			activity = new NullActivityImpl((NullActivityHandle)activityHandle);
			break;
		case profileTableActivity:
			activity =  new ProfileTableActivityImpl(
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
	
	public void activityEnded() {
		
		// check activity type
		switch (activityType) {
		
		case externalActivity:
			// external activity, notify RA that the activity has ended
			try {
				ResourceAdaptorEntity raEntity = getResourceManagement().getResourceAdaptorEntity(activitySource);
				ResourceAdaptor ra = raEntity.getResourceAdaptor();
				if (ra != null) {
					ra.activityEnded(getActivityHandle());
				}
				else {
					logger.warn("Unable to find RA for notifaction that activity "+this+" has ended");
				}
			} catch (UnrecognizedResourceAdaptorEntityException e) {
				logger.warn("Unable to find RA Entity for notifaction that activity "+this+" has ended",e);
			}
			break;
		
		case nullActivity:
			// null activity, warn the factory
			getNullActivityFactoryImpl().activityEnded((NullActivityHandle)activityHandle);
			break;
			
		case profileTableActivity:
			// do nothing
			break;
			
		case serviceActivity:
			ServiceActivityImpl serviceActivity = (ServiceActivityImpl) getActivity();			
			
			try {
				// change service state to inactive if it is stopping
				Service service = getServiceManagement().getService(serviceActivity.getService());
				if (service.getState().isStopping()) {
					service.setState(ServiceState.INACTIVE);
					// schedule task to remove outstanding root sbb entities of the service
					new RootSbbEntitiesRemovalTask(serviceActivity.getService());
					Logger.getLogger(ServiceManagement.class).info("Deactivated "+ serviceActivity.getService());
				}
			} catch (UnrecognizedServiceException e) {
				logger.error("Unable to find "+serviceActivity.getService()+" to deactivate",e);
			}
			
			break;

		default:
			throw new SLEEException("Unknown activity type " + activityType);
		}
		
	}
	
	@Override
	public String toString() {
		return "ac handle : type="+activityType+", source="+activitySource+", handle="+activityHandle;
	}
	
}
