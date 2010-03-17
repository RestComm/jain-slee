package javax.slee.management;

import javax.management.ObjectName;
import javax.slee.InvalidArgumentException;
import javax.slee.InvalidStateException;
import javax.slee.SbbID;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.ResourceAdaptorID;

/**
 * The ResourceManagementMBean interface defines resource adaptor-related management operations.
 * <p>
 * Using the ResourceManagementMBean a management client may create, remove, activate and deactivate
 * resource adaptor entities, configure resource adaptor properties, and manage resource adaptor
 * entity link names.
 * <p>
 * The JMX Object Name of a <code>ResourceManagementMBean</code> object is specified by
 * the {@link #OBJECT_NAME} constant.  The Object Name can also be obtained by a
 * management client via the {@link SleeManagementMBean#getResourceManagementMBean()} method.
 * @since SLEE 1.1
 */
public interface ResourceManagementMBean {
    /**
     * The JMX Object Name string of the SLEE Resource Management MBean, equal to the string
     * "javax.slee.management:name=ResourceManagement".
     */
    public static final String OBJECT_NAME = "javax.slee.management:name=ResourceManagement";

    /**
     * The notification type of {@link ResourceAdaptorEntityStateChangeNotification ResourceAdaptorEntityStateChange}
     * notifications emitted by this MBean.  The notification type is equal to the
     * string "<code>javax.slee.management.resourceadaptorentitystatechange</code>".
     */
    public static final String RESOURCE_ADAPTOR_ENTITY_STATE_CHANGE_NOTIFICATION_TYPE =
        "javax.slee.management.resourceadaptorentitystatechange";


    /**
     * Get the configuration properties, and their default values if any, for the specified
     * resource adaptor.
     * @param id the identifier of the resource adaptor.
     * @return a <code>ConfigProperties</code> object containing the configuration properties of
     *        the resource adaptor.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorException if <code>id</code> is not a recognizable
     *        <code>ResourceAdaptorID</code> object for the SLEE or it does not correspond
     *        with a resource adaptor installed in the SLEE.
     * @throws ManagementException if the configuration properties could not be obtained
     *        due to a system-level failure.
     */
    public ConfigProperties getConfigurationProperties(ResourceAdaptorID id)
        throws NullPointerException, UnrecognizedResourceAdaptorException, ManagementException;

