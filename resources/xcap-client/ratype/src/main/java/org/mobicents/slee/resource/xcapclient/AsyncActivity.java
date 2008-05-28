package org.mobicents.slee.resource.xcapclient;

import org.openxdm.xcap.common.key.XcapUriKey;

/**
 * Activity object for doing async requests on the XCAP Client RA.
 * @author emmartins
 *
 */
public interface AsyncActivity {	
	
	/**
	 * Retrieves the XML resource from the XCAP server.
	 */
	public void get(XcapUriKey key);
	
	/**
	 * Puts the specified content in the XCAP server.
	 * @param mimetype
	 * @param content
	 */
	public void put(XcapUriKey key,String mimetype, String content);

	/**
	 * Puts the specified content in the XCAP server.
	 * @param mimetype
	 * @param content
	 */
	public void put(XcapUriKey key,String mimetype, byte[] content);

	/**
	 *  Puts the specified content in the XCAP Server,
	 *  if the ETag matches with the one in the server
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(XcapUriKey key,String eTag, String mimetype,
			String content);

	/**
	 * Puts the specified content in the XCAP Server,
	 * if the ETag matches with the one in the server
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfMatch(XcapUriKey key,String eTag, String mimetype,
			byte[] content);

	/**
	 * Puts the specified content in the XCAP Server,
	 * if the ETag doesn't match the one in the server
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(XcapUriKey key,String eTag,
			String mimetype, String content);

	/**
	 * Puts the specified content in the XCAP Server,
	 * if the ETag doesn't match the one in the server
	 * @param eTag
	 * @param mimetype
	 * @param content
	 */
	public void putIfNoneMatch(XcapUriKey key,String eTag,
			String mimetype, byte[] content);

	/**
	 * Deletes the resource in the XCAP Server.
	 */
	public void delete(XcapUriKey key);

	/**
	 * Deletes the resource in the XCAP Server, if the 
	 * ETag matches the one in the server.
	 * @param eTag
	 */
	public void deleteIfMatch(XcapUriKey key,String eTag);

	/**
	 * Deletes the resource in the XCAP Server, if the 
	 * ETag doesn't match the one in the server.
	 * @param eTag
	 */
	public void deleteIfNoneMatch(XcapUriKey key,String eTag);
		
	/**
	 * Ends this activity;
	 */
	public void endActivity();
	
}
