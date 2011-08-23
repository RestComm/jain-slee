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

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.slee.ActivityContextInterface;
import javax.slee.NoSuchObjectLocalException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.SbbLocalObjectExt;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.interceptors.SbbLocalObjectInterceptor;
import org.mobicents.slee.container.eventrouter.EventRoutingTransactionData;
import org.mobicents.slee.container.sbb.SbbLocalObject;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;
import org.mobicents.slee.runtime.sbbentity.SbbEntityImpl;

/**
 * This is a SLEE provided interface to the Sbb object. The SLEE uses this
 * interface to allow sbbs to access local methods.
 * 
 * The following is excerpted from Slee Spec
 * <p>
 * An SBB may define SBB specific local interface methods in an SBB specific
 * local interface. The SBB specific local interface must be public and must
 * extend, either directly or indirectly, the SbbLocalObject interface. All SBBs
 * have an SBB local interface. If the SBB Developer does not provide an SBB
 * local interface for an SBB, then the SBB local interface of the SBB is the
 * generic SbbLocalObject interface. The names of the SBB specific local
 * interface methods must not begin with �sbb� or �ejb�. The SLEE provides the
 * implementation of the methods defined in the SBB local interface. More
 * precisely, the SLEE provides a concrete class that implements each SBB local
 * interface. An SBB local object is an instance of this class. The SLEE
 * provided implementations of these methods delegate invocations on an SBB
 * local object that represents an SBB entity to an SBB object that represents
 * the SBB entity (if the SBB entity has not been removed). The SBB Developer
 * provides the implementation of SBB Developer defined local interface methods
 * declared in the SBB specific extension of the SbbLocalObject interface in the
 * SBB abstract class. For each method defined by the SBB Developer, there must
 * be a matching method in the SBB abstract class. The matching method must
 * have:
 * <ul>
 * <li>The same name.
 * <li>The same number of arguments, and same argument and return types.
 * <li>The same set of exceptions in the throws clause.
 * </ul>
 * 
 * <p>
 * All SBB local objects that represent an SBB entity that does not exist are
 * invalid. An attempted invocation on an invalid SBB local object marks the
 * current transaction for rollback and throws a
 * javax.slee.TransactionRolledbackLocalException (a subclass of javax.slee.
 * SLEEException). An SBB Developer defined local interface method is a
 * mandatory transactional method (see Section 9.6.1). The SLEE throws a
 * javax.slee.TransactionRequiredLocalException if an SBB Developer defined
 * local interface method of an SBB local object is invoked without a valid
 * transaction context.
 * 
 * <p>
 * This method may also throw a javax.slee.SLEEException if the method failed
 * due to a SLEE level or system level failure. If the caller of this method
 * receives this exception, the caller does not know, in general, whether the
 * corresponding method implementation in the SBB abstract class was invoked.
 * The caller also does not know if the transaction has been marked for
 * rollback. However, the caller may determine the transaction status by using
 * the getRollbackOnly method (see Section 6.10.3).
 * <p>
 * The SBB Developer defined SBB local interface methods must not throw
 * java.rmi.Remote- Exception, any subclass of RemoteException, or any
 * RuntimeException. For more information on exception handling for SBB local
 * object invocations, see Section 6.9.
 * <p>
 * Parameters to local interface methods are passed by reference.
 * <p>
 * Note that the SbbLocalObject does not expose the methods of the
 * javax.slee.Sbb interface, event handler methods, or the various callback
 * methods. These methods are used by the SLEE to manage SBB object instances,
 * deliver events, and handle callbacks.
 * 
 * @author M. Ranganathan
 * @author F. Moggia
 * @author eduardomartins
 */
