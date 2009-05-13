package org.mobicents.slee.runtime.facilities.profile;

import java.util.HashMap;
import java.util.Map;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileObjectPool;
import org.mobicents.slee.container.profile.ProfileTableTransactionView;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski</a>
 * @author martins       
 */
public abstract class AbstractProfileEvent {

	/**
	 * the address pointing to the event's profile
	 */
	private final Address profileAddress;

	/**
	 * the id of the event's profile 
	 */
	private final ProfileID profileID;
	
	/**
	 * a snapshot of the event's profile, when the event occurred 
	 */
	private final ProfileEntity profileAfterAction;

	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param profileEntityAfterAction
	 */
	AbstractProfileEvent(ProfileEntity profileEntityAfterAction) {
		
		this.profileAfterAction = profileEntityAfterAction;
		this.profileAddress = new Address(AddressPlan.SLEE_PROFILE, profileEntityAfterAction.getTableName() + "/"
				+ profileEntityAfterAction.getProfileName());
		this.profileID = new ProfileID(this.profileAddress);		
	}

	/**
	 * Retrieves the address pointing to the event's profile
	 * @return
	 */
	public Address getProfileAddress() {
		return profileAddress;
	}
	
	/**
	 * Retrieves a snapshot of the instance of the event's profile, when the event occurred
	 * @return
	 */
	public ProfileEntity getProfileConcreteAfterAction() {
		return profileAfterAction;
	}
	
	/**
	 * Retrieves the id of the event's profile
	 * @return
	 */
	public ProfileID getProfile() {
		return profileID;
	}
	
	/**
	 * Verifies if the specified class can be loaded by current thread class loader 
	 * @return
	 */
	boolean isProfileClassVisible() {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(profileAfterAction.getClass().getName());
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * Retrieves a local object valid for thus current transaction.
	 * 
	 * @param profilePojo
	 * @return
	 * @throws TransactionRequiredLocalException
	 */
	ProfileObject getProfileObjectValidInCurrentTransaction(ProfileEntity profileEntity) throws TransactionRequiredLocalException {
		// check tx
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		txManager.mandateTransaction();
		
		// look for an assigned object in local map
		if (txData == null) {
			txData = new HashMap<ProfileEntity, ProfileObject>();
		}
		ProfileObject profileObject = (ProfileObject) txData.get(profileEntity);
		if (profileObject == null) {
			// get an object from the table
			profileEntity.setReadOnly(true);
			ProfileObjectPool pool = sleeContainer.getProfileObjectPoolManagement().getObjectPool(profileEntity.getTableName());
			profileObject = pool.borrowObject();
			profileObject.profileActivate(profileEntity);
			ProfileTableTransactionView.passivateProfileObjectOnTxEnd(txManager, profileObject, pool);
			txData.put(profileEntity, profileObject);
		}
		return profileObject;
	}
	
	/**
	 * a map to hold loaded profile objects
	 */
	private Map<ProfileEntity,ProfileObject> txData = null;
	
	/**
	 * Retrieves the event type id for the event object
	 * @return
	 */
	public abstract EventTypeID getEventTypeID();
	
}
