/**
 * 
 */
package org.mobicents.slee.container.component.sbb;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.ComponentWithLibraryRefsDescriptor;
import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;


/**
 * @author martins
 *
 */
public interface SbbDescriptor extends ComponentWithLibraryRefsDescriptor {

	/**
	 * Retrieves the map between aci attributes names and aliases.
	 * @return
	 */
	public Map<String,String> getActivityContextAttributeAliases();

	public ProfileSpecificationID getAddressProfileSpecRef();

	public Map<String, CMPFieldDescriptor> getCmpFields();

	public Set<EventTypeID> getDefaultEventMask();

	public List<EjbRefDescriptor> getEjbRefs();
	  
	public List<EnvEntryDescriptor> getEnvEntries();
	  
	public Map<EventTypeID, EventEntryDescriptor> getEventEntries();
	  
	/**
	 * Retrieves the map between event names and event types
	 * @return
	 */
	public Map<String,EventTypeID> getEventTypes();

	public Map<String, GetChildRelationMethodDescriptor> getGetChildRelationMethodsMap();

	public Map<String, GetProfileCMPMethodDescriptor> getGetProfileCMPMethods();
	
	public List<ProfileSpecRefDescriptor> getProfileSpecRefs();

	public List<ResourceAdaptorTypeBindingDescriptor> getResourceAdaptorTypeBindings();

	public SbbAbstractClassDescriptor getSbbAbstractClass();

	public String getSbbActivityContextInterface();

	public String getSbbAlias();

	public SbbID getSbbID();

	public SbbLocalInterfaceDescriptor getSbbLocalInterface();
	
	public List<SbbRefDescriptor> getSbbRefs();

	public UsageParametersInterfaceDescriptor getSbbUsageParametersInterface();
	
	public String getSecurityPermissions();

}
