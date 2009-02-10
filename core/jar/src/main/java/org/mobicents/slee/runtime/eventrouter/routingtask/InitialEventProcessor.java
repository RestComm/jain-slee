package org.mobicents.slee.runtime.eventrouter.routingtask;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.container.service.ServiceFactory;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactory;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class InitialEventProcessor {

	private static final Logger logger = Logger.getLogger(InitialEventProcessor.class);
	
	private static final HandleRollback handleRollback = new HandleRollback();
	private static final HandleSbbRollback handleSbbRollback = new HandleSbbRollback();
	
	/**
	 * Process the initial events of a service. This method possibly creates new
	 * Sbbs. The container keeps a factory that creates new Sbbs keyed on the
	 * convergence name of the service.
	 */
	public void processInitialEvents(ServiceComponent svc,
			MobicentsSbbDescriptor rootSbbDescriptor, DeferredEvent deferredEvent, SleeTransactionManager txMgr, ActivityContextFactory activityContextFactory)
			throws Exception {

		if (logger.isDebugEnabled()) {
			logger.debug("Initial event processing for " + svc.getServiceID());
		}
		
		Exception caught = null;
		SbbEntity sbbEntity = null;
		SbbObject sbbObject = null;
		ClassLoader invokerClassLoader = null;
		ClassLoader oldClassLoader = Thread.currentThread()
				.getContextClassLoader();
		
		try {
		
			txMgr.begin();

			try {

				/*
				 * Start of SLEE originated invocation sequence
				 * ============================================ We run a SLEE
				 * originated invocation sequence here for every service that
				 * this might be an intial event for
				 */

				/*
				 * Use the initial event select to compute the convergence names
				 * for the service. This is a method that is provided by the
				 * service deployment. The names set is composed by only one
				 * convergence name the error is due an error in the pseudocode
				 */
				final Service service = ServiceFactory.getService(svc
						.getServiceDescriptor());
				
				if (service.getState().isActive()) {

					String name = rootSbbDescriptor.computeConvergenceName(deferredEvent, svc);

					if (logger.isDebugEnabled()) {
						logger.debug("Convergence name computed for "
								+ svc.getServiceID() + " is " + name);
					}

					if (name != null) {

						if (!service.containsConvergenceName(name)) {

							if (logger.isDebugEnabled()) {
								logger.debug("not found the convergence name "
										+ name + ", creating new sbb entity");
							}
							
							// Create a new root sbb entity
							sbbEntity = service.addChild(name);
							// change class loader
							invokerClassLoader = rootSbbDescriptor.getClassLoader();
							Thread.currentThread().setContextClassLoader(
									invokerClassLoader);
							// invoke sbb lifecycle methods on sbb entity creation
							try {
								sbbEntity.assignAndCreateSbbObject();								
								sbbObject = sbbEntity.getSbbObject();
							} catch (Exception ex) {
								sbbObject = sbbEntity.getSbbObject();
								if (sbbObject != null) {
									try {
										sbbEntity.removeAndReleaseSbbObject();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								throw ex;
							}
							// attach sbb entity on AC
							ActivityContext ac = activityContextFactory.getActivityContext(deferredEvent.getActivityContextId(),true);
							if (ac.attachSbbEntity(sbbEntity.getSbbEntityId())) {
								// do the reverse on the sbb entity
								sbbEntity.afterACAttach(deferredEvent.getActivityContextId());
							}
							// passivate sbb object
							try {
								sbbEntity.passivateAndReleaseSbbObject();
								sbbObject = sbbEntity.getSbbObject();
							} catch (Exception ex) {
								sbbObject = sbbEntity.getSbbObject();
								if (sbbObject != null) {
									try {
										sbbEntity.removeAndReleaseSbbObject();										
									} catch (Exception e) {
										logger.error(e.getMessage(),e);
									}
								}								
								throw ex;
							}
						} else {
							
							if (logger.isDebugEnabled()) {
								logger.debug("found the convergence name " + name + ", attaching entity to AC (if not attached yet)");
							}
							// get sbb entity id for this convergence name
							String rootSbbEntityId = service.getRootSbbEntityId(name);
							// attach sbb entity on AC
							if (activityContextFactory.getActivityContext(deferredEvent.getActivityContextId(),true).attachSbbEntity(rootSbbEntityId)) {
								// do the reverse on the sbb entity
								SbbEntityFactory.getSbbEntity(rootSbbEntityId)
								.afterACAttach(deferredEvent.getActivityContextId());
							}
						}

					} else {
						if (logger.isDebugEnabled()) {
							logger.debug("Service with id:"	+ svc.getServiceID() + " returns a null convergence name. Either the service does not exist or it is not interested in the event.");
						}
					}
				}
				
			} catch (Exception e) {
				logger.error("Caught an error! ", e);
				caught = e;
				
			}

			boolean invokeSbbRolledBack = handleRollback.handleRollback(sbbObject, null, null, caught, invokerClassLoader, txMgr);
			
			if (sbbEntity == null && invokeSbbRolledBack) {
				// If there is no entity associated then the invokeSbbRolledBack is
				// handle in
				// the same tx, otherwise in a new tx (6.10.1)
				handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, txMgr);
				/* original code was, confirm in specs that at this time we should not send event object and aci
				 * 
					handleSbbRolledBack(sbbEntity, sbbObject, deferredEvent,
							invokerClassLoader, false);
				 */
			}
						
			// commit or rollback the tx. if the setRollbackOnly flag is set then this will trigger rollback action.
			if (logger.isDebugEnabled()) {
				logger.debug("Committing SLEE Originated Invocation Sequence");
			}
			
			try {
				txMgr.commit();
			} catch (Exception e) {
				logger.error("failed to commit transaction, invoking sbbRolledBack",e);
				invokeSbbRolledBack = true;
			}
			
			// We may need to run sbbRolledBack for invocation sequence 1 in another tx
			if (sbbEntity != null && invokeSbbRolledBack) {
				handleSbbRollback.handleSbbRolledBack(sbbEntity, sbbObject, null, null, invokerClassLoader, false, txMgr);
				/* original code was, confirm in specs that at this time we should not send event object and aci
				 * 
				handleSbbRolledBack(sbbEntity, sbbObject, deferredEvent,
						invokerClassLoader, false);
						*/
			}
						
		} catch (Exception e) {
			logger.error("Failed to process initial event for "
					+ svc.getServiceID(), e);
		}
		
		if (invokerClassLoader != null) {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}
	}

	
}
