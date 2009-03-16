package org.mobicents.slee.container.deployment.interceptors;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.profile.ProfileObject;
import org.mobicents.slee.container.profile.SleeProfileManagement;

public class DefaultProfileLocalObjectInterceptorImpl implements ProfileLocalObjectInterceptor {

	private static final Logger logger = Logger.getLogger(DefaultProfileLocalObjectInterceptorImpl.class);

	private SleeProfileManagement sleeProfileManagement = null;
	private ProfileObject profileObject = null;

	public DefaultProfileLocalObjectInterceptorImpl(SleeProfileManagement sleeProfileManagement) {
		this.sleeProfileManagement = sleeProfileManagement;
	}

	public ProfileObject getProfile() {
		return this.profileObject;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		// THis might be a perf killer, however we need to acces PO object, is
		// there any better way ?
		if (this.profileObject == null) {
			logger.error("Intercepting in PLO when there is no profile object to proxy to");
			return null;
		}

		return method.invoke(this.profileObject.getProfileConcrete(), args);

	}

	public void setProfile(ProfileObject profile) {
		this.profileObject = profile;
		if (this.profileObject != null) {
			// Just in case
			this.profileObject.setSbbInvoked(true);
		}

	}

}
