/**
 * Start time:11:33:37 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Compare;

/**
 * Start time:11:33:37 2009-01-29<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com">baranowb - Bartosz Baranowski
 *         </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MCompare {

	private Compare compare=null;
	
	private String attributeName=null;
	private String op=null;
	//optional
	private String value=null;
	private String parameter=null;
	private String collatorRef=null;
	
	
	
	public MCompare(Compare compare) {
		super();
		this.compare = compare;
		this.attributeName=this.compare.getAttributeName();
		this.op=this.compare.getOp();
		this.value=this.compare.getValue();
		this.parameter=this.compare.getParameter();
		this.collatorRef=this.compare.getCollatorRef();
		
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
