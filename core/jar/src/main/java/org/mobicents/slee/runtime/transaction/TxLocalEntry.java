/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform      *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 *  Created on 2005-5-28                             *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.transaction;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;

import EDU.oswego.cs.dl.util.concurrent.FIFOReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.SyncList;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;

class TxLocalEntry {
	
	private SyncList afterCommitActions = new SyncList(new ArrayList(), new WriterPreferenceReadWriteLock());        
    
	private SyncList afterRollbackActions = new SyncList(new ArrayList(), new WriterPreferenceReadWriteLock()); 
    
    private SyncList prepareActions =  new SyncList(new ArrayList(), new WriterPreferenceReadWriteLock());
	
    private Map data;
    
    private String transaction;
    
    private static Logger logger = Logger.getLogger(TxLocalEntry.class);
    
    public void addAfterCommitAction(TransactionalAction action) {
        if(logger.isDebugEnabled())
        logger.debug("addAfterCommitAction " + action + " list " + afterCommitActions);
            afterCommitActions.add(action);    
        if(logger.isDebugEnabled())
        logger.debug("after adding listLength is " + afterCommitActions.size());
    }
            
    
    public TxLocalEntry (String transaction) {
        this.transaction = transaction;
    }
    
    public void addPrepareAction( TransactionalAction action ) {
        if (logger.isDebugEnabled()) {
            Integer exetran=null;
            try {
                exetran = TransactionManagerImpl.makeKey(SleeContainer
                        .getTransactionManager().getTransaction());
            } catch (Exception ex) {
                exetran=null;
            }
            logger.debug("[ transaction owner = " + transaction  + 
                    ", [current executing transaction = " +
                    exetran + "]");
        }
        prepareActions.add(action);
    }
    
    public void addAfterRollbackAction(TransactionalAction action) {
            afterRollbackActions.add(action);    
    }
           
    public List getAfterCommitActions() {
        return afterCommitActions;
    }
    
    public List getAfterRollbackActions() {
        return afterRollbackActions;
    }
    
    public List getPrepareActions() {
        if (logger.isDebugEnabled()) {
            Integer exetran=null;
            try {
                exetran = TransactionManagerImpl.makeKey(SleeContainer
                        .getTransactionManager().getTransaction());
            } catch (Exception ex) {
                exetran=null;
            }
            logger.debug("[ transaction owner = " + transaction  + 
                    ", [current executing transaction = " +
                    exetran + "]");
        }
        return prepareActions;
    }
    public Object getData(Object key) { 

        if (data == null)
            return null;
        return data.get(key);
    }
    
    public void putData(Object key, Object value) {
        if (data == null)
            data = new HashMap();
        data.put(key, value);
    } 
    
    public void removeData(Object key) {
        if (data == null)
            return;
    	data.remove(key);
    }
    
    public void executeAfterCommitActions() {
       
        if (afterCommitActions != null) {
          
            executeActions(afterCommitActions);
        } else {
            if(logger.isDebugEnabled())
            logger.debug("No commit actions to execute");
        }
    }
    

    
    public void executePrepareActions( ) {
        if (prepareActions != null) {
            if(logger.isDebugEnabled())
            logger.debug("Executing prepare actions");
            executeActions(prepareActions);
        } else {
            if(logger.isDebugEnabled())
            logger.debug("No prepare actions to execute");
        }   
      
    }
    
    public void executeAfterRollbackActions() {
        if (afterRollbackActions != null) {
            if(logger.isDebugEnabled())
            logger.debug("Executing rollback actions");
            executeActions(afterRollbackActions);
        } else {
            if(logger.isDebugEnabled())
            logger.debug("No rollback actions to execute");
        }
    }
     
    private void executeActions(List actions) { 
        try {
            
        	
        		while ( !actions.isEmpty()) {
        		
                if(logger.isDebugEnabled())
                    logger.debug("*** ITERATION");
                
                TransactionalAction action = (TransactionalAction)actions.remove(0);
        		
                if(logger.isDebugEnabled())
                    logger.debug("Executing action:" + action);
                try {
                    action.execute(); 	                  
                } catch(Throwable t){
                    if(logger.isDebugEnabled())
                        logger.error("FAILED DURING PREPARE ACTION:" + t.getMessage());
                    t.printStackTrace();
                    throw new RuntimeException("FAILED DURING ACTION:", t);
                    
                }
            }
        } catch (ConcurrentModificationException e) {
            Integer exetran=null;
            try {
                exetran = TransactionManagerImpl.makeKey(SleeContainer
                        .getTransactionManager().getTransaction());
            } catch (Exception ex) {
                exetran=null;
            }
            
            
            String s = "unexpecting concurrent accesses of transaction data. " +
            " [ transaction owner = " + transaction  + ", [current executing transaction = " +
             exetran + "]";
            logger.error(s);
            throw e;
           
        }
    }
}