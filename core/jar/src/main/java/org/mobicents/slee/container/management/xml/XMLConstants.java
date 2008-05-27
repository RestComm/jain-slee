/*
 * The Open SLEE project.
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is no claim of correctness and
 * NO WARRANTY OF ANY KIND provided with this code.
 */
package org.mobicents.slee.container.management.xml;

/**
 * Contains all strings used when parsing xml. I'm using a simple naming convention
 * for variable names which is as follows: <br>
 * <node name>_ND - for node names
 * <attribute name>_AT - for attribute names
 * @author Emil Ivov
 * @version 1.0
 */

public class XMLConstants
{
    //----------------------- SBB Deployment Descriptor -----------------------
    //BOOLEAN attribute values
    public static final String BOOLEAN_ATV_TRUE  = "True";
    public static final String BOOLEAN_ATV_FALSE = "False";

    public static final String SBB_ND       = "sbb";
    public static final String MC_SBB_ND = "sbb";

    //sbb description strings
    public static final String SBB_JAR_ND     = "sbb-jar";
    public static final String DESCRIPTION_ND  = "description";
    public static final String SBB_NAME_ND     = "sbb-name";
    public static final String SBB_VENDOR_ND   = "sbb-vendor";
    public static final String SBB_VERSION_ND  = "sbb-version";
    public static final String SBB_ALIAS_ND    = "sbb-alias";
    public static final String SBB_REF_ND      = "sbb-ref";
    
    //mobicents-sbb description strings
    public static final String MC_SBB_NAME_ND     = "sbb-name";
    public static final String MC_SBB_VENDOR_ND   = "sbb-vendor";
    public static final String MC_SBB_VERSION_ND  = "sbb-version";
    public static final String MC_EJB_REF_ND 	  = "ejb-ref";
    public static final String MC_EJB_REF_NAME 	  = "ejb-ref-name";
    public static final String MC_JNDI_NAME 	  = "jndi-name";

    //profile related strings
    public static final String PROFILE_SPEC_REF_ND     = "profile-spec-ref";
    public static final String PROFILE_SPEC_NAME_ND    = "profile-spec-name";
    public static final String PROFILE_SPEC_VENDOR_ND  = "profile-spec-vendor";
    public static final String PROFILE_SPEC_VERSION_ND = "profile-spec-version";
    public static final String PROFILE_SPEC_ALIAS_ND   = "profile-spec-alias";

    //class related string
    public static final String SBB_CLASSES_ND          = "sbb-classes";
  
    public static final String SBB_USAGE_PARAMETERS_INTERFACE_ND
                                         ="sbb-usage-parameters-interface";
    public static final String SBB_USAGE_PARAMETERS_INTERFACE_NAME_ND
                                         ="sbb-usage-parameters-interface-name";
    public static final String SBB_ABSTRACT_CLASS_ND   = "sbb-abstract-class";
    public static final String SBB_ABSTRACT_CLASS_NAME_ND
                                         = "sbb-abstract-class-name";
    
    public static final String REENTRANT_ATTRIBUTE_AT       = "reentrant";

    //event related strings
    public static final String EVENT_ND              = "event";
    public static final String EVENT_NAME_NODE_ND    = "event-name";
    public static final String EVENT_TYPE_REF_ND     = "event-type-ref";
    

    public static final String INITIAL_EVENT_SELECT_ND = "initial-event-select";

    public static final String INITIAL_EVENT_SELECTOR_METHOD_ND = "initial-event-selector-method-name";
    public static final String EVENT_RESOURCE_OPTION_ND         = "event-resource-option";

    public static final String EVENT_DIRECTION_AT                 = "event-direction";
    public static final String EVENT_DIRECTION_ATV_FIRE           = "Fire";
    public static final String EVENT_DIRECTION_ATV_RECEIVE        = "Receive";
    public static final String EVENT_DIRECTION_ATV_FIREANDRECEIVE = "FireAndReceive";

    public static final String MASK_ON_ATTACH_AT = "mask-on-attach";
    public static final String INITIAL_EVENT_AT  = "initial-event";
    public static final String VARIABLE_AT       = "variable";

    public static final String CMP_FIELD_ND       = "cmp-field";
    public static final String CMP_FIELD_NAME_ND  = "cmp-field-name";

