package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.util.Iterator;

import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactory;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

public class ActivityEndEventPostProcessor {

	private static final Logger logger = Logger.getLogger(ActivityEndEventPostProcessor.class);
	
	private static final HandleRollback handleRollback = new HandleRollback();
	private static final HandleSbbRollback handleSbbRollback = new HandleSbbRollback();
	
	/**
	 * 
	 * Execute cascading remove on any root sbb entities whose attachment count
	 * has gone to zero after activity end event has detached the sbbs. If the
	 * activity end event is a received event for the sbb then this would be
	 * handled in the previous slee originated invocation sequence. It's done
	 * here in the case that the sbb is attached to the ac and so needs to be
	 * detached but activity end event is not one of it's received events. So
	 * this is a SLEE Originated Invocation Sequence containing at most one SLEE
	 * Originated Invocation of type "Remove Only"
	 * 
	 * @param ach the {@link ActivityContext} id of the activity which is ending
	 * @return
	 * @throws SystemException
	 */
	public void process(ActivityContextHandle ach,SleeTransactionManager txMgr, ActivityContextFactory acf)	throws SystemException {

		if (logger.isDebugEnabled()) {
			logger.debug("Handling an activity end event on AC "+ ach);
		}

		boolean loopAgain = false;
		Iterator<?> iter = null;

		do {

			boolean invokeSbbRolledBack = false;
			Exception caught = null;
			SbbEntity rootSbbEntity = null;
			SbbEntity sbbEntity = null;
			ActivityContext ac = null;
			String sbbEntityId = null;

			try {
				
				// 1. start a new tx for each sbb entity attached
				txMgr.begin();
				
				// 2. load ac
				ac = (ActivityContext) acf.getActivityContext(ach);
				
				// 3. get sbbs attached, once is enough since when activity ended no new attachments can be done
				if (iter == null) {
					iter = ac.getSbbAttachmentSet().iterator();
				}
				 
				if (iter.hasNext()) {
					// 4.1. load next sbb entity 
					sbbEntityId = (String) iter.next();
					try {
						sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
					} catch (Exception e) {
						// silently ignore, the sbb entity may be removed concurrently, we don't care
					}
					if (logger.isDebugEnabled()) {
						logger.debug("Dettaching sbb entity " + sbbEntityId	+ " on handle activity end event for ac " + ach);
					}
					if (sbbEntity != null) {
						// 4.2. if sbb entity found then detach from ac
						sbbEntity.afterACDetach(ach);
						// 4.3. get the sbb entity root
						if (sbbEntity.isRootSbbEntity()) {
							rootSbbEntity = sbbEntity;
						} else {
							try {
								rootSbbEntity = SbbEntityFactory.getSbbEntity(sbbEntity.getRootSbbId());
							} catch (Exception e) {
								// silently ignore, the sbb entity may be removed concurrently, we don't care
							}
						}
						if (rootSbbEntity != null && rootSbbEntity.getAttachmentCount() == 0) {
							// 4.4. claim root sbb entity if its attach count is 0
							if (logger.isDebugEnabled()) {
								logger.debug("Removing root sbb entity "+sbbEntity.getRootSbbId()+" because AC attachement count is now 0.");
							}
							SbbEntityFactory.removeSbbEntity(rootSbbEntity,true);
						}
					}
				}

				// 5. raise flag to another loop if there is another sbb entity to detach the AC
				loopAgain = iter.hasNext();
				
				if (!loopAgain) {
					// 6. there are no more sbb entities, lets use this tx to remove the AC
					acf.removeActivityContext(ac);
				}

			} catch (Exception e) {
				logger.error("Failure while handling ActivityEndEvent for ac with handle "+ach, e);
				caught = e;
				loopAgain = true;
			} finally {
				
				if (rootSbbEntity != null) {
					invokeSbbRolledBack = handleRollback.handleRollback(null,caught,rootSbbEntity.getSbbComponent().getClassLoader(),txMgr);
				}
				
				try {
					txMgr.commit();
					// We may need to run sbbRolledBack
					if (invokeSbbRolledBack) {
						try {
							if (rootSbbEntity != null) {
								handleSbbRollback.handleSbbRolledBack(rootSbbEntity, null, null, null, rootSbbEntity.getSbbComponent().getClassLoader(), true, txMgr,false);
							}
						} catch (Exception ex) {
							logger.error("problem in handleSbbRolledBack processing! ",ex);
						}
					}	
				} catch (Exception ex) {
					logger.error("Problem committing transaction!", ex);
					// reset flag to do another round, ac removal must be ensured
					loopAgain = true;
				}	
			}
			
		} while (loopAgain);		
	}
}
