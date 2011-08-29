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

package net.java.client.slee.resource.http.event;

import java.io.Serializable;

import org.apache.http.HttpResponse;

/**
 * Service sending Request asynchronously will receive
 * net.java.client.slee.resource.http.event.ResponseEvent as soon as the
 * Resource Adaptor receives the Response and emits ResponseEvent. </br>
 * ResponseEvent carries either the Response if everything went fine else
 * Exception if there was any problem
 * 
 * @author amit bhayani
 * @author martins
 * 
 */
public class ResponseEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8346946507877889058L;

	private Object requestApplicationData;
	private HttpResponse httpResponse;

	private Exception exception;

	private int id;

	public ResponseEvent(HttpResponse httpResponse, Object requestApplicationData) {
		this.httpResponse = httpResponse;
		this.requestApplicationData = requestApplicationData;
		id = httpResponse.hashCode() * 31 + "null".hashCode();
	}

	public ResponseEvent(Exception exception, Object requestApplicationData) {
		this.requestApplicationData = requestApplicationData;
		this.exception = exception;
		id = "null".hashCode() * 31 + exception.hashCode();
	}

	/**
	 * The application data included in the HTTP Request.
	 * @return
	 */
	public Object getRequestApplicationData() {
		return requestApplicationData;
	}
	
	/**
	 * The response to the HTTP Request.
	 * @return
	 */
	public HttpResponse getHttpResponse() {
		return this.httpResponse;
	}

	/**
	 * The exception which occurred when sending the HTTP Request, if any.
	 * @return
	 */
	public Exception getException() {
		return exception;
	}

	public boolean equals(Object o) {
		if (o != null && o.getClass() == this.getClass()) {
			return ((ResponseEvent) o).id == this.id;
		} else {
			return false;
		}
	}

	public int hashCode() {
		return id;
	}

}
