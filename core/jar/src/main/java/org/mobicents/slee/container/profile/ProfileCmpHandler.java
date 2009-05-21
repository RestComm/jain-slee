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
	 * @param profileObject
	 * @throws IllegalStateException
	 * @throws ReadOnlyProfileException
	 */
	public static void beforeSetCmpField(ProfileObject profileObject) throws IllegalStateException, ReadOnlyProfileException {
		
		sleeContainer.getTransactionManager().mandateTransaction();
		
		if (profileObject.getState() != ProfileObjectState.READY && profileObject.getState() != ProfileObjectState.PROFILE_INITIALIZATION) {
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
	 */
	public static void afterSetCmpField(ProfileObject profileObject) {
		profileObject.getProfileEntity().markAsDirty();
		ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}
	
	/**
	 * 
	 * @param profileObject
	 * @throws IllegalStateException
	 */
	public static void beforeGetCmpField(ProfileObject profileObject) throws IllegalStateException {
		
		sleeContainer.getTransactionManager().mandateTransaction();

		// not a snapshot, so ensure object in ready state
		//if (profileObject.getState() != ProfileObjectState.READY && profileObject.getState() != ProfileObjectState.PROFILE_INITIALIZATION) {
		if (profileObject.getProfileEntity() == null) {
			throw new IllegalStateException("Profile object must be in ready state");
		}
		
		//ProfileCallRecorderTransactionData.addProfileCall(profileObject);			
	}

	/**
	 * 
	 * @param profileObject
	 */
	public static void afterGetCmpField(ProfileObject profileObject) {

		//ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
	}

}
