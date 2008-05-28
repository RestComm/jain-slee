package org.mobicents.slee.resource.http;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class HttpServletRaSessionListener implements HttpSessionListener {

	private static Logger log = Logger
			.getLogger(HttpServletRaSessionListener.class.getName());

	private static final String RA_ENTRY_POINT_PARAM = "ra-entry-point-jndi-name";

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		log.debug("sessionCreated sessionId = "
				+ httpSessionEvent.getSession().getId());

	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		log.debug("sessionDestroyed called for sessionId = "
				+ httpSessionEvent.getSession().getId());

		HttpSession session = httpSessionEvent.getSession();
		HttpServletResourceEntryPoint resourceEntryPoint = (HttpServletResourceEntryPoint) session
				.getAttribute(RA_ENTRY_POINT_PARAM);

		if (resourceEntryPoint != null) {
			resourceEntryPoint.endHttpSessionActivity(session.getId());
			log.debug("sessionDestroyed for sessionId = "
					+ httpSessionEvent.getSession().getId());
		}

	}

}
