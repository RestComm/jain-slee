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

import org.mobicents.xcap.client.uri.encoding.UriComponentEncoder;

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
		if (documentSelector.length() != 0) {
			documentSelector.append(PATH_SEPARATOR);
		}
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
		return UriComponentEncoder.encodePath(this.toString());
	}
}
