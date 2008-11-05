/*
* The Open SLEE project
*
* The source code contained in this file is in in the public domain.
* It can be used in any project or product without prior permission,
* license or royalty payments. There is no claim of correctness and
* NO WARRANTY OF ANY KIND provided with this code.
*/
package org.mobicents.slee.container.component.deployment;

import org.mobicents.slee.container.component.CMPField;
import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.MobicentsResourceAdaptorTypeBindingInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbDescriptorInternalImpl;
import org.mobicents.slee.container.component.MobicentsSbbEventEntryInternalImpl;
import org.mobicents.slee.container.component.EJBReference;
import org.mobicents.slee.container.component.EnvironmentEntry;
import org.mobicents.slee.container.component.GetChildRelationMethod;
import org.mobicents.slee.container.component.ProfileCMPMethod;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.component.ResourceAdaptorEntityBinding;
import org.mobicents.slee.container.component.MobicentsResourceAdaptorTypeBinding;
import org.mobicents.slee.container.component.MobicentsSbbDescriptor;
import org.mobicents.slee.container.component.SbbEventEntry;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.SbbRef;
import org.mobicents.slee.container.deployment.ConcreteClassGeneratorUtils;
import org.mobicents.slee.container.management.*;
import org.mobicents.slee.container.management.xml.*;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

import org.w3c.dom.*;
import java.util.*;
import java.io.*;
import javax.slee.*;

import org.apache.log4j.Logger;

/**
 * Parses Sbb Deployment Descriptors into Sbb Components
 * 
 * @author Emil Ivov
 * @author M. Rangnathan
 * 
 * @version 1.0
 */

