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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.media.mscontrol.MsControlException;
import javax.media.mscontrol.join.JoinEvent;
import javax.media.mscontrol.join.JoinEventListener;
import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.join.JoinableContainer;

import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;

/**
 * @author baranowb
 * 
 */
public abstract class JoinableWrapper implements JoinableExt {

	protected final Joinable wrappedObject;
	protected final JoinableContainerWrapper joinableContainer; // possible
																// father, used
																// as source. It
																// should be
																// "wrapper"
	protected final MsResourceAdaptor ra;

	protected final Map<Joinable, JoinableExt> joinees = new HashMap<Joinable, JoinableExt>();
	// we keep ref to listeners, since on release( )without unjoin impl will
	// generate unjoin, but activity will be dead
	// FIXME: investigate if RA should do if(joined){ wait for unjoins;
	// terminateActivity }else{ terminate activity }
	protected final Set<WrapperJoinEventListener> joinListeners = new HashSet<WrapperJoinEventListener>();

	/**
	 * @param wrappedObject
	 * @param joinableContainer
	 * @param ra
	 */
	public JoinableWrapper(Joinable wrappedObject, JoinableContainerWrapper joinableContainer, MsResourceAdaptor ra) {
		if (wrappedObject == null) {
			throw new IllegalArgumentException("Joinable must not be null.");
		}
		if (ra == null) {
			throw new IllegalArgumentException("MsResourceAdaptor must not be null.");
		}
		if (joinableContainer == null) {
			throw new IllegalArgumentException("JoinableContainer must not be null.");
		}
		this.ra = ra;
		this.wrappedObject = wrappedObject;
		this.joinableContainer = joinableContainer;
	}

	public JoinableContainer getContainer() {
		return joinableContainer;
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
		Joinable[] filter = this.wrappedObject.getJoinees(direction);
		for (int index = 0; index < filter.length; index++) {
			filter[index] = this.joinees.get(filter[index]);
		}

		return filter;
	}

	
	public void join(Direction direction, Joinable other) throws MsControlException {
		// should we fire event
		// this does nasty thing - lock thread.
		JoinableExt otherWrapper = (JoinableExt) other;
		WrapperJoinEventListener lst = new WrapperJoinEventListener(joinableContainer, this, (JoinableExt) other);
		this.joinableContainer.addListener(lst);
		this.joinListeners.add(lst);
		try {

			this.wrappedObject.join(direction, other);

		} catch (MsControlException e) {
			// whooops
			this.joinableContainer.removeListener(lst);
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
		WrapperJoinEventListener lst = new WrapperJoinEventListener(joinableContainer, this, (JoinableExt) other);
		this.joinableContainer.addListener(lst);
		this.joinListeners.add(lst);
		try {
			this.wrappedObject.joinInitiate(direction, (Joinable) otherWrapper.getWrappedObject(), ctx);

		} catch (MsControlException e) {
			// whooops
			this.joinListeners.remove(lst);
			this.joinableContainer.removeListener(lst);
			throw e;
		}
	}

	
	public void unjoin(Joinable other) throws MsControlException {
		JoinableExt otherWrapper = (JoinableExt) other;
		this.wrappedObject.unjoin((Joinable) otherWrapper.getWrappedObject());
	}

	
	public void unjoinInitiate(Joinable other, Serializable ctx) throws MsControlException {
		JoinableExt otherWrapper = (JoinableExt) other;
		this.wrappedObject.unjoinInitiate((Joinable) otherWrapper.getWrappedObject(), ctx);
	}

	// -------------------- private --------------------

	// returns activity handle on which we must deliver events from lst :)
	protected abstract MsActivityHandle getEventHandle();

	
	public void addJoinee(JoinableExt otherJoinable) {
		this.joinees.put((Joinable) otherJoinable.getWrappedObject(), otherJoinable);
	}

	
	public void removeJoinee(JoinableExt otherJoinable) {
		this.joinees.remove((Joinable) otherJoinable.getWrappedObject());

	}

	
	public Object getWrappedObject() {
		return this.wrappedObject;
	}

	// ----------------- private and priv classes

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
