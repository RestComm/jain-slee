package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.runtime.cache.ProfileTableCacheData;

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

	public SleeProfileManagement getProfileManagement();

	public ProfileTableNotification getProfileTableNotification();

	public String getProfileTableName();

	public ProfileSpecificationComponent getProfileSpecificationComponent();

	/**
	 * This method assigns PO to existing profile, otherwise it throws
	 * exception. In case of bool flag set to true it also tries to create
	 * 
	 * @param profileName
	 *            - valid, existing profile profile name
	 * @return
	 * @throws UnrecognizedProfileNameException
	 */
	public ProfileObject assignProfileObject(String profileName, boolean create) throws UnrecognizedProfileNameException, ProfileAlreadyExistsException;

	public void deassignProfileObject(ProfileObject profileObject);

	public ProfileTableActivity getActivity();

	public Collection<ProfileID> getProfilesIDs();

	public ProfileID getProfileByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException;

	public Collection<ProfileID> getProfilesByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, SLEEException;

	public Object findProfileMBean(String newProfileName);

	public boolean isProfileCommitted(String newProfileName) throws Exception;

	public ObjectName addProfile(String newProfileName, boolean isDefault) throws TransactionRequiredLocalException, NullPointerException, SingleProfileException, InvalidArgumentException, ProfileAlreadyExistsException;

	public ObjectName getProfileMBean(String profileName, boolean isDefault);

	/**
	 * Returns JMX MBean name of usage mbean
	 * 
	 * @return
	 * @throws InvalidArgumentException
	 *             - confusing exception name, however it is thrown when this
	 *             tables specs does not define Usage parameters
	 */
	public ObjectName getUsageMBeanName() throws InvalidArgumentException;

	public Collection<String> getProfileNames();

	public void removeProfileTable();

	public void rename(String newProfileTableName);

}
