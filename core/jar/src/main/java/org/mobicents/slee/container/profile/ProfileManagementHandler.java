package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;


public class ProfileManagementHandler{

	private ProfileCmpHandler profileCmpHandler = null;
	private ProfileObject profileObject = null;
	
	
	public void setProfileCmpHandler(ProfileCmpHandler profileCmpHandler) {
		this.profileCmpHandler = profileCmpHandler;
	}

	public void setProfileObject(ProfileObject profileObject) {
		this.profileObject = profileObject;
	}

	public boolean isProfileDirty() {
		return profileObject.getProfileConcrete().getProfileDirty();
	}

	public boolean isProfileValid(ProfileID arg0) throws NullPointerException, SLEEException {
		// TODO Auto-generated method stub
		return false;
	}

	public void markProfileDirty() {
		profileObject.getProfileConcrete().setProfileDirty(true);
		
	}

	public void profileInitialize() {
		//FIXME: ??
		
	}

	public void profileLoad() {
		// TODO Auto-generated method stub
		
	}

	public void profileStore() {
		// TODO Auto-generated method stub
		
	}

	public void profileVerify() throws ProfileVerificationException {
		// TODO Auto-generated method stub
		
	}

	public void profileActivate() {
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

	public void setProfileContext(ProfileContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void unsetProfileContext() {
		// TODO Auto-generated method stub
		
	}
	

	

}
