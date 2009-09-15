package org.mobicents.slee.container.component.deployment.jaxb.descriptors;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.DeploymentException;
import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MEnvEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.MSecurityPermissions;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MEjbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MLibraryRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MProfileSpecRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.common.references.MSbbRef;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MActivityContextAttributeAlias;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MEventEntry;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetChildRelationMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MGetProfileCMPMethod;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MResourceAdaptorTypeBinding;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbb;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbAbstractClass;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbActivityContextInterface;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbCMPField;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbClasses;
import org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb.MSbbLocalInterface;

/**
 * Start time:16:54:43 2009-01-19<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 * @author <a href="mailto:emmartins@gmail.com"> Eduardo Martins </a>
 */
public class SbbDescriptorImpl {

	private String description;

	private SbbID sbbID;
	private String sbbAlias;

	private List<MSbbRef> sbbRefs;
	private List<MProfileSpecRef> profileSpecRefs;

	private MSbbClasses sbbClasses;

	// might be bad, we ommit sbb-classes/description, phew
	private ProfileSpecificationID addressProfileSpecRef;

	/**
	 * mapping between event type id to event entries in descriptor
	 */
	private Map<EventTypeID, MEventEntry> eventEntries;

	/**
	 * mapping between event name and event type id in descriptor
	 */
	private Map<String, EventTypeID> eventTypesPerName;
	
	private Map<String,MActivityContextAttributeAlias> activityContextAttributeAliases;
	private List<MEnvEntry> envEntries;
	private List<MResourceAdaptorTypeBinding> resourceAdaptorTypeBindings;

	// 1.1 stuff, profile specs refs have alias element, so we need another.
	private List<MLibraryRef> libraryRefs;
	private List<MEjbRef> ejbRefs;

	private MSecurityPermissions securityPermisions;

	private boolean isSlee11;

	private Set<ComponentID> dependenciesSet = new HashSet<ComponentID>();

	/**
	 * references sbb abstract class map of get profile cmp methods
	 */
	private Map<String, MGetProfileCMPMethod> getProfileCMPMethods;

	/**
	 * references sbb abstract class map of get child relation methods
	 */
	private Map<String, MGetChildRelationMethod> getChildRelationMethods;

	/**
	 * cmp fields mapped by field name and with sbb alias dereferenced (for sbb local object cmps)
	 */
    private Map<String,MSbbCMPField> cmpFields;

    /**
     * this is the default set of masked event types, used when an sbb entity related with this sbb attaches to an aci
     */
    private Set<EventTypeID> defaultEventMask;
    
