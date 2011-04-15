/**
 * 
 */
package org.mobicents.slee.enabler.xdmc;

import java.io.IOException;
import java.net.URI;

import org.mobicents.slee.enabler.sip.SubscriptionException;
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

	/**
	 * Method which triggers subscription to changes in XDMS resources. Depending on implementation of enabler it can use no-patching, xcap-diff or aggregated mode.
	 * @param subscriber - address that identifies local entity
	 * @param notifier - address that  identifies remote entity(ie. XCAP Diff entity), ie. sip:tests@xcap.example.com
	 * @param expires - duration of subscription, in seconds.
	 * @param resourceURIs - array of resource uris to which enabler will subscribe, ie. String{}[resource-lists/users/sip:joe@example.com/index,rls-services/users/sip:joe@example.com/index/~~/*\/service%5b@uri='sip:marketing@example.com'%5d]
	 * @throws SubscriptionException 
	 */
	public void subscribe(URI subscriber, URI notifier, int expires, String[] resourceURIs) throws SubscriptionException;
	
	/**
	 * Terminates a subscription to changes in XDMS resources.
	 * @param subscriber
	 * @param notifier
	 */
	public void unsubscribe(URI subscriber, URI notifier);

	
}
