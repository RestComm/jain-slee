/*
 * Created on Aug 11, 2004
 *
 * The Open SLEE project
 * 
 * A SLEE for the People!
 * 
 * The source code contained in this file is in in the public domain.          
 * It can be used in any project or product without prior permission, 	      
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 * 
 */
package org.mobicents.slee.runtime.facilities;

import java.util.Map;

import javax.slee.ActivityContextInterface;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.ActivityContextNamingFacility;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.NameAlreadyBoundException;
import javax.slee.facilities.NameNotBoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/*
 * Ranga - Initial and refactored for Tx isolation.
 * Tim  
 */

/**
 * Activity Context Naming facility implementation.
 * 
 * @author M. Rangananthan.
 * @author Tim 
 * @author martins
 * 
 *  
 */
public class ActivityContextNamingFacilityImpl implements
        ActivityContextNamingFacility {

    private static Logger log = Logger.getLogger(ActivityContextNamingFacilityImpl.class);
    
    private final SleeContainer sleeContainer;
    
    private final ActivityContextNamingFacilityCacheData cacheData;
    
    /**
     * constructor.
     *  
     */
    public ActivityContextNamingFacilityImpl(SleeContainer sleeContainer) {
    	this.sleeContainer = sleeContainer;
    	cacheData = new ActivityContextNamingFacilityCacheData(sleeContainer.getCluster());
    	cacheData.create();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.facilities.ActivityContextNamingFacility#bind(javax.slee.ActivityContextInterface,
     *      java.lang.String)
     */
    public void bind(ActivityContextInterface aci, String aciName)
            throws NullPointerException, IllegalArgumentException,
            TransactionRequiredLocalException, NameAlreadyBoundException,
            FacilityException {
        // Check if we are in the context of a transaction.
       
    	sleeContainer.getTransactionManager().mandateTransaction();
            
        if (aciName == null)
            throw new NullPointerException("null aci name");
        else if (aciName.equals(""))
            throw new IllegalArgumentException("empty name");
        else if (aci == null )
            throw new NullPointerException ("Null ACI! ");
          
        try {        
        	org.mobicents.slee.runtime.activity.ActivityContextInterface sleeAci = (org.mobicents.slee.runtime.activity.ActivityContextInterface)aci;
        	ActivityContext ac = sleeAci.getActivityContext();
        	ActivityContextHandle ach = ac.getActivityContextHandle();
        	cacheData.bindName(ach,aciName);
	        ac.addNameBinding(aciName);
	        if (log.isDebugEnabled()) {
	        	log.debug("aci name "+aciName+" bound to "+ach+" . Tx is "+sleeContainer.getTransactionManager().getTransaction());
	        }
        } catch (NameAlreadyBoundException ex) {
            if (log.isDebugEnabled()) {
                log.debug("name already bound " + aciName);
            }
            throw ex;
        } catch (Exception e) {
        	throw new FacilityException("Failed to put ac name binding in cache", e);
        }
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.facilities.ActivityContextNamingFacility#unbind(java.lang.String)
     */
    public void unbind(String aciName) throws NullPointerException,
            TransactionRequiredLocalException, NameNotBoundException,
            FacilityException {
        //Check if we are in the context of a transaction.
        
    	sleeContainer.getTransactionManager().mandateTransaction();

        if (aciName == null)
            throw new NullPointerException("null activity context name!");
        
        try {       
            
        	ActivityContextHandle ach = (ActivityContextHandle) cacheData.unbindName(aciName);         
            ActivityContext ac = sleeContainer.
            	getActivityContextFactory().getActivityContext(ach);
            if ( ac != null )
                ac.removeNameBinding(aciName);
            if (log.isDebugEnabled()) {
	        	log.debug("aci name "+aciName+" unbound from "+ach+" . Tx is "+sleeContainer.getTransactionManager().getTransaction());
	        }
        } catch ( NameNotBoundException ex) {
            if (log.isDebugEnabled()) {
                log.debug("Name not bound " + aciName);
            }
            throw ex;
        } catch (Exception e) {
        	throw new FacilityException("Failed to remove ac name binding from cache", e);
        }
	      
    }
        
    public void removeName(String aciName) throws NullPointerException,
		    TransactionRequiredLocalException, NameNotBoundException, FacilityException 
    {
		//Check if we are in the context of a transaction.
		
    	sleeContainer.getTransactionManager().mandateTransaction();
    	
		if (log.isDebugEnabled()) {
	        log.debug("Removing name from facility: aci " + aciName);
		}
		
		cacheData.unbindName(aciName);
  
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.facilities.ActivityContextNamingFacility#lookup(java.lang.String)
     */
    public ActivityContextInterface lookup(String acName)
            throws NullPointerException, TransactionRequiredLocalException,
            FacilityException {
        
    	final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
    	
    	sleeContainer.getTransactionManager().mandateTransaction();
       
        if (acName == null)
            throw new NullPointerException("null ac name");
        
        try {        
            
	        ActivityContextHandle ach = (ActivityContextHandle) cacheData.lookupName(acName);
	        
	        if (log.isDebugEnabled()) {
	        	log.debug("lookup of aci name "+acName+" result is "+ach+" . Tx is "+sleeContainer.getTransactionManager().getTransaction());
	        }
	        
            if (ach == null) {
        		return null;
        	}       	
        	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
        	if (ac == null) {
        		cacheData.unbindName(acName);
        		throw new FacilityException("name found but unable to retrieve activity context");
        	}
        	
        	return new ActivityContextInterfaceImpl(ac);
        	        	        
        } catch (Exception e) {
        	throw new FacilityException("Failed to look-up ac name binding", e);
        }
        
    }
    
    public Map getBindings() {
    	
    	try {
    		return cacheData.getNameBindings();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
    	
		return null;
    }
    
    @Override
	public String toString() {
		return 	"Activity context Naming Facility: " +
				"\n+-- Names: " + getBindings().keySet();
	}
}