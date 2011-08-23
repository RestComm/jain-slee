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

/*
 * Created on Jul 14, 2004
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project, or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.runtime.sbbentity;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.sbbentity.ChildRelation;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
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
public class ChildRelationImpl implements ChildRelation {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(ChildRelationImpl.class);
	
	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	private GetChildRelationMethodDescriptor getChildRelationMethod;    
    private SbbEntityImpl sbbEntity;
    
    private HashSet<SbbLocalObject> getLocalObjects() {
        HashSet<SbbLocalObject> localObjects = new HashSet<SbbLocalObject>();
        SbbEntity childSbbEntity = null;
        for (SbbEntityID sbbEntityId : sbbEntity.cacheData.getChildRelationSbbEntities(getChildRelationMethod.getChildRelationMethodName())) {
            childSbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity(sbbEntityId, false);
        	if (childSbbEntity != null) {
        		localObjects.add(childSbbEntity.getSbbLocalObject());
        	}
        }
        return localObjects;
    }

    /**
     * Creates a new instance of a child relation
     * @param getChildRelationMethod the child relation method
     * @param sbbEntity the sbb entity that owns this child relation
     */
    public ChildRelationImpl(GetChildRelationMethodDescriptor getChildRelationMethod,
             SbbEntityImpl sbbEntity) {
    	
        if (getChildRelationMethod == null)
            throw new NullPointerException("null getChildRelationMethod");
        
        this.sbbEntity = sbbEntity;
        this.getChildRelationMethod = getChildRelationMethod;        
    }

    @SuppressWarnings("rawtypes")
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
        
    	final SbbLocalObjectImpl sbblocal = (SbbLocalObjectImpl) object;
        final SbbEntityID sbbEntityId = sbblocal.getSbbEntityId();
        if(!idBelongsToChildRelation(sbbEntityId)) {
        	return false;
        }
        
        return new SbbEntityCacheData(sbbEntityId,sleeContainer.getCluster().getMobicentsCache()).exists();
    }

    private boolean idBelongsToChildRelation(SbbEntityID sbbEntityID) {
    	 if(sbbEntityID.isRootSbbEntity()) {
         	return false;
         }
         if(!sbbEntityID.getParentChildRelation().equals(getChildRelationMethod.getChildRelationMethodName())) {
         	return false;
         }
         if(!sbbEntityID.getParentSBBEntityID().equals(sbbEntity.getSbbEntityId())) {
         	return false;
         }
         return true;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.ChildRelation#create()
     */
    public SbbLocalObject create() throws CreateException,
            TransactionRequiredLocalException, SLEEException {        
    	return create(sleeContainer.getUuidGenerator().createUUID());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#size()
     */
    public int size() {
        return sbbEntity.cacheData.getChildRelationSbbEntities(getChildRelationMethod.getChildRelationMethodName()).size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#clear()
     */
    @SuppressWarnings("rawtypes")
	public void clear() {
    	sleeContainer.getTransactionManager().mandateTransaction();
    	for (Iterator it = iterator();it.hasNext();) {
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
        return size() == 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Collection#toArray()
     */
    public Object[] toArray() {

    	sleeContainer.getTransactionManager().mandateTransaction();

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
    	
    	sleeContainer.getTransactionManager().mandateTransaction();
        
    	if(logger.isDebugEnabled()) {
        	logger.debug("removing sbb local object " + object);
        }
        
        if (object == null)
            throw new NullPointerException("Null arg for remove ");
        
        if (!(object instanceof SbbLocalObject))
            return false;
        
    	final SbbLocalObjectImpl sbbLocalObjectImpl = (SbbLocalObjectImpl) object;
        if(!idBelongsToChildRelation(sbbLocalObjectImpl.getSbbEntityId()) && !sbbLocalObjectImpl.getSbbEntity().isRemoved()) {
        	return false;
        }
        else {
        	sbbLocalObjectImpl.remove();
        	return true;
        }
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
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
    public boolean containsAll(Collection c) {
        
    	if (c == null)
            throw new NullPointerException("null collection!");
        
        for (Iterator it = c.iterator(); it.hasNext(); ) {
            if (!contains(it.next())) {                        	
            	return false;
            }
        }
        
        if(logger.isDebugEnabled()) {
        	logger.debug("containsAll : collection = " + c + " > all in child relation");
        }
        return true;
    }

    /**
     * Removing an SBB entity from a child relation initiates a cascading
     * removal of the SBB entity tree rooted by the SBB entity, similar to
     * invoking the remove method on an SBB local object that represents the SBB
     * entity.
     *  
     */
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object[] toArray(Object[] a) {
        if (a == null)
            throw new NullPointerException("null arg!");
        HashSet localObjects = this.getLocalObjects();
        return localObjects.toArray(a);
    }
     
    public Set<SbbEntityID> getSbbEntitySet(){
    	return new HashSet<SbbEntityID>(sbbEntity.cacheData.getChildRelationSbbEntities(getChildRelationMethod.getChildRelationMethodName()));
    }
    
	// --- ITERATOR
	
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
    @SuppressWarnings("rawtypes")
    class ChildRelationIterator implements Iterator {

        private Iterator<SbbEntityID> myIterator;
        private SbbEntityID nextEntity;
        private SbbLocalObject nextSbbLocalObject;
        
        public ChildRelationIterator() {        	        	
        	this.myIterator = getSbbEntitySet().iterator();
        }
      
        public void remove() {        
        	if (nextSbbLocalObject != null) {
        		myIterator.remove();
            	nextSbbLocalObject.remove();
        	}
        	else {
        		throw new IllegalStateException();
        	}
        }

        public boolean hasNext() {
            return myIterator.hasNext();
        }
        
        public Object next() {
            nextEntity = myIterator.next();
            SbbEntity childSbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity(nextEntity, false);
            if (childSbbEntity != null) {
            	this.nextSbbLocalObject =  childSbbEntity.getSbbLocalObject();
            	sbbEntity.addChildWithSbbObject(childSbbEntity);
            	return nextSbbLocalObject;
            }
            else {
            	return next();
            }            
        }

    }
    
    // extension methods
    
    private void validateChildName(String childName) throws IllegalArgumentException, NullPointerException {
    	if (childName == null) {
    		throw new NullPointerException("null child name"); 
    	}
    	if (childName.isEmpty()) {
    		throw new IllegalArgumentException("empty child name");
    	}
    }
    
	@Override
	public SbbLocalObjectExt create(String childName) throws CreateException,
			IllegalArgumentException, NullPointerException,
			TransactionRequiredLocalException, SLEEException {

		if (logger.isDebugEnabled())
			logger.debug("Creating child "+childName+" from relation "+getChildRelationMethod.getChildRelationMethodName()+" of entity "+sbbEntity.getSbbEntityId());
		
		validateChildName(childName);

		final SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();

		sleeTransactionManager.mandateTransaction();

		final SbbEntity childSbbEntity = sleeContainer.getSbbEntityFactory()
				.createNonRootSbbEntity(sbbEntity.getSbbEntityId(),
						getChildRelationMethod.getChildRelationMethodName(),
						childName);

		if (logger.isDebugEnabled())
			logger.debug("Created child Sbb Entity: "
					+ childSbbEntity.getSbbEntityId());

		childSbbEntity.setPriority(getChildRelationMethod.getDefaultPriority());

		/*
		 * Exception handling here must follow Sec. 9.12.1 of spec This is a
		 * non-slee originated method invocation
		 * 
		 * If a non-SLEE originated method invocation returns by throwing a
		 * checked exception, then the following occurs: � The state of the
		 * transaction is unaffected � The checked exception is propagated to
		 * the caller. It is expected that the caller will have the appropriate
		 * logic to handle the exception. If a non-SLEE originated method
		 * invocation returns by throwing a RuntimeException, then: � The
		 * transaction is marked for rollback. � The SBB object that was invoked
		 * is discarded, i.e. is moved to the Does Not Exist state. � A
		 * javax.slee.TransactionRolledBackLocalException is propagated to the
		 * caller. The transaction will eventually be rolled back when the
		 * highest level SLEE originated invocation re-turns as described in
		 * Section 9.12.2. The sbbRolledBack method is not invoked for an SBB
		 * originated method transaction because the transa ction is only marked
		 * for rollback and will only be rolled back when the highest level SLEE
		 * originated invocation returns. The sbbRolledBack method is only
		 * invoked on the SBB entity in-voked by the highest level SLEE
		 * originated invocation. If the RuntimeException propagates to the
		 * highest level (i.e. the SLEE originated method invocation returns by
		 * throwing a RuntimeException) the SLEE originated method invocation
		 * exception handling mechanism is init iated.
		 */
		
		if (System.getSecurityManager()!=null) {
            try {
            	AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
                public Object run() throws CreateException {
                	assignSbbObjectToChild(childSbbEntity,sleeTransactionManager);
                    return null;
                }
            });
            }
            catch (PrivilegedActionException e) {
				final Throwable t = e.getCause();
				if (t instanceof CreateException) {
					throw (CreateException) t;
				}
				else {
					throw new SLEEException(t.getMessage(),t);
				}
			}
    	}
        else {
        	assignSbbObjectToChild(childSbbEntity,sleeTransactionManager);
        }
    	sbbEntity.addChildWithSbbObject(childSbbEntity);
		return childSbbEntity.getSbbLocalObject();
	}

	private void assignSbbObjectToChild(final SbbEntity childSbbEntity, final SleeTransactionManager sleeTransactionManager) throws CreateException {
    	final Thread t = Thread.currentThread();
    	final ClassLoader cl = t.getContextClassLoader();
    	t.setContextClassLoader(childSbbEntity.getSbbComponent().getClassLoader());
    	try {
			// All checked exceptions (i.e. CreateException) are propagated to
			// the caller
			childSbbEntity.assignSbbObject();

		} catch (CreateException e) {
			// All RuntimeExceptions are dealt with here
			if (logger.isDebugEnabled())
				logger.error("Caught CreateException in creating child entity",
						e);

			childSbbEntity.trashObject();
			// Propagate exception to caller.
			throw e;
		} catch (Exception e) {
			// All RuntimeExceptions are dealt with here
			logger.error("Caught RuntimeException in creating child entity", e);
			try {
				sleeTransactionManager.setRollbackOnly();
			} catch (SystemException e1) {
				logger.error("Failed to set rollbackonly", e);
			}
			childSbbEntity.trashObject();
		}
        finally {
        	t.setContextClassLoader(cl);            
        }
    }
	
	@Override
	public SbbLocalObjectExt get(String childName)
			throws IllegalArgumentException, NullPointerException,
			TransactionRequiredLocalException, SLEEException {

		if (logger.isTraceEnabled())
			logger.trace("Retreiving child "+childName+" from relation "+getChildRelationMethod.getChildRelationMethodName()+" of entity "+sbbEntity.getSbbEntityId());
				
		validateChildName(childName);

		SleeTransactionManager sleeTransactionManager = sleeContainer
				.getTransactionManager();

		sleeTransactionManager.mandateTransaction();

		SbbEntity childSbbEntity = sleeContainer.getSbbEntityFactory()
				.getSbbEntity(
						new NonRootSbbEntityID(sbbEntity.getSbbEntityId(),
								getChildRelationMethod
										.getChildRelationMethodName(),
								childName), false);

		if (logger.isDebugEnabled())
			logger.debug("Retrieved child "+childName+" from relation "+getChildRelationMethod.getChildRelationMethodName()+" of entity "+sbbEntity.getSbbEntityId()+" -> "+childSbbEntity);
		
		
		return childSbbEntity == null ? null : childSbbEntity
				.getSbbLocalObject();
	}
}