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

import java.util.Map;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaErr;
import javax.media.mscontrol.resource.ResourceEvent;
import javax.media.mscontrol.vxml.VxmlDialog;
import javax.media.mscontrol.vxml.VxmlDialogEvent;

import org.mobicents.slee.resource.mediacontrol.wrapper.ResourceEventWrapper;

/**
 * @author baranowb
 * 
 */
public class VXMLDialogEventWrapper implements VxmlDialogEvent {

	public static final String DISCONNECTION_REQUESTED = "javax.media.mscontrol.vxml.VxmlDialogEvent.DISCONNECTION_REQUESTED";

	public static final String ERROR_EVENT = "javax.media.mscontrol.vxml.VxmlDialogEvent.ERROR_EVENT";

	public static final String EXITED = "javax.media.mscontrol.vxml.VxmlDialogEvent.EXITED";

	public static final String MIDCALL_EVENT_RECEIVED = "javax.media.mscontrol.vxml.VxmlDialogEvent.MIDCALL_EVENT_RECEIVED";

	public static final String PREPARED = "javax.media.mscontrol.vxml.VxmlDialogEvent.PREPARED";

	public static final String STARTED = "javax.media.mscontrol.vxml.VxmlDialogEvent.STARTED";

	protected final VXMLDialogWrapper source;
	protected final VxmlDialogEvent wrappedEvent;
	

	/**
	 * @param resourceEvent
	 */
	public VXMLDialogEventWrapper(VxmlDialogEvent resourceEvent, VXMLDialogWrapper source) {

		this.source = source;
		this.wrappedEvent = resourceEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.MediaEvent#getSource()
	 */
	
	public VxmlDialog getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.vxml.VxmlDialogEvent#getEventName()
	 */
	
	public String getEventName() {
		return this.wrappedEvent.getEventName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.media.mscontrol.vxml.VxmlDialogEvent#getNameList()
	 */
	
	public Map<String, Object> getNameList() {
		// FIXME: potentially dangerous?
		return this.wrappedEvent.getNameList();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getError()
	 */
	
	public MediaErr getError() {
		return this.wrappedEvent.getError();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getErrorText()
	 */
	
	public String getErrorText() {
		return this.wrappedEvent.getErrorText();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#getEventType()
	 */
	
	public EventType getEventType() {
		return this.wrappedEvent.getEventType();
	}

	/* (non-Javadoc)
	 * @see javax.media.mscontrol.MediaEvent#isSuccessful()
	 */
	
	public boolean isSuccessful() {
		return this.wrappedEvent.isSuccessful();
	}

}
