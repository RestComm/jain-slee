package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.NotCompliantMBeanException;
import javax.slee.SbbID;
import javax.slee.facilities.TimerID;
import javax.slee.management.ManagementException;
import javax.slee.nullactivity.NullActivity;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityType;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;

/**
 * This class provides minimum information for management console. It also tries
 * to clean container of dead activities
 * 
 * @author Bartosz Baranowski
 * @author Eduardo Martins
 * 
 */
@SuppressWarnings("unchecked")
public class ActivityManagementMBeanImpl extends ServiceMBeanSupport
		implements ActivityManagementMBeanImplMBean {

	// Name convention is to conform to other mbeans - also ServiceMBeanSupport
	// is not StandadMBean which allows
	// Passing MBean interface class which enables custom names this is why
	// MBean interface has sufix - MBeanImplMbean
	
	// 10 minutes interval between querries
	private long querryInterval = 600 * 1000;

	private long maxActivityIdleTime = 600 * 1000;

	private ActivityContextFactoryImpl acFactory = null;

	private org.mobicents.slee.runtime.transaction.SleeTransactionManager txMgr = null;

	private SleeContainer container = null;

	private TimerTask currentQuestioner = null;

	private Timer queryRunner = new Timer("MOBICENTS_ACTIVITY_LIVELINESS_TIMER");

	private static Logger logger = Logger
			.getLogger(ActivityManagementMBeanImpl.class);

	public ActivityManagementMBeanImpl() throws NotCompliantMBeanException {
		// super(ActivityManagementMBeanImpl.class);
		
	}

	// === PROPERTIES

	public int getActivityContextCount() {
		// prepareBean();
		boolean newtx = txMgr.requireTransaction();
		try {
			return this.acFactory.getActivityContextCount();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		finally {
			if (newtx) {
				try {
					txMgr.rollback();
				} catch (SystemException e) {
					logger.error("Failed to rollback new tx for retreival of activity context count",e);
				}
			}
		}
	}

	public long getQueryActivityContextLivelinessPeriod() {

		return this.querryInterval;
	}

	public void setQueryActivityContextLivelinessPeriod(long set) {

		long originalV = this.querryInterval;

		if (set <= 100)
			this.querryInterval = 15000; // 15 secs
		else
			this.querryInterval = set;

		logger.info("Reinitiating liveliness query to run ["
				+ !(querryInterval == 0) + "]");
		if (currentQuestioner.cancel()) {
			if (querryInterval == 0) {
				return;
			}
			currentQuestioner = new PeriodicLivelinessScanner();
			queryRunner.schedule(currentQuestioner, querryInterval);
		} else if (originalV == 0) {
			currentQuestioner = new PeriodicLivelinessScanner();
			queryRunner.schedule(currentQuestioner, querryInterval);
		}
		logger.info("Reinitiated");
	}

	public void setActivityContextMaxIdleTime(long set) {
		if (set <= 100 && set > 0)
			this.maxActivityIdleTime = 15000;
		else
			this.maxActivityIdleTime = set;
	}

	public long getActivityContextMaxIdleTime() {
		return this.maxActivityIdleTime;
	}

	// --- OPERATIONS

	public void endActivity(String nullACID) throws ManagementException {

		// Again this is tx method
		logger.info("Trying to stop null activity[" + nullACID + "]!!");
		// prepareBean();
		boolean createdTx = false;
		try {
			createdTx = txMgr.requireTransaction();
			ActivityContext ac = acFactory.getActivityContext(nullACID,false);
			if (ac == null) {
				logger.debug("There is no ac associated with given acID["
						+ nullACID + "]!!");
				throw new ManagementException("Can not find AC for given ID["
						+ nullACID + "], try again!!!");
			}
			if (ac.getActivityContextHandle().getActivityType() == ActivityType.nullActivity) {
				logger.debug("Scheduling activity end for acID[" + nullACID
						+ "]");

				NullActivity nullActivity = (NullActivity) ac.getActivityContextHandle().getActivity();
				if (nullActivity != null) {
					nullActivity.endActivity();
				}
			} else {
				logger.debug("AC is not null activity context");
				throw new IllegalArgumentException("Given ID[" + nullACID
						+ "] does not point to NullActivity");
			}
			
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

	}

	public void queryActivityContextLiveness() {

		logger.info("Extorting liveliness query!!");
		// prepareBean();
		if (currentQuestioner.cancel()) {
			currentQuestioner = new PeriodicLivelinessScanner();
			currentQuestioner.run();
		}
		logger.info("Extortion complete");
		// currentQuestioner=new PeriodicLivelinessScanner();

	}

	public String[] listActivityContextsFactories() {

		logger.info("Listing AC  factory");
		boolean createdTx = false;
		String[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();

			// Now we need to sort acs per factory type, raentity ?
			Set factoriesSet = new HashSet();

			Iterator<ActivityContextHandle> it = this.acFactory.getAllActivityContextsHandles()
					.iterator();

			logger.debug("Gathering information");

			while (it.hasNext()) {
				factoriesSet.add(it.next().getActivitySource());
			}

			logger.debug("Composing response");
			// Now lets gather details
			if (factoriesSet.size() == 0)
				return null;

			ret = new String[factoriesSet.size()];
			ret = (String[]) factoriesSet.toArray(ret);

		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

		return ret;
	}

	public Object[] listActivityContexts(boolean inDetails) {

		// prepareBean();
		logger.info("Listing ACs with details[" + inDetails + "]");
		boolean createdTx = false;
		Object[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();
			ret = listWithCriteria(false, inDetails, LIST_BY_NO_CRITERIA, null);
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}
		}

		return ret;
	}

	public Object[] retrieveActivityContextDetails(String AC_ID)
			throws ManagementException {
		logger.info("Retrieving AC details for acID[" + AC_ID + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] o = null;
		try {
			createdTx = txMgr.requireTransaction();
			ActivityContext ac = this.acFactory.getActivityContext(AC_ID,false);
			if (ac == null) {
				logger.debug("Ac retrieval failed, no such ac[" + AC_ID
						+ "]!!!");
				throw new ManagementException(
						"Activity Context does not exist (ACID[" + AC_ID
						+ "]), try another one!!");
			}
			o = getDetails(ac);
		} catch (Throwable e) {
			logger.error(e.getMessage(),e);
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(),e);
				}
			}

		}

		return o;
	}

	/*
	 * This method should be run in tx
	 */

	/**
	 * This is main place where SLEE is accessed. This functions lists AC in
	 * various different ways. It can either return Object[] of arrays
	 * representing AC of simple Object[] that in fact contains String objects
	 * representing IDs of Activity Contexts
	 * 
	 * @param listIDsOnly -
	 *            tells to list only its, if this is true, second boolean flag
	 *            is ignored. Return value is Object[]{String,String,....}
	 * @param inDetails -
	 *            Tells whether sub-arrays containing name bindings, attached
	 *            sbb entities should be passed, or only value representing
	 *            their length. If true arrays are passed, if false, only
	 *            values.
	 * @param criteria -
	 *            Critteria to search by - if this is different than
	 *            {@link ActivityManagementMBeanImplMBean.LIST_BY_NO_CRITERIA}
	 *            next parameter has to have value.
	 * @param comparisonCriteria -
	 *            this has to contain String representation of criteria ac
	 *            should be looked by: SbbEID, RA Entity Name , SbbID or
	 *            activity class name - like getClass().getName().
	 * @return
	 *            <ul>
	 *            <li>Object[]{Stirng, String}</li>
	 *            <li>Object[] ={@link #retrieveActivityContextDetails()},{@link #retrieveActivityContextDetails()},....
	 *            </li>
	 *            </ul>
	 */
	private Object[] listWithCriteria(boolean listIDsOnly, boolean inDetails,
			int criteria, String comparisonCriteria) {

		logger.info("Listing with criteria[" + criteria + "] with details["
				+ inDetails + "] only IDS[" + listIDsOnly + "]");

		Iterator<String> it = this.acFactory.getAllActivityContextsIds().iterator();
		ArrayList lst = new ArrayList();

		// Needed by LIST_BY_SBBID
		HashMap sbbEntityIdToSbbID = new HashMap();

		while (it.hasNext()) {
			ActivityContext ac = this.acFactory.getActivityContext(it.next(),false);
			if (ac == null) {
				continue;
			}
			Object activity = ac.getActivityContextHandle().getActivity();
			if (activity != null) {  
				
				switch (criteria) {
				case LIST_BY_ACTIVITY_CLASS:

					if (!activity.getClass().getCanonicalName().equals(comparisonCriteria)) {
						ac = null; // we dont want this one here
					}						
					break;

				case LIST_BY_RAENTITY:

					if (ac.getActivityContextHandle().getActivityType() == ActivityType.externalActivity) {
						if (!ac.getActivityContextHandle().getActivitySource().equals(comparisonCriteria))
							ac = null;
					} else
						ac = null;
					break;

				case LIST_BY_SBBENTITY:

					// is this enough ?
					if (comparisonCriteria.equals("")
							|| !ac.getSortedCopyOfSbbAttachmentSet().contains(
									comparisonCriteria)) {
						ac = null;
					}

					break;

				case LIST_BY_SBBID:
					ComponentIDPropertyEditor propertyEditor = new ComponentIDPropertyEditor();
					propertyEditor.setAsText(comparisonCriteria);
					SbbID idBeingLookedUp = (SbbID) propertyEditor.getValue();
					boolean match = false;
					SbbID implSbbID = null;
					List list = ac.getSortedCopyOfSbbAttachmentSet();
					for (int i = 0; i < list.size(); i++) {
						String sbbEID = (String) list.get(i);

						if (sbbEntityIdToSbbID.containsKey(sbbEID)) {
							implSbbID = (SbbID) sbbEntityIdToSbbID.get(sbbEID);
						} else {
							SbbEntity sbbe = SbbEntityFactory.getSbbEntityWithoutLock(sbbEID);
							implSbbID = sbbe.getSbbId();
							sbbEntityIdToSbbID.put(sbbEID, implSbbID);
						}

						if (!implSbbID.equals(idBeingLookedUp)) {

							match = false;
							continue;
						} else {
							match = true;
							break;
						}

					}

					if (!match) {
						ac = null;
					}

					break;

				case LIST_BY_NO_CRITERIA:

					break;

				default:

					continue;
				}

				if (ac == null)
					continue;

				// Now we have to check - if we want only IDS
				Object singleResult = null;
				if (!listIDsOnly) {
					logger.debug("Adding AC[" + ac.getActivityContextId() + "]");

					Object[] o = getDetails(ac);

					if (!inDetails) {

						// This is stupid, but can save some bandwith, not sure if
						// we
						// should care. But Console is java script, and
						// sometimes that can be pain, so lets ease it
						o[SBB_ATTACHMENTS] = ((Object[]) o[SBB_ATTACHMENTS]).length
						+ "";
						o[NAMES_BOUND_TO] = ((Object[]) o[NAMES_BOUND_TO]).length
						+ "";
						o[TIMERS_ATTACHED] = ((Object[]) o[TIMERS_ATTACHED]).length
						+ "";
						o[DATA_PROPERTIES] = ((Object[]) o[DATA_PROPERTIES]).length
						+ "";
					}

					singleResult = o;

				} else {

					singleResult = ac.getActivityContextId();
				}

				lst.add(singleResult);
			}

		}
		if (lst.size() == 0)
			return null;

		logger.info("RETURN SIZE[" + lst.size() + "]");

		Object[] ret = new Object[lst.size()];
		ret = lst.toArray(ret);

		return ret;
	}

	/*
	 * FIXME uncomment code when everything is commited
	 * 
	 * This method should be run in tx
	 */
	private Object[] getDetails(ActivityContext ac) {

		logger.debug("Retrieveing details for acID["
				+ ac.getActivityContextId() + "]");
		Object[] o = new Object[ARRAY_SIZE];

		o[ActivityManagementMBeanImplMBean.AC_ID] = ac.getActivityContextId();
		logger.debug("======[getDetails]["
				+ o[ActivityManagementMBeanImplMBean.AC_ID] + "]["
				+ ac.hashCode() + "]");
		
		ActivityContextHandle ach = ac.getActivityContextHandle();
		if (ach.getActivityType() == ActivityType.externalActivity) {
			o[RA] = ach.getActivitySource();
		}
		
		o[ACTIVITY_CLASS] = ach.getActivity().getClass().getName();
		logger.debug("======[getDetails][ACTIVITY_CLASS][" + o[ACTIVITY_CLASS]
				+ "]");
		// Date d = new Date(ac.getLastAccessTime());
		// o[LAST_ACCESS_TIME] = d;
		o[LAST_ACCESS_TIME] = ac.getLastAccessTime() + "";
		logger.debug("======[getDetails][LAST_ACCESS_TIME]["
				+ o[LAST_ACCESS_TIME] + "]["
				+ new Date(Long.parseLong((String) o[LAST_ACCESS_TIME])) + "]");
		// Do we want it sorted? - other way we need to introduce another
		// method, or parse String that contains MAP....
		List lst = ac.getSortedCopyOfSbbAttachmentSet();
		String[] tmp = new String[lst.size()];
		tmp = (String[]) lst.toArray(tmp);
		o[SBB_ATTACHMENTS] = tmp;

		Set s = ac.getNamingBindingCopy();
		tmp = new String[s.size()];
		tmp = (String[]) s.toArray(tmp);
		o[NAMES_BOUND_TO] = tmp;

		s = ac.getAttachedTimersCopy();
		tmp = new String[s.size()];
		Iterator it = s.iterator();
		int counter = 0;
		while (it.hasNext()) {
			tmp[counter++] = ((TimerID) it.next()).toString();
		}

		o[TIMERS_ATTACHED] = tmp;

		Map m = ac.getDataAttributesCopy();
		tmp = new String[m.size()];
		it = m.keySet().iterator();
		counter = 0;
		while (it.hasNext()) {
			Object k = it.next();
			Object v = m.get(k);
			tmp[counter++] = k + "=" + v;
		}
		o[DATA_PROPERTIES] = tmp;

		return o;
	}

	// Should those methods throw something if sbbeid, classname or etity does
	// not exist?

	public Object[] retrieveActivityContextIDByActivityType(
			String fullQualifiedActivityClassName) {

		logger.info("Retrieving AC by activity class name["
				+ fullQualifiedActivityClassName + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();

			ret = listWithCriteria(true, true, LIST_BY_ACTIVITY_CLASS,
					fullQualifiedActivityClassName);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

		return ret;
	}

	public Object[] retrieveActivityContextIDByResourceAdaptorEntityName(
			String entityName) {

		logger.info("Retrieving AC by entity name[" + entityName + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();
			ret = listWithCriteria(true, true, LIST_BY_RAENTITY, entityName);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

		return ret;
	}

	public Object[] retrieveActivityContextIDBySbbEntityID(String sbbEID) {

		logger.info("Retrieving ACs by sbb entity id [" + sbbEID + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();
			ret = listWithCriteria(true, true, LIST_BY_SBBENTITY, sbbEID);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

		return ret;
	}

	public Object[] retrieveActivityContextIDBySbbID(String sbbID) {

		logger.info("Retrieving ACs by sbb id [" + sbbID + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] ret = null;
		try {
			createdTx = txMgr.requireTransaction();
			ret = listWithCriteria(true, true, LIST_BY_SBBID, sbbID);
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

		return ret;
	}

	public Map retrieveNamesToActivityContextIDMappings() {
		ActivityContextNamingFacilityImpl naming = (ActivityContextNamingFacilityImpl) container
				.getActivityContextNamingFacility();

		boolean createdTx = false;
		Map ret = null;
		try {
			createdTx = txMgr.requireTransaction();
			ret = naming.getBindings();
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (Throwable e) {
					logger.error(e.getMessage(), e);
				}
			}

		}

		return ret;
	}

	// == Some helpers
	private void prepareBean() {
		if (this.acFactory == null) {
			this.container = SleeContainer.lookupFromJndi();
			this.acFactory = container.getActivityContextFactory();
			this.txMgr = container.getTransactionManager();
			this.currentQuestioner = new PeriodicLivelinessScanner();
			this.queryRunner.schedule(this.currentQuestioner,
					this.querryInterval);
		}

	}

	// TimerClass to run periodic livelines querry - here is decided which ac
	// are going to be querried, and possibly destroyed
	// , depends on impl

	/**
	 * FIXME uncomment code when everything is commited
	 */
	private class PeriodicLivelinessScanner extends TimerTask {

		private Set<ActivityContextHandle> getActivityContextHandles() {
			if(logger.isDebugEnabled())
				logger
					.debug("Periodic Liveliness Task is on the run, retrieveing all ACs");
			
			try {
				txMgr.begin();
				return acFactory.getAllActivityContextsHandles();
			}
			catch (Exception e) {
				logger.error("failed to retrieve all ACs to do periodic liveness scan", e);
				return null;
			}
			finally {
				try {
					txMgr.rollback();
				}
				catch (Exception e) {
					logger.error("failed to end tx to retrieve all ACs and do periodic liveness scan", e);
				}
			}
		}
		
		private void queryLiveness(ActivityContextHandle ach,long currentTime) {
			
			if(logger.isDebugEnabled())
				logger
					.debug("Periodic Liveliness Task is on the run, processing AC "+ach);
			
			try {
				txMgr.begin();
				ActivityContext ac = acFactory.getActivityContext(ach,false);
				if (ac != null) {  
					if (ach.getActivityType() != ActivityType.externalActivity)
						return;

					if ((currentTime - ac.getLastAccessTime()) < maxActivityIdleTime) {
						// This one has been accessed in near past, so we dont
						// want to query it
						return;
					}
					ResourceAdaptorEntity raEntity = container.getResourceManagement()
					.getResourceAdaptorEntity(
							ach.getActivitySource());
					if (raEntity.getResourceAdaptorObject().getActivity(ach.getActivityHandle()) != null) {
						if (logger.isDebugEnabled()) {
							logger.debug("Invoking ra entity "+raEntity.getName()+" queryLiveness() for activity handle "+ach.getActivityHandle());
						}
						raEntity.getResourceAdaptorObject().queryLiveness(
								ach.getActivityHandle());
					}
					else {
						logger.warn("Ending leaked activity with handle "+ach.getActivityHandle()+" of ra entity "+raEntity.getName()+" since the ra entity getActivity() returns null");
						if (!ac.isEnding()) {
							// end it
							ac.endActivity();
						}
						else {
							// force removal
							container.getActivityContextFactory().removeActivityContext(ac);
						}
					}					
				}
			}
			catch (Exception e) {
				logger.error("failed to query liveness on AC "+ach, e);
				
			}
			finally {
				try {
					txMgr.commit();
				}
				catch (Exception e) {
					logger.error("failed to end tx to to query liveness on AC "+ach, e);
				}
			}
			
		}
		
		public void run() {
			Set<ActivityContextHandle> achs = this.getActivityContextHandles();
			if (achs != null) {
				long currentTime = System.currentTimeMillis();
				for (ActivityContextHandle ach : achs) {
					this.queryLiveness(ach,currentTime);										
				}
			}
			if (querryInterval != 0) {
				// Lets schedule again				
				currentQuestioner = new PeriodicLivelinessScanner();
				queryRunner.schedule(currentQuestioner, querryInterval);
				if(logger.isDebugEnabled())
					logger.debug("Periodic Liveliness Task scheduled for next run");
			}
		}

	}

	// -------------------- JBOSS MBean LIFECYCLE METHODS

	/**
	 * 
	 * start MBean service lifecycle method
	 * 
	 */
	public void startService() throws Exception {
		if(logger.isDebugEnabled()) {
			logger.debug("Starting Activity Manager MBean");
		}
		prepareBean();
		logger.info("Activity Management MBean started");
	}

	/**
	 * 
	 * stop MBean service lifecycle method
	 * 
	 */
	protected void stopService() throws Exception {

		this.queryRunner.cancel();
		

	}

	/*
	public String dumpContainerState() {
		Set set = XACacheTestViewer.getXACacheMap().keySet();
	    StringBuilder sb = new StringBuilder(SleeContainer.lookupFromJndi().getEventRouter().toString());
	    sb.append("\n"+ActivityContext.dumpStaticState());
	    sb.append("\n"+SleeContainer.lookupFromJndi().dumpState());
		for(Iterator i=set.iterator();i.hasNext();) {
	    	String key = (String)i.next();
	    	sb.append("\nXACache key "+key+" values = "+((Map)XACacheTestViewer.getXACacheMap().get(key)).keySet());
	    }
	    return sb.toString();
	}
	*/
	
}
