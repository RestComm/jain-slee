package org.mobicents.ant.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.slee.resource.ResourceAdaptorID;

import org.apache.tools.ant.BuildException;
import org.mobicents.ant.SubTask;
import org.mobicents.slee.container.management.jmx.SleeCommandInterface;

public class CreateRaEntityTask implements SubTask {
	// Obtain a suitable logger.
	private static Logger logger = Logger
			.getLogger(org.mobicents.ant.tasks.CreateRaEntityTask.class
					.getName());

	public void run(SleeCommandInterface slee) {

		try {
			ResourceAdaptorID ra = new ResourceAdaptorID(componentName,componentVendor,componentVersion);

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

	// The setter for the "entityName" attribute
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	// The setter for the "props" attribute
	public void setProperties(String props) {
		this.props = props;
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
	
	private String entityName = null;

	private String props = null;
}