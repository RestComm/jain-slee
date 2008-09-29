package org.mobicents.slee.resource.http;

import javax.servlet.http.HttpSession;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.CouldNotStartActivityException;

import net.java.slee.resource.http.HttpServletRaSbbInterface;
import net.java.slee.resource.http.HttpSessionActivity;

/**
 * 
 * Implementation class for HttpServletRaSbbInterface
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * 
 */
public class HttpServletRaSbbInterfaceImpl implements HttpServletRaSbbInterface {

	private final HttpServletResourceAdaptor ra;
	
	public HttpServletRaSbbInterfaceImpl(HttpServletResourceAdaptor ra) {
		this.ra = ra;
	}
	
	public HttpSessionActivity getHttpSessionActivity(HttpSession httpSession) throws NullPointerException, IllegalStateException, ActivityAlreadyExistsException, CouldNotStartActivityException {
		return ra.getHttpSessionActivityImpl(httpSession);
	}
}
