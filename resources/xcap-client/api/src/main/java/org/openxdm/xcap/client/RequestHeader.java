package org.openxdm.xcap.client;

/**
 * A name / value pair object wrapper, representing an xcap client request header.
 * 
 * @author martins
 *
 */
public class RequestHeader {

	/**
	 * the name of the request header
	 */
	private final String name;
	
	/**
	 * the value of the request header
	 */
	private final String value;
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public RequestHeader(String name, String value) {
		if (name == null) {
			throw new NullPointerException("null request header name");
		}
		this.name = name;
		this.value = value;
	}
	
	/**
	 * Retrieves the name of the request header
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the value of the request header
	 * @return
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "RequestHeader{ "+name+": "+value+" }";
	}
}
