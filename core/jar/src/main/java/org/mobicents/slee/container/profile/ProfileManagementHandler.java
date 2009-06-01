package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * ProfileManagementHandler.java
 * 
 * <br>
 * Project: mobicents <br>
 * 3:26:16 PM Apr 3, 2009 <br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileManagementHandler {

	private static Logger logger = Logger
			.getLogger(ProfileManagementHandler.class);

	public static boolean isProfileDirty(ProfileObject profileObject) {
		if (logger.isDebugEnabled()) {
			logger.info("[isProfileDirty] @ " + profileObject);
		}

		return profileObject.getProfileEntity().isDirty();
	}

	public static boolean isProfileValid(ProfileObject profileObject,
			ProfileID profileId) throws NullPointerException, SLEEException {
		if (logger.isDebugEnabled()) {
			logger.info("[isProfileValid(" + profileId + ")] @ "
					+ profileObject);
		}

		if (profileId == null) {
			throw new NullPointerException("null profile id");
		}
		
		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		
		boolean terminateTx = txManager.requireTransaction(); 
		try {
			ProfileTableImpl profileTable = sleeContainer.getSleeProfileTableManager().getProfileTable(profileId.getProfileTableName());
			return profileTable.profileExists(profileId.getProfileName());
		}
		catch (UnrecognizedProfileTableNameException e) {
			return false;
		}
		catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		finally {
			try {
				txManager.requireTransactionEnd(terminateTx, false);
			} catch (Throwable e) {
				logger.error(e.getMessage(),e);
			}
		}
	}

	public static void markProfileDirty(ProfileObject profileObject) {
		if (logger.isDebugEnabled()) {
			logger.info("[markProfileDirty] @ " + profileObject);
		}

		profileObject.getProfileEntity().setDirty(true);
	}

	public static void profileInitialize(ProfileObject profileObject) {
		
	}

	public static void profileLoad(ProfileObject profileObject) {
		
	}

	public static void profileStore(ProfileObject profileObject) {
		
	}

	public static void profileVerify(ProfileObject profileObject)
			throws ProfileVerificationException {
	
	}

	public static void profileActivate(ProfileObject profileObject) {
	
	}

	public static void profilePassivate(ProfileObject profileObject) {
	
	}

	public static void profilePostCreate(ProfileObject profileObject)
			throws CreateException {
	
	}

	public static void profileRemove(ProfileObject profileObject) {
		
	}

	public static void setProfileContext(ProfileObject profileObject,
			ProfileContext profileContext) {
		
	}

	public static void unsetProfileContext(ProfileObject profileObject) {
		
	}

	// Usage methods. Here we can be static for sure. Rest must be tested.
	public static Object getProfileUsageParam(ProfileObject profileObject,
			String name) throws UnrecognizedUsageParameterSetNameException {
		if (logger.isDebugEnabled()) {
			logger.info("[getProfileUsageParam(" + name + ")] @ "
					+ profileObject);
		}

		if (name == null) {
			throw new NullPointerException(
					"UsageParameterSet name must not be null.");
		}

		ProfileTableImpl profileTable = profileObject
				.getProfileTable();

		return profileTable.getProfileTableUsageMBean()
				.getInstalledUsageParameterSet(name);
	}

	public static Object getProfileDefaultUsageParam(
			ProfileObject profileObject) {
		if (logger.isDebugEnabled()) {
			logger.info("[getProfileDefaultUsageParam] @ "
					+ profileObject);
		}

		ProfileTableImpl profileTable = profileObject
				.getProfileTable();

		return profileTable.getProfileTableUsageMBean()
				.getInstalledUsageParameterSet(null);
	}

	private static ClassLoader switchContextClassLoader(
			ClassLoader newClassLoader) {
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(newClassLoader);

		return oldClassLoader;
	}

}
