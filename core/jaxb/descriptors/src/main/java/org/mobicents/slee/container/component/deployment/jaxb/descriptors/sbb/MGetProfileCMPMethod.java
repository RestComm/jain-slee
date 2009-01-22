/**
 * Start time:11:32:55 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;

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

	private GetProfileCmpMethod getProfileCmpMethod=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod llGetProfileCmpMethod=null;
	
	private String description=null;
	private String profileSpecAliasRef=null;
	private String profileCmpMethodName=null;
	public MGetProfileCMPMethod(GetProfileCmpMethod getProfileCmpMethod) {
		super();
		this.getProfileCmpMethod = getProfileCmpMethod;
		this.description=this.getProfileCmpMethod.getDescription()==null?null:this.getProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=this.getProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=this.getProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
	}
	public MGetProfileCMPMethod(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetProfileCmpMethod llGetProfileCmpMethod) {
		super();
		this.llGetProfileCmpMethod = llGetProfileCmpMethod;
		this.description=this.llGetProfileCmpMethod.getDescription()==null?null:this.llGetProfileCmpMethod.getDescription().getvalue();
		this.profileSpecAliasRef=this.llGetProfileCmpMethod.getProfileSpecAliasRef().getvalue();
		this.profileCmpMethodName=this.llGetProfileCmpMethod.getGetProfileCmpMethodName().getvalue();
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
	
	
	
	
}
