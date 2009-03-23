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