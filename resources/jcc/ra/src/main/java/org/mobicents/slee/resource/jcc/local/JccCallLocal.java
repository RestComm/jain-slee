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

/*
 * File Name     : JccCallLocal.java
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
 * Wraps JccCall to disallow addConnectionListener, addCallListener methods. 
 * When a disallowed method is invoked, the resource adaptor
 * entity throws a SecurityException.
 *
 * @author Oleg Kulikov
 * @author Pavel Mitrenko
 */
public class JccCallLocal implements JccCall {
    
    private JccCall call;
    private JccProviderLocal provider;
    
    /** Creates a new instance of JccCallLocal */
    public JccCallLocal(JccCall call, JccProviderLocal provider) {
        this.call = call;
        this.provider = provider;
    }

    /**
     * Only the resource adaptor can add itself as a listener.
     * The resource adaptor should not allow any other objects to add themselves
     * as listeners.
     */
    public void addCallListener(JccCallListener listener) 
        throws ResourceUnavailableException, MethodNotSupportedException {
            if (listener instanceof JccResourceAdaptor) {
                call.addCallListener(listener);
            } else {
                throw new SecurityException("SBB may not add event listener");
            }
    }

    /**
     * Only the resource adaptor can add itself as a listener.
     * The resource adaptor should not allow any other objects to add themselves
     * as listeners.
     */
    public void addConnectionListener(JccConnectionListener listener, EventFilter eventFilter) 
        throws ResourceUnavailableException, MethodNotSupportedException {
            if (listener instanceof JccResourceAdaptor) {
                call.addCallListener(listener);
            } else {
                throw new SecurityException("SBB may not add event listener");
            }
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#connect(String, String).
     */
    public JccConnection[] connect(JccAddress origAddress, String dialedDigits) 
        throws ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException, InvalidStateException, MethodNotSupportedException {
            JccConnection[] connections = call.connect(origAddress, dialedDigits);
            JccConnectionLocal[] localConnections = new JccConnectionLocal[connections.length];
            
            for (int i = 0; i < connections.length; i++) {
                localConnections[i] = new JccConnectionLocal(connections[i], provider, this);
            }
            
            return localConnections;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#createConnection(String, String, String, String).
     */
    public JccConnection createConnection(String targetAddress, String originatingAddress, 
            String originalCalledAddress, String redirectingAddress) 
        throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidArgumentException, InvalidPartyException {
            return new JccConnectionLocal(call.createConnection(
                    targetAddress, originatingAddress, 
                    originalCalledAddress, redirectingAddress), 
                    provider, this);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#getConnections().
     */
    public JccConnection[] getConnections() {
        JccConnection[] connections = call.getConnections();
        JccConnectionLocal[] localConnections = new JccConnectionLocal[connections.length];
            
        for (int i = 0; i < connections.length; i++) {
            localConnections[i] = new JccConnectionLocal(connections[i], provider, this);
        }
            
        return localConnections;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#getProvider().
     */
    public JccProvider getProvider() {
        return provider;
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#getState().
     */
    public int getState() {
        return call.getState();
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#release(int).
     */
    public void release(int causeCode) 
        throws PrivilegeViolationException, ResourceUnavailableException, 
            InvalidStateException, InvalidArgumentException {
        call.release(causeCode);
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeCallListener(JccCallListener listener) {
            if (listener instanceof JccResourceAdaptor) {
                call.removeCallListener(listener);
            } else {
                throw new SecurityException("SBB may not remove event listener");
            }
    }

    /**
     * Only the resource adaptor can remove  a listener.
     */
    public void removeConnectionListener(JccConnectionListener listener) {
            if (listener instanceof JccResourceAdaptor) {
                call.removeConnectionListener(listener);
            } else {
                throw new SecurityException("SBB may not remove event listener");
            }
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#routeCall(String, String, String, String).
     */
    public JccConnection routeCall(String targetAddress, String originatingAddress, 
            String originalDestinationAddress, String redirectingAddress) 
        throws InvalidStateException, ResourceUnavailableException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException, InvalidArgumentException {
        return new JccConnectionLocal(call.routeCall(targetAddress, originatingAddress, 
                originalDestinationAddress, redirectingAddress), 
                provider, this);
    }

    /**
     * (Non-Javadoc)
     * @see javax.csapi.cc.jcc.JccCall#superviseCall(JccCallListener, double, int).
     */
    public void superviseCall(JccCallListener listener, double time, int treatment) throws MethodNotSupportedException {
        call.superviseCall(listener, time, treatment);
    }
    
    public boolean equals(Object other) {
        return other instanceof JccCallLocal  && ((JccCallLocal)other).call == call;
    }

	public String toString() {
		return call.toString();
	}
}
