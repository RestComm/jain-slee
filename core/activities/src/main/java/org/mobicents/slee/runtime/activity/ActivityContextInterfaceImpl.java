package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextInterface;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.sbbentity.SbbEntity;

/**
 * 
 * Activity context interface - default implementation. The Sbb deployer has to
 * imbed an instance of this as a proxy object in each sbb ACI.
 * 
 * This is the SLEE wrapper data structure for Activity Contexts. The Sbb gets
 * to access this rather than the activity. The reason this exists is because
 * the activity context can be at a different location than the activity context
 * interface (does not need to be co-located in the same jvm. )
 * 
 * @author M. Ranganathan
 * @author Ralf Siedow
 * @author martins
 *  
 */
public class ActivityContextInterfaceImpl implements ActivityContextInterface {

	private static Logger logger = Logger
			.getLogger(ActivityContextInterfaceImpl.class);

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();
	
	private boolean doTraceLogs = logger.isTraceEnabled();

	private final ActivityContext activityContext;

	/**
	 * This is allocated by the Slee to wrap an incoming event (activity).
	 * 
	 * @param activityContextHandle
	 */
	public ActivityContextInterfaceImpl(ActivityContext activityContext) {
		this.activityContext = activityContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.ActivityContextInterface#getActivity()
	 */
	public Object getActivity() throws TransactionRequiredLocalException,
			SLEEException {

		sleeContainer.getTransactionManager().mandateTransaction();
		
		return activityContext.getActivityContextHandle().getActivityObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.ActivityContextInterface#attach(javax.slee.SbbLocalObject)
	 */
	public void attach(SbbLocalObject sbbLocalObject)
			throws NullPointerException, TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {

		if (doTraceLogs) {
        	logger.trace("attach( ac = "+activityContext+" , sbbLocalObject = " + sbbLocalObject + " )");
		}
		
		if (sbbLocalObject == null)
			throw new NullPointerException("null SbbLocalObject !");

		sleeContainer.getTransactionManager().mandateTransaction();

		org.mobicents.slee.container.sbb.SbbLocalObject sbbLocalObjectImpl = (org.mobicents.slee.container.sbb.SbbLocalObject) sbbLocalObject;

		SbbEntity sbbEntity = sbbLocalObjectImpl.getSbbEntity();

		boolean attached = getActivityContext().attachSbbEntity(sbbEntity.getSbbEntityId());

		boolean setRollbackAndThrowException = false;

		if (attached) {
			try {
				if (sbbEntity.isRemoved()) {
					setRollbackAndThrowException = true;
				} else {
					// attach entity from ac
					sbbEntity.afterACAttach(getActivityContext().getActivityContextHandle());
				}
			} catch (Exception e) {
				setRollbackAndThrowException = true;
			}
		}

		if (setRollbackAndThrowException) {
			try {
				sleeContainer.getTransactionManager().setRollbackOnly();
			} catch (SystemException e) {
				logger
						.warn(
								"failed to set rollback flag while asserting valid sbb entity",
								e);
			}
			throw new TransactionRolledbackLocalException(
					"Failed to attach invalid sbb entity. SbbID " + sbbEntity.getSbbEntityId());
		}

		if (attached) {

			//            	JSLEE 1.0 Spec, Section 8.5.8 excerpt:
			//        		The SLEE delivers the event to an SBB entity that stays attached once. The SLEE may deliver the
			//        		event to the same SBB entity more than once if it has been detached and then re -attached. 
			final EventRoutingTask routingTask = activityContext.getLocalActivityContext().getCurrentEventRoutingTask();
			EventContext eventContextImpl = routingTask != null ? routingTask.getEventContext() : null; 
			if (eventContextImpl != null && eventContextImpl.getSbbEntitiesThatHandledEvent().remove(sbbEntity.getSbbEntityId())) {
				if (doTraceLogs) {
		        	logger.trace("Removed the SBB Entity ["
									+ sbbEntity.getSbbEntityId()
									+ "] from the delivered set of activity context ["
									+ getActivityContext().getActivityContextHandle()
									+ "]. Seems to be a reattachment after detachment in the same event delivery transaction. See JSLEE 1.0 Spec, Section 8.5.8.");
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.ActivityContextInterface#detach(javax.slee.SbbLocalObject)
	 */
	public void detach(SbbLocalObject sbbLocalObject)
			throws NullPointerException, TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		
		if (doTraceLogs) {
        	logger.trace("detach( ac = "+activityContext+" , sbbLocalObject = " + sbbLocalObject + " )");
		}

		if (sbbLocalObject == null)
			throw new NullPointerException("null SbbLocalObject !");

		sleeContainer.getTransactionManager().mandateTransaction();

		org.mobicents.slee.container.sbb.SbbLocalObject sbbLocalObjectImpl = (org.mobicents.slee.container.sbb.SbbLocalObject) sbbLocalObject;

		SbbEntity sbbEntity = sbbLocalObjectImpl.getSbbEntity();

		// detach ac from entity
		final ActivityContext ac = getActivityContext();
		ac.detachSbbEntity(sbbEntity.getSbbEntityId());

		boolean setRollbackAndThrowException = false;
		try {
			if (sbbEntity.isRemoved()) {
				setRollbackAndThrowException = true;
			} else {
				// detach entity from ac
				sbbEntity.afterACDetach(getActivityContext().getActivityContextHandle());
			}
		} catch (Exception e) {
			setRollbackAndThrowException = true;
		}
		if (setRollbackAndThrowException) {
			try {
				sleeContainer.getTransactionManager().setRollbackOnly();
			} catch (SystemException e) {
				logger
						.warn(
								"failed to set rollback flag while asserting valid sbb entity",
								e);
			}
			throw new TransactionRolledbackLocalException(
					"Failed to detach invalid sbb entity. SbbID " + sbbEntity.getSbbEntityId());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.ActivityContextInterface#isEnding()
	 */
	public boolean isEnding() throws TransactionRequiredLocalException,
			SLEEException {
		sleeContainer.getTransactionManager().mandateTransaction();
		return getActivityContext().isEnding();
	}

	public ActivityContext getActivityContext() {
		return activityContext;
	}

	@Override
	public int hashCode() {
		return activityContext.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((ActivityContextInterfaceImpl) obj).activityContext
					.equals(this.activityContext);
		} else {
			return false;
		}
	}

	/**
	 * @see javax.slee.ActivityContextInterface#isAttached(SbbLocalObject)
	 */
	public boolean isAttached(SbbLocalObject sbbLocalObject) throws NullPointerException,
			TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {
		
		if (sbbLocalObject == null) {
			throw new NullPointerException("null sbbLocalObject");
		}
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		if (sbbLocalObject instanceof org.mobicents.slee.container.sbb.SbbLocalObject) {
			org.mobicents.slee.container.sbb.SbbLocalObject sbbLocalObjectImpl = (org.mobicents.slee.container.sbb.SbbLocalObject) sbbLocalObject;
			SbbEntity sbbEntity = sbbLocalObjectImpl.getSbbEntity();
			if (sbbEntity != null && !sbbEntity.isRemoved()) {				
				return sbbEntity.isAttached(activityContext.getActivityContextHandle());
			}
		}
		
		try {
			sleeContainer.getTransactionManager().setRollbackOnly();
		} catch (Exception e) {
			throw new SLEEException(e.getMessage(),e);
		}
		throw new TransactionRolledbackLocalException("the sbbLocalObject argument must represent a valid SBB entity");
	}
}