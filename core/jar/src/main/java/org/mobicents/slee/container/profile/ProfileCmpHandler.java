package org.mobicents.slee.container.profile;

import java.lang.reflect.Method;

import javax.slee.SLEEException;
import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

/**
 * Start time:18:25:55 2009-03-17<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * This class encapsulates logic to set/get cmp field of profile,
 * acting as an interceptor.
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileCmpHandler {

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	/**
	 * 
	 * @param fieldName
	 * @param value
	 */
	public static void setCmpField(ProfileObject profileObject, String fieldName, Object value) throws UnsupportedOperationException, IllegalStateException
	{
		sleeContainer.getTransactionManager().mandateTransaction();

		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());

		try {
			ProfileCallRecorderTransactionData.addProfileCall(profileObject);

			if (profileObject.isProfileReadOnly()) {				
				throw new ReadOnlyProfileException("Profile: " + profileObject.getProfileName() + ", table:" + profileObject.getProfileTableConcrete().getProfileTableName() + " ,is not writeable.");
			}

			// FIXME: Alexandre: This has been moved to MBean (done) / PLOC (missing)
			//if (profileObject.getState() != ProfileObjectState.READY) {
			//	throw new IllegalStateException("Profile object must be in ready state");
			//}

			try
			{
				String methodName = "set" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0)) + "JPA";
				Method m = profileObject.getProfileConcrete().getClass().getMethod(methodName, value.getClass());
				m.invoke( profileObject.getProfileConcrete(), value );
			}
			catch (Exception e) {
			  throw new SLEEException("Failure setting CMP Field (" + fieldName + ").", e);
			}		
			
			profileObject.setProfileDirty(true);
		}
		finally {
			ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
			t.setContextClassLoader(oldClassLoader);			
		}
	}

	/**
	 * 
	 * @param fieldName
	 *            = Introspector.decapitalize(method.getName().substring(3));
	 * @return
	 */
	public static Object getCmpField(ProfileObject profileObject, String fieldName)
	{
	  sleeContainer.getTransactionManager().mandateTransaction();
	  
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(profileObject.getProfileSpecificationComponent().getClassLoader());

		try
		{
			ProfileCallRecorderTransactionData.addProfileCall(profileObject);

			if (profileObject.getState() != ProfileObjectState.READY) {
				throw new IllegalStateException("Profile object must be in ready state");
			}

			Object retVal = null;

			try
			{
				String methodName = "get" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0)) + "JPA";
				Method m = profileObject.getProfileConcrete().getClass().getMethod(methodName);
				retVal = m.invoke( profileObject.getProfileConcrete());
			}
			catch(Exception e )
			{
        throw new SLEEException("Failure getting CMP Field (" + fieldName + ").", e);
			}

			return retVal;
		}
		finally
		{
			ProfileCallRecorderTransactionData.removeProfileCall(profileObject);
			t.setContextClassLoader(oldClassLoader);
		}
	}

	public static void commitChanges(ProfileObject profileObject) {
		if(profileObject.isProfileDirty()) {
			JPAUtils.INSTANCE.persistProfile(profileObject);	    
		}
	}
}
