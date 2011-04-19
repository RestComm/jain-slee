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

package org.mobicents.slee.runtime.activity;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.activity.ActivityContextHandle;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class UnreferencedActivity2ndCheckTask implements Runnable {

	private static final Logger logger = Logger
			.getLogger(UnreferencedActivity2ndCheckTask.class);
	private static final SleeContainer sleeContainer = SleeContainer
			.lookupFromJndi();

	private final ActivityContextHandle ach;
	private final ActivityContextFactoryImpl factory;
	
	UnreferencedActivity2ndCheckTask(ActivityContextImpl ac) {
		this.ach = ac.getActivityContextHandle();
		this.factory = ac.getFactory();
	}

	public void run() {

		SleeTransactionManager txManager = sleeContainer
				.getTransactionManager();
		boolean rollback = true;
		try {
			txManager.begin();
			ActivityContextImpl ac = factory.getActivityContext(ach);
			if (ac != null && !ac.isEnding()) {
				ac.unreferencedActivity2ndCheck();
			}
			rollback = false;
		} catch (Exception e) {
			logger.error(
					"failure while running unrefered activity 2nd check for ac with id "
							+ ach, e);
		} finally {
			try {
				if (rollback) {
					txManager.rollback();
				} else {
					txManager.commit();
				}
			} catch (Exception e) {
				logger
						.error(
								"failure while ending tx, while running unrefered activity 2nd check for ac with id "
										+ ach, e);
			}
		}
	}
}
