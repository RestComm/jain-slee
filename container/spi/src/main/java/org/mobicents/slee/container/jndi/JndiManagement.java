package org.mobicents.slee.container.jndi;

import org.mobicents.slee.container.component.SleeComponent;

/**
 * 
 * @author mobicents
 *
 */
public interface JndiManagement {

	void componentInstall(SleeComponent component);
	
	void componentUninstall(SleeComponent component);
	
	void pushJndiContext(SleeComponent component);
	
	void popJndiContext();
}
