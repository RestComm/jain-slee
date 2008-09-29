package net.java.slee.resource.http;

import javax.servlet.http.HttpSession;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;


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
	 * {@link HttpSession}. If the activity does not exist a new one is
	 * created.
	 * 
	 * @param httpSession
	 * @return
	 * @throws CouldNotStartActivityException 
	 * @throws ActivityAlreadyExistsException 
	 * @throws IllegalStateException 
	 * @throws NullPointerException 
	 */
	public HttpSessionActivity getHttpSessionActivity(HttpSession httpSession) throws NullPointerException, IllegalStateException, ActivityAlreadyExistsException, CouldNotStartActivityException;
	
}
