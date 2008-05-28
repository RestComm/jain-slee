package org.mobicents.slee.resource.http.events;

import java.rmi.server.UID;
import java.util.EventObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.java.slee.resource.http.events.HttpServletRequestEvent;

public class HttpServletRequestEventImpl extends EventObject implements
		HttpServletRequestEvent {

	private static final long serialVersionUID = -7683636914960112816L;

	private HttpServletRequest hreq = null;

	private String id = null;

	private HttpServletResponse hresponse;

	public HttpServletRequestEventImpl(HttpServletRequest req,
			HttpServletResponse response, Object source) {
		super(source);
		this.hreq = req;
		this.hresponse = response;
		this.id = new UID().toString();
	}

	public HttpServletRequest getRequest() {
		return hreq;
	}

	public HttpServletResponse getResponse() {
		return hresponse;
	}

	/**
	 * Returns an event ID, unique in respect to the VM where it was generated
	 */
	public String getId() {
		return id;
	}

	public String toString() {
		return getClass().getName() + "[id=" + id + "]";
	}

	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((HttpServletRequestEventImpl)o).id.equals(this.id);
		}
		else {
			return false;
		}
	}

	public int hashCode() {
		return id.hashCode();
	}

}
