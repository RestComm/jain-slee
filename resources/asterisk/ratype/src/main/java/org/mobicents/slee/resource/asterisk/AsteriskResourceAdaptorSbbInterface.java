/*
 * Created on 19/Abr/2005
 *
 */
package org.mobicents.slee.resource.asterisk;

import org.asteriskjava.manager.action.ManagerAction;

/**
 * @author Sancho
 * @version 1.0
 * 
 */
public interface AsteriskResourceAdaptorSbbInterface {

	public abstract void sendAction(ManagerAction managerAction);
	
}
