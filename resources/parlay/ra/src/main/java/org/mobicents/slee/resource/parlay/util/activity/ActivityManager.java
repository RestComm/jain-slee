package org.mobicents.slee.resource.parlay.util.activity;

import javax.slee.resource.ActivityHandle;

/**
 * Classes implementing this interface will be updated by the Parlay
 * services in the RA when activities are created or destroyed.
 * 
 */
public interface ActivityManager {

    void activityEnded(ActivityHandle activityHandle);

    void activityUnreferenced(ActivityHandle activityHandle);
    
    void queryLiveness(ActivityHandle activityHandle);

    Object getActivity(ActivityHandle activityHandle);
  
    ActivityHandle getActivityHandle(Object activity);
    
    void activityStarted(ActivityHandle activityHandle);
    
    void activityStartedSuspended(ActivityHandle activityHandle);
    
    void activityEnding(ActivityHandle activityHandle);
    
    void add(ActivityHandle handle, Object activity);
    
    void remove(ActivityHandle handle, Object activity);
}
