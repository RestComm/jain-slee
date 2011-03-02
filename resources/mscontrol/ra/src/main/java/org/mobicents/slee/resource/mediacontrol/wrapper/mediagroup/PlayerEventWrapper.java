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

import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.resource.Action;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 * 
 */
public class PlayerEventWrapper extends ResourceEventWrapper implements PlayerEvent {
	public static final String PLAY_COMPLETED = "javax.media.mscontrol.mediagroup.PlayerEvent.PLAY_COMPLETED";

	public static final String PAUSED = "javax.media.mscontrol.mediagroup.PlayerEvent.PAUSED";

	public static final String RESUMED = "javax.media.mscontrol.mediagroup.PlayerEvent.RESUMED";

	public static final String SPEED_CHANGED = "javax.media.mscontrol.mediagroup.PlayerEvent.SPEED_CHANGED";

	public static final String VOLUME_CHANGED = "javax.media.mscontrol.mediagroup.PlayerEvent.VOLUME_CHANGED";

	private PlayerWrapper player;

	/**
	 * @param resourceEvent
	 */
	public PlayerEventWrapper(PlayerEvent resourceEvent, PlayerWrapper player) {
		super(resourceEvent);
		this.player = player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public Player getSource() {
		return player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getChangeType()
	 */
	
	public Action getChangeType() {
		return ((PlayerEvent) super.resourceEvent).getChangeType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getIndex()
	 */
	
	public int getIndex() {
		return ((PlayerEvent) super.resourceEvent).getIndex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getOffset()
	 */
	
	public int getOffset() {
		return ((PlayerEvent) super.resourceEvent).getOffset();
	}

}
