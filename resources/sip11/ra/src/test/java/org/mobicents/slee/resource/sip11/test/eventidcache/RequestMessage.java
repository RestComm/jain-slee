package org.mobicents.slee.resource.sip11.test.eventidcache;

import java.text.ParseException;
import java.util.ListIterator;

import javax.sip.SipException;
import javax.sip.address.URI;
import javax.sip.header.ContentDispositionHeader;
import javax.sip.header.ContentEncodingHeader;
import javax.sip.header.ContentLanguageHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.Header;

public class RequestMessage implements javax.sip.message.Request {

	private final String method;
	
	public RequestMessage(String method) {
		this.method = method;
	}
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getMethod() {
		return method;
	}

	public URI getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMethod(String arg0) throws ParseException {
		// TODO Auto-generated method stub
		
	}

	public void setRequestURI(URI arg0) {
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
