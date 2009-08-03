/*
 * Created on Jul 30, 2004
 *
 *The Open SLEE project
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 *
 */

package org.mobicents.slee.runtime.activity;

import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.EventContextImpl;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

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

		return activityContext.getActivityContextHandle().getActivity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.ActivityContextInterface#attach(javax.slee.SbbLocalObject)
	 */
	public void attach(SbbLocalObject sbbLocalObject)
			throws NullPointerException, TransactionRequiredLocalException,
			TransactionRolledbackLocalException, SLEEException {

		if (sbbLocalObject == null)
			throw new NullPointerException("null SbbLocalObject !");

		sleeContainer.getTransactionManager().mandateTransaction();

		SbbLocalObjectImpl sbbLocalObjectImpl = (SbbLocalObjectImpl) sbbLocalObject;

		String sbbeId = sbbLocalObjectImpl.getSbbEntityId();

		boolean attached = getActivityContext().attachSbbEntity(sbbeId);

		if (logger.isDebugEnabled()) {
			logger
					.debug("ActivityContextInterface.attach(): ACI attach Called for "
							+ sbbLocalObject
							+ " AC = "
							+ getActivityContext()
							+ " SbbEntityId "
							+ sbbeId);
		}

		boolean setRollbackAndThrowException = false;

		if (attached) {
			try {
				SbbEntity sbbEntity = sbbLocalObjectImpl.getSbbEntity();
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
					"Failed to attach invalid sbb entity. SbbID " + sbbeId);
		}

		if (attached) {

			//            	JSLEE 1.0 Spec, Section 8.5.8 excerpt:
			//        		The SLEE delivers the event to an SBB entity that stays attached once. The SLEE may deliver the
			//        		event to the same SBB entity more than once if it has been detached and then re -attached. 
			EventContextImpl eventContextImpl = sleeContainer.getEventRouter().getEventRouterActivity(
					getActivityContext().getActivityContextHandle()).getCurrentEventContext();
			if (eventContextImpl != null && eventContextImpl.getSbbEntitiesThatHandledEvent().remove(sbbeId)) {
				if (logger.isDebugEnabled()) {
					logger
							.debug("Removed the SBB Entity ["
									+ sbbeId
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
		if (logger.isDebugEnabled()) {
			logger.debug("ACI detach called for : " + sbbLocalObject + " AC = "
					+ getActivityContext().getActivityContextHandle());
		}

		if (sbbLocalObject == null)
			throw new NullPointerException("null SbbLocalObject !");

		sleeContainer.getTransactionManager().mandateTransaction();

		SbbLocalObjectImpl sbbLocalObjectImpl = (SbbLocalObjectImpl) sbbLocalObject;

		String sbbeId = sbbLocalObjectImpl.getSbbEntityId();

		// detach ac from entity
		final ActivityContext ac = getActivityContext();
		ac.detachSbbEntity(sbbeId);

		boolean setRollbackAndThrowException = false;
		try {
			SbbEntity sbbEntity = sbbLocalObjectImpl.getSbbEntity();
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
					"Failed to detach invalid sbb entity. SbbID " + sbbeId);
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
		
		if (sbbLocalObject instanceof SbbLocalObjectImpl) {
			SbbLocalObjectImpl sbbLocalObjectImpl = (SbbLocalObjectImpl) sbbLocalObject;
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