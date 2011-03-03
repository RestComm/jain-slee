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

import javax.media.mscontrol.MediaConfig;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.mediagroup.MediaGroup;
import javax.media.mscontrol.mediagroup.Player;
import javax.media.mscontrol.mediagroup.Recorder;
import javax.media.mscontrol.mediagroup.signals.SignalDetector;
import javax.media.mscontrol.mediagroup.signals.SignalGenerator;
import javax.media.mscontrol.resource.Action;
import javax.media.mscontrol.resource.AllocationEventListener;

import org.mobicents.slee.resource.mediacontrol.MsActivity;
import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.join.JoinableContainerWrapper;

/**
 * @author baranowb
 * 
 */
public class MediaGroupWrapper extends JoinableContainerWrapper implements MediaGroup, MsActivity {

	protected final MsActivityHandle handle = new MsActivityHandle(this);

	protected final MediaGroup wrappedMediaGroup;
	protected PlayerWrapper player;
	protected RecorderWrapper recorder;
	protected SignalDetectorWrapper signalDetector;
	protected SignalGeneratorWrapper signalGenerator;
	protected WrapperAllocationEventListener mgwAllocationEventWrapper = new WrapperAllocationEventListener(this,this.handle);
	/**
	 * @param wrappedObject
	 * @param ra
	 */
	public MediaGroupWrapper(MediaGroup wrappedMediaGroup, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedMediaGroup, mediaSession, ra);
		this.wrappedMediaGroup = wrappedMediaGroup;
		this.wrappedMediaGroup.addListener(this.mgwAllocationEventWrapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#confirm()
	 */
	
	public void confirm() throws MsControlException {
		this.wrappedMediaGroup.confirm();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.resource.ResourceContainer#getConfig()
	 */
	
	public MediaConfig getConfig() {
		return this.wrappedMediaGroup.getConfig();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#getResource(java.lang
	 * .Class)
	 */
	
	public <R> R getResource(Class<R> filter) throws MsControlException {
		// this will return some resource... player, recorder, speech
		// recognition... echl
		R filteredResource;
		try {
			filteredResource = filter.cast(player);
			return filteredResource;
		} catch (ClassCastException cce) {

		}
		try {
			filteredResource = filter.cast(recorder);
			return filteredResource;
		} catch (ClassCastException cce) {

		}
		try {
			filteredResource = filter.cast(signalDetector);
			return filteredResource;
		} catch (ClassCastException cce) {

		}
		try {
			filteredResource = filter.cast(signalGenerator);
			return filteredResource;
		} catch (ClassCastException cce) {

		}
		// TODO: add check in sub/super classes
		return this.wrappedMediaGroup.getResource(filter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.ResourceContainer#triggerAction(javax.
	 * media.mscontrol.resource.Action)
	 */
	
	public void triggerAction(Action action) {
		this.wrappedMediaGroup.triggerAction(action);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#release()
	 */
	
	public void release() {
		// call super, to release joined,inform MediaSessionWrapper, ...etc
		super.release();
		// issue term
		this.wrappedMediaGroup.removeListener(this.mgwAllocationEventWrapper);
		super.ra.endActivity(getActivityHandle());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.MediaGroup#getPlayer()
	 */
	
	public Player getPlayer() throws MsControlException {
		if (this.player == null) {
			Player realPlayer = this.wrappedMediaGroup.getPlayer();
			this.player = new PlayerWrapper(realPlayer, this, this.mediaSession, this.ra);
		}

		return this.player;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.MediaGroup#getRecorder()
	 */
	
	public Recorder getRecorder() throws MsControlException {
		if (this.recorder == null) {
			Recorder realRecorder = this.wrappedMediaGroup.getRecorder();
			this.recorder = new RecorderWrapper(realRecorder, this, this.mediaSession, this.ra);
		}

		return this.recorder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.MediaGroup#getSignalDetector()
	 */
	
	public SignalDetector getSignalDetector() throws MsControlException {
		if (this.signalDetector == null) {
			SignalDetector realSignalDetector = this.wrappedMediaGroup.getSignalDetector();
			this.signalDetector = new SignalDetectorWrapper(realSignalDetector, this, this.mediaSession);
		}

		return this.signalDetector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.MediaGroup#getSignalGenerator()
	 */
	
	public SignalGenerator getSignalGenerator() throws MsControlException {
		if (this.signalGenerator == null) {
			SignalGenerator realSignalGenerator = this.wrappedMediaGroup.getSignalGenerator();
			this.signalGenerator = new SignalGeneratorWrapper(realSignalGenerator, this, this.mediaSession);
		}

		return this.signalGenerator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.mediagroup.MediaGroup#stop()
	 */
	
	public void stop() {
		this.wrappedMediaGroup.stop();
	}

	// --------------------------- JSLEE specific ------------------
	
	protected MsActivityHandle getEventHandle() {
		return this.getActivityHandle();
	}

	/**
	 * @return
	 */
	public MsActivityHandle getActivityHandle() {
		return this.handle;
	}

	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.AllocationEventNotifier#addListener(javax
	 * .media.mscontrol.resource.AllocationEventListener)
	 */
	
	public void addListener(AllocationEventListener arg0) {
		throw new SecurityException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.resource.AllocationEventNotifier#removeListener
	 * (javax.media.mscontrol.resource.AllocationEventListener)
	 */
	
	public void removeListener(AllocationEventListener arg0) {
		throw new SecurityException();
	}

}
