package org.mobicents.xcap.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.mobicents.xcap.client.XcapClient;
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.XcapResponse;

public class XcapClientImpl implements XcapClient {
	
	private static final Log log = LogFactory.getLog(XcapClientImpl.class);
	
	private final DefaultHttpClient client;
	private final XcapResponseHandler responseHandler = new XcapResponseHandler();
	
	public XcapClientImpl() {		
		client = new DefaultHttpClient();
      	client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, 
      		    HttpVersion.HTTP_1_0);
      	client.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, 
      		    "UTF-8");
	}
		
	// CLIENT MANAGEMENT
	
	public void setAuthenticationCredentials(String userName, String password) {
		client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName,password));
	}
	
	public void unsetAuthenticationCredentials() {
		client.getCredentialsProvider().clear();
	}
	
	public void shutdown() {		
		log.info("shutdown()");
		if(client != null) {
			client.getConnectionManager().shutdown();
		}
	}
	
	// CLIENT OPERATIONS 
	
	private XcapResponse execute(HttpUriRequest request, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (additionalRequestHeaders != null) {
			for(Header header : additionalRequestHeaders) {
				request.addHeader(header);
			}
		}
		
		final XcapResponse response = client.execute(request,responseHandler);
        
        if (log.isDebugEnabled()) {
			log.debug("Received:\n--BEGIN--\n"+response.toString()+"\n--END--");
		}
				
		return response;
	}
	
	private void setRequestEntity(HttpPut request, String content, String mimetype) throws UnsupportedEncodingException {
		request.setHeader(XcapConstant.HEADER_CONTENT_TYPE,mimetype);
		request.setEntity(new StringEntity(content,"UTF-8"));
	}
	
	private void setRequestEntity(HttpPut request, byte[] content, String mimetype) throws UnsupportedEncodingException {
		request.setHeader(XcapConstant.HEADER_CONTENT_TYPE,mimetype);
		request.setEntity(new ByteArrayEntity(content));
	}
	
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {		
		
		if (log.isDebugEnabled()) {
			log.debug("get(uri="+uri+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		return execute(new HttpGet(uri),additionalRequestHeaders);        
	}
	
	public XcapResponse put(URI uri, String mimetype, String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("put(uri="+uri+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		return execute(request,additionalRequestHeaders);
								
	}
	
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype, String content, Header[] additionalRequestHeaders)  throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("putIfMatch(uri="+uri+", eTag="+eTag+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
		
	}
	
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype, String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("putIfNoneMatch(uri="+uri+", eTag="+eTag+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
		
	}		
	
	public XcapResponse put(URI uri, String mimetype, byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("put(uri="+uri+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		return execute(request,additionalRequestHeaders);
		
	}
	
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype, byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("putIfMatch(uri="+uri+", eTag="+eTag+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");			
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
	}
	
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype, byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("putIfNoneMatch(uri="+uri+", eTag="+eTag+", mimetype="+mimetype+", content="+content+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
	}
	
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("delete(uri="+uri+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		return execute(new HttpDelete(uri),additionalRequestHeaders);
	}
	
	public XcapResponse deleteIfMatch(URI uri, String eTag, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {

		if (log.isDebugEnabled()) {
			log.debug("deleteIfMatch(uri="+uri+", eTag="+eTag+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpDelete request = new HttpDelete(uri);
		request.setHeader(XcapConstant.HEADER_IF_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
	}
	
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException {
		
		if (log.isDebugEnabled()) {
			log.debug("deleteIfNoneMatch( uri = "+uri+" , eTag = "+eTag+" , additionalRequestHeaders = ( "+additionalRequestHeaders+" ) )");
		}
		
		final HttpDelete request = new HttpDelete(uri);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH,eTag);
		return execute(request,additionalRequestHeaders);
	}
	
}
