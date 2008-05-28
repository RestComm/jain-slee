package org.mobicents.slee.resource.rules.ra;

import javax.slee.UnrecognizedActivityException;
import javax.slee.UnrecognizedEventException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.resource.BootstrapContext;
import javax.slee.resource.SleeEndpoint;

import org.apache.log4j.Logger;
import org.drools.event.ObjectAssertedEvent;
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.WorkingMemoryEventListener;
import org.mobicents.slee.resource.rules.events.FactAssertedEvent;

public class CallWorkingMemoryListener implements WorkingMemoryEventListener {

	private static transient Logger logger = Logger
			.getLogger(CallWorkingMemoryListener.class);

	private RulesActivityHandle handle;

	private BootstrapContext bootstrapContext;

	// private Address address = new Address(AddressPlan.IP, "127.0.0.1");
	private SleeEndpoint sleeEndPoint = null;

	private EventLookupFacility eventLookup = null;

	// public CallWorkingMemoryListener() {

	// }

	public CallWorkingMemoryListener(RulesActivityHandle handle,
			BootstrapContext bootstrapContext) {
		this.handle = handle;
		this.bootstrapContext = bootstrapContext;
		this.sleeEndPoint = bootstrapContext.getSleeEndpoint();
		this.eventLookup = bootstrapContext.getEventLookupFacility();

	}

	public void objectAsserted(ObjectAssertedEvent arg0) {
		logger
				.debug("objectAsserted() of CallWorkingMemoryListener. Object Asserted");

		int eventID;

		FactAssertedEvent event = new FactAssertedEvent(this.handle);

		try {
			eventID = eventLookup.getEventID(
					"org.mobicents.slee.rules.FactAsserted", "org.mobicents",
					"1.0");
			sleeEndPoint.fireEvent(handle, (Object) event, eventID, null);
		} catch (UnrecognizedEventException uee) {
			logger.error("Caught an UnrecognizedEventException: ");
			uee.printStackTrace();
			throw new RuntimeException(
					"RAFrameResourceAdaptor.onEvent(): UnrecognizedEventException caught.",
					uee);
		} catch (UnrecognizedActivityException uaee) {
			logger.error("Caught an UnrecognizedActivityException: ");
			uaee.printStackTrace();
		}

	}

	public void objectModified(ObjectModifiedEvent arg0) {
		logger.debug("objectModified() of CallWorkingMemoryListener.");

	}

	public void objectRetracted(ObjectRetractedEvent arg0) {
		logger.debug("objectModified() of CallWorkingMemoryListener.");

	}

}
