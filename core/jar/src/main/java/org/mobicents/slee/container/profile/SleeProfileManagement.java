package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ProfileTable;
import javax.slee.profile.UnrecognizedProfileTableNameException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;

/**
 * 
 * Start time:16:56:21 2009-03-13<br>
 * Project: mobicents-jainslee-server-core<br>
 * Class that manages ProfileSpecification, profile tables, ProfileObjects. It is responsible for seting up profile specification env, creating
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class SleeProfileManagement {

	
	private SleeContainer sleeContainer = null;
	
	public ProfileTable getProfileTable(String profileTableName, ProfileSpecificationID profileSpecificationID) throws SLEEException,UnrecognizedProfileTableNameException{
		// TODO Auto-generated method stub
		//FIXME: do we need spec ID to get profile table?
		return null;
	}

	public SleeContainer getSleeContainer() {
		return sleeContainer;
	}

	public ProfileSpecificationComponent getProfileSpecificationComponent(ProfileSpecificationID profileSpecificationId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
}
