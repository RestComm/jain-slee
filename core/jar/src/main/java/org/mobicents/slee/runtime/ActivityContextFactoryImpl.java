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
package org.mobicents.slee.runtime;

import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.facilities.NullActivityContext;
import org.mobicents.slee.runtime.facilities.NullActivityImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/*
 * 
 * Francesco Moggia - Initial version M. Ranganathan - Initial version.
 * Transaction Isolation fixes. Tim Fox - Make class thread-safe and being able
 * to look-up based on key.
 */

/**
 * Activity context factory -- return an activity context given an activity or
 * create one and put it in the table. This also implements the activity context
 * naming facility for the SLEE.
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Tim Fox
 * @author eduardomartins
 * 
 * 
 */
public class ActivityContextFactoryImpl implements ActivityContextFactory {

	private static Logger log;

	static {
		log = Logger.getLogger(ActivityContextFactoryImpl.class);

	}

	/* A map of activity context ID to activity context */
	private static String ACID_AC_MAP = "ActivityContextFactoryImpl:IdToAc";
	private static String A_ID_MAP = "ActivityContextFactoryImpl:AToId";
	
	private Map idToAcCacheMap;
	private Map activityToIdCacheMap;
	
	private static ConcurrentHashMap<Object, String>uncommittedActivityToIdMap = new ConcurrentHashMap<Object, String>();
	
	private SleeTransactionManager tm;

	public ActivityContextFactoryImpl(SleeContainer container) {
		tm = SleeContainer.getTransactionManager();		
		idToAcCacheMap = new CacheableMap(ACID_AC_MAP);
		activityToIdCacheMap = new CacheableMap(A_ID_MAP);
	}	
	
	/**
	 * Get an activity context for the activity. If one doesn't already exist -
	 * create one.
	 */
	public ActivityContext getActivityContext(Object activity) {

		if (log.isDebugEnabled())
			log.debug("getActivityContext(): for Object " + activity);

		if (activity == null) {
			throw new RuntimeException("an activity is null");
		}
		
		tm.mandateTransaction();
		
		boolean refresh = true;
		
		String id = (String) activityToIdCacheMap.get(activity);
		
		if (id == null) {
			// id is not, try looking at uncommited mapping, using that id
			// mapping can prevent concurrency issues, when a sbb creates a new
			// AC for a RA activity and the RA fires an event on that activity
			// before the sbb tx ends
			id = uncommittedActivityToIdMap.get(activity);
		}

		return getActivityContext(id,activity,true,refresh);
        
    }

	/**
	 * Get an activity context for the activity. If one doesn't already exist -
	 * create one.	
	 * @param id - activity context id to fetch
	 * @param activity - activity that should be masked by ac
	 * @param refresh - flag indicating wheather access time should become last timestamp of ac access
	 * @return ac
	 * @throws Exception
	 */
	private ActivityContext getActivityContext(String id, Object activity, boolean createIfNotExist, boolean refresh) {

        if (id == null) {
			// create a new one if required
			ActivityContext ac = null;
			if (createIfNotExist) {
				ac = createActivityContext(activity, refresh);
			}
			return ac;
		} else {
			// retrieve
			if (log.isDebugEnabled())
				log.debug("Retreiveing AC with id " + id);
			ActivityContext ac = (ActivityContext) idToAcCacheMap.get(id);
			if (ac == null) {
				if (createIfNotExist) {
					ac = createActivityContext(activity, id, refresh);
				}
			} else {
				if (refresh) {
					ac.updateLastAccessTime();
				}
			}

			return ac;
		}

	}

	/**
	 * Looks up an AC object by its ID, updates ac last access time to current
	 * time
	 */
	public ActivityContext getActivityContextById(String id) {
		// tx mandated inside inner invocation
		return getActivityContextById(id,true);
	}

