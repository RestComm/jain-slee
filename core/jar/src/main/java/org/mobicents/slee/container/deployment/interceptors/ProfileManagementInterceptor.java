package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.container.management.SleeProfileManager;
import org.mobicents.slee.container.profile.SleeProfileManagement;
/**
 * 
 * Start time:07:07:17 2009-03-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileManagementInterceptor {

	public Object getProfile() ;

	public SleeProfileManagement getProfileManagement() ;

	public String getProfileName() ;

	public ProfileSpecificationComponent getProfileSpecificationComponent() ;

	public String getProfileTableName() ;

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable ;

	public void setProfile(Object profile) ;

	public void setProfileManager(SleeProfileManagement profileManagement) ;

	public void setProfileName(String profileName) ;

	public void setProfileSpecificationComponent(ProfileSpecificationComponent profileSpecificationComponent) ;

	public void setProfileTableName(String profileTableName);
	
	/**
	 * Inovked from slee components
	 */
	public void commitChanges();
}
