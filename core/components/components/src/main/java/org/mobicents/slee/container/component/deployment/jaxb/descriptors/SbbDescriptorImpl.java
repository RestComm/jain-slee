package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.UsageParametersInterfaceDescriptor;
import org.mobicents.slee.container.component.common.EnvEntryDescriptor;
import org.mobicents.slee.container.component.common.ProfileSpecRefDescriptor;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MActivityContextAttributeAlias;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbb;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbClasses;
import org.mobicents.slee.container.component.sbb.CMPFieldDescriptor;
import org.mobicents.slee.container.component.sbb.EjbRefDescriptor;
import org.mobicents.slee.container.component.sbb.EventEntryDescriptor;
import org.mobicents.slee.container.component.sbb.GetChildRelationMethodDescriptor;
import org.mobicents.slee.container.component.sbb.GetProfileCMPMethodDescriptor;
import org.mobicents.slee.container.component.sbb.ResourceAdaptorTypeBindingDescriptor;
import org.mobicents.slee.container.component.sbb.SbbAbstractClassDescriptor;
import org.mobicents.slee.container.component.sbb.SbbDescriptor;
import org.mobicents.slee.container.component.sbb.SbbLocalInterfaceDescriptor;
import org.mobicents.slee.container.component.sbb.SbbRefDescriptor;

