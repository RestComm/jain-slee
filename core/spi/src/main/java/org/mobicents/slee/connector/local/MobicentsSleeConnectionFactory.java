/**
 * 
 */
package org.mobicents.slee.connector.local;

import javax.slee.connection.SleeConnectionFactory;

import org.mobicents.slee.container.SleeContainerModule;

/**
 * Interface for Mobicents SleeConnectionFactory to comply with. 
 * @author baranowb
 *
 */
public interface MobicentsSleeConnectionFactory extends SleeConnectionFactory, SleeContainerModule {

	/**
	 * Sets JNDI name to be used while binding this factory. Example value:
	 * <ul>
	 * <li>/SleeConnectionFactory</li>
	 * <li>java:comp/PrivateSleeConnectionFactory</li>
	 * </ul>
	 * @param name
	 */
	public void setJNDIName(String name);
	public String getJNDIName();
	
}
