package org.mobicents.slee.example.jdbc;

import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.slee.ActivityContextInterface;
import javax.slee.CreateException;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;

import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.resource.jdbc.JdbcActivity;
import org.mobicents.slee.resource.jdbc.JdbcActivityContextInterfaceFactory;
import org.mobicents.slee.resource.jdbc.JdbcResourceAdaptorSbbInterface;
import org.mobicents.slee.resource.jdbc.event.PreparedStatementUpdateCountEvent;
import org.mobicents.slee.resource.jdbc.event.StatementResultSetEvent;
import org.mobicents.slee.resource.jdbc.event.StatementSQLExceptionEvent;
import org.mobicents.slee.resource.jdbc.event.StatementUpdateCountEvent;

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
		try {
			Statement statement = jdbcRA.getConnection().createStatement();
			tracer.info("Created statement, executing query...");
			jdbcActivity.executeQuery(statement,
					"CREATE TABLE TestTable (Name VARCHAR(30));");
		} catch (Throwable e) {
			tracer.severe("failed to create statement", e);
		}
	}

	/**
	 * Event handler for {@link StatementResultSetEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStatementResultSetEvent(StatementResultSetEvent event,
			ActivityContextInterface aci) {
		tracer.info("Received a StatementResultSetEvent, as result of executed SQL "
				+ event.getSQL());
		tracer.info("Result: " + event.getResultSet());
		try {
			PreparedStatement preparedStatement = jdbcRA.getConnection()
					.prepareStatement("INSERT INTO TestTable VALUES(?)");
			preparedStatement.setString(1, "Mobicents");
			tracer.info("Created prepared statement, executing...");
			((JdbcActivity) aci.getActivity()).executeUpdate(preparedStatement);
		} catch (Throwable e) {
			tracer.severe("failed to create statement", e);
		}
	}

	/**
	 * Event handler for {@link StatementUpdateCountEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onPreparedStatementUpdateCountEvent(
			PreparedStatementUpdateCountEvent event,
			ActivityContextInterface aci) {
		tracer.info("Received a PreparedStatementUpdateCountEvent.");
		tracer.info("Update Count: " + event.getUpdateCount());
		try {
			Statement anotherStatement = jdbcRA.getConnection()
					.createStatement();
			tracer.info("Created statement, executing query...");
			((JdbcActivity) aci.getActivity()).executeUpdate(anotherStatement,
					"DROP TABLE TestTable;");
		} catch (Throwable e) {
			tracer.severe("failed to create statement", e);
		}
	}

	/**
	 * Event handler for {@link StatementUpdateCountEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStatementUpdateCountEvent(StatementUpdateCountEvent event,
			ActivityContextInterface aci) {
		tracer.info("Received a StatementUpdateCountEvent, as result of executed SQL "
				+ event.getSQL());
		tracer.info("Update Count: " + event.getUpdateCount());
		tracer.info("Ending JDBC Activity...");
		((JdbcActivity) aci.getActivity()).endActivity();
	}

	/**
	 * Event handler for {@link SQLExceptionEvent}.
	 * 
	 * @param event
	 * @param aci
	 */
	public void onStatementSQLExceptionEvent(StatementSQLExceptionEvent event,
			ActivityContextInterface aci) {
		tracer.info(
				"Received a StatementSQLExceptionEvent, as result of executed SQL "
						+ event.getSQL(), event.getSQLException());
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
