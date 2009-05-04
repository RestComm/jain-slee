package org.mobicents.slee.container.profile;

import javax.slee.profile.ProfileLocalObject;

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

	boolean isSnapshot();

	ProfileObject getProfileObject();
	
}
