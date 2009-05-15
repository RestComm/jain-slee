package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.management.ObjectName;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.deployment.profile.jpa.ProfileEntity;
import org.mobicents.slee.container.management.jmx.ProfileTableUsageMBeanImpl;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.facilities.MNotificationSource;

/**
 * 
 * Start time:16:36:52 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Extension to profile table interface defined FIXME: checkl if all methods
 * should be mandatory in TX
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileTableConcrete extends ProfileTable {

	public boolean doesFireEvents();
	
	public SleeContainer getSleeContainer();
	
	public MNotificationSource getProfileTableNotification();

	public String getProfileTableName();

	public ProfileSpecificationComponent getProfileSpecificationComponent();

	/**
	 * Borrows a profile object from the profile table pool
	 * @return
	 */
	//public ProfileObject borrowProfileObject();
	
	/**
	 * Returns a profile object from the profile table pool
	 * @param profileObject
	 */
	//void returnProfileObject(ProfileObject profileObject);

	public ActivityContext getActivityContext();
	
	public ProfileTableActivity getActivity();

	public Collection<ProfileID> getProfilesIDs();

	public ProfileID getProfileByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException;

	public Collection<ProfileID> getProfilesByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, SLEEException;

	public boolean profileExists(String newProfileName);

	/**
	 * Creates the default profile
	 * @throws CreateException
	 * @throws ProfileVerificationException
	 */
	public void createDefaultProfile() throws CreateException, ProfileVerificationException;
	
	/**
	 * Creates the profile with the specified name.
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws ProfileAlreadyExistsException
	 * @throws SLEEException
	 * @throws CreateException
	 */
	public ProfileObject createProfile(String profileName) throws TransactionRequiredLocalException, ProfileAlreadyExistsException, SLEEException,
			CreateException;

	/**
	 * 
	 * @param profileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws UnrecognizedProfileNameException
	 * @throws SLEEException
	 */
	public ProfileObject getProfile(String profileName) throws TransactionRequiredLocalException, SLEEException;

	/**
	 * 
	 * @param profileName
	 * @param invokeConcreteSbb
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public boolean removeProfile(String profileName, boolean invokeConcreteSbb)
			throws TransactionRequiredLocalException, SLEEException;
	
	/**
	 * Return Usage MBean if profile specification declares UsageInterface.
	 * 
	 * @return
	 */
	public ProfileTableUsageMBeanImpl getProfileTableUsageMBean();

	/**
	 * Return collection of Strings. Each string represent profile name in this
	 * profile table.
	 * 
	 * @return
	 */
	public Collection<String> getProfileNames();

	public void removeProfileTable() throws TransactionRequiredLocalException, SLEEException;

	// public void rename(String newProfileTableName);

	/**
	 * Method to start activity for this table.
	 */
	public void register();

}
