/***************************************************
 *                                                 *
 *  Mobicents: The Open Source VoIP Platform       *
 *                                                 *
 *  Distributable under LGPL license.              *
 *  See terms of license at gnu.org.               *
 *                                                 *
 ***************************************************/
package org.mobicents.ant.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class DeactivateServiceTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.DeactivateServiceTask.class
					.getName());

	public void run(SleeCommandInterface slee)  {

		try {
			ComponentKey id = new ComponentKey(this.id);
			ServiceIDImpl service = new ServiceIDImpl(id);

			// Invoke the operation
			Object result = slee.invokeOperation("-deactivateService", service
					.toString(), null, null);

			if (result == null) {
				logger.info("No response");
			} else {
				logger.info(result.toString());
			}
		} catch (java.lang.SecurityException seEx) {
			throw new BuildException(seEx);
		} catch (Exception ex) {
			// Log the error
			logger.log(Level.WARNING, "Bad result: " + slee.commandBean + "."
					+ slee.commandString + "\n" + ex.getCause().toString());

		}
	}

	// The setter for the "id" attribute
	public void setServiceID(String id) {
		this.id = id;
	}

	private String id = null;
}