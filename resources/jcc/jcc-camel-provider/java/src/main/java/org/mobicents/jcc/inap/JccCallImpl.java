/*
 * File Name     : JccCallImpl.java
 *
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.jcc.inap;

import java.util.ArrayList;
import java.util.Vector;

import javax.csapi.cc.jcc.*;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentReaderHashMap;

import java.util.Collection;
import org.apache.log4j.Logger;

/**
 *
 * @author Oleg Kulikov
 */
public class JccCallImpl implements JccCall {
    public static int CALL_ID_GENERATOR = 1;
    
    protected String callID;
    protected int direction;
    
    protected JccInapProviderImpl provider;
    protected int state = JccCall.IDLE;
    
    protected ConcurrentReaderHashMap connections = new ConcurrentReaderHashMap();
    
    protected ArrayList callListeners = new ArrayList();
    protected ArrayList connectionListeners = new ArrayList();
    
    private static Logger logger = Logger.getLogger(JccCallImpl.class);
    private JccAddress callingNumber;
    
    protected JccCallImpl(JccInapProviderImpl provider, JccAddress callingNumber) {
        this.provider = provider;
        this.callingNumber = callingNumber;
        this.provider.calls.put(callingNumber.getName(), this);
        this.callID = Integer.toString(CALL_ID_GENERATOR++);
        this.state = ACTIVE;
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#createConnection(String, String, String, String).
     */
    public JccConnection createConnection(
            String targetAddress,
            String originatingAddress,
            String originalCalledAddress,
            String redirectingAddress)
            throws InvalidStateException,
            ResourceUnavailableException,
            PrivilegeViolationException,
            MethodNotSupportedException,
            InvalidArgumentException,
            InvalidPartyException {
        return null;
    }
    
    protected void fireCallEvent(Vector callListeners, JccCallEvent event) {
        for (int i = 0; i < callListeners.size(); i++) {
            JccCallListener listener = (JccCallListener) callListeners.get(i);
            switch (event.getID()) {
                case JccCallEvent.CALL_ACTIVE :
                    listener.callActive(event);
                    break;
                case JccCallEvent.CALL_CREATED :
                    listener.callCreated(event);
                    break;
                case JccCallEvent.CALL_EVENT_TRANSMISSION_ENDED :
                    listener.callEventTransmissionEnded(event);
                    break;
                case JccCallEvent.CALL_INVALID :
                    listener.callInvalid(event);
                    break;
                case JccCallEvent.CALL_SUPERVISE_END :
                    listener.callSuperviseEnd(event);
                    break;
                case JccCallEvent.CALL_SUPERVISE_START :
                    listener.callSuperviseStart(event);
                    break;
            }
        }
    }
        
    protected void fireCallEvent(JccCallEvent event) {
        //fireCallEvent(callListeners, event);
        //fireCallEvent(provider.callListeners, event);
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#addCallListener(JccCallListener).
     */
    public void addCallListener(JccCallListener listener)
    throws ResourceUnavailableException, MethodNotSupportedException {
        callListeners.add(listener);
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#addConnectionListener(JccConnectionListener, EventFilter).
     */
    public void addConnectionListener(JccConnectionListener cl, EventFilter filter)
    throws ResourceUnavailableException, MethodNotSupportedException {
        Object[] listener = new Object[2];
        listener[0] = cl;
        //listener[1] = filter;
        listener[1] = new DefaultFilter();
        
        connectionListeners.add(listener);
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#connect(JccAddress, String).
     */
    public JccConnection[] connect(JccAddress origaddr, String dialedDigits)
    throws ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException, InvalidStateException, MethodNotSupportedException {
        return null;
    }
    
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#getConnections().
     */
    public synchronized JccConnection[] getConnections() {
        switch (state) {
            case JccCall.ACTIVE : {
                int count = connections.size();
                
                JccConnection[] list = new JccConnection[count];
                Object[] conn = connections.values().toArray();
                
                for (int i = 0; i < count; i++) {
                    list[i] = (JccConnection) conn[i];
                }
                
                return list;
            }
            default : return null;
        }
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#getProvider().
     */
    public JccProvider getProvider() {
        return provider;
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#getState().
     */
    public int getState() {
        return state;
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#release(int).
     */
    public void release(int causeCode)
    throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException, InvalidArgumentException {
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#removeCallListener(JccCallListener).
     */
    public void removeCallListener(JccCallListener calllistener) {
    }
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#removeConnectionListener(JccConnectionListener).
     */
    public void removeConnectionListener(JccConnectionListener cl) {
    }
    
    
    /**
     * (Non-Javadoc).
     * @see javax.csapi.cc.jcc.JccCall#superviceCall(JccCallListener, double, int).
     */
    public void superviseCall(JccCallListener calllistener, double time, int treatment) throws MethodNotSupportedException {
    }
    
    protected void forceRelease() {
        Collection<AbstractConnection> list = connections.values();
        for (AbstractConnection connection : list) {
            connection.forceDisconnect();
        }
    }
    
    protected synchronized void append(JccConnection connection) {
        //store connection reference within call
        connections.put(connection.getAddress().getName(), connection);
        
        //store connection reference within provider
        long id = ((AbstractConnection) connection).getID().getId();
        provider.connections.put(new Long(id), connection);
    }
    
    protected synchronized void remove(JccConnection connection) {
        //remove connection from call
    	if(logger.isInfoEnabled())
    	{
    		logger.info("Removing from call: "+callID+", connection: "+connection.toString()+"---"+connection.getAddress().getName()+", from list: "+connections);
    	}
    	
        connections.remove(connection.getAddress().getName());
        
        //remove connection from provider
        long id = ((AbstractConnection) connection).getID().getId();
        if(logger.isInfoEnabled())
    	{
    		logger.info("Removing from call: "+callID+", connection: "+id+", from provider: "+provider.connections);
    	}
        provider.connections.remove(new Long(id));
        
        //clear call if there is no more connections
        if (connections.size() == 0) {
            clear();
        }
    }
    
    protected void clear() {
        callListeners.clear();
        connectionListeners.clear();
        provider.calls.remove(callingNumber.getName());
    }
    
    public JccConnection routeCall(String string, String string0, String string1, String string2) throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException, InvalidArgumentException {
        return null; //send Initiate call attempt.
    }
    
    public String toString() {
        return "(callID=" + callID + ")";
    }
    
}
