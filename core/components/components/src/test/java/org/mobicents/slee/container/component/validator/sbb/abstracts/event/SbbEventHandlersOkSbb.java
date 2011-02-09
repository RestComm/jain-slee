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
public abstract class SbbEventHandlersOkSbb extends SbbConstraintsOkSbb {

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
			ActivityContextInterface aci, Address address, ServiceID SID);

	public void onCustomEventFive(XEvent event,
			ActivityContextInterface aci, EventContext ctx) {

	}

	public abstract void fireCustomEventFive(XEvent event,
			ActivityContextInterface aci, Address address);

	public void onCustomEventSix(XEvent event, ACIConstraintsOk aci) {

	}
}
