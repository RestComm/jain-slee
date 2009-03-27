package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.runtime.sbbentity.SbbEntity;

public class ProfileManagementHandler {
	private static Logger logger = Logger.getLogger(ProfileManagementHandler.class);
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

		if (logger.isDebugEnabled()) {
			logger.debug("[profileInitialize] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profileLoad] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profileStore] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profileVerify] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profileActivate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profilePassivate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profilePostCreate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[profileRemove] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[setProfileContext] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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
		if (logger.isDebugEnabled()) {
			logger.debug("[unsetProfileContext] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
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

	// Usage methods. Here we can be static for sure. Rest must be tested.
	public static Object getProfileUsageParam(ProfileConcrete profileConcrete, String name) throws UnrecognizedUsageParameterSetNameException {

		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileUsageParam]: ProfileName = " + profileConcrete.getProfileName() + " , ProfileTableName = " + profileConcrete.getProfileTableConcrete().getProfileTableName()
					+ " , name = " + name);
		}
		if (name == null) {
			throw new NullPointerException("UsageParameterSet name must not be null.");
		}

		ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileTableConcrete();

		return profileTableConcrete.getProfileTableUsageMBean().getInstalledUsageParameterSet(name);
	}

	public static Object getProfileUsageParam(ProfileConcrete profileConcrete) {
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileUsageParam]: ProfileName = " + profileConcrete.getProfileName() + " , ProfileTableName = " + profileConcrete.getProfileTableConcrete().getProfileTableName());
		}

		ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileTableConcrete();

		return profileTableConcrete.getProfileTableUsageMBean().getInstalledUsageParameterSet(null);
	}

}
