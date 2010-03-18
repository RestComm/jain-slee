package org.mobicents.slee.container.component.validator.sbb.abstracts.profilecmp;

import javax.naming.Context;
import javax.slee.ActivityContextInterface;
import javax.slee.RolledBackContext;
import javax.slee.SbbContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

public abstract class SbbConstraintsProfileCMPWrongParameterSbb implements javax.slee.Sbb {

	/**
	 * Called when an sbb object is instantied and enters the pooled state.
	 */
	public void setSbbContext(SbbContext context) {

	}

	/*
	 * Init the connection and retrieve data when the service is activated by
	 * SLEE
	 */
	public void onServiceStartedEvent(
			javax.slee.serviceactivity.ServiceStartedEvent event,
			ActivityContextInterface aci) {

	}

	public void unsetSbbContext() {

	}

	public void sbbCreate() throws javax.slee.CreateException {

	}

	public void sbbPostCreate() throws javax.slee.CreateException {

	}

	public void sbbActivate() {

	}

	public void sbbPassivate() {

	}

	public void sbbRemove() {

	}

	public void sbbLoad() {

	}

	public void sbbStore() {

	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {

	}

	public void sbbRolledBack(RolledBackContext sbbRolledBack) {

	}

	protected SbbContext getSbbContext() {
		return null;
	}

	// fake cmp interface
	public abstract ProfileCmpInterface getFirstProfileCMP(String id)
			throws UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException;

	public abstract SuperProfileInterface probeSecondProfileCMP(ProfileID id)
			throws UnrecognizedProfileTableNameException,
			UnrecognizedProfileNameException;

}