	/**
	 * Looks up an AC object by its ID, updates ac access time to current
	 * timestamp
	 * 
	 * @param id -
	 *            the ActivityContext id
	 * @param updateAccessTime -
	 *            if true current timestamp will become last access time to AC
	 * @return
	 */
	public ActivityContext getActivityContextById(String id, boolean updateAccessTime) {
		tm.mandateTransaction();
		return getActivityContext(id,null,false,updateAccessTime);
	}
	
	public Set getAllActivityContextsIds() {
		tm.mandateTransaction();
		return new HashSet(idToAcCacheMap.keySet());
	}
	
	/**
	 * Remove the activity context from the table.
	 * 
	 * @param activity --
	 *            activity for which the ActivityContext is removed.
	 * 
	 */
	public void removeActivityContext(String id) {
	
		tm.mandateTransaction();
		if (id != null) {
			if (log.isDebugEnabled())
				log.debug("Removing AC with id " + id);
			ActivityContext activityContext = (ActivityContext) idToAcCacheMap
					.remove(id);
			if (activityContext != null) {
				activityContext.markForRemove();
			}
			activityToIdCacheMap.remove(activityContext.getActivity());
		}
	}
	
	/*
	 * This method is only for testing
	 * 
	 */
	public int getActivityContextCount() {
		tm.mandateTransaction();
		return idToAcCacheMap.size();
	}

	public String getActivityContextId(Object activity) {
		tm.mandateTransaction();
		return (String) this.activityToIdCacheMap.get(activity); 
	}

	public Object getActivityFromKey(String key) {
		tm.mandateTransaction();
		ActivityContext ac = getActivityContextById(key);
		if (ac != null) {
			return ac.getActivity();
		}
		else {
			return null;
		}
	}

	/**
	 * Creates a new ActivityContext instance with the specified activity
	 * object and register it in the factory. The id is generated.
	 * 
	 * @param activity
	 */
	private ActivityContext createActivityContext(Object activity, boolean refresh) {
		
		String id = createNewActivityContextId(activity);
		ActivityContext ac = this.createActivityContext(activity,id,refresh);
		
		// make the mapping visible to other tx, this will help prevent a concurrency issue here
		uncommittedActivityToIdMap.put(activity, id);
		final Object finalActivity = activity;
		TransactionalAction txAction = new TransactionalAction() {

			private Object activity =  finalActivity;
			
			public void execute() {
				uncommittedActivityToIdMap.remove(activity);
			}
		};

		tm.addAfterCommitAction(txAction);
		tm.addAfterRollbackAction(txAction);
		
		return ac;
	
	}

	/**
	 * Creates a new ActivityContext instance with the specified id and activity
	 * object and register it in the factory.
	 * 
	 * @param activity
	 * @param id
	 */
	public ActivityContext createActivityContext(Object activity, String id) {
		tm.mandateTransaction();
		return createActivityContext(activity,id,true);
	}
	
	/**
	 * Creates a new ActivityContext instance with the specified id and activity
	 * object and register it in the factory.
	 * 
	 * @param activity
	 * @param id
	 */
	private ActivityContext createActivityContext(Object activity, String id, boolean refresh) {
		
		//if null activity the ac id must be set here because of hashcode
		boolean nullActivity = false;
		if(activity instanceof NullActivityImpl) {
			((NullActivityImpl)activity).setActivityContextId(id);
			nullActivity = true;
		}		
		    
		if (log.isDebugEnabled())
			log.debug("createActivityContext(activity="+activity+", id=" + id+")");        	

		ActivityContext ac = null;
		if(!nullActivity) {
			ac = new ActivityContext(id, activity,refresh);
		}
		else {
			ac =  new NullActivityContext(id, activity,refresh);
		}
		idToAcCacheMap.put(id,ac);
		activityToIdCacheMap.put(activity,id);

		return ac;        	
        	
        
	}
	
	public String createNewActivityContextId(Object activity) {		
		return new VMID().toString();
	}
	
	@Override
	public String toString() {
		return 	"Activity Context Factory: " +
				"\n+-- Activities size: " + activityToIdCacheMap.size() +
				"\n+-- AC IDs sizes: " + idToAcCacheMap.size();
	}

}