package org.mobicents.slee.resource.rules.ra;

import javax.slee.resource.ActivityHandle;

import org.apache.log4j.Logger;
import org.mobicents.slee.resource.rules.ratype.RulesSession;

public class RulesActivityHandle implements ActivityHandle {

	private static Logger logger = Logger.getLogger(RulesActivityHandle.class);

	private String handle = null;

	public RulesActivityHandle(String id) {
		logger.debug("RulesActivityHandle(" + id + ") called.");
		this.handle = id;

	}

	public RulesActivityHandle(RulesSession rulesSession) {
		logger.debug("RulesActivityHandle(" + rulesSession + ") called.");
		this.handle = rulesSession.getId();
	}

	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((RulesActivityHandle)o).handle.equals(this.handle);
		}
		else {
			return false;
		}
	}

	// ActivityHandle interface
	public int hashCode() {
		return handle.hashCode();
	}
}
