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

package org.mobicents.slee.enabler.sip;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * Static information needed to create a SIP subscription.
 * 
 * Example of usage:
 * 
 * SubscriptionData subscriptionData = new
 * SubscriptionData().setSubscriberURI(subscriber
 * ).setNotifierURI(notifier).setEventPackage
 * (EVENT_PACKAGE).setExpires(expires).
 * setAcceptedContentTypes(ACCEPTED_CONTENT_TYPES
 * ).setEventParameters(EVENT_PARAMETERS);
 * 
 * @author martins
 * 
 */
public class SubscriptionData implements Externalizable {

	private static final EventPackageParameter[] EMPTY_EVENT_PARAMETERS = {};

	private static final ContentType[] EMPTY_ACCEPTED_CONTENT_TYPES = {};

	private String subscriberURI;

	private String subscriberDisplayName;

	private String notifierURI;

	private String eventPackage;

	private EventPackageParameter[] eventParameters = EMPTY_EVENT_PARAMETERS;

	private ContentType[] acceptedContentTypes = EMPTY_ACCEPTED_CONTENT_TYPES;

	private boolean supportResourceLists;

	// private SubscriptionContent content;

	private int expires = 3600;

	/*
	 * public SubscriptionContent getContent() { return content; }
	 * 
	 * public SubscriptionData setContent(SubscriptionContent content) {
	 * this.content = content; return this; }
	 */

	/**
	 * Retrieves the subscriber's URI, i.e, the entity requesting the
	 * subscription.
	 */
	public String getSubscriberURI() {
		return subscriberURI;
	}

	/**
	 * Sets the subscriber's URI, i.e, the entity requesting the subscription.
	 * 
	 * @param subscriberURI
	 * @return
	 */
	public SubscriptionData setSubscriberURI(String subscriberURI) {
		this.subscriberURI = subscriberURI;
		return this;
	}

	/**
	 * Retrieves the subscriber's display name. This information is optional.
	 * 
	 * @return
	 */
	public String getSubscriberDisplayName() {
		return subscriberDisplayName;
	}

	/**
	 * Sets the subscriber's display name. This information is optional.
	 * 
	 * @param subscriberDisplayName
	 * @return
	 */
	public SubscriptionData setSubscriberDisplayName(
			String subscriberDisplayName) {
		this.subscriberDisplayName = subscriberDisplayName;
		return this;
	}

	/**
	 * Retrieves the notifier's URI, i.e., the URI identifying the resource
	 * being subscribed.
	 * 
	 * @return
	 */
	public String getNotifierURI() {
		return notifierURI;
	}

	/**
	 * Sets the notifier's URI, i.e., the URI identifying the resource being
	 * subscribed.
	 * 
	 * @param notifierURI
	 * @return
	 */
	public SubscriptionData setNotifierURI(String notifierURI) {
		this.notifierURI = notifierURI;
		return this;
	}

	/**
	 * Retrieves the subscription's event package, for instance "presence".
	 * 
	 * @return
	 */
	public String getEventPackage() {
		return eventPackage;
	}

	/**
	 * Sets the subscription's event package, for instance "presence".
	 * 
	 * @param eventPackage
	 * @return
	 */
	public SubscriptionData setEventPackage(String eventPackage) {
		this.eventPackage = eventPackage;
		return this;
	}

	/**
	 * Retrieves the optional subscription event parameters, for instance in
	 * xcap-diff event package there is a parameter named diff-processing which
	 * can indicate what kind of content/detail should be included in
	 * notifications.
	 * 
	 * @return
	 */
	public EventPackageParameter[] getEventParameters() {
		return eventParameters;
	}

	/**
	 * Sets the optional subscription event parameters, for instance in
	 * xcap-diff event package there is a parameter named diff-processing which
	 * can indicate what kind of content/detail should be included in
	 * notifications.
	 * 
	 * @param eventParameters
	 * @return
	 */
	public SubscriptionData setEventParameters(
			EventPackageParameter[] eventParameters) {
		if (eventParameters == null) {
			throw new NullPointerException("null event parameters");
		}
		this.eventParameters = eventParameters;
		return this;
	}

