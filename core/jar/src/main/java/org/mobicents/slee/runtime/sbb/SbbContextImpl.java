/*
 * SbbContextImpl.java
 *
 * Created on June 29, 2004, 12:51 PM
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 * 
 * BLAH BLAH...
 */

package org.mobicents.slee.runtime.sbb;

import java.io.Serializable;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.InvalidArgumentException;
import javax.slee.NotAttachedException;
import javax.slee.SLEEException;
import javax.slee.SbbContext;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.Tracer;
import javax.slee.management.ManagementException;
import javax.slee.management.NotificationSource;
import javax.slee.management.SbbNotification;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextFactory;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.facilities.TracerImpl;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * SBB Context Implementation.
 * 
 * The SLEE provides each SBB object with an SbbContext object. The SbbContext
 * object gives the SBB object access to the SBB object뭩 context maintained by
 * the SLEE, allows the SBB object to invoke functions provided by the SLEE, and
 * obtain information about the SBB entity assigned to the SBB object. The
 * SbbContext object implements the SbbContext interface. The SbbContext
 * interface declares the following methods: � Methods to access information
 * determined at runtime. o A getSbbLocalObject method to get an SBB local
 * object that represents the SBB entity assigned to the SBB object of the
 * SbbContext object. o A getService method to get a ServiceID object that
 * encapsulates the component identity of the Service that the SBB entity is a
 * descendent of, i.e. the SBB entity is in an SBB entity tree whose root SBB
 * entity is instantiated by the SLEE for the child relation (from SLEE as
 * parent to the root SBB) specified by the Service. o A getSbb method to get an
 * SbbID object that encapsulates the component identity of the SBB. � Activity
 * Context methods. These methods are discussed in greater detail in Section
 * 7.7.1. o A getActivities method. This method returns all the Activity
 * Contexts that the SBB entity assigned to the SBB object of the SbbContext
 * object is attached to. More precisely, it returns an Activity Context
 * Interface object for each Activity Context attached to the SBB entity
 * assigned to the SBB object of the SbbContext object. � Event mask methods.
 * These methods are described in Section 8.4.3. o A maskEvent method. This
 * method masks event types that the SBB entity assigned to the SBB object of
 * the SbbContext object no longer wishes to receive from an Activity Context. o
 * A getEventMask method. This returns the set of masked event types for an
 * Activity Context that SBB entity assigned to the SBB object of the SbbContext
 * object is attached to. � Transaction methods. These methods are described in
 * Section 6.10. o A setRollbackOnly method. The SBB Developer uses this method
 * to mark the transaction of the current method invocation for rollback. o A
 * getRollbackOnly method. The SBB Developer uses this method to determine if
 * the transaction of the current method invocation has been marked for
 * rollback.
 * 
 * @author M. Ranganathan
 * @author F. Moggia
 * @author Eduardo Martins
 */
