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

package org.mobicents.ant.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;

import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class ChangeSleeStateTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.ChangeSleeStateTask.class
					.getName());

	public void run(SleeCommandInterface slee) {
		String command = null;

		if (state.equals(START)) {
			command = SleeCommandInterface.START_SLEE_OPERATION;
		} else if (state.equals(STOP)) {
			command = SleeCommandInterface.STOP_SLEE_OPERATION;
		}

		if (state.equals(NO_CHANGE)) {
			logger.warning("Bad result: state=" + START + " or state=" + STOP);
		} else {
			try {
				// Invoke the operation
				Object result = slee.invokeOperation(command, null, null, null);

				if (result == null) {
					logger.info("No response");
				} else {
					logger.info(result.toString());
				}
			} catch (java.lang.SecurityException seEx) {
				throw new BuildException(seEx);
			} catch (Exception ex) {
				// Log the error
				logger.log(Level.WARNING, "Bad result: " + slee.commandBean
						+ "." + slee.commandString + "\n"
						+ ex.getCause().toString());
			}
		}
	}

	// The setter for the "state" attribute
	public void setState(String state) {
		this.state = state;
	}

	private String state = NO_CHANGE;
}