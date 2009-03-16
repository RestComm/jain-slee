package org.mobicents.slee.container.profile;

import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileNameException;

/**
 * 
 * Start time:16:36:52 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Extension to profile table interface defined
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileTableConcrete extends ProfileTable {

	public SleeProfileManagement getProfileManagement();

	public ProfileTableNotification getProfileTableNotification();

	public String getProfileTableName();
	
	/**
	 * This method assigns PO to existing profile, otherwise it throws exception
	 * @param profileName - valid, existing profile profile name
	 * @return
	 * @throws UnrecognizedProfileNameException
	 */
	public ProfileObject assignProfileObject(String profileName) throws UnrecognizedProfileNameException;

	public void deassignProfileObject(ProfileObject profileObject);

}
