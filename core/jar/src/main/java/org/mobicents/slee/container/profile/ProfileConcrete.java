package org.mobicents.slee.container.profile;

import javax.slee.profile.Profile;
import javax.slee.profile.ProfileManagement;

/**
 * 
 * Start time:16:51:13 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * Profile object interface which declares extra management methods to store
 * some runtime properties. This interface represents actual object that has
 * local copy of profile data. Its logical counter part is
 * SbbEntity+SbbConcrete. We can have multiple object representing SbbE cached
 * data, with this object it is similar with this. However we cant use instance
 * of this classes as ProfileEntities, since they MUST act ass concrete impl
 * (see how SbbConcrete is used inside SbbObject)
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileConcrete extends Profile ,ProfileManagement {

	public void setProfileObject(ProfileObject profileObject);
	public ProfileObject getProfileObject();

	public void setProfileName(String profileName);
	public String getProfileName();

	public void setTableName(String profileTableName);
	public String getTableName();

}
