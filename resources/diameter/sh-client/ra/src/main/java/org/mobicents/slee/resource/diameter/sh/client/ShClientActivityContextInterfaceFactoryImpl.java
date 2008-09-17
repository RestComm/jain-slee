package org.mobicents.slee.resource.diameter.sh.client;

import javax.slee.ActivityContextInterface;
import javax.slee.UnrecognizedActivityException;

import org.mobicents.slee.container.SleeContainer;

import net.java.slee.resource.diameter.sh.client.ShClientActivity;
import net.java.slee.resource.diameter.sh.client.ShClientActivityContextInterfaceFactory;
import net.java.slee.resource.diameter.sh.client.ShClientNotificationActivity;

public class ShClientActivityContextInterfaceFactoryImpl implements ShClientActivityContextInterfaceFactory {

	public ShClientActivityContextInterfaceFactoryImpl(SleeContainer serviceContainer, String entityName) {
		// TODO Auto-generated constructor stub
	}

	public ActivityContextInterface getActivityContextInterface(ShClientActivity activity) throws UnrecognizedActivityException {
		// TODO Auto-generated method stub
		return null;
	}

	public ActivityContextInterface getActivityContextInterface(ShClientNotificationActivity activity) throws UnrecognizedActivityException {
		// TODO Auto-generated method stub
		return null;
	}

}
