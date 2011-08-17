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
 * Content for the initial request of a SIP subscription.
 * 
 * Example of usage:
 * 
 * SubscriptionRequestContent subscriptionContent = new SubscriptionRequestContent().setContent(CONTENT).setContentType(CONTENT_TYPE);
 * 
 * @author martins
 *
 */
public class SubscriptionRequestContent implements Externalizable {

	private ContentType contentType;
	private String content;
	
	/**
	 * Retrieves the content to be included in the subscription request.
	 * @return
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the content to be included in the subscription request.
	 * @param content
	 * @return
	 */
	public SubscriptionRequestContent setContent(String content) {
		this.content = content;
		return this;
	}
	
	/**
	 * Retrieves the content type.
	 * @return
	 */
	public ContentType getContentType() {
		return contentType;
	}
	
	/**
	 * Sets the content to be included in the subscription request.
	 * @param contentType
	 * @return
	 */
	public SubscriptionRequestContent setContentType(ContentType contentType) {
		this.contentType = contentType;
		return this;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeUTF(content);
		out.writeObject(contentType);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		content = in.readUTF();
		contentType = (ContentType) in.readObject();		
	}
	
}
