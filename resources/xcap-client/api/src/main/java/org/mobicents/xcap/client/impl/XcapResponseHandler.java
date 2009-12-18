/**
 * 
 */
package org.mobicents.xcap.client.impl;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.XcapResponse;

/**
 * @author martins
 *
 */
public class XcapResponseHandler implements ResponseHandler<XcapResponse> {

	/* (non-Javadoc)
	 * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
	 */
	public XcapResponse handleResponse(HttpResponse httpResponse)
			throws ClientProtocolException, IOException {
		
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		
		String eTag = null;
		if(statusCode == 200 || statusCode == 201) {
			// sucessful response
			Header eTagHeader = httpResponse.getFirstHeader(XcapConstant.HEADER_ETAG);
			if(eTagHeader  != null) {
				eTag = eTagHeader.getValue();
			}			
		}
		
		final Header[] headers = httpResponse.getAllHeaders();
		
		final HttpEntity httpEntity = httpResponse.getEntity();
		
		final XcapEntityImpl xcapEntity = httpEntity != null ? new XcapEntityImpl(httpEntity) : null;
		
		return new XcapResponseImpl(statusCode, eTag, headers, xcapEntity);
	}

}
