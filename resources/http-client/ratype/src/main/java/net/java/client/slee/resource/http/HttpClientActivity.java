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

package net.java.client.slee.resource.http;

import org.apache.commons.httpclient.HttpMethod;

/**
 * HttpClientActivity is created by service by calling
 * createHttpClientActivity() method of HttpClientResourceAdaptorSbbInterface.
 * Service that wants to send the Request asynchronously uses and attaches
 * itself to HttpClientActivity to receive the ResponseEvent latter. <br/>
 * 
 * @author amit.bhayani
 * 
 */
public interface HttpClientActivity {

	/**
	 * @return Uniques Id for this activity
	 */
	public String getSessionId();

	/**
	 * HttpClientActivity can be created by calling createHttpClientActivity(
	 * boolean endOnReceivingResponse) method of
	 * HttpClientResourceAdaptorSbbInterface. <br/>If endOnReceivingResponse is
	 * set to true this Activity will end as soon as the ResponseEvent is sent
	 * by the ResourceAdaptor and calling this method explicitly will throw
	 * IllegalStateException <br/> If endOnReceivingResponse is set to false,
	 * service using this activity has to explicitly call endActivity() method
	 * to end the Activity.
	 */
	public void endActivity();

	/**
	 * @return Returns true if this Activity is set to end as soon as the
	 *         Response is received
	 */
	public boolean getEndOnReceivingResponse();

	/**
	 * The service that wants to send the Request asynchronously has to first
	 * create instance of HttpMethod by calling createHttpMethod() of
	 * HttpClientResourceAdaptorSbbInterface, the service also creates Activity
	 * and attaches itself to this Activity and then calls executeMethod passing
	 * the instance of HttpMethod
	 * 
	 * @param httpMethod
	 */
	public void executeMethod(HttpMethod httpMethod);

}
