package org.mobicents.slee.resource.jdbc;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ResourceAdaptorTypeID;

/**
 * The {@link ActivityContextInterface} factory interface for a JDBC Resource
 * Adaptor.
 * 
 * @author martins
 * 
 */
public interface JdbcActivityContextInterfaceFactory {

	/**
	 * the ID of the RA Type
	 */
	public static final ResourceAdaptorTypeID RATYPE_ID = JdbcResourceAdaptorSbbInterface.RATYPE_ID;
	
	/**
	 * Retrieves the {@link ActivityContextInterface} associated with the
	 * provided activity.
	 * 
	 * @param activity
	 * @return
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	public ActivityContextInterface getActivityContextInterface(
			JdbcActivity activity) throws UnrecognizedActivityException,
			FactoryException;

}
