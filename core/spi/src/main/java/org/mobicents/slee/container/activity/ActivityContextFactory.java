/**
 * 
 */
package org.mobicents.slee.container.activity;

import java.util.Set;

import javax.slee.resource.ActivityAlreadyExistsException;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 * 
 */
public interface ActivityContextFactory extends SleeContainerModule {

	/**
	 * 
	 * @param activityContext
	 * @param activityFlags
	 * @throws ActivityAlreadyExistsException
	 */
	public ActivityContext createActivityContext(ActivityContextHandle ach,
			int activityFlags) throws ActivityAlreadyExistsException;

	/**
	 * Retrieves the {@link ActivityContext} for the specified
	 * {@link ActivityContextHandle}.
	 * 
	 * @param ach
	 * @return null if no such activity context exists
	 */
	public ActivityContext getActivityContext(ActivityContextHandle ach);

	/**
	 * @return Set of all registered SLEE activity context handles
	 */
	public Set<ActivityContextHandle> getAllActivityContextsHandles();

	/**
	 * Retrieves the local activity context view for the {@link ActivityContext}
	 * with the specified handle
	 * 
	 * @param ach
	 * @param create
	 *            indicates if the local ac should be created, when it is not
	 *            found
	 * @return
	 */
	public LocalActivityContext getLocalActivityContext(
			ActivityContextHandle ach, boolean create);

	/**
	 * @return
	 */
	public int getActivityContextCount();

}
