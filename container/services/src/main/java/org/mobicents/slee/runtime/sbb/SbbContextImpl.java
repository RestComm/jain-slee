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

package org.mobicents.slee.runtime.sbb;

import java.util.ArrayList;
import java.util.List;

import javax.slee.ActivityContextInterface;
import javax.slee.InvalidArgumentException;
import javax.slee.NotAttachedException;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.AlarmFacility;
import javax.slee.facilities.TimerFacility;
import javax.slee.facilities.Tracer;
import javax.slee.management.NotificationSource;
import javax.slee.management.SbbNotification;
import javax.slee.management.UnrecognizedLinkNameException;
import javax.slee.nullactivity.NullActivityContextInterfaceFactory;
import javax.slee.nullactivity.NullActivityFactory;
import javax.slee.profile.ProfileFacility;
import javax.slee.profile.ProfileTableActivityContextInterfaceFactory;
import javax.slee.resource.ResourceAdaptorTypeID;
import javax.slee.serviceactivity.ServiceActivityContextInterfaceFactory;
import javax.slee.serviceactivity.ServiceActivityFactory;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextFactory;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.sbb.SbbLocalObject;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

/**
 * SBB Context Implementation.
 * 
 * The SLEE provides each SBB object with an SbbContext object. The SbbContext
 * object gives the SBB object access to the SBB object�s context maintained by
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
public class SbbContextImpl implements SbbContextExt {

	private static final Logger logger = Logger.getLogger(SbbContextImpl.class);

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	/** The SBB entity to which I am assigned. */
	private final SbbObject sbbObject;

	/**
	 * Notification source for this Sbb
	 */
	private final NotificationSource notificationSource;

	/**
	 * Creates a new instance of SbbContextImpl
	 * 
	 * @param notificationSource
	 */
	public SbbContextImpl(SbbObject sbbObject) {
		this.sbbObject = sbbObject;
		this.notificationSource = new SbbNotification(getService(), getSbb());
	}

	private static ActivityContextInterface[] EMPTY_ACI_ARRAY = {};

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getActivities()
	 */
	public ActivityContextInterface[] getActivities()
			throws TransactionRequiredLocalException, IllegalStateException,
			SLEEException {
		if (logger.isTraceEnabled()) {
			logger.trace("getActivities() " + this.sbbObject.getState());
		}
		if (SbbObjectState.READY != this.sbbObject.getState()) {
			throw new IllegalStateException(
					"Cannot call SbbContext.getActivities() in "
							+ this.sbbObject.getState());
		}
		ActivityContextFactory acf = sleeContainer.getActivityContextFactory();
		List<ActivityContextInterface> result = new ArrayList<ActivityContextInterface>();
		ActivityContext ac = null;
		for (ActivityContextHandle ach : sbbObject.getSbbEntity()
				.getActivityContexts()) {
			ac = acf.getActivityContext(ach);
			if (ac != null) {
				result.add(ac.getActivityContextInterface());
			}
		}
		return result.toArray(EMPTY_ACI_ARRAY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.SbbContext#getEventMask(javax.slee.ActivityContextInterface)
	 */
	public String[] getEventMask(ActivityContextInterface aci)
			throws NullPointerException, TransactionRequiredLocalException,
			IllegalStateException, NotAttachedException, SLEEException {
		if (aci == null)
			throw new NullPointerException(
					"Activity Context Interface cannot be null.");
		if (sbbObject == null || sbbObject.getState() != SbbObjectState.READY)
			throw new IllegalStateException("Wrong state! "
					+ (sbbObject == null ? null : sbbObject.getState()));

		if (this.sbbObject.getSbbEntity() == null) {
			throw new IllegalStateException(
					"Wrong state! SbbEntity is not assigned");
		}
		sleeContainer.getTransactionManager().mandateTransaction();
		ActivityContextHandle ach = ((org.mobicents.slee.container.activity.ActivityContextInterface) aci)
				.getActivityContext().getActivityContextHandle();
		if (!sbbObject.getSbbEntity().isAttached(ach))
			throw new NotAttachedException("ACI not attached to SBB");

		return sbbObject.getSbbEntity().getEventMask(ach);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getRollbackOnly()
	 */
	public boolean getRollbackOnly() throws TransactionRequiredLocalException,
			SLEEException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getSbb()
	 */
	public SbbID getSbb() throws SLEEException {
		return this.sbbObject.getSbbComponent().getSbbID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getSbbLocalObject()
	 */
	public SbbLocalObject getSbbLocalObject()
			throws TransactionRequiredLocalException, IllegalStateException,
			SLEEException {
		sleeContainer.getTransactionManager().mandateTransaction();

		if (this.sbbObject.getState() != SbbObjectState.READY)
			throw new IllegalStateException("Bad state : "
					+ this.sbbObject.getState());

		return sbbObject.getSbbEntity().getSbbLocalObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getService()
	 */
	public ServiceID getService() throws SLEEException {
		// return this.sbbObject.getSbbEntity().getServiceId();
		return this.sbbObject.getServiceID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#maskEvent(java.lang.String[],
	 * javax.slee.ActivityContextInterface)
	 */
	public void maskEvent(String[] eventNames, ActivityContextInterface aci)
			throws NullPointerException, TransactionRequiredLocalException,
			IllegalStateException, UnrecognizedEventException,
			NotAttachedException, SLEEException {
		if (SbbObjectState.READY != this.sbbObject.getState()) {
			throw new IllegalStateException(
					"Cannot call SbbContext maskEvent in "
							+ this.sbbObject.getState());
		}

		if (this.sbbObject.getSbbEntity() == null) {
			// this shouldnt happen since SbbObject state ready shoudl be set
			// when its fully setup, but....
			throw new IllegalStateException(
					"Wrong state! SbbEntity is not assigned");
		}

		sleeContainer.getTransactionManager().mandateTransaction();
		ActivityContextHandle ach = ((org.mobicents.slee.container.activity.ActivityContextInterface) aci)
				.getActivityContext().getActivityContextHandle();

		if (!sbbObject.getSbbEntity().isAttached(ach))
			throw new NotAttachedException("ACI is not attached to SBB ");
		sbbObject.getSbbEntity().setEventMask(ach, eventNames);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#setRollbackOnly()
	 */
	public void setRollbackOnly() throws TransactionRequiredLocalException,
			SLEEException {
		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();
		sleeTransactionManager.mandateTransaction();
		try {
			sleeTransactionManager.setRollbackOnly();
		} catch (SystemException e) {
			throw new SLEEException("failed to mark tx for rollback", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.SbbContext#getTracer(java.lang.String)
	 */
	public Tracer getTracer(String tracerName) throws NullPointerException,
			IllegalArgumentException, SLEEException {

		try {
			return sleeContainer.getTraceManagement().createTracer(
					this.notificationSource, tracerName, true);
		} catch (InvalidArgumentException e) {
			throw new IllegalArgumentException(e);
		}
	}

	// SbbContextExt

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.SbbContextExt#getActivityContextInterfaceFactory(javax
	 * .slee.resource.ResourceAdaptorTypeID)
	 */
	public Object getActivityContextInterfaceFactory(
			ResourceAdaptorTypeID raTypeID) throws NullPointerException,
			IllegalArgumentException {
		if (raTypeID == null) {
			throw new NullPointerException("null ra type id");
		}
		if (!sbbObject.getSbbComponent().getDependenciesSet().contains(raTypeID)) {
			throw new IllegalArgumentException("ra type "+raTypeID+" not referred by the sbb.");
		}
		return sleeContainer.getComponentRepository().getComponentByID(raTypeID).getActivityContextInterfaceFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getActivityContextNamingFacility()
	 */
	public ActivityContextNamingFacility getActivityContextNamingFacility() {
		return sleeContainer.getActivityContextNamingFacility();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getAlarmFacility()
	 */
	public AlarmFacility getAlarmFacility() {
		return sbbObject.getSbbComponent().getAlarmFacility();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.SbbContextExt#getNullActivityContextInterfaceFactory()
	 */
	public NullActivityContextInterfaceFactory getNullActivityContextInterfaceFactory() {
		return sleeContainer.getNullActivityContextInterfaceFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getNullActivityFactory()
	 */
	public NullActivityFactory getNullActivityFactory() {
		return sleeContainer.getNullActivityFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getProfileFacility()
	 */
	public ProfileFacility getProfileFacility() {
		return sleeContainer.getSleeProfileTableManager().getProfileFacility();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.mobicents.slee.SbbContextExt#
	 * getProfileTableActivityContextInterfaceFactory()
	 */
	public ProfileTableActivityContextInterfaceFactory getProfileTableActivityContextInterfaceFactory() {
		return sleeContainer.getSleeProfileTableManager()
				.getProfileTableActivityContextInterfaceFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.SbbContextExt#getResourceAdaptorInterface(javax.slee
	 * .resource.ResourceAdaptorTypeID, java.lang.String)
	 */
	public Object getResourceAdaptorInterface(ResourceAdaptorTypeID raTypeID,
			String raLink) throws NullPointerException,
			IllegalArgumentException {
		if (raTypeID == null) {
			throw new NullPointerException("null ra type id");
		}
		if (raLink == null) {
			throw new NullPointerException("null ra link");
		}
		if (!sbbObject.getSbbComponent().getDependenciesSet().contains(raTypeID)) {
			throw new IllegalArgumentException("ra type "+raTypeID+" not referred by the sbb.");
		}
		final ResourceManagement resourceManagement = sleeContainer.getResourceManagement();
		
		String raEntityName = null;
		try {
			raEntityName = resourceManagement.getResourceAdaptorEntityName(raLink);
		} catch (UnrecognizedLinkNameException e) {
			throw new IllegalArgumentException("ra link "+raLink+" not found.");
		}
		
		return resourceManagement.getResourceAdaptorEntity(raEntityName).getResourceAdaptorInterface(raTypeID);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.SbbContextExt#getServiceActivityContextInterfaceFactory
	 * ()
	 */
	public ServiceActivityContextInterfaceFactory getServiceActivityContextInterfaceFactory() {
		return sleeContainer.getServiceManagement().getServiceActivityContextInterfaceFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getServiceActivityFactory()
	 */
	public ServiceActivityFactory getServiceActivityFactory() {
		return sleeContainer.getServiceManagement().getServiceActivityFactory();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.SbbContextExt#getTimerFacility()
	 */
	public TimerFacility getTimerFacility() {
		return sleeContainer.getTimerFacility();
	}

}
