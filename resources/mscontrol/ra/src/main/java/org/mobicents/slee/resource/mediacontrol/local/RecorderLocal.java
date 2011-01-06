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
package org.mobicents.slee.resource.mediacontrol.local;

import java.net.URI;

import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Recorder;
import javax.media.mscontrol.mediagroup.RecorderEvent;
import javax.media.mscontrol.resource.RTC;

import org.mobicents.slee.resource.mediacontrol.MediaGroupActivityHandle;

/**
 * @author baranowb
 * 
 */
public class RecorderLocal implements Recorder {

	private Recorder recorder;
	private MediaGroupLocal mediaGroup;
	private MediaSessionLocal mediaSession;

	/**
	 * @param recorder
	 * @param mediaGroup
	 * @param mediaSession
	 */
	public RecorderLocal(Recorder recorder, MediaGroupLocal mediaGroup, MediaSessionLocal mediaSession) {
		super();
		this.recorder = recorder;
		this.mediaGroup = mediaGroup;
		this.mediaSession = mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.Resource#getContainer()
	 */
	@Override
	public MediaGroup getContainer() {
		return mediaGroup;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	@Override
	public void addListener(MediaEventListener<RecorderEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}
	
	public void addListenerLocal(MediaGroupActivityHandle arg0) {
		recorder.addListener(arg0);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEventNotifier#getMediaSession()
	 */
	@Override
	public MediaSession getMediaSession() {
		return mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	@Override
	public void removeListener(MediaEventListener<RecorderEvent> arg0) {
		throw new SecurityException("SBB can't register listener");

	}

	public void removeListenerLocal(MediaGroupActivityHandle arg0) {
		recorder.removeListener(arg0);

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.Recorder#record(java.net.URI,
	 * javax.media.mscontrol.resource.RTC[], javax.media.mscontrol.Parameters)
	 */
	@Override
	public void record(URI arg0, RTC[] arg1, Parameters arg2) throws MsControlException {
		recorder.record(arg0, arg1, arg2);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.Recorder#stop()
	 */
	@Override
	public void stop() {
		recorder.stop();

	}

}
