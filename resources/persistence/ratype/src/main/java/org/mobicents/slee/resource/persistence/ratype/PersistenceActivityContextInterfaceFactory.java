package org.mobicents.slee.resource.persistence.ratype;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

public interface PersistenceActivityContextInterfaceFactory {

	
	public ActivityContextInterface getActivityContextInterface(
			SbbEntityManager em) throws NullPointerException,
            UnrecognizedActivityException, FactoryException;
	
}
