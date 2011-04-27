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

package org.mobicents.slee.resource.sip11.test.eventidcache;

import java.text.ParseException;
import java.util.ListIterator;

import javax.sip.SipException;
import javax.sip.header.ContentDispositionHeader;
import javax.sip.header.ContentEncodingHeader;
import javax.sip.header.ContentLanguageHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.Header;

public class ResponseMessage implements javax.sip.message.Response {

	private final int status;
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public ResponseMessage(int status) {
		this.status = status;
	}
	
	public String getReasonPhrase() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStatusCode() {
		return status;
	}

	public void setReasonPhrase(String arg0) throws ParseException {
		// TODO Auto-generated method stub
		
	}

	public void setStatusCode(int arg0) throws ParseException {
		// TODO Auto-generated method stub
		
	}

	public void addFirst(Header arg0) throws SipException, NullPointerException {
		// TODO Auto-generated method stub
		
	}

	public void addHeader(Header arg0) {
		// TODO Auto-generated method stub
		
	}

	public void addLast(Header arg0) throws SipException, NullPointerException {
		// TODO Auto-generated method stub
		
	}

	public Object getContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentDispositionHeader getContentDisposition() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentEncodingHeader getContentEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentLanguageHeader getContentLanguage() {
		// TODO Auto-generated method stub
		return null;
	}

	public ContentLengthHeader getContentLength() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExpiresHeader getExpires() {
		// TODO Auto-generated method stub
		return null;
	}

	public Header getHeader(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public byte[] getRawContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSIPVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator getUnrecognizedHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeContent() {
		// TODO Auto-generated method stub
		
	}

	public void removeFirst(String arg0) throws NullPointerException {
		// TODO Auto-generated method stub
		
	}

	public void removeHeader(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void removeLast(String arg0) throws NullPointerException {
		// TODO Auto-generated method stub
		
	}

	public void setContent(Object arg0, ContentTypeHeader arg1)
			throws ParseException {
		// TODO Auto-generated method stub
		
	}

	public void setContentDisposition(ContentDispositionHeader arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setContentEncoding(ContentEncodingHeader arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setContentLanguage(ContentLanguageHeader arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setContentLength(ContentLengthHeader arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setExpires(ExpiresHeader arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setHeader(Header arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setSIPVersion(String arg0) throws ParseException {
		// TODO Auto-generated method stub
		
	}

}
