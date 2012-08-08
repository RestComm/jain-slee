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

package org.mobicents.slee.runtime.facilities;

import java.util.Map;

import javax.slee.ActivityContextInterface;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.NameAlreadyBoundException;
import javax.slee.facilities.NameNotBoundException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.AbstractSleeContainerModule;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.container.util.JndiRegistrationManager;

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
public class ActivityContextNamingFacilityImpl extends AbstractSleeContainerModule implements
        ActivityContextNamingFacility {

    private static Logger log = Logger.getLogger(ActivityContextNamingFacilityImpl.class);
    
    private ActivityContextNamingFacilityCacheData cacheData;
    
    @Override
    public void sleeInitialization() {
    	JndiRegistrationManager.registerWithJndi("slee/facilities", "activitycontextnaming",
				this);
    }
    
    @Override
    public void sleeStarting() {
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
        	org.mobicents.slee.container.activity.ActivityContextInterface sleeAci = (org.mobicents.slee.container.activity.ActivityContextInterface)aci;
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
        
    public void removeName(String aciName) throws NameNotBoundException {		
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
        	
        	return ac.getActivityContextInterface();
        	        	        
        } catch (Exception e) {
        	throw new FacilityException("Failed to look-up ac name binding", e);
        }
        
    }
    
	@SuppressWarnings("rawtypes")
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