	public SbbDescriptorImpl(MSbb sbb,
			MSecurityPermissions sbbJarSecurityPermissions, boolean isSlee11)
			throws DeploymentException {
		try {
			this.description = sbb.getDescription();
			this.sbbID = new SbbID(sbb.getSbbName(), sbb.getSbbVendor(), sbb
					.getSbbVersion());

			this.sbbAlias = sbb.getSbbAlias();

			this.libraryRefs = sbb.getLibraryRef();
			this.ejbRefs = sbb.getEjbRef();
			this.profileSpecRefs = sbb.getProfileSpecRef();

			String addressProfileSpecAliasRef = sbb
					.getAddressProfileSpecAliasRef();
			if (addressProfileSpecAliasRef != null) {
				if (this.profileSpecRefs != null) {
					for (MProfileSpecRef mProfileSpecRef : this.profileSpecRefs) {
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

			this.sbbClasses = sbb.getSbbClasses();

			// build event type map and sets optimized for runtime
			eventEntries = new HashMap<EventTypeID, MEventEntry>(sbb.getEvent()
					.size() * 2 + 1);
			eventTypesPerName = new HashMap<String,EventTypeID>(sbb.getEvent()
					.size() * 2 + 1);
			for (MEventEntry mEventEntry : sbb.getEvent()) {			
				EventTypeID eventTypeID = mEventEntry.getEventReference().getComponentID();
				if (eventEntries.containsKey(eventTypeID)) {
					throw new DeploymentException("the sbb descriptor contains multiple event handler methods for "+eventTypeID);
				}
				eventEntries.put(eventTypeID, mEventEntry);
				eventTypesPerName.put(mEventEntry.getEventName(), eventTypeID);
			}

			activityContextAttributeAliases = new HashMap<String, MActivityContextAttributeAlias>();
			for (MActivityContextAttributeAlias alias : sbb.getActivityContextAttributeAlias()) {
				for (String sbbActivityContextAttributeName : alias.getSbbActivityContextAttributeName()) {
					activityContextAttributeAliases.put(sbbActivityContextAttributeName, alias);
	        	}
			}
			
			this.envEntries = sbb.getEnvEntry();

			this.resourceAdaptorTypeBindings = sbb
					.getResourceAdaptorTypeBinding();

			this.securityPermisions = sbbJarSecurityPermissions;

			this.isSlee11 = isSlee11;

			// lets prepare child relation and profile cmp methods with aliases
			// dereferenced, for optimized runtime performance
			this.getChildRelationMethods = sbbClasses.getSbbAbstractClass()
					.getChildRelationMethods();
			for (MGetChildRelationMethod mGetChildRelationMethod : getChildRelationMethods
					.values()) {
				for (MSbbRef mSbbRef : sbbRefs) {
					if (mGetChildRelationMethod.getSbbAliasRef().equals(
							mSbbRef.getSbbAlias())) {
						mGetChildRelationMethod.setSbbID(mSbbRef
								.getComponentID());
						break;
					}
				}
			}
			this.getProfileCMPMethods = sbbClasses.getSbbAbstractClass()
					.getProfileCMPMethods();
			for (MGetProfileCMPMethod mGetProfileCMPMethod : getProfileCMPMethods
					.values()) {
				for (MProfileSpecRef mProfileSpecRef : profileSpecRefs) {
					if (mGetProfileCMPMethod.getProfileSpecAliasRef().equals(
							mProfileSpecRef.getProfileSpecAlias())) {
						mGetProfileCMPMethod
								.setProfileSpecificationID(mProfileSpecRef
										.getComponentID());
						break;
					}
				}
			}

			// build cmp field map
			this.cmpFields = new HashMap<String,MSbbCMPField>();
		    for(MSbbCMPField field : sbbClasses.getSbbAbstractClass().getCmpFields()) {
		      this.cmpFields.put(field.getCmpFieldName(),field);
		      if (field.getSbbAliasRef() != null) {
		    	  // dereference the alias
		    	  for (MSbbRef ref : sbbRefs) {
		    		  if (ref.getSbbAlias().equals(field.getSbbAliasRef())) {
		    			  field.setSbbRef(ref.getComponentID());
		    			  break;
		    		  }
		    	  }
		      }
		    }
		    
		    // build default event mask for this sbb entities
		 	Collection<MEventEntry> mEventEntries = getEventEntries().values();
			HashSet<EventTypeID> maskedEvents = null;
			if (mEventEntries != null) {
				maskedEvents = new HashSet<EventTypeID>();
				for (MEventEntry mEventEntry : mEventEntries) {
					if (mEventEntry.isMaskOnAttach()) {
						maskedEvents.add(mEventEntry.getEventReference()
								.getComponentID());
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

		for (MSbbRef sbbRef : sbbRefs) {
			this.dependenciesSet.add(sbbRef.getComponentID());
		}

		for (MProfileSpecRef profileSpecRef : profileSpecRefs) {
			this.dependenciesSet.add(profileSpecRef.getComponentID());
		}

		for (MLibraryRef libraryRef : libraryRefs) {
			this.dependenciesSet.add(libraryRef.getComponentID());
		}

		for (MResourceAdaptorTypeBinding binding : resourceAdaptorTypeBindings) {
			this.dependenciesSet.add(binding.getResourceAdaptorTypeRef());
		}

		// FIXME: EJB's do not have component ID... what gives?
		// for(MEjbRef ejbRef : ejbRefs)
		// {
		// this.dependenciesSet.add( ejbRef.getComponentID() );
		// }
	}

	public String getDescription() {
		return description;
	}

	public SbbID getSbbID() {
		return sbbID;
	}

	public String getSbbAlias() {
		return sbbAlias;
	}

	public List<MSbbRef> getSbbRefs() {
		return sbbRefs;
	}

	public List<MProfileSpecRef> getProfileSpecRefs() {
		return profileSpecRefs;
	}

	public MSbbClasses getSbbClasses() {
		return sbbClasses;
	}

	public ProfileSpecificationID getAddressProfileSpecRef() {
		return addressProfileSpecRef;
	}

	/**
	 * Retrieves the map between event type ids and event entries, in descriptors
	 * 
	 * @return
	 */
	public Map<EventTypeID, MEventEntry> getEventEntries() {
		return eventEntries;
	}

	/**
	 * Retrieves the event type id mapped to the specified event name
	 * @param eventName
	 * @return
	 */
	public EventTypeID getEventTypeID(String eventName) {
		return eventTypesPerName.get(eventName);
	}
	
	public Map<String,MActivityContextAttributeAlias> getActivityContextAttributeAliases() {
		return activityContextAttributeAliases;
	}

	public List<MEnvEntry> getEnvEntries() {
		return envEntries;
	}

	public List<MResourceAdaptorTypeBinding> getResourceAdaptorTypeBindings() {
		return resourceAdaptorTypeBindings;
	}

	public List<MLibraryRef> getLibraryRefs() {
		return libraryRefs;
	}

	public List<MEjbRef> getEjbRefs() {
		return ejbRefs;
	}

	public MSecurityPermissions getSecurityPermissions() {
		return securityPermisions;
	}

	public Set<ComponentID> getDependenciesSet() {
		return this.dependenciesSet;
	}

	public boolean isSlee11() {
		return isSlee11;
	}

	// Convenience methods
	public MSbbAbstractClass getSbbAbstractClass() {
		return this.sbbClasses.getSbbAbstractClass();
	}

	public MSbbLocalInterface getSbbLocalInterface() {
		return this.sbbClasses.getSbbLocalInterface();
	}

	public MSbbActivityContextInterface getSbbActivityContextInterface() {
		return this.sbbClasses.getSbbActivityContextInterface();
	}

	/**
	 * @see MSbbAbstractClass#getChildRelationMethods()
	 * @return
	 */
	public Map<String, MGetChildRelationMethod> getGetChildRelationMethods() {
		return getChildRelationMethods;
	}

	/**
	 * @see MSbbAbstractClass#getGetProfileCMPMethods()
	 * @return
	 */
	public Map<String, MGetProfileCMPMethod> getGetProfileCMPMethods() {
		return getProfileCMPMethods;
	}

	/**
	 * Retrieves cmp fields mapped by field name and with sbb alias dereferenced (for sbb local object cmps) 
	 * @return
	 */
	public Map<String, MSbbCMPField> getCmpFields() {
		return cmpFields;
	}
	
	/**
	 * Retrieves the default set of event types masked, for sbb entities attaching to acis
	 * @return the defaultEventMask
	 */
	public Set<EventTypeID> getDefaultEventMask() {
		return defaultEventMask;
	}
}
