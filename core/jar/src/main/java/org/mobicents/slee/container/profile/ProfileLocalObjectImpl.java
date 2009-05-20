package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;

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
	
	protected final ProfileObject profileObject;
		
	public ProfileLocalObjectImpl(ProfileObject profileObject) {
		this.profileObject = profileObject;	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileName()
	 */
	public String getProfileName() throws SLEEException {
		return profileObject.getProfileEntity().getProfileName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTable()
	 */
	public ProfileTable getProfileTable() throws SLEEException {
		return profileObject.getProfileTableConcrete();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileTableName()
	 */
	public String getProfileTableName() throws SLEEException {
		return profileObject.getProfileTableConcrete().getProfileTableName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.slee.profile.ProfileLocalObject#isIdentical(javax.slee.profile.
	 * ProfileLocalObject)
	 */
	public boolean isIdentical(ProfileLocalObject other) throws SLEEException {
		
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
			logger.debug("removing profile with name " + getProfileName() + " from table with name " + getProfileTableName());
		}

		sleeContainer.getTransactionManager().mandateTransaction();

		try {
			profileObject.getProfileEntity().remove();		
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

}
