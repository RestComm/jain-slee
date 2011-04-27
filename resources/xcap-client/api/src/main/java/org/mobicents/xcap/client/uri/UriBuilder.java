/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

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
