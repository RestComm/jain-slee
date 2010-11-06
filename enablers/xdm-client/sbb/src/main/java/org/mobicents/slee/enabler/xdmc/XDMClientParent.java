/**
 * 
 */
package org.mobicents.slee.enabler.xdmc;

import java.net.URI;

/**
 * @author martins
 * 
 */
public interface XDMClientParent {

	/**
	 * Provides the response for an XML resource GET request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param mimetype
	 * @param content
	 * @param eTag
	 */
	public void getResponse(URI uri, int responseCode, String mimetype,
			String content, String eTag);

	/**
	 * Provides the response for an XML resource PUT request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void putResponse(URI uri, int responseCode, String responseContent,
			String eTag);

	/**
	 * Provides the response for an XML resource DELETE request.
	 * 
	 * @param uri
	 * @param responseCode
	 * @param eTag
	 * @param responseContent
	 */
	public void deleteResponse(URI uri, int responseCode,
			String responseContent, String eTag);

	/**
	 * Callback that indicates a document subscribed in the XDM client was
	 * updated
	 */
	// public void documentUpdated(DocumentSelector documentSelector,
	// String oldETag, String newETag, String documentAsString);

	/**
	 * Callback that indicates a element in a subscribed document was updated
	 */
	// public void elementUpdated(DocumentSelector documentSelector,
	// NodeSelector nodeSelector, Map<String, String> namespaces,
	// String oldETag, String newETag, String documentAsString,
	// String elementAsString);

	/**
	 * Callback that indicates a attribute in a subscribed document was updated
	 */
	// public void attributeUpdated(DocumentSelector documentSelector,
	// NodeSelector nodeSelector, AttributeSelector attributeSelector,
	// Map<String, String> namespaces, String oldETag, String newETag,
	// String documentAsString, String attributeValue);

}
