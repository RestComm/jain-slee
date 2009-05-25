package org.mobicents.slee.container.profile;

import java.util.Collection;

import javax.slee.InvalidArgumentException;
import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;
import javax.slee.profile.AttributeTypeMismatchException;
import javax.slee.profile.ProfileLocalObject;
import javax.slee.profile.UnrecognizedQueryNameException;

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
	 * @param queryName
	 * @param arguments
	 * @return
	 * @throws InvalidArgumentException
	 * @throws AttributeTypeMismatchException
	 * @throws UnrecognizedQueryNameException
	 * @throws SLEEException
	 * @throws NullPointerException
	 */
	public static Collection<ProfileLocalObject> handle(
			ProfileTableImpl profileTable, String queryName, Object[] arguments)
			throws NullPointerException, TransactionRequiredLocalException, SLEEException,
			UnrecognizedQueryNameException, AttributeTypeMismatchException,
			InvalidArgumentException {
		return profileTable.getProfilesByStaticQuery(queryName, arguments);
	}

}
