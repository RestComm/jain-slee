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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.Address;
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
import javax.sql.DataSource;

import org.mobicents.slee.resource.jdbc.event.PreparedStatementEvent;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementResultSetEvent;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementResultSetEventImpl;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementSQLExceptionEvent;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementSQLExceptionEventImpl;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementUnknownResultEvent;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementUnknownResultEventImpl;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementUpdateCountEvent;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementUpdateCountEventImpl;
import org.mobicents.slee.resource.jdbc.event.StatementEvent;
import org.mobicents.slee.resource.jdbc.event.StatementResultSetEvent;
import org.mobicents.slee.resource.jdbc.event.StatementResultSetEventImpl;
import org.mobicents.slee.resource.jdbc.event.StatementSQLExceptionEvent;
import org.mobicents.slee.resource.jdbc.event.StatementSQLExceptionEventImpl;
import org.mobicents.slee.resource.jdbc.event.StatementUnknownResultEvent;
import org.mobicents.slee.resource.jdbc.event.StatementUnknownResultEventImpl;
import org.mobicents.slee.resource.jdbc.event.StatementUpdateCountEvent;
import org.mobicents.slee.resource.jdbc.event.StatementUpdateCountEventImpl;

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
	
	/**
	 * the RA interface for SBBs
	 */
	private JdbcResourceAdaptorSbbInterfaceImpl sbbInterface =  new JdbcResourceAdaptorSbbInterfaceImpl(this);

	/**
	 * the RA's Marshaller
	 */
	private JdbcResourceAdaptorMarshaller marshaller = new JdbcResourceAdaptorMarshaller(this);
	
	/**
	 * the RA "logger"
	 */
	private Tracer tracer;

	private DataSource datasource;

	private ExecutorService executorService;
	
	private FireableEventType preparedStatementResultSetEventType;
	private FireableEventType preparedStatementSQLExceptionEventType;
	private FireableEventType preparedStatementUnknownResultEventType;
	private FireableEventType preparedStatementUpdateCountEventType;
	private FireableEventType statementResultSetEventType;
	private FireableEventType statementSQLExceptionEventType;
	private FireableEventType statementUnknownResultEventType;
	private FireableEventType statementUpdateCountEventType;

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
		// close the jdbc connection if needed
		Statement statement = null;
		if (eventObject instanceof PreparedStatementEvent) {
			statement = ((PreparedStatementEvent)eventObject).getPreparedStatement();
		}
		else {
			statement = ((StatementEvent)eventObject).getStatement();
		}
		closeConnectionIfNeeded(statement);
	}

	@Override
	public Object getActivity(ActivityHandle activityHandle) {
		return activityHandle;
	}

	@Override
	public ActivityHandle getActivityHandle(Object activityObject) {
		return (JdbcActivityImpl) activityObject;
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
			this.datasource = (DataSource) InitialContext.doLookup(datasourceName);
		} catch (NamingException e) {
			tracer.severe("failed to retrieve the data source", e);			
		}
		this.executorService = Executors.newFixedThreadPool(executorServiceThreads);
	}

	@Override
	public void raConfigurationUpdate(ConfigProperties configuration) {
		// NOT USED
	}

	@Override
	public void raConfigure(ConfigProperties configuration) {
		this.datasourceName = (String) configuration.getProperty(DATASOURCE_JNDI_NAME_CONFIG_PROPERTY).getValue();
		this.executorServiceThreads = (Integer) configuration.getProperty(EXECUTOR_SERVICE_THREADS_CONFIG_PROPERTY).getValue();		
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
		String datasourceName = (String) properties.getProperty("DATASOURCE_JNDI_NAME").getValue();
		try {
			InitialContext.doLookup(datasourceName);
		} catch (NamingException e) {
			throw new InvalidConfigurationException("bad datasource name",e);
		}
		Integer executorServiceThreads = (Integer) properties.getProperty("EXECUTOR_SERVICE_THREADS").getValue();
		if (executorServiceThreads < 1) {
			throw new InvalidConfigurationException("executor service threads must be a positive integer");
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
		try {
			this.preparedStatementResultSetEventType = context.getEventLookupFacility().getFireableEventType(PreparedStatementResultSetEvent.EVENT_TYPE_ID);
			this.preparedStatementSQLExceptionEventType = context.getEventLookupFacility().getFireableEventType(PreparedStatementSQLExceptionEvent.EVENT_TYPE_ID);
			this.preparedStatementUnknownResultEventType = context.getEventLookupFacility().getFireableEventType(PreparedStatementUnknownResultEvent.EVENT_TYPE_ID);
			this.preparedStatementUpdateCountEventType = context.getEventLookupFacility().getFireableEventType(PreparedStatementUpdateCountEvent.EVENT_TYPE_ID);
			this.statementResultSetEventType = context.getEventLookupFacility().getFireableEventType(StatementResultSetEvent.EVENT_TYPE_ID);
			this.statementSQLExceptionEventType = context.getEventLookupFacility().getFireableEventType(StatementSQLExceptionEvent.EVENT_TYPE_ID);
			this.statementUnknownResultEventType = context.getEventLookupFacility().getFireableEventType(StatementUnknownResultEvent.EVENT_TYPE_ID);
			this.statementUpdateCountEventType = context.getEventLookupFacility().getFireableEventType(StatementUpdateCountEvent.EVENT_TYPE_ID);
		} catch (Throwable e) {
			tracer.severe("Failed to retrieve fireable event types",e);
		}
	}

	@Override
	public void unsetResourceAdaptorContext() {
		this.context = null;
		this.tracer = null;
		this.preparedStatementResultSetEventType = null;
		this.preparedStatementSQLExceptionEventType = null;
		this.preparedStatementUnknownResultEventType = null;
		this.preparedStatementUpdateCountEventType = null;
		this.statementResultSetEventType = null;
		this.statementSQLExceptionEventType = null;
		this.statementUnknownResultEventType = null;
		this.statementUpdateCountEventType = null;
	}

	// --- end of SLEE 1.1 RA contract

	// --- activity management
	/**
	 * 
	 * @return
	 */
	JdbcActivityImpl createActivity() {		
		JdbcActivityImpl activity = new JdbcActivityImpl(this, UUID.randomUUID()
				.toString());
		try {
			this.context.getSleeEndpoint().startActivitySuspended(activity, activity,
					ActivityFlags.SLEE_MAY_MARSHAL);
		} catch (Exception e) {
			tracer.severe("failed to start activity "+activity.getId(),e);
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
		}
		catch (Exception e) {
			tracer.severe("failed to end activity",e);
		}
	}
	
	// --- async sql execution
	
	/**
	 * 
	 * @param preparedStatement
	 * @param activity
	 */
	void executeQuery(final PreparedStatement preparedStatement, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeQuery( preparedStatement = "+preparedStatement+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					ResultSet resultSet = preparedStatement.executeQuery();
					// build event
					Object event = new PreparedStatementResultSetEventImpl(preparedStatement, resultSet);
					// fire event
					fireEvent(preparedStatementResultSetEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}
					closeConnectionIfNeeded(preparedStatement);
					// exception, build event
					Object event = new PreparedStatementSQLExceptionEventImpl(preparedStatement, e);
					// fire event
					fireEvent(preparedStatementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	private void fireEvent(FireableEventType eventType, Object event, ActivityHandle activity, int eventFlags) {
		if(tracer.isFineEnabled()) {
			tracer.fine("firing event: eventType = "+eventType+" , event = "+event+" , activity = "+activity+", eventFlags = "+eventFlags);
		}
		try {
			context.getSleeEndpoint().fireEvent(activity, eventType, event, null, null,eventFlags);
		} catch (Throwable e) {
			tracer.severe("failed to fire event "+eventType+" in handle "+activity, e);
		}
	}
	
	private void closeConnectionIfNeeded(Statement statement) {
		try{
			if (!statement.getConnection().isClosed()) {
				statement.getConnection().close();
			}
		}
		catch(Exception f) {
			tracer.severe("failed to close connection",f);
		}
	}
	
	/**
	 * 
	 * @param preparedStatement
	 * @param activity
	 */
	void executeUpdate(final PreparedStatement preparedStatement, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeUpdate( preparedStatement = "+preparedStatement+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					int updateCount = preparedStatement.executeUpdate();
					// build event
					Object event = new PreparedStatementUpdateCountEventImpl(preparedStatement, updateCount);
					// fire event
					fireEvent(preparedStatementUpdateCountEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}	
					closeConnectionIfNeeded(preparedStatement);
					// exception, build event
					Object event = new PreparedStatementSQLExceptionEventImpl(preparedStatement, e);
					// fire event
					fireEvent(preparedStatementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param preparedStatement
	 * @param activity
	 */
	void execute(final PreparedStatement preparedStatement, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("execute( preparedStatement = "+preparedStatement+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					boolean executionResult = preparedStatement.execute();
					// build event
					Object event = new PreparedStatementUnknownResultEventImpl(preparedStatement, executionResult);
					// fire event
					fireEvent(preparedStatementUnknownResultEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(preparedStatement);
					// exception, build event
					Object event = new PreparedStatementSQLExceptionEventImpl(preparedStatement, e);
					// fire event
					fireEvent(preparedStatementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param activity
	 */
	void execute(final Statement statement, final String sql, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("execute( statement = "+statement+" , sql = "+sql+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					boolean executionResult = statement.execute(sql);
					// build event
					Object event = new StatementUnknownResultEventImpl(null, null, null, sql, statement, executionResult);
					// fire event
					fireEvent(statementUnknownResultEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, null, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param autoGeneratedKeys
	 * @param activity
	 */
	void execute(final Statement statement, final String sql, final int autoGeneratedKeys, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("execute( statement = "+statement+" , sql = "+sql+" , autoGeneratedKeys = "+autoGeneratedKeys+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					boolean executionResult = statement.execute(sql);
					// build event
					Object event = new StatementUnknownResultEventImpl(autoGeneratedKeys, null, null, sql, statement, executionResult);
					// fire event
					fireEvent(statementUnknownResultEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(autoGeneratedKeys, null, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param columnIndexes
	 * @param activity
	 */
	void execute(final Statement statement, final String sql, final int[] columnIndexes, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("execute( statement = "+statement+" , sql = "+sql+" , columnIndexes = "+columnIndexes == null ? null : Arrays.asList(columnIndexes)+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					boolean executionResult = statement.execute(sql);
					// build event
					Object event = new StatementUnknownResultEventImpl(null, columnIndexes, null, sql, statement, executionResult);
					// fire event
					fireEvent(statementUnknownResultEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, columnIndexes, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);		
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param columnNames
	 * @param activity
	 */
	void execute(final Statement statement, final String sql, final String[] columnNames, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("execute( statement = "+statement+" , sql = "+sql+" , columnNames = "+columnNames == null ? null : Arrays.asList(columnNames)+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					boolean executionResult = statement.execute(sql);
					// build event
					Object event = new StatementUnknownResultEventImpl(null, null, columnNames, sql, statement, executionResult);
					// fire event
					fireEvent(statementUnknownResultEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, null, columnNames, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param activity
	 */
	void executeQuery(final Statement statement, final String sql, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeQuery( statement = "+statement+" , sql = "+sql+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					ResultSet resultSet = statement.executeQuery(sql);
					// build event
					Object event = new StatementResultSetEventImpl(sql, statement, resultSet);
					// fire event
					fireEvent(statementResultSetEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, null, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param activity
	 */
	void executeUpdate(final Statement statement, final String sql, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeUpdate( statement = "+statement+" , sql = "+sql+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					int updateCount = statement.executeUpdate(sql);
					// build event
					Object event = new StatementUpdateCountEventImpl(null, null, null, sql, statement, updateCount);
					// fire event
					fireEvent(statementUpdateCountEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, null, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param autoGeneratedKeys
	 * @param activity
	 */
	void executeUpdate(final Statement statement, final String sql,
			final int autoGeneratedKeys, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeUpdate( statement = "+statement+" , sql = "+sql+" , autoGeneratedKeys = "+autoGeneratedKeys+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					int updateCount = statement.executeUpdate(sql);
					// build event
					Object event = new StatementUpdateCountEventImpl(autoGeneratedKeys, null, null, sql, statement, updateCount);
					// fire event
					fireEvent(statementUpdateCountEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(autoGeneratedKeys, null, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param columnIndexes
	 * @param activity
	 */
	void executeUpdate(final Statement statement, final String sql,
			final int[] columnIndexes, final JdbcActivityImpl activity) {
		if(tracer.isFineEnabled()) {
			tracer.fine("executeUpdate( statement = "+statement+" , sql = "+sql+" , columnIndexes = "+columnIndexes == null ? null : Arrays.asList(columnIndexes)+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					int updateCount = statement.executeUpdate(sql);
					// build event
					Object event = new StatementUpdateCountEventImpl(null, columnIndexes, null, sql, statement, updateCount);
					// fire event
					fireEvent(statementUpdateCountEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}		
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, columnIndexes, null, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);	
	}
	
	/**
	 * 
	 * @param statement
	 * @param sql
	 * @param columnNames
	 * @param activity
	 */
	void executeUpdate(final Statement statement, final String sql,
			final String[] columnNames, final JdbcActivityImpl activity) {		
		if(tracer.isFineEnabled()) {
			tracer.fine("executeUpdate( statement = "+statement+" , sql = "+sql+" , columnNames = "+columnNames == null ? null : Arrays.asList(columnNames)+" , activity = "+activity+" )");
		}		
		// build runnable task
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					// execute
					int updateCount = statement.executeUpdate(sql);
					// build event
					Object event = new StatementUpdateCountEventImpl(null, null, columnNames, sql, statement, updateCount);
					// fire event
					fireEvent(statementUpdateCountEventType, event, activity, EventFlags.REQUEST_EVENT_UNREFERENCED_CALLBACK);
				}
				catch (SQLException e) {
					if (tracer.isFineEnabled()) {
						tracer.fine("failed execute sql",e);						
					}			
					closeConnectionIfNeeded(statement);
					// exception, build event
					Object event = new StatementSQLExceptionEventImpl(null, null, columnNames, sql, statement, e);
					// fire event
					fireEvent(statementSQLExceptionEventType, event, activity, EventFlags.NO_FLAGS);
				}
			}
		};
		// submit to executor
		executorService.submit(task);		
	}

}
