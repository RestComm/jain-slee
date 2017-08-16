/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2017, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

/**
 * 
 */
package org.mobicents.slee.container.component.profile;

import javax.slee.profile.Profile;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.component.profile.ProfileConcreteClassInfo;

/**
 * Information about the need to invoke {@link Profile} methods.
 * 
 * @author martins
 * 
 */
public class ProfileConcreteClassInfoImpl implements ProfileConcreteClassInfo {

	private static final Logger logger = Logger.getLogger(ProfileConcreteClassInfoImpl.class);

	private boolean invokeSetProfileContext;
	private boolean invokeUnsetProfileContext;
	private boolean invokeProfileInitialize;
	private boolean invokeProfilePostCreate;
	private boolean invokeProfileActivate;
	private boolean invokeProfilePassivate;
	private boolean invokeProfileRemove;
	private boolean invokeProfileLoad;
	private boolean invokeProfileStore;
	private boolean invokeProfileVerify;

	/**
	 * @param methodName
	 * @param invoke
	 */
	public void setInvokeInfo(String methodName, boolean invoke) {
		if (methodName.equals("profileActivate")) {
			invokeProfileActivate = invoke;
		}
		else if (methodName.equals("profileInitialize")) {
			invokeProfileInitialize= invoke;
		} 
		else if (methodName.equals("profileVerify")) {
			invokeProfileVerify = invoke;
		}
		else if (methodName.equals("profileLoad")) {
			invokeProfileLoad = invoke;
		}
		else if (methodName.equals("profilePassivate")) {
			invokeProfilePassivate = invoke;
		}
		else if (methodName.equals("profilePostCreate")) {
			invokeProfilePostCreate = invoke;
		}
		else if (methodName.equals("profileRemove")) {
			invokeProfileRemove = invoke;
		}
		else if (methodName.equals("profileStore")) {
			invokeProfileStore = invoke;
		}
		else if (methodName.equals("setProfileContext")) {
			invokeSetProfileContext = invoke;
		}
		else if (methodName.equals("unsetProfileContext")) {
			invokeUnsetProfileContext = invoke;
		}
		else {
			logger.warn("Unrecognized method from javax.slee.Profile, unable to set info on whether to invoke it or not in runtime. Method name: "+methodName);
		}
	}
	
	/**
	 * 
	 * @return the invokeProfileActivate
	 */
	public boolean isInvokeProfileActivate() {
		return invokeProfileActivate;
	}

	/**
	 * 
	 * @return the invokeProfileInitialize
	 */
	public boolean isInvokeProfileInitialize() {
		return invokeProfileInitialize;
	}

	/**
	 * 
	 * @return the invokeProfileVerify
	 */
	public boolean isInvokeProfileVerify() {
		return invokeProfileVerify;
	}

	/**
	 * 
	 * @return the invokeProfileLoad
	 */
	public boolean isInvokeProfileLoad() {
		return invokeProfileLoad;
	}

	/**
	 * 
	 * @return the invokeProfilePassivate
	 */
	public boolean isInvokeProfilePassivate() {
		return invokeProfilePassivate;
	}

	/**
	 * 
	 * @return the invokeProfilePostCreate
	 */
	public boolean isInvokeProfilePostCreate() {
		return invokeProfilePostCreate;
	}

	/**
	 * 
	 * @return the invokeProfileRemove
	 */
	public boolean isInvokeProfileRemove() {
		return invokeProfileRemove;
	}
	
	/**
	 * 
	 * @return the invokeProfileStore
	 */
	public boolean isInvokeProfileStore() {
		return invokeProfileStore;
	}

	/**
	 * 
	 * @return the invokeSetProfileContext
	 */
	public boolean isInvokeSetProfileContext() {
		return invokeSetProfileContext;
	}

	/**
	 * 
	 * @return the invokeUnsetProfileContext
	 */
	public boolean isInvokeUnsetProfileContext() {
		return invokeUnsetProfileContext;
	}

}
