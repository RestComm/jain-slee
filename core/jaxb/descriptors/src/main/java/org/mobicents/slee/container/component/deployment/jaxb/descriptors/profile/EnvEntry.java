/**
 * Start time:17:22:14 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import javax.slee.management.DeploymentException;

/**
 * Start time:17:22:14 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EnvEntry {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry = null;

	private String description, entryName, entryValue, entryType;

	public EnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry)
			 {
		super();
		this.envEntry = envEntry;
		this.description = this.envEntry.getDescription() == null ? null
				: this.envEntry.getDescription().getvalue();

//		if (this.envEntry.getEnvEntryName() == null
//				|| this.envEntry.getEnvEntryName().getvalue() == null
//				|| this.envEntry.getEnvEntryName().getvalue().compareTo("") == 0) {
//			throw new DeploymentException(
//					"Env Entry can not have name of null or empty value");
//		}
//
////		if (this.envEntry.getEnvEntryValue() == null
////				|| this.envEntry.getEnvEntryValue().getvalue() == null
////				|| this.envEntry.getEnvEntryValue().getvalue().compareTo("") == 0) {
////			throw new DeploymentException(
////					"Env Entry can not have value of null or empty value");
////		}
//		if (this.envEntry.getEnvEntryType() == null
//				|| this.envEntry.getEnvEntryType().getvalue() == null
//				|| this.envEntry.getEnvEntryType().getvalue().compareTo("") == 0) {
//			throw new DeploymentException(
//					"Env Entry can not have type of null or empty value");
//		}
		this.entryName = this.envEntry.getEnvEntryName().getvalue();
		//Optional
		if(this.envEntry.getEnvEntryValue()!=null)
			this.entryValue = this.envEntry.getEnvEntryValue().getvalue();
		
		this.entryType = this.envEntry.getEnvEntryType().getvalue();
	}

	public String getDescription() {
		return description;
	}

	public String getEntryName() {
		return entryName;
	}

	public String getEntryValue() {
		return entryValue;
	}

	public String getEntryType() {
		return entryType;
	}

}
