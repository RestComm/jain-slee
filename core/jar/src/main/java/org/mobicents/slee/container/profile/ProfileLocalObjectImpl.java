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
 * @author martins
 */
public class ProfileLocalObjectImpl implements ProfileLocalObjectConcrete {

	protected static final Logger logger = Logger.getLogger(ProfileLocalObjectImpl.class);

	protected final ProfileSpecificationID profileSpecificationId;
	protected final ProfileSpecificationComponent component;
	
	private final SleeContainer sleeContainer;
	private final String profileName;
	private final String profileTableName;
	
	protected ProfileObject profileObject;
	
	public ProfileLocalObjectImpl(ProfileSpecificationID profileSpecificationId, String profileTableName, String profileName, SleeContainer sleeContainer) {

		if (profileSpecificationId == null || profileTableName == null || profileName == null) {
			throw new NullPointerException("Parameters must not be null");
		}

		this.profileName = profileName;
		this.profileTableName = profileTableName;
		this.profileSpecificationId = profileSpecificationId;

		this.sleeContainer = sleeContainer;
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
		return this.profileName;
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

		try {
			return sleeContainer.getSleeProfileTableManager().getProfileTable(profileTableName);
		} catch (Exception e) {
			throw new SLEEException("Failed to obtain ProfileTable interface.", e);
		}
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

		// If they are the same object, they are identical too :)
		if (this == other) {
			return true;
		}

		if (!(other instanceof ProfileLocalObjectImpl)) {
			return false;
		}
		
		return this.getProfileTableName().equals(other.getProfileTableName()) && this.getProfileName().equals(other.getProfileName());
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

		sleeContainer.getTransactionManager().mandateTransaction();

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
		final SleeTransactionManager sleeTransactionManager = sleeContainer.getTransactionManager();
		try {
			sleeTransactionManager.mandateTransaction();
		} catch (TransactionRequiredLocalException trle) {
			throw new SLEEException("No transaction present.", trle);
		}


		if (this.profileObject != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("[allocateProfileObject] on: " + this.profileName + ", from table:" + this.profileTableName + " profile object is not null, returning.");
			}

			return;
		}

		ProfileTableConcrete profileTable = (ProfileTableConcrete) getProfileTable();
		this.profileObject = profileTable.assignAndActivateProfileObject(profileName);
		
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
