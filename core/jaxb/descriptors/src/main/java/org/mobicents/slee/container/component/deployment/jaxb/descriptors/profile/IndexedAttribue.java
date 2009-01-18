/**
 * Start time:23:37:29 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile;

import org.mobicents.slee.container.component.deployment.jaxb.slee.profile.ProfileIndex;

/**
 * Represents indexed attribute from slee 1.0 specs.
 * Start time:23:37:29 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class IndexedAttribue {

	//FIXME: unique is boolean, parse?
	private String name, unique;
	private ProfileIndex indexValue=null;
	public IndexedAttribue(ProfileIndex indexValue) {
		super();
		this.indexValue = indexValue;
	}
	public String getName() {
		return name;
	}
	public String getUnique() {
		return unique;
	}
	
	
	

}
