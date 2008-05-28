package org.mobicents.slee.resource.xcapclient;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;
import javax.slee.resource.ResourceAdaptor;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.resource.xcapclient.AsyncActivity;
import org.mobicents.slee.resource.xcapclient.XCAPClientActivityContextInterfaceFactory;
import org.mobicents.slee.resource.xcapclient.XCAPResourceAdaptorActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 * @author Eduardo Martins
 * @version 2.0
 *
 */

public class XCAPClientActivityContextInterfaceFactoryImpl implements
		ResourceAdaptorActivityContextInterfaceFactory,
		XCAPClientActivityContextInterfaceFactory {
	
    private final String jndiName = "java:slee/resources/xcapclientacif";
    private String raEntityName;
    private SleeContainer sleeContainer;
    private ActivityContextFactory activityContextFactory;

    public XCAPClientActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String raEntityName) {
        this.sleeContainer = sleeContainer;
        this.activityContextFactory = sleeContainer.getActivityContextFactory();
        this.raEntityName = raEntityName;
    }
    
	public String getJndiName() {
		return jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(AsyncActivity activity) throws NullPointerException,
	UnrecognizedActivityException, FactoryException {
		
		// if parameter is null throw exception
		if (activity == null) {
			throw new NullPointerException();
		}
		
		// get handle
		XCAPResourceAdaptorActivityHandle handle = ((AsyncActivityImpl)activity).getHandle();
		
		// check if activity exists
		ResourceAdaptorEntity raEntity = sleeContainer.getResourceAdaptorEnitity(raEntityName);
		ResourceAdaptor ra  = raEntity.getResourceAdaptor();
				
		// if it doesn't exist throw exception
		if (ra.getActivity(handle) == null) {
			throw new UnrecognizedActivityException(activity);
		}
		else {
			return new ActivityContextInterfaceImpl(this.sleeContainer,
				this.activityContextFactory.getActivityContext(new SleeActivityHandle(raEntityName, handle, sleeContainer)).getActivityContextId());
		}
	}
	
}
