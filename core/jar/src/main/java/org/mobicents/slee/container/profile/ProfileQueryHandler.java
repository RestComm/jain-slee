package org.mobicents.slee.container.profile;

import javax.slee.SLEEException;
import javax.slee.TransactionRequiredLocalException;

import org.mobicents.slee.container.deployment.profile.jpa.JPAUtils;

/**
 * 
 * Start time:12:49:42 2009-03-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * This class handles queries call to certain ProfileTable impl objects. It must
 * be called within transaction
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
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
	public static java.util.Collection handle(ProfileTableImpl profileTable, String queryName, Object[] arguments) throws TransactionRequiredLocalException,SLEEException {

		profileTable.getSleeContainer().getTransactionManager().mandateTransaction();
		
		return JPAUtils.INSTANCE.getProfilesByStaticQuery( profileTable.getProfileTableName(), queryName, arguments );
	}

}
