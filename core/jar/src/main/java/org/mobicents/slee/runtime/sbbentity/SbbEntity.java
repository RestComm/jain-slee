package org.mobicents.slee.runtime.sbbentity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventContext;
import javax.slee.EventTypeID;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.SbbComponent.EventHandlerMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.profile.ProfileLocalObjectImpl;
import org.mobicents.slee.container.profile.ProfileTableImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.cache.SbbEntityCacheData;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.EventContextID;
import org.mobicents.slee.runtime.eventrouter.EventContextImpl;
import org.mobicents.slee.runtime.eventrouter.EventRouterActivity;
import org.mobicents.slee.runtime.eventrouter.EventRouterThreadLocals;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionData;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectPool;
import org.mobicents.slee.runtime.sbb.SbbObjectState;

/**
 * 
 * This class is the Sbb Entity as defined in the Spec The SbbEntity class holds
 * the persistent state of the SbbEntity All the fields that are not transient
 * in this class they will be cached
 * 
 * @author F. Moggia
 * @author M. Ranganathan
 * @author Tim
 * @author Ralf Siedow - bug fixes
 * @author eduardomartins
 * 
 */
public class SbbEntity {

	static private final Logger log = Logger.getLogger(SbbEntity.class);
	static private final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	private final String sbbeId; // This is the primary key of the SbbEntity.

	private final SbbComponent sbbComponent;
	private SbbObject sbbObject;
	private final SbbObjectPool pool;

	// cache data
	protected SbbEntityCacheData cacheData;

	private boolean isRemoved;

	/**
	 * Call this constructor when there's no cached image and the Sbb entity is
	 * being created for the first time.
	 * 
	 * @param container
	 * @param sbbeId
	 * @param sbbID
	 * @param convergenceName
	 * @param svcId
	 */
	SbbEntity(String sbbEntityId, String parentSbbEntityId,
			String parentChildRelationName, String rootSbbEntityId,
			SbbID sbbID, String convergenceName, ServiceID svcId)
			throws Exception {

		if (sbbID == null)
			throw new NullPointerException("Null sbbID");

		this.sbbeId = sbbEntityId;
		cacheData = sleeContainer.getCache().getSbbEntityCacheData(sbbEntityId);
		cacheData.create();

		setParentSbbEntityId(parentSbbEntityId);
		setParentChildRelation(parentChildRelationName);
		setRootSbbId(rootSbbEntityId);
		setSbbId(sbbID);
		setServiceId(svcId);
		setServiceConvergenceName(convergenceName);

		this.pool = sleeContainer.getSbbPoolManagement().getObjectPool(
				getServiceId(), getSbbId());
		this.sbbComponent = sleeContainer.getComponentRepositoryImpl()
				.getComponentByID(getSbbId());
		if (this.sbbComponent == null) {
			String s = "Sbb component/descriptor not found for sbbID["
					+ getSbbId() + "],\n" + "  sbbEntityID[" + sbbeId + "],\n"
					+ "  Transaction[ID:"
					+ sleeContainer.getTransactionManager().getTransaction()
					+ "]";
			log.warn(s);
			throw new RuntimeException(s);
		}

	}

	/**
	 * Constructors an already existing sbb entity from the treecache given it's
	 * id. Note that we do not add a transactional action for this constructor.
	 * 
	 * @param sbbeId
	 * @throws RuntimeException
	 *             if recreation from cache is not possible, i.e., does not
	 *             exists
	 */
	SbbEntity(String sbbEntityId) {

		if (sbbEntityId == null)
			throw new NullPointerException(
					"SbbEntity cannot be instantiated for sbbeId == null");

		this.sbbeId = sbbEntityId;

		cacheData = sleeContainer.getCache().getSbbEntityCacheData(sbbEntityId);
		if (cacheData.exists()) {

			this.pool = sleeContainer.getSbbPoolManagement().getObjectPool(
					getServiceId(), getSbbId());
			this.sbbComponent = sleeContainer.getComponentRepositoryImpl()
					.getComponentByID(getSbbId());
			if (this.sbbComponent == null) {
				String s = "Sbb component/descriptor not found for sbbID["
						+ getSbbId() + "],\n" + "  sbbEntityID[" + sbbeId + "]";
				log.warn(s);
				throw new RuntimeException(s);
			}
		} else {
			throw new IllegalStateException("Sbb entity " + sbbEntityId
					+ " not found");
		}
	}

