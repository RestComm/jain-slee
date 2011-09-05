package org.mobicents.slee.resource.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.slee.transaction.SleeTransactionManager;

import org.mobicents.slee.resource.jdbc.task.JdbcTaskContext;

public class JdbcTaskContextImpl implements JdbcTaskContext {

	private String username;
	private String password;
	protected Connection connection;
	private final JdbcResourceAdaptor ra;

	public JdbcTaskContextImpl(JdbcResourceAdaptor ra) {
		this.ra = ra;
	}

	@Override
	public void setConnectionCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			if (username != null) {
				connection = ra.getDatasource().getConnection(username,
						password);
			} else {
				connection = ra.getDatasource().getConnection();
			}
		}
		return connection;
	}

	@Override
	public SleeTransactionManager getSleeTransactionManager() {
		return ra.getTxManager();
	}

}
