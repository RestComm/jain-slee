package org.mobicents.slee.container.profile;

import java.util.Map;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;
import org.mobicents.slee.runtime.transaction.TransactionalAction;

public class ProfileTableTransactionView {

	private final ProfileTableImpl profileTable;

	public ProfileTableTransactionView(ProfileTableImpl profileTable) {
		this.profileTable = profileTable;
	}

	private Map getTxData() throws SLEEException {
		final SleeTransactionManager txManager = profileTable.getSleeContainer().getTransactionManager();
		txManager.mandateTransaction();
		try {
			return txManager.getTransactionContext().getData();
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(), e);
		}		
	}
	
	public ProfileObject getProfile(String profileName)
			throws TransactionRequiredLocalException, SLEEException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileName,
				profileTable.getProfileTableName());
		ProfileObject value = (ProfileObject) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getSleeContainer()
					.getProfileObjectPoolManagement().getObjectPool(
							profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(value,pool);
			try {
				value.profileActivate(profileName);
			} catch (UnrecognizedProfileNameException e) {
				value.invalidateObject();
				pool.invalidateObject(value);
				return null;
			}
			txData.put(key, value);
		}
		return value;
	}
	
	public ProfileObject getProfile(ProfileEntity profileEntity)
	throws TransactionRequiredLocalException, SLEEException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileEntity.getProfileName(),
				profileTable.getProfileTableName());
		ProfileObject value = (ProfileObject) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getSleeContainer()
			.getProfileObjectPoolManagement().getObjectPool(
					profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(value,pool);
			value.profileActivate(profileEntity);			
			txData.put(key, value);
		}
		return value;
	}

	public ProfileObject createProfile(String profileName) throws TransactionRequiredLocalException, SLEEException, CreateException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileName,
				profileTable.getProfileTableName());
		ProfileObject value = (ProfileObject) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getSleeContainer()
			.getProfileObjectPoolManagement().getObjectPool(
					profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(value,pool);
			value.profileCreate(profileName);
			txData.put(key, value);
		}
		return value;
	}
	
	public void passivateProfileObjectOnTxEnd(final ProfileObject profileObject, final ProfileObjectPool pool) {
		passivateProfileObjectOnTxEnd(profileTable.getSleeContainer().getTransactionManager(), profileObject,pool);
	}
	
	public static void passivateProfileObjectOnTxEnd(SleeTransactionManager txManager, final ProfileObject profileObject, final ProfileObjectPool pool) {
		TransactionalAction afterRollbackAction = new TransactionalAction() {
			public void execute() {
				profileObject.invalidateObject();
				pool.returnObject(profileObject);			
			}
		};
		TransactionalAction beforeCommitAction = new TransactionalAction() {
			public void execute() {
				if (profileObject.getState() == ProfileObjectState.READY) {
					if (!profileObject.getProfileEntity().isRemove()) {
						profileObject.fireAddOrUpdatedEventIfNeeded();
						profileObject.profilePassivate();
					}
					else {
						profileObject.profileRemove(true);
					}
					pool.returnObject(profileObject);
				}
			}
		};
		try{
			txManager.addAfterRollbackAction(afterRollbackAction);
			txManager.addBeforeCommitPriorityAction(beforeCommitAction);
		} catch (SystemException e) {
			throw new SLEEException(e.getMessage(),e);
		}				
	}
	
	private class ProfileTransactionID {

		private final String profileName;
		private final String profileTableName;

		public ProfileTransactionID(String profileName, String profileTableName) {
			this.profileName = profileName;
			this.profileTableName = profileTableName;
		}

		@Override
		public int hashCode() {
			return profileTableName.hashCode() * 31
					+ ((profileName == null) ? 0 : profileName.hashCode());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ProfileTransactionID other = (ProfileTransactionID) obj;
			if (profileName == null) {
				if (other.profileName != null)
					return false;
			} else if (!profileName.equals(other.profileName))
				return false;
			return profileTableName.equals(other.profileTableName);
		}
	}
}
