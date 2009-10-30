package net.java.slee.resource.http;

import javax.servlet.http.HttpSession;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;

/**
 * 
 * Provides SBB with an interface
 * 
 * @author Ivelin Ivanov
 * @version 1.0
 * 
 */
public interface HttpServletRaSbbInterface {

	/**
	 * Retrieves an {@link HttpSessionActivity} for the specified
	 * {@link HttpSession}. If the activity does not exist a new one is created.
	 * 
	 * @param httpSession
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException if the http session instance class is not recognizable
	 * @throws IllegalStateException
	 * @throws ActivityAlreadyExistsException
	 * @throws StartActivityException
	 * @throws SLEEException
	 */
	public HttpSessionActivity getHttpSessionActivity(HttpSession httpSession)
			throws  NullPointerException, IllegalArgumentException, IllegalStateException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException;

}
