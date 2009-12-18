package org.mobicents.slee.resource.xcapclient;

import java.util.List;

import org.openxdm.xcap.client.RequestHeader;
import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Activity object for doing async requests on the XCAP Client RA.
 * 
 * @author emmartins
 * 
 */
public interface AsyncActivity {

	/**
	 * Retrieves the XML resource from the XCAP server.
	 * @param key
	 * @param additionalRequestHeaders
	 */
	public void get(XcapUriKey key, List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param mimetype
	 * @param content
	 */
	public void put(XcapUriKey key, String mimetype, String content,
			List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP server.
	 * 
	 * @param mimetype
	 * @param content
	 */
	public void put(XcapUriKey key, String mimetype, byte[] content,
			List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(XcapUriKey key, String eTag, String mimetype,
			String content, List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag matches with
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content, List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			String content, List<RequestHeader> additionalRequestHeaders);

	/**
	 * Puts the specified content in the XCAP Server, if the ETag doesn't match
	 * the one in the server
	 * 
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(XcapUriKey key, String eTag, String mimetype,
			byte[] content, List<RequestHeader> additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server.
	 */
	public void delete(XcapUriKey key,
			List<RequestHeader> additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server, if the ETag matches the one in
	 * the server.
	 * 
	 * @param eTag
	 */
	public void deleteIfMatch(XcapUriKey key, String eTag,
			List<RequestHeader> additionalRequestHeaders);

	/**
	 * Deletes the resource in the XCAP Server, if the ETag doesn't match the
	 * one in the server.
	 * 
	 * @param eTag
	 */
	public void deleteIfNoneMatch(XcapUriKey key, String eTag,
			List<RequestHeader> additionalRequestHeaders);

	/**
	 * Ends this activity;
	 */
	public void endActivity();

}
