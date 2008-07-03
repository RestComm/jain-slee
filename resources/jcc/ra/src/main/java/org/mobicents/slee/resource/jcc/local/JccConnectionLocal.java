/*
 * File Name     : JccConnectionLocal.java
 *
 * The Java Call Control RA
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
package org.mobicents.slee.resource.jcc.local;

import javax.csapi.cc.jcc.*;

/**
 * Wraps JccConnection to disallow addConnectionListener, addCallListener methods. 
 * When a disallowed method is invoked, the resource adaptor
 * entity throws a SecurityException.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccConnectionLocal implements JccConnection {
    
    private JccConnection connection;
    private JccProviderLocal provider;
    private JccCallLocal call;
    private int id = (int) new java.util.Date().getTime();
    
    /** Creates a new instance of JccConnectionLocal */
    public JccConnectionLocal(JccConnection connection, JccProviderLocal provider, 
            JccCallLocal call) {
        this.connection = connection;
        this.provider = provider;
        this.call = call;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#answer()
     */
    public void answer() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException, MethodNotSupportedException {
        connection.answer();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#attachMedia()
     */
    public void attachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
        connection.attachMedia();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#continueProcessing()
     */
    public void continueProcessing() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
        connection.continueProcessing();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#detachMedia()
     */
    public void detachMedia() throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException {
        connection.detachMedia();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getAddress()
     */
    public JccAddress getAddress() {
        return connection.getAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getCall()
     */
    public JccCall getCall() {
        return call;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getDestinationAddress()
     */
    public String getDestinationAddress() {
        return connection.getDestinationAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getLastAddress()
     */
    public String getLastAddress() {
        return connection.getLastAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getMidCallData()
     */
    public MidCallData getMidCallData() throws InvalidStateException, ResourceUnavailableException, MethodNotSupportedException {
        return connection.getMidCallData();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getOriginalAddress()
     */
    public String getOriginalAddress() {
        return connection.getOriginalAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#originatingAddress()
     */
    public JccAddress getOriginatingAddress() {
        return connection.getOriginatingAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#RedirectedAddress()
     */
    public String getRedirectedAddress() {
        return connection.getRedirectedAddress();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#getSate()
     */
    public int getState() {
        return connection.getState();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#isBlocked()
     */
    public boolean isBlocked() {
        return connection.isBlocked();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#release(int)
     */
    public void release(int causeCode) throws PrivilegeViolationException, ResourceUnavailableException, InvalidStateException, InvalidArgumentException {
        connection.release(causeCode);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#routeConnection(boolean)
     */
    public void routeConnection(boolean attachMedia) throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException, InvalidArgumentException {
        connection.routeConnection(attachMedia);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccConnection#selectRoute(String)
     */
    public void selectRoute(String address) throws MethodNotSupportedException, InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException {
        connection.selectRoute(address);
    }
    
    public boolean equals(Object other) {
        System.out.println("Other: " + other);
        boolean res = other != null && other instanceof JccConnectionLocal &&
                ((JccConnectionLocal) other).toString().equals(connection.toString());
        System.out.println("Compare connections, result : " + res);
        return res;    
    }
    
    public int hasCode() {
        return connection.hashCode();
    }
    
    public String toString() {
        return "JccConnectionLocal[" + connection.toString() + "]";
    }
}
