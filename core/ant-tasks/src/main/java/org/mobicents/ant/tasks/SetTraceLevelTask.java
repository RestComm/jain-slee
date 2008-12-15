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

import javax.slee.InvalidArgumentException;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;

import org.mobicents.slee.container.component.ComponentKey;
import org.mobicents.slee.container.component.EventTypeIDImpl;
import org.mobicents.slee.container.component.ProfileSpecificationIDImpl;
import org.mobicents.slee.container.component.ResourceAdaptorIDImpl;
import org.mobicents.slee.container.component.SbbIDImpl;
import org.mobicents.slee.container.component.ServiceIDImpl;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;
import org.mobicents.slee.resource.ResourceAdaptorTypeIDImpl;

public class SetTraceLevelTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.SetTraceLevelTask.class
					.getName());

	public void run(SleeCommandInterface slee) {

		try {
			String component = null;

			if (type.equalsIgnoreCase("event")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new EventTypeIDImpl(componentKey).toString();

			} else if (type.equalsIgnoreCase("ra")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new ResourceAdaptorIDImpl(componentKey).toString();

			} else if (type.equalsIgnoreCase("ratype")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new ResourceAdaptorTypeIDImpl(componentKey)
						.toString();

			} else if (type.equalsIgnoreCase("pspec")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new ProfileSpecificationIDImpl(componentKey)
						.toString();

			} else if (type.equalsIgnoreCase("service")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new ServiceIDImpl(componentKey).toString();

			} else if (type.equalsIgnoreCase("sbb")) {
				ComponentKey componentKey = new ComponentKey(componentID);
				component = new SbbIDImpl(componentKey).toString();
			}

			else {
				throw new InvalidArgumentException(
						"Unrecognized component type: " + type + "\n"
								+ " Supported component types are: "
								+ "event, ra, ratype, pspec, service and sbb");
			}

			// Invoke the operation
			Object result = slee.invokeOperation("-setTraceLevel", component,
					level, null);

			if (result == null) {
				logger.info("No response");
			} else {
				logger.info(result.toString());
			}

		} catch (java.lang.SecurityException seEx) {
			throw new BuildException(seEx);
		} catch (InvalidArgumentException ex) {
			// Log the error
			logger.log(Level.WARNING, ex.getMessage());

		} catch (Exception ex) {
			// Log the error
			logger.log(Level.WARNING, "Bad result: " + slee.commandBean + "."
					+ slee.commandString + "\n" + ex.getCause().toString());
		}
	}

	// The setter for the "componentID" attribute
	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}

	// The setter for the "type" attribute
	public void setType(String type) {
		this.type = type;
	}

	// The setter for the "level" attribute
	public void setLevel(String level) {
		this.level = level;
	}

	private String componentID = null;

	private String type = null;

	private String level = null;
}