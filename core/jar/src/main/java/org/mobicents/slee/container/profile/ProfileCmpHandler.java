package org.mobicents.slee.container.profile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;

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

	private ProfileObject profileObject = null;

	private boolean dirty = false;

	private static final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
	
	public void setProfileObject(ProfileObject profileObject) {
		this.profileObject = profileObject;
	}

	/**
	 * 
	 * @param fieldName
	 * @param value
	 */
	public void setCmpField(String fieldName, Object value) throws UnsupportedOperationException, IllegalStateException
	{
	  sleeContainer.getTransactionManager().mandateTransaction();
		
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			// this operation is allowed ONLY
			// 1. for 1.1 profiles - if profile is write able
			// 2. for management clients
			// if a sbb tries to set a value, it is not authorized

			// This covers Management client
			if (this.profileObject.isManagementView())
			{
				// write check is a double check to MBean, but lets be clear here
				if (!this.profileObject.isWriteable())
				{
					throw new ReadOnlyProfileException("Profile: " + this.profileObject.getProfileName() + ", table:" + this.profileObject.getProfileTableConcrete().getProfileTableName() + " ,is not writeable.");
				}
			}
			else
			{
				// this gets
				if (!this.profileObject.isProfileSpecificationWriteable())
				{
					throw new ReadOnlyProfileException("Profile: " + this.profileObject.getProfileName() + ", table:" + this.profileObject.getProfileTableConcrete().getProfileTableName() + " ,is not writeable.");
				}
			}

			if (!this.profileObject.isCanAccessCMP()) {
				throw new IllegalStateException("Can not access CMP field at this moment.");
			}

			// FIXME: Alexandre: [DONE] Add the real set code here (call set on concrete?)
			try
      {
			  String methodName = "set" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0));
        Method m = this.profileObject.getProfileConcrete().getClass().getMethod(methodName, value.getClass());
        m.invoke( this.profileObject.getProfileConcrete(), value );
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
			this.profileObject.getProfileConcrete().setProfileDirty(true);
		}
		finally
		{
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}
	}

	/**
	 * 
	 * @param fieldName
	 *            = Introspector.decapitalize(method.getName().substring(3));
	 * @return
	 */
	public Object getCmpField(String fieldName)
	{
	  sleeContainer.getTransactionManager().mandateTransaction();
	  
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try
		{
			ProfileCallRecorderTransactionData.addProfileCall(this.profileObject.getProfileConcrete());
			
			if (!this.profileObject.isCanAccessCMP())
			{
				throw new IllegalStateException("Can not access CMP field at this moment.");
			}
			
			Object retVal = null;
			
      // FIXME: Alexandre: [DONE] Return value here (call get on concrete?)
      try
      {
        String methodName = "get" + fieldName.replace(fieldName.charAt(0), fieldName.toUpperCase().charAt(0));
        Method m = this.profileObject.getProfileConcrete().getClass().getMethod(methodName);
        retVal = m.invoke( this.profileObject.getProfileConcrete());
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
			t.setContextClassLoader(oldClassLoader);
			ProfileCallRecorderTransactionData.removeProfileCall(this.profileObject.getProfileConcrete());
		}
	}

	public void commitChanges()
	{
		Thread t = Thread.currentThread();
		ClassLoader oldClassLoader = t.getContextClassLoader();
		t.setContextClassLoader(this.profileObject.getProfileSpecificationComponent().getClassLoader());
		
		try {
		  if(dirty)
		  {
	      // FIXME: Alexandre : Add call to persist in concrete		    
		  }
		} finally {
			t.setContextClassLoader(oldClassLoader);
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
