package org.mobicents.xcap.client;

import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

public interface XcapClient {

	/**
	 * 
	 * Sets the username and password for authentication in the server. 
	 * 
	 * @param userName
	 * @param password
	 */
	public void setAuthenticationCredentials(String userName, String password);
	
	/**
	 * Unsets the authentication credentials.
	 */
	public void unsetAuthenticationCredentials();
	
	/**
	 * Shutdown the client.
	 */
	public void shutdown();

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified key.
	 * @param uri
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public XcapResponse get(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key.
	 * 
	 * @param uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, String content, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key.
	 * 
	 * @param uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse put(URI uri, String mimetype, byte[] content, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag matches the current one on the server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag,
			String mimetype, String content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse putIfNoneMatch(URI uri, String eTag,
			String mimetype, byte[] content, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key.
	 * 
	 * @param uri
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse delete(URI uri, Header[] additionalRequestHeaders) throws ClientProtocolException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key, if the specified
	 * ETag matches the current one on the server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse deleteIfMatch(URI uri, String eTag, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	 * @param uri
	 * @param eTag
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public XcapResponse deleteIfNoneMatch(URI uri, String eTag, Header[] additionalRequestHeaders)
			throws ClientProtocolException, IOException;

}
