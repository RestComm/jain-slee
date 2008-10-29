package org.mobicents.slee.resource.xmpp;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 * @author Eduardo Martins
 * @author Neutel
 * @version 2.1
 *
 */

public class XmppActivityContextInterfaceFactoryImpl implements
		ResourceAdaptorActivityContextInterfaceFactory,
		XmppActivityContextInterfaceFactory {
	
    private final String jndiName = "java:slee/resources/xmppacif";
    private XmppResourceAdaptor ra;
    private String raEntityName;
    private SleeContainer sleeContainer;
    private ActivityContextFactory activityContextFactory;
    
    
    public XmppActivityContextInterfaceFactoryImpl(SleeContainer sleeContainer, String raEntityName, XmppResourceAdaptor ra) {
        this.sleeContainer = sleeContainer;
        this.activityContextFactory = sleeContainer.getActivityContextFactory();
        this.ra = ra;
        this.raEntityName = raEntityName;
    }
    
	public String getJndiName() {
		return jndiName;
	}

	public ActivityContextInterface getActivityContextInterface(String connectionId) throws NullPointerException,
	UnrecognizedActivityException, FactoryException {
		
		// if parameter is null throw exception
		if (connectionId == null) {
			throw new NullPointerException();
		}
		
		// create handle
		XmppActivityHandle handle = new XmppActivityHandle(connectionId);
		
		// check if activity exists
		if (ra.getActivity(handle) == null) {
			throw new UnrecognizedActivityException(connectionId);
		}
		else {
			return new ActivityContextInterfaceImpl(
				this.activityContextFactory.getActivityContext(new SleeActivityHandle(raEntityName, handle, sleeContainer)).getActivityContextId());
		}
	}

}
