/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
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

	/**
	 * 
	 */
	Notify() {
		super();
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((contentSubType == null) ? 0 : contentSubType.hashCode());
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + expires;
		result = prime * result + ((notifier == null) ? 0 : notifier.hashCode());
		result = prime * result + retryAfter;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((statusExtension == null) ? 0 : statusExtension.hashCode());
		result = prime * result + ((subscriber == null) ? 0 : subscriber.hashCode());
		result = prime * result + ((terminationReason == null) ? 0 : terminationReason.hashCode());
		result = prime * result + ((terminationReasonExtension == null) ? 0 : terminationReasonExtension.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notify other = (Notify) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (contentSubType == null) {
			if (other.contentSubType != null)
				return false;
		} else if (!contentSubType.equals(other.contentSubType))
			return false;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (expires != other.expires)
			return false;
		if (notifier == null) {
			if (other.notifier != null)
				return false;
		} else if (!notifier.equals(other.notifier))
			return false;
		if (retryAfter != other.retryAfter)
			return false;
		if (status != other.status)
			return false;
		if (statusExtension == null) {
			if (other.statusExtension != null)
				return false;
		} else if (!statusExtension.equals(other.statusExtension))
			return false;
		if (subscriber == null) {
			if (other.subscriber != null)
				return false;
		} else if (!subscriber.equals(other.subscriber))
			return false;
		if (terminationReason != other.terminationReason)
			return false;
		if (terminationReasonExtension == null) {
			if (other.terminationReasonExtension != null)
				return false;
		} else if (!terminationReasonExtension.equals(other.terminationReasonExtension))
			return false;
		return true;
	}

}
