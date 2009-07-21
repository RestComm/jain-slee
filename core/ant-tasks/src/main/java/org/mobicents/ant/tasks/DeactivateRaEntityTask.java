package org.mobicents.ant.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;

import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class DeactivateRaEntityTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.DeactivateRaEntityTask.class
					.getName());

	public void run(SleeCommandInterface slee) {

		try {
			// Invoke the operation
			Object result = slee.invokeOperation(
					SleeCommandInterface.DEACTIVATE_RA_ENTITY_OPERATION,
					entityName, null, null);

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
		
		// wait a few of seconds, give it time to complete deactivation, else
		// a further entity removal invocation will fail
		// TODO instead pool the resource management mbean to learn when
		// deactivation is complete
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// The setter for the "entityName" attribute
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	private String entityName = null;
}