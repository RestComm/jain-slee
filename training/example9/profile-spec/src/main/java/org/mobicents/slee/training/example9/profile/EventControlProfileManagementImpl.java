package org.mobicents.slee.training.example9.profile;

import javax.slee.CreateException;
import javax.slee.facilities.Tracer;
import javax.slee.profile.Profile;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileVerificationException;

/**
 * EventControlProfileManagementImpl implementation of CMP's with
 * ProfileManagement
 * 
 * @author MoBitE info@mobite.co.in
 * 
 */
public abstract class EventControlProfileManagementImpl implements Profile,
		EventControlProfileCMP {

	private ProfileContext profileCtx;
	private Tracer tracer;

	public void profileInitialize() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileInitialize() called");
		}
		setActivityId(null);
		setAny(false);
		setInit(false);
	}

	public void profileLoad() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileLoad() called");
		}
	}

	public void profileStore() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileStore() called");
		}
	}

	public void profileVerify() throws ProfileVerificationException {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileVerify() called");
		}
		String action = getActivityId();
		if (action != null)
			verifyAction(action);

	}

	private void verifyAction(String value) throws ProfileVerificationException {
		try {
			int i = Integer.parseInt(value);
		} catch (NumberFormatException ne) {
			this.tracer.severe("verifyAction failed for value" + value);

			throw new ProfileVerificationException("Invalid Activity ID "
					+ value + " is not a valid action");
		}
	}

	public void profileActivate() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileActivate() called");
		}
	}

	public void profilePassivate() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profilePassivate() called");
		}
	}

	public void profilePostCreate() throws CreateException {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profilePostCreate() called");
		}
	}

	public void profileRemove() {
		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("profileRemove() called");
		}
	}

	public void setProfileContext(ProfileContext ctx) {
		this.profileCtx = ctx;
		this.tracer = this.profileCtx
				.getTracer(EventControlProfileManagementImpl.class
						.getSimpleName());

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("setProfileContext() called");
		}

	}

	public void unsetProfileContext() {
		this.profileCtx = null;

		if (this.tracer.isFineEnabled()) {
			this.tracer.fine("unsetProfileContext() called");
		}
	}
}
