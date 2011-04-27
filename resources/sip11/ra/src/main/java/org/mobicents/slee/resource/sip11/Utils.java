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

package org.mobicents.slee.resource.sip11;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class Utils {

	private static Set<String> DIALOG_CREATING_METHODS;
	
	/**
	 * 
	 * @return
	 */
	public static Set<String> getDialogCreatingMethods() {
		if (DIALOG_CREATING_METHODS == null) {
			final Set<String> set = new HashSet<String>();
			set.add(Request.INVITE);
			set.add(Request.REFER);
			set.add(Request.SUBSCRIBE);
			DIALOG_CREATING_METHODS = Collections.unmodifiableSet(set);
		}
		return DIALOG_CREATING_METHODS;
	}

	private static Set<String> HEADERS_TO_OMMIT_ON_REQUEST_COPY;
	
	/**
	 * 
	 * @return
	 */
	public static Set<String> getHeadersToOmmitOnRequestCopy() {
		if (HEADERS_TO_OMMIT_ON_REQUEST_COPY == null) {
			final Set<String> set = new HashSet<String>();
			set.add(RouteHeader.NAME);
			set.add(RecordRouteHeader.NAME);
			set.add(ViaHeader.NAME);
			set.add(CallIdHeader.NAME);
			set.add(CSeqHeader.NAME);
			set.add(FromHeader.NAME);
			set.add(ToHeader.NAME);
			set.add(ContentLengthHeader.NAME);
			HEADERS_TO_OMMIT_ON_REQUEST_COPY = Collections.unmodifiableSet(set);
		}
		return HEADERS_TO_OMMIT_ON_REQUEST_COPY;
	}
	
	private static Set<String> HEADERS_TO_OMMIT_ON_RESPONSE_COPY;

	/**
	 * 
	 * @return
	 */
	public static Set<String> getHeadersToOmmitOnResponseCopy() {
		if (HEADERS_TO_OMMIT_ON_RESPONSE_COPY == null) {
			final Set<String> set = new HashSet<String>();
			set.add(RouteHeader.NAME);
			set.add(RecordRouteHeader.NAME);
			set.add(ViaHeader.NAME);
			set.add(CallIdHeader.NAME);
			set.add(CSeqHeader.NAME);
			set.add(ContactHeader.NAME);
			set.add(FromHeader.NAME);
			set.add(ToHeader.NAME);
			set.add(ContentLengthHeader.NAME);
			HEADERS_TO_OMMIT_ON_RESPONSE_COPY = Collections.unmodifiableSet(set);
		}
		return HEADERS_TO_OMMIT_ON_RESPONSE_COPY;
	}
	
	/**
	 * Generates route list the same way dialog does.
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	public static List<RouteHeader> getRouteList(Response response, HeaderFactory headerFactory) throws ParseException {
		// we have record route set, as we are client, this is reversed
		final ArrayList<RouteHeader> routeList = new ArrayList<RouteHeader>();
		final ListIterator<?> rrLit = response.getHeaders(RecordRouteHeader.NAME);
		while (rrLit.hasNext()) {
			final RecordRouteHeader rrh = (RecordRouteHeader) rrLit.next();
			final RouteHeader rh = headerFactory.createRouteHeader(rrh.getAddress());
			final Iterator<?> pIt = rrh.getParameterNames();
			while (pIt.hasNext()) {
				final String pName = (String) pIt.next();
				rh.setParameter(pName, rrh.getParameter(pName));
			}
			routeList.add(0, rh);
		}
		return routeList;
	}
	
	/**
	 * Forges Request-URI using contact and To name par to address URI, this is
	 * required on dialog fork, this is how target is determined
	 * 
	 * @param response
	 * @return
	 * @throws ParseException 
	 */
	public static URI getRequestUri(Response response, AddressFactory addressFactory) throws ParseException {
		final ContactHeader contact = ((ContactHeader) response.getHeader(ContactHeader.NAME));
		return (contact != null) ? (URI) contact.getAddress().getURI().clone() : null;
	}
}
