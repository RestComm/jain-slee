package org.mobicents.slee.container.profile;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.profile.ProfileContext;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileVerificationException;
import javax.slee.usage.UnrecognizedUsageParameterSetNameException;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

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
	
	public static boolean isProfileDirtyBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileDirtyBefore] @ " + profileObject);
    }
  
    return false;
  }

  public static boolean isProfileDirty(ProfileObject profileObject)
	{
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileDirty] @ " + profileObject);
    }

		return profileObject.isProfileDirty();
	}

	public static boolean isProfileDirtyAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileDirtyAfter] @ " + profileObject);
    }
  
    return false;
  }

  public static boolean isProfileValidBefore(ProfileObject profileObject, ProfileID profileId) throws NullPointerException, SLEEException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileValidBefore(" + profileId + ")] @ " + profileObject);
    }
  
    return true;
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

	public static boolean isProfileValidAfter(ProfileObject profileObject, ProfileID profileId) throws NullPointerException, SLEEException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[isProfileValidAfter(" + profileId + ")] @ " + profileObject);
    }
  
    return true;
  }

  public static void markProfileDirtyBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[markProfileDirtyBefore] @ " + profileObject);
    }
  }

  public static void markProfileDirty(ProfileObject profileObject)
	{
    if (logger.isDebugEnabled())
    {
      logger.info("[markProfileDirty] @ " + profileObject);
    }
    
		profileObject.setProfileDirty(true);
	}

	public static void markProfileDirtyAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[markProfileDirtyAfter] @ " + profileObject);
    }
  }

  public static void profileInitializeBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileInitializeBefore] @ " + profileObject);
    }
  }

  public static void profileInitializeAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileInitializeAfter] @ " + profileObject);
    }
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

	public static void profileLoadBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileLoadBefore] @ " + profileObject);
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

	public static void profileLoadAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileLoadAfter] @ " + profileObject);
    }
  }

  public static void profileStoreBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileStoreBefore] @ " + profileObject);
    }
    
    SleeContainer.lookupFromJndi().getTransactionManager().mandateTransaction();
  }

  public static void profileStore(ProfileObject profileObject)
	{
		if (logger.isDebugEnabled())
		{
      logger.info("[profileStore] @ " + profileObject);
		}
		
		// NO-OP. If this was not implemented in abstract, nothing to do.
	}

	public static void profileStoreAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileStoreAfter] @ " + profileObject);
    }
    boolean doRollback = true;
    try{
    logger.info( "PERSISTING " + profileObject );
    JPAUtils.INSTANCE.persistProfile(profileObject);
    logger.info( "PERSISTED SUCCESSFULLY!" );
    doRollback = false;
    }
    finally {
      SleeTransactionManager tm = SleeContainer.lookupFromJndi().getTransactionManager();
      tm.requireTransactionEnd( true, doRollback );
    }
  }

  public static void profileVerifyBefore(ProfileObject profileObject) throws ProfileVerificationException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileVerifyBefore] @ " + profileObject);
    }
  }

  public static void profileVerify(ProfileObject profileObject) throws ProfileVerificationException
	{
		if (logger.isDebugEnabled())
		{
      logger.info("[profileVerify] @ " + profileObject);
		}
		
    // NO-OP. If this was not implemented in abstract, nothing to do.
	}

	public static void profileVerifyAfter(ProfileObject profileObject) throws ProfileVerificationException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileVerifyAfter] @ " + profileObject);
    }
  }

  public static void profileActivateBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileActivateBefore] @ " + profileObject);
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

	public static void profileActivateAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileActivateAfter] @ " + profileObject);
    }
  }

  public static void profilePassivateBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profilePassivateBefore] @ " + profileObject);
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

	public static void profilePassivateAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profilePassivateAfter] @ " + profileObject);
    }
  }

  public static void profilePostCreateBefore(ProfileObject profileObject) throws CreateException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profilePostCreateBefore] @ " + profileObject);
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

	public static void profilePostCreateAfter(ProfileObject profileObject) throws CreateException
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profilePostCreateAfter] @ " + profileObject);
    }
  }

  public static void profileRemoveBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileRemoveBefore] @ " + profileObject);
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

	public static void profileRemoveAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[profileRemoveAfter] @ " + profileObject);
    }
  }

  public static void setProfileContextBefore(ProfileObject profileObject, ProfileContext profileContext)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[setProfileContextBefore] @ " + profileObject);
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

	public static void setProfileContextAfter(ProfileObject profileObject, ProfileContext profileContext)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[setProfileContextAfter] @ " + profileObject);
    }
  }

  public static void unsetProfileContextBefore(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[unsetProfileContextBefore] @ " + profileObject);
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

	public static void unsetProfileContextAfter(ProfileObject profileObject)
  {
    if (logger.isDebugEnabled())
    {
      logger.info("[unsetProfileContextAfter] @ " + profileObject);
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
