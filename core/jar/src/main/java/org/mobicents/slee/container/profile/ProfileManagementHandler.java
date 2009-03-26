package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;

public class ProfileManagementHandler {

	private ProfileCmpHandler profileCmpHandler = null;
	private ProfileObject profileObject = null;

	public void setProfileCmpHandler(ProfileCmpHandler profileCmpHandler) {
		this.profileCmpHandler = profileCmpHandler;
	}

	public void setProfileObject(ProfileObject profileObject) {
		this.profileObject = profileObject;
	}

	public boolean isProfileDirty() {
		return profileObject.getProfileConcrete().getProfileDirty();
	}

	public boolean isProfileValid(ProfileID arg0) throws NullPointerException, SLEEException {
		// TODO Auto-generated method stub
		return false;
	}

	public void markProfileDirty() {

		profileObject.getProfileConcrete().setProfileDirty(true);

	}

	public void profileInitialize() {

		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profileLoad() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profileStore() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profileVerify() throws ProfileVerificationException {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profileActivate() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profilePassivate() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}
	}

	public void profilePostCreate() throws CreateException {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void profileRemove() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}
	}

	public void setProfileContext(ProfileContext arg0) {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

	public void unsetProfileContext() {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		try {
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: ??
		} finally {
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}

	}

}
