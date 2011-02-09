package org.mobicents.slee.resource;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.activity.ActivityType;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.resource.ResourceAdaptorActivityContextHandle;
import org.mobicents.slee.container.resource.ResourceAdaptorEntity;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class EndAllActivitiesRAEntityTimerTask extends TimerTask {

	private static final Logger logger = Logger.getLogger(EndAllActivitiesRAEntityTimerTask.class);
	
	private static final long delay = 60000;
	
	private final ResourceAdaptorEntity raEntity;
	private final SleeContainer sleeContainer;
	
	private boolean canceled = false;
	
	// TODO replace by ft timer?
	private final static Timer timer = new Timer();
	
	public EndAllActivitiesRAEntityTimerTask(ResourceAdaptorEntity raEntity,SleeContainer sleeContainer) {
		this.raEntity = raEntity;
		this.sleeContainer = sleeContainer;
		timer.schedule(this, delay);
	}
	
	@Override
	public boolean cancel() {
		canceled = true;
		return super.cancel();
	}
	
	private void endAllActivities() {

		// end all activities
		SleeTransactionManager txManager = sleeContainer
				.getTransactionManager();
		boolean rb = true;
		try {
			txManager.begin();
			for (ActivityContextHandle handle : sleeContainer
					.getActivityContextFactory()
					.getAllActivityContextsHandles()) {
				if (handle.getActivityType() == ActivityType.RA) {
					final ResourceAdaptorActivityContextHandle raHandle = (ResourceAdaptorActivityContextHandle) handle;
					if (raHandle.getResourceAdaptorEntity().equals(raEntity))
					try {
						if (logger.isDebugEnabled()) {
							logger.debug("Forcing the end of activity " + handle);
						}
						ActivityContext ac = sleeContainer
								.getActivityContextFactory()
								.getActivityContext(handle);
						if (ac != null) {
							// if it has a suspended event context then resume it
							EventRoutingTask routingTask = ac.getLocalActivityContext().getCurrentEventRoutingTask();
							EventContext eventContext = routingTask != null ? routingTask.getEventContext() : null;
							if (eventContext != null && eventContext.isSuspended()) {
								eventContext.resumeDelivery();
							}
							// end activity
							ac.endActivity();							
						}
					} catch (Exception e) {
						if (logger.isDebugEnabled()) {
							logger.debug("Failed to end activity " + handle, e);
						}
					}
				}
			}
			rb = false;
		} catch (Exception e) {
			logger.error("Exception while ending all activities for ra entity "
					+ raEntity.getName(), e);

		} finally {
			try {
				if (rb) {
					txManager.rollback();
				} else {
					txManager.commit();
				}
			} catch (Exception e) {
				logger.error(
						"Error in tx management while ending all activities for ra entity "
								+ raEntity.getName(), e);
			}
		}			
	}

	@Override
	public synchronized void run() {
		if (!canceled) {
			cancel();
			logger.info("Forcing the end of all activities for ra entity "
					+ raEntity.getName());
			try {
				endAllActivities();
			} catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
		}
	}
}
