package org.mobicents.slee.container.profile;

import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;

/**
 * Start time:18:25:55 2009-03-17<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This class encapsulates logic to set/get cmp field of profile,
 * acting as an interceptor.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileCmpHandler {

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param fieldName
	 * @param value
	 */
	public static void beforeSetCmpField(ProfileObject profileObject) throws IllegalStateException, ReadOnlyProfileException {
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		if (profileObject.getProfileEntity().getProfileName() != null && profileObject.getState() != ProfileObjectState.READY) {
			throw new IllegalStateException("Profile object must be in ready state");
		}
		
		if (profileObject.getProfileEntity().isReadOnly()) {				
			throw new ReadOnlyProfileException("Profile: " + profileObject.getProfileEntity().getProfileName() + ", table:" + profileObject.getProfileTableConcrete().getProfileTableName() + " ,is not writeable.");
		}
		
		ProfileCallRecorderTransactionData.addProfileCall(profileObject);			
	}

	/**
	 * 
	 * @param profileObject
	 * @param fieldName
	 * @param value
	 * @throws IllegalStateException
	 * @throws ReadOnlyProfileException
	 */
	public static void afterSetCmpField(ProfileObject profileObject) {

		profileObject.getProfileEntity().setDirty(true);
		ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}
	
	/**
	 * 
	 * @param fieldName
	 * @param value
	 */
	public static void beforeGetCmpField(ProfileObject profileObject) throws IllegalStateException {
		
		sleeContainer.getTransactionManager().mandateTransaction();

		// not a snapshot, so ensure object in ready state
		if (profileObject.getState() != ProfileObjectState.READY) {
			throw new IllegalStateException("Profile object must be in ready state");
		}
		
		ProfileCallRecorderTransactionData.addProfileCall(profileObject);			
	}

	/**
	 * 
	 * @param profileObject
	 * @param fieldName
	 * @param value
	 * @throws IllegalStateException
	 * @throws ReadOnlyProfileException
	 */
	public static void afterGetCmpField(ProfileObject profileObject) {

		ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}

}
