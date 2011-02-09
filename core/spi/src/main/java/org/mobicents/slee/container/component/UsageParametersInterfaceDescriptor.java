/**
 * 
 */
package org.mobicents.slee.container.component;

import java.util.List;

/**
 * @author martins
 *
 */
public interface UsageParametersInterfaceDescriptor {

	public String getUsageParametersInterfaceName();

	public List<UsageParameterDescriptor> getUsageParameter();
}
