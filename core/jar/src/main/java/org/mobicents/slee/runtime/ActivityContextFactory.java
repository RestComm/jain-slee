
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
package org.mobicents.slee.runtime;

import java.util.Collection;
import java.util.Set;


/** 
 * Activity context factory implementation interface.
 * 
 * @author F.Moggia
 * 
 */
public interface ActivityContextFactory {
	
	/** Get an activity context or create one if one does not
	 * exist
	 * @param activity -- activity for which to create the activity context
	 * @return created activity context.
	 */
    public ActivityContext getActivityContext(Object activity);
    
    /** Remove an activity context from the hash table given the
     * activity.
     * @param activity
     */
    
    public void removeActivityContext(String activityContextId);
    
   
    /** Get an activity context given it's key.
     * Does not create one if it is not already there
     * @author Tim
     *
     */
    public ActivityContext getActivityContextById(String key);
    
    public String getActivityContextId(Object activity);

    public Object getActivityFromKey(String key);
    
    /**
     * @return Set of all registered SLEE activity context ids
     */
    public Set getAllActivityContextsIds();
    
}
