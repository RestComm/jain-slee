package org.mobicents.slee.resource.xcapclient;

import java.net.URI;

import org.apache.http.Header;

/**
 * Activity object for doing async requests on the XCAP Client RA.
 * 
 * @author emmartins
 * 
 */
public interface AsyncActivity {

	/**
	 * Retrieves the XML resource from the XCAP server.
	 * @param uri
	 * @param additionalRequestHeaders
	 */
	public void get(URI uri, Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param mimetype
	 * @param content
	 */
	public void put(URI uri, String mimetype, String content,
			Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param mimetype
	 * @param content
	 */
	public void put(URI uri, String mimetype, byte[] content,
			Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			String content, Header[] additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(URI uri, String eTag, String mimetype,
			byte[] content, Header[] additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server.
	 */
	public void delete(URI uri,
			Header[] additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server, if the ETag matches the one in
	 * the server.
	 * 
	 * @param eTag
	 */
	public void deleteIfMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server, if the ETag doesn't match the
	 * one in the server.
	 * 
	 * @param eTag
	 */
	public void deleteIfNoneMatch(URI uri, String eTag,
			Header[] additionalRequestHeaders);

	/**
	 * Ends this activity;
	 */
	public void endActivity();

}