	/**
	 * Retrieves the optional content types, which should be accepted by the
	 * enabler.
	 * 
	 * @return
	 */
	public ContentType[] getAcceptedContentTypes() {
		return acceptedContentTypes;
	}

	/**
	 * Sets the optional content types, which should be accepted by the enabler.
	 * 
	 * @param acceptedContentTypes
	 * @return
	 */
	public SubscriptionData setAcceptedContentTypes(
			ContentType[] acceptedContentTypes) {
		if (eventParameters == null) {
			throw new NullPointerException("null accepted content types");
		}
		this.acceptedContentTypes = acceptedContentTypes;
		return this;
	}

	/**
	 * Indicates if the subscription should support resource lists as the
	 * notifier. If true the enabler includes all the mandatory accepted content
	 * types (rlmi and multipart) related with RLS notifications, it also
	 * includes the mandatory Supported header.
	 * 
	 * @return
	 */
	public boolean isSupportResourceLists() {
		return supportResourceLists;
	}

	/**
	 * Defines if the subscription should support resource lists as the
	 * notifier. If true the enabler includes all the mandatory accepted content
	 * types (rlmi and multipart) related with RLS notifications, it also
	 * includes the mandatory Supported header.
	 * 
	 * @param supportResourceLists
	 * @return
	 */
	public SubscriptionData setSupportResourceLists(boolean supportResourceLists) {
		this.supportResourceLists = supportResourceLists;
		return this;
	}

	/**
	 * Retrieves the period between subscription refreshes.
	 * 
	 * @return
	 */
	public int getExpires() {
		return expires;
	}

	/**
	 * Sets the period between subscription refreshes.
	 * 
	 * @param expires
	 * @return
	 */
	public SubscriptionData setExpires(int expires) {
		this.expires = expires;
		return this;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// subscriber uri
		out.writeUTF(subscriberURI);
		// resource uri
		out.writeUTF(notifierURI);
		// event package
		out.writeUTF(eventPackage);
		// support resource lists
		out.writeBoolean(supportResourceLists);
		// event parameters
		out.writeInt(eventParameters.length);
		for (EventPackageParameter parameter : eventParameters) {
			out.writeObject(parameter);
		}
		// accepted content types
		out.writeInt(acceptedContentTypes.length);
		for (ContentType contentType : acceptedContentTypes) {
			out.writeObject(contentType);
		}
		// content
		/*
		 * if (content != null) { out.writeBoolean(true);
		 * out.writeObject(content); } else { out.writeBoolean(false); }
		 */
		// subscriber display name
		if (subscriberDisplayName != null) {
			out.writeBoolean(true);
			out.writeUTF(subscriberDisplayName);
		} else {
			out.writeBoolean(false);
		}
		// expires
		out.writeInt(expires);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// subscriber uri
		subscriberURI = in.readUTF();
		// resource uri
		notifierURI = in.readUTF();
		// event package
		eventPackage = in.readUTF();
		// support resource lists
		supportResourceLists = in.readBoolean();
		// event parameters
		int eventParametersLenght = in.readInt();
		if (eventParametersLenght > 0) {
			eventParameters = new EventPackageParameter[eventParametersLenght];
			for (int i = 0; i < eventParametersLenght; i++) {
				eventParameters[i] = (EventPackageParameter) in.readObject();
			}
		}
		// accepted content types
		int acceptedContentTypesLenght = in.readInt();
		if (acceptedContentTypesLenght > 0) {
			acceptedContentTypes = new ContentType[acceptedContentTypesLenght];
			for (int i = 0; i < acceptedContentTypesLenght; i++) {
				acceptedContentTypes[i] = (ContentType) in.readObject();
			}
		}
		// content
		/*
		 * if (in.readBoolean()) { content = (SubscriptionContent)
		 * in.readObject(); }
		 */
		// subscriber display name
		if (in.readBoolean()) {
			subscriberDisplayName = in.readUTF();
		}
		// expires
		expires = in.readInt();
	}

}
