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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.mobicents.slee.util.JndiRegistrationManager;

class TxLocalEntry {
		
	private List<TransactionalAction> afterCommitActions = new ArrayList<TransactionalAction>();        
	private List<TransactionalAction> afterRollbackActions = new ArrayList<TransactionalAction>(); 
    private List<TransactionalAction> prepareActions = new ArrayList<TransactionalAction>(); 
	
    private Map data = new HashMap();
    
    private String transaction;
    
    private static Logger logger = Logger.getLogger(TxLocalEntry.class);
    
    private SleeTransactionManager _SleeTransactionManager;
    private SleeTransactionManager getSleeTransactionManager() {
    	try {
			if (_SleeTransactionManager == null)
				_SleeTransactionManager = (SleeTransactionManager) JndiRegistrationManager.getFromJndi("slee/"
						+ SleeTransactionManager.JNDI_NAME);
			return _SleeTransactionManager;
		} catch (Exception ex) {
			logger.error("Error fetching transaciton manager!", ex);
			return null;
		}
    }
    
    public void addAfterCommitAction(TransactionalAction action) {
        if(logger.isDebugEnabled())
        logger.debug("addAfterCommitAction " + action + " list " + afterCommitActions);
            afterCommitActions.add(action);    
    }
            
    
    public TxLocalEntry (String transaction) {
        this.transaction = transaction;
    }
    
    public void addPrepareAction( TransactionalAction action ) {
        if (logger.isDebugEnabled()) {
            Integer exetran=null;
            try {
                exetran = TransactionManagerImpl.makeKey(getSleeTransactionManager().getTransaction());
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
           
    public List<TransactionalAction> getAfterCommitActions() {
        return afterCommitActions;
    }
    
    public List<TransactionalAction> getAfterRollbackActions() {
        return afterRollbackActions;
    }
    
    public List<TransactionalAction> getPrepareActions() {
        if (logger.isDebugEnabled()) {
            Integer exetran=null;
            try {
                exetran = TransactionManagerImpl.makeKey(getSleeTransactionManager().getTransaction());
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
        return data.get(key);
    }
    
    public void putData(Object key, Object value) {
        data.put(key, value);
    } 
    
    public void removeData(Object key) {
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
     
    private void executeActions(List<TransactionalAction> actions) { 
        	
        while (!actions.isEmpty()) {
			TransactionalAction action = (TransactionalAction) actions
					.remove(0);
			if (logger.isDebugEnabled())
				logger.debug("Executing action:" + action);
			try {
				action.execute();
			} catch (Throwable t) {
				if (logger.isDebugEnabled())
					logger.error("FAILED DURING PREPARE ACTION:"
							+ t.getMessage());
				throw new RuntimeException("FAILED DURING ACTION:", t);
			}
		}
    }
}