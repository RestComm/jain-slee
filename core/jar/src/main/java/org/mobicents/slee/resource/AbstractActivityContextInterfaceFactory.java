package org.mobicents.slee.resource;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.runtime.activity.ActivityContext;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.mobicents.slee.runtime.activity.ActivityContextHandlerFactory;
import org.mobicents.slee.runtime.activity.ActivityContextInterfaceImpl;

/**
 * Implementation of the abstract resource adaptor aci factory code.
 * 
 * @author martins
 * 
 */
public class AbstractActivityContextInterfaceFactory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private transient SleeContainer sleeContainer;
	
	/**
	 * 
	 */
	private final ResourceAdaptorTypeID resourceAdaptorTypeID;

	/**
	 * 
	 * @param sleeContainer
	 * @param resourceAdaptorTypeID
	 */
	protected AbstractActivityContextInterfaceFactory(
			SleeContainer sleeContainer,
			ResourceAdaptorTypeID resourceAdaptorTypeID) {
		this.sleeContainer = sleeContainer;
		this.resourceAdaptorTypeID = resourceAdaptorTypeID;
	}

	/**
	 * This method returns the aci for the specified activity, if exists, it
	 * should be invoked by each impl of methods of an ra type aci factory.
	 * 
	 * @param activity
	 * @return
	 * @throws NullPointerException
	 * @throws UnrecognizedActivityException
	 * @throws FactoryException
	 */
	protected ActivityContextInterface getACI(Object activity)
			throws NullPointerException, UnrecognizedActivityException,
			FactoryException {
		if (activity == null) {
			throw new NullPointerException("null activity object");
		}
		ActivityHandle handle = null;
		for (ResourceAdaptorEntity raEntity : sleeContainer
				.getResourceManagement().getResourceAdaptorEntitiesPerType()
				.get(resourceAdaptorTypeID)) {
			handle = raEntity.getResourceAdaptorObject().getActivityHandle(
					activity);
			if (handle != null) {
				ActivityContextHandle ach = ActivityContextHandlerFactory
						.createExternalActivityContextHandle(
								raEntity.getName(), handle);
				ActivityContext ac = sleeContainer.getActivityContextFactory()
						.getActivityContext(ach);
				if (ac != null) {
					return new ActivityContextInterfaceImpl(ac);
				}
				break;
			}
		}

		throw new UnrecognizedActivityException(activity.toString());
	}

	@Override
	public int hashCode() {
		return resourceAdaptorTypeID.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == this.getClass()) {
			return ((AbstractActivityContextInterfaceFactory) obj).resourceAdaptorTypeID
					.equals(this.resourceAdaptorTypeID);
		} else {
			return false;
		}
	}

	private void readObject(ObjectInputStream is)
			throws ClassNotFoundException, IOException {
		is.defaultReadObject();
		sleeContainer = SleeContainer.lookupFromJndi();
	}

}
