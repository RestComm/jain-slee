/*
 * Created on Jul 8, 2004
 *
 * The Open SLEE project.
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */
package org.mobicents.slee.runtime.activity;

import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.slee.resource.ActivityAlreadyExistsException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityContext;
import org.mobicents.slee.runtime.facilities.nullactivity.NullActivityHandle;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Activity context factory -- return an activity context given an activity or
 * create one and put it in the table. This also implements the activity context
 * naming facility for the SLEE.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Tim Fox
 * @author eduardomartins second version
 * @version 2.0
 * 
 * 
 */
public class ActivityContextFactoryImpl implements ActivityContextFactory {

	private static Logger logger = Logger.getLogger(ActivityContextFactoryImpl.class);

	/**
	 * {@link ActivityContextHandle} to AC ID mapping
	 */
	private final ConcurrentHashMap<ActivityContextHandle, String> activityContexts = new ConcurrentHashMap<ActivityContextHandle, String>();
	
	/**
	 * the container for this factory
	 */
	private final SleeContainer sleeContainer;
	
	public ActivityContextFactoryImpl(SleeContainer sleeContainer) {		
		this.sleeContainer = sleeContainer;		
	}

	/*
	 * creates an instance of the activity context for specified ac handle and id
	 */
	private ActivityContext createActivityContextInstance(ActivityContextHandle ach, String acId, boolean updateAccessTime) {
		if (ach.getActivityType() == ActivityType.nullActivity) {
			return new NullActivityContext(ach,acId,updateAccessTime);
		}
		else {
			return new ActivityContext(ach,acId,updateAccessTime);
		}
	}
	
	public ActivityContext createActivityContext(final ActivityContextHandle ach) throws ActivityAlreadyExistsException {
		
		if (!activityContexts.containsKey(ach)) {
			// create id
			String acId = null;
			if (ach.getActivityType() != ActivityType.nullActivity) {
				acId = "ac:" + sleeContainer.getUuidGenerator().createUUID();
			}
			else {
				// lets reuse its id
				acId = "ac:" + ((NullActivityHandle) ach.getActivityHandle()).getId();			
			}		
			// create ac
			ActivityContext ac = createActivityContextInstance(ach,acId,true);
			// warn event router about it
			sleeContainer.getEventRouter().activityStarted(ach);
			// add rollback tx action to remove state created
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					removeActivityContextData(ach);					
				}
			};
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
			// map ach to id
			if (activityContexts.putIfAbsent(ach, acId) != null) {
				ac.markForRemove();
				throw new ActivityAlreadyExistsException(); 
			}
			return ac;
		}
		else {
			throw new ActivityAlreadyExistsException(); 
		}
	}

	public ActivityContext getActivityContext(ActivityContextHandle ach, boolean updateAccessTime) {
		String acId = activityContexts.get(ach);
		if (acId != null) {
			return createActivityContextInstance(ach,acId,updateAccessTime);
		}
		else {
			return null; 
		}
	}
	
	public ActivityContext getActivityContext(String acId, boolean updateAccessTime) {
		ActivityContextHandle ach = null;
		for (Entry<ActivityContextHandle, String> entry : activityContexts.entrySet()) {
			if (entry.getValue().equals(acId)) {
				ach = entry.getKey();
			}
		}
		if (ach != null) {
			return createActivityContextInstance(ach,acId,updateAccessTime);
		}
		else {
			return null; 
		}
	}

	public Set<ActivityContextHandle> getAllActivityContextsHandles() {
		return activityContexts.keySet();
	}

	public void removeActivityContext(final ActivityContext ac) {

		// since the ac ended we may need to warn the activity source that it has ended
		ac.getActivityContextHandle().activityEnded();
		
		// remove references to this AC in timer and ac naming facility
		ac.removeNamingBindings();
		ac.removeFromTimers(); // Spec 7.3.4.1 Step 10
		
		ac.markForRemove();
		
		removeActivityContextData(ac.getActivityContextHandle());
		
		// add rollback tx action to remove state created
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				try {
					createActivityContext(ac.getActivityContextHandle());
				} catch (ActivityAlreadyExistsException e) {
					// ignore
					if (logger.isDebugEnabled()) {
						logger.debug(e.getMessage(),e);
					}
				}					
			}
		};
		sleeContainer.getTransactionManager().addAfterRollbackAction(action);
		
		if (logger.isDebugEnabled()) {
			logger.debug("Activity context with handle "+ac.getActivityContextHandle()+" removed");
		}
		
	}

	/*
	 * removes the data related with the activity context
	 */
	private void removeActivityContextData(ActivityContextHandle ach) {
		// remove ac handle from factory
		if (activityContexts.remove(ach) != null) {
			// inform the event router, may have resources to close
			sleeContainer.getEventRouter().activityEnded(ach);			
		}
	}
	
	public int getActivityContextCount() {
		return activityContexts.size();
	}
	
	@Override
	public String toString() {
		return "ActivityContext Factory: " 
			+ "\n+-- Number of ACs: " + activityContexts.size();
	}
	
}