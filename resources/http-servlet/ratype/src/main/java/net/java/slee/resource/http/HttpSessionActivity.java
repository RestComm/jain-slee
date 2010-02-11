package net.java.slee.resource.http;

/**
 * HttpSessionActivity represents HttpSession created from incoming
 * javax.servlet.http.HttpServletRequest. The implementing ResourceAdaptor can
 * create a HttpSession calling getSession() on incoming request and assign the
 * session.getId() to SessionId of HttpSessionActivity. <br/> The SBB can
 * expicitly end the HttpSessionActivity by calling invalidate() on HttpSession
 * Object or when ever the HttpSession times out Web Container will invalidate
 * session. <br/>
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * @version 1.0
 * 
 */
public interface HttpSessionActivity {

	/**
	 * Retrieves the Session's ID.
	 * 
	 * @return Session ID
	 */
	public String getSessionId();
	

}
