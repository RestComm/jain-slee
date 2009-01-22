/**
 * Start time:11:43:35 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.sbb;


/**
 * Start time:11:43:35 2009-01-20<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MGetChildRelationMethod {

	
	private org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod getChildRelationMethod=null;
	private org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetChildRelationMethod llGetChildRelationMethod=null;
	
	private String description=null;
	private String sbbAliasRef=null;
	private String childRelationMethodName=null;
	private byte defaultPriority=0;
	public MGetChildRelationMethod(org.mobicents.slee.container.component.deployment.jaxb.slee.sbb.GetChildRelationMethod getChildRelationMethod) {
		super();
		this.getChildRelationMethod = getChildRelationMethod;
		this.description=this.getChildRelationMethod.getDescription()==null?null:this.getChildRelationMethod.getDescription().getvalue();
		this.sbbAliasRef=this.getChildRelationMethod.getSbbAliasRef().getvalue();
		this.childRelationMethodName=this.getChildRelationMethod.getGetChildRelationMethodName().getvalue();
		String v=this.getChildRelationMethod.getDefaultPriority().getvalue();
		this.defaultPriority=Byte.parseByte(v);
	}
	public MGetChildRelationMethod(
			org.mobicents.slee.container.component.deployment.jaxb.slee11.sbb.GetChildRelationMethod llGetChildRelationMethod) {
		super();
		this.llGetChildRelationMethod = llGetChildRelationMethod;
		
		this.description=this.llGetChildRelationMethod.getDescription()==null?null:this.llGetChildRelationMethod.getDescription().getvalue();
		this.sbbAliasRef=this.llGetChildRelationMethod.getSbbAliasRef().getvalue();
		this.childRelationMethodName=this.llGetChildRelationMethod.getGetChildRelationMethodName().getvalue();
		String v=this.llGetChildRelationMethod.getDefaultPriority().getvalue();
		this.defaultPriority=Byte.parseByte(v);
	}
	public String getDescription() {
		return description;
	}
	public String getSbbAliasRef() {
		return sbbAliasRef;
	}
	public String getChildRelationMethodName() {
		return childRelationMethodName;
	}
	public byte getDefaultPriority() {
		return defaultPriority;
	}
	
	
	
	
	
}
