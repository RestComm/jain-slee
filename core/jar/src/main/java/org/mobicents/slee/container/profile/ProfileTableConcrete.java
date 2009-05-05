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

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
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
	 * Assigns a profile object and activates it, invoking profileActivate()
	 * @param profileName
	 * @return
	 */
	public ProfileObject assignAndActivateProfileObject(String profileName);

	/**
	 * Returns a profile object instance to the table
	 * @param profileObject
	 * @param remove if the object should be removed or passivated
	 */
	public void deassignProfileObject(ProfileObject profileObject, boolean remove);

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
	 * @param newProfileName
	 * @return
	 * @throws TransactionRequiredLocalException
	 * @throws ProfileAlreadyExistsException
	 * @throws SLEEException
	 * @throws CreateException
	 */
	public ProfileObject createProfile(String newProfileName) throws TransactionRequiredLocalException, ProfileAlreadyExistsException, SLEEException,
			CreateException;

	/**
	 * Returns JMX MBean name of usage mbean
	 * 
	 * @return
	 * @throws InvalidArgumentException
	 *             - confusing exception name, however it is thrown when this
	 *             tables specs does not define Usage parameters
	 */
	public ObjectName getUsageMBeanName() throws InvalidArgumentException;

	/**
	 * Return Usage MBean if profile specification declares UsageInterface.
	 * 
	 * @return
	 */
	public ProfileTableUsageMBeanImpl getProfileTableUsageMBean();

	public void setProfileTableUsageMBean(ProfileTableUsageMBeanImpl profileTableUsageMBean);

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
