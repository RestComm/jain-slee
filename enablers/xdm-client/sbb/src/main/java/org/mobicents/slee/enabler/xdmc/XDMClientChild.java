/**
 * 
 */
package org.mobicents.slee.enabler.xdmc;

import java.io.IOException;
import java.net.URI;

import org.mobicents.xcap.client.auth.Credentials;
import org.mobicents.xcap.client.auth.CredentialsFactory;

/**
 * @author martins
 *
 */
public interface XDMClientChild {

	/**
	 * Retrieves the factory to create request authentication credentials.
	 * @return
	 */
	public CredentialsFactory getCredentialsFactory();
	
	/**
	 * Sets the parent, which will be used by the client to provide async results.
	 * 
	 * @param parent
	 */
	public void setParentSbb(XDMClientParentSbbLocalObject parentSbb);

	// --- get/put/delete interface methods

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * Response is async.
	 * @param uri
	 *            the request uri
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void get(URI uri, Credentials credentials) throws IOException;

	/**
	 * Retrieves the XML resource from the XCAP server, for the specified uri.
	 * Response is async.
	 * @param uri
	 *            the request uri
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void get(URI uri, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void put(URI uri, String mimetype, byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server.
	 * 
	 * @param uri
	 *            the request uri
	 * @param mimetype
	 *            the mimetype of the content to put, for document each XCAP App
	 *            Usage defines their own mimetype, but for elements and
	 *            attributes you can use {@link ElementResource} and
	 *            {@link AttributeResource} static MIMETYPE fields.
	 * @param content
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void put(URI uri, String mimetype, byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag matches the current one on the server.
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
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag matches the current one on the server.
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
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag does not matches the current one on the
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
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Credentials credentials) throws IOException;

	/**
	 * Puts the specified content in the XCAP Server, if the specified ETag does not matches the current one on the
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
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI.
	 * 
	  * @param uri
	 *            the request uri
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.            
	 * @throws IOException 
	 */
	public void delete(URI uri, Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI.
	 * 
	  * @param uri
	 *            the request uri
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void delete(URI uri, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void deleteIfMatch(URI uri, String eTag, Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void deleteIfMatch(URI uri, String eTag, String assertedUserId) throws IOException;
	
	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param credentials
	 *            authentication credentials, can be null for requests that
	 *            don't need authentication.
	 * @throws IOException 
	 */
	public void deleteIfNoneMatch(URI uri, String eTag, 
			Credentials credentials) throws IOException;

	/**
	 * Deletes the content related the specified XCAP URI, if the specified
	 * ETag does not matches the current one on the server.
	 * 
	  * @param uri
	 *            the request uri
	 * @param eTag
	 * @param assertedUserId
	 *            user id already authenticated 
	 * @throws IOException 
	 */
	public void deleteIfNoneMatch(URI uri, String eTag, 
			String assertedUserId) throws IOException;
	
	// --- subscribe/unsubscribe interface methods

	// TODO
	
	/**
	 * Subscribes changes on a XML document, stored on the XDM.
	 * @param xdmHost
	 * @param documentSelector
	 */
	//public void subscribeDocument(String xdmHost, DocumentSelector documentSelector);

	/**
	 * Unsubscribes changes on a XML document, stored on the XDM.
	 * @param xdmHost
	 * @param documentSelector
	 */
	//public void unsubscribeDocument(String xdmHost, DocumentSelector documentSelector);

	/**
	 * Subscribes changes on XML documents of the specified app usage, stored on
	 * the XDM.
	 * @param xdmHost
	 * @param auid
	 */
	//public void subscribeAppUsage(String xdmHost, String auid);

	/**
	 * Unsubscribes changes on XML documents of the specified app usage, stored
	 * on the XDM.
	 * @param xdmHost
	 * @param auid
	 */
	//public void unsubscribeAppUsage(String xdmHost, String auid);
	
}
