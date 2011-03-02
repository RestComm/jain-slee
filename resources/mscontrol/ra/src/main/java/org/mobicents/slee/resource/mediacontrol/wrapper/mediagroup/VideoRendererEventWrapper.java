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

import javax.media.mscontrol.resource.ResourceEvent;
import javax.media.mscontrol.resource.video.VideoRenderer;
import javax.media.mscontrol.resource.video.VideoRendererEvent;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 * 
 */
public class VideoRendererEventWrapper extends ResourceEventWrapper implements VideoRendererEvent {

	public static final String RENDERING_COMPLETED = "javax.media.mscontrol.resource.video.VideoRendererEvent.RENDERING_COMPLETED";

	private final VideoRenderer source; // TODO: change to wrapper.

	/**
	 * @param resourceEvent
	 */
	public VideoRendererEventWrapper(ResourceEvent resourceEvent, VideoRenderer source) {
		super(resourceEvent);
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public VideoRenderer getSource() {
		return this.source;
	}

}
