/**
 * Start time:10:34:43 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.common;

import javax.slee.profile.ProfileSpecificationID;


/**
 * Start time:10:34:43 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MProfileSpecsReference {

	//This is common, either we have 3 identical classes or this one common taking strings as args
	
	private String description=null;
	//used in sbbs
	private String profileSpecAlias=null;
	private ProfileSpecificationID referenceKey=null;
	public MProfileSpecsReference(String description, String alias, String name, String vendor, String version) {
		super();
		this.description = description;
		this.profileSpecAlias = alias;
		this.referenceKey=new ProfileSpecificationID(name,vendor,version);
	}
	public String getDescription() {
		return description;
	}
	public String getProfileSpecAlias() {
		return profileSpecAlias;
	}
	public ProfileSpecificationID getReferenceProfileSpecificationID() {
		return referenceKey;
	}
	
	
	
}
