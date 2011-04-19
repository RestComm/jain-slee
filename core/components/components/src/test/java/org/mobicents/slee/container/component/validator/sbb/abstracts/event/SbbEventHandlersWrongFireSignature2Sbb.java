/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/**
 * Start time:12:30:06 2009-02-05<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator.sbb.abstracts.event;

import javax.slee.ActivityContextInterface;
import javax.slee.Address;
import javax.slee.EventContext;
import javax.slee.InitialEventSelector;
import javax.slee.ServiceID;

import org.mobicents.slee.container.component.validator.sbb.abstracts.SbbConstraintsOkSbb;
import org.mobicents.slee.container.component.validator.sbb.abstracts.aci.ACIConstraintsOk;

/**
 * Start time:12:30:06 2009-02-05<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class SbbEventHandlersWrongFireSignature2Sbb extends SbbConstraintsOkSbb {

	public InitialEventSelector myInitialEventSelector(InitialEventSelector ies) {
		return null;
	}

	public void onCustomEventOne(XEvent event, ActivityContextInterface aci) {

	}

	public void onCustomEventTwo(XEvent event, ActivityContextInterface aci,
			EventContext ctx) {

	}

	public abstract void fireCustomEventThree(XEvent event,
			ActivityContextInterface aci, Address address);

	public abstract void fireCustomEventFour(XEvent event,
			ActivityContextInterface aci, Address address, String s);

	public void onCustomEventFive(XEvent event,
			ActivityContextInterface aci, EventContext ctx) {

	}

	public abstract void fireCustomEventFive(XEvent event,
			ActivityContextInterface aci, Address address, ServiceID SID);

	public void onCustomEventSix(XEvent event, ACIConstraintsOk aci) {

	}
}
