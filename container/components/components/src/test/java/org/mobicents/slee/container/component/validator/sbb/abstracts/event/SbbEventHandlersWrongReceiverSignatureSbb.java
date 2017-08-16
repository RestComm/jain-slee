/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * Start time:12:30:06 2009-02-05<br>
 * Project: restcomm-jainslee-server-core<br>
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
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class SbbEventHandlersWrongReceiverSignatureSbb extends SbbConstraintsOkSbb {

	public InitialEventSelector myInitialEventSelector(InitialEventSelector ies) {
		return null;
	}

	public void onCustomEventOne(XEvent event) {

	}

	public void onCustomEventTwo(XEvent event, ActivityContextInterface aci,
			EventContext ctx) {

	}

	public abstract void fireCustomEventThree(XEvent event,
			ActivityContextInterface aci, Address address);

	public abstract void fireCustomEventFour(XEvent event,
			ActivityContextInterface aci, Address address, ServiceID SID);

	public void onCustomEventFive(XEvent event,
			ActivityContextInterface aci, EventContext ctx) {

	}

	public abstract void fireCustomEventFive(XEvent event,
			ActivityContextInterface aci, Address address);

	public void onCustomEventSix(XEvent event, ACIConstraintsOk aci) {

	}
}
