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
	
	public static boolean isProfileDirty(ProfileObject profileObject)
	{
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileDirty] @ " + profileObject);
    }

		return profileObject.isProfileDirty();
	}

	public static boolean isProfileValid(ProfileObject profileObject, ProfileID profileId) throws NullPointerException, SLEEException
	{
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileValid(" + profileId + ")] @ " + profileObject);
    }

    // FIXME: Alexandre: Validate the profile
		return true;
	}

	public static void markProfileDirty(ProfileObject profileObject)
	{
    if (logger.isDebugEnabled())
    {
      logger.info("[markProfileDirty] @ " + profileObject);
    }
    
		profileObject.setProfileDirty(true);
	}

	public static void profileInitialize(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
      logger.info("[profileInitialize] @ " + profileObject);
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
      logger.info("[profileLoad] @ " + profileObject);
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
      logger.info("[profileStore] @ " + profileObject);
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      profileObject.profileStore();
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
      logger.info("[profileVerify] @ " + profileObject);
		}
		
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      profileObject.profileVerify();
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
      logger.info("[profileActivate] @ " + profileObject);
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
      logger.info("[profilePassivate] @ " + profileObject);
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
      logger.info("[profilePostCreate] @ " + profileObject);
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
      logger.info("[profileRemove] @ " + profileObject);
		}
    
		boolean removeProfileCall = false;
		
    ClassLoader oldClassLoader = switchContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());
    
    try
    {
      ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());
      removeProfileCall = true;
      // FIXME: Alexandre: Do profile removal.
    }
    finally
    {
      switchContextClassLoader(oldClassLoader);
      
      if(removeProfileCall)
        ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
    }
	}

	public static void setProfileContext(ProfileObject profileObject, ProfileContext profileContext)
	{
		if (logger.isDebugEnabled())
		{
      logger.info("[setProfileContext] @ " + profileObject);
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
      logger.info("[unsetProfileContext] @ " + profileObject);
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
      logger.info("[getProfileUsageParam(" + name + ")] @ " + profileConcrete.getProfileObject());
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
      logger.info("[getProfileDefaultUsageParam] @ " + profileConcrete.getProfileObject());
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
