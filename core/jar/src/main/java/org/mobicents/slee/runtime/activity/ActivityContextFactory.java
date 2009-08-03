
/*
 * Created on Jul 8, 2004
 *
 * The Open SLEE project
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.runtime.activity;

import java.util.Set;

import javax.slee.resource.ActivityAlreadyExistsException;

/** 
 * Activity context factory implementation interface.
 * 
 * @author F.Moggia
 * @author Eduardo Martins
 * 
 */
public interface ActivityContextFactory {
	
	/**
	 * Retrieves the {@link ActivityContext} for the specified {@link ActivityContextHandle}.
	 * @param ach
	 * @return null if no such activity context exists
	 */
    public ActivityContext getActivityContext(ActivityContextHandle ach);
    
    /**
     * Removes the {@link ActivityContext} for the specified {@link ActivityContextHandle}.
     * @param ac
     */
    public void removeActivityContext(ActivityContext ac);
   
    /**
     * Convenience method which results in invoking createActivityContext(ach,ActivityFlags.NO_FLAGS)
     * @param activityContext
     * @throws ActivityAlreadyExistsException 
     */
    public ActivityContext createActivityContext(ActivityContextHandle ach) throws ActivityAlreadyExistsException;
    
    /**
     * 
     * @param activityContext
     * @param activityFlags
     * @throws ActivityAlreadyExistsException 
     */
    public ActivityContext createActivityContext(ActivityContextHandle ach, int activityFlags) throws ActivityAlreadyExistsException;
    
    /**
     * @return Set of all registered SLEE activity context handles
     */
    public Set<ActivityContextHandle> getAllActivityContextsHandles();
    
}
