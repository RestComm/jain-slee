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
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

/**
 * Start time:14:20:46 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * This class implements profile local interface to provide all required
 * methods. It obtains on the fly ProfileObject to perform its tasks.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileLocalObjectImpl implements ProfileLocalObjectConcrete {

	protected static final Logger logger = Logger.getLogger(ProfileLocalObjectImpl.class);

	protected String profileName = null;
	protected String profileTableName = null;
	protected ProfileSpecificationID profileSpecificationId = null;
	protected SleeProfileTableManager sleeProfileManagement = null;
	protected boolean isDefault = true;
	protected ProfileObject profileObject;
	protected ProfileSpecificationComponent component = null;
	private SleeContainer sleeContainer;
	private SleeTransactionManager sleeTransactionManager;

	public ProfileLocalObjectImpl(ProfileSpecificationID profileSpecificationId, String profileTableName, String profileName, SleeProfileTableManager sleeProfileManagement, boolean isDefault) {
		super();

		if (profileSpecificationId == null || profileTableName == null || profileName == null) {
			throw new NullPointerException("Parameters must not be null");
		}

		// FIXME: does any of below methods require CL change?
		this.profileName = profileName;
		this.profileTableName = profileTableName;
		this.profileSpecificationId = profileSpecificationId;
		this.sleeProfileManagement = sleeProfileManagement;

		this.sleeContainer = this.sleeProfileManagement.getSleeContainer();
		this.sleeTransactionManager = this.sleeContainer.getTransactionManager();
		this.component = sleeContainer.getComponentRepositoryImpl().getComponentByID(this.profileSpecificationId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileName()
	 */
	public String getProfileName() throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("Getting profile name for: " + this.profileName);
		}
		return this.isDefault ? null : this.profileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTable()
	 */
	public ProfileTable getProfileTable() throws SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileTable], table: " + this.profileTableName + ", for profile: " + this.profileName);
		}

		ProfileTable pt = null;
		try {
			pt = this.sleeProfileManagement.getProfileTable(profileTableName);
		} catch (Exception e) {
			throw new SLEEException("Failed to obtain ProfileTable interface.", e);
		}

		// FIXME: Alexandre: Unnecessary check, if not found exception will be
		// thrown.
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
		if (logger.isDebugEnabled()) {
			logger.debug("[isIdentical] on: " + this.profileName + ", from table:" + this.profileTableName + ", this: " + this + ", other: " + other);
		}

		if (other == null)
			throw new SLEEException("Unable to perform operation: ProfileLocalObject 'other' cannot be null.");

		ProfileLocalObjectImpl otherImpl = (ProfileLocalObjectImpl) other;

		// If one is default and the other isn't, fail
		if (otherImpl.isDefault != this.isDefault) {
			return false;
		}

		// If they are the same object, they are identical too :)
		if (this == other) {
			return true;
		}

		// Table name must be the same, and cannot be null
		if (this.getProfileTableName().equals(otherImpl.getProfileTableName())) {
			// Profile name can be null (default) or must be equal
			if (this.getProfileName() == null && other.getProfileName() == null || this.getProfileName().equals(otherImpl.getProfileName())) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#remove()
	 */
	public void remove() throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[remove] on: " + this.profileName + ", from table:" + this.profileTableName);
		}

		this.sleeTransactionManager.mandateTransaction();

		try {
			this.getProfileTable().remove(this.getProfileName());
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

	public boolean isIsolateSecurityPermissions() {
		ProfileSpecificationDescriptorImpl descriptor = this.component.getDescriptor();
		boolean isolateSecurityPermissions = descriptor.getProfileLocalInterface() == null ? false : descriptor.getProfileLocalInterface().isIsolateSecurityPermissions();
		return isolateSecurityPermissions;
	}

	/**
	 * This method allocates ProfileObject to serve calls. If this is snapshot,
	 * this method does nothing as we ha
	 */
	public void allocateProfileObject() throws UnrecognizedProfileNameException, UnrecognizedProfileTableNameException, SLEEException {
		if (logger.isDebugEnabled()) {
			logger.debug("[allocateProfileObject] on: " + this.profileName + ", from table:" + this.profileTableName);
		}

		// FIXME: mayeb we should be protected ?
		try {
			sleeTransactionManager.mandateTransaction();
		} catch (TransactionRequiredLocalException trle) {
			throw new SLEEException("No transaction present.", trle);
		}

		try {
			if (this.profileObject != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("[allocateProfileObject] on: " + this.profileName + ", from table:" + this.profileTableName + " profile object is not null, returning.");
				}

				return;
			}

			ProfileTableConcrete profileTable = (ProfileTableConcrete) this.sleeProfileManagement.getProfileTable(profileTableName);
			this.profileObject = profileTable.assignAndActivateProfileObject(profileName);

			// Set flag that SLEE component interacts with it. this is true only
			// in case of JMX client
			this.profileObject.setManagementView(false);
		} catch (UnrecognizedProfileTableNameException uptne) {
			try {
				sleeTransactionManager.rollback();
				throw new TransactionRolledbackLocalException("No such profile table: " + profileTableName, uptne);
			} catch (IllegalStateException ise) {
				logger.error("", ise);
			} catch (SecurityException se) {
				logger.error("", se);
			} catch (SystemException syse) {
				logger.error("", syse);
			}
		}

		try {
			sleeTransactionManager.addBeforeCommitAction(new BeforeCommitTransctAction());
			sleeTransactionManager.addAfterRollbackAction(new RollbackTransctAction());
		} catch (SystemException e) {
			throw new SLEEException("Failed to add management task", e);
		}
	}

	private void removeProfileObject() {
		this.profileObject = null;
	}

	public ProfileConcrete getProfileConcrete() {
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileConcrete] on: " + this.profileName + ", from table:" + this.profileTableName);
		}

		// FIXME: Alex: Instantiate concrete profile.
		return null;
	}

	private class BeforeCommitTransctAction implements TransactionalAction {
		public void execute() {
			try {
				// FIXME: Alexandre: Shouldn't we do anything else here?
				// ProfileTableConcrete profileTable = (ProfileTableConcrete)
				// sleeProfileManagement.getProfileTable(profileTableName);
				removeProfileObject();
			} catch (Exception e) {
				logger.error("Failed to deallocate ProfileObject, please report this to dev team.", e);
			}
		}
	}

	// for now its the same
	private class RollbackTransctAction extends BeforeCommitTransctAction {
	}

}
