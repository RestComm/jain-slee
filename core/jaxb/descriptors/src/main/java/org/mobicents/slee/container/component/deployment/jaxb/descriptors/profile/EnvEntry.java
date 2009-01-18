/**
 * Start time:17:22:14 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

/**
 * Start time:17:22:14 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class EnvEntry {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry=null;
	
	private String description,entryName,entryValue,entryType;

	public EnvEntry(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.EnvEntry envEntry) {
		super();
		this.envEntry = envEntry;
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
