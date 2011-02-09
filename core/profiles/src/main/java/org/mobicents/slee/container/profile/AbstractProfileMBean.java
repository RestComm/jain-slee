package org.mobicents.slee.container.profile;

import javax.slee.management.ManagementException;
import javax.slee.profile.ProfileMBean;

public interface AbstractProfileMBean extends ProfileMBean {

	/**
	 * Closes the mbean and the underlying profile object, whatever its state is
	 * @throws ManagementException
	 */
	public void close() throws ManagementException;
	
}
