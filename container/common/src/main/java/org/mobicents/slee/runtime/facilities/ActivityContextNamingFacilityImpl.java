/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
import org.mobicents.slee.container.CacheType;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.facilities.ActivityContextNamingFacility;
import org.mobicents.slee.runtime.facilities.cluster.ActivityContextNamingFacilityCacheData;
import org.mobicents.slee.runtime.facilities.cluster.ActivityContextNamingFacilityFactoryCacheData;
import org.restcomm.cluster.MobicentsCluster;

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
    
    private MobicentsCluster cluster;
    private ActivityContextNamingFacilityFactoryCacheData acnCacheData;
    
    @Override
    public void sleeInitialization() {
    }
    
    @Override
    public void sleeStarting() {
    	cluster=sleeContainer.getCluster(CacheType.ACI_NAMING);  
    	acnCacheData=new ActivityContextNamingFacilityFactoryCacheData(cluster);
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
        	ActivityContextNamingFacilityCacheData cacheData=new ActivityContextNamingFacilityCacheData(aciName, cluster);
        	cacheData.bindName(ach);
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
            
        	ActivityContextNamingFacilityCacheData cacheData=new ActivityContextNamingFacilityCacheData(aciName, cluster);
        	ActivityContextHandle ach = cacheData.unbindName();         
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
    	ActivityContextNamingFacilityCacheData cacheData=new ActivityContextNamingFacilityCacheData(aciName, cluster);
    	cacheData.unbindName();    	
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
            
        	ActivityContextNamingFacilityCacheData cacheData=new ActivityContextNamingFacilityCacheData(acName, cluster);
        	ActivityContextHandle ach = cacheData.lookupName();
	        
	        if (log.isDebugEnabled()) {
	        	log.debug("lookup of aci name "+acName+" result is "+ach+" . Tx is "+sleeContainer.getTransactionManager().getTransaction());
	        }
	        
            if (ach == null) {
        		return null;
        	}       	
        	ActivityContext ac = sleeContainer.getActivityContextFactory().getActivityContext(ach);
        	if (ac == null) {
        		cacheData.unbindName();
        		throw new FacilityException("name found but unable to retrieve activity context");
        	}
        	
        	return ac.getActivityContextInterface();
        	        	        
        } catch (Exception e) {
        	throw new FacilityException("Failed to look-up ac name binding", e);
        }
        
    }
    
	public Map<String, ActivityContextHandle> getBindings() {
    	
    	try {
    		return acnCacheData.getNameBindings();
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