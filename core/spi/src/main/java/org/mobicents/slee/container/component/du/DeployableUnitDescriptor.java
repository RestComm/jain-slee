/**
 * 
 */
package org.mobicents.slee.container.component.du;

import java.util.List;

/**
 * @author martins
 *
 */
public interface DeployableUnitDescriptor {

	public List<String> getJarEntries();

	public List<String> getServiceEntries();

	public boolean isSlee11();
	
}
