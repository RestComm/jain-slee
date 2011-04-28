/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.example.slee.connection;

import javax.naming.InitialContext;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;

import org.apache.log4j.Logger;
import org.mobicents.example.slee.connection.SleeConnectionTestMBean;
import org.mobicents.slee.service.events.CustomEvent;

public class SleeConnectionTest implements SleeConnectionTestMBean {

	private static final Logger logger = Logger
			.getLogger(SleeConnectionTest.class);

	private final static String eventName = "org.mobicents.slee.service.connectivity.Event_1";
	private final static String eventVendor = "org.mobicents";
	private final static String eventVersion = "1.0";

	public void fireEvent(String messagePassed) {

		// depending on deployment it does following:
		// 1. lookup RA and make RMI calls through it
		// 2. lookup local Bean, which makes direct calls to container!
		logger.info("Attempting call to SleeConnectionFactory.");
		try {

			// this is called for local container, that is - RA is deployed
			// localy, and it accessess
			// remote host with SLEE, no props required here in CTX
			// initialization
			InitialContext ic = new InitialContext();
			// this is call to local JNDI space, private, it cant be accessed
			// from other JVM
			SleeConnectionFactory factory = (SleeConnectionFactory) ic
					.lookup("java:/MobicentsConnectionFactory");

			SleeConnection conn1 = null;
			try {
				conn1 = factory.getConnection();

				ExternalActivityHandle handle = conn1.createActivityHandle();

				EventTypeID requestType = conn1.getEventTypeID(eventName,
						eventVendor, eventVersion);
				CustomEvent customEvent = new CustomEvent();
				customEvent.setMessage(messagePassed);
				logger.info("The event type is: " + requestType);

				conn1.fireEvent(customEvent, requestType, handle, null);
			} finally {
				if (conn1 != null)
					conn1.close();
			}

		} catch (Exception e) {
			logger.error("Exception caught in event fire method!", e);

		}

	}

}
