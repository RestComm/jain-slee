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

package org.mobicents.slee.enabler.rest.client.example;

import java.net.URLEncoder;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.slee.ActivityContextInterface;
import javax.slee.ActivityEndEvent;
import javax.slee.RolledBackContext;
import javax.slee.Sbb;
import javax.slee.SbbContext;
import javax.slee.facilities.Tracer;
import javax.slee.serviceactivity.ServiceStartedEvent;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.mobicents.slee.ChildRelationExt;
import org.mobicents.slee.SbbContextExt;
import org.mobicents.slee.enabler.rest.client.RESTClientEnablerChild;
import org.mobicents.slee.enabler.rest.client.RESTClientEnablerChildSbbLocalObject;
import org.mobicents.slee.enabler.rest.client.RESTClientEnablerParent;
import org.mobicents.slee.enabler.rest.client.RESTClientEnablerRequest;
import org.mobicents.slee.enabler.rest.client.RESTClientEnablerResponse;

/**
 * @author baranowb
 * 
 */
public abstract class RESTClientEnablerExampleSbb implements Sbb,
		RESTClientEnablerParent {

	// //////////////
	// SLEE Stuff //
	// //////////////

	private SbbContextExt sbbContext;
	private Tracer tracer;

	private static final String twitterStatusUpdateBaseURI = "http://api.twitter.com/1/statuses/update.json?status=";

	private CommonsHttpOAuthConsumer consumer;

	// ////////////////////
	// SBB LO Callbacks //
	// ////////////////////

	@Override
	public void onResponse(RESTClientEnablerChildSbbLocalObject child,
			RESTClientEnablerResponse response) {

		String uri = response.getRequest().getUri();
		RESTClientEnablerRequest.Type type = response.getRequest().getType();
		HttpResponse httpResponse = response.getHttpResponse();
		if (httpResponse != null) {
			String content = null;
			if (httpResponse.getEntity() != null) {
				try {
					content = EntityUtils.toString(httpResponse.getEntity());
				} catch (Exception e) {
					tracer.severe("failed to extract response content", e);
				}
			}
			tracer.info("onResponse. Child '" + child + "', request type '"
					+ type + "', uri '" + uri + "', status '"
					+ httpResponse.getStatusLine().getStatusCode()
					+ "', response content '" + content + "'");
		} else {
			tracer.info("onResponse. Child '" + child + "', request type '"
					+ type + "', uri '" + uri + "', exception '"
					+ response.getExecutionException() + "'");
		}
	}

	// //////////////////
	// Event handlers //
	// //////////////////

	public void onServiceStartedEvent(ServiceStartedEvent event,
			ActivityContextInterface aci) {
		tracer.info("Mobicents SLEE REST Client Enabler Example service activation.");
		try {
			RESTClientEnablerChild child = (RESTClientEnablerChild) getChildRelation()
					.create(ChildRelationExt.DEFAULT_CHILD_NAME);
			String uri = twitterStatusUpdateBaseURI
					+ URLEncoder.encode(
							"Mobicents SLEE REST Client Application Enabler Example ACTIVATED - "
									+ new Date(), "UTF-8");
			RESTClientEnablerRequest request = new RESTClientEnablerRequest(
					RESTClientEnablerRequest.Type.POST, uri)
					.setOAuthConsumer(consumer);
			child.execute(request);
		} catch (Exception e) {
			tracer.severe("failed to post service activation to twitter", e);
		}
	}

	public void onActivityEndEvent(ActivityEndEvent event,
			ActivityContextInterface aci) {
		tracer.info("Mobicents SLEE REST Client Enabler Example service deactivation.");
		try {
			RESTClientEnablerChild child = (RESTClientEnablerChild) getChildRelation()
					.get(ChildRelationExt.DEFAULT_CHILD_NAME);
			String uri = twitterStatusUpdateBaseURI
					+ URLEncoder.encode(
							"Mobicents SLEE REST Client Application Enabler Example DEACTIVATED - "
									+ new Date(), "UTF-8");
			RESTClientEnablerRequest request = new RESTClientEnablerRequest(
					RESTClientEnablerRequest.Type.POST, uri)
					.setOAuthConsumer(consumer);
			child.execute(request);
		} catch (Exception e) {
			tracer.severe("failed to post service deactivation to twitter", e);
		}
	}

	// ///////////////////
	// Child relations //
	// ///////////////////
	public abstract ChildRelationExt getChildRelation();

	// //////////////////
	// SLEE callbacks //
	// //////////////////
	public void setSbbContext(SbbContext context) {
		sbbContext = (SbbContextExt) context;
		this.tracer = this.sbbContext.getTracer("WsClientEnabler");
		// this.timerFacility = sbbContext.getTimerFacility();
		try {
			Context myEnv = (Context) new InitialContext()
					.lookup("java:comp/env");
			String twitterConsumerKey = (String) myEnv
					.lookup("twitter.consumerKey");
			String twitterConsumerSecret = (String) myEnv
					.lookup("twitter.consumerSecret");
			String twitterAccessToken = (String) myEnv
					.lookup("twitter.accessToken");
			String twitterAccessTokenKey = (String) myEnv
					.lookup("twitter.accessTokenKey");
			consumer = new CommonsHttpOAuthConsumer(twitterConsumerKey,
					twitterConsumerSecret);
			consumer.setTokenWithSecret(twitterAccessToken,
					twitterAccessTokenKey);
		} catch (NamingException e) {
			tracer.severe("failed to set sbb context", e);
		}
	}

	public void unsetSbbContext() {
		this.sbbContext = null;
	}

	public void sbbCreate() throws javax.slee.CreateException {
	}

	public void sbbPostCreate() throws javax.slee.CreateException {
	}

	public void sbbActivate() {
	}

	public void sbbPassivate() {
	}

	public void sbbRemove() {
	}

	public void sbbLoad() {
	}

	public void sbbStore() {
	}

	public void sbbExceptionThrown(Exception exception, Object event,
			ActivityContextInterface activity) {
	}

	public void sbbRolledBack(RolledBackContext context) {
	}

}
