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
package org.mobicents.slee.resource.mediacontrol.wrapper.join;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.MediaSession;
import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.JoinEventListener;
import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.join.JoinableContainer;
import javax.media.mscontrol.join.JoinableStream;
import javax.media.mscontrol.join.JoinableStream.StreamType;

import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaObjectWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;

/**
 * @author baranowb
 * 
 */
public abstract class JoinableContainerWrapper extends MediaObjectWrapper implements JoinableContainerExt {
	// extending MediaObjectWrapper, since only MediaObject subclasses will
	// extend this class.
	// thank JSR309 class hierarchy chaos!

	protected final JoinableContainer wrappedJoinableContainer;
	protected final MediaSessionWrapper mediaSession;

	protected final Map<JoinableStream, JoinableStreamWrapper> joinableStreams = Collections.synchronizedMap(new HashMap<JoinableStream, JoinableStreamWrapper>());
	// FIXME: push method to JoinableExt ?
	protected final Map<Joinable, JoinableExt> joinees = new HashMap<Joinable, JoinableExt>();
	// we keep ref to listeners, since on release( )without unjoin impl will
	// generate unjoin, but activity will be dead
	// FIXME: investigate if RA should do if(joined){ wait for unjoins;
	// terminateActivity }else{ terminate activity }
	protected final Set<WrapperJoinEventListener> joinListeners = Collections.synchronizedSet(new HashSet<WrapperJoinEventListener>());

