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

import javax.slee.EventTypeID;
import javax.slee.InvalidArgumentException;
import javax.slee.SbbID;
import javax.slee.ServiceID;
import javax.slee.profile.ProfileSpecificationID;
import javax.slee.resource.ResourceAdaptorID;
import javax.slee.resource.ResourceAdaptorTypeID;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class SetTraceLevelTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.SetTraceLevelTask.class
					.getName());

	public void run(SleeCommandInterface slee) {

		try {
			String component = null;
			// FIXME normalize those string values with the ones used in jmx property editors
			if (type.equalsIgnoreCase("event")) {
				component = new EventTypeID(componentName,componentVendor,componentVersion).toString();

			} else if (type.equalsIgnoreCase("ra")) {
				component = new ResourceAdaptorID(componentName,componentVendor,componentVersion).toString();

			} else if (type.equalsIgnoreCase("ratype")) {
				component = new ResourceAdaptorTypeID(componentName,componentVendor,componentVersion)
						.toString();

			} else if (type.equalsIgnoreCase("pspec")) {
				component = new ProfileSpecificationID(componentName,componentVendor,componentVersion)
						.toString();

			} else if (type.equalsIgnoreCase("service")) {
				component = new ServiceID(componentName,componentVendor,componentVersion).toString();

			} else if (type.equalsIgnoreCase("sbb")) {
				component = new SbbID(componentName,componentVendor,componentVersion).toString();
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

	// The setter for the "type" attribute
	public void setType(String type) {
		this.type = type;
	}

	// The setter for the "level" attribute
	public void setLevel(String level) {
		this.level = level;
	}

	public void setComponentName(String s) {
		this.componentName = s;
	}
	
	public void setComponentVendor(String s) {
		this.componentVendor = s;
	}
	
	public void setComponentVersion(String s) {
		this.componentVersion = s;
	}

	private String componentName = null;
	private String componentVendor = null;
	private String componentVersion = null;
	
	private String type = null;

	private String level = null;
}