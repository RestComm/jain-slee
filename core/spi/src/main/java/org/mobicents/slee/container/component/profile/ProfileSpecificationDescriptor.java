/**
 * 
 */
package org.mobicents.slee.container.component.profile;

import java.util.List;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.profile.cmp.ProfileCMPInterfaceDescriptor;
import org.mobicents.slee.container.component.profile.query.CollatorDescriptor;
import org.mobicents.slee.container.component.profile.query.QueryDescriptor;

/**
 * @author martins
 * 
 */
public interface ProfileSpecificationDescriptor extends
		ComponentWithLibraryRefsDescriptor {

	/**
	 * 
	 * @return
	 */
	public List<? extends CollatorDescriptor> getCollators();

	/**
	 * 
	 * @return
	 */
	public List<? extends EnvEntryDescriptor> getEnvEntries();

	/**
	 * 
	 * @return
	 */
	public boolean getEventsEnabled();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileIndexDescriptor> getIndexedAttributes();

	/**
	 * 
	 * @return
	 */
	public ProfileAbstractClassDescriptor getProfileAbstractClass();

	/**
	 * 
	 * @return
	 */
	public ProfileCMPInterfaceDescriptor getProfileCMPInterface();

	/**
	 * 
	 * @return
	 */
	public ProfileLocalInterfaceDescriptor getProfileLocalInterface();

	/**
	 * 
	 * @return
	 */
	public String getProfileManagementInterface();

	/**
	 * 
	 * @return
	 */
	public ProfileSpecificationID getProfileSpecificationID();

	/**
	 * 
	 * @return
	 */
	public List<? extends ProfileSpecRefDescriptor> getProfileSpecRefs();

	/**
	 * 
	 * @return
	 */
	public String getProfileTableInterface();

	/**
	 * 
	 * @return
	 */
	public UsageParametersInterfaceDescriptor getProfileUsageParameterInterface();

	/**
	 * 
	 * @return
	 */
	public List<? extends QueryDescriptor> getQueryElements();

	/**
	 * 
	 * @return
	 */
	public boolean getReadOnly();

	/**
	 * 
	 * @return
	 */
	public String getSecurityPermissions();

	/**
	 * 
	 * @return
	 */
	public boolean isIsolateSecurityPermissions();

	/**
	 * 
	 * @return
	 */
	public boolean isSingleProfile();

}
