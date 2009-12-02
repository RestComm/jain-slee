package org.rhq.plugins.jslee;

import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.ServiceID;
import javax.slee.management.ServiceManagementMBean;
import javax.slee.management.ServiceState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class ServiceComponent implements ResourceComponent<JainSleeServerComponent>, MeasurementFacet, OperationFacet {
	private final Log log = LogFactory.getLog(this.getClass());

	private ResourceContext<JainSleeServerComponent> resourceContext;
	private ServiceID serviceId = null;
	private MBeanServerUtils mbeanUtils = null;

	private ObjectName servicemanagement;
	private ServiceState serviceState = ServiceState.INACTIVE;

	public void start(ResourceContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException,
			Exception {
		log.info("start");

		this.resourceContext = context;
		this.servicemanagement = new ObjectName(ServiceManagementMBean.OBJECT_NAME);

		this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

		String name = this.resourceContext.getPluginConfiguration().getSimple("name").getStringValue();
		String version = this.resourceContext.getPluginConfiguration().getSimple("version").getStringValue();
		String vendor = this.resourceContext.getPluginConfiguration().getSimple("vendor").getStringValue();

		serviceId = new ServiceID(name, vendor, version);

	}

	public void stop() {
		log.info("stop");

	}

	public AvailabilityType getAvailability() {
		log.info("getAvailability");
		try {
			MBeanServerConnection connection = this.mbeanUtils.getConnection();
			serviceState = (ServiceState) connection.invoke(this.servicemanagement, "getState",
					new Object[] { this.serviceId }, new String[] { ServiceID.class.getName() });
		} catch (Exception e) {
			log.error("getAvailability failed for ServiceID = " + this.serviceId);
			this.serviceState = ServiceState.INACTIVE;
			return AvailabilityType.DOWN;
		}

		return AvailabilityType.UP;

	}

	public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
		log.info("getValues");
		for (MeasurementScheduleRequest request : metrics) {
			if (request.getName().equals("state")) {
				report.addData(new MeasurementDataTrait(request, this.serviceState.toString()));
			}
		}

	}

	public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException,
			Exception {
		log.info("ServiceComponent.invokeOperation() with name = " + name);

		OperationResult result = new OperationResult();
		if ("changeServiceState".equals(name)) {
			String message = null;
			String action = parameters.getSimple("action").getStringValue();
			MBeanServerConnection connection = this.mbeanUtils.getConnection();
			ServiceManagementMBean serviceManagementMBean = (ServiceManagementMBean) MBeanServerInvocationHandler
					.newProxyInstance(connection, this.servicemanagement,
							javax.slee.management.ServiceManagementMBean.class, false);
			if ("activate".equals(action)) {
				serviceManagementMBean.activate(this.serviceId);
				message = "Successfully Activated Service " + this.serviceId;
			} else if ("deactivate".equals(action)) {
				serviceManagementMBean.deactivate(this.serviceId);
				message = "Successfully DeActivated Service " + this.serviceId;
			}

			result.getComplexResults().put(new PropertySimple("result", message));

		} else {
			throw new UnsupportedOperationException("Operation [" + name + "] is not supported yet.");
		}
		return null;
	}

}