    public static final String GET_CHILD_RELATION_METHOD_ND      = "get-child-relation-method";
    public static final String SBB_ALIAS_REF_ND                  = "sbb-alias-ref";
    public static final String GET_CHILD_RELATION_METHOD_NAME_ND = "get-child-relation-method-name";
    public static final String DEFAULT_PRIORITY_ND               = "default-priority";

    public static final String GET_PROFILE_CMP_METHOD_ND         = "get-profile-cmp-method";
    public static final String PROFILE_SPEC_ALIAS_REF_ND         = "profile-spec-alias-ref";
    public static final String GET_PROFILE_CMP_METHOD_NAME_ND       = "get-profile-cmp-method-name";

    //LOCAL Interface
    public static final String SBB_LOCAL_INTERFACE_ND      = "sbb-local-interface";
    public static final String SBB_LOCAL_INTERFACE_NAME_ND = "sbb-local-interface-name";

    public static final String SBB_ACTIVITY_CONTEXT_INTERFACE_ND      = "sbb-activity-context-interface";
    public static final String SBB_ACTIVITY_CONTEXT_INTERFACE_NAME_ND = "sbb-activity-context-interface-name";

    public static final String ADDRESS_PROFILE_SPEC_ALIAS_REF_ND = "address-profile-spec-alias-ref";

    public static final String ACTIVITY_CONTEXT_ATTRIBUTE_ALIAS_ND = "activity-context-attribute-alias";

    public static final String ATTRIBUTE_ALIAS_NAME_ND = "attribute-alias-name";
    public static final String SBB_ACTIVITY_CONTEXT_ATTRIBUTE_NAME_ND = "sbb-activity-context-attribute-name";

    //-------------------- EVENT TYPE DEPLOYMENT DESCRIPTOR --------------------
    public static final String EVENT_TYPE_PREFIX     = "event-type";
    public static final String EVENT_DEFINITION_ND   = "event-definition";
    public static final String EVENT_TYPE_NAME_ND    = "event-type-name";
    public static final String EVENT_TYPE_VENDOR_ND  = "event-type-vendor";
    public static final String EVENT_TYPE_VERSION_ND = "event-type-version";
    public static final String EVENT_CLASS_NAME_ND   = "event-class-name";


    //---------------- SERVICE DEPLOYMENT DESCRIPTOR ---------------------------
    public static final String ROOT_SBB_ND                    = "root-sbb";
    public static final String ADDRESS_PROFILE_TABLE_ND       = "address-profile-table";
    public static final String RESOURCE_INFO_PROFILE_TABLE_ND = "resource-info-profile-table";
    
    //----------------- EJB REF deployment descriptor-------------------------------------
    public static final String EJB_REF_ND 							= "ejb-ref";
    public static final String EJB_REF_NAME 					= "ejb-ref-name";
    public static final String EJB_REF_TYPE 					= "ejb-ref-type";
    public static final String EJB_REF_HOME 						= "home";
    public static final String EJB_REF_REMOTE						= "remote";
    public static final String EJB_REF_LINK 						= "ejb-link";
    
    //---------------- ENV Entry deployment descriptor
    public static final String ENV_ENTRY_ND					= "env-entry";
    public static final String ENV_ENTRY_DESCRIPTION 		= "description";
    public static final String ENV_ENTRY_NAME 				= "env-entry-name";
    public static final String ENV_ENTRY_VALUE 				= "env-entry-value";
    public static final String ENV_ENTRY_TYPE 				= "env-entry-type";
    
    //--------------  Resource Adapter type binding elements
   
