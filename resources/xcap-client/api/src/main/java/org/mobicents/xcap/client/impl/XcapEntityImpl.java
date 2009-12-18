/**
 * 
 */
package org.mobicents.xcap.client.impl;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.mobicents.xcap.client.XcapEntity;
import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class XcapEntityImpl implements XcapEntity {

	private final HttpEntity httpEntity;
	private final byte[] rawContent;
	
	private String contentAsString;
	
	/**
	 * @param httpEntity
	 * @throws IOException 
	 */
	public XcapEntityImpl(HttpEntity httpEntity) throws IOException {
		this.httpEntity = httpEntity;
		rawContent = EntityUtils.toByteArray(httpEntity);
		httpEntity.consumeContent();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#getContentAsString()
	 */
	public String getContentAsString() {
		if (contentAsString == null) {
			contentAsString = new String(rawContent, UriComponentEncoder.UTF8_CHARSET);
		}
		return contentAsString;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#getContentEncoding()
	 */
	public Header getContentEncoding() {
		return httpEntity.getContentEncoding();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#getContentLength()
	 */
	public long getContentLength() {
		return httpEntity.getContentLength();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#getContentType()
	 */
	public Header getContentType() {
		return httpEntity.getContentType();
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#getRawContent()
	 */
	public byte[] getRawContent() {
		return rawContent;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.xcap.client.XcapEntity#isChunked()
	 */
	public boolean isChunked() {
		return httpEntity.isChunked();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getContentAsString();
	}
}
