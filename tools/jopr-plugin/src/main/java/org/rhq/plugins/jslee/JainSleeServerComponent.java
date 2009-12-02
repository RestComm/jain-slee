package org.rhq.plugins.jslee;

import java.util.Date;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.slee.management.SleeManagementMBean;
import javax.slee.management.SleeState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean;
import org.mobicents.slee.runtime.activity.ActivityContextHandle;
import org.rhq.core.domain.configuration.Configuration;
import org.rhq.core.domain.configuration.PropertyList;
import org.rhq.core.domain.configuration.PropertyMap;
import org.rhq.core.domain.configuration.PropertySimple;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataTrait;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.InvalidPluginConfigurationException;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.core.pluginapi.measurement.MeasurementFacet;
import org.rhq.core.pluginapi.operation.OperationFacet;
import org.rhq.core.pluginapi.operation.OperationResult;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

public class JainSleeServerComponent implements JainSleeServerUtils, MeasurementFacet, OperationFacet {
	private final Log log = LogFactory.getLog(this.getClass());

	volatile MBeanServerUtils mBeanServerUtils = null;

	volatile private SleeState sleeState = SleeState.STOPPED;

	public MBeanServerUtils getMBeanServerUtils() {
		return this.mBeanServerUtils;
	}

	public void start(ResourceContext resourceContext) throws InvalidPluginConfigurationException, Exception {
		log.info("start called");

		Configuration pluginConfig = resourceContext.getPluginConfiguration();

		String namingURL = pluginConfig.getSimple("namingURL").getStringValue();
		String principal = pluginConfig.getSimple("principal").getStringValue();
		String credentials = pluginConfig.getSimple("credentials").getStringValue();

		this.mBeanServerUtils = new MBeanServerUtils(namingURL);

	}

	public void stop() {
		// TODO Auto-generated method stub

	}

	public AvailabilityType getAvailability() {
		log.info("getAvailability called " + this.mBeanServerUtils);
		if (this.mBeanServerUtils != null) {

			try {
				ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);
				MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
				this.sleeState = (SleeState) connection.getAttribute(sleemanagement, "State");
				log.info("SleeState = " + this.sleeState);
				return AvailabilityType.UP;

			} catch (Exception e) {
				log.error("Something terribly wrong here ", e);
				return AvailabilityType.DOWN;
			}
		} else {
			log.error("Returning availability as DOWN");
			return AvailabilityType.DOWN;
		}
	}

	public void getValues(MeasurementReport report, Set<MeasurementScheduleRequest> metrics) throws Exception {
		log.info("getValues() called hurray");
		for (MeasurementScheduleRequest request : metrics) {
			if (request.getName().equals("state")) {
				report.addData(new MeasurementDataTrait(request, this.sleeState.toString()));
			}
		}
	}

	public OperationResult invokeOperation(String name, Configuration parameters) throws InterruptedException,
			Exception {
		log.info("invokeOperation() with name = " + name);
		OperationResult result = new OperationResult();

		if ("sleeState".equals(name)) {
			String message = null;
			String action = parameters.getSimple("action").getStringValue();
			ObjectName sleemanagement = new ObjectName(SleeManagementMBean.OBJECT_NAME);
			SleeManagementMBean sleeManagementMBean = (SleeManagementMBean) MBeanServerInvocationHandler
					.newProxyInstance(this.mBeanServerUtils.getConnection(), sleemanagement,
							javax.slee.management.SleeManagementMBean.class, false);

			if ("start".equals(action)) {
				sleeManagementMBean.start();
				message = "Successfully started Mobicents JSLEE Server";
			} else if ("stop".equals(action)) {
				sleeManagementMBean.stop();
				message = "Successfully stopped Mobicents JSLEE Server";
			} else if ("shutdown".equals(action)) {
				sleeManagementMBean.shutdown();
				message = "Successfully shutdown Mobicents JSLEE Server";
			}
			result.getComplexResults().put(new PropertySimple("result", message));
		} else if ("listActivityContexts".equals(name)) {
			MBeanServerConnection connection = this.mBeanServerUtils.getConnection();
			ObjectName actMana = new ObjectName("org.mobicents.slee:name=ActivityManagementMBean");

			ActivityManagementMBeanImplMBean aciManagMBean = (ActivityManagementMBeanImplMBean) MBeanServerInvocationHandler
					.newProxyInstance(connection, actMana,
							org.mobicents.slee.container.management.jmx.ActivityManagementMBeanImplMBean.class, false);

			Object[] activities = aciManagMBean.listActivityContexts(true);

			PropertyList columnList = new PropertyList("result");
			if (activities != null) {
				for (Object obj : activities) {
					Object[] tempObjects = (Object[]) obj;
					PropertyMap col = new PropertyMap("element");

					Object tempObj = tempObjects[0];
					PropertySimple activityHandle = new PropertySimple("ActivityHandle",
							tempObj != null ? ((ActivityContextHandle) tempObj).getActivityHandle().toString() : "-");

					col.put(activityHandle);

					col.put(new PropertySimple("Class", tempObjects[1]));

					tempObj = tempObjects[2];
					Date d = new Date(Long.parseLong((String) tempObj));
					col.put(new PropertySimple("LastAccessTime", d));

					tempObj = tempObjects[3];
					col.put(new PropertySimple("ResourceAdaptor", tempObj == null ? "-" : tempObj));

					tempObj = tempObjects[4];
					// PropertyList propertyList = new PropertyList("SbbAttachments");
					String[] strArr = (String[]) tempObj;
					StringBuffer sb = new StringBuffer();
					for (String s : strArr) {
						// PropertyMap SbbAttachment = new PropertyMap("SbbAttachment");
						// SbbAttachment.put(new PropertySimple("SbbAttachmentValue", s));
						// propertyList.add(SbbAttachment);
						sb.append(s).append("<br/>");
					}
					col.put(new PropertySimple("SbbAttachmentValue", sb.toString()));

					tempObj = tempObjects[5];
					// propertyList = new PropertyList("NamesBoundTo");
					sb = new StringBuffer();
					strArr = (String[]) tempObj;
					for (String s : strArr) {
						// PropertyMap NameBoundTo = new PropertyMap("NameBoundTo");
						// NameBoundTo.put(new PropertySimple("NameBoundToValue", s));
						// propertyList.add(NameBoundTo);
						sb.append(s).append("<br/>");
					}
					col.put(new PropertySimple("NameBoundToValue", sb.toString()));

					tempObj = tempObjects[6];
					// propertyList = new PropertyList("Timers");
					sb = new StringBuffer();
					strArr = (String[]) tempObj;
					for (String s : strArr) {
						// PropertyMap Timer = new PropertyMap("Timer");
						// Timer.put(new PropertySimple("TimerValue", s));
						// propertyList.add(Timer);
						sb.append(s).append("<br/>");
					}
					col.put(new PropertySimple("TimerValue", sb.toString()));

					tempObj = tempObjects[7];
					// propertyList = new PropertyList("DataProperties");
					sb = new StringBuffer();
					strArr = (String[]) tempObj;
					for (String s : strArr) {
						// PropertyMap DataProperty = new PropertyMap("DataProperty");
						// DataProperty.put(new PropertySimple("DataPropertyValue", s));
						// propertyList.add(DataProperty);
						sb.append(s).append("<br/>");
					}
					col.put(new PropertySimple("DataPropertyValue", sb.toString()));

					columnList.add(col);

				}
			}
			result.getComplexResults().put(columnList);
		} else {
			throw new UnsupportedOperationException("Operation [" + name + "] is not supported yet.");
		}

		return result;
	}

}
