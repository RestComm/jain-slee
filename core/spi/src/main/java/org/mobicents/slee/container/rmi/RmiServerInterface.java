/**
 * 
 */
package org.mobicents.slee.container.rmi;

import org.mobicents.slee.connector.local.MobicentsSleeConnectionFactory;
import org.mobicents.slee.container.SleeContainerModule;

/**
 * @author martins
 *
 */
public interface RmiServerInterface extends SleeContainerModule {
		
	public void setJndiName(String name);
	public String getJndiName();
}