public class SbbContextImpl implements SbbContext, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7746746092548069113L;

	volatile private static Logger logger = Logger.getLogger(SbbContextImpl.class);

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

	/** The SBB entity to which I am assigned. */
	private SbbObject sbbObject;
	/**
	 * Notification source for this Sbb
	 */
	private NotificationSource notificationSource = null;

	/**
	 * Creates a new instance of SbbContextImpl
	 * 
	 * @param notificationSource
	 */
	public SbbContextImpl(SbbObject sbbObject) {
		this.sbbObject = sbbObject;
		this.notificationSource = new SbbNotification(getService(),getSbb());
	}

	public ActivityContextInterface[] getActivities() throws TransactionRequiredLocalException, IllegalStateException, SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("getActivities() " + this.sbbObject.getState());
		}
		if (SbbObjectState.READY != this.sbbObject.getState()) {
			throw new IllegalStateException("Cannot call SbbContext getActivities in " + this.sbbObject.getState());
		}
		Set activities = sbbObject.getSbbEntity().getActivityContexts();
		Object[] activityContextHandles = activities.toArray();
		ActivityContextInterface[] aci = new ActivityContextInterface[activityContextHandles.length];
		if (logger.isDebugEnabled()) {
			logger.debug("The Sbb is attached to " + activityContextHandles.length + "activities");
		}
		ActivityContextFactory acf = sleeContainer.getActivityContextFactory();
		for (int i = 0; i < activityContextHandles.length; i++) {
			ActivityContext ac = acf.getActivityContext((ActivityContextHandle) activityContextHandles[i]);
			aci[i] = new ActivityContextInterfaceImpl(ac);
		}
		return aci;
	}

	public String[] getEventMask(ActivityContextInterface aci) throws NullPointerException, TransactionRequiredLocalException, IllegalStateException, NotAttachedException, SLEEException {
		if (aci == null)
			throw new NullPointerException("Activity Context Interface cannot be null.");
		if (sbbObject == null || sbbObject.getState() != SbbObjectState.READY)
			throw new IllegalStateException("Wrong state! " + (sbbObject == null ? null : sbbObject.getState()));

		if (this.sbbObject.getSbbEntity() == null) {
			throw new IllegalStateException("Wrong state! SbbEntity is not assigned");
		}
		sleeContainer.getTransactionManager().mandateTransaction();
		ActivityContextHandle ach = ((ActivityContextInterfaceImpl) aci).getActivityContext().getActivityContextHandle();
		if (!sbbObject.getSbbEntity().isAttached(ach))
			throw new NotAttachedException("ACI not attached to SBB");

		return sbbObject.getSbbEntity().getEventMask(ach);
	}

	public boolean getRollbackOnly() throws TransactionRequiredLocalException, SLEEException {
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();
		if (logger.isDebugEnabled()) {
			logger.debug("in getRollbackOnly on " + this);
		}
		try {
			return txMgr.getRollbackOnly();
		} catch (SystemException e) {
			throw new SLEEException("Problem with the tx manager!");
		}
	}

	public SbbID getSbb() throws SLEEException {
		return this.sbbObject.getSbbComponent().getSbbID();
	}

	public SbbLocalObject getSbbLocalObject() throws TransactionRequiredLocalException, IllegalStateException, SLEEException {
		sleeContainer.getTransactionManager().mandateTransaction();
		if (this.sbbObject == null || this.sbbObject.getSbbEntity() == null || this.sbbObject.getState() != SbbObjectState.READY)
			throw new IllegalStateException("Bad state : " + this.sbbObject.getState());
		Class sbbLocalClass;
		if ((sbbLocalClass = sbbObject.getSbbComponent().getSbbLocalInterfaceConcreteClass()) != null) {
			Object[] objs = { sbbObject.getSbbEntity() };
			Class[] types = { SbbEntity.class };
			try {
				return (SbbLocalObject) sbbLocalClass.getConstructor(types).newInstance(objs);
			} catch (Exception e) {
				throw new RuntimeException("Failed to create Sbb Local Interface.", e);
			}
		} else {
			return new SbbLocalObjectImpl(this.sbbObject.getSbbEntity());
		}
	}

	public ServiceID getService() throws SLEEException {
		// return this.sbbObject.getSbbEntity().getServiceId();
		return this.sbbObject.getServiceID();
	}

	public void maskEvent(String[] eventNames, ActivityContextInterface aci) throws NullPointerException, TransactionRequiredLocalException, IllegalStateException, UnrecognizedEventException,
			NotAttachedException, SLEEException {
		if (SbbObjectState.READY != this.sbbObject.getState()) {
			throw new IllegalStateException("Cannot call SbbContext maskEvent in " + this.sbbObject.getState());
		}

		if (this.sbbObject.getSbbEntity() == null) {
			// this shouldnt happen since SbbObject state ready shoudl be set
			// when its fully setup, but....
			throw new IllegalStateException("Wrong state! SbbEntity is not assigned");
		}

		sleeContainer.getTransactionManager().mandateTransaction();
		ActivityContextHandle ach = ((ActivityContextInterfaceImpl) aci).getActivityContext().getActivityContextHandle();

		if (!sbbObject.getSbbEntity().isAttached(ach))
			throw new NotAttachedException("ACI is not attached to SBB ");
		sbbObject.getSbbEntity().setEventMask(ach, eventNames);

	}

	/**
	 * A setRollbackOnly method. The SBB Developer uses this method to mark the
	 * transaction of the current method invocation for rollback. A
	 * getRollbackOnly method. The SBB Developer uses this method to determine
	 * if the transaction of the current method invocation has been marked for
	 * rollback.
	 */
	public void setRollbackOnly() throws TransactionRequiredLocalException, SLEEException {
		SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		sleeTransactionManager.mandateTransaction();

		logger.debug("in setRollbackOnly on " + this);

		try {
			sleeTransactionManager.setRollbackOnly();

		} catch (SystemException e) {
			throw new SLEEException("tx manager failure!");
		}
	}

	public Tracer getTracer(String tracerName) throws NullPointerException, IllegalArgumentException, SLEEException {
		
		try {
			TracerImpl.checkTracerName(tracerName, this.notificationSource);
		} catch (InvalidArgumentException e1) {
			throw new IllegalArgumentException(e1);
		}
		try {
			return this.sleeContainer.getTraceFacility().getTraceMBeanImpl().createTracer(this.notificationSource, tracerName, true);
		} catch (ManagementException e) {

			// e.printStackTrace();
			throw new SLEEException("Failed to crate tracer: " + tracerName, e);
		}
	}

}
