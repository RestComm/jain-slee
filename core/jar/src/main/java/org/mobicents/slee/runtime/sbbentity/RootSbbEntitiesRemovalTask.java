package org.mobicents.slee.runtime.sbbentity;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.slee.ServiceID;
import javax.slee.management.ServiceState;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.MobicentsManagement;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class RootSbbEntitiesRemovalTask extends TimerTask {

	private static Logger logger = Logger
			.getLogger(RootSbbEntitiesRemovalTask.class);

	// static map with scheduled tasks
	private static ConcurrentHashMap<ServiceID, RootSbbEntitiesRemovalTask> tasksScheduled = new ConcurrentHashMap<ServiceID, RootSbbEntitiesRemovalTask>();

	// number of minutes before complete removal of lingering service
	// sbbentities

	// running static timer
	private static Timer runningTimer = new Timer();

	// the service id of the task
	private ServiceID serviceID;

	/**
	 * Retreives the running task for the service with the specified service id.
	 * 
	 * @param serviceID
	 *            null if no such task is scheduled
	 * @return
	 */
	public static RootSbbEntitiesRemovalTask getTask(ServiceID serviceID) {
		return tasksScheduled.get(serviceID);
	}

	/**
	 * Constructs a new instance of a timer task that removes all sbb entities
	 * from the service with the specified service id.
	 * 
	 * @param serviceID
	 */
	public RootSbbEntitiesRemovalTask(ServiceID serviceID) {

		this.serviceID = serviceID;

		if (tasksScheduled.putIfAbsent(this.serviceID, this) == null) {
			// secondsInMinute*1000*sbbEntityRemovalDelay)
			long executionTime = (long) (60 * 1000 * MobicentsManagement.entitiesRemovalDelay);
			runningTimer.schedule(this, executionTime);
			if (logger.isDebugEnabled()) {
				logger.debug(" == Service SBB Entities REMOVAL SCHEDULED FOR:"
						+ this.serviceID + " in [" + (double) executionTime
						/ (1000) + "] seconds ==");
			}
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.serviceID.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null && arg0.getClass() == this.getClass()) {
			return this.serviceID
					.equals(((RootSbbEntitiesRemovalTask) arg0).serviceID);
		} else {
			return false;
		}
	}

	private void removeAllSbbEntities() {

		if (logger.isDebugEnabled()) {
			logger.debug("SBB Entities REMOVAL STARTING for service "
					+ serviceID);
		}

		this.cancel();

		SleeTransactionManager sleeTransactionManager = SleeContainer
				.getTransactionManager();

		Iterator i = null;

		while (true) {

			try {
				// begin transaction
				sleeTransactionManager.begin();

				if (i == null) {
					// get service
					Service service = SleeContainer.lookupFromJndi()
							.getServiceManagement().getService(serviceID);
					if (service.getState() == ServiceState.INACTIVE) {
						i = service.getChildObj().iterator();
					} else {
						// wrong service state, finish task
						break;
					}
				}

				if (i.hasNext()) {
					// get sbb entity
					SbbEntity sbbEntity = null;
					try {
						sbbEntity = SbbEntityFactory.getSbbEntity((String) i
								.next());
					} catch (Exception e) {
						// entity does not exists anymore, continue
						continue;
					}
					// save current class loader
					ClassLoader oldClassLoader = Thread.currentThread()
							.getContextClassLoader();
					try {
						// change to the sbb object class loader
						Thread.currentThread().setContextClassLoader(
								sbbEntity.getSbbDescriptor().getClassLoader());
						// remove sbb entity
						SbbEntityFactory.removeSbbEntity(sbbEntity, false);
					} catch (Exception ex) {
						if (logger.isDebugEnabled()) {
							logger.debug("error removing entity "
									+ sbbEntity.getSbbEntityId(), ex);
						}
					} finally {
						// restore old class loader
						Thread.currentThread().setContextClassLoader(
								oldClassLoader);
					}
				} else {
					// no more entities, finish task
					break;
				}

			} catch (Exception e) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"exception while removing pending root sbb entities of service "
									+ serviceID, e);
				}
			} finally {
				try {
					sleeTransactionManager.commit();
				} catch (SystemException e) {
					if (logger.isDebugEnabled()) {
						logger
								.debug(
										"exception while commiting tx while removing pending root sbb entities of service "
												+ serviceID, e);
					}
				}
			}

		}

		if (logger.isDebugEnabled()) {
			logger.debug("SBB Entities REMOVAL ENDED for service " + serviceID);
		}

	}

	// @Override
	public void run() {

		synchronized (this) {
			// remove the task from the map
			if (tasksScheduled.get(serviceID) == null) {
				return;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("RUNNING TASK ON SERVICE UNINSTALL FOR "
						+ serviceID);
			}

			// run the task in the executor to have it's own transactions
			Runnable runnable = new Runnable() {
				public void run() {
					removeAllSbbEntities();
				}
			};
			try {
				executorService.submit(runnable).get();
			} catch (Exception e) {
				logger.error(
						"Failed to execute task to remove pending root sbb entities of "
								+ serviceID, e);
			}

			tasksScheduled.remove(serviceID);
		}

	}

	private static ExecutorService executorService = Executors
			.newSingleThreadExecutor();

}
