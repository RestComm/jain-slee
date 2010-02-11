package org.mobicents.xcap.client;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.auth.Credentials;
import org.apache.http.client.ClientProtocolException;

public interface XcapClient {

	/**
	 * 
	 * Sets the global authentication credentials, those will be used when there
	 * are no credentials for the request. Those credentials are applied to any
	 * host.
	 * 
	 * @param credentials
	 */
	public void setAuthenticationCredentials(Credentials credentials);

	/**
	 * Unsets the authentication credentials.
	 */
	public void unsetAuthenticationCredentials();

	/**
	 * Shutdown the client.
	 */
	public void shutdown();

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, String content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, byte[] content,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the uri, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Deletes the content related the specified XCAP URI uri.
	 * 
	 * @param uri
	 *            the request uri
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders,
			Credentials credentials) throws ClientProtocolException,
			IOException;

	/**
	 * Deletes the content related the specified XCAP URI uri, if the specified
	 * ETag matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse deleteIfMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws ClientProtocolException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI uri, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param eTag
	 * @param additionalRequestHeaders
	 *            additional headers to include in the XCAP request
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders, Credentials credentials)
			throws ClientProtocolException, IOException;

}
