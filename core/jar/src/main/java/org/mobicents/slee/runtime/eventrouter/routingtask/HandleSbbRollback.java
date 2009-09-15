package org.mobicents.slee.runtime.eventrouter.routingtask;

import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceFactory;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.RolledBackContextImpl;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectPool;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class HandleSbbRollback {

	private static final Logger logger = Logger.getLogger(HandleSbbRollback.class);
	
	private final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param sbbEntity
	 *            The sbb entity - optional - if the invocation sequence does
	 *            not have a target sbb entity this is null
	 * @param sbbObject
	 *            The sbb object - optional this is only specified if the
	 *            invocation sequence does not have a target sbb entity
	 * @param eventObject
	 *            The slee event - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param aci
	 *            The aci where the event was fired - only specified if the transaction that rolled
	 *            back tried to deliver an event
	 * @param contextClassLoader
	 * @param removeRolledBack
	 * 
	 * 
	 */
	public void handleSbbRolledBack(SbbEntity sbbEntity, SbbObject sbbObject, DeferredEvent de, ActivityContextInterface aci,
			ClassLoader contextClassLoader,
			boolean removeRolledBack,SleeTransactionManager txMgr, boolean keepSbbEntityIfTxRollbacks) {
		// Sanity checks
		if ((sbbEntity == null && sbbObject == null)
				|| (sbbEntity != null && sbbObject != null)) {
			logger
					.error("Illegal State! Only one of sbbEntity or SbbObject can be specified");

			return;
		}

		/*
		 * Depending on whether exceptions were thrown during the invocation
		 * sequence we may need to invoke the sbbRolledBack method This must be
		 * done on a different sbb object and a new transaction See Spec. Sec.
		 * 9.12.2 for details
		 */
		if (logger.isDebugEnabled()) {
			logger.debug("Invoking sbbRolledBack");
		}

		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		
		boolean createERTD = false;
		try {

			// Only start new tx if there's a target sbb entity (6.10.1)
			if (sbbEntity != null) {
				txMgr.begin();
				if(EventRoutingTransactionData.getFromTransactionContext()==null){
					createERTD = true;
					EventRoutingTransactionData ertd= new EventRoutingTransactionData(de,aci);
					ertd.putInTransactionContext();
				}
				// we have to refresh the sbb entity by reading it frmo the
				// cache
				try {
					sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntity.getSbbEntityId());
				} catch (Exception e) {
					// sbb entity does not exists, recreate it
					if (sbbEntity.isRootSbbEntity()) {
						ServiceComponent serviceComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(sbbEntity.getServiceId());
						final Service service = ServiceFactory.getService(serviceComponent);
						sbbEntity = service.addChild(sbbEntity.getServiceConvergenceName());
					}
					else {
						sbbEntity = SbbEntityFactory.createSbbEntity(sbbEntity.getSbbId(), sbbEntity.getServiceId(), sbbEntity.getParentSbbEntityId(), sbbEntity.getParentChildRelation(), sbbEntity.getRootSbbId(), sbbEntity.getServiceConvergenceName());
					}
					if(keepSbbEntityIfTxRollbacks) {
						// recreate attachment too
						ActivityContext ac = ((org.mobicents.slee.runtime.activity.ActivityContextInterface)aci).getActivityContext();
						if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
							sbbEntity.afterACAttach(ac.getActivityContextHandle());
						}
					}
					else {
						// the sbb entity recreated should not be kept
						txMgr.setRollbackOnly();
					}
				}
			}

			RolledBackContext rollbackContext = new RolledBackContextImpl(
					de!=null?de.getEvent():null,
					aci, removeRolledBack);

			Thread.currentThread().setContextClassLoader(contextClassLoader);

			if (sbbEntity != null) {
				// We invoke the callback method a *different* sbb object 9.12.2
				// and 6.10.1
				if (logger.isDebugEnabled()) {
					logger
							.debug("Invoking sbbRolledBack on different sbb object");
				}
				SbbObjectPool pool = sbbEntity.getObjectPool();

				// Get rid of old object (if any) first
				if (sbbEntity.getSbbObject() != null) {
					// This was set to DOES_NOT_EXIST here because
					// unsetSbbContext
					// should not be called in this case.
					sbbEntity.getSbbObject().setState(
							SbbObjectState.DOES_NOT_EXIST);
					pool.invalidateObject(sbbEntity.getSbbObject());
				}

				sbbEntity.assignSbbObject();
				sbbObject = sbbEntity.getSbbObject();
				sbbObject.sbbLoad();
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking sbbRolledBack");
			}
			// We only invoke this on objects in the ready state 6.10.1
			// E.g. if an exception was thrown from a sbbCreate then there will
			// be no sbb entity
			// and the the sbbobject won't be in the ready state so we invoke it
			if (sbbObject.getState() == SbbObjectState.READY)
				sbbObject.sbbRolledBack(rollbackContext);

			if (sbbEntity != null) {
				sbbObject.sbbStore();
			
				try {
					if (txMgr.getRollbackOnly()) {
						txMgr.rollback();
					}
					else {
						if(createERTD)
						{
							EventRoutingTransactionData.getFromTransactionContext().removeFromTransactionContext();
						}
						txMgr.commit();
					}
				} catch (SystemException ex) {					
					throw new RuntimeException("tx manager System Failure ", ex);
				}
			}
		} catch (Exception e) {
			// If an exception is thrown here we just log it and don't retry
			if (sbbObject != null && sbbEntity != null) {
				sbbObject = sbbEntity.getSbbObject();
				sbbObject.setState(SbbObjectState.DOES_NOT_EXIST);
			}
			logger
					.error(
							"Exception thrown in attempting to invoke sbbRolledBack",
							e);
			sbbObject.sbbExceptionThrown(e);
		} finally {
			try {
				if (txMgr.getTransaction() != null) {
					try {
						if (txMgr.getRollbackOnly()) {
							txMgr.rollback();
						}
						else {
							txMgr.commit();
						}
					} catch (SystemException ex) {					
						throw new RuntimeException("tx manager System Failure ", ex);
					}
				}
			} catch (Exception e2) {
				logger.error("Failed to commit transaction", e2);
				throw new RuntimeException("Failed to commit tx ", e2);
			}
			finally {
				Thread.currentThread().setContextClassLoader(oldClassLoader);
			}
		}
	}
	
}
