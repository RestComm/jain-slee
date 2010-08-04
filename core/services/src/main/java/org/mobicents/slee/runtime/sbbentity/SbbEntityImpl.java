package org.mobicents.slee.runtime.sbbentity;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent.EventHandlerMethod;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.event.EventContextHandle;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.profile.ProfileLocalObject;
import org.mobicents.slee.container.profile.ProfileTable;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionDataImpl;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;

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
public class SbbEntityImpl implements SbbEntity {

	static private final Logger log = Logger.getLogger(SbbEntityImpl.class);
	
	private final String sbbeId; // This is the primary key of the SbbEntity.

	private final SbbComponent sbbComponent;
	private SbbObject sbbObject;
	private final SbbObjectPool pool;

	// cache data
	protected SbbEntityCacheData cacheData;

	private boolean removed;
	private final boolean created;
	
	// this is in cache data but we need to have it here also, to rebuild an sbb entity that was never really created due to tx rollback
	private transient SbbEntityImmutableData sbbEntityImmutableData;
	
	private final SbbEntityFactoryImpl sbbEntityFactory;
	private final SleeContainer sleeContainer;
	
	private boolean doTraceLogs = log.isTraceEnabled();
	
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
	SbbEntityImpl(String sbbEntityId, SbbEntityImmutableData sbbEntityImmutableData, SbbEntityCacheData cacheData, SbbEntityFactoryImpl sbbEntityFactory) {
		this.sbbEntityFactory = sbbEntityFactory;
		this.sleeContainer = sbbEntityFactory.getSleeContainer();
		this.sbbeId = sbbEntityId;
		this.cacheData = cacheData;
		this.sbbEntityImmutableData = sbbEntityImmutableData;
		
		this.pool = sleeContainer.getSbbManagement().getObjectPool(
				sbbEntityImmutableData.getServiceID(), sbbEntityImmutableData.getSbbID());
		this.sbbComponent = pool.getSbbComponent();
		this.created = true;		
	}

	/**
	 * Constructors an already existing sbb entity from the cache given it's
	 * id.
	 * @param sbbEntityId
	 * @param cacheData
	 * @param sbbEntityFactory
	 */
	SbbEntityImpl(String sbbEntityId, SbbEntityCacheData cacheData, SbbEntityFactoryImpl sbbEntityFactory) {
		
		if (sbbEntityId == null)
			throw new NullPointerException(
					"SbbEntity cannot be instantiated for sbbeId == null");

		this.sbbEntityFactory = sbbEntityFactory;
		this.sleeContainer = sbbEntityFactory.getSleeContainer();

		this.sbbeId = sbbEntityId;
		this.cacheData = cacheData;
		this.sbbEntityImmutableData = (SbbEntityImmutableData) cacheData.getSbbEntityImmutableData();
		this.pool = sleeContainer.getSbbManagement().getObjectPool(getServiceId(), getSbbId());
		this.sbbComponent = pool.getSbbComponent();
		this.created = false;
	}

	public ServiceID getServiceId() {
		return sbbEntityImmutableData.getServiceID();
	}

