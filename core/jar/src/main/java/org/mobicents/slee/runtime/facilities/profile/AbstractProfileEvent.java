package org.mobicents.slee.runtime.facilities.profile;

import javax.slee.Address;
import javax.slee.AddressPlan;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.profile.ProfileConcrete;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.ProfileTableConcrete;
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
	 * a snapshot of the concrete instance of the event's profile, when the event occurred 
	 */
	private final ProfileConcrete profileConcreteAfterAction;

	private final static SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param profileConcreteAfterAction
	 */
	AbstractProfileEvent(ProfileConcrete profileConcreteAfterAction) {
		
		this.profileConcreteAfterAction = profileConcreteAfterAction;
		this.profileAddress = new Address(AddressPlan.SLEE_PROFILE, profileConcreteAfterAction.getTableName() + "/"
				+ profileConcreteAfterAction.getProfileName());
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
	 * Retrieves a snapshot of the concrete instance of the event's profile, when the event occurred
	 * @return
	 */
	public ProfileConcrete getProfileConcreteAfterAction() {
		return profileConcreteAfterAction;
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
	boolean isProfileConcreteClassVisible() {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(profileConcreteAfterAction.getClass().getName());
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * Retrieves a local object valid for thus current transaction.
	 * 
	 * @param profileConcrete
	 * @return
	 * @throws TransactionRequiredLocalException
	 */
	ProfileLocalObject getProfileLocalObjectValidInCurrentTransaction(ProfileConcrete profileConcrete) throws TransactionRequiredLocalException {
		// check tx
		final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
		txManager.mandateTransaction();
		// get an object from the table
		final ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileObject().getProfileTableConcrete();
		final ProfileObject profileObject = profileTableConcrete.assignAndActivateProfileObject(profileConcrete.getProfileName());
		// set the concrete object and raise read only flag
		profileObject.setProfileConcrete(profileConcrete);
		profileObject.setProfileReadOnly(true);
		// add tx action to release object after tx ends
		TransactionalAction action = new TransactionalAction() {
			public void execute() {
				profileTableConcrete.deassignProfileObject(profileObject, false);				
			}
		};
		try {
			txManager.addAfterCommitAction(action);
			txManager.addAfterRollbackAction(action);
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}
		// finally get the object
		return profileObject.getProfileLocalObject();
	}
	
	/**
	 * Retrieves the event type id for the event object
	 * @return
	 */
	public abstract EventTypeID getEventTypeID();
	
}