public class SbbDeploymentDescriptorParser
    extends AbstractDeploymentDescriptorParser
{

	public static Logger logger = null;
	static {
		logger = Logger.getLogger(SbbDeploymentDescriptorParser.class);
	}
	
    public MobicentsSbbDescriptor parseSbbComponent(Element sbbNode, MobicentsSbbDescriptorInternalImpl descriptorImpl)
        throws IOException, Exception
    {
        ComponentKey sbbKey = createKey(sbbNode);

        descriptorImpl.setComponentKey(sbbKey);
        String description = XMLUtils.getElementTextValue(sbbNode, XMLConstants.DESCRIPTION_ND);
        descriptorImpl.setDescription(description);
        String aliasStr = XMLUtils.getElementTextValue(sbbNode, XMLConstants.SBB_ALIAS_ND);
        List sbbRefsNodeList = XMLUtils.getAllChildElements(sbbNode, XMLConstants.SBB_REF_ND);

        //Handle "sbb-ref"s
        HashMap sbbReferences = new HashMap();
        if(aliasStr != null)
            sbbReferences.put(aliasStr, sbbKey);

        for(Iterator iter = sbbRefsNodeList.iterator(); iter.hasNext();){
            Element sbbRefNode  = (Element)iter.next();
            String  nameStr     = XMLUtils.getElementTextValue( sbbRefNode,
                                                XMLConstants.SBB_NAME_ND);
            String  vendorStr   = XMLUtils.getElementTextValue( sbbRefNode,
                                                XMLConstants.SBB_VENDOR_ND);
            String  versionStr  = XMLUtils.getElementTextValue( sbbRefNode,
                                                XMLConstants.SBB_VERSION_ND);
            String  sbbRefAlias = XMLUtils.getElementTextValue( sbbRefNode,
                                                XMLConstants.SBB_ALIAS_ND);
            
           
            
            
            ComponentKey sbbRefKey = new ComponentKey(nameStr, vendorStr,
                                                      versionStr);
            //add the sbb reference to the table
            if(sbbReferences.put(sbbRefAlias, sbbRefKey) != null){
                throw new XMLException("Found multiple appearances of "
                                       + sbbRefKey.toString());
            }
          
            SbbRef sbbRef = new SbbRef(sbbRefKey,sbbRefAlias);
       
            descriptorImpl.addRef(sbbRef);
            
        }
        
        // handle ejbrefs
        List ejbRefNodeList = XMLUtils.getAllChildElements(sbbNode, XMLConstants.EJB_REF_ND);
        HashSet ejbRefs = new HashSet ();
        for (Iterator iter = ejbRefNodeList.iterator(); iter.hasNext();) {
            Element ejbRefNode = (Element) iter.next();
           
            String ejbRefName = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.EJB_REF_NAME);
            String ejbRefType = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.EJB_REF_TYPE);
            String ejbRefRemote = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.EJB_REF_REMOTE);
            String ejbRefHome = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.EJB_REF_HOME);
            String ejbRefLink = XMLUtils.getElementTextValue(ejbRefNode,XMLConstants.EJB_REF_LINK);
            EJBReference ejbRef = new EJBReference ( ejbRefName, ejbRefType, ejbRefHome,ejbRefRemote, ejbRefLink);
            ejbRefs.add(ejbRef);
        }
        descriptorImpl.setEjbRefs(ejbRefs);

        //Handle "profile-spec-ref" nodes
        List profileSpecRefNodes = XMLUtils.getAllChildElements(sbbNode,
                                        XMLConstants.PROFILE_SPEC_REF_ND);
        HashMap profileSpecReferences = new HashMap();
        for(Iterator iter = profileSpecRefNodes.iterator();iter.hasNext();){
            Element profileSpecNode = (Element)iter.next();
            String  specNameStr     = XMLUtils.getElementTextValue(
                profileSpecNode, XMLConstants.PROFILE_SPEC_NAME_ND);
            String  specVendorStr   = XMLUtils.getElementTextValue(
                profileSpecNode, XMLConstants.PROFILE_SPEC_VENDOR_ND);
            String  specVersionStr  = XMLUtils.getElementTextValue(
                profileSpecNode, XMLConstants.PROFILE_SPEC_VERSION_ND);
            String  specAliasStr    = XMLUtils.getElementTextValue(
                profileSpecNode, XMLConstants.PROFILE_SPEC_ALIAS_ND);
            ComponentKey profileSpecKey =
                new ComponentKey(specNameStr,
                        				specVendorStr, specVersionStr);
            ProfileSpecificationIDImpl pidImpl = new ProfileSpecificationIDImpl(profileSpecKey);
            if(profileSpecReferences.put(specAliasStr, pidImpl) != null){
                throw new XMLException("Multiple declarations of a profile spec reference are not allowed. "
                                       + "Detected multiple declarations of: " + profileSpecKey.toString());
            }
           
        }
        //  Set the profile spec references in the sbb descriptor.
        descriptorImpl.setProfileSpecReferences(profileSpecReferences);

        //Extract "sbb-classes"
        Element sbbClassesNode = XMLUtils.getChildElement(sbbNode,
            XMLConstants.SBB_CLASSES_ND, true);
        
        //Extract "usage-parameters-interface"
        String usageParametersInterfaceStr = null;
        Element usageParametersInterfaceNode = XMLUtils.getChildElement(
            sbbClassesNode,XMLConstants.SBB_USAGE_PARAMETERS_INTERFACE_ND,true);
        if(usageParametersInterfaceNode != null){
            usageParametersInterfaceStr = XMLUtils.getElementTextValue(
                usageParametersInterfaceNode,
                XMLConstants.SBB_USAGE_PARAMETERS_INTERFACE_NAME_ND);
            descriptorImpl.setUsageParametersInterface(usageParametersInterfaceStr);
        }

        Element sbbAbstractClassNode = XMLUtils.getChildElement(
            sbbClassesNode, XMLConstants.SBB_ABSTRACT_CLASS_ND, true);

        boolean reentrant = (new Boolean(sbbAbstractClassNode.getAttribute(
            XMLConstants.REENTRANT_ATTRIBUTE_AT))).booleanValue();

        descriptorImpl.setReentrant(reentrant);


        String sbbAbstractClassStr =  XMLUtils.getElementTextValue(
            sbbAbstractClassNode, XMLConstants.SBB_ABSTRACT_CLASS_NAME_ND);

        descriptorImpl.setSbbAbstractClassName(sbbAbstractClassStr);

        //CMP Fields
        HashSet duplicatesDetector = new HashSet();

        List sbbCMPFieldNodes = XMLUtils.getAllChildElements(sbbAbstractClassNode,
            XMLConstants.CMP_FIELD_ND);
        CMPField sbbCMPFields[] = new CMPField[sbbCMPFieldNodes.size()];

        Iterator iterator = sbbCMPFieldNodes.iterator();
        for (int i = 0; i < sbbCMPFields.length; i++) {
            Element sbbCMPFieldNode = (Element) iterator.next();
            String cmpFieldName = XMLUtils.getElementTextValue(sbbCMPFieldNode,
                XMLConstants.CMP_FIELD_NAME_ND);
            if (!duplicatesDetector.add(cmpFieldName)) {
                throw new XMLException("SBB CMP field name '" + cmpFieldName
                     + "' appears in multiple <cmp-field> declarations");
            }

            String sbbAliasRef = XMLUtils.getElementTextValue(sbbCMPFieldNode,
                "sbb-alias-ref");
            ComponentKey referencedSbbKey = null;
            if (sbbAliasRef != null) {
                referencedSbbKey = (ComponentKey) sbbReferences.get(sbbAliasRef);
                if (sbbKey == null) {
                    throw new XMLException("SBB alias reference '" + sbbAliasRef
                                     + "' unknown in <cmp-field> declaration");
                }
            }
            sbbCMPFields[i] = new CMPField(cmpFieldName, referencedSbbKey);
        }

        descriptorImpl.setCMPFields(sbbCMPFields);

        //Child Relations
        List childRelationMethodNodes = XMLUtils.getAllChildElements(
            sbbAbstractClassNode, XMLConstants.GET_CHILD_RELATION_METHOD_ND);
        ArrayList childRelationMethodList = new ArrayList();
        for(iterator = childRelationMethodNodes.iterator(); iterator.hasNext();){
            Element childRelationMethodNode = (Element)iterator.next();
            String sbbAliasRef = XMLUtils.getElementTextValue(
               childRelationMethodNode, XMLConstants.SBB_ALIAS_REF_ND);
            ComponentKey childKey = (ComponentKey)sbbReferences.get(sbbAliasRef);
            if(childKey == null){
                throw new XMLException("SBB alias reference '" + sbbAliasRef
                    + "' unknown in <get-child-relation-method> declaration");
            }
            descriptorImpl.setChildSbbComponentID(new SbbIDImpl(childKey));
            
            String getChildRelationMethodName = XMLUtils.getElementTextValue(
               childRelationMethodNode, XMLConstants.GET_CHILD_RELATION_METHOD_NAME_ND);
            String defaultPriority = XMLUtils.getElementTextValue(
                childRelationMethodNode, XMLConstants.DEFAULT_PRIORITY_ND);
            if(childKey != null){
                try{
                    childRelationMethodList.add(new GetChildRelationMethod(
                         new SbbIDImpl(childKey), getChildRelationMethodName,
                         Byte.parseByte(defaultPriority)));
                }
                catch(NumberFormatException nfe){
                    throw new XMLException(defaultPriority +"is not a valid "
                        +"default priority value in <get-child-relation-method>."
                        +" Value must be between -128 and 127 (inclusive)." );
                }
            }
        }
        GetChildRelationMethod childRelationMethods[] =
            (GetChildRelationMethod[])childRelationMethodList.toArray(
                new GetChildRelationMethod[childRelationMethodList.size()]);

        descriptorImpl.setChildRelationMethods(childRelationMethods) ;

        //Extract Event lists
        List eventNodes = XMLUtils.getAllChildElements(sbbNode,
                                                       XMLConstants.EVENT_ND);

        duplicatesDetector.clear();

        for (Iterator eventNodesIterator = eventNodes.iterator(); eventNodesIterator.hasNext(); ){
            //Create an event descriptor object.
        	MobicentsSbbEventEntryInternalImpl eventEntry = null;
            Element eventNode = (Element) eventNodesIterator.next();
            String eventName = XMLUtils.getElementTextValue(eventNode,
                XMLConstants.EVENT_NAME_NODE_ND);
            Element eventTypeRefNode = XMLUtils.getChildElement(eventNode,
                XMLConstants.EVENT_TYPE_REF_ND);
            ComponentKey eventTypeRefKey = createKey(eventTypeRefNode,
                XMLConstants.EVENT_TYPE_PREFIX);
            if(!duplicatesDetector.add(eventTypeRefKey)){
                throw new XMLException(
                    "Only one <event> declaration is permitted per event type: "
                    + eventTypeRefKey.toString());
            }

            List initialEventSelectorNodes = XMLUtils.getAllChildElements(
                eventNode, XMLConstants.INITIAL_EVENT_SELECT_ND);
            int initialEventSelectors[] = new int[initialEventSelectorNodes.size()];
            Iterator evtSelectorsIterator = initialEventSelectorNodes.iterator();
            for(int i = 0; i < initialEventSelectors.length; i++){
                Element initialEventSelectNode = (Element)evtSelectorsIterator.next();
            	logger.debug("Initial-Event-Select variable = " + 
            			initialEventSelectNode.getAttribute(XMLConstants.VARIABLE_AT));                
                initialEventSelectors[i] = MobicentsSbbEventEntryInternalImpl.eventSelectVarToInt(
                    initialEventSelectNode.getAttribute(XMLConstants.VARIABLE_AT));
            }

            String initialEventSelectorMethod = XMLUtils.getElementTextValue(
                eventNode, XMLConstants.INITIAL_EVENT_SELECTOR_METHOD_ND);
            if (initialEventSelectorMethod != null) {
            	if ((initialEventSelectorMethod.toLowerCase().startsWith("sbb")) ||
            		(initialEventSelectorMethod.toLowerCase().startsWith("ejb"))) {
            		throw new XMLException("<The initial event selector method> can not start with sbb or ejb: "
            		           			+ initialEventSelectorMethod);
            	}
            }

            String resourceOption = XMLUtils.getElementTextValue(eventNode,
                XMLConstants.EVENT_RESOURCE_OPTION_ND);
            String direction = eventNode.getAttribute(XMLConstants.EVENT_DIRECTION_AT);
            boolean fired =
                direction.equalsIgnoreCase(XMLConstants.EVENT_DIRECTION_ATV_FIRE)
                || direction.equals(XMLConstants.EVENT_DIRECTION_ATV_FIREANDRECEIVE);
            boolean received =
                direction.equalsIgnoreCase(XMLConstants.EVENT_DIRECTION_ATV_RECEIVE)
                || direction.equals(XMLConstants.EVENT_DIRECTION_ATV_FIREANDRECEIVE);

            if ( !fired && !received ) throw new XMLException ("Neither Fired Nor Recieved !");
            boolean maskOnAttach = eventNode.getAttribute(XMLConstants.MASK_ON_ATTACH_AT)
                                       .equalsIgnoreCase(XMLConstants.BOOLEAN_ATV_TRUE); // ralf: did the same as in the line below
            boolean initial      = eventNode.getAttribute(XMLConstants.INITIAL_EVENT_AT)
            // Francesco: EMIL I changed this method now we can use "True" or "true"
                                       .equalsIgnoreCase(XMLConstants.BOOLEAN_ATV_TRUE);

            //Try and detect any incoherent event property declarations.
            //Detect event direction and create the event id.
            if(fired){
                if(!received){
                    if ( logger.isDebugEnabled()) {
                        logger.debug(">>>>> Event is FIRED ONLY");
                    }
                    if(initial){
                        throw new XMLException("Cannot specify the initial-event property for a fired event: "
                                               + eventTypeRefKey.toString());
                    }
                    if(initialEventSelectors.length > 0 || initialEventSelectorMethod != null){
                        throw new XMLException("Cannot specify initial event selectors for a fired event: "
                                               + eventTypeRefKey.toString());
                    }
                    if(maskOnAttach){
                        throw new XMLException("Cannot specify the mask-on-attach property for a fired event: "
                                               + eventTypeRefKey.toString());
                    }
                    if(resourceOption != null){
                        throw new XMLException("Cannot specify an event resource option for a fired event: "
                                           + eventTypeRefKey.toString());
                    }
                }
                eventEntry = new MobicentsSbbEventEntryInternalImpl( eventTypeRefKey, SbbEventEntry.FIRED);
                eventEntry.setEventName(eventName);
                eventEntry.setFired(true);
                eventEntry.setResourceOption( resourceOption );
            }

            if(received){
                if(initial && initialEventSelectors.length == 0
                   && initialEventSelectorMethod == null){
                   throw new XMLException(
                       "Need to specify at least one of <initial-event-select> "
                       +"or <initial-event-selector-method-name> elements for "
                       +"initial event: " + eventTypeRefKey.toString());
                }

                //if the event is also fired then it has already been creaated
                if( eventEntry == null) {
                    eventEntry = new MobicentsSbbEventEntryInternalImpl(eventTypeRefKey, SbbEventEntry.RECEIVED);
                	eventEntry.setEventName(eventName);
                } else
                    eventEntry.setReceived(true);

                eventEntry.setInitial(initial);
                eventEntry.setInitialEventSelectors(initialEventSelectors);
                eventEntry.setInitialEventSelectorMethod(initialEventSelectorMethod);
                eventEntry.setResourceOption( resourceOption );
                if(maskOnAttach)
                    eventEntry.maskEvent();
            }
            if ( logger.isDebugEnabled() ) {
                logger.debug("MobicentsSbbDescriptor.parseSbbComponent(): >>>>> Event entry added: " + eventEntry);
            }
            descriptorImpl.addEvent(eventEntry);
        }



        List profileCMPMethodNodes = XMLUtils.getAllChildElements(
            sbbAbstractClassNode, XMLConstants.GET_PROFILE_CMP_METHOD_ND);
        ArrayList profileCMPMethodList = new ArrayList();
        for(iterator = profileCMPMethodNodes.iterator(); iterator.hasNext();)
        {
            Element profileCMPMethodNode = (Element)iterator.next();
            String profileSpecAliasRef = XMLUtils.getElementTextValue(
                profileCMPMethodNode, XMLConstants.PROFILE_SPEC_ALIAS_REF_ND);
            ComponentKey profileSpecKey =
                ((ProfileSpecificationIDImpl)profileSpecReferences.
                        get(profileSpecAliasRef)).getComponentKey();
            if(profileSpecKey == null)
            {
                new XMLException("Profile specification alias reference '"
                                 + profileSpecAliasRef
                                 + "' unknown in <get-profile-cmp-method> declaration");
            } else
            {
                String getProfileCMPMethod = XMLUtils.getElementTextValue(
                    profileCMPMethodNode, XMLConstants.GET_PROFILE_CMP_METHOD_NAME_ND);
                profileCMPMethodList.add(new ProfileCMPMethod(
                    profileSpecKey, getProfileCMPMethod));
            }
        }

        try
        {
        	Collections.sort(profileCMPMethodList);
        } catch (Exception ex1)
        {
            //do nothing
            logger.warn("sort threw exception: "+ ex1.toString());
        }

        ProfileCMPMethod profileCMPMethods[] =
            (ProfileCMPMethod[])profileCMPMethodList.toArray(
                new ProfileCMPMethod[profileCMPMethodList.size()]);
        descriptorImpl.setProfileCMPMethods(profileCMPMethods) ;
        
        String sbbLocalInterface = null;
        Element sbbLocalInterfaceNode = XMLUtils.getChildElement(sbbClassesNode,
            XMLConstants.SBB_LOCAL_INTERFACE_ND);
        if(sbbLocalInterfaceNode != null)
        {
            sbbLocalInterface = XMLUtils.getElementTextValue(sbbLocalInterfaceNode,
                XMLConstants.SBB_LOCAL_INTERFACE_NAME_ND);
        }
        if(sbbLocalInterface == null)
        {
            sbbLocalInterface = SbbLocalObject.class.getName();
        }
        // Class name of the sbb local interface.
        descriptorImpl.setSbbLocalInterfaceClassName(sbbLocalInterface);
        String sbbActivityContextInterface = null;
        Element sbbActivityContextInterfaceNode = XMLUtils.getChildElement(
            sbbClassesNode, XMLConstants.SBB_ACTIVITY_CONTEXT_INTERFACE_ND);
        if(sbbActivityContextInterfaceNode != null)
        {
            sbbActivityContextInterface = XMLUtils.getElementTextValue(
                sbbActivityContextInterfaceNode, XMLConstants.SBB_ACTIVITY_CONTEXT_INTERFACE_NAME_ND);
            if(sbbActivityContextInterface.equals(ActivityContextInterface.class.getName()))
            {
                sbbActivityContextInterface = null;
            }
            else
                descriptorImpl.setActivityContextInterfaceClassName(sbbActivityContextInterface);
        }
        ProfileSpecificationIDImpl addressProfileSpec = null;
        String addressProfileSpecRef = XMLUtils.getElementTextValue(sbbNode,
            XMLConstants.ADDRESS_PROFILE_SPEC_ALIAS_REF_ND);
        if(addressProfileSpecRef != null)
        {
            assertNonZeroLength(addressProfileSpecRef, XMLConstants.ADDRESS_PROFILE_SPEC_ALIAS_REF_ND);
            addressProfileSpec = (ProfileSpecificationIDImpl)profileSpecReferences.get(addressProfileSpecRef);
            if(addressProfileSpec == null)
            {
                new XMLException("Profile specification alias reference '"
                + addressProfileSpecRef
                + "' unknown in <address-profile-spec-alias-ref> declaration");
            }
            descriptorImpl.setAddressProfileSpecAlias(addressProfileSpecRef);
        }



        List aciAttrAliasNodes = XMLUtils.getAllChildElements(sbbNode,
            XMLConstants.ACTIVITY_CONTEXT_ATTRIBUTE_ALIAS_ND);
        HashMap aciAttributeAliases = new HashMap();
        for(iterator = aciAttrAliasNodes.iterator(); iterator.hasNext();)
        {
            Element aciAttrAliasNode = (Element)iterator.next();
            String aliasName = XMLUtils.getElementTextValue(aciAttrAliasNode,
                XMLConstants.ATTRIBUTE_ALIAS_NAME_ND);
            for(Iterator iterator2 = XMLUtils.getAllChildElements(aciAttrAliasNode,
                XMLConstants.SBB_ACTIVITY_CONTEXT_ATTRIBUTE_NAME_ND).iterator(); iterator2.hasNext();)
            {
                String aciAttributeName = XMLUtils.getElementTextValue( (Element)iterator2.next());
                assertNonZeroLength(aciAttributeName, XMLConstants.SBB_ACTIVITY_CONTEXT_ATTRIBUTE_NAME_ND);
                if(aciAttributeAliases.put(aciAttributeName, aliasName) != null)
                {
                    throw new XMLException(
                        "Activity context interface attribute name '" + aciAttributeName
                        + "' appears multiple times in <activity-context-attribute-alias> declarations");
                }
            }

        }
        descriptorImpl.setActivityContextInterfaceAttributeAliases(aciAttributeAliases);

        List envEntryNodes = XMLUtils.getAllChildElements(sbbNode, 
                	XMLConstants.ENV_ENTRY_ND);
        HashSet envEntries = new HashSet();
        
        for (Iterator it = envEntryNodes.iterator(); it.hasNext() ; ) {
            Element envEntry = (Element) it.next();
            String name = XMLUtils.getElementTextValue(envEntry,
                    XMLConstants.ENV_ENTRY_NAME);
            String value = XMLUtils.getElementTextValue (envEntry,
                    XMLConstants.ENV_ENTRY_VALUE);
            String type = XMLUtils.getElementTextValue( envEntry, 
                    XMLConstants.ENV_ENTRY_TYPE);
            description = XMLUtils.getElementTextValue(envEntry, 
                    XMLConstants.ENV_ENTRY_DESCRIPTION);
            envEntries.add ( new EnvironmentEntry (description,
                    			name, value,type ));
        }
        descriptorImpl.setEnvironmentEntries( envEntries);
       
        List resourceAdapterTypeBindingNodes = XMLUtils.getAllChildElements(sbbNode,
                XMLConstants.RESOURCE_ADAPTOR_TYPE_BINDING);
        HashSet resourceAdapterTypeBindings = new HashSet();
        for (Iterator it = resourceAdapterTypeBindingNodes.iterator(); it.hasNext();){
        	MobicentsResourceAdaptorTypeBindingInternalImpl ratBinding = new MobicentsResourceAdaptorTypeBindingInternalImpl();
            Element raTypeBinding= (Element)it.next();
            description = XMLUtils.getElementTextValue(raTypeBinding, 
                    XMLConstants.RESOURCE_ADAPTOR_TYPE_DESCRIPTION);
            Element raTypeRef = XMLUtils.getChildElement(raTypeBinding, 
                    XMLConstants.RESOURCE_ADPATOR_TYPE_REF_ND);
            //Element raTypeRef = (Element)raTypeBinding.getAttributeNode(
            //        XMLConstants.RESOURCE_ADAPTOR_TYPE_DESCRIPTION);
            String name = XMLUtils.getElementTextValue(raTypeRef,
                    XMLConstants.RESOURCE_ADAPTOR_TYPE_NAME);
            String vendor = XMLUtils.getElementTextValue(raTypeRef,
                    XMLConstants.RESOURCE_ADAPTOR_TYPE_VENDOR);
            String version = XMLUtils.getElementTextValue(raTypeRef,
                    XMLConstants.RESOURCE_ADAPTOR_TYPE_VERSION);
            
            ResourceAdaptorTypeIDImpl raTypeIDImpl = 
                new ResourceAdaptorTypeIDImpl
                (new ComponentKey(name,vendor,version));
            descriptorImpl.addResourceAdapterType(raTypeIDImpl);
            ratBinding.setResourceAdapterTypeId(raTypeIDImpl);
            String acifName = XMLUtils.getElementTextValue ( raTypeBinding,
                  XMLConstants.RESOURCE_ADAPTOR_ACTIVITY_CONTEXT_INTERFACE_FACTORY_NAME);
            if ( logger.isDebugEnabled() ) {
                logger.debug("ACI Factory Name " + acifName);
            }
            ratBinding.setActivityContextInterfaceFactoryName(acifName);
            HashSet raEntityBindings = new HashSet();
            List raEntityBindingList = XMLUtils.getAllChildElements(raTypeBinding,
                    XMLConstants.RESOURCE_ADAPTOR_ENTITY_BINDING_ND);
            if ( logger.isDebugEnabled()){
            	logger.debug("entity type bindings >>>>>>>> " + raEntityBindingList.size());
            }
            for (Iterator it1 = raEntityBindingList.iterator(); it1.hasNext();) {
                Element raEntityBindingNode = (Element) it1.next();
                String descr = XMLUtils.getElementTextValue(raEntityBindingNode,
                        XMLConstants.RESOURCE_ADAPTOR_ENTITY_BINDING_DECRITPTION);
                String bname = XMLUtils.getElementTextValue(raEntityBindingNode,
                        XMLConstants.RESOURCE_ADAPTOR_ENTITY_BINDING_OJBECT_NAME);
                String elink = XMLUtils.getElementTextValue(raEntityBindingNode,
                        XMLConstants.RESOURCE_ADAPTOR_ENTITY_BINDING_ENTITY_LINK);
                // TODO -- check -- is this the only piece of information to add?
                descriptorImpl.addResourceAdapterEntityLink(elink);
                ResourceAdaptorEntityBinding raEntityLink =
                    new ResourceAdaptorEntityBinding(descr,bname,elink);
                raEntityBindings.add(raEntityLink);
                descriptorImpl.addResourceAdaptorEntityBinding(raTypeIDImpl,raEntityLink);
                
            }
            ratBinding.setResourceAdapterEntityBindings(raEntityBindings);
            
            resourceAdapterTypeBindings.add(ratBinding);
        }
        
        descriptorImpl.setResourceAdapterTypeBindings(resourceAdapterTypeBindings);


        return descriptorImpl;

    }
}
