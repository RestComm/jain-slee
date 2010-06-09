package org.rhq.plugins.jslee;

import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.mobicents.slee.container.management.jmx.EventRouterConfigurationMBean;
import org.mobicents.slee.container.management.jmx.EventRouterStatisticsMBean;
import org.rhq.core.domain.measurement.AvailabilityType;
import org.rhq.core.domain.measurement.MeasurementDataNumeric;
import org.rhq.core.domain.measurement.MeasurementReport;
import org.rhq.core.domain.measurement.MeasurementScheduleRequest;
import org.rhq.core.pluginapi.inventory.ResourceContext;
import org.rhq.plugins.jmx.MBeanResourceComponent;
import org.rhq.plugins.jslee.utils.JainSleeServerUtils;
import org.rhq.plugins.jslee.utils.MBeanServerUtils;

/**
 * JAIN SLEE 2.x Event Router Statistics Component
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ExecutorComponent extends MBeanResourceComponent {

  private ResourceContext<JainSleeServerComponent> resourceContext;

  volatile MBeanServerUtils mbeanUtils = null;
  
  Integer executorId = null;

  public void start(ResourceContext context) {
    log.info("ExecutorComponent.start");

    this.resourceContext = context;

    this.executorId = this.resourceContext.getPluginConfiguration().getSimple("executorId").getIntegerValue();
    this.mbeanUtils = ((JainSleeServerUtils) context.getParentResourceComponent()).getMBeanServerUtils();    
  }

  public AvailabilityType getAvailability() {
    try {
      MBeanServerConnection connection = this.mbeanUtils.getConnection();
  
      ObjectName erConfigObjectName = new ObjectName("org.mobicents.slee:name=EventRouterConfiguration"/* FIXME */);
  
      EventRouterConfigurationMBean erConfigMBean = (EventRouterConfigurationMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
          erConfigObjectName, EventRouterConfigurationMBean.class, false);
  
      return this.executorId < erConfigMBean.getEventRouterThreads() ? AvailabilityType.UP : AvailabilityType.DOWN;
    }
    catch (Exception e) {
      log.debug("Failed to get Availability for Executor #" + this.executorId, e);
    }
    
    return AvailabilityType.DOWN;
  }

  public void getValues(MeasurementReport report, Set requests) {
    try {
      Set<MeasurementScheduleRequest> metrics = requests;
      
      EventRouterStatisticsMBean erStats = getEventRouterStatisticsMBean();
      
      for (MeasurementScheduleRequest request : metrics) {
        if (request.getName().equals("activitiesMapped")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getActivitiesMapped(executorId))));
        }
        else if (request.getName().equals("averageEventRoutingTime")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getAverageEventRoutingTime(executorId))));
        }
        else if (request.getName().equals("executedTasks")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getExecutedTasks(executorId))));
        }
        else if (request.getName().equals("executingTime")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getExecutingTime(executorId))));
        }
        else if (request.getName().equals("idleTime")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getIdleTime(executorId))));
        }
        else if (request.getName().equals("miscTasksExecuted")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getMiscTasksExecuted(executorId))));
        }
        else if (request.getName().equals("miscTasksExecutingTime")) {
          report.addData(new MeasurementDataNumeric(request, Double.valueOf(erStats.getMiscTasksExecutingTime(executorId))));
        }
      }
    }
    catch (Exception e) {
      log.debug("Failure gathering values for Executor #" + executorId, e);
    }
  }

  private EventRouterStatisticsMBean getEventRouterStatisticsMBean() throws Exception {
    MBeanServerConnection connection = this.mbeanUtils.getConnection();

    ObjectName erStatsObjectName = new ObjectName("org.mobicents.slee:name=EventRouterStatistics"/* FIXME */);
    EventRouterStatisticsMBean erStatsMBean = (EventRouterStatisticsMBean) MBeanServerInvocationHandler.newProxyInstance(connection, 
        erStatsObjectName, EventRouterStatisticsMBean.class, false);

    return erStatsMBean;
  }
}
