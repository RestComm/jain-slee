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

import java.io.Serializable;

/**
 * Simple pojo to pass information about notify
 * 
 * @author baranowb
 * 
 */
public class Notify implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static final int NO_TIMEOUT = -1;
	// TODO: add time stamp?

	protected TerminationReason terminationReason;
	protected String terminationReasonExtension;
	protected SubscriptionStatus status;
	protected String statusExtension;
	protected int retryAfter = NO_TIMEOUT;
	protected int expires = NO_TIMEOUT;

	protected String content;
	// is this required, always easier for parent on act?
	protected String contentType;
	protected String contentSubType;
	protected String notifier;
	protected String subscriber;

	void setTerminationReason(TerminationReason terminationReason) {
		this.terminationReason = terminationReason;
	}

	void setTerminationReasonExtension(String terminationReasonExtension) {
		this.terminationReasonExtension = terminationReasonExtension;
	}

	void setStatus(SubscriptionStatus status) {
		this.status = status;
	}

	void setStatusExtension(String statusExtension) {
		this.statusExtension = statusExtension;
	}

	void setRetryAfter(int retryAfter) {
		this.retryAfter = retryAfter;
	}

	void setExpires(int expires) {
		this.expires = expires;
	}

	void setContent(String content) {
		this.content = content;
	}

	void setContentType(String contentType) {
		this.contentType = contentType;
	}

	void setContentSubType(String contentSubType) {
		this.contentSubType = contentSubType;
	}

	void setNotifier(String notifier) {
		this.notifier = notifier;
	}

	void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * Fetches termination reason if subscription has been terminated.
	 * 
	 * @return
	 */
	public TerminationReason getTerminationReason() {
		return terminationReason;
	}

	/**
	 * In case {@link #getTerminationReason()} returns
	 * {@link TerminationReason.extension}, this method returns string
	 * representation of "extension"
	 * 
	 * @return
	 */
	public String getTerminationReasonExtension() {
		return terminationReasonExtension;
	}

	/**
	 * Status of subscription.
	 * 
	 * @return
	 */
	public SubscriptionStatus getStatus() {
		return status;
	}

	/**
	 * In case {@link #getStatus()} returns {@link SubscriptionStatus.extension}
	 * , this method returns string representation of "extension"
	 * 
	 * @return
	 */
	public String getStatusExtension() {
		return statusExtension;
	}

	/**
	 * Indicates if "retry-after" was present in exchanged messages. If this
	 * method returns true, enabler user should retry after some time with new
	 * enabler instance.
	 * 
	 * @return
	 */
	public boolean isRetryAfter() {
		return this.retryAfter != NO_TIMEOUT;
	}

	/**
	 * Return number of second, which enabler user must wait before his next
	 * subscription attempt.
	 * 
	 * @return
	 */
	public int getRetryAfter() {
		return retryAfter;
	}

	/**
	 * Determines if notifier returned expires time of subscription.
	 * 
	 * @return
	 */
	public boolean isExpires() {
		return this.expires != NO_TIMEOUT;
	}

	/**
	 * Return number of seconds subscription will be alive.
	 * 
	 * @return
	 */
	public int getExpires() {
		return expires;
	}

	/**
	 * 
	 * @return document send from notifier.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @return main content type of document send from notifier.
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 
	 * @return sub content type of document send from notifier.
	 */
	public String getContentSubType() {
		return contentSubType;
	}

	/**
	 * 
	 * @return notifier which sent notification.
	 */
	public String getNotifier() {
		return notifier;
	}

	/**
	 * 
	 * @return subscriber (us), which receives notification.
	 */
	public String getSubscriber() {
		return subscriber;
	}

	@Override
	public String toString() {
		return "Notify " + "[notifier=" + notifier + ", subscriber=" + subscriber + ", terminationReason=" + terminationReason
				+ ", terminationReasonExtension=" + terminationReasonExtension + ", status=" + status + ", statusExtension=" + statusExtension
				+ ", retryAfter=" + retryAfter + ", expires=" + expires + ", contentType=" + contentType + ", contentSubType=" + contentSubType
				+ ",  content=\n" + content + "\n]";
	}

}
