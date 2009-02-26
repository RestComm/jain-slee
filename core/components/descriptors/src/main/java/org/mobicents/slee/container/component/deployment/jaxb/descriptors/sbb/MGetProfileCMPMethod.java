/**
 * Start time:11:32:55 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

import javax.slee.profile.ProfileSpecificationID;

import org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetProfileCmpMethod;

/**
 * Start time:11:32:55 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MGetProfileCMPMethod {

	private String description=null;
	private String profileSpecAliasRef=null;
	private String profileCmpMethodName=null;
	
	/**
	 * the id of the profile specification deferenced by the alias name
	 */
	private ProfileSpecificationID profileSpecificationID;
	
	public MGetProfileCMPMethod(GetProfileCmpMethod getProfileCmpMethod) {
		super();
		this.description=getProfileCmpMethod.getDescription()==null?null:getProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=getProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=getProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
	}
	public MGetProfileCMPMethod(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod llGetProfileCmpMethod) {
		super();
		this.description=llGetProfileCmpMethod.getDescription()==null?null:llGetProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=llGetProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=llGetProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
	}
	public String getDescription() {
		return description;
	}
	public String getProfileSpecAliasRef() {
		return profileSpecAliasRef;
	}
	public String getProfileCmpMethodName() {
		return profileCmpMethodName;
	}
	
	public ProfileSpecificationID getProfileSpecificationID() {
		return profileSpecificationID;
	}
	
	public void setProfileSpecificationID(
			ProfileSpecificationID profileSpecificationID) {
		this.profileSpecificationID = profileSpecificationID;
	}
		
}
