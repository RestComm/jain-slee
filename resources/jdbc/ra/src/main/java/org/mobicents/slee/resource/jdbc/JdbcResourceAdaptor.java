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

package org.mobicents.slee.resource.jdbc;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.Address;
import javax.slee.facilities.EventLookupFacility;
import javax.slee.facilities.Tracer;
import javax.slee.resource.ActivityFlags;
import javax.slee.resource.ActivityHandle;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.EventFlags;
import javax.slee.resource.FailureReason;
import javax.slee.resource.FireableEventType;
import javax.slee.resource.InvalidConfigurationException;
import javax.slee.resource.Marshaler;
import javax.slee.resource.ReceivableService;
import javax.slee.resource.ResourceAdaptor;
import javax.slee.resource.ResourceAdaptorContext;
import javax.slee.transaction.SleeTransactionManager;
import javax.sql.DataSource;

import org.mobicents.slee.resource.jdbc.event.JdbcTaskExecutionThrowableEvent;
import org.mobicents.slee.resource.jdbc.event.JdbcTaskExecutionThrowableEventImpl;
import org.mobicents.slee.resource.jdbc.task.JdbcTask;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskResult;
import org.mobicents.slee.resource.jdbc.task.simple.SimpleJdbcTaskResultEvent;

/**
 * The JDBC Resource Adaptor object.
 * 
 * @author martins
 * 
 */
public class JdbcResourceAdaptor implements ResourceAdaptor {

	/**
	 * the RA context, which gives
	 */
	private ResourceAdaptorContext context;

	private static final String DATASOURCE_JNDI_NAME_CONFIG_PROPERTY = "DATASOURCE_JNDI_NAME";
	private String datasourceName;

	private static final String EXECUTOR_SERVICE_THREADS_CONFIG_PROPERTY = "EXECUTOR_SERVICE_THREADS";
	private int executorServiceThreads;

	private static final String RA_SBB_INTERFACE_CONNECTION_GETTERS_ON_CONFIG_PROPERTY = "RA_SBB_INTERFACE_CONNECTION_GETTERS_ON";
	private boolean raSbbInterfaceConnectionGettersOn;

	/**
	 * the RA interface for SBBs
	 */
	private JdbcResourceAdaptorSbbInterfaceImpl sbbInterface = new JdbcResourceAdaptorSbbInterfaceImpl(
			this);

	/**
	 * the RA's Marshaller
	 */
	private JdbcResourceAdaptorMarshaller marshaller = new JdbcResourceAdaptorMarshaller(
			this);

	/**
	 * the RA "logger"
	 */
	private Tracer tracer;

	private DataSource datasource;

	private ExecutorService executorService;

	private FireableEventType simpleJdbcTaskResultEventType;
	private FireableEventType jdbcTaskExecutionThrowableEventType;

	private SleeTransactionManager txManager;
	private EventLookupFacility eventLookupFacility;

	// --- getters which expose RA functionalities, needed by other classes

	/**
	 * 
	 * @return
	 */
	public ResourceAdaptorContext getContext() {
		return context;
	}

	/**
	 * 
	 * @return
	 */
	public DataSource getDatasource() {
		return datasource;
	}

	/**
	 * 
	 * @return
	 */
	public Tracer getTracer() {
		return this.tracer;
	}

	/**
	 * 
	 * @return
	 */
	public SleeTransactionManager getTxManager() {
		return txManager;
	}

	// --- beginning of SLEE 1.1 RA contract ---

	@Override
	public void activityEnded(ActivityHandle activityHandle) {
		// NOT USED
	}

	@Override
	public void activityUnreferenced(ActivityHandle activityHandle) {
		// NOT USED
	}

	@Override
	public void administrativeRemove(ActivityHandle activityHandle) {
		// NOT USED
	}

	@Override
	public void eventProcessingFailed(ActivityHandle activityHandle,
			FireableEventType eventType, Object eventObject, Address address,
			ReceivableService service, int eventFlags, FailureReason reason) {
		// NOT USED
	}

