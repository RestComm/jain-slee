package org.mobicents.slee.container.profile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
			ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());

			if (profileObject.isProfileReadOnly()) {				
				throw new ReadOnlyProfileException("Profile: " + profileObject.getProfileName() + ", table:" + profileObject.getProfileTableConcrete().getProfileTableName() + " ,is not writeable.");
			}

			// FIXME: Alexandre: This has been moved to MBean (done) / PLOC (missing)
			//if (profileObject.getState() != ProfileObjectState.READY) {
			//	throw new IllegalStateException("Profile object must be in ready state");
			//}

			// FIXME: Alexandre: [DONE] Add the real set code here (call set on concrete?)
			try
			{
				String methodName = "set" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0)) + "JPA";
				Method m = profileObject.getProfileConcrete().getClass().getMethod(methodName, value.getClass());
				m.invoke( profileObject.getProfileConcrete(), value );
			}
			catch ( SecurityException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( NoSuchMethodException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( IllegalArgumentException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( IllegalAccessException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( InvocationTargetException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			profileObject.setProfileDirty(true);
			
		}
		finally
		{
			ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
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
			ProfileCallRecorderTransactionData.addProfileCall(profileObject.getProfileConcrete());

			if (profileObject.getState() != ProfileObjectState.READY) {
				throw new IllegalStateException("Profile object must be in ready state");
			}

			Object retVal = null;

			// FIXME: Alexandre: [DONE] Return value here (call get on concrete?)
			try
			{
				String methodName = "get" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0)) + "JPA";
				Method m = profileObject.getProfileConcrete().getClass().getMethod(methodName);
				retVal = m.invoke( profileObject.getProfileConcrete());
			}
			catch ( NoSuchMethodException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( IllegalArgumentException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( IllegalAccessException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch ( InvocationTargetException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return retVal;
		}
		finally
		{
			ProfileCallRecorderTransactionData.removeProfileCall(profileObject.getProfileConcrete());
			t.setContextClassLoader(oldClassLoader);
		}
	}

	public static void commitChanges(ProfileObject profileObject) {
		if(profileObject.isProfileDirty()) {
			JPAUtils.INSTANCE.getEntityManager(profileObject.getProfileSpecificationComponent().getComponentID()).persist(profileObject.getProfileConcrete());	    
		}
	}

	/**
	 * Get the default value set for a profile attribute depending of his type
	 * 
	 * @param fieldType
	 *            the profile attribute type
	 * @return the default valu
	 */
	private Object getDefautValue(Class fieldType) {
		// Handle all primitives types
		if (fieldType.equals(int.class))
			return new Integer(0);
		if (fieldType.equals(long.class))
			return new Long(0);
		if (fieldType.equals(double.class))
			return new Double(0);
		if (fieldType.equals(short.class))
			return new Short(new Integer(0).shortValue());
		if (fieldType.equals(float.class))
			return new Float(0);
		if (fieldType.equals(char.class))
			return new Character(' ');
		if (fieldType.equals(boolean.class))
			return new Boolean(false);
		if (fieldType.equals(byte.class))
			return new Byte(new Integer(0).byteValue());

		return null;
	}

}
