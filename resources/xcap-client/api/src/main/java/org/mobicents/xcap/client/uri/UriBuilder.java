/**
 * 
 */
package org.mobicents.xcap.client.uri;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author martins
 *
 */
public class UriBuilder {

	private String schemeAndAuthority;
	private String xcapRoot;
	private String documentSelector;
	private String elementSelector;
	private String terminalSelector;
	private String namespaceBindings;
	
	/**
	 * @return the schemeAndAuthority
	 */
	public String getSchemeAndAuthority() {
		return schemeAndAuthority;
	}
	/**
	 * @param schemeAndAuthority the schemeAndAuthority to set
	 */
	public UriBuilder setSchemeAndAuthority(String schemeAndAuthority) {
		this.schemeAndAuthority = schemeAndAuthority;
		return this;
	}
	
	/**
	 * @return the xcapRoot
	 */
	public String getXcapRoot() {
		return xcapRoot;
	}
	/**
	 * @param xcapRoot the xcapRoot to set
	 */
	public UriBuilder setXcapRoot(String xcapRoot) {
		this.xcapRoot = xcapRoot;
		return this;
	}
	/**
	 * @return the documentSelector
	 */
	public String getDocumentSelector() {
		return documentSelector;
	}
	/**
	 * @param documentSelector the documentSelector to set
	 */
	public UriBuilder setDocumentSelector(String documentSelector) {
		this.documentSelector = documentSelector;
		return this;
	}
	
	/**
	 * @return the elementSelector
	 */
	public String getElementSelector() {
		return elementSelector;
	}
	
	/**
	 * @param elementSelector the elementSelector to set
	 */
	public UriBuilder setElementSelector(String elementSelector) {
		this.elementSelector = elementSelector;
		return this;
	}
	
	/**
	 * @return the terminalSelector
	 */
	public String getTerminalSelector() {
		return terminalSelector;
	}
	
	/**
	 * @param terminalSelector the terminalSelector to set
	 */
	public UriBuilder setTerminalSelector(String terminalSelector) {
		this.terminalSelector = terminalSelector;
		return this;
	}
	
	
	/**
	 * @return the namespaceBindings
	 */
	public String getNamespaceBindings() {
		return namespaceBindings;
	}
	/**
	 * @param namespaceBindings the namespaceBindings to set
	 */
	public UriBuilder setNamespaceBindings(String namespaceBindings) {
		this.namespaceBindings = namespaceBindings;
		return this;
	}
	
	private static final String RESOURCE_SELECTOR_SEPARATOR = "/~~";
	
	public URI toURI() throws URISyntaxException {
		final StringBuilder sb = new StringBuilder(schemeAndAuthority);
		if (xcapRoot != null) {
			sb.append(xcapRoot);
		}
		sb.append(documentSelector);
		if (elementSelector != null) {
			sb.append(RESOURCE_SELECTOR_SEPARATOR).append(elementSelector);
			if (terminalSelector != null) {
				sb.append(terminalSelector);
			}	
			if (namespaceBindings != null) {
				sb.append('?').append(namespaceBindings);
			}
		}
		
		return new URI(sb.toString());
	}
	
	
}
