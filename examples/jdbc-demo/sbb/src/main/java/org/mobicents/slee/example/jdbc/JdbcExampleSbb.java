package org.mobicents.slee.example.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;
import javax.slee.transaction.SleeTransaction;

import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.resource.jdbc.JdbcActivity;
import org.mobicents.slee.resource.jdbc.JdbcActivityContextInterfaceFactory;
import org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.jdbc.event.JdbcTaskExecutionExceptionEvent;
import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;
import org.mobicents.slee.resource.jdbc.task.simple.SimpleJdbcTask;
import org.mobicents.slee.resource.jdbc.task.simple.SimpleJdbcTaskResultEvent;

/**
 * A SBB that examples usage of the JDBC RA.
 * 
 * @author martins
 * 
 */
public abstract class JdbcExampleSbb implements Sbb {

	/**
	 * the SBB object context
	 */
	private SbbContextExt contextExt;

	/**
	 * the SBB logger
	 */
	private Tracer tracer;

	/**
	 * the JDBC RA SBB Interface
	 */
	private JdbcResourceAdaptorSbbInterface jdbcRA;

	/**
	 * the JDBC RA {@link ActivityContextInterface} factory
	 */
	private JdbcActivityContextInterfaceFactory jdbcACIF;

	/**
	 * Event handler for {@link ServiceStartedEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {

		tracer.info("JDBC Example started.");

		JdbcActivity jdbcActivity = jdbcRA.createActivity();
		tracer.info("Created JDBC RA activity, using RA's SBB Interface.");

		ActivityContextInterface jdbcACI = jdbcACIF
				.getActivityContextInterface(jdbcActivity);
		jdbcACI.attach(contextExt.getSbbLocalObject());
		tracer.info("Retrieved the ACI related to the JDBC RA activity, and attached the sbb entity.");

		SimpleJdbcTask task = new SimpleJdbcTask() {
			@Override
			public Object executeSimple(JdbcTaskContext context) {
				SleeTransaction tx = null;
				try {
					tx = context.getSleeTransactionManager()
							.beginSleeTransaction();
					Connection connection = context.getConnection();
					Statement statement = connection.createStatement();
					tracer.info("Created statement to create table, executing query...");
					statement
							.execute("CREATE TABLE TestTable (Name VARCHAR(30));");
					PreparedStatement preparedStatement = connection
							.prepareStatement("INSERT INTO TestTable VALUES(?)");
					preparedStatement.setString(1, "Mobicents");
					tracer.info("Created prepared statement for data insert, executing...");
					preparedStatement.execute();
					preparedStatement = connection
							.prepareStatement("SELECT ? From TestTable;");
					preparedStatement.setString(1, "Name");
					tracer.info("Created prepared statement for data query, executing...");
					preparedStatement.execute();
					ResultSet resultSet = preparedStatement.getResultSet();
					resultSet.next();
					tracer.info("Data query first result: "
							+ resultSet.getString(1));
					Statement anotherStatement = connection.createStatement();
					tracer.info("Created statement to drop table, executing update...");
					anotherStatement.executeUpdate("DROP TABLE TestTable;");
					tx.commit();
					tx = null;
					return true;
				} catch (Exception e) {
					tracer.severe("failed to create table", e);
					if (tx != null) {
						try {
							tx.rollback();
						} catch (Exception f) {
							tracer.severe("failed to rollback tx", f);
						}
					}
					return false;
				}
			}
		};
		jdbcActivity.execute(task);
	}

	/**
	 * Event handler for {@link SimpleJdbcTaskResultEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onSimpleJdbcTaskResultEvent(SimpleJdbcTaskResultEvent event,
			ActivityContextInterface aci) {
		tracer.info("Received a SimpleJdbcTaskResultEvent, task = "
				+ event.getTask() + ", result object = " + event.getResult());
		((JdbcActivity) aci.getActivity()).endActivity();
	}

	/**
	 * Event handler for {@link SQLExceptionEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onJdbcTaskExecutionExceptionEvent(
			JdbcTaskExecutionExceptionEvent event, ActivityContextInterface aci) {
		tracer.info(
				"Received a JdbcTaskExecutionExceptionEvent, as result of executed task "
						+ event.getTask(), event.getException());
		tracer.info("Ending JDBC Activity...");
		((JdbcActivity) aci.getActivity()).endActivity();
	}

	// --- SLEE 1.1 SBB contract

	@Override
	public void setSbbContext(SbbContext context) {
		this.tracer = context.getTracer("JdbcExampleSbb");
		this.contextExt = (SbbContextExt) context;
		this.jdbcRA = (JdbcResourceAdaptorSbbInterface) contextExt
				.getResourceAdaptorInterface(
						JdbcResourceAdaptorSbbInterface.RATYPE_ID, "JDBCRA");
		this.jdbcACIF = (JdbcActivityContextInterfaceFactory) contextExt
				.getActivityContextInterfaceFactory(JdbcActivityContextInterfaceFactory.RATYPE_ID);
	}

	@Override
	public void unsetSbbContext() {
		this.contextExt = null;
		this.tracer = null;
		this.jdbcRA = null;
		this.jdbcACIF = null;
	}

	@Override
	public void sbbActivate() {

	}

	@Override
	public void sbbCreate() throws CreateException {

	}

	@Override
	public void sbbExceptionThrown(Exception arg0, Object arg1,
			ActivityContextInterface arg2) {

	}

	@Override
	public void sbbLoad() {

	}

	@Override
	public void sbbPassivate() {

	}

	@Override
	public void sbbPostCreate() throws CreateException {

	}

	@Override
	public void sbbRemove() {

	}

	@Override
	public void sbbRolledBack(RolledBackContext arg0) {

	}

	@Override
	public void sbbStore() {

	}

}