    public static final String RESOURCE_ADAPTOR_TYPE_JAR_ND = "resource-adaptor-type-jar";
    public static final String RESOURCE_ADAPTOR_TYPE_ND = "resource-adaptor-type";
    public static final String RESOURCE_ADAPTOR_TYPE_BINDING = "resource-adaptor-type-binding";
    public static final String RESOURCE_ADAPTOR_TYPE_DESCRIPTION = "description";
    public static final String RESOURCE_ADAPTOR_ACTIVITY_CONTEXT_INTERFACE_FACTORY_NAME = "activity-context-interface-factory-name";
    public static final String RESOURCE_ADAPTOR_TYPE_CLASSES = "resource-adaptor-type-classes";
    public static final String RESOURCE_ADAPTOR_TYPE_ACTIVITY_TYPE = "activity-type";
    public static final String RESOURCE_ADAPTOR_TYPE_ACTIVITY_TYPE_NAME = "activity-type-name";
    public static final String ACTIVITY_CONTEXT_INTERFACE_FACTORY_INTERFACE = "activity-context-interface-factory-interface";
    public static final String ACTIVITY_CONTEXT_INTERFACE_FACTORY_INTERFACE_NAME = "activity-context-interface-factory-interface-name";
    
   
    //--------------- Resource adaptor jar elements
    public static final String RESOURCE_ADAPTOR = "resource-adaptor";
    public static final String RESOURCE_ADAPTOR_JAR_ND = "resource-adaptor-jar";
    public static final String RESOURCE_ADAPTOR_NAME = "resource-adaptor-name";
    public static final String RESOURCE_ADAPTOR_VENDOR = "resource-adaptor-vendor"; 
    public static final String RESOURCE_ADAPTOR_VERSION = "resource-adaptor-version";
    public static final String RESOURCE_ADAPTOR_TYPE_REF = "resource-adaptor-type-ref";
    public static final String RESOURCE_ADAPTOR_CLASSES  = "resource-adaptor-classes";
    public static final String RESOURCE_ADAPTOR_CLASS  = "resource-adaptor-class";
    public static final String RESOURCE_ADAPTOR_CLASS_NAME  = "resource-adaptor-class-name";
    public static final String RESOURCE_ADAPTOR_CONFIG_PROPERTY = "config-property";
    public static final String RESOURCE_ADAPTOR_CONFIG_PROPERTY_NAME = "config-property-name";        
    public static final String RESOURCE_ADAPTOR_CONFIG_PROPERTY_TYPE = "config-property-type";        
    public static final String RESOURCE_ADAPTOR_CONFIG_PROPERTY_VALUE = "config-property-value";        
    
    //------ Resource adapter type ref
    public static final String RESOURCE_ADAPTOR_INTERFACE = "resource-adaptor-interface";
    public static final String RESOURCE_ADAPTOR_INTERFACE_NAME = "resource-adaptor-interface-name";
    public static final String RESOURCE_ADPATOR_TYPE_REF_ND = "resource-adaptor-type-ref";
    public static final String RESOURCE_ADAPTOR_TYPE_NAME = "resource-adaptor-type-name";
    public static final String RESOURCE_ADAPTOR_TYPE_VERSION = "resource-adaptor-type-version";
    public static final String RESOURCE_ADAPTOR_TYPE_VENDOR = "resource-adaptor-type-vendor";
    
    //----------------Resource adapter entity binding   
    public static final String RESOURCE_ADAPTOR_ENTITY_BINDING_ND = "resource-adaptor-entity-binding";
    public static final String RESOURCE_ADAPTOR_ENTITY_BINDING_DECRITPTION="description";
    public static final String RESOURCE_ADAPTOR_ENTITY_BINDING_OJBECT_NAME = "resource-adaptor-object-name";
    public static final String RESOURCE_ADAPTOR_ENTITY_BINDING_ENTITY_LINK="resource-adaptor-entity-link";

    //---------------- SERVICE DEPLOYMENT DESCRIPTOR ---------------------------
    public static final String SERVICE_XML_ND                 = "service-xml";
    public static final String JAR_ND                         = "jar";
    
    //----------------------- Profile Specification Deployment Descriptor -----------------------
    public static final String PROFILE_SPEC_ND       = "profile-spec";
    public static final String PROFILE_CLASSES_ND     = "profile-classes";
    public static final String PROFILE_INDEX_ND     = "profile-index";
    public static final String PROFILE_INDEX_UNIQUE_AT     = "unique";
    public static final String PROFILE_HINTS_ND     = "profile-hints";
    public static final String PROFILE_HINTS_SINGLE_PROFILE_AT     = "single-profile";
    
    public static final String PROFILE_CLASSES_DESCRIPTION_ND     = "description";
    public static final String PROFILE_CLASSES_CMP_INTERFACE_NAME_ND    = "profile-cmp-interface-name";
    public static final String PROFILE_CLASSES_MANAGEMENT_INTERFACE_NAME_ND  = "profile-management-interface-name";
    public static final String PROFILE_CLASSES_MANAGEMENT_ABSTRACT_CLASS_NAME_ND = "profile-management-abstract-class-name";    
}
