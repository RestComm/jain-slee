package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import javax.slee.nullactivity.NullActivity;
import javax.slee.resource.ResourceException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.jboss.system.ServiceMBeanSupport;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.SleeInternalEndpoint;
import org.mobicents.slee.runtime.cache.XACacheTestViewer;
import org.mobicents.slee.runtime.facilities.ActivityContextNamingFacilityImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;

/**
 * This class provides minimum inforamtion for management console. It also tries
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

	public void endActivity(String nullACID) throws ResourceException {

		// Again this is tx method
		logger.info("Trying to stop null activity[" + nullACID + "]!!");
		// prepareBean();
		boolean createdTx = false;
		try {
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ActivityContext ac = acFactory.getActivityContextById(nullACID,
					false);
			if (ac == null) {
				logger.debug("There is no ac associated with given acID["
						+ nullACID + "]!!");
				throw new ResourceException("Can not find AC for given ID["
						+ nullACID + "], try again!!!");
			}
			if (ac.getActivity() instanceof NullActivity) {
				logger.debug("Scheduling activity end for acID[" + nullACID
						+ "]");
				SleeInternalEndpoint end = container.getSleeEndpoint();

				end.scheduleActivityEndedEvent(ac.getActivity());
			} else {
				logger.debug("AC is not null activity context");
				throw new IllegalArgumentException("Given ID[" + nullACID
						+ "] does not point to NullActivity");
			}
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}

			// Now we need to sort acs per factory type, raentity ?
			Set factoriesSet = new HashSet();

			Iterator it = this.acFactory.getAllActivityContextsIds()
					.iterator();

			logger.debug("Gathering information");

			while (it.hasNext()) {
				String id = (String) it.next();
				ActivityContext ac = this.acFactory.getActivityContextById(id);
				if (ac != null) {  
					Object key = null;
					Object activity = ac.getActivity();
					if (activity instanceof SleeActivityHandle) {
						key = ((SleeActivityHandle) activity).getRaEntityName();

					} else {
						// null and service activities fall here
						key = activity.getClass().getName();
					}
					factoriesSet.add(key);
				}
			}

			logger.debug("Composing response");
			// Now lets gather details
			if (factoriesSet.size() == 0)
				return null;

			ret = new String[factoriesSet.size()];
			ret = (String[]) factoriesSet.toArray(ret);

		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ret = listWithCriteria(false, inDetails, LIST_BY_NO_CRITERIA, null);
		} catch (SystemException e) {

			e.printStackTrace();

		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
				}
			}
		}

		return ret;
	}

	public Object[] retrieveActivityContextDetails(String AC_ID)
			throws ResourceException {
		logger.info("Retrieving AC details for acID[" + AC_ID + "]");
		// prepareBean();
		boolean createdTx = false;
		Object[] o = null;
		try {
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}

			ActivityContext ac = this.acFactory.getActivityContextById(AC_ID,
					false);
			if (ac == null) {
				logger.debug("Ac retrieval failed, no such ac[" + AC_ID
						+ "]!!!");
				throw new ResourceException(
						"Activity Context does not exist (ACID[" + AC_ID
								+ "]), try another one!!");
			}
			o = getDetails(ac);

		} catch (SystemException e) {

			e.printStackTrace();
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();

				} catch (SystemException e) {

					e.printStackTrace();
				}
			}

		}

		return o;
	}

	/*
	 * This method should be run in tx
	 */

	/**
	 * This is main place where SLEE is accessed. This functions lists ac in
	 * various different ways. It can either return Object[] of arrays
	 * representing AC of simple Object[] that in fact contains Stirgn objects
	 * representing IDs of Actvity Contexts
	 * 
	 * @param listIDsOnly -
	 *            tells to list only its, if this is true, second boolean flag
	 *            is ignored. Return value is Object[]{Stirng,String,....}
	 * @param inDetails -
	 *            Tells wheather subbarays containing name bindings, attached
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

		Iterator it = this.acFactory.getAllActivityContextsIds().iterator();
		ArrayList lst = new ArrayList();

		// Needed by LIST_BY_SBBID
		HashMap sbbEntityIdToSbbID = new HashMap();

		while (it.hasNext()) {
			String id = (String) it.next();
			ActivityContext ac = this.acFactory.getActivityContextById(id);
			if (ac != null) {  
				Object activity = null;

				switch (criteria) {
				case LIST_BY_ACTIVITY_CLASS:

					String activityClassName = null;
					activity = ac.getActivity();
					if (activity instanceof SleeActivityHandle)
						activity = ((SleeActivityHandle) activity).getActivity();

					activityClassName = activity.getClass().getCanonicalName();

					if (!activityClassName.equals(comparisonCriteria))
						ac = null; // we dont want this one here
					break;

				case LIST_BY_RAENTITY:

					String raEntityName = null;

					activity = ac.getActivity();
					if (activity instanceof SleeActivityHandle) {
						SleeActivityHandle SLAH = (SleeActivityHandle) activity;
						raEntityName = SLAH.getRaEntityName();
						if (!raEntityName.equals(comparisonCriteria))
							ac = null;
					} else
						ac = null;

					break;

				case LIST_BY_SBBENTITY:

					// BACKA - String uno="sdgdasgasghashsadhah";
					// String due="";
					// uno.contains(due)==true !!!!!!

					// is this enough ?

					if (comparisonCriteria.equals("")
							|| !ac.getSortedCopyOfSbbAttachmentSet().contains(
									comparisonCriteria)) {

						ac = null;
					}

					break;

				case LIST_BY_SBBID:

					SbbID idBeingLookedUp = new SbbIDImpl(new ComponentKey(
							comparisonCriteria));
					boolean match = false;
					SbbID implSbbID = null;
					List list = ac.getSortedCopyOfSbbAttachmentSet();
					for (int i = 0; i < list.size(); i++) {
						String sbbEID = (String) list.get(i);

						if (sbbEntityIdToSbbID.containsKey(sbbEID)) {
							implSbbID = (SbbID) sbbEntityIdToSbbID.get(sbbEID);
						} else {
							SbbEntity sbbe = SbbEntityFactory.getSbbEntity(sbbEID);
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
		String activityClassName = null;

		Object activity = ac.getActivity();
		if (activity instanceof SleeActivityHandle) {
			SleeActivityHandle SLAH = (SleeActivityHandle) activity;
			activityClassName = SLAH.getActivity().getClass()
					.getCanonicalName();
			o[RA] = SLAH.getRaEntityName();
		} else {
			activityClassName = activity.getClass().getCanonicalName();
		}

		o[ACTIVITY_CLASS] = activityClassName;
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}

			ret = listWithCriteria(true, true, LIST_BY_ACTIVITY_CLASS,
					fullQualifiedActivityClassName);
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {
			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ret = listWithCriteria(true, true, LIST_BY_RAENTITY, entityName);
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ret = listWithCriteria(true, true, LIST_BY_SBBENTITY, sbbEID);
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ret = listWithCriteria(true, true, LIST_BY_SBBID, sbbID);
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			if (!txMgr.isInTx()) {
				txMgr.begin();
				createdTx = true;
			}
			ret = naming.getBindings();
		} catch (SystemException e) {

			e.printStackTrace();
		} finally {

			if (createdTx) {
				try {
					txMgr.commit();
				} catch (SystemException e) {

					e.printStackTrace();
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
			this.txMgr = SleeContainer.getTransactionManager();
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

		public void run() {

			// We need to run in tx FOR SURE!!!!
			if(logger.isDebugEnabled())
				logger
					.debug("Periodic Liveliness Task is on the run, scanning through acs");
			 //ActivityContext.doDebug();
			boolean createdTx = txMgr.requireTransaction();
			boolean rollback = true;
			try {

				Iterator it = acFactory.getAllActivityContextsIds().iterator();
				long currentTime = System.currentTimeMillis();
				// For now lets querry all of them.
				while (it.hasNext()) {

					String id = (String) it.next();
					ActivityContext ac = acFactory.getActivityContextById(id);
					if (ac != null) {  
						if (!(ac.getActivity() instanceof SleeActivityHandle))
							continue;

						if ((currentTime - ac.getLastAccessTime()) < maxActivityIdleTime) {
							// This one has been accessed in near past, so we dont
							// want to querry it
							continue;
						}

						SleeActivityHandle SLAH = (SleeActivityHandle) ac
								.getActivity();
						if (SLAH.getResourceAdaptor() != null)
							SLAH.getResourceAdaptor().queryLiveness(
									SLAH.getHandle());
					}
				}
				rollback = false;
			} catch (Exception e) {
				logger.error("Failure in periodic liveliness scanner", e);
			} finally {
				try {
					if (createdTx) {
						if (rollback) {
							txMgr.rollback();
						}
						else {
							txMgr.commit();	
						}

					} else {
						if (rollback) {
							txMgr.setRollbackOnly();
						}	
					}
				} catch (SystemException e) {
					logger.error("Tx manager failure in periodic liveliness scanner", e);
				}
				if (querryInterval == 0)
					return;

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
