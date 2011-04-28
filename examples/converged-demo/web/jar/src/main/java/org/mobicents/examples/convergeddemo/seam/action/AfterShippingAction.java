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

package org.mobicents.examples.convergeddemo.seam.action;

import java.math.BigDecimal;

import javax.naming.InitialContext;
import javax.slee.EventTypeID;
import javax.slee.connection.ExternalActivityHandle;
import javax.slee.connection.SleeConnection;
import javax.slee.connection.SleeConnectionFactory;

import org.apache.log4j.Logger;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.mobicents.slee.service.events.CustomEvent;

/**
 * An example of a Seam component used to handle a jBPM transition event.
 * 
 * @author Amit Bhayani
 */
@Name("afterShipping")
public class AfterShippingAction {
	
	private static Logger logger = Logger.getLogger(AfterShippingAction.class);
	
	@In
	String customerfullname;

	@In
	String cutomerphone;

	@In
	BigDecimal amount;

	@In
	Long orderId;
	
	@In
	String userName;

	public void orderShipped() {
		logger.info("*************** Fire ORDER_SHIPPED  ***************************");
		logger.info("First Name = " + customerfullname);
		logger.info("Phone = " + cutomerphone);
		logger.info("orderId = " + orderId);

		Thread t = new Thread(new Runnable() {

			public void run() {

				try {

					InitialContext ic = new InitialContext();

					SleeConnectionFactory factory = (SleeConnectionFactory) ic
							.lookup("java:/MobicentsConnectionFactory");

					SleeConnection conn1 = null;
					conn1 = factory.getConnection();

					ExternalActivityHandle handle = conn1.createActivityHandle();

					EventTypeID requestType = conn1.getEventTypeID(
							"org.mobicents.slee.service.dvddemo.ORDER_SHIPPED",
							"org.mobicents", "1.0");
					CustomEvent customEvent = new CustomEvent(orderId, amount,
							customerfullname, cutomerphone, userName);

					conn1.fireEvent(customEvent, requestType, handle, null);
					conn1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
