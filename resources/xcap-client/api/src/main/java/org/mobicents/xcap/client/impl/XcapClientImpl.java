package org.mobicents.xcap.client.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.mobicents.xcap.client.XcapClient;
import org.mobicents.xcap.client.XcapConstant;
import org.mobicents.xcap.client.XcapResponse;
import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.header.Header;
import org.mobicents.xcap.client.impl.auth.CredentialsFactoryImpl;
import org.mobicents.xcap.client.impl.auth.CredentialsImpl;
import org.mobicents.xcap.client.impl.auth.SingleCredentialsProvider;
import org.mobicents.xcap.client.impl.header.HeaderFactoryImpl;
import org.mobicents.xcap.client.impl.header.HeaderImpl;

/**
 * Impl of the {@link XcapClient}.
 * 
 * @author martins
 * 
 */
public class XcapClientImpl implements XcapClient {

	private static final Log log = LogFactory.getLog(XcapClientImpl.class);

	private final DefaultHttpClient client;
	private final XcapResponseHandler responseHandler = new XcapResponseHandler();
	private final SingleCredentialsProvider credentialsProvider = new SingleCredentialsProvider();
	private final HeaderFactoryImpl headerFactory = new HeaderFactoryImpl();
	private final CredentialsFactoryImpl credentialsFactory = new CredentialsFactoryImpl();

	/**
	 * 
	 */
	public XcapClientImpl() {
		client = new DefaultHttpClient();
		client.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_0);
		client.getParams().setParameter(
				CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");
		client.setCredentialsProvider(credentialsProvider);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#getCredentialsFactory()
	 */
	public CredentialsFactoryImpl getCredentialsFactory() {
		return credentialsFactory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#getHeaderFactory()
	 */
	public HeaderFactoryImpl getHeaderFactory() {
		return headerFactory;
	}

	// CLIENT MANAGEMENT

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.xcap.client.XcapClient#setAuthenticationCredentials(org
	 * .mobicents.xcap.client.auth.Credentials)
	 */
	public void setAuthenticationCredentials(Credentials credentials) {
		credentialsProvider.setCredentials(((CredentialsImpl) credentials)
				.getWrappedCredentials());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.xcap.client.XcapClient#unsetAuthenticationCredentials()
	 */
	public void unsetAuthenticationCredentials() {
		credentialsProvider.setCredentials(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#shutdown()
	 */
	public void shutdown() {
		log.info("shutdown()");
		if (client != null) {
			client.getConnectionManager().shutdown();
		}
	}

	// CLIENT OPERATIONS

	private static final HttpContext NULL_HTTP_CONTEXT = null;

	private XcapResponse execute(URI uri, HttpUriRequest request,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {

		if (additionalRequestHeaders != null) {
			for (Header header : additionalRequestHeaders) {
				request.addHeader(((HeaderImpl) header).getWrappedHeader());
			}
		}

		HttpContext httpContext = NULL_HTTP_CONTEXT;
		if (credentials != null) {
			httpContext = new BasicHttpContext();
			httpContext.setAttribute(ClientContext.CREDS_PROVIDER,
					new SingleCredentialsProvider(
							((CredentialsImpl) credentials)
									.getWrappedCredentials()));
		}

		final XcapResponseImpl response = client.execute(request, responseHandler,
				httpContext);
		
		if (log.isDebugEnabled()) {
			log.debug("Received:\n--BEGIN--\n" + response.toString()
					+ "\n--END--");
		}

		return response;
	}

	private void setRequestEntity(HttpPut request, String content,
			String mimetype) throws UnsupportedEncodingException {
		request.setHeader(XcapConstant.HEADER_CONTENT_TYPE, mimetype);
		request.setEntity(new StringEntity(content, "UTF-8"));
	}

	private void setRequestEntity(HttpPut request, byte[] content,
			String mimetype) throws UnsupportedEncodingException {
		request.setHeader(XcapConstant.HEADER_CONTENT_TYPE, mimetype);
		request.setEntity(new ByteArrayEntity(content));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#get(java.net.URI,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("get(uri=" + uri + " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		return execute(uri,new HttpGet(uri), additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#put(java.net.URI,
	 * java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse put(URI uri, String mimetype, String content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("put(uri=" + uri + ", mimetype=" + mimetype
					+ ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		return execute(uri,request, additionalRequestHeaders, credentials);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("putIfMatch(uri=" + uri + ", eTag=" + eTag
					+ ", mimetype=" + mimetype + ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfNoneMatch(java.net.URI,
	 * java.lang.String, java.lang.String, java.lang.String,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("putIfNoneMatch(uri=" + uri + ", eTag=" + eTag
					+ ", mimetype=" + mimetype + ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#put(java.net.URI,
	 * java.lang.String, byte[], org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse put(URI uri, String mimetype, byte[] content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("put(uri=" + uri + ", mimetype=" + mimetype
					+ ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		return execute(uri,request, additionalRequestHeaders, credentials);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("putIfMatch(uri=" + uri + ", eTag=" + eTag
					+ ", mimetype=" + mimetype + ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#putIfNoneMatch(java.net.URI,
	 * java.lang.String, java.lang.String, byte[],
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("putIfNoneMatch(uri=" + uri + ", eTag=" + eTag
					+ ", mimetype=" + mimetype + ", content=" + content
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpPut request = new HttpPut(uri);
		setRequestEntity(request, content, mimetype);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#delete(java.net.URI,
	 * org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("delete(uri=" + uri + " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		return execute(uri,new HttpDelete(uri), additionalRequestHeaders,
				credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#deleteIfMatch(java.net.URI,
	 * java.lang.String, org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse deleteIfMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("deleteIfMatch(uri=" + uri + ", eTag=" + eTag
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpDelete request = new HttpDelete(uri);
		request.setHeader(XcapConstant.HEADER_IF_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.xcap.client.XcapClient#deleteIfNoneMatch(java.net.URI,
	 * java.lang.String, org.mobicents.xcap.client.header.Header[],
	 * org.mobicents.xcap.client.auth.Credentials)
	 */
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws IOException {

		if (log.isDebugEnabled()) {
			log.debug("deleteIfNoneMatch( uri = " + uri + " , eTag = " + eTag
					+ " , additionalRequestHeaders = ( "
					+ Arrays.toString(additionalRequestHeaders) + " ) )");
		}

		final HttpDelete request = new HttpDelete(uri);
		request.setHeader(XcapConstant.HEADER_IF_NONE_MATCH, eTag);
		return execute(uri,request, additionalRequestHeaders, credentials);
	}

}
