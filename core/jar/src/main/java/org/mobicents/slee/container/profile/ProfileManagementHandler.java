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
	
//	private ProfileCmpHandler profileCmpHandler = null;
//
//	public void setProfileCmpHandler(ProfileCmpHandler profileCmpHandler) {
//		this.profileCmpHandler = profileCmpHandler;
//	}

	public static boolean isProfileDirty(ProfileObject profileObject)
	{
		return profileObject.getProfileConcrete().getProfileDirty();
	}

	public static boolean isProfileValid(ProfileObject profileObject, ProfileID profileId) throws NullPointerException, SLEEException
	{
		// FIXME: Alexandre: Validate the profile
		return false;
	}

	public static void markProfileDirty(ProfileObject profileObject)
	{
		profileObject.getProfileConcrete().setProfileDirty(true);
	}

	public static void profileInitialize(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileInitialize] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
		ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
			// FIXME: Alexandre: Do profile initialization.
		}
		finally
		{
		  switchContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
		}
	}

	public static void profileLoad(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileLoad] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}

    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile load.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profileStore(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileStore] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile store.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profileVerify(ProfileObject profileObject) throws ProfileVerificationException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileVerify] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile verification.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profileActivate(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileActivate] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile activation.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profilePassivate(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profilePassivate] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile passivation.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profilePostCreate(ProfileObject profileObject) throws CreateException
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profilePostCreate] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile post-creation actions.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void profileRemove(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[profileRemove] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Do profile removal.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void setProfileContext(ProfileObject profileObject, ProfileContext profileContext)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[setProfileContext] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Set profile context.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void unsetProfileContext(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
			logger.debug("[unsetProfileContext] on: " + profileObject.getProfileName() + ", from table:" + profileObject.getProfileTableConcrete().getProfileTableName());
		}
    
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      // FIXME: Alexandre: Unset profile context.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	// Usage methods. Here we can be static for sure. Rest must be tested.
	public static Object getProfileUsageParam(ProfileConcrete profileConcrete, String name) throws UnrecognizedUsageParameterSetNameException
	{
		if (logger.isDebugEnabled())
		{
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
		if (logger.isDebugEnabled())
		{
			logger.debug("[getProfileUsageParam]: ProfileName = " + profileConcrete.getProfileName() + " , ProfileTableName = " + profileConcrete.getProfileTableConcrete().getProfileTableName());
		}

		ProfileTableConcrete profileTableConcrete = profileConcrete.getProfileTableConcrete();

		return profileTableConcrete.getProfileTableUsageMBean().getInstalledUsageParameterSet(null);
	}

	private static ClassLoader switchContextClassLoader(ClassLoader newClassLoader)
	{
    Thread t = Thread.currentThread();
    ClassLoader oldClassLoader = t.getContextClassLoader();
    t.setContextClassLoader(newClassLoader);
	  
    return oldClassLoader;
	}
}
