package org.mobicents.slee.container.management.jmx;

import java.util.Map;

import javax.slee.management.ManagementException;

import org.jboss.system.ServiceMBean;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;

/**
 * This is MBean interface that provides outside world with information about
 * activities. It also provides management methods to:
 * <ul>
 * <li>end null activities</li>
 * <li>modify time between automatic activities liveliness query</li>
 * <li>issue request for liveliness querry</li>
 * </ul>
 * 
 * It also define multiple static values that desciibe structure of activity
 * context representation. Representation of AC is array. Those values are: <br>
 * <ul>
 * <li>{@link #AC_ID} </li>
 * <li>{@link #ACTIVITY_CLASS} </li>
 * <li>{@link #LAST_ACCESS_TIME}</li>
 * <li>{@link #RA}</li>
 * <li>{@link #SBB_ATTACHMENTS}</li>
 * <li>{@link #NAMES_BOUND_TO}</li>
 * <li>{@link #TIMERS_ATTACHED}</li>
 * <li>{@link #DATA_PROPERTIES}</li>
 * <li>{@link #ARRAY_SIZE}</li>
 * </ul>
 * 
 * @author Bartosz Baranowski
 * @author Eduardo Martins
 */
public interface ActivityManagementMBeanImplMBean extends ServiceMBean
 {

	/**
	 * Defines index of String field in Object[], this String is representation
	 * of activity context ID field
	 */
	public final static int AC_ID = 0;

	/**
	 * Defines index of String field in Object[], this field contains fully
	 * qualified name of activity class that this ac activity context
	 * represents, as getClass().getName() woudl return.
	 */
	public final static int ACTIVITY_CLASS = 1;

	/**
	 * Defines index of String field in Object[], this field contains String
	 * represenation of number in <b>long</b> format. This number is timestamp
	 * of last access to activity context.
	 */
	public final static int LAST_ACCESS_TIME = 2;

	/**
	 * Defines index of Stirng field in Object[], this field represents RA
	 * entity name that created this activity context. <b>This can be null for
	 * activities like NullActivities, ServiceAtivities</b>
	 */
	public final static int RA = 3;

	/**
	 * Defines index of field in Object[]. If boolean flag <b>inDetails</b> is
	 * set to false ({@link #listActivityContexts(boolean)}) its value is
	 * String representation of number of attached sbbs. Otherwise it contains
	 * String[] which holds in its cells Sbb Entities IDS of sbb that are
	 * attached to AC.
	 */
	public final static int SBB_ATTACHMENTS = 4;

	/**
	 * Defines index of field in Object[]. If boolean flag <b>inDetails</b> is
	 * set to false ({@link #listActivityContexts(boolean)}) its value
	 * represents (as String) number of names given to this ac. Otherwise it
	 * contains String[] which holds names given to this ac
	 */
	public final static int NAMES_BOUND_TO = 5;

	/**
	 * Defines index of field in Object[]. If boolean flag <b>inDetails</b> is
	 * set to false ({@link #listActivityContexts(boolean)}) its value is
	 * String representation of number of timers attached. Otherwise it contains
	 * String[] which holds TimerIDs of attached timems.
	 */
	public final static int TIMERS_ATTACHED = 6;

	/**
	 * Defines index of field in Object[]. If boolean flag <b>inDetails</b> is
	 * set to false ({@link #listActivityContexts(boolean)}) its value is
	 * String representation of number of data properties set for this ac.
	 * Otherwise it contains String[] which holds data properties in its cells.<br>
	 * Format of a cell is [keyOfProperty=valueOfProperrty]
	 */
	public final static int DATA_PROPERTIES = 7;

	/**
	 * Defines ac array representation size, simply for ease modification - this
	 * value is defined in one place.
	 */
	public final static int ARRAY_SIZE = 8;

	// --- STATICS FOR LISTING TYPES

	/**
	 * Defines static value which indicates that all ac should be listed.
	 */
	public final static int LIST_BY_NO_CRITERIA = -1;

	/**
	 * Defines static value which indicates that only ac that should be listed
	 * are those which activity class name matches passed one.
	 */
	public final static int LIST_BY_ACTIVITY_CLASS = 0;

	/**
	 * Defines static value which indicates that only ac that shoudl be listed
	 * are those which raentity name matches passed one
	 */
	public final static int LIST_BY_RAENTITY = 1;

	/**
	 * Defines static value which indicates that only ac that should be listed
	 * are those which have attached sbb enityi with id matching one that has
	 * been passed
	 */
	public final static int LIST_BY_SBBENTITY = 2;

	/**
	 * Defines static value which indicates that only ac that should be listed
	 * are those which have attached sbbs with id matching one that has been
	 * passed
	 */
	public final static int LIST_BY_SBBID = 3;

	// ===== ATTRIBUTES =====

	/**
	 * Retreives the number of activity context in the container.
	 */
	public int getActivityContextCount();

	/**
	 * Retrieve the period between activity context liveness queries.
	 * 
	 * @return the period between activity context liveness queries.
	 */
	public long getQueryActivityContextLivelinessPeriod();

	/**
	 * Sets the period between activity context liveness queries. Its lower
	 * value should have boundry, after passing which MBean sets some default
	 * value.
	 * 
	 * @param new
	 *            value of period between liveliness queries
	 */
	public void setQueryActivityContextLivelinessPeriod(long set);

	/**
	 * Set max time an activity context can be idle. Its value indicates how
	 * long null ac can be idle before it will be removed. seting <b>"0"</b>
	 * turns of automatic null ac ending.
	 * 
	 * @param set
	 *            max time an activity context can be idle.
	 */
	public void setActivityContextMaxIdleTime(long set);

	/**
	 * Retreives max time an activity context can be idle
	 * 
	 * @return max time an activity context can be idle;
	 */
	public long getActivityContextMaxIdleTime();

	/**
	 * List all activity context with details.
	 * 
	 * @return report = Object[[{@link #retrieveActivityContextDetails()}],[{@link #retrieveActivityContextDetails()}][....]].
	 *         If boolean flag is set to <b>false</b> values with indexes:
	 *         <ul>
	 *         <li>{@link #SBB_ATTACHMENTS}</li>
	 *         <li>{@link #NAMES_BOUND_TO}</li>
	 *         <li>{@link #TIMERS_ATTACHED}</li>
	 *         <li>{@link #DATA_PROPERTIES}</li>
	 *         </ul>
	 *         will contain String represenation of integer number (which tells
	 *         how many of certain property there is - see description of
	 *         indexes) General description of return Object[] can be found here:
	 *         <b>http://groups.google.com/group/mobicents-public/web/mobicents-activity-context-mbean</b>
	 * @param if
	 *            <b>true</b> output will have list of all properties(lie sbb attachemtn set, etc.), not just
	 *            number of them.
	 */
	public Object[] listActivityContexts(boolean inDetails);

	/**
	 * Query activity liveness on all resource adaptors. Calls
	 * {@link javax.slee.resource.ResoourceAdaptor.queryLiveliness(javax.slee.resource.ActivityHandle)})
	 * for each RA activity present in container.
	 * 
	 */
	public void queryActivityContextLiveness();

	/**
	 * Tries to end null activity
	 * 
	 * @param nullACID -
	 *            activity context id of null activity to end.
	 * 
	 * @throws ManagementException -
	 *             when passed ID does not exist or AC that it identifies is not
	 *             null AC.
	 * 
	 */
	public void endActivity(ActivityContextHandle nullACID) throws ManagementException;

	/**
	 * 
	 * 
	 * 
	 * @return 
	 * 
	 */
	public String[] listActivityContextsFactories();

	/**
	 * Retieves list of Activity Context Handles related with specified Activity
	 * object class name
	 * 
	 * @param fullQualifiedActivityClassName -
	 *            like javax.sip.ServerTransaction
	 * @return
	 *            <ul>
	 *            <li>Object[] constaining Strings in each field representing
	 *            IDs of ACs</li>
	 *            <li>null - if no acs have been found for passed activity
	 *            class name</li>
	 *            </ul>
	 */
	public Object[] retrieveActivityContextIDByActivityType(
			String fullQualifiedActivityClassName);

	/**
	 * Retrieves list of Activity Context IDs related with specified Resource
	 * Adaptor entity name.
	 * 
	 * @param entityName
	 *            the entity name of the Resource Adaptor
	 * @return
	 *            <ul>
	 *            <li>Object[] constaining Strings in each field representing
	 *            IDs of ACs which have been created by ra entity with given
	 *            name</li>
	 *            <li>null - if no acs have been found for passed ra entity
	 *            name</li>
	 *            </ul>
	 */
	public Object[] retrieveActivityContextIDByResourceAdaptorEntityName(
			String entityName);

	/**
	 * Retrieves list of Activity Context IDs related with specified SBB ID
	 * 
	 * @param sbbID -
	 *            SbbID, it should look like "SbbName#SbbVendor#SbbVersion"
	 * @return
	 *            <ul>
	 *            <li>Object[] constaining Strings in each field representing
	 *            IDs of ACs which have attached sbbs with given SbbID</li>
	 *            <li>null - if no acs have been found for passed SbbID</li>
	 *            </ul>
	 */
	public Object[] retrieveActivityContextIDBySbbID(String sbbID);

	/**
	 * Retrieves list of Activity Context IDs related with specified SBB entity
	 * ID
	 * 
	 * @param sbbEID - sbb entity ID for which Activity contexts IDs will be returned
	 * @return
	 *            <ul>
	 *            <li>Object[] constaining Strings in each field representing
	 *            IDs of ACs which have attached sbb wiht passed SbbEID</li>
	 *            <li>null - if no acs have been found for passed Sbb Entity ID</li>
	 *            </ul>
	 */
	public Object[] retrieveActivityContextIDBySbbEntityID(String sbbEID);

	/**
	 * Retrieves details of the Activity Context with the specified ID. Lists
	 * Sbbs attached, bound names etc - same as list listActivitContexts(true)
	 * but for one, and returns it in array, which is more conveniant to parse
	 * that String with tree.
	 * 
	 * @return Object[] - its structure has been defined here <b>http://groups.google.com/group/mobicents-public/web/mobicents-activity-context-mbean?version=8</b>, also You can read static fields descritpiotn in MBean interface for this MbeanImpl
	 * @throws ManagementException
	 *             if ac is not found
	 */
	public Object[] retrieveActivityContextDetails(ActivityContextHandle ach)
			throws ManagementException;

	
	/**
	 * This method returns map containing name bindings of activity contexts.
	 * @return Map containing mappings name->ach
	 */
	public Map retrieveNamesToActivityContextIDMappings();
	
}