	public String getServiceConvergenceName() {
		return sbbEntityImmutableData.getConvergenceName();
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

		if (doTraceLogs) {
			log.trace("getCMPField(cmpFieldName = "+cmpFieldName+") ");
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		CmpWrapper cmpWrapper = (CmpWrapper) cacheData
				.getCmpField(cmpFieldName);
		if (cmpWrapper != null) {
			switch (cmpWrapper.getType()) {

			case sbblo:
				// it's a sbbLocalObject cmp
				String sbbEntityId = (String) cmpWrapper.getValue();
				SbbEntity sbbEntity = sbbEntityFactory.getSbbEntity(sbbEntityId,false);
				if (sbbEntity == null)
					return null;
				else if (sbbEntity.isRemoved())
					return null;
				else {
					return sbbEntity.getSbbLocalObject();
				}

			case aci:
				final ActivityContext ac = sleeContainer
						.getActivityContextFactory().getActivityContext(
								(ActivityContextHandle) cmpWrapper.getValue());
				if (ac != null) {
					return ac.getActivityContextInterface();
				} else {
					return null;
				}

			case eventctx:
				final EventContextHandle eventContextHandle = (EventContextHandle) cmpWrapper
						.getValue();
				return sleeContainer.getEventContextFactory().getEventContext(eventContextHandle);

			case profilelo:
				ProfileLocalObjectCmpValue profileLocalObjectCmpValue = (ProfileLocalObjectCmpValue) cmpWrapper
						.getValue();
				try {
					ProfileTable profileTable = sleeContainer
							.getSleeProfileTableManager().getProfileTable(
									profileLocalObjectCmpValue
											.getProfileTableName());
					ProfileLocalObject ploc = (ProfileLocalObject) profileTable
							.find(profileLocalObjectCmpValue.getProfileName());
					return ploc;
				} catch (UnrecognizedProfileTableNameException e) {
					if (log.isDebugEnabled()) {
						log.debug("Unable to rebuild profile local object stored in CMP field, the profile table does not exist anymore: "
								+ profileLocalObjectCmpValue
										.getProfileTableName(), e);
					}
					return null;
				} 

			case normal:
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
			log.debug("Sbb entity "+getSbbEntityId()+" setting cmp field "+cmpFieldName+" to "+object);
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		CmpType cmpType = null;
		Serializable cmpValue = null;

		// TODO optimize by adding the cmp type to the generated setter method?
		if (object instanceof javax.slee.SbbLocalObject) {
			SbbLocalObjectImpl sbbLocalObjectImpl = null;
			try {
				sbbLocalObjectImpl = (SbbLocalObjectImpl) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown SbbLocalObject implementation");
			}
			CMPFieldDescriptor field = sbbComponent.getDescriptor().getCmpFields()
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
		} else if (object instanceof javax.slee.ActivityContextInterface) {
			org.mobicents.slee.container.activity.ActivityContextInterface activityContextInterfaceImpl = null;
			try {
				activityContextInterfaceImpl = (org.mobicents.slee.container.activity.ActivityContextInterface) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown ActivityContextInterface implementation");
			}
			cmpType = CmpType.aci;
			cmpValue = activityContextInterfaceImpl.getActivityContext().getActivityContextHandle();
		} else if (object instanceof javax.slee.EventContext) {
			EventContext eventContextImpl = null;
			try {
				eventContextImpl = (EventContext) object;
			} catch (ClassCastException e) {
				throw new IllegalArgumentException("CMP value being set ("
						+ object
						+ ") is an unknown EventContext implementation");
			}
			cmpType = CmpType.eventctx;
			cmpValue = eventContextImpl.getEventContextHandle();
		} else if (object instanceof javax.slee.profile.ProfileLocalObject) {
			ProfileLocalObject profileLocalObjectConcreteImpl = null;
			try {
				profileLocalObjectConcreteImpl = (ProfileLocalObject) object;
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
			cmpValue = (Serializable) object;
		}
		
		CmpWrapper cmpWrapper = new CmpWrapper(cmpFieldName, cmpType, cmpValue);
		cacheData.setCmpField(cmpFieldName, cmpWrapper);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#afterACAttach(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public void afterACAttach(ActivityContextHandle ach) {
		
		// add to cache
		cacheData.attachActivityContext(ach);

		// update event mask
		Set<EventTypeID> maskedEvents = sbbComponent.getDescriptor().getDefaultEventMask();
		if (maskedEvents != null && !maskedEvents.isEmpty()) {
			cacheData.updateEventMask(ach, new HashSet<EventTypeID>(maskedEvents));
		}
		
		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" attached to AC with handle " + ach
					+ " , events added to current mask: " + maskedEvents);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#afterACDetach(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public void afterACDetach(ActivityContextHandle ach) {

		// remove from cache
		cacheData.detachActivityContext(ach);

		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" detached from AC with handle " + ach);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getMaskedEventTypes(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public Set<EventTypeID> getMaskedEventTypes(ActivityContextHandle ach) {
		return cacheData.getMaskedEventTypes(ach);
	}

	public void setEventMask(ActivityContextHandle ach, String[] eventMask)
			throws UnrecognizedEventException {

		HashSet<EventTypeID> maskedEvents = new HashSet<EventTypeID>();

		if (eventMask != null && eventMask.length != 0) {
			
			EventTypeID eventTypeID = null;
			EventEntryDescriptor sbbEventEntry = null;
			for (int i = 0; i < eventMask.length; i++) {
				
				eventTypeID = sbbComponent.getDescriptor().getEventTypes().get(eventMask[i]);
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
			log.debug("Sbb entity "+getSbbEntityId()+" set event mask for AC "+ach+". Masked events: " + maskedEvents);
		}
		
	}
	
	public Set<ActivityContextHandle> getActivityContexts() {
		return cacheData.getActivityContexts();		
	}

	private static final String[] emptyStringArray = {};

	public String[] getEventMask(ActivityContextHandle ach) {

		Set<EventTypeID> maskedEvents = cacheData.getMaskedEventTypes(ach);

		if (doTraceLogs) {
			log.trace("SbbEntity "+getSbbEntityId()+" retrieved event mask for AC "+ach+". Masked events: " + maskedEvents);
		}

		if (maskedEvents == null || maskedEvents.isEmpty()) {
			return emptyStringArray;
		} else {
			String[] events = new String[maskedEvents.size()];
			Iterator<EventTypeID> evMaskIt = maskedEvents.iterator();
			for (int i = 0; evMaskIt.hasNext(); i++) {
				EventTypeID eventTypeId = evMaskIt.next();
				events[i] = sbbComponent.getDescriptor().getEventEntries().get(
						eventTypeId).getEventName();
			}
			return events;
		}
	}

	public String getRootSbbId() {
		return sbbEntityImmutableData.getRootSbbEntityID();
	}

	public boolean isRootSbbEntity() {
		return getParentSbbEntityId() == null;
	}

	public int getAttachmentCount() {
		int attachmentCount = getActivityContexts().size();
		// needs to add all children attachement counts too
		for (String sbbEntityId : cacheData.getAllChildSbbEntities()) {
			// recreated the sbb entity
			SbbEntity childSbbEntity = sbbEntityFactory.getSbbEntity(sbbEntityId, false);
			if (childSbbEntity != null) {
				attachmentCount += childSbbEntity.getAttachmentCount();
			}
		}
		return attachmentCount;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getPriority()
	 */
	public byte getPriority() {
		return cacheData.getPriority().byteValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#setPriority(byte)
	 */
	public void setPriority(byte priority) {
		cacheData.setPriority(Byte.valueOf(priority));
		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" priority set to " + priority);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#remove(boolean)
	 */
	public void remove(boolean removeFromParent)
			throws TransactionRequiredException, SystemException {
	
		if (doTraceLogs) {
			log.trace("remove(removeFromParent="+removeFromParent+")");
		}

		if (removeFromParent) {
			removeFromParent();
		}
		removeEntityTree();

		if (log.isDebugEnabled()) {
			log.debug("Removed sbb entity " + getSbbEntityId());
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

		if (doTraceLogs) {
			log.trace("removeEntityTree()");
		}
		
		// removes the SBB entity from all Activity Contexts.
		for (Iterator<ActivityContextHandle> i = this.getActivityContexts().iterator(); i.hasNext();) {
			ActivityContextHandle ach = i.next();
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
		boolean invokingServiceSet = SleeThreadLocals
				.getInvokingService() != null;
		if (!invokingServiceSet) {
			SleeThreadLocals.setInvokingService(getServiceId());
		}
		try {
			if (this.sbbObject == null) {
				this.assignSbbObject();
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
				SleeThreadLocals.setInvokingService(null);
			}
		}

		// gather all entities in child relations from cache
		Set<String> childSbbEntities = cacheData.getAllChildSbbEntities();

		// remove this entity data from cache
		removeFromCache();

		// now remove children
		for (Object childSbbEntityId : childSbbEntities) {
			SbbEntity childSbbEntity = sbbEntityFactory.getSbbEntity((String) childSbbEntityId,false);
			if (childSbbEntity != null) {
				// recreated the sbb entity and remove it
				sbbEntityFactory.removeSbbEntity(childSbbEntity, false,true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#trashObject()
	 */
	public void trashObject() {
		try {
			// FIXME shouldn't just return the object to the pool?
			this.pool.returnObject(sbbObject);
			this.sbbObject = null;
		} catch (Exception e) {
			throw new RuntimeException("Unexpected exception ", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#sbbRolledBack(java.lang.Object, javax.slee.ActivityContextInterface, boolean)
	 */
	public void sbbRolledBack(Object event,
			ActivityContextInterface activityContextInterface,
			boolean removeRollback) {
		if (sbbObject != null) {
			sbbObject.sbbRolledBack(event,activityContextInterface,removeRollback);
			sbbObject.sbbStore();
			sbbObject.sbbPassivate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbEntityId()
	 */
	public String getSbbEntityId() {
		return this.sbbeId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbId()
	 */
	public SbbID getSbbId() {
		return sbbEntityImmutableData.getSbbID();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#invokeEventHandler(org.mobicents.slee.core.event.SleeEvent, org.mobicents.slee.runtime.activity.ActivityContext, org.mobicents.slee.core.event.EventContext)
	 */
	public void invokeEventHandler(EventContext sleeEvent, ActivityContext ac,
			EventContext eventContextImpl) throws Exception {

		
		// get event handler method
		final EventHandlerMethod eventHandlerMethod = sbbComponent
				.getEventHandlerMethods().get(sleeEvent.getEventTypeId());
		// build aci
		ActivityContextInterface aci = ac.getActivityContextInterface();
		ActivityContextInterface activityContextInterface = null;
		if (eventHandlerMethod.getHasCustomACIParam()) {
			try {
				activityContextInterface = (ActivityContextInterface) this
						.getSbbComponent()
						.getActivityContextInterfaceConcreteClass()
						.getConstructor(
								new Class[] { org.mobicents.slee.container.activity.ActivityContextInterface.class,
										SbbComponent.class }).newInstance(
								new Object[] { aci, sbbComponent });
			} catch (Exception e) {
				String s = "Could not create Custom ACI!";
				// log.error(s, e);
				throw new SLEEException(s, e);
			}
		} else {
			activityContextInterface = aci;
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
		final EventRoutingTransactionData data = new EventRoutingTransactionDataImpl(
				sleeEvent, activityContextInterface);
		if (!isReentrant()) {
			data.getInvokedNonReentrantSbbEntities().add(sbbeId);
		}
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		txContext.setEventRoutingTransactionData(data);
		// invoke method
		try {

			//This is required. Since domain chain may indicate RA for instance, or SLEE deployer. If we dont do that test: tests/runtime/security/Test1112012Test.xml and second one, w
			//will fail because domain of SLEE tck ra is too restrictive (or we have bad desgin taht allows this to happen?)
			if(System.getSecurityManager()!=null) {
				AccessController.doPrivileged(new PrivilegedExceptionAction<Object>(){
				public Object run() throws IllegalAccessException, InvocationTargetException{
					eventHandlerMethod.getEventHandlerMethod().invoke(
							sbbObject.getSbbConcrete(), parameters);
					return null;
				}});
			}
			else {
				eventHandlerMethod.getEventHandlerMethod().invoke(
						sbbObject.getSbbConcrete(), parameters);
			}
		} catch(PrivilegedActionException pae) {
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
		} catch(IllegalAccessException iae) {
			throw new RuntimeException(iae);
		} catch(InvocationTargetException ite) {
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
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		// remove data from tx context
		txContext.setEventRoutingTransactionData(null);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#passivateAndReleaseSbbObject()
	 */
	public void passivateAndReleaseSbbObject() throws Exception {
		this.sbbObject.sbbPassivate();
		this.sbbObject.setState(SbbObjectState.POOLED);
		this.sbbObject.setSbbEntity(null);
		this.pool.returnObject(this.sbbObject);
		this.sbbObject = null;
		if (childsWithSbbObjects != null) {
			for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
			.hasNext();) {
				SbbEntity childSbbEntity = i.next();
				if (childSbbEntity.getSbbObject() != null) {
					childSbbEntity.passivateAndReleaseSbbObject();
				}
				i.remove();
			}
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
		if (childsWithSbbObjects != null) {
			for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
			.hasNext();) {
				SbbEntity childSbbEntity = i.next();
				if (childSbbEntity.getSbbObject() != null) {
					childSbbEntity.removeAndReleaseSbbObject();
				}
				i.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getObjectPool()
	 */
	public SbbObjectPool getObjectPool() {
		return this.pool;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbObject()
	 */
	public SbbObject getSbbObject() {
		return this.sbbObject;
	}

	public boolean isAttached(ActivityContextHandle ach) {
		return this.getActivityContexts().contains(ach);
	}

	public SbbComponent getSbbComponent() {
		return this.sbbComponent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getChildRelation(java.lang.String)
	 */
	public ChildRelationImpl getChildRelation(String accessorName) {
		return new ChildRelationImpl(this.sbbComponent.getDescriptor()
				.getGetChildRelationMethodsMap().get(accessorName), this);		
	}

	public void asSbbActivityContextInterface(ActivityContextInterface aci) {
		try {
			Class<?> aciclass = this.getSbbComponent()
					.getActivityContextInterfaceConcreteClass();
			if (aciclass != null) {

				Class<?>[] argTypes = new Class[] { aci.getClass(),
						SbbComponent.class };
				Constructor<?> cons = aciclass.getConstructor(argTypes);
				Object retval = cons.newInstance(new Object[] { aci,
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
			return ((SbbEntityImpl) obj).sbbeId.equals(this.sbbeId);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return sbbeId.hashCode();
	}
	
	/**
	 * TODO improve with sbb local object storage		
	 * @return
	 */
	public SbbLocalObjectImpl getSbbLocalObject() {
		
		if (doTraceLogs)
			log.trace("getSbbLocalObject()");

		// The concrete class generated in ConcreteLocalObjectGenerator
		final Class<?> sbbLocalClass = sbbComponent.getSbbLocalInterfaceConcreteClass();
		if (sbbLocalClass != null) {
			Object[] objs = { this };
			Constructor<?> constructor = sbbComponent.getSbbLocalObjectClassConstructor();
			if (constructor == null) {
				final Class<?>[] types = { SbbEntityImpl.class };
				try {
					constructor = sbbLocalClass.getConstructor(types);
				} catch (Throwable e) {
					throw new SLEEException("Unable to retrieve sbb local object generated class constructor",e);
				}
				sbbComponent.setSbbLocalObjectClassConstructor(constructor);
			}
			try {
				return (SbbLocalObjectImpl) constructor.newInstance(objs);
			} catch (Throwable e) {
				throw new SLEEException(
						"Failed to create Sbb Local Interface.", e);
			}
		} else {
			return new SbbLocalObjectImpl(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#isRemoved()
	 */
	public boolean isRemoved() {
		return removed;
	}

	/**
	 * Remove entity from cache.
	 */
	private void removeFromCache() {
		cacheData.remove();
		removed = true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getParentChildRelation()
	 */
	public String getParentChildRelation() {
		return sbbEntityImmutableData.getParentChildRelationName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getParentSbbEntityId()
	 */
	public String getParentSbbEntityId() {
		return sbbEntityImmutableData.getParentSbbEntityID();
	}

	// It removes the SBB entity from the ChildRelation object that the SBB
	// entity belongs
	// to.
	private void removeFromParent() throws TransactionRequiredException,
			SystemException {

		if (doTraceLogs) {
			log.trace("removeFromParent()");
		}

		if (this.getParentSbbEntityId() != null) {
			SbbEntityImpl parent = sbbEntityFactory.getSbbEntity(this.getParentSbbEntityId(),false);
			if (parent != null) {
					parent.getChildRelation(getParentChildRelation()).removeChild(
							this.getSbbEntityId());
			}
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

	private Set<SbbEntity> childsWithSbbObjects = null;

	protected void addChildWithSbbObject(SbbEntity childSbbEntity) {
		if (childsWithSbbObjects == null) {
			childsWithSbbObjects = new HashSet<SbbEntity>();
		}
		childsWithSbbObjects.add(childSbbEntity);
	}

	@Override
	public String toString() {
		return "SbbEntity:"+sbbeId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#assignSbbObject()
	 */
	public void assignSbbObject() throws Exception {
		
		try {
			// get one object from the pool
			this.sbbObject = (SbbObject) this.pool.borrowObject();
			// invoke the appropriate sbb life-cycle methods
			this.sbbObject.setSbbEntity(this);
			if (created) {
				this.sbbObject.sbbCreate();
				this.sbbObject.setState(SbbObjectState.READY);
				this.sbbObject.sbbPostCreate();
			}
			else {
				this.sbbObject.sbbActivate();
				this.sbbObject.setState(SbbObjectState.READY);
			}
			this.sbbObject.sbbLoad();
		} catch (Exception e) {
			log.error("Failed to assign and create sbb object", e);
			if (created) {
				removeFromCache();
			}
			throw e;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#isCreated()
	 */
	public boolean isCreated() {
		return created;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.sbbentity.SbbEntity#isReentrant()
	 */
	public boolean isReentrant() {
		return sbbComponent.isReentrant();
	}
}
