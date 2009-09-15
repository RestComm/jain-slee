/*
 * SbbLocalInterfaceImpl.java
 *
 * Created on June 28, 2004, 3:54 PM
 * 
 * The Mobicents Open SLEE Project
 * 
 * A SLEE for the People!
 *
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */

package org.mobicents.slee.runtime.sbb;

import javax.slee.NoSuchObjectLocalException;
import javax.slee.RolledBackContext;
import javax.slee.SLEEException;
import javax.slee.SbbLocalObject;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.transaction.SystemException;
import javax.transaction.TransactionRequiredException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.eventrouter.RolledBackContextImpl;
import org.mobicents.slee.runtime.eventrouter.routingtask.EventRoutingTransactionData;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;
import org.mobicents.slee.runtime.sbbentity.SbbEntityFactory;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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
 * interface methods must not begin with “sbb” or “ejb”. The SLEE provides the
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

    private ClassLoader contextClassLoader;

    /** This is the Sbb entity to which the local object maps */
    private String sbbEntityId;

    /**
     * Container where this resides -- for now this is the same location where
     * the sbb resides.
     */
    private SleeContainer sleeContainer;

    private static final Logger logger = Logger.getLogger(SbbLocalObjectImpl.class);

    private boolean rollbackOnly;

    /*
     * This flag is set to true if the sbb local object is removed (no longer
     * valid)
     */
    private boolean isRemoved;

    public SbbLocalObjectImpl() {
    }

    public SbbEntity getSbbEntity() {
    	return SbbEntityFactory.getSbbEntity(this.sbbEntityId);     	
    }

    /**
     * We need this in case we want to invoke some methods on the sbb using the
     * local object.
     * 
     * @return the classloader from the Descriptor.
     */
    public ClassLoader getContextClassLoader() {
        return this.contextClassLoader;
    }

    /**
     * Constructor -- assume that the Sbb entity is co-located with the Sbb
     * local object.
     * 
     * @param sbbEntity --
     *            sbb entity for which this is a local object.
     */

    public SbbLocalObjectImpl(SbbEntity sbbEntity) {
        this();
        
        this.sleeContainer = SleeContainer.lookupFromJndi();
        this.contextClassLoader = sbbEntity.getSbbComponent().getClassLoader();
        this.sbbEntityId = sbbEntity.getSbbEntityId();
        if (sbbEntity.getSbbObject() == null){
            try {
                // Check if the object is in the cache or not is not 
                // enough to determine if sbbCreate should be called
                sbbEntity.assignSbbObject();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if(logger.isDebugEnabled())
            logger.debug("sbbLocalObject created for sbbEntity "
                + sbbEntity.getSbbEntityId());
    }

    public byte getSbbPriority() throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException {
        sleeContainer.getTransactionManager().mandateTransaction();

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

        sleeContainer.getTransactionManager().mandateTransaction();
        return this.getSbbEntity().getPriority();

    }

    public boolean isIdentical(SbbLocalObject obj)
            throws TransactionRequiredLocalException, SLEEException {    	
    	sleeContainer.getTransactionManager().mandateTransaction();
        
        // emmartins: adding checks for object state
        if (this.rollbackOnly) {
            try {
            	sleeContainer.getTransactionManager().setRollbackOnly();
            } catch (SystemException ex) {
                throw new SLEEException("unable to set rollbackOnly in transaction manager", ex);
            }
            throw new TransactionRolledbackLocalException(
                    "Unable to proceed, transaction is set to rollback");
        }
        else if (this.isRemoved) {
            throw new NoSuchObjectLocalException("sbb local object is removed");
        }
        else {
        	return this.equals(obj);
        }
    }

    public void remove() throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException {
        if(logger.isDebugEnabled())
            logger.debug("remove() called on sbbLocalObject "
                + this.getSbbEntityId()
                + " isRollback = " + this.rollbackOnly
                + " isRemoved = " + this.isRemoved);
        
        
        sleeContainer.getTransactionManager().mandateTransaction();
        
        if (this.rollbackOnly) {
            try {
                sleeContainer.getTransactionManager().setRollbackOnly();
            } catch (SystemException ex) {
                throw new SLEEException("unable to set rollbackOnly in transaction manager", ex);
            }
            throw new TransactionRolledbackLocalException(
                    "Unable to proceed, transaction is set to rollback");
        }
        
        // Ralf: I've put this after the if(isrolledback)
        if (this.isRemoved)
            throw new NoSuchObjectLocalException("sbb local object is removed");
        
        // emmartins: moved here because of test 323
        SbbEntity sbbEntity = this.getSbbEntity();
		sbbEntity.checkReEntrant();              
        
		if (logger.isDebugEnabled()) {
            logger.debug("nonSleeInitiatedCascadingRemoval : " + sbbEntity.getSbbId()
                    + " entityID = " + sbbEntityId);
        }
        
        try {
        	SbbEntityFactory.removeSbbEntity(sbbEntity, true);
        } catch (TransactionRequiredException e1) {
            throw new TransactionRequiredLocalException("Removal of the sbb entity failed", e1);
        } catch (SystemException e) {
            throw new RuntimeException("Removal of the sbb entity failed",e);
        }
        
        try {
            if (sleeContainer.getTransactionManager().getRollbackOnly()) {
            	EventRoutingTransactionData ertd = EventRoutingTransactionData.getFromTransactionContext();
            	RolledBackContext sbbRolledBackContext = new RolledBackContextImpl(
            			ertd.getEventBeingDelivered().getEvent(),ertd.getAciReceivingEvent(), true);
				sleeContainer.getTransactionManager()
						.addAfterRollbackAction(
								new RolledBackAction(sbbEntityId,
										sbbRolledBackContext));
			}
		} catch (Exception e) {
            throw new RuntimeException("Failed to check and possibly set rollback context of entity "+sbbEntityId,e);
        }

        // I Think this should set isRemoved only to true but then test 323
        // will fail.
        // :-(
        // Ralf: see above
        this.rollbackOnly = true;
        this.isRemoved = true;

    }

    public void setSbbPriority(byte priority)
            throws TransactionRequiredLocalException,
            NoSuchObjectLocalException, SLEEException {
    	sleeContainer.getTransactionManager().mandateTransaction();

        if (this.rollbackOnly) {
            try {
            	sleeContainer.getTransactionManager().setRollbackOnly();
            } catch (SystemException ex) {
                throw new SLEEException("unable to set rollbackOnly in transaction manager", ex);
            }

            throw new TransactionRolledbackLocalException(
                    "Could not set sbb priority");

        }
        if (this.isRemoved)
            throw new NoSuchObjectLocalException("sbb local object is removed");
        this.getSbbEntity().setPriority(priority);

    }

    public boolean equals(Object obj) {
    	if (obj != null && obj.getClass() == this.getClass()) {
    		SbbLocalObjectImpl other = (SbbLocalObjectImpl) obj;
    		return this.getSbbEntityId().equals(
                        other.getSbbEntityId()) && this.isRemoved == other.isRemoved;
    	}
    	else {
    		return false;
    	}       
    }

    @Override
    public int hashCode() {
    	return this.getSbbEntityId().hashCode();
    }
    
    public String getSbbEntityId() {
        return this.sbbEntityId;
    }
    
    private class RolledBackAction implements TransactionalAction {
        String sbbeId;

        RolledBackContext rollbackContext;

        public RolledBackAction(String sbbeId,
                RolledBackContext context) {
            this.sbbeId = sbbeId;
            this.rollbackContext = context;
        }

        public void execute() {
            SbbEntity sbbe = null;
            try {
            	sleeContainer.getTransactionManager().begin();
                sbbe = SbbEntityFactory.getSbbEntity(sbbeId);
                sbbe.sbbRolledBack(rollbackContext);
                sbbe.getSbbObject().sbbStore();
                sleeContainer.getTransactionManager().commit();
            } catch (RuntimeException e) {
            	if (sbbe != null) {
            		sbbe.trashObject();
            		logger.error("Exception while executing RolledBackAction",e);
            		try {
            			if (sleeContainer.getTransactionManager().getTransaction() != null)
            				sleeContainer.getTransactionManager().rollback();
            		} catch (SystemException e1) {
                    logger.error(e1);
                }
            	}
            } catch (Exception e) {
            	if (sbbe != null) {
            		sbbe.trashObject();
            		logger.error("Exception while executing RolledBackAction",e);
            		try {
            			if (sleeContainer.getTransactionManager().getTransaction() != null)
            				sleeContainer.getTransactionManager().rollback();
            		} catch (SystemException e1) {
            			logger.error(e1);
            		}
            	}
            }
        }

    }

}