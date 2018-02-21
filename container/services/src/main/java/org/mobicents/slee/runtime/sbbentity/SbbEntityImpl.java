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

package org.mobicents.slee.runtime.sbbentity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.UnrecognizedEventException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.component.sbb.SbbComponent;
import org.mobicents.slee.container.component.sbb.SbbComponent.EventHandlerMethod;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.jndi.JndiManagement;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbb.SbbObjectPool;
import org.mobicents.slee.container.sbb.SbbObjectState;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionDataImpl;
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
	
	private final SbbEntityID sbbeId; // This is the primary key of the SbbEntity.

	private SbbObject sbbObject;

	// cache some objects that may be expensive to retrieve from sbb entity id
	private SbbID _sbbID = null;
	private SbbObjectPool _pool = null;
	private SbbComponent _sbbComponent = null;

	// cache data
	protected SbbEntityCacheData cacheData;

	private final boolean created;
		
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
	SbbEntityImpl(SbbEntityID sbbEntityId, SbbEntityCacheData cacheData, boolean created, SbbEntityFactoryImpl sbbEntityFactory) {
		this.sbbEntityFactory = sbbEntityFactory;
		this.sleeContainer = sbbEntityFactory.getSleeContainer();
		this.sbbeId = sbbEntityId;
		this.cacheData = cacheData;
		this.created = created;		
	}

	public ServiceID getServiceId() {
		return sbbeId.getServiceID();
	}

	public String getServiceConvergenceName() {
		return sbbeId.getServiceConvergenceName();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.sbbentity.SbbEntity#getCMPField(java.lang.String)
	 */
	public Object getCMPField(String cmpFieldName) {
		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" getting cmp field "+cmpFieldName);
		}
		sleeContainer.getTransactionManager().mandateTransaction();
		return cacheData.getCmpField(cmpFieldName); 
	}
		
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.container.sbbentity.SbbEntity#setCMPField(java.lang.String, java.lang.Object)
	 */
	public void setCMPField(String cmpFieldName, Object cmpFieldValue) {
		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" setting cmp field "+cmpFieldName+" to value "+cmpFieldValue);
		}
		sleeContainer.getTransactionManager().mandateTransaction();							
		cacheData.setCmpField(cmpFieldName, cmpFieldValue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#afterACAttach(org.mobicents.slee.runtime.activity.ActivityContextHandle)
	 */
	public void afterACAttach(ActivityContextHandle ach) {
		
		// add to cache
		cacheData.attachActivityContext(ach);

		// update event mask
		Set<EventTypeID> maskedEvents = getSbbComponent().getDescriptor().getDefaultEventMask();
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
				
				eventTypeID = getSbbComponent().getDescriptor().getEventTypes().get(eventMask[i]);
				if (eventTypeID == null)
					throw new UnrecognizedEventException(
							"Event is not known by this SBB.");
				
				sbbEventEntry = getSbbComponent().getDescriptor()
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
				events[i] = getSbbComponent().getDescriptor().getEventEntries().get(
						eventTypeId).getEventName();
			}
			return events;
		}
	}

	public int getAttachmentCount() {
		int attachmentCount = getActivityContexts().size();
		// needs to add all children attachement counts too
		for (SbbEntityID sbbEntityId : cacheData.getAllChildSbbEntities()) {
			// recreated the sbb entity
			SbbEntity childSbbEntity = sbbEntityFactory.getSbbEntity(sbbEntityId, false);
			if (childSbbEntity != null) {
				attachmentCount += childSbbEntity.getAttachmentCount();
			}
		}
		return attachmentCount;
	}

	private Byte priority = null;
	
	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getPriority()
	 */
	public byte getPriority() {
		if (priority == null) {
			priority = cacheData.getPriority();
		}
		if (priority == null) {
			// TODO check if alternative to fetch default priority and have non null only for custom performs good
			return 0;
		}
		return priority.byteValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#setPriority(byte)
	 */
	public void setPriority(byte value) {
		priority = Byte.valueOf(value);
		cacheData.setPriority(priority);
		if (log.isDebugEnabled()) {
			log.debug("Sbb entity "+getSbbEntityId()+" priority set to " + priority);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#remove()
	 */
	public void remove() {
	
		if (doTraceLogs) {
			log.trace("remove()");
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

		// remove children
		for (SbbEntityID childSbbEntityId : cacheData.getAllChildSbbEntities()) {
			SbbEntity childSbbEntity = sbbEntityFactory.getSbbEntity(childSbbEntityId,false);
			if (childSbbEntity != null) {
				// recreate the sbb entity and remove it
				sbbEntityFactory.removeSbbEntity(childSbbEntity,false);
			}
		}

		cacheData.remove();
				
		if (log.isDebugEnabled()) {
			log.debug("Removed sbb entity " + getSbbEntityId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#trashObject()
	 */
	public void trashObject() {
		try {
			// FIXME shouldn't just return the object to the pool?
			getObjectPool().returnObject(sbbObject);
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
			passivateAndReleaseSbbObject();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbEntityId()
	 */
	public SbbEntityID getSbbEntityId() {
		return this.sbbeId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbId()
	 */
	public SbbID getSbbId() {
		if (_sbbID == null) {
			ServiceComponent serviceComponent = sleeContainer.getComponentRepository().getComponentByID(getServiceId());
			SbbComponent sbbComponent = null;
			if (sbbeId.isRootSbbEntity()) {
				_sbbID = serviceComponent.getRootSbbComponent().getSbbID();
			}
			else {
				// put chain of parent sbb entity ids in a stack 
				final LinkedList<SbbEntityID> stack = new LinkedList<SbbEntityID>();
				SbbEntityID sbbEntityID = sbbeId.getParentSBBEntityID();
				while(!sbbEntityID.isRootSbbEntity()) {
					stack.push(sbbEntityID);
					sbbEntityID = sbbEntityID.getParentSBBEntityID();
				}
				// now find out the sbb component of the parent
				sbbComponent = serviceComponent.getRootSbbComponent();
				GetChildRelationMethodDescriptor getChildRelationMethodDescriptor = null;
				while(!stack.isEmpty()) {
					sbbEntityID = stack.pop();
					getChildRelationMethodDescriptor = sbbComponent.getDescriptor().getGetChildRelationMethodsMap().get(sbbEntityID.getParentChildRelation());
					sbbComponent = sleeContainer.getComponentRepository().getComponentByID(getChildRelationMethodDescriptor.getSbbID());
				}
				getChildRelationMethodDescriptor = sbbComponent.getDescriptor().getGetChildRelationMethodsMap().get(sbbeId.getParentChildRelation());
				_sbbID = getChildRelationMethodDescriptor.getSbbID();				
			}
		}
		return _sbbID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#invokeEventHandler(org.mobicents.slee.core.event.SleeEvent, org.mobicents.slee.runtime.activity.ActivityContext, org.mobicents.slee.core.event.EventContext)
	 */
	public void invokeEventHandler(EventContext sleeEvent, ActivityContext ac,
			EventContext eventContextImpl) throws Exception {

		
		// get event handler method
		final SbbComponent sbbComponent = getSbbComponent();
		final EventHandlerMethod eventHandlerMethod = sbbComponent
				.getEventHandlerMethods().get(sleeEvent.getEventTypeId());
		// build aci
		ActivityContextInterface aci = asSbbActivityContextInterface(ac.getActivityContextInterface());
		// now build the param array
		final Object[] parameters ;
		if (eventHandlerMethod.getHasEventContextParam()) {
			parameters = new Object[] { sleeEvent.getEvent(),
					aci, eventContextImpl };
		} else {
			parameters = new Object[] { sleeEvent.getEvent(),
					aci };
		}

		// store some info about the invocation in the tx context
		final EventRoutingTransactionData data = new EventRoutingTransactionDataImpl(
				sleeEvent, aci);		
		final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
		txContext.setEventRoutingTransactionData(data);
		// track sbb entity invocations if reentrant
		Set<SbbEntityID> invokedSbbentities = null;
		if (!isReentrant()) {
			invokedSbbentities = txContext.getInvokedNonReentrantSbbEntities();
			invokedSbbentities.add(sbbeId);
		}
		final JndiManagement jndiManagement = sleeContainer.getJndiManagement();
		jndiManagement.pushJndiContext(sbbComponent);
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
				    throw (RuntimeException) realException;
				} else if (realException instanceof Error) {
				    throw (Error) realException;
				} else if (realException instanceof Exception) {
				    throw (Exception) realException;
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
			    throw (RuntimeException) realException;
			} else if (realException instanceof Error) {
			    throw (Error) realException;
			} else if (realException instanceof Exception) {
			    throw (Exception) realException;
			}
		} catch(Exception e) {
			log.error(e.getMessage(),e);
		}
		finally {
			jndiManagement.popJndiContext();
			if(invokedSbbentities != null) {
				invokedSbbentities.remove(sbbeId);
			}
		}
		// remove data from tx context
		txContext.setEventRoutingTransactionData(null);
		
	}

	public ActivityContextInterface asSbbActivityContextInterface(
			ActivityContextInterface aci) {
		final SbbComponent sbbComponent = getSbbComponent();
		final Class<?> aciClass = sbbComponent.getActivityContextInterfaceConcreteClass();
		if (aciClass == null) {
			return aci;
		}
		else {
			try {
				return (ActivityContextInterface) aciClass
				.getConstructor(
						new Class[] { org.mobicents.slee.container.activity.ActivityContextInterface.class,
								SbbComponent.class }).newInstance(
										new Object[] { aci, sbbComponent });
			} catch (Exception e) {
				String s = "Could not create Custom ACI!";
				// log.error(s, e);
				throw new SLEEException(s, e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#passivateAndReleaseSbbObject()
	 */
	public void passivateAndReleaseSbbObject() {
		this.sbbObject.sbbStore();
		this.sbbObject.sbbPassivate();
		this.sbbObject.setState(SbbObjectState.POOLED);
		this.sbbObject.setSbbEntity(null);
		try {
			getObjectPool().returnObject(this.sbbObject);
		} catch (Exception e) {
			log.error("failed to return sbb object "+sbbObject+" to pool",e);
		}
		this.sbbObject = null;
		if (childsWithSbbObjects != null) {
			for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
			.hasNext();) {
				SbbEntity childSbbEntity = i.next();
				if (childSbbEntity.getSbbObject() != null) {
					Thread t = Thread.currentThread();
			    	ClassLoader cl = t.getContextClassLoader();
			        t.setContextClassLoader(childSbbEntity.getSbbComponent().getClassLoader());
					try {
						childSbbEntity.passivateAndReleaseSbbObject();
					}
					finally {
						t.setContextClassLoader(cl);
					}
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
		this.sbbObject.sbbStore();
		this.sbbObject.sbbRemove();
		this.sbbObject.setState(SbbObjectState.POOLED);
		this.sbbObject.setSbbEntity(null);
		getObjectPool().returnObject(this.sbbObject);
		this.sbbObject = null;
		if (childsWithSbbObjects != null) {
			for (Iterator<SbbEntity> i = childsWithSbbObjects.iterator(); i
			.hasNext();) {
				SbbEntity childSbbEntity = i.next();
				if (childSbbEntity.getSbbObject() != null) {
					Thread t = Thread.currentThread();
			    	ClassLoader cl = t.getContextClassLoader();
			        t.setContextClassLoader(childSbbEntity.getSbbComponent().getClassLoader());
					try {
						childSbbEntity.removeAndReleaseSbbObject();
					}
					finally {
						t.setContextClassLoader(cl);
					}	
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
		if (_pool == null) {
			_pool = sleeContainer.getSbbManagement().getObjectPool(getServiceId(), getSbbId());			
		}
		return this._pool;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getSbbObject()
	 */
	public SbbObject getSbbObject() {
		return this.sbbObject;
	}

	public boolean isAttached(ActivityContextHandle ach) {
		return cacheData.isAttached(ach);
	}

	public SbbComponent getSbbComponent() {
		if (_sbbComponent == null) {
			_sbbComponent = sleeContainer.getComponentRepository().getComponentByID(getSbbId());
		}
		return _sbbComponent;
	}

	/*
	 * (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbbentity.SbbEntity#getChildRelation(java.lang.String)
	 */
	public ChildRelationImpl getChildRelation(String accessorName) {
		return new ChildRelationImpl(this.getSbbComponent().getDescriptor()
				.getGetChildRelationMethodsMap().get(accessorName), this);		
	}

	@Override
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
		final Class<?> sbbLocalClass = getSbbComponent().getSbbLocalInterfaceConcreteClass();
		if (sbbLocalClass != null) {
			Object[] objs = { this };
			Constructor<?> constructor = getSbbComponent().getSbbLocalObjectClassConstructor();
			if (constructor == null) {
				final Class<?>[] types = { SbbEntityImpl.class };
				try {
					constructor = sbbLocalClass.getConstructor(types);
				} catch (Throwable e) {
					throw new SLEEException("Unable to retrieve sbb local object generated class constructor",e);
				}
				getSbbComponent().setSbbLocalObjectClassConstructor(constructor);
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
		return cacheData.isRemoved() || !cacheData.exists();
	}

	/**
	 * Remove entity from cache.
	 */
	private void removeFromCache() {
		cacheData.remove();
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
			this.sbbObject = getObjectPool().borrowObject();
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
		return getSbbComponent().isReentrant();
	}
}
