package org.mobicents.slee.resource.http;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

/**
 * 
 * @author martins
 *
 */
public class HttpServletRaSessionListener implements HttpSessionListener {

	private static final Logger log = Logger
			.getLogger(HttpServletRaSessionListener.class.getName());

	/**
	 * even if multiple listeners are instanciated only one becomes active
	 */
	private static AtomicBoolean masterListener = new AtomicBoolean(true);
	
	private boolean active;
	
	public HttpServletRaSessionListener() {
		if (masterListener.compareAndSet(true, false)) {
			active = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		if (active) {
			if (log.isDebugEnabled()) 
				log.debug("sessionCreated sessionId = "
						+ httpSessionEvent.getSession().getId());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		if (active) {
			if (log.isDebugEnabled())
				log.debug("sessionDestroyed called for sessionId = "
						+ httpSessionEvent.getSession().getId());

			HttpSession session = httpSessionEvent.getSession();
			HttpSessionWrapper wrapper = new HttpSessionWrapper(session);
			String name = wrapper.getResourceEntryPoint();
			if (name != null) {
				HttpServletResourceEntryPoint resourceEntryPoint = HttpServletResourceEntryPointManager.getResourceEntryPoint(name);
				if (resourceEntryPoint != null) {
					resourceEntryPoint.onSessionTerminated(session.getId());
				}
			}
		}
	}

}
