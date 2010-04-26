/**
 * 
 */
package org.mobicents.slee.container.component.ra;

import java.util.List;

import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;

/**
 * @author martins
 *
 */
public interface ResourceAdaptorDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<ResourceAdaptorTypeID> getResourceAdaptorTypeRefs();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileSpecRefDescriptor> getProfileSpecRefs();

	/**
	 * 
	 * @return
	 */
	public List<? extends ConfigPropertyDescriptor> getConfigProperties();

	/**
	 * 
	 * @return
	 */
	public boolean getIgnoreRaTypeEventTypeCheck();

	/**
	 * 
	 * @return
	 */
	public UsageParametersInterfaceDescriptor getResourceAdaptorUsageParametersInterface();

	/**
	 * 
	 * @return
	 */
	public String getResourceAdaptorClassName();

	/**
	 * 
	 * @return
	 */
	public boolean getSupportsActiveReconfiguration();

	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorID getResourceAdaptorID();

	/**
	 * 
	 * @return
	 */
	public String getSecurityPermissions();

}
