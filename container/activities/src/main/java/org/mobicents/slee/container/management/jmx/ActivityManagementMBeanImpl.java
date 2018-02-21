/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.management.jmx;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.slee.SbbID;
import javax.slee.facilities.TimerID;
import javax.slee.management.ManagementException;
import javax.slee.nullactivity.NullActivity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityFactory;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.runtime.activity.ActivityContextFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContextImpl;


/**
 * This class provides minimum information for management console. It also tries
 * to clean container of dead activities
 * 
 * @author Bartosz Baranowski
 * @author Eduardo Martins
 * 
 */
public class ActivityManagementMBeanImpl extends MobicentsServiceMBeanSupport
		implements ActivityManagementMBeanImplMBean {

	// Name convention is to conform to other mbeans - also ServiceMBeanSupport
	// is not StandadMBean which allows
	// Passing MBean interface class which enables custom names this is why
	// MBean interface has sufix - MBeanImplMbean
	
	private final ActivityContextFactoryImpl acFactory;
	private final SbbEntityFactory sbbEntityFactory;

	private static Logger logger = Logger
			.getLogger(ActivityManagementMBeanImpl.class);

	public ActivityManagementMBeanImpl(SleeContainer sleeContainer) {
		super(sleeContainer);
		this.acFactory = (ActivityContextFactoryImpl) sleeContainer.getActivityContextFactory();
		this.sbbEntityFactory = sleeContainer.getSbbEntityFactory();
	}

	// === PROPERTIES

	public int getActivityContextCount() {
		return this.acFactory.getActivityContextCount();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean#setTimeBetweenLivenessQueries(long)
	 */
	public void setTimeBetweenLivenessQueries(long set) {
		acFactory.getConfiguration().setTimeBetweenLivenessQueries(set);
		if (set == 0) {
			cancelLivenessQuery();
		}
		else {
			if (scheduledFuture == null) {
				scheduleLivenessQuery();
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean#getTimeBetweenLivenessQueries()
	 */
	public long getTimeBetweenLivenessQueries() {
		return acFactory.getConfiguration().getTimeBetweenLivenessQueries();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean#setActivityContextMaxIdleTime(long)
	 */
	public void setActivityContextMaxIdleTime(long set) {
		acFactory.getConfiguration().setMaxTimeIdle(set);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean#getActivityContextMaxIdleTime()
	 */
	public long getActivityContextMaxIdleTime() {
		return acFactory.getConfiguration().getMaxTimeIdle();
	}

	// --- OPERATIONS

	public void endActivity(ActivityContextHandle ach) throws ManagementException {

		// Again this is tx method
		logger.info("Trying to stop null activity[" + ach + "]!!");
		ActivityContext ac = acFactory.getActivityContext(ach);
		if (ac == null) {
			logger.debug("There is no ac associated with given acID["
						+ ach + "]!!");
			throw new ManagementException("Can not find AC for given ID["
					+ ach + "], try again!!!");
		}
		if (ac.getActivityContextHandle().getActivityType() == ActivityType.NULL) {
			logger.debug("Scheduling activity end for acID[" + ach
						+ "]");
			NullActivity nullActivity = (NullActivity) ac.getActivityContextHandle().getActivityObject();
			if (nullActivity != null) {
				nullActivity.endActivity();
			}
		} else {
			logger.debug("AC is not null activity context");
			throw new IllegalArgumentException("Given ID[" + ach
					+ "] does not point to NullActivity");
		}		
	}

	public void queryActivityContextLiveness() {

		logger.info("Extorting liveliness query!!");
		// prepareBean();
		cancelLivenessQuery();
		new PeriodicLivelinessScanner().run();
		logger.info("Extortion complete");
		// currentQuestioner=new PeriodicLivelinessScanner();

	}

	public String[] listActivityContextsFactories() {
		logger.info("Listing AC factories");
		Set<String> factoriesSet = new HashSet<String>();		
		for (ActivityContextHandle ach : this.acFactory.getAllActivityContextsHandles()) {
			factoriesSet.add(ach.getActivityType() == ActivityType.RA ? ((ResourceAdaptorActivityContextHandle)ach).getResourceAdaptorEntity().getName() : "");
		}		
		if (factoriesSet.isEmpty())
			return null;
		String[] ret = new String[factoriesSet.size()];
		ret = (String[]) factoriesSet.toArray(ret);
		return ret;
	}

	public Object[] listActivityContexts(boolean inDetails) {
		logger.info("Listing ACs with details[" + inDetails + "]");
		return listWithCriteria(false, inDetails, LIST_BY_NO_CRITERIA, null);
	}

	public Object[] retrieveActivityContextDetails(ActivityContextHandle ach)
			throws ManagementException {
		logger.info("Retrieving AC details for " + ach);
		ActivityContextImpl ac = this.acFactory.getActivityContext(ach);
		if (ac == null) {
			logger.debug("Ac retrieval failed, no such ac[" + ach
						+ "]!!!");
			throw new ManagementException(
						"Activity Context does not exist (ACID[" + ach
						+ "]), try another one!!");
		}
		return getDetails(ac);
	}

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

		Iterator<ActivityContextHandle> it = this.acFactory.getAllActivityContextsHandles().iterator();
		ArrayList<Object> lst = new ArrayList<Object>();

		// Needed by LIST_BY_SBBID
		HashMap<SbbEntityID,SbbID> sbbEntityIdToSbbID = new HashMap<SbbEntityID,SbbID>();

		while (it.hasNext()) {
			ActivityContextHandle achOrig = it.next();
			//JmxActivityContextHandle ach = ActivityContextHandleSerializer.encode(achOrig);
			ActivityContextImpl ac = this.acFactory.getActivityContext(achOrig);
			if (ac == null) {
				continue;
			}
			Object activity = achOrig.getActivityObject();

			if (activity != null) {  
				
	      String acId = ac.getStringID();
	      String acSource = achOrig.getActivityType() == ActivityType.RA ? ((ResourceAdaptorActivityContextHandle)achOrig).getResourceAdaptorEntity().getName() : "";

	      switch (criteria) {
				case LIST_BY_ACTIVITY_CLASS:

					if (!activity.getClass().getCanonicalName().equals(comparisonCriteria)) {
						ac = null; // we dont want this one here
					}						
					break;

				case LIST_BY_RAENTITY:

					if (achOrig.getActivityType() == ActivityType.RA) {
						if (!acSource.equals(comparisonCriteria))
							ac = null;
					} else
						ac = null;
					break;

				case LIST_BY_SBBENTITY:
					
					for (SbbEntityID sbbEntityID : ac.getSbbAttachmentSet()) {
						if (sbbEntityID.toString().equals(comparisonCriteria)) {
							break;
						}
					}
					ac = null;
					break;

				case LIST_BY_SBBID:
					ComponentIDPropertyEditor propertyEditor = new ComponentIDPropertyEditor();
					propertyEditor.setAsText(comparisonCriteria);
					SbbID idBeingLookedUp = (SbbID) propertyEditor.getValue();
					boolean match = false;
					SbbID implSbbID = null;
					for (SbbEntityID sbbEntityID : ac.getSbbAttachmentSet()) {
						if (sbbEntityIdToSbbID.containsKey(sbbEntityID)) {
							implSbbID = sbbEntityIdToSbbID.get(sbbEntityID);
						} else {
							SbbEntity sbbe = sbbEntityFactory.getSbbEntity(sbbEntityID,false);
							if (sbbe == null) {
								continue;
							}
							implSbbID = sbbe.getSbbId();
							sbbEntityIdToSbbID.put(sbbEntityID, implSbbID);
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
					logger.debug("Adding AC[" + acId + "]");

					Object[] o = getDetails(ac);

					if (!inDetails) {

						// This is stupid, but can save some bandwith, not sure if
						// we
						// should care. But Console is java script, and
						// sometimes that can be pain, so lets ease it
					    o[SBB_ATTACHMENTS] = Integer.toString(((Object[]) o[SBB_ATTACHMENTS]).length);
						o[NAMES_BOUND_TO] = Integer.toString(((Object[]) o[NAMES_BOUND_TO]).length);
						o[TIMERS_ATTACHED] = Integer.toString(((Object[]) o[TIMERS_ATTACHED]).length);
						o[DATA_PROPERTIES] = Integer.toString(((Object[]) o[DATA_PROPERTIES]).length);
					}

					singleResult = o;

				} else {

					singleResult = acId;
				}

				lst.add(singleResult);
			}

		}
		if (lst.isEmpty())
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
	@SuppressWarnings("rawtypes")
	private Object[] getDetails(ActivityContextImpl ac) {

		logger.debug("Retrieveing details for acID["
				+ ac.getActivityContextHandle() + "]");
		Object[] o = new Object[ARRAY_SIZE];

		ActivityContextHandle achOrig = ac.getActivityContextHandle();
		//JmxActivityContextHandle ach = ActivityContextHandleSerializer.encode(achOrig);
		String acId = ac.getStringID();
		
		o[ActivityManagementMBeanImplMBean.AC_ID] = acId;
		logger.debug("======[getDetails]["
				+ o[ActivityManagementMBeanImplMBean.AC_ID] + "]["
				+ ac.hashCode() + "]");
		
		
		if (achOrig.getActivityType() == ActivityType.RA) {
			o[RA] = ((ResourceAdaptorActivityContextHandle)achOrig).getResourceAdaptorEntity().getName();
		}
		
		o[ACTIVITY_CLASS] = achOrig.getActivityObject().getClass().getName();
		logger.debug("======[getDetails][ACTIVITY_CLASS][" + o[ACTIVITY_CLASS]
				+ "]");
		// Date d = new Date(ac.getLastAccessTime());
		// o[LAST_ACCESS_TIME] = d;
		o[LAST_ACCESS_TIME] = ac.getLastAccessTime() + "";
		logger.debug("======[getDetails][LAST_ACCESS_TIME]["
				+ o[LAST_ACCESS_TIME] + "]["
				+ new Date(Long.parseLong((String) o[LAST_ACCESS_TIME])) + "]");
		
		Set<SbbEntityID> sbbAttachmentSet = ac.getSbbAttachmentSet();
		String[] tmp = new String[sbbAttachmentSet.size()];
		Iterator<?> it = sbbAttachmentSet.iterator();
		int counter = 0;
		while (it.hasNext()) {
			tmp[counter++] = it.next().toString();
		}
		o[SBB_ATTACHMENTS] = tmp;

		Set<String> nameBindindsSet = ac.getNamingBindings();
		tmp = new String[nameBindindsSet.size()];
		tmp = nameBindindsSet.toArray(tmp);
		o[NAMES_BOUND_TO] = tmp;

		Set<TimerID> attachedTimersSet = ac.getAttachedTimers();
		tmp = new String[attachedTimersSet.size()];
		it = attachedTimersSet.iterator();
		counter = 0;
		while (it.hasNext()) {
			tmp[counter++] = ((TimerID) it.next()).toString();
		}
		o[TIMERS_ATTACHED] = tmp;

		Map m = ac.getDataAttributes();
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
		return listWithCriteria(true, true, LIST_BY_ACTIVITY_CLASS,
					fullQualifiedActivityClassName);
	}

	public Object[] retrieveActivityContextIDByResourceAdaptorEntityName(
			String entityName) {

		logger.info("Retrieving AC by entity name[" + entityName + "]");
		return listWithCriteria(false, true, LIST_BY_RAENTITY, entityName);
	}

	public Object[] retrieveActivityContextIDBySbbEntityID(String sbbEID) {

		logger.info("Retrieving ACs by sbb entity id [" + sbbEID + "]");
		return listWithCriteria(true, true, LIST_BY_SBBENTITY, sbbEID);
	}

	public Object[] retrieveActivityContextIDBySbbID(String sbbID) {

		logger.info("Retrieving ACs by sbb id [" + sbbID + "]");
		return listWithCriteria(true, true, LIST_BY_SBBID, sbbID);
	}

	// TimerClass to run periodic livelines querry - here is decided which ac
	// are going to be querried, and possibly destroyed
	// , depends on impl

	/**
	 * FIXME uncomment code when everything is commited
	 */
	private class PeriodicLivelinessScanner extends TimerTask {

		private void queryLiveness(ActivityContextHandle ach,long currentTime) {
			
			if (ach.getActivityType() != ActivityType.RA)
				return;
			
			if(logger.isDebugEnabled())
				logger
					.debug("Periodic Liveliness Task is on the run, processing AC "+ach);
			
			ActivityContextImpl ac = acFactory.getActivityContext(ach);
			if(ac == null) {
				return;
			}
			
			if ((currentTime - ac.getLastAccessTime()) < acFactory.getConfiguration().getMaxTimeIdleInMs()) {
				// This one has been accessed in near past, so we dont
				// want to query it
				return;
			}
			
			final ResourceAdaptorActivityContextHandle raach = (ResourceAdaptorActivityContextHandle) ach; 
			final ResourceAdaptorEntity raEntity = raach.getResourceAdaptorEntity();
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking ra entity "+raEntity.getName()+" queryLiveness() for activity handle "+ach.getActivityHandle());
			}
			raEntity.getResourceAdaptorObject().queryLiveness(
								ach.getActivityHandle());								
			
		}
		
		public void run() {
			long currentTime = System.currentTimeMillis();
			for (ActivityContextHandle ach : acFactory.getAllActivityContextsHandles()) {
				this.queryLiveness(ach,currentTime);										
			}
			scheduleLivenessQuery();
		}

	}

	// -------------------- JBOSS MBean LIFECYCLE METHODS

	private ScheduledFuture<?> scheduledFuture;
	
	private void scheduleLivenessQuery() {
		final long timeBetweenLivenessQueries = acFactory.getConfiguration().getTimeBetweenLivenessQueries();
		if (timeBetweenLivenessQueries > 0) {
			this.scheduledFuture = this.acFactory.getSleeContainer().getNonClusteredScheduler().schedule(new PeriodicLivelinessScanner(),timeBetweenLivenessQueries,TimeUnit.MINUTES);
			if(logger.isDebugEnabled())
				logger.debug("Periodic Liveliness Task scheduled to run in "+timeBetweenLivenessQueries+" minutes");
		}
		else {
			this.scheduledFuture = null;
		}
	}
	
	public void cancelLivenessQuery() {
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			scheduledFuture = null;
		}
	}
	
	/**
	 * 
	 * 
	 * start MBean service lifecycle method
	 * 
	 */
	public void startService() throws Exception {
		if(logger.isDebugEnabled()) {
			logger.debug("Starting Activity Manager MBean");
		}
		logger.info("Activity Management MBean started");
		scheduleLivenessQuery();
	}

	/**
	 * 
	 * stop MBean service lifecycle method
	 * 
	 */
	protected void stopService() throws Exception {
		cancelLivenessQuery();
	}
	
}
