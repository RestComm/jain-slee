package org.mobicents.slee.resource.xmpp;

import java.io.Serializable;

/**
 * This object represents a generic, not bound with any specific API, XMPP connection.
 * 
 * This object is the Activity Object for the XMPP Resource Adaptor.
 * 
 * @author Eduardo Martins
 * @version 2.1
 */
public class XmppConnection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String connectionId;
	private Object connection;
	private final String toString;
	
	public XmppConnection(String connectionId, Object connection) {
		this.connectionId = connectionId;
		this.connection = connection;
		this.toString = "XmppConnection { connectionId="+connectionId+",connection="+connection.toString()+"}";
	}
	
	public String getConnectionId() {
		return connectionId;
	}
	
	public Object getConnection() {
		return connection;
	}
	
	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((XmppConnection)o).connectionId.equals(this.connectionId);
		}
		else {
			return false;
		}
	}
	
	public int hashCode() {
		return connectionId.hashCode();
	}
	
	public String toString() {
		return toString;
	}
}
