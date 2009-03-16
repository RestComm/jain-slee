package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import org.mobicents.slee.container.profile.ProfileObject;

/**
 * 
 * Start time:16:30:44 2009-03-16<br>
 * Project: mobicents-jainslee-server-core<br>
 * This is interceptor interface for
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface ProfileLocalObjectInterceptor {

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;

	public void setProfile(ProfileObject profile);

	public ProfileObject getProfile();
}
