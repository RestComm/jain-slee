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
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.mediagroup.RecorderEvent;
import javax.slee.resource.ActivityHandle;

import org.mobicents.slee.resource.mediacontrol.local.MediaGroupLocal;
import org.mobicents.slee.resource.mediacontrol.local.MediaSessionLocal;
import org.mobicents.slee.resource.mediacontrol.local.PlayerLocal;
import org.mobicents.slee.resource.mediacontrol.local.RecorderLocal;
import org.mobicents.slee.resource.mediacontrol.local.event.JoinEventLocal;
import org.mobicents.slee.resource.mediacontrol.local.event.PlayerEventLocal;
import org.mobicents.slee.resource.mediacontrol.local.event.RecorderEventLocal;

/**
 *
 * @author kulikov
 */
public class MediaGroupActivityHandle implements ActivityHandle, JoinEventListener, MediaEventListener {
    private McResourceAdaptor ra;
    private MediaGroupLocal mediaGroup;
    
    public MediaGroupActivityHandle(McResourceAdaptor ra, MediaGroupLocal mediaGroup) {
        this.ra = ra;
        this.mediaGroup = mediaGroup;
        
        try {
            if (mediaGroup.getPlayer() != null) {
                ((PlayerLocal)mediaGroup.getPlayer()).addListenerLocal(this);
            }
        
            if (mediaGroup.getRecorder() != null) {
                ((RecorderLocal)mediaGroup.getRecorder()).addListenerLocal(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        
        return ((MediaGroupActivityHandle)other).mediaGroup.equals(this.mediaGroup);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.mediaGroup != null ? this.mediaGroup.hashCode() : 0);
        return hash;
    }

    public void onEvent(JoinEvent event) {
    	//TODO: fix this once MediaGroup handles joinable properly.
    	JoinEventLocal localEvent = new JoinEventLocal(event,mediaGroup);
        if (event.getEventType().equals(JoinEvent.JOINED)) {
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.JOINED", this, localEvent);
        } else if (event.getEventType().equals(JoinEvent.UNJOINED)) {
            ra.fireEvent("javax.media.mscontrol.join.JoinEvent.UNJOINED", this, localEvent);
        } 
    }

	public void onEvent(MediaEvent event) {

		if (event instanceof PlayerEvent) {
			try {
				PlayerEventLocal localEvent = new PlayerEventLocal((PlayerEvent)event,(PlayerLocal)mediaGroup.getPlayer());
				if (event.getEventType().equals(PlayerEvent.PLAY_COMPLETED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.PLAY_COMPLETED", this, localEvent);
				} else if (event.getEventType().equals(PlayerEvent.PAUSED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.PAUSED", this, localEvent);
				} else if (event.getEventType().equals(PlayerEvent.RESUMED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.RESUMED", this, localEvent);
				} else if (event.getEventType().equals(PlayerEvent.SPEED_CHANGED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.SPEED_CHANGED", this, localEvent);
				} else if (event.getEventType().equals(PlayerEvent.VOLUME_CHANGED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.VOLUME_CHANGED", this, localEvent);
				} else if (event.getEventType().equals(PlayerEvent.PAUSED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.PlayerEvent.PAUSED", this, localEvent);
				}
			} catch (MsControlException e) {
				e.printStackTrace();
			}
		} else
		{
			try {
				RecorderEventLocal localEvent = new RecorderEventLocal((RecorderEvent) event, (RecorderLocal) mediaGroup.getRecorder());
				if (event.getEventType().equals(RecorderEvent.PAUSED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.RecorderEvent.PAUSED", this, localEvent);
				} else if (event.getEventType().equals(RecorderEvent.RECORD_COMPLETED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.RecorderEvent.RECORD_COMPLETED", this, localEvent);
				} else if (event.getEventType().equals(RecorderEvent.RESUMED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.RecorderEvent.RESUMED", this, localEvent);
				} else if (event.getEventType().equals(RecorderEvent.STARTED)) {
					ra.fireEvent("javax.media.mscontrol.mediagroup.RecorderEvent.STARTED", this, localEvent);
				}
			} catch (MsControlException e) {
				e.printStackTrace();
			}
		}
	}
}
