package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.CreateException;
import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.AttributeNotIndexedException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.ProfileTableActivity;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.profile.UnrecognizedAttributeException;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;
import javax.transaction.SystemException;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.SleeProfileTableManager;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;
import org.mobicents.slee.runtime.cache.ProfileTableCacheData;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.facilities.profile.ProfileTableActivityHandle;

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

	public SleeProfileTableManager getProfileManagement();

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

	public void deassignProfileObject(ProfileObject profileObject, boolean remove);

	public ProfileTableActivity getActivity();

	public Collection<ProfileID> getProfilesIDs();

	public ProfileID getProfileByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException, AttributeTypeMismatchException,
			SLEEException;

	public Collection<ProfileID> getProfilesByIndexedAttribute(String attributeName, Object attributeValue) throws UnrecognizedAttributeException, AttributeNotIndexedException,
			AttributeTypeMismatchException, SLEEException;

	public boolean isProfileCommitted(String newProfileName) throws Exception;

	public ObjectName addProfile(String newProfileName, boolean isDefault) throws TransactionRequiredLocalException, SingleProfileException, ProfileAlreadyExistsException, SLEEException,
			CreateException, ProfileVerificationException;

	public ObjectName getProfileMBean(String profileName, boolean isDefault) throws UnrecognizedProfileNameException;

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
	 * Return collection of Strings. Each string represent profile name in this
	 * profile table.
	 * 
	 * @return
	 */
	public Collection<String> getProfileNames();

	public void removeProfileTable() throws TransactionRequiredLocalException, SLEEException;

	// public void rename(String newProfileTableName);
	/**
	 * Method for old style?
	 * 
	 * @param profileID
	 * @return
	 */
	public Object getSbbCMPProfile(String profileName) throws SLEEException, UnrecognizedProfileNameException;

	/**
	 * Method to start activity for this table.
	 */
	public void register();

	/**
	 * Fire profile Added event for this profile table activity. It does check
	 * if event shoudl be fired before doing so.
	 * 
	 * @param profileLocalObject
	 *            - snapshot view of profile objct.
	 * @throws SLEEException
	 *             - thrown in case of any error
	 */
	public void fireProfileAddedEvent(ProfileLocalObjectConcrete profileLocalObject) throws SLEEException;

	/**
	 * Fire profile remove event on this profile table activity. It does check
	 * if event shoudl be fired before doing so.
	 * 
	 * @param profileLocalObject
	 *            - snapshot view of the profile
	 * @throws SLEEException
	 *             - thrown in case of any error
	 */
	public void fireProfileRemovedEvent(ProfileLocalObjectConcrete profileLocalObject) throws SLEEException;

	/**
	 * Fire profiel updated event on this profile table activty. It does check
	 * if event shoudl be fired before doing so.
	 * 
	 * @param profileLocalObjectBeforeAction
	 *            - snapshot profile local object with CMPs set to profile
	 *            before commit.
	 * @param profileLocalObjectAfterAction
	 *            - snapshot profile local obejct with CMPs set to current
	 *            values (current in term of commit.)
	 * @throws SLEEException
	 *             - thrown in case of any error
	 */
	public void fireProfileUpdatedEvent(ProfileLocalObjectConcrete profileLocalObjectBeforeAction, ProfileLocalObjectConcrete profileLocalObjectAfterAction) throws SLEEException;

}
