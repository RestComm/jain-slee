/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.runtime.facilities;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import javax.management.NotCompliantMBeanException;
import javax.slee.ComponentID;
import javax.slee.UnrecognizedComponentException;
import javax.slee.facilities.FacilityException;
import javax.slee.facilities.Level;
import javax.slee.facilities.TraceFacility;
import javax.slee.management.TraceNotification;
import javax.transaction.SystemException;

import org.jboss.logging.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.jmx.TraceMBeanImpl;
import org.mobicents.slee.runtime.transaction.TransactionalAction;


/**
 *Implementation of the trace facility.
 *
 *@author M. Ranganathan
 *
 */
public class TraceFacilityImpl implements TraceFacility {
    
    private Hashtable traceLevelTable;
    private TraceMBeanImpl traceMBeanImpl; 		// The MBean for this trace facility.
    private HashSet notificationTypes;
  
    private  static Logger log;
    
    static {
        log = Logger.getLogger(TraceFacilityImpl.class);
    }
	/*
	 * @return Returns the traceMBeanImpl.
	 */
	public TraceMBeanImpl getTraceMBeanImpl()
	{
		return traceMBeanImpl;
	}
	
    class TraceLevel {
        private Level level;
        private int seqno;
        public TraceLevel (Level level) {
            this.level = level;
        }
        public int getSeqno() {
            return this.seqno++;
        }
        
        public Level getLevel() { return this.level; }
    }

    public TraceFacilityImpl(TraceMBeanImpl traceMB) throws NotCompliantMBeanException {
        this.notificationTypes = new HashSet();
        this.traceLevelTable = new Hashtable();
        this.traceMBeanImpl = traceMB;
    }
    
    public void setTraceLevelOnTransaction (final ComponentID componentId, Level newLevel) throws SystemException {
        TraceLevel traceLevel  = (TraceLevel) this.traceLevelTable.get(componentId);
        final Level oldLevel = traceLevel == null ? null : traceLevel.level;
        if (traceLevel == null) {
      	  this.traceLevelTable.put(componentId,new TraceLevel(newLevel));
        }
        else {
      	  traceLevel.level = newLevel;
        }
        // since we are in a tx and this information is not tx aware ad rollback action
        TransactionalAction action = new TransactionalAction() {
  	    	public void execute() {
  	    		if (oldLevel == null) {
  	    			traceLevelTable.remove(componentId);
  	    		}
  	    		else {
  	    			((TraceLevel)traceLevelTable.get(componentId)).level = oldLevel;
  	    		}
  	    	}
  	    };
  	    SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
    }
    
    public void setTraceLevel (ComponentID componentId, Level newLevel) {
    	TraceLevel traceLevel  = (TraceLevel) this.traceLevelTable.get(componentId);
    	if (traceLevel == null) {
    		this.traceLevelTable.put(componentId,new TraceLevel(newLevel));
    	}
    	else {
    		traceLevel.level = newLevel;
    	}
    }
    
    public void unSetTraceLevel (final ComponentID componentId) throws SystemException {
        final TraceLevel level = (TraceLevel) traceLevelTable.remove(componentId);
        if (level != null) {
        	// since we are in a tx and this information is not tx aware ad rollback action
        	TransactionalAction action = new TransactionalAction() {
    	    	public void execute() {
    	    		traceLevelTable.put(componentId,level);    
    	    	}
    	    };
    	    SleeContainer.lookupFromJndi().getTransactionManager().addAfterRollbackAction(action);
        }
    }
    
    public void checkComponentID (ComponentID componentId) throws UnrecognizedComponentException {
        if (this.traceLevelTable.get(componentId) == null) 
            throw new UnrecognizedComponentException (componentId.toString());
    }
    /* (non-Javadoc)
     * @see javax.slee.facilities.TraceFacility#getTraceLevel(javax.slee.ComponentID)
     */
    public Level getTraceLevel(ComponentID componentId) throws NullPointerException,
            UnrecognizedComponentException, FacilityException {
        checkComponentID( componentId);
        return ((TraceLevel) this.traceLevelTable.get(componentId)).getLevel();
    }

    /* (non-Javadoc)
     * @see javax.slee.facilities.TraceFacility#createTrace(javax.slee.ComponentID, javax.slee.facilities.Level, java.lang.String, java.lang.String, long)
     */
    public void createTrace(ComponentID componentId, Level level, String messageType,
            String message, long timeStamp) throws NullPointerException,
            IllegalArgumentException, UnrecognizedComponentException,
            FacilityException {
        	if (log.isDebugEnabled()) {
        	    log.debug("createTrace: " + componentId + " level = " + level + " messageType " + messageType + " message " + message + " timeStamp " + timeStamp );
        	}
        	checkComponentID( componentId);
        	TraceLevel tl  = (TraceLevel) this.traceLevelTable.get(componentId);
        	this.notificationTypes.add(messageType);
        	if (tl == null) throw new UnrecognizedComponentException("Could not find " + componentId);
        	Level lev = tl.getLevel();
        	int seqno = tl.getSeqno();
        	if (lev.isOff()) return;
        	// Check if we should log this message.
        	//if (level.isHigherLevel(lev)) {
        	if (!lev.isHigherLevel(level)){
        	    TraceNotification traceNotification = new TraceNotification ( traceMBeanImpl, messageType, componentId, 
        	            level , message, null, seqno, timeStamp);
        	    this.traceMBeanImpl.sendNotification(traceNotification);
        	}

    }

    /* (non-Javadoc)
     * @see javax.slee.facilities.TraceFacility#createTrace(javax.slee.ComponentID, javax.slee.facilities.Level, java.lang.String, java.lang.String, java.lang.Throwable, long)
     */
    public void createTrace(ComponentID componentId, Level level, String messageType,
            String message, Throwable cause, long timeStamp)
            throws NullPointerException, IllegalArgumentException,
            UnrecognizedComponentException, FacilityException {
        checkComponentID( componentId);
        
        TraceLevel tl  = (TraceLevel) this.traceLevelTable.get(componentId);
        if (log.isDebugEnabled()) {
            log.debug( " createTrace: "  + componentId + " level " + level + " messageType " + messageType + " message " + message 
    	        + " cause " + cause + " time stamp " + timeStamp );
        }
    	if (tl == null) throw new UnrecognizedComponentException("Could not find " + componentId);
    	this.notificationTypes.add(messageType);
    	Level lev = tl.getLevel();
    	int seqno = tl.getSeqno();
    	if (lev.isOff()) return;
    	// Check if we should log this message.
    	if (level.isHigherLevel(lev)) {
    	    TraceNotification traceNotification = new TraceNotification ( traceMBeanImpl, messageType, componentId, 
    	            level , message,cause, seqno, timeStamp);
    	    this.traceMBeanImpl.sendNotification( traceNotification);
    	}

    }
    
    public String[] getNotificationTypes() {
        String ntypes[] = new String[ this.notificationTypes.size() ];
        Iterator it =  this.notificationTypes.iterator();
        int i = 0;
        while (it.hasNext()) {
            ntypes[i ++ ] = (String) it.next();
            
        }
        return ntypes;
    }

}

