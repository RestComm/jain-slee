package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;

/**
 * 
 * ProfileManagementHandler.java
 *
 * <br>Project:  mobicents
 * <br>3:26:16 PM Apr 3, 2009 
 * <br>
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileManagementHandler {
  
	private static Logger logger = Logger.getLogger(ProfileManagementHandler.class);
	
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

	public boolean isProfileValid(ProfileID profileId) throws NullPointerException, SLEEException {
		// FIXME: Alexandre: Validate the profile
		return false;
	}

	public void markProfileDirty() {
		profileObject.getProfileConcrete().setProfileDirty(true);
	}

	public void profileInitialize()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[profileInitialize] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// FIXME: Alexandre: Do profile initialization.
		}
		finally
		{
		  switchContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}
	}

	public void profileLoad()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[profileLoad] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}

    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile load.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profileStore()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[profileStore] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile store.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profileVerify() throws ProfileVerificationException {
		if (logger.isDebugEnabled()) {
			logger.debug("[profileVerify] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile verification.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profileActivate() {
		if (logger.isDebugEnabled()) {
			logger.debug("[profileActivate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile activation.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profilePassivate() {
		if (logger.isDebugEnabled()) {
			logger.debug("[profilePassivate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile passivation.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profilePostCreate() throws CreateException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[profilePostCreate] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile post-creation actions.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void profileRemove()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[profileRemove] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile removal.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void setProfileContext(ProfileContext profileContext)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[setProfileContext] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Set profile context.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	public void unsetProfileContext()
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[unsetProfileContext] on: " + this.profileObject.getProfileName() + ", from table:" + this.profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
      // FIXME: Alexandre: Unset profile context.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
    }
	}

	// Usage methods. Here we can be static for sure. Rest must be tested.
	public static Object getProfileUsageParam(ProfileConcrete profileConcrete, String name) throws UnrecognizedUsageParameterSetNameException
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileUsageParam]: ProfileName = " + profileConcrete.getProfileName() + " , ProfileTableName = " + profileConcrete.getProfileTableConcrete().getProfileTableName() + " , name = " + name);
		}
		
		if (name == null) {
			throw new NullPointerException("UsageParameterSet name must not be null.");
		}

		ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileTableConcrete();

		return profileTableConcrete.getProfileTableUsageMBean().getInstalledUsageParameterSet(name);
	}

	public static Object getProfileDefaultUsageParam(ProfileConcrete profileConcrete)
	{
		if (logger.isDebugEnabled()) {
			logger.debug("[getProfileUsageParam]: ProfileName = " + profileConcrete.getProfileName() + " , ProfileTableName = " + profileConcrete.getProfileTableConcrete().getProfileTableName());
		}

		ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileTableConcrete();

		return profileTableConcrete.getProfileTableUsageMBean().getInstalledUsageParameterSet(null);
	}

	private ClassLoader switchContextClassLoader(ClassLoader newClassLoader)
	{
    Thread t = Thread.currentThread();
    ClassLoader oldClassLoader = t.getContextClassLoader();
    t.setContextClassLoader(newClassLoader);
	  
    return oldClassLoader;
	}
}
