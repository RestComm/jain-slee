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
package org.mobicents.slee.resource.mediacontrol.wrapper.vxml;

import java.net.URL;
import java.util.Map;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaEventListener;
import javax.media.mscontrol.MediaObject;
import javax.media.mscontrol.Parameters;
import javax.media.mscontrol.join.Joinable;
import javax.media.mscontrol.vxml.VxmlDialog;
import javax.media.mscontrol.vxml.VxmlDialogEvent;

import org.mobicents.slee.resource.mediacontrol.MsActivity;
import org.mobicents.slee.resource.mediacontrol.MsActivityHandle;
import org.mobicents.slee.resource.mediacontrol.MsResourceAdaptor;
import org.mobicents.slee.resource.mediacontrol.wrapper.MediaSessionWrapper;
import org.mobicents.slee.resource.mediacontrol.wrapper.join.JoinableContainerWrapper;

/**
 * @author baranowb
 * 
 */
public class VXMLDialogWrapper extends JoinableContainerWrapper implements VxmlDialog, MsActivity {

	protected final VxmlDialog wrappedVXMLDialog;
	protected final VXMLDialogEventListener vxmlDialogEventListener = new VXMLDialogEventListener(this);
	protected final MsActivityHandle activityHandle = new MsActivityHandle(this);
	private boolean activityTerminated = false;

	/**
	 * @param wrappedObject
	 * @param mediaSession
	 * @param ra
	 */
	public VXMLDialogWrapper(MediaObject wrappedObject, MediaSessionWrapper mediaSession, MsResourceAdaptor ra) {
		super(wrappedObject, mediaSession, ra);
		this.wrappedVXMLDialog = (VxmlDialog) wrappedObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.join.JoinableDialog#getJoinables()
	 */
	
	public Joinable[] getJoinables() {
		// damn tricky.... ech - why JSR creates special method for that...
		// instead to relly on reusing existing ones....
		// trickiest part will be .... that user wont be able to determine real
		// type... of it ech.
		return null;
	}

	
	public void acceptEvent(String arg0, Map<String, Object> arg1) {
		this.wrappedVXMLDialog.acceptEvent(arg0, arg1);
	}

	
	public void prepare(String arg0, Parameters arg1, Map<String, Object> arg2) {
		this.wrappedVXMLDialog.prepare(arg0, arg1, arg2);
	}

	
	public void prepare(URL arg0, Parameters arg1, Map<String, Object> arg2) {
		this.wrappedVXMLDialog.prepare(arg0, arg1, arg2);
	}

	
	public void start(Map<String, Object> arg0) {
		this.wrappedVXMLDialog.start(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.vxml.VxmlDialog#terminate(boolean)
	 */
	
	public void terminate(boolean immediatly) {
		// FIXME: add remove lst as conditional op??
		this.wrappedVXMLDialog.terminate(immediatly);
		if (!activityTerminated) {

			super.ra.endActivity(getActivityHandle());
			super.release();// this may be bad...
			activityTerminated = true;
		}

	}

	
	public void release() {

		if (!activityTerminated) {
			super.ra.endActivity(getActivityHandle());
			activityTerminated = true;
		}
		super.release();
	}

	// ------------- private ----------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.resource.mediacontrol.wrapper.join.
	 * JoinableContainerWrapper#getActivityHandle()
	 */
	
	public MsActivityHandle getActivityHandle() {
		return this.activityHandle;
}


	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.mediacontrol.wrapper.join.JoinableContainerWrapper#getEventHandle()
	 */
	
	protected MsActivityHandle getEventHandle() {
		return this.getActivityHandle();
	}
	// ------------------- private -----------------------

	private class VXMLDialogEventListener implements MediaEventListener<VxmlDialogEvent> {

		private final VXMLDialogWrapper source;

		/**
		 * @param vxmlDialogWrapper
		 */
		public VXMLDialogEventListener(VXMLDialogWrapper source) {
			this.source = source;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * javax.media.mscontrol.MediaEventListener#onEvent(javax.media.mscontrol
		 * .MediaEvent)
		 */
		
		public void onEvent(VxmlDialogEvent vxmlDialogEvent) {
			VXMLDialogEventWrapper localEvent = new VXMLDialogEventWrapper(vxmlDialogEvent, source);
			EventType eventType = localEvent.getEventType();
			if (eventType.equals(VxmlDialogEvent.DISCONNECTION_REQUESTED)) {
				try {
					ra.fireEvent(VXMLDialogEventWrapper.DISCONNECTION_REQUESTED, this.source.getActivityHandle(), localEvent);
					// FIXME: add release??
				} finally {
					ra.endActivity(this.source.getActivityHandle());
				}
			} else if (eventType.equals(VxmlDialogEvent.ERROR_EVENT)) {
				try {
					ra.fireEvent(VXMLDialogEventWrapper.ERROR_EVENT, this.source.getActivityHandle(), localEvent);
					// FIXME: add release??
				} finally {
					ra.endActivity(this.source.getActivityHandle());
				}
			} else if (eventType.equals(VxmlDialogEvent.EXITED)) {
				try {
					ra.fireEvent(VXMLDialogEventWrapper.EXITED, this.source.getActivityHandle(), localEvent);
					// FIXME: add release??
				} finally {
					ra.endActivity(this.source.getActivityHandle());
				}
			} else if (eventType.equals(VxmlDialogEvent.MIDCALL_EVENT_RECEIVED)) {
				ra.fireEvent(VXMLDialogEventWrapper.MIDCALL_EVENT_RECEIVED, this.source.getActivityHandle(), localEvent);
			} else if (eventType.equals(VxmlDialogEvent.PREPARED)) {
				ra.fireEvent(VXMLDialogEventWrapper.PREPARED, this.source.getActivityHandle(), localEvent);
			} else if (eventType.equals(VxmlDialogEvent.STARTED)) {
				ra.fireEvent(VXMLDialogEventWrapper.STARTED, this.source.getActivityHandle(), localEvent);
			} else {
				logger.warning("Received unknown VXMLDialog Event: " + eventType);
			}
		}

	}

	// ------------------- not allowed -------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#addListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void addListener(MediaEventListener<VxmlDialogEvent> arg0) {
		throw new SecurityException();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.media.mscontrol.MediaEventNotifier#removeListener(javax.media.mscontrol
	 * .MediaEventListener)
	 */
	
	public void removeListener(MediaEventListener<VxmlDialogEvent> arg0) {
		throw new SecurityException();

	}


}
