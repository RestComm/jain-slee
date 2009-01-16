/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/

package org.mobicents.slee.container.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.slee.ComponentID;
import javax.slee.EventTypeID;
import javax.slee.SbbID;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentException;
import javax.slee.management.SbbDescriptor;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.mobicents.slee.container.service.ServiceComponent;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;
import org.mobicents.slee.runtime.eventrouter.DeferredEvent;

/**
 * This class contains the information about the Sbb Component extracted from
 * the deployement descriptor. This is the class that gets filled in by the Sbb
 * deployment descriptor parser.
 * 
 * @author Francesco Moggia
 * @author M. Ranganathan
 * @author Emil Ivov
 * @author Ivelin Ivanov
 * 
 *  
 */

public interface MobicentsSbbDescriptor extends Serializable, SbbDescriptor,
        DeployedComponent {


	/**
	 * Set up the comp/env naming environment for the Sbb. This method is called
	 * during deployment and sets up the naming context for the SBB environment.
	 * It also checks to see if the resource adaptors etc. are installed.
	 * 
	 * @param container
	 * @throws Exception
	 */
	public void setupSbbEnvironment() throws Exception;

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

    public void addEventMap(String typeName, SbbEventEntry eventEntry) ;

    

    /**
     * Get the reEntrant flag
     */
    public boolean isReentrant() ;

    /**
     * @return Returns the initialEvents.
     */
    public Set getInitialEventTypes() ;

    public void addRef(SbbRef ref) ;

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
    public String getEventName(EventTypeID typeName) ;

    /**
     * Get the event type given the typeName
     * 
     * @return the event type
     */
    public SbbEventEntry getEventType(String typeName) ;

    /**
     * get the concreteSbb for the sbb. this is a pointer to the stuff that you
     * generate. This is accessed to create an instance of the SBB.
     * 
     * @return Returns the concreteSbb.
     */
    public Class getConcreteSbbClass() ;

    /**
     * Class of concrete SBB to set. you call this method to set the concrete
     * SBB after you generate it from the abstract SBB.
     * 
     * @param concreteSbb --
     *            the concreteSbb Class to set.
     *  
     */

    public void setConcreteSbb(Class concreteSbbClass) ;

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
    public String computeConvergenceName(DeferredEvent sleeEvent, ServiceComponent svc)
            throws Exception ;

    /**
     * get the received events set.
     * 
     * @return Returns the receivedEvents.
     */
    public Set getReceivedEvents() ;

    /**
     * get the fired event set.
     * 
     * @return firedEvent set
     */
    public Set getFiredEvents() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.SbbDescriptor#getSbbs()
     */
    public SbbID[] getSbbs() ;

    public void setChildSbbComponentID(SbbID sbbid) ;

    /**
     * Add an event.
     * 
     * @param event
     *            the event to add.
     * @throws Exception
     *             if you try to add an unknown event type. Event types are
     *             defined in event.jar
     */
    public void addEvent(SbbEventEntry eventEntry) throws Exception;

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

    public void addEventEntry(EventTypeID eventTypeId, SbbEventEntry eventEntry) ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.SbbDescriptor#getProfileSpecifications()
     */
    public ProfileSpecificationID[] getProfileSpecifications() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.SbbDescriptor#getAddressProfileSpecification()
     */
    public ProfileSpecificationID getAddressProfileSpecification() ;

  
    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.SbbDescriptor#getResourceAdaptorTypes()
     */
    public ResourceAdaptorTypeID[] getResourceAdaptorTypes() ;

    
    public Iterator getResourceAdapterTypeBindings() ;

 
    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.SbbDescriptor#getResourceAdaptorEntityLinks()
     */
    public String[] getResourceAdaptorEntityLinks() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getDeployableUnit()
     */
    public DeployableUnitID getDeployableUnit() ;



    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getSource()
     */
    public String getSource() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getID()
     */
    public ComponentID getID() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getName()
     */
    public String getName() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getVendor()
     */
    public String getVendor() ;

    /*
     * (non-Javadoc)
     * 
     * @see javax.slee.management.ComponentDescriptor#getVersion()
     */
    public String getVersion() ;

    /**
     * set the component key. The SLEE container registers the component using
     * this key.
     */
    public void setComponentKey(ComponentKey componentKey) throws Exception ;

    /**
     * EMIL -- this is where you call your parser.
     */

    public void parseSbb() ;

    /**
     * Set the abstract class name for the sbb.
     * 
     * @param className
     */
    public void setSbbAbstractClassName(String className) ;
    /**
     * Retrieve the sbb abstract Class name described by the descriptor
     * 
     * @return the sbb abstract Class name
     */
    public String getSbbAbstractClassName() ;

    /**
     * Set the childRelationMethods field.
     * 
     * @param childRelationMethods --
     *            an array of GetChildRelationMethod[] parsed from the
     *            deployment descriptor.
     *  
     */
    public void setChildRelationMethods(
            GetChildRelationMethod[] childRelationMethods) ;

    /**
     * Get the childrelation acessor methods.
     * 
     * @return -- an array of childrelation accessors.
     *  
     */
    public GetChildRelationMethod[] getChildRelationMethods() ;

    /**
     * 
     * Retreive the get child relation method for the specified name, returns null if does not exists.
     * @param name
     * @return
     */
    public GetChildRelationMethod getChildRelationMethod(String name);
    
    /**
     * Set the cmpfields array.
     * 
     * @param sbbCMPFields --
     *            an array of SBBCMPFields to set.
     * 
     *  
     */

    public void setCMPFields(CMPField[] cmpFields) ;

    /**
     * Get the CMP Fields.
     * 
     * @return -- an array containing the cmp fields
     */
    public CMPField[] getCMPFields() ;

    /**
     * set the profile cmp methods.
     * 
     * @param profileCMPMethods --
     *            the profile cmp methods.
     */
    public void setProfileCMPMethods(ProfileCMPMethod[] profileCmpMethods) ;

    /**
     * get the profile cmp methods.
     * 
     * @return an array containing the profileCMP emthods.
     */
    public ProfileCMPMethod[] getProfileCMPMethods() ;

    /**
     * set the SBB local interface
     * 
     * @throws Exception
     */
    public void setSbbLocalInterfaceClassName(String localInterfaceClassName)
            throws Exception ;

    public Class getSbbLocalInterface() ;

    public void setLocalInterfaceConcreteClass(Class clazz) ;

    public Class getLocalInterfaceConcreteClass() ;

    /**
     * Retrieve the activity context interface class
     * 
     * @return the activity context interface class
     */
    public Class getActivityContextInterface() ;

    /**
     * Set the activity context interface class. This is an abstract class.
     * 
     * @throws Exception
     */
    public void setActivityContextInterfaceClassName(
            String activityContextInterfaceClassName) throws Exception ;

    /**
     * set the concrete class for the activityContextInterface.
     * 
     * @throws Exception
     */
    public void setActivityContextInterfaceConcreteClass(Class aciConcreteClass) ;

    public Class getActivityContextInterfaceConcreteClass() ;
   



    /**
     * Get the Usage parameters interface.
     * 
     * @return the usage parameters interface.
     */
    public String getUsageParametersInterface() ;

    /**
     * getEJBReferences
     * 
     * @return an array containing the EjbRefs for this Sbb.
     */
    public HashSet getEjbRefs() ;



    public HashSet getEnvEntries();



    /**
     * Get the sbb event entry.
     * 
     * @return the set of Sbb event entries.
     */

    public HashSet getSbbEventEntries() ;

    public EventTypeID[] getEventTypes() ;

    /**
     * @return Returns the sbbRef.
     */
    public HashSet getSbbRef() ;

    /**
     * @param sbbRef
     *            The sbbRef to set.
     */
    public void setSbbRef(HashSet sbbRef) ;

    public HashMap getActivityContextInterfaceAttributeAliases() ;

    /**
     * @return Returns the loader.
     */
    public ClassLoader getClassLoader() ;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.slee.container.management.DeployedComponent#setDeployableUnit(javax.slee.management.DeployableUnitID)
	 */
    public void setDeployableUnit(DeployableUnitID deployableUnitID) ;

    /**
     * @param description
     */
    public void setDescription(String description) ;

    public String getDescription() ;

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
    public void checkDeployment() throws DeploymentException ;
    public void postInstall() throws Exception ;

    public String getActivityContextInterfaceClassName() ;

    /**
     * @param raTypeIDImpl
     * @param raEntityLink
     */
    public void addResourceAdaptorEntityBinding(
            ResourceAdaptorTypeIDImpl raTypeID,
            ResourceAdaptorEntityBinding raEntityLink) ;

    /**
     * Get an entity binding for a given resource adaptor type
     *  
     */
    public Iterator getResourceAdaptorEntityBindings(
            ResourceAdaptorTypeIDImpl raTypeID) ;

    /**
     * @param usageParamClazz
     */
    public void setUsageParameterClass(Class usageParamClazz) ;
    public Class getUsageParameterClass() ;

    /**
     * @return the path to the root where the Sbb was deployed.
     */
    public String getDeploymentPath() ;
    /**
     * 
     * @return HashMap containing mapping between EventTypeID and coresponding SbbEventEntry
     */
    public HashMap getEventTypesMappings();

}