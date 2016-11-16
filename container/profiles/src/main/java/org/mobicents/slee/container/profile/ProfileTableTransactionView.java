/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.slee.container.profile;

import java.util.Map;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.UnrecognizedProfileNameException;

import org.mobicents.slee.container.profile.entity.ProfileEntity;
import org.mobicents.slee.container.transaction.SleeTransactionManager;
import org.mobicents.slee.container.transaction.TransactionContext;
import org.mobicents.slee.container.transaction.TransactionalAction;

/**
 * The transaction view of a profile table.
 * 
 * @author martins
 * 
 */
public class ProfileTableTransactionView {

	/**
	 * the profile table
	 */
	private final ProfileTableImpl profileTable;

	/**
	 * 
	 * @param profileTable
	 */
	public ProfileTableTransactionView(ProfileTableImpl profileTable) {
		this.profileTable = profileTable;
	}

	/**
	 * 
	 * @return
	 * @throws SLEEException
	 */
	private Map getTxData() throws SLEEException {
		final SleeTransactionManager txManager = profileTable
				.getSleeContainer().getTransactionManager();
		txManager.mandateTransaction();
		return txManager.getTransactionContext().getData();		
	}

	/**
	 * Retrieves a profile object for the table and specified profile name,
	 * there is only one profile object per profile entity per transaction
	 * 
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileObjectImpl getProfile(String profileName)
			throws TransactionRequiredLocalException, SLEEException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileName,
				profileTable.getProfileTableName());
		ProfileObjectImpl value = (ProfileObjectImpl) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getProfileManagement()
				.getObjectPoolManagement().getObjectPool(
							profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(profileTable.getSleeContainer()
					.getTransactionManager(), value, pool);
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

	/**
	 * 
	 * Retrieves a profile object for the table and specified profile entity,
	 * there is only one profile object per profile entity per transaction
	 * 
	 * @param profileEntity
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileObjectImpl getProfile(ProfileEntity profileEntity)
			throws TransactionRequiredLocalException, SLEEException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileEntity
				.getProfileName(), profileTable.getProfileTableName());
		ProfileObjectImpl value = (ProfileObjectImpl) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getProfileManagement()
			.getObjectPoolManagement().getObjectPool(
							profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(profileTable.getSleeContainer()
					.getTransactionManager(), value, pool);
			value.profileActivate(profileEntity);
			txData.put(key, value);
		}
		return value;
	}

	/**
	 * 
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 * @throws CreateException
	 */
	public ProfileObjectImpl createProfile(String profileName)
			throws TransactionRequiredLocalException, SLEEException,
			CreateException {

		Map txData = getTxData();
		ProfileTransactionID key = new ProfileTransactionID(profileName,
				profileTable.getProfileTableName());
		ProfileObjectImpl value = (ProfileObjectImpl) txData.get(key);
		if (value == null) {
			ProfileObjectPool pool = profileTable.getProfileManagement()
			.getObjectPoolManagement().getObjectPool(
							profileTable.getProfileTableName());
			value = pool.borrowObject();
			passivateProfileObjectOnTxEnd(profileTable.getSleeContainer()
					.getTransactionManager(), value, pool);
			value.profileCreate(profileName);
			txData.put(key, value);
		}
		return value;
	}

	/**
	 * Adds transactional actions to the active transaction to passivate a
	 * profile object.
	 * 
	 * @param txManager
	 * @param profileObject
	 * @param pool
	 */
	public static void passivateProfileObjectOnTxEnd(
			SleeTransactionManager txManager,
			final ProfileObjectImpl profileObject, final ProfileObjectPool pool) {
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
					} else {
						profileObject.profileRemove(true, false);
					}
					pool.returnObject(profileObject);
				}
			}
		};
		final TransactionContext txContext = txManager.getTransactionContext();
		txContext.getAfterRollbackActions().add(afterRollbackAction);
		txContext.getBeforeCommitActions().add(beforeCommitAction);		
	}

	/**
	 * 
	 * @author martins
	 * 
	 */
	private static class ProfileTransactionID {

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
