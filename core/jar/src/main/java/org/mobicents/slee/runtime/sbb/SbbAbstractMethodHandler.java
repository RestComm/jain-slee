package org.mobicents.slee.runtime.sbb;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.ChildRelation;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.ServiceID;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.resource.EventFlags;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcrete;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * The logic to implement sbb abstract methods.
 * 
 * @author martins
 * 
 */
public class SbbAbstractMethodHandler {

	private static final Logger logger = Logger
			.getLogger(SbbAbstractMethodHandler.class);

	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	// CMP ACCESSORs

	public static Object getCMPField(SbbEntity sbbEntity, String cmpFieldName,
			Class<?> returnType) {

		Object cmpFieldValue = sbbEntity.getCMPField(cmpFieldName);

		if (cmpFieldValue == null) {
			if (returnType.isPrimitive()) {
				if (returnType.equals(Integer.TYPE)) {
					return new Integer(0);
				} else if (returnType.equals(Boolean.TYPE)) {
					return new Boolean("false");
				} else if (returnType.equals(Long.TYPE)) {
					return new Long(0);
				} else if (returnType.equals(Double.TYPE)) {
					return new Double(0);
				} else if (returnType.equals(Float.TYPE)) {
					return new Float(0);
				}
			}
		}
		return cmpFieldValue;
	}

	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			Object cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}

	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			byte cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			short cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			int cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			long cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			float cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			double cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			boolean cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
	}
	
	public static void setCMPField(SbbEntity sbbEntity, String cmpFieldName,
			char cmpFieldValue) {
		sbbEntity.setCMPField(cmpFieldName, cmpFieldValue);
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

		if (logger.isDebugEnabled()) {
			logger
					.debug("ChildRelation Interceptor:"
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
		sleeContainer.getTransactionManager().mandateTransaction();

		// rebuild the ac from the aci in the 2nd argument of the invoked
		// method, check it's state
		ActivityContext ac = ((org.mobicents.slee.runtime.activity.ActivityContextInterface) aci)
				.getActivityContext();
		if (logger.isDebugEnabled()) {
			logger.debug("invoke(): firing event on "
					+ ac.getActivityContextId());
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
		
		// fire the event
		ac.fireEvent(eventTypeID, eventObject, (Address) address, serviceID,
				EventFlags.NO_FLAGS);

	}

	// GET PROFILE CMP METHODS

	/**
	 * Retrieves a profile given the cmp method name and profile id
	 */
	public static Object getProfileCMPMethod(SbbEntity sbbEntity,
			String getProfileCMPMethodName, ProfileID profileID)
			throws UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException {

		MGetProfileCMPMethod mGetProfileCMPMethod = sbbEntity.getSbbComponent()
				.getDescriptor().getGetProfileCMPMethods().get(
						getProfileCMPMethodName);
		if (mGetProfileCMPMethod == null)
			throw new AbstractMethodError("Profile CMP Method not found");

		if (sbbEntity.getSbbObject().getState() != SbbObjectState.READY) {
			throw new IllegalStateException(
					"Could not invoke getProfileCMP Method, Sbb Object is not in the READY state!");
		}

		SleeProfileTableManager sleeProfileManager = sleeContainer
				.getSleeProfileTableManager();

		try {
			
			ProfileTableConcrete profileTable = sleeProfileManager.getProfileTable(profileID.getProfileName());
			
			if (!profileTable.profileExists(profileID.getProfileName())) {
				throw new UnrecognizedProfileNameException(profileID.toString());
			}
			
			final ProfileObject po = profileTable.assignAndActivateProfileObject(profileID.getProfileName());
			po.profileLoad();
			
			TransactionalAction action = new TransactionalAction() {
				public void execute() {
					try {
						po.getProfileTableConcrete().deassignProfileObject(po, false);						
					} catch (Exception e) {
						logger.error("Failed to deallocate ProfileObject");
					}
				}
			};
			
			sleeContainer.getTransactionManager().addBeforeCommitAction(action);
			sleeContainer.getTransactionManager().addAfterRollbackAction(action);
			return po.getProfileConcrete();
		} catch (SystemException e) {
			throw new SLEEException("low-level failure", e);
		}

	}

	// SBB USAGE PARAMS

	public static Object getSbbUsageParameterSet(SbbEntity sbbEntity, String name)
			throws UnrecognizedUsageParameterSetNameException {
		if (logger.isDebugEnabled()) {
			logger.debug("getSbbUsageParameterSet(): serviceId = "
					+ sbbEntity.getServiceId() + " , sbbID = "
					+ sbbEntity.getSbbId() + " , name = " + name);
		}
		return getServiceUsageMBeanImpl(sbbEntity.getServiceId())
				.getInstalledUsageParameterSet(sbbEntity.getSbbId(), name);
	}

	public static Object getDefaultSbbUsageParameterSet(SbbEntity sbbEntity) {
		if (logger.isDebugEnabled()) {
			logger.debug("getDefaultSbbUsageParameterSet(): "
					+ sbbEntity.getServiceId() + " sbbID = "
					+ sbbEntity.getSbbId());
		}
		return getServiceUsageMBeanImpl(sbbEntity.getServiceId())
				.getDefaultInstalledUsageParameterSet(sbbEntity.getSbbId());
	}

	private static ServiceUsageMBeanImpl getServiceUsageMBeanImpl(
			ServiceID serviceID) {
		return (ServiceUsageMBeanImpl) sleeContainer
				.getComponentRepositoryImpl().getComponentByID(serviceID)
				.getServiceUsageMBean();
	}
	
}
