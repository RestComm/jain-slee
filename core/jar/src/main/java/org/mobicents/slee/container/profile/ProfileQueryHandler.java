package org.mobicents.slee.container.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.ProfileID;
import javax.slee.profile.ProfileLocalObject;

import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

/**
 * 
 * Start time:12:49:42 2009-03-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * This class handles queries call to certain ProfileTable impl objects. It must
 * be called within transaction
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ProfileQueryHandler {

	/**
	 * Method that does lookup and creates PLOs
	 * 
	 * @param profileTable
	 *            - profile table impl object for which we perform search.
	 * @param queryName
	 *            - name of this query, this is requiered so it can be located
	 *            in ProfileSpecComponent and changed into acting object.
	 * @param arguments
	 *            - arguemnts of a query.
	 * @return collection of ProfileLocalObject.
	 * @throws SLEEException
	 *             - if something goes really wrong.
	 */
	public static Collection handle(ProfileTableImpl profileTable, String queryName, Object[] arguments) throws TransactionRequiredLocalException,SLEEException
	{
		profileTable.getSleeContainer().getTransactionManager().mandateTransaction();
		
		Collection<ProfileLocalObject> plocs = new ArrayList<ProfileLocalObject>();
		Collection<ProfileID> profileIDs = JPAUtils.INSTANCE.getProfilesByStaticQuery( profileTable.getProfileTableName(), queryName, arguments );

		Class plocClass = profileTable.getProfileSpecificationComponent().getProfileLocalObjectConcreteClass() == null ? ProfileLocalObjectImpl.class : profileTable.getProfileSpecificationComponent().getProfileLocalObjectConcreteClass(); 

		for(ProfileID profileID : profileIDs)
		{
		  ProfileObject po = profileTable.getProfile(profileID.getProfileName());
      try {
        plocs.add((ProfileLocalObject) plocClass.getConstructor(ProfileObject.class).newInstance(po));
      }
      catch ( Exception e ) {
        throw new SLEEException("Unable to create Profile Local Object.", e);
      }
		}
		
		return Collections.unmodifiableCollection(plocs);
	}

}