	@Override
	public void eventProcessingSuccessful(ActivityHandle activityHandle,
			FireableEventType eventType, Object eventObject, Address address,
			ReceivableService service, int eventFlags) {
		// NOT USED
	}

	@Override
	public void eventUnreferenced(ActivityHandle activityHandle,
			FireableEventType eventType, Object eventObject, Address address,
			ReceivableService service, int eventFlags) {
		// not used
	}

	@Override
	public Object getActivity(ActivityHandle activityHandle) {
		return activityHandle;
	}

	@Override
	public ActivityHandle getActivityHandle(Object activityObject) {
		if (activityObject instanceof JdbcActivityImpl) {
			final JdbcActivityImpl jdbcActivity = (JdbcActivityImpl) activityObject;
			if (jdbcActivity.getRaEntityName().equals(
					getContext().getEntityName())) {
				return jdbcActivity;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public Marshaler getMarshaler() {
		return this.marshaller;
	}

	@Override
	public Object getResourceAdaptorInterface(String className) {
		return this.sbbInterface;
	}

	@Override
	public void queryLiveness(ActivityHandle activityHandle) {
		// NOT USED
	}

	@Override
	public void raActive() {
		try {
			this.datasource = (DataSource) InitialContext
					.doLookup(datasourceName);
		} catch (NamingException e) {
			tracer.severe("failed to retrieve the data source", e);
		}
		this.executorService = Executors
				.newFixedThreadPool(executorServiceThreads);
	}

	@Override
	public void raConfigurationUpdate(ConfigProperties configuration) {
		// NOT USED
	}

	@Override
	public void raConfigure(ConfigProperties configuration) {
		this.datasourceName = (String) configuration.getProperty(
				DATASOURCE_JNDI_NAME_CONFIG_PROPERTY).getValue();
		this.executorServiceThreads = (Integer) configuration.getProperty(
				EXECUTOR_SERVICE_THREADS_CONFIG_PROPERTY).getValue();
		this.raSbbInterfaceConnectionGettersOn = (Boolean) configuration
				.getProperty(
						RA_SBB_INTERFACE_CONNECTION_GETTERS_ON_CONFIG_PROPERTY)
				.getValue();
		this.sbbInterface
				.setRaSbbInterfaceConnectionGettersOn(raSbbInterfaceConnectionGettersOn);
	}

	@Override
	public void raInactive() {
		this.datasource = null;
		this.executorService = null;
	}

	@Override
	public void raStopping() {
		// NOT USED
	}

	@Override
	public void raUnconfigure() {
		this.datasourceName = null;
		this.executorServiceThreads = 0;
	}

	@Override
	public void raVerifyConfiguration(ConfigProperties properties)
			throws InvalidConfigurationException {
		String datasourceName = (String) properties.getProperty(
				"DATASOURCE_JNDI_NAME").getValue();
		try {
			InitialContext.doLookup(datasourceName);
		} catch (NamingException e) {
			throw new InvalidConfigurationException("bad datasource name", e);
		}
		Integer executorServiceThreads = (Integer) properties.getProperty(
				"EXECUTOR_SERVICE_THREADS").getValue();
		if (executorServiceThreads < 1) {
			throw new InvalidConfigurationException(
					"executor service threads must be a positive integer");
		}
	}

	@Override
	public void serviceActive(ReceivableService service) {
		// THE RA DOES NOT USES EVENT FILTERING
	}

	@Override
	public void serviceInactive(ReceivableService service) {
		// THE RA DOES NOT USES EVENT FILTERING
	}

	@Override
	public void serviceStopping(ReceivableService service) {
		// THE RA DOES NOT USES EVENT FILTERING
	}

	@Override
	public void setResourceAdaptorContext(ResourceAdaptorContext context) {
		this.context = context;
		this.tracer = context.getTracer(this.getClass().getSimpleName());
		this.txManager = context.getSleeTransactionManager();
		this.eventLookupFacility = context.getEventLookupFacility();
		try {
			this.simpleJdbcTaskResultEventType = eventLookupFacility
					.getFireableEventType(SimpleJdbcTaskResultEvent.EVENT_TYPE_ID);
			this.jdbcTaskExecutionThrowableEventType = eventLookupFacility
					.getFireableEventType(JdbcTaskExecutionThrowableEvent.EVENT_TYPE_ID);
		} catch (Throwable e) {
			tracer.severe("Failed to retrieve fireable event types", e);
		}
	}

	@Override
	public void unsetResourceAdaptorContext() {
		this.context = null;
		this.tracer = null;
		this.jdbcTaskExecutionThrowableEventType = null;
		this.simpleJdbcTaskResultEventType = null;
	}

	// --- end of SLEE 1.1 RA contract

	// --- activity management
	/**
	 * 
	 * @return
	 */
	JdbcActivityImpl createActivity() {
		JdbcActivityImpl activity = new JdbcActivityImpl(this, UUID
				.randomUUID().toString());
		try {
			this.context.getSleeEndpoint().startActivitySuspended(activity,
					activity, ActivityFlags.SLEE_MAY_MARSHAL);
		} catch (Exception e) {
			tracer.severe("failed to start activity " + activity.getId(), e);
		}
		return activity;
	}

	/**
	 * 
	 * @param activity
	 */
	void endActivity(JdbcActivityImpl activity) {
		try {
			this.context.getSleeEndpoint().endActivityTransacted(activity);
		} catch (Exception e) {
			tracer.severe("failed to end activity", e);
		}
	}

	// --- async sql execution

	private void fireEvent(FireableEventType eventType, Object event,
			ActivityHandle activity, int eventFlags) {
		if (tracer.isFineEnabled()) {
			tracer.fine("firing event: eventType = " + eventType
					+ " , event = " + event + " , activity = " + activity
					+ ", eventFlags = " + eventFlags);
		}
		try {
			context.getSleeEndpoint().fireEvent(activity, eventType, event,
					null, null, eventFlags);
		} catch (Throwable e) {
			tracer.severe("failed to fire event " + eventType + " in handle "
					+ activity, e);
		}
	}

	public void execute(final JdbcTask jdbcTask, final JdbcActivityImpl activity) {
		if (tracer.isFineEnabled()) {
			tracer.fine("execute( task = " + jdbcTask + " , activity = "
					+ activity + " )");
		}

		// get class loader from execution request context, task may need it for
		// actual execution
		final ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();

		// build runnable task
		Runnable task = new Runnable() {

			@Override
			public void run() {

				Thread.currentThread().setContextClassLoader(classLoader);

				final JdbcTaskContextImpl context = new JdbcTaskContextImpl(
						JdbcResourceAdaptor.this);
				try {
					// execute
					JdbcTaskResult taskResult = jdbcTask.execute(context);
					// protect the thread against bad tasks, which have open
					// transaction after task execution
					if (txManager.getTransaction() != null) {
						txManager.commit();
					}
					if (taskResult == null) {
						// no event to fire
						if (tracer.isFineEnabled()) {
							tracer.fine("execution of task " + jdbcTask
									+ " in activity " + activity
									+ " returned null result.");
						}
						return;
					}
					FireableEventType eventType = null;
					if (taskResult.getEventTypeID() == SimpleJdbcTaskResultEvent.EVENT_TYPE_ID) {
						eventType = simpleJdbcTaskResultEventType;
					} else {
						eventType = eventLookupFacility
								.getFireableEventType(taskResult
										.getEventTypeID());
					}
					fireEvent(eventType, taskResult.getEventObject(), activity,
							EventFlags.NO_FLAGS);
				} catch (Throwable e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed to complete task execution", e);
					}
					// build event
					Object event = new JdbcTaskExecutionThrowableEventImpl(e,
							jdbcTask);
					// fire event
					fireEvent(jdbcTaskExecutionThrowableEventType, event,
							activity, EventFlags.NO_FLAGS);
				} finally {
					if (context.connection != null) {
						try {
							if (!context.connection.isClosed()) {
								context.connection.close();
							}
						} catch (Exception e) {
							if (tracer.isFineEnabled()) {
								tracer.fine(
										"failure in connection closing procedure",
										e);
							}
						}
					}
				}
			}
		};

		// submit to executor
		executorService.submit(task);
	}

}
