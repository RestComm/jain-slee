package org.mobicents.ant.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;

import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class RemoveRaLinkTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.RemoveRaLinkTask.class.getName());

	public void run(SleeCommandInterface slee) {

		try {
			// Invoke the operation
			Object result = slee.invokeOperation(
					SleeCommandInterface.REMOVE_RA_LINK_OPERATION, linkName,
					null, null);

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

	// The setter for the "linkName" attribute
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	private String linkName = null;
}