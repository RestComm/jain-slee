package org.mobicents.slee.resource.rules.ra;

import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.jboss.logging.Logger;
import org.mobicents.slee.resource.rules.ratype.RulesSession;

public class RulesSessionImpl implements RulesSession {
	private Logger log = Logger.getLogger(this.getClass());

	String rulesFileName = null;

	WorkingMemory workingMemory = null;

	private String sessionID;

	public RulesSessionImpl(String rulesFileName, WorkingMemory workingMemory) {
		this.sessionID = (new UID()).toString();
		this.rulesFileName = rulesFileName;
		this.workingMemory = workingMemory;
	}

	public String getId() {
		return sessionID;
	}

	private WorkingMemory getWorkingMemory() {
		return workingMemory;
	}

	public List executeRules(final List objects) {

		WorkingMemory workingMemory = this.getWorkingMemory();

		final List results;
		Object obj = workingMemory.getGlobal("result");
		if (obj != null) {
			results = (List) obj;
			results.clear();
		} else {
			results = new ArrayList();
		}
		workingMemory.setGlobal("result", results);

		for (final Iterator objectIter = objects.iterator(); objectIter
				.hasNext();) {

			// Remember if Fact is assertedOnce we just need to call modify as
			// workingMemory keep's the reference
			// TODO Should implement Property Change Listener so that change in
			// property of Fact is taken care automatically
			Object fact = objectIter.next();
			FactHandle callFactHandle = workingMemory.getFactHandle(fact);
			if (callFactHandle != null) {
				workingMemory.modifyObject(callFactHandle, fact);
			} else {
				workingMemory.assertObject(fact);
			}

		}

		workingMemory.fireAllRules();

		return results;
	}

	public void dispose() {
		log
				.debug("dispose() of RulesSession called. Calling dispose on WorkingMemory");
		if (workingMemory != null) {
			workingMemory.dispose();
		}
	}

}
