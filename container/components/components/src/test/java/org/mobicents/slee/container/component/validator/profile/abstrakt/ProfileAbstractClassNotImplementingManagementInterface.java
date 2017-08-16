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
 * Start time:16:31:35 2009-02-14<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.validator.profile.abstrakt;

import javax.slee.CreateException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;


/**
 * Start time:16:31:35 2009-02-14<br>
 * Project: restcomm-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public abstract class ProfileAbstractClassNotImplementingManagementInterface implements  ProfileBaseCMPInterface
{

	
	
	
	
	public ProfileAbstractClassNotImplementingManagementInterface() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void profileActivate() {
		// TODO Auto-generated method stub
		
	}

	public void profileInitialize() {
		// TODO Auto-generated method stub
		
	}

	public void profileLoad() {
		// TODO Auto-generated method stub
		
	}

	public void profilePassivate() {
		// TODO Auto-generated method stub
		
	}

	public void profilePostCreate() throws CreateException {
		// TODO Auto-generated method stub
		
	}

	public void profileRemove() {
		// TODO Auto-generated method stub
		
	}

	public void profileStore() {
		// TODO Auto-generated method stub
		
	}

	public void profileVerify() throws ProfileVerificationException {
		// TODO Auto-generated method stub
		
	}

	public void setProfileContext(ProfileContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void unsetProfileContext() {
		// TODO Auto-generated method stub
		
	}

	
	// profile LO methods
	
	public void makeDogBark(int xxxx)
	{}
	public void dontLookAtMeImUglyDefinedMethodWithLongNameAndSpankTheCat(java.io.Serializable cheese)
	{}
	
	// Management interface
	public void doSomeTricktMGMTMagic(int xxxx)
	{}
	public void dontLookAtMeImUglyDefinedMethodWithLongName(java.io.Serializable cheese)
	{}
	
	// USAGE
	
	public abstract UsageOkInterface getDefaultUsageParameterSet();

	public abstract UsageOkInterface getUsageParameterSet(String x) throws UnrecognizedUsageParameterSetNameException;
	

}
