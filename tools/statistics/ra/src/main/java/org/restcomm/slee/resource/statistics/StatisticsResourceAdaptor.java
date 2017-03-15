/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.restcomm.slee.resource.statistics;


import javax.management.ObjectName;
import javax.slee.Address;
import javax.slee.InvalidArgumentException;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.management.UnrecognizedResourceAdaptorEntityException;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.resource.SleeEndpoint;

import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.management.ResourceManagement;

import org.restcomm.commons.statistics.reporter.RestcommStatsReporter;
import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class StatisticsResourceAdaptor implements ResourceAdaptor {
	private transient Tracer tracer;

	private ResourceAdaptorContext raContext;
	private SleeContainer sleeContainer;
	private ResourceManagement resourceManagement;

	public StatisticsResourceAdaptor() { }

	public ResourceAdaptorContext getResourceAdaptorContext() {
		return raContext;
	}

	// Restcomm Statistics
	protected static final String STATISTICS_SERVER = "statistics.server";
	protected static final String DEFAULT_STATISTICS_SERVER = "https://statistics.restcomm.com/rest/";

	private RestcommStatsReporter statsReporter = new RestcommStatsReporter();
	private MetricRegistry metrics = RestcommStatsReporter.getMetricRegistry();
	// define metric name
	private Counter counterCalls = metrics.counter("calls");
	private Counter counterSeconds = metrics.counter("seconds");
	private Counter counterMessages = metrics.counter("messages");

	// lifecycle methods

	public void setResourceAdaptorContext(ResourceAdaptorContext raContext) {
		this.raContext = raContext;
		this.tracer = raContext.getTracer(StatisticsResourceAdaptor.class.getSimpleName());

		this.sleeContainer = SleeContainer.lookupFromJndi();
		if (this.sleeContainer != null) {
			this.resourceManagement = sleeContainer.getResourceManagement();
		}
	}

	private class StatisticsTimerTask extends TimerTask {

		@Override
		public void run() {

			if (resourceManagement == null) {
				return;
			}

			for (String raEntity: resourceManagement.getResourceAdaptorEntities()) {
				if (tracer.isFineEnabled()) {
					tracer.fine("RA Entity: " + raEntity);
				}

				try {
					ObjectName usageMBeanName = resourceManagement.getResourceUsageMBean(raEntity);

					// null for default set
					String usageParameterSetName = null; // "statisitcs";
					Object usageParameterSet = ManagementFactory.getPlatformMBeanServer()
							.invoke(usageMBeanName, "getInstalledUsageParameterSet",
									new Object[] {usageParameterSetName},
									new String[] {String.class.getName()});

					if (usageParameterSet != null) {
						try {
							Method method = usageParameterSet.getClass()
									.getMethod("getParameter", String.class, boolean.class);

							// get and reset
							Long calls = (Long) method.invoke(usageParameterSet, "calls", true);
							if (tracer.isFineEnabled()) {
								tracer.fine("calls: " + calls);
							}
							counterCalls.inc(calls);

							Long messages = (Long) method.invoke(usageParameterSet, "messages", true);
							if (tracer.isFineEnabled()) {
								tracer.fine("messages: " + messages);
							}
							counterMessages.inc(messages);

							Long seconds = (Long) method.invoke(usageParameterSet, "seconds", true);
							if (tracer.isFineEnabled()) {
								tracer.fine("seconds: " + seconds);
							}
							counterSeconds.inc(seconds);

						} catch (Exception e) {
							if (tracer.isWarningEnabled()) {
								tracer.warning("Cant get Usage parameter value", e);
							}
						}
					}

					//ManagementFactory.getPlatformMBeanServer()
					//		.invoke(usageMBeanName, "resetAllUsageParameters",
					//				null, null);

				} catch (UnrecognizedResourceAdaptorEntityException e) {
					// TODO
				} catch (InvalidArgumentException e) {
					// TODO
				} catch (Exception e) {
					// TODO
				}
			} // end of for

			// TODO: check counters?
			//if (calls != 0 || messages != 0 || seconds != 0) {
			if (statsReporter != null) {
				statsReporter.report();
			}
			//}
		}
	}

	public void raConfigure(ConfigProperties properties) {
	}

	public void raActive() {
		if (statsReporter == null) {
			statsReporter = new RestcommStatsReporter();
		}

		String statisticsServer = Version.getVersionProperty(STATISTICS_SERVER);
		if (statisticsServer == null || !statisticsServer.contains("http")) {
			statisticsServer = DEFAULT_STATISTICS_SERVER;
		}

		if (tracer.isFineEnabled()) {
			tracer.fine("statisticsServer: " + statisticsServer);
		}

		//define remote server address (optionally)
		statsReporter.setRemoteServer(statisticsServer);
		String projectName = System.getProperty("RestcommProjectName", "jainslee");
		String projectType = System.getProperty("RestcommProjectType", "community");
		String projectVersion = System.getProperty("RestcommProjectVersion",
				Version.getVersionProperty(Version.RELEASE_VERSION));

		if (tracer.isFineEnabled()) {
			tracer.fine("Restcomm Stats " + projectName + " " + projectType + " " + projectVersion);
		}

		statsReporter.setProjectName(projectName);
		statsReporter.setProjectType(projectType);
		statsReporter.setVersion(projectVersion);

		Version.printVersion();

		// TODO: define periodicity - now to once a minute (for testing)
		//define periodicity - default to once a day
		//statsReporter.start(86400, TimeUnit.SECONDS);
		raContext.getTimer().schedule(new StatisticsTimerTask(), 0, 30 * 1000);
	}

	public void raStopping() { }

	public void raInactive() {
		statsReporter.stop();
		statsReporter = null;
	}

	public void raUnconfigure() { }

	public void unsetResourceAdaptorContext() {
		raContext = null;
		tracer = null;

		sleeContainer = null;
		resourceManagement = null;
	}

	// config management methods
	public void raVerifyConfiguration(ConfigProperties properties)
			throws javax.slee.resource.InvalidConfigurationException {
	}

	public void raConfigurationUpdate(ConfigProperties properties) {
		throw new UnsupportedOperationException();
	}


	// event filtering methods
	public void serviceActive(ReceivableService service) {
	}

	public void serviceStopping(ReceivableService service) {
	}

	public void serviceInactive(ReceivableService service) {
	}


	// mandatory callbacks
	public void administrativeRemove(ActivityHandle handle) {
	}

	public Object getActivity(ActivityHandle activityHandle) {
		return null;
	}

	public ActivityHandle getActivityHandle(Object activity) {
		return null;
	}

	// optional call-backs
	public void activityEnded(ActivityHandle handle) {
	}

	public void activityUnreferenced(ActivityHandle activityHandle) {
	}

	public void eventProcessingFailed(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5, FailureReason arg6) {
	}

	public void eventProcessingSuccessful(ActivityHandle arg0,
			FireableEventType arg1, Object arg2, Address arg3,
			ReceivableService arg4, int arg5) {
	}

	public void eventUnreferenced(ActivityHandle arg0, FireableEventType arg1,
			Object event, Address arg3, ReceivableService arg4, int arg5) {
	}

	public void queryLiveness(ActivityHandle activityHandle) {
	}

	// interface accessors
	public Object getResourceAdaptorInterface(String arg0) {
		return null;
	}

	public Marshaler getMarshaler() {
		return null;
	}

	// ra logic
}
