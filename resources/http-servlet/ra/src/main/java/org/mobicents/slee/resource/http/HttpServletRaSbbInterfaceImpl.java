package org.mobicents.slee.resource.http;

import javax.servlet.http.HttpSession;
import javax.slee.SLEEException;
import javax.slee.resource.ActivityAlreadyExistsException;
import javax.slee.resource.StartActivityException;

import net.java.slee.resource.http.HttpServletRaSbbInterface;
import net.java.slee.resource.http.HttpSessionActivity;

/**
 * 
 * Implementation class for HttpServletRaSbbInterface
 * 
 * @author Ivelin Ivanov
 * @author amit.bhayani
 * @author martins
 * 
 */
public class HttpServletRaSbbInterfaceImpl implements HttpServletRaSbbInterface {

	/**
	 * 
	 */
	private final HttpServletResourceAdaptor ra;
	
	/**
	 * 
	 * @param ra
	 */
	public HttpServletRaSbbInterfaceImpl(HttpServletResourceAdaptor ra) {
		this.ra = ra;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.java.slee.resource.http.HttpServletRaSbbInterface#getHttpSessionActivity(javax.servlet.http.HttpSession)
	 */
	public HttpSessionActivity getHttpSessionActivity(HttpSession httpSession)
			throws NullPointerException, IllegalArgumentException, IllegalStateException,
			ActivityAlreadyExistsException, StartActivityException,
			SLEEException {
		if (httpSession == null) {
			throw new NullPointerException("null http session");
		}
		if (!(httpSession instanceof HttpSessionWrapper)) {
			throw new IllegalArgumentException();
		}
		final HttpSessionWrapper httpSessionWrapper = (HttpSessionWrapper) httpSession;
		final HttpSessionActivityImpl activity = new HttpSessionActivityImpl(httpSession.getId());
		if (httpSessionWrapper.getResourceEntryPoint() == null) {
			ra.getSleeEndpoint().startActivitySuspended(activity,activity);
			httpSessionWrapper.setResourceEntryPoint(ra.getName());
		}
		return activity;
	}
}
