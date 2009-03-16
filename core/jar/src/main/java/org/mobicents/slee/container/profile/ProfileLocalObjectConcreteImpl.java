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
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.interceptors.ProfileLocalObjectInterceptor;
import org.mobicents.slee.container.deployment.interceptors.DefaultProfileLocalObjectInterceptorImpl;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

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

	protected static final Logger logger = Logger.getLogger(ProfileLocalObjectConcreteImpl.class);

	protected String profileName = null;
	protected String profileTableName = null;
	protected ProfileSpecificationID profileSpecificationId = null;
	protected SleeProfileManagement sleeProfileManagement = null;
	protected boolean isDefault = true;
	protected ProfileObject profileObject = null;

	// FIXME add this.
	private ProfileLocalObjectInterceptor interceptor = null;

	public ProfileLocalObjectConcreteImpl(String profileTableName, ProfileSpecificationID profileSpecificationId, String profileName, SleeProfileManagement sleeProfileManagement, boolean isDefault) {
		super();
		if (profileTableName == null || profileName == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}

		this.profileName = profileName;
		this.profileTableName = profileTableName;
		this.profileSpecificationId = profileSpecificationId;
		this.sleeProfileManagement = sleeProfileManagement;
		this.interceptor = new DefaultProfileLocalObjectInterceptorImpl(this.sleeProfileManagement);

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
			pt = this.sleeProfileManagement.getProfileTable(profileTableName, this.profileSpecificationId);
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

		SleeContainer sleeContainer = this.sleeProfileManagement.getSleeContainer();
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();
		ProfileTableConcreteImpl pt = (ProfileTableConcreteImpl) getProfileTable();
		try {
			pt.remove(getProfileName());
		} catch (ReadOnlyProfileException rope) {
			throw new SLEEException("Referenced profile is read only profile.");
		} catch (NullPointerException npe) {
			throw new SLEEException("Refernced profile is default profile, can not remove it");
		}
	}

	public boolean isSnapshot() {
		return this.profileObject.isSnapshot();
	}

	public void setSnapshot() {
		this.profileObject.setSnapshot();

	}

	/**
	 * This method allocates ProfileObject to serve calls
	 */
	public void allocateProfileObject() throws UnrecognizedProfileNameException, UnrecognizedProfileTableNameException, SLEEException {
		
		SleeTransactionManager sleeTransactionManager = this.sleeProfileManagement.getSleeContainer().getTransactionManager();
		try{
		sleeTransactionManager.mandateTransaction();
		}catch(TransactionRequiredLocalException trle)
		{
			throw new SLEEException("No transaction present.",trle);
		}
		ProfileTableConcrete profileTable = (ProfileTableConcrete) this.sleeProfileManagement.getProfileTable(profileTableName, this.profileSpecificationId);
		this.profileObject = profileTable.assignProfileObject(profileName);
		// Set flag that SLEE component interacts with it. this is false only in
		// case of JMX client
		this.profileObject.setSbbInvoked(true);
		this.interceptor.setProfile(this.profileObject);
		try {
			sleeTransactionManager.addBeforeCommitAction(new BeforeCommitTransctAction());
			sleeTransactionManager.addAfterRollbackAction(new RollbackTransctAction());
			
		} catch (SystemException e) {
			//FIXME: what should we do here? 
			e.printStackTrace();
		}

	}

	private ProfileObject getProfileObject() {
		return this.profileObject;
	}

	private void removeProfileObject() {
		this.profileObject = null;
		this.interceptor.setProfile(null);
	}

	private class BeforeCommitTransctAction implements TransactionalAction {

		public void execute() {
			try {
				ProfileTableConcrete profileTable = (ProfileTableConcrete) sleeProfileManagement.getProfileTable(profileTableName, profileSpecificationId);
				profileTable.deassignProfileObject(getProfileObject());
				removeProfileObject();

			} catch (Exception e) {
			
				logger.error("Failed to deallocate ProfileObject, please report this to dev team.");
				
			}
		}
	}

	// for now its the same
	private class RollbackTransctAction extends BeforeCommitTransctAction {
	}

}
