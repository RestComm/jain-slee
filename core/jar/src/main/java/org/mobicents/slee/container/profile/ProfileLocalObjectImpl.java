package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.TransactionRolledbackLocalException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileTable;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.ProfileSpecificationDescriptorImpl;

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
	
	private final SleeContainer sleeContainer;
	
	private ProfileObject profileObject;
	
	private final boolean snapshot;
	
	public ProfileLocalObjectImpl(ProfileObject profileObject, boolean snapshot, SleeContainer sleeContainer) {
		this.profileObject = profileObject;
		this.snapshot = snapshot;
		this.sleeContainer = sleeContainer;		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.profile.ProfileLocalObject#getProfileName()
	 */
	public String getProfileName() throws SLEEException {
		return profileObject.getProfileName();
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

		this.getProfileTable().remove(this.getProfileName());		
	}

	public boolean isSnapshot() {
		return snapshot;
	}

	public ProfileObject getProfileObject() {
		return profileObject;
	}
	
	public boolean isIsolateSecurityPermissions() {
		ProfileSpecificationDescriptorImpl descriptor = this.profileObject.getProfileSpecificationComponent().getDescriptor();
		boolean isolateSecurityPermissions = descriptor.getProfileLocalInterface() == null ? false : descriptor.getProfileLocalInterface().isIsolateSecurityPermissions();
		return isolateSecurityPermissions;
	}
	

}
