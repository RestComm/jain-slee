package org.rhq.plugins.jslee;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.ComponentID;
import javax.slee.management.DeployableUnitDescriptor;
import javax.slee.management.DeployableUnitID;
import javax.slee.management.DeploymentMBean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.pluginapi.inventory.DeleteResourceFacet;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceComponent;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class DeployableUnitComponent implements ResourceComponent<JainSleeServerComponent>, OperationFacet,
		DeleteResourceFacet {
	private final Log log = LogFactory.getLog(this.getClass());

	private ResourceContext<JainSleeServerComponent> resourceContext;
	private DeployableUnitID deployableUnitID = null;
	private MBeanServerUtils mbeanUtils = null;

	private ObjectName deploymentObjName;

	public void start(ResourceContext<JainSleeServerComponent> context) throws InvalidPluginConfigurationException,
			Exception {
		log.info("DeployableUnitComponent.start");

		this.resourceContext = context;
		this.deploymentObjName = new ObjectName(DeploymentMBean.OBJECT_NAME);

		this.mbeanUtils = context.getParentResourceComponent().getMBeanServerUtils();

		String url = this.resourceContext.getPluginConfiguration().getSimple("url").getStringValue();
		this.deployableUnitID = new DeployableUnitID(url);
	}

	public void stop() {
		log.info("DeployableUnitComponent.stop");
	}

	public AvailabilityType getAvailability() {
		log.info("getAvailability");
		try {
			MBeanServerConnection connection = this.mbeanUtils.getConnection();
			DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(
					connection, this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

			DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(this.deployableUnitID);

		} catch (Exception e) {
			log.error("getAvailability failed for DeployableUnitID = " + this.deployableUnitID);

			return AvailabilityType.DOWN;
		}

		return AvailabilityType.UP;
	}

	public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException,
			Exception {
		log.info("DeployableUnitComponent.invokeOperation() with name = " + name);

		OperationResult result = new OperationResult();
		if ("listComponents".equals(name)) {
			MBeanServerConnection connection = this.mbeanUtils.getConnection();
			DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(
					connection, this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);

			DeployableUnitDescriptor deployableUnitDescriptor = deploymentMBean.getDescriptor(this.deployableUnitID);

			ComponentID[] components = deployableUnitDescriptor.getComponents();

			PropertyList columnList = new PropertyList("result");

			for (ComponentID componentID : components) {
				PropertyMap col = new PropertyMap("element");

				col.put(new PropertySimple("Name", componentID.getName()));
				col.put(new PropertySimple("Vendor", componentID.getVendor()));
				col.put(new PropertySimple("Version", componentID.getVersion()));

				columnList.add(col);

			}

			result.getComplexResults().put(columnList);
		} else {
			throw new UnsupportedOperationException("Operation [" + name + "] is not supported yet.");
		}

		return result;
	}

	public void deleteResource() throws Exception {
		MBeanServerConnection connection = this.mbeanUtils.getConnection();
		DeploymentMBean deploymentMBean = (DeploymentMBean) MBeanServerInvocationHandler.newProxyInstance(connection,
				this.deploymentObjName, javax.slee.management.DeploymentMBean.class, false);
		deploymentMBean.uninstall(this.deployableUnitID);

	}

}
