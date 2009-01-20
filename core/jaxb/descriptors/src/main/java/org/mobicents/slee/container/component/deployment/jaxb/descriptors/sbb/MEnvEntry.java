/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import javax.slee.management.DeploymentException;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.EnvEntry;

/**
 * Start time:14:26:21 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MEnvEntry {
	
	
	
	private String description, entryName, entryValue, entryType;
	private EnvEntry envEntry;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry llEnvEntry;

	public MEnvEntry(
			EnvEntry envEntry)
			 {
		super();
		this.envEntry = envEntry;
		this.description = this.envEntry.getDescription() == null ? null
				: this.envEntry.getDescription().getvalue();

		
		this.entryName = this.envEntry.getEnvEntryName().getvalue();
		//Optional
		if(this.envEntry.getEnvEntryValue()!=null)
			this.entryValue = this.envEntry.getEnvEntryValue().getvalue();
		
		this.entryType = this.envEntry.getEnvEntryType().getvalue();
	}

	public MEnvEntry(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.EnvEntry envEntry)
			 {
		super();
		this.llEnvEntry = envEntry;
		this.description = this.envEntry.getDescription() == null ? null
				: this.envEntry.getDescription().getvalue();

		
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
