/**
 * 
 */
package org.mobicents.xcap.client.uri;

import org.mobicents.xcap.client.uri.enconding.UriComponentEncoder;

/**
 * @author martins
 *
 */
public class DocumentSelectorBuilder {

	private static final char PATH_SEPARATOR = '/';
	private static final String USER_DOC_PARENT = "users";
	private static final String GLOBAL_DOC_PARENT = "global";
	
	private final StringBuilder documentSelector = new StringBuilder();
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public DocumentSelectorBuilder appendPathSegment(String pathSegment) {
		documentSelector.append(PATH_SEPARATOR);
		documentSelector.append(pathSegment);
		return this;
	}
	
	public static DocumentSelectorBuilder getUserDocumentSelectorBuilder(String auid, String user, String documentName) {
		return new DocumentSelectorBuilder().appendPathSegment(auid).appendPathSegment(USER_DOC_PARENT).appendPathSegment(user).appendPathSegment(documentName);
	}
	
	public static DocumentSelectorBuilder getGlobalDocumentSelectorBuilder(String auid, String documentName) {
		return new DocumentSelectorBuilder().appendPathSegment(auid).appendPathSegment(GLOBAL_DOC_PARENT).appendPathSegment(documentName);		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return documentSelector.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String toPercentEncodedString() {
		return UriComponentEncoder.encodeQuery(this.toString());
	}
}