    /**
     * Get the configuration properties for a resource adaptor entity.
     * @param entityName the name of the resource adaptor entity.
     * @return a <code>ConfigProperties</code> object containing the configuration properties of
     *        the resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws ManagementException if the configuration properties could not be obtained
     *        due to a system-level failure.
     */
    public ConfigProperties getConfigurationProperties(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException;

    /**
     * Create a resource adaptor entity using the specified configuration properties.  The
     * resource adaptor entity is initialized and enters the {@link ResourceAdaptorEntityState#INACTIVE}
     * state before this method returns.
     * <p>
     * Properties are specified using a <code>ConfigProperties</code> object.  Properties for the
     * resource adaptor that the resource adaptor entity is to be created from can be obtained
     * using the {@link #getConfigurationProperties(ResourceAdaptorID) getConfigurationProperties}
     * method.  The management client must ensure that all properties included in the
     * <code>ConfigProperties</code> object have a non-null value before passing it to this method.
     * The {@link javax.slee.resource.ResourceAdaptor#raVerifyConfiguration raVerifyConfiguration}
     * method is invoked on a resource adaptor object to test validity of the configuration
     * properties for the Resource Adaptor.
     * @param id the identifier of the resource adaptor the resource adaptor entity
     *        should be constructed from.
     * @param entityName the name of the resource adaptor entity to create.
     * @param properties the configuration properties for the resource adaptor entity.
     * @throws NullPointerException if any argument is <code>null</code>.
     * @throws InvalidArgumentException if <code>entityName</code> is zero-length.
     * @throws UnrecognizedResourceAdaptorException if <code>id</code> is not a recognizable
     *        <code>ResourceAdaptorID</code> object for the SLEE or it does not correspond
     *        with a resource adaptor installed in the SLEE.
     * @throws ResourceAdaptorEntityAlreadyExistsException if a resource adaptor entity with
     *        the given name already exists.
     * @throws InvalidConfigurationException if one or more of the configuration properties
     *        has a <code>null</code> value, or if the configuration properties were not valid
     *        for the Resource Adaptor as determined by the
     *        {@link javax.slee.resource.ResourceAdaptor#raVerifyConfiguration raVerifyConfiguration}
     *        method.
     * @throws ManagementException if the resource adaptor entity could not be created
     *        due to a system-level failure.
     */
    public void createResourceAdaptorEntity(ResourceAdaptorID id, String entityName, ConfigProperties properties)
        throws NullPointerException, InvalidArgumentException, UnrecognizedResourceAdaptorException,
               ResourceAdaptorEntityAlreadyExistsException, InvalidConfigurationException, ManagementException;

    /**
     * Remove a resource adaptor entity.  The resource adaptor entity must be in the
     * {@link ResourceAdaptorEntityState#INACTIVE} state.
     * @param entityName the name of the resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws InvalidStateException if the resource adaptor entity is not in the
     *        {@link javax.slee.management.ResourceAdaptorEntityState#INACTIVE} state.
     * @throws DependencyException if the resource adaptor entity is bound to one or
     *        more link names.
     * @throws ManagementException if the resource adaptor entity could not be removed
     *        due to a system-level failure.
     */
    public void removeResourceAdaptorEntity(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException,
               InvalidStateException, DependencyException, ManagementException;

    /**
     * Reconfigure a resource adaptor entity with new configuration properties. Properties
     * are specified using a <code>ConfigProperties</code> object.
     * 
     * <p> 
     * Only properties that need updating need to be included in the <code>ConfigProperties</code> object.
     * <p>
     *   
     * The management client must ensure that all properties included in the <code>ConfigProperties</code>
     * object have a non-null value before passing it to this method.
     * <p>
     * A Resource Adaptor may elect to support reconfiguration when resource adaptor objects
     * representing its resource adaptor entities are active using the
     * <tt>supports-active-reconfiguration</tt> attribute of the
     * <tt>&lt;resource-adaptor-class&gt;</tt> deployment descriptor element. If the value
     * of the <tt>supports-active-reconfiguration</tt> attribute is <tt>False</tt>,
     * this method may only be invoked to reconfigure a resource adaptor entity when it is
     * in the Inactive state, or when the SLEE is in the Stopped state.
     * If the value of the <tt>supports-active-reconfiguration</tt> attribute is <tt>True</tt>,
     * then a resource adaptor entity may be reconfigured when it, and the SLEE, are in any
     * state, i.e. reconfiguration is possible while the resource adaptor entity is creating
     * activities and firing events in the SLEE.
     * <p>
     * The {@link javax.slee.resource.ResourceAdaptor#raVerifyConfiguration raVerifyConfiguration}
     * method is invoked on a resource adaptor object to test validity of configuration
     * properties specified as an argument to this method.
     * @param entityName the name of the resource adaptor entity.
     * @param properties the values of configuration properties to be updated for the resource adaptor entity.
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *        does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws InvalidStateException if the Resource Adaptor's deployment descriptor
     *        specified that it did not support active reconfiguration and either the
     *        resource adaptor entity is not in the Inactive state or the SLEE is not in
     *        the Stopped state.
     * @throws InvalidConfigurationException if one or more of the configuration properties
     *        has a <code>null</code> value, or if the configuration properties were not valid
     *        for the Resource Adaptor as determined by the
     *        {@link javax.slee.resource.ResourceAdaptor#raVerifyConfiguration raVerifyConfiguration}
     *        method.
     * @throws ManagementException if the resource adaptor entity configuration properties
     *        could not be updated due to a system-level failure.
     */
    public void updateConfigurationProperties(String entityName, ConfigProperties properties)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException,
               InvalidStateException, InvalidConfigurationException,
               ManagementException;

    /**
     * Get the resource adaptor component identifier from which a resource adaptor entity was
     * created.
     * @param entityName the name of the resource adaptor entity.
     * @return the identifier of the resource adaptor component the resource adaptor entity
     *        was created from.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws ManagementException if the resource adaptor component identifier could not
     *        be obtained due to a system-level failure.
     */
    public ResourceAdaptorID getResourceAdaptor(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException;

    /**
     * Activate a resource adaptor entity.  The resource adaptor entity must be in the
     * {@link ResourceAdaptorEntityState#INACTIVE} state, and transitions to the
     * {@link ResourceAdaptorEntityState#ACTIVE} state during this method invocation.
     * @param entityName the name of the resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws InvalidStateException if the resource adaptor entity is not in the
     *        {@link ResourceAdaptorEntityState#INACTIVE} state.
     * @throws ManagementException if the resource adaptor entity could not be activated
     *        due to a system-level failure.
     */
    public void activateResourceAdaptorEntity(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException,
               InvalidStateException, ManagementException;

    /**
     * Deactivate a resource adaptor entity.  The resource adaptor entity must be in the
     * {@link ResourceAdaptorEntityState#ACTIVE} state, and transitions to the
     * {@link ResourceAdaptorEntityState#STOPPING} state during this method invocation.
     * The resource adaptor entity spontaneously returns to the
     * {@link ResourceAdaptorEntityState#INACTIVE} state once all activities created by the
     * resource adaptor entity have ended.
     * @param entityName the name of the resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws InvalidStateException if the resource adaptor entity is not in the
     *        {@link javax.slee.management.ResourceAdaptorEntityState#ACTIVE} state.
     * @throws ManagementException if the resource adaptor entity could not be deactivated
     *        due to a system-level failure.
     */
    public void deactivateResourceAdaptorEntity(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException,
               InvalidStateException, ManagementException;

    /**
     * Get the current state of a resource adaptor entity.
     * @param entityName the name of the resource adaptor entity.
     * @return the current state of the resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws ManagementException if the state of the resource adaptor entity could not
     *        be obtained due to a system-level failure.
     */
    public ResourceAdaptorEntityState getState(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException;

    /**
     * Get the set of all resource adaptor entities that have been created in the SLEE.
     * @return an array of resource adaptor entity identifiers.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public String[] getResourceAdaptorEntities()
        throws ManagementException;

    /**
     * Get the set of resource adaptor entities that have been created from a specified
     * resource adaptor component.
     * @param id the identifier of the resource adaptor.
     * @return an array of strings identifying the resource adaptor entites that have been
     *        created from the resource adaptor definition.
     * @throws NullPointerException if <code>id</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorException if <code>id</code> is not a recognizable
     *        <code>ResourceAdaptorID</code> object for the SLEE or it does not correspond
     *        with a resource adaptor installed in the SLEE.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public String[] getResourceAdaptorEntities(ResourceAdaptorID id)
        throws NullPointerException, UnrecognizedResourceAdaptorException, ManagementException;

    /**
     * Get the set of resource adaptor entities that are in a particular state.
     * @param state the required state.
     * @return an array of strings identifying the resource adaptor entities that are
     *        in the specified state.
     * @throws NullPointerException if <code>state</code> is <code>null</code>.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public String[] getResourceAdaptorEntities(ResourceAdaptorEntityState state)
        throws NullPointerException, ManagementException;

    /**
     * Bind a resource adaptor entity to a link name.
     * <p>
     * Link names are used to establish the bindings between SBBs and resource adaptor
     * entities.  If an SBB requires a resource adaptor entity of a particular resource
     * adaptor type to be bound into its JNDI environment, it specifies the required
     * resource adaptor type and a link name in its deployment descriptor.  A resource
     * adaptor entity of the correct resource adaptor type must be bound to that link
     * name before the SBB can be deployed.
     * <p>
     * Only resource adaptor entities of resource adaptor types that define a resource
     * adaptor interface may be bound to link names.
     * @param entityName the name of the resource adaptor entity.
     * @param linkName the link name.  The name must be unique within the scope of the SLEE. 
     * @throws NullPointerException if either argument is <code>null</code>.
     * @throws InvalidArgumentException if <code>linkName</code> is zero-length, or if
     *        the resource adaptor type of the resource adaptor entity does not define
     *        a resource adaptor interface.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws LinkNameAlreadyBoundException if the link name has already be bound.
     * @throws ManagementException if the link name could not be bound due to a
     *        system-level failure.
     */
    public void bindLinkName(String entityName, String linkName)
        throws NullPointerException, InvalidArgumentException, UnrecognizedResourceAdaptorEntityException,
               LinkNameAlreadyBoundException, ManagementException;

    /**
     * Remove a link name binding from a resource adaptor entity.
     * @param linkName the link name.
     * @throws NullPointerException if <code>linkName</code> is <code>null</code>.
     * @throws UnrecognizedLinkNameException if <code>linkName</code> has not been bound
     *        to a resource adaptor entity.
     * @throws DependencyException if the link name is in use by one or more deployed SBBs.
     * @throws ManagementException if the link name could not be unbound due to a
     *        system-level failure.
     */
    public void unbindLinkName(String linkName)
        throws NullPointerException, UnrecognizedLinkNameException, DependencyException,
               ManagementException;

    /**
     * Get the set of link names that have been bound to resource adaptor entities.
     * @return an array of link names.
     * @throws ManagementException if the link names could not be obtained due to a
     *        system-level failure.
     */
    public String[] getLinkNames()
        throws ManagementException;

    /**
     * Get the set of link names that a particular resource adaptor entity has been
     * bound to.
     * @param entityName the name of the resource adaptor entity.
     * @return an array of link names identifying the link names that the resource adaptor
     *        entity is bound to.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *         does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws ManagementException if the link names could not be obtained due to a
     *        system-level failure.
     */
    public String[] getLinkNames(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException, ManagementException;

    /**
     * Get the set of SBB component identifiers that identify the SBBs that are bound
     * to the specified link name by way of a <code>resource-adaptor-entity-link</code>
     * element in their deployment descriptor.
     * @param linkName the link name.
     * @return an array of SBB component identifiers identifying the SBBs that are bound
     *        to the link name.
     * @throws NullPointerException if <code>linkName</code> is <code>null</code>.
     * @throws UnrecognizedLinkNameException if <code>linkName</code> has not been bound
     *        to a resource adaptor entity.
     * @throws ManagementException if the SBB component identifiers could not be obtained
     *        due to a system-level failure.
     */
    public SbbID[] getBoundSbbs(String linkName)
        throws NullPointerException, UnrecognizedLinkNameException, ManagementException;

    /**
     * Get the resource adaptor entity that a link name is bound to.
     * @param linkName the link name.
     * @return the identifier of the resource adaptor entity that is bound to the link name.
     * @throws NullPointerException if <code>linkName</code> is <code>null</code>.
     * @throws UnrecognizedLinkNameException if <code>linkName</code> has not been bound
     *        to a resource adaptor entity.
     * @throws ManagementException if the identifier could not be obtained due to a
     *        system-level failure.
     */
    public String getResourceAdaptorEntity(String linkName)
        throws NullPointerException, UnrecognizedLinkNameException, ManagementException;

    /**
     * Get an array of resource adaptor entity names which are bound to a corresponding
     * array of link names.
     * @param linkNames an array of link names.
     * @return an array of resource adaptor entity identifiers.  This array will be the same
     *        length as the supplied array, and if <code>entities == getResourceAdaptorEntities(linkNames)</code>
     *        then entities[i] == getResourceAdaptorEntity(linkNames[i]). Any unrecognized
     *        link name present in <code>linkNames</code> results in a <code>null</code> value
     *        at the corresponding array index in this array.
     * @throws NullPointerException if <code>linkNames</code> is <code>null</code>.
     * @throws ManagementException if the identifiers could not be obtained due to a
     *        system-level failure.
     */
    public String[] getResourceAdaptorEntities(String[] linkNames)
        throws NullPointerException, ManagementException;

    /**
     * Get the JMX Object Name of a {@link ResourceUsageMBean} object for a resource
     * adaptor entity.
     * <p>
     * The JMX Object name of the Resource Usage MBean is composed of at least:
     * <ul>
     *   <li>the {@link ResourceUsageMBean#BASE_OBJECT_NAME base name} which specifies the
     *       domain and type of the MBean
     *   <li>the {@link ResourceUsageMBean#RESOURCE_ADAPTOR_ENTITY_NAME_KEY} property,
     *       with a value equal to <code>entityName</code>
     * </ul>
     * @param entityName the name of the resource adaptor entity.
     * @return the Object Name of a <code>ResourceUsageMBean</code> object for the
     *        specified resource adaptor entity.
     * @throws NullPointerException if <code>entityName</code> is <code>null</code>.
     * @throws UnrecognizedResourceAdaptorEntityException if <code>entityName</code>
     *        does not correspond with a resource adaptor entity existing in the SLEE.
     * @throws InvalidArgumentException if the resource adaptor component that the
     *        specified resource adaptor entity was created from does not define a usage
     *        parameters interface.
     * @throws ManagementException if the Object Name could not be obtained due to a
     *        system-level failure.
     */
    public ObjectName getResourceUsageMBean(String entityName)
        throws NullPointerException, UnrecognizedResourceAdaptorEntityException,
               InvalidArgumentException, ManagementException;
}
