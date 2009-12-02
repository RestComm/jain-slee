package org.rhq.plugins.jslee;

import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.management.ResourceManagementMBean;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.Property;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.domain.resource.CreateResourceStatus;
import org.rhq.core.pluginapi.configuration.ConfigurationFacet;
import org.rhq.core.pluginapi.configuration.ConfigurationUpdateReport;
import org.rhq.core.pluginapi.inventory.CreateChildResourceFacet;
import org.rhq.core.pluginapi.inventory.CreateResourceReport;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;
import org.rhq.plugins.jslee.utils.ResourceAdaptorUtils;

public class ResourceAdaptorComponent implements ResourceAdaptorUtils, MeasurementFacet, ConfigurationFacet,
		CreateChildResourceFacet {
	private final Log log = LogFactory.getLog(this.getClass());

	private ResourceContext<JainSleeServerComponent> resourceContext;
	private ResourceAdaptorID raId = null;
	private MBeanServerUtils mbeanUtils = null;

	private ConfigProperties configProperties = null;

	private ObjectName resourceManagement;

	public void start(ResourceContext context) throws InvalidPluginConfigurationException, Exception {
		log.info("start");

		this.resourceContext = context;
		this.resourceManagement = new ObjectName(ResourceManagementMBean.OBJECT_NAME);

		this.mbeanUtils = ((JainSleeServerUtils) context.getParentResourceComponent()).getMBeanServerUtils();

		String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
		String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
		String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

		this.raId = new ResourceAdaptorID(name, vendor, version);

	}

	public void stop() {
		log.info("stop");

	}

	public AvailabilityType getAvailability() {
		log.info("getAvailability");
		try {
			MBeanServerConnection connection = this.mbeanUtils.getConnection();
			ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
					.newProxyInstance(connection, this.resourceManagement,
							javax.slee.management.ResourceManagementMBean.class, false);

			configProperties = resourceManagementMBean.getConfigurationProperties(this.raId);

		} catch (Exception e) {
			log.error("getAvailability failed for ResourceAdaptorID = " + this.raId);

			return AvailabilityType.DOWN;
		}

		return AvailabilityType.UP;

	}

	public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
		log.info("ResourceAdaptorComponent.getValues");
		for (MeasurementScheduleRequest request : metrics) {
			if (request.getName().equals("entities")) {

				MBeanServerConnection connection = this.mbeanUtils.getConnection();
				ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
						.newProxyInstance(connection, this.resourceManagement,
								javax.slee.management.ResourceManagementMBean.class, false);

				String[] raEntities = resourceManagementMBean.getResourceAdaptorEntities(this.raId);
				report.addData(new MeasurementDataNumeric(request, (double) raEntities.length));
			} else if (request.getName().equals("ratype")) {
				report.addData(new MeasurementDataTrait(request, "blaaa#1.0#blaaaa"));
			}
		}
	}

	public ResourceAdaptorID getResourceAdaptorID() {
		return this.raId;
	}

	public MBeanServerUtils getMBeanServerUtils() {
		return this.mbeanUtils;
	}

	public Configuration loadResourceConfiguration() throws Exception {
		Configuration config = new Configuration();

		PropertyList columnList = new PropertyList("properties");
		for (ConfigProperties.Property confProp : this.configProperties.getProperties()) {
			PropertyMap col = new PropertyMap("propertyDefinition");

			col.put(new PropertySimple("propertyName", confProp.getName()));
			col.put(new PropertySimple("propertyType", confProp.getType()));
			col.put(new PropertySimple("propertyValue", confProp.getValue()));

			columnList.add(col);
		}

		config.put(columnList);

		return config;
	}

	public void updateResourceConfiguration(ConfigurationUpdateReport arg0) {
		// No update is allowed isnt it?

	}

	public CreateResourceReport createResource(CreateResourceReport report) {
		try {
			Configuration configuration = report.getResourceConfiguration();

			String entityName = configuration.getSimple("entityName").getStringValue();
			PropertyList columnList = configuration.getList("properties");

			ConfigProperties props = new ConfigProperties();

			for (Property c : columnList.getList()) {
				PropertyMap column = (PropertyMap) c;

				String propName = column.getSimple("propertyName").getStringValue();
				String propType = column.getSimple("propertyType").getStringValue();
				String propValue = column.getSimple("propertyValue").getStringValue();

				log.info("propertyType = " + propType + " propValue = " + propValue);

				Object value = null;
				if (propType.equals("java.lang.String")) {
					value = propValue;
				} else if (propType.equals("java.lang.Integer")) {
					value = Integer.valueOf(propValue);
				} else if (propType.equals("java.lang.Long")) {
					value = Long.valueOf(propValue);
				}
				ConfigProperties.Property p = new ConfigProperties.Property(propName, propType, value);
				props.addProperty(p);
			}

			MBeanServerConnection connection;

			connection = this.mbeanUtils.getConnection();

			ResourceManagementMBean resourceManagementMBean = (ResourceManagementMBean) MBeanServerInvocationHandler
					.newProxyInstance(connection, this.resourceManagement,
							javax.slee.management.ResourceManagementMBean.class, false);

			report.setResourceKey(entityName);
			resourceManagementMBean.createResourceAdaptorEntity(this.raId, entityName, props);

			report.setStatus(CreateResourceStatus.SUCCESS);
			report.setResourceName(entityName);

		} catch (Exception e) {
			log.error("Adding new ResourceAdaptor Entity failed ", e);
			report.setException(e);
			report.setStatus(CreateResourceStatus.FAILURE);

		}
		return report;
	}

}
