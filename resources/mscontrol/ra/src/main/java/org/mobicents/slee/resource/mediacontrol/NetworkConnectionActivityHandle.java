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
import org.mobicents.slee.resource.mediacontrol.local.SdpPortManagerLocal;
import org.mobicents.slee.resource.mediacontrol.local.event.JoinEventLocal;
import org.mobicents.slee.resource.mediacontrol.local.event.SdpPortManagerEventLocal;

/**
 *
 * @author kulikov
 */
public class NetworkConnectionActivityHandle implements ActivityHandle, MediaEventListener, JoinEventListener {
    private NetworkConnectionLocal networkConnection;
    private McResourceAdaptor ra;
    
    public NetworkConnectionActivityHandle(McResourceAdaptor ra, NetworkConnectionLocal networkConnection) throws MsControlException {
        this.ra = ra;
        this.networkConnection = networkConnection;
        ((SdpPortManagerLocal)networkConnection.getSdpPortManager()).addListenerLocal(this);
        networkConnection.addActivityHandle(this);
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
        
        return ((NetworkConnectionActivityHandle)other).networkConnection.equals(this.networkConnection);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.networkConnection != null ? this.networkConnection.hashCode() : 0);
        return hash;
    }

    public void onEvent(MediaEvent event) {
		try {
			SdpPortManagerEventLocal localEvent = new SdpPortManagerEventLocal((SdpPortManagerEvent) event,
					(SdpPortManagerLocal) networkConnection.getSdpPortManager());
			if (event.getEventType().equals(SdpPortManagerEvent.OFFER_GENERATED)) {
				ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.OFFER_GENERATED", this, localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_GENERATED)) {
				ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_GENERATED", this, localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.ANSWER_PROCESSED)) {
				ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.ANSWER_PROCESSED", this, localEvent);
			} else if (event.getEventType().equals(SdpPortManagerEvent.NETWORK_STREAM_FAILURE)) {
				ra.fireEvent("javax.media.mscontrol.networkconnection.SdpPortManagerEvent.NETWORK_STREAM_FAILURE", this, localEvent);
				ra.terminateActivity(this);
			}
		} catch (MsControlException e) {
			e.printStackTrace();
		}
    }

    public void onEvent(JoinEvent event) {
    	//TODO: fix this once MediaGroup handles joinable properly.
    	JoinEventLocal localEvent = new JoinEventLocal(event,networkConnection);
        if (event.getEventType().equals(JoinEvent.JOINED)) {
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.JOINED", this, localEvent);
        } else if (event.getEventType().equals(JoinEvent.UNJOINED)) {
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.UNJOINED", this, localEvent);
        } 
    }
}
