/**
 * 
 */
package org.mobicents.xcap.client.impl;

import java.io.Serializable;

import org.apache.http.Header;
import org.mobicents.xcap.client.XcapEntity;
import org.mobicents.xcap.client.XcapResponse;

/**
 * @author martins
 *
 */
public class XcapResponseImpl implements XcapResponse, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int statusCode;
	private final String eTag;
	private final Header[] headers;
	private final XcapEntity xcapEntity;
	
	/**
	 * @param statusCode
	 * @param eTag
	 * @param headers
	 * @param xcapEntity
	 */
	public XcapResponseImpl(int statusCode, String eTag, Header[] headers,
			XcapEntity xcapEntity) {
		this.statusCode = statusCode;
		this.eTag = eTag;
		this.headers = headers;
		this.xcapEntity = xcapEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getETag()
	 */
	public String getETag() {
		return eTag;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getEntity()
	 */
	public XcapEntity getEntity() {
		return xcapEntity;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getHeaders()
	 */
	public Header[] getHeaders() {
		return headers;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapResponse#getCode()
	 */
	public int getCode() {
		return statusCode;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Response {").append("\nStatusCode: ").append(statusCode).append("\nHeaders: ");
		boolean first=true;
		for(int i=0;i<headers.length;i++) {
			Header header = headers[i];
			if (!first) {
				sb.append(", ");
				
			} else {
				first=false;
			}
			sb.append(header.getName()).append("=").append(header.getValue());
		}
		
		sb.append("\nEntity:\n").append(xcapEntity).append("\n}");
		return sb.toString();
	}
	
}
