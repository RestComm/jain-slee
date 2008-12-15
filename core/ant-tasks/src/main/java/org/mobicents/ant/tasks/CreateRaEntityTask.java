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
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class CreateRaEntityTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.CreateRaEntityTask.class
					.getName());

	public void run(SleeCommandInterface slee) {

		try {
			ComponentKey raId = new ComponentKey(this.raId);
			ResourceAdaptorIDImpl ra = new ResourceAdaptorIDImpl(raId);

			// Invoke the operation
			Object result = slee.invokeOperation("-createRaEntity", ra
					.toString(), entityName, props);

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
	public void setResourceAdaptorID(String id) {
		this.raId = id;
	}

	// The setter for the "entityName" attribute
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	// The setter for the "props" attribute
	public void setProperties(String props) {
		this.props = props;
	}

	private String raId = null;

	private String entityName = null;

	private String props = null;
}