/*
 * Created on Jul 14, 2004
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project, or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime.sbbentity;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javax.slee.ChildRelation;
import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.GetChildRelationMethod;
import org.mobicents.slee.runtime.cache.CacheableSet;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete;
import org.mobicents.slee.runtime.sbb.SbbLocalObjectImpl;

/**
 * 
 * Implements the ChildRelation interface
 * 
 * @author F.Moggia
 * @author M. Ranganathan
 * @author Ralf Siedow
 * @author eduardomartins
 * 
 *  
 */
public class ChildRelationImpl implements ChildRelation, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger logger = Logger.getLogger(ChildRelationImpl.class);
	
    private CacheableSet children = null;
    private GetChildRelationMethod getChildRelationMethod;    
    private SbbEntity sbbEntity;

    private HashSet<SbbLocalObject> getLocalObjects() {
        HashSet<SbbLocalObject> localObjects = new HashSet<SbbLocalObject>();
        for (Iterator it = this.children.iterator(); it.hasNext();) {
            String sbbEntityId = (String) it.next();
            localObjects.add(SbbEntityFactory.getSbbEntity(sbbEntityId).createSbbLocalObject());
        }
        return localObjects;
    }

    /*
     * JAIN SLEE 1.0 Specification, Final Release Page 62 The iterator method
     * and the Iterator objects returned by this method. Chapter 6 The SBB
     * Abstract Class
     * 
     * The SLEE must implement the methods of the Iterator objects returned by
     * the iterator method by following the contract defined by the
     * java.util.Iterator interface. o The next method of the Iterator object
     * returns an object that implements the SBB local interface of the child
     * SBB of the child relation. Java typecast can be used to narrow the
     * returned object reference to the SBB local interface of the child SBB. o
     * The remove method of the Iterator object removes the SBB entity that is
     * represented by the last SBB local object returned by the next method of
     * the Iterator object from the child relation. Removing an SBB entity from
     * a child relation by invoking this remove method initiates a cascading
     * removal of the SBB entity tree rooted by the SBB entity, similar to
     * invoking the remove method on an SBB local object that represents the SBB
     * entity. The behavior of the Iterator object is unspecified if the
     * underlying child relation is modified while the iteration is in progress
     * in any way other than by calling this remove method.
     */
    class ChildRelationIterator implements Iterator {

        private Iterator myIterator;
        private String nextEntity;
        private SbbLocalObject nextSbbLocalObject;
        
        public ChildRelationIterator() {
        	if(logger.isDebugEnabled()){
        		logger.debug("ChildRelationIterator() for child relation with name "+getChildRelationMethod.getMethodName()+" of entity "+sbbEntity.getSbbEntityId());
        	}
        	this.myIterator = children.iterator();
        }
      
        /*
         * public ChildRelationIterator(HashSet entities) { this.sbbEntities =
         * new HashSet(); for (Iterator it = entities.iterator(); it.hasNext(); ) {
         * SbbEntity sbbe = (SbbEntity) it.next(); SbbLocalObjectImpl
         * sbbLocalObject = (SbbLocalObjectImpl) sbbe.getSbbLocalObject(); if (!
         * sbbLocalObject.isRemoved()) this.sbbEntities.add(sbbe); }
         * 
         * this.myIterator = this.sbbEntities.iterator(); }
         */        
        public void remove() {
        	if(logger.isDebugEnabled()) {
        		logger.debug("ChildRelationIterator.remove() for child relation with name "+getChildRelationMethod.getMethodName()+" of entity "+sbbEntity.getSbbEntityId());
        	}
            myIterator.remove();
            nextSbbLocalObject.remove();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return myIterator.hasNext();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#next()
         */
        public Object next() {
            nextEntity = (String) myIterator.next();
            SbbEntity childSbbEntity = SbbEntityFactory.getSbbEntity(nextEntity);
            this.nextSbbLocalObject = childSbbEntity.createSbbLocalObject();
            sbbEntity.addChildWithSbbObject(childSbbEntity);
            return nextSbbLocalObject;
        }

    }

    /**
     * Creates a new instance of a child relation
     * @param getChildRelationMethod the child relation method
     * @param sbbEntity the sbb entity that owns this child relation
     */
    public ChildRelationImpl(GetChildRelationMethod getChildRelationMethod,
             SbbEntity sbbEntity) {
    	
        if (getChildRelationMethod == null)
            throw new NullPointerException("null getChildRelationMethod");
        
        this.sbbEntity = sbbEntity;
        this.getChildRelationMethod = getChildRelationMethod;
        this.children = new CacheableSet(sbbEntity.getSbbEntityId()+":childRelation:"+getChildRelationMethod.getMethodName());
    }

    public Iterator iterator() {
        return new ChildRelationIterator();
    }

    /**
     * The contains method. This method returns true if the SBB entity
     * represented by the SBB local object specified by the input argument is a
     * member of this child relation. If the method argument is not an SBB local
     * object, is an invalid SBB local object, or is an SBB local object whose
     * underlying SBB entity is not a member of this child relation, then this
     * method returns false.
     *  
     */
    public boolean contains(Object object) {
        if (!(object instanceof SbbLocalObject))
            return false;
        SbbLocalObjectImpl sbblocal = (SbbLocalObjectImpl) object;
        String sbbEntityId = sbblocal.getSbbEntityId();
        return this.children.contains(sbbEntityId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.ChildRelation#create()
     */
    public SbbLocalObject create() throws CreateException,
            TransactionRequiredLocalException, SLEEException {
        
    	SleeContainer.getTransactionManager().mandateTransaction();

        SbbEntity childSbbEntity = SbbEntityFactory.createSbbEntity(
						this.getChildRelationMethod.getSbbID(),
						this.sbbEntity.getServiceId(),
						this.sbbEntity.getSbbEntityId(),
						this.getChildRelationMethod.getMethodName(),
						this.sbbEntity.getRootSbbId(),
						this.sbbEntity.getServiceConvergenceName());
      
        if (logger.isDebugEnabled())
            logger.debug("ChildRelation.create() : Created Sbb Entity: " + childSbbEntity.getSbbEntityId());

        childSbbEntity.setPriority(getChildRelationMethod.getDefaultPriority());
        
        /*
         * Exception handling here must follow Sec. 9.12.1 of spec
         * This is a non-slee originated method invocation
         * 
         * If a non-SLEE originated method invocation returns by throwing a checked exception, then the following
		 *	occurs:
		 *	� The state of the transaction is unaffected
		 *	� The checked exception is propagated to the caller.
		 *	It is expected that the caller will have the appropriate logic to handle the exception.
		 *	If a non-SLEE originated method invocation returns by throwing a RuntimeException, then:
		 *	� The transaction is marked for rollback.
		 *	� The SBB object that was invoked is discarded, i.e. is moved to the Does Not Exist state.
		 *	� A javax.slee.TransactionRolledBackLocalException is propagated to the caller.
		 *	The transaction will eventually be rolled back when the highest level SLEE originated invocation re-turns
		 *	as described in Section 9.12.2.
		 *	The sbbRolledBack method is not invoked for an SBB originated method transaction because the
		 *	transa ction is only marked for rollback and will only be rolled back when the highest level SLEE
		 *	originated invocation returns. The sbbRolledBack method is only invoked on the SBB entity in-voked
		 *	by the highest level SLEE originated invocation.
		 *	If the RuntimeException propagates to the highest level (i.e. the SLEE originated method invocation
		 *	returns by throwing a RuntimeException) the SLEE originated method invocation exception handling
		 *	mechanism is init iated.		
		*/
        
        try {            
        	//All checked exceptions (i.e. CreateException) are propagated to the caller
            if(childSbbEntity.getSbbObject() == null){
            	childSbbEntity.assignAndCreateSbbObject();
            }    	        	
        	
        } catch ( CreateException e) {
            //          All RuntimeExceptions are dealt with here
            if ( logger.isDebugEnabled())
                logger.error("Caught CreateException in creating child entity", e);
            
            childSbbEntity.trashObject();
            // Propagate exception to caller.
            throw e;
        } catch ( Exception e) {
        	//All RuntimeExceptions are dealt with here
            logger.error("Caught RuntimeException in creating child entity", e);
            try {
                SleeContainer.getTransactionManager().setRollbackOnly();
            } catch (SystemException e1) {
            	logger.error("Failed to set rollbackonly", e);
            }
            childSbbEntity.trashObject();
        } 
        
        children.add(childSbbEntity.getSbbEntityId()); // Ralf Siedow: added this
        sbbEntity.addChildWithSbbObject(childSbbEntity);
        return childSbbEntity.createSbbLocalObject();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#size()
     */
    public int size() {
        return this.children.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#clear()
     */
    public void clear() {
    	SleeContainer.getTransactionManager().mandateTransaction();
        for ( Iterator it = this.iterator(); it.hasNext() ; ) {
            it.next();
            it.remove();
        }
        

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#isEmpty()
     */
    public boolean isEmpty() {
        return !this.iterator().hasNext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {

    	SleeContainer.getTransactionManager().mandateTransaction();

        return this.getLocalObjects().toArray();
    }

    /**
     * This operation is not supported ( see page 62 ) of the spec. The add
     * methods add and addAll. Any attempts to add to the child relation using
     * these methods result in a java.lang. UnsupportedOperationException. For
     * example, the SLEE must throw the UnsupportedOperationException when these
     * methods are invoked with arguments that add to the collection. o An
     * invocation of an add method that has no effect on the collection, such as
     * invoking the addAll method with an empty collection may or may not throw
     * an UnsupportedOperationException . The create method of the ChildRelation
     * interface should be used to create a child SBB entity. This causes the
     * SLEE to automatically add an SBB local object that represents the newly
     * created SBB entity to the collection.
     */
    public boolean add(Object object) {
        if (object == null)
            throw new NullPointerException("null arg! ");
        else
            throw new UnsupportedOperationException("Operation not supported !");
    }

    /**
     * Spec page 62 The remove methods: clear, remove, removeAll, and retainAll.
     * o These methods may remove SBB entities from the child relation. The
     * input argument specifies which SBB entities will be removed from the
     * child relation or retained in the child relation by specifying the SBB
     * local object or collection of SBB local objects that represent the SBB
     * entities to be removed or retained. o Removing an SBB entity from a child
     * relation initiates a cascading removal of the SBB entity tree rooted by
     * the SBB entity, similar to invoking the remove method on an SBB local
     * object that represents the SBB entity.
     *  
     */
    public boolean remove(Object object) {
    	SleeContainer.getTransactionManager().mandateTransaction();
        if(logger.isDebugEnabled()) {
        	logger.debug("removing sbb local object " + object);
        }
        if (object == null)
            throw new NullPointerException("Null arg for remove ");
        if (!(object instanceof SbbLocalObject))
            return false;
        String target = ((SbbLocalObjectImpl)object).getSbbEntityId();
        boolean retval = this.children.remove(target);
        if (retval) {
            ((SbbLocalObjectImpl) object).remove();
            retval = false;
        }
        return retval;
    }

    public void remove() {
    	if(logger.isDebugEnabled()) {
    		logger.debug("Removing child relation of sbb entity "+this.sbbEntity.getSbbEntityId()+" with name "+this.getChildRelationMethod.getMethodName());
    	}
    	children.remove();
    }
    
    /**
     * This operation is not supported ( see page 62 ) of the spec. The add
     * methods add and addAll. Any attempts to add to the child relation using
     * these methods result in a java.lang. UnsupportedOperationException. For
     * example, the SLEE must throw the UnsupportedOperationException when these
     * methods are invoked with arguments that add to the collection. o An
     * invocation of an add method that has no effect on the collection, such as
     * invoking the addAll method with an empty collection may or may not throw
     * an UnsupportedOperationException . The create method of the ChildRelation
     * interface should be used to create a child SBB entity. This causes the
     * SLEE to automatically add an SBB local object that represents the newly
     * created SBB entity to the collection.
     */
    public boolean addAll(Collection c) {
        if (c == null)
            throw new NullPointerException("Null arg!");
        else
            throw new UnsupportedOperationException("Operation not supported !");
    }

    /**
     * This method returns true if all SBB entities represented by the SBB local
     * objects in the collection specified by the input argument are members of
     * this child relation. If the collection contains an object that is not an
     * SBB local object, an SBB local object that is invalid, or an SBB local
     * object whose underlying SBB entity is not a member of this child
     * relation, then this method returns false.
     */
    public boolean containsAll(Collection c) {
        if (c == null)
            throw new NullPointerException("null collection!");
        HashSet localInterfaces = getLocalObjects();
        if(logger.isDebugEnabled()) {
        	logger.debug("containsAll : collection = " + c + " sbbLocalObject = " + localInterfaces);
        }
        boolean retval  = true;
        for ( Iterator it = c.iterator(); it.hasNext(); ) {
            SbbLocalObjectConcrete sbbLocalInterface = ( SbbLocalObjectConcrete) it.next();
            if(logger.isDebugEnabled()) {
            	logger.debug("Checking membership for : "  + sbbLocalInterface.getSbbEntityId());
            }
            if ( ! children.contains(sbbLocalInterface.getSbbEntityId())) {
                retval = false;
                break;
            }
        }
        
        return retval;
    }

    /**
     * Removing an SBB entity from a child relation initiates a cascading
     * removal of the SBB entity tree rooted by the SBB entity, similar to
     * invoking the remove method on an SBB local object that represents the SBB
     * entity.
     *  
     */
    public boolean removeAll(Collection c) {
        boolean flag = true;
        if (c == null)
            throw new NullPointerException(" null collection ! ");
        for ( Iterator it = c.iterator(); it.hasNext();	) {
           flag &= this.remove( it.next());
            
        }
        return flag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection c) {
        boolean flag = false;
        if (c == null)
            throw new NullPointerException(" null arg! ");
        for ( Iterator it = this.iterator(); it.hasNext();) {
            if ( ! c.contains(it.next())) {
                flag = true;
                it.remove();
            }
            
        }
        return flag;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#toArray(java.lang.Object[])
     */
    public Object[] toArray(Object[] a) {
        if (a == null)
            throw new NullPointerException("null arg!");
        HashSet localObjects = this.getLocalObjects();
        return localObjects.toArray(a);
    }
     
     public CacheableSet getSbbEntitySet(){
         return this.children;
     }
     
}