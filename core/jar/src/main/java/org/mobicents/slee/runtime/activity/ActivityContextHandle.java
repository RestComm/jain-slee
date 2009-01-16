package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.UnrecognizedServiceException;
import javax.slee.management.ServiceState;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceAdaptor;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.ServiceManagement;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityFactoryImpl;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityImpl;
import org.mobicents.slee.runtime.sbbentity.RootSbbEntitiesRemovalTask;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityHandle;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityImpl;

/**
 * The handle for an {@link ActivityContext}.
 * Useful to understand what is the source or the type of the related activity, or even get that activity object.
 * 
 * @author martins
 *
 */
public class ActivityContextHandle {
	
	private static final Logger logger = Logger.getLogger(ActivityContextHandle.class);
	
	private static ResourceManagement _resourceManagement = null;
	private static ResourceManagement getResourceManagement() {
		if (_resourceManagement == null) {
			_resourceManagement = SleeContainer.lookupFromJndi().getResourceManagement();			
		}
		return _resourceManagement;
	}
		
	private static ServiceManagement _serviceManagement = null;
	private static ServiceManagement getServiceManagement() {
		if (_serviceManagement == null) {
			_serviceManagement = SleeContainer.lookupFromJndi().getServiceManagement();			
		}
		return _serviceManagement;
	}
	
	private static SleeProfileManager _sleeProfileManager = null;
	private static SleeProfileManager getSleeProfileManager() {
		if (_sleeProfileManager == null) {
			_sleeProfileManager = SleeContainer.lookupFromJndi().getSleeProfileManager();			
		}
		return _sleeProfileManager;
	}
	
	private static NullActivityFactoryImpl _nullActivityFactoryImpl = null;
	private static NullActivityFactoryImpl getNullActivityFactoryImpl() {
		if (_nullActivityFactoryImpl == null) {
			_nullActivityFactoryImpl = SleeContainer.lookupFromJndi().getNullActivityFactory();			
		}
		return _nullActivityFactoryImpl;
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
			return other.activityType == this.activityType 
				&& other.activitySource.equals(this.activitySource)
				&& other.activityHandle.equals(this.activityHandle);
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
			// profile table activity, clean up
			getSleeProfileManager().removeProfileAfterTableActivityEnd(((ProfileTableActivity) getActivity()).getProfileTableName());
			break;
			
		case serviceActivity:
			// service activity ending
			ServiceActivityImpl serviceActivity = (ServiceActivityImpl) getActivity();			
			// change service state to inactive
			try {
				getServiceManagement().getService(serviceActivity.getService()).setState(ServiceState.INACTIVE);
				// schedule task to remove outstanding root sbb entities of the service
				new RootSbbEntitiesRemovalTask(serviceActivity.getService());
				Logger.getLogger(ServiceManagement.class).info("Deactivated "+ serviceActivity.getService());					
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
		return "activity context handle : type="+activityType+", source="+activitySource+", handle"+activityHandle;
	}
	
}