public class SbbLocalObjectImpl implements SbbLocalObject,
        SbbLocalObjectConcrete {

    /**
     * Container where this resides -- for now this is the same location where
     * the sbb resides.
     */
    private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

    private static final Logger logger = Logger.getLogger(SbbLocalObjectImpl.class);

    private boolean rollbackOnly;

    /*
     * This flag is set to true if the sbb local object is removed (no longer
     * valid)
     */
    private boolean isRemoved;

    private final SbbEntityImpl sbbEntity;

    private ClassLoader contextClassLoader;
    
    private final boolean trace;
    
    /**
     * used by class extensions to invoke custom sbb local object methods 
     */
    protected SbbLocalObjectInterceptor sbbLocalObjectInterceptor = new SbbLocalObjectInterceptor();
    
    /**
     * Constructor -- assume that the Sbb entity is co-located with the Sbb
     * local object.
     * 
     * @param sbbEntity --
     *            sbb entity for which this is a local object.
     */
    public SbbLocalObjectImpl(final SbbEntityImpl sbbEntity) {
        this.sbbEntity = sbbEntity;
        if (sbbEntity.getSbbObject() == null){
        	if (System.getSecurityManager()!=null) {
                AccessController.doPrivileged(new PrivilegedAction<Object>() {
                    public Object run() {
                    	assignSbbObject();
                        return null;
                    }
                });
        	}
            else {
            	assignSbbObject();
            }        	
        }
        trace = logger.isTraceEnabled();        
        if(trace)
            logger.trace("SbbLocalObjectImpl(sbbEntity = "+sbbEntity.getSbbEntityId()+" )");
    }

    private void assignSbbObject() {
    	final Thread t = Thread.currentThread();
    	final ClassLoader cl = t.getContextClassLoader();
    	t.setContextClassLoader(sbbEntity.getSbbComponent().getClassLoader());
    	try {
            sbbEntity.assignSbbObject();
        } catch (Exception e) {
           logger.error(e.getMessage(),e);
        }
        finally {
        	t.setContextClassLoader(cl);            
        }
    }
    
	/* (non-Javadoc)
	 * @see org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete#getContextClassLoader()
	 */
	public ClassLoader getContextClassLoader() {
		if (contextClassLoader == null) {
			contextClassLoader = sbbEntity.getSbbComponent().getClassLoader();
		}
		return contextClassLoader;
	}
	
	/**
	 * @return the sbbEntity
	 */
	public SbbEntityImpl getSbbEntity() {
		return sbbEntity;
	}
	
	/**
	 * Validates an invocation of the {@link SbbLocalObject}.
	 * 
	 * @throws TransactionRolledbackLocalException
	 * @throws NoSuchObjectLocalException
	 * @throws SLEEException
	 */
	private void validateInvocation() throws TransactionRolledbackLocalException, NoSuchObjectLocalException, SLEEException {
		// validate tx
		sleeContainer.getTransactionManager().mandateTransaction();
		// validate object
		if (this.rollbackOnly) {
			try {
				sleeContainer.getTransactionManager().setRollbackOnly();
			} catch (SystemException ex) {
				throw new SLEEException("unable to set rollbackOnly in transaction manager", ex);
			}
			throw new TransactionRolledbackLocalException(
			"Unable to proceed, transaction is set to rollback");
		}   	
		if (this.isRemoved)
			throw new NoSuchObjectLocalException("sbb local object is removed");
	}

	/*
	 * (non-Javadoc)
	 * @see javax.slee.SbbLocalObject#getSbbPriority()
	 */
    public byte getSbbPriority() throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException { 
    	validateInvocation();
    	return sbbEntity.getPriority();
    }
    
    /*
     * (non-Javadoc)
     * @see javax.slee.SbbLocalObject#isIdentical(javax.slee.SbbLocalObject)
     */
    public boolean isIdentical(javax.slee.SbbLocalObject obj)
            throws TransactionRequiredLocalException, SLEEException {    	
    	validateInvocation();
        return this.equals(obj);
    }

    /*
     * (non-Javadoc)
     * @see javax.slee.SbbLocalObject#remove()
     */
    public void remove() throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException {
    	if(trace)
    		logger.trace("remove()");
    	        
       validateInvocation();
        
       if (!sbbEntity.isReentrant()
				&& sleeContainer.getTransactionManager().getTransactionContext().getEventRoutingTransactionData()
						.getInvokedNonReentrantSbbEntities().contains(sbbEntity.getSbbEntityId()))
			throw new SLEEException(" re-entrancy not allowed ");             
        
		if (logger.isDebugEnabled()) {
            logger.debug("nonSleeInitiatedCascadingRemoval : " + sbbEntity.getSbbId()
                    + " entityID = " + sbbEntity.getSbbEntityId());
        }
        
        try {
        	sleeContainer.getSbbEntityFactory().removeSbbEntity(sbbEntity,false);
        } catch (Throwable e) {
            throw new SLEEException("Removal of the sbb entity failed",e);
        }
        
        try {
            if (sleeContainer.getTransactionManager().getRollbackOnly()) {
            	final TransactionContext txContext = sleeContainer.getTransactionManager().getTransactionContext();
            	EventRoutingTransactionData ertd = txContext.getEventRoutingTransactionData();
				txContext.getAfterRollbackActions()
						.add(
								new RolledBackAction(sbbEntity.getSbbEntityId(),ertd.getEventBeingDelivered().getEvent(),ertd.getAciReceivingEvent(), true));
			}
		} catch (Exception e) {
            throw new SLEEException("Failed to check and possibly set rollback context of entity "+sbbEntity.getSbbEntityId(),e);
        }

        // I Think this should set isRemoved only to true but then test 323
        // will fail.
        // :-(
        // Ralf: see above
        this.rollbackOnly = true;
        this.isRemoved = true;

    }

    /*
     * (non-Javadoc)
     * @see javax.slee.SbbLocalObject#setSbbPriority(byte)
     */
    public void setSbbPriority(byte priority)
            throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException {
    	if(trace)
    		logger.trace("setSbbPriority( priority = "+priority+" )");
    	validateInvocation();
    	sbbEntity.setPriority(priority);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		final SbbLocalObjectImpl other = (SbbLocalObjectImpl) obj;
    		return this.sbbEntity.getSbbEntityId().equals(
    				other.sbbEntity.getSbbEntityId()) && this.isRemoved == other.isRemoved;
    	}
    	else {
    		return false;
    	}       
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
    	return sbbEntity.getSbbEntityId().hashCode();
    }
    
    /*
     * (non-Javadoc)
     * @see org.mobicents.slee.runtime.sbb.SbbLocalObjectConcrete#getSbbEntityId()
     */
    public SbbEntityID getSbbEntityId() {
        return sbbEntity.getSbbEntityId();
    }
    
    private static class RolledBackAction implements TransactionalAction {
        
    	final SbbEntityID sbbeId;
    	
    	private Object event;

    	private ActivityContextInterface activityContextInterface;

    	private boolean removeRollback;

        public RolledBackAction(SbbEntityID sbbeId,Object event,
                ActivityContextInterface activityContextInterface,
                boolean removeRollback) {
            this.event = event;
            this.activityContextInterface = activityContextInterface;
            this.removeRollback = removeRollback;
            this.sbbeId = sbbeId;
        }

        /*
         * (non-Javadoc)
         * @see org.mobicents.slee.runtime.transaction.TransactionalAction#execute()
         */
        public void execute() {
            SbbEntity sbbe = null;
            final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
            try {
            	txManager.begin();
                sbbe = sleeContainer.getSbbEntityFactory().getSbbEntity(sbbeId,true);
                if (sbbe != null) {
                	sbbe.sbbRolledBack(event,activityContextInterface,removeRollback);
                	sbbe.getSbbObject().sbbStore();
                }
                txManager.commit();
            } catch (RuntimeException e) {
            	if (sbbe != null) {
            		sbbe.trashObject();
            		logger.error("Exception while executing RolledBackAction",e);
            		try {
            			if (txManager.getTransaction() != null)
            				txManager.rollback();
            		} catch (SystemException e1) {
                    logger.error(e1.getMessage(),e1);
                }
            	}
            } catch (Exception e) {
            	if (sbbe != null) {
            		sbbe.trashObject();
            		logger.error("Exception while executing RolledBackAction",e);
            		try {
            			if (txManager.getTransaction() != null)
            				txManager.rollback();
            		} catch (SystemException e1) {
            			logger.error(e1.getMessage(),e1);
            		}
            	}
            }
        }
    }

    @Override
    public String toString() {
    	return new StringBuilder("SbbLocalObjectImpl[").append(sbbEntity != null ? sbbEntity.getSbbEntityId() : "null").append("]").toString();
    }
    
    // extension methods
    
    @Override
    public String getChildRelation() throws TransactionRequiredLocalException,
    		SLEEException {
    	
    	validateInvocation();    	
    	
    	return getSbbEntityId().getParentChildRelation();
    }
    
    @Override
    public String getName() throws NoSuchObjectLocalException,
    		TransactionRequiredLocalException, SLEEException {
    	
    	validateInvocation();    	
    	
    	return getSbbEntityId().getName();
    }
    
    @Override
    public SbbLocalObjectExt getParent() throws NoSuchObjectLocalException,
    		TransactionRequiredLocalException, SLEEException {
    	
    	validateInvocation();    	
    	
    	SbbLocalObjectExt parent = null;
    	final SbbEntityID parentSbbEntityID = getSbbEntityId().getParentSBBEntityID();
    	if (parentSbbEntityID != null) {
    		SbbEntity parentSbbEntity = sleeContainer.getSbbEntityFactory().getSbbEntity(parentSbbEntityID, false);
    		if (parentSbbEntity != null) {
    			parent = parentSbbEntity.getSbbLocalObject();
    		}
    	}
    	return parent;
    }
        
}