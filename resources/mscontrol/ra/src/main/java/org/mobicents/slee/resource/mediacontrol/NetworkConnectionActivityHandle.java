/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.slee.resource.mediacontrol;

import javax.media.mscontrol.MediaEvent;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.JoinEventListener;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.slee.resource.ActivityHandle;
import org.mobicents.slee.resource.mediacontrol.local.NetworkConnectionLocal;

/**
 *
 * @author kulikov
 */
public class NetworkConnectionActivityHandle implements ActivityHandle, MediaEventListener, JoinEventListener {
    private NetworkConnectionLocal connection;
    private McResourceAdaptor ra;
    
    public NetworkConnectionActivityHandle(McResourceAdaptor ra, NetworkConnectionLocal connection) throws MsControlException {
        this.ra = ra;
        this.connection = connection;
        connection.getSdpPortManager().addListener(this);
        connection.addActivityHandle(this);
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        
        if (other == null) {
            return false;
        }
        
        if (this.getClass() != other.getClass()) {
            return false;
        }
        
        return ((NetworkConnectionActivityHandle)other).connection.equals(this.connection);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.connection != null ? this.connection.hashCode() : 0);
        return hash;
    }

    public void onEvent(MediaEvent event) {
        if (event.getEventType().equals(SdpPortManagerEvent.OFFER_GENERATED)) {
            //TODO: wrap event to with local objects
            ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.OFFER_GENERATED", this, event);
        } else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_GENERATED)) {
            ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_GENERATED", this, event);
        } else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_PROCESSED)) {
            ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_PROCESSED", this, event);
        } else if (event.getEventType().equals(SdpPortManagerEvent.NETWORK_STREAM_FAILURE)) {
            ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.NETWORK_STREAM_FAILURE", this, event);
            ra.terminateActivity(this);
        }
    }

    public void onEvent(JoinEvent event) {
        if (event.getEventType().equals(JoinEvent.JOINED)) {
            //TODO: wrap event to with local objects
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.JOINED", this, event);
        } else if (event.getEventType().equals(JoinEvent.UNJOINED)) {
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.UNJOINED", this, event);
        } 
    }
}
