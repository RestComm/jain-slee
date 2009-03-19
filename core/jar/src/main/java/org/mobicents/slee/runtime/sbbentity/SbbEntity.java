/*
 * Mobicents: The Open Source SLEE Platform      
 *
 * Copyright 2003-2005, CocoonHive, LLC., 
 * and individual contributors as indicated
 * by the @authors tag. See the copyright.txt 
 * in the distribution for a full listing of   
 * individual contributors.
 *
 * This is free software; you can redistribute it
 * and/or modify it under the terms of the 
 * GNU General Public License (GPL) as
 * published by the Free Software Foundation; 
 * either version 2 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that 
 * it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR 
 * PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the 
 * GNU General Public
 * License along with this software; 
 * if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, 
 * Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site:
 * http://www.fsf.org.
 */

package org.mobicents.slee.runtime.sbbentity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.SbbLocalObject;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRequiredException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.EventTypeComponent;
import org.mobicents.slee.container.component.SbbComponent;
import org.mobicents.slee.container.component.ServiceComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.management.jmx.InstalledUsageParameterSet;
import org.mobicents.slee.container.management.jmx.ServiceUsageMBeanImpl;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.container.service.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.activity.ActivityContextState;
import org.mobicents.slee.runtime.cache.SbbEntityCacheData;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.eventrouter.EventContextImpl;
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
	static private final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private Transaction transaction;
	private DeferredEvent currentEvent;

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
		
		this.pool = sleeContainer.getSbbManagement().getSbbPoolManagement()
				.getObjectPool(getSbbId());
		this.sbbComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(getSbbId());
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
					
			this.pool = sleeContainer.getSbbManagement().getSbbPoolManagement()
					.getObjectPool(getSbbId());
			this.sbbComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(getSbbId());
			if (this.sbbComponent == null) {
				String s = "Sbb component/descriptor not found for sbbID["
						+ getSbbId() + "],\n" + "  sbbEntityID[" + sbbeId + "]";
				log.warn(s);
				throw new RuntimeException(s);
			}
		}
		else {
			throw new IllegalStateException(
			"Sbb entity "+sbbEntityId+" not found");
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

		CmpWrapper cmpWrapper = (CmpWrapper) cacheData.getCmpField(cmpFieldName);
		if (cmpWrapper != null) {
			if (cmpWrapper.getType() == CmpType.sbblo) {

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
			} else {
				// May return null here. The DefaultPersistenceInterceptor takes
				// care of
				// returning the right default value.
				if (log.isDebugEnabled()) {
					log.debug("getCMPField() value = "
							+ cmpWrapper.getValue());
				}
				return cmpWrapper.getValue();
			}
		}
		else {
			return null;
		}
	}

	public void setCMPField(String cmpFieldName, Object object)
			throws TransactionRequiredLocalException {

		if (log.isDebugEnabled()) {
			log
					.debug("putCMPField(): putting cmp field : "
							+ cmpFieldName + "/" + " object = "
							+ object);
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		CmpType cmpType = null;
		Object cmpValue = null;
		if (object instanceof SbbLocalObject) {
			cmpType = CmpType.sbblo;
			cmpValue = ((SbbLocalObjectImpl) object).getSbbEntityId();
		}
		else {
			cmpType = CmpType.normal;
			cmpValue = object;
		}
		CmpWrapper cmpWrapper = new CmpWrapper(cmpFieldName,cmpType,cmpValue);
		cacheData.setCmpField(cmpFieldName,cmpWrapper);
	}
	
	public void afterACAttach(String acId) {

		// add event mask entry
		Collection<MEventEntry> mEventEntries = sbbComponent.getDescriptor().getEventEntries().values();
		HashSet<EventTypeID> maskedEvents = null;
		if (mEventEntries != null) {
			maskedEvents = new HashSet<EventTypeID>();
			for (MEventEntry mEventEntry : mEventEntries) {
				if (mEventEntry.isMaskOnAttach()) {
					maskedEvents.add(mEventEntry.getEventReference().getComponentID());
				}
			}			
		}
		// add to cache
		cacheData.attachActivityContext(acId);
		cacheData.updateEventMask(acId, maskedEvents);
		
		if (log.isDebugEnabled()) {
			log.debug("attached sbb entity " + sbbeId + " to ac " + acId+" , events added to current mask "+maskedEvents);
		}
	}

	public void afterACDetach(String acId) {

		// remove from cache
		cacheData.detachActivityContext(acId);
		
		if (log.isDebugEnabled()) {
			log.debug("detached sbb entity " + sbbeId + " to ac " + acId);
		}
	}

	public Set getMaskedEventTypes(String acId) {

		Set eventMaskSet = cacheData.getMaskedEventTypes(acId);

		if (log.isDebugEnabled()) {
			log.debug("event mask for sbb entity " +sbbeId+" and ac "+ acId+" --> "+eventMaskSet);
		}
		
		if (eventMaskSet == null) {
			return Collections.EMPTY_SET;
		}
		else {
			return eventMaskSet;
		}
	}

	public void setEventMask(String acId, String[] eventMask)
			throws UnrecognizedEventException {

		HashSet<EventTypeID> maskedEvents = new HashSet<EventTypeID>();

		if (eventMask != null && eventMask.length != 0) {

			for (int i = 0; i < eventMask.length; i++) {
				MEventEntry sbbEventEntry = sbbComponent.getDescriptor().getEventEntries().get(eventMask[i]);
				if (sbbEventEntry == null)
					throw new UnrecognizedEventException(
							"Event is not known by this SBB.");
				if (sbbEventEntry.isReceived()) {
					maskedEvents.add(sbbEventEntry.getEventReference().getComponentID());
				} else {
					throw new UnrecognizedEventException("Event "
							+ eventMask[i]
							+ " has no receive direction for this SBB.");
				}
			}
		}

		cacheData.setEventMask(acId, maskedEvents);

		if (log.isDebugEnabled()) {
			log.debug("set event mask "+maskedEvents+" for sbb entity " +sbbeId+" and ac "+ acId);
		}		
	}
	
	public Set getActivityContexts() {
		Set result = cacheData.getActivityContexts();
		return result == null ? Collections.EMPTY_SET : result;
	}

	private static final String[] emptyStringArray = {};
	
	public String[] getEventMask(String acId) {
		
		Set maskedEvents = (Set) cacheData.getMaskedEventTypes(acId);
		
		if (log.isDebugEnabled()) {
			log.debug("set event mask "+maskedEvents+" for sbb entity " +sbbeId+" and ac "+ acId);
		}	
		
		if (maskedEvents == null || maskedEvents.isEmpty()) {			
			return emptyStringArray;
		}
		else {
			String[] events = new String[maskedEvents.size()];
			Iterator evMaskIt = maskedEvents.iterator();
			for (int i = 0; evMaskIt.hasNext(); i++) {
				EventTypeID eventTypeId = (EventTypeID) evMaskIt.next();
				events[i] = sbbComponent.getDescriptor().getEventEntries().get(eventTypeId).getEventName();				
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
		for (MGetChildRelationMethod getChildRelationMethod : this.sbbComponent.getDescriptor().getGetChildRelationMethods().values()) {
			// (re)create child relation obj
			ChildRelationImpl childRelationImpl = new ChildRelationImpl(
					getChildRelationMethod, this);
			// iterate all sbb entities in this child relation
			for (Iterator i = childRelationImpl.getSbbEntitySet().iterator(); i
					.hasNext();) {
				String childSbbEntityID = (String) i.next();
				// recreated the sbb entity
				SbbEntity childSbbEntity = SbbEntityFactory
						.getSbbEntity(childSbbEntityID);
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
			log.debug("set sbb entity "+sbbeId+" priority to " + priority);
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
			String acId = (String) i.next();
			// get ac
			ActivityContext ac = SleeContainer.lookupFromJndi()
					.getActivityContextFactory().getActivityContext(acId,true);
			// remove the sbb entity from the attachment set.
			if (ac != null && ac.getState() == ActivityContextState.ACTIVE) {
				ac.detachSbbEntity(this.sbbeId);
			}
			// no need to remove ac from entity because the entity is being
			// removed
		}

		// It invokes the appropriate life cycle methods (see Section 6.3) of an
		// SBB object that caches the SBB entity state.
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
		}

		// gather all entities in child relations from cache
		Set childSbbEntities = cacheData.getAllChildSbbEntities();

		// remove this entity data from cache
		removeFromCache();
		
		// now remove children
		for (Object childSbbEntityId : childSbbEntities) {
			// recreated the sbb entity and remove it
			SbbEntityFactory.removeSbbEntity((String)childSbbEntityId, false);
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

	private static final String TRANSACTION_CONTEXT_DATA_KEY_CURRENT_EVENT = "ce";
	
	public DeferredEvent getCurrentEvent() {
		return this.currentEvent;
	}

	public void setCurrentEvent(DeferredEvent sleeEvent) {
		this.currentEvent = sleeEvent;
	}

	/**
	 * 
	 * see JSLEE 1.0 spec, section 8.4.2 "SBB abstract class event handler
	 * methods"
	 * 
	 */
	private Method getEventHandlerMethod(DeferredEvent sleeEvent) {

		EventTypeID eventType = sleeEvent.getEventTypeId();
		// Note -- this naming convention is part of the slee specification.
		String methodName = "on" + sbbComponent.getDescriptor().getEventEntries().get(eventType).getEventName();

		Class concreteClass = sbbComponent.getConcreteSbbClass();

		if (log.isDebugEnabled()) {
			log.debug("invoking event handler " + methodName + " on "
					+ concreteClass.getName() + " ID " + sbbComponent.getSbbID()
					+ " sbbEntity " + this + " currentEvent " + sleeEvent);
		}

		Class[] args = new Class[2];
		EventTypeComponent eventComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(sleeEvent.getEventTypeId());
		
		// Once an error has been seen, we fire no more event handler
		// methods.

		ClassLoader ccl = SleeContainerUtils.getCurrentThreadClassLoader();
		try {
			args[0] = ccl.loadClass(eventComponent.getDescriptor().getEventClassName());
		} catch (ClassNotFoundException e) {
			String s = "Caught ClassNotFoundException in loading class";
			log.error(s, e);
			throw new RuntimeException(s, e);
		}
		if (log.isDebugEnabled()) {
			log.debug("event className is "
					+ eventComponent.getDescriptor().getEventClassName());
			log
					.debug("event class is ARGS[0] of the event handler: args[0] == "
							+ args[0]);
		}

		// Signature has to match correctly with the invoked method.

		Method method = null;
		boolean isCustomAciMethod = false;
		// Is there a custom SBB activity context interface.
		args[1] = sbbComponent.getActivityContextInterface();
		if (args[1] != null) {			
			try {
				method = concreteClass.getMethod(methodName, args);
				isCustomAciMethod = true;
			} catch (NoSuchMethodException e) {
				String s = "Caught NoSuchMethodException in loading class. There is no event handler with custom SBB ACI argument";
				if (log.isDebugEnabled()) {
					log.debug(s, e);
				}				
			}
		}
		if (!isCustomAciMethod) {
			// since there is no event handler with custom SBB ACI, let's look
			// for a handler with generic ACI argument
			try {
				// since there is no event handler with custom SBB ACI, let's
				// look for a handler with generic ACI argument
				args[1] = ActivityContextInterface.class;
				method = concreteClass.getMethod(methodName, args);
			} catch (NoSuchMethodException e) {
				String s = "Caught NoSuchMethodException while loading event handler method.";
				log.error(s, e);
				throw new RuntimeException(s, e);
			}
		}

		return method;
	}

	/**
	 * Implementing SLEE 8.4.2
	 * 
	 * @param sleeEvent
	 *            to be delivered to the SBB
	 * @param ac 
	 * @return arguments that will be passed to the SBB event handler method
	 */
	private Object[] getEventHandlerParameters(DeferredEvent sleeEvent, ActivityContext ac) {

		Object[] parameters = new Object[2];
		if (log.isDebugEnabled()) {
			log.debug("parameter [0] "
					+ sleeEvent.getEvent().getClass().getName());
		}
		parameters[0] = sleeEvent.getEvent();

		if (log.isDebugEnabled()) {
			log.debug("**PARAMETER 0 IS:" + parameters[0]);
			log.debug("**PARAM 0 class is:"
					+ parameters[0].getClass().getName());
		}
		ActivityContextInterface activityContextInterface = null;

		if (this.getSbbComponent().getActivityContextInterface() != null) {
			ActivityContextInterfaceImpl aciImpl = new ActivityContextInterfaceImpl(ac);
			Class aciClass = this.getSbbComponent()
					.getActivityContextInterfaceConcreteClass();
			try {
				// activityContextInterface = (ActivityContextInterface)
				// aciClass.getConstructor(new Class[] {
				// aciImpl.getClass(),this.getSbbDescriptor().getClass()
				// }).newInstance(new Object[] { aciImpl,
				// this.getSbbDescriptor() });
				activityContextInterface = (ActivityContextInterface) aciClass
						.getConstructor(
								new Class[] {
										aciImpl.getClass(),
										SbbComponent.class })
						.newInstance(
								new Object[] { aciImpl, this.getSbbComponent() });
			} catch (Exception e) {
				String s = "Could Not create ACI!";
				// log.error(s, e);
				throw new RuntimeException(s, e);
			}

		} else {
			activityContextInterface = new ActivityContextInterfaceImpl(ac);
		}

		// Stow this information away in case we have to call sbbExceptionThrown
		sleeEvent.setLoadedAci(activityContextInterface);

		parameters[1] = (ActivityContextInterface) activityContextInterface;
		if (log.isDebugEnabled()) {
			log.debug("**PARAMETER 1 IS:" + parameters[1]);
			log.debug("**PARAM 1 class is:"
					+ parameters[1].getClass().getName());
		}

		return parameters;
	}

	private void setServiceActivityFactory() throws Exception {
		// store the serviceID in tx local data so shared service
		// activity factory can use it
		sleeContainer.getTransactionManager().getTransactionContext().getData().put(
				ServiceActivityFactoryImpl.TXLOCALDATA_SERVICEID_KEY,
				getServiceId());
	}

	/**
	 * Actually invoke the event handler.
	 * 
	 */
	public void invokeEventHandler(DeferredEvent sleeEvent, ActivityContext ac, EventContextImpl eventContextImpl) throws Exception {

		// FIXME event context
		.
		
		// Actually invoke the event handler.
		Method method = getEventHandlerMethod(sleeEvent);
		setServiceActivityFactory();
		Object[] parameters = getEventHandlerParameters(sleeEvent,ac);

		try {
			this.transaction = sleeContainer.getTransactionManager()
					.getTransaction();

			method.invoke(this.sbbObject.getSbbConcrete(), parameters);

		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			// Remember the actual exception is hidden inside the
			// InvocationTarget exception when you use reflection!
			Throwable realException = e.getCause();
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
		}
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
			this.sbbObject.setServiceID(this.getServiceId());
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
			this.sbbObject.setServiceID(this.getServiceId());
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
		this.sbbObject.setServiceID(null);
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
		this.sbbObject.setServiceID(null);
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

	public boolean isAttached(String acId) {
		return this.getActivityContexts().contains(acId);
	}

	public InstalledUsageParameterSet getDefaultSbbUsageParameterSet() {
		if (log.isDebugEnabled()) {
			log.debug("getDefaultSbbUsageParameterSet(): " + getServiceId()
					+ " sbbID = " + getSbbId());
		}
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		ServiceComponent serviceComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(getServiceId());
		ServiceUsageMBeanImpl serviceUsageMbean = (ServiceUsageMBeanImpl) serviceComponent.getServiceUsageMBean();
		return serviceUsageMbean.getDefaultInstalledUsageParameterSet(getSbbId());
	}

	public Object getSbbUsageParameterSet(String name) {
		if (log.isDebugEnabled()) {
			log.debug("getSbbUsageParameterSet(): serviceId = " + getServiceId()
					+ " , sbbID = " + getSbbId()+" , name = "+name);
		}
		SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		ServiceComponent serviceComponent = sleeContainer.getComponentRepositoryImpl().getComponentByID(getServiceId());
		ServiceUsageMBeanImpl serviceUsageMbean = (ServiceUsageMBeanImpl) serviceComponent.getServiceUsageMBean();
		return serviceUsageMbean.getInstalledUsageParameterSet(getSbbId(),name);
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
		if ((getChildRelationMethod = this.sbbComponent
				.getDescriptor().getGetChildRelationMethods().get(accessorName)) != null) {
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
		try {
			if ((!this.getSbbComponent().getDescriptor().getSbbAbstractClass().isReentrant())
					&& this.transaction == sleeContainer.getTransactionManager().getTransaction())
				throw new SLEEException(" re-entrancy not allowed ");
		} catch (SystemException ex) {
			throw new RuntimeException(
					"Transaction Manager exception while checkReEntrant!", ex);
		}
	}

	public SbbLocalObjectImpl createSbbLocalObject() {
		Class sbbLocalClass;
		if (log.isDebugEnabled())
			log
					.debug("createSbbLocalObject "
							+ this.getSbbComponent());

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
					.getChildRelation(getParentChildRelation()).removeChild(this.getSbbEntityId());
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

}