	/**
	 * @param wrappedObject
	 * @param mediaSession
	 * @param ra
	 */
	public JoinableContainerWrapper(MediaObject wrappedObject, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedObject, ra);
		if (mediaSession == null) {
			throw new IllegalArgumentException("MediaSession must not be null.");
		}
		this.mediaSession = mediaSession;
		// this may not work, however all JoinableContainers are MediaObjects?
		this.wrappedJoinableContainer = (JoinableContainer) wrappedObject;
	}

	
	public MediaSession getMediaSession() {
		return this.mediaSession;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.join.JoinableContainer#getJoinableStream(javax.
	 * media.mscontrol.join.JoinableStream.StreamType)
	 */
	
	public JoinableStream getJoinableStream(StreamType arg0) throws MsControlException {

		JoinableStream js = this.wrappedJoinableContainer.getJoinableStream(arg0);
		// check if we have wrapper for it.
		if (this.joinableStreams.containsKey(js)) {
			js = this.joinableStreams.get(js);
		} else {
			JoinableStreamWrapper jsw = new JoinableStreamWrapper(js, this, this.ra);
			this.joinableStreams.put(js, jsw);
			js = jsw;
		}
		return js;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.JoinableContainer#getJoinableStreams()
	 */
	
	public JoinableStream[] getJoinableStreams() throws MsControlException {
		JoinableStream[] streams = this.wrappedJoinableContainer.getJoinableStreams();
		if (this.joinableStreams.size() != streams.length) {
			for (int index = 0; index < streams.length; index++) {

				if (this.joinableStreams.containsKey(streams[index])) {
					streams[index] = this.joinableStreams.get(streams[index]);
				} else {
					JoinableStreamWrapper jsw = new JoinableStreamWrapper(streams[index], this, this.ra);
					this.joinableStreams.put(streams[index], jsw);
					streams[index] = jsw;
				}
			}
		}

		return streams;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.Joinable#getJoinees()
	 */
	
	public Joinable[] getJoinees() throws MsControlException {
		return this.joinees.values().toArray(new Joinable[this.joinees.size()]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.join.Joinable#getJoinees(javax.media.mscontrol.
	 * join.Joinable.Direction)
	 */
	
	public Joinable[] getJoinees(Direction direction) throws MsControlException {
		// damn tricky, depend on impl to show us what we must pass....
		Joinable[] filter = this.wrappedJoinableContainer.getJoinees(direction);
		for (int index = 0; index < filter.length; index++) {
			filter[index] = this.joinees.get(filter[index]);
		}

		return filter;
	}

	
	public void join(Direction direction, Joinable other) throws MsControlException {
		// should we fire event
		// this does nasty thing - lock thread.
		JoinableExt otherWrapper = (JoinableExt) other;
		WrapperJoinEventListener lst = new WrapperJoinEventListener(this, this, (JoinableExt) other);
		this.wrappedJoinableContainer.addListener(lst);
		this.joinListeners.add(lst); //
		try {

			this.wrappedJoinableContainer.join(direction, (Joinable) otherWrapper.getWrappedObject());

		} catch (MsControlException e) {
			// whooops
			this.wrappedJoinableContainer.removeListener(lst);
			this.joinListeners.remove(lst); //
			throw e;
		}
	}

	
	public void joinInitiate(Direction direction, Joinable other, Serializable ctx) throws MsControlException {
		// FIXME: this may not work properly if unjoin is called on other
		// FIXME 2: it does not work...
		// joinable.... depends how impl handles that
		// check it
		JoinableExt otherWrapper = (JoinableExt) other;
		WrapperJoinEventListener lst = new WrapperJoinEventListener(this, this, (JoinableExt) other);
		this.wrappedJoinableContainer.addListener(lst);
		this.joinListeners.add(lst);
		try {
			this.wrappedJoinableContainer.joinInitiate(direction, (Joinable) otherWrapper.getWrappedObject(), ctx);

		} catch (MsControlException e) {
			// whooops
			this.joinListeners.remove(lst);
			this.wrappedJoinableContainer.removeListener(lst);
			throw e;
		}
	}

	
	public void unjoin(Joinable other) throws MsControlException {
		JoinableExt otherWrapper = (JoinableExt) other;
		this.wrappedJoinableContainer.unjoin((Joinable) otherWrapper.getWrappedObject());
	}

	
	public void unjoinInitiate(Joinable other, Serializable ctx) throws MsControlException {
		JoinableExt otherWrapper = (JoinableExt) other;
		this.wrappedJoinableContainer.unjoinInitiate((Joinable) otherWrapper.getWrappedObject(), ctx);
	}

	
	public void release() {
		// remove join lsts
		Iterator<WrapperJoinEventListener> it = this.joinListeners.iterator();
		while (it.hasNext()) {
			JoinEventListener lst = it.next();
			it.remove();
			this.wrappedJoinableContainer.removeListener(lst);
		}
		this.mediaSession.release(this);
		super.release();
		// FIXME: add unjoin ops ?
	}

	// --------------------------- not allowed ---------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.join.JoinEventNotifier#addListener(javax.media.
	 * mscontrol.join.JoinEventListener)
	 */
	
	public void addListener(JoinEventListener arg0) {
		throw new SecurityException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.join.JoinEventNotifier#removeListener(javax.media
	 * .mscontrol.join.JoinEventListener)
	 */
	
	public void removeListener(JoinEventListener arg0) {
		throw new SecurityException();
	}

	// ---------------------- private/protected
	// return activity handle on which events must be delivered.
	protected abstract MsActivityHandle getEventHandle();

	
	public void addJoinee(JoinableExt otherJoinable) {
		this.joinees.put((Joinable) otherJoinable.getWrappedObject(), (JoinableExt) otherJoinable);
	}

	
	public void removeJoinee(JoinableExt otherJoinable) {
		this.joinees.remove((Joinable) otherJoinable.getWrappedObject());

	}

	// single event lst, so we know WHICH exactly we join/unjoin
	private class WrapperJoinEventListener implements JoinEventListener {
		// FIXME: make it object?
		private final JoinableContainerWrapper source;
		private final JoinableExt thisJoinable;
		private final JoinableExt otherJoinable;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.join.JoinEventListener#onEvent(javax.media.
		 * mscontrol.join.JoinEvent)
		 */
		/**
		 * @param source
		 * @param thisJoinable
		 * @param otherJoinable
		 */
		public WrapperJoinEventListener(JoinableContainerWrapper source, JoinableExt thisJoinable, JoinableExt otherJoinable) {
			super();
			this.source = source;
			this.thisJoinable = thisJoinable;
			this.otherJoinable = otherJoinable;

		}

		
		public void onEvent(JoinEvent event) {
			if (event.getThisJoinable().equals(this.thisJoinable.getWrappedObject()) && event.getOtherJoinable().equals(this.otherJoinable.getWrappedObject())) {
				JoinEventWrapper localEvent = new JoinEventWrapper(event, source, thisJoinable, otherJoinable);
				// TODO: currently there is bug in JSR? Joinable j1,j2;
				// j1.join(j2); j2.release(); does not trigger unjoin event....
				if (event.getEventType().equals(JoinEvent.JOINED)) {
					//

					thisJoinable.addJoinee(otherJoinable);
					otherJoinable.addJoinee(thisJoinable);
					ra.fireEvent(JoinEventWrapper.SLEE_EVENT_JOINED, getEventHandle(), localEvent);
				} else if (event.getEventType().equals(JoinEvent.UNJOINED)) {

					thisJoinable.removeJoinee(otherJoinable);
					otherJoinable.removeJoinee(thisJoinable);
					// remove.
					source.wrappedJoinableContainer.removeListener(this);
					source.joinListeners.remove(this);
					ra.fireEvent(JoinEventWrapper.SLEE_EVENT_UNJOINED, getEventHandle(), localEvent);

				}
			}

		}

	}

}
