/*
 * File Name     : JccProviderLocal.java
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
import org.mobicents.slee.resource.jcc.ra.JccResourceAdaptor;

/**
 * Wraps JccProvider to disallow addConnectionListener, addCallListener and 
 * addProviderListener methods. 
 *
 * When a disallowed method is invoked, the resource adaptor entity 
 * throws a SecurityException.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccProviderLocal implements JccProvider {
    
    private JccProvider provider;
    private JccCall call;
    private JccConnection connection;
    
    /** 
     * Creates a new instance of JccProviderLocal 
     */
    public JccProviderLocal(JccProvider provider) {
        this.provider = provider;
    }

    /**
     * Only the resource adaptor can add itself as a listener.
     * The resource adaptor should not allow any other objects to add themselves
     * as listeners.
     */
    public void addCallListener(JccCallListener listener) 
        throws MethodNotSupportedException, ResourceUnavailableException {
            if (listener instanceof JccResourceAdaptor) {
                provider.addCallListener(listener);
            } else {
                throw new SecurityException("SBB may not add event listener");
            }
    }

    public void addCallLoadControlListener(CallLoadControlListener listener) 
        throws MethodNotSupportedException, ResourceUnavailableException {
            throw new MethodNotSupportedException();
    }

    /**
     * Only the resource adaptor can add itself as a listener.
     * The resource adaptor should not allow any other objects to add themselves
     * as listeners.
     */
    public void addConnectionListener(JccConnectionListener listener, EventFilter filter) 
        throws ResourceUnavailableException, MethodNotSupportedException {
            if (listener instanceof JccResourceAdaptor) {
                provider.addConnectionListener(listener, filter);
            } else {
                throw new SecurityException("SBB may not add event listener");
            }
    }

    /**
     * Only the resource adaptor can add itself as a listener.
     * The resource adaptor should not allow any other objects to add themselves
     * as listeners.
     */
    public void addProviderListener(JccProviderListener listener) 
        throws ResourceUnavailableException, MethodNotSupportedException {
            if (listener instanceof JccResourceAdaptor) {
                provider.addProviderListener(listener);
            } else {
                throw new SecurityException("SBB may not add event listener");
            }
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createCall().
     */
    public JccCall createCall() 
        throws InvalidStateException, ResourceUnavailableException, 
            PrivilegeViolationException, MethodNotSupportedException {
        return new JccCallLocal(provider.createCall(), this);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterAddressRange(String, String, int, int).
     */
    public EventFilter createEventFilterAddressRange(String lowAddress, String highAddress, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterAddressRange(lowAddress, 
                    highAddress, matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterAddressRangeEx(String, int, int).
     */
    public EventFilter createEventFilterAddressRegEx(String addressRegEx, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterAddressRegEx(addressRegEx, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterAnd(EventFilter[], int).
     */
    public EventFilter createEventFilterAnd(EventFilter[] filters, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterAnd(filters, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterCauseCode(int, int, int).
     */
    public EventFilter createEventFilterCauseCode(int param, int param1, int param2) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterCauseCode(param, param1, param2);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterDestAddressRange(String, String, int, int).
     */
    public EventFilter createEventFilterDestAddressRange(String lowAddress, String highAddress, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterDestAddressRange(lowAddress, highAddress, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterDestAddressRangeEx(String, int, int).
     */
    public EventFilter createEventFilterDestAddressRegEx(String addressRegEx, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterDestAddressRegEx(addressRegEx, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterEventSet(int[], int[]).
     */
    public EventFilter createEventFilterEventSet(int[] blockEvents, int[] notifyEvents) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterEventSet(blockEvents, notifyEvents);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterMidCallEvent(int, String, int, int).
     */
    public EventFilter createEventFilterMidCallEvent(int midCallType, String midCallValue, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterMidCallEvent(midCallType, midCallValue, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterMinimunCollectedAddressLength(int, int, int).
     */
    public EventFilter createEventFilterMinimunCollectedAddressLength(int minLength, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterMinimunCollectedAddressLength(minLength, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterOr(EventFilter[], int).
     */
    public EventFilter createEventFilterOr(EventFilter[] filters, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterOr(filters, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterOrigAddressRange(String, String, int, int) .
     */
    public EventFilter createEventFilterOrigAddressRange(String lowAddress, String highAddress, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterOrigAddressRange(lowAddress, highAddress, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#createEventFilterOrigAddressRangeEx(String, int, int) .
     */
    public EventFilter createEventFilterOrigAddressRegEx(String addressRegEx, 
            int matchDisposition, int nomatchDisposition) 
        throws ResourceUnavailableException, InvalidArgumentException {
            return provider.createEventFilterOrigAddressRegEx(addressRegEx, 
                    matchDisposition, nomatchDisposition);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#getAddress(String)
     */
    public JccAddress getAddress(String str) throws InvalidPartyException {
        return provider.getAddress(str);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#getName().
     */
    public String getName() {
        return provider.getName();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#getState().
     */
    public int getState() {
        return provider.getState();
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeCallListener(JccCallListener listener) {
        if (listener instanceof JccResourceAdaptor) {
            provider.removeCallListener(listener);
        } else {
            throw new SecurityException("SBB may not remove listener");
        }
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeCallLoadControlListener(CallLoadControlListener listener) {
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeConnectionListener(JccConnectionListener listener) {
        if (listener instanceof JccResourceAdaptor) {
            provider.removeConnectionListener(listener);
        } else {
            throw new SecurityException("SBB may not remove listener");
        }
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeProviderListener(JccProviderListener listener) {
        if (listener instanceof JccResourceAdaptor) {
            provider.removeProviderListener(listener);
        } else {
            throw new SecurityException("SBB may not remove listener");
        }
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#setCallLoadControl(JccAddress[], souble, double[] int[]).
     */
    public void setCallLoadControl(JccAddress[] jccAddress, double param, double[] values, int[] values3) 
    throws MethodNotSupportedException {
        provider.setCallLoadControl(jccAddress, param, values, values3);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccProvider#shutdown().
     */
    public void shutdown() {
        provider.shutdown();
    }
    
}