/**
 * Start time:16:54:43 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class SbbDescriptorImpl extends AbstractComponentWithLibraryRefsDescriptor implements SbbDescriptor {

	private SbbID sbbID;
	private String sbbAlias;

	private List<SbbRefDescriptor> sbbRefs;
	private List<ProfileSpecRefDescriptor> profileSpecRefs;

	// might be bad, we ommit sbb-classes/description, phew
	private ProfileSpecificationID addressProfileSpecRef;

	/**
	 * mapping between event type id to event entries in descriptor
	 */
	private Map<EventTypeID, EventEntryDescriptor> eventEntries;

	/**
	 * mapping between event name and event type id in descriptor
	 */
	private Map<String, EventTypeID> eventTypesPerName;
	
	private Map<String,String> activityContextAttributeAliases;
	private List<EnvEntryDescriptor> envEntries;
	private List<ResourceAdaptorTypeBindingDescriptor> resourceAdaptorTypeBindings;

	// 1.1 stuff, profile specs refs have alias element, so we need another.
	private List<EjbRefDescriptor> ejbRefs;

	private String securityPermisions;

	/**
	 * references sbb abstract class map of get profile cmp methods
	 */
	private Map<String, GetProfileCMPMethodDescriptor> getProfileCMPMethods;

	/**
	 * references sbb abstract class map of get child relation methods
	 */
	private final Map<String, GetChildRelationMethodDescriptor> getChildRelationMethodsMap;
	/**
	 * values() of the map above 
	 */
	private final Collection<GetChildRelationMethodDescriptor> getChildRelationMethodsCollection;
	
	/**
	 * cmp fields mapped by field name and with sbb alias dereferenced (for sbb local object cmps)
	 */
    private Map<String,CMPFieldDescriptor> cmpFields;

    /**
     * this is the default set of masked event types, used when an sbb entity related with this sbb attaches to an aci
     */
    private Set<EventTypeID> defaultEventMask;
    
    private final SbbAbstractClassDescriptor sbbAbstractClassDescriptor;
    private final SbbLocalInterfaceDescriptor sbbLocalInterfaceDescriptor;
    private final String sbbActivityContextInterface;
    private final UsageParametersInterfaceDescriptor sbbUsageParametersInterface;
    
	public SbbDescriptorImpl(MSbb sbb,
			MSecurityPermissions sbbJarSecurityPermissions, boolean isSlee11)
			throws DeploymentException {
		
		super(isSlee11);
		super.setLibraryRefs(sbb.getLibraryRefs());
		
		try {
			this.sbbID = new SbbID(sbb.getSbbName(), sbb.getSbbVendor(), sbb
					.getSbbVersion());

			this.sbbAlias = sbb.getSbbAlias();

			this.ejbRefs = sbb.getEjbRef();
			this.profileSpecRefs = sbb.getProfileSpecRef();

			String addressProfileSpecAliasRef = sbb
					.getAddressProfileSpecAliasRef();
			if (addressProfileSpecAliasRef != null) {
				if (this.profileSpecRefs != null) {
					for (ProfileSpecRefDescriptor mProfileSpecRef : this.profileSpecRefs) {
						if (mProfileSpecRef.getProfileSpecAlias().equals(
								addressProfileSpecAliasRef)) {
							this.addressProfileSpecRef = mProfileSpecRef
									.getComponentID();
						}
					}
				} else {
					throw new DeploymentException(
							"the address profile spec alias in sbb descriptor is defined but there are no profile specs references");
				}
			}
			else {
				if (isSlee11) {
					this.addressProfileSpecRef = new ProfileSpecificationID("AddressProfileSpec","javax.slee","1.1");
				}
				else {
					this.addressProfileSpecRef = new ProfileSpecificationID("AddressProfileSpec","javax.slee","1.0");
				}
			}

			this.sbbRefs = sbb.getSbbRef();

			final MSbbClasses sbbClasses = sbb.getSbbClasses();
			this.sbbAbstractClassDescriptor = sbbClasses.getSbbAbstractClass();
			this.sbbLocalInterfaceDescriptor = sbbClasses.getSbbLocalInterface();
			this.sbbActivityContextInterface = sbbClasses.getSbbActivityContextInterface() == null ? null : sbbClasses.getSbbActivityContextInterface().getInterfaceName();
			this.sbbUsageParametersInterface = sbbClasses.getSbbUsageParametersInterface();
			
			// build event type map and sets optimized for runtime
			eventEntries = new HashMap<EventTypeID, EventEntryDescriptor>(sbb.getEvent()
					.size() * 2 + 1);
			eventTypesPerName = new HashMap<String,EventTypeID>(sbb.getEvent()
					.size() * 2 + 1);
			for (MEventEntry mEventEntry : sbb.getEvent()) {			
				EventTypeID eventTypeID = mEventEntry.getEventReference();
				if (eventEntries.containsKey(eventTypeID)) {
					throw new DeploymentException("the sbb descriptor contains multiple event handler methods for "+eventTypeID);
				}
				eventEntries.put(eventTypeID, mEventEntry);
				eventTypesPerName.put(mEventEntry.getEventName(), eventTypeID);
			}

			activityContextAttributeAliases = new HashMap<String, String>();
			for (MActivityContextAttributeAlias alias : sbb.getActivityContextAttributeAlias()) {
				for (String sbbActivityContextAttributeName : alias.getSbbActivityContextAttributeName()) {
					activityContextAttributeAliases.put(sbbActivityContextAttributeName, alias.getAttributeAliasName());
	        	}
			}
			
			this.envEntries = sbb.getEnvEntry();

			this.resourceAdaptorTypeBindings = sbb
					.getResourceAdaptorTypeBinding();

			this.securityPermisions = sbbJarSecurityPermissions == null ? null : sbbJarSecurityPermissions.getSecurityPermissionSpec();

			// lets prepare child relation and profile cmp methods with aliases
			// dereferenced, for optimized runtime performance
			this.getChildRelationMethodsMap = sbbClasses.getSbbAbstractClass()
					.getChildRelationMethods();
			for (GetChildRelationMethodDescriptor mGetChildRelationMethod : getChildRelationMethodsMap
					.values()) {
				for (SbbRefDescriptor mSbbRef : sbbRefs) {
					if (((MGetChildRelationMethod)mGetChildRelationMethod).getSbbAliasRef().equals(
							mSbbRef.getSbbAlias())) {
						((MGetChildRelationMethod)mGetChildRelationMethod).setSbbID(mSbbRef
								.getComponentID());
						break;
					}
				}
			}
			this.getChildRelationMethodsCollection = getChildRelationMethodsMap.values();
			this.getProfileCMPMethods = sbbClasses.getSbbAbstractClass()
					.getProfileCMPMethods();
			for (GetProfileCMPMethodDescriptor getProfileCMPMethodDescriptor : getProfileCMPMethods
					.values()) {
				MGetProfileCMPMethod mGetProfileCMPMethod = (MGetProfileCMPMethod) getProfileCMPMethodDescriptor;
				for (ProfileSpecRefDescriptor mProfileSpecRef : profileSpecRefs) {
					if (mGetProfileCMPMethod.getProfileSpecAliasRef().equals(
							mProfileSpecRef.getProfileSpecAlias())) {
						((MGetProfileCMPMethod)mGetProfileCMPMethod)
								.setProfileSpecificationID(mProfileSpecRef
										.getComponentID());
						break;
					}
				}
			}

			// build cmp field map
			this.cmpFields = new HashMap<String,CMPFieldDescriptor>();
		    for(CMPFieldDescriptor cmpFieldDescriptor : sbbClasses.getSbbAbstractClass().getCmpFields()) {
		      this.cmpFields.put(cmpFieldDescriptor.getCmpFieldName(),cmpFieldDescriptor);
		      MSbbCMPField mSbbCMPField = (MSbbCMPField) cmpFieldDescriptor;
		      if (mSbbCMPField.getSbbAliasRef() != null) {
		    	  // dereference the alias
		    	  for (SbbRefDescriptor ref : sbbRefs) {
		    		  if (ref.getSbbAlias().equals(mSbbCMPField.getSbbAliasRef())) {
		    			  mSbbCMPField.setSbbRef(ref.getComponentID());
		    			  break;
		    		  }
		    	  }
		      }
		    }
		    
		    // build default event mask for this sbb entities
		 	Collection<EventEntryDescriptor> mEventEntries = getEventEntries().values();
			HashSet<EventTypeID> maskedEvents = null;
			if (mEventEntries != null) {
				maskedEvents = new HashSet<EventTypeID>();
				for (EventEntryDescriptor mEventEntry : mEventEntries) {
					if (mEventEntry.isMaskOnAttach()) {
						maskedEvents.add(mEventEntry.getEventReference());
					}
				}
			}
			if (maskedEvents != null && !maskedEvents.isEmpty()) {
				defaultEventMask = Collections.unmodifiableSet(maskedEvents);
			}
		    
			buildDependenciesSet();
		} catch (DeploymentException e) {
			throw e;
		} catch (Throwable e) {
			throw new DeploymentException("Failed to build sbb descriptor", e);
		}
	}

	private void buildDependenciesSet() {

		this.dependenciesSet.addAll(eventEntries.keySet());
		this.dependenciesSet.add(addressProfileSpecRef);

		for (SbbRefDescriptor sbbRef : sbbRefs) {
			this.dependenciesSet.add(sbbRef.getComponentID());
		}

		for (ProfileSpecRefDescriptor profileSpecRef : profileSpecRefs) {
			this.dependenciesSet.add(profileSpecRef.getComponentID());
		}

		for (ResourceAdaptorTypeBindingDescriptor binding : resourceAdaptorTypeBindings) {
			this.dependenciesSet.add(binding.getResourceAdaptorTypeRef());
		}

		// FIXME: EJB's do not have component ID... what gives?
		// for(MEjbRef ejbRef : ejbRefs)
		// {
		// this.dependenciesSet.add( ejbRef.getComponentID() );
		// }
	}

	public SbbID getSbbID() {
		return sbbID;
	}

	public String getSbbAlias() {
		return sbbAlias;
	}

	public List<SbbRefDescriptor> getSbbRefs() {
		return sbbRefs;
	}

	public List<ProfileSpecRefDescriptor> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	public ProfileSpecificationID getAddressProfileSpecRef() {
		return addressProfileSpecRef;
	}

	/**
	 * Retrieves the map between event type ids and event entries, in descriptors
	 * 
	 * @return
	 */
	public Map<EventTypeID, EventEntryDescriptor> getEventEntries() {
		return eventEntries;
	}

	/**
	 * Retrieves the event type id mapped to the specified event name
	 * @param eventName
	 * @return
	 */
	public Map<String,EventTypeID> getEventTypes() {
		return eventTypesPerName;
	}
	
	public Map<String,String> getActivityContextAttributeAliases() {
		return activityContextAttributeAliases;
	}

	public List<EnvEntryDescriptor> getEnvEntries() {
		return envEntries;
	}

	public List<ResourceAdaptorTypeBindingDescriptor> getResourceAdaptorTypeBindings() {
		return resourceAdaptorTypeBindings;
	}

	public List<EjbRefDescriptor> getEjbRefs() {
		return ejbRefs;
	}

	public String getSecurityPermissions() {
		return securityPermisions;
	}

	public SbbAbstractClassDescriptor getSbbAbstractClass() {
		return this.sbbAbstractClassDescriptor;
	}

	public SbbLocalInterfaceDescriptor getSbbLocalInterface() {
		return this.sbbLocalInterfaceDescriptor;
	}

	public String getSbbActivityContextInterface() {
		return this.sbbActivityContextInterface;
	}

	/**
	 * @see MSbbAbstractClass#getChildRelationMethods()
	 * @return
	 */
	public Map<String, GetChildRelationMethodDescriptor> getGetChildRelationMethodsMap() {
		return getChildRelationMethodsMap;
	}

	/**
	 *  
	 * @return the getChildRelationMethodsCollection
	 */
	public Collection<GetChildRelationMethodDescriptor> getGetChildRelationMethodsCollection() {
		return getChildRelationMethodsCollection;
	}
	
	/**
	 * @see MSbbAbstractClass#getGetProfileCMPMethods()
	 * @return
	 */
	public Map<String, GetProfileCMPMethodDescriptor> getGetProfileCMPMethods() {
		return getProfileCMPMethods;
	}

	/**
	 * Retrieves cmp fields mapped by field name and with sbb alias dereferenced (for sbb local object cmps) 
	 * @return
	 */
	public Map<String, CMPFieldDescriptor> getCmpFields() {
		return cmpFields;
	}
	
	/**
	 * Retrieves the default set of event types masked, for sbb entities attaching to acis
	 * @return the defaultEventMask
	 */
	public Set<EventTypeID> getDefaultEventMask() {
		return defaultEventMask == null ? null : new HashSet<EventTypeID>(defaultEventMask);
	}

	/* (non-Javadoc)
	 * @see org.mobicents.slee.core.component.sbb.SbbDescriptor#getSbbUsageParametersInterface()
	 */
	public UsageParametersInterfaceDescriptor getSbbUsageParametersInterface() {
		return sbbUsageParametersInterface;
	}
}
