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

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;
import org.mobicents.slee.container.component.sbb.GetProfileCMPMethodDescriptor;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.event.EventContextHandle;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.management.ProfileManagement;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;
import org.mobicents.slee.container.profile.ProfileLocalObject;
import org.mobicents.slee.container.profile.ProfileTable;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.sbbentity.ChildRelation;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.sbbentity.ProfileLocalObjectCmpValue;

/**
 * The logic to implement sbb abstract methods.
 * 
 * @author martins
 * 
 */
public class SbbAbstractMethodHandler {

	private static final Logger logger = Logger.getLogger(SbbAbstractMethodHandler.class);

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	// CMP ACCESSORs

	private static final Boolean DEFAULT_CMP_VALUE_BOOLEAN = Boolean.FALSE;
	private static final Byte DEFAULT_CMP_VALUE_BYTE = Byte.valueOf((byte) 0);
	private static final Character DEFAULT_CMP_VALUE_CHAR = Character.valueOf((char) 0);
	private static final Double DEFAULT_CMP_VALUE_DOUBLE = Double.valueOf(0);
	private static final Float DEFAULT_CMP_VALUE_FLOAT = Float.valueOf(0);
	private static final Integer DEFAULT_CMP_VALUE_INTEGER = Integer.valueOf(0);
	private static final Long DEFAULT_CMP_VALUE_LONG = Long.valueOf(0);
	private static final Short DEFAULT_CMP_VALUE_SHORT = Short.valueOf((short)0);
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			char cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Character.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			byte cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Byte.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			short cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Short.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			int cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Integer.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			long cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Long.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			float cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Float.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			double cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Double.valueOf(cmpFieldValue));
	}
	
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName,
			boolean cmpFieldValue) {
		SbbAbstractMethodHandler.setCMPFieldOfTypePrimitiveOrUnknown(sbbEntity,cmpFieldName, Boolean.valueOf(cmpFieldValue));
	}
	
	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @param cmpFieldValue
	 */
	public static void setCMPFieldOfTypeActivityContextInterface(SbbEntity sbbEntity, String cmpFieldName, ActivityContextInterface cmpFieldValue) {
		if (cmpFieldValue == null) {
			sbbEntity.setCMPField(cmpFieldName, null);
			return;
		}
		org.mobicents.slee.container.activity.ActivityContextInterface aci = null;
		try {
			aci = (org.mobicents.slee.container.activity.ActivityContextInterface) cmpFieldValue;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("CMP value being set (" + cmpFieldValue + ") is an unknown ActivityContextInterface implementation");
		}
		sbbEntity.setCMPField(cmpFieldName, aci.getActivityContext().getActivityContextHandle());
	}

	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @param cmpFieldValue
	 */
	public static void setCMPFieldOfTypeEventContext(SbbEntity sbbEntity, String cmpFieldName, javax.slee.EventContext cmpFieldValue) {
		if (cmpFieldValue == null) {
			sbbEntity.setCMPField(cmpFieldName, null);
			return;
		}
		EventContext eventContextImpl = null;
		try {
			eventContextImpl = (EventContext) cmpFieldValue;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("CMP value being set (" + cmpFieldValue + ") is an unknown EventContext implementation");
		}
		sbbEntity.setCMPField(cmpFieldName,eventContextImpl.getEventContextHandle());
	}

	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @param cmpFieldValue
	 */
	public static void setCMPFieldOfTypePrimitiveOrUnknown(SbbEntity sbbEntity, String cmpFieldName, Object cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName,cmpFieldValue);
	}

	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @param object
	 */
	public static void setCMPFieldOfTypeProfileLocalObject(SbbEntity sbbEntity, String cmpFieldName, javax.slee.profile.ProfileLocalObject cmpFieldValue) {
		if (cmpFieldValue == null) {
			sbbEntity.setCMPField(cmpFieldName, null);
			return;
		}
		ProfileLocalObject profileLocalObjectConcreteImpl = null;
		try {
			profileLocalObjectConcreteImpl = (ProfileLocalObject) cmpFieldValue;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("CMP value being set (" + cmpFieldValue + ") is an unknown ProfileLocalObject implementation");
		}
		final ProfileLocalObjectCmpValue profileLocalObjectCmpValue = new ProfileLocalObjectCmpValue(profileLocalObjectConcreteImpl.getProfileTableName(),profileLocalObjectConcreteImpl.getProfileName());
		sbbEntity.setCMPField(cmpFieldName, profileLocalObjectCmpValue);
	}

	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @param cmpFieldValue
	 */
	public static void setCMPFieldOfTypeSbbLocalObject(SbbEntity sbbEntity, String cmpFieldName, SbbLocalObject cmpFieldValue) {
		if (cmpFieldValue == null) {
			sbbEntity.setCMPField(cmpFieldName, null);
			return;
		}
		SbbLocalObjectImpl sbbLocalObjectImpl = null;
		try {
			sbbLocalObjectImpl = (SbbLocalObjectImpl) cmpFieldValue;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("CMP value being set ("+ cmpFieldValue+ ") is an unknown SbbLocalObject implementation");
		}
		final CMPFieldDescriptor field = sbbEntity.getSbbComponent().getDescriptor().getCmpFields().get(cmpFieldName);
		if (field.getSbbRef() != null && !field.getSbbRef().equals(sbbLocalObjectImpl.getSbbEntity().getSbbComponent().getSbbID())) {
			throw new IllegalArgumentException("CMP value being set ("+ sbbLocalObjectImpl.getSbbEntity().getSbbComponent().getSbbID()+ ") is for a different sbb then the one expected ("+ field.getSbbRef() + ")");
		}
		sbbEntity.setCMPField(cmpFieldName, sbbLocalObjectImpl.getSbbEntityId());
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @param returnType
	 * @return
	 */
	public static Object getCMPFieldOfTypeUnknown(SbbEntity sbbEntity,String cmpFieldName) {
		return sbbEntity.getCMPField(cmpFieldName);
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Integer getCMPFieldOfTypeInteger(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return DEFAULT_CMP_VALUE_INTEGER;
		}
		else {
			return (Integer) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Boolean getCMPFieldOfTypeBoolean(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {			
			return DEFAULT_CMP_VALUE_BOOLEAN;
		}
		else {
			return (Boolean) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Byte getCMPFieldOfTypeByte(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return DEFAULT_CMP_VALUE_BYTE;
		}
		else {
			return (Byte) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Character getCMPFieldOfTypeChar(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {			
			return DEFAULT_CMP_VALUE_CHAR;
		}
		else {
			return (Character) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Short getCMPFieldOfTypeShort(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {			
			return DEFAULT_CMP_VALUE_SHORT;
		}
		else {
			return (Short) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Long getCMPFieldOfTypeLong(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return DEFAULT_CMP_VALUE_LONG;
		}
		else {
			return (Long) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Double getCMPFieldOfTypeDouble(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return DEFAULT_CMP_VALUE_DOUBLE;
		}
		else {
			return (Double) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param cmpFieldName
	 * @return
	 */
	public static Float getCMPFieldOfTypeFloat(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return DEFAULT_CMP_VALUE_FLOAT;
		}
		else {
			return (Float) cmpFieldValue;
		}
	}
	
	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @return
	 */
	public static ActivityContextInterface getCMPFieldOfTypeActivityContextInterface(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return null;
		}
		else {
			final ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext((ActivityContextHandle) cmpFieldValue);
			if (ac != null) {
				return sbbEntity.asSbbActivityContextInterface(ac.getActivityContextInterface());
			} else {
				return null;
			}
		}
	}
	
	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @return
	 */
	public static javax.slee.EventContext getCMPFieldOfTypeEventContext(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return null;
		}
		else {
			return sleeContainer.getEventContextFactory().getEventContext((EventContextHandle)cmpFieldValue);
		}
	}
	
	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @return
	 */
	public static javax.slee.profile.ProfileLocalObject getCMPFieldOfTypeProfileLocalObject(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return null;
		}
		else {
			final ProfileLocalObjectCmpValue profileLocalObjectCmpValue = (ProfileLocalObjectCmpValue) cmpFieldValue;
			try {
				final ProfileTable profileTable = sleeContainer.getSleeProfileTableManager().getProfileTable(profileLocalObjectCmpValue.getProfileTableName());
				return profileTable.find(profileLocalObjectCmpValue.getProfileName());
			} catch (UnrecognizedProfileTableNameException e) {
				return null;
			}
		}
	}
	
	/**
	 * 
	 * @param sbbEntity
	 * @param cmpFieldName
	 * @return
	 */
	public static SbbLocalObject getCMPFieldOfTypeSbbLocalObject(SbbEntity sbbEntity,String cmpFieldName) {
		final Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);
		if (cmpFieldValue == null) {
			return null;
		}
		else {
			final SbbEntity anotherSbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity((SbbEntityID) cmpFieldValue,false);
			if (anotherSbbEntity == null)
				return null;
			else if (anotherSbbEntity.isRemoved())
				return null;
			else {
				return anotherSbbEntity.getSbbLocalObject();
			}
		}
	}
	
	// CHILD RELATION GETTER

	/**
	 * Retrieves the {@link ChildRelation} for the specified sbb entity and get
	 * child relation method name
	 */
	public static ChildRelation getChildRelation(SbbEntity sbbEntity,
			String childRelationMethodName) {

		if (sbbEntity.getSbbObject().getState() != SbbObjectState.READY)
			throw new IllegalStateException(
					"Could not invoke getChildRelation Method, Sbb Object is not in the READY state!");

		if (logger.isTraceEnabled()) {
			logger.trace("ChildRelation Interceptor:"
							+ childRelationMethodName);
		}

		return sbbEntity.getChildRelation(childRelationMethodName);
	}

	// EVENT FIRING

	/**
	 * The logic to fire an event from an SLEE 1.0 Sbb
	 * 
	 * @param sbbEntity
	 *            an sbb entity with an object assigned
	 * @param eventTypeID
	 *            the id of the event to fire
	 * @param eventObject
	 *            the event object, can't be null
	 * @param aci
	 *            the activity context where the event will be fired, can't be
	 *            null
	 * @param address
	 *            the optional address to fire the event
	 */
	public static void fireEvent(SbbEntity sbbEntity, EventTypeID eventTypeID,
			Object eventObject, ActivityContextInterface aci, Address address) {
		fireEvent(sbbEntity, eventTypeID, eventObject, aci, address, null);
	}

	/**
	 * The logic to fire an event from an SLEE 1.1 Sbb
	 * 
	 * @param sbbEntity
	 *            an sbb entity with an object assigned
	 * @param eventTypeID
	 *            the id of the event to fire
	 * @param eventObject
	 *            the event object, can't be null
	 * @param aci
	 *            the activity context where the event will be fired, can't be
	 *            null
	 * @param address
	 *            the optional address to fire the event
	 * @param serviceID
	 *            the optional service id to fire the event
	 */
	public static void fireEvent(SbbEntity sbbEntity, EventTypeID eventTypeID,
			Object eventObject, ActivityContextInterface aci, Address address,
			ServiceID serviceID) {

		if (sleeContainer.getCongestionControl().refuseFireEvent()) {
			throw new SLEEException("congestion control refused event");
		}

		// JAIN SLEE (TM) specs - Section 8.4.1
		// The SBB object must have an assigned SBB entity when it invokes this
		// method.
		// Otherwise, this method throws a java.lang.IllegalStateException.
		if (sbbEntity == null || sbbEntity.getSbbObject() == null
				|| sbbEntity.getSbbObject().getState() != SbbObjectState.READY)
			throw new IllegalStateException("SbbObject not assigned!");

		// JAIN SLEE (TM) specs - Section 8.4.1
		// The event ... cannot be null. If ... argument is null, the fire
		// event method throws a java.lang.NullPointerException.
		if (eventObject == null)
			throw new NullPointerException(
					"JAIN SLEE (TM) specs - Section 8.4.1: The event ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");

		// JAIN SLEE (TM) specs - Section 8.4.1
		// The activity ... cannot be null. If ... argument is null, the fire
		// event method throws a java.lang.NullPointerException.
		if (aci == null)
			throw new NullPointerException(
					"JAIN SLEE (TM) specs - Section 8.4.1: The activity ... cannot be null. If ... argument is null, the fire event method throws a java.lang.NullPointerException.");

		// JAIN SLEE (TM) specs - Section 8.4.1
		// It is a mandatory transactional method (see Section 9.6.1).
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		txManager.mandateTransaction();

		// rebuild the ac from the aci in the 2nd argument of the invoked
		// method, check it's state
		ActivityContext ac = ((org.mobicents.slee.container.activity.ActivityContextInterface) aci)
				.getActivityContext();
		if (logger.isTraceEnabled()) {
			logger.trace("invoke(): firing event on "
					+ ac);
		}
		
		// exception not in specs by mandated by
		// tests/activities/activitycontext/Test560Test.xml , it's preferable to
		// do double check on here than have the aci fire method throwing it and
		// the ra slee endpoint having to translate it to activity ending
		// exception, it is not common to have custom event firing in sbbs
		if (ac.isEnding()) {
			throw new IllegalStateException("activity context "
					+ ac.getActivityContextHandle() + " is ending");
		}
		
		final EventRoutingTransactionData transactionData = txManager.getTransactionContext().getEventRoutingTransactionData();
		if (transactionData != null) {
			final EventContext eventBeingDelivered = transactionData.getEventBeingDelivered();
			if (eventBeingDelivered != null && eventBeingDelivered.getEvent() == eventObject) {
				// there is an event being delivered by this tx and it matches the event being fired, lets copy the ref handler
				// fire the event
				ac.fireEvent(eventTypeID, eventObject, (Address) address, serviceID, eventBeingDelivered.getReferencesHandler());
				return;
			}
		}
		// seems it is not a refire
		ac.fireEvent(eventTypeID, eventObject, (Address) address, serviceID, null, null, null);	

	}

	// GET PROFILE CMP METHODS

	/**
	 * Retrieves a profile given the cmp method name and profile id
	 */
	public static Object getProfileCMPMethod(SbbEntity sbbEntity,
			String getProfileCMPMethodName, ProfileID profileID)
			throws UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException {

		GetProfileCMPMethodDescriptor mGetProfileCMPMethod = sbbEntity.getSbbComponent()
				.getDescriptor().getGetProfileCMPMethods().get(
						getProfileCMPMethodName);
		if (mGetProfileCMPMethod == null)
			throw new AbstractMethodError("Profile CMP Method not found");

		if (sbbEntity.getSbbObject().getState() != SbbObjectState.READY) {
			throw new IllegalStateException(
					"Could not invoke getProfileCMP Method, Sbb Object is not in the READY state!");
		}

		ProfileManagement sleeProfileManager = sleeContainer
				.getSleeProfileTableManager();

		ProfileTable profileTable = sleeProfileManager.getProfileTable(profileID.getProfileTableName());

		if (!profileTable.profileExists(profileID.getProfileName())) {
			throw new UnrecognizedProfileNameException(profileID.toString());
		}
	
		return profileTable.getProfile(profileID.getProfileName()).getProfileCmpSlee10Wrapper();		
	}

	// SBB USAGE PARAMS

	public static Object getSbbUsageParameterSet(SbbEntity sbbEntity, String name)
			throws UnrecognizedUsageParameterSetNameException {
		if (logger.isTraceEnabled()) {
			logger.trace("getSbbUsageParameterSet(): serviceId = "
					+ sbbEntity.getSbbEntityId().getServiceID() + " , sbbID = "
					+ sbbEntity.getSbbId() + " , name = " + name);
		}
		return getServiceUsageMBeanImpl(sbbEntity.getSbbEntityId().getServiceID())
				.getInstalledUsageParameterSet(sbbEntity.getSbbId(), name);
	}

	public static Object getDefaultSbbUsageParameterSet(SbbEntity sbbEntity) {
		if (logger.isTraceEnabled()) {
			logger.trace("getDefaultSbbUsageParameterSet(): "
					+ sbbEntity.getSbbEntityId().getServiceID() + " sbbID = "
					+ sbbEntity.getSbbId());
		}
		return getServiceUsageMBeanImpl(sbbEntity.getSbbEntityId().getServiceID())
				.getDefaultInstalledUsageParameterSet(sbbEntity.getSbbId());
	}

	private static ServiceUsageMBeanImpl getServiceUsageMBeanImpl(
			ServiceID serviceID) {
		return (ServiceUsageMBeanImpl) sleeContainer
				.getComponentRepository().getComponentByID(serviceID)
				.getServiceUsageMBean();
	}
	
}
