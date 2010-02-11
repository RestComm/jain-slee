package net.java.slee.resource.http;


/**
 * HttpServletRequestActivity represents an incoming
 * javax.servlet.http.HttpServletRequest. The implementing ResourceAdaptor ends
 * the activity after posting the request event that created the activity.<br/>
 * 
 * @author Eduardo Martins
 * @version 1.0
 * 
 */
public interface HttpServletRequestActivity {

	/**
	 * Retrieves the Request's ID.
	 * 
	 * @return 
	 */
	public Object getRequestID();
	

}
