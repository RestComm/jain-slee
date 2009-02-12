package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

/**
 * Start time:11:33:37 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCompare {

	private org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Compare compare11;
	
	private String attributeName;
	private String op;
	private String value;
	private String parameter;
	private String collatorRef;

	public MCompare(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Compare compare11)
	{
	  this.compare11 = compare11;
	  
		this.attributeName = compare11.getAttributeName();
		this.op = compare11.getOp();
		this.value = compare11.getValue();
		this.parameter = compare11.getParameter();
		this.collatorRef = compare11.getCollatorRef();
	}
	
	public String getAttributeName() {
		return attributeName;
	}
	
	public String getOp() {
		return op;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public String getCollatorRef() {
		return collatorRef;
	}

}
