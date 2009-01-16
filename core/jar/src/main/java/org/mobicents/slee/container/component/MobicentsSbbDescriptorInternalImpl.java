package org.mobicents.slee.container.component;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.LinkRef;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.slee.Address;
import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.InitialEventSelector;
import javax.slee.SbbID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.LibraryID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.commons.pool.ObjectPool;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.InitialEventSelectorImpl;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ComponentClassLoadingManagement;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.profile.SleeProfileManager;
import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.ResourceAdaptorEntity;
import org.mobicents.slee.resource.ResourceAdaptorType;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;
import org.mobicents.slee.runtime.facilities.TimerFacilityImpl;
import org.mobicents.slee.runtime.sbb.SbbConcrete;
import org.mobicents.slee.runtime.sbb.SbbObject;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityContextInterfaceFactoryImpl;
import org.mobicents.slee.runtime.serviceactivity.ServiceActivityFactoryImpl;

public class MobicentsSbbDescriptorInternalImpl implements
		MobicentsSbbDescriptorInternal, MobicentsSbbDescriptor {

	private static final long serialVersionUID = 9153818261657909355L;
	
	// The set of initial events
	private Set initialEvents;

	// the set of received events.
	private Set receivedEvents;

	// Allows you to access an event name given its
	// type name.
	private Map typeNameToNameMap;

	// All events known to this descriptor are stored in this map
	private Map typeNameToTypeMap;

	// The re-entrant flag
	private boolean reentrant;

	// SBB abstract class fully qualified path name
	private String abstractClassName;

	// The concrete SBB class
	private transient Class concreteSbbClass;

	// SBB source.
	// TODO -- what is this?
	private String source;

	// Address profile tables that this SBB accesses.
	private HashSet profiles;

	private HashSet childSbbs;

	private CMPField[] cmpFields;

	private HashSet firedEvents;

	private String sbbAlias;

	/*
	 * The resource adapter types that this cares about
	 */

	private HashSet resourceAdaptorTypeBindings;

	private HashSet resourceAdapterEntityLinks;

	private HashSet resourceAdapterTypeIDs;

	private HashMap eventTypes;

	private HashSet receivedEventTypes;

	private HashSet initialEventTypes;

	private HashSet firedEventTypes;

	private HashSet sbbEventEntries;

	private SbbIDImpl sbbID;

	private transient HashMap<String, GetChildRelationMethod> childRelationMethods;

	private transient ProfileCMPMethod[] profileCMPMethods;

	private DeployableUnitIDImpl deployableUnitID;

	private HashMap resourceAdaptorEntityBindings;

	/*
	 * EJB Ref elements These elements are optional. Each ejb-ref element binds
	 * a reference to an Enterprise JavaBean home into the JNDI component
	 * environment of the SBB. This allows the SBB to invoke Enterprise
	 * JavaBeans. Section 20.3 in the Enterprise JavaBeans 2.0, Final Release
	 * specification describes this element and how components get access to the
	 * referenced Enterprise JavaBean home.
	 */

	private HashSet ejbRefs;

	/*
	 * Environment Entries Zero or more env-entry elements. These elements are
	 * optional. Each env-entry element binds an environment entry into the JNDI
	 * component environment of the SBB (see Section 6.13).
	 */
	private HashSet envEntries;

	/*
	 * Zero or more profile-spec-ref elements. Each Profile Specification used
	 * by the SBB must be identified by a profile-spec-ref element.
	 */

	private HashMap profileSpecReferences;

	// The address profile spec for the SBB.
	private transient ProfileSpecificationID addressProfile;

	/*
	 * Zero or more sbb-ref elements. Each SBB referenced by the SBB of the
	 * enclosing sbb element must be identified by an sbbref element.
	 */

	private HashSet sbbRef;

	// Ptr to sbb local interface class.
	private transient Class sbbLocalInterfaceClass;

	private transient Class sbbLocalInterfaceConcreteClass;

	// Ptr to the activity context local interface.
	private String activityContextInterfaceClassName;

	private transient Class activityContextInterface;

	// Ptr to activity context interface concrete class.
	private transient Class activityContextInterfaceConcreteClass;

	/*
	 * Zero or more activity-context-attribute-alias elements. These elements
	 * are optional. These elements and Activity Context attribute aliasing are
	 * described in detail in Section 7.8.
	 */

	private HashMap activityContextInterfaceAttributeAliases;

	/*
	 * An sbb-usage-parameter-interface element. This element is optional. If
	 * the SBB developer defines an SBB Usage Parameters inter- face, this
	 * element identifies the SBB Usage Parameters interface. This element
	 * identifies the class name of the SBB Usage Parameters interfac
	 */

	private String usageParameterInterface;

	private String usageParametersInterfaceDescription;

	private transient static Logger logger;

	private String description;

	private String sbbLocalInterfaceClassName;

	/*
	 * This is the generated concrete class for the usage parameter. Its just a
	 * handy place to put this.
	 */
	private transient Class usageParameterClass;

	static {
		logger = Logger.getLogger(MobicentsSbbDescriptor.class);
	}

	/**
	 * Default constructor. The fields of this will be filled in with the stuff
	 * that is parsed from the deployment descriptor.
	 * 
	 */
	public MobicentsSbbDescriptorInternalImpl() {
		this.resourceAdaptorEntityBindings = new HashMap();
		this.childSbbs = new HashSet();
		this.envEntries = new HashSet();
		this.ejbRefs = new HashSet();
		this.profiles = new HashSet();
		this.initialEvents = new HashSet();
		this.receivedEvents = new HashSet();
		this.typeNameToNameMap = new HashMap();
		this.firedEvents = new HashSet();
		this.resourceAdaptorTypeBindings = new HashSet();
		this.typeNameToTypeMap = new HashMap();
		this.eventTypes = new HashMap();
		this.firedEventTypes = new HashSet();
		this.receivedEventTypes = new HashSet();
		this.initialEventTypes = new HashSet();
		this.reentrant = false;
		this.sbbEventEntries = new HashSet();
		this.sbbRef = new HashSet();
		this.profileSpecReferences = new HashMap();
		this.activityContextInterfaceAttributeAliases = new HashMap();
		this.resourceAdapterEntityLinks = new HashSet();
		this.resourceAdapterTypeIDs = new HashSet();

	}

	/**
	 * Add a type name to type map element.
	 * 
	 * @param typeName
	 *            is the local name by which the sbb entry refers to the event.
	 *            this is used to generate the onXXXEvent method name.
	 * 
	 * @param eventType
	 *            is the Sbb event entry to which the name maps.
	 * 
	 */

	public void addEventMap(String typeName, SbbEventEntry eventEntry) {
		this.typeNameToTypeMap.put(typeName, eventEntry);
	}

	/**
	 * Re-entrant flag
	 * 
	 * @param flag --
	 *            the re-entrant flag read from the deplpyment descriptor
	 */
	public void setReentrant(boolean flag) {
		this.reentrant = flag;
	}

	/**
	 * Get the reEntrant flag
	 */
	public boolean isReentrant() {
		return this.reentrant;
	}

	/**
	 * @return Returns the initialEvents.
	 */
	public Set getInitialEventTypes() {

		return initialEventTypes;
	}

	public void addRef(SbbRef ref) {
		sbbRef.add(ref);
	}

	/**
	 * Get the event name given the type name of the event. Note that the SLEE
	 * routes events by name but the resource adapter produces typed events.
	 * This is odd.
	 * 
	 * @param typeName -
	 *            typeName for which you want to get the event name.
	 * 
	 * @return the event name.
	 */
	public String getEventName(EventTypeID typeName) {
		return (String) this.typeNameToNameMap.get(typeName);
	}

	/**
	 * Get the event type given the typeName
	 * 
	 * @return the event type
	 */
	public SbbEventEntry getEventType(String typeName) {
		logger.debug("typeName = " + typeName);
		return (SbbEventEntry) this.typeNameToTypeMap.get(typeName);
	}

	/**
	 * get the concreteSbb for the sbb. this is a pointer to the stuff that you
	 * generate. This is accessed to create an instance of the SBB.
	 * 
	 * @return Returns the concreteSbb.
	 */
	public Class getConcreteSbbClass() {
		return concreteSbbClass;
	}

	/**
	 * Class of concrete SBB to set. you call this method to set the concrete
	 * SBB after you generate it from the abstract SBB.
	 * 
	 * @param concreteSbb --
	 *            the concreteSbb Class to set.
	 * 
	 */

	public void setConcreteSbb(Class concreteSbbClass) {
		this.concreteSbbClass = concreteSbbClass;
	}

	/**
	 * Compute a convergence name for the Sbb for the given Slee event.
	 * Convergence names are used to instantiate the Sbb. I really ought to move
	 * this to SleeContainer.java
	 * 
	 * @param sleeEvent -
	 *            slee event for the convergence name computation
	 * @return the convergence name or null if this is not an initial event for
	 *         this service
	 */
	public String computeConvergenceName(DeferredEvent sleeEvent,
			ServiceComponent svc) throws Exception {
		
		
		final SbbEventEntry entry = (SbbEventEntry) eventTypes.get(sleeEvent.getEventTypeId());
		
		InitialEventSelectorImpl selector = new InitialEventSelectorImpl(
				sleeEvent.getEventTypeId(), sleeEvent.getEvent(), sleeEvent.getActivityContextHandle(), entry
						.getInitialEventSelectors(), entry
						.getInitialEventSelectorMethod(), sleeEvent
						.getAddress());

		/*
		 * An initial-event-selector-method-name element. This element is
		 * optional and is meaningful only if initial-event is true. It
		 * identifies an in itial event selector method. The SLEE invokes this
		 * optional method to d etermine if an event of the specified event type
		 * is an initial event if the SBB is a root SBB of a Service (see
		 * Section 8.5.4). Note that this method is not static. You can either
		 * used a pooled instance of the object or create a new instance of the
		 * object to run the specified method.
		 */
		if (selector.isSelectMethod()) {
			// According to Section 8.5.4, page 115, some fields should be set
			// before calling the selector method
			// selector.setEvent(sleeEvent.getEventObject());
			// selector.setEventName(); //TODO: not sure what value to put here
			// selector.setActivity(sleeEvent.getActivityContext().getActivity());//TODO:
			// see how to get the activity now
			selector.setAddress(sleeEvent.getAddress());
			selector.setCustomName(null);
			selector.setInitialEvent(true);

			ObjectPool pool = SleeContainer.lookupFromJndi().getSbbManagement()
					.getSbbPoolManagement().getObjectPool(
							svc.getRootSbbComponent().getID());
			SbbObject sbbObject = (SbbObject) pool.borrowObject();
			sbbObject.setServiceID(svc.getServiceID());
			SbbConcrete concreteSbb = (SbbConcrete) sbbObject.getSbbConcrete();

			Class[] argtypes = new Class[] { InitialEventSelector.class };
			Method m = this.getConcreteSbbClass().getMethod(
					selector.getSelectMethodName(), argtypes);
			Object[] args = new Object[] { selector };

			ClassLoader oldCl = Thread.currentThread().getContextClassLoader();
			try {
				Thread.currentThread().setContextClassLoader(
						this.getClassLoader());
				selector = (InitialEventSelectorImpl) m.invoke(concreteSbb,
						args);
				if (selector == null) {
					logger
							.debug("Sbb returned null. So its not interested in this event");
					return null;
				}
				if (!selector.isInitialEvent()) {
					logger
							.debug("Sbb has determined it will not attend to this event");
					return null;
				}

			} finally {
				Thread.currentThread().setContextClassLoader(oldCl);
				sbbObject.setServiceID(null);
				pool.returnObject(sbbObject);
			}
		}

		String convergenceName = null;

		StringBuffer buff = new StringBuffer();

		if (selector.isActivityContextSelected()) {
			buff.append(sleeEvent.getActivityContextHandle().toString());
		} else
			buff.append("null");

		// buff.append(e.getActivityContext().getActivityContextID());
		// TODO the ProfileTle select varile for now is null

		buff.append("null");

		if (selector.isAddressSelected()) {
			Address address = selector.getAddress();

			if (address == null)
				buff.append("null");
			else
				buff.append(address.toString());
		} else
			buff.append("null");

		// If event type is selected append it to te convergence name.
		if (selector.isEventTypeSelected()) {
			buff.append(selector.getEventTypeID());
		} else
			buff.append("null");

		/*
		 * Event. The value of this variable (if selected) is unique for each
		 * event fired, e.g. each invocation of an SBB fire event method or each
		 * firing of an event by a resource adaptor (using SLEE vendor specific
		 * fire event methods). It is unique regardless of whether the same pair
		 * of event object and Activity Context object (in the case of SBB fired
		 * event, or Activity object in case of resource adaptor fired event)
		 * are passed to the fire event method. There are two unique events
		 * fired in the following scenarios:
		 * 
		 * o An SBB invoking the same fire event method twice. From the SLEEs
		 * perspective, the two fire method invocations fire two unique events
		 * even if the Activity Context object and event object passed to the
		 * fire event method are the same.
		 * 
		 * o An SBB firing an event in its event handler method. The event fired
		 * through the fire event method is a different event even if the same
		 * Activity Context object and event object passed to the event handler
		 * method is passed to the fire event method.
		 * 
		 * o A resource adaptor entity invoking one or more SLEE provided
		 * methods for firing events multiple times. From the SLEE???s
		 * perspective, these invocations fire multiple unique events even if
		 * the Activity object and event object passed are the same.
		 */

		if (selector.isEventSelected()) {
			buff.append(sleeEvent.hashCode()); // TODO: use a more unique value
			// than the hash code
		} else
			buff.append("null");
		/*
		 * The address attribute of the InitialEventSelector object provides the
		 * default address. The value of this attribute may be null if there is
		 * no default address. The value of this attribute determines the value
		 * of the address variable in the convergence name if the address
		 * variable is selected and is also used to look up Address Profiles in
		 * the Address Profile Table of the Service if the Address Profile
		 * variable is selected.
		 * 
		 * o If the address attribute is null when the initial event selector
		 * method returns, then the address convergence name variable is not
		 * selected, i.e. same as setting AddressSelected attribute to false.
		 * 
		 * o If the AddressProfile variable is set to true and the address is
		 * not null but does not locate any Address Profile in the Address
		 * Profile Table of the Service, then no convergence name is created,
		 * i.e. same as setting PossibleInitialEvent to false.
		 */
		if (selector.isAddressProfileSelected()) {
			ProfileSpecificationID addressProfileId = this
					.getAddressProfileSpecification();

			if (selector.getAddress() == null) {
				buff.append("null");
			} else {
				SleeContainer sleeContainer = SleeContainer.lookupFromJndi();
				ProfileSpecificationDescriptorImpl profileSpecDescriptor = (ProfileSpecificationDescriptorImpl) sleeContainer
						.getComponentManagement().getComponentDescriptor(
								addressProfileId);
				if (profileSpecDescriptor == null) {
					throw new Exception("Could not find address profile ! "
							+ addressProfileId);
				}
				SleeProfileManager sleeProfileManager = sleeContainer.getSleeProfileManager();
				String addressProfileTable = sleeContainer
						.getServiceManagement().getServiceComponent(
								svc.getServiceID()).getServiceDescriptor()
						.getAddressProfileTable();
				// Cannot find an address profile table spec. ( is this the same
				// as
				// the second condtion above?
				if (logger.isDebugEnabled()) {
					logger
							.debug("addressProfileTable = "
									+ addressProfileTable);
				}
				if (addressProfileTable == null) {
					throw new Exception(
							"null address profile table in service !");
				}
				logger.debug("indexes = "
						+ profileSpecDescriptor.getProfileIndexes());

				String profileName = profileSpecDescriptor.getName();
				if (logger.isDebugEnabled()) {
					logger.debug("profileName = " + profileName
							+ " addressString = "
							+ selector.getAddress().getAddressString());
				}

				Collection profileNames = sleeProfileManager
						.getProfilesByIndexedAttribute(addressProfileTable,
								"addresses", selector.getAddress(), true);
				if (profileNames == null || profileNames.isEmpty())
					throw new Exception("Could not find the specified profile");
				// Check -- JEAN - is this how you retrieve an address profile?

				buff.append(profileNames.iterator().next().toString());

			}

		} else
			buff.append("null");

		String customName = selector.getCustomName();

		buff.append(customName);

		if (logger.isDebugEnabled()) {
			logger.debug("computed convergence name = " + buff.toString());
			logger.debug("selector = " + selector);
		}
		return buff.toString();
	}

	/**
	 * get the received events set.
	 * 
	 * @return Returns the receivedEvents.
	 */
	public Set getReceivedEvents() {
		return receivedEventTypes;
	}

	/**
	 * get the fired event set.
	 * 
	 * @return firedEvent set
	 */
	public Set getFiredEvents() {
		return this.firedEventTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.SbbDescriptor#getSbbs()
	 */
	public SbbID[] getSbbs() {
		// get the Sbbs that are referenced by this sbb.
		HashSet retval = new HashSet();
		retval.addAll(this.childSbbs);
		// go through the CMP fields and add each CMP field
		for (int i = 0; cmpFields != null && i < cmpFields.length; i++) {
			Object f = cmpFields[i];
			if (f instanceof SbbID)
				retval.add(f);
		}
		SbbID[] sbbIDs = new SbbID[retval.size()];
		retval.toArray(sbbIDs);
		return sbbIDs;
	}

	public void setChildSbbComponentID(SbbID sbbid) {
		this.childSbbs.add(sbbid);
	}

	/**
	 * Add an event.
	 * 
	 * @param event
	 *            the event to add.
	 * @throws Exception
	 *             if you try to add an unknown event type. Event types are
	 *             defined in event.jar
	 */
	public void addEvent(SbbEventEntry eventEntry) throws Exception {
		logger.debug("ADD EVENT INVOKED FOR EVENT " + this.getID()
				+ " Event Name " + eventEntry.getEventName() + " IS INITIAL "
				+ eventEntry.isInitial());
		logger.debug("ADD EVENT ADDING " + eventEntry);

		this.sbbEventEntries.add(eventEntry);
		this.addEventMap(eventEntry.getEventName(), eventEntry);
	}

	/**
	 * Add an event entry to the deployment descriptor. This is necessary
	 * because events are specified in a different xml descriptor file than the
	 * sbb deployment descriptor.
	 * 
	 * @param eventTypeId --
	 *            event type id (hashed by the container)
	 * 
	 * @param eventEntry --
	 *            SbbEventEentry entry specified by the deployment descriptor.
	 */

	public void addEventEntry(EventTypeID eventTypeId, SbbEventEntry eventEntry) {

		logger.debug("addEventEntry: sbbId " + this.getID() + " eventTypeID "
				+ eventTypeId + "eventEntry " + eventEntry);

		this.typeNameToNameMap.put(eventTypeId, eventEntry.getEventName());
		// this.addEventMap(eventEntry.getEventName(),eventEntry);
		this.eventTypes.put(eventTypeId, eventEntry);
		if (eventEntry.isFired()) {
			firedEvents.add(eventTypeId);
			this.firedEventTypes.add(eventTypeId);

		}
		if (eventEntry.isReceived()) {
			receivedEvents.add(eventTypeId);
			this.receivedEventTypes.add(eventTypeId);
		}
		if (eventEntry.isInitial()) {
			initialEvents.add(eventTypeId);
			this.initialEventTypes.add(eventTypeId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.SbbDescriptor#getProfileSpecifications()
	 */
	public ProfileSpecificationID[] getProfileSpecifications() {

		ProfileSpecificationID[] retval = new ProfileSpecificationID[this.profiles
				.size()];
		profiles.toArray(retval);
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.SbbDescriptor#getAddressProfileSpecification()
	 */
	public ProfileSpecificationID getAddressProfileSpecification() {
		return this.addressProfile;
	}

	/**
	 * Add a resource adapter type.
	 * 
	 * @param resourceAdapterType --
	 *            RA type to add
	 */
	public void addResourceAdapterType(ResourceAdaptorTypeID resourceAdapterType) {
		this.resourceAdapterTypeIDs.add(resourceAdapterType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.SbbDescriptor#getResourceAdaptorTypes()
	 */
	public ResourceAdaptorTypeID[] getResourceAdaptorTypes() {
		ResourceAdaptorTypeID[] raTypeIDs = new ResourceAdaptorTypeID[this.resourceAdapterTypeIDs
				.size()];
		return (ResourceAdaptorTypeID[]) this.resourceAdapterTypeIDs
				.toArray(raTypeIDs);
	}

	public void setResourceAdapterTypeBindings(HashSet resourceAdapterTypes) {
		this.resourceAdaptorTypeBindings = resourceAdapterTypes;

	}

	public Iterator getResourceAdapterTypeBindings() {
		return this.resourceAdaptorTypeBindings.iterator();
	}

	public void addResourceAdapterEntityLink(String entityLink) {
		this.resourceAdapterEntityLinks.add(entityLink);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.SbbDescriptor#getResourceAdaptorEntityLinks()
	 */
	public String[] getResourceAdaptorEntityLinks() {

		return (String[]) this.resourceAdapterEntityLinks
				.toArray(new String[this.resourceAdapterEntityLinks.size()]);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
	 */
	public DeployableUnitID getDeployableUnit() {
		return this.deployableUnitID;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getSource()
	 */
	public String getSource() {
		return this.source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getID()
	 */
	public ComponentID getID() {

		return this.sbbID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getName()
	 */
	public String getName() {
		return this.sbbID.getComponentKey().getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getVendor()
	 */
	public String getVendor() {
		return this.sbbID.getComponentKey().getVendor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.slee.management.ComponentDescriptor#getVersion()
	 */
	public String getVersion() {

		return this.sbbID.getComponentKey().getVersion();
	}

	/**
	 * set the component key. The SLEE container registers the component using
	 * this key.
	 */
	public void setComponentKey(ComponentKey componentKey) throws Exception {
		SbbIDImpl sbbId = new SbbIDImpl(componentKey);

		this.sbbID = sbbId;
	}

	/**
	 * EMIL -- this is where you call your parser.
	 */

	public void parseSbb() {

	}

	/**
	 * Set the abstract class name for the sbb.
	 * 
	 * @param className
	 */
	public void setSbbAbstractClassName(String className) {
		// Emil: RANGA changing this to a string so that parsing won't fail
		// during deployment (i.e. when the class is not yet in the classpath)
		this.abstractClassName = className;
		// FIXME doesn't work even if the class implements Sbb, test done in the
		// verifier anyway
		/**
		 * if (! abstractClass.isInstance(Sbb.class) ) { throw new
		 * ClassCastException (className+" Bad sbb class"); }
		 */
	}

	/**
	 * Retrieve the sbb abstract Class name described by the descriptor
	 * 
	 * @return the sbb abstract Class name
	 */
	public String getSbbAbstractClassName() {
		return abstractClassName;
	}

	/**
	 * Set the childRelationMethods field.
	 * 
	 * @param childRelationMethods --
	 *            an array of GetChildRelationMethod[] parsed from the
	 *            deployment descriptor.
	 * 
	 */
	public void setChildRelationMethods(
			GetChildRelationMethod[] childRelationMethods) {
		this.childRelationMethods = new HashMap<String, GetChildRelationMethod>(
				childRelationMethods.length * 2 + 1);
		for (GetChildRelationMethod getChildRelationMethod : childRelationMethods) {
			this.childRelationMethods.put(getChildRelationMethod
					.getMethodName(), getChildRelationMethod);
		}
	}

	/**
	 * Get the childrelation acessor methods.
	 * 
	 * @return -- an array of childrelation accessors.
	 * 
	 */
	public GetChildRelationMethod[] getChildRelationMethods() {
		return this.childRelationMethods.values().toArray(
				new GetChildRelationMethod[this.childRelationMethods.size()]);
	}

	public GetChildRelationMethod getChildRelationMethod(String name) {
		return this.childRelationMethods.get(name);
	}

	/**
	 * Set the cmpfields array.
	 * 
	 * @param sbbCMPFields --
	 *            an array of SBBCMPFields to set.
	 * 
	 * 
	 */

	public void setCMPFields(CMPField[] cmpFields) {
		this.cmpFields = cmpFields;
	}

	/**
	 * Get the CMP Fields.
	 * 
	 * @return -- an array containing the cmp fields
	 */
	public CMPField[] getCMPFields() {
		return this.cmpFields;
	}

	/**
	 * set the profile cmp methods.
	 * 
	 * @param profileCMPMethods --
	 *            the profile cmp methods.
	 */
	public void setProfileCMPMethods(ProfileCMPMethod[] profileCmpMethods) {
		this.profileCMPMethods = profileCmpMethods;
	}

	/**
	 * get the profile cmp methods.
	 * 
	 * @return an array containing the profileCMP emthods.
	 */
	public ProfileCMPMethod[] getProfileCMPMethods() {
		return this.profileCMPMethods;
	}

	/**
	 * set the SBB local interface
	 * 
	 * @throws Exception
	 */
	public void setSbbLocalInterfaceClassName(String localInterfaceClassName)
			throws Exception {

		this.sbbLocalInterfaceClassName = localInterfaceClassName;

	}

	public Class getSbbLocalInterface() {
		return this.sbbLocalInterfaceClass;
	}

	public void setLocalInterfaceConcreteClass(Class clazz) {
		this.sbbLocalInterfaceConcreteClass = clazz;
	}

	public Class getLocalInterfaceConcreteClass() {
		return this.sbbLocalInterfaceConcreteClass;
	}

	/**
	 * Retrieve the activity context interface class
	 * 
	 * @return the activity context interface class
	 */
	public Class getActivityContextInterface() {

		return this.activityContextInterface;
	}

	/**
	 * Set the activity context interface class. This is an abstract class.
	 * 
	 * @throws Exception
	 */
	public void setActivityContextInterfaceClassName(
			String activityContextInterfaceClassName) throws Exception {

		this.activityContextInterfaceClassName = activityContextInterfaceClassName;

	}

	/**
	 * set the concrete class for the activityContextInterface.
	 * 
	 * @throws Exception
	 */
	public void setActivityContextInterfaceConcreteClass(Class aciConcreteClass) {
		this.activityContextInterfaceConcreteClass = aciConcreteClass;

	}

	public Class getActivityContextInterfaceConcreteClass() {
		return this.activityContextInterfaceConcreteClass;
	}

	/**
	 * setAddressProfileSpecAlias Key for the sbb address profile spec.
	 * 
	 * @throws Exception
	 *             if the profile spec is not found.
	 */
	public void setAddressProfileSpecAlias(String key) throws Exception {
		if (!this.profileSpecReferences.containsKey(key))
			throw new Exception("nothing known about the key " + key);
		else {
			this.addressProfile = (ProfileSpecificationID) this.profileSpecReferences
					.get(key);
		}
	}

	/**
	 * set the profile spec references for the sbb.
	 * 
	 * @param pspecRefs -
	 *            a hash map of profile spec references from the SBB parser.
	 * 
	 * @throws Exception
	 */
	public void setProfileSpecReferences(HashMap pspecRefs) {
		this.profileSpecReferences = pspecRefs;
		for (Iterator it = this.profileSpecReferences.values().iterator(); it
				.hasNext();) {
			ProfileSpecificationIDImpl ck = (ProfileSpecificationIDImpl) it
					.next();
			this.profiles.add(ck);
		}
	}

	/**
	 * Set the activity context interface attribute aliases.
	 * 
	 * @throws Exception
	 */
	public void setActivityContextInterfaceAttributeAliases(
			HashMap aciAttributeAliases) {
		this.activityContextInterfaceAttributeAliases = aciAttributeAliases;

	}

	/**
	 * set the usage parameters interface
	 * 
	 * @throws Exception
	 */
	public void setUsageParametersInterface(String usageParamsInterfaceClassName)
			throws Exception {
		this.usageParameterInterface = usageParamsInterfaceClassName;
	}

	/**
	 * Get the Usage parameters interface.
	 * 
	 * @return the usage parameters interface.
	 */
	public String getUsageParametersInterface() {
		return this.usageParameterInterface;

	}

	/**
	 * getEJBReferences
	 * 
	 * @return an array containing the EjbRefs for this Sbb.
	 */
	public HashSet getEjbRefs() {
		return this.ejbRefs;
	}

	public void setEjbRefs(HashSet ejbRefs) {

		this.ejbRefs = ejbRefs;

	}

	public HashSet getEnvEntries() {
		return this.envEntries;
	}

	public void setEnvironmentEntries(HashSet envEntries) {
		this.envEntries = envEntries;
	}

	/**
	 * Get the sbb event entry.
	 * 
	 * @return the set of Sbb event entries.
	 */

	public HashSet getSbbEventEntries() {
		return this.sbbEventEntries;
	}

	public EventTypeID[] getEventTypes() {
		EventTypeID[] myTypes = new EventTypeID[this.eventTypes.size()];
		this.eventTypes.keySet().toArray(myTypes); // ralf: changed to keySet
													// as
		// it contains the correct
		// types
		return myTypes;
	}

	/**
	 * @return Returns the sbbRef.
	 */
	public HashSet getSbbRef() {
		return sbbRef;
	}

	/**
	 * @param sbbRef
	 *            The sbbRef to set.
	 */
	public void setSbbRef(HashSet sbbRef) {
		this.sbbRef = sbbRef;
	}

	public HashMap getActivityContextInterfaceAttributeAliases() {

		return this.activityContextInterfaceAttributeAliases;
	}

	/**
	 * @return Returns the loader.
	 */
	public ClassLoader getClassLoader() {
		return ComponentClassLoadingManagement.INSTANCE.getClassLoader(getID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.DeployedComponent#setDeployableUnit(javax.slee.management.DeployableUnitID)
	 */
	public void setDeployableUnit(DeployableUnitID deployableUnitID) {
		this.deployableUnitID = (DeployableUnitIDImpl) deployableUnitID;

	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;

	}

	public String getDescription() {
		return this.description;
	}

	/*
	 * public String toString() { return new
	 * StringBuffer().append(sbbID.toString()).append( "\ndescription =
	 * ").append(this.description).toString(); }
	 */

	/**
	 * Call this after unjarring and unpacking everything and generating all the
	 * files you need.
	 * 
	 * @throws DeploymentException
	 */
	public void checkDeployment() throws DeploymentException {
		ClassLoader currentClassLoader = Thread.currentThread()
				.getContextClassLoader();
		Thread.currentThread().setContextClassLoader(getClassLoader());
		try {
			if (this.activityContextInterfaceClassName != null) {
				if (this.activityContextInterface == null)
					this.activityContextInterface = this.getClassLoader()
							.loadClass(activityContextInterfaceClassName);

				if (!this.activityContextInterface.isInterface())
					throw new ClassCastException("Must be an interface "
							+ activityContextInterfaceClassName);

				if (!findInterface(this.activityContextInterface,
						"javax.slee.ActivityContextInterface"))
					throw new Exception(
							this.activityContextInterfaceClassName
									+ " must extend javax.slee.ActivityContextInterface");
			}

			if (this.sbbLocalInterfaceClassName != null) {
				this.sbbLocalInterfaceClass = this.getClassLoader().loadClass(
						sbbLocalInterfaceClassName);

				if (!findInterface(this.sbbLocalInterfaceClass,
						"javax.slee.SbbLocalObject"))
					throw new Exception(this.sbbLocalInterfaceClassName
							+ " must extend javax.slee.SbbLocalObject");
			}

		} catch (Exception ex) {
			throw new DeploymentException(
					"Deployment was not consistent .. clean up ", ex);
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	public void postInstall() throws Exception {

	}

	public String getActivityContextInterfaceClassName() {
		return this.activityContextInterfaceClassName;
	}

	/**
	 * @param raTypeIDImpl
	 * @param raEntityLink
	 */
	public void addResourceAdaptorEntityBinding(
			ResourceAdaptorTypeIDImpl raTypeID,
			ResourceAdaptorEntityBinding raEntityLink) {
		HashSet rabindingSet = (HashSet) resourceAdaptorEntityBindings
				.get(raTypeID);
		if (rabindingSet == null) {
			rabindingSet = new HashSet();
			this.resourceAdaptorEntityBindings.put(raTypeID, rabindingSet);

		}

		rabindingSet.add(raEntityLink);

	}

	/**
	 * Get an entity binding for a given resource adaptor type
	 * 
	 */
	public Iterator getResourceAdaptorEntityBindings(
			ResourceAdaptorTypeIDImpl raTypeID) {
		HashSet rabindings = (HashSet) this.resourceAdaptorEntityBindings
				.get(raTypeID);
		if (rabindings == null) {
			return new HashSet().iterator();
		} else {
			return rabindings.iterator();
		}
	}

	/**
	 * @param usageParamClazz
	 */
	public void setUsageParameterClass(Class usageParamClazz) {
		this.usageParameterClass = usageParamClazz;

	}

	public Class getUsageParameterClass() {
		return usageParameterClass;
	}

	/**
	 * @return the path to the root where the Sbb was deployed.
	 */
	public String getDeploymentPath() {
		return ((DeployableUnitIDImpl) (this.getDeployableUnit()))
				.getDUDeployer().getTempClassDeploymentDir().getAbsolutePath();
	}

	/**
	 * 
	 * @return HashMap containing mapping between EventTypeID and coresponding
	 *         SbbEventEntry
	 */
	public HashMap getEventTypesMappings() {
		return new HashMap(eventTypes);
	}

	/**
	 * public Map getNamedUsageParameterTable() { return
	 * namedUsageParameterTable; }
	 * 
	 * public void putNamedUsageParameterSet(String name, Object newParams) {
	 * namedUsageParameterTable.put(name, newParams); }
	 * 
	 * 
	 * public String[] getAllUsageParameterNames() { Collection col =
	 * this.namedUsageParameterTable.keySet(); String[] retval = new
	 * String[col.size()]; col.toArray(retval); return retval; }
	 */

	public LibraryID[] getLibraries() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * Sets the default usage parameter set associated with the SBB
	 * 
	 * @param usageParm
	 * 
	 * public void setDefaultUsageParameterSet( InstalledUsageParameterSet
	 * usageParam) { defaultUsageParameterSet = usageParam; }
	 * 
	 * 
	 * 
	 * @return the default usage parameter set associated with the SBB
	 * 
	 * 
	 * public InstalledUsageParameterSet getDefaultUsageParameterSet() { return
	 * defaultUsageParameterSet; }
	 */

	public void setupSbbEnvironment() throws Exception {

		Context ctx = (Context) new InitialContext().lookup("java:comp");

		if (logger.isDebugEnabled()) {
			logger.debug("Setting up SBB env. Initial context is " + ctx);
		}

		Context envCtx = null;
		try {
			envCtx = ctx.createSubcontext("env");
		} catch (NameAlreadyBoundException ex) {
			envCtx = (Context) ctx.lookup("env");
		}

		Context sleeCtx = null;

		try {
			sleeCtx = envCtx.createSubcontext("slee");
		} catch (NameAlreadyBoundException ex) {
			sleeCtx = (Context) envCtx.lookup("slee");
		}

		// Do all the context binding stuff just once during init and
		// just do the linking here.

		Context newCtx;

		String containerName = "java:slee/container/Container";
		try {
			newCtx = sleeCtx.createSubcontext("container");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("container");
		}

		try {
			newCtx.bind("Container", new LinkRef(containerName));
		} catch (NameAlreadyBoundException ex) {

		}

		String nullAciFactory = "java:slee/nullactivity/nullactivitycontextinterfacefactory";
		String nullActivityFactory = "java:slee/nullactivity/nullactivityfactory";

		try {
			newCtx = sleeCtx.createSubcontext("nullactivity");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("nullactivity");
		}

		try {
			newCtx.bind("activitycontextinterfacefactory", new LinkRef(
					nullAciFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		try {
			newCtx.bind("factory", new LinkRef(nullActivityFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		String serviceActivityContextInterfaceFactory = "java:slee/serviceactivity/"
				+ ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME;
		String serviceActivityFactory = "java:slee/serviceactivity/"
				+ ServiceActivityFactoryImpl.JNDI_NAME;
		try {
			newCtx = sleeCtx.createSubcontext("serviceactivity");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("serviceactivity");

		}
		try {
			newCtx.bind(ServiceActivityContextInterfaceFactoryImpl.JNDI_NAME,
					new LinkRef(serviceActivityContextInterfaceFactory));
		} catch (NameAlreadyBoundException ex) {

		}
		try {
			newCtx.bind(ServiceActivityFactoryImpl.JNDI_NAME, new LinkRef(
					serviceActivityFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		String timer = "java:slee/facilities/" + TimerFacilityImpl.JNDI_NAME;
		String aciNaming = "java:slee/facilities/activitycontextnaming";

		try {
			newCtx = sleeCtx.createSubcontext("facilities");
		} catch (NameAlreadyBoundException ex) {

		} finally {
			newCtx = (Context) sleeCtx.lookup("facilities");
		}
		try {
			newCtx.bind("timer", new LinkRef(timer));
		} catch (NameAlreadyBoundException ex) {

		}

		try {
			newCtx.bind("activitycontextnaming", new LinkRef(aciNaming));
		} catch (NameAlreadyBoundException ex) {
		}

		String trace = "java:slee/facilities/trace";
		try {
			newCtx.bind("trace", new LinkRef(trace));
		} catch (NameAlreadyBoundException ex) {
		}

		String alarm = "java:slee/facilities/alarm";
		try {
			newCtx.bind("alarm", new LinkRef(alarm));
		} catch (NameAlreadyBoundException ex) {
		}

		String profile = "java:slee/facilities/profile";
		try {
			newCtx.bind("profile", new LinkRef(profile));
		} catch (NameAlreadyBoundException ex) {
		}
		String profilteTableAciFactory = "java:slee/facilities/profiletableactivitycontextinterfacefactory";
		try {
			newCtx.bind("profiletableactivitycontextinterfacefactory",
					new LinkRef(profilteTableAciFactory));
		} catch (NameAlreadyBoundException ex) {

		}

		// For each resource that the Sbb references, bind the implementing
		// object name to its comp/env

		if (logger.isDebugEnabled()) {
			logger.debug("Number of Resource Bindings:"
					+ getResourceAdapterTypeBindings());
		}
		for (Iterator it = getResourceAdapterTypeBindings(); it.hasNext();) {
			MobicentsResourceAdaptorTypeBinding binding = (MobicentsResourceAdaptorTypeBinding) it
					.next();

			ResourceAdaptorTypeIDImpl resourceAdaptorTypeID = (ResourceAdaptorTypeIDImpl) binding
					.getResourceAdapterTypeId();
			ResourceAdaptorType raType;
			if ((raType = SleeContainer.lookupFromJndi()
					.getResourceManagement().getResourceAdaptorType(
							resourceAdaptorTypeID)) == null) {
				throw new Exception(
						"Resource Adaptor type is not implemented: "
								+ resourceAdaptorTypeID);
			}

			for (Iterator it1 = getResourceAdaptorEntityBindings(resourceAdaptorTypeID); it1
					.hasNext();) {

				ResourceAdaptorEntityBinding ralink = (ResourceAdaptorEntityBinding) it1
						.next();
				String raObjectName = ralink.getResourceAdapterObjectName();
				String linkName = ralink.getResourceAdaptorEntityLink();
				/*
				 * The Deployment descriptor specifies Zero or more
				 * resource-adaptor-entity-binding elements. Each
				 * resource-adaptor-entity-binding element binds an object that
				 * implements the resource adaptor interface of the resource
				 * adaptor type into the JNDI comp onent environment of the SBB
				 * (see Section 6.13.3). Each resource- adaptorentity- binding
				 * element contains the following sub-elements: A description
				 * element. This is an optional informational element. A
				 * resource-adaptor-object?name element. This element specifies
				 * the location within the JNDI component environment to which
				 * the object that implements the resource adaptor interface
				 * will be bound. A resource-adaptor-entity-link element. This
				 * is an optional element. It identifies the resource adaptor
				 * entity that provides the object that should be bound into the
				 * JNDI component environment of the SBB. The identified
				 * resource adaptor entity must be an instance of a resource
				 * adaptor whose resource adaptor type is specified by the
				 * resourceadaptor- type-ref sub-element of the enclosing
				 * resource-adaptortype- binding element.
				 */
				ResourceManagement resourceManagement = SleeContainer
						.lookupFromJndi().getResourceManagement();
				ResourceAdaptorEntity raEntity = resourceManagement
						.getResourceAdaptorEntity(resourceManagement
								.getResourceAdaptorEntityName(linkName));
				if (raEntity == null)
					throw new Exception(
							"Could not find Resource adaptor Entity for Link Name: ["
									+ linkName + "] of RA Type ["
									+ raType.getResourceAdaptorTypeID() + "]");

				NameParser parser = ctx.getNameParser("");
				Name local = parser.parse(raObjectName);
				int tokenCount = local.size();

				Context subContext = envCtx;

				for (int i = 0; i < tokenCount - 1; i++) {
					String nextTok = local.get(i);
					try {
						subContext.lookup(nextTok);
					} catch (NameNotFoundException nfe) {
						subContext.createSubcontext(nextTok);
					} finally {
						subContext = (Context) subContext.lookup(nextTok);
					}
				}
				String lastTok = local.get(tokenCount - 1);
				// Bind the resource adaptor instance to where the Sbb expects
				// to find it.
				if (logger.isDebugEnabled()) {
					logger
							.debug("setupSbbEnvironment: Binding a JNDI reference to resource adaptor instance ["
									+ raEntity.getFactoryInterfaceJNDIName()
									+ "] to where the Sbb expects to find it ["
									+ lastTok + "]");
				}
				// subContext.bind(lastTok, raEntity.getResourceAdaptor());
				try {
					subContext.bind(lastTok, new LinkRef(raEntity
							.getFactoryInterfaceJNDIName()));
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"setupSbbEnvironment: Failed to bind JNDI reference ["
									+ lastTok
									+ "] to resource adaptor instance ["
									+ raEntity.getFactoryInterfaceJNDIName()
									+ "] due to NameAlreadyBoundException", e);
				}
			}

			String localFactoryName = binding
					.getActivityContextInterfaceFactoryName();
			if (localFactoryName != null) {
				String globalFactoryName = SleeContainer.lookupFromJndi()
						.getResourceManagement()
						.getActivityContextInterfaceFactories().get(
								binding.getResourceAdapterTypeId())
						.getJndiName();
				NameParser parser = ctx.getNameParser("");
				Name local = parser.parse(localFactoryName);
				int nameSize = local.size();
				Context tempCtx = envCtx;

				for (int a = 0; a < nameSize - 1; a++) {
					String temp = local.get(a);
					try {
						tempCtx.lookup(temp);
					} catch (NameNotFoundException ne) {
						tempCtx.createSubcontext(temp);

					} finally {
						tempCtx = (Context) tempCtx.lookup(temp);
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("ACI factory reference binding: "
							+ local.get(nameSize - 1) + " to "
							+ globalFactoryName);
				}
				String factoryRefName = local.get(nameSize - 1);
				try {
					tempCtx
							.bind(factoryRefName,
									new LinkRef(globalFactoryName));
				} catch (NameAlreadyBoundException e) {
					logger.warn(
							"setupSbbEnvironment: Failed to bind ACI factory JNDI reference ["
									+ factoryRefName
									+ "] to global factory name ["
									+ globalFactoryName
									+ "] due to NameAlreadyBoundException", e);
				}
			}

		}

		/*
		 * Bind the ejb-refs
		 */
		try {
			envCtx.createSubcontext("ejb");
		} catch (NameAlreadyBoundException ex) {
			envCtx.lookup("ejb");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Created ejb local context");
		}

		Iterator iter = getEjbRefs().iterator();
		while (iter.hasNext()) {
			EJBReference ejbRef = (EJBReference) iter.next();

			String jndiName = ejbRef.getJndiName();
			if (jndiName == null) {
				logger
						.warn("JNDI name not specified so defaulting to ejb-name");
				jndiName = ejbRef.getEjbRefName();
			}

			if (logger.isDebugEnabled()) {
				logger.debug("Binding ejb: " + ejbRef.getEjbRefName()
						+ " with link to " + jndiName);
			}

			try {
				envCtx.bind(ejbRef.getEjbRefName(), new LinkRef(jndiName));
			} catch (NameAlreadyBoundException ex) {
			}

			/*
			 * Validate the ejb reference has the correct type and classes as
			 * specified in deployment descriptor
			 */

			/*
			 * TODO I think I know the problem here. It seems the ejb is loaded
			 * AFTER the sbb is loaded, hence the validation fails here since it
			 * cannot locate the ejb. We need to force the ejb to be loaded
			 * before the sbb
			 */

			/*
			 * Commented out for now
			 * 
			 * 
			 * Object obj = new InitialContext().lookup("java:comp/env/" +
			 * ejbRef.getEjbRefName());
			 * 
			 * Object homeObject = null; try { Class homeClass =
			 * Thread.currentThread().getContextClassLoader().loadClass(home);
			 * 
			 * homeObject = PortableRemoteObject.narrow(obj, homeClass);
			 * 
			 * if (!homeClass.isInstance(homeObject)) { throw new
			 * DeploymentException("Looked up ejb home is not an instanceof " +
			 * home); } } catch (ClassNotFoundException e) { throw new
			 * DeploymentException("Failed to load class " + home); } catch
			 * (ClassCastException e) { throw new DeploymentException("Failed to
			 * lookup ejb reference using jndi name " + jndiName); }
			 * 
			 * Object ejb = null; try { Method m =
			 * homeObject.getClass().getMethod("create", null); Object ejbObject =
			 * m.invoke(home, null);
			 * 
			 * Class ejbClass =
			 * Thread.currentThread().getContextClassLoader().loadClass(remote);
			 * if (!ejbClass.isInstance(ejbObject)) { throw new
			 * DeploymentException("Looked up ejb object is not an instanceof " +
			 * remote); } } catch (ClassNotFoundException e) { throw new
			 * DeploymentException("Failed to load class " + remote); }
			 * 
			 */

			/*
			 * A note on the <ejb-link> link. The semantics of ejb-link when
			 * used to reference a remote ejb are not defined in the SLEE spec.
			 * In J2EE it is defined to mean a reference to an ejb deployed in
			 * the same J2EE application whose <ejb-name> is the same as the
			 * link (optionally the ejb-jar) file is also specifed. In SLEE
			 * there is no J2EE application and ejbs cannot be deployed in the
			 * SLEE container, therefore we do nothing with <ejb-link> since I
			 * am not sure what should be done with it anyway! - Tim
			 */

		}

		/* Set the environment entries */
		iter = getEnvEntries().iterator();
		while (iter.hasNext()) {
			EnvironmentEntry ee = (EnvironmentEntry) iter.next();
			Class type = null;

			if (logger.isDebugEnabled()) {
				logger.debug("Got an environment entry:" + ee);
			}

			try {
				type = Thread.currentThread().getContextClassLoader()
						.loadClass(ee.getType());
			} catch (Exception e) {
				throw new DeploymentException(ee.getType()
						+ " is not a valid type for an environment entry");
			}
			Object entry = null;
			String s = ee.getValue();

			try {
				if (type == String.class) {
					entry = new String(s);
				} else if (type == Character.class) {
					if (s.length() != 1) {
						throw new DeploymentException(
								s
										+ " is not a valid value for an environment entry of type Character");
					}
					entry = new Character(s.charAt(0));
				} else if (type == Integer.class) {
					entry = new Integer(s);
				} else if (type == Boolean.class) {
					entry = new Boolean(s);
				} else if (type == Double.class) {
					entry = new Double(s);
				} else if (type == Byte.class) {
					entry = new Byte(s);
				} else if (type == Short.class) {
					entry = new Short(s);
				} else if (type == Long.class) {
					entry = new Long(s);
				} else if (type == Float.class) {
					entry = new Float(s);
				}
			} catch (NumberFormatException e) {
				throw new DeploymentException("Environment entry value " + s
						+ " is not a valid value for type " + type);
			}
			if (logger.isDebugEnabled()) {
				logger.debug("Binding environment entry with name:"
						+ ee.getName() + " type  " + entry.getClass()
						+ " with value:" + entry + ". Current classloader = "
						+ Thread.currentThread().getContextClassLoader());
			}
			try {
				envCtx.bind(ee.getName(), entry);
			} catch (NameAlreadyBoundException ex) {
				logger.error("Name already bound ! ", ex);
			}
		}

	}

	private boolean findInterface(Class clazz, String toFind) {
		if (clazz.isInterface() && clazz.getName().equals(toFind))
			return true;

		if (clazz.getInterfaces().length > 0) {
			for (Class interfaze : clazz.getInterfaces()) {
				if (interfaze.getName().equals(toFind))
					return true;

				if (findInterface(interfaze, toFind))
					return true;
			}
		}

		return false;
	}
	
	// descriptors are stored in maps

	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == getClass()) {
			return ((MobicentsSbbDescriptorInternalImpl) obj).sbbID
					.equals(this.sbbID);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return sbbID.hashCode();
	}
	
}
