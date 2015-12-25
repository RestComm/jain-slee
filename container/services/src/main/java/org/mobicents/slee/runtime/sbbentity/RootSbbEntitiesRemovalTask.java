/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2014, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 * This file incorporates work covered by the following copyright contributed under the GNU LGPL : Copyright 2007-2011 Red Hat.
 */

package org.mobicents.slee.runtime.sbbentity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.util.concurrent.SleeThreadFactory;

public class RootSbbEntitiesRemovalTask implements Runnable {

	private static final Logger logger = Logger
			.getLogger(RootSbbEntitiesRemovalTask.class);

    private final static SleeThreadFactory SLEE_THREAD_FACTORY = new SleeThreadFactory("SLEE-RootSbbEntitiesRemovalTask");

    // the service id of the task
	private ServiceComponent serviceComponent;

	/**
	 * Constructs a new instance of a timer task that removes all sbb entities
	 * from the service with the specified service id.
	 * 
	 * @param serviceComponent
	 */
	public RootSbbEntitiesRemovalTask(ServiceComponent serviceComponent) {
		this.serviceComponent = serviceComponent;
	}

	private void removeAllSbbEntities() {

		if (logger.isDebugEnabled()) {
			logger.debug("SBB Entities REMOVAL STARTING for service "
					+ serviceComponent);
		}

		final SleeContainer sleeContainer = SleeContainer.lookupFromJndi();

		for (final SbbEntityID sbbEntityID : sleeContainer
				.getSbbEntityFactory().getRootSbbEntityIDs(
						serviceComponent.getServiceID())) {

			try {
				sleeContainer.getTransactionManager().begin();

				SbbEntity sbbEntity = sleeContainer.getSbbEntityFactory()
						.getSbbEntity(sbbEntityID, true);

				if (sbbEntity != null) {
					// finally force sbb entity removal
					sleeContainer.getSbbEntityFactory().removeSbbEntity(
							sbbEntity, false);
				}
			} catch (Exception e) {
				// ignore
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}

			try {
				sleeContainer.getTransactionManager().commit();
			} catch (Exception e) {
				// ignore
				if (logger.isDebugEnabled()) {
					logger.debug(e.getMessage(), e);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SBB Entities REMOVAL ENDED for service "
					+ serviceComponent);
		}

	}

	@Override
	public void run() {

		if (logger.isDebugEnabled()) {
			logger.debug("RUNNING TASK ON SERVICE UNINSTALL FOR "
					+ serviceComponent);
		}

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					removeAllSbbEntities();
				} catch (Exception e) {
					logger.error(
							"Failed to execute task to remove pending root sbb entities of "
									+ serviceComponent, e);
				}
			}
		};

		ExecutorService exec = Executors.newSingleThreadExecutor(SLEE_THREAD_FACTORY);
		try {
			exec.submit(r).get();
		} catch (Throwable e) {
			logger.error(e);
		}
		exec.shutdown();
	}

}
