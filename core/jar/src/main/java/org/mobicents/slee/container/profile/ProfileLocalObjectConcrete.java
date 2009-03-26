package org.mobicents.slee.container.profile;

import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.UnrecognizedProfileNameException;
import javax.slee.profile.UnrecognizedProfileTableNameException;

/**
 * 
 * Start time:14:18:40 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * Conveniance interface to define some
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileLocalObjectConcrete extends ProfileLocalObject {

	/**
	 * Set flag indicating wheather this PLO is just a snapshot.
	 * 
	 * @param isSnapshot
	 */
	void setSnapshot();

	boolean isSnapshot();

	/**
	 * Causes this PLO to get ProfileObject.
	 * 
	 * @throws UnrecognizedProfileNameException
	 *             - if profile does not exist.
	 * @throws UnrecognizedProfileTableNameException
	 *             - if profile table does not exist.
	 */
	public void allocateProfileObject() throws UnrecognizedProfileNameException, UnrecognizedProfileTableNameException;

	/**
	 * This methods should return snapshost view, it is used in methods of
	 * profile events.
	 * 
	 * @return
	 */
	public ProfileConcrete getProfileConcrete();

}
