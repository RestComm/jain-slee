/*
 * Created on 11/Abr/2005
 *
 */
package org.mobicents.slee.resource.asterisk;

import javax.slee.ActivityContextInterface;
import javax.slee.FactoryException;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory;
import org.mobicents.slee.resource.SleeActivityHandle;
import org.mobicents.slee.runtime.ActivityContextFactory;
import org.mobicents.slee.runtime.ActivityContextInterfaceImpl;

/**
 * @author Sancho
 * @version 1.0
 *
 */
public class AsteriskActivityContextInterfaceFactoryImpl implements
		AsteriskActivityContextInterfaceFactory,ResourceAdaptorActivityContextInterfaceFactory {
	
    private final String jndiName = "java:slee/resources/asteriskacif";
    private String raEntityName;
    private SleeContainer serviceContainer;

    private ActivityContextFactory activityContextFactory;

    public AsteriskActivityContextInterfaceFactoryImpl(SleeContainer svcContainer, String entityName) {
        this.serviceContainer = svcContainer;
        this.activityContextFactory = svcContainer.getActivityContextFactory();
        this.raEntityName = entityName;
        
    }
	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.ResourceAdaptorActivityContextInterfaceFactory#getJndiName()
	 */
	public String getJndiName() {
		// TODO Auto-generated method stub
		return jndiName;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.resource.asterisk.AsteriskActivityContextInterfaceFactory#getActivityContextInterface(net.sf.asterisk.manager.ManagerConnection)
	 */
	public ActivityContextInterface getActivityContextInterface(
			AsteriskManagerMessage asteriskManagerMessage) throws NullPointerException,
			UnrecognizedActivityException, FactoryException {
        if (asteriskManagerMessage == null)
            throw new NullPointerException("asterisk connection ! huh!!");
        return new ActivityContextInterfaceImpl(
                this.activityContextFactory.getActivityContext(new SleeActivityHandle(raEntityName, new AsteriskActivityHandle(asteriskManagerMessage.MessageID()), serviceContainer)).getActivityContextId());
    }

}
