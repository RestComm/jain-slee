package org.mobicents.xcap.client.impl.header;

import org.apache.http.message.BasicHeader;
import org.mobicents.xcap.client.header.Header;

/**
 * Implementation for the {@link Header} interface, extending the Apache
 * {@link BasicHeader}.
 * 
 * @author martins
 * 
 */
public class HeaderImpl implements Header {

	private org.apache.http.Header wrappedHeader;

	/**
	 * 
	 * @param name
	 * @param value
	 */
	public HeaderImpl(String name, String value) {
		wrappedHeader = new BasicHeader(name, value);
	}

	/**
	 * 
	 * @param wrappedHeader
	 */
	public HeaderImpl(org.apache.http.Header wrappedHeader) {
		this.wrappedHeader = wrappedHeader;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.message.BasicHeader#getName()
	 */
	public String getName() {
		return wrappedHeader.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.http.message.BasicHeader#getValue()
	 */
	public String getValue() {
		return wrappedHeader.getValue();
	}

	/**
	 * 
	 * @return
	 */
	public org.apache.http.Header getWrappedHeader() {
		return wrappedHeader;
	}
	
	@Override
	public String toString() {
		return wrappedHeader.toString();
	}
}
