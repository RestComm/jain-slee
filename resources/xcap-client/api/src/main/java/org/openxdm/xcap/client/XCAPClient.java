package org.openxdm.xcap.client;

import java.io.IOException;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.openxdm.xcap.common.key.XcapUriKey;
import org.openxdm.xcap.common.resource.AttributeResource;
import org.openxdm.xcap.common.resource.ElementResource;

public interface XCAPClient {

	/**
	 * 
	 * Sets the username and passowrd for authentication in the server. 
	 * 
	 * @param userName
	 * @param password
	 */
	public void setAuthenticationCredentials(String userName, String password);
	
	/**
	 * Turns on or off the request authentication.
	 * 
	 * @param value
	 */
	public void setDoAuthentication(boolean value);
	
	/**
	 * Indicates if the client is set to do authentication on server.
	 * @return
	 */
	public boolean getDoAuthentication();
	
	/**
	 * Shutdown the client.
	 */
	public void shutdown();

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified key.
	 * @param key
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response get(XcapUriKey key, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key.
	 * 
	 * @param key
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response put(XcapUriKey key, String mimetype, String content, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key.
	 * 
	 * @param key
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response put(XcapUriKey key, String mimetype, byte[] content, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag matches the current one on the server.
	 * 
	 * @param key
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			String content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag matches the current one on the server.
	 * 
	 * @param key
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response putIfMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param key
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response putIfNoneMatch(XcapUriKey key, String eTag,
			String mimetype, String content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Puts the specified content in the XCAP Server, in the XCAP URI pointed by
	 * the key, if the specified ETag does not matches the current one on the
	 * server.
	 * 
	 * @param key
	 * @param eTag
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and attributes
	 *            you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response putIfNoneMatch(XcapUriKey key, String eTag,
			String mimetype, byte[] content, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key.
	 * 
	 * @param key
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response delete(XcapUriKey key, List<RequestHeader> additionalRequestHeaders) throws HttpException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key, if the specified
	 * ETag matches the current one on the server.
	 * 
	 * @param key
	 * @param eTag
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response deleteIfMatch(XcapUriKey key, String eTag, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException;

	/**
	 * Deletes the content related the specified XCAP URI key, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	 * @param key
	 * @param eTag
	 * @param additionalRequestHeaders a list containing {@link RequestHeader}s to add in the XCAP request
	 * @return
	 * @throws HttpException
	 * @throws IOException
	 */
	public Response deleteIfNoneMatch(XcapUriKey key, String eTag, List<RequestHeader> additionalRequestHeaders)
			throws HttpException, IOException;

}
