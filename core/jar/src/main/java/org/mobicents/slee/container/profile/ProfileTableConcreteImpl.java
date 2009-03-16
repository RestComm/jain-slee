package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.CreateException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.management.ProfileTableNotification;
import javax.slee.profile.ProfileAlreadyExistsException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.profile.ReadOnlyProfileException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ProfileSpecificationComponent;
import org.mobicents.slee.runtime.transaction.SleeTransactionManager;

/**
 * 
 * Start time:11:20:19 2009-03-14<br>
 * Project: mobicents-jainslee-server-core<br>
 * This is wrapper class for defined profiles. Its counter part is Service class
 * which manages SbbEntities. Actual profile with its data is logical SbbEntity.
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileTableConcreteImpl implements ProfileTableConcrete {

	public static final int _UNICODE_RANGE_START = 0x0020;
	public static final int _UNICODE_RANGE_END = 0x007e;
	public static final int _UNICODE_SLASH = 0x002f;
	
	
	private SleeProfileManagement sleeProfileManagement = null;
	private String profileTableName = null;
	private ProfileTableNotification profileTableNotification = null;
	private SleeContainer sleeContainer = null;
	private ProfileSpecificationID profileSpecificationId;
	public ProfileTableConcreteImpl(SleeProfileManagement sleeProfileManagement, String profileTableName, ProfileSpecificationID profileSpecificationId) {
		super();

		if (sleeProfileManagement == null || profileTableName == null || profileSpecificationId == null) {
			throw new NullPointerException("Parameters must not be null");
		}
		this.sleeProfileManagement = sleeProfileManagement;
		this.profileTableName = profileTableName;
		this.profileTableNotification = new ProfileTableNotification(this.profileTableName);
		this.sleeContainer = this.sleeProfileManagement.getSleeContainer();
		this.profileSpecificationId= profileSpecificationId;

	}

	public SleeProfileManagement getProfileManagement() {

		return sleeProfileManagement;
	}

	public String getProfileTableName() {
		return this.profileTableName;
	}

	public ProfileTableNotification getProfileTableNotification() {
		return this.profileTableNotification;
	}

	public ProfileLocalObject create(String profileName) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, ReadOnlyProfileException,
			ProfileAlreadyExistsException, CreateException, SLEEException {

		
		// Its transactional method
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();
		
		validateProfileName(profileName);
		
		// FIXME add check for profile existency, this can be done only when we
		// have JPA ?
		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
		if(component == null)
		{
			throw new SLEEException("Could not find profile specification "+this.profileSpecificationId+" for profile table: "+this.profileTableName);
		}
		if (component.getDescriptor().getReadOnly()) {
			throw new ReadOnlyProfileException("Profile Specification declares profile to be read only.");
		}

		
		 
		
		// Pool p = sleeProfileManagement.getPool(this.profileTableName);
		//ProfileObjec po = p.borrowObject();

		ProfileObject po = new ProfileObject(this,profileSpecificationId);
		//FIXME: those should be done by pool;
		po.setProfileName(profileName);
		po.setProfileContext(new ProfileContextImpl(this));
		po.setState(ProfileObjectState.POOLED);
		
		po.profilePostCreate();
		po.setState(ProfileObjectState.READY);
		//FIXME: add action to transaction to passivate this object?
		//FIXME: add ProfileLocalObject, somehow
		
		return null;
	}

	public ProfileLocalObject find(String profielName) throws NullPointerException, TransactionRequiredLocalException, SLEEException {
		return this.find(profielName, false);
	}

	/**
	 * 
	 * @param profielName
	 * @param allowNull - this is used only for default profile.
	 * @return
	 * @throws NullPointerException
	 * @throws TransactionRequiredLocalException
	 * @throws SLEEException
	 */
	public ProfileLocalObject find(String profielName, boolean allowNull) throws NullPointerException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Collection findAll() throws TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public ProfileLocalObject findProfileByAttribute(String arg0, Object arg1) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection findProfilesByAttribute(String arg0, Object arg1) throws NullPointerException, IllegalArgumentException, TransactionRequiredLocalException, SLEEException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(String profileName) throws NullPointerException, ReadOnlyProfileException, TransactionRequiredLocalException, SLEEException {
		if(profileName == null)
		{
			throw new NullPointerException("Profile name must not be null");
		}
		
		SleeTransactionManager txMgr = sleeContainer.getTransactionManager();
		txMgr.mandateTransaction();
		
		ProfileSpecificationComponent component = this.sleeProfileManagement.getProfileSpecificationComponent(this.profileSpecificationId);
		if(component == null)
		{
			throw new SLEEException("No defined profiel specification.");
		}
		
		if(component.getDescriptor().getReadOnly())
		{
			throw new ReadOnlyProfileException("Profile specification: "+this.profileSpecificationId +", is read only.");
		}
		
		//FIXME: add this
		//if(profileExists)
		{
//			ProfileObject po = new ProfileObject(this,profileSpecificationId);
//			//FIXME: those should be done by pool;
//			po.setProfileName(profileName);
//			po.setProfileContext(new ProfileContextImpl(this));
//			po.setState(ProfileObjectState.POOLED);
//			
//			po.profileLoad();
//			po.setState(ProfileObjectState.READY);
//			po.profileRemove();
//			po.setState(ProfileObjectState.POOLED);
		}
		
		return false;
	}
	
	
	public static void validateProfileName(String profileName) throws IllegalArgumentException, NullPointerException
	{
		if (profileName == null) {
			throw new NullPointerException("ProfileName must not be null");
		}
		if(profileName.length()==0)
		{
			throw new IllegalArgumentException("Profile name must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}
		
		for(int i = 0;i<profileName.length();i++)
		{
			Character c=  profileName.charAt(i);
			int unicodeCode = c.charValue();
			if(c.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START<=c && c<=_UNICODE_RANGE_END))
			{
				//fine
			}else
			{
				throw new IllegalArgumentException("Profile name contains illegal character, name: "+profileName+", at index: "+i);
			}
		}
		
	}
	
	public static void validateProfileTableName(String profileTableName) throws IllegalArgumentException, NullPointerException
	{
		if (profileTableName == null) {
			throw new NullPointerException("ProfileTableName must not be null");
		}
		if(profileTableName.length()==0)
		{
			throw new IllegalArgumentException("ProfileTableName must not be empty, see section 10.2.4 of JSLEE 1.1 specs");
		}
		
		for(int i = 0;i<profileTableName.length();i++)
		{
			Character c=  profileTableName.charAt(i);
			int unicodeCode = c.charValue();
			if( (c.isLetterOrDigit(c.charValue()) || (_UNICODE_RANGE_START<=c && c<=_UNICODE_RANGE_END)) && unicodeCode!=_UNICODE_SLASH)
			{
				//fine
			}else
			{
				throw new IllegalArgumentException("ProfileTableName contains illegal character, name: "+profileTableName+", at index: "+i);
			}
		}
	}

	
	
	
	
	
	

}
