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
package org.mobicents.slee.resource.mediacontrol.wrapper.mediagroup;

import java.net.URI;

import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.resource.RTC;

import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper;

/**
 * @author baranowb
 * 
 */
public class PlayerWrapper implements Player, Wrapper {

	protected final Player wrappedPlayer;
	protected final MediaGroupWrapper mediaGroup;
	protected final MediaSessionWrapper mediaSession;
	protected final MsResourceAdaptor ra;
	protected final PlayerEventWrapperListener playerListener = new PlayerEventWrapperListener(this);

	/**
	 * @param wrappedObject
	 * @param ra
	 */
	public PlayerWrapper(Player wrappedObject, MediaGroupWrapper mediaGroup, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		if (wrappedObject == null) {
			throw new IllegalArgumentException("Player must not be null.");
		}
		if (mediaGroup == null) {
			throw new IllegalArgumentException("MediaGroup must not be null.");
		}
		if (mediaSession == null) {
			throw new IllegalArgumentException("MediaSession must not be null.");
		}
		if (ra == null) {
			throw new IllegalArgumentException("MsResourceAdaptor must not be null.");
		}
		this.mediaGroup = mediaGroup;
		this.mediaSession = mediaSession;
		this.wrappedPlayer = wrappedObject;
		this.ra = ra;
		this.wrappedPlayer.addListener(this.playerListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.Player#play(java.net.URI[],
	 * javax.media.mscontrol.resource.RTC[], javax.media.mscontrol.Parameters)
	 */
	
	public void play(URI[] uri, RTC[] rtc, Parameters parameters) throws MsControlException {
		this.wrappedPlayer.play(uri, rtc, parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.Player#play(java.net.URI,
	 * javax.media.mscontrol.resource.RTC[], javax.media.mscontrol.Parameters)
	 */
	
	public void play(URI uri, RTC[] rtc, Parameters parameters) throws MsControlException {
		this.wrappedPlayer.play(uri, rtc, parameters);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.Player#stop(boolean)
	 */
	
	public void stop(boolean stopAll) {
		this.wrappedPlayer.stop(stopAll);

	}

	public MediaGroup getContainer() {
		return this.mediaGroup;
	}

	public MediaSession getMediaSession() {
		return this.mediaSession;
	}

	// --------------------------- not allowed ---------------------
	public void addListener(MediaEventListener<PlayerEvent> arg0) {
		throw new SecurityException();
	}

	public void removeListener(MediaEventListener<PlayerEvent> arg0) {
		throw new SecurityException();
	}

	// --------------------------- private --------------------------
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.slee.resource.mediacontrol.wrapper.Wrapper#getWrappedObject
	 * ()
	 */
	
	public Object getWrappedObject() {
		return this.wrappedPlayer;
	}

	private class PlayerEventWrapperListener implements MediaEventListener<PlayerEvent> {
		private final PlayerWrapper wrapper;

		/**
		 * @param wrapper
		 */
		public PlayerEventWrapperListener(PlayerWrapper wrapper) {
			super();
			this.wrapper = wrapper;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MediaEventListener#onEvent(javax.media.mscontrol
		 * .MediaEvent)
		 */
		
		public void onEvent(PlayerEvent event) {
			PlayerEventWrapper localEvent = new PlayerEventWrapper(event, wrapper);
			if (event.getEventType().equals(PlayerEvent.PLAY_COMPLETED)) {
				ra.fireEvent(localEvent.PLAY_COMPLETED, mediaGroup.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(PlayerEvent.PAUSED)) {
				ra.fireEvent(localEvent.PAUSED, mediaGroup.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(PlayerEvent.RESUMED)) {
				ra.fireEvent(localEvent.RESUMED, mediaGroup.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(PlayerEvent.SPEED_CHANGED)) {
				ra.fireEvent(localEvent.SPEED_CHANGED, mediaGroup.getActivityHandle(), localEvent);
			} else if (event.getEventType().equals(PlayerEvent.VOLUME_CHANGED)) {
				ra.fireEvent(localEvent.VOLUME_CHANGED, mediaGroup.getActivityHandle(), localEvent);
			}

		}

	}

}