	public ServiceID getServiceId() {
		return cacheData.getServiceId();
	}

	private void setServiceId(ServiceID svcId) {
		cacheData.setServiceId(svcId);
	}

	private void setServiceConvergenceName(String convergenceName) {
		cacheData.setServiceConvergenceName(convergenceName);
	}

	public String getServiceConvergenceName() {
		return cacheData.getServiceConvergenceName();
	}

	/**
	 * Debugging printf of cached state of a node.
	 * 
	 */
	public void printNode() {
		if (log.isDebugEnabled()) {
			log.debug("\n SbbEntity.printNode() { " + "\nsbbEntityID  = "
					+ this.sbbeId + "\nsbbID  = " + getSbbId()
					+ "\nattachmentCount = " + getAttachmentCount()
					+ "\nrootSbbId = " + this.getRootSbbId() + "\nserviceID = "
					+ getServiceId() + "\nactivityContexts = "
					+ this.getActivityContexts() + "\nconvergenceName = "
					+ getServiceConvergenceName() + "\n}");
		}
	}

	/**
	 * The generated code to access CMP Fields needs to call this method.
	 * 
	 * @param cmpField
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SystemException
	 */
	public Object getCMPField(String cmpFieldName)
			throws TransactionRequiredLocalException {

		if (log.isDebugEnabled()) {
			log.debug("getCMPField() " + cmpFieldName);
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		CmpWrapper cmpWrapper = (CmpWrapper) cacheData
				.getCmpField(cmpFieldName);
		if (cmpWrapper != null) {
			switch (cmpWrapper.getType()) {

			case sbblo:
				// it's a sbbLocalObject cmp
				String sbbEntityId = (String) cmpWrapper.getValue();
				SbbEntity sbbEntity = null;
				try {
					sbbEntity = SbbEntityFactory.getSbbEntity(sbbEntityId);
				} catch (Exception ex) {
					// Maybe the sbb entity has been removed already.
				}
				if (sbbEntity == null)
					return null;
				else if (sbbEntity.isRemoved())
					return null;
				else {
					return sbbEntity.createSbbLocalObject();
				}

			case aci:
				final ActivityContext ac = sleeContainer
						.getActivityContextFactory().getActivityContext(
								(ActivityContextHandle) cmpWrapper.getValue());
				if (ac != null) {
					return new ActivityContextInterfaceImpl(ac);
				} else {
					return null;
				}

			case eventctx:
				final EventContextID eventContextID = (EventContextID) cmpWrapper
						.getValue();
				final EventRouterActivity eventRouterActivity = sleeContainer
						.getEventRouter().getEventRouterActivity(
								eventContextID.getActivityContextHandle());
				if (eventRouterActivity != null) {
					EventContextImpl eventContextImpl = eventRouterActivity
							.getCurrentEventContext();
					if (eventContextImpl != null) {
						if (eventContextID.getEventObject().equals(
								eventContextImpl.getEventContextID()
										.getEventObject())) {
							return eventContextImpl;
						} else {
							return null;
						}
					} else {
						return null;
					}
				} else {
					return null;
				}

			case profilelo:
				ProfileLocalObjectCmpValue profileLocalObjectCmpValue = (ProfileLocalObjectCmpValue) cmpWrapper
						.getValue();
				try {
					ProfileTableImpl profileTable = this.sleeContainer
							.getSleeProfileTableManager().getProfileTable(
									profileLocalObjectCmpValue
											.getProfileTableName());
					ProfileLocalObjectImpl ploc = (ProfileLocalObjectImpl) profileTable
							.find(profileLocalObjectCmpValue.getProfileName());
					return ploc;
				} catch (UnrecognizedProfileTableNameException e) {
					if (log.isDebugEnabled()) {
						log.debug("Profile table does not exist anymore: "
								+ profileLocalObjectCmpValue
										.getProfileTableName(), e);
					}
					return null;
				} 

			case normal:
				if (log.isDebugEnabled()) {
					log.debug("getCMPField() value = " + cmpWrapper.getValue());
				}
				return cmpWrapper.getValue();

			default:
				throw new SLEEException(
						"invalid cmp type retrieved from cache "
								+ cmpWrapper.getType());

			}
		} else {
			return null;
		}
	}

	public void setCMPField(String cmpFieldName, Object object)
			throws TransactionRequiredLocalException {

		if (log.isDebugEnabled()) {
			log.debug("putCMPField(): putting cmp field : " + cmpFieldName
					+ "/" + " object = " + object);
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		CmpType cmpType = null;
		Object cmpValue = null;

		// TODO optimize by adding the cmp type to the generated setter method?
		if (object instanceof SbbLocalObject) {
			SbbLocalObjectImpl sbbLocalObjectImpl = null;
			try {
				sbbLocalObjectImpl = (SbbLocalObjectImpl) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown SbbLocalObject implementation");
			}
			MSbbCMPField field = sbbComponent.getDescriptor().getCmpFields()
					.get(cmpFieldName);
			if (field.getSbbRef() != null
					&& !field.getSbbRef().equals(
							sbbLocalObjectImpl.getSbbEntity().getSbbComponent()
									.getSbbID())) {
				throw new IllegalArgumentException("CMP value being set ("
						+ sbbLocalObjectImpl.getSbbEntity().getSbbComponent()
								.getSbbID()
						+ ") is for a different sbb then the one expected ("
						+ field.getSbbRef() + ")");
			}
			cmpType = CmpType.sbblo;
			cmpValue = sbbLocalObjectImpl.getSbbEntityId();
		} else if (object instanceof ActivityContextInterface) {
			org.mobicents.slee.runtime.activity.ActivityContextInterface activityContextInterfaceImpl = null;
			try {
				activityContextInterfaceImpl = (org.mobicents.slee.runtime.activity.ActivityContextInterface) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown ActivityContextInterface implementation");
			}
			cmpType = CmpType.aci;
			cmpValue = activityContextInterfaceImpl.getActivityContext().getActivityContextHandle();
		} else if (object instanceof EventContext) {
			EventContextImpl eventContextImpl = null;
			try {
				eventContextImpl = (EventContextImpl) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown EventContext implementation");
			}
			cmpType = CmpType.eventctx;
			cmpValue = eventContextImpl.getEventContextID();
		} else if (object instanceof ProfileLocalObject) {
			ProfileLocalObjectImpl profileLocalObjectConcreteImpl = null;
			try {
				profileLocalObjectConcreteImpl = (ProfileLocalObjectImpl) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown ProfileLocalObject implementation");
			}
			cmpType = CmpType.profilelo;
			cmpValue = new ProfileLocalObjectCmpValue(profileLocalObjectConcreteImpl
					.getProfileTableName(), profileLocalObjectConcreteImpl.getProfileName());
		} else {
			cmpType = CmpType.normal;
			cmpValue = object;
		}
		CmpWrapper cmpWrapper = new CmpWrapper(cmpFieldName, cmpType, cmpValue);
		cacheData.setCmpField(cmpFieldName, cmpWrapper);
	}

	public void afterACAttach(ActivityContextHandle ach) {

		// add event mask entry
		Collection<MEventEntry> mEventEntries = sbbComponent.getDescriptor()
				.getEventEntries().values();
		HashSet<EventTypeID> maskedEvents = null;
		if (mEventEntries != null) {
			maskedEvents = new HashSet<EventTypeID>();
			for (MEventEntry mEventEntry : mEventEntries) {
				if (mEventEntry.isMaskOnAttach()) {
					maskedEvents.add(mEventEntry.getEventReference()
							.getComponentID());
				}
			}
		}
		// add to cache
		cacheData.attachActivityContext(ach);
		cacheData.updateEventMask(ach, maskedEvents);

		if (log.isDebugEnabled()) {
			log.debug("attached sbb entity " + sbbeId + " to ac " + ach
					+ " , events added to current mask " + maskedEvents);
		}
	}

	public void afterACDetach(ActivityContextHandle ach) {

		// remove from cache
		cacheData.detachActivityContext(ach);

		if (log.isDebugEnabled()) {
			log.debug("detached sbb entity " + sbbeId + " to ac " + ach);
		}
	}

	public Set getMaskedEventTypes(ActivityContextHandle ach) {

		Set eventMaskSet = cacheData.getMaskedEventTypes(ach);

		if (log.isDebugEnabled()) {
			log.debug("event mask for sbb entity " + sbbeId + " and ac " + ach
					+ " --> " + eventMaskSet);
		}

		if (eventMaskSet == null) {
			return Collections.EMPTY_SET;
		} else {
			return eventMaskSet;
		}
	}

	public void setEventMask(ActivityContextHandle ach, String[] eventMask)
			throws UnrecognizedEventException {

		HashSet<EventTypeID> maskedEvents = new HashSet<EventTypeID>();

		if (eventMask != null && eventMask.length != 0) {
			
			EventTypeID eventTypeID = null;
			MEventEntry sbbEventEntry = null;
			for (int i = 0; i < eventMask.length; i++) {
				
				eventTypeID = sbbComponent.getDescriptor().getEventTypeID(eventMask[i]);
				if (eventTypeID == null)
					throw new UnrecognizedEventException(
							"Event is not known by this SBB.");
				
				sbbEventEntry = sbbComponent.getDescriptor()
						.getEventEntries().get(eventTypeID);
				if (sbbEventEntry.isReceived()) {
					maskedEvents.add(eventTypeID);
				} else {
					throw new UnrecognizedEventException("Event "
							+ eventMask[i]
							+ " has no receive direction for this SBB.");
				}
			}
		}

		cacheData.setEventMask(ach, maskedEvents);

		if (log.isDebugEnabled()) {
			log.debug("set event mask " + maskedEvents + " for sbb entity "
					+ sbbeId + " and ac " + ach);
		}
	}

	public Set getActivityContexts() {
		Set result = cacheData.getActivityContexts();
		return result == null ? Collections.EMPTY_SET : result;
	}

	private static final String[] emptyStringArray = {};

	public String[] getEventMask(ActivityContextHandle ach) {

		Set maskedEvents = (Set) cacheData.getMaskedEventTypes(ach);

		if (log.isDebugEnabled()) {
			log.debug("set event mask " + maskedEvents + " for sbb entity "
					+ sbbeId + " and ac " + ach);
		}

		if (maskedEvents == null || maskedEvents.isEmpty()) {
			return emptyStringArray;
		} else {
			String[] events = new String[maskedEvents.size()];
			Iterator evMaskIt = maskedEvents.iterator();
			for (int i = 0; evMaskIt.hasNext(); i++) {
				EventTypeID eventTypeId = (EventTypeID) evMaskIt.next();
				events[i] = sbbComponent.getDescriptor().getEventEntries().get(
						eventTypeId).getEventName();
			}
			return events;
		}
	}

	public String getRootSbbId() {
		return cacheData.getRootSbbId();
	}

	public boolean isRootSbbEntity() {
		return getParentSbbEntityId() == null;
	}

	private void setRootSbbId(String rootSbbEntityId) {
		cacheData.setRootSbbId(rootSbbEntityId);
	}

	public int getAttachmentCount() {
		int attachmentCount = getActivityContexts().size();
		// needs to add all children attachement counts too
		for (MGetChildRelationMethod getChildRelationMethod : this.sbbComponent
				.getDescriptor().getGetChildRelationMethods().values()) {
			// (re)create child relation obj
			ChildRelationImpl childRelationImpl = new ChildRelationImpl(
					getChildRelationMethod, this);
			// iterate all sbb entities in this child relation
			for (Iterator i = childRelationImpl.getSbbEntitySet().iterator(); i
					.hasNext();) {
				String childSbbEntityID = (String) i.next();
				// recreated the sbb entity
				SbbEntity childSbbEntity = SbbEntityFactory
						.getSbbEntityWithoutLock(childSbbEntityID);
				attachmentCount += childSbbEntity.getAttachmentCount();
			}
		}
		return attachmentCount;
	}

	public byte getPriority() {
		return cacheData.getPriority().byteValue();
	}

	public void setPriority(byte priority) {
		cacheData.setPriority(Byte.valueOf(priority));
		if (log.isDebugEnabled()) {
			log.debug("set sbb entity " + sbbeId + " priority to " + priority);
		}
	}

	/**
	 * Remove the SbbEntity (Spec. 5.5.4) It detaches the SBB entity from all
	 * Activity Contexts. It invokes the appropriate life cycle methods (see
	 * Section 6.3) of an SBB object that caches the SBB entity’s state. It
	 * removes the SBB entity from the ChildRelation object that the SBB entity
	 * belongs to. It removes the persistent representation of the SBB entity.
	 * 
	 * @param removeFromParent
	 *            indicates if the entity should be removed from it's parent or
	 *            not
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	protected void remove(boolean removeFromParent)
			throws TransactionRequiredException, SystemException {
		if (log.isDebugEnabled()) {
			log.debug("SbbEntity.remove(): Removing entity with id:"
					+ this.getSbbEntityId());
		}

		if (removeFromParent) {
			removeFromParent();
		}
		removeEntityTree();

		if (log.isDebugEnabled()) {
			log.debug("REMOVED SBB ENTITY " + this.sbbeId);
		}
	}

	/**
	 * Removes the entity tree from this entity, that is, all sbb entities on
	 * it's child relations.
	 * 
	 * @throws TransactionRequiredException
	 * @throws SystemException
	 */
	private void removeEntityTree() throws TransactionRequiredException,
			SystemException {

		if (log.isDebugEnabled()) {
			log.debug("removing entity tree for sbbeid " + this.sbbeId);
		}

		// removes the SBB entity from all Activity Contexts.
		for (Iterator i = this.getActivityContexts().iterator(); i.hasNext();) {
			ActivityContextHandle ach = (ActivityContextHandle) i.next();
			// get ac
			ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
			// remove the sbb entity from the attachment set.
			if (ac != null && !ac.isEnding()) {
				ac.detachSbbEntity(this.sbbeId);
			}
			// no need to remove ac from entity because the entity is being
			// removed
		}

		// It invokes the appropriate life cycle methods (see Section 6.3) of an
		// SBB object that caches the SBB entity state.
		boolean invokingServiceSet = EventRouterThreadLocals
				.getInvokingService() != null;
		if (!invokingServiceSet) {
			EventRouterThreadLocals.setInvokingService(getServiceId());
		}
		try {
			if (this.sbbObject == null) {
				this.assignAndActivateSbbObject();
			}
			sbbObject.sbbStore();
			removeAndReleaseSbbObject();
		} catch (Exception e) {
			try {
				sleeContainer.getTransactionManager().setRollbackOnly();
				this.trashObject();
			} catch (Exception e2) {
				throw new RuntimeException("Transaction Failure.", e2);
			}
		} finally {
			if (!invokingServiceSet) {
				EventRouterThreadLocals.setInvokingService(null);
			}
		}

		// gather all entities in child relations from cache
		Set childSbbEntities = cacheData.getAllChildSbbEntities();

		// remove this entity data from cache
		removeFromCache();

		// now remove children
		for (Object childSbbEntityId : childSbbEntities) {
			// recreated the sbb entity and remove it
			SbbEntityFactory.removeSbbEntity((String) childSbbEntityId, false);
		}
	}

	public void trashObject() {
		try {
			// FIXME shouldn't just return the object to the pool?
			this.pool.returnObject(sbbObject);
			this.sbbObject = null;
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception ", e);
		}
	}

	public void sbbRolledBack(RolledBackContext context) {
		if (sbbObject != null) {
			sbbObject.sbbRolledBack(context);
			sbbObject.sbbStore();
			sbbObject.sbbPassivate();
		} else {
			if (log.isInfoEnabled())
				log.info("Unexpected case. Check it!"); // CHECKME
		}
	}

	public String getSbbEntityId() {
		return this.sbbeId;
	}

	public SbbID getSbbId() {
		return cacheData.getSbbId();
	}

	private void setSbbId(SbbID sbbId) {
		cacheData.setSbbId(sbbId);
	}

	/**
	 * Actually invoke the event handler.
	 * 
	 */
	public void invokeEventHandler(DeferredEvent sleeEvent, ActivityContext ac,
			EventContextImpl eventContextImpl) throws Exception {

		
		// get event handler method
		final EventHandlerMethod eventHandlerMethod = sbbComponent
				.getEventHandlerMethods().get(sleeEvent.getEventTypeId());
		// build aci
		ActivityContextInterfaceImpl aciImpl = new ActivityContextInterfaceImpl(
				ac);
		ActivityContextInterface activityContextInterface = null;
		if (eventHandlerMethod.getHasCustomACIParam()) {
			try {
				activityContextInterface = (ActivityContextInterface) this
						.getSbbComponent()
						.getActivityContextInterfaceConcreteClass()
						.getConstructor(
								new Class[] { aciImpl.getClass(),
										SbbComponent.class }).newInstance(
								new Object[] { aciImpl, sbbComponent });
			} catch (Exception e) {
				String s = "Could not create Custom ACI!";
				// log.error(s, e);
				throw new SLEEException(s, e);
			}
		} else {
			activityContextInterface = aciImpl;
		}
		// now build the param array
		final Object[] parameters ;
		if (eventHandlerMethod.getHasEventContextParam()) {
			parameters = new Object[] { sleeEvent.getEvent(),
					activityContextInterface, eventContextImpl };
		} else {
			parameters = new Object[] { sleeEvent.getEvent(),
					activityContextInterface };
		}

		// store some info about the invocation in the tx context
		EventRoutingTransactionData data = new EventRoutingTransactionData(
				sleeEvent, activityContextInterface);
		data.getInvokedSbbEntities().add(sbbeId);
		data.putInTransactionContext();
		// invoke method
		try {

			//This is required. Since domain chain may indicate RA for instance, or SLEE deployer. If we dont do that test: tests/runtime/security/Test1112012Test.xml and second one, w
			//will fail because domain of SLEE tck ra is too restrictive (or we have bad desgin taht allows this to happen?)
			if(System.getSecurityManager()!=null)
			{
				AccessController.doPrivileged(new PrivilegedExceptionAction(){

				public Object run() throws IllegalAccessException, InvocationTargetException{
					eventHandlerMethod.getEventHandlerMethod().invoke(
							sbbObject.getSbbConcrete(), parameters);
					return null;
				}});
			
			}else
			{
				eventHandlerMethod.getEventHandlerMethod().invoke(
						sbbObject.getSbbConcrete(), parameters);
			}
	
		} catch(PrivilegedActionException pae)
		{
			Throwable cause = pae.getException();
			if(cause instanceof  IllegalAccessException)
			 {
				throw new RuntimeException(cause);
			} else if(cause instanceof InvocationTargetException ) {
				// Remember the actual exception is hidden inside the
				// InvocationTarget exception when you use reflection!
				Throwable realException = cause.getCause();
				if (realException instanceof RuntimeException) {
					RuntimeException re = (RuntimeException) realException;
					throw re;
				} else if (realException instanceof Error) {
					Error re = (Error) realException;
					throw re;
				} else if (realException instanceof Exception) {
					Exception re = (Exception) realException;
					throw re;
				}
			}else
			{
				pae.printStackTrace();
			}
		}catch(IllegalAccessException iae)
		{
			throw new RuntimeException(iae);
		}catch(InvocationTargetException ite)
		{
			Throwable realException = ite.getCause();
			if (realException instanceof RuntimeException) {
				RuntimeException re = (RuntimeException) realException;
				throw re;
			} else if (realException instanceof Error) {
				Error re = (Error) realException;
				throw re;
			} else if (realException instanceof Exception) {
				Exception re = (Exception) realException;
				throw re;
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		// remove data from tx context
		data.removeFromTransactionContext();
		
	}

	/**
	 * Assigns the sbb entity to a sbb object, and then invoke sbbActivate()
	 * 
	 * @throws Exception
	 */
	public void assignAndActivateSbbObject() throws Exception {
		try {
			// get one object from the pool
			this.sbbObject = (SbbObject) this.pool.borrowObject();
			// invoke the appropriate sbb life-cycle methods
			this.sbbObject.sbbActivate();
			this.sbbObject.setSbbEntity(this);
			this.sbbObject.setState(SbbObjectState.READY);
		} catch (Exception e) {
			log.error("Failed to assign and activate sbb object", e);
			throw e;
		}
	}

	/**
	 * Assigns the sbb entity to a sbb object, and then invoke sbbCreate() and
	 * sbbPostCreate()
	 * 
	 * @throws Exception
	 */
	public void assignAndCreateSbbObject() throws Exception {
		try {
			// get one object from the pool
			this.sbbObject = (SbbObject) this.pool.borrowObject();
			// invoke the appropriate sbb life-cycle methods
			this.sbbObject.setSbbEntity(this);
			this.sbbObject.sbbCreate();
			this.sbbObject.setState(SbbObjectState.READY);
			this.sbbObject.sbbPostCreate();
		} catch (Exception e) {
			log.error("Failed to assign and create sbb object", e);
			removeFromCache();
			throw e;
		}
	}

	/**
	 * Invoke sbbPassivate() and then release the sbb object from the entity
	 * 
	 * @throws Exception
	 */
	public void passivateAndReleaseSbbObject() throws Exception {
		this.sbbObject.sbbPassivate();
		this.sbbObject.setState(SbbObjectState.POOLED);
		this.sbbObject.setSbbEntity(null);
		this.pool.returnObject(this.sbbObject);
		this.sbbObject = null;
		for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
				.hasNext();) {
			SbbEntity childSbbEntity = i.next();
			if (childSbbEntity.getSbbObject() != null) {
				childSbbEntity.passivateAndReleaseSbbObject();
			}
			i.remove();
		}
	}

	/**
	 * Invoke sbbRemove() and then release the sbb object from the entity
	 * 
	 * @throws Exception
	 */
	public void removeAndReleaseSbbObject() throws Exception {
		this.sbbObject.sbbRemove();
		this.sbbObject.setState(SbbObjectState.POOLED);
		this.sbbObject.setSbbEntity(null);
		this.pool.returnObject(this.sbbObject);
		this.sbbObject = null;
		for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
				.hasNext();) {
			SbbEntity childSbbEntity = i.next();
			if (childSbbEntity.getSbbObject() != null) {
				childSbbEntity.removeAndReleaseSbbObject();
			}
			i.remove();
		}
	}

	public SbbObjectPool getObjectPool() {
		return this.pool;
	}

	public SbbObject getSbbObject() {
		return this.sbbObject;
	}

	public boolean isAttached(ActivityContextHandle ach) {
		return this.getActivityContexts().contains(ach);
	}

	public SbbComponent getSbbComponent() {
		return this.sbbComponent;
	}

	/**
	 * Retrieves the child relation impl with the specified name, if it's a
	 * valid name for this sbb component.
	 * 
	 * @param accessorName
	 * @return null if it's an invalid name for this sbb component.
	 */
	public ChildRelationImpl getChildRelation(String accessorName) {

		MGetChildRelationMethod getChildRelationMethod = null;
		// get the child relation metod from the sbb component
		if ((getChildRelationMethod = this.sbbComponent.getDescriptor()
				.getGetChildRelationMethods().get(accessorName)) != null) {
			// this is a valid name of a child relation for this entity
			return new ChildRelationImpl(getChildRelationMethod, this);
		} else {
			// invalid child relation name
			log
					.warn("Sbb entity "
							+ this.getSbbEntityId()
							+ " can't get the child relation named "
							+ accessorName
							+ ". Does not exist such a relation in sbb component with id "
							+ this.getSbbId());
			return null;
		}
	}

	public void asSbbActivityContextInterface(ActivityContextInterface aci) {
		try {
			ActivityContextInterfaceImpl aciImpl = (ActivityContextInterfaceImpl) aci;
			Class aciclass = this.getSbbComponent()
					.getActivityContextInterfaceConcreteClass();
			if (aciclass != null) {

				Class[] argTypes = new Class[] { aciImpl.getClass(),
						SbbComponent.class };
				Constructor cons = aciclass.getConstructor(argTypes);
				Object retval = cons.newInstance(new Object[] { aciImpl,
						this.getSbbComponent() });
				SbbConcrete sbbConcrete = (SbbConcrete) this.getSbbObject()
						.getSbbConcrete();
				sbbConcrete.sbbSetActivityContextInterface(retval);
			}
		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((SbbEntity) obj).sbbeId.equals(this.sbbeId);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return sbbeId.hashCode();
	}

	public void checkReEntrant() throws SLEEException {
		if ((!this.getSbbComponent().getDescriptor().getSbbAbstractClass()
				.isReentrant())
				&& EventRoutingTransactionData.getFromTransactionContext()
						.getInvokedSbbEntities().contains(sbbeId))
			throw new SLEEException(" re-entrancy not allowed ");
	}

	public SbbLocalObjectImpl createSbbLocalObject() {
		Class sbbLocalClass;
		if (log.isDebugEnabled())
			log.debug("createSbbLocalObject " + this.getSbbComponent());

		// The concrete class generated in ConcreteLocalObjectGenerator
		if ((sbbLocalClass = sbbComponent.getSbbLocalInterfaceConcreteClass()) != null) {
			if (log.isDebugEnabled())
				log.debug("creatingCustom local class "
						+ sbbLocalClass.getName());
			Object[] objs = { this };
			Class[] types = { SbbEntity.class };
			try {
				return (SbbLocalObjectImpl) sbbLocalClass.getConstructor(types)
						.newInstance(objs);
			} catch (Exception e) {
				throw new RuntimeException(
						"Failed to create Sbb Local Interface.", e);
			}
		} else {
			return new SbbLocalObjectImpl(this);
		}
	}

	/**
	 * 
	 * Returns true if the SbbEntity is in the process of being removed
	 * 
	 * @return Returns the isRemoved.
	 */
	public boolean isRemoved() {
		return isRemoved;
	}

	/**
	 * Remove entity from cache.
	 */
	private void removeFromCache() {

		if (log.isDebugEnabled()) {
			log.debug("removing sbb entity " + sbbeId + " from cache");
		}

		cacheData.remove();
		isRemoved = true;
	}

	/**
	 * Retreives the name of the child relation of the parent this sbb entity
	 * belongs.
	 * 
	 * @return
	 */
	public String getParentChildRelation() {
		return cacheData.getParentChildRelation();
	}

	/**
	 * Sets the parent child relation name.
	 * 
	 * @param name
	 */
	private void setParentChildRelation(String parentChildRelation) {
		cacheData.setParentChildRelation(parentChildRelation);
	}

	/**
	 * Retreives the id of the parent sbb entity.
	 * 
	 * @return
	 */
	public String getParentSbbEntityId() {
		return cacheData.getParentSbbEntityId();
	}

	/**
	 * Sets the parent sbb entity id.
	 * 
	 * @param name
	 */
	private void setParentSbbEntityId(String parentSbbEntityId) {
		cacheData.setParentSbbEntityId(parentSbbEntityId);
	}

	// It removes the SBB entity from the ChildRelation object that the SBB
	// entity belongs
	// to.
	private void removeFromParent() throws TransactionRequiredException,
			SystemException {

		if (log.isDebugEnabled()) {
			log.debug("Removing sbb entity " + this.getSbbEntityId()
					+ " from parent " + this.getParentSbbEntityId());
		}

		if (this.getParentSbbEntityId() != null) {
			SbbEntityFactory.getSbbEntity(this.getParentSbbEntityId())
					.getChildRelation(getParentChildRelation()).removeChild(
							this.getSbbEntityId());
		} else {
			// it's a root sbb entity, remove from service
			try {
				Service service = sleeContainer.getServiceManagement()
						.getService(this.getServiceId());
				service.removeConvergenceName(this.getServiceConvergenceName());
			} catch (Exception e) {
				log.info("Failed to remove the root sbb entity " + this.sbbeId
						+ " with convergence name "
						+ this.getServiceConvergenceName()
						+ " from the service " + this.getServiceId(), e);
			}
		}
	}

	private HashSet<SbbEntity> childsWithSbbObjects = new HashSet<SbbEntity>();

	protected void addChildWithSbbObject(SbbEntity childSbbEntity) {
		childsWithSbbObjects.add(childSbbEntity);
	}

	@Override
	public String toString() {
		return "SbbEntity:"+sbbeId;
	}
}
