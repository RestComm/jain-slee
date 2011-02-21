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
package org.mobicents.slee.resource.mediacontrol.wrapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.Parameter;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.resource.AllocationEvent;
import javax.media.mscontrol.resource.AllocationEventListener;
import javax.media.mscontrol.resource.ResourceContainer;
import javax.slee.facilities.Tracer;

import org.mobicents.slee.resource.mediacontrol.McActivityHandle;
import org.mobicents.slee.resource.mediacontrol.McResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.networkconnection.NetworkConnectionWrapper;

/**
 * Base class for all media objects, implements some basic methods... :)
 * 
 * @author baranowb
 * 
 */
public abstract class MediaObjectWrapper implements MediaObject, Wrapper {

	protected final Tracer logger;
	protected final MediaObject wrappedObject;
	protected final McResourceAdaptor ra;
	// map which holds childred if present. Maps real object to wrapper.
	protected final Map<MediaObject, MediaObjectWrapper> realToWrapperMap = Collections.synchronizedMap(new HashMap<MediaObject, MediaObjectWrapper>());

	/**
	 * 
	 */
	public MediaObjectWrapper(MediaObject wrappedObject, McResourceAdaptor ra) {
		if (wrappedObject == null) {
			throw new IllegalArgumentException("MediaObject must not be null.");
		}
		if (ra == null) {
			throw new IllegalArgumentException("McResourceAdaptor must not be null.");
		}
		this.ra = ra;
		this.wrappedObject = wrappedObject;
		this.logger = ra.getTracer(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#getMediaObjects()
	 */
	@Override
	public Iterator<MediaObject> getMediaObjects() {
		return new ArrayList<MediaObject>(this.realToWrapperMap.values()).iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#getMediaObjects(java.lang.Class)
	 */
	@Override
	public <T extends MediaObject> Iterator<T> getMediaObjects(Class<T> filter) {
		ArrayList<T> localList = new ArrayList<T>();
		Iterator<MediaObject> allObjects = this.getMediaObjects();
		while (allObjects.hasNext()) {
			MediaObject x = allObjects.next();
			try {
				T xx = filter.cast(x);
				localList.add(xx);
			} catch (ClassCastException cce) {
				// skip
			}
		}
		return localList.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#createParameters()
	 */
	@Override
	public Parameters createParameters() {
		return wrappedObject.createParameters();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaObject#getParameters(javax.media.mscontrol
	 * .Parameter[])
	 */
	@Override
	public Parameters getParameters(Parameter[] params) {

		return this.wrappedObject.getParameters(params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#getURI()
	 */
	@Override
	public URI getURI() {
		return this.wrappedObject.getURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaObject#setParameters(javax.media.mscontrol
	 * .Parameters)
	 */
	@Override
	public void setParameters(Parameters params) {
		this.wrappedObject.setParameters(params);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaObject#release()
	 */
	@Override
	public void release() {
		Set<MediaObjectWrapper> childs = new HashSet<MediaObjectWrapper>(this.realToWrapperMap.values());
		for (MediaObjectWrapper child : childs) {
			child.release(); // this will call release on real object and make
								// possible callbacks.

		}
		this.wrappedObject.release();
	}

	// --------------- Wrapper methods ------------

	public void release(Wrapper child) {
		// child has been released, remove if from local store.
		this.realToWrapperMap.remove(child.getWrappedObject());
	}

	@Override
	public MediaObject getWrappedObject() {
		return wrappedObject;
	}
	
	
	// -------------- private --------------
	
	public McResourceAdaptor getRA()
	{
		return this.ra;
	}
	
	
	//only exp, to check if its really needed.
	protected class WrapperAllocationEventListener implements AllocationEventListener {

		private final MediaObjectWrapper wrapper;
		private final McActivityHandle eventHandle; //to avoid definition in media object wrapper, we pass it as arg.
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.resource.AllocationEventListener#onEvent(javax
		 * .media.mscontrol.resource.AllocationEvent)
		 */
		/**
		 * @param wrapper
		 */
		public WrapperAllocationEventListener(MediaObjectWrapper wrapper,McActivityHandle eventHandle) {
			super();
			this.wrapper = wrapper;
			this.eventHandle = eventHandle;
		}

		@Override
		public void onEvent(AllocationEvent allocationEvent) {
			// event type is damn interface... ech.
			EventType type = allocationEvent.getEventType();
			if (type.equals(AllocationEvent.ALLOCATION_CONFIRMED)) {
				if(logger.isFineEnabled())
				{
					logger.fine("Allocation: " + type + ", on: " + wrapper);
				}
				//cast is valid, since only ResourceContainer will use this class
				ra.fireEvent(AllocationEventWrapper.ALLOCATION_CONFIRMED, eventHandle, new AllocationEventWrapper(allocationEvent, (ResourceContainer) wrapper));
			} else if (type.equals(AllocationEvent.IRRECOVERABLE_FAILURE)) {
				if(logger.isFineEnabled())
				{
					logger.fine("Allocation: " + type + ", on: " + wrapper);
				}
				try{
					
					ra.fireEvent(AllocationEventWrapper.IRRECOVERABLE_FAILURE, eventHandle, new AllocationEventWrapper(allocationEvent, (ResourceContainer) wrapper));
				}finally
				{
					onFailure();
				}
			} else {
				if(logger.isWarningEnabled())
				{
					logger.warning("Allocation(unknown!): " + type + ", on: " + wrapper);
				}
			}
		}
		public void onFailure()
		{
			ra.endActivity(eventHandle);
		}

	}

}
