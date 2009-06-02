package org.mobicents.slee.container.profile;

import javax.slee.NoSuchObjectLocalException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.transaction.SystemException;
import javax.transaction.Transaction;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.profile.ProfileEntity;

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
public class ProfileLocalObjectImpl implements ProfileLocalObject {

	protected static final Logger logger = Logger.getLogger(ProfileLocalObjectImpl.class);
	
	protected static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * the profile object to be used
	 */
	protected final ProfileObject profileObject;
	
	/**
	 * the transaction that defines if the profile object is tsill valid 
	 */
	protected final Transaction transaction;
	
	/**
	 * the name of the profile related with this object
	 */
	private final String profileName;
	
	public ProfileLocalObjectImpl(ProfileObject profileObject) {
		this.profileObject = profileObject;
		try {
			this.transaction = sleeContainer.getTransactionManager().getTransaction();
			this.profileName = profileObject.getProfileEntity().getProfileName();
		} catch (Throwable e) {
			throw new SLEEException(e.getMessage(),e);
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileName()
	 */
	public String getProfileName() throws SLEEException {
		return profileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTable()
	 */
	public ProfileTable getProfileTable() throws SLEEException {
		return profileObject.getProfileTable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTableName()
	 */
	public String getProfileTableName() throws SLEEException {
		return profileObject.getProfileTable().getProfileTableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.profile.ProfileLocalObject#isIdentical(javax.slee.profile.
	 * ProfileLocalObject)
	 */
	public boolean isIdentical(ProfileLocalObject other) throws SLEEException {
		
		if (!(other instanceof ProfileLocalObjectImpl)) {
			return false;
		}
		
		return this._equals(other);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProfileLocalObjectImpl) {
			ProfileLocalObjectImpl other = (ProfileLocalObjectImpl) obj;
			return this._equals(other);
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	private boolean _equals(ProfileLocalObject other) {
		
		if (!this.getProfileTableName().equals(other.getProfileTableName())) {
			return false;
		}
		
		if (this.getProfileName() == null) {
			if (other.getProfileName() == null) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			return this.getProfileName().equals(other.getProfileName());
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#remove()
	 */
	public void remove() throws TransactionRequiredLocalException, TransactionRolledbackLocalException, SLEEException {

		if (logger.isDebugEnabled()) {
			logger.debug("removing profile with name " + getProfileName() + " from table with name " + getProfileTableName());
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		try {
			ProfileEntity profileEntity = profileObject.getProfileEntity();
			if (profileEntity != null) {
				// confirm it is still the same tx
				checkTransaction();
				// remove
				profileEntity.remove();
			}
			else {
				// there is no profile assigned to the object
				if(getProfileTable().find(getProfileName()) == null) {
					// this exception has priority
					throw new NoSuchObjectLocalException("the profile with name "+getProfileName()+" was not found on table with name "+getProfileTableName());
				}
				else {
					throw new IllegalStateException("the profile object is no longer valid");
				}
			}								
		}
		catch (RuntimeException e) {
			try {
				profileObject.invalidateObject();
				sleeContainer.getTransactionManager().setRollbackOnly();
			} catch (SystemException e1) { 
				throw new SLEEException(e1.getMessage(),e1); 
			};
			throw new TransactionRolledbackLocalException(e.getMessage(),e);
		}
	}
	
	/**
	 * Verifies that the current transaction is still the one used to create the object
	 * @throws IllegalStateException
	 */
	protected void checkTransaction() throws IllegalStateException {
		try {
			if (!sleeContainer.getTransactionManager().getTransaction().equals(this.transaction)) {
				throw new IllegalStateException();
			}
		} catch (SystemException e) {
			throw new IllegalStateException();
		}		
	}

}
