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
package org.mobicents.slee.resource.mediacontrol.local.event;

import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.PlayerEvent;
import javax.media.mscontrol.resource.Action;

import org.mobicents.slee.resource.mediacontrol.local.PlayerLocal;

/**
 * @author baranowb
 *
 */
public class PlayerEventLocal extends ResourceEventLocal implements PlayerEvent{

	private PlayerLocal player;
	
	/**
	 * @param resourceEvent
	 */
	public PlayerEventLocal(PlayerEvent resourceEvent,PlayerLocal player) {
		super(resourceEvent);
		this.player = player;
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	@Override
	public Player getSource() {
		return player;
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getChangeType()
	 */
	@Override
	public Action getChangeType() {
		return ((PlayerEvent)super.resourceEvent).getChangeType();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getIndex()
	 */
	@Override
	public int getIndex() {
		return ((PlayerEvent)super.resourceEvent).getIndex();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.mediagroup.PlayerEvent#getOffset()
	 */
	@Override
	public int getOffset() {
		return ((PlayerEvent)super.resourceEvent).getOffset();
	}

	

}
