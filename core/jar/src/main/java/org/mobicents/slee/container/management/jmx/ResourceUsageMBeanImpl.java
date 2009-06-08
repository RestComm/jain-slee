package org.mobicents.slee.container.management.jmx;

import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.slee.SLEEException;
import javax.slee.management.ManagementException;
import javax.slee.management.ProfileTableUsageMBean;
import javax.slee.management.ResourceAdaptorEntityNotification;
import javax.slee.management.ResourceUsageMBean;
import javax.slee.usage.UsageMBean;
import javax.slee.usage.UsageNotificationManagerMBean;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.ResourceAdaptorComponent;

/**
 * Implementation of the {@link ProfileTableUsageMBean} from SLEE 1.1 specs.
 * 
 * @author martins
 * 
 */
public class ResourceUsageMBeanImpl extends AbstractUsageMBeanImplParent
		implements ResourceUsageMBean {

	private static final long serialVersionUID = 2670146310843436229L;

	private static final transient Logger logger = Logger
			.getLogger(ResourceUsageMBeanImpl.class);

	/**
	 * the ra entity name for this mbean
	 */
	private String entityName;

	public ResourceUsageMBeanImpl(String entityName,
			ResourceAdaptorComponent component, SleeContainer sleeContainer)
			throws NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException {
		super(ResourceUsageMBean.class, component,
				new ResourceAdaptorEntityNotification(entityName),
				sleeContainer);
		this.entityName = entityName;
	}

	@Override
	protected ObjectName generateUsageNotificationManagerMBeanObjectName()
			throws MalformedObjectNameException, NullPointerException {

		String objectNameAsString = UsageNotificationManagerMBean.BASE_OBJECT_NAME
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE
				+ ','
				+ ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY
				+ '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}

	@Override
	protected ObjectName generateUsageParametersMBeanObjectName(String name)
			throws MalformedObjectNameException, NullPointerException {

		String objectNameAsString = UsageMBean.BASE_OBJECT_NAME
				+ (name != null ? "," + UsageMBean.USAGE_PARAMETER_SET_NAME_KEY
						+ '=' + ObjectName.quote(name) : "")
				+ ','
				+ UsageMBean.NOTIFICATION_SOURCE_KEY
				+ '='
				+ ResourceAdaptorEntityNotification.USAGE_NOTIFICATION_TYPE
				+ ','
				+ ResourceAdaptorEntityNotification.RESOURCE_ADAPTOR_ENTITY_NAME_KEY
				+ '=' + ObjectName.quote(entityName);
		return new ObjectName(objectNameAsString);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		try {
			return "Resource Entity " + entityName + " Usage MBean : " 
				+ "\n+-- Usage Parameter Sets: "	+ getUsageParameterSets();
		} catch (ManagementException e) {
			throw new SLEEException(e.getMessage(),e);
		}
	}
	
	public String getEntityName() throws ManagementException {
		return entityName;
	}
}
