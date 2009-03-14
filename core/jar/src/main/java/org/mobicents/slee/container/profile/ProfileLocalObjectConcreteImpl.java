/**
 * Start time:14:20:46 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * Start time:14:20:46 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * This class implements profile local interface to provide all required
 * methods. It obtains on the fly ProfileObject to perform its tasks.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileLocalObjectConcreteImpl implements ProfileLocalObjectConcrete {

	private String profileName = null;
	private String profileTableName = null;
	private ProfileSpecificationID profileSpecificationId = null;
	private SleeProfileManagement sleeProfileManager = null;
	private boolean isDefault = false;

	//FIXME add this.
	private Object interceptor = null;
	
	public ProfileLocalObjectConcreteImpl(String profileTableName, ProfileSpecificationID profileSpecificationId, String profileName, SleeProfileManagement sleeProfileManager, boolean isDefault) {
		super();
		if (profileTableName == null || profileName == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}

		this.profileName = profileName;
		this.profileTableName = profileTableName;
		this.profileSpecificationId = profileSpecificationId;
		this.sleeProfileManager = sleeProfileManager;
		this.isDefault = isDefault;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileName()
	 */
	public String getProfileName() throws SLEEException {
		if (!this.isDefault)
			return this.profileName;
		else
			return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTable()
	 */
	public ProfileTable getProfileTable() throws SLEEException {
		ProfileTable pt = null;
		try {
			pt = this.sleeProfileManager.getProfileTable(profileTableName, this.profileSpecificationId);
		} catch (Exception e) {
			throw new SLEEException("Failed to obtain ProfileTable interface.", e);
		}
		if (pt == null) {
			throw new SLEEException("Did not find profile table: " + this.profileTableName);
		}
		return pt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTableName()
	 */
	public String getProfileTableName() throws SLEEException {
		return this.profileTableName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.profile.ProfileLocalObject#isIdentical(javax.slee.profile.
	 * ProfileLocalObject)
	 */
	public boolean isIdentical(ProfileLocalObject other) throws SLEEException {
		if (other == null)
			throw new SLEEException("Other profile local object must not be null");

		ProfileLocalObjectConcreteImpl otherImpl = (ProfileLocalObjectConcreteImpl) other;
		if (otherImpl.isDefault != this.isDefault) {
			return false;
		}

		if (this.isDefault) {
			if (this.getProfileName() == null && otherImpl.getProfileName() == null) {

			} else {
				return false;
			}
		} else {
			if (this.getProfileName().compareTo(otherImpl.getProfileName()) == 0) {

			} else {
				return false;
			}
		}

		if (this.getProfileTableName().compareTo(otherImpl.getProfileTableName()) == 0) {

		} else {
			return false;
		}

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#remove()
	 */
	public void remove() throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException {
		
		SleeContainer sleeContainer = this.sleeProfileManager.getSleeContainer();
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();
		ProfileTableConcreteImpl pt = (ProfileTableConcreteImpl) getProfileTable();
		try{
		pt.remove(getProfileName());
		}catch(ReadOnlyProfileException rope)
		{
			throw new SLEEException("Referenced profile is read only profile.");
		}catch(NullPointerException npe)
		{
			throw new SLEEException("Refernced profile is default profile, can not remove it");
		}
	}

}
