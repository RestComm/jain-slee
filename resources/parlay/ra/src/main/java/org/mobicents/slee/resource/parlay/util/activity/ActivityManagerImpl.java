package org.mobicents.slee.resource.parlay.util.activity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.CouldNotStartActivityException;
import javax.slee.resource.SleeEndpoint;

/**
 *
 * Class Description for ActivityManagerImpl
 */
public class ActivityManagerImpl implements ActivityManager {
    private static final String FAILED_ENDING_SLEE_ACTIVITY = "Failed ending SLEE activity.";

    private static final String FAILED_STARTING_SLEE_ACTIVITY = "Failed starting SLEE activity.";

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
        .getLog(ActivityManagerImpl.class);
    
    public ActivityManagerImpl(SleeEndpoint sleeEndpoint) {
        this.sleeEndpoint = sleeEndpoint;
        
        activities = new HashMap();
        handles = new HashMap();
    }

    private final transient SleeEndpoint sleeEndpoint;
    
    private final transient Map activities;
    
    private final transient Map handles;

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#activityEnded(javax.slee.resource.ActivityHandle)
     */
    public void activityEnded(ActivityHandle activityHandle) {
        // TODO activityEnded
        if(logger.isDebugEnabled()) {
            logger.debug("Received activityEnded()");
        }

        // RA should release any activity resources here.
        
        // As we have many types of activity this is activity type specific.
        // e.g. in a IpUI the operation would be release() in an MPCCS call 
        // it is deassignCall()
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#activityUnreferenced(javax.slee.resource.ActivityHandle)
     */
    public void activityUnreferenced(ActivityHandle activityHandle) {
        // TODO activityUnreferenced
        if(logger.isDebugEnabled()) {
            logger.debug("Received activityUnreferenced()");
        }
        
        // No SBBs interested in activity, RA may do implicit end

    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#queryLiveness(javax.slee.resource.ActivityHandle)
     */
    public void queryLiveness(ActivityHandle activityHandle) {
        if(logger.isDebugEnabled()) {
            logger.debug("Received queryLiveness()");
        }
        
        // Query liveness, if dead call activityEnding()

    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#getActivity(javax.slee.resource.ActivityHandle)
     */
    public Object getActivity(ActivityHandle activityHandle) {
        return activities.get(activityHandle);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#getActivityHandle(java.lang.Object)
     */
    public ActivityHandle getActivityHandle(Object activity) {
        return (ActivityHandle) handles.get(activity);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#activityStarted(javax.slee.resource.ActivityHandle)
     */
    public void activityStarted(ActivityHandle activityHandle) {
        try {
            sleeEndpoint.activityStarted(activityHandle);
        }
        catch (NullPointerException e) { //NOPMD
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (IllegalStateException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (ActivityAlreadyExistsException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (CouldNotStartActivityException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#activityStartedSuspended(javax.slee.resource.ActivityHandle)
     */
    public void activityStartedSuspended(ActivityHandle activityHandle) {
        try {
            sleeEndpoint.activityStartedSuspended(activityHandle);
        }
        catch (NullPointerException e) { //NOPMD
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (IllegalStateException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (ActivityAlreadyExistsException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
        catch (CouldNotStartActivityException e) {
            logger.error(FAILED_STARTING_SLEE_ACTIVITY, e);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#activityEnding(javax.slee.resource.ActivityHandle)
     */
    public void activityEnding(ActivityHandle activityHandle) {
        try {
            sleeEndpoint.activityEnding(activityHandle);
        }
        catch (NullPointerException e) { //NOPMD
            logger.error(FAILED_ENDING_SLEE_ACTIVITY, e);
        }
        catch (IllegalStateException e) {
            logger.error(FAILED_ENDING_SLEE_ACTIVITY, e);
        }
        catch (UnrecognizedActivityException e) {
            logger.error(FAILED_ENDING_SLEE_ACTIVITY, e);
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#add(javax.slee.resource.ActivityHandle, java.lang.Object)
     */
    public void add(ActivityHandle handle, Object activity) {
        activities.put(handle, activity);
        handles.put(activity, handle);
    }

    /* (non-Javadoc)
     * @see org.mobicents.slee.resource.parlay.util.activity.ActivityManager#remove(javax.slee.resource.ActivityHandle, java.lang.Object)
     */
    public void remove(ActivityHandle handle, Object activity) {
        activities.remove(handle);
        handles.remove(activity);
    }

}
