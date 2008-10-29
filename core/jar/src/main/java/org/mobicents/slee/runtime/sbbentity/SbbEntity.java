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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ActivityContextInterface;
import javax.slee.EventTypeID;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.UnrecognizedEventException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionRequiredException;

import org.apache.commons.pool.ObjectPool;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeContainerUtils;
import org.mobicents.slee.container.component.CMPField;
import org.mobicents.slee.container.component.GetChildRelationMethod;
import org.mobicents.slee.container.component.MobicentsEventTypeDescriptor;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.service.Service;
import org.mobicents.slee.runtime.ActivityContext;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.ActivityContextState;
import org.mobicents.slee.runtime.SleeEvent;
import org.mobicents.slee.runtime.cache.CacheableMap;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.sbb.SbbObjectState;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityFactoryImpl;
import org.mobicents.slee.runtime.transaction.TransactionManagerImpl;

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
	
    transient static private Logger log = Logger.getLogger(SbbEntity.class);
    transient private Transaction transaction;
    transient SleeEvent currentEvent;
    
    private final String sbbeId; // This is the primary key of the SbbEntity.
    private MobicentsSbbDescriptor sbbComponent;
    private String cacheNodeName;
    private SbbObject sbbObject;
    private CacheableMap eventMask;
    private CacheableSet sbbLocalObjectCmpFields;
    private ObjectPool pool;
    private CacheableMap cmpFields;
    private CacheableMap cachedSbbEntityAttributes;
    private CacheableSet attachedActivityContexts;
    private boolean isBeingRemoved = false;
	
	// cache ids
    private static final String PARENT_SBB_ENTITY_ID = "parentSbbEntityId";
	private static final String PARENT_CHILD_RELATION = "parentChildRelationName";
    private static final String ROOT_SBB_ID = "rootSbbId";
    private static final String SERVICE_CONVERGENCE_NAME = "serviceConvergenceName";
    private static final String SBB_ID_CACHE = TransactionManagerImpl.TCACHE;
    private static final String SBB_ID = "sbbID";
    private static final String ACTIVITY_CONTEXTS_CACHE = TransactionManagerImpl.TCACHE;
    private static final String ACTIVITY_CONTEXTS = "activityContexts";
    private static final String PRIORITY = "priority";
    private static final String EVENT_MASK_CACHE = TransactionManagerImpl.TCACHE;
    private static final String EVENT_MASK = "eventMask";
    private static final String SBB_LOCAL_OBJECT_CMP_FIELDS = "sbbLocalObjectCmpFields";
    private static final String SERVICE_ID = "serviceId";
    private static final String CMP_FIELDS_CACHE = TransactionManagerImpl.TCACHE;
    private static final String CMP_FIELDS = "cmpFields";
	private static final String CACHED_SBBE_ATTRS = "cached-sbb-entitiy-attributes";

	// local cache of fields
	private ServiceID serviceID = null;
	private SbbID sbbID = null;
	private String convergenceName = null;
	private String rootSbbEID = null;
	private String parentSbbEID = null;
	private String parentChildRelation = null;

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
     SbbEntity(String sbbEntityId, String parentSbbEntityId, String parentChildRelationName, String rootSbbEntityId, SbbID sbbID, String convergenceName,
            ServiceID svcId) throws Exception {
        
    	if (sbbID == null)
            throw new NullPointerException("Null sbbID");
        
        this.sbbeId = sbbEntityId;
        
    	this.cacheNodeName = "sbbentity:" + sbbeId;
        this.sbbLocalObjectCmpFields = new CacheableSet(CMP_FIELDS_CACHE + "-" + SBB_LOCAL_OBJECT_CMP_FIELDS + ":" + cacheNodeName);
        this.eventMask = new CacheableMap(EVENT_MASK_CACHE + "-" + EVENT_MASK + ":" + cacheNodeName);
        this.cmpFields = new CacheableMap(CMP_FIELDS_CACHE+ "-" + CMP_FIELDS + ":" + cacheNodeName);
        this.cachedSbbEntityAttributes = new CacheableMap(SBB_ID_CACHE + "-" + CACHED_SBBE_ATTRS  + ":" + cacheNodeName);
        this.attachedActivityContexts = new CacheableSet(ACTIVITY_CONTEXTS_CACHE + "-" + ACTIVITY_CONTEXTS+"_"+sbbeId);

        setParentSbbEntityId(parentSbbEntityId);
        setParentChildRelation(parentChildRelationName);
        setRootSbbId(rootSbbEntityId);
        setSbbId(sbbID);
        setServiceId(svcId);
        setServiceConvergenceName( convergenceName );
    
        this.pool = (ObjectPool) SleeContainer.lookupFromJndi().getSbbPoolManagement().getObjectPool(getSbbId());        
        this.sbbComponent = (MobicentsSbbDescriptor) SleeContainer.lookupFromJndi()
    	.getSbbComponent(getSbbId());
	    if (this.sbbComponent == null) {
	        String s = "Sbb component/descriptor not found for sbbID[" + getSbbId() + "],\n" +
	        		"  sbbEntityID[" + sbbeId + "],\n" +
	        		"  Transaction[ID:" + SleeContainer.getTransactionManager().getTransaction() + "]";
	        log.warn(s);
	        throw new RuntimeException(s);
	    }
	    
    }

	/**
     * Constructors an already existing sbb entity from the treecache given it's
     * id. Note that we do not add a transactional action for this constructor.
     * 
     * @param sbbeId
     * @throws RuntimeException if recreation from cache is not possible, i.e., does not exists
     */
    SbbEntity(String sbbEntityId) {
    	
    	if (sbbEntityId == null) throw new NullPointerException("SbbEntity cannot be instantiated for sbbeId == null");
    	
    	this.sbbeId = sbbEntityId;
    	this.cacheNodeName = "sbbentity:" + sbbeId;
    	this.cachedSbbEntityAttributes = new CacheableMap(SBB_ID_CACHE + "-" + CACHED_SBBE_ATTRS  + ":" + this.cacheNodeName);
    	
    	SbbID sbbId = getSbbId();
        
        if (sbbId != null) {       	
        	
        	this.eventMask = new CacheableMap(EVENT_MASK_CACHE + "-" + EVENT_MASK + ":" + this.cacheNodeName);        	
        	this.sbbLocalObjectCmpFields = new CacheableSet(CMP_FIELDS_CACHE + "-" + SBB_LOCAL_OBJECT_CMP_FIELDS + ":" + this.cacheNodeName);
        	this.cmpFields = new CacheableMap(CMP_FIELDS_CACHE+ "-" + CMP_FIELDS + ":" + this.cacheNodeName);        	        	
        	this.attachedActivityContexts = new CacheableSet(ACTIVITY_CONTEXTS_CACHE + "-" + ACTIVITY_CONTEXTS+"_"+sbbeId);
    
        	this.pool = (ObjectPool) SleeContainer.lookupFromJndi().getSbbPoolManagement().getObjectPool(sbbId);    	
        	this.sbbComponent = (MobicentsSbbDescriptor) SleeContainer.lookupFromJndi()
        	.getSbbComponent(sbbId);
        	if (this.sbbComponent == null) {
        		String s = "Sbb component/descriptor not found for sbbID[" + getSbbId() + "],\n" +
        		"  sbbEntityID[" + sbbeId + "]";
        		log.warn(s);
        		throw new RuntimeException(s);
        	}
        }
        else {
        	this.cachedSbbEntityAttributes.remove();
        	throw new IllegalStateException("Sbb id not found, unable to recreate sbb entity");
        }   
    }

    private Object getObjectFromCache(Object key) {
		return cachedSbbEntityAttributes.get(key);
	}

    private Object putObjectInCache(Object key, Object value) {
		return cachedSbbEntityAttributes.put(key, value);
	}
    
    public ServiceID getServiceId() {
    	if (serviceID == null) {
    		serviceID = (ServiceID)getObjectFromCache(SERVICE_ID);
    	}
    	return serviceID;
    }

    private void setServiceId(ServiceID svcId) {
        putObjectInCache(SERVICE_ID, svcId);
        serviceID = svcId;
    }
    
    private void setServiceConvergenceName(String convergenceName) {
    	putObjectInCache(SERVICE_CONVERGENCE_NAME, convergenceName);
    	this.convergenceName = convergenceName;
	}

    public String getServiceConvergenceName() {
    	if (convergenceName == null) {
    		convergenceName = (String)getObjectFromCache(SERVICE_CONVERGENCE_NAME);
    	}
    	return convergenceName;
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
                    + "\nrootSbbId = " + this.getRootSbbId() 
                    + "\nserviceID = " + getServiceId()
                    + "\nactivityContexts = " + this.getActivityContexts()
                    + "\neventMask = " + this.eventMask
                    + "\nconvergenceName = "+ getServiceConvergenceName()
                    + "\nsbbLocalObjectCmpFields = "+ this.sbbLocalObjectCmpFields
                    + "\ncmpFields = "+ this.cmpFields + "\n}");
        }
    }    

    private void addEventMaskEntry(String acID, Set evMask, boolean replace) {
        if (log.isDebugEnabled()) {
            log.debug("addEventMaskEntry : " + acID + " eventMask = " + evMask);
        }
        Set oldEvMask = (Set) this.eventMask.get(acID);
        if (oldEvMask == null) {
            this.eventMask.put(acID, evMask);
        } else {
            if (!replace)
                oldEvMask.addAll(evMask);
            else
                this.eventMask.put(acID, evMask);
        }
    }

    private void removeEventMaskEntry(String acID) {
        if (log.isDebugEnabled()) {
            log.debug("removeEventMaskEntry : " + acID);
        }
        this.eventMask.remove(acID);
    }

    private void addAcToActivityContexts(String acId) {
        if (log.isDebugEnabled()) {
            log.debug("addAcToActivityContexts : sbbEid " + this.sbbeId
                    + " acId " + acId);
        }
        this.getActivityContexts().add(acId);
    }

    private void removeAcFromActivityContexts(String acId) {
        if (log.isDebugEnabled()) {
            log.debug("removeAcFromActivityContexts : sbbEid "
                    + this.sbbeId + " acId " + acId);
        }
        
        this.getActivityContexts().remove(acId);
    }

    /**
     * The generated code to access CMP Fields needs to call this method.
     * 
     * @param cmpField
     * @return @throws
     *         TransactionRequiredLocalException
     * @throws SystemException
     */
    public Object getCMPField(CMPField cmpField)
            throws TransactionRequiredLocalException, SystemException {
       
        if (log.isDebugEnabled()) {
            log.debug("getCMPField() " + cmpField.getFieldName());
        }

    	SleeContainer.getTransactionManager().mandateTransaction();
        
    	String cmpFieldKey = this.genCMPFieldKey(cmpField);
  
        if (this.sbbLocalObjectCmpFields.contains(cmpFieldKey)) {
        	// it's a sbbLocalObject cmp
            String sbbEntityId = (String) this.cmpFields.get(cmpFieldKey);
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
                log.debug("getCMPField() value = " + this.cmpFields.get(cmpFieldKey));
            }
            return this.cmpFields.get(cmpFieldKey);

        }
    }

    public void setCMPField(CMPField field, Object object)
            throws TransactionRequiredLocalException, SystemException {
       
    	if (log.isDebugEnabled()) {
            log.debug("putCMPField(): putting cmp field : "
                            + this.getCMPFieldsNodeName() + "/" + " object = "
                            + object);
        }
    	
    	SleeContainer.getTransactionManager().mandateTransaction();
        
    	String cmpFieldKey = this.genCMPFieldKey(field);
        if (object instanceof SbbLocalObjectImpl) {
        	// it's a sbbLocalObject so we actually store the sbb entity id
            this.sbbLocalObjectCmpFields.add(cmpFieldKey);
            SbbLocalObjectImpl sbbLocal = (SbbLocalObjectImpl) object;
            this.cmpFields.put(cmpFieldKey, sbbLocal.getSbbEntityId());
        } else {
            this.cmpFields.put(cmpFieldKey, object);
        }
    }

    private String getCMPFieldsNodeName() {
        return this.cacheNodeName + "/CMP";
    }

    private String genCMPFieldKey(CMPField cmpField) {
        return cmpField.getFieldName();
    }

    public void afterACAttach(String acId) {
       
    	if (log.isDebugEnabled()) {
            log.debug("afterACAttach " + acId + " sbbID = " + getSbbId());
        }
        
    	// add event mask entry
        EventTypeID[] eventTypeIDs = sbbComponent.getEventTypes();
        if (eventTypeIDs != null) {
            HashSet<EventTypeID> maskedEvents = new HashSet<EventTypeID>();
            for (EventTypeID eventTypeID : eventTypeIDs) {
                SbbEventEntry sbbEventEntry = sbbComponent
                        .getEventType(sbbComponent
                                .getEventName(eventTypeID));
                if (sbbEventEntry.isMasked()) {
                    maskedEvents.add(eventTypeID);
                }
            }
            if (!maskedEvents.isEmpty()) {
            	this.addEventMaskEntry(acId, maskedEvents, false);
            }
        }
        
        // add to ACs attached
        addAcToActivityContexts(acId);
        
    }

    public void afterACDetach(String acId) {
    	
    	if (log.isDebugEnabled()) {
            log.debug("afterACDetach " + acId + " sbbID = " + getSbbId());
        }
    	
        try {
        	// remove from ACs attached
            this.removeAcFromActivityContexts(acId);
            // remove event mask entry
             removeEventMaskEntry(acId);
        } catch (Exception ex) {
            throw new RuntimeException("unexpected error", ex);
        }
    }

    public Set<EventTypeID> getMaskedEventTypes(String acId) {

        if (log.isDebugEnabled()) {
            log.debug("getMaskedEventTypes: " + acId);
        }
        Set<EventTypeID> eventMaskSet = (Set<EventTypeID>) this.eventMask.get(acId);
        if (eventMaskSet == null) {
        	eventMaskSet = new HashSet<EventTypeID>();
        }
        return eventMaskSet;
    }

    public void setEventMask(String acId, String[] eventMask)
            throws UnrecognizedEventException {

        HashSet<EventTypeID> maskedEvents = new HashSet<EventTypeID>();

        if (log.isDebugEnabled()) {
            log.debug("setEventMask " + acId + " eventMask = " + eventMask);
        }

        // Ralf Siedow: added event mask reset
        
        if (eventMask != null && eventMask.length != 0) {

            for (int i = 0; i < eventMask.length; i++) {
                SbbEventEntry sbbEventEntry = sbbComponent
                        .getEventType(eventMask[i]);
                if (sbbEventEntry == null)
                    throw new UnrecognizedEventException(
                            "Event is not known by this SBB.");
                if (sbbEventEntry.isReceived()) {
                    maskedEvents.add(SleeContainer.lookupFromJndi()
                            .getEventType(sbbEventEntry.getEventTypeRefKey()));
                } else {
                    throw new UnrecognizedEventException("Event "
                            + eventMask[i]
                            + " has no receive direction for this SBB.");
                }
            }
        }
        
        this.addEventMaskEntry(acId, maskedEvents, true);

    }
    

    public Set getActivityContexts() {
    	return attachedActivityContexts;
    }
    
    public String[] getEventMask(String acId) {
        
        Set evMask = (Set) this.eventMask.get(acId);
        if (log.isDebugEnabled()) {
            log.debug("getEventMask: returning  event mask for" + acId);
        }
        
        if (evMask == null) {
            log.debug("getEventMask: returning null event mask for" + acId);
            evMask = new HashSet();
        }

        String[] events = new String[evMask.size()];
        if (evMask.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("eventMask = " + eventMask);
            }
            return events;
        }
        
        Iterator evMaskIt = evMask.iterator();
        MobicentsSbbDescriptor sbbComponent = this.getSbbDescriptor();
        for (int i = 0; evMaskIt.hasNext(); i++) {
            EventTypeID eventTypeId = (EventTypeID) evMaskIt.next();
            events[i] = sbbComponent.getEventName(eventTypeId);
            if (log.isDebugEnabled()) {
                log.debug("getEventMask:eventName = " + events[i]);
            }
        }

        return events;
    }

    public String getRootSbbId() {
        if(rootSbbEID == null)  {
        	rootSbbEID = (String) this.getObjectFromCache(ROOT_SBB_ID);
        }
        return rootSbbEID;
    }
    
    public boolean isRootSbbEntity() {
    	return getParentSbbEntityId() == null;
    }

    private void setRootSbbId(String rsbbId) {
    	putObjectInCache(ROOT_SBB_ID, rsbbId);
    	this.rootSbbEID =  rsbbId;
    }
    
    public int getAttachmentCount() {	    	
    	int attachmentCount = getActivityContexts().size();
       	// needs to add all children attachement counts too
    	for (GetChildRelationMethod getChildRelationMethod : this.sbbComponent.getChildRelationMethods()) {
    		// (re)create child relation obj
    		ChildRelationImpl childRelationImpl = new ChildRelationImpl(getChildRelationMethod,this);
    		// iterate all sbb entities in this child relation
    		for (Iterator i = childRelationImpl.getSbbEntitySet().iterator(); i.hasNext();) {
    			String childSbbEntityID = (String) i.next();
    			// recreated the sbb entity
    			SbbEntity childSbbEntity = SbbEntityFactory.getSbbEntity(childSbbEntityID);
    			attachmentCount += childSbbEntity.getAttachmentCount();
    		}
    	}
    	if (log.isDebugEnabled()) {
            log.debug(sbbeId+" getAttachmentCount()="+attachmentCount);
    	}
    	return attachmentCount;
    }

    public byte getPriority() {
    	return ((Byte)getObjectFromCache(PRIORITY)).byteValue();
    }

    public void setPriority(byte priority) {
        if (log.isDebugEnabled()) {
            log.debug("SbbEntity.setPriority() " + priority);
        }
        putObjectInCache(PRIORITY, Byte.valueOf(priority));
    }

  
    /**
     * Remove the SbbEntity (Spec. 5.5.4) It detaches the SBB entity from all
     * Activity Contexts. It invokes the appropriate life cycle methods (see
     * Section 6.3) of an SBB object that caches the SBB entity’s state. It
     * removes the SBB entity from the ChildRelation object that the SBB entity
     * belongs to. It removes the persistent representation of the SBB entity.
     * 
     * @param removeFromParent indicates if the entity should be removed from it's parent or not
     * @throws TransactionRequiredException
     * @throws SystemException
     */
    protected void remove(boolean removeFromParent) throws TransactionRequiredException, SystemException {
    	if (log.isDebugEnabled()) {
    		log.debug("SbbEntity.remove(): Removing entity with id:"
    				+ this.getSbbEntityId());
    	}

    	if (removeFromParent) {
    		removeFromParent();
    	}
        removeEntityTree();
        
        if (log.isDebugEnabled()) {
    		log.debug("REMOVED SBB ENTITY "+this.sbbeId);
        }
    }

    /**
     * Removes the entity tree from this entity, that is, all sbb entities on it's child relations.
     * @throws TransactionRequiredException
     * @throws SystemException
     */
    private void removeEntityTree() throws TransactionRequiredException,
            SystemException {
       
    	if (log.isDebugEnabled()) {
        	log.debug("removing entity tree for sbbeid "+this.sbbeId);
        }
    	
        // removes the SBB entity from all Activity Contexts.
    	for (Iterator i = this.getActivityContexts().iterator(); i.hasNext();) {
    		String acId = (String) i.next();
    		// get ac
    		ActivityContext ac = SleeContainer.lookupFromJndi()
            .getActivityContextFactory().getActivityContextById(acId);
    		 // remove the sbb entity from the attachment set.
            if (ac != null && ac.getState().equals(ActivityContextState.ACTIVE)) {
                ac.detachSbbEntity(this.sbbeId);
            }
            // no need to remove ac from entity because the entity is being removed
    	}
       
        // 	It invokes the appropriate life cycle methods (see Section 6.3) of an
        //  SBB object that caches the SBB entity state.
        try {
            if (this.sbbObject == null) {
                this.assignAndActivateSbbObject();
            }
            sbbObject.sbbStore();      
            removeAndReleaseSbbObject();
        } catch (Exception e) {
            try {
                SleeContainer.getTransactionManager().setRollbackOnly();
                this.trashObject();
            } catch (Exception e2) {
                throw new RuntimeException("Transaction Failure.", e2);
            }
        }
        
        // remove all entities in child relations
        for (GetChildRelationMethod getChildRelationMethod : this.sbbComponent.getChildRelationMethods()) {
        	// (re)create child relation obj
        	ChildRelationImpl childRelationImpl = new ChildRelationImpl(getChildRelationMethod,this);
        	// iterate all sbb entities in this child relation
        	for (Iterator i = childRelationImpl.getSbbEntitySet().iterator(); i.hasNext();) {
        		String childSbbEntityID = (String) i.next();
        		// recreated the sbb entity and remove it
        		SbbEntityFactory.removeSbbEntity(childSbbEntityID,false);
        	}
        	// remove the child relation
        	childRelationImpl.remove();
        }
        
        // remove entity data from cache
        removeFromCache();
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
    	if (this.sbbID == null) {
    		this.sbbID = (SbbID) getObjectFromCache(SBB_ID);
    	}
    	return sbbID;
    }

    private void setSbbId(SbbID sbbId) {
    	putObjectInCache(SBB_ID, sbbId);
    	this.sbbID = sbbId;
    }

    public String getUsageParameterPathName(String name) {
        return Service.getUsageParametersPathName(getServiceId(), getSbbId(),
                name);
    }

    public String getUsageParameterPathName() {
        return Service.getUsageParametersPathName(getServiceId(), getSbbId());
    }

    public SleeEvent getCurrentEvent() {
        return this.currentEvent;
    }

    public void setCurrentEvent(SleeEvent sleeEvent) {
        this.currentEvent = sleeEvent;
    }

	/**
	 * 
	 * see JSLEE 1.0 spec, section 8.4.2 "SBB abstract class event handler methods"
	 * 
	 */
    private Method getEventHandlerMethod(SleeEvent sleeEvent) {

        EventTypeID eventType = sleeEvent.getEventTypeID();
        MobicentsSbbDescriptor sbbDescriptor = this.getSbbDescriptor();
        // Note -- this naming convention is part of the slee specification.
        String methodName = "on" + sbbDescriptor.getEventName(eventType);

        Class concreteClass = sbbDescriptor.getConcreteSbbClass();
        
        if (log.isDebugEnabled()) {
            log.debug("invoking event handler " + methodName + " on "
                    + concreteClass.getName() + " ID " + sbbDescriptor.getID()
                    + " sbbEntity " + this + " currentEvent " + sleeEvent);
        }

        Class[] args = new Class[2];
        MobicentsEventTypeDescriptor eventDescriptor = SleeContainer.lookupFromJndi()
                .getEventDescriptor(sleeEvent.getEventTypeID());
        if (log.isDebugEnabled()) {
            log.debug("EventType ID" + sleeEvent.getEventTypeID());
            log.debug("EventDescriptor ID"
                    + SleeContainer.lookupFromJndi().getEventDescriptor(sleeEvent
                            .getEventTypeID()));
        }
        // Once an error has been seen, we fire no more event handler
        // methods.

        ClassLoader ccl = SleeContainerUtils.getCurrentThreadClassLoader();
        try {
            args[0] = ccl.loadClass(eventDescriptor.getEventClassName());
        } catch (ClassNotFoundException e) {
            String s = "Caught ClassNotFoundException in loading class";
            log.error(s, e);
            throw new RuntimeException(s, e);
        }
        if (log.isDebugEnabled()) {
            log.debug("event className is " + eventDescriptor.getEventClassName());
            log.debug("event class is ARGS[0] of the event handler: args[0] == " + args[0]);
        }

        // Signature has to match correctly with the invoked method.

        Method method = null;
    	boolean isCustomAciMethod = false;
    	// Is there a custom SBB activity context interface. 
        String customAciName = sbbDescriptor.getActivityContextInterfaceClassName();
        if ( customAciName != null ) { 
        	// since there is a custom SBB ACI declared, see if there is an event handler with it in the signature
            try {
            	args[1] = ccl.loadClass( customAciName );
            } catch (ClassNotFoundException e) {
                String s = "Caught ClassNotFoundException while attempting to check for event handler signature with custom SBB ACI.";
                log.error(s, e);
                throw new RuntimeException(s, e);
            }
        	try {
        		method = concreteClass.getMethod(methodName, args);
        		isCustomAciMethod = true;
            } catch (NoSuchMethodException e) {
                String s = "Caught NoSuchMethodException in loading class. There is no event handler with custom SBB ACI argument";
                if (log.isDebugEnabled()) {
                	log.debug(s, e);
                };
            }
        }
        if (!isCustomAciMethod) {
        	// since there is no event handler with custom SBB ACI, let's look for a handler with generic ACI argument
            try {
            	// since there is no event handler with custom SBB ACI, let's look for a handler with generic ACI argument
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
     * @param sleeEvent to be delivered to the SBB
     * @return arguments that will be passed to the SBB event handler method
     */
    private Object[] getEventHandlerParameters(SleeEvent sleeEvent) {

        Object[] parameters = new Object[2];
        if (log.isDebugEnabled()) {
            log.debug("parameter [0] "
                    + sleeEvent.getEventObject().getClass().getName());
        }
        parameters[0] = sleeEvent.getEventObject();

        if (log.isDebugEnabled()) {
            log.debug("**PARAMETER 0 IS:" + parameters[0]);
            log.debug("**PARAM 0 class is:"
                    + parameters[0].getClass().getName());
        }
        ActivityContextInterface activityContextInterface = null;

        if (this.getSbbDescriptor().getActivityContextInterface() != null) {
            ActivityContextInterfaceImpl aciImpl = new ActivityContextInterfaceImpl(sleeEvent.getActivityContextID());
            Class aciClass = this.getSbbDescriptor()
                    .getActivityContextInterfaceConcreteClass();
            try {
                //activityContextInterface = (ActivityContextInterface) aciClass.getConstructor(new Class[] { aciImpl.getClass(),this.getSbbDescriptor().getClass() }).newInstance(new Object[] { aciImpl, this.getSbbDescriptor() });
            	activityContextInterface = (ActivityContextInterface) aciClass.getConstructor(new Class[] { aciImpl.getClass(),org.mobicents.slee.container.component.MobicentsSbbDescriptor.class }).newInstance(new Object[] { aciImpl, this.getSbbDescriptor() });
            } catch (Exception e) {
                String s = "Could Not create ACI!";
                //log.error(s, e);
                throw new RuntimeException(s, e);
            }

        } else {
            activityContextInterface = new ActivityContextInterfaceImpl(sleeEvent.getActivityContextID());
        }

        // Stow this information away in case we have to call sbbExceptionThrown
        sleeEvent.setActivityContextInterface(activityContextInterface);

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
    	SleeContainer.getTransactionManager()
    	.putTxLocalData(ServiceActivityFactoryImpl.TXLOCALDATA_SERVICEID_KEY,
    	 getServiceId());
    }

    /**
     * Actually invoke the event handler.
     *  
     */
    public void invokeEventHandler(SleeEvent sleeEvent) throws Exception {

        //Actually invoke the event handler.
        Method method = getEventHandlerMethod(sleeEvent);
        setServiceActivityFactory();
        Object[] parameters = getEventHandlerParameters(sleeEvent);
       
        if (log.isDebugEnabled()) {
            log.debug(this.sbbeId+" sbb entity invoking event handler:" + method);
        }
        
        try {
            this.transaction = SleeContainer.getTransactionManager()
                    .getTransaction();

            method.invoke(this.sbbObject.getSbbConcrete(), parameters);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            //Remember the actual exception is hidden inside the
            //InvocationTarget exception when you use reflection!
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
        if (log.isDebugEnabled()) {
            log.debug(this.sbbeId+" sbb entity invoked event handler:" + method);
        }
    }

    /**
     * Assigns the sbb entity to a sbb object, and then invoke sbbActivate()
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
     * Assigns the sbb entity to a sbb object, and then invoke sbbCreate() and sbbPostCreate()
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
     * @throws Exception
     */
    public void passivateAndReleaseSbbObject() throws Exception {
    	this.sbbObject.sbbPassivate();
    	this.sbbObject.setState(SbbObjectState.POOLED);
    	this.sbbObject.setSbbEntity(null);
    	this.sbbObject.setServiceID(null);
    	this.pool.returnObject(this.sbbObject);
    	this.sbbObject = null;
    	for (Iterator<SbbEntity>i=childsWithSbbObjects.iterator();i.hasNext();) {
    		SbbEntity childSbbEntity = i.next();
    		if (childSbbEntity.getSbbObject() != null) {
    			childSbbEntity.passivateAndReleaseSbbObject();
    		}
    		i.remove();
    	}
    	if (log.isDebugEnabled()) {
    		log.debug("releaseObject: Returned SbbObject to the Pool!");
    	}
    }

    /**
     * Invoke sbbRemove() and then release the sbb object from the entity
     * @throws Exception
     */
    public void removeAndReleaseSbbObject() throws Exception {        
    	this.sbbObject.sbbRemove();
    	this.sbbObject.setState(SbbObjectState.POOLED);
    	this.sbbObject.setSbbEntity(null);
    	this.sbbObject.setServiceID(null);
    	this.pool.returnObject(this.sbbObject);
    	this.sbbObject = null;
    	for (Iterator<SbbEntity>i=childsWithSbbObjects.iterator();i.hasNext();) {
    		SbbEntity childSbbEntity = i.next();
    		if (childSbbEntity.getSbbObject() != null) {
    			childSbbEntity.removeAndReleaseSbbObject();
    		}
    		i.remove();
    	}
    	if (log.isDebugEnabled()) {
    		log
    		.debug("releaseObject: Removing Entity Returned SbbObject to the Pool!");
    	}
    }

    public ObjectPool getObjectPool() {
        return this.pool;
    }

    public SbbObject getSbbObject() {
        return this.sbbObject;
    }

    public boolean checkAttached(String acId) {
        return this.getActivityContexts().contains(acId);
    }

    public Object getDefaultSbbUsageParameterSet() {
        return Service.getDefaultUsageParameterSet(getServiceId(), getSbbId());
    }

    public Object getSbbUsageParameterSet(String name) {
        return Service.getNamedUsageParameter(getServiceId(), getSbbId(), name);
    }

    public MobicentsSbbDescriptor getSbbDescriptor() {
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
    	
    	GetChildRelationMethod getChildRelationMethod = null;
    	// get the child relation metod from the sbb component
    	if ((getChildRelationMethod = this.sbbComponent.getChildRelationMethod(accessorName)) != null) {
    		// this is a valid name of a child relation for this entity
    		return new ChildRelationImpl(getChildRelationMethod,this);
    	}
    	else {
    		// invalid child relation name
    		log.warn("Sbb entity "+this.getSbbEntityId()+" can't get the child relation named "+accessorName+". Does not exist such a relation in sbb component with id "+this.getSbbId());
    		return null;
    	}
    }

    public void asSbbActivityContextInterface(ActivityContextInterface aci) {
        try {
            ActivityContextInterfaceImpl aciImpl = (ActivityContextInterfaceImpl) aci;
            Class aciclass = this.getSbbDescriptor()
                    .getActivityContextInterfaceConcreteClass();
            if (aciclass != null) {

                Class[] argTypes = new Class[] { aciImpl.getClass(),
                        MobicentsSbbDescriptor.class };
                Constructor cons = aciclass.getConstructor(argTypes);
                Object retval = cons.newInstance(new Object[] { aciImpl,
                        this.getSbbDescriptor() });
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
    		return ((SbbEntity)obj).sbbeId.equals(this.sbbeId);
    	}
    	else {
    		return false;
    	}       
    }
    
    @Override
    public int hashCode() {
    	return sbbeId.hashCode();
    }

	public void checkReEntrant() throws SLEEException {
        try {
            if ((!this.getSbbDescriptor().isReentrant())
                    && this.transaction == SleeContainer
                            .getTransactionManager().getTransaction())
                throw new SLEEException(" re-entrancy not allowed ");
        } catch (SystemException ex) {
            throw new RuntimeException("Transaction Manager exception while checkReEntrant!",ex);
        }
    }

    public SbbLocalObjectImpl createSbbLocalObject() {
        Class sbbLocalClass;
        MobicentsSbbDescriptor sbbDescriptor = this.getSbbDescriptor();
        if (log.isDebugEnabled())
            log
                    .debug("createSbbLocalObject "
                            + this.getSbbDescriptor().getID());

        // The concrete class generated in ConcreteLocalObjectGenerator
        if ((sbbLocalClass = sbbDescriptor.getLocalInterfaceConcreteClass()) != null) {
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
        return isBeingRemoved;
    }

   
    /**
     * Remove entity from cache.
     */
    private void removeFromCache() {
    	
    	if (log.isDebugEnabled()) {
    		log.debug("removing sbb entity "+sbbeId+" from cache");
    	}
    	
        cmpFields.remove();
        eventMask.remove();
        cachedSbbEntityAttributes.remove();
        sbbLocalObjectCmpFields.remove();
        attachedActivityContexts.remove();
        isBeingRemoved = true;
        
    }

	/**
	 * Retreives the name of the child relation of the parent this sbb entity
	 * belongs.
	 * 
	 * @return
	 */
	public String getParentChildRelation() {
		if (parentChildRelation == null) {
			parentChildRelation = (String) getObjectFromCache(PARENT_CHILD_RELATION); 
		}
		return parentChildRelation;
	}

	/**
	 * Sets the parent child relation name.
	 * @param name
	 */
	private void setParentChildRelation(String parentChildRelation) {
		putObjectInCache(PARENT_CHILD_RELATION, parentChildRelation);
		this.parentChildRelation = parentChildRelation;
	}
	
	/**
	 * Retreives the id of the parent sbb entity.
	 * 
	 * @return
	 */
	public String getParentSbbEntityId() {
		if (parentSbbEID == null) {
			parentSbbEID = (String) getObjectFromCache(PARENT_SBB_ENTITY_ID);
		}
		return parentSbbEID;
	}

	/**
	 * Sets the parent sbb entity id.
	 * @param name
	 */
	private void setParentSbbEntityId(String parentSbbEntityId) {
		putObjectInCache(PARENT_SBB_ENTITY_ID, parentSbbEntityId);
		this.parentSbbEID = parentSbbEntityId;
	}
	
    // It removes the SBB entity from the ChildRelation object that the SBB
    // entity belongs
    //	to.
    private void removeFromParent() throws TransactionRequiredException,
            SystemException {       

    	if (log.isDebugEnabled()) {
    		log.debug("Removing sbb entity "+this.getSbbEntityId()+ " from parent "+this.getParentSbbEntityId());
    	}

    	if (this.getParentSbbEntityId() != null) {
    		SbbEntityFactory.getSbbEntity(this.getParentSbbEntityId()).
    			getChildRelation(getParentChildRelation()).getSbbEntitySet().remove(this.getSbbEntityId());
    	}
    	else {
    		// it's a root sbb entity, remove from service 
    		try {
				SleeContainer.lookupFromJndi().getService(this.getServiceId()).removeConvergenceName(this.getServiceConvergenceName());
			} catch (Exception e) {
				log.info("Failed to remove the root sbb entity "+this.sbbeId+" with convergence name "+this.getServiceConvergenceName()+" from the service "+this.getServiceId(), e);
			}
    	}
    }
    
    private HashSet<SbbEntity> childsWithSbbObjects = new HashSet<SbbEntity>();
    
    protected void addChildWithSbbObject(SbbEntity childSbbEntity) {
    	childsWithSbbObjects.add(childSbbEntity);
    }
    
